package crime.entities;

import crime.KeyValue;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.EventHandler;
import javafx.geometry.Insets;
import javafx.scene.control.*;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.control.cell.TextFieldTableCell;
import javafx.scene.layout.HBox;
import javafx.scene.layout.VBox;

import java.sql.*;
import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

public class Verbrechen extends Entity {
    private static String eName = "Verbrechen";

    /*********************************
     * Attributes
     *********************************/

    private Attribute iD = new Attribute("", false, "\\d+");
    private Attribute name = new Attribute("", true, ".+");
    private Attribute datum = new Attribute("", false, "\\d{1,2}\\.\\d{1,2}\\.\\d{4,4}");
    private Attribute fall = new Attribute("", true, "\\d+");
    private Attribute art = new Attribute("", true, "\\d+");
    private Attribute bezirk = new Attribute("", true, "\\d+");

    /*********************************
     * Constructor
     *********************************/

    public Verbrechen(List<String> list, Connection c) {
        if (checkInsert(list)) {
            this.iD.setValue(list.get(0));
            this.name.setValue(list.get(1));
            this.datum.setValue(list.get(2));
            this.fall.setValue(list.get(3));
            this.art.setValue(list.get(4));
            this.bezirk.setValue(list.get(5));

            setPk(new KeyValue("ID", getID()));
            setEntityName(eName);

            insertTupel(c);
            setIsCreated(true);
        } else {
            System.out.println("invalid new Entry");
        }
    }

