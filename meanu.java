import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.KeyEvent;
import java.awt.event.WindowEvent;
import java.awt.event.WindowListener;
//import java.awt.event.KeyListener;
//import java.awt.event.MouseListener;
//import javax.swing.event.MenuListener;
public class meanu {
    public static void main(String[] args) {
        MenuItem item1 = new MenuItem("auto");
        MenuItem item2 = new MenuItem("copy");
        MenuItem item3 = new MenuItem("paste");
        MenuItem item4 = new MenuItem("-");
        Menu item5 = new Menu("patton");
        MenuItem item6 = new MenuItem("//",new MenuShortcut(KeyEvent.VK_Q,true));
        MenuItem item7 = new MenuItem("cancle \"//\"");
        Menu menu1 = new Menu("edit");
        Menu menu0 = new Menu("document");
        MenuBar menuBar = new MenuBar();
        Frame frame = new Frame("menu");
        TextArea textArea = new TextArea();
        menu1.add(item1);
        menu1.add(item2);
        menu1.add(item3);
        menu1.add(item4);
        menu1.add(item5);
        item5.add(item6);
        item5.add(item7);
        menuBar.add(menu0);
        menuBar.add(menu1);
        frame.setMenuBar(menuBar);
        item6.addActionListener((@SuppressWarnings("unused")ActionEvent e) -> {
            textArea.append("//");
        });
        frame.addWindowListener(new WindowListener() {
            @Override
            public void windowOpened(WindowEvent e) {
                //throw new UnsupportedOperationException("Not supported yet.");
            }

            @Override
            public void windowClosing(WindowEvent e) {
                System.exit(0);
            }

            @Override
            public void windowClosed(WindowEvent e) {
                //throw new UnsupportedOperationException("Not supported yet.");
            }

            @Override
            public void windowIconified(WindowEvent e) {
                //throw new UnsupportedOperationException("Not supported yet.");
            }

            @Override
            public void windowDeiconified(WindowEvent e) {
                //throw new UnsupportedOperationException("Not supported yet.");
            }

            @Override
            public void windowActivated(WindowEvent e) {
                //throw new UnsupportedOperationException("Not supported yet.");
            }

            @Override
            public void windowDeactivated(WindowEvent e) {
                //throw new UnsupportedOperationException("Not supported yet.");
            }
        
        });
        frame.add(textArea);
        frame.pack();
        frame.setVisible(true);
    }
}
