package cl.huertohogar.usuario_backend.repository;

import org.springframework.data.jpa.repository.JpaRepository;

import cl.huertohogar.usuario_backend.model.Password;

public interface PasswordRepository extends JpaRepository<Password, Integer> {

}
