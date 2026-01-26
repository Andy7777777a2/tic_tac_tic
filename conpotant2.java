import java.awt.*;
import javax.swing.*;
public class conpotant2 {
    public void init(){
        Frame frame = new Frame("a");
        Panel p1 = new Panel(new FlowLayout(FlowLayout.LEFT,0,0));
        frame.add(p1);
        Panel p2 = new Panel(new BorderLayout());
        p1.add(p2);
        p2.add(new TextArea(5,20));
        Box p3 = Box.createHorizontalBox();
        p2.add(p3,BorderLayout.SOUTH);
        Choice c1 = new Choice();
        p3.add(c1);
        CheckboxGroup c2 = new CheckboxGroup();
        Checkbox c3 = new Checkbox("male",c2,true);
        Checkbox c4 = new Checkbox("female",c2,false);
        p3.add(c3);
        p3.add(c4);
        Checkbox c5 = new Checkbox("marred",false);
        p3.add(c5);
        c1.add("red");
        c1.add("blue");
        c1.add("green");
        List l = new List();
        l.add("red");
        l.add("green");
        l.add("blue");
        p1.add(l);
        Box p4 = Box.createHorizontalBox();
        frame.add(p4,BorderLayout.SOUTH);
        p4.add(new TextField(20));
        p4.add(new Button("OK"));
        frame.pack();
        frame.setVisible(true);
    }
    public static void main(String[] args) {
        new conpotant().init();
    }
}
