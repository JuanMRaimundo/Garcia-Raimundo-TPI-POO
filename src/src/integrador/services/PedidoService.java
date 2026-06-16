/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package src.integrador.services;

import src.integrador.entities.Pedido;
import src.integrador.entities.Usuario;
import src.integrador.entities.Producto;
import src.integrador.entities.DetallePedido;
import src.integrador.enums.Estado;
import src.integrador.enums.FormaPago;
import src.integrador.exception.DatoInvalidoException;
import src.integrador.exception.EntidadNoEncontradaException;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

/**
 * Servicio más complejo del sistema: gestiona Pedidos y sus Detalles.
 * Coordina UsuarioService y ProductoService para validar relaciones.
 *
 * Responsabilidades:
 * - Crear pedidos (asignando usuario automáticamente)
 * - Agregar/eliminar/buscar detalles dentro de un pedido
 * - Calcular totales (via interface Calculable implementada en Pedido)
 * - Cambiar estado y forma de pago
 * - Validar stock al agregar detalles
 *
 * @author Garcia Bautista
 */
public class PedidoService {
    
    // ============================================
    // ATRIBUTOS
    // ============================================
    
    /** Colección de pedidos */
    private List<Pedido> pedidos;
    
    /** Contador autoincremental */
    private Long nextId;
    
    /** Servicio de usuarios (inyectado) - necesario para validar que exista el cliente */
    private UsuarioService usuarioService;
    
    /** Servicio de productos (inyectado) - necesario para validar productos y stock */
    private ProductoService productoService;
    
    // ============================================
    // CONSTRUCTOR (DOBLE INYECCIÓN)
    // ============================================
    
    /**
     * Constructor que recibe ambos servicios dependientes.
     * El orden de parámetros no importa, pero ambos son obligatorios.
     * 
     * @param usuarioService Servicio para gestionar usuarios
     * @param productoService Servicio para gestionar productos
     */
    public PedidoService(UsuarioService usuarioService, ProductoService productoService) {
        this.pedidos = new ArrayList<>();
        this.nextId = 1L;
        this.usuarioService = usuarioService;
        this.productoService = productoService;
    }
    
    // ============================================
    // MÉTODO CRUD: CREATE PEDIDO
    // ============================================
    
    /**
     * Crea un nuevo pedido vacío para un usuario específico.
     * El pedido se crea en estado PENDIENTE con fecha de hoy.
     * Los detalles se agregan después via agregarDetalleAPedido().
     * 
     * @param usuarioId ID del usuario que realiza el pedido
     * @return El pedido creado (vacío, sin detalles aún)
     * @throws EntidadNoEncontradaException Si el usuario no existe o está dado de baja
     */
    public Pedido crearPedido(Long usuarioId) throws EntidadNoEncontradaException {
        
        // Validamos que el usuario exista y esté activo
        Usuario usuario = this.usuarioService.buscarUsuarioPorId(usuarioId);
        
        // Creamos el pedido con valores por defecto
        Pedido nuevoPedido = new Pedido();
        nuevoPedido.setId(this.nextId);
        this.nextId++;
        
        // Seteamos datos automáticos
        nuevoPedido.setFecha(LocalDate.now());           // Fecha de hoy
        nuevoPedido.setEstado(Estado.PENDIENTE);         // Estado inicial
        nuevoPedido.setFormaPago(null);                  // Se define después
        nuevoPedido.setTotal(0.0);                       // Se calcula al agregar detalles
        nuevoPedido.setUsuario(usuario);                 // Asignamos el cliente
        
        // Agregamos a la colección
        this.pedidos.add(nuevoPedido);
        
        return nuevoPedido;
    }
    
    // ============================================
    // MÉTODO: AGREGAR DETALLE AL PEDIDO
    // ============================================
    
    /**
     * Agrega un detalle (línea de producto) a un pedido existente.
     * 
     * Validaciones críticas:
     * 1. El pedido debe existir y estar activo
     * 2. El producto debe existir, estar activo y tener stock suficiente
     * 3. La cantidad debe ser >= 1
     * 
     * Tras agregar, recalcula automáticamente el total del pedido.
     * 
     * @param pedidoId ID del pedido
     * @param productoId ID del producto a agregar
     * @param cantidad Cantidad de unidades (>= 1)
     * @return Subtotal de esta línea (cantidad * precio unitario)
     * @throws EntidadNoEncontradaException Si pedido o producto no existen
     * @throws DatoInvalidoException Si cantidad inválida o stock insuficiente
     */
    public double agregarDetalleAPedido(Long pedidoId, Long productoId, int cantidad) 
            throws EntidadNoEncontradaException, DatoInvalidoException {
        
        // Validar pedido
        Pedido pedido = buscarPedidoPorId(pedidoId);
        
        // Validar producto
        Producto producto = this.productoService.buscarProductoPorId(productoId);
        
        // Validar cantidad mínima
        if (cantidad < 1) {
            throw new DatoInvalidoException(
                "ERROR: La cantidad debe ser al menos 1. Valor ingresado: " + cantidad
            );
        }
        
        // Validar stock disponible
        if (producto.getStock() < cantidad) {
            throw new DatoInvalidoException(
                String.format("ERROR: Stock insuficiente. Solicitado: %d | Disponible: %d", 
                              cantidad, producto.getStock())
            );
        }
        
        // Todo válido → Agregamos el detalle usando el método de la entidad Pedido
        // El subtotal se calcula automáticamente dentro de addDetallePedido
        pedido.addDetallePedido(cantidad, null, producto); // subtotal se calcula solo
        
        // Opcional: Descontar stock del producto (descomentar si quieren esta funcionalidad)
        // producto.setStock(producto.getStock() - cantidad);
        
        // Retornamos el subtotal de la línea agregada (para mostrar en UI)
        return cantidad * producto.getPrecio();
    }
    
