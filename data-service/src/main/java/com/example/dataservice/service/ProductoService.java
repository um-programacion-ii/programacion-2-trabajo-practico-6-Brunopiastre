package com.example.dataservice.service;

import com.example.dataservice.dto.ProductoDTO;
import com.example.dataservice.dto.ProductoRequest;
import com.example.dataservice.entity.Categoria;
import com.example.dataservice.entity.Inventario;
import com.example.dataservice.entity.Producto;
import com.example.dataservice.exception.ResourceNotFoundException;
import com.example.dataservice.repository.CategoriaRepository;
import com.example.dataservice.repository.ProductoRepository;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.util.List;
import java.util.stream.Collectors;

@Service
@Transactional
public class ProductoService {

    private final ProductoRepository productoRepository;
    private final CategoriaRepository categoriaRepository;

    public ProductoService(ProductoRepository productoRepository, CategoriaRepository categoriaRepository) {
        this.productoRepository = productoRepository;
        this.categoriaRepository = categoriaRepository;
    }

    public List<ProductoDTO> obtenerTodos() {
        return productoRepository.findAll().stream()
                .map(this::toDTO)
                .collect(Collectors.toList());
    }

    public ProductoDTO buscarPorId(Long id) {
        Producto producto = productoRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Producto no encontrado con ID: " + id));
        return toDTO(producto);
    }

    public List<ProductoDTO> buscarPorCategoria(String nombreCategoria) {
        return productoRepository.findByCategoria_Nombre(nombreCategoria)
                .stream()
                .map(this::toDTO)
                .collect(Collectors.toList());
    }

    public ProductoDTO guardar(ProductoRequest request) {
        Producto producto = fromRequest(request);
        // Vincular categoría por nombre si viene informada
        if (request.getCategoriaNombre() != null && !request.getCategoriaNombre().isBlank()) {
            Categoria categoria = categoriaRepository.findByNombre(request.getCategoriaNombre())
                    .orElseThrow(() -> new ResourceNotFoundException("Categoría no encontrada: " + request.getCategoriaNombre()));
            producto.setCategoria(categoria);
        } else {
            producto.setCategoria(null);
        }
        // Inventario
        if (request.getStock() != null) {
            Inventario inventario = new Inventario();
            inventario.setProducto(producto);
            inventario.setCantidad(request.getStock());
            inventario.setFechaActualizacion(LocalDateTime.now());
            producto.setInventario(inventario);
        }
        Producto guardado = productoRepository.save(producto);
        return toDTO(guardado);
    }

    public ProductoDTO actualizar(Long id, ProductoRequest request) {
        Producto existente = productoRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Producto no encontrado con ID: " + id));
        existente.setNombre(request.getNombre());
        existente.setDescripcion(request.getDescripcion());
        existente.setPrecio(request.getPrecio());

        if (request.getCategoriaNombre() != null && !request.getCategoriaNombre().isBlank()) {
            Categoria categoria = categoriaRepository.findByNombre(request.getCategoriaNombre())
                    .orElseThrow(() -> new ResourceNotFoundException("Categoría no encontrada: " + request.getCategoriaNombre()));
            existente.setCategoria(categoria);
        } else {
            existente.setCategoria(null);
        }

        if (request.getStock() != null) {
            if (existente.getInventario() == null) {
                Inventario inventario = new Inventario();
                inventario.setProducto(existente);
                existente.setInventario(inventario);
            }
            existente.getInventario().setCantidad(request.getStock());
            existente.getInventario().setFechaActualizacion(LocalDateTime.now());
        }

        Producto actualizado = productoRepository.save(existente);
        return toDTO(actualizado);
    }

    public void eliminar(Long id) {
        if (!productoRepository.existsById(id)) {
            throw new ResourceNotFoundException("Producto no encontrado con ID: " + id);
        }
        productoRepository.deleteById(id);
    }

    private ProductoDTO toDTO(Producto p) {
        Integer stock = p.getInventario() != null ? p.getInventario().getCantidad() : null;
        Integer stockMinimo = p.getInventario() != null ? p.getInventario().getStockMinimo() : null;
        Boolean stockBajo = null;
        if (stock != null && stockMinimo != null) {
            stockBajo = stock <= stockMinimo;
        }
        return new ProductoDTO(
                p.getId(),
                p.getNombre(),
                p.getDescripcion(),
                p.getPrecio(),
                p.getCategoria() != null ? p.getCategoria().getNombre() : null,
                stock,
                stockBajo
        );
    }

    private Producto fromRequest(ProductoRequest r) {
        Producto p = new Producto();
        p.setNombre(r.getNombre());
        p.setDescripcion(r.getDescripcion());
        p.setPrecio(r.getPrecio());
        return p;
    }
}
