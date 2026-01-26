
//import java.awt.Frame;

import java.awt.*;
import java.awt.event.ContainerEvent;
import java.awt.event.ContainerListener;
import java.awt.event.ItemEvent;
import java.awt.event.TextEvent;
import javax.swing.BoxLayout;
public class listener_2_2 {
    public static void main(String[] args) {
        Frame frame =new Frame("a");
        frame.setLayout(new BoxLayout(frame,0));
        Choice c = new Choice();
        c.add("1");
        c.add("b");
        TextField t = new TextField("c");
        frame.pack();
        frame.setVisible(true);
        //@SuppressWarnings("unused")
        c.addItemListener((ItemEvent e) -> {
            System.out.println((e.getItem()));
        });
        t.addTextListener((@SuppressWarnings("unused") TextEvent e) -> {System.out.println(t.getText());});
        frame.addContainerListener(new ContainerListener() {
                @Override
                public void componentAdded(ContainerEvent e){
                    System.out.println(e + "Added");
                }
                @Override
                public void componentRemoved(ContainerEvent e){
                    System.out.println(e + "Removed");
                }
            }
        );
        frame.setVisible(true);
        frame.add(c);
        frame.add(t); 
        frame.pack();
    }
}
