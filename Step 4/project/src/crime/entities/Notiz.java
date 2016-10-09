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

public class Notiz extends Entity {
    private static String eName = "Notizen";

    /*********************************
     * Attributes
     *********************************/

    private Attribute iD = new Attribute("", false, "\\d+");
    private Attribute datum = new Attribute("", true, "\\d{1,2}\\.\\d{1,2}\\.\\d{4,4}");
    private Attribute text = new Attribute("", true, ".+");
    private Attribute polizist = new Attribute("", true, "\\d+");
    private Attribute fall = new Attribute("", true, "\\d+");

    /*********************************
     * Constructor
     *********************************/

    public Notiz(List<String> list, Connection c) {
        if (checkInsert(list)) {
            this.iD.setValue(list.get(0));
            this.datum.setValue(list.get(1));
            this.text.setValue(list.get(2));
            this.polizist.setValue(list.get(3));
            this.fall.setValue(list.get(4));

            setPk(new KeyValue("ID", getID()));
            setEntityName(eName);

            insertTupel(c);
            setIsCreated(true);
        } else {
            System.out.println("invalid new Entry");
        }
    }

    public Notiz(ResultSet rs) throws SQLException {
        this.iD.setValue(rs.getString(1));
        this.datum.setValue(rs.getString(2));
        this.text.setValue(rs.getString(3));
        this.polizist.setValue(rs.getString(4));
        this.fall.setValue(rs.getString(5));

        setPk(new KeyValue("ID", getID()));
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

    public String getDatum() {
        return datum.getValue();
    }

    public boolean setDatum(String datum) {
        return this.datum.setValue(datum);
    }

    public String getText() {
        return text.getValue();
    }

    public boolean setText(String text) {
        return this.text.setValue(text);
    }

    public String getPolizist() {
        return polizist.getValue();
    }

    public boolean setPolizist(String polizist) {
        return this.polizist.setValue(polizist);
    }

    public String getFall() {
        return fall.getValue();
    }

    public boolean setFall(String fall) {
        return this.fall.setValue(fall);
    }

    /*********************************
     * methods
     *********************************/

    public static ObservableList<Notiz> buildData(Connection c, TableView tableView) {
        try {
            ResultSet rs = c.createStatement().executeQuery("SELECT * FROM " + eName + ";");

            return buildDataInner(c, rs, tableView);
        } catch (Exception e) {
            e.printStackTrace();
        }

        return null;
    }

    public static ObservableList<Notiz> buildData(Connection c, TableView tableView, String key, String value) {
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

    public static ObservableList<Notiz> buildDataInner(Connection c, ResultSet rs, TableView tableView) throws SQLException{
        ObservableList<Notiz> data = FXCollections.observableArrayList();

        // create the columns dynamically
        for (int i = 0; i < rs.getMetaData().getColumnCount(); i++) {
            final int j = i;
            TableColumn col = new TableColumn(rs.getMetaData().getColumnName(i+1));
            col.setCellValueFactory(new PropertyValueFactory<Notiz, String>(rs.getMetaData().getColumnName(i + 1)));
            col.setCellFactory(TextFieldTableCell.forTableColumn());
            col.setOnEditCommit(
                    new EventHandler<TableColumn.CellEditEvent<Notiz, String>>() {
                        @Override
                        public void handle(TableColumn.CellEditEvent<Notiz, String> t) {
                            Notiz p = t.getRowValue();
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
            Notiz p = new Notiz(rs);
            data.add(p);
        }

        return data;
    }

    /*********************************
     * methods to be adjusted
     *********************************/

    private void insertTupel(Connection c) {
        try {
            String query = "INSERT INTO " + getEntityName() + " VALUES (null,?,?,?,?);";
            PreparedStatement ps = c.prepareStatement(query, Statement.RETURN_GENERATED_KEYS);
            ps.setString(1, getDatum());
            ps.setString(2, getText());
            ps.setString(3, getPolizist());
            ps.setString(4, getFall());
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
                this.datum.checkRequiredRegex(l.get(1)) &&
                this.text.checkRequiredRegex(l.get(2)) &&
                this.polizist.checkRequiredRegex(l.get(3)) &&
                this.fall.checkRequiredRegex(l.get(4))) {
            return true;
        }
        return false;
    }

    public static Collection<VBox> showDetail(Connection c, String key, String value) {
        Collection<VBox> col = FXCollections.observableArrayList();

        VBox vb = createDetail(c, "Notizen", Notiz.class, key, value);
        Notiz v = (Notiz)((TableView)vb.getChildren().get(1)).getItems().get(0);
        col.add(vb);
        col.add(createDetail(c, "Fälle", Fall.class, "ID", v.getFall()));
        col.add(createDetail(c, "Polizisten", Polizist.class, "PersNr", v.getPolizist()));

        return col;
    }

    public static Notiz createDummy(Connection c) {
        List<String> l = new ArrayList<>();

        l.add("0");
        l.add("11.11.1111");
        l.add("dummy");
        l.add("0");
        l.add("0");

        return new Notiz(l, c);
    }
}
