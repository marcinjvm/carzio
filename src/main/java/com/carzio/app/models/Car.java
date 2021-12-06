package com.carzio.app.models;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.DBRef;
import org.springframework.data.mongodb.core.mapping.Document;
import org.springframework.format.annotation.NumberFormat;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.Size;
import java.util.Date;
import java.util.HashSet;
import java.util.Set;

@Getter
@Setter
@NoArgsConstructor
@Document(collection = "cars")
public class Car {
    @Id
    private String id;

    @NotBlank
    @Size(max = 20)
    private String brand;

    @NotBlank
    @Size(max = 20)
    private String model;

    @NumberFormat
    private Integer year;

    @NumberFormat
    private Float mileage;

    private Engine engine;

    @DBRef
    private Set<Repair> repairs = new HashSet<>();

    @DBRef
    private Set<Refueling> refuelings = new HashSet<>();

    private Date deleteDate;

    public Car(@NotBlank @Size(max = 20) String brand, @NotBlank @Size(max = 20) String model, @NotBlank Integer year, @NotBlank Float mileage, @NotBlank String engine) {
        this.brand = brand;
        this.model = model;
        this.year = year;
        this.mileage = mileage;
        this.engine = Engine.valueOf(engine);
    }

    public Car(@NotBlank @Size(max = 20) String brand, @NotBlank @Size(max = 20) String model, @NotBlank Integer year, @NotBlank Float mileage, Engine engine) {
        this.brand = brand;
        this.model = model;
        this.year = year;
        this.mileage = mileage;
        this.engine = engine;
    }

    public Car(@NotBlank String id, @NotBlank @Size(max = 20) String brand, @NotBlank @Size(max = 20) String model, @NotBlank Integer year, @NotBlank Float mileage, Engine engine) {
        this.id = id;
        this.brand = brand;
        this.model = model;
        this.year = year;
        this.mileage = mileage;
        this.engine = engine;
    }
}
