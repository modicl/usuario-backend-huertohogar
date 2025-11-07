package cl.huertohogar.usuario_backend.repository;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import cl.huertohogar.usuario_backend.model.Orden;

public interface OrdenRepository extends JpaRepository<Orden, Integer> {

    @Query(value = "SELECT * FROM orden WHERE id_orden = ?1", nativeQuery = true)
    List<Orden> findByIdOrden(Integer idOrden);

}
