package controller;

import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.Node;
import javafx.scene.control.ChoiceBox;
import javafx.scene.control.ComboBox;
import jpcap.JpcapCaptor;
import jpcap.NetworkInterface;

import java.util.ArrayList;
import java.util.List;

public class ConfigureController {
    @FXML ComboBox<String> NetworkInterface;
    MainController mainController;
    List<String> NetworkInterfaceStr = new ArrayList<>();
    NetworkInterface[] devices = JpcapCaptor.getDeviceList();
    public void Init(MainController mainController){
        this.mainController = mainController;
        for (NetworkInterface device :devices){
            NetworkInterfaceStr.add(device.description);
            ObservableList<String> obList = FXCollections.observableList(NetworkInterfaceStr);
            NetworkInterface.setItems(obList);
        }
    }

    public void OK(ActionEvent actionEvent) {
        int index = NetworkInterface.getSelectionModel().getSelectedIndex();
        System.out.println(index);
        mainController.networkInterface = devices[index];
        ((Node) (actionEvent.getSource())).getScene().getWindow().hide();
    }
}
