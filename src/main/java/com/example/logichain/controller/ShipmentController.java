
package com.example.logichain.controller;

import com.example.logichain.dto.ShipmentDTO;
import com.example.logichain.model.Shipment;
import com.example.logichain.model.ShipmentStatus;
import com.example.logichain.service.ShipmentService;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.*;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/shipments")
public class ShipmentController {

    @Autowired
    private ShipmentService service;

    @GetMapping
    public ResponseEntity<List<ShipmentDTO>> getAllShipments(
            @RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "5") int size,
            @RequestParam(defaultValue = "id") String sortBy,
            @RequestParam(required = false) String source,
            @RequestParam(required = false) String status){

        return ResponseEntity.ok(service.getAllShipments(page,size,sortBy,source,status));
    }

    @PostMapping
    public ResponseEntity<ShipmentDTO> createShipment(@Valid @RequestBody ShipmentDTO dto){
        return ResponseEntity.status(HttpStatus.CREATED).body(service.saveShipment(dto));
    }

    @GetMapping("/{id}")
    public ResponseEntity<ShipmentDTO> getShipmentById(@PathVariable int id){
        return ResponseEntity.ok(service.getShipmentById(id));
    }

    @PutMapping("/{id}")
    public ResponseEntity<ShipmentDTO> updateShipmentById(@PathVariable int id,@Valid @RequestBody ShipmentDTO dto){
        return ResponseEntity.ok(service.updateShipmentById(id,dto));
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteShipmentById(@PathVariable int id){
        service.deleteShipmentById(id);
        return ResponseEntity.noContent().build();
    }

    @GetMapping("/search")
    public ResponseEntity<List<ShipmentDTO>> searchBySource(@RequestParam String source){

        return ResponseEntity.ok(service.searchBySource(source));
    }

    @PutMapping("/{id}/status")
    public ResponseEntity<ShipmentDTO> updateStatus(@PathVariable int id,@RequestParam String status){
        return ResponseEntity.ok(service.updateStatus(id,status));
    }

}