package com.example.springboot.webflux.api.rest.exampleapirest.controllers;

import com.example.springboot.webflux.api.rest.exampleapirest.models.documents.Producto;
import com.example.springboot.webflux.api.rest.exampleapirest.models.services.ProductoService;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.http.codec.multipart.FilePart;
import org.springframework.web.bind.annotation.*;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

import java.io.File;
import java.net.URI;
import java.util.Date;
import java.util.UUID;

@RestController
@RequestMapping("api/productos")
public class ProductoController {

    @Autowired
    private ProductoService service;

    @Value("${config.uploads.path}")
    private String path;

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


    @PostMapping("/upload/{id}")
    public Mono<ResponseEntity<Producto>> upload(@PathVariable String id, @RequestPart FilePart file){
        return service.findById(id).flatMap(p -> {
                    p.setFoto(UUID.randomUUID().toString() + "-" + file.filename()
                            .replace(" ", "")
                            .replace(":", "")
                            .replace("\\", ""));

                    return file.transferTo(new File(path + p.getFoto())).then(service.save(p));
                }).map(p -> ResponseEntity.ok(p))
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

    @PutMapping("/{id}")
    public Mono<ResponseEntity<Producto>> editar(@RequestBody Producto producto, @PathVariable String id){
        return service.findById(id).flatMap(p -> {
                    p.setNombre(producto.getNombre());
                    p.setPrecio(producto.getPrecio());
                    p.setCategoria(producto.getCategoria());

                    return service.save(p);
                }).map( p ->ResponseEntity.created(URI.create("/api/productos".concat(p.getId())))
                .contentType(MediaType.APPLICATION_JSON)
                .body(p))
                .defaultIfEmpty(ResponseEntity.notFound().build());
    }

    @DeleteMapping("/{id}")
        public Mono<ResponseEntity<Void>> eliminar(@PathVariable String id){
            return service.findById(id).flatMap(p -> {
                return service.delete(p).then(Mono.just(new ResponseEntity<Void>(HttpStatus.NO_CONTENT) ));
            }).defaultIfEmpty(new ResponseEntity<Void>(HttpStatus.NOT_FOUND));
        }

}
