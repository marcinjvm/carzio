package com.carzio.app.services;

import com.carzio.app.exceptions.ErrCode;
import com.carzio.app.models.Car;
import com.carzio.app.models.User;
import com.carzio.app.payload.response.MessageResponse;
import com.carzio.app.repository.CarRepository;
import com.carzio.app.repository.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;

import java.util.*;

@Service
public class CarService {
    private final CarRepository carRepository;
    private final UserRepository userRepository;

    @Autowired
    public CarService(CarRepository carRepository, UserRepository userRepository) {
        this.carRepository = carRepository;
        this.userRepository = userRepository;
    }

    public List<Car> findAllByUserId(String userId) {
        Optional<User> user = userRepository.findById(userId);
        if (!user.isPresent()) {
            throw new RuntimeException(ErrCode.CARS_OF_USER_OF_ID_NOT_FOUND
                    .getMessage(userId, Locale.ENGLISH));
        }
        return new ArrayList<>(user.get().getCars());
    }

    public Car findById(String id) {
        return carRepository.findById(id)
                .orElseThrow(() ->
                        new RuntimeException(ErrCode.CAR_OF_ID_NOT_FOUND
                                .getMessage(id, Locale.ENGLISH))
                );
    }

    public ResponseEntity<?> createCar(Car car, String userId) {
        Optional<User> user = userRepository.findById(userId);
        if (!user.isPresent()) {
            throw new RuntimeException(ErrCode.CARS_OF_USER_OF_ID_NOT_FOUND
                    .getMessage(userId, Locale.ENGLISH));
        }
        Car newCar = new Car(car.getBrand(), car.getModel(), car.getYear(), car.getMileage(), car.getEngine());
        save(newCar);
        User user1 = user.get();
        user1.getCars().add(newCar);
        userRepository.save(user1);
        return new ResponseEntity(new MessageResponse("Created"), HttpStatus.CREATED);
    }

    public ResponseEntity<?> updateCar(Car car) {
        Car carFoundById = findById(car.getId());
        carFoundById.setBrand(car.getBrand());
        carFoundById.setModel(car.getModel());
        carFoundById.setYear(car.getYear());
        carFoundById.setMileage(car.getMileage());
        carFoundById.setEngine(car.getEngine());
        save(carFoundById);
        return new ResponseEntity(new MessageResponse("Updated"), HttpStatus.OK);
    }

    public ResponseEntity<?> deleteCar(String id) {
        Car car = findById(id);
        car.setDeleteDate(Calendar.getInstance().getTime());
        carRepository.save(car);
        return new ResponseEntity(new MessageResponse("Deleted"), HttpStatus.OK);
    }

    public void save(Car car) {
        carRepository.save(car);
    }
}
