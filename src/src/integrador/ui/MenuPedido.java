/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package src.integrador.ui;

import src.integrador.entities.Pedido;
import src.integrador.entities.Usuario;
import src.integrador.entities.Producto;
import src.integrador.entities.DetallePedido;
import src.integrador.enums.Estado;
import src.integrador.enums.FormaPago;
import src.integrador.services.PedidoService;
import src.integrador.services.UsuarioService;
import src.integrador.services.ProductoService;
import src.integrador.exception.DatoInvalidoException;
import src.integrador.exception.EntidadNoEncontradaException;
import java.util.List;

/**
 * Submenú Pedidos - El más complejo, pero sin Scanner propio.
 * Todo delegado a ConsolaUtil.
 *
 * @author Garcia Bautista
 */
public class MenuPedido {
    
    private PedidoService pedidoService;
    private UsuarioService usuarioService;
    private ProductoService productoService;
    private ConsolaUtil c;
    
    public MenuPedido(PedidoService pedidoService, 
                      UsuarioService usuarioService, 
                      ProductoService productoService) {
        this.pedidoService = pedidoService;
        this.usuarioService = usuarioService;
        this.productoService = productoService;
        this.c = ConsolaUtil.getInstance();
    }
    
    public void mostrar() {
        int opcion = -1;
        
        do {
            System.out.println("\n┌────────────────────────────────────────────┐");
            System.out.println("│      📦 GESTIÓN DE PEDIDOS                   │");
            System.out.println("├────────────────────────────────────────────┤");
            System.out.println("│  1. Listar pedidos                          │");
            System.out.println("│  2. Crear nuevo pedido (con detalles)       │");
            System.out.println("│  3. Ver detalle de un pedido                │");
            System.out.println("│  4. Cambiar estado/forma de pago           │");
            System.out.println("│  5. Eliminar pedido                         │");
            System.out.println("│  0. Volver                                  │");
            System.out.println("└────────────────────────────────────────────┘");
            
            opcion = c.leerEntero("Seleccione: ");
            
            try {
                switch (opcion) {
                    case 1: listar(); break;
                    case 2: crearPedidoCompleto(); break;
                    case 3: verDetalle(); break;
                    case 4: actualizarEstadoOPago(); break;
                    case 5: eliminar(); break;
                    case 0: System.out.println("<<< Volviendo..."); break;
                    default: System.out.println("❌ Inválido.");
                }
            } catch (Exception e) {
                System.out.println("⚠️  " + e.getMessage());
            }
        } while (opcion != 0);
    }
    
    private void listar() {
        System.out.println("\n--- LISTADO DE PEDIDOS ---");
        List<Pedido> lista = pedidoService.listarPedidos();
        
        if (lista.isEmpty()) {
            System.out.println("📭 No hay pedidos registrados.");
        } else {
            c.imprimirLinea('─', 85);
            System.out.printf("%-6s %-12s %-20s %-12s %-10s %-8s%n", 
                "ID", "FECHA", "CLIENTE", "ESTADO", "TOTAL", "PAGO");
            c.imprimirLinea('─', 85);
            for (Pedido p : lista) {
                String cliente = (p.getUsuario() != null) ? 
                    p.getUsuario().getNombre() + " " + p.getUsuario().getApellido() : "Sin usuario";
                String pago = (p.getFormaPago() != null) ? p.getFormaPago().name() : "Sin definir";
                System.out.printf("%-6d %-12s %-20s %-12s $%-9.2f %-8s%n",
                    p.getId(), p.getFecha(), cliente, p.getEstado(), p.getTotal(), pago);
            }
            c.imprimirLinea('═', 85);
        }
    }
    
