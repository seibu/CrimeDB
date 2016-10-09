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

public class Behörde extends Entity {
    private static String eName = "Behörden";

    /*********************************
     * Attributes
     *********************************/

    private Attribute iD = new Attribute("", false, "\\d+");
    private Attribute name = new Attribute("", true, ".+");
    private Attribute typ = new Attribute("", true, ".+");
    private Attribute bezirk = new Attribute("", true ,"\\d+");

    /*********************************
     * Constructor
     *********************************/

    public Behörde(List<String> list, Connection c) {
        if (checkInsert(list)) {
            this.iD.setValue(list.get(0));
            this.name.setValue(list.get(1));
            this.typ.setValue(list.get(2));
            this.bezirk.setValue(list.get(3));

            setPk(new KeyValue("ID", getID()));
            setEntityName(eName);

            insertTupel(c);
            setIsCreated(true);
        } else {
            System.out.println("invalid new Entry");
        }
    }

    public Behörde(ResultSet rs) throws SQLException {
        this.iD.setValue(rs.getString(1));
        this.name.setValue(rs.getString(2));
        this.typ.setValue(rs.getString(3));
        this.bezirk.setValue(rs.getString(4));

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

    public String getTyp() {
        return typ.getValue();
    }

    public boolean setTyp(String typ) {
        return this.typ.setValue(typ);
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

    public static ObservableList<Behörde> buildData(Connection c, TableView tableView) {
        try {
            ResultSet rs = c.createStatement().executeQuery("SELECT * FROM " + eName + ";");

            return buildDataInner(c, rs, tableView);
        } catch (Exception e) {
            e.printStackTrace();
        }

        return null;
    }

    public static ObservableList<Behörde> buildData(Connection c, TableView tableView, String key, String value) {
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

    public static ObservableList<Behörde> buildDataInner(Connection c, ResultSet rs, TableView tableView) throws SQLException{
        ObservableList<Behörde> data = FXCollections.observableArrayList();

        // create the columns dynamically
        for (int i = 0; i < rs.getMetaData().getColumnCount(); i++) {
            final int j = i;
            TableColumn col = new TableColumn(rs.getMetaData().getColumnName(i+1));
            col.setCellValueFactory(new PropertyValueFactory<Behörde, String>(rs.getMetaData().getColumnName(i + 1)));
            col.setCellFactory(TextFieldTableCell.forTableColumn());
            col.setOnEditCommit(
                    new EventHandler<TableColumn.CellEditEvent<Behörde, String>>() {
                        @Override
                        public void handle(TableColumn.CellEditEvent<Behörde, String> t) {
                            Behörde p = t.getRowValue();
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
            Behörde p = new Behörde(rs);
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
            ps.setString(2, getTyp());
            ps.setString(3, getBezirk());
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
                this.typ.checkRequiredRegex(l.get(2)) &&
                this.bezirk.checkRequiredRegex(l.get(3))) {
            return true;
        }
        return false;
    }

    public static Collection<VBox> showDetail(Connection c, String key, String value) {
        Collection<VBox> col = FXCollections.observableArrayList();

        VBox vb = createDetail(c, "Behörden", Behörde.class, key, value);
        Behörde v = (Behörde)((TableView)vb.getChildren().get(1)).getItems().get(0);
        col.add(vb);
        col.add(createDetail(c, "Bezirke", Bezirk.class, "ID", v.getBezirk()));

        return col;
    }

    public static Behörde createDummy(Connection c) {
        List<String> l = new ArrayList<>();

        l.add("0");
        l.add("dummy");
        l.add("dummy");
        l.add("0");

        return new Behörde(l, c);
    }
}
