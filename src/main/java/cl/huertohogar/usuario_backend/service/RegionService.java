package cl.huertohogar.usuario_backend.service;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import cl.huertohogar.usuario_backend.exception.RegionNotFoundException;
import cl.huertohogar.usuario_backend.exception.RegionNotValidException;
import cl.huertohogar.usuario_backend.model.Region;
import cl.huertohogar.usuario_backend.repository.RegionRepository;
import jakarta.transaction.Transactional;

@Service
@Transactional
public class RegionService {
    
    @Autowired
    private RegionRepository regionRepository;

    // CREATE 
    public Region save(Region region) {
        if (region == null) {
            throw new RegionNotValidException("La región no puede ser nula");
        }
        if (region.getNombreRegion() == null || region.getNombreRegion().trim().isEmpty()) {
            throw new RegionNotValidException("El nombre de la región es obligatorio");
        }
        
        // Validar que no exista una región con el mismo nombre
        if (regionRepository.existsByNombreRegion(region.getNombreRegion())) {
            throw new RegionNotValidException("Ya existe una región con el nombre: " + region.getNombreRegion());
        }
        
        return regionRepository.save(region);
    }

    // READ 
    public List<Region> findAll() {
        List<Region> regiones = regionRepository.findAll();
        if (regiones.isEmpty()) {
            throw new RegionNotFoundException("No se encontraron regiones");
        }
        return regiones;
    }

    // READ por ID
    public Region findById(Integer id) {
        return regionRepository.findById(id)
            .orElseThrow(() -> new RegionNotFoundException("Región no encontrada con id: " + id));
    }

    // UPDATE 
    public Region update(Integer id, Region regionActualizada) {
        Region regionExistente = findById(id);
        
        // Validaciones
        if (regionActualizada.getNombreRegion() == null || regionActualizada.getNombreRegion().trim().isEmpty()) {
            throw new RegionNotValidException("El nombre de la región es obligatorio");
        }
        
        // Validar que el nuevo nombre no esté en uso por otra región
        Region regionConMismoNombre = regionRepository.findByNombreRegion(regionActualizada.getNombreRegion());
        if (regionConMismoNombre != null && !regionConMismoNombre.getIdRegion().equals(id)) {
            throw new RegionNotValidException("Ya existe otra región con el nombre: " + regionActualizada.getNombreRegion());
        }
        
        // Actualizar campos
        regionExistente.setNombreRegion(regionActualizada.getNombreRegion());

        return regionRepository.save(regionExistente);
    }

    // PATCH 
    public Region partialUpdate(Integer id, Region regionActualizada) {
        Region regionExistente = findById(id);
        
        // Solo actualiza los campos que no son nulos
        if (regionActualizada.getIdRegion() != null) {
            if (regionActualizada.getIdRegion() < 0) {
                throw new RegionNotValidException("El ID de la región no puede ser negativo");
            }
            regionExistente.setIdRegion(regionActualizada.getIdRegion());
        }
        
        if (regionActualizada.getNombreRegion() != null) {
            if (regionActualizada.getNombreRegion().trim().isEmpty()) {
                throw new RegionNotValidException("El nombre de la región no puede estar vacío");
            }
            
            // Validar que el nuevo nombre no esté en uso por otra región
            Region regionConMismoNombre = regionRepository.findByNombreRegion(regionActualizada.getNombreRegion());
            if (regionConMismoNombre != null && !regionConMismoNombre.getIdRegion().equals(id)) {
                throw new RegionNotValidException("Ya existe otra región con el nombre: " + regionActualizada.getNombreRegion());
            }
            
            regionExistente.setNombreRegion(regionActualizada.getNombreRegion());
        }

        return regionRepository.save(regionExistente);
    }

    // DELETE 
    public void deleteById(Integer id) {
        Region region = findById(id);
        regionRepository.delete(region);
    }

    // CONSULTAS PERSONALIZADAS
    
    // Buscar región por nombre
    public Region findByNombreRegion(String nombreRegion) {
        if (nombreRegion == null || nombreRegion.trim().isEmpty()) {
            throw new RegionNotValidException("El nombre de la región no puede estar vacío");
        }
        
        Region region = regionRepository.findByNombreRegion(nombreRegion);
        if (region == null) {
            throw new RegionNotFoundException("No se encontró región con nombre: " + nombreRegion);
        }
        return region;
    }

    // Verificar si existe una región por nombre
    public boolean existsByNombreRegion(String nombreRegion) {
        if (nombreRegion == null || nombreRegion.trim().isEmpty()) {
            return false;
        }
        return regionRepository.existsByNombreRegion(nombreRegion);
    }

}
