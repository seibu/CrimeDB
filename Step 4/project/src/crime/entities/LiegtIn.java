package crime.entities;

import crime.KeyValue;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.EventHandler;
import javafx.scene.control.Label;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.TextField;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.control.cell.TextFieldTableCell;
import javafx.scene.layout.HBox;
import javafx.scene.layout.VBox;

import java.sql.*;
import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

public class LiegtIn extends Entity {
    private static String eName = "liegt_in";

    /*********************************
     * Attributes
     *********************************/

    private Attribute bezirk = new Attribute("", true, "\\d+");
    private Attribute oberbezirk = new Attribute("", true, "\\d+");

    /*********************************
     * Constructor
     *********************************/

    public LiegtIn(List<String> list, Connection c) {
        if (checkInsert(list)) {
            this.bezirk.setValue(list.get(0));
            this.oberbezirk.setValue(list.get(1));

            setPk(new KeyValue("Bezirk", getBezirk()));
            setEntityName(eName);

            insertTupel(c);
            setIsCreated(true);
        } else {
            System.out.println("invalid new Entry");
        }
    }

    public LiegtIn(ResultSet rs) throws SQLException {
        this.bezirk.setValue(rs.getString(1));
        this.oberbezirk.setValue(rs.getString(2));

        setPk(new KeyValue("Bezirk", getBezirk()));
        setEntityName(eName);
    }

    /*********************************
     * Getter and Setter
     *********************************/

    public String getBezirk() {
        return bezirk.getValue();
    }

    public boolean setBezirk(String bezirk) {
        return this.bezirk.setValue(bezirk);
    }

    public String getOberbezirk() {
        return oberbezirk.getValue();
    }

    public boolean setOberbezirk(String oberbezirk) {
        return this.oberbezirk.setValue(oberbezirk);
    }

    /*********************************
     * methods
     *********************************/

    public static ObservableList<LiegtIn> buildData(Connection c, TableView tableView) {
        try {
            ResultSet rs = c.createStatement().executeQuery("SELECT * FROM " + eName + ";");

            return buildDataInner(c, rs, tableView);
        } catch (Exception e) {
            e.printStackTrace();
        }

        return null;
    }

    public static ObservableList<LiegtIn> buildData(Connection c, TableView tableView, String key, String value) {
        try {
            String query = "SELECT * FROM " + eName + " WHERE " + key + "=?;";
            PreparedStatement ps = c.prepareStatement(query);
            ps.setString(1, value);
            ResultSet rs = ps.executeQuery();

            return buildDataInner(c, rs, tableView);
        } catch (Exception e) {
            e.printStackTrace();
        }

        return null;
    }

    public static ObservableList<LiegtIn> buildDataInner(Connection c, ResultSet rs, TableView tableView) throws SQLException{
        ObservableList<LiegtIn> data = FXCollections.observableArrayList();

        // create the columns dynamically
        for (int i = 0; i < rs.getMetaData().getColumnCount(); i++) {
            final int j = i;
            TableColumn col = new TableColumn(rs.getMetaData().getColumnName(i+1));
            col.setCellValueFactory(new PropertyValueFactory<LiegtIn, String>(rs.getMetaData().getColumnName(i + 1)));
            col.setCellFactory(TextFieldTableCell.forTableColumn());
            col.setOnEditCommit(
                    new EventHandler<TableColumn.CellEditEvent<LiegtIn, String>>() {
                        @Override
                        public void handle(TableColumn.CellEditEvent<LiegtIn, String> t) {
                            LiegtIn p = t.getRowValue();
                            if (p.setValueForAttr(t.getTableColumn().getText(), t.getNewValue(), c)) {

                            } else {
                                t.getTableView().getItems().set(t.getTablePosition().getRow(), p);
                            }
                        }
                    }
            );

            tableView.getColumns().addAll(col);
        }

        while(rs.next()) {
            LiegtIn p = new LiegtIn(rs);
            data.add(p);
        }

        return data;
    }

    /*********************************
     * methods to be adjusted
     *********************************/

    private void insertTupel(Connection c) {
        try {
            String query = "INSERT INTO " + getEntityName() + " VALUES (?,?);";
            PreparedStatement ps = c.prepareStatement(query);
            ps.setString(1, getBezirk());
            ps.setString(2, getOberbezirk());
            ps.executeUpdate();
        } catch (SQLException e) {
            e.printStackTrace();
        }

    }

    private boolean checkInsert(List<String> l) {
        if (this.bezirk.checkRequiredRegex(l.get(0)) &&
                this.oberbezirk.checkRequiredRegex(l.get(1))) {
            return true;
        }
        return false;
    }

    public static Collection<VBox> showDetail(Connection c, String key, String value) {
        Collection<VBox> col = FXCollections.observableArrayList();

        VBox vb = createDetail(c, "BezirkBezirk", LiegtIn.class, key, value);
        LiegtIn v = (LiegtIn)((TableView)vb.getChildren().get(1)).getItems().get(0);
        col.add(vb);
        col.add(createDetail(c, "Bezirk", Bezirk.class, "ID", v.getBezirk()));
        col.add(createDetail(c, "Bezirk", Bezirk.class, "ID", v.getOberbezirk()));

        return col;
    }

    public static LiegtIn createDummy(Connection c) {
        List<String> l = new ArrayList<>();

        l.add("0");
        l.add("0");

        return new LiegtIn(l, c);
    }
}
