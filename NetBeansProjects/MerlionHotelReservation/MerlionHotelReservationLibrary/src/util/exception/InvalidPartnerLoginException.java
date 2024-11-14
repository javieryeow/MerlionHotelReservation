/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Exception.java to edit this template
 */
package util.exception;

/**
 *
 * @author javieryeow
 */
public class InvalidPartnerLoginException extends Exception {

    /**
     * Creates a new instance of <code>InvalidPartnerLoginException</code>
     * without detail message.
     */
    public InvalidPartnerLoginException() {
        super();
    }

    /**
     * Constructs an instance of <code>InvalidPartnerLoginException</code> with
     * the specified detail message.
     *
     * @param msg the detail message.
     */
    public InvalidPartnerLoginException(String msg) {
        super(msg);
    }
}
