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
    Ontwerpcomponent HAL9001DB = new Ontwerpcomponent("HAL9001DB", "database", 5100,  90, "dbserverImage.png");
    Ontwerpcomponent HAL9002DB = new Ontwerpcomponent("HAL9002DB", "database", 7700,  95, "dbserverImage.png");
    Ontwerpcomponent HAL9003DB = new Ontwerpcomponent("HAL9003DB", "database", 12200,  98, "dbserverImage.png");
    Ontwerpcomponent HAL9001W = new Ontwerpcomponent("HAL9001W", "webserver", 2200,  80, "webserverImage.png");
    Ontwerpcomponent HAL9002W = new Ontwerpcomponent("HAL9002W", "webserver", 3200,  90, "webserverImage.png");
    Ontwerpcomponent HAL9003W = new Ontwerpcomponent("HAL9003W", "webserver", 5100,  95, "webserverImage.png");
    private ArrayList<Ontwerpcomponent> design = new ArrayList<>();
    private ArrayList<Point> designLocations = new ArrayList<>();

    JLabel jlDbserverImage1, jlDbserverImage2, jlDbserverImage3, jlWebserverImage1, jlWebserverImage2, jlWebserverImage3, jlFirewallImage;

    Point xydb1, xydb2, xydb3, xyw1, xyw2, xyw3, xyfw;
    Point prevPtdb1, prevPtdb2, prevPtdb3, prevPtw1, prevPtw2, prevPtw3, prevPtfire;
    Point currentpt;
    private Color backClr2 = new Color(43, 43, 43);
    ClickListener clickListener = new ClickListener();
    DragListener dragListener = new DragListener();

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
        add(imageOpslag, BorderLayout.EAST);
        imageOpslag.setBorder(new LineBorder(Color.LIGHT_GRAY, 3));
        imageOpslag.setBackground(backClr2);
        imageOpslag.add(jlDbserverImage1);
        imageOpslag.add(jlDbserverImage2);
        imageOpslag.add(jlDbserverImage3);
        imageOpslag.add(jlWebserverImage1);
        imageOpslag.add(jlWebserverImage2);
        imageOpslag.add(jlWebserverImage3);
        imageOpslag.add(jlFirewallImage);

        this.addMouseListener(clickListener);
        this.addMouseMotionListener(dragListener);
        jlDbserverImage1.addMouseListener(clickListener);
        jlDbserverImage1.addMouseMotionListener(dragListener);

        setVisible(true);
    }

    public void paintComponent(Graphics g) {
        super.paintComponent(g);
//        if(xydb1!=null){
//            jlDbserverImage1.getIcon().paintIcon(this, g, (int) xydb1.getX(), (int) xydb1.getY());
//        }
        for(Ontwerpcomponent component: design){
            for(int i=0;i<design.size();i++){
                component.getImage().paintIcon(this, g, (int) designLocations.get(i).getX(), (int) designLocations.get(i).getY());
            }
        }

    }

    private class ClickListener extends MouseAdapter {//https://docs.oracle.com/javase/tutorial/uiswing/events/mouselistener.html
        public void mousePressed(MouseEvent e) {
            if(xydb1==null){
                //locaties van de images
                xydb1 = new Point(jlDbserverImage1.getLocationOnScreen());
            }

            if(e.getSource()==jlDbserverImage1){
                currentpt=xydb1;
                if(e.getClickCount()==2){
                    JOptionPane.showMessageDialog(jlDbserverImage1, "Naam: "+ontwerpcomponenten.get(1).getNaam()+"\nType: "+ontwerpcomponenten.get(1).getType()+"\nBeschikbaarheid: "+ontwerpcomponenten.get(1).getBeschikbaarheidspercentage()+"% \nKosten: €"+ontwerpcomponenten.get(1).getKosten(), ontwerpcomponenten.get(1).getNaam(), JOptionPane.INFORMATION_MESSAGE);
                }else{
                    prevPtdb1 = e.getPoint();
                    Ontwerpcomponent ontwerpcomponent = ontwerpcomponenten.get(1);
                    design.add(ontwerpcomponent);
                    designLocations.add(currentpt);
                    JLabel jLabel = new JLabel(ontwerpcomponent.getImage());
                    jLabel.addMouseListener(clickListener);
                    jLabel.addMouseMotionListener(dragListener);
                }
            }
        }
    }

    private class DragListener extends MouseMotionAdapter {
        public void mouseDragged(MouseEvent e) {
            currentpt = e.getPoint();
            if(e.getSource()==jlDbserverImage1){

                xydb1.translate(
                        (int) (currentpt.getX() - prevPtdb1.getX()),
                        (int) (currentpt.getY() - prevPtdb1.getY())
                );
                prevPtdb1 = currentpt;
            }



            repaint();
        }
    }
}