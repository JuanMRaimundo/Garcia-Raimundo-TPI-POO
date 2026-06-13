/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package src.integrador.entities;

import java.time.LocalDateTime;

/**
 *
 * @author JuanMR
 */
public abstract class Base {
    private Long id;
    private boolean eliminado;
    private LocalDateTime createdAt;
    
    public Base(){
        this.eliminado=false; //por defecto falso
        this.createdAt= LocalDateTime.now();//Generamos automaticamente la fecha de creación
    }
        //DECLARAMOS GETTERS Y SETTERS
    
    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public boolean isEliminado() {
        return eliminado;
    }

    public void setEliminado(boolean eliminado) {
        this.eliminado = eliminado;
    }

    public LocalDateTime getCreatedAt() {
        return createdAt;
    }

    public void setCreatedAt(LocalDateTime createdAt) {
        this.createdAt = createdAt;
    }


}
