import javax.naming.CommunicationException;
import javax.xml.crypto.Data;
import java.sql.*;

public class main {
    public static void main(String[] args) throws SQLException {
        DatabaseConnectieErrorDialog databaseConnectieErrorDialog = new DatabaseConnectieErrorDialog();

        Monitoringcomponent pfSense1 = new Monitoringcomponent("pfSense", "Firewall", 99.998, 4000, "192.168.1.26");
        Monitoringcomponent db1 = new Monitoringcomponent("DB1", "HAL9002DB", 95, 7700, "192.168.1.26");
        Monitoringcomponent db2 = new Monitoringcomponent("DB2", "HAL9002DB", 95, 7700, "192.168.1.26");
        Monitoringcomponent ws1 = new Monitoringcomponent("WS1", "HAL9002W", 90, 3200, "192.168.1.131");
        Monitoringcomponent ws2 = new Monitoringcomponent("WS2", "HAL9002W", 90, 3200, "192.168.1.131");

//        Monitoringcomponent db1 = new Monitoringcomponent("Database1", "database", "Online", 90, 80, 256, 20, 90.00);
//        Monitoringcomponent db2 = new Monitoringcomponent("Database2","database" ,"Offline", 40, 45, 315, 20, 90.00);
//        Monitoringcomponent db3 = new Monitoringcomponent("Database3", "database", "Online", 80, 80, 256, 20, 90.00);
//        Monitoringcomponent db4 = new Monitoringcomponent("Database4", "database","Online", 120, 80, 256, 20, 95.00);
//        Monitoringcomponent ws1 = new Monitoringcomponent("Webserver1", "database","Online", 120, 80, 256, 50, 90.00);
//        Monitoringcomponent ws2 = new Monitoringcomponent("Webserver2", "webserver","Offline", 120, 80, 312, 50, 90.00);
//        Monitoringcomponent ws3 = new Monitoringcomponent("Webserver3", "webserver","Online", 60, 120, 256, 50, 90.00);
//        Monitoringcomponent ws4 = new Monitoringcomponent("Webserver4","webserver","Online", 120, 80, 256, 50, 90.00);
//        Monitoringcomponent ws5 = new Monitoringcomponent("Webserver5","webserver","Offline", 120, 80, 256, 50, 80.00);
//        Monitoringcomponent pfSense1 = new Monitoringcomponent("pfSense1","firewall","Online", 2000, 30, 1256, 120, 99.99);

        Groep firewall = new Groep("Firewall", 99.97);
        firewall.componenten.add(pfSense1);

        Groep db = new Groep("Databaseservers", 99.98);
        db.componenten.add(db1);
        db.componenten.add(db2);
        //db.componenten.add(db3);
        //db.componenten.add(db4);

        Groep ws = new Groep("Webservers", 99.99);
        ws.componenten.add(ws1);
        ws.componenten.add(ws2);
        //ws.componenten.add(ws3);
        //ws.componenten.add(ws4);
        //ws.componenten.add(ws5);

        Monitoringnetwerk netwerk1 = new Monitoringnetwerk("Netwerk 1", 200, 99.99);
        netwerk1.groepen.add(firewall);
        netwerk1.groepen.add(db);
        netwerk1.groepen.add(ws);

//        MonitoringFrame monitoringFrame1 = new MonitoringFrame(netwerk1);

        OntwerpFrame ontwerpFrame = new OntwerpFrame();
        ontwerpFrame.add(netwerk1);

        Connection connection = DriverManager.getConnection("jdbc:mysql://localhost:3306/nerdygadgets", "root", "");
        Statement statement = connection.createStatement();
        ResultSet rs = statement.executeQuery("SELECT * FROM ontwerpnetwerk");

        Ontwerpnetwerk ontwerpnetwerk = new Ontwerpnetwerk();

        while (rs.next()) {
            if (!Ontwerpnetwerk.getOntwerpNetwerken().contains(ontwerpnetwerk)) {
                ontwerpnetwerk = new Ontwerpnetwerk(rs.getString("NaamNetwerk") + "%", rs.getDouble("Kosten"),
                        rs.getDouble("Beschikbaarheid"));
            }

            for (Ontwerpcomponent ontwerpcomponent : Ontwerpcomponent.getOntwerpcomponenten()) {
                if (ontwerpcomponent.getNaam().equals(rs.getString("NaamComponent"))) {
                    for (int i = 0; i < rs.getInt("AantalGebruikt"); i++) {
                        if (ontwerpcomponent.getType().equals("firewall")) {
                            Groep firewallgroep = new Groep("firewall");
                            firewallgroep.componenten.add(ontwerpcomponent);
                            ontwerpnetwerk.groepen.add(firewallgroep);
                        }

                        if (ontwerpcomponent.getType().equals("webserver")) {
                            Groep webservergroep = new Groep("webserver");
                            webservergroep.componenten.add(ontwerpcomponent);
                            ontwerpnetwerk.groepen.add(webservergroep);
                        }

                        if (ontwerpcomponent.getType().equals("database")) {
                            Groep databasegroep = new Groep("database");
                            databasegroep.componenten.add(ontwerpcomponent);
                            ontwerpnetwerk.groepen.add(databasegroep);
                        }
                    }
                }
            }
        }
        statement.close();
    }
}