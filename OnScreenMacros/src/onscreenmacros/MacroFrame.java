package onscreenmacros;

import java.awt.AWTException;
import java.awt.Container;
import java.awt.Dimension;
import java.awt.GridLayout;
import java.awt.Image;
import java.awt.MenuItem;
import java.awt.PopupMenu;
import java.awt.SystemTray;
import java.awt.TrayIcon;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.awt.event.MouseMotionAdapter;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.net.URISyntaxException;
import java.util.LinkedList;
import java.util.Properties;
import java.util.Scanner;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.imageio.ImageIO;
import javax.swing.ImageIcon;
import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JPanel;

public class MacroFrame extends JFrame {
    
    public static final Properties PROPS = new Properties();
    private String orientation;
    private int buttonWidth;
    private int buttonHeight;
    private int buttonsPerPanel;
    
    private LinkedList<JPanel> panels;
    private int currentPanel;
    
    private LinkedList<JButton> buttons;
    private LinkedList<File> buttonImages;
    
    private int posX;
    private int posY;
    
    private String directoryPath;
    private String buttonTextPath;
    private String defaultImagePath;
    private String dragImagePath;
    
    public MacroFrame() {
        getDirPath();
        getProperties();
        getResources();
        initialize();
        createTrayIcon();
        getButtons();
    }
    
    private void initialize(){ 
        currentPanel = 0;
        panels = new LinkedList<>();
        buttons = new LinkedList<>();
        buttonImages = new LinkedList<>();
        setUndecorated(true);
        setSize(300, 300);
        setLocationRelativeTo(null);
        setAlwaysOnTop(true);
        setFocusableWindowState(false);
        setDefaultCloseOperation(EXIT_ON_CLOSE);
        setVisible(true);
    }
    
    private void getResources() {
        buttonTextPath = directoryPath + "/Buttons.txt";
        defaultImagePath = directoryPath + "/default.png";
        dragImagePath = directoryPath + "/drag.png";
        File buttonText = new File(buttonTextPath);
        File defaultImage = new File(defaultImagePath);
        File dragImage = new File(dragImagePath);
        try {
            if(!buttonText.exists()) {
               ExportResource("Buttons.txt");
            }
            if(!defaultImage.exists()) {
                ExportResource("default.png");
            }
            if(!dragImage.exists()) {
                ExportResource("drag.png");
            }
        } catch (Exception ex) {
            System.err.println("Error Loading Resources!");
        }
    }
    
