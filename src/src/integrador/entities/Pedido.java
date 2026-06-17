/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package src.integrador.entities;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;
import src.integrador.enums.Estado;
import src.integrador.enums.FormaPago;
import src.integrador.interfaces.Calculable;

/**
 *
 * @author JuanMR
 */
public class Pedido extends Base implements Calculable {
    private LocalDate fecha; 
    private Estado estado; 
    private Double total; 
    private FormaPago formaPago;     
    //listado de detalles relación 1:N con detallePedido
    private List<DetallePedido> detalles;
    //Usuario que realiza el pedido - Relación de Agregación
    private Usuario usuario;
    
        // Constructor vacío
    public Pedido() {
        super();
        this.total = 0.0;
        this.detalles = new ArrayList<>();
    }  
    
     
    public Pedido(LocalDate fecha, Estado estado, FormaPago formaPago, Usuario usuario) {
        super();
        this.fecha = fecha;
        this.estado = estado;
        this.formaPago = formaPago;
        this.usuario = usuario;
        
        this.total = 0.0; // Se calcula, no se setea manualmente 
        this.detalles = new ArrayList<>();
    } 
    
    //GETTERS Y SETTERS : 
    public LocalDate getFecha() { return fecha; }
    public void setFecha(LocalDate fecha) { this.fecha = fecha; }

    public Estado getEstado() { return estado; }
    public void setEstado(Estado estado) { this.estado = estado; }

    public Double getTotal() { return total; }
    public void setTotal(Double total) { this.total = total; }

    public FormaPago getFormaPago() { return formaPago; }
    public void setFormaPago(FormaPago formaPago) { this.formaPago = formaPago; }

    public List<DetallePedido> getDetalles() { return detalles; }
    public void setDetalles(List<DetallePedido> detalles) { this.detalles = detalles; }

    public Usuario getUsuario() { return usuario; }
    public void setUsuario(Usuario usuario) { this.usuario = usuario; }
    
    //SOBREESCRIBIMOS EL METODO DE LA INTERFACE CALCULABLE: 
    @Override
    public double calcularTotal() {
        double suma = 0.0;
        for (DetallePedido dp : detalles) {
            if (dp.getSubtotal() != null) {
                suma += dp.getSubtotal();
            }
        }
        this.total = suma;
        return this.total;
    }
    
    //Metodo para agregar el detallePedido al Pedido (acá retificamos la realción de composición )
    public void addDetallePedido (int cantidad, Double subtotal, Producto producto){
        DetallePedido nuevoDetalle = new DetallePedido(cantidad,producto);
        this.detalles.add(nuevoDetalle);
        this.calcularTotal(); //recalculamos el total del pedido
    }    
    
    //Metodo para buscar el detalle
    public DetallePedido findDetallePedidoByProducto(Producto producto){
        for (DetallePedido detalle : detalles) {
            // Como DetallePedido no tiene ID, comparamos por el ID del Producto que contiene
            if (detalle.getProducto() != null && detalle.getProducto().getId().equals(producto.getId())) {
                return detalle;
            }
        }
        return null;
    }
    
    //Metodo para eliminar el detalle - reutilizamos el metodo find...
    public void deleteDetallePedidoByProducto(Producto producto) {
        DetallePedido dp = findDetallePedidoByProducto(producto);
        if (dp != null) {
            this.detalles.remove(dp);
            this.calcularTotal(); // Recalcula el total del pedido al restar el producto
        }
    }
    
    //SOBREESCRIBIMOS EL METODO toString()
    @Override
    public String toString() {
        return String.format("Pedido [ID: %d | Fecha: %s | Estado: %s | Total: $%.2f]", 
                             getId(), getFecha(), getEstado(), getTotal());
    }

}
