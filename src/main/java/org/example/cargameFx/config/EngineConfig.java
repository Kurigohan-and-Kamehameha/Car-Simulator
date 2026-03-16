package org.example.cargameFx.config;

import org.example.cargameFx.engine.ElectricEngine;
import org.example.cargameFx.engine.Engine;
import org.example.cargameFx.engine.FuelEngine;
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
