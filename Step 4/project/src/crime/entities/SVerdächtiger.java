package crime.entities;

import crime.KeyValue;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.cell.PropertyValueFactory;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

public class SVerdächtiger extends Entity {
    private static String eName = "Verdächtiger";

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

    public SVerdächtiger(ResultSet rs) throws SQLException {
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

    public String getVorname() {
        return vorname.getValue();
    }

    public boolean setVorname(String vorname) {
        return this.vorname.setValue(vorname);
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

    public String getGeburtsdatum() {
        return geburtsdatum.getValue();
    }

    public boolean setGeburtsdatum(String geburtsdatum) {
        return this.geburtsdatum.setValue(geburtsdatum);
    }

    public String getTodesdatum() {
        return todesdatum.getValue();
    }

    public boolean setTodesdatum(String todesdatum) {
        return this.todesdatum.setValue(todesdatum);
    }

    /*********************************
     * methods
     *********************************/

    public static ObservableList<SVerdächtiger> buildData(Connection c, TableView tableView, String value) {
        try {
            String query = "SELECT DISTINCT p.* FROM Personen p" +
                    " INNER JOIN Verdächtiger v ON v.PersNr = p.PersNr" +
                    " WHERE p.Name LIKE ? OR p.Vorname LIKE ?;";

            PreparedStatement ps = c.prepareStatement(query);
            ps.setString(1, "%" + value + "%");
            ps.setString(2, "%" + value + "%");
            ResultSet rs = ps.executeQuery();

            return buildDataInner(c, rs, tableView);
        } catch (Exception e) {
            e.printStackTrace();
        }

        return null;
    }

    public static ObservableList<SVerdächtiger> buildDataInner(Connection c, ResultSet rs, TableView tableView) throws SQLException{
        ObservableList<SVerdächtiger> data = FXCollections.observableArrayList();

        // create the columns dynamically
        for (int i = 0; i < rs.getMetaData().getColumnCount(); i++) {
            final int j = i;
            TableColumn col = new TableColumn(rs.getMetaData().getColumnName(i+1));
            col.setCellValueFactory(new PropertyValueFactory<Person, String>(rs.getMetaData().getColumnName(i + 1)));

            tableView.getColumns().addAll(col);
        }

        while(rs.next()) {
            SVerdächtiger p = new SVerdächtiger(rs);
            data.add(p);
        }

        return data;
    }
}
