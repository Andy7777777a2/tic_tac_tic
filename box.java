import java.awt.*;
import javax.swing.BoxLayout;
public class box {
    public static void main(String[] args) {
        Frame frame = new Frame("a");
        frame.setLayout(new BoxLayout(frame, BoxLayout.Y_AXIS));
        System.out.println(BoxLayout.LINE_AXIS);
        System.out.println(BoxLayout.PAGE_AXIS);
        System.out.println(BoxLayout.X_AXIS);
        System.out.println(BoxLayout.Y_AXIS);
        System.out.println(BoxLayout.LINE_AXIS);
        frame.add(new Button("1"));
        frame.add(new Button("2"));
        frame.add(new Button("."));
        frame.pack();
        frame.setVisible(true);
    }
}
