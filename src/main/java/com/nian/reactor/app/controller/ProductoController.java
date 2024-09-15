package com.nian.reactor.app.controller;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;

import com.nian.reactor.app.models.DAO.ProductoDao;
import com.nian.reactor.app.models.documents.Producto;

import reactor.core.publisher.Flux;

@Controller
public class ProductoController {

    @Autowired
    private ProductoDao productoDao;

    private static Logger log = LoggerFactory.getLogger(ProductoController.class);

    @GetMapping({"/listar","/"})
    public String listar(Model model){
        Flux<Producto> productos = productoDao.findAll()
                .map(producto -> {
                    producto.setNombre(producto.getNombre().toUpperCase());
                    return producto;
                });

        productos.subscribe(prod -> log.info("Producto: "+prod.getNombre()));

        model.addAttribute("productos", productos);
        model.addAttribute("titulo", "Listado de Productos");
        return "listar";
    }

}
