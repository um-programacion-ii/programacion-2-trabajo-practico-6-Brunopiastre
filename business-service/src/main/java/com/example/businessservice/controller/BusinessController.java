package com.example.businessservice.controller;

import com.example.businessservice.dto.ProductoDTO;
import com.example.businessservice.dto.ProductoRequest;
import com.example.businessservice.service.CategoriaBusinessService;
import com.example.businessservice.service.ProductoBusinessService;
import jakarta.validation.Valid;
import org.springframework.http.HttpStatus;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import java.math.BigDecimal;
import java.util.List;

@RestController
@RequestMapping("/api")
@Validated
public class BusinessController {

    private final ProductoBusinessService productoBusinessService;
    private final CategoriaBusinessService categoriaBusinessService;

    public BusinessController(ProductoBusinessService productoBusinessService,
                              CategoriaBusinessService categoriaBusinessService) {
        this.productoBusinessService = productoBusinessService;
        this.categoriaBusinessService = categoriaBusinessService;
    }

    // Productos
    @GetMapping("/productos")
    public List<ProductoDTO> obtenerTodosLosProductos() {
        return productoBusinessService.obtenerTodosLosProductos();
    }

    @GetMapping("/productos/{id}")
    public ProductoDTO obtenerProductoPorId(@PathVariable("id") Long id) {
        return productoBusinessService.obtenerProductoPorId(id);
    }

    @PostMapping("/productos")
    @ResponseStatus(HttpStatus.CREATED)
    public ProductoDTO crearProducto(@Valid @RequestBody ProductoRequest request) {
        return productoBusinessService.crearProducto(request);
    }

    @PutMapping("/productos/{id}")
    public ProductoDTO actualizarProducto(@PathVariable("id") Long id, @Valid @RequestBody ProductoRequest request) {
        return productoBusinessService.actualizarProducto(id, request);
    }

    @DeleteMapping("/productos/{id}")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    public void eliminarProducto(@PathVariable("id") Long id) {
        productoBusinessService.eliminarProducto(id);
    }

    @GetMapping("/productos/categoria/{nombre}")
    public List<ProductoDTO> obtenerProductosPorCategoria(@PathVariable("nombre") String nombre) {
        return productoBusinessService.obtenerProductosPorCategoria(nombre);
    }

    // Reportes
    @GetMapping("/reportes/stock-bajo")
    public List<ProductoDTO> obtenerProductosConStockBajo() {
        return productoBusinessService.obtenerProductosConStockBajo();
    }

    @GetMapping("/reportes/valor-inventario")
    public BigDecimal obtenerValorTotalInventario() {
        return productoBusinessService.calcularValorTotalInventario();
    }
}
