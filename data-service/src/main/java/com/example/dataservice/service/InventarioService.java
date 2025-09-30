package com.example.dataservice.service;

import com.example.dataservice.entity.Inventario;
import com.example.dataservice.entity.Producto;
import com.example.dataservice.exception.ResourceNotFoundException;
import com.example.dataservice.repository.InventarioRepository;
import com.example.dataservice.repository.ProductoRepository;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.util.List;
import java.util.stream.Collectors;

@Service
@Transactional
public class InventarioService {

    private final InventarioRepository inventarioRepository;
    private final ProductoRepository productoRepository;

    public InventarioService(InventarioRepository inventarioRepository, ProductoRepository productoRepository) {
        this.inventarioRepository = inventarioRepository;
        this.productoRepository = productoRepository;
    }

    public List<Inventario> obtenerTodos() {
        return inventarioRepository.findAll();
    }

    public Inventario buscarPorId(Long id) {
        return inventarioRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Inventario no encontrado con ID: " + id));
    }

    public Inventario guardar(Inventario inventario) {
        if (inventario.getProducto() != null && inventario.getProducto().getId() != null) {
            Producto producto = productoRepository.findById(inventario.getProducto().getId())
                    .orElseThrow(() -> new ResourceNotFoundException("Producto no encontrado con ID: " + inventario.getProducto().getId()));
            inventario.setProducto(producto);
        }
        inventario.setFechaActualizacion(LocalDateTime.now());
        return inventarioRepository.save(inventario);
    }

    public Inventario actualizar(Long id, Inventario inventario) {
        Inventario existente = buscarPorId(id);
        existente.setCantidad(inventario.getCantidad());
        existente.setStockMinimo(inventario.getStockMinimo());
        if (inventario.getProducto() != null && inventario.getProducto().getId() != null) {
            Producto producto = productoRepository.findById(inventario.getProducto().getId())
                    .orElseThrow(() -> new ResourceNotFoundException("Producto no encontrado con ID: " + inventario.getProducto().getId()));
            existente.setProducto(producto);
        }
        existente.setFechaActualizacion(LocalDateTime.now());
        return inventarioRepository.save(existente);
    }

    public Inventario actualizarStock(Long inventarioId, Integer nuevaCantidad) {
        Inventario existente = buscarPorId(inventarioId);
        existente.setCantidad(nuevaCantidad);
        existente.setFechaActualizacion(LocalDateTime.now());
        return inventarioRepository.save(existente);
    }

    public void eliminar(Long id) {
        if (!inventarioRepository.existsById(id)) {
            throw new ResourceNotFoundException("Inventario no encontrado con ID: " + id);
        }
        inventarioRepository.deleteById(id);
    }

    public List<Inventario> obtenerProductosConStockBajo() {
        return inventarioRepository.findAll().stream()
                .filter(inv -> inv.getStockMinimo() != null && inv.getCantidad() != null && inv.getCantidad() <= inv.getStockMinimo())
                .collect(Collectors.toList());
    }
}
