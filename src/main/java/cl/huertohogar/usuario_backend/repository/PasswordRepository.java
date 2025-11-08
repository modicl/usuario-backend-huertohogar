package cl.huertohogar.usuario_backend.repository;

import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import cl.huertohogar.usuario_backend.model.Password;

public interface PasswordRepository extends JpaRepository<Password, Integer> {

    // Buscar contraseña por ID de usuario
    @Query(value = "SELECT * FROM password WHERE id_usuario = ?1", nativeQuery = true)
    Optional<Password> findByIdUsuario(Integer idUsuario);

    // Verificar si existe una contraseña para un usuario
    @Query(value = "SELECT CASE WHEN COUNT(*) > 0 THEN true ELSE false END FROM password WHERE id_usuario = ?1", nativeQuery = true)
    boolean existsByIdUsuario(Integer idUsuario);

    // Eliminar contraseña por ID de usuario
    @Query(value = "DELETE FROM password WHERE id_usuario = ?1", nativeQuery = true)
    void deleteByIdUsuario(Integer idUsuario);

}
