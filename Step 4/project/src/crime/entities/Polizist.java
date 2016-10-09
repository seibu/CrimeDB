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

public class Polizist extends Entity {
    private static String eName = "Polizisten";

    /*********************************
     * Attributes
     *********************************/

    private Attribute persNr = new Attribute("", true, "\\d+");
    private Attribute dienstgrad = new Attribute("", true, ".+");

    /*********************************
     * Constructor
     *********************************/

    public Polizist(List<String> list, Connection c) {
        if (checkInsert(list)) {
            this.persNr.setValue(list.get(0));
            this.dienstgrad.setValue(list.get(1));

            setPk(new KeyValue("PersNr", getPersNr()));
            setEntityName("Polizisten");

            insertTupel(c);
            setIsCreated(true);
        } else {
            System.out.println("invalid new Entry");
        }
    }

    public Polizist(ResultSet rs) throws SQLException {
        this.persNr.setValue(rs.getString(1));
        this.dienstgrad.setValue(rs.getString(2));

        setPk(new KeyValue("PersNr", getPersNr()));
        setEntityName("Polizisten");
    }

    /*********************************
     * Getter and Setter
     *********************************/

    public String getPersNr() {
        return persNr.getValue();
    }
    public boolean setPersNr(String persNr) {
        return this.persNr.setValue(persNr);
    }

    public String getDienstgrad() {
        return dienstgrad.getValue();
    }
    public boolean setDienstgrad(String dienstgrad) {
        return this.dienstgrad.setValue(dienstgrad);
    }

    /*********************************
     * methods
     *********************************/

    public static ObservableList<Polizist> buildData(Connection c, TableView tableView) {
        try {
            ResultSet rs = c.createStatement().executeQuery("SELECT * FROM " + eName + ";");

            return buildDataInner(c, rs, tableView);
        } catch (Exception e) {
            e.printStackTrace();
        }

        return null;
    }

    public static ObservableList<Polizist> buildData(Connection c, TableView tableView, String key, String value) {
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

    public static ObservableList<Polizist> buildDataInner(Connection c, ResultSet rs, TableView tableView) throws SQLException{
        ObservableList<Polizist> data = FXCollections.observableArrayList();

        // create the columns dynamically
        for (int i = 0; i < rs.getMetaData().getColumnCount(); i++) {
            final int j = i;
            TableColumn col = new TableColumn(rs.getMetaData().getColumnName(i+1));
            col.setCellValueFactory(new PropertyValueFactory<Polizist, String>(rs.getMetaData().getColumnName(i + 1)));
            col.setCellFactory(TextFieldTableCell.forTableColumn());
            col.setOnEditCommit(
                    new EventHandler<TableColumn.CellEditEvent<Polizist, String>>() {
                        @Override
                        public void handle(TableColumn.CellEditEvent<Polizist, String> t) {
                            Polizist p = t.getRowValue();
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
            Polizist p = new Polizist(rs);
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
            ps.setString(1, getPersNr());
            ps.setString(2, getDienstgrad());
            ps.executeUpdate();
        } catch (SQLException e) {
            e.printStackTrace();
        }

    }

    private boolean checkInsert(List<String> l) {
        if (this.persNr.checkRequiredRegex(l.get(0)) &&
                this.dienstgrad.checkRequiredRegex(l.get(1))) {
            return true;
        }
        return false;
    }

    public static Collection<VBox> showDetail(Connection c, String key, String value) {
        Collection<VBox> col = FXCollections.observableArrayList();

        col.add(createDetail(c, "Polizisten", Polizist.class, key, value));
        col.add(createDetail(c, "Personen", Person.class, key, value));
        col.add(createDetail(c, "Notizen", Notiz.class, "Polizist", value));
        col.add(createDetail(c, "Indizien", Indiz.class, "Polizist", value));
        col.add(createDetail(c, "PolizistFall", ArbeitetAn.class, "Polizist", value));
        col.add(createDetail(c, "Zeitraum", Zeitraum.class, "Polizist", value));

        return col;
    }

    public static Polizist createDummy(Connection c) {
        List<String> l = new ArrayList<>();

        l.add("0");
        l.add("dummy");

        return new Polizist(l, c);
    }
}
