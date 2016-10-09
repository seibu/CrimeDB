package crime.entities;

import crime.KeyValue;
import crime.Main;
import javafx.beans.property.SimpleStringProperty;
import javafx.beans.property.StringProperty;
import javafx.beans.value.ObservableValue;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.EventHandler;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.control.*;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.control.cell.TextFieldTableCell;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.HBox;
import javafx.scene.layout.VBox;
import javafx.stage.FileChooser;
import javafx.stage.Stage;

import javax.imageio.ImageIO;
import javax.swing.*;
import java.io.*;
import java.nio.file.Files;
import java.nio.file.Path;
import java.sql.*;
import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

public class Indiz extends Entity {
    private static String eName = "Indizien";

    private static File selectedFile = null;

    private static StringProperty selectedImage = new SimpleStringProperty();

    /*********************************
     * Attributes
     *********************************/

    private Attribute iD = new Attribute("", false, "\\d+");
    private Attribute anlagedatum = new Attribute("", true, "\\d{1,2}\\.\\d{1,2}\\.\\d{4,4}");
    private Attribute text = new Attribute("", false, ".+");
    private Attribute bild = new Attribute("", true ,".+");
    private Attribute polizist = new Attribute("", true, "\\d+");
    private Attribute fall = new Attribute("", true, "\\d+");

    /*********************************
     * Constructor
     *********************************/

    public Indiz(List<String> list, Connection c) {
        if (checkInsert(list)) {
            this.iD.setValue(list.get(0));
            this.anlagedatum.setValue(list.get(1));
            this.text.setValue(list.get(2));
            this.bild.setValue(list.get(3));
            this.polizist.setValue(list.get(4));
            this.fall.setValue(list.get(5));

            setPk(new KeyValue("ID", getID()));
            setEntityName(eName);

            insertTupel(c);
            setIsCreated(true);
        } else {
            System.out.println("invalid new Entry");
        }
    }

