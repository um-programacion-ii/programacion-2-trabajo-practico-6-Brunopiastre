package com.example.businessservice.dto;

import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.Data;

import java.math.BigDecimal;

@Data
public class ProductoRequest {
    @NotBlank
    private String nombre;

    private String descripcion;

    @NotNull
    @Min(1)
    private BigDecimal precio;

    private String categoriaNombre;

    @NotNull
    @Min(0)
    private Integer stock;
}
