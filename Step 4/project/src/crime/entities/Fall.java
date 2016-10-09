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

public class Fall extends Entity {
    private static String eName = "Fälle";

    /*********************************
     * Attributes
     *********************************/

    private Attribute iD = new Attribute("", false, "\\d+");
    private Attribute name = new Attribute("", true, ".+");
    private Attribute eröffnungsdatum = new Attribute("", true, "\\d{1,2}\\.\\d{1,2}\\.\\d{4,4}");
    private Attribute enddatum = new Attribute("", false, "\\d{1,2}\\.\\d{1,2}\\.\\d{4,4}");

    /*********************************
     * Constructor
     *********************************/

    public Fall(List<String> list, Connection c) {
        if (checkInsert(list)) {
            this.iD.setValue(list.get(0));
            this.name.setValue(list.get(1));
            this.eröffnungsdatum.setValue(list.get(2));
            this.enddatum.setValue(list.get(3));

            setPk(new KeyValue("iD", getID()));
            setEntityName(eName);

            insertTupel(c);
            setIsCreated(true);
        } else {
            System.out.println("invalid new Entry");
        }
    }

    public Fall(ResultSet rs) throws SQLException {
        this.iD.setValue(rs.getString(1));
        this.name.setValue(rs.getString(2));
        this.eröffnungsdatum.setValue(rs.getString(3));
        this.enddatum.setValue(rs.getString(4));

        setPk(new KeyValue("iD", getID()));
        setEntityName(eName);
    }

    /*********************************
     * Getter and Setter
     *********************************/

    public String getID() {
        return iD.getValue();
    }
    public boolean setID(String iD) {
        return this.iD.setValue(iD);
    }

    public String getName() {
        return name.getValue();
    }
    public boolean setName(String name) {
        return this.name.setValue(name);
    }

    public String getEnddatum() {
        return enddatum.getValue();
    }
    public boolean setEnddatum(String enddatum) {
        return this.enddatum.setValue(enddatum);
    }

    public String getEröffnungsdatum() {
        return eröffnungsdatum.getValue();
    }
    public boolean setEröffnungsdatum(String eröffnungsdatum) {
        return this.eröffnungsdatum.setValue(eröffnungsdatum);
    }

    /*********************************
     * methods
     *********************************/

    public static ObservableList<Fall> buildData(Connection c, TableView tableView) {
        try {
            ResultSet rs = c.createStatement().executeQuery("SELECT * FROM " + eName + ";");
            return buildDataInner(c, rs, tableView);
        } catch (Exception e) {
            e.printStackTrace();
        }

        return null;
    }

    public static ObservableList<Fall> buildData(Connection c, TableView tableView, String key, String value) {
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

    public static ObservableList<Fall> buildDataInner(Connection c, ResultSet rs, TableView tableView) throws SQLException{
        ObservableList<Fall> data = FXCollections.observableArrayList();

        // create the columns dynamically
        for (int i = 0; i < rs.getMetaData().getColumnCount(); i++) {
            final int j = i;
            TableColumn col = new TableColumn(rs.getMetaData().getColumnName(i+1));
            col.setCellValueFactory(new PropertyValueFactory<Fall, String>(rs.getMetaData().getColumnName(i + 1)));
            col.setCellFactory(TextFieldTableCell.forTableColumn());
            col.setOnEditCommit(
                    new EventHandler<TableColumn.CellEditEvent<Fall, String>>() {
                        @Override
                        public void handle(TableColumn.CellEditEvent<Fall, String> t) {
                            Fall p = t.getRowValue();
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
            Fall p = new Fall(rs);
            data.add(p);
        }

        return data;
    }

    /*********************************
     * methods to be adjusted
     *********************************/

    private void insertTupel(Connection c) {
        try {
            String query = "INSERT INTO " + getEntityName() + " VALUES (null,?,?,?);";
            PreparedStatement ps = c.prepareStatement(query, Statement.RETURN_GENERATED_KEYS);
            ps.setString(1, getName());
            ps.setString(2, getEröffnungsdatum());
            ps.setString(3, getEnddatum());
            ps.executeUpdate();
            ResultSet rs = ps.getGeneratedKeys();
            rs.next();
            setID(rs.getString(1));
            getPk().setValue(getID());
        } catch (SQLException e) {
            e.printStackTrace();
        }

    }

    private boolean checkInsert(List<String> l) {
        if (this.iD.checkRequiredRegex(l.get(0)) &&
                this.name.checkRequiredRegex(l.get(1)) &&
                this.eröffnungsdatum.checkRequiredRegex(l.get(2)) &&
                this.enddatum.checkRequiredRegex(l.get(3))) {
            return true;
        }
        return false;
    }

    public static Collection<VBox> showDetail(Connection c, String key, String value) {
        Collection<VBox> col = FXCollections.observableArrayList();

        col.add(createDetail(c, "Fälle", Fall.class, key, value));
        col.add(createDetail(c, "Verbrechen", Verbrechen.class, "Fall", value));
        col.add(createDetail(c, "Indizien", Indiz.class, "Fall", value));
        col.add(createDetail(c, "Notizen", Notiz.class, "Fall", value));
        col.add(createDetail(c, "PolizistFall", ArbeitetAn.class, "Fall", value));

        return col;
    }

    public static Fall createDummy(Connection c) {
        List<String> l = new ArrayList<>();

        l.add("0");
        l.add("dummy");
        l.add("11.11.1111");
        l.add("11.11.1111");

        return new Fall(l, c);
    }
}
