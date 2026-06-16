/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package src.integrador.ui;

import src.integrador.entities.Producto;
import src.integrador.entities.Categoria;
import src.integrador.services.ProductoService;
import src.integrador.services.CategoriaService;
import src.integrador.exception.DatoInvalidoException;
import src.integrador.exception.EntidadNoEncontradaException;
import java.util.List;

/**
 * Submenú Productos - Sin Scanner propio.
 * Usa ConsolaUtil para toda entrada de datos.
 *
 * @author [Tu Nombre]
 */
public class MenuProducto {
    
    private ProductoService productoService;
    private CategoriaService categoriaService;
    private ConsolaUtil c; // Alias para ConsolaUtil
    
    public MenuProducto(ProductoService productoService, CategoriaService categoriaService) {
        this.productoService = productoService;
        this.categoriaService = categoriaService;
        this.c = ConsolaUtil.getInstance();
    }
    
    public void mostrar() {
        int opcion = -1;
        
        do {
            System.out.println("\n┌────────────────────────────────────────────┐");
            System.out.println("│      🛒 GESTIÓN DE PRODUCTOS                │");
            System.out.println("├────────────────────────────────────────────┤");
            System.out.println("│  1. Listar productos                       │");
            System.out.println("│  2. Crear producto                         │");
            System.out.println("│  3. Editar producto                        │");
            System.out.println("│  4. Eliminar producto                      │");
            System.out.println("│  0. Volver                                 │");
            System.out.println("└────────────────────────────────────────────┘");
            
            opcion = c.leerEntero("Seleccione: ");
            
            try {
                switch (opcion) {
                    case 1: listar(); break;
                    case 2: crear(); break;
                    case 3: editar(); break;
                    case 4: eliminar(); break;
                    case 0: System.out.println("<<< Volviendo..."); break;
                    default: System.out.println("❌ Inválido.");
                }
            } catch (Exception e) {
                System.out.println("⚠️  " + e.getMessage());
            }
        } while (opcion != 0);
    }
    
    private void listar() {
        System.out.println("\n--- LISTADO DE PRODUCTOS ---");
        List<Producto> lista = productoService.listarProductos();
        
        if (lista.isEmpty()) {
            System.out.println("📭 No hay productos cargados.");
        } else {
            c.imprimirLinea('─', 80);
            System.out.printf("%-5s %-25s %-10s %-8s %-20s%n", 
                "ID", "NOMBRE", "PRECIO", "STOCK", "CATEGORÍA");
            c.imprimirLinea('─', 80);
            for (Producto p : lista) {
                String catName = (p.getCategoria() != null) ? p.getCategoria().getNombre() : "Sin categoría";
                System.out.printf("%-5d %-25s $%-9.2f %-8d %-20s%n",
                    p.getId(), p.getNombre(), p.getPrecio(), p.getStock(), catName);
            }
            c.imprimirLinea('═', 80);
        }
    }
    
    private void crear() {
        System.out.println("\n--- CREAR NUEVO PRODUCTO ---");
        
        // Mostrar categorías disponibles
        System.out.println("Categorías disponibles:");
        List<Categoria> cats = categoriaService.listarCategorias();
        if (cats.isEmpty()) {
            System.out.println("❌ ERROR: No hay categorías creadas. Primere cree una categoría.");
            return;
        }
        for (Categoria cat : cats) {
            System.out.println("   " + cat.getId() + ". " + cat.getNombre());
        }
        
        // Leer datos usando ConsolaUtil
        long catId = c.leerLong("\nSeleccione ID de categoría: ");
        String nombre = c.leerStringNoVacio("Nombre del producto: ");
        double precio = c.leerDoublePositivo("Precio ($): "); // Ya valida >= 0
        String desc = c.leerString("Descripción: ");
        int stock = c.leerEnteroNoNegativo("Stock (cantidad disponible): "); // Ya valida >= 0
        String imagen = c.leerString("URL de imagen (opcional, Enter para omitir): ");
        
        // Disponible por defecto true
        boolean disponible = true; 
        
        try {
            Producto nuevo = productoService.crearProducto(
                nombre, precio, desc, stock, imagen, disponible, catId);
            System.out.println("✅ Producto creado exitosamente. ID #" + nuevo.getId());
        } catch (DatoInvalidoException | EntidadNoEncontradaException e) {
            System.out.println("❌ " + e.getMessage());
        }
    }
    
    private void editar() {
        System.out.println("\n--- EDITAR PRODUCTO ---");
        listar();
        
        if (productoService.listarProductos().isEmpty()) return;
        
        long id = c.leerLong("ID del producto a editar: ");
        
        System.out.println("(Deje vacío para mantener valor actual)");
        String nom = c.leerStringOpcional("Nuevo nombre: ");
        Double prec = c.leerDoubleOpcional("Nuevo precio ($): ");
        String desc = c.leerStringOpcional("Nueva descripción: ");
        Integer stck = c.leerEnteroOpcional("Nuevo stock: ");
        
        try {
            Producto editado = productoService.editarProducto(id, prec, stck, desc, null, null, null);
            if (nom != null && !nom.isEmpty()) editado.setNombre(nom);
            System.out.println("✅ Producto #" + id + " actualizado.");
        } catch (Exception e) {
            System.out.println("❌ " + e.getMessage());
        }
    }
    
    private void eliminar() {
        System.out.println("\n--- ELIMINAR PRODUCTO ---");
        listar();
        
        if (productoService.listarProductos().isEmpty()) return;
        
        long id = c.leerLong("ID a eliminar: ");
        
        if (c.confirmarAccion("¿Confirmar eliminación? (S/N): ")) {
            try {
                productoService.eliminarProducto(id);
                System.out.println("✅ Producto #" + id + " eliminado (baja lógica).");
            } catch (EntidadNoEncontradaException e) {
                System.out.println("❌ " + e.getMessage());
            }
        }
    }
}