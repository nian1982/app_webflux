package com.nian.reactor.app.models.DAO;

import org.springframework.data.mongodb.repository.ReactiveMongoRepository;

import com.nian.reactor.app.models.documents.Producto;

public interface ProductoDao extends ReactiveMongoRepository<Producto, String> {

}
