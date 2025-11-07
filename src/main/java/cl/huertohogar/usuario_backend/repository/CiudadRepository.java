package cl.huertohogar.usuario_backend.repository;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import cl.huertohogar.usuario_backend.model.Ciudad;

public interface CiudadRepository extends JpaRepository<Ciudad, Integer> {

    @Query(value = "SELECT * FROM ciudad WHERE id_ciudad = ?1", nativeQuery = true)
    Ciudad findByIdCiudad(Integer idCiudad);

    @Query(value = "SELECT * FROM ciudad WHERE id_region = ?1", nativeQuery = true)
    List<Ciudad> findByIdRegion(Integer idRegion);
    
}
