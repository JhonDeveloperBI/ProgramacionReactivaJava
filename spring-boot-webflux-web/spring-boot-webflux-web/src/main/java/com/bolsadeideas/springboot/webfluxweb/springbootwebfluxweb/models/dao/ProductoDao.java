package com.bolsadeideas.springboot.webfluxweb.springbootwebfluxweb.models.dao;

import com.bolsadeideas.springboot.webfluxweb.springbootwebfluxweb.models.documents.Producto;
import org.springframework.data.mongodb.repository.ReactiveMongoRepository;

public interface ProductoDao extends ReactiveMongoRepository<Producto,String> {

}