    private void crearPedidoCompleto() {
        System.out.println("\n=== CREAR NUEVO PEDIDO ===\n");
        
        // PASO 1: Seleccionar usuario
        System.out.println("--- Paso 1: Seleccionar cliente ---");
        List<Usuario> usuarios = usuarioService.listarUsuarios();
        if (usuarios.isEmpty()) {
            System.out.println("❌ No hay usuarios activos. Cree un usuario primero.");
            return;
        }
        
        for (Usuario u : usuarios) {
            System.out.println("   " + u.getId() + ". " + u.getNombre() + " " + 
                               u.getApellido() + " (" + u.getMail() + ")");
        }
        
        long usuarioId = c.leerLong("ID del cliente: ");
        
        // PASO 2: Crear pedido vacío
        Pedido pedido = null;
        try {
            pedido = pedidoService.crearPedido(usuarioId);
            System.out.println("✅ Pedido creado temporalmente. ID #" + pedido.getId());
        } catch (EntidadNoEncontradaException e) {
            System.out.println("❌ " + e.getMessage());
            return;
        }
        
        // PASO 3: Ciclo agregar detalles
        System.out.println("\n--- Paso 2: Agregar productos al pedido ---");
        boolean seguirAgregando = true;
        int numeroDetalle = 1;
        
        while (seguirAgregando) {
            System.out.println("\n   📝 Detalle #" + numeroDetalle);
            
            List<Producto> productos = productoService.listarProductos();
            if (productos.isEmpty()) {
                System.out.println("   ⚠️  No hay productos disponibles.");
                break;
            }
            
            System.out.println("   Productos disponibles:");
            for (Producto p : productos) {
                String cat = (p.getCategoria() != null) ? p.getCategoria().getNombre() : "";
                System.out.printf("      %d. %s - $%.2f (Stock: %d) [%s]%n", 
                    p.getId(), p.getNombre(), p.getPrecio(), p.getStock(), cat);
            }
            
            long prodId = c.leerLong("   ID del producto: ");
            int cant = c.leerEnteroPositivo("   Cantidad: "); // Ya valida >= 1
            
            try {
                double subtotal = pedidoService.agregarDetalleAPedido(pedido.getId(), prodId, cant);
                System.out.println("   ✅ Producto agregado. Subtotal línea: $" + 
                    String.format("%.2f", subtotal));
                numeroDetalle++;
            } catch (EntidadNoEncontradaException | DatoInvalidoException e) {
                System.out.println("   ❌ " + e.getMessage());
            }
            
            String resp = c.leerString("   ¿Agregar otro producto? (S/N): ");
            seguirAgregando = resp.equalsIgnoreCase("S") || resp.equalsIgnoreCase("SI");
        }
        
        // PASO 4: Resumen y forma de pago
        System.out.println("\n=== RESUMEN DEL PEDIDO ===");
        mostrarDetallePedidoInterno(pedido);
        
        System.out.println("\n--- Forma de pago ---");
        System.out.println("   1. TARJETA");
        System.out.println("   2. TRANSFERENCIA");
        System.out.println("   3. EFECTIVO");
        int fpOp = c.leerEntero("Seleccione (1-3): ");
        
        FormaPago fp;
        switch (fpOp) {
            case 1: fp = FormaPago.TARJETA; break;
            case 2: fp = FormaPago.TRANSFERENCIA; break;
            default: fp = FormaPago.EFECTIVO; break;
        }
        
        try {
            pedidoService.actualizarFormaDePago(pedido.getId(), fp);
            System.out.println("\n╔══════════════════════════════════════════════╗");
            System.out.println("║  ✅ PEDIDO REGISTRADO EXITOSAMENTE            ║");
            System.out.println("╠══════════════════════════════════════════════╣");
            System.out.printf("║  Pedido ID: %-35d║%n", pedido.getId());
            System.out.printf("║  Total: $%-38.2f║%n", pedido.calcularTotal());
            System.out.printf("║  Forma de pago: %-31s║%n", fp);
            System.out.println("╚══════════════════════════════════════════════╝");
        } catch (EntidadNoEncontradaException e) {
            System.out.println("❌ Error finalizando: " + e.getMessage());
        }
    }
    
    private void verDetalle() {
        System.out.println("\n--- DETALLE DE PEDIDO ---");
        listar();
        
        if (pedidoService.listarPedidos().isEmpty()) return;
        
        long id = c.leerLong("ID del pedido a consultar: ");
        
        try {
            Pedido p = pedidoService.buscarPedidoPorId(id);
            mostrarDetallePedidoInterno(p);
        } catch (EntidadNoEncontradaException e) {
            System.out.println("❌ " + e.getMessage());
        }
    }
    
