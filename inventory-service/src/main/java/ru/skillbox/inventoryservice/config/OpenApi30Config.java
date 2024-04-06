package ru.skillbox.inventoryservice.config;

import io.swagger.v3.oas.annotations.OpenAPIDefinition;
import io.swagger.v3.oas.annotations.info.Info;
import io.swagger.v3.oas.annotations.servers.Server;
import org.springframework.context.annotation.Configuration;

@Configuration
@OpenAPIDefinition(info = @Info(title = "Inventory API", version = "v1"),
        servers = {@Server(url = "http://localhost:9090/api")
})
public class OpenApi30Config {

}
