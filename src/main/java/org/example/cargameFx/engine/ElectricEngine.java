package org.example.cargameFx.engine;

import org.example.cargameFx.enums.EngineType;

public class ElectricEngine implements Engine{
    @Override
    public EngineType getType() {
        return EngineType.ELECTRIC;
    }

}
