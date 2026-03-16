package org.example.cargameFx.subject;


import org.example.cargameFx.entity.Entity;

import java.util.ArrayList;
import java.util.List;

public class Model {
    private final List<Entity> entityList = new ArrayList<Entity>();

    public void addEntity(Entity entity){
        entityList.add(entity);
    }

    public void removeEntity(Entity entity){
        entityList.remove(entity);
    }

    public List<Entity> getEntityList() {
        return entityList;
    }

}
