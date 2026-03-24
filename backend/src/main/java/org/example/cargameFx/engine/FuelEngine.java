package org.example.cargameFx.engine;

import org.example.cargameFx.enums.EngineType;

public class FuelEngine implements Engine{
    @Override
    public EngineType getType() {
        return EngineType.FUEL;
    }

    @Override
    public double capacity() {
        return 4000;
    }
}
