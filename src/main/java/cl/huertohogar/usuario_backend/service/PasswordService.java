package cl.huertohogar.usuario_backend.service;

import java.util.List;
import java.util.regex.Pattern;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;

import cl.huertohogar.usuario_backend.exception.AuthenticationFailedException;
import cl.huertohogar.usuario_backend.exception.PasswordNotFoundException;
import cl.huertohogar.usuario_backend.exception.PasswordNotValidException;
import cl.huertohogar.usuario_backend.model.Password;
import cl.huertohogar.usuario_backend.repository.PasswordRepository;
import jakarta.transaction.Transactional;

@Service
@Transactional
public class PasswordService {
    
    @Autowired
    private PasswordRepository passwordRepository;
    
    // BCrypt encoder para cifrado de contraseñas
    private final BCryptPasswordEncoder passwordEncoder = new BCryptPasswordEncoder();
    
    // Patrón para validar contraseñas seguras
    // Mínimo 8 caracteres, al menos una mayúscula, una minúscula, un número y un carácter especial
    private static final Pattern PASSWORD_PATTERN = Pattern.compile(
        "^(?=.*[a-z])(?=.*[A-Z])(?=.*\\d)(?=.*[@$!%*?&])[A-Za-z\\d@$!%*?&]{8,}$"
    );

    // CREATE contraseña(cifrada)
    public Password save(Password password) {
        if (password == null) {
            throw new PasswordNotValidException("La contraseña no puede ser nula");
        }
        if (password.getPassword() == null || password.getPassword().trim().isEmpty()) {
            throw new PasswordNotValidException("La contraseña es obligatoria");
        }
        if (password.getIdUsuario() == null) {
            throw new PasswordNotValidException("El ID de usuario es obligatorio");
        }
        
        // Validar formato de contraseña segura
        if (!isValidPassword(password.getPassword())) {
            throw new PasswordNotValidException(
                "La contraseña debe tener al menos 8 caracteres, una mayúscula, una minúscula, un número y un carácter especial (@$!%*?&)"
            );
        }
        
        // Verificar que el usuario no tenga ya una contraseña
        if (passwordRepository.existsByIdUsuario(password.getIdUsuario())) {
            throw new PasswordNotValidException(
                "El usuario con id " + password.getIdUsuario() + " ya tiene una contraseña registrada"
            );
        }
        
        // Cifrar la contraseña antes de guardarla
        String hashedPassword = passwordEncoder.encode(password.getPassword());
        password.setPassword(hashedPassword);
        
        return passwordRepository.save(password);
    }

    // CREATE - Crear contraseña con texto plano (para facilitar el uso)
    public Password createPassword(Integer idUsuario, String plainPassword) {
        Password password = new Password();
        password.setIdUsuario(idUsuario);
        password.setPassword(plainPassword);
        return save(password);
    }

    // READ 
    public List<Password> findAll() {
        List<Password> passwords = passwordRepository.findAll();
        if (passwords.isEmpty()) {
            throw new PasswordNotFoundException("No se encontraron contraseñas registradas");
        }
        return passwords;
    }

    // READ por ID
    public Password findById(Integer id) {
        return passwordRepository.findById(id)
            .orElseThrow(() -> new PasswordNotFoundException("Contraseña no encontrada con id: " + id));
    }

    // READ por ID de usuario
    public Password findByIdUsuario(Integer idUsuario) {
        return passwordRepository.findByIdUsuario(idUsuario)
            .orElseThrow(() -> new PasswordNotFoundException(
                "No se encontró contraseña para el usuario con id: " + idUsuario
            ));
    }

    // UPDATE - Actualizar contraseña (requiere contraseña anterior para seguridad)
    public Password update(Integer id, String oldPassword, String newPassword) {
        Password passwordExistente = findById(id);
        
        // Validar contraseña anterior
        if (!verifyPassword(oldPassword, passwordExistente.getPassword())) {
            throw new AuthenticationFailedException("La contraseña anterior no es correcta");
        }
        
        // Validar nueva contraseña
        if (newPassword == null || newPassword.trim().isEmpty()) {
            throw new PasswordNotValidException("La nueva contraseña es obligatoria");
        }
        
        if (!isValidPassword(newPassword)) {
            throw new PasswordNotValidException(
                "La contraseña debe tener al menos 8 caracteres, una mayúscula, una minúscula, un número y un carácter especial (@$!%*?&)"
            );
        }
        
        // No permitir que la nueva contraseña sea igual a la anterior
        if (verifyPassword(newPassword, passwordExistente.getPassword())) {
            throw new PasswordNotValidException("La nueva contraseña no puede ser igual a la anterior");
        }
        
        // Cifrar y actualizar
        String hashedPassword = passwordEncoder.encode(newPassword);
        passwordExistente.setPassword(hashedPassword);
        
        return passwordRepository.save(passwordExistente);
    }

    // UPDATE - Cambiar contraseña por ID de usuario
    public Password updateByIdUsuario(Integer idUsuario, String oldPassword, String newPassword) {
        Password password = findByIdUsuario(idUsuario);
        return update(password.getIdPassword(), oldPassword, newPassword);
    }

    // RESET - Resetear contraseña (sin validar contraseña anterior - solo para admin)
    public Password resetPassword(Integer id, String newPassword) {
        Password passwordExistente = findById(id);
        
        if (newPassword == null || newPassword.trim().isEmpty()) {
            throw new PasswordNotValidException("La nueva contraseña es obligatoria");
        }
        
        if (!isValidPassword(newPassword)) {
            throw new PasswordNotValidException(
                "La contraseña debe tener al menos 8 caracteres, una mayúscula, una minúscula, un número y un carácter especial (@$!%*?&)"
            );
        }
        
        // Cifrar y actualizar
        String hashedPassword = passwordEncoder.encode(newPassword);
        passwordExistente.setPassword(hashedPassword);
        
        return passwordRepository.save(passwordExistente);
    }

    // DELETE 
    public void deleteById(Integer id) {
        Password password = findById(id);
        passwordRepository.delete(password);
    }

    // DELETE por ID de usuario
    public void deleteByIdUsuario(Integer idUsuario) {
        if (!passwordRepository.existsByIdUsuario(idUsuario)) {
            throw new PasswordNotFoundException(
                "No se encontró contraseña para el usuario con id: " + idUsuario
            );
        }
        passwordRepository.deleteByIdUsuario(idUsuario);
    }

    // AUTENTICACIÓN - Verificar si la contraseña es correcta (para login)
    public boolean authenticate(Integer idUsuario, String plainPassword) {
        try {
            Password password = findByIdUsuario(idUsuario);
            return verifyPassword(plainPassword, password.getPassword());
        } catch (PasswordNotFoundException e) {
            return false;
        }
    }

    // AUTENTICACIÓN - Verificar contraseña con manejo de excepciones
    public void authenticateOrThrow(Integer idUsuario, String plainPassword) {
        if (!authenticate(idUsuario, plainPassword)) {
            throw new AuthenticationFailedException("Credenciales inválidas");
        }
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

    // UTILIDAD - Verificar si existe una contraseña para un usuario
    public boolean existsByIdUsuario(Integer idUsuario) {
        return passwordRepository.existsByIdUsuario(idUsuario);
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

}
