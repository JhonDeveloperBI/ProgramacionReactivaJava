package com.example.webFlux;

import com.example.webFlux.models.Usuario;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import reactor.core.publisher.Flux;

import java.util.Locale;


@SpringBootApplication
public class WebFluxApplication implements CommandLineRunner {
    private  static final Logger log = LoggerFactory.getLogger(WebFluxApplication.class);

	public static void main(String[] args) {
		SpringApplication.run(WebFluxApplication.class, args);
	}

	@Override
	public void run(String... args) throws Exception {
		Flux<Usuario> nombres = Flux.just("Jhon","Juan","Pedro","Maria","Andrea")
				.map(nombre ->  new Usuario(nombre.toUpperCase(),null))
				.doOnNext(usuario ->{
					if( usuario == null){
						throw new RuntimeException("Los nombres no pueden ser vacios");
					}
					System.out.println(usuario.getNombre());
				})
				.map(usuario -> { String  nombre = usuario.getNombre().toLowerCase();
				usuario.setNombre( nombre);
				return usuario;
				});


		//subscribe
		nombres.subscribe(e -> log.info(e.toString()),
				error -> log.error(error.getMessage()),
				new Runnable() {
					@Override
					public void run() {
						log.info("Ha finalizado la ejecucion del observable con exito");
					}
				}); // observador, consumidor
	}


}
