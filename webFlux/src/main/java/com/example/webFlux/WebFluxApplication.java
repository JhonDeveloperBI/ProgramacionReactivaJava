package com.example.webFlux;

import com.example.webFlux.models.Comentarios;
import com.example.webFlux.models.Usuario;
import com.example.webFlux.models.UsuarioComentarios;
import org.reactivestreams.Subscriber;
import org.reactivestreams.Subscription;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

import java.time.Duration;
import java.util.*;
import java.util.concurrent.CountDownLatch;


@SpringBootApplication
public class WebFluxApplication implements CommandLineRunner {
    private  static final Logger log = LoggerFactory.getLogger(WebFluxApplication.class);

	public static void main(String[] args) {
		SpringApplication.run(WebFluxApplication.class, args);
	}

	@Override
	public void run(String... args) throws Exception {
    // exampleIterable();
	 //exampleFlatMap();
	 //exampleToString();
	//	exampleCollectList();
		//	exampleUserWithComentariosFlatMap();
		//exampleUserWithComentariosZipWith();
		//exampleUserWithComentariosZipWithForm2();
		//exampleZipWithRangos();
		//exampleInterval();
	//	exampleDelayElements();
		//exampleIntervalInfinito();
		//exampleIntervalFromCreated();
		contraPresion();
	}

	// contrapesion backpresure
	public void contraPresion(){

	/*	Flux.range(1,10)
				.log() //traza completa
				.subscribe( new Subscriber<Integer>(){

					private Subscription s;

					private Integer limite = 5;
					private  Integer consumido = 0;

					@Override
					public void onSubscribe(Subscription subscription) {
						this.s = subscription;
						//s.request(Long.MAX_VALUE);
						s.request(limite);
					}

					@Override
					public void onNext(Integer o) {
						log.info(o.toString());
						consumido++;
						if( consumido == limite){
							consumido = 0;
							s.request(limite);
						}
					}

					@Override
					public void onError(Throwable throwable) {

					}

					@Override
					public void onComplete() {

					}
				});

	 */

		Flux.range(1,10)
				.log()
				.limitRate(2)
				.subscribe();
	}

	public void exampleIntervalFromCreated(){
		Flux.create(emitter -> {
			Timer timer = new Timer();
			timer.schedule(new TimerTask() {
				private Integer contador =  0;
				@Override
				public void run() {
					emitter.next(++contador);
					if(contador == 10){
						timer.cancel();
						emitter.complete();
					}

					if( contador == 8){
						timer.cancel();
						emitter.error( new InterruptedException("Error, proceso interrumpido flux 8"));
					}
				}
			}, 1000, 1000);
		})
			//	.doOnNext(next -> log.info(next.toString()))
			//	.doOnComplete(() -> log.info(" Termino !"))
				.subscribe( next -> log.info( next.toString()),
						error -> log.error(error.getMessage()),
				() -> log.info("proceso terminado") );
	}

	public void exampleIntervalInfinito() throws InterruptedException {
		CountDownLatch latch = new CountDownLatch(1);

		Flux.interval(Duration.ofSeconds(1))
				.doOnTerminate(() -> latch.countDown())
				.flatMap(i ->{
					if( i >= 5){
						return Flux.error( new InterruptedException(" Solo hasta 5!"));
					}
					return Flux.just(i);
				})
				.map(i -> "Hola" + i)
				.retry(2) // intenta ejecutarse 3 veces cada vez que ocurre un error
				.subscribe(s -> log.info(s), e -> log.error(e.getMessage()));

		latch.await();
	}

	public void exampleDelayElements(){

		Flux<Integer> rango = Flux.range(1,12)
				.delayElements(Duration.ofSeconds(1))
				.doOnNext( i -> log.info(i.toString()));

		rango.blockLast();
	}

	public void exampleInterval(){
		Flux<Integer> rango = Flux.range(1,12);
		Flux<Long> retraso = Flux.interval(Duration.ofSeconds(1));

		rango.zipWith( retraso, ( ra, re ) -> ra)
				.doOnNext( i -> log.info(i.toString()))
				.blockLast();

	}

	public void exampleZipWithRangos(){
              Flux.just(1,2,3,4)
					  .map( i -> (i *2) )
					  .zipWith(Flux.range(0,4),(uno,dos) -> String.format("Primer Flux: %d, Segundo Flux: %d",uno,dos))
					  .subscribe( texto -> log.info(texto));
	}