    private void mostrarDetallePedidoInterno(Pedido p) {
        System.out.println("┌────────────────────────────────────────────────────┐");
        System.out.printf("│ Pedido #: %-42d│%n", p.getId());
        System.out.printf("│ Fecha: %-45s│%n", p.getFecha());
        System.out.printf("│ Cliente: %-43s│%n", 
            (p.getUsuario() != null) ? p.getUsuario().getNombre() + " " + p.getUsuario().getApellido() : "N/A");
        System.out.printf("│ Estado: %-45s│%n", p.getEstado());
        System.out.printf("│ Pago: %-47s│%n", (p.getFormaPago() != null) ? p.getFormaPago() : "Pendiente");
        System.out.println("├────────────────────────────────────────────────────┤");
        System.out.println("│ DETALLE DE PRODUCTOS:                             │");
        System.out.println("├────────────────────────────────────────────────────┤");
        
        if (p.getDetalles() == null || p.getDetalles().isEmpty()) {
            System.out.println("│ (Sin productos agregados)                          │");
        } else {
            int num = 1;
            for (DetallePedido dp : p.getDetalles()) {
                System.out.printf("│ %d. %s%n", num++, dp.toString());
            }
        }
        
        System.out.println("├────────────────────────────────────────────────────┤");
        System.out.printf("│ TOTAL FINAL: $%-39.2f│%n", p.calcularTotal());
        System.out.println("└────────────────────────────────────────────────────┘");
    }
    
    private void actualizarEstadoOPago() {
        System.out.println("\n--- ACTUALIZAR PEDIDO ---");
        listar();
        
        if (pedidoService.listarPedidos().isEmpty()) return;
        
        long id = c.leerLong("ID del pedido a actualizar: ");
        
        System.out.println("¿Qué desea actualizar?");
        System.out.println("   1. Estado");
        System.out.println("   2. Forma de pago");
        int tipo = c.leerEntero("Seleccione (1-2): ");
        
        try {
            if (tipo == 1) {
                System.out.println("Estados posibles:");
                System.out.println("   1. PENDIENTE   2. CONFIRMADO   3. TERMINADO   4. CANCELADO");
                int estOp = c.leerEntero("Nuevo estado (1-4): ");
                
                Estado nuevoEstado;
                switch (estOp) {
                    case 1: nuevoEstado = Estado.PENDIENTE; break;
                    case 2: nuevoEstado = Estado.CONFIRMADO; break;
                    case 3: nuevoEstado = Estado.TERMINADO; break;
                    case 4: nuevoEstado = Estado.CANCELADO; break;
                    default: throw new DatoInvalidoException("Estado inválido.");
                }
                
                pedidoService.actualizarEstadoPedido(id, nuevoEstado);
                System.out.println("✅ Estado actualizado a " + nuevoEstado);
                
            } else {
                System.out.println("   1. TARJETA   2. TRANSFERENCIA   3. EFECTIVO");
                int fpOp = c.leerEntero("Nueva forma de pago: ");
                FormaPago fp;
                switch (fpOp) {
                    case 1: fp = FormaPago.TARJETA; break;
                    case 2: fp = FormaPago.TRANSFERENCIA; break;
                    default: fp = FormaPago.EFECTIVO; break;
                }
                pedidoService.actualizarFormaDePago(id, fp);
                System.out.println("✅ Forma de pago actualizada a " + fp);
            }
        } catch (Exception e) {
            System.out.println("❌ " + e.getMessage());
        }
    }
    
    private void eliminar() {
        System.out.println("\n--- ELIMINAR PEDIDO ---");
        listar();
        
        if (pedidoService.listarPedidos().isEmpty()) return;
        
        long id = c.leerLong("ID a eliminar: ");
        
        if (c.confirmarAccion("¿Eliminar este pedido? (Los datos se conservan ocultos) (S/N): ")) {
            try {
                pedidoService.eliminarPedido(id);
                System.out.println("✅ Pedido #" + id + " eliminado (baja lógica).");
            } catch (EntidadNoEncontradaException e) {
                System.out.println("❌ " + e.getMessage());
            }
        }
    }
}