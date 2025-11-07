package cl.huertohogar.usuario_backend.config;


import org.springframework.context.annotation.Configuration;

import io.swagger.v3.oas.annotations.OpenAPIDefinition;
import io.swagger.v3.oas.annotations.info.Contact;
import io.swagger.v3.oas.annotations.info.Info;
import io.swagger.v3.oas.annotations.servers.Server;

@Configuration
@OpenAPIDefinition(
    info = @Info(
        title = "API de Usuarios - Huerto Hogar",
        description = "API REST para gestión de usuarios, passwords, órdenes, ciudades, y regiones en Huerto Hogar",
        version = "1.0.0",
        contact = @Contact(
            name = "Huerto Hogar",
            email = "contacto@huertohogar.cl"
        )
    ),
    servers = {
        @Server(
            description = "Local",
            url = "http://localhost:8080"
        )
    }
)

public class OpenAPIConfig {

}
