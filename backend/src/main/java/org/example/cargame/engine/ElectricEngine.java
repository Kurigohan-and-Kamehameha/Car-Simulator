package org.example.cargame.engine;

import org.example.cargame.enums.EngineType;

public class ElectricEngine implements Engine{
    @Override
    public EngineType getType() {
        return EngineType.ELECTRIC;
    }

    @Override
    public double capacity() {
        return 2000;
    }

}