    private void createTrayIcon() {
        SystemTray systemTray = SystemTray.getSystemTray();
        Image trayIcon = null;
        try {
            trayIcon = ImageIO.read(new File(defaultImagePath));
        } catch (IOException ex) {
            Logger.getLogger(MacroFrame.class.getName()).log(Level.SEVERE, null, ex);
        }
        TrayIcon icon = new TrayIcon(trayIcon);
        
        PopupMenu popup = new PopupMenu();
        MenuItem exit = new MenuItem("Exit");
        
        exit.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent event) {
                MacroFrame.this.dispose();
                System.exit(0);
            }
        });
        
        popup.add(exit);
        icon.setPopupMenu(popup);
        try {
            systemTray.add(icon);
        } catch (AWTException ex) {
            Logger.getLogger(MacroFrame.class.getName()).log(Level.SEVERE, null, ex);
        }
    }
    
    private JButton getDragButton(JFrame frame) {
        JButton dragButton = new JButton();
        Image img = null;
        try {
            img = ImageIO.read(new File(dragImagePath));
        } catch (IOException ex) {
            System.exit(1);
        }
        dragButton.setBorder(null);
        dragButton.setIcon(new ImageIcon(img));
        dragButton.addMouseListener(new MouseAdapter() {
            @Override
            public void mousePressed(MouseEvent me) {
                // Get x,y and store them
                posX = me.getX();
                posY = me.getY();

            }

            @Override
            public void mouseDragged(MouseEvent me) {

                frame.setLocation(frame.getLocation().x + me.getX() - posX,
                                  frame.getLocation().y + me.getY() - posY);
            }
             
            @Override
            public void mouseClicked(MouseEvent me) {
                if(me.getClickCount() == 2) {
                    cycleButtons();
                }
                
            } 
        });

        dragButton.addMouseMotionListener(new MouseMotionAdapter() {
            @Override
            public void mouseDragged(MouseEvent me) {

                frame.setLocation(frame.getLocation().x + me.getX() - posX,
                                  frame.getLocation().y + me.getY() - posY);
            }
        });
        
        dragButton.setPreferredSize(new Dimension(buttonWidth, buttonHeight));
        return dragButton;
    }
    
    private void getButtons(){
        File buttonFile = new File(buttonTextPath);
        Scanner scanner = null;
        try {
            scanner = new Scanner(buttonFile);
        } catch (FileNotFoundException ex) {
            System.exit(1);
        }
        
        while(scanner.hasNextLine()) {
            buttons.add(new MacroButton(scanner.nextLine()));
            buttonImages.add(new File(directoryPath + "/" + scanner.nextLine()));
        }
        
        buttons.get(0).setPreferredSize(new Dimension(buttonWidth, buttonHeight));
        JButton temp;
        
        for(int i = 0; i < buttons.size(); i++) {
            Image img;
            temp = buttons.get(i);
            try {
                img = ImageIO.read(buttonImages.get(i));
                temp.setBorder(null);
                temp.setIcon(new ImageIcon(img));
                temp.setPreferredSize(new Dimension(buttonWidth, buttonHeight));
            } catch (IOException ex) {
                System.exit(1);
            }
        }
        
        createPanels();
        add(panels.get(currentPanel));
        pack();
    }
    
    static public String ExportResource(String resourceName) throws Exception {
        InputStream stream = null;
        OutputStream resStreamOut = null;
        String jarFolder;
        try {
            stream = MacroFrame.class.getResourceAsStream(resourceName);
            if(stream == null) {
                throw new Exception("Cannot get resource \"" + resourceName + "\" from Jar file.");
            }

            int readBytes;
            byte[] buffer = new byte[4096];
            jarFolder = new File(MacroFrame.class.getProtectionDomain().getCodeSource().getLocation().toURI().getPath()).getParentFile().getPath().replace('\\', '/');
            resStreamOut = new FileOutputStream(jarFolder + "/" + resourceName);
            while ((readBytes = stream.read(buffer)) > 0) {
                resStreamOut.write(buffer, 0, readBytes);
            }
        } catch (Exception ex) {
            throw ex;
        } finally {
            stream.close();
            resStreamOut.close();
        }

        return jarFolder + "/" + resourceName;
    }
    
    private void getDirPath() {
        try {
            directoryPath = new File(MacroFrame.class.getProtectionDomain().getCodeSource().getLocation().toURI().getPath()).getParentFile().getPath().replace('\\', '/');
        } catch (URISyntaxException ex) {
            System.err.println("Couldn't Locate jar directory!");
        }
    }
    
    private void getProperties() {
        File propertiesFile = new File(directoryPath + "/settings.properties");
        OutputStream out = null;
        if(!propertiesFile.exists()) {
            try {
                propertiesFile.createNewFile();
                out = new FileOutputStream(propertiesFile);
                
                PROPS.setProperty("orientation", "vertical");
                PROPS.setProperty("buttonheight", "30");
                PROPS.setProperty("buttonwidth", "30");
                PROPS.setProperty("buttonsperpanel", "5");
                
                PROPS.store(out, null);
                out.close();
            } catch (IOException ex) {
                System.err.println("Could Not Create Properties File!");
            } 
        }
        
        InputStream in = null;
        try {
            in = new FileInputStream(directoryPath + "/settings.properties");
            PROPS.load(in);
        } catch (FileNotFoundException ex) {
            System.err.println("Properties File Not Found!");
        } catch (IOException ex) {
            System.err.println("Error Loading Properties!");
        }
        orientation = PROPS.getProperty("orientation");
        buttonWidth = Integer.parseInt(PROPS.getProperty("buttonwidth"));
        buttonHeight = Integer.parseInt(PROPS.getProperty("buttonheight"));
        buttonsPerPanel = Integer.parseInt(PROPS.getProperty("buttonsperpanel"));
    }
    
    private void cycleButtons() {
        Container contentPane = getContentPane();
        
        contentPane.invalidate();
        contentPane.removeAll();
        
        if(currentPanel == panels.size() - 1) {
            currentPanel = 0;
        } else {
            currentPanel += 1;
        }
        contentPane.add(panels.get(currentPanel));
        contentPane.revalidate();
        pack();
    }
    
    private void createPanels() {
        int numOfPanels = (int)Math.ceil((double)buttons.size() / (double) buttonsPerPanel);
        int currentButton = 0;
        for(int i = 0; i < numOfPanels; i++) {
            JPanel temp = new JPanel();
            
            for(int j = 0; j < buttonsPerPanel; j++) {
                if(currentButton < buttons.size()) {
                    temp.add(buttons.get(currentButton));
                    currentButton++;
                }
            }
            temp.add(getDragButton(this));
            if(orientation.equals("vertical")) {
                temp.setLayout(new GridLayout(temp.getComponentCount(), 1, 0, 0));
                temp.setPreferredSize(new Dimension(buttonWidth, temp.getComponentCount() * buttonHeight));
            } else if(orientation.equals("horizontal")) {
                temp.setLayout(new GridLayout(1, temp.getComponentCount(), 0, 0));
                temp.setPreferredSize(new Dimension (buttonHeight, temp.getComponentCount() * buttonWidth));
            }
            panels.add(temp);
        }
    }
}