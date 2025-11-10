package cl.huertohogar.usuario_backend.service;

import java.util.List;
import java.util.regex.Pattern;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;

import cl.huertohogar.usuario_backend.exception.AuthenticationFailedException;
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
    
    // BCrypt encoder para cifrado de contraseñas
    private final BCryptPasswordEncoder passwordEncoder = new BCryptPasswordEncoder();
    
    // Patrón para validar contraseñas seguras
    // Mínimo 8 caracteres, al menos una mayúscula, una minúscula, un número y un carácter especial
    private static final Pattern PASSWORD_PATTERN = Pattern.compile(
        "^(?=.*[a-z])(?=.*[A-Z])(?=.*\\d)(?=.*[@$!%*?&])[A-Za-z\\d@$!%*?&]{8,}$"
    );

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
        if (usuario.getPasswordHashed() == null || usuario.getPasswordHashed().trim().isEmpty()) {
            throw new UsuarioNotValidException("La contraseña del usuario es obligatoria");
        }

        
        
        // Validar formato de contraseña segura
        if (!isValidPassword(usuario.getPasswordHashed())) {
            throw new UsuarioNotValidException(
                "La contraseña debe tener al menos 8 caracteres, una mayúscula, una minúscula, un número y un carácter especial (@$!%*?&)"
            );
        }
        
        // Hashear la contraseña antes de guardarla
        String hashedPassword = passwordEncoder.encode(usuario.getPasswordHashed());
        usuario.setPasswordHashed(hashedPassword);
        
        return usuarioRepository.save(usuario);
    }

    // READ 
    public List<Usuario> findAll() {
        List<Usuario> usuarios = usuarioRepository.findAll();
        if (usuarios.isEmpty()) {
            throw new UsuarioNotFoundException("No se encontraron usuarios");
        }
        return usuarios;
    }

    // READ por ID
    public Usuario findById(Integer id) {
        return usuarioRepository.findById(id)
            .orElseThrow(() -> new UsuarioNotFoundException("Usuario no encontrado con id: " + id));
    }

    // UPDATE 
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
        if (usuarioActualizado.getPasswordHashed() == null || usuarioActualizado.getPasswordHashed().trim().isEmpty()) {
            throw new UsuarioNotValidException("La contraseña del usuario es obligatoria");
        }
        
        // Actualizar campos
        usuarioExistente.setPNombre(usuarioActualizado.getPNombre());
        usuarioExistente.setSNombre(usuarioActualizado.getSNombre());
        usuarioExistente.setAPaterno(usuarioActualizado.getAPaterno());
        usuarioExistente.setAMaterno(usuarioActualizado.getAMaterno());
        usuarioExistente.setEmail(usuarioActualizado.getEmail());
        usuarioExistente.setPasswordHashed(usuarioActualizado.getPasswordHashed());

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

        if (usuarioActualizado.getPasswordHashed() != null) {
            usuarioExistente.setPasswordHashed(usuarioActualizado.getPasswordHashed());
        }

        return usuarioRepository.save(usuarioExistente);
    }

    // DELETE 
    public void deleteById(Integer id) {
        Usuario usuario = findById(id);
        usuarioRepository.delete(usuario);
    }

    // CONSULTAS RANDOM
    
    // Buscar usuario por email
    public Usuario findByEmail(String email) {
        Usuario usuario = usuarioRepository.findByEmail(email);
        if (usuario == null) {
            throw new UsuarioNotFoundException("No se encontró usuario con email: " + email);
        }
        return usuario;
    }

    // Buscar por Apellido Paterno
    public List<Usuario> findByAPaterno(Integer idAPaterno) {
        List<Usuario> usuarios = usuarioRepository.findByAPaterno(idAPaterno);
        if (usuarios.isEmpty()) {
            throw new UsuarioNotFoundException("No se encontraron usuarios con apellido paterno id: " + idAPaterno);
        }
        return usuarios;
    }

    // AUTENTICACIÓN - Verificar si la contraseña es correcta (para login)
    public boolean authenticate(Integer idUsuario, String plainPassword) {
        try {
            Usuario usuario = findById(idUsuario);
            return verifyPassword(plainPassword, usuario.getPasswordHashed());
        } catch (UsuarioNotFoundException e) {
            return false;
        }
    }

    // AUTENTICACIÓN - Autenticar por EMAIL y contraseña (recomendado)
    public boolean authenticateByEmail(String email, String plainPassword) {
        try {
            Usuario usuario = findByEmail(email);
            return verifyPassword(plainPassword, usuario.getPasswordHashed());
        } catch (UsuarioNotFoundException e) {
            return false;
        }
    }

    // AUTENTICACIÓN - Verificar contraseña con manejo de excepciones
    public void authenticateOrThrow(Integer idUsuario, String plainPassword) {
        if (!authenticate(idUsuario, plainPassword)) {
            throw new AuthenticationFailedException("Credenciales inválidas");
        }
    }

    // AUTENTICACIÓN - Autenticar por EMAIL con manejo de excepciones
    public Usuario authenticateByEmailOrThrow(String email, String plainPassword) {
        Usuario usuario = findByEmail(email);
        if (!verifyPassword(plainPassword, usuario.getPasswordHashed())) {
            throw new AuthenticationFailedException("Credenciales inválidas");
        }
        return usuario;
    }

    // VALIDACIÓN - Verificar si una contraseña cumple con los requisitos de seguridad
    public boolean isValidPassword(String password) {
        if (password == null || password.isEmpty()) {
            return false;
        }
        return PASSWORD_PATTERN.matcher(password).matches();
    }

    // UTILIDAD - Verificar si una contraseña en texto plano coincide con el hash
    private boolean verifyPassword(String plainPassword, String hashedPassword) {
        return passwordEncoder.matches(plainPassword, hashedPassword);
    }

    // UTILIDAD - Obtener la fortaleza de la contraseña
    public String getPasswordStrength(String password) {
        if (password == null || password.length() < 8) {
            return "DÉBIL";
        }
        
        int strength = 0;
        if (password.matches(".*[a-z].*")) strength++;
        if (password.matches(".*[A-Z].*")) strength++;
        if (password.matches(".*\\d.*")) strength++;
        if (password.matches(".*[@$!%*?&].*")) strength++;
        
        if (strength >= 4 && password.length() >= 12) {
            return "MUY FUERTE";
        } else if (strength >= 4) {
            return "FUERTE";
        } else if (strength >= 3) {
            return "MODERADA";
        } else {
            return "DÉBIL";
        }
    }

    // CAMBIAR CONTRASEÑA - Cambiar contraseña verificando la anterior
    public Usuario changePassword(Integer idUsuario, String oldPassword, String newPassword) {
        Usuario usuario = findById(idUsuario);
        
        // Validar contraseña anterior
        if (!verifyPassword(oldPassword, usuario.getPasswordHashed())) {
            throw new AuthenticationFailedException("La contraseña anterior no es correcta");
        }
        
        // Validar nueva contraseña
        if (newPassword == null || newPassword.trim().isEmpty()) {
            throw new UsuarioNotValidException("La nueva contraseña es obligatoria");
        }
        
        if (!isValidPassword(newPassword)) {
            throw new UsuarioNotValidException(
                "La contraseña debe tener al menos 8 caracteres, una mayúscula, una minúscula, un número y un carácter especial (@$!%*?&)"
            );
        }
        
        // No permitir que la nueva contraseña sea igual a la anterior
        if (verifyPassword(newPassword, usuario.getPasswordHashed())) {
            throw new UsuarioNotValidException("La nueva contraseña no puede ser igual a la anterior");
        }
        
        // Cifrar y actualizar
        String hashedPassword = passwordEncoder.encode(newPassword);
        usuario.setPasswordHashed(hashedPassword);
        
        return usuarioRepository.save(usuario);
    }

    // RESET CONTRASEÑA - Resetear contraseña sin validar la anterior (solo para admin)
    public Usuario resetPassword(Integer idUsuario, String newPassword) {
        Usuario usuario = findById(idUsuario);
        
        if (newPassword == null || newPassword.trim().isEmpty()) {
            throw new UsuarioNotValidException("La nueva contraseña es obligatoria");
        }
        
        if (!isValidPassword(newPassword)) {
            throw new UsuarioNotValidException(
                "La contraseña debe tener al menos 8 caracteres, una mayúscula, una minúscula, un número y un carácter especial (@$!%*?&)"
            );
        }
        
        // Cifrar y actualizar
        String hashedPassword = passwordEncoder.encode(newPassword);
        usuario.setPasswordHashed(hashedPassword);
        
        return usuarioRepository.save(usuario);
    }
}
