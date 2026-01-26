import java.awt.*;
public class grid {
    public static void main(String[] args) {
        Frame frame = new Frame("計算機");
        BorderLayout g1 = new BorderLayout(0,0);
        frame.setLayout(g1);
        GridLayout g2 = new GridLayout(4,5,4,4);
        //Panel p1 = new Panel(g1);
        Panel p2 = new Panel(g2);
        frame.add(new TextField("answer"),"North");
        for (int i = 0; i < 10; i++) {
            p2.add(new Button(String.valueOf(i)));
        }
        p2.add(new Button("+"));
        p2.add(new Button("-"));
        p2.add(new Button("*"));
        p2.add(new Button("/"));
        p2.add(new Button("."));
        p2.add(new Button("Del"));
        p2.add(new Button("="));
        p2.add(new Button("<"));
        p2.add(new Button(">"));
        p2.add(new Button("AC"));
        frame.add(p2);
        frame.setVisible(true);
        frame.pack();
    }
}
