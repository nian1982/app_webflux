package com.nian.reactor.app.models.services;

import com.nian.reactor.app.models.documents.Producto;

import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

public interface ProductoService {

    Flux<Producto> getAll();
    Mono<Producto> getById(String id);
    Mono<Producto> save(Producto producto);
    Mono<Void> delete(String id);

}
