//import java.lang.reflect.Array;
/*import java.awt.Button;
import java.awt.Dialog;
import java.awt.Frame;
import java.awt.Label;
import java.awt.TextField;*/
import java.awt.*;
import static java.lang.String.format;
import java.util.*;
//import java.util.stream.Collectors;
public class tic_tac_toc15 {
    static private class game{
        Map<String, Integer> directions_map = new HashMap<>();
        int[] total_numbers_of_directions;
        private class game_main{
            int number;
            private unit[] units;
            @SuppressWarnings("unused")
            private void initialize(String[] location, int total, unit[] edits){
                number = (int)Math.pow((double)location.length, (double)total);
                units = new unit[number];
                for(int i = 0;i < units.length;i++){
                    units[i] = new unit("",i);
                    units[i].exist = !exist(edits,i);
                }
            }
        }
        class show extends Frame{
            int directions_of_x;
            int directions_of_y;
            Label show_directions = new Label("");
            Button change_direction_button = new Button("change direction");
            change_direction change_direction = new change_direction();
            //int number;
            unit[] units;
            int[] locations;
            Panel panel2 = new Panel(new FlowLayout());
            private void reflash(){
                int[] directions = {directions_of_x,directions_of_y};
                show_directions.setText(format("x:%s,y:%s",get_key(directions_map,directions_of_x)[0],get_key(directions_map, directions_of_y)[0]));
                unit[][] board = new unit[total_numbers_of_directions[directions_of_x]][total_numbers_of_directions[directions_of_y]];
                Button[][] button_board = new Button[total_numbers_of_directions[directions_of_x]][total_numbers_of_directions[directions_of_y]];
                for (int i = 0;i<board.length;i++) {
                    for (int j = 0;j<board[i].length;j++){
                        int[] ij = {i,j};
                        try {
                            if(exist(units, digital_transforming(total_numbers_of_directions,directions,ij,locations))){
                                    //try{
                                button_board[i][j] = new Button("");
                                final int fi,fj;
                                fi = i;
                                fj = j;
                                button_board[i][j].addActionListener((_) -> {
                                    locations[directions_of_x] = fi;
                                    locations[directions_of_y] = fj;
                                    reflash();
                                });//}catch(Exception e){System.out.println(e.getMessage());}
                            }
                        } catch (Exception e) {
                            //System.out.println(e.getMessage());
                            Dialog d = new Dialog(this,e.getMessage(),true);
                            d.add(new Label(e.getMessage()));
                            d.setVisible(true);
                            d.pack();
                        }
                    }
                }
                Panel panel = new Panel(new GridLayout(total_numbers_of_directions[directions_of_x], total_numbers_of_directions[directions_of_y]));
                for (Button[] i : button_board) {
                    for (Button j : i) {
                        panel.add(j != null ? j : new Label(""));
                    }
                }
                this.add(panel,BorderLayout.CENTER);
                this.add(panel2,BorderLayout.SOUTH);
                this.pack();
            }
            @SuppressWarnings({ "empty-statement"})
            private class change_direction extends Frame{
                boolean is_x = true;
                Button change_button = new Button("edit y");
                TextField change = new TextField();
                Button sand = new Button("sand");
                Label witch_is_being_edited = new Label("x");
                Panel panel1 = new Panel(new FlowLayout());
                Panel panel2 = new Panel(new FlowLayout());
                {
                    change_button.addActionListener((_) -> {
                        is_x = !is_x;
                        change_button.setLabel(is_x ? "edit y" : "edit x");
                        witch_is_being_edited.setText(is_x ? "x" : "y");
                    });
                    sand.addActionListener((_) -> {
                        if (!directions_map.containsKey(change.getText())){
                            Dialog d = new Dialog(this,"not find the direction",true);
                            d.add(new Label("not find the direction"));
                            d.pack();
                            d.setVisible(true);
                        }
                        if (is_x) {
                            directions_of_x = directions_map.get(change.getText());
                        }else{
                            directions_of_y = directions_map.get(change.getText());
                        }
                        reflash();
                    });
                    panel1.add(change);
                    panel1.add(sand);
                    panel2.add(change_button);
                    panel2.add(witch_is_being_edited);
                    this.add(panel1,BorderLayout.CENTER);
                    this.add(panel2,BorderLayout.SOUTH);
                    this.pack();
                }
            }
            public show(int x,int y,int number) throws HeadlessException {
                directions_of_x = x;
                directions_of_y = y;
                //this.number = number;
                units = new unit[number];
                locations = new int[total_numbers_of_directions.length];
                panel2.add(show_directions);
                panel2.add(change_direction_button);
                change_direction_button.addActionListener((_) -> {change_direction.setVisible(true);});
                reflash();
            }
        }
        @SuppressWarnings("ImplicitArrayToString")
        private int digital_transforming(int[] digitals,int[] choosen_digitals,int[] numbers,int[] locations) throws Exception{
            if (choosen_digitals.length != numbers.length) {throw new Exception("The number of choosen digitals is not equals to \"numbers\".");}
            if (digitals.length != locations.length) {throw new Exception("The number of digitals is not equals to locations'.");}
            try{int[] table = digitals.clone();
            int result = 0;
            System.out.println("in");
            for (int i = 1; i < digitals.length; i++) {
                table[i] *= table[i-1];
            }
            int ii = 0;
            for (int i : choosen_digitals) {
                System.out.println(ii + "," + choosen_digitals.toString());
                result += numbers[ii]*((i != 0) ? table[i-1] : 1) - locations[i];
                ii++;
            }
            for(int i : locations){
                result += i;
            }
            return result;}catch(Exception e){System.out.println(e.getMessage());}throw new Exception();
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
            boolean result = false;
            if(units == null){return false;}
            for (unit i : units) {
                if(i.directions == value ){
                    result = true;
                }
            }
            return result;
        }
        /*@SuppressWarnings("unused")
        private int find(Object[] objects, Object object){
            int result = -1;
            for (int i = 0 ; i < objects.length ; i++) {
                if(objects[i].equals(object)){
                    result = i;
                }
            }
            return result;
        }
        @SuppressWarnings("empty-statement")
        private int[] find2(Object[][] objects, Object object){
            int[] result = {-1,-1};
            for (int i = 0 ; i < objects.length ; i++) {
                for (int j = 0; j < objects[i].length; j++) {
                    if(objects[i][j].equals(object)){
                        int[] k = {i,j};
                        result = k;
                    }
                }
            }
            return result;
        }*/
        public game(){
            //
        }
    }
    @SuppressWarnings("unused")
    public static Object[] get_key(Map<Object,Object> map, Object value){
        return map.entrySet().stream()
                             .filter(o -> o.getValue() == value)
                             .map(Map.Entry::getKey)
                             .toArray(Object[]::new);
    }
    public static Object[] get_key(Map<String,Integer> map, int value){
        return map.entrySet().stream()
                             .filter(o -> o.getValue() == value)
                             .map(Map.Entry::getKey)
                             .toArray(Object[]::new);
    }
    @SuppressWarnings("UseSpecificCatch")
    public static void main(String[] args) {
        game game = new game();
        game.directions_map.put("x", 0);
        game.directions_map.put("y", 1);
        game.directions_map.put("z", 2);
        int[] a2_3 = {2,2,2};
        game.total_numbers_of_directions = a2_3;
        try{
            game.show a = game.new show(0,1,8);
            a.setVisible(true);
        }catch(Exception _){}
    }
}
