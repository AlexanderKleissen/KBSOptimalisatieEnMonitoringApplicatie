import javax.swing.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;

public class DatabaseConnectieErrorDialog extends JDialog implements ActionListener {

   DatabaseConnectieErrorDialog() {
       try {
           Connection connection = DriverManager.getConnection("jdbc:mysql://11.11.20.100:3306/nerdygadgets", "root", "m2okbsd1"); //Verbinding met database wordt gemaakt
           connection.close();
       } catch (SQLException e) {
           JOptionPane.showMessageDialog(this, "Kan geen verbinding maken met de database", "Database connectie error", JOptionPane.ERROR_MESSAGE);
           System.out.println(e);
       }
    }

    @Override
    public void actionPerformed(ActionEvent e) {

    }
}
