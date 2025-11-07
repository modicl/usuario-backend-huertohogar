package cl.huertohogar.usuario_backend.service;

import java.time.LocalDate;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import cl.huertohogar.usuario_backend.exception.OrdenNotFoundException;
import cl.huertohogar.usuario_backend.exception.OrdenNotValidException;
import cl.huertohogar.usuario_backend.model.Orden;
import cl.huertohogar.usuario_backend.repository.OrdenRepository;
import jakarta.transaction.Transactional;

@Service
@Transactional
public class OrdenService {

    @Autowired
    private OrdenRepository ordenRepository;

    @Autowired
    private UsuarioService usuarioService;

    // CREATE - Crear una nueva orden
    public Orden save(Orden orden) {
        if (orden == null) {
            throw new OrdenNotValidException("La orden no puede ser nula");
        }
        if (orden.getUsuario() == null) {
            throw new OrdenNotValidException("El usuario de la orden es obligatorio");
        }
        // Validar que el usuario existe
        usuarioService.findById(orden.getUsuario().getIdUsuario());
        
        if (orden.getFechaOrden() == null) {
            orden.setFechaOrden(LocalDate.now());
        }
        if (orden.getEstado() == null || orden.getEstado().trim().isEmpty()) {
            throw new OrdenNotValidException("El estado de la orden es obligatorio");
        }
        if (orden.getTotalOrden() == null || orden.getTotalOrden() <= 0) {
            throw new OrdenNotValidException("El total de la orden debe ser mayor a 0");
        }
        if (orden.getDireccionEnvio() == null || orden.getDireccionEnvio().trim().isEmpty()) {
            throw new OrdenNotValidException("La dirección de envío es obligatoria");
        }
        return ordenRepository.save(orden);
    }

    // READ 
    public List<Orden> findAll() {
        List<Orden> ordenes = ordenRepository.findAll();
        if (ordenes.isEmpty()) {
            throw new OrdenNotFoundException("No se encontraron órdenes");
        }
        return ordenes;
    }

    // READ por ID
    public Orden findById(Integer id) {
        return ordenRepository.findById(id)
            .orElseThrow(() -> new OrdenNotFoundException("Orden no encontrada con id: " + id));
    }

    // UPDATE 
    public Orden update(Integer id, Orden ordenActualizada) {
        Orden ordenExistente = findById(id);
        
        // Validaciones
        if (ordenActualizada.getUsuario() == null) {
            throw new OrdenNotValidException("El usuario de la orden es obligatorio");
        }
        // Validar que el usuario existe
        usuarioService.findById(ordenActualizada.getUsuario().getIdUsuario());
        
        if (ordenActualizada.getFechaOrden() == null) {
            throw new OrdenNotValidException("La fecha de la orden es obligatoria");
        }
        if (ordenActualizada.getEstado() == null || ordenActualizada.getEstado().trim().isEmpty()) {
            throw new OrdenNotValidException("El estado de la orden es obligatorio");
        }
        if (ordenActualizada.getTotalOrden() == null || ordenActualizada.getTotalOrden() <= 0) {
            throw new OrdenNotValidException("El total de la orden debe ser mayor a 0");
        }
        if (ordenActualizada.getDireccionEnvio() == null || ordenActualizada.getDireccionEnvio().trim().isEmpty()) {
            throw new OrdenNotValidException("La dirección de envío es obligatoria");
        }
        
        // Actualizar campos
        ordenExistente.setUsuario(ordenActualizada.getUsuario());
        ordenExistente.setFechaOrden(ordenActualizada.getFechaOrden());
        ordenExistente.setEstado(ordenActualizada.getEstado());
        ordenExistente.setTotalOrden(ordenActualizada.getTotalOrden());
        ordenExistente.setDireccionEnvio(ordenActualizada.getDireccionEnvio());

        return ordenRepository.save(ordenExistente);
    }

    // PATCH 
    public Orden partialUpdate(Integer id, Orden ordenActualizada) {
        Orden ordenExistente = findById(id);
        
        // Solo actualiza los campos que no son nulos
        if (ordenActualizada.getUsuario() != null) {
            // Validar que el usuario existe
            usuarioService.findById(ordenActualizada.getUsuario().getIdUsuario());
            ordenExistente.setUsuario(ordenActualizada.getUsuario());
        }
        
        if (ordenActualizada.getFechaOrden() != null) {
            ordenExistente.setFechaOrden(ordenActualizada.getFechaOrden());
        }
        
        if (ordenActualizada.getEstado() != null) {
            if (ordenActualizada.getEstado().trim().isEmpty()) {
                throw new OrdenNotValidException("El estado de la orden no puede estar vacío");
            }
            ordenExistente.setEstado(ordenActualizada.getEstado());
        }
        
        if (ordenActualizada.getTotalOrden() != null) {
            if (ordenActualizada.getTotalOrden() <= 0) {
                throw new OrdenNotValidException("El total de la orden debe ser mayor a 0");
            }
            ordenExistente.setTotalOrden(ordenActualizada.getTotalOrden());
        }
        
        if (ordenActualizada.getDireccionEnvio() != null) {
            if (ordenActualizada.getDireccionEnvio().trim().isEmpty()) {
                throw new OrdenNotValidException("La dirección de envío no puede estar vacía");
            }
            ordenExistente.setDireccionEnvio(ordenActualizada.getDireccionEnvio());
        }

        return ordenRepository.save(ordenExistente);
    }

    // DELETE 
    public void deleteById(Integer id) {
        Orden orden = findById(id);
        ordenRepository.delete(orden);
    }
    
    // Buscar ordenes por usuario
    public List<Orden> findByUsuario(Integer idUsuario) {
        // Validar que el usuario existe
        usuarioService.findById(idUsuario);
        
        List<Orden> ordenes = ordenRepository.findByUsuarioIdUsuario(idUsuario);
        if (ordenes.isEmpty()) {
            throw new OrdenNotFoundException("No se encontraron órdenes para el usuario con id: " + idUsuario);
        }
        return ordenes;
    }

    // Buscar por estado
    public List<Orden> findByEstado(String estado) {
        List<Orden> ordenes = ordenRepository.findByEstado(estado);
        if (ordenes.isEmpty()) {
            throw new OrdenNotFoundException("No se encontraron órdenes con estado: " + estado);
        }
        return ordenes;
    }

    // Buscar por rango de fechas
    public List<Orden> findByRangoFechas(LocalDate fechaInicio, LocalDate fechaFin) {
        List<Orden> ordenes = ordenRepository.findByFechaOrdenBetween(fechaInicio, fechaFin);
        if (ordenes.isEmpty()) {
            throw new OrdenNotFoundException("No se encontraron órdenes entre " + fechaInicio + " y " + fechaFin);
        }
        return ordenes;
    }

    // Actualizar estado de una orden
    public Orden updateEstado(Integer id, String nuevoEstado) {
        if (nuevoEstado == null || nuevoEstado.trim().isEmpty()) {
            throw new OrdenNotValidException("El estado no puede estar vacío");
        }
        
        Orden orden = findById(id);
        orden.setEstado(nuevoEstado);
        return ordenRepository.save(orden);
    }

    // Calcular total de órdenes por usuario
    public Double calcularTotalPorUsuario(Integer idUsuario) {
        List<Orden> ordenes = findByUsuario(idUsuario);
        return ordenes.stream()
            .mapToDouble(Orden::getTotalOrden)
            .sum();
    }
}