package crime;

import crime.entities.Entity;

public class View {
    private String name;
    private String entity;
    private KeyValue kv;
    private Entity detail;

    View(String name, String entity, String key, String value) {
        this.name = name;
        this.entity = entity;
        kv = new KeyValue(key, value);
    }

    View(String name, String entity, Entity detail) {
        this.name = name;
        this.entity = entity;
        this.detail = detail;
    }

    public String getName() {
        return name;
    }
    public void setName(String name) {
        this.name = name;
    }

    public String getEntity() {
        return entity;
    }
    public void setEntity(String entity) {
        this.entity = entity;
    }

    public KeyValue getKv() {
        return kv;
    }
    public void setKv(KeyValue kv) {
        this.kv = kv;
    }

    public Entity getDetail() {
        return detail;
    }
    public void setDetail(Entity detail) {
        this.detail = detail;
    }
}
