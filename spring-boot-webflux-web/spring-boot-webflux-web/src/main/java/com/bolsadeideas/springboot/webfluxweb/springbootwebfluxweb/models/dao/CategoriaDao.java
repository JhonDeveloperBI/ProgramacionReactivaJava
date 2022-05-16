package com.bolsadeideas.springboot.webfluxweb.springbootwebfluxweb.models.dao;

import com.bolsadeideas.springboot.webfluxweb.springbootwebfluxweb.models.documents.Categoria;
import org.springframework.data.mongodb.repository.ReactiveMongoRepository;

public interface CategoriaDao extends ReactiveMongoRepository<Categoria,String> {


}
