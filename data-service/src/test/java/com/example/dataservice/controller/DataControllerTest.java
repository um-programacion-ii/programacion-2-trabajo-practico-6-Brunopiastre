package com.example.dataservice.controller;

import com.example.dataservice.dto.ProductoDTO;
import com.example.dataservice.service.CategoriaService;
import com.example.dataservice.service.InventarioService;
import com.example.dataservice.service.ProductoService;
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

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@WebMvcTest(DataController.class)
class DataControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @MockBean
    private ProductoService productoService;

    @MockBean
    private CategoriaService categoriaService;

    @MockBean
    private InventarioService inventarioService;

    @Test
    @DisplayName("GET /data/productos debe devolver 200 y lista de productos")
    void obtenerTodosLosProductos_ok() throws Exception {
        ProductoDTO p1 = new ProductoDTO(1L, "Prod 1", "Desc", new BigDecimal("10.00"), null, null, null);
        Mockito.when(productoService.obtenerTodos()).thenReturn(List.of(p1));

        mockMvc.perform(get("/data/productos"))
                .andExpect(status().isOk())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON))
                .andExpect(jsonPath("$[0].id").value(1))
                .andExpect(jsonPath("$[0].nombre").value("Prod 1"));
    }
}
