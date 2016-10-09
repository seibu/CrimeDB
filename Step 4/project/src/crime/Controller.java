package crime;

import crime.entities.Entity;
import crime.entities.Person;
import javafx.collections.ObservableList;
import javafx.event.EventHandler;
import javafx.fxml.FXML;
import javafx.scene.Node;
import javafx.scene.control.*;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.HBox;
import javafx.scene.layout.VBox;

import java.util.ArrayList;
import java.util.List;

public class Controller {
    private static Crime crime = new Crime();
    private static Main main;

    /*********************************
     * root
     *********************************/

    @FXML
    private void back() {
        crime.showPreView(main);
    }

    @FXML
    private void showMain() {
        View view = new View("main", "", "","");
        crime.showView(main, view);
    }

    /*********************************
     * main
     *********************************/

    @FXML
    ComboBox s_entity;

    @FXML
    TextField s_tb;

    @FXML
    private void showSearch() {
        View view = new View("search", s_entity.getSelectionModel().getSelectedItem().toString(), "", s_tb.getText());
        crime.showView(main, view);
    }

    @FXML
    private void showEntities() {
        View view = new View("entities", "", "", "");
        crime.showView(main, view);
    }

    @FXML
    private void showErm() {
        View view = new View("erm", "", "", "");
        crime.showView(main, view);
    }

    /*********************************
     * entites
     *********************************/

    @FXML
    private ListView e_entities;

    @FXML
    private void showEntity() {
        View view = new View("list", e_entities.getSelectionModel().getSelectedItem().toString(), "","");
        crime.showView(main,view);
    }

    /*********************************
     * list
     *********************************/

    @FXML
    TableView tv_list;

    @FXML
    Label e_label;

    @FXML
    HBox filterHbox;

    @FXML
    private void showDetail() {
        Entity e = (Entity) tv_list.getSelectionModel().getSelectedItem();
        if (e != null) {
            View view = new View("detail", crime.getView().getEntity(), e.getPk().getKey(), e.getPk().getValue());
            crime.showView(main, view);
        } else {
            System.out.println("noEntry selected");
        }
    }

    @FXML
    private void addEntry() {
        crime.addDummy(crime.getView().getEntity(), tv_list);
    }

    @FXML
    private void deleteEntry() {
        Entity e = (Entity) tv_list.getSelectionModel().getSelectedItem();
        crime.deleteEntry(e, tv_list);
    }

    /*********************************
     * search
     *********************************/

    @FXML
    Label s_label;

    @FXML
    TableView tv_search;

    @FXML
    private void showDetail2() {
        Entity e = (Entity) tv_search.getSelectionModel().getSelectedItem();
        if (e != null) {
            View view = new View("detail", crime.getView().getEntity(), e.getPk().getKey(), e.getPk().getValue());
            crime.showView(main, view);
        } else {
            System.out.println("noEntry selected");
        }
    }


    /*********************************
     * detail
    *********************************/

    @FXML
    VBox detailView;

    @FXML
    private void showDetailDetail() {
        Entity e = (Entity) Crime.getDetailEntity().getSelectionModel().getSelectedItem();
        if (e != null) {
            View view = new View("detail", Crime.getDetailEntity().getId(), e.getPk().getKey(), e.getPk().getValue());
            crime.showView(main, view);
        } else {
            System.out.println("noEntry selected");
        }
    }

    @FXML
    private void addEntryDetail() {
        if (Crime.getDetailEntity() != null) {
            crime.addDummy(Crime.getDetailEntity().getId(), Crime.getDetailEntity());
        }
    }

    @FXML
    private void deleteEntryDetail() {
        Entity e = (Entity) Crime.getDetailEntity().getSelectionModel().getSelectedItem();
        crime.deleteEntry(e, Crime.getDetailEntity());
    }

    /*********************************
     * others
     *********************************/

    public void setMain(Main main) {
        this.main = main;
    }

    @FXML
    public void initialize() {
        if (e_label != null) {
            e_label.setText(crime.getView().getEntity());
        }

        if (tv_list != null) {
            crime.showEntity(tv_list, filterHbox);
        }

        if (detailView != null) {
            crime.showDetail(detailView);
        }

        if (s_label != null) {
            s_label.setText("Suche nach " + crime.getView().getEntity() + " - " + crime.getView().getKv().getValue());
            crime.showEntity2(tv_search);
        }
    }
}
