//import java.lang.reflect.Array;
import java.awt.Frame;
import java.awt.TextField;
import static java.lang.String.format;
import java.util.*;
public class tic_tac_toc1 {
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
        private void reflash(){
            show_directions.setText(format("x:%x,y:%x",directions_of_x,directions_of_y));
        }
    }
    public static void main(String[] args) {
        //
    }
}
