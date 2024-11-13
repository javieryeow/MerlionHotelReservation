/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Exception.java to edit this template
 */
package util.exception;

/**
 *
 * @author wkgaret
 */
public class RoomTypeUnavailableException extends Exception {
     public RoomTypeUnavailableException() {
        super();
    }
    
    
    public RoomTypeUnavailableException(String message) {
        super(message);
    }
}