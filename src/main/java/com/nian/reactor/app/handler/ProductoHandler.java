package com.nian.reactor.app.handler;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.MediaType;
import org.springframework.stereotype.Component;
import static org.springframework.web.reactive.function.BodyInserters.*;

import java.net.URI;
import java.util.Date;

import org.springframework.web.reactive.function.server.ServerRequest;
import org.springframework.web.reactive.function.server.ServerResponse;

import com.nian.reactor.app.models.documents.Producto;
import com.nian.reactor.app.models.services.ProductoService;

import reactor.core.publisher.Mono;

@Component
public class ProductoHandler {

    private final Logger log = LoggerFactory.getLogger(ProductoHandler.class);

    private final ProductoService productoService;

    public ProductoHandler(ProductoService productoService) {
        this.productoService = productoService;
    }


    // public Mono<ServerResponse> upload(ServerRequest request) {
    //     String id = request.pathVariable("id");

    // }

    public Mono<ServerResponse> listar(ServerRequest request) {
        return ServerResponse.ok()
                .contentType(MediaType.APPLICATION_JSON)
                .body(productoService.getAll(), Producto.class);
    }

    public Mono<ServerResponse> ver(ServerRequest request) {
        String id = request.pathVariable("id");

        return productoService.getById(id)
                .flatMap(p -> ServerResponse
                        .ok()
                        .contentType(MediaType.APPLICATION_JSON)
                        .body(fromValue(p)))
                // .body(BodyInserters.fromValue(p))) //sin el statid del import
                .switchIfEmpty(ServerResponse.notFound().build());
    }

    public Mono<ServerResponse> crear(ServerRequest request) {
        Mono<Producto> producto = request.bodyToMono(Producto.class);

        return producto.flatMap(p -> {
            if (p.getCreateAt() == null) {
                p.setCreateAt(new Date());
            }
            return productoService.save(p);
        }).flatMap(p -> ServerResponse
                .created(URI.create("/api/v1/productos/".concat(p.getId())))
                .contentType(MediaType.APPLICATION_JSON)
                .body(fromValue(p)));
    }

    public Mono<ServerResponse> editar(ServerRequest request) {
        Mono<Producto> producto = request.bodyToMono(Producto.class);
        String id = request.pathVariable("id");
    
        log.error("\nID = " + id + "\n");
    
        Mono<Producto> productoDb = productoService.getById(id);
    
        return productoDb
            .log("Producto DB") // Log para mostrar el producto desde la base de datos
            .zipWith(producto.log("Producto Request"), (db, req) -> {
                db.setNombre(req.getNombre());
                db.setPrecio(req.getPrecio());
    
                // Validar si la fecha es null
                if (req.getCreateAt() == null) {
                    db.setCreateAt(db.getCreateAt() != null ? db.getCreateAt() : new Date());
                } else {
                    db.setCreateAt(req.getCreateAt());
                }
    
                return db;
            })
            .log("Producto Updated") // Log para mostrar el producto actualizado
            .flatMap(p -> ServerResponse.created(URI.create("/api/v2/productos/".concat(p.getId())))
                    .contentType(MediaType.APPLICATION_JSON)
                    .body(productoService.save(p), Producto.class))
            .switchIfEmpty(ServerResponse.notFound().build());
    }
    
    

    // public Mono<ServerResponse> editar(ServerRequest request) {
    // Mono<Producto> producto = request.bodyToMono(Producto.class);
    // String id = request.pathVariable("id");

    // log.error("\nID = " + id + "\n");

    // Mono<Producto> productoDb = productoService.getById(id);

    // log.error("producto = " + productoDb);

    // return productoDb.zipWith(producto, (db, req) -> {
    // BeanUtils.copyProperties(req, db);
    // return db;
    // })
    // .flatMap(p -> {
    // if (p.getId() == null || p.getId().isEmpty()) {
    // return ServerResponse.status(HttpStatus.INTERNAL_SERVER_ERROR)
    // .bodyValue("El ID del producto no puede ser nulo o vacío.");
    // }
    // return
    // ServerResponse.created(URI.create("/api/v1/productos/".concat(p.getId())))
    // .contentType(MediaType.APPLICATION_JSON)
    // .body(productoService.save(p), Producto.class);
    // })
    // .switchIfEmpty(ServerResponse.notFound().build());
    // }

    // public Mono<ServerResponse> editar(ServerRequest request){
    // Mono<Producto> producto = request.bodyToMono(Producto.class);
    // String id = request.pathVariable("id");

    // Mono<Producto> productoDb = productoService.getById(id);

    // return productoDb.zipWith(producto, (db, req) -> {
    // BeanUtils.copyProperties(req, db);
    // return db;
    // })
    // .flatMap(p ->
    // ServerResponse.created(URI.create("/api/v1/productos/".concat(p.getId())))
    // .contentType(MediaType.APPLICATION_JSON)
    // .body(productoService.save(p), Producto.class))
    // .switchIfEmpty(ServerResponse.notFound().build());
    // }

}