    public Indiz(ResultSet rs) throws SQLException {
        this.iD.setValue(rs.getString(1));
        this.anlagedatum.setValue(rs.getString(2));
        this.text.setValue(rs.getString(3));
        this.bild.setValue(rs.getString(4));
        this.polizist.setValue(rs.getString(5));
        this.fall.setValue(rs.getString(6));

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

    public String getAnlagedatum() {
        return anlagedatum.getValue();
    }

    public boolean setAnlagedatum(String anlagedatum) {
        return this.anlagedatum.setValue(anlagedatum);
    }

    public String getText() {
        return text.getValue();
    }

    public boolean setText(String text) {
        return this.text.setValue(text);
    }

    public String getBild() {
        return bild.getValue();
    }

    public boolean setBild(String bild) {
        return this.bild.setValue(bild);
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

    public static ObservableList<Indiz> buildData(Connection c, TableView tableView) {
        try {
            ResultSet rs = c.createStatement().executeQuery("SELECT * FROM " + eName + ";");

            return buildDataInner(c, rs, tableView);
        } catch (Exception e) {
            e.printStackTrace();
        }

        return null;
    }

    public static ObservableList<Indiz> buildData(Connection c, TableView tableView, String key, String value) {
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

    public static ObservableList<Indiz> buildDataInner(Connection c, ResultSet rs, TableView tableView) throws SQLException{
        ObservableList<Indiz> data = FXCollections.observableArrayList();

        // create the columns dynamically
        for (int i = 0; i < rs.getMetaData().getColumnCount(); i++) {
            final int j = i;
            TableColumn col = new TableColumn(rs.getMetaData().getColumnName(i+1));
            col.setCellValueFactory(new PropertyValueFactory<Indiz, String>(rs.getMetaData().getColumnName(i + 1)));
            col.setCellFactory(TextFieldTableCell.forTableColumn());
            col.setOnEditCommit(
                    new EventHandler<TableColumn.CellEditEvent<Indiz, String>>() {
                        @Override
                        public void handle(TableColumn.CellEditEvent<Indiz, String> t) {
                            Indiz p = t.getRowValue();
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
            Indiz p = new Indiz(rs);
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
            ps.setString(1, getAnlagedatum());
            ps.setString(2, getText());
            ps.setString(3, getBild());
            ps.setString(4, getPolizist());
            ps.setString(5, getFall());
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
                this.anlagedatum.checkRequiredRegex(l.get(1)) &&
                this.text.checkRequiredRegex(l.get(2)) &&
                this.bild.checkRequiredRegex(l.get(3)) &&
                this.polizist.checkRequiredRegex(l.get(4)) &&
                this.fall.checkRequiredRegex(l.get(5))) {
            return true;
        }
        return false;
    }

    public static Collection<VBox> showDetail(Connection c, String key, String value) {
        Collection<VBox> col = FXCollections.observableArrayList();

        VBox vb = createDetail(c, "Indizien", Indiz.class, key, value);
        Indiz v = (Indiz)((TableView)vb.getChildren().get(1)).getItems().get(0);
        col.add(vb);
        col.add(createDetail(c, "Fälle", Fall.class, "ID", v.getFall()));
        col.add(createDetail(c, "Polizisten", Polizist.class, "PersNr", v.getPolizist()));

        // Show image if existing
        if (v.getBild() != null && !"".equals(v.getBild())) {
            String path = checkForImage(v.getBild());
            if (!"".equals(path)) {
                VBox image = new VBox();
                image.setAlignment(Pos.CENTER);
                ImageView iv = new ImageView();
                iv.setFitWidth(400);
                iv.setFitHeight(400);
                iv.setPreserveRatio(true);

                iv.setImage(new Image(path));

                image.getChildren().add(iv);

                col.add(image);
            }
        }

        return col;
    }

    public static String checkForImage(String imgName) {
        File f = new File(System.getProperty("user.dir").replace("\\", "/") + "/images/" + imgName);

        String path = "";
        if (f.exists()) {
            path = f.toURI().toString();
        }

        return path;
    }

    public static Indiz createDummy(Connection c) {
        List<String> l = new ArrayList<>();

        l.add("0");
        l.add("11.11.1111");
        l.add("dummy");
        if (selectedFile != null) {

            // check if a file with this name already exists
            File f = new File(System.getProperty("user.dir").replace("\\", "/") + "/images/" + selectedFile.getName());

            int i = 0;
            while (f.exists()) {
                f =  new File(System.getProperty("user.dir").replace("\\", "/") + "/images/" + i + "_" + selectedFile.getName());
                i++;
            }

            l.add(f.getName());

            try {
                f.getParentFile().mkdirs();
                Files.copy(selectedFile.toPath(), f.toPath());
            } catch (java.io.IOException e) {
                e.printStackTrace();
            }

            selectedFile = null;
            selectedImage.setValue("");
        } else {
            l.add("dummy");
        }
        l.add("0");
        l.add("0");

        return new Indiz(l, c);
    }

    public static void createImage(HBox hBox) {
        Button b = new Button("Bild auswählen");
        b.setOnAction(event -> {
            FileChooser fc = new FileChooser();
            fc.setTitle("Bild auswählen");
            fc.getExtensionFilters().addAll(
                    new FileChooser.ExtensionFilter("Bild Dateien", "*.png", "*.jpeg", "*.gif"));
            selectedFile = fc.showOpenDialog(new Stage());
            selectedImage.setValue(selectedFile.getName());
        });

        HBox.setMargin(b, new Insets(0, 5, 0, 20));

        Label l = new Label("");
        l.textProperty().bind(selectedImage);

        hBox.getChildren().addAll(b, l);
    }

    @Override
    public int deleteTupel(Connection c, Entity e) {
        String query = "DELETE FROM " + getEntityName() + " WHERE " + e.getPk().getKey() + "=?;";
        try {
            PreparedStatement ps = c.prepareStatement(query);
            ps.setString(1, e.getPk().getValue());
            int rows = ps.executeUpdate();

            try {

                // check if this is the only use!!!! of the image
                Indiz i = (Indiz) e;
                String queryI = "SELECT COUNT(*) FROM " + getEntityName() + " WHERE Bild=?;";
                PreparedStatement ps2 = c.prepareStatement(queryI);
                ps2.setString(1, i.getBild());
                ResultSet rs = ps2.executeQuery();

                if (rs.getInt(1) < 1) {
                    File f = new File(System.getProperty("user.dir").replace("\\", "/") + "/images/" + i.getBild());
                    if (f.exists()) {
                        Files.delete(f.toPath());
                    }
                }
            } catch (IOException e1) {
                e1.printStackTrace();
            }

            return rows;
        } catch (SQLException e1) {
            e1.printStackTrace();
        }

        return 0;
    }
}
