//import java.lang.reflect.Array;
/*import java.awt.Button;
import java.awt.Dialog;
import java.awt.Frame;
import java.awt.Label;
import java.awt.TextField;*/
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.KeyEvent;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import static java.awt.event.MouseEvent.BUTTON3;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;
import static java.lang.String.format;
//import java.lang.reflect.Array;
import java.util.*;
//import java.util.Arrays.*;
//import static java.util.Arrays.asList;
//import javax.swing.plaf.basic.BasicBorders;
//import javax.swing.event.MenuEvent;
//import java.util.Arrays.*;
//import javax.swing.event.MenuEvent;
//import java.util.stream.Collectors;
public class tic_tac_toc36 {
    private final char[] EMPTY_BUIDER = new char[]{'-','.','*','#','@','~','`','+','%','$'};
    static private class game{
        Map<String, Integer> directions_map = new HashMap<>();
        int[] total_numbers_of_directions;
        private unit[] units;
        int number = 1;
        private ArrayList<String> players;
        private StringBuilder empty_builder;
        private String empty;
        private class game_main{
            game_show game_show;
            @SuppressWarnings("unused")
            private void initialize(String[] location, int[] total, unit[] edits, ArrayList<String> players){
                total_numbers_of_directions = total;
                game.this.players = players;
                while (!game.this.players.parallelStream().filter(i -> i.equals(empty)).findAny().isEmpty()) { 
                    //
                }
                for (int i = 0;i < location.length;i++) {
                    directions_map.put(location[i], i);
                }
                for(int i : total_numbers_of_directions){number *= i;}
                units = new unit[number];
                for(int i = 0;i < units.length;i++){
                    units[i] = new unit("",i);
                    units[i].exist = !exist_in_Array(edits,i);
                }
                game_show = new game_show(0,1,number);
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
            Label text = new Label();
            Window window;
            Button move_to = new Button("move to");
            move_setting move_setting = new move_setting();
            @SuppressWarnings("unused")
            boolean first = true;
            Button[][] button_board;
            private void reflash(){
                action_before_reflash(game.this);
                Object[] x_key = get_key(directions_map,directions_of_x);
                Object[] y_key = get_key(directions_map, directions_of_y);
                lab_locations.setText(Arrays.toString(locations));
                change_direction.change.removeAll();
                for (String i : directions_map.keySet()) {
                    change_direction.change.add(i);
                }
                move_setting.choose_direction.removeAll();
                for (String i : directions_map.keySet()) {
                    move_setting.choose_direction.add(i);
                }
                if(x_key.length == 0 || y_key.length == 0){
                    Dialog d = new Dialog(this,format("%s not found.", x_key.length != y_key.length ? "One of the directions is" : "Both directions are"),true);
                    d.add(new Label(format("%s not found.", x_key.length != y_key.length ? "One of the directions is" : "Both directions are")));
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
                    button_board = new Button[1][total_numbers_of_directions[directions_of_x]];
                    for (int i = 0; i < button_board[0].length; i++) {
                        try {
                            final int DT = digital_transforming(total_numbers_of_directions,new int[]{directions_of_x},new int[]{i},locations);
                            if (exist_in_Array(units, DT)) {
                                button_board[0][i] = new Button(label_of_button(game.this));
                                final int fi = i;
                                button_board[0][i].addActionListener((_) -> {
                                    locations[directions_of_y] = fi;
                                    reflash();
                                });
                                button_board[0][i].addMouseListener(new MouseAdapter(){
                                    @Override
                                    public void mouseEntered(MouseEvent e){
                                        show_tip(e, DT);
                                    }
                                    @Override
                                    public void mouseExited(MouseEvent e){
                                        window.setVisible(false);
                                    }
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
                    for (Button i : button_board[0]) {
                        panel.add(i != null ? i : new Label(""));
                    }
                }else{
                    //lab_locations.setText(java.util.Arrays.toString(locations)); 
                    int[] directions = {directions_of_x,directions_of_y};
                    show_directions.setText(format("x:%s,y:%s",x_key[0],y_key[0]));
                    //unit[][] board = new unit[total_numbers_of_directions[directions_of_x]][total_numbers_of_directions[directions_of_y]];
                    button_board = new Button[total_numbers_of_directions[directions_of_x]][total_numbers_of_directions[directions_of_y]];
                    for (int i = 0;i<button_board.length;i++) {
                        for (int j = 0;j<button_board[i].length;j++){
                            int[] ij = {i,j};
                            try {
                                int DT = digital_transforming(total_numbers_of_directions,directions,ij,locations);
                                //System.out.println(DT);
                                if(exist_in_Array(units, DT)){
                                        //try{
                                    button_board[i][j] = new Button(units[DT].show/*String.valueOf(j)*/);
                                    final int fi,fj;
                                    fi = i;
                                    fj = j;
                                    button_board[i][j].addActionListener(e -> {
                                        action_before_change_the_location(e, game.this);
                                        locations[directions_of_x] = fi;
                                        locations[directions_of_y] = fj;
                                        action_after_change_the_location(e, game.this);
                                        reflash();
                                    });//}catch(Exception e){System.out.println(e.getMessage());}
                                    button_board[i][j].addMouseListener(new MouseAdapter(){
                                        @Override
                                        public void mouseEntered(MouseEvent e){
                                            show_tip(e, DT);
                                        }
                                        @Override
                                        public void mouseExited(MouseEvent ee){
                                            window.setVisible(false);
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
                action_after_reflash(game.this);
                first = false;
            }
            @SuppressWarnings({ "empty-statement"})
            protected class change_direction extends Frame{
                boolean is_x = true;
                Button change_button = new Button("edit y");
                Choice change = new Choice();
                Button send = new Button("send");
                Label witch_is_being_edited = new Label("x");
                Panel panel1 = new Panel(new FlowLayout());
                Panel panel2 = new Panel(new FlowLayout());
                {
                    change_button.addActionListener((_) -> {
                        is_x = !is_x;
                        change_button.setLabel(is_x ? "edit y" : "edit x");
                        witch_is_being_edited.setText(is_x ? "x" : "y");
                    });
                    send.addActionListener((_) -> {
                        if (change.getSelectedItem() == null){
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
                            directions_of_x = directions_map.get(change.getSelectedItem());
                        }else{
                            directions_of_y = directions_map.get(change.getSelectedItem());
                        }
                        reflash();
                    });
                    for (String i : directions_map.keySet()) {
                        change.add(i);
                    }
                    panel1.add(change);
                    panel1.add(send);
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
            protected class move_setting extends Frame{
                Button send = new Button("send");
                TextField chosen_locations = new TextField();
                Choice choose_direction = new Choice();
                {
                    send.addActionListener((_) -> {
                        /*try{
                        System.out.println(digital_transforming(total_numbers_of_directions, 
                                                                        new int[]{directions_map.get(choose_direction.getSelectedItem())}, 
                                                                        new int[]{par}, 
                                                                        locations));}catch(Exception e){System.out.println(e.getMessage());}*/
                        //try{
                        try {
                            String location = chosen_locations.getText();
                            if(location.equals("") || location.isEmpty() || location.isBlank()){return;}
                            final int par = Integer.parseInt(location);
                            if(choose_direction.getSelectedItem() == null || 
                            !exist_in_Array(units, digital_transforming(total_numbers_of_directions, 
                                                                        new int[]{directions_map.get(choose_direction.getSelectedItem())}, 
                                                                        new int[]{par}, 
                                                                        locations)))
                            {return;}
                            locations[directions_map.get(choose_direction.getSelectedItem())] = par;
                            reflash();
                        //}catch(Exception e){}
                        }catch(NumberFormatException _){/*return;*/} catch (Exception e) {
                            // TODO Auto-generated catch block
                            Dialog d = new Dialog(show.this,e.getMessage(),true);
                            d.add(new Label(e.getMessage()));
                            d.addWindowListener(new WindowAdapter() {
                                @Override
                                public void windowClosing(WindowEvent e){
                                    d.setVisible(false);
                                }
                            });
                            d.pack();
                            d.setVisible(true);
                            //return;
                        }
                    });
                    for (String i : directions_map.keySet()) {
                        choose_direction.add(i);
                    }
                    this.setLayout(new FlowLayout(FlowLayout.LEFT,10,10));
                    this.add(choose_direction);
                    this.add(chosen_locations);
                    this.add(send);
                    this.addWindowListener(new WindowAdapter() {
                        @Override
                        public void windowClosing(WindowEvent e){
                            move_setting.this.setVisible(false);
                        }
                    });
                    this.pack();
                }
            }
            protected void show_tip(MouseEvent e,int DT){
                try{
                    text.setText(Arrays.toString(digital_to_array_transforming(total_numbers_of_directions, DT)));
                    window.removeAll();
                    window.add(text);
                    window.pack();
                    window.setLocation(e.getLocationOnScreen().x,e.getLocationOnScreen().y + 15);
                    window.setVisible(true);
                } catch (Exception ee) {
                    //System.out.println(ee.getMessage()+".");
                      //  System.out.println(format("Connot read field \"x\" because the return value of \"%s\" is null", "tic_tac_toc$game$show.getMousePosition()"));
                    //System.out.println(ee.getMessage().equals(format("Connot read field \"x\" because the return value of \"%s\" is null", "tic_tac_toc$game$show.getMousePosition()")));//why false
                    // TODO Auto-generated catch block
                    Dialog d = new Dialog(show.this,ee.getMessage(),true);
                    d.add(new Label(ee.getMessage()));
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
            protected String label_of_button(game game){return "";}
            public show(int x,int y,int number) throws HeadlessException {
                directions_of_x = x;
                directions_of_y = y;
                window = new Window(show.this);
                //this.number = number;
                locations = new int[total_numbers_of_directions.length];
                /*for (int i = 0; i < units.length; i++) {
                    try {
                        //System.out.println(java.util.Arrays.toString(digital_to_array_transforming(total_numbers_of_directions, i)));
                        units[i] = new unit(i);
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
                }*/
                panel2.add(show_directions);
                panel2.add(change_direction_button);
                panel2.add(lab_locations);
                panel2.add(move_to);
                        //System.out.println("000");
                change_direction_button.addActionListener((_) -> {change_direction.setVisible(true);});
                reflash();
                this.addWindowListener(new WindowAdapter() {
                    @Override
                    public void windowClosing(WindowEvent e){
                        //show.this.setVisible(false);
                        if(window != null){
                            window.dispose();
                        }
                        show.this.dispose();
                    }
                });
                move_to.addActionListener((_) -> {move_setting.setVisible(true);});
            }
            @SuppressWarnings("unused")
            protected void action_before_change_the_location(ActionEvent e, game game){}
            @SuppressWarnings("unused")
            protected void action_after_change_the_location(ActionEvent e, game game){}
            @SuppressWarnings("unused")
            protected void action_before_reflash(game game){}
            @SuppressWarnings("unused")
            protected void action_after_reflash(game game){}
        }
        @SuppressWarnings("ImplicitArrayToString")
        private int digital_transforming(int[] digitals,int[] choosen_digitals,int[] numbers,int[] locations) throws Exception{
            if (choosen_digitals.length != numbers.length) {throw new Exception("The number of choosen digitals is not equals to \"numbers\".");}
            if (digitals.length != locations.length) {throw new Exception("The number of digitals is not equals to locations'.");}
            if (choosen_digitals.length > digitals.length){throw new Exception("The number of choosen digitals is more than the number of digitals.");}
            int errors = 0;
            for (int i = 0; i < numbers.length; i++) {
                if(choosen_digitals[i] >= digitals.length){errors++;continue;}
                if(digitals[choosen_digitals[i]] <= numbers[i] || numbers[i] < 0){throw new Exception("The location in not exist.");}
            }
            if(errors == 1){throw new Exception("One of the chosen digitals is not exist.");}
            if(errors > 1){throw new Exception("Some of the chosen digitals are not exist.");}
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
            System.out.println(locations + "," + Arrays.toString(result));
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
        private boolean exist_in_Array(unit[] units, int value){
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
        private class edit extends show{
            private final PopupMenu chose = new PopupMenu();
            private final MenuItem edit = new MenuItem("edit", new MenuShortcut(KeyEvent.VK_E, false));
            //private final MenuBar menuBar = new MenuBar();
            @Override
            protected void action_after_reflash(game game){
                for (Button[] i : button_board) {
                    for (Button j : i) {
                        j.addMouseListener(new MouseAdapter() {//Button是新的
                            @Override
                            public void mousePressed(MouseEvent e){
                                if(e.getButton() == BUTTON3){
                                    //System.out.println(e);
                                    chose.show(e.getComponent(), e.getX(), e.getY());
                                }
                            }
                        });
                    }
                }
            }
            public edit(int x, int y, int number){
                super(x, y, number);
                chose.add(edit);
                //menuBar.add(chose);
                this.add(chose);
            }
        }
        private class game_show extends show{
            Button edit_button = new Button("edit");
            edit edit = new edit(directions_of_x, directions_of_y, number);
            Panel panel3 = new Panel(new BorderLayout());
            public game_show(int x, int y, int number){
                super(x, y, number);
                edit_button.addActionListener((_) -> {
                    edit.setVisible(true);
                });
                panel3.add(edit_button, BorderLayout.EAST);
                this.add(panel3, BorderLayout.NORTH);
                this.pack();
            }
            @Override
            protected void action_after_reflash(game game){
                System.out.println(first);
                if(!first){
                    this.add(panel3, BorderLayout.NORTH);
                }
                this.pack();
                //
                //this.pack();
            }
        }
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
        ArrayList<String> players = new ArrayList<>();
        players.add("O");players.add("X");
        try{
            game.game_main game_main = game.new game_main();
            game_main.initialize(new String[]{"x","y","z"},new int[]{2,3,3}, null, players);
            game_main.game_show.setVisible(true);
            //System.exit(0);
        }catch(Exception _){}
    }
}
