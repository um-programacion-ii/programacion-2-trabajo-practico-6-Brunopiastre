package com.example.dataservice.service;

import com.example.dataservice.dto.CategoriaDTO;
import com.example.dataservice.entity.Categoria;
import com.example.dataservice.exception.ResourceNotFoundException;
import com.example.dataservice.repository.CategoriaRepository;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.stream.Collectors;

@Service
@Transactional
public class CategoriaService {

    private final CategoriaRepository categoriaRepository;

    public CategoriaService(CategoriaRepository categoriaRepository) {
        this.categoriaRepository = categoriaRepository;
    }

    public List<CategoriaDTO> obtenerTodas() {
        return categoriaRepository.findAll().stream()
                .map(this::toDTO)
                .collect(Collectors.toList());
    }

    public Categoria buscarPorId(Long id) {
        return categoriaRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Categoría no encontrada con ID: " + id));
    }

    public Categoria buscarPorNombre(String nombre) {
        return categoriaRepository.findByNombre(nombre)
                .orElseThrow(() -> new ResourceNotFoundException("Categoría no encontrada: " + nombre));
    }

    public Categoria guardar(Categoria categoria) {
        return categoriaRepository.save(categoria);
    }

    public Categoria actualizar(Long id, Categoria categoria) {
        Categoria existente = buscarPorId(id);
        existente.setNombre(categoria.getNombre());
        existente.setDescripcion(categoria.getDescripcion());
        return categoriaRepository.save(existente);
    }

    public void eliminar(Long id) {
        if (!categoriaRepository.existsById(id)) {
            throw new ResourceNotFoundException("Categoría no encontrada con ID: " + id);
        }
        categoriaRepository.deleteById(id);
    }

    private CategoriaDTO toDTO(Categoria c) {
        return new CategoriaDTO(c.getId(), c.getNombre(), c.getDescripcion());
    }
}
