/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package src.integrador.services;

import src.integrador.entities.Categoria;
import src.integrador.exception.DatoInvalidoException;
import src.integrador.exception.EntidadNoEncontradaException;
import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

/**
 * Servicio que gestiona la lógica de negocio de las Categorías.
 * Implementa el patrón Service Layer: contiene toda la validación
 * y manipulación de la colección de categorías.
 *
 * Responsabilidades:
 * - CRUD completo de Categorías (Crear, Leer, Actualizar, Eliminar)
 * - Validaciones de negocio (nombre único, campos obligatorios)
 * - Baja lógica (soft delete) mediante campo 'eliminado'
 *
 * @author Garcia Bautista
 */
public class CategoriaService {
    
    // ============================================
    // ATRIBUTOS DEL SERVICIO
    // ============================================
    
    /**
     * Colección principal que almacena todas las categorías.
     * Usa ArrayList para tamaño dinámico y acceso por índice.
     * Simula una base de datos en memoria.
     */
    private List<Categoria> categorias;
    
    /**
     * Contador autoincremental para generar IDs únicos.
     * Cada vez que se crea una categoría, se incrementa.
     */
    private Long nextId;
    
    // ============================================
    // CONSTRUCTOR
    // ============================================
    
    /**
     * Constructor del servicio.
     * Inicializa la colección vacía y el contador en 1.
     * Se llama desde Main.java durante la configuración.
     */
    public CategoriaService() {
        this.categorias = new ArrayList<>();
        this.nextId = 1L; // Empezamos en 1 (no en 0)
    }
    
    // ============================================
    // MÉTODO CRUD: CREATE (Crear)
    // ============================================
    
    /**
     * Crea una nueva categoría y la agrega a la colección.
     * 
     * Validaciones realizadas:
     * 1. nombre no puede ser nulo ni vacío
     * 2. descripcion no puede ser nula ni vacía
     * 3. No puede existir otra categoría activa con el mismo nombre
     * 
     * @param nombre Nombre de la categoría (obligatorio, único)
     * @param descripcion Descripción de la categoría (obligatoria)
     * @return La categoría creada con su ID asignado
     * @throws DatoInvalidoException Si algún dato no pasa la validación
     */
    public Categoria crearCategoria(String nombre, String descripcion) throws DatoInvalidoException {
        
        // Validación 1: Nombre obligatorio
        if (nombre == null || nombre.trim().isEmpty()) {
            throw new DatoInvalidoException("ERROR: El nombre de la categoría es obligatorio y no puede estar vacío.");
        }
        
        // Validación 2: Descripción obligatoria
        if (descripcion == null || descripcion.trim().isEmpty()) {
            throw new DatoInvalidoException("ERROR: La descripción de la categoría es obligatoria y no puede estar vacía.");
        }
        
        // Validación 3: Unicidad del nombre (solo entre categorías NO eliminadas)
        if (existeNombreCategoria(nombre.trim())) {
            throw new DatoInvalidoException("ERROR: Ya existe una categoría activa con el nombre '" + nombre.trim() + "'.");
        }
        
        // Si pasó todas las validaciones → Creamos la instancia
        Categoria nuevaCategoria = new Categoria(nombre.trim(), descripcion.trim());
        
        // Asignamos ID autoincremental
        nuevaCategoria.setId(this.nextId);
        
        // Incrementamos el contador para el próximo ID
        this.nextId++;
        
        // Agregamos a la colección
        this.categorias.add(nuevaCategoria);
        
        // Retornamos la categoría creada (útil para mostrar confirmación con ID)
        return nuevaCategoria;
    }
    
    // ============================================
    // MÉTODO CRUD: READ - Listar Todas
    // ============================================
    
    /**
     * Retorna todas las categorías que NO han sido eliminadas (baja lógica).
     * Filtra la colección completa ignorando donde eliminado == true.
     * 
     * @return Lista de categorías activas (puede estar vacía si no hay ninguna)
     */
    public List<Categoria> listarCategorias() {
        // Usamos Stream API para filtrar solo las no eliminadas
        return this.categorias.stream()
                .filter(c -> !c.isEliminado()) // Solo las activas
                .collect(Collectors.toList()); // Convertimos a List nuevamente
    }
    
    // ============================================
    // MÉTODO CRUD: READ - Buscar por ID
    // ============================================
    
    /**
     * Busca una categoría por su ID único.
     * 
     * Reglas de búsqueda:
     * - Debe existir en la colección
     * - No debe estar marcada como eliminada (baja lógica)
     * 
     * @param id Identificador de la categoría a buscar
     * @return La categoría encontrada
     * @throws EntidadNoEncontradaException Si no existe o está eliminada
     */
    public Categoria buscarCategoriaPorId(Long id) throws EntidadNoEncontradaException {
        
        // Buscamos en la colección por ID
        for (Categoria categoria : this.categorias) {
            if (categoria.getId().equals(id)) {
                
                // Verificamos que no esté eliminada (baja lógica)
                if (categoria.isEliminado()) {
                    throw new EntidadNoEncontradaException(
                        "ERROR: La categoría con ID " + id + " fue eliminada previamente (baja lógica)."
                    );
                }
                
                // Si existe y está activa → la retornamos
                return categoria;
            }
        }
        
        // Si recorrimos todo y no encontramos → excepción
        throw new EntidadNoEncontradaException(
            "ERROR: No existe ninguna categoría con ID " + id + "."
        );
    }
    
