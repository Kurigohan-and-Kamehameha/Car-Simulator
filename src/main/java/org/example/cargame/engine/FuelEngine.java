package org.example.cargame.engine;

import org.example.cargame.enums.EngineType;

public class FuelEngine implements Engine{
    @Override
    public EngineType getType() {
        return EngineType.FUEL;
    }
}
