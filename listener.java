import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import javax.swing.JFrame;
public class listener {
    TextField t = new TextField("c");
    public void init(){
        JFrame frame = new JFrame("a");
        Button button = new Button("Imput:b");
        frame.add(button);
        button.addActionListener(new Listener());
        frame.add(t,"North");
        frame.pack();
        frame.setVisible(true);
        frame.setDefaultCloseOperation(3);
    }
    public static void main(String[] args) {
        new listener().init();
    }
    private class Listener implements ActionListener{
        @Override
        public void actionPerformed(ActionEvent e){
            t.setText("b");
        }
    }
}
