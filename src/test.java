import jpcap.JpcapCaptor;
import jpcap.NetworkInterface;
import jpcap.PacketReceiver;
import jpcap.packet.IPPacket;
import jpcap.packet.Packet;

public class test implements PacketReceiver {
    public static void main(String[] args) {
        NetworkInterface[] devices = JpcapCaptor.getDeviceList();
        for(NetworkInterface device : devices)
        {
            System.out.println(device.name + "  " + device.description);
        }
        System.out.println("-------------");
        JpcapCaptor captor = null;
        int capLen = 1512;
        boolean promiseCheck = true;
        try {
            captor = JpcapCaptor.openDevice(devices[2],capLen,promiseCheck,50);
        }catch (Exception e){
            e.printStackTrace();
        }

        test t = new test();

        int i = 0;
        while (i < 10)
        {
            Packet packet = captor.getPacket();
            if(packet instanceof IPPacket &&  ((IPPacket) packet).version == 4){
                i++;
                IPPacket ipPacket = (IPPacket) packet;
                System.out.println("版本：IPV4");
                System.out.println("优先权："+ ipPacket.priority);
                System.out.println("服务设置: 最大的吞吐量" + ipPacket.t_flag);
                String protocol = "";
                switch (ipPacket.protocol){
                    case 1:
                        protocol = "ICMP";
                        break;
                    case 2:
                        protocol = "IGMP";
                        break;
                    case 6:
                        protocol = "TCP";
                        break;
                    case 8:
                        protocol = "EGP";
                        break;
                    case 9:
                        protocol = "IGP";
                        break;
                    case 17:
                        protocol = "UDP";
                        break;
                    case 41:
                        protocol = "IPV6";
                        break;
                    case 89:
                        protocol = "OSPF";
                        break;
                    default:
                        break;
                }
                System.out.println("协议: " + protocol);
                System.out.println("源IP: " + ipPacket.src_ip.getHostAddress());
                System.out.println("目的IP: " + ipPacket.dst_ip.getHostAddress());
                System.out.println("源主机名: " + ipPacket.src_ip.getHostName());
                System.out.println("目的主机名: " + ipPacket.dst_ip.getHostName());
            }
        }
    }

    @Override
    public void receivePacket(Packet packet) {
        Analysis(packet);
    }

    private void Analysis(Packet packet) {
    }
}
