import javax.naming.CommunicationException;
import javax.xml.crypto.Data;
import java.sql.*;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.Objects;

public class main {
    public static void main(String[] args) throws SQLException {
        DatabaseConnectieErrorDialog databaseConnectieErrorDialog = new DatabaseConnectieErrorDialog();

        Monitoringcomponent pfSense1 = new Monitoringcomponent("pfSense", "Firewall", 99.998, 4000, "11.11.200.1");
        Monitoringcomponent db1 = new Monitoringcomponent("DB1", "HAL9002DB", 95, 7700, "11.11.20.2");
        Monitoringcomponent db2 = new Monitoringcomponent("DB2", "HAL9002DB", 95, 7700, "11.11.20.3");
        Monitoringcomponent ws1 = new Monitoringcomponent("WS1", "HAL9002W", 90, 3200, "11.11.2.2");
        Monitoringcomponent ws2 = new Monitoringcomponent("WS2", "HAL9002W", 90, 3200, "11.11.2.3");


        Groep firewall = new Groep("Firewall", 99.97);
        firewall.componenten.add(pfSense1);

        Groep db = new Groep("Databaseservers", 99.98);
        db.componenten.add(db1);
        db.componenten.add(db2);

        Groep ws = new Groep("Webservers", 99.99);
        ws.componenten.add(ws1);
        ws.componenten.add(ws2);

        Monitoringnetwerk netwerk1 = new Monitoringnetwerk("Netwerk 1", 200, 99.99);
        netwerk1.groepen.add(firewall);
        netwerk1.groepen.add(db);
        netwerk1.groepen.add(ws);

     //   MonitoringFrame monitoringFrame1 = new MonitoringFrame(netwerk1);

        //Ontwerpcomponenten uit database halen
        Connection connection = DriverManager.getConnection("jdbc:mysql://11.11.20.100:3306/nerdygadgets", "root", "m2okbsd1");
        Statement statement = connection.createStatement();
        ResultSet rs = statement.executeQuery("SELECT * FROM component_ontwerpen");
        while (rs.next()) {
            Ontwerpcomponent ontwerpcomponent = new Ontwerpcomponent(rs.getString("NaamComponent"), rs.getString("Type"),
                    rs.getDouble("Kosten"), rs.getDouble("Beschikbaarheid"));

        }
        rs.close();

        OntwerpFrame ontwerpFrame = new OntwerpFrame();
        ontwerpFrame.add(netwerk1);

        try {
            ResultSet rs2 = statement.executeQuery("SELECT distinct NaamNetwerk FROM ontwerpnetwerk");
            ArrayList<String> componentOntwerpenLijst = new ArrayList();
            while (rs2.next()) {
            Ontwerpnetwerk ontwerpnetwerk = new Ontwerpnetwerk();
            ontwerpnetwerk.setNaam(rs2.getString(1));
            }
            rs2.close();

        } catch (SQLException e) {
            System.out.println(e);
        }
        connection.close();
    }
}