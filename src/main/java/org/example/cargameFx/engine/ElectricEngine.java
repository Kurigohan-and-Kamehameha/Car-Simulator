package org.example.cargameFx.engine;

import org.example.cargameFx.enums.EngineType;
import org.springframework.stereotype.Component;

public class ElectricEngine implements Engine{
    @Override
    public EngineType getType() {
        return EngineType.ELECTRIC;
    }

}
