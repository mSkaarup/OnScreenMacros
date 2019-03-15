package onscreenmacros;

import java.awt.AWTException;
import java.awt.event.ActionEvent;
import java.util.Arrays;
import java.util.LinkedHashMap;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;
import javax.swing.JButton;

/**
 *
 * @author Marc
 */
public class MacroButton extends JButton {
    private MacroBot robot;
    private List<String>  keys;
    private List<Boolean> isHeld;
    
    public MacroButton(String mString) {
        try {
            robot = new MacroBot();
        } catch(AWTException ex) {
            System.err.println("Problem Making MacroRobot!");
        }
        keys = new LinkedList<>();
        isHeld = new LinkedList<>();
        mString = mString.replaceAll(" ", "");     
        String[] temp = mString.split(",");
        List<String> parts = new LinkedList<>(Arrays.asList(temp));
        
        for(String string : parts) {
            if(string.matches("^(.+[+])+.+$")) {
                temp = string.split("\\+");
                for(String s : temp ) {
                    keys.add(s);
                    isHeld.add(Boolean.TRUE);
                }
            } else if(string.matches(".+")) {
                keys.add(string);
                isHeld.add(Boolean.FALSE);
            }
        }
        this.addActionListener();
    }
    
    private void addActionListener() {
        this.addActionListener((ActionEvent event) -> {
            runMacro();
        });
    }
    
    private void runMacro() {
        for(int i = 0; i < keys.size(); i++) {
            robot.type(keys.get(i), isHeld.get(i));
        }
        robot.releaseHeldKeys();
    }
}
