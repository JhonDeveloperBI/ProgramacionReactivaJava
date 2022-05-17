package com.example.springboot.webflux.api.rest.exampleapirest.controllers;

import com.example.springboot.webflux.api.rest.exampleapirest.models.documents.Producto;
import com.example.springboot.webflux.api.rest.exampleapirest.models.services.ProductoService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

@RestController
@RequestMapping("api/productos")
public class ProductoController {

    @Autowired
    private ProductoService service;

    @GetMapping
    public Mono<ResponseEntity<Flux<Producto>>> lista(){
        return Mono.just(
                ResponseEntity.ok( )
                        .contentType(MediaType.APPLICATION_JSON)
                        .body(service.findAll())
        );
    }

    @GetMapping("/{id}")
    public Mono<ResponseEntity<Producto>> ver(@PathVariable String id){
         return service.findById(id).map( p ->
                 ResponseEntity.ok()
                         .contentType(MediaType.APPLICATION_JSON)
                         .body(p))
                         .defaultIfEmpty(ResponseEntity.notFound().build());

    }

}
