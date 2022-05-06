package com.example.webFlux;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import reactor.core.publisher.Flux;



@SpringBootApplication
public class WebFluxApplication implements CommandLineRunner {
    private  static final Logger log = LoggerFactory.getLogger(WebFluxApplication.class);

	public static void main(String[] args) {
		SpringApplication.run(WebFluxApplication.class, args);
	}

	@Override
	public void run(String... args) throws Exception {
		Flux<String> nombres = Flux.just("Jhon","Juan","Pedro","","Andrea")
				.doOnNext(e ->{
					if( e.isEmpty()){
						throw new RuntimeException("Los nombres no pueden ser vacios");
					}
					System.out.println(e);
				});

		//subscribe
		nombres.subscribe(e -> log.info(e),
				error -> log.error( error.getMessage())); // observador, consumidor
	}


}
