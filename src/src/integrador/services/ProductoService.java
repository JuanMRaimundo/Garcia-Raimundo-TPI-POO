/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package src.integrador.services;

import src.integrador.entities.Categoria;
import src.integrador.entities.Producto;
import src.integrador.exception.DatoInvalidoException;
import src.integrador.exception.EntidadNoEncontradaException;
import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

/**
 * Servicio que gestiona la lógica de negocio de los Productos.
 * Depende de CategoriaService para validar que las categorías existan.
 *
 * Responsabilidades:
 * - CRUD completo de Productos
 * - Validaciones: precio >= 0, stock >= 0, categoría existente
 * - Relación N:1 con Categoría (muchos productos → una categoría)
 *
 * @author Garcia Bautista
 */
public class ProductoService {
    
    // ============================================
    // ATRIBUTOS DEL SERVICIO
    // ============================================
    
    /** Colección principal de productos (simula tabla BD en memoria) */
    private List<Producto> productos;
    
    /** Contador autoincremental para IDs */
    private Long nextId;
    
    /** 
     * Referencia al servicio de categorías (inyectada por constructor).
     * Necesaria para validar que la categoría asignada a un producto exista.
     */
    private CategoriaService categoriaService;
    
    // ============================================
    // CONSTRUCTOR (CON INYECCIÓN DE DEPENDENCIA)
    // ============================================
    
    /**
     * Constructor que recibe el servicio de categorías como dependencia.
     * Esto permite validar que las categorías existan al crear/editar productos.
     * 
     * @param categoriaService Instancia de CategoriaService ya inicializada
     */
    public ProductoService(CategoriaService categoriaService) {
        this.productos = new ArrayList<>();
        this.nextId = 1L;
        this.categoriaService = categoriaService; // Guardamos la referencia
    }
    
    // ============================================
    // MÉTODO CRUD: CREATE
    // ============================================
    
    /**
     * Crea un nuevo producto y lo asocia a una categoría.
     * 
     * Validaciones de negocio (según reglas página 6 del enunciado):
     * 1. nombre obligatorio y no vacío
     * 2. precio >= 0 (no puede ser negativo)
     * 3. stock >= 0 (no puede ser negativo)
     * 4. categoría debe existir y estar activa
     * 
     * @param nombre Nombre del producto
     * @param precio Precio unitario (>= 0)
     * @param descripcion Descripción detallada
     * @param stock Cantidad disponible en inventario (>= 0)
     * @param imagen URL o ruta de imagen (opcional, puede ser vacío)
     * @param disponible ¿Está disponible para la venta? (true/false)
     * @param categoriaId ID de la categoría a la que pertenece
     * @return El producto creado con ID asignado
     * @throws DatoInvalidoException Si algún dato no cumple validaciones
     * @throws EntidadNoEncontradaException Si la categoría no existe
     */
    public Producto crearProducto(String nombre, Double precio, String descripcion, 
                                   int stock, String imagen, Boolean disponible, 
                                   Long categoriaId) throws DatoInvalidoException, EntidadNoEncontradaException {
        
        // Validación 1: Nombre obligatorio
        if (nombre == null || nombre.trim().isEmpty()) {
            throw new DatoInvalidoException("ERROR: El nombre del producto es obligatorio.");
        }
        
        // Validación 2: Precio no negativo (REGLA DE NEGOCIO CRÍTICA)
        if (precio == null || precio < 0) {
            throw new DatoInvalidoException("ERROR: El precio no puede ser negativo. Valor ingresado: " + precio);
        }
        
        // Validación 3: Stock no negativo (REGLA DE NEGOCIO CRÍTICA)
        if (stock < 0) {
            throw new DatoInvalidoException("ERROR: El stock no puede ser negativo. Valor ingresado: " + stock);
        }
        
        // Validación 4: Categoría debe existir (buscamos vía CategoriaService)
        Categoria categoria = null;
        try {
            categoria = this.categoriaService.buscarCategoriaPorId(categoriaId);
        } catch (EntidadNoEncontradaException e) {
            throw new EntidadNoEncontradaException(
                "ERROR: No se puede crear el producto porque la categoría con ID " + categoriaId + " no existe."
            );
        }
        
        // Si pasó todas las validaciones → Creamos el producto
        Producto nuevoProducto = new Producto(
            nombre.trim(),
            precio,
            (descripcion != null) ? descripcion.trim() : "", // Descripción opcional pero si viene, limpiamos
            stock,
            (imagen != null) ? imagen.trim() : "",           // Imagen opcional
            (disponible != null) ? disponible : true,         // Default true si no especifica
            categoria                                         // Asociamos la categoría encontrada
        );
        
        // Asignamos ID autoincremental
        nuevoProducto.setId(this.nextId);
        this.nextId++;
        
        // Agregamos a la colección
        this.productos.add(nuevoProducto);
        
        return nuevoProducto;
    }
    
    // ============================================
    // MÉTODO CRUD: READ - Listar Todos
    // ============================================
    
    /**
     * Lista todos los productos activos (no eliminados).
     * Opcionalmente filtra por categoría si se proporciona un categoriaId.
     * 
     * @param categoriaId ID de categoría para filtrar (null = sin filtro, muestra todos)
     * @return Lista de productos activos (filtrados o no)
     */
    public List<Producto> listarProductos(Long categoriaId) {
        List<Producto> resultado = this.productos.stream()
                .filter(p -> !p.isEliminado()) // Solo activos
                .collect(Collectors.toList());
        
        // Si nos dieron un categoryId → filtramos más
        if (categoriaId != null) {
            resultado = resultado.stream()
                    .filter(p -> p.getCategoria() != null && p.getCategoria().getId().equals(categoriaId))
                    .collect(Collectors.toList());
        }
        
        return resultado;
    }
    