    // ============================================
    // MÉTODO CRUD: UPDATE (Editar/Actualizar)
    // ============================================
    
    /**
     * Edita/modifica los datos de una categoría existente.
     * Permite actualizar nombre y/o descripción.
     * 
     * Validaciones:
     * - La categoría debe existir y estar activa
     * - Los nuevos valores deben ser válidos (no vacíos)
     * - Si se cambia el nombre, debe seguir siendo único
     * 
     * @param id ID de la categoría a editar
     * @param nuevoNombre Nuevo nombre (si es null o vacío, se mantiene el actual)
     * @param nuevaDescripcion Nueva descripción (si es null o vacía, se mantiene la actual)
     * @return La categoría actualizada
     * @throws EntidadNoEncontradaException Si la categoría no existe
     * @throws DatoInvalidoException Si los nuevos datos son inválidos
     */
    public Categoria editarCategoria(Long id, String nuevoNombre, String nuevaDescripcion) 
            throws EntidadNoEncontradaException, DatoInvalidoException {
        
        // Paso 1: Buscamos la categoría (esto valida existencia y que no esté eliminada)
        Categoria categoria = buscarCategoriaPorId(id);
        
        // Paso 2: Actualizamos nombre SOLO si se proporcionó uno válido
        if (nuevoNombre != null && !nuevoNombre.trim().isEmpty()) {
            
            // Validamos que el nuevo nombre no coincida con OTRA categoría existente
            // (permitimos mantener el mismo nombre, pero no duplicar con otra)
            String nombreLimpio = nuevoNombre.trim();
            for (Categoria c : this.categorias) {
                if (!c.getId().equals(id) && // No es la misma categoría que estamos editando
                    !c.isEliminado() &&       // Está activa
                    c.getNombre().equalsIgnoreCase(nombreLimpio)) { // Mismo nombre (case-insensitive)
                    
                    throw new DatoInvalidoException(
                        "ERROR: Ya existe otra categoría activa con el nombre '" + nombreLimpio + "'."
                    );
                }
            }
            
            // Si pasó la validación → actualizamos
            categoria.setNombre(nombreLimpio);
        }
        
        // Paso 3: Actualizamos descripción SOLO si se proporcionó una válida
        if (nuevaDescripcion != null && !nuevaDescripcion.trim().isEmpty()) {
            categoria.setDescripcion(nuevaDescripcion.trim());
        }
        
        // Retornamos la categoría modificada
        return categoria;
    }
    
    // ============================================
    // MÉTODO CRUD: DELETE (Eliminar - Baja Lógica)
    // ============================================
    
    /**
     * Elimina una categoría de forma lógica (soft delete).
     * NO la borra físicamente de la colección, solo marca eliminado=true.
     * 
     * Reglas de negocio (CRÍTICAS):
     * - La categoría debe existir y estar activa
     * - La categoría NO debe tener productos asociados activos
     *   (esta validación requiere acceso a ProductoService, se hace externamente o aquí si se inyectó)
     * 
     * @param id ID de la categoría a eliminar
     * @throws EntidadNoEncontradaException Si no existe
     * @throws DatoInvalidoException Si tiene productos asociados (se lanza desde afuera o aquí)
     */
    public void eliminarCategoria(Long id) throws EntidadNoEncontradaException, DatoInvalidoException {
        
        // Buscamos la categoría (valida existencia y estado activo)
        Categoria categoria = buscarCategoriaPorId(id);
        
        // Marcamos como eliminada (baja lógica)
        categoria.setEliminado(true);
        
        // Nota: La validación de "tiene productos asociados" debería hacerse
        // ANTES de llamar a este método, desde el Menu o desde un servicio coordinador,
        // porque este servicio no tiene acceso directo a la lista de productos.
        // Alternativa: inyectar ProductoService en el constructor de CategoriaService.
    }
    
    // ============================================
    // MÉTODOS AUXILIARES / PRIVADOS
    // ============================================
    
    /**
     * Verifica si ya existe una categoría ACTIVA con el nombre dado.
     * Método auxiliar usado por crearCategoria() y editarCategoria().
     * 
     * @param nombre Nombre a verificar (ya debe venir trimmeado)
     * @return true si existe una categoría activa con ese nombre, false si no
     */
    private boolean existeNombreCategoria(String nombre) {
        for (Categoria categoria : this.categorias) {
            // Comparamos ignorando mayúsculas/minúsculas y verificando que no esté eliminada
            if (!categoria.isEliminado() && categoria.getNombre().equalsIgnoreCase(nombre)) {
                return true; // Encontró duplicado
            }
        }
        return false; // No encontró, el nombre está disponible
    }
    
    /**
     * Retorna la lista COMPLETA de categorías (incluyendo eliminadas).
     * Útil para validaciones internas o reportes de auditoría.
     * 
     * @return Lista de todas las categorías (activas + eliminadas)
     */
    public List<Categoria> listarTodasIncluyendoEliminadas() {
        return new ArrayList<>(this.categorias); // Retornamos copia para evitar modificación externa
    }
}