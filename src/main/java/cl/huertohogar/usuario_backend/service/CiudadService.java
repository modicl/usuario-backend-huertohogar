package cl.huertohogar.usuario_backend.service;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import cl.huertohogar.usuario_backend.exception.CiudadNotFoundException;
import cl.huertohogar.usuario_backend.exception.CiudadNotValidException;
import cl.huertohogar.usuario_backend.model.Ciudad;
import cl.huertohogar.usuario_backend.repository.CiudadRepository;
import jakarta.transaction.Transactional;

@Service
@Transactional
public class CiudadService {
    
    @Autowired
    private CiudadRepository ciudadRepository;

    // CREATE 
    public Ciudad save(Ciudad ciudad) {
        if (ciudad == null) {
            throw new CiudadNotValidException("La ciudad no puede ser nula");
        }
        if (ciudad.getNombreCiudad() == null || ciudad.getNombreCiudad().trim().isEmpty()) {
            throw new CiudadNotValidException("El nombre de la ciudad es obligatorio");
        }
        if (ciudad.getRegion() == null) {
            throw new CiudadNotValidException("La región de la ciudad es obligatoria");
        }
        return ciudadRepository.save(ciudad);
    }

    // READ 
    public List<Ciudad> findAll() {
        List<Ciudad> ciudades = ciudadRepository.findAll();
        if (ciudades.isEmpty()) {
            throw new CiudadNotFoundException("No se encontraron ciudades");
        }
        return ciudades;
    }

    // READ por ID
    public Ciudad findById(Integer id) {
        return ciudadRepository.findById(id)
            .orElseThrow(() -> new CiudadNotFoundException("Ciudad no encontrada con id: " + id));
    }

    // UPDATE 
    public Ciudad update(Integer id, Ciudad ciudadActualizada) {
        Ciudad ciudadExistente = findById(id);
        
        // Validaciones
        if (ciudadActualizada.getNombreCiudad() == null || ciudadActualizada.getNombreCiudad().trim().isEmpty()) {
            throw new CiudadNotValidException("El nombre de la ciudad es obligatorio");
        }
        if (ciudadActualizada.getRegion() == null) {
            throw new CiudadNotValidException("La región de la ciudad es obligatoria");
        }
        
        // Actualizar campos
        ciudadExistente.setNombreCiudad(ciudadActualizada.getNombreCiudad());
        ciudadExistente.setRegion(ciudadActualizada.getRegion());

        return ciudadRepository.save(ciudadExistente);
    }

    // PATCH 
    public Ciudad partialUpdate(Integer id, Ciudad ciudadActualizada) {
        Ciudad ciudadExistente = findById(id);
        
        // Solo actualiza los campos que no son nulos
        if (ciudadActualizada.getIdCiudad() != null) {
            if (ciudadActualizada.getIdCiudad() < 0) {
                throw new CiudadNotValidException("El ID de la ciudad no puede ser negativo");
            }
            ciudadExistente.setIdCiudad(ciudadActualizada.getIdCiudad());
        }
        
        if (ciudadActualizada.getNombreCiudad() != null) {
            if (ciudadActualizada.getNombreCiudad().trim().isEmpty()) {
                throw new CiudadNotValidException("El nombre de la ciudad no puede estar vacío");
            }
            ciudadExistente.setNombreCiudad(ciudadActualizada.getNombreCiudad());
        }

        if (ciudadActualizada.getRegion() != null) {
            ciudadExistente.setRegion(ciudadActualizada.getRegion());
        }

        return ciudadRepository.save(ciudadExistente);
    }

    // DELETE 
    public void deleteById(Integer id) {
        Ciudad ciudad = findById(id);
        ciudadRepository.delete(ciudad);
    }

    // CONSULTAS PERSONALIZADAS
    
    // Buscar ciudades por región (excepción si no hay ciudades en región)
    public List<Ciudad> findByIdRegion(Integer idRegion) {
        List<Ciudad> ciudades = ciudadRepository.findByIdRegion(idRegion);
        if (ciudades.isEmpty()) {
            throw new CiudadNotFoundException("No se encontraron ciudades para la región con id: " + idRegion);
        }
        return ciudades;
    }

    // Buscar ciudad por ID usando query personalizada
    public Ciudad findByIdCiudad(Integer idCiudad) {
        Ciudad ciudad = ciudadRepository.findByIdCiudad(idCiudad);
        if (ciudad == null) {
            throw new CiudadNotFoundException("No se encontró ciudad con id: " + idCiudad);
        }
        return ciudad;
    }

}