    /**
     * Sobrecarga: lista todos los productos sin filtro.
     * Equivale a llamar listarProductos(null).
     */
    public List<Producto> listarProductos() {
        return listarProductos(null);
    }
    
    // ============================================
    // MÉTODO CRUD: READ - Buscar por ID
    // ============================================
    
    /**
     * Busca un producto por su ID.
     * Valida que exista y que no esté eliminado.
     * 
     * @param id ID del producto a buscar
     * @return El producto encontrado
     * @throws EntidadNoEncontradaException Si no existe o está eliminado
     */
    public Producto buscarProductoPorId(Long id) throws EntidadNoEncontradaException {
        
        for (Producto producto : this.productos) {
            if (producto.getId().equals(id)) {
                if (producto.isEliminado()) {
                    throw new EntidadNoEncontradaException(
                        "ERROR: El producto con ID " + id + " fue eliminado previamente."
                    );
                }
                return producto;
            }
        }
        
        throw new EntidadNoEncontradaException(
            "ERROR: No existe ningún producto con ID " + id + "."
        );
    }
    
    // ============================================
    // MÉTODO CRUD: UPDATE
    // ============================================
    
    /**
     * Edita un producto existente.
     * Permite modificar: precio, stock, descripción, imagen, disponibilidad, categoría.
     * 
     * @param id ID del producto a editar
     * @param nuevoPrecio Nuevo precio (null = no cambiar)
     * @param nuevoStock Nuevo stock (null = no cambiar, usar int no Integer para indicar obligatoriedad)
     * @param nuevaDescripcion Nueva descripción (null/vacío = no cambiar)
     * @param nuevaImagen Nueva imagen (null = no cambiar)
     * @param nuevaDisponibilidad Nueva disponibilidad (null = no cambiar)
     * @param nuevaCategoriaId Nueva categoría (null = no cambiar)
     * @return Producto editado
     * @throws EntidadNoEncontradaException Si no existe
     * @throws DatoInvalidoException Si datos inválidos
     */
    public Producto editarProducto(Long id, Double nuevoPrecio, Integer nuevoStock,
                                    String nuevaDescripcion, String nuevaImagen,
                                    Boolean nuevaDisponibilidad, Long nuevaCategoriaId) 
            throws EntidadNoEncontradaException, DatoInvalidoException {
        
        // Buscamos el producto
        Producto producto = buscarProductoPorId(id);
        
        // Actualizamos precio si vino válido
        if (nuevoPrecio != null) {
            if (nuevoPrecio < 0) {
                throw new DatoInvalidoException("ERROR: El precio no puede ser negativo.");
            }
            producto.setPrecio(nuevoPrecio);
        }
        
        // Actualizamos stock si vino válido (usamos Integer para permitir null = "no cambiar")
        if (nuevoStock != null) {
            if (nuevoStock < 0) {
                throw new DatoInvalidoException("ERROR: El stock no puede ser negativo.");
            }
            producto.setStock(nuevoStock);
        }
        
        // Actualizamos descripción si vino no vacía
        if (nuevaDescripcion != null && !nuevaDescripcion.trim().isEmpty()) {
            producto.setDescripcion(nuevaDescripcion.trim());
        }
        
        // Actualizamos imagen si vino no vacía
        if (nuevaImagen != null && !nuevaImagen.trim().isEmpty()) {
            producto.setImagen(nuevaImagen.trim());
        }
        
        // Actualizamos disponibilidad si vino valor
        if (nuevaDisponibilidad != null) {
            producto.setDisponible(nuevaDisponibilidad);
        }
        
        // Actualizamos categoría si vino ID
        if (nuevaCategoriaId != null) {
            Categoria nuevaCat = this.categoriaService.buscarCategoriaPorId(nuevaCategoriaId);
            producto.setCategoria(nuevaCat);
        }
        
        return producto;
    }
    
    // ============================================
    // MÉTODO CRUD: DELETE (Baja Lógica)
    // ============================================
    
    /**
     * Elimina un producto de forma lógica (soft delete).
     * Marca eliminado=true pero NO lo remueve de la colección.
     * 
     * IMPORTANTE: Antes de llamar a este método, se debe verificar
     * que el producto NO esté referenciado en ningún DetallePedido
     * de ningún Pedido (integridad referencial).
     * Esa validación la hace quien llama (Menu o PedidoService).
     * 
     * @param id ID del producto a eliminar
     * @throws EntidadNoEncontradaException Si no existe
     */
    public void eliminarProducto(Long id) throws EntidadNoEncontradaException {
        Producto producto = buscarProductoPorId(id);
        producto.setEliminado(true);
    }
    
    // ============================================
    // MÉTODO AUXILIAR: Verificar uso en Pedidos
    // ============================================
    
    /**
     * Verifica si un producto está siendo utilizado en algún DetallePedido.
     * Este método sería llamado por PedidoService antes de permitir eliminar.
     * Como ProductoService no tiene acceso a pedidos, esta validación
     * debe hacerse desde afuera o inyectando PedidoService.
     * 
     * Alternativa: Recibir una lista de pedidos como parámetro en eliminarProducto().
     * 
     * @param productoId ID del producto a verificar
     * @param pedidos Lista de todos los pedidos (viene de PedidoService)
     * @return true si el producto está en al menos un detalle de algún pedido activo
     */
    public boolean estaProductoEnAlgunPedido(Long productoId, List<src.integrador.entities.Pedido> pedidos) {
        // Este método es un ejemplo de cómo podría implementarse
        // si se decide que ProductoService valide esto directamente
        return false; // Placeholder - implementar según diseño final
    }
}