//import java.lang.reflect.Array;
import java.util.*;
public class tic_tac_toc0 {
    private ArrayList<String> directions;
    private class game_main{
        int number;
        private unit[] units;
        @SuppressWarnings("unused")
        private void start(ArrayList<String> directions, int total, unit[] edits){
            number = (int)Math.pow((double)directions.size(), (double)total)-edits.length;
            units = new unit[number];
            for(int i = 0;i == units.length;i++){
                units[i] = new unit();
            }
        }
        private class unit{
            @SuppressWarnings("unused")
            private ArrayList<Integer> directions = new ArrayList<>();
            @SuppressWarnings("unused")
            private String show;
            private unit(String show,ArrayList<Integer> directions){
                this.show = show;
                this.directions = directions;
            }
            @SuppressWarnings({"unused"})
            private unit(ArrayList<Integer> directions){
                this(null, directions);
            }
            @SuppressWarnings({"unused"})
            private unit(String show){
                this(show,null);
            }
            @SuppressWarnings({"unused"})
            private unit(){
                this(null, null);
            }
        }
    }
    public static void main(String[] args) {
        //
    }
}
