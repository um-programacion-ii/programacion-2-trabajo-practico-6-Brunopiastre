package com.example.dataservice.repository;

import com.example.dataservice.entity.Inventario;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface InventarioRepository extends JpaRepository<Inventario, Long> {
    List<Inventario> findByCantidadLessThanEqual(Integer cantidad);
}
