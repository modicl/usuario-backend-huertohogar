package cl.huertohogar.usuario_backend.service;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import cl.huertohogar.usuario_backend.exception.UsuarioNotFoundException;
import cl.huertohogar.usuario_backend.exception.UsuarioNotValidException;
import cl.huertohogar.usuario_backend.model.Usuario;
import cl.huertohogar.usuario_backend.repository.UsuarioRepository;
import jakarta.transaction.Transactional;

@Service
@Transactional
public class UsuarioService {
    @Autowired
    private UsuarioRepository usuarioRepository;

 // CREATE - Crear un nuevo usuario
    public Usuario save(Usuario usuario) {
        if (usuario == null) {
            throw new UsuarioNotValidException("El usuario no puede ser nulo");
        }
        if (usuario.getPNombre() == null || usuario.getPNombre().trim().isEmpty()) {
            throw new UsuarioNotValidException("El primer nombre del usuario es obligatorio");
        }
        if (usuario.getSNombre() == null || usuario.getSNombre().trim().isEmpty()) {
            throw new UsuarioNotValidException("El segundo nombre del usuario es obligatorio");
        }
        if (usuario.getAPaterno() == null || usuario.getAPaterno().trim().isEmpty()) {
            throw new UsuarioNotValidException("El apellido paterno del usuario es obligatorio");
        }
        if (usuario.getAMaterno() == null || usuario.getAMaterno().trim().isEmpty()) {
            throw new UsuarioNotValidException("El apellido materno del usuario es obligatorio");
        }
        if (usuario.getEmail() == null || usuario.getEmail().trim().isEmpty()) {
            throw new UsuarioNotValidException("El email del usuario es obligatorio");
        }
        if (usuario.getPassword() == null || usuario.getPassword().toString().trim().isEmpty()) {
            throw new UsuarioNotValidException("La contraseña del usuario es obligatoria");
        }
        return usuarioRepository.save(usuario);
    }

    // READ - Obtener todos los usuarios
    public List<Usuario> findAll() {
        List<Usuario> usuarios = usuarioRepository.findAll();
        if (usuarios.isEmpty()) {
            throw new UsuarioNotFoundException("No se encontraron usuarios");
        }
        return usuarios;
    }

    // READ - Obtener usuario por ID
    public Usuario findById(Integer id) {
        return usuarioRepository.findById(id)
            .orElseThrow(() -> new UsuarioNotFoundException("Usuario no encontrado con id: " + id));
    }

    // UPDATE - Actualizar un usuario existente
    public Usuario update(Integer id, Usuario usuarioActualizado) {
        Usuario usuarioExistente = findById(id);
        
        // Validaciones
        if (usuarioActualizado.getPNombre() == null || usuarioActualizado.getPNombre().trim().isEmpty()) {
            throw new UsuarioNotValidException("El primer nombre del usuario es obligatorio");
        }
        if (usuarioActualizado.getSNombre() == null || usuarioActualizado.getSNombre().trim().isEmpty()) {
            throw new UsuarioNotValidException("El segundo nombre del usuario es obligatorio");
        }
        if (usuarioActualizado.getAPaterno() == null || usuarioActualizado.getAPaterno().trim().isEmpty()) {
            throw new UsuarioNotValidException("El apellido paterno del usuario es obligatorio");
        }
        if (usuarioActualizado.getAMaterno() == null || usuarioActualizado.getAMaterno().trim().isEmpty()) {
            throw new UsuarioNotValidException("El apellido materno del usuario es obligatorio");
        }
        if (usuarioActualizado.getPassword() == null || usuarioActualizado.getPassword().toString().trim().isEmpty()) {
            throw new UsuarioNotValidException("La contraseña del usuario es obligatoria");
        }
        
        // Actualizar campos
        usuarioExistente.setPNombre(usuarioActualizado.getPNombre());
        usuarioExistente.setSNombre(usuarioActualizado.getSNombre());
        usuarioExistente.setAPaterno(usuarioActualizado.getAPaterno());
        usuarioExistente.setAMaterno(usuarioActualizado.getAMaterno());
        usuarioExistente.setEmail(usuarioActualizado.getEmail());
        usuarioExistente.setPassword(usuarioActualizado.getPassword());

        return usuarioRepository.save(usuarioExistente);
    }

    // PATCH 
    public Usuario partialUpdate(Integer id, Usuario usuarioActualizado) {
        Usuario usuarioExistente = findById(id);
        
        // Solo actualiza los campos que no son nulos
        if (usuarioActualizado.getIdUsuario() != null) {
            if (usuarioActualizado.getIdUsuario() < 0) {
                throw new UsuarioNotValidException("El ID del usuario no puede ser negativo");
            }
            usuarioExistente.setIdUsuario(usuarioActualizado.getIdUsuario());
        }
        if (usuarioActualizado.getPNombre() != null) {
            usuarioExistente.setPNombre(usuarioActualizado.getPNombre());
        }

        if (usuarioActualizado.getSNombre() != null) {
            usuarioExistente.setSNombre(usuarioActualizado.getSNombre());
        }

        if (usuarioActualizado.getAPaterno() != null) {
            usuarioExistente.setAPaterno(usuarioActualizado.getAPaterno());
        }

        if (usuarioActualizado.getAMaterno() != null) {
            usuarioExistente.setAMaterno(usuarioActualizado.getAMaterno());
        }

        if (usuarioActualizado.getEmail() != null) {
            usuarioExistente.setEmail(usuarioActualizado.getEmail());
        }

        if (usuarioActualizado.getPassword() != null) {
            usuarioExistente.setPassword(usuarioActualizado.getPassword());
        }

        return usuarioRepository.save(usuarioExistente);
    }

    // DELETE - Eliminar un usuario
    public void deleteById(Integer id) {
        Usuario usuario = findById(id);
        usuarioRepository.delete(usuario);
    }

    // CONSULTAS PERSONALIZADAS
    
    // Buscar usuario por email
    public Usuario findByEmail(String email) {
        Usuario usuario = usuarioRepository.findByEmail(email);
        if (usuario == null) {
            throw new UsuarioNotFoundException("No se encontró usuario con email: " + email);
        }
        return usuario;
    }

}
