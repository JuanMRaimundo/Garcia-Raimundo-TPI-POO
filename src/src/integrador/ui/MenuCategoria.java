/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package src.integrador.ui;

import src.integrador.entities.Categoria;
import src.integrador.services.CategoriaService;
import src.integrador.exception.DatoInvalidoException;
import src.integrador.exception.EntidadNoEncontradaException;
import java.util.List;

/**
 * Submenú Categorías - Usa ConsolaUtil en lugar de Scanner propio.
 * Nota cómo el constructor es más simple sin el parámetro Scanner.
 *
 * @author Garcia Bautista
 */
public class MenuCategoria {
    
    // Solo el service que necesita, SIN scanner
    private CategoriaService categoriaService;
    
    // Referencia al utilitario de consola (compartido)
    private ConsolaUtil c; // 'c' como alias corto para escribir menos
    
    /**
     * Constructor simplificado - Solo recibe el servicio.
     * El Scanner se obtiene de ConsolaUtil.getInstance() automáticamente.
     * 
     * @param categoriaService Servicio de categorías inyectado
     */
    public MenuCategoria(CategoriaService categoriaService) {
        this.categoriaService = categoriaService;
        this.c = ConsolaUtil.getInstance(); // Obtener la instancia compartida
    }
    
    /**
     * Ejecuta el submenú de categorías.
     */
    public void mostrar() {
        int opcion = -1;
        
        do {
            System.out.println("\n┌────────────────────────────────────────────┐");
            System.out.println("│      📂 GESTIÓN DE CATEGORÍAS              │");
            System.out.println("├────────────────────────────────────────────┤");
            System.out.println("│  1. Listar categorías                      │");
            System.out.println("│  2. Crear categoría                        │");
            System.out.println("│  3. Editar categoría                       │");
            System.out.println("│  4. Eliminar categoría                     │");
            System.out.println("│  0. Volver al menú principal               │");
            System.out.println("└────────────────────────────────────────────┘");
            
            // Usamos 'c.' en vez de 'this.scanner.'
            opcion = c.leerEntero("Seleccione operación: ");
            
            try {
                switch (opcion) {
                    case 1: listar(); break;
                    case 2: crear(); break;
                    case 3: editar(); break;
                    case 4: eliminar(); break;
                    case 0: System.out.println("<<< Volviendo al menú principal..."); break;
                    default: System.out.println("❌ Opción inválida.");
                }
            } catch (Exception e) {
                System.out.println("⚠️  " + e.getMessage());
            }
            
        } while (opcion != 0);
    }
    
    // ===== OPERACIÓN 1: LISTAR =====
    private void listar() {
        System.out.println("\n--- LISTADO DE CATEGORÍAS ---");
        List<Categoria> lista = categoriaService.listarCategorias();
        
        if (lista.isEmpty()) {
            System.out.println("📭 No hay categorías cargadas en el sistema.");
        } else {
            c.imprimirLinea('─', 75);
            System.out.printf("%-5s %-25s %-40s%n", "ID", "NOMBRE", "DESCRIPCIÓN");
            c.imprimirLinea('─', 75);
            for (Categoria cat : lista) {
                System.out.printf("%-5d %-25s %-40s%n", 
                    cat.getId(), cat.getNombre(), cat.getDescripcion());
            }
            c.imprimirLinea('═', 75);
            System.out.println("Total: " + lista.size() + " categoría(s).");
        }
    }
    
    // ===== OPERACIÓN 2: CREAR =====
    private void crear() {
        System.out.println("\n--- CREAR NUEVA CATEGORÍA ---");
        
        // Usamos c.leerStringNoVacio en vez de leerStringNoVacio local
        String nombre = c.leerStringNoVacio("Ingrese nombre: ");
        String descripcion = c.leerStringNoVacio("Ingrese descripción: ");
        
        try {
            Categoria nueva = categoriaService.crearCategoria(nombre, descripcion);
            System.out.println("✅ ÉXITO: Categoría creada con ID #" + nueva.getId());
            System.out.println("   Nombre: " + nueva.getNombre());
        } catch (DatoInvalidoException e) {
            System.out.println("❌ " + e.getMessage());
        }
    }
    
    // ===== OPERACIÓN 3: EDITAR =====
    private void editar() {
        System.out.println("\n--- EDITAR CATEGORÍA ---");
        
        listar(); // Mostramos lista primero
        
        if (categoriaService.listarCategorias().isEmpty()) {
            return; // Nada que editar
        }
        
        long id = c.leerLong("\nIngrese ID de la categoría a editar: ");
        
        System.out.println("(Deje vacío y presione Enter para mantener el valor actual)");
        String nuevoNombre = c.leerStringOpcional("Nuevo nombre: ");
        String nuevaDesc = c.leerStringOpcional("Nueva descripción: ");
        
        try {
            Categoria editada = categoriaService.editarCategoria(id, nuevoNombre, nuevaDesc);
            System.out.println("✅ ÉXITO: Categoría #" + id + " actualizada.");
            System.out.println("   Datos actuales: " + editada.toString());
        } catch (EntidadNoEncontradaException | DatoInvalidoException e) {
            System.out.println("❌ " + e.getMessage());
        }
    }
    
    // ===== OPERACIÓN 4: ELIMINAR =====
    private void eliminar() {
        System.out.println("\n--- ELIMINAR CATEGORÍA (Baja Lógica) ---");
        
        listar();
        
        if (categoriaService.listarCategorias().isEmpty()) {
            return;
        }
        
        long id = c.leerLong("\nIngrese ID de la categoría a eliminar: ");
        
        // Confirmación usando c.confirmarAccion
        if (!c.confirmarAccion("¿Está seguro de eliminar esta categoría? (S/N): ")) {
            System.out.println("Operación cancelada por el usuario.");
            return;
        }
        
        try {
            categoriaService.eliminarCategoria(id);
            System.out.println("✅ ÉXISO: Categoría #" + id + " eliminada (baja lógica).");
            System.out.println("   Ya no aparecerá en listados, pero los datos se conservan.");
        } catch (EntidadNoEncontradaException | DatoInvalidoException e) {
            System.out.println("❌ " + e.getMessage());
        }
    }
}