    // ============================================
    // MÉTODO CRUD: READ - Listar Pedidos
    // ============================================
    
    /**
     * Lista todos los pedidos activos.
     * Opcionalmente filtra por usuario.
     * 
     * @param usuarioId ID de usuario para filtrar (null = todos)
     * @return Lista de pedidos
     */
    public List<Pedido> listarPedidos(Long usuarioId) {
        List<Pedido> resultado = this.pedidos.stream()
                .filter(p -> !p.isEliminado())
                .collect(Collectors.toList());
        
        if (usuarioId != null) {
            resultado = resultado.stream()
                    .filter(p -> p.getUsuario() != null && p.getUsuario().getId().equals(usuarioId))
                    .collect(Collectors.toList());
        }
        
        return resultado;
    }
    
    /** Sobrecarga sin filtro */
    public List<Pedido> listarPedidos() {
        return listarPedidos(null);
    }
    
    /**
     * Busca pedido por ID.
     */
    public Pedido buscarPedidoPorId(Long id) throws EntidadNoEncontradaException {
        for (Pedido pedido : this.pedidos) {
            if (pedido.getId().equals(id)) {
                if (pedido.isEliminado()) {
                    throw new EntidadNoEncontradaException(
                        "ERROR: El pedido con ID " + id + " fue eliminado."
                    );
                }
                return pedido;
            }
        }
        throw new EntidadNoEncontradaException(
            "ERROR: No existe el pedido con ID " + id + "."
        );
    }
    
    // ============================================
    // MÉTODO: ACTUALIZAR ESTADO Y/O FORMA DE PAGO
    // ============================================
    
    /**
     * Actualiza el estado de un pedido.
     * Podrían validarse transiciones válidas (ej: no pasar de CANCELADO a CONFIRMADO),
     * pero por simplicidad permitimos cualquier cambio.
     * 
     * @param pedidoId ID del pedido
     * @param nuevoEstado Nuevo estado (PENDIENTE/CONFIRMADO/TERMINADO/CANCELADO)
     * @throws EntidadNoEncontradaException Si no existe
     */
    public void actualizarEstadoPedido(Long pedidoId, Estado nuevoEstado) 
            throws EntidadNoEncontradaException {
        Pedido pedido = buscarPedidoPorId(pedidoId);
        pedido.setEstado(nuevoEstado);
    }
    
    /**
     * Actualiza la forma de pago de un pedido.
     * 
     * @param pedidoId ID del pedido
     * @param nuevaFormaPago TARJETA/TRANSFERENCIA/EFECTIVO
     * @throws EntidadNoEncontradaException Si no existe
     */
    public void actualizarFormaDePago(Long pedidoId, FormaPago nuevaFormaPago) 
            throws EntidadNoEncontradaException {
        Pedido pedido = buscarPedidoPorId(pedidoId);
        pedido.setFormaPago(nuevaFormaPago);
    }
    
    // ============================================
    // MÉTODO CRUD: DELETE (Baja Lógica)
    // ============================================
    
    /**
     * Elimina un pedido de forma lógica.
     * IMPORTANTE: No elimina los detalles ni afecta stocks.
     * El pedido simplemente desaparece de listados futuros.
     * 
     * @param pedidoId ID a eliminar
     * @throws EntidadNoEncontradaException Si no existe
     */
    public void eliminarPedido(Long pedidoId) throws EntidadNoEncontradaException {
        Pedido pedido = buscarPedidoPorId(pedidoId);
        pedido.setEliminado(true);
    }
    
    // Validación para chequear que no hayam usuarios con pedidos relacionados antes de eliminar a dicho usuario
    public boolean tienePedidosElUsuario(Long usuarioId) {
        return this.pedidos.stream()
                .anyMatch(p -> p.getUsuario() != null && p.getUsuario().getId().equals(usuarioId));
    }
}