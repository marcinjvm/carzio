package com.carzio.app.controllers;

import com.carzio.app.models.Car;
import com.carzio.app.services.CarService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import java.util.List;

@RestController
public class CarController {
    private final CarService carService;

    @Autowired
    public CarController(CarService carService) {
        this.carService = carService;
    }

    @GetMapping("/user/cars")
    List<Car> findAllByUserId(@RequestParam("userId") String userId) {
        return carService.findAllByUserId(userId);
    }

    @GetMapping("/cars")
    Car findById(@RequestParam("id") String id) {
        return carService.findById(id);
    }

    @PostMapping("/cars")
    public ResponseEntity<?> createCar(@Valid @RequestBody Car car, @RequestParam("userId") String userId) {
        return carService.createCar(car, userId);
    }

    @PutMapping("/cars")
    public ResponseEntity<?> updateCar(@Valid @RequestBody Car car) {
        return carService.updateCar(car);
    }

    @DeleteMapping("/cars")
    public ResponseEntity<?> deleteCar(@RequestParam("id") String id) {
        return carService.deleteCar(id);
    }
}
