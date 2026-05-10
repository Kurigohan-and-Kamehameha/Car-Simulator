package org.example.cargame.persistence.entities;

import jakarta.persistence.*;
import org.example.cargame.enums.EngineType;
import org.example.cargame.persistence.EnergyStorageData;

import java.util.HashMap;
import java.util.Map;

@Entity
@Table(name = "energy_storage_component")
public class EnergyStorageComponentEntity {

    @Id
    private Long id;

    @ElementCollection(fetch = FetchType.EAGER)
    @CollectionTable(name = "energy_storage_component_storages", joinColumns = @JoinColumn(name = "component_id"))
    @MapKeyColumn(name = "engine_type")
    @MapKeyEnumerated(EnumType.STRING)
    private Map<EngineType, EnergyStorageData> storages = new HashMap<>();

    public Long getId() { return id; }
    public void setId(Long id) { this.id = id; }

    public Map<EngineType, EnergyStorageData> getStorages() {
        return storages;
    }

    public void setStorages(Map<EngineType, EnergyStorageData> storages) {
        this.storages = storages;
    }
}
