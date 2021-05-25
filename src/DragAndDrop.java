import javax.swing.*;
import javax.swing.border.LineBorder;
import java.awt.*;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.awt.event.MouseMotionAdapter;
import java.util.ArrayList;

public class DragAndDrop extends JPanel {
    private ArrayList<Ontwerpcomponent> ontwerpcomponenten = new ArrayList<>();
    private ArrayList<JLabel> images = new ArrayList<>();
    Ontwerpcomponent firewallpfSense = new Ontwerpcomponent("pfSense", "firewall", 4000, 99.998, "firewallImage.png");
    Ontwerpcomponent HAL9001DB = new Ontwerpcomponent("HAL9001DB", "database", 5100,  90, "dbserverImage1.png");
    Ontwerpcomponent HAL9002DB = new Ontwerpcomponent("HAL9002DB", "database", 7700,  95, "dbserverImage2.png");
    Ontwerpcomponent HAL9003DB = new Ontwerpcomponent("HAL9003DB", "database", 12200,  98, "dbserverImage3.png");
    Ontwerpcomponent HAL9001W = new Ontwerpcomponent("HAL9001W", "webserver", 2200,  80, "webserverImage1.png");
    Ontwerpcomponent HAL9002W = new Ontwerpcomponent("HAL9002W", "webserver", 3200,  90, "webserverImage2.png");
    Ontwerpcomponent HAL9003W = new Ontwerpcomponent("HAL9003W", "webserver", 5100,  95, "webserverImage3.png");
    private ArrayList<Ontwerpcomponent> design = new ArrayList<>();
    private ArrayList<Point> designLocations = new ArrayList<>();
    Ontwerpcomponent gekozenComponent;
    JLabel gekozenImage;

    JLabel jlDbserverImage1, jlDbserverImage2, jlDbserverImage3, jlWebserverImage1, jlWebserverImage2, jlWebserverImage3, jlFirewallImage;

    Point location, prevPt, currentpt;
    private Color backClr2 = new Color(43, 43, 43);
    ClickListener clickListener = new ClickListener();
    DragListener dragListener = new DragListener();
    JPanel paintPanel = new JPanel();

    DragAndDrop() {
        this.setLayout(new BorderLayout());
        this.setBackground(backClr2);

        //alle mogelijke componenten
        ontwerpcomponenten.add(firewallpfSense);
        ontwerpcomponenten.add(HAL9001DB);
        ontwerpcomponenten.add(HAL9002DB);
        ontwerpcomponenten.add(HAL9003DB);
        ontwerpcomponenten.add(HAL9001W);
        ontwerpcomponenten.add(HAL9002W);
        ontwerpcomponenten.add(HAL9003W);
        //textvakken met de images
        jlDbserverImage1 = new JLabel(ontwerpcomponenten.get(1).getImage());
        jlDbserverImage2 = new JLabel(ontwerpcomponenten.get(2).getImage());
        jlDbserverImage3 = new JLabel(ontwerpcomponenten.get(3).getImage());
        jlWebserverImage1 = new JLabel(ontwerpcomponenten.get(4).getImage());
        jlWebserverImage2 = new JLabel(ontwerpcomponenten.get(5).getImage());
        jlWebserverImage3 = new JLabel(ontwerpcomponenten.get(6).getImage());
        jlFirewallImage = new JLabel(ontwerpcomponenten.get(0).getImage());
        //arraylist met images
        images.add(jlDbserverImage1);
        images.add(jlDbserverImage2);
        images.add(jlDbserverImage3);
        images.add(jlWebserverImage1);
        images.add(jlWebserverImage2);
        images.add(jlWebserverImage3);
        images.add(jlFirewallImage);

        //panel voor de mogelijke componenten
        JPanel imageOpslag = new JPanel(new GridLayout(4, 2, 10, 0));
        imageOpslag.setBorder(new LineBorder(Color.LIGHT_GRAY, 3));
        imageOpslag.setBackground(backClr2);
        imageOpslag.add(jlDbserverImage1);
        imageOpslag.add(jlDbserverImage2);
        imageOpslag.add(jlDbserverImage3);
        imageOpslag.add(jlWebserverImage1);
        imageOpslag.add(jlWebserverImage2);
        imageOpslag.add(jlWebserverImage3);
        imageOpslag.add(jlFirewallImage);
        add(imageOpslag, BorderLayout.EAST);

        //click and draglistener toevoegen
        jlDbserverImage1.addMouseListener(clickListener);
        jlDbserverImage1.addMouseMotionListener(dragListener);
        jlDbserverImage2.addMouseListener(clickListener);
        jlDbserverImage2.addMouseMotionListener(dragListener);
        jlDbserverImage3.addMouseListener(clickListener);
        jlDbserverImage3.addMouseMotionListener(dragListener);
        jlWebserverImage1.addMouseListener(clickListener);
        jlWebserverImage1.addMouseMotionListener(dragListener);
        jlWebserverImage2.addMouseListener(clickListener);
        jlWebserverImage2.addMouseMotionListener(dragListener);
        jlWebserverImage3.addMouseListener(clickListener);
        jlWebserverImage3.addMouseMotionListener(dragListener);
        jlFirewallImage.addMouseListener(clickListener);
        jlFirewallImage.addMouseMotionListener(dragListener);
        this.addMouseListener(clickListener);

        setVisible(true);
    }

    public void paintComponent(Graphics g) {
        super.paintComponent(g);
        int i=0;
        for(Ontwerpcomponent component: design){
            component.getImage().paintIcon(this, g, (int) designLocations.get(i).getX(), (int) designLocations.get(i).getY());
            i++;
        }
    }


    private class ClickListener extends MouseAdapter {//https://docs.oracle.com/javase/tutorial/uiswing/events/mouselistener.html
        public void mousePressed(MouseEvent e) {

            //locaties van de images
            for(JLabel image: images){
                if(image == e.getSource()){
                    location=image.getLocationOnScreen();
                    gekozenImage=image;
                    for(Ontwerpcomponent ontwerpcomponent: ontwerpcomponenten){
                        if (ontwerpcomponent.getImage().equals(image.getIcon())){
                            gekozenComponent=ontwerpcomponent;
                        }
                    }
                }
            }

            currentpt=location;
            if(e.getClickCount()==2){
                JOptionPane.showMessageDialog(gekozenImage, "Naam: "+gekozenComponent.getNaam()+"\nType: "+gekozenComponent.getType()+"\nBeschikbaarheid: "+gekozenComponent.getBeschikbaarheidspercentage()+"% \nKosten: €"+gekozenComponent.getKosten(), gekozenComponent.getNaam(), JOptionPane.INFORMATION_MESSAGE);
            }else{
                prevPt= e.getPoint();
                Ontwerpcomponent component = new Ontwerpcomponent(gekozenComponent.getNaam(), gekozenComponent.getType(), Double.parseDouble(gekozenComponent.getKosten()), Double.parseDouble(gekozenComponent.getBeschikbaarheidspercentage()), gekozenComponent.getImage()) ;
                design.add(component);
                designLocations.add(currentpt);
            }

        }
    }

    private class DragListener extends MouseMotionAdapter {
        public void mouseDragged(MouseEvent e) {
            currentpt = e.getPoint();

            location.translate(
                    (int) (currentpt.getX() - prevPt.getX()),
                    (int) (currentpt.getY() - prevPt.getY())
            );
            prevPt = currentpt;

            repaint();
        }
    }
}