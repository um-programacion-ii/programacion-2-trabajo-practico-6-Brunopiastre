package com.example.businessservice.controller;

import com.example.businessservice.dto.ProductoDTO;
import com.example.businessservice.dto.ProductoRequest;
import com.example.businessservice.service.CategoriaBusinessService;
import com.example.businessservice.service.ProductoBusinessService;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;

import java.math.BigDecimal;
import java.util.List;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.eq;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@WebMvcTest(BusinessController.class)
class BusinessControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @MockBean
    private ProductoBusinessService productoBusinessService;

    @MockBean
    private CategoriaBusinessService categoriaBusinessService;

    @Test
    @DisplayName("GET /api/productos debe retornar 200 y lista de productos")
    void obtenerTodosLosProductos_ok() throws Exception {
        List<ProductoDTO> productos = List.of(
                new ProductoDTO(1L, "Prod 1", "Desc", new BigDecimal("10.00"), "Cat", 3, false),
                new ProductoDTO(2L, "Prod 2", "Desc", new BigDecimal("5.50"), "Cat", 2, true)
        );
        Mockito.when(productoBusinessService.obtenerTodosLosProductos()).thenReturn(productos);

        mockMvc.perform(get("/api/productos"))
                .andExpect(status().isOk())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON))
                .andExpect(jsonPath("$[0].id").value(1))
                .andExpect(jsonPath("$[0].nombre").value("Prod 1"))
                .andExpect(jsonPath("$[1].stockBajo").value(true));
    }

    @Test
    @DisplayName("POST /api/productos debe crear y retornar 201 con el producto creado")
    void crearProducto_creaYRetorna201() throws Exception {
        ProductoRequest req = new ProductoRequest();
        req.setNombre("Nuevo");
        req.setDescripcion("Desc");
        req.setPrecio(new BigDecimal("12.34"));
        req.setCategoriaNombre("Cat");
        req.setStock(7);

        ProductoDTO creado = new ProductoDTO(10L, "Nuevo", "Desc", new BigDecimal("12.34"), "Cat", 7, false);
        Mockito.when(productoBusinessService.crearProducto(any(ProductoRequest.class))).thenReturn(creado);

        String body = "{" +
                "\"nombre\":\"Nuevo\"," +
                "\"descripcion\":\"Desc\"," +
                "\"precio\":12.34," +
                "\"categoriaNombre\":\"Cat\"," +
                "\"stock\":7" +
                "}";

        mockMvc.perform(post("/api/productos")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(body))
                .andExpect(status().isCreated())
                .andExpect(header().doesNotExist("Location"))
                .andExpect(jsonPath("$.id").value(10))
                .andExpect(jsonPath("$.nombre").value("Nuevo"));
    }

    @Test
    @DisplayName("PUT /api/productos/{id} debe actualizar y retornar 200")
    void actualizarProducto_ok() throws Exception {
        ProductoDTO actualizado = new ProductoDTO(2L, "Act", "D", new BigDecimal("9.99"), "Cat", 1, false);
        Mockito.when(productoBusinessService.actualizarProducto(eq(2L), any(ProductoRequest.class))).thenReturn(actualizado);

        String body = "{" +
                "\"nombre\":\"Act\"," +
                "\"descripcion\":\"D\"," +
                "\"precio\":9.99," +
                "\"categoriaNombre\":\"Cat\"," +
                "\"stock\":1" +
                "}";

        mockMvc.perform(put("/api/productos/2")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(body))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.id").value(2))
                .andExpect(jsonPath("$.precio").value(9.99));
    }
}
