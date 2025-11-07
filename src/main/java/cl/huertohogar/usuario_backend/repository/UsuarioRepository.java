package cl.huertohogar.usuario_backend.repository;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import cl.huertohogar.usuario_backend.model.Usuario;

public interface UsuarioRepository extends JpaRepository<Usuario, Integer> {

    @Query(value = "SELECT * FROM Usuario WHERE id_usuario = ?1", nativeQuery = true)
    List<Usuario> findByIdUsuario(Integer idUsuario);

    @Query(value = "SELECT * FROM Usuario WHERE nombre = ?1", nativeQuery = true)
    Usuario findByNombre(String nombre);

    @Query(value = "SELECT * FROM Usuario WHERE email = ?1", nativeQuery = true)
    Usuario findByEmail(String email);

}
