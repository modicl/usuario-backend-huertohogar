package cl.huertohogar.usuario_backend.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import cl.huertohogar.usuario_backend.model.Region;

public interface RegionRepository extends JpaRepository<Region, Integer> {

    // Buscar región por nombre
    @Query(value = "SELECT * FROM region WHERE nombre_region = ?1", nativeQuery = true)
    Region findByNombreRegion(String nombreRegion);

    // Verificar si existe una región por nombre
    @Query(value = "SELECT CASE WHEN COUNT(*) > 0 THEN true ELSE false END FROM region WHERE nombre_region = ?1", nativeQuery = true)
    boolean existsByNombreRegion(String nombreRegion);

}
