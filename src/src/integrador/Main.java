/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package src.integrador;

import src.integrador.services.CategoriaService;
import src.integrador.services.ProductoService;
import src.integrador.services.UsuarioService;
import src.integrador.services.PedidoService;
import src.integrador.ui.MenuPrincipal;

/**
 * Clase principal - Punto de entrada.
 * Simplificado: ConsolaUtil maneja el Scanner internamente.
 *
 * @author Garcia Bautista
 */
public class Main {
    
    public static void main(String[] args) {
        
        // Mostrar encabezado
        System.out.println("╔══════════════════════════════════════════╗");
        System.out.println("║     🍕 FOOD STORE - SISTEMA DE GESTIÓN    ║");
        System.out.println("╚══════════════════════════════════════════╝");
        System.out.println("Inicializando sistema...\n");
        
        // Inicializar Servicios (orden por dependencias)
        CategoriaService categoriaService = new CategoriaService();
        ProductoService productoService = new ProductoService(categoriaService);
        UsuarioService usuarioService = new UsuarioService();
        PedidoService pedidoService = new PedidoService(usuarioService, productoService);
        
        // Crear Menú Principal (ya NO le pasamos Scanner, solo Services)
        // El menú usará ConsolaUtil.getInstance() internamente para leer datos
        MenuPrincipal menuPrincipal = new MenuPrincipal(
            categoriaService,
            productoService,
            usuarioService,
            pedidoService
        );
        
        // Ejecutar
        menuPrincipal.mostrar();
        
        // Mensaje de despedida
        System.out.println("\n¡Gracias por usar Food Store! 👋");
    }
}