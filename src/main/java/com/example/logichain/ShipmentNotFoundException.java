
package com.example.logichain;

public class ShipmentNotFoundException extends RuntimeException {
    public ShipmentNotFoundException() {
        super("Shipment Not Found");
    }
}
