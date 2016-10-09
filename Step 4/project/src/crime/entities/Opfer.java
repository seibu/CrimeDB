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

public class Opfer extends Entity {
    private static String eName = "Opfer";

    /*********************************
     * Attributes
     *********************************/

    private Attribute iD = new Attribute("", false, "\\d+");
    private Attribute persNr = new Attribute("", true, "\\d+");
    private Attribute verbrechen = new Attribute("", true, "\\d+");

    /*********************************
     * Constructor
     *********************************/

    public Opfer(List<String> list, Connection c) {
        if (checkInsert(list)) {
            this.iD.setValue(list.get(0));
            this.persNr.setValue(list.get(1));
            this.verbrechen.setValue(list.get(2));

            setPk(new KeyValue("ID", getID()));
            setEntityName(eName);

            insertTupel(c);
            setIsCreated(true);
        } else {
            System.out.println("invalid new Entry");
        }
    }

    public Opfer(ResultSet rs) throws SQLException {
        this.iD.setValue(rs.getString(1));
        this.persNr.setValue(rs.getString(2));
        this.verbrechen.setValue(rs.getString(3));

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

    public String getPersNr() {
        return persNr.getValue();
    }

    public boolean setPersNr(String persNr) {
        return this.persNr.setValue(persNr);
    }

    public String getVerbrechen() {
        return verbrechen.getValue();
    }

    public boolean setVerbrechen(String verbrechen) {
        return this.verbrechen.setValue(verbrechen);
    }

    /*********************************
     * methods
     *********************************/

    public static ObservableList<Opfer> buildData(Connection c, TableView tableView) {
        try {
            ResultSet rs = c.createStatement().executeQuery("SELECT * FROM " + eName + ";");

            return buildDataInner(c, rs, tableView);
        } catch (Exception e) {
            e.printStackTrace();
        }

        return null;
    }

    public static ObservableList<Opfer> buildData(Connection c, TableView tableView, String key, String value) {
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

    public static ObservableList<Opfer> buildDataInner(Connection c, ResultSet rs, TableView tableView) throws SQLException{
        ObservableList<Opfer> data = FXCollections.observableArrayList();

        // create the columns dynamically
        for (int i = 0; i < rs.getMetaData().getColumnCount(); i++) {
            final int j = i;
            TableColumn col = new TableColumn(rs.getMetaData().getColumnName(i+1));
            col.setCellValueFactory(new PropertyValueFactory<Opfer, String>(rs.getMetaData().getColumnName(i + 1)));
            col.setCellFactory(TextFieldTableCell.forTableColumn());
            col.setOnEditCommit(
                    new EventHandler<TableColumn.CellEditEvent<Opfer, String>>() {
                        @Override
                        public void handle(TableColumn.CellEditEvent<Opfer, String> t) {
                            Opfer p = t.getRowValue();
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
            Opfer p = new Opfer(rs);
            data.add(p);
        }

        return data;
    }

    /*********************************
     * methods to be adjusted
     *********************************/

    private void insertTupel(Connection c) {
        try {
            String query = "INSERT INTO " + getEntityName() + " VALUES (null,?,?);";
            PreparedStatement ps = c.prepareStatement(query, Statement.RETURN_GENERATED_KEYS);
            ps.setString(1, getPersNr());
            ps.setString(2, getVerbrechen());
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
                this.persNr.checkRequiredRegex(l.get(1)) &&
                this.verbrechen.checkRequiredRegex(l.get(2))) {
            return true;
        }
        return false;
    }

    public static Collection<VBox> showDetail(Connection c, String key, String value) {
        Collection<VBox> col = FXCollections.observableArrayList();

        VBox vb = createDetail(c, "Opfer", Opfer.class, key, value);
        Opfer v = (Opfer)((TableView)vb.getChildren().get(1)).getItems().get(0);
        col.add(vb);
        col.add(createDetail(c, "Personen", Person.class, "PersNr", v.getPersNr()));
        col.add(createDetail(c, "Verbrechen", Verbrechen.class, "ID", v.getVerbrechen()));

        return col;
    }

    public static Opfer createDummy(Connection c) {
        List<String> l = new ArrayList<>();

        l.add("0");
        l.add("0");
        l.add("0");

        return new Opfer(l, c);
    }
}
