/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Exception.java to edit this template
 */
package util.exception;

/**
 *
 * @author wkgaret
 */
public class RoomTypeNotFoundException extends Exception {
     public RoomTypeNotFoundException() {
       super();
    }
    
    public RoomTypeNotFoundException(String message) {
        super(message);
    }
}
