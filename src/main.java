public class main {
    public static void main(String[] args) {
        Monitoringcomponent db1 = new Monitoringcomponent("Database1", "database", "Online", 90, 80, 256, 20, 99.99);
        Monitoringcomponent db2 = new Monitoringcomponent("Database2","database" ,"Offline", 40, 45, 315, 20, 98.00);
        Monitoringcomponent db3 = new Monitoringcomponent("Database3", "database", "Online", 80, 80, 256, 20, 99.99);
        Monitoringcomponent db4 = new Monitoringcomponent("Database4", "database","Online", 120, 80, 256, 20, 99.99);
        Monitoringcomponent ws1 = new Monitoringcomponent("Webserver1", "database","Online", 120, 80, 256, 50, 99.99);
        Monitoringcomponent ws2 = new Monitoringcomponent("Webserver2", "webserver","Offline", 120, 80, 312, 50, 99.99);
        Monitoringcomponent ws3 = new Monitoringcomponent("Webserver3", "webserver","Online", 60, 120, 256, 50, 99.99);
        Monitoringcomponent ws4 = new Monitoringcomponent("Webserver4","webserver","Online", 120, 80, 256, 50, 99.89);
        Monitoringcomponent ws5 = new Monitoringcomponent("Webserver5","webserver","Offline", 120, 80, 256, 50, 90.00);
        Monitoringcomponent pfSense1 = new Monitoringcomponent("pfSense1","firewall","Online", 2000, 30, 1256, 120, 99.99);

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
        firewall.componenten.add(pfSense1);

        Monitoringnetwerk netwerk1 = new Monitoringnetwerk("Netwerk 1", 200, 99.99);
        netwerk1.groepen.add(db);
        netwerk1.groepen.add(ws);
        netwerk1.groepen.add(firewall);

        Monitoringnetwerk netwerk2 = new Monitoringnetwerk("Netwerk 2", 350, 90);
        Monitoringnetwerk netwerk4 = new Monitoringnetwerk("Netwerk 4", 350, 90);
        Monitoringnetwerk netwerk6 = new Monitoringnetwerk("Netwerk 6", 350, 90);
        Monitoringnetwerk netwerk7 = new Monitoringnetwerk("Netwerk 7", 350, 90);
//        MonitoringFrame monitoringFrame1 = new MonitoringFrame(netwerk1);

        Ontwerpcomponent db5 = new Ontwerpcomponent("Database5", "database", 100, 256,  99.90);
        Ontwerpcomponent db6 = new Ontwerpcomponent("Database6","database" ,400, 45,  98.00);
        Ontwerpcomponent db7 = new Ontwerpcomponent("Database7", "database", 300, 20,  99.98);
        Ontwerpcomponent db8 = new Ontwerpcomponent("Database8", "database",120, 100,  99.96);
        Ontwerpcomponent ws6 = new Ontwerpcomponent("Webserver6", "database",930, 80,  99.93);
        Ontwerpcomponent ws7 = new Ontwerpcomponent("Webserver7", "webserver",125, 40,  99.99);
        Ontwerpcomponent ws8 = new Ontwerpcomponent("Webserver8", "webserver", 980, 120, 99.91);
        Ontwerpcomponent ws9 = new Ontwerpcomponent("Webserver9","webserver", 470, 80, 99.89);
        Ontwerpcomponent ws10 = new Ontwerpcomponent("Webserver10","webserver", 390, 50,  90.00);
        Ontwerpcomponent pfSense2 = new Ontwerpcomponent("pfSense2","firewall",230, 30, 99.99);

        Groep dbOntwerpComponenten = new Groep("OntwerpDatabaseservers", 99.99);
        dbOntwerpComponenten.componenten.add(db5);
        dbOntwerpComponenten.componenten.add(db6);
        dbOntwerpComponenten.componenten.add(db7);
        dbOntwerpComponenten.componenten.add(db8);

        Groep wsOntwerpComponenten = new Groep("OntwerpWebservers", 99.98);
        wsOntwerpComponenten.componenten.add(ws6);
        wsOntwerpComponenten.componenten.add(ws7);
        wsOntwerpComponenten.componenten.add(ws8);
        wsOntwerpComponenten.componenten.add(ws9);
        wsOntwerpComponenten.componenten.add(ws10);

        Groep firewallOntwerpComponent = new Groep("OntwerpFirewall", 99.99);
        firewallOntwerpComponent.componenten.add(pfSense2);

        Ontwerpnetwerk netwerk3 = new Ontwerpnetwerk("Ontwerpnetwerk uit main", 999, 99.99, 99.98);
        netwerk3.groepen.add(dbOntwerpComponenten);
        netwerk3.groepen.add(wsOntwerpComponenten);
        netwerk3.groepen.add(firewallOntwerpComponent);

        //Ontwerpnetwerk netwerk5 = new Ontwerpnetwerk("Netwerk 5", 999, 99.99, 99.98);

        OntwerpFrame ontwerpFrame = new OntwerpFrame();
    }
}