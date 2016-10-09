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

public class ArbeitetAn extends Entity {
    private static String eName = "arbeitet_an";

    /*********************************
     * Attributes
     *********************************/

    private Attribute iD = new Attribute("", false, "\\d+");
    private Attribute polizist = new Attribute("", true, "\\d+");
    private Attribute fall = new Attribute("", true, "\\d+");
    private Attribute von = new Attribute("", true, "\\d{1,2}\\.\\d{1,2}\\.\\d{4,4}");
    private Attribute bis = new Attribute("", false, "\\d{1,2}\\.\\d{1,2}\\.\\d{4,4}");

    /*********************************
     * Constructor
     *********************************/

    public ArbeitetAn(List<String> list, Connection c) {
        if (checkInsert(list)) {
            this.iD.setValue(list.get(0));
            this.polizist.setValue(list.get(1));
            this.fall.setValue(list.get(2));
            this.von.setValue(list.get(3));
            this.bis.setValue(list.get(4));

            setPk(new KeyValue("ID", getID()));
            setEntityName(eName);

            insertTupel(c);
            setIsCreated(true);
        } else {
            System.out.println("invalid new Entry");
        }
    }

    public ArbeitetAn(ResultSet rs) throws SQLException {
        this.iD.setValue(rs.getString(1));
        this.polizist.setValue(rs.getString(2));
        this.fall.setValue(rs.getString(3));
        this.von.setValue(rs.getString(4));
        this.bis.setValue(rs.getString(5));

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

    public String getVon() {
        return von.getValue();
    }

    public boolean setVon(String von) {
        return this.von.setValue(von);
    }

    public String getBis() {
        return bis.getValue();
    }

    public boolean setBis(String bis) {
        return this.bis.setValue(bis);
    }

    /*********************************
     * methods
     *********************************/

    public static ObservableList<ArbeitetAn> buildData(Connection c, TableView tableView) {
        try {
            ResultSet rs = c.createStatement().executeQuery("SELECT * FROM " + eName + ";");

            return buildDataInner(c, rs, tableView);
        } catch (Exception e) {
            e.printStackTrace();
        }

        return null;
    }

    public static ObservableList<ArbeitetAn> buildData(Connection c, TableView tableView, String key, String value) {
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

    public static ObservableList<ArbeitetAn> buildDataInner(Connection c, ResultSet rs, TableView tableView) throws SQLException{
        ObservableList<ArbeitetAn> data = FXCollections.observableArrayList();

        // create the columns dynamically
        for (int i = 0; i < rs.getMetaData().getColumnCount(); i++) {
            final int j = i;
            TableColumn col = new TableColumn(rs.getMetaData().getColumnName(i+1));
            col.setCellValueFactory(new PropertyValueFactory<ArbeitetAn, String>(rs.getMetaData().getColumnName(i + 1)));
            col.setCellFactory(TextFieldTableCell.forTableColumn());
            col.setOnEditCommit(
                    new EventHandler<TableColumn.CellEditEvent<ArbeitetAn, String>>() {
                        @Override
                        public void handle(TableColumn.CellEditEvent<ArbeitetAn, String> t) {
                            ArbeitetAn p = t.getRowValue();
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
            ArbeitetAn p = new ArbeitetAn(rs);
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
            ps.setString(1, getPolizist());
            ps.setString(2, getFall());
            ps.setString(3, getVon());
            ps.setString(4, getBis());
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
                this.polizist.checkRequiredRegex(l.get(1)) &&
                this.fall.checkRequiredRegex(l.get(2)) &&
                this.von.checkRequiredRegex(l.get(3)) &&
                this.bis.checkRequiredRegex(l.get(4))) {
            return true;
        }
        return false;
    }

    public static Collection<VBox> showDetail(Connection c, String key, String value) {
        Collection<VBox> col = FXCollections.observableArrayList();

        VBox vb = createDetail(c, "PolizistFall", ArbeitetAn.class, key, value);
        ArbeitetAn v = (ArbeitetAn)((TableView)vb.getChildren().get(1)).getItems().get(0);
        col.add(vb);
        col.add(createDetail(c, "Polizisten", Polizist.class, "PersNr", v.getPolizist()));
        col.add(createDetail(c, "Fälle", Fall.class, "ID", v.getFall()));

        return col;
    }

    public static ArbeitetAn createDummy(Connection c) {
        List<String> l = new ArrayList<>();

        l.add("0");
        l.add("0");
        l.add("0");
        l.add("11.11.1111");
        l.add("11.11.1111");

        return new ArbeitetAn(l, c);
    }
}
