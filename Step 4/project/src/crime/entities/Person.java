package crime.entities;

import crime.KeyValue;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.EventHandler;
import javafx.scene.control.*;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.control.cell.TextFieldTableCell;
import javafx.scene.layout.HBox;
import javafx.scene.layout.VBox;

import java.sql.*;
import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

public class Person extends Entity {
    private static String eName = "Personen";

    /*********************************
     * Attributes
     *********************************/

    private Attribute persNr = new Attribute("", false, "\\d+");
    private Attribute name = new Attribute("", true, ".+");
    private Attribute vorname = new Attribute("", true, ".+");
    private Attribute nationalität = new Attribute("", true ,".+");
    private Attribute geschlecht = new Attribute("", true, "[m|f]");
    private Attribute geburtsdatum = new Attribute("", true, "\\d{1,2}\\.\\d{1,2}\\.\\d{4,4}");
    private Attribute todesdatum = new Attribute("", false, "\\d{1,2}\\.\\d{1,2}\\.\\d{4,4}");

    /*********************************
     * Constructor
     *********************************/

    public Person(List<String> list, Connection c) {
        if (checkInsert(list)) {
            this.persNr.setValue(list.get(0));
            this.name.setValue(list.get(1));
            this.vorname.setValue(list.get(2));
            this.nationalität.setValue(list.get(3));
            this.geschlecht.setValue(list.get(4));
            this.geburtsdatum.setValue(list.get(5));
            this.todesdatum.setValue(list.get(6));

            setPk(new KeyValue("PersNr", getPersNr()));
            setEntityName(eName);

            insertTupel(c);
            setIsCreated(true);
        } else {
            System.out.println("invalid new Entry");
        }
    }

    public Person(ResultSet rs) throws SQLException {
        this.persNr.setValue(rs.getString(1));
        this.name.setValue(rs.getString(2));
        this.vorname.setValue(rs.getString(3));
        this.nationalität.setValue(rs.getString(4));
        this.geschlecht.setValue(rs.getString(5));
        this.geburtsdatum.setValue(rs.getString(6));
        this.todesdatum.setValue(rs.getString(7));

        setPk(new KeyValue("PersNr", getPersNr()));
        setEntityName(eName);
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

    public String getName() {
        return name.getValue();
    }
    public boolean setName(String name) {
        return this.name.setValue(name);
    }

    public String getTodesdatum() {
        return todesdatum.getValue();
    }
    public boolean setTodesdatum(String todesdatum) {
        return this.todesdatum.setValue(todesdatum);
    }

    public String getGeburtsdatum() {
        return geburtsdatum.getValue();
    }
    public boolean setGeburtsdatum(String geburtsdatum) {
        return this.geburtsdatum.setValue(geburtsdatum);
    }

    public String getNationalität() {
        return nationalität.getValue();
    }
    public boolean setNationalität(String nationalität) {
        return this.nationalität.setValue(nationalität);
    }

    public String getGeschlecht() {
        return geschlecht.getValue();
    }
    public boolean setGeschlecht(String geschlecht) {
        return this.geschlecht.setValue(geschlecht);
    }

    public String getVorname() {
        return vorname.getValue();
    }
    public boolean setVorname(String vorname) {
        return this.vorname.setValue(vorname);
    }

    /*********************************
     * methods
     *********************************/

    public static ObservableList<Person> buildData(Connection c, TableView tableView) {
        try {
            ResultSet rs = c.createStatement().executeQuery("SELECT * FROM " + eName + ";");

            return buildDataInner(c, rs, tableView);
        } catch (Exception e) {
            e.printStackTrace();
        }

        return null;
    }

    public static ObservableList<Person> buildData(Connection c, TableView tableView, String key, String value) {
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

    public static ObservableList<Person> buildDataInner(Connection c, ResultSet rs, TableView tableView) throws SQLException{
        ObservableList<Person> data = FXCollections.observableArrayList();

        // create the columns dynamically
        for (int i = 0; i < rs.getMetaData().getColumnCount(); i++) {
            final int j = i;
            TableColumn col = new TableColumn(rs.getMetaData().getColumnName(i+1));
            col.setCellValueFactory(new PropertyValueFactory<Person, String>(rs.getMetaData().getColumnName(i + 1)));
            col.setCellFactory(TextFieldTableCell.forTableColumn());
            col.setOnEditCommit(
                    new EventHandler<TableColumn.CellEditEvent<Person, String>>() {
                        @Override
                        public void handle(TableColumn.CellEditEvent<Person, String> t) {
                            Person p = t.getRowValue();
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
            Person p = new Person(rs);
            data.add(p);
        }

        return data;
    }

    /*********************************
     * methods to be adjusted
     *********************************/

    private void insertTupel(Connection c) {
        try {
            String query = "INSERT INTO " + getEntityName() + " VALUES (null,?,?,?,?,?,?);";
            PreparedStatement ps = c.prepareStatement(query, Statement.RETURN_GENERATED_KEYS);
            ps.setString(1, getName());
            ps.setString(2, getVorname());
            ps.setString(3, getNationalität());
            ps.setString(4, getGeschlecht());
            ps.setString(5, getGeburtsdatum());
            ps.setString(6, getTodesdatum());
            ps.executeUpdate();
            ResultSet rs = ps.getGeneratedKeys();
            rs.next();
            setPersNr(rs.getString(1));
            getPk().setValue(getPersNr());
        } catch (SQLException e) {
            e.printStackTrace();
        }

    }

    private boolean checkInsert(List<String> l) {
        if (this.persNr.checkRequiredRegex(l.get(0)) &&
                this.name.checkRequiredRegex(l.get(1)) &&
                this.vorname.checkRequiredRegex(l.get(2)) &&
                this.nationalität.checkRequiredRegex(l.get(3)) &&
                this.geschlecht.checkRequiredRegex(l.get(4)) &&
                this.geburtsdatum.checkRequiredRegex(l.get(5)) &&
                this.todesdatum.checkRequiredRegex(l.get(6))) {
            return true;
        }
        return false;
    }

    public static Collection<VBox> showDetail(Connection c, String key, String value) {
        Collection<VBox> col = FXCollections.observableArrayList();

        col.add(createDetail(c, "Personen", Person.class, key, value));
        col.add(createDetail(c, "Polizisten", Polizist.class, key, value));
        col.add(createDetail(c, "Verdächtiger", Verdächtiger.class, key, value));
        col.add(createDetail(c, "Opfer", Opfer.class, key, value));

        return col;
    }

    public static Person createDummy(Connection c) {
        List<String> l = new ArrayList<>();

        l.add("1");
        l.add("dummy");
        l.add("dummy");
        l.add("dummy");
        l.add("m");
        l.add("11.11.1111");
        l.add("");

        return new Person(l, c);
    }
}
