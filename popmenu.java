import java.awt.*;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.awt.event.WindowEvent;
import java.awt.event.WindowListener;
import javax.swing.BoxLayout;
public class popmenu {
    public static void main(String[] args) {
        Frame frame = new Frame("pop menu");
        frame.setLayout(new BoxLayout(frame, BoxLayout.Y_AXIS));
        TextArea textArea = new TextArea(7,10);
        Panel emptyP = new Panel();
        MenuItem item1 = new MenuItem("//");
        MenuItem item2 = new MenuItem("cancle\"//\"");
        MenuItem item3 = new MenuItem("copy");
        MenuItem item4 = new MenuItem("save");
        PopupMenu PopupMenu = new PopupMenu();
        PopupMenu.add(item1);
        PopupMenu.add(item2);
        PopupMenu.add(item3);
        PopupMenu.add(item4);
        emptyP.addMouseListener(new MouseAdapter() {
            @Override
            public void mouseReleased(MouseEvent e){
                if(e.isPopupTrigger())
                    PopupMenu.show(emptyP, e.getX(), e.getY());
            }
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
        frame.add(emptyP);
        emptyP.setPreferredSize(new Dimension(500, 700));
        frame.pack();
        frame.setVisible(true);
        emptyP.add(PopupMenu);
    }
}
