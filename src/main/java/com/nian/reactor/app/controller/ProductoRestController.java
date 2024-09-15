package com.nian.reactor.app.controller;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.nian.reactor.app.models.DAO.ProductoDao;
import com.nian.reactor.app.models.documents.Producto;

import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

@RestController
@RequestMapping("/producto")
public class ProductoRestController {

    @Autowired
    private ProductoDao dao;

    private static final Logger log = LoggerFactory.getLogger(ProductoRestController.class);

    @GetMapping
    public Flux<Producto> index(){
        Flux<Producto> productos = dao.findAll()
                .map(p -> {
                    p.getNombre().toUpperCase();
                    return p;
                })
                .doOnNext(pl -> log.info("Producto: "+pl.getNombre()));

        return productos;        
    }

    @GetMapping("/{id}")
    public Mono<Producto> getById(@PathVariable String id){
        Mono<Producto> producto = dao.findById(id);
        return producto;
    }



}
