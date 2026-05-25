package com.pruebatecnica.seguros.config;

import java.io.IOException;

import org.springframework.stereotype.Component;
import org.springframework.web.filter.OncePerRequestFilter;

import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;

/**
 * Filtro de seguridad de nivel de entrada que intercepta todas las peticiones HTTP.
 * Valida la presencia y validez de una API Key en el encabezado para proteger los endpoints de negocio.
 */
@Component
public class FilterApiKey extends OncePerRequestFilter {
    
    private static final String API_KEY_HEADER = "x-api-key";
    private static final String VALID_API_KEY = "123456"; // Definida como constante por requerimiento de la prueba

    /**
     * Intercepta la cadena de filtros para validar la autenticación.
     * Excluye rutas de infraestructura y documentación (H2, Swagger, Core Mock) del requerimiento de API Key.
     */
    @Override
    protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response, FilterChain filterChain)
            throws ServletException, IOException {
        
        String path = request.getRequestURI();
        
        // Exclusión de rutas públicas: Infraestructura, Consola H2 y Documentación OpenAPI
        if (  	path.startsWith("/h2-console") || 
        		path.startsWith("/v3/api-docs") || 
        		path.startsWith("/swagger-ui")) {
            
            filterChain.doFilter(request, response);
            return;
        }

        // Validación de autenticación
        String apiKey = request.getHeader(API_KEY_HEADER);
        if (apiKey == null || !apiKey.equals(VALID_API_KEY)) {
            response.setStatus(HttpServletResponse.SC_UNAUTHORIZED);
            response.setContentType("application/json");
            response.getWriter().write("{\"error\": \"No autorizado. API Key invalida o ausente.\"}");
            return;
        }
        
        filterChain.doFilter(request, response);
    }
}