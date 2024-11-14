/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Exception.java to edit this template
 */
package util.exception;

/**
 *
 * @author javieryeow
 */
public class CustomerAlreadyExistException extends Exception {

    /**
     * Creates a new instance of <code>CustomerAlreadyExistException</code>
     * without detail message.
     */
    public CustomerAlreadyExistException() {
        super();
    }

    /**
     * Constructs an instance of <code>CustomerAlreadyExistException</code> with
     * the specified detail message.
     *
     * @param msg the detail message.
     */
    public CustomerAlreadyExistException(String msg) {
        super(msg);
    }
}
