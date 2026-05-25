package com.pruebatecnica.seguros.config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import io.swagger.v3.oas.models.Components;
import io.swagger.v3.oas.models.OpenAPI;
import io.swagger.v3.oas.models.info.Info;
import io.swagger.v3.oas.models.security.SecurityRequirement;
import io.swagger.v3.oas.models.security.SecurityScheme;

/**
 * Configuración centralizada para la documentación de la API mediante OpenAPI (Swagger).
 * Define los metadatos globales que serán visualizados en la interfaz de Swagger UI.
 */
@Configuration
public class OpenApiConfig {

    /**
     * Define el bean de configuración de OpenAPI.
     * Personaliza el título, versión y descripción que aparecerán en el portal de documentación.
     * * @return Objeto OpenAPI configurado con la información del proyecto.
     */
	@Bean
	public OpenAPI customOpenAPI() {
	    final String securitySchemeName = "bearerAuth"; // Aunque sea API Key, se usa este nombre para el esquema
	    return new OpenAPI()
	            .addSecurityItem(new SecurityRequirement().addList(securitySchemeName))
	            .components(new Components()
	                .addSecuritySchemes(securitySchemeName,
	                    new SecurityScheme()
	                        .name("x-api-key") // Nombre del header que espera tu filtro
	                        .type(SecurityScheme.Type.APIKEY)
	                        .in(SecurityScheme.In.HEADER)))
	            .info(new Info()
	                    .title("API de Gestión de Pólizas")
	                    .version("1.0")
	                    .description("Documentación técnica. Usa el botón 'Authorize' con '123456'"));
	}
}