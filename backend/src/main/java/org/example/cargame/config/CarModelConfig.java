package org.example.cargame.config;

import org.example.cargame.CarModel;
import org.example.cargame.factories.CarFactory;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class CarModelConfig {
    CarFactory carFactory;
    public CarModelConfig(CarFactory carFactory){
        this.carFactory = carFactory;
    }

    @Bean
    public CarModel createModel() {
        CarModel model = new CarModel();
        //carFactory.createCar(model, "A");

        //carFactory.createCar2(model);
        System.out.println(model.getAllEntities());
        return model;
    }
}
