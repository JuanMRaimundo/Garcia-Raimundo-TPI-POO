/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package src.integrador.entities;

/**
 *
 * @author JuanMR
 */
public class DetallePedido {
    private int cantidad; 
    private Double subtotal; 
    private Producto producto; 

    // Constructor sin id ni super(), nacerá cuando lo convoque la clase Pedido
    public DetallePedido(int cantidad, Producto producto) {
        this.cantidad = cantidad;
        this.producto = producto;
        this.subtotal = calcularSubtotal(); // Se calcula automáticamente 
    }

    public int getCantidad() {
        return cantidad;
    }

    public void setCantidad(int cantidad) {
        this.cantidad = cantidad;
        this.subtotal = calcularSubtotal(); // Recálculo automático al setar cantidades
    }

    public Double getSubtotal() {
        return subtotal;
    }

    public void setSubtotal(Double subtotal) {
        this.subtotal = subtotal;
    }

    public Producto getProducto() {
        return producto;
    }

    public void setProducto(Producto producto) {
        this.producto = producto;
        this.subtotal = calcularSubtotal(); // Recalculo si cambian el producto
    }

    // Método de cálculo interno [cite: 784]
    public Double calcularSubtotal() {
        if (this.producto != null && this.producto.getPrecio() != null) {
            return this.cantidad * this.producto.getPrecio();
        }
        return 0.0;
    }
    
    //SOBREESCRIBIMOS EL toString()
    @Override
    public String toString() {
        return String.format("  - %s x %d => Subtotal: $%.2f", 
                             producto.getNombre(), getCantidad(), getSubtotal());
    }
}
