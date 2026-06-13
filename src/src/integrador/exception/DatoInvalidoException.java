/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Exception.java to edit this template
 */
package src.integrador.exception;

/**
 *
 * @author JuanMR
 */
public class DatoInvalidoException extends Exception {

    /**
     * Creates a new instance of <code>DatoInvalidoException</code> without
     * detail message.
     */
    public DatoInvalidoException() {
    }

    /**
     * Constructs an instance of <code>DatoInvalidoException</code> with the
     * specified detail message.
     *
     * @param msg the detail message.
     */
    public DatoInvalidoException(String mensaje) {
        super(mensaje);
    }
}
