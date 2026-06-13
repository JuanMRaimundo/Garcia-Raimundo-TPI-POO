/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Exception.java to edit this template
 */
package src.integrador.exception;

/**
 *
 * @author JuanMR
 */
public class EmailDuplicadoException extends Exception {

    /**
     * Creates a new instance of <code>EmailDuplicadoException</code> without
     * detail message.
     */
    public EmailDuplicadoException() {
    }

    /**
     * Constructs an instance of <code>EmailDuplicadoException</code> with the
     * specified detail message.
     *
     * @param msg the detail message.
     */
    public EmailDuplicadoException(String mensaje) {
        super(mensaje);
    }
}
