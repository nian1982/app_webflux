package com.nian.reactor.app;

import java.sql.Date;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.data.mongodb.core.ReactiveMongoTemplate;

import com.nian.reactor.app.models.DAO.ProductoDao;
import com.nian.reactor.app.models.documents.Producto;

import reactor.core.publisher.Flux;

@SpringBootApplication
public class AppApplication implements CommandLineRunner {

	@Autowired
	private ProductoDao productoDao;

	@Autowired
	private ReactiveMongoTemplate mongoTemplate;

	private static final Logger log = LoggerFactory.getLogger(AppApplication.class);
	public static void main(String[] args) {
		SpringApplication.run(AppApplication.class, args);
	}

	@Override
	public void run(String... args) throws Exception {

		mongoTemplate.dropCollection("productos").subscribe();
		
		Flux.just(new Producto("PS5", 2.850),
					new Producto("PS4", 1.850),
					new Producto("PS3", 1.150)		
				)
				.flatMap(producto -> {
					producto.setCreateAt(new Date(0));
					return productoDao.save(producto);
				})
				.subscribe(producto -> log.info("Insert: "+producto.toString()));
	}

}
