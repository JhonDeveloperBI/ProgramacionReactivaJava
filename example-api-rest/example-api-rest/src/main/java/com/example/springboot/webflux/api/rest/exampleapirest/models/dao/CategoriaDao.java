package com.example.springboot.webflux.api.rest.exampleapirest.models.dao;


import com.example.springboot.webflux.api.rest.exampleapirest.models.documents.Categoria;
import org.springframework.data.mongodb.repository.ReactiveMongoRepository;
import reactor.core.publisher.Mono;

public interface CategoriaDao extends ReactiveMongoRepository<Categoria,String> {

    public Mono<Categoria> findByNombre(String nombre);
}
