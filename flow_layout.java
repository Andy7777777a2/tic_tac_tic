import java.awt.*;
public class flow_layout{
    public static void main(String[] args) {
        Frame frame = new Frame("a");
        frame.setLayout(new FlowLayout(FlowLayout.LEFT,20,10));
        for (int i = 0; i < 100; i++) {
            frame.add(new Button(String.valueOf(i)));
        }
        //frame.setBounds(100, 100, 500, 500);
        frame.pack();
        frame.setVisible(true);
    }
}