package com.bolsadeideas.springboot.webfluxweb.springbootwebfluxweb;

import com.bolsadeideas.springboot.webfluxweb.springbootwebfluxweb.models.dao.ProductoDao;
import com.bolsadeideas.springboot.webfluxweb.springbootwebfluxweb.models.documents.Producto;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import reactor.core.publisher.Flux;

@SpringBootApplication
public class SpringBootWebfluxWebApplication implements CommandLineRunner {

	private static final Logger log = LoggerFactory.getLogger(SpringBootWebfluxWebApplication.class);

	@Autowired
	private ProductoDao dao;

	public static void main(String[] args) {
		SpringApplication.run(SpringBootWebfluxWebApplication.class, args);
	}

	@Override
	public void run(String... args) throws Exception {
		Flux.just(new Producto("TV panasonic",550.36),
				new Producto("Playstation",600.00),
				new Producto("Playstation 1 ",600.00),
				new Producto("Playstation 2",600.00),
				new Producto("Playstation 3",600.00),
				new Producto("Playstation 4",600.00),
				new Producto("Playstation 5",600.00),
				new Producto("Playstation 6",600.00),
				new Producto("Playstation 7",600.00)
		).flatMap(producto -> dao.save(producto))
				.subscribe(producto -> log.info("Insert:" + producto.getId()+
						" " + producto.getNombre() ));


	}
}
