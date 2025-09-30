package com.example.businessservice.service;

import com.example.businessservice.client.DataServiceClient;
import com.example.businessservice.dto.ProductoDTO;
import com.example.businessservice.dto.ProductoRequest;
import com.example.businessservice.exception.MicroserviceCommunicationException;
import com.example.businessservice.exception.ProductoNoEncontradoException;
import com.example.businessservice.exception.ValidacionNegocioException;
import feign.FeignException;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.util.List;

@Service
@Slf4j
public class ProductoBusinessService {

    private final DataServiceClient dataServiceClient;

    public ProductoBusinessService(DataServiceClient dataServiceClient) {
        this.dataServiceClient = dataServiceClient;
    }

    public List<ProductoDTO> obtenerTodosLosProductos() {
        try {
            return dataServiceClient.obtenerTodosLosProductos();
        } catch (FeignException e) {
            log.error("Error al obtener productos del microservicio de datos", e);
            throw new MicroserviceCommunicationException("Error de comunicación con el servicio de datos", e);
        }
    }

    public ProductoDTO obtenerProductoPorId(Long id) {
        try {
            return dataServiceClient.obtenerProductoPorId(id);
        } catch (FeignException.NotFound e) {
            throw new ProductoNoEncontradoException("Producto no encontrado con ID: " + id);
        } catch (FeignException e) {
            log.error("Error al obtener producto del microservicio de datos", e);
            throw new MicroserviceCommunicationException("Error de comunicación con el servicio de datos", e);
        }
    }

    public ProductoDTO crearProducto(ProductoRequest request) {
        validarProducto(request);
        try {
            return dataServiceClient.crearProducto(request);
        } catch (FeignException e) {
            log.error("Error al crear producto en el microservicio de datos", e);
            throw new MicroserviceCommunicationException("Error de comunicación con el servicio de datos", e);
        }
    }

    public ProductoDTO actualizarProducto(Long id, ProductoRequest request) {
        validarProducto(request);
        try {
            return dataServiceClient.actualizarProducto(id, request);
        } catch (FeignException.NotFound e) {
            throw new ProductoNoEncontradoException("Producto no encontrado con ID: " + id);
        } catch (FeignException e) {
            log.error("Error al actualizar producto en el microservicio de datos", e);
            throw new MicroserviceCommunicationException("Error de comunicación con el servicio de datos", e);
        }
    }

    public void eliminarProducto(Long id) {
        try {
            dataServiceClient.eliminarProducto(id);
        } catch (FeignException.NotFound e) {
            throw new ProductoNoEncontradoException("Producto no encontrado con ID: " + id);
        } catch (FeignException e) {
            log.error("Error al eliminar producto en el microservicio de datos", e);
            throw new MicroserviceCommunicationException("Error de comunicación con el servicio de datos", e);
        }
    }

    public List<ProductoDTO> obtenerProductosPorCategoria(String nombreCategoria) {
        try {
            return dataServiceClient.obtenerProductosPorCategoria(nombreCategoria);
        } catch (FeignException e) {
            log.error("Error al obtener productos por categoría del microservicio de datos", e);
            throw new MicroserviceCommunicationException("Error de comunicación con el servicio de datos", e);
        }
    }

    public List<ProductoDTO> obtenerProductosConStockBajo() {
        try {
            // Si el DataService devolviera InventarioDTO, aquí se podría mapear a ProductoDTO.
            return dataServiceClient.obtenerTodosLosProductos().stream()
                    .filter(p -> Boolean.TRUE.equals(p.getStockBajo()))
                    .toList();
        } catch (FeignException e) {
            log.error("Error al obtener stock bajo del microservicio de datos", e);
            throw new MicroserviceCommunicationException("Error de comunicación con el servicio de datos", e);
        }
    }

    public BigDecimal calcularValorTotalInventario() {
        try {
            return dataServiceClient.obtenerTodosLosProductos().stream()
                    .map(p -> p.getPrecio().multiply(BigDecimal.valueOf(p.getStock() == null ? 0 : p.getStock())))
                    .reduce(BigDecimal.ZERO, BigDecimal::add);
        } catch (FeignException e) {
            log.error("Error al calcular valor de inventario desde el microservicio de datos", e);
            throw new MicroserviceCommunicationException("Error de comunicación con el servicio de datos", e);
        }
    }

    private void validarProducto(ProductoRequest request) {
        if (request.getPrecio() == null || request.getPrecio().compareTo(BigDecimal.ZERO) <= 0) {
            throw new ValidacionNegocioException("El precio debe ser mayor a cero");
        }
        if (request.getStock() == null || request.getStock() < 0) {
            throw new ValidacionNegocioException("El stock no puede ser negativo");
        }
        if (request.getNombre() == null || request.getNombre().isBlank()) {
            throw new ValidacionNegocioException("El nombre es obligatorio");
        }
    }
}
