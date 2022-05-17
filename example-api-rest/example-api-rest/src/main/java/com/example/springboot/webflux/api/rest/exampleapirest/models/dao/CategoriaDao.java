package com.example.springboot.webflux.api.rest.exampleapirest.models.dao;


import com.example.springboot.webflux.api.rest.exampleapirest.models.documents.Categoria;
import org.springframework.data.mongodb.repository.ReactiveMongoRepository;

public interface CategoriaDao extends ReactiveMongoRepository<Categoria,String> {


}
