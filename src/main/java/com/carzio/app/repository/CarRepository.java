package com.carzio.app.repository;

import com.carzio.app.models.Car;
import org.springframework.data.mongodb.repository.MongoRepository;

import java.util.List;

public interface CarRepository extends MongoRepository<Car, String> {
    List<Car> findAll();
}
