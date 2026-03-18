package org.example.cargameFx.config;

import org.example.cargameFx.factories.CarFactory;
import org.example.cargameFx.subject.Model;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class ModelConfig {

    @Bean
    public Model model(CarFactory carFactory){
        Model model = new Model();
        model.addEntity(carFactory.createCar());
        return model;
    }
}
