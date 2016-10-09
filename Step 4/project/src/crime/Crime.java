package crime;

import crime.entities.*;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.layout.HBox;
import javafx.scene.layout.VBox;

import java.sql.Connection;
import java.sql.DriverManager;
import java.util.List;

public class Crime {
    private View view;
    private View preView;

    private Connection c = null;

    private static TableView detailEntity;

    public static void setDetailEntity(TableView detailEntity) {
        Crime.detailEntity = detailEntity;
    }

    public static TableView getDetailEntity() {
        return detailEntity;
    }

    public View getView() {
        return view;
    }

    public Crime() {
        view = new View("main", "", "", "");

        initDB();
    }

    private void initDB() {
        if (c == null) {
            try {
                Class.forName("org.sqlite.JDBC");
                c = DriverManager.getConnection("jdbc:sqlite:crime.sqlite");
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
    }

    public void showPreView(Main main) {
        if (preView != null) {
            showView(main, preView);
        }
    }

    public void showView(Main main, View view) {
        // preview = old view
        preView = this.view;
        // view = new view
        this.view = view;

        switch (view.getName()) {
            case "main":
                main.showFXML(Config.FXML_MAIN);
                break;
            case "entities":
                main.showFXML(Config.FXML_ENTITIES);
                break;
            case "list":
                main.showFXML(Config.FXML_LIST);
                break;
            case "detail":
                main.showFXML(Config.FXML_DETAIL);
                break;
            case "search":
                main.showFXML(Config.FXML_SEARCH);
                break;
            case "erm":
                main.showFXML(Config.FXML_ERM);
                break;
        }
    }

    public void showEntity(TableView tv, HBox hBox) {
        switch(view.getEntity()) {
            case "Personen":
                tv.setItems(Person.buildData(c, tv));
                break;
            case "Polizisten":
                tv.setItems(Polizist.buildData(c, tv));
                break;
            case "Verdächtiger":
                tv.setItems(Verdächtiger.buildData(c, tv));
                break;
            case "Opfer":
                tv.setItems(Opfer.buildData(c, tv));
                break;
            case "Fälle":
                tv.setItems(Fall.buildData(c, tv));
                break;
            case "Arten":
                tv.setItems(Art.buildData(c, tv));
                break;
            case "Bezirke":
                tv.setItems(Bezirk.buildData(c, tv));
                break;
            case "Verbrechen":
                Verbrechen.createFilter(hBox, c, tv);
                tv.setItems(Verbrechen.buildData(c, tv));
                break;
            case "Notizen":
                tv.setItems(Notiz.buildData(c, tv));
                break;
            case "Indizien":
                Indiz.createImage(hBox);
                tv.setItems(Indiz.buildData(c, tv));
                break;
            case "Behörden":
                tv.setItems(Behörde.buildData(c, tv));
                break;
            case "Zeitraum":
                tv.setItems(Zeitraum.buildData(c, tv));
                break;
            case "PolizistFall":
                tv.setItems(ArbeitetAn.buildData(c, tv));
                break;
            case "BezirkBezirk":
                tv.setItems(LiegtIn.buildData(c, tv));
                break;
        }
    }

    public void showEntity2(TableView tableView) {
        switch(view.getEntity()) {
            case "Polizisten":
                tableView.setItems(SPolizist.buildData(c, tableView, view.getKv().getValue()));
                break;
            case "Verdächtiger":
                tableView.setItems(SVerdächtiger.buildData(c, tableView, view.getKv().getValue()));
                break;
            case "Opfer":
                tableView.setItems(SOpfer.buildData(c, tableView, view.getKv().getValue()));
                break;
        }
    }

    public void deleteEntry(Entity entity, TableView tv) {
        tv.getItems().removeAll(entity);
        entity.deleteTupel(c, entity);
    }

    public void showDetail(VBox vbox) {
        //System.out.println(view.getEntity());

        switch(view.getEntity()) {
            case "Personen":
                vbox.getChildren().addAll(Person.showDetail(c, view.getKv().getKey(), view.getKv().getValue()));
                break;
            case "Polizisten":
                vbox.getChildren().addAll(Polizist.showDetail(c, view.getKv().getKey(), view.getKv().getValue()));
                break;
            case "Verdächtiger":
                vbox.getChildren().addAll(Verdächtiger.showDetail(c, view.getKv().getKey(), view.getKv().getValue()));
                break;
            case "Opfer":
                vbox.getChildren().addAll(Opfer.showDetail(c, view.getKv().getKey(), view.getKv().getValue()));
                break;
            case "Fälle":
                vbox.getChildren().addAll(Fall.showDetail(c, view.getKv().getKey(), view.getKv().getValue()));
                break;
            case "Arten":
                vbox.getChildren().addAll(Art.showDetail(c, view.getKv().getKey(), view.getKv().getValue()));
                break;
            case "Bezirke":
                vbox.getChildren().addAll(Bezirk.showDetail(c, view.getKv().getKey(), view.getKv().getValue()));
                break;
            case "Verbrechen":
                vbox.getChildren().addAll(Verbrechen.showDetail(c, view.getKv().getKey(), view.getKv().getValue()));
                break;
            case "Notizen":
                vbox.getChildren().addAll(Notiz.showDetail(c, view.getKv().getKey(), view.getKv().getValue()));
                break;
            case "Indizien":
                vbox.getChildren().addAll(Indiz.showDetail(c, view.getKv().getKey(), view.getKv().getValue()));
                break;
            case "Behörden":
                vbox.getChildren().addAll(Behörde.showDetail(c, view.getKv().getKey(), view.getKv().getValue()));
                break;
            case "Zeitraum":
                vbox.getChildren().addAll(Zeitraum.showDetail(c, view.getKv().getKey(), view.getKv().getValue()));
                break;
            case "PolizistFall":
                vbox.getChildren().addAll(ArbeitetAn.showDetail(c, view.getKv().getKey(), view.getKv().getValue()));
                break;
            case "BezirkBezirk":
                vbox.getChildren().addAll(LiegtIn.showDetail(c, view.getKv().getKey(), view.getKv().getValue()));
                break;
        }
    }

    public void addDummy(String entity, TableView tv) {
        //System.out.println(view.getEntity());

        switch(entity) {
            case "Personen":
                tv.getItems().add(Person.createDummy(c));
                break;
            case "Polizisten":
                tv.getItems().add(Polizist.createDummy(c));
                break;
            case "Verdächtiger":
                tv.getItems().add(Verdächtiger.createDummy(c));
                break;
            case "Opfer":
                tv.getItems().add(Opfer.createDummy(c));
                break;
            case "Fälle":
                tv.getItems().add(Fall.createDummy(c));
                break;
            case "Arten":
                tv.getItems().add(Art.createDummy(c));
                break;
            case "Bezirke":
                tv.getItems().add(Bezirk.createDummy(c));
                break;
            case "Verbrechen":
                tv.getItems().add(Verbrechen.createDummy(c));
                break;
            case "Notizen":
                tv.getItems().add(Notiz.createDummy(c));
                break;
            case "Indizien":
                tv.getItems().add(Indiz.createDummy(c));
                break;
            case "Behörden":
                tv.getItems().add(Behörde.createDummy(c));
                break;
            case "Zeitraum":
                tv.getItems().add(Zeitraum.createDummy(c));
                break;
            case "PolizistFall":
                tv.getItems().add(ArbeitetAn.createDummy(c));
                break;
            case "BezirkBezirk":
                tv.getItems().add(LiegtIn.createDummy(c));
                break;
        }
    }
}
