package com.example.webFlux;

import com.example.webFlux.models.Usuario;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

import java.util.ArrayList;
import java.util.List;
import java.util.Locale;


@SpringBootApplication
public class WebFluxApplication implements CommandLineRunner {
    private  static final Logger log = LoggerFactory.getLogger(WebFluxApplication.class);

	public static void main(String[] args) {
		SpringApplication.run(WebFluxApplication.class, args);
	}

	@Override
	public void run(String... args) throws Exception {
    // exampleIterable();
	 exampleFlatMap();

	}

	public void exampleFlatMap() throws Exception {

		List<String> usuariosList = new ArrayList<>();
		usuariosList.add("Jhon Pepito");
		usuariosList.add("Juan Guzman");
		usuariosList.add("Pedro Tal");
		usuariosList.add("Maria Valbuena");
		usuariosList.add("Andrea Ronaldo");
		usuariosList.add("Bruce Lee");
		usuariosList.add("Bruce Willis");

		 Flux.fromIterable(usuariosList)
				 .map(nombre ->  new Usuario(nombre.split(" ")[0].toUpperCase(),nombre.split(" ")[1].toUpperCase()))
				 .flatMap(usuario -> {
					 if(usuario.getNombre().equalsIgnoreCase("bruce")){
						 return Mono.just(usuario);
					 }else{
						 return Mono.empty();
					 }
				 }  )
				 .map(usuario -> { String  nombre = usuario.getNombre().toLowerCase();
					usuario.setNombre( nombre);
					return usuario;
				 })
				 .subscribe( u -> log.info(u.toString())); // observador, consumidor
	}

	public void exampleIterable() throws Exception {

		List<String> usuariosList = new ArrayList<>();
		usuariosList.add("Jhon Pepito");
		usuariosList.add("Juan Guzman");
		usuariosList.add("Pedro Tal");
		usuariosList.add("Maria Valbuena");
		usuariosList.add("Andrea Ronaldo");
		usuariosList.add("Bruce Lee");
		usuariosList.add("Bruce Willis");

		Flux<String> nombres = Flux.fromIterable(usuariosList); /*Flux.just("Jhon Pepito","Juan Guzman","Pedro Tal","Maria Valbuena","Andrea Ronaldo","Bruce Lee","Bruce Willis");*/

		Flux<Usuario> usuarios = nombres.map(nombre ->  new Usuario(nombre.split(" ")[0].toUpperCase(),nombre.split(" ")[1].toUpperCase()))
				.filter(usuario ->  usuario.getNombre().toLowerCase().equals(("bruce")) )
				.doOnNext(usuario ->{
					if( usuario == null){
						throw new RuntimeException("Los nombres no pueden ser vacios");
					}
					System.out.println(usuario.getNombre().concat(usuario.getApellido()));
				})
				.map(usuario -> { String  nombre = usuario.getNombre().toLowerCase();
					usuario.setNombre( nombre);
					return usuario;
				});


		//subscribe
		usuarios.subscribe(e -> log.info(e.toString()),
				error -> log.error(error.getMessage()),
				new Runnable() {
					@Override
					public void run() {
						log.info("Ha finalizado la ejecucion del observable con exito");
					}
				}); // observador, consumidor
	}
}
