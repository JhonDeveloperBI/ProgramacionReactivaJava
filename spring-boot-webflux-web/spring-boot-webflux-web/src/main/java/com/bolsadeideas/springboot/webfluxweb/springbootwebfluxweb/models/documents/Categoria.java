package com.bolsadeideas.springboot.webfluxweb.springbootwebfluxweb.models.documents;

import lombok.Getter;
import lombok.Setter;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;

@Getter
@Setter
@Document(collection = "categorias")
public class Categoria {

    @Id
    private String id;

    private String nombre;

    public Categoria(){

    }

    public Categoria(String nombre) {
        this.nombre = nombre;
    }
}
