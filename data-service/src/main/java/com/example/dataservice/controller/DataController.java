package com.example.dataservice.controller;

import com.example.dataservice.dto.CategoriaDTO;
import com.example.dataservice.dto.InventarioDTO;
import com.example.dataservice.dto.ProductoDTO;
import com.example.dataservice.dto.ProductoRequest;
import com.example.dataservice.service.CategoriaService;
import com.example.dataservice.service.InventarioService;
import com.example.dataservice.service.ProductoService;
import jakarta.validation.Valid;
import org.springframework.http.HttpStatus;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/data")
@Validated
public class DataController {

  private final ProductoService productoService;
  private final CategoriaService categoriaService;
  private final InventarioService inventarioService;

  public DataController(ProductoService productoService,
                        CategoriaService categoriaService,
                        InventarioService inventarioService) {
    this.productoService = productoService;
    this.categoriaService = categoriaService;
    this.inventarioService = inventarioService;
  }

  // --- Productos ---
  @GetMapping("/productos")
  public List<ProductoDTO> obtenerTodosLosProductos() {
    return productoService.obtenerTodos();
  }

  @GetMapping("/productos/{id}")
  public ProductoDTO obtenerProductoPorId(@PathVariable("id") Long id) {
    return productoService.buscarPorId(id);
  }

  @PostMapping("/productos")
  @ResponseStatus(HttpStatus.CREATED)
  public ProductoDTO crearProducto(@Valid @RequestBody ProductoRequest request) {
    return productoService.guardar(request);
  }

  @PutMapping("/productos/{id}")
  public ProductoDTO actualizarProducto(@PathVariable("id") Long id, @Valid @RequestBody ProductoRequest request) {
    return productoService.actualizar(id, request);
  }

  @DeleteMapping("/productos/{id}")
  @ResponseStatus(HttpStatus.NO_CONTENT)
  public void eliminarProducto(@PathVariable("id") Long id) {
    productoService.eliminar(id);
  }

  @GetMapping("/productos/categoria/{nombre}")
  public List<ProductoDTO> obtenerProductosPorCategoria(@PathVariable("nombre") String nombre) {
    return productoService.buscarPorCategoria(nombre);
  }

  // --- Categorias ---
  @GetMapping("/categorias")
  public List<CategoriaDTO> obtenerTodasLasCategorias() {
    return categoriaService.obtenerTodas();
  }

  // --- Inventario ---
  @GetMapping("/inventario/stock-bajo")
  public List<InventarioDTO> obtenerProductosConStockBajo() {
    return inventarioService.obtenerProductosConStockBajo();
  }
}
