
import java.awt.*;
//import javax.swing.border.Border;

public class border_layout{
    public static void main(String[] args) {
        Frame frame = new Frame("a");
        BorderLayout borderLayout = new BorderLayout(30,20);
        Button n =  new Button("n");
        Button w =  new Button("w");
        Button c =  new Button("c");
        Button e =  new Button("e");
        Button s =  new Button("s");
        frame.setLayout(borderLayout);
        frame.add(n,BorderLayout.NORTH);
        frame.add(w,BorderLayout.WEST);
        frame.add(c,BorderLayout.CENTER);
        frame.add(e,BorderLayout.EAST);
        frame.add(s,BorderLayout.SOUTH);
        frame.pack();
        frame.setVisible(true);
        System.out.println(BorderLayout.NORTH);
    }
}