package com.bolsadeideas.springboot.webfluxweb.springbootwebfluxweb.models.services;

import com.bolsadeideas.springboot.webfluxweb.springbootwebfluxweb.models.dao.CategoriaDao;
import com.bolsadeideas.springboot.webfluxweb.springbootwebfluxweb.models.dao.ProductoDao;
import com.bolsadeideas.springboot.webfluxweb.springbootwebfluxweb.models.documents.Categoria;
import com.bolsadeideas.springboot.webfluxweb.springbootwebfluxweb.models.documents.Producto;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

@Service //steorotipo  una fachada
public class ProductoServiceImpl implements ProductoService{

    @Autowired
    private ProductoDao dao;

    @Autowired
    private CategoriaDao categoriaDao;

    @Override
    public Flux<Producto> findAll() {
        return dao.findAll();
    }

    @Override
    public Flux<Producto> findAllConNombreUpperCase() {
        return dao.findAll().map(producto -> {
            producto.setNombre(producto.getNombre().toUpperCase());
            return producto;
        });
    }

    @Override
    public Flux<Producto> findAllConNombreUpperCaseRepeat() {
        return findAllConNombreUpperCase().repeat(5000);
    }

    @Override
    public Mono<Producto> findById(String id) {
        return dao.findById(id);
    }

    @Override
    public Mono<Producto> save(Producto producto) {
        return dao.save(producto);
    }

    @Override
    public Mono<Void> delete(Producto producto) {
        return dao.delete(producto);
    }

    @Override
    public Flux<Categoria> findAllCategoria() {
        return categoriaDao.findAll();
    }

    @Override
    public Mono<Categoria> findByIdCategoria(String id) {
        return categoriaDao.findById(id);
    }

    @Override
    public Mono<Categoria> save(Categoria categoria) {
        return categoriaDao.save(categoria);
    }
}
