package controller;

import javafx.application.Platform;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.JavaFXBuilderFactory;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Menu;
import javafx.scene.control.MenuBar;
import javafx.scene.control.MenuItem;
import javafx.scene.control.TextArea;
import javafx.stage.Stage;
import jpcap.JpcapCaptor;
import jpcap.NetworkInterface;
import jpcap.PacketReceiver;
import jpcap.packet.*;

import java.io.IOException;

import java.net.URL;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public class MainController implements PacketReceiver{
    @FXML MenuBar menu;
    @FXML TextArea Text;
    JpcapCaptor jpcapCaptor;
    NetworkInterface networkInterface;
    List<Packet> packets = new ArrayList<>();
    MenuItem Start;
    MenuItem Stop;
    public void Init(){
        Menu fileMenu = new Menu("File");
        MenuItem exitMenuItem = new MenuItem("exit");
        exitMenuItem.setOnAction(actionEvent -> Platform.exit());
        fileMenu.getItems().add(exitMenuItem);

        Menu captureMenu = new Menu("Capture");
        MenuItem NetWorkOption = new MenuItem("Network Card Information");
        MenuItem Configure = new MenuItem("Configure");
        Start = new MenuItem("Start");
        Stop = new MenuItem("Stop");

        NetWorkOption.setOnAction(this::NetworkOptionHandler);
        Configure.setOnAction(this::ConfigureHandler);
        Start.setOnAction(this::StartHandler);
        Stop.setOnAction(this::StopHandler);
//        Stop.setDisable(true);
        captureMenu.getItems().addAll(NetWorkOption,Configure,Start,Stop);

        Menu Help = new Menu("Help");

        menu.getMenus().setAll(fileMenu,captureMenu,Help);


    }

    public void NetworkOptionHandler(ActionEvent actionEvent){

    }

    public void ConfigureHandler(ActionEvent actionEvent) {
        try {
            Stage stage = new Stage();
            URL location = getClass().getResource("/ui/ConfigureFXML.fxml");
            FXMLLoader fxmlLoader = new FXMLLoader();
            fxmlLoader.setLocation(location);
            fxmlLoader.setBuilderFactory(new JavaFXBuilderFactory());
            Parent root = fxmlLoader.load();
            stage.setTitle("IP数据报抓取");
            Scene scene = new Scene(root, 497, 148);
            stage.setScene(scene);
            ConfigureController controller = fxmlLoader.getController();   //获取Controller的实例对象
            controller.Init(this);
            stage.show();
        }catch (Exception e){
            e.printStackTrace();
        }
    }

    public void StartHandler(ActionEvent actionEvent){
        try {
            System.out.println(networkInterface);
            jpcapCaptor = JpcapCaptor.openDevice(networkInterface,2048,true,5000);
        } catch (IOException e) {
            e.printStackTrace();
        }
//        Start.setDisable(true);
//        Stop.setDisable(false);
        Text.appendText("开始抓包\n");
        jpcapCaptor.processPacket(100,this);
        System.out.println("开始抓包");

    }

    public void StopHandler(ActionEvent actionEvent){
        if (jpcapCaptor != null){
            jpcapCaptor.breakLoop();
            Text.appendText("停止抓包\n");
            System.out.println("停止抓包");
//            Start.setDisable(false);
//            Stop.setDisable(true);
        }
    }

    public void Analysis(Packet packet){
        Text.appendText("\n");
        Text.appendText("Captured Length: " + packet.caplen + "bytes\n");
        Text.appendText("Length of this Packet:" + packet.len + " byte\n");
        Text.appendText("Length of Header:" + packet.header.length + " byte\n");
        Text.appendText("Length of Data:" + packet.data.length + " byte\n");
        Text.appendText("---Header---\n");
        DatalinkPacket dPacket = packet.datalink;
        if (dPacket instanceof EthernetPacket) {
            EthernetPacket ePacket = (EthernetPacket) dPacket;
            Text.appendText("src_mac:");
            int flag1 = 0;
            for (byte b : ePacket.src_mac) {
                flag1++;
                if (flag1 < ePacket.src_mac.length) {
                    Text.appendText(Integer.toHexString(b & 0xff) + ":");
                } else
                    Text.appendText(Integer.toHexString(b & 0xff) + "\n");
            }
            Text.appendText("dst_mac:");
            int flag2 = 0;
            for (byte b : ePacket.dst_mac) {
                flag2++;
                if (flag2 < ePacket.dst_mac.length) {
                    Text.appendText(Integer.toHexString(b & 0xff) + ":");
                } else
                    Text.appendText(Integer.toHexString(b & 0xff) + "\n");
            }
            Text.appendText("frameType:" + Integer.toHexString(ePacket.frametype & 0xffff) + "\n");
            Text.appendText("\n");
        } else {
            Text.appendText(dPacket + "\n");
            Text.appendText("\n");
        }
        if (packet instanceof ARPPacket) {
            ARPPacket aPacket = (ARPPacket) packet;
            Text.appendText("HardType：" + aPacket.hardtype + "\n");
            Text.appendText("ProtoType：" + aPacket.prototype + "\n");
            Text.appendText("HLen：" + aPacket.hlen + "\n");
            Text.appendText("PLen：" + aPacket.plen + "\n");
            Text.appendText("Operation：" + aPacket.operation + "\n");
            Text.appendText("Sender_hardAddr：" + Arrays.toString(aPacket.sender_hardaddr) + "\n");
            Text.appendText("sender_protoAddr：" + Arrays.toString(aPacket.sender_protoaddr) + "\n");
            Text.appendText("target_hardAddr：" + Arrays.toString(aPacket.target_hardaddr) + "\n");
            Text.appendText("target_protoAddr：" + Arrays.toString(aPacket.target_protoaddr) + "\n");
            Text.appendText("\n");
        }
        if (packet instanceof IPPacket) {
            IPPacket iPacket = (IPPacket) packet;
            Text.appendText("IP版本: " + iPacket.version + " \n");
            if (iPacket.version == 4) {
                Text.appendText("Type of service:" + iPacket.rsv_tos + "\n");
                Text.appendText("Priority:" + iPacket.priority + "\n");
                Text.appendText("Packet Length:" + iPacket.length + "\n");
                Text.appendText("Identification:" + iPacket.ident + "\n");
                Text.appendText("Don't Frag? " + iPacket.dont_frag + "\n");
                Text.appendText("More Frag? " + iPacket.more_frag + "\n");
                Text.appendText("Frag Offset:" + iPacket.offset + "\n");
                Text.appendText("Time to Live:" + iPacket.hop_limit + "\n");
                Text.appendText("Protocol:" + iPacket.protocol + "  (TCP = 6; UDP = 17)\n");
                Text.appendText("Source address:" + iPacket.src_ip.toString() + "\n");
                Text.appendText("Destination address:" + iPacket.dst_ip.toString() + "\n");
                Text.appendText("Options:" + Arrays.toString(iPacket.option) + "\n");
                Text.appendText("\n");
            }
            if (iPacket instanceof UDPPacket) {
                Text.appendText("---UDP---\n");
                UDPPacket uPacket = (UDPPacket) iPacket;
                Text.appendText("Source Port:" + uPacket.src_port + "\n");
                Text.appendText("Destination Port:" + uPacket.dst_port + "\n");
                Text.appendText("Length:" + uPacket.length + "\n");
                Text.appendText("\n");
            }
            if (iPacket instanceof TCPPacket) { // 分析TCP协议
                Text.appendText("---TCP---\n");
                TCPPacket tPacket = (TCPPacket) iPacket;
                Text.appendText("Source Port:" + tPacket.src_port + "\n");
                Text.appendText("Destination Port:" + tPacket.dst_port + "\n");
                Text.appendText("Sequence Number:" + tPacket.sequence + "\n");
                Text.appendText("Acknowledge Number:" + tPacket.ack_num + "\n");
                Text.appendText("URG:" + tPacket.urg + "\n");
                Text.appendText("ACK:" + tPacket.ack + "\n");
                Text.appendText("PSH:" + tPacket.psh + "\n");
                Text.appendText("RST:" + tPacket.rst + "\n");
                Text.appendText("SYN:" + tPacket.syn + "\n");
                Text.appendText("FIN:" + tPacket.fin + "\n");
                Text.appendText("Window Size:" + tPacket.window + "\n");
                Text.appendText("Urgent Pointer:" + tPacket.urgent_pointer + "\n");
                Text.appendText("Option:" + Arrays.toString(tPacket.option) + "\n");
                Text.appendText("\n");
                if (tPacket.src_port == 80 || tPacket.dst_port == 80) {
                    Text.appendText("---HTTP---\n");
                    byte[] data = tPacket.data;
                    if (data.length == 0) {
                        Text.appendText("无数据返回！\n");
                    } else {
                        Text.appendText("已收到数据！\n");
//                            String str = new String(data);
//                            Text.appendText(str + "\n");
                    }
                }
            }
            Text.appendText("\n");
        }
    }

    @Override
    public void receivePacket(Packet packet) {
        Analysis(packet);
    }
}
