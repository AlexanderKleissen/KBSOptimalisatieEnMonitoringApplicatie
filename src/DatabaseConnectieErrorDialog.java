import javax.swing.*;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;

public class DatabaseConnectieErrorDialog extends JDialog {

   DatabaseConnectieErrorDialog() {
       try {
           Connection connection = DriverManager.getConnection("jdbc:mysql://localhost:3306/nerdygadgets", "root", ""); //Verbinding met database wordt gemaakt
           connection.close();
       } catch (SQLException e) {
           JOptionPane.showMessageDialog(this, "Kan geen verbinding maken met de database", "Database connectie error", JOptionPane.ERROR_MESSAGE);
           System.out.println(e);
       }
    }
}
