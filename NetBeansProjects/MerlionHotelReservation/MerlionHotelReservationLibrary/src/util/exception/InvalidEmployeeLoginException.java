/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Exception.java to edit this template
 */
package util.exception;

/**
 *
 * @author javieryeow
 */
public class InvalidEmployeeLoginException extends Exception {

    /**
     * Creates a new instance of <code>InvalidEmployeeLoginException</code>
     * without detail message.
     */
    public InvalidEmployeeLoginException() {
        super();
    }

    /**
     * Constructs an instance of <code>InvalidEmployeeLoginException</code> with
     * the specified detail message.
     *
     * @param msg the detail message.
     */
    public InvalidEmployeeLoginException(String msg) {
        super(msg);
    }
}
