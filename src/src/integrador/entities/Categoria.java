/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package src.integrador.entities;

/**
 *
 * @author JuanMR
 */
public class Categoria extends Base {
    private String nombre;
    private String descripcion;
    //la lista de productos la inicializamos en el servicio (CategoriaService)
  

    public Categoria(String nombre, String descripcion) {
        super();
        this.nombre = nombre;
        this.descripcion = descripcion;
    }
        //DEFINIMOS GETTERS Y SETTERS
    public String getNombre() {
        return nombre;
    }

    public void setNombre(String nombre) {
        this.nombre = nombre;
    }

    public String getDescripcion() {
        return descripcion;
    }

    public void setDescripcion(String descripcion) {
        this.descripcion = descripcion;
    }
       //SOBREESCRIBIMOS EL METODO toString()
    @Override
    public String toString() {
        return String.format("Categoria [ID: %d | Nombre: %s | Desc: %s]", 
                             getId(), getNombre(), getDescripcion());
    }
}
