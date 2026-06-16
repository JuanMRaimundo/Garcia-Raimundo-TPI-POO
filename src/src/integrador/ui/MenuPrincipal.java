/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package src.integrador.ui;

import src.integrador.services.CategoriaService;
import src.integrador.services.ProductoService;
import src.integrador.services.UsuarioService;
import src.integrador.services.PedidoService;

/**
 * Menú principal - Ya NO contiene Scanner.
 * Usa ConsolaUtil para todas las lecturas.
 *
 * @author Garcia Bautista
 */
public class MenuPrincipal {
    
    // Solo servicios, SIN scanner
    private CategoriaService categoriaService;
    private ProductoService productoService;
    private UsuarioService usuarioService;
    private PedidoService pedidoService;
    
    // Referencia a ConsolaUtil (singleton compartido)
    private ConsolaUtil consola;
    
    /**
     * Constructor - Recibe solo los 4 servicios.
     * El Scanner se maneja vía ConsolaUtil internamente.
     */
    public MenuPrincipal(CategoriaService categoriaService,
                         ProductoService productoService,
                         UsuarioService usuarioService,
                         PedidoService pedidoService) {
        this.categoriaService = categoriaService;
        this.productoService = productoService;
        this.usuarioService = usuarioService;
        this.pedidoService = pedidoService;
        
        // Obtener la instancia única de ConsolaUtil
        this.consola = ConsolaUtil.getInstance();
    }
    
    /**
     * Ejecuta el bucle principal del menú.
     */
    public void mostrar() {
        int opcion = -1;
        
        do {
            mostrarEncabezado();
            mostrarOpciones();
            
            // Usamos consola en vez de scanner directo
            opcion = consola.leerEntero("Seleccione una opción: ");
            
            procesarOpcion(opcion);
            
        } while (opcion != 0);
    }
    
    private void mostrarEncabezado() {
        System.out.println("\n╔═══════════════════════════════════════════════════════╗");
        System.out.println("║         🍕 SISTEMA DE PEDIDOS - FOOD STORE             ║");
        System.out.println("╚═══════════════════════════════════════════════════════╝");
    }
    
    private void mostrarOpciones() {
        System.out.println("┌───────────────────────────────────────────────────────┐");
        System.out.println("│  1️⃣  Categorías                                        │");
        System.out.println("│  2️⃣  Productos                                         │");
        System.out.println("│  3️⃣  Usuarios                                          │");
        System.out.println("│  4️⃣  Pedidos                                           │");
        System.out.println("│  0️⃣  Salir del sistema                                 │");
        System.out.println("└───────────────────────────────────────────────────────┘");
    }
    
    private void procesarOpcion(int opcion) {
        System.out.println();
        
        switch (opcion) {
            case 1:
                System.out.println(">>> Accediendo a Gestión de Categorías...");
                // Pasamos solo los services que necesita este submenú
                MenuCategoria menuCat = new MenuCategoria(categoriaService);
                menuCat.mostrar();
                break;
                
            case 2:
                System.out.println(">>> Accediendo a Gestión de Productos...");
                MenuProducto menuProd = new MenuProducto(productoService, categoriaService);
                menuProd.mostrar();
                break;
                
            case 3:
                System.out.println(">>> Accediendo a Gestión de Usuarios...");
                MenuUsuario menuUsu = new MenuUsuario(usuarioService, pedidoService);
                menuUsu.mostrar();
                break;
                
            case 4:
                System.out.println(">>> Accediendo a Gestión de Pedidos...");
                MenuPedido menuPed = new MenuPedido(pedidoService, usuarioService, productoService);
                menuPed.mostrar();
                break;
                
            case 0:
                // Salir - no hace nada, el bucle termina
                break;
                
            default:
                System.out.println("❌ ERROR: Opción no válida. Ingrese un número del 0 al 4.");
                break;
        }
    }
}