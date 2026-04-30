
package com.example.logichain.repository;

import com.example.logichain.model.Shipment;
import com.example.logichain.model.ShipmentStatus;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface ShipmentRepository extends JpaRepository<Shipment, Integer> {

    Page<Shipment> findBySourceContainingIgnoreCase(String source, Pageable pageable);

    List<Shipment> findBySourceContainingIgnoreCase(String source);

    Page<Shipment> findBySourceContainingIgnoreCaseAndStatus(String source, ShipmentStatus status, Pageable pageable);

    Page<Shipment> findByStatus(ShipmentStatus shipmentStatus, Pageable pageable);
}
