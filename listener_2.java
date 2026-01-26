
//import java.awt.Frame;

import java.awt.*;
import java.awt.event.ItemEvent;
import javax.swing.BoxLayout;
public class listener_2 {
    public static void main(String[] args) {
        Frame frame =new Frame("a");
        frame.setLayout(new BoxLayout(frame,0));
        Choice c = new Choice();
        c.add("1");
        c.add("b");
        TextField t = new TextField("c");
        frame.add(c);
        frame.add(t);
        frame.pack();
        frame.setVisible(true);
        //@SuppressWarnings("unused")
        c.addItemListener((ItemEvent e) -> {
            System.out.println((e.getItem()));
        });
    }
}
