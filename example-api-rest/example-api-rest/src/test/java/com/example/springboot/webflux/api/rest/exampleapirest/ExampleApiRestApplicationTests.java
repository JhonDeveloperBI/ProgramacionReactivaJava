package com.example.springboot.webflux.api.rest.exampleapirest;

import com.example.springboot.webflux.api.rest.exampleapirest.models.documents.Producto;
import com.example.springboot.webflux.api.rest.exampleapirest.models.services.ProductoService;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.test.web.reactive.server.WebTestClient;
import reactor.core.publisher.Mono;

import java.util.Collections;
import java.util.List;

@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
class ExampleApiRestApplicationTests {

	@Autowired
	private WebTestClient client;

	@Autowired
	private ProductoService productoService;

	@Test
	public void listarTest() {
		client.get()
				.uri("/api/v2/productos")
				.accept(MediaType.APPLICATION_JSON)
				.exchange()
				.expectStatus().isOk()
				.expectHeader().contentType(MediaType.APPLICATION_JSON)
				.expectBodyList(Producto.class)
				.hasSize(9);
	}

	@Test
	public void listar2Test() {
		client.get()
				.uri("/api/v2/productos")
				.accept(MediaType.APPLICATION_JSON)
				.exchange()
				.expectStatus().isOk()
				.expectHeader().contentType(MediaType.APPLICATION_JSON)
				.expectBodyList(Producto.class)
				.consumeWith(response ->{
					List<Producto> productos = response.getResponseBody();
					productos.forEach( p ->{
						System.out.println(p.getNombre());
					});

					Assertions.assertTrue(productos.size() > 0);
				});

	}

	@Test
	public void verTest() {
		Mono<Producto> productoMono = productoService.findByNombre("TV panasonic");

		client.get()
				.uri("/api/v2/productos/{id}", Collections.singletonMap("id",productoMono.block().getId()))
				.accept(MediaType.APPLICATION_JSON)
				.exchange()
				.expectStatus().isOk()
				.expectHeader().contentType(MediaType.APPLICATION_JSON)
				.expectBody()
				.jsonPath("$.id").isNotEmpty()
				.jsonPath("$.nombre").isEqualTo("TV panasonic");

	}

}
