package com.example.springboot.webflux.api.rest.exampleapirest.controllers;

import com.example.springboot.webflux.api.rest.exampleapirest.models.documents.Producto;
import com.example.springboot.webflux.api.rest.exampleapirest.models.services.ProductoService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

import java.net.URI;
import java.util.Date;

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

    @PostMapping
    public Mono<ResponseEntity<Producto>> crear(@RequestBody  Producto producto){
        if(producto.getCreateAt() == null){
            producto.setCreateAt( new Date());
        }

        return service.save(producto).map(p -> ResponseEntity.created(URI.create("/api/productos/"
                .concat(p.getId())))
                .contentType(MediaType.APPLICATION_JSON)
                .body(p));

    }

}
