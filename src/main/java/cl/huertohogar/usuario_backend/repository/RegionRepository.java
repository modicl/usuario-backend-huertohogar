package cl.huertohogar.usuario_backend.repository;

import org.springframework.data.jpa.repository.JpaRepository;

import cl.huertohogar.usuario_backend.model.Region;

public interface RegionRepository extends JpaRepository<Region, Integer> {

}
