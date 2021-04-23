public class main {
    public static void main(String[] args) {
        MonitoringFrame monitoringFrame = new MonitoringFrame();

        Monitoringcomponent db1 = new Monitoringcomponent("Online", 90, 80, 256, 20, 99.99);
        Monitoringcomponent db2 = new Monitoringcomponent("Offline", 40, 45, 315, 20, 98.00);
        Monitoringcomponent db3 = new Monitoringcomponent("Online", 80, 80, 256, 20, 99.99);
        Monitoringcomponent db4 = new Monitoringcomponent("Online", 120, 80, 256, 20, 99.99);
        Monitoringcomponent ws1 = new Monitoringcomponent("Online", 120, 80, 256, 50, 99.99);
        Monitoringcomponent ws2 = new Monitoringcomponent("Offline", 120, 80, 312, 50, 99.99);
        Monitoringcomponent ws3 = new Monitoringcomponent("Online", 60, 120, 256, 50, 99.99);
        Monitoringcomponent ws4 = new Monitoringcomponent("Online", 120, 80, 256, 50, 99.89);
        Monitoringcomponent ws5 = new Monitoringcomponent("Offline", 120, 80, 256, 50, 90.00);
        Monitoringcomponent pFSense = new Monitoringcomponent("Online", 2000, 30, 1256, 120, 99.99);

        Groep db = new Groep("Databaseservers", 99.98);
        db.componenten.add(db1);
        db.componenten.add(db2);
        db.componenten.add(db3);
        db.componenten.add(db4);
        Groep ws = new Groep("Webservers", 99.99);
        ws.componenten.add(ws1);
        ws.componenten.add(ws2);
        ws.componenten.add(ws3);
        ws.componenten.add(ws4);
        ws.componenten.add(ws5);
        Groep firewall = new Groep("Firewall", 99.97);
        firewall.componenten.add(pFSense);

        Monitoringnetwerk netwerk1 = new Monitoringnetwerk("Netwerk 1", 200, 99.99);
        netwerk1.groepen.add(db);
        netwerk1.groepen.add(ws);
        netwerk1.groepen.add(firewall);

        Monitoringnetwerk netwerk2 = new Monitoringnetwerk("Netwerk 2", 350, 90);

        MonitoringFrame monitoringFrame1 = new MonitoringFrame(netwerk1);
    }
}