//import java.lang.reflect.Array;
/*import java.awt.Button;
import java.awt.Dialog;
import java.awt.Frame;
import java.awt.Label;
import java.awt.TextField;*/
import java.awt.*;
import java.awt.event.MouseAdapter;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;
//import java.awt.event.WindowFocusListener;
//import java.awt.event.WindowListener;

import static java.lang.String.format;
import java.util.*;
import javax.swing.event.MenuEvent;
//import java.util.stream.Collectors;
public class tic_tac_toc {
    static private class game{
        Map<String, Integer> directions_map = new HashMap<>();
        int[] total_numbers_of_directions;
        private unit[] units;
        private class game_main{
            int number;
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
            //unit[] units;
            int[] locations;
            Panel panel2 = new Panel(new FlowLayout());
            Label lab_locations = new Label();
            Panel panel;
            private void reflash(){
                Object[] x_key = get_key(directions_map,directions_of_x);
                Object[] y_key = get_key(directions_map, directions_of_y);
                lab_locations.setText(java.util.Arrays.toString(locations));
                if(x_key.length == 0 || y_key.length == 0){
                    Dialog d = new Dialog(this,format("The %s not found.", x_key.length != y_key.length ? "direction is" : "directions are"),true);
                    d.add(new Label(format("The %s not found.", x_key.length != y_key.length ? "direction is" : "directions are")));
                    d.addWindowListener(new WindowAdapter() {
                        @Override
                        public void windowClosing(WindowEvent e) {
                            d.setVisible(false);
                        }
                    });
                    d.pack();
                    d.setVisible(true);
                    return;
                }
                System.out.println(directions_of_x == directions_of_y);
                if(/*x_key[0] == y_key[0]*/directions_of_x == directions_of_y){
                    //Dialog d = new Dialog(this,"The directions are the same.",true);
                    //d.add(new Label("The directions are the same."));
                    //d.addWindowListener(new WindowAdapter() {
                    //    @Override
                    //    public void windowClosing(WindowEvent e) {
                    //        d.setVisible(false);
                    //    }
                    //});
                    //d.pack();
                    //d.setVisible(true);
                    //return;
                    show_directions.setText("x,y:" + x_key[0]);
                    Button[] button_board = new Button[total_numbers_of_directions[directions_of_x]];
                    for (int i = 0; i < button_board.length; i++) {
                        try {
                            int DT = digital_transforming(total_numbers_of_directions,new int[]{directions_of_x},new int[]{i},locations);
                            if (exist(units, DT)) {
                                button_board[i] = new Button(units[DT].show);
                                final int fi = i;
                                button_board[i].addActionListener((_) -> {
                                    locations[directions_of_y] = fi;
                                    reflash();
                                });
                            }
                        } catch (Exception e) {
                            // TODO Auto-generated catch block
                            Dialog d = new Dialog(this,e.getMessage(),true);
                            d.add(new Label(e.getMessage()));
                            d.addWindowListener(new WindowAdapter() {
                                @Override
                                public void windowClosing(WindowEvent e){
                                    d.setVisible(false);
                                }
                            });
                            d.pack();
                            d.setVisible(true);
                        }
                    }
                    panel = new Panel(new GridLayout(1, total_numbers_of_directions[directions_of_x]));
                    for (Button i : button_board) {
                        panel.add(i != null ? i : new Label(""));
                    }
                }else{
                    //lab_locations.setText(java.util.Arrays.toString(locations)); 
                    int[] directions = {directions_of_x,directions_of_y};
                    show_directions.setText(format("x:%s,y:%s",x_key[0],y_key[0]));
                    //unit[][] board = new unit[total_numbers_of_directions[directions_of_x]][total_numbers_of_directions[directions_of_y]];
                    Button[][] button_board = new Button[total_numbers_of_directions[directions_of_x]][total_numbers_of_directions[directions_of_y]];
                    for (int i = 0;i<button_board.length;i++) {
                        for (int j = 0;j<button_board[i].length;j++){
                            int[] ij = {i,j};
                            try {
                                int DT = digital_transforming(total_numbers_of_directions,directions,ij,locations);
                                //System.out.println(DT);
                                if(exist(units, DT)){
                                        //try{
                                    button_board[i][j] = new Button(units[DT].show/*String.valueOf(j)*/);
                                    final int fi,fj;
                                    fi = i;
                                    fj = j;
                                    button_board[i][j].addActionListener((_) -> {
                                        locations[directions_of_x] = fi;
                                        locations[directions_of_y] = fj;
                                        reflash();
                                    });//}catch(Exception e){System.out.println(e.getMessage());}
                                    button_board[i][j].addMouseMotionListener(new MouseAdapter(){
                                        @Override
                                        public void mouseEntered(java.awt.event.MouseEvent e){
                                            try{
                                                Label text = new Label(java.util.Arrays.toString(digital_to_array_transforming(total_numbers_of_directions, i));
                                            } catch (Exception e) {
                                                // TODO Auto-generated catch block
                                                Dialog d = new Dialog(this,e.getMessage(),true);
                                                d.add(new Label(e.getMessage()));
                                                d.addWindowListener(new WindowAdapter() {
                                                    @Override
                                                    public void windowClosing(WindowEvent e){
                                                    d.setVisible(false);
                                                }
                                            });
                                            d.pack();
                                            d.setVisible(true);
                                            }
                                        }
                                    });
                                }
                            } catch (Exception e) {
                                //System.out.println(e.getMessage());
                                Dialog d = new Dialog(this,e.getMessage(),true);
                                d.add(new Label(e.getMessage()));
                                d.addWindowListener(new WindowAdapter() {
                                    @Override
                                    public void windowClosing(WindowEvent e){
                                        d.setVisible(false);
                                    }
                                });
                                d.pack();
                                d.setVisible(true);
                            }
                        }
                    }
                    //System.out.println("000");
                    panel = new Panel(new GridLayout(total_numbers_of_directions[directions_of_x], total_numbers_of_directions[directions_of_y]));
                    for (Button[] i : button_board) {
                        for (Button j : i) {
                            panel.add(j != null ? j : new Label(""));
                        }
                    }
                }
                this.removeAll();
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
                            d.addWindowListener(new WindowAdapter() {
                                @Override
                                public void windowClosing(WindowEvent e){
                                    d.setVisible(false);
                                }
                            });
                            d.setVisible(true);
                            //System.out.println("000");
                            return;
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
                    this.addWindowListener(new WindowAdapter() {
                        @Override
                        public void windowClosing(WindowEvent e){
                            change_direction.this.setVisible(false);
                        }
                    });
                }
            }
            public show(int x,int y,int number) throws HeadlessException {
                directions_of_x = x;
                directions_of_y = y;
                //this.number = number;
                units = new unit[number];
                locations = new int[total_numbers_of_directions.length];
                for (int i = 0; i < units.length; i++) {
                    try {
                        //System.out.println(java.util.Arrays.toString(digital_to_array_transforming(total_numbers_of_directions, i)));
                        units[i] = new unit(),i);
                    } catch (Exception e) {
                        Dialog d = new Dialog(this,e.getMessage(),true);
                        d.add(new Label(e.getMessage()));
                        d.addWindowListener(new WindowAdapter() {
                            @Override
                            public void windowClosing(WindowEvent e){
                                d.setVisible(false);
                            }
                        });
                        d.pack();
                        d.setVisible(true);
                    }
                }
                panel2.add(show_directions);
                panel2.add(change_direction_button);
                panel2.add(lab_locations);
                        //System.out.println("000");
                change_direction_button.addActionListener((_) -> {change_direction.setVisible(true);});
                reflash();
                this.addWindowListener(new WindowAdapter() {
                    @Override
                    public void windowClosing(WindowEvent e){
                        //show.this.setVisible(false);
                        show.this.dispose();
                    }
                });
            }
        }
        @SuppressWarnings("ImplicitArrayToString")
        private int digital_transforming(int[] digitals,int[] choosen_digitals,int[] numbers,int[] locations) throws Exception{
            if (choosen_digitals.length != numbers.length) {throw new Exception("The number of choosen digitals is not equals to \"numbers\".");}
            if (digitals.length != locations.length) {throw new Exception("The number of digitals is not equals to locations'.");}
            if (choosen_digitals.length > digitals.length){throw new Exception("The number of choosen digitals is more than the number of digitals.");}
            int[] table = digitals.clone();
            int result = 0;
            System.out.println("in");
            for (int i = 1; i < digitals.length; i++) {
                table[i] *= table[i-1];
            }
            int ii = 0;
            for (int i : choosen_digitals) {
                System.out.println(ii + "," + choosen_digitals.toString());
                result += (numbers[ii] - locations[i])*((i != 0) ? table[i-1] : 1);
                ii++;
            }
            for(int i =0 ; i < locations.length ; i++){
                result += locations[i]*((i != 0) ? table[i-1] : 1);
            }
            return result;
        }
        @SuppressWarnings("ImplicitArrayToString")
        private int[] digital_to_array_transforming(int[] digitals,int locations) throws Exception{
            //if (choosen_digitals.length != numbers.length) {throw new Exception("The number of choosen digitals is not equals to \"numbers\".");}
            //if (digitals.length != locations.length) {throw new Exception("The number of digitals is not equals to locations'.");}
            int[] table = digitals.clone();
            int[] result = new int[digitals.length];
            System.out.println("in");
            for (int i = 1; i < digitals.length; i++) {
                table[i] *= table[i-1];
            }
            for (int i = 0 ; i < digitals.length ; i++) {
                System.out.println(i + "," + digitals.toString());
                result[i] = (int)(locations%table[i] / ((i == 0) ? 1 : table[i-1]));
            }
            System.out.println(locations + "," + java.util.Arrays.toString(result));
            return result;
        }//印記憶體是刻意的
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
            if(units == null || units.length == 0){return false;}
            //System.out.println(units[0]);
            for (unit i : units) {
                if(i != null && i.directions == value ){
                    result = true;
                }
            }//try{}catch(Exception e){System.out.println(e.getMessage());}//throw new Exception();
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
        game.total_numbers_of_directions = new int[]{2,3,3};
        try{
            game.show a = game.new show(0,1,18);
            a.setVisible(true);
            //System.exit(0);
        }catch(Exception _){}
    }
}
