import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.awt.event.MouseMotionListener;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;
import javax.swing.Box;
import javax.swing.event.MenuListener;
//import javax.swing.text.GapContent;
public class draw {
    private static  class selfCanvas extends Canvas {
        @Override
        public void paint(Graphics g) {
            switch (shape) {
                case "r" -> {
                    g.setColor(Color.BLACK);
                    g.drawRect(X, Y, X-x, Y-y);
                }
                case "o" -> {
                    g.setColor(Color.RED);
                    g.drawOval(X, Y, X-x, Y-y);
                }
                case null -> {}
                default -> throw new AssertionError();
            }
        }
    }
    private static class myFrame extends Frame implements MouseMotionListener{

        @Override
        public void mouseDragged(MouseEvent e) {
            //throw new UnsupportedOperationException("Not supported yet.");
        }

        /*@Override
        public void mouseMoved(MouseEvent e) {
            throw new UnsupportedOperationException("Not supported yet.");
        }*/
        @Override
        public void mouseMoved(MouseEvent e){
            switch (swap) {
                case 0 -> {
                    X = e.getX();
                    Y = e.getY();
                }
                case 1 -> {
                    x = e.getX();
                    y = e.getY();
                }
                default -> throw new AssertionError();
            }
            System.out.print(" ");
            canvas.repaint();
            }
    }
    private static String shape = null;
    private static int X,Y,x,y;
    private static int swap = 0;
    public static void main(String[] args) {
        Frame frame = new Frame("a");
        Canvas canvas = new selfCanvas();
        Button buttonR = new Button("r"),buttonO = new Button("o");
        ActionListener actionListener = (ActionEvent e) -> {
            shape = e.getActionCommand();
            System.out.println(e.getActionCommand());
            canvas.repaint();
        };
        buttonO.addActionListener(actionListener);
        buttonR.addActionListener(actionListener);
        canvas.addMouseListener(new MouseAdapter() {
            @Override
            public void mouseReleased(MouseEvent e){
                if(!e.isPopupTrigger())
                    swap = 1-swap;
                canvas.repaint();
                System.out.print(swap);
            }
        });
        Box panel = Box.createHorizontalBox();
        panel.add(buttonR);
        panel.add(buttonO);
        frame.add(canvas);
        frame.add(panel, BorderLayout.SOUTH);
        frame.pack();
        frame.addWindowListener(new WindowAdapter() {
            @Override
            public void windowClosing(WindowEvent e){
                System.exit(0);
            }
        });
        frame.setVisible(true);
    }
}
