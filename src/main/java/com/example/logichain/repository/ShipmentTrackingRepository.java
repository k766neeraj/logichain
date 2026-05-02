package com.example.logichain.repository;

import com.example.logichain.model.ShipmentTracking;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface ShipmentTrackingRepository extends JpaRepository<ShipmentTracking,Integer> {

    List<ShipmentTracking> findByShipmentIdOrderByTimestampAsc(int shipmentId);

}
