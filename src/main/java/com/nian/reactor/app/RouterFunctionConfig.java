package com.nian.reactor.app;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import org.springframework.web.reactive.function.server.RouterFunction;
import org.springframework.web.reactive.function.server.RouterFunctions;
import org.springframework.web.reactive.function.server.ServerResponse;
import static org.springframework.web.reactive.function.server.RequestPredicates.*;

import com.nian.reactor.app.handler.ProductoHandler;

// import static org.springframework.web.reactive.function.server.RouterFunctions.route;

@Configuration
public class RouterFunctionConfig {

    
    /*
     * se puede agregar la peticion de forma estatica des la importacion y eliminando el RequestPredicates y 
     * agregando el static en el import mas el . route, * 
     * import static org.springframework.web.reactive.function.server.RouterFunctions;
     * import static org.springframework.web.reactive.function.server.RequestPredicates;
     */

    @Bean
    public RouterFunction<ServerResponse> routes(ProductoHandler handler){
        // return RouterFunctions.route(RequestPredicates.GET("/api/v2/productos"), request -> {
        return RouterFunctions.route(GET("/api/v2/productos").or(GET("/api/v3/productos")), handler::listar)
                .andRoute(GET("/api/v2/productos/{id}"), handler::ver)
                .andRoute(POST("/api/v2/productos"), handler::crear)
                .andRoute(PUT("/api/v2/productos/{id}"), handler::editar);
    }
}
