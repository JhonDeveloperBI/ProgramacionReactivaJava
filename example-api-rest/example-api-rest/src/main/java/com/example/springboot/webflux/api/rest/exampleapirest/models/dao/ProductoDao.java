package com.example.springboot.webflux.api.rest.exampleapirest.models.dao;

import com.example.springboot.webflux.api.rest.exampleapirest.models.documents.Producto;
import org.springframework.data.mongodb.repository.ReactiveMongoRepository;

public interface ProductoDao extends ReactiveMongoRepository<Producto,String> {

}
