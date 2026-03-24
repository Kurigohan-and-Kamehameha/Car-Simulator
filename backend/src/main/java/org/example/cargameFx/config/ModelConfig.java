package org.example.cargameFx.config;

import org.example.cargameFx.Model;
import org.example.cargameFx.factories.CarFactory;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class ModelConfig {
    private final CarFactory carFactory;

    public ModelConfig(CarFactory carFactory){
        this.carFactory = carFactory;
    }

    @Bean
    public Model createModel() {
        Model model = new Model();
        carFactory.createCar(model);
        return model;
    }
}
