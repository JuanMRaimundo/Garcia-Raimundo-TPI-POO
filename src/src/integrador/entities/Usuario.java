/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package src.integrador.entities;

import src.integrador.enums.Rol;

/**
 *
 * @author JuanMR
 */
public class Usuario extends Base{
    private String nombre;
    private String apellido;
    private String mail; 
    private String celular;
    private String contrasenia; 
    private Rol rol;
    //La realcion 1:N con pedido la modelamos en el UsuarioService
    
    //Constructor
    public Usuario(String nombre, String apellido, String mail, String celular, String contrasenia, Rol rol) {
        super();
        this.nombre = nombre;
        this.apellido = apellido;
        this.mail = mail;
        this.celular = celular;
        this.contrasenia = contrasenia;
        this.rol = rol;
    }
    
    //GETTERS Y SETTERS: 
    
    public String getNombre() {
        return nombre;
    }

    public void setNombre(String nombre) {
        this.nombre = nombre;
    }

    public String getApellido() {
        return apellido;
    }

    public void setApellido(String apellido) {
        this.apellido = apellido;
    }

    public String getMail() {
        return mail;
    }

    public void setMail(String mail) {
        this.mail = mail;
    }

    public String getCelular() {
        return celular;
    }

    public void setCelular(String celular) {
        this.celular = celular;
    }

    public String getContrasenia() {
        return contrasenia;
    }

    public void setContrasenia(String contrasenia) {
        this.contrasenia = contrasenia;
    }

    public Rol getRol() {
        return rol;
    }

    public void setRol(Rol rol) {
        this.rol = rol;
    }
    // SOBREESCRIBIMOS EL toString()
    @Override
    public String toString() {
        return String.format("Usuario [ID: %d | %s %s | Mail: %s | Rol: %s]", 
                             getId(), getNombre(), getApellido(), getMail(), getRol());
    }
}

