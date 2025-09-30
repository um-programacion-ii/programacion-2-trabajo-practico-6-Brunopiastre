package com.example.dataservice.repository;

import com.example.dataservice.entity.Producto;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface ProductoRepository extends JpaRepository<Producto, Long> {
    List<Producto> findByCategoria_Nombre(String nombreCategoria);
}