	public void exampleUserWithComentariosZipWithForm2(){
		Mono<Usuario> usuarioMono = Mono.fromCallable(()-> new Usuario("John","Doe"));

		Mono<Comentarios> comentariosUsuarioMono = Mono.fromCallable(()->{
			Comentarios comentarios = new Comentarios();
			comentarios.addComentario("comentario 1");
			comentarios.addComentario("comentario 2");
			comentarios.addComentario("comentario 3");

			return comentarios;
		});

		Mono<UsuarioComentarios> usuarioComentariosMono =	usuarioMono
				.zipWith( comentariosUsuarioMono )
						.map( tuple ->{
							Usuario u = tuple.getT1();
							Comentarios c = tuple.getT2();
							return  new UsuarioComentarios(u,c);
						});

		usuarioComentariosMono.subscribe(uc -> log.info(uc.toString()));
	}

	public void exampleUserWithComentariosZipWith(){
		Mono<Usuario> usuarioMono = Mono.fromCallable(()-> new Usuario("John","Doe"));

		Mono<Comentarios> comentariosUsuarioMono = Mono.fromCallable(()->{
			Comentarios comentarios = new Comentarios();
			comentarios.addComentario("comentario 1");
			comentarios.addComentario("comentario 2");
			comentarios.addComentario("comentario 3");

			return comentarios;
		});

	Mono<UsuarioComentarios> usuarioComentariosMono =	usuarioMono
			.zipWith(comentariosUsuarioMono,(usuario, comentariosUsuario ) -> new UsuarioComentarios(usuario,comentariosUsuario) );

	  usuarioComentariosMono.subscribe(uc -> log.info(uc.toString()));
	}


	public void exampleUserWithComentariosFlatMap(){
		Mono<Usuario> usuarioMono = Mono.fromCallable(()-> new Usuario("John","Doe"));

		Mono<Comentarios> comentariosUsuarioMono = Mono.fromCallable(()->{
			Comentarios comentarios = new Comentarios();
			comentarios.addComentario("comentario 1");
			comentarios.addComentario("comentario 2");
			comentarios.addComentario("comentario 3");

			return comentarios;
		});

		usuarioMono.flatMap( u -> comentariosUsuarioMono.map(c -> new UsuarioComentarios(u,c)))
				.subscribe(uc -> log.info(uc.toString()));
	}

	public void exampleCollectList() throws Exception {

		List<Usuario> usuariosList = new ArrayList<>();
		usuariosList.add(new Usuario("Jhon", "Pepito"));
		usuariosList.add(new Usuario("Juan", "Guzman"));
		usuariosList.add(new Usuario("Pedro", "Tal"));
		usuariosList.add(new Usuario("Maria", "Valbuena"));
		usuariosList.add(new Usuario("Andrea", "Ronaldo"));
		usuariosList.add(new Usuario("Bruce", "Lee"));
		usuariosList.add(new Usuario("Bruce", "Willis"));

		Flux.fromIterable(usuariosList)
				.collectList()
				.subscribe(lista -> {
					lista.forEach(item ->
					log.info(item.toString()) );
				});
	}


	public void exampleToString() throws Exception {

		List<Usuario> usuariosList = new ArrayList<>();
		usuariosList.add(new Usuario("Jhon","Pepito"));
		usuariosList.add(new Usuario("Juan","Guzman"));
		usuariosList.add(new Usuario("Pedro","Tal"));
		usuariosList.add(new Usuario("Maria","Valbuena"));
		usuariosList.add(new Usuario("Andrea", "Ronaldo"));
		usuariosList.add(new Usuario("Bruce" ,"Lee"));
		usuariosList.add(new Usuario("Bruce","Willis"));

		Flux.fromIterable(usuariosList)
				.map(usuario -> usuario.getNombre().toUpperCase().concat(" ").concat(usuario.getApellido().toUpperCase()))
				.flatMap(nombre -> {
					if(nombre.contains("bruce".toUpperCase())){
						return Mono.just(nombre);
					}else{
						return Mono.empty();
					}
				}  )
				.map(nombre -> {
					return nombre.toLowerCase();
				})
				.subscribe( u -> log.info(u.toString())); // observador, consumidor
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
