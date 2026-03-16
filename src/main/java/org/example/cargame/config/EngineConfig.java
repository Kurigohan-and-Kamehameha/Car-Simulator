package org.example.cargame.config;

import org.example.cargame.engine.ElectricEngine;
import org.example.cargame.engine.Engine;
import org.example.cargame.engine.FuelEngine;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class EngineConfig {
    @Bean("FUEL")
    public Engine fuelEngine() {
        return new FuelEngine();
    }

    @Bean("ELECTRIC")
    public Engine electricEngine() {
        return new ElectricEngine();
    }
}
