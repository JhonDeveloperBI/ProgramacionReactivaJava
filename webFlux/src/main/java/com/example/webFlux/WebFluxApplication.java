package com.example.webFlux;

import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import reactor.core.publisher.Flux;

@SpringBootApplication
public class WebFluxApplication implements CommandLineRunner {

	public static void main(String[] args) {
		SpringApplication.run(WebFluxApplication.class, args);
	}

	@Override
	public void run(String... args) throws Exception {
		Flux<String> nombres = Flux.just("Jhon","Juan","Pedro","Andrea")
				.doOnNext(System.out::println); //reference method
			//	.doOnNext(elemento ->  System.out.println(elemento));

		//subscribe
		nombres.subscribe();
	}


}
