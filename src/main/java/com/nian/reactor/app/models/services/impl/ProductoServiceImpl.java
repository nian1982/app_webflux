package com.nian.reactor.app.models.services.impl;

import org.springframework.stereotype.Service;

import com.nian.reactor.app.models.DAO.ProductoDao;
import com.nian.reactor.app.models.documents.Producto;
import com.nian.reactor.app.models.services.ProductoService;

import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

@Service
public class ProductoServiceImpl implements ProductoService {

    private final ProductoDao productoDao;

    public ProductoServiceImpl(ProductoDao productoDao){
        this.productoDao = productoDao;
    }

    @Override
    public Flux<Producto> getAll() {
        return productoDao.findAll();
    }

    @Override
    public Mono<Producto> getById(String id) {
        return productoDao.findById(id);
    }

    @Override
    public Mono<Producto> save(Producto producto) {
        return productoDao.save(producto);
    }

    @Override
    public Mono<Void> delete(String id) {
        return productoDao.deleteById(id);
    }

}
