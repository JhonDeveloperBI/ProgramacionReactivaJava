package com.bolsadeideas.springboot.webfluxweb.springbootwebfluxweb.controllers;


import com.bolsadeideas.springboot.webfluxweb.springbootwebfluxweb.models.documents.Producto;
import com.bolsadeideas.springboot.webfluxweb.springbootwebfluxweb.models.services.ProductoService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.thymeleaf.spring5.context.webflux.ReactiveDataDriverContextVariable;
import reactor.core.publisher.Flux;

import java.time.Duration;

@Controller
public class ProductoController {

    private static  final Logger log = LoggerFactory.getLogger(ProductoController.class);

    @Autowired
    private ProductoService service;

    @GetMapping({"/listar","/"})
    public String listar(Model model){
        Flux<Producto> productos = service.findAllConNombreUpperCase();

        productos.subscribe(prod -> log.info(prod.getNombre()));

        model.addAttribute("productos",productos);
        model.addAttribute("titulo","Listado de productos");

        return "listar";
    }

    @GetMapping("/listar-datadriver")
    public String listarDataDriver(Model model){
        Flux<Producto> productos = service.findAllConNombreUpperCase().delayElements(Duration.ofSeconds(1));

        productos.subscribe(prod -> log.info(prod.getNombre()));

        model.addAttribute("productos", new ReactiveDataDriverContextVariable(productos,2) );
        model.addAttribute("titulo","Listado de productos");

        return "listar";
    }

    @GetMapping("/listar-full")
    public String listarFull(Model model){
        Flux<Producto> productos = service.findAllConNombreUpperCaseRepeat();


        model.addAttribute("productos",productos);
        model.addAttribute("titulo","Listado de productos");

        return "listar";
    }

    @GetMapping("/listar-chunked")
    public String listarChunked(Model model){
        Flux<Producto> productos = service.findAllConNombreUpperCaseRepeat();


        model.addAttribute("productos",productos);
        model.addAttribute("titulo","Listado de productos");

        return "listar-chunked";
    }
}
