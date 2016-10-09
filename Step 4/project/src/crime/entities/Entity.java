package crime.entities;

import crime.Crime;
import crime.KeyValue;
import javafx.collections.ObservableList;
import javafx.scene.control.Label;
import javafx.scene.control.TableView;
import javafx.scene.layout.VBox;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.SQLException;

public abstract class Entity {
    private KeyValue pk = new KeyValue();
    private KeyValue update = new KeyValue();
    private String entityName;
    private boolean isCreated = false;

    public boolean isCreated() {
        return isCreated;
    }
    public void setIsCreated(boolean isCreated) {
        this.isCreated = isCreated;
    }

    public KeyValue getPk() {
        return pk;
    }
    public void setPk(KeyValue pk) {
        this.pk = pk;
    }

    public KeyValue getUpdate() {
        return update;
    }
    public void setUpdate(KeyValue update) {
        this.update = update;
    }

    public String getEntityName() {
        return entityName;
    }
    public void setEntityName(String entityName) {
        this.entityName = entityName;
    }

    public boolean setValueForAttr(String attr, String value, Connection c) {
        try {
            if ((Boolean) getClass().getMethod("set" + attr, String.class).invoke(this, value)) {
                getUpdate().setKey(attr);
                getUpdate().setValue(value);
                updateTupel(c);
            }
            return (Boolean) getClass().getMethod("set" + attr, String.class).invoke(this, value);
        } catch (Exception e) {
            e.printStackTrace();
        }
        return false;
    }

    public int updateTupel(Connection c) throws SQLException {
        String query = "UPDATE " + getEntityName() + " SET " + getUpdate().getKey() + "=? WHERE " + getPk().getKey() + "=?;";

        PreparedStatement ps = c.prepareStatement(query);
        ps.setString(1, getUpdate().getValue());
        ps.setString(2, getPk().getValue());
        int rows = ps.executeUpdate();
        return rows;
    }

    public int deleteTupel(Connection c, Entity e) {
        String query = "DELETE FROM " + getEntityName() + " WHERE " + e.getPk().getKey() + "=?;";
        try {
            PreparedStatement ps = c.prepareStatement(query);
            ps.setString(1, e.getPk().getValue());
            int rows = ps.executeUpdate();
            return rows;
        } catch (SQLException e1) {
            e1.printStackTrace();
        }

        return 0;
    }

    public static VBox createDetail(Connection c, String entity, Class cl, String key, String value) {
        VBox vb = new VBox();
        Label l = new Label(entity);
        TableView tv = new TableView();
        tv.setEditable(true);
        tv.setId(entity);
        tv.setPrefHeight(80);

        tv.setOnMouseClicked(event -> {
            Crime.setDetailEntity(tv);
        });

        try {

            tv.setItems((ObservableList) cl.getMethod("buildData", Connection.class, TableView.class, String.class, String.class).invoke(cl, c, tv, key, value));
        } catch (Exception e) {
            e.printStackTrace();
        }

        vb.getChildren().addAll(l, tv);
        return vb;
    }
}
