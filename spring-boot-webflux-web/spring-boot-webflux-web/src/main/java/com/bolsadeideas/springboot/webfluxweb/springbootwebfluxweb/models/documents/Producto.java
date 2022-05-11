package com.bolsadeideas.springboot.webfluxweb.springbootwebfluxweb.models.documents;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;

import java.util.Date;

@Getter
@Setter
@AllArgsConstructor
@Document(collection = "productos")
public class Producto {

    @Id
    private String id;

    private  String nombre;
    private  Double precio;
    private Date createAt;


    public Producto(String nombre, Double precio){
        this.nombre = nombre;
        this.precio = precio;
    }

}