    public Verbrechen(ResultSet rs) throws SQLException {
        this.iD.setValue(rs.getString(1));
        this.name.setValue(rs.getString(2));
        this.datum.setValue(rs.getString(3));
        this.fall.setValue(rs.getString(4));
        this.art.setValue(rs.getString(5));
        this.bezirk.setValue(rs.getString(6));

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

    public String getName() {
        return name.getValue();
    }

    public boolean setName(String name) {
        return this.name.setValue(name);
    }

    public String getDatum() {
        return datum.getValue();
    }

    public boolean setDatum(String datum) {
        return this.datum.setValue(datum);
    }

    public String getFall() {
        return fall.getValue();
    }

    public boolean setFall(String fall) {
        return this.fall.setValue(fall);
    }

    public String getArt() {
        return art.getValue();
    }

    public boolean setArt(String art) {
        return this.art.setValue(art);
    }

    public String getBezirk() {
        return bezirk.getValue();
    }

    public boolean setBezirk(String bezirk) {
        return this.bezirk.setValue(bezirk);
    }

    /*********************************
     * methods
     *********************************/

    public static ObservableList<Verbrechen> buildData(Connection c, TableView tableView) {
        try {
            ResultSet rs = c.createStatement().executeQuery("SELECT * FROM " + eName + ";");

            return buildDataInner(c, rs, tableView);
        } catch (Exception e) {
            e.printStackTrace();
        }

        return null;
    }

    public static ObservableList<Verbrechen> buildData(Connection c, TableView tableView, String key, String value) {
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

    private static ObservableList<Verbrechen> buildData(Connection c, TableView tableView, List<KeyValue> kvs) {
        try {
            String query = "SELECT * FROM " + eName + " WHERE";

            for (int i = 0; i < kvs.size(); i++) {
                if (i > 0) {
                    query += " AND";
                }
                query += " " + kvs.get(i).getKey() + "=?";
            }
            query += ";";

            PreparedStatement ps = c.prepareStatement(query);

            for (int i = 0; i < kvs.size(); i++) {
                ps.setString(i + 1, kvs.get(i).getValue());
            }

            ResultSet rs = ps.executeQuery();

            return buildDataInner(c, rs, tableView);
        } catch (Exception e) {
            e.printStackTrace();
        }

        return null;
    }

    public static ObservableList<Verbrechen> buildDataInner(Connection c, ResultSet rs, TableView tableView) throws SQLException{
        ObservableList<Verbrechen> data = FXCollections.observableArrayList();

        // create the columns dynamically
        for (int i = 0; i < rs.getMetaData().getColumnCount(); i++) {
            final int j = i;
            TableColumn col = new TableColumn(rs.getMetaData().getColumnName(i+1));
            col.setCellValueFactory(new PropertyValueFactory<Verbrechen, String>(rs.getMetaData().getColumnName(i + 1)));
            col.setCellFactory(TextFieldTableCell.forTableColumn());
            col.setOnEditCommit(
                    new EventHandler<TableColumn.CellEditEvent<Verbrechen, String>>() {
                        @Override
                        public void handle(TableColumn.CellEditEvent<Verbrechen, String> t) {
                            Verbrechen p = t.getRowValue();
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
            Verbrechen p = new Verbrechen(rs);
            data.add(p);
        }

        return data;
    }

    /*********************************
     * methods to be adjusted
     *********************************/

    private void insertTupel(Connection c) {
        try {
            String query = "INSERT INTO " + getEntityName() + " VALUES (null,?,?,?,?,?);";
            PreparedStatement ps = c.prepareStatement(query, Statement.RETURN_GENERATED_KEYS);
            ps.setString(1, getName());
            ps.setString(2, getDatum());
            ps.setString(3, getFall());
            ps.setString(4, getArt());
            ps.setString(5, getBezirk());
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
                this.datum.checkRequiredRegex(l.get(2)) &&
                this.fall.checkRequiredRegex(l.get(3)) &&
                this.art.checkRequiredRegex(l.get(4)) &&
                this.bezirk.checkRequiredRegex(l.get(5))) {
            return true;
        }
        return false;
    }

    public static Collection<VBox> showDetail(Connection c, String key, String value) {
        Collection<VBox> col = FXCollections.observableArrayList();

        VBox vb = createDetail(c, "Verbrechen", Verbrechen.class, key, value);
        Verbrechen v = (Verbrechen)((TableView)vb.getChildren().get(1)).getItems().get(0);
        col.add(vb);

        col.add(createDetail(c, "Verdächtiger", Verdächtiger.class, "Verbrechen", value));
        col.add(createDetail(c, "Opfer", Opfer.class, "Verbrechen", value));
        col.add(createDetail(c, "Fälle", Fall.class, "ID", v.getFall()));
        col.add(createDetail(c, "Arten", Art.class, "ID", v.getArt()));
        col.add(createDetail(c, "Bezirke", Bezirk.class, "ID", v.getBezirk()));

        return col;
    }

    public static Verbrechen createDummy(Connection c) {
        List<String> l = new ArrayList<>();

        l.add("0");
        l.add("dummy");
        l.add("11.11.1111");
        l.add("0");
        l.add("0");
        l.add("0");

        return new Verbrechen(l, c);
    }

    public static void createFilter(HBox hBox, Connection c, TableView tv) {
        ComboBox<String> art = new ComboBox();
        ComboBox<String> bezirk = new ComboBox();

        Label l1 = new Label("Art:");
        art.setOnAction(event -> {
                    String value1 = art.getSelectionModel().getSelectedItem();
                    String value2 = bezirk.getSelectionModel().getSelectedItem();
                    tv.getItems().clear();
                    tv.getColumns().clear();
                    if ("-- kein --".equals(value1) && "-- kein --".equals(value2)) {
                        tv.setItems(Verbrechen.buildData(c, tv));
                    } else {
                        List<KeyValue> kvs = new ArrayList<>();
                        if (!"-- kein --".equals(value1)) {
                            kvs.add(new KeyValue("Art", value1));
                        }
                        if (!"-- kein --".equals(value2)) {
                            kvs.add(new KeyValue("Bezirk", value2));
                        }
                        tv.setItems(Verbrechen.buildData(c, tv, kvs));
                    }
                }
        );
        art.getItems().add("-- kein --");
        art.getSelectionModel().select(0);

        Label l2 = new Label("Bezirk:");
        bezirk.setOnAction(event -> {
                    String value1 = bezirk.getSelectionModel().getSelectedItem();
                    String value2 = art.getSelectionModel().getSelectedItem();
                    tv.getItems().clear();
                    tv.getColumns().clear();
                    if ("-- kein --".equals(value1) && "-- kein --".equals(value2)) {
                        tv.setItems(Verbrechen.buildData(c, tv));
                    } else {
                        List<KeyValue> kvs = new ArrayList<>();
                        if (!"-- kein --".equals(value1)) {
                            kvs.add(new KeyValue("Bezirk", value1));
                        }
                        if (!"-- kein --".equals(value2)) {
                            kvs.add(new KeyValue("Art", value2));
                        }
                        tv.setItems(Verbrechen.buildData(c, tv, kvs));
                    }
                }
        );
        bezirk.getItems().add("-- kein --");
        bezirk.getSelectionModel().select(0);

        try {
            PreparedStatement ps = c.prepareStatement("SELECT DISTINCT Art FROM Verbrechen;");
            ResultSet rs = ps.executeQuery();
            while (rs.next()) {
                art.getItems().add(rs.getString(1));
            }

            PreparedStatement ps2 = c.prepareStatement("SELECT DISTINCT Bezirk FROM Verbrechen;");
            ResultSet rs2 = ps2.executeQuery();
            while (rs2.next()) {
                bezirk.getItems().add(rs2.getString(1));
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }

        HBox.setMargin(l1, new Insets(0, 5, 0, 20));
        HBox.setMargin(l2, new Insets(0, 5, 0, 20));
        hBox.getChildren().addAll(l1, art, l2, bezirk);
    }
}
