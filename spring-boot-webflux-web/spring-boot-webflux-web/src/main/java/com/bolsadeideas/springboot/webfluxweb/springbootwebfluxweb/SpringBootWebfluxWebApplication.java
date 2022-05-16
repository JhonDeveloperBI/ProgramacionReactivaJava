package com.bolsadeideas.springboot.webfluxweb.springbootwebfluxweb;

import com.bolsadeideas.springboot.webfluxweb.springbootwebfluxweb.models.documents.Categoria;
import com.bolsadeideas.springboot.webfluxweb.springbootwebfluxweb.models.documents.Producto;
import com.bolsadeideas.springboot.webfluxweb.springbootwebfluxweb.models.services.ProductoServiceImpl;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.data.mongodb.core.ReactiveMongoTemplate;
import reactor.core.publisher.Flux;

import java.util.Date;

@SpringBootApplication
public class SpringBootWebfluxWebApplication implements CommandLineRunner {

	private static final Logger log = LoggerFactory.getLogger(SpringBootWebfluxWebApplication.class);

	@Autowired
	private ProductoServiceImpl productoService;

	@Autowired
	private ReactiveMongoTemplate mongoTemplate;

	public static void main(String[] args) {
		SpringApplication.run(SpringBootWebfluxWebApplication.class, args);
	}

	@Override
	public void run(String... args) throws Exception {
		mongoTemplate.dropCollection("productos").subscribe();
		mongoTemplate.dropCollection("categorias").subscribe();

		Categoria electronico = new Categoria("Electrónico");
		Categoria deporte = new Categoria("Deporte");
		Categoria computacion = new Categoria("Computación");
		Categoria muebles = new Categoria("Muebles");

		Flux.just(electronico,deporte,computacion,muebles).flatMap(categoria ->{
					return productoService.save(categoria);
				})
				.doOnNext(categoria -> log.info("Insert:" + categoria.getId()+
						" " + categoria.getNombre() )).thenMany(

						Flux.just(new Producto("TV panasonic",550.36, electronico),
								new Producto("Sony Camara HD",600.00,electronico),
								new Producto("Apple ipod ",600.00,electronico),
								new Producto("Sony Notebook",600.00,computacion),
								new Producto("Hewlett Packard Multifuncional",600.00,computacion),
								new Producto("Bicicleta",600.00,deporte),
								new Producto("Notebook Omen 17",600.00,computacion),
								new Producto("Mica Comada 5 cajones",600.00,muebles),
								new Producto("TV Sony Bravia OLED 4k",600.00,electronico)
						).flatMap(producto ->{
							producto.setCreateAt(new Date());
							return productoService.save(producto);
						})
				)
				.subscribe(producto -> log.info("Insert:" + producto.getId()+
						" " + producto.getNombre() ));


	}
}
