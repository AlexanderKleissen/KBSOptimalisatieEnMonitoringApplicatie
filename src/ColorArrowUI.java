import javax.swing.*;
import javax.swing.plaf.basic.BasicArrowButton;
import javax.swing.plaf.basic.BasicComboBoxUI;
import java.awt.*;

public class ColorArrowUI extends BasicComboBoxUI { //hiermee kunnen we de kleur van het pijltje van de combobox wijzigen
    private Color backClr1 = new Color(60, 63, 65); //de kleur van de rest

    @Override
    protected JButton createArrowButton() {
        final Color background = UIManager.getColor("ComboBoxUI.buttonBackgroundColor");
        final Color pressedButtonBorderColor = UIManager.getColor("ComboBox.buttonShadow"); //kleur van de knop indien ingedrukt
        final Color triangle = Color.white;               //kleur driehoekje
        final JButton button = new BasicArrowButton(BasicArrowButton.SOUTH, background, pressedButtonBorderColor, triangle, background);
        button.setName("ComboBox.arrowButton"); //Mandatory, as per BasicComboBoxUI#createArrowButton().
        return button;
    }
}