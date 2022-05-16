package com.bolsadeideas.springboot.webfluxweb.springbootwebfluxweb.models.services;

import com.bolsadeideas.springboot.webfluxweb.springbootwebfluxweb.models.documents.Categoria;
import com.bolsadeideas.springboot.webfluxweb.springbootwebfluxweb.models.documents.Producto;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

public interface ProductoService {

  public Flux<Producto> findAll();

  public Flux<Producto> findAllConNombreUpperCase();

  public Flux<Producto> findAllConNombreUpperCaseRepeat();

  public Mono<Producto> findById(String id);

  public Mono<Producto> save( Producto producto);

  public Mono<Void> delete(Producto producto);

  public Flux<Categoria> findAllCategoria();

  public Mono<Categoria> findByIdCategoria(String id);

  public Mono<Categoria> save( Categoria categoria);
}
