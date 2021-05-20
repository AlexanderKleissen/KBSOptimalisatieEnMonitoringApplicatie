import java.awt.*;
import java.awt.event.*;
import javax.swing.*;

public class DragAndDrop extends JPanel {

    ImageIcon image = new ImageIcon("smileSmall.png");
    final int WIDTH = image.getIconWidth();
    final int HEIGHT = image.getIconHeight();
    Point imageCorner;
    Point prevPt;

    DragAndDrop() {
        imageCorner = new Point(0, 0);
        ClickListener clickListener = new ClickListener();
        DragListener dragListener = new DragListener();
        this.addMouseListener(clickListener);
        this.addMouseMotionListener(dragListener);
    }

    public void paintComponent(Graphics g) {
        super.paintComponent(g);
        image.paintIcon(this, g, (int) imageCorner.getX(), (int) imageCorner.getY());
    }

    private class ClickListener extends MouseAdapter {
        public void mousePressed(MouseEvent e) {
            if (e.getClickCount() == 2) {
                System.out.println("double click");
            } else {
                prevPt = e.getPoint();
            }
        }
    }

    private class DragListener extends MouseMotionAdapter {
        public void mouseDragged(MouseEvent e) {
            Point currentpt = e.getPoint();
            if ((e.getPoint().getX() < ((int) imageCorner.getX() + WIDTH) &&
                    e.getPoint().getY() < (int) (imageCorner.getY() + HEIGHT)) &&
                    (e.getPoint().getX() > (int) imageCorner.getX() &&
                            e.getPoint().getY() > (int) imageCorner.getY())) {
                imageCorner.translate(
                        (int) (currentpt.getX() - prevPt.getX()),
                        (int) (currentpt.getY() - prevPt.getY())
                );
                prevPt = currentpt;
            }
            repaint();
        }
    }
}