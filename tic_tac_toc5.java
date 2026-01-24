//import java.lang.reflect.Array;
/*import java.awt.Button;
import java.awt.Dialog;
import java.awt.Frame;
import java.awt.Label;
import java.awt.TextField;*/
import java.awt.*;
import static java.lang.String.format;
import java.util.*;
public class tic_tac_toc5 {
    Map<String, Integer> directions_map = new HashMap<>();
    int[] total_numbers_of_directions;
    int[] digital_of_directions;
    private class game_main{
        int number;
        private unit[] units;
        @SuppressWarnings("unused")
        private void initialize(String[] location, int total, unit[] edits){
            number = (int)Math.pow((double)location.length, (double)total);
            units = new unit[number];
            for(int i = 0;i == units.length;i++){
                units[i] = new unit("",i);
                units[i].exist = exist(edits,i);
            }
        }
    }
    private class show extends Frame{
        int directions_of_x;
        int directions_of_y;
        TextField show_directions;
        Button change_direction_button = new Button("change direction");
        change_direction change_direction;
        int number;
        unit[] units =  new unit[number];
        int[] directions = {directions_of_x,directions_of_y};
        private void reflash(){
            show_directions.setText(format("x:%x,y:%x",directions_of_x,directions_of_y));
            unit[][] board = new unit[total_numbers_of_directions[directions_of_x]][total_numbers_of_directions[directions_of_y]];
            Button[][] button_board = new Button[total_numbers_of_directions[directions_of_x]][total_numbers_of_directions[directions_of_y]];
            for (int i = 0;i<board.length;i++) {
                for (int j = 0;j<board[i].length;j++){
                    int[] ij = {i,j};
                    try {
                        if(exist(units, digital_transforming(digital_of_directions,directions,ij))){button_board[i][j] = new Button("");}
                    } catch (Exception e) {
                        new Dialog(this,e.getMessage(),true).setVisible(true);
                    }
                }
            }
        }
        {
            change_direction_button.addActionListener((_) -> {change_direction.setVisible(true);});
            reflash();
        }
        @SuppressWarnings({ "empty-statement"})
        private class change_direction extends Frame{
            boolean is_x = true;
            Button change_button = new Button("edit y");
            TextField change = new TextField();
            Button sand = new Button("sand");
            Label witch_is_being_edited = new Label("x");
            {
                change_button.addActionListener((_) -> {
                    is_x = !is_x;
                    change_button.setLabel(format("%c",is_x ? "y" : "x"));
                    witch_is_being_edited.setText(format("%x", is_x ? "x" : "y"));
                });
                sand.addActionListener((_) -> {
                    if (!directions_map.containsKey(change.getText())){
                        new Dialog(this,"not find the direction",true).setVisible(true);
                    }
                    if (is_x) {
                        directions_of_x = directions_map.get(change.getText());
                    }else{
                        directions_of_y = directions_map.get(change.getText());
                    }
                    reflash();
                });
            }
        }
    }
    private int digital_transforming(int[] digitals,int[] choosen_digitals,int[] numbers) throws Exception{
        if (choosen_digitals.length != numbers.length) {throw new Exception("The number of choosen digitals is not equals to \"numbers\".");}
        int[] table = digitals.clone();
        int result = 0;
        for (int i = 1; i < digitals.length; i++) {
            table[i] *= table[i-1];
        }
        for (int i : choosen_digitals) {
            result += numbers[i]*((i != 0) ? table[i-1] : 1);
        }
        return result;
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
    public static void main(String[] args) {
        //
    }
}
