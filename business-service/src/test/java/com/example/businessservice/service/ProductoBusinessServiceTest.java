package com.example.businessservice.service;

import com.example.businessservice.client.DataServiceClient;
import com.example.businessservice.dto.ProductoDTO;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.math.BigDecimal;
import java.util.Arrays;
import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
class ProductoBusinessServiceTest {

    @Mock
    private DataServiceClient dataServiceClient;

    @InjectMocks
    private ProductoBusinessService productoBusinessService;

    @Test
    void cuandoObtenerTodosLosProductos_entoncesRetornaLista() {
        // Arrange
        List<ProductoDTO> productosEsperados = Arrays.asList(
                new ProductoDTO(1L, "Producto 1", "Descripción 1", BigDecimal.valueOf(100), "Categoría 1", 10, false),
                new ProductoDTO(2L, "Producto 2", "Descripción 2", BigDecimal.valueOf(200), "Categoría 2", 5, true)
        );

        when(dataServiceClient.obtenerTodosLosProductos()).thenReturn(productosEsperados);

        // Act
        List<ProductoDTO> resultado = productoBusinessService.obtenerTodosLosProductos();

        // Assert
        assertEquals(productosEsperados, resultado);
    }

    @Test
    void calcularValorTotalInventario_debeSumarPrecioPorStock() {
        // Arrange
        List<ProductoDTO> productos = Arrays.asList(
                new ProductoDTO(1L, "A", null, new BigDecimal("10.50"), "Cat", 2, false), // 21.00
                new ProductoDTO(2L, "B", null, new BigDecimal("5.00"), "Cat", 3, false),   // 15.00
                new ProductoDTO(3L, "C", null, new BigDecimal("7.25"), "Cat", null, false) // null stock -> 0
        );
        when(dataServiceClient.obtenerTodosLosProductos()).thenReturn(productos);

        // Act
        BigDecimal total = productoBusinessService.calcularValorTotalInventario();

        // Assert
        assertEquals(new BigDecimal("36.00"), total);
    }
}
