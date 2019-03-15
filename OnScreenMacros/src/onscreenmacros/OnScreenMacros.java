package onscreenmacros;

import javax.swing.SwingUtilities;

/**
 *
 * @author Marc
 */
public class OnScreenMacros {
    public static void main(String[] args) {    
    SwingUtilities.invokeLater(new Runnable() {
        @Override
        public void run() {
            new MacroFrame();
        }
    });
}
}
