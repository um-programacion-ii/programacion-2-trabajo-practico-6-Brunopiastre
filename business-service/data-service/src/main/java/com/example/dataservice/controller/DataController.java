package com.example.dataservice.controller;

import com.example.dataservice.entity.Categoria;
import com.example.dataservice.entity.Inventario;
import com.example.dataservice.entity.Producto;
import com.example.dataservice.exception.ResourceNotFoundException;
import com.example.dataservice.repository.CategoriaRepository;
import com.example.dataservice.repository.InventarioRepository;
import com.example.dataservice.repository.ProductoRepository;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/data")
@RequiredArgsConstructor
public class DataController {

    private final ProductoRepository productoRepository;
    private final CategoriaRepository categoriaRepository;
    private final InventarioRepository inventarioRepository;

    // ----------------- PRODUCTOS -----------------

    @PostMapping("/productos")
    public Producto crearProducto(@Valid @RequestBody Producto producto) {
        return productoRepository.save(producto);
    }

    @GetMapping("/productos")
    public List<Producto> listarProductos() {
        return productoRepository.findAll();
    }

    @GetMapping("/productos/{id}")
    public Producto obtenerProducto(@PathVariable Long id) {
        return productoRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Producto no encontrado con id " + id));
    }

    @PutMapping("/productos/{id}")
    public Producto actualizarProducto(@PathVariable Long id, @Valid @RequestBody Producto productoDetalles) {
        Producto producto = productoRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Producto no encontrado con id " + id));

        producto.setNombre(productoDetalles.getNombre());
        producto.setDescripcion(productoDetalles.getDescripcion());
        producto.setPrecio(productoDetalles.getPrecio());
        return productoRepository.save(producto);
    }

    @DeleteMapping("/productos/{id}")
    public ResponseEntity<Void> eliminarProducto(@PathVariable Long id) {
        Producto producto = productoRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Producto no encontrado con id " + id));
        productoRepository.delete(producto);
        return ResponseEntity.noContent().build();
    }

    // ----------------- CATEGORIAS -----------------

    @PostMapping("/categorias")
    public Categoria crearCategoria(@Valid @RequestBody Categoria categoria) {
        return categoriaRepository.save(categoria);
    }

    @GetMapping("/categorias")
    public List<Categoria> listarCategorias() {
        return categoriaRepository.findAll();
    }

    @GetMapping("/categorias/{id}")
    public Categoria obtenerCategoria(@PathVariable Long id) {
        return categoriaRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Categoría no encontrada con id " + id));
    }

    @PutMapping("/categorias/{id}")
    public Categoria actualizarCategoria(@PathVariable Long id, @Valid @RequestBody Categoria categoriaDetalles) {
        Categoria categoria = categoriaRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Categoría no encontrada con id " + id));

        categoria.setNombre(categoriaDetalles.getNombre());
        categoria.setDescripcion(categoriaDetalles.getDescripcion());
        return categoriaRepository.save(categoria);
    }

    @DeleteMapping("/categorias/{id}")
    public ResponseEntity<Void> eliminarCategoria(@PathVariable Long id) {
        Categoria categoria = categoriaRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Categoría no encontrada con id " + id));
        categoriaRepository.delete(categoria);
        return ResponseEntity.noContent().build();
    }

    // ----------------- INVENTARIOS -----------------

    @PostMapping("/inventarios")
    public Inventario crearInventario(@Valid @RequestBody Inventario inventario) {
        return inventarioRepository.save(inventario);
    }

    @GetMapping("/inventarios")
    public List<Inventario> listarInventarios() {
        return inventarioRepository.findAll();
    }

    @GetMapping("/inventarios/{id}")
    public Inventario obtenerInventario(@PathVariable Long id) {
        return inventarioRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Inventario no encontrado con id " + id));
    }

    @PutMapping("/inventarios/{id}")
    public Inventario actualizarInventario(@PathVariable Long id, @Valid @RequestBody Inventario inventarioDetalles) {
        Inventario inventario = inventarioRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Inventario no encontrado con id " + id));

        inventario.setCantidad(inventarioDetalles.getCantidad());
        return inventarioRepository.save(inventario);
    }

    @DeleteMapping("/inventarios/{id}")
    public ResponseEntity<Void> eliminarInventario(@PathVariable Long id) {
        Inventario inventario = inventarioRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Inventario no encontrado con id " + id));
        inventarioRepository.delete(inventario);
        return ResponseEntity.noContent().build();
    }
}
