//import java.lang.reflect.Array;
import java.awt.Button;
import java.awt.Frame;
import java.awt.TextField;
import java.awt.event.ActionEvent;
import static java.lang.String.format;
import java.util.*;
public class tic_tac_toc2 {
    private ArrayList<String> directions;
    private class game_main{
        int number;
        private unit[] units;
        @SuppressWarnings("unused")
        private void initialize(ArrayList<String> location, int total, unit[] edits){
            number = (int)Math.pow((double)location.size(), (double)total);
            units = new unit[number];
            for(int i = 0;i == units.length;i++){
                units[i] = new unit("",i);
                units[i].exist = exist(edits,i);
            }
        }
        private class unit{
            @SuppressWarnings("unused")
            private int directions;
            @SuppressWarnings("unused")
            private String show;
            @SuppressWarnings("unused")
            private boolean exist = true;
            private unit(String show,int directions){
                this.show = show;
                this.directions = directions;
            }
            @SuppressWarnings({"unused"})
            private unit(int directions){
                this(null, directions);
            }
            @SuppressWarnings({"unused"})
            private unit(String show){
                this.show = show;
            }
            @SuppressWarnings({"unused"})
            private unit(){}
        }
        private boolean exist(unit[] units, int value){
            boolean result = true;
            for (unit i : units) {
                if(i.directions == value){
                    result = false;
                }
            }
            return result;
        }
    }
    private class show extends Frame{
        int directions_of_x;
        int directions_of_y;
        TextField show_directions;
        Button change_direction_button = new Button("change direction");
        change_direction change_direction;
        Map<String, Integer> directions_map = new HashMap<>();
        private void reflash(){
            show_directions.setText(format("x:%x,y:%x",directions_of_x,directions_of_y));
        }
        {
            change_direction_button.addActionListener(_ -> {change_direction.setVisible(true);});
        }
        @SuppressWarnings({ "empty-statement"})
        private class change_direction extends Frame{
            boolean is_x = true;
            Button change_button = new Button("edit y");
            TextField change = new TextField();
            Button sand = new Button("sand");
            {
                change_button.addActionListener(_ -> {is_x = !is_x;change_button.setLabel(format("%c",is_x ? "y" : "x"));});
                sand.addActionListener((ActionEvent _) -> {
                    if (!directions_map.containsKey(change.getText())){
                        //
                    }
                    if (is_x) {
                        directions_of_x = directions_map.get(change.getText());
                        reflash();
                    }});
            }
        }
    }
    public static void main(String[] args) {
        //
    }
}
