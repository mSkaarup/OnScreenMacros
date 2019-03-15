package onscreenmacros;

import java.awt.AWTException;
import java.awt.Robot;
import static java.awt.event.KeyEvent.*;
import java.util.LinkedList;
import java.util.List;

/**
 *
 * @author Marc
 */
public class MacroBot extends Robot {
    
    private final List<Integer> heldKeys;
    
    public MacroBot() throws AWTException {
        super();
        heldKeys = new LinkedList<>();
    }
    
    public void type(String key, boolean isHeld) throws IllegalArgumentException {
         String temp = key.toLowerCase();
         System.out.println(temp);
        switch (temp) {
        case "backspace" : doType(VK_BACK_SPACE, isHeld);        break;
        case "control"   : doType(VK_CONTROL, isHeld);           break;
        case "enter"     : doType(VK_ENTER, isHeld);             break;
        case "shift"     : doType(VK_SHIFT, isHeld);             break;
        case "space"     : doType(VK_SPACE, isHeld);             break;
        case "tab"       : doType(VK_TAB, isHeld);               break;
        case "alt"       : doType(VK_ALT, isHeld);               break;
        
        case "a"         : doType(VK_A, isHeld);                 break;
        case "b"         : doType(VK_B, isHeld);                 break;
        case "c"         : doType(VK_C, isHeld);                 break;
        case "d"         : doType(VK_D, isHeld);                 break;
        case "e"         : doType(VK_E, isHeld);                 break;
        case "f"         : doType(VK_F, isHeld);                 break;
        case "g"         : doType(VK_G, isHeld);                 break;
        case "h"         : doType(VK_H, isHeld);                 break;
        case "i"         : doType(VK_I, isHeld);                 break;
        case "j"         : doType(VK_J, isHeld);                 break;
        case "k"         : doType(VK_K, isHeld);                 break;
        case "l"         : doType(VK_L, isHeld);                 break;
        case "m"         : doType(VK_M, isHeld);                 break;
        case "n"         : doType(VK_N, isHeld);                 break;
        case "o"         : doType(VK_O, isHeld);                 break;
        case "p"         : doType(VK_P, isHeld);                 break;
        case "q"         : doType(VK_Q, isHeld);                 break;
        case "r"         : doType(VK_R, isHeld);                 break;
        case "s"         : doType(VK_S, isHeld);                 break;
        case "t"         : doType(VK_T, isHeld);                 break;
        case "u"         : doType(VK_U, isHeld);                 break;
        case "v"         : doType(VK_V, isHeld);                 break;
        case "w"         : doType(VK_W, isHeld);                 break;
        case "x"         : doType(VK_X, isHeld);                 break;
        case "y"         : doType(VK_Y, isHeld);                 break;
        case "z"         : doType(VK_Z, isHeld);                 break;
        
        case "0"         : doType(VK_0, isHeld);                 break;
        case "1"         : doType(VK_1, isHeld);                 break;
        case "2"         : doType(VK_2, isHeld);                 break;
        case "3"         : doType(VK_3, isHeld);                 break;
        case "4"         : doType(VK_4, isHeld);                 break;
        case "5"         : doType(VK_5, isHeld);                 break;
        case "6"         : doType(VK_6, isHeld);                 break;
        case "7"         : doType(VK_7, isHeld);                 break;
        case "8"         : doType(VK_8, isHeld);                 break;
        case "9"         : doType(VK_9, isHeld);                 break;
        
        case "("         : doType(VK_LEFT_PARENTHESIS, isHeld);  break;
        case ")"         : doType(VK_RIGHT_PARENTHESIS, isHeld); break;
        case "]"         : doType(VK_CLOSE_BRACKET, isHeld);     break;
        case "["         : doType(VK_OPEN_BRACKET, isHeld);      break;
        case "`"         : doType(VK_BACK_QUOTE, isHeld);        break;
        case "~"         : doType(VK_BACK_QUOTE, isHeld);        break;
        case "\\"        : doType(VK_BACK_SLASH, isHeld);        break;
        case ";"         : doType(VK_SEMICOLON, isHeld);         break;
        case "."         : doType(VK_PERIOD, isHeld);            break;
        case "="         : doType(VK_EQUALS, isHeld);            break;
        case "-"         : doType(VK_MINUS, isHeld);             break;    
        case ","         : doType(VK_COMMA, isHeld);             break;
        case "/"         : doType(VK_SLASH, isHeld);             break;
        case "shortdelay": super.delay(100);                     break;
        case "delay"     : super.delay(250);                     break;
        case "longdelay" : super.delay(500);                     break;

        default:
            throw new IllegalArgumentException("Cannot type character " + key);
        }
    }
    
    private void doType(int keyCode, boolean hold) {
        if(hold) {
            holdKey(keyCode);
        } else {
            releaseHeldKeys();
            pressKey(keyCode);
        }
    }
    
    private void pressKey(int keyCode) {
        super.keyPress(keyCode);
        super.delay(50);
        super.keyRelease(keyCode);
    }
    
    private void holdKey(int keyCode) {
        super.keyPress(keyCode);
        super.delay(200);
        heldKeys.add(keyCode);
    }
    
    public void releaseHeldKeys() {
        
        if(!heldKeys.isEmpty()) {
            for(Integer i : heldKeys) {
                super.keyRelease(i);
            }
        }
        heldKeys.clear();
    }
}
