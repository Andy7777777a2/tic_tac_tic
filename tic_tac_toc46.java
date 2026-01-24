//import java.lang.reflect.Array;
/*import java.awt.Button;
import java.awt.Dialog;
import java.awt.Frame;
import java.awt.Label;
import java.awt.TextField;*/
import java.awt.*;
import java.awt.event.ActionEvent;
//import java.awt.event.ItemEvent;
import java.awt.event.KeyEvent;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import static java.awt.event.MouseEvent.BUTTON3;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;
//import java.io.File;
import static java.lang.String.format;
//import java.nio.file.Path;
//import java.lang.reflect.Array;
import java.util.*;
//import java.util.function.Supplier;
//import javax.swing.border.EmptyBorder;
//import java.util.Arrays.*; 
//import static java.util.Arrays.asList;
//import javax.swing.plaf.basic.BasicBorders;
//import javax.swing.event.MenuEvent;
//import java.util.Arrays.*;
//import javax.swing.event.MenuEvent;
//import java.util.stream.Collectors;
import java.util.function.IntSupplier;
//import javax.lang.model.type.UnionType;
//import javax.swing.border.EmptyBorder;
import java.nio.file.*;
import java.io.*;
public class tic_tac_toc46 {
    private final static char[] EMPTY_BUIDER = new char[]{'-','.','*','#','@','~','`','+','%','$'};
    static private class game{
        public class file_util/* * / implements AutoCloseable */{
            public static Path creat_name(Path path, String name, String string) throws IOException{
                if(string == null){string = "";}
                if(!string.isEmpty() && !string.startsWith(".")){string = string + ".";}
                int counter = 0;
                while(true){
                    String file_name = format("%s_%02d_%s",name,counter,string);
                    Path candidate = path.resolve(file_name);
                    try {
                        return Files.createFile(candidate);
                    } catch (FileAlreadyExistsException e) {
                        // TODO: handle exception
                        counter++;
                    }
                }
            }
            public static Path creat_directory_name(Path path, String directioy) throws IOException{
                int counter = 0;
                while (true) {
                    String name = format("%s_%02d", directioy, counter);
                    Path candidate = path.resolve(name);
                    try {
                        return Files.createDirectories(candidate);
                    } catch (FileAlreadyExistsException e) {
                        counter++;
                    }
                }
            }
        }
        class game_id_creater{
            private static final String RUN_ID = java.time.Instant.now().toString() + "_" + System.nanoTime();
            private static final java.util.concurrent.atomic.AtomicInteger COUNTER = 
                             new java.util.concurrent.atomic.AtomicInteger(0);
            public static final String NEXT_GAME_ID(){
                String t = java.time.Instant.now().toString();
                int n = COUNTER.getAndIncrement();
                return RUN_ID + "-" + t + "-" + n;
            }
        }
        class game_logger{
            private final Path root_directory;
            private final String game_id;
            private final Path game_directory;

            game_logger(Path root_directory, String game_id) throws IOException{
                Files.createDirectories(root_directory);
                this.root_directory = root_directory;
                this.game_id = game_id;
                this.game_directory = file_util.creat_directory_name(root_directory, "game-" + game_id);
            }
            Path new_log(String category) throws IOException{
                return file_util.creat_name(game_directory,  category, ".log");
            }
        }
        Map<String, Integer> directions_map = new HashMap<>();
        int[] total_numbers_of_directions;
        private unit[] units;
        int number = 1;
        private ArrayList<String> players;
        private String empty;
        private String DNE;
        //private class game_main{
        game_show game_show;
        @SuppressWarnings({"unused", "Convert2Lambda"})
        private void initialize(String[] location, int[] total, unit[] edits, ArrayList<String> players){
            total_numbers_of_directions = total;
            game.this.players = players;
            try {
                empty = "";
                ArrayList<String> dne = new ArrayList<>(game.this.players);
                dne.add(empty);
                DNE = get_a_unused_string(dne);
            } catch (Exception e) {
                Dialog d = new Dialog((Frame)null,e.getMessage(),true);
                d.add(new Label(e.getMessage()));
                d.addWindowListener(new WindowAdapter() {
                    @Override
                    public void windowClosing(WindowEvent e){
                        d.setVisible(false);
                        System.exit(0);
                    }
                });
                d.pack();
                d.setVisible(true);
            }
                //if (empty.equals("-")) {
                //    empty = "\uFEFF-";
                //}
            if (DNE.equals("-")) {
                DNE = "\uFEFF-";
            }
            System.out.println(empty);
            System.out.println(DNE);
            for (int i = 0;i < location.length;i++) {
                directions_map.put(location[i], i);
            }
            for(int i : total_numbers_of_directions){number *= i;}
            units = new unit[number];
            for(int i = 0;i < units.length;i++){
                units[i] = new unit(empty,i);
                units[i].exist = !exist_in_Array(edits,i);
            }
            game_show = new game_show(0,1,number);
        }
    //    }
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
            Component[][] button_board;
            protected void reflash(){
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
                    button_board = new Component[1][total_numbers_of_directions[directions_of_x]];
                    for (int i = 0; i < button_board[0].length; i++) {
                        try {
                            final int DT = digital_transforming(total_numbers_of_directions,new int[]{directions_of_x},new int[]{i},locations);
                            //if (exist_in_Array(units, DT) && ) {
                        button_board[0][i] =units[DT].exist ? new Button(label_of_button(game.this, units[DT])/*units[DT].show*/) : the_item_of_empty();
                            final int fi = i;
                                //if(button_board[0][i].getClass() == Button.class){
                            if(button_board[0][i].getClass() == Button.class){
                                ((Button)button_board[0][i]).addActionListener(e -> {
                                    action_before_change_the_location(e, game.this);
                                    locations[directions_of_x] = fi;
                                    action_after_change_the_location(e, game.this);
                                    reflash();
                                });//}catch(Exception e){System.out.println(e.getMessage());}
                            }
                                //}else{
                                    //button_board[0][i] =//;
                                //}
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
                            //}
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
                    for (Component i : button_board[0]) {
                        panel.add(i);
                    }
                }else{
                    //lab_locations.setText(java.util.Arrays.toString(locations)); 
                    //int[] directions = {directions_of_x,directions_of_y};
                    show_directions.setText(format("x:%s,y:%s",x_key[0],y_key[0]));
                    //unit[][] board = new unit[total_numbers_of_directions[directions_of_x]][total_numbers_of_directions[directions_of_y]];
                    button_board = new Component[total_numbers_of_directions[directions_of_x]][total_numbers_of_directions[directions_of_y]];
                    for (int i = 0;i<button_board.length;i++) {
                        for (int j = 0;j<button_board[i].length;j++){
                            //int[] ij = {i,j};
                            try {
                                int DT = digital_transforming(total_numbers_of_directions,/*directions*/new int[]{directions_of_x, directions_of_y},/*ij*/new int[]{i, j},locations);
                                final int fi,fj;
                                fi = i;
                                fj = j;
                                //System.out.println(DT);
                                //if(exist_in_Array(units, DT) && ){
                                        //try{
                                    button_board[i][j] = units[DT].exist ? new Button(label_of_button(game.this, units[DT])/*units[DT].show/*String.valueOf(j)*/) : the_item_of_empty();
                                //}else{
                                    //System.out.println("000" + (button_board[i][j]));
                                    //button_board[i][j] =//;
                                //System.out.println("000");
                                //}
                                if(button_board[i][j].getClass() == Button.class){
                                    ((Button)button_board[i][j]).addActionListener(e -> {
                                        action_before_change_the_location(e, game.this);
                                        locations[directions_of_x] = fi;
                                        locations[directions_of_y] = fj;
                                        action_after_change_the_location(e, game.this);
                                        reflash();
                                    });//}catch(Exception e){System.out.println(e.getMessage());}
                                }
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
                            } catch (Exception e) {
                                //System.out.println(e.getMessage());
                                //e.printStackTrace();
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
                    for (Component[] i : button_board) {
                        for (Component j : i) {
                            panel.add(j);
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
                      //  System.out.println(format("Connot read field \"x\" because the return value of \"%s\" is null", "tic_tac_toc.getMousePosition()"));
                    //System.out.println(ee.getMessage().equals(format("Connot read field \"x\" because the return value of \"%s\" is null", "tic_tac_toc.getMousePosition()")));//why false
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
            protected String label_of_button(game game, unit unit){return unit.show;}
            protected Component the_item_of_empty(){return new Label("");}
            @SuppressWarnings("OverridableMethodCallInConstructor")
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
            if (locations < 0) {throw new Exception("The value is a negative number.");}
            int[] table = digitals.clone();
            int[] result = new int[digitals.length];
            System.out.println("in");
            for (int i = 1; i < digitals.length; i++) {
                if (table[i-1] == 0) {throw new Exception("Exist the digital whose maximum is 0.");}
                table[i] *= table[i-1];
            }
            for (int i = 0 ; i < digitals.length ; i++) {
                System.out.println(i + "," + digitals.toString());
                result[i] = (int)(locations%table[i] / ((i == 0) ? 1 : table[i-1]));
            }
            System.out.println(locations + "," + Arrays.toString(result));
            return result;
        }//印記憶體是刻意的
        @SuppressWarnings("ImplicitArrayToString")
        private int[] digital_to_array_transforming(IntSupplier digitals,int locations) throws Exception{//小心邏輯
            if (locations < 0) {throw new Exception("The value is a negative number.");}
            //if (choosen_digitals.length != numbers.length) {throw new Exception("The number of choosen digitals is not equals to \"numbers\".");}
            //if (digitals.length != locations.length) {throw new Exception("The number of digitals is not equals to locations'.");}
            int table/* * / = 1*/;
            ArrayList<Integer> result = new ArrayList<>();
            System.out.println("in");
            do{
            //System.out.println(locations + "," + Arrays.toString(result.toArray()));
                //System.out.println(i + "," + digitals.toString());
                //System.out.println(table);
                //System.out.println(table);
                table = digitals.getAsInt();//別有0
                if (table == 0) {throw new Exception("Exist the digital whose maximum is 0.");}
                locations /= table;
                result.add(locations%table);//別%0
            }while (locations/*/table*/ != 0) ;//別/0
            System.out.println(result.toString());
            return result.stream().mapToInt(i -> i).toArray();
        }
        @SuppressWarnings("ImplicitArrayToString")
        private String to_empty_string(int[] input) throws Exception{
            StringBuilder string_builder = new StringBuilder();
            for(int i : input){
                if(i >= EMPTY_BUIDER.length){throw new Exception("The builder of empty has a problemn.");}
                string_builder.append(EMPTY_BUIDER[i]);
            }
            System.out.println(input);
            return string_builder.reverse().toString();
        }
        private String get_a_unused_string(ArrayList<String> used) throws Exception{
            int empty_builder = 0;
            try{
                while (used.contains(to_empty_string(digital_to_array_transforming(() -> EMPTY_BUIDER.length, empty_builder)))) { 
                    empty_builder++;
                }
                System.out.println(empty_builder);
                return to_empty_string(digital_to_array_transforming(() -> EMPTY_BUIDER.length, empty_builder));
            }catch(Exception e){
                throw e;
            }
        }
        private class unit{
            @SuppressWarnings("unused")
            private int directions;
            @SuppressWarnings("unused")
            private String show;
            @SuppressWarnings("unused")
            private boolean exist = true;
            @SuppressWarnings("unused")
            private int[] location;
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
            unit set_show(String string){
                this.show = string;
                return this;
            }
            unit set_exist(boolean b){
                this.exist = b;
                return this;
            }
            @SuppressWarnings("unused")
            public unit(int directions, String show, int[] location) {
                this.directions = directions;
                this.show = show;
                this.location = location;
            }
            @SuppressWarnings("unused")
            unit set_location(int[] location){
                this.location = location;
                return this;
            }
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
            private final MenuItem empty_menuItem = new MenuItem(empty, new MenuShortcut(KeyEvent.VK_E, false));
            private final MenuItem DNE_menuItem = new MenuItem(DNE, new MenuShortcut(KeyEvent.VK_D, false));
            //private final MenuBar menuBar = new MenuBar();
            private int chosen_x;
            private int chosen_y;
            @Override
            protected void action_after_reflash(game game){
                for (int i = 0; i < button_board.length; i++) {
                    for (int j = 0; j < button_board[i].length; j++) {
                        final int fi = i, fj = j;
                        button_board[i][j].addMouseListener(new MouseAdapter() {//Button是新的
                            @Override
                            public void mousePressed(MouseEvent e){
                                if(e.getButton() == BUTTON3){
                                    //System.out.println(e);
                                    chosen_x = fi;
                                    chosen_y = fj;
                                    //System.out.println("000");
                                    chose.show(e.getComponent(), e.getX(), e.getY());
                                }
                            }
                        });
                    }
                }
                lab_locations.setText(lab_locations.getText() + format(" empty:%s", DNE));
                //System.out.println("success");
                action_after_reflash_in_edit(game);
                this.pack();
            }
            protected void action_after_reflash_in_edit(@SuppressWarnings("unused") tic_tac_toc47.game game) {}/*/ TODO Auto-generated method stub/*throw new UnsupportedOperationException("Unimplemented method 'action_after_reflash_in_edit'"); */
            @Override
            protected Component the_item_of_empty(){/*System.out.println("000");*/return new Button(DNE);}
            //@Override
            //protected String label_of_button(game game){return }
            @SuppressWarnings("OverridableMethodCallInConstructor")
            public edit(int x, int y, int number){
                super(x, y, number);
                for (String player : players) {
                    MenuItem i = new MenuItem(player);
                    chose.add(i);
                    i.addActionListener((_) -> {
                        try{
                            int[] d, c;
                            if(directions_of_x != directions_of_y) {d = new int[]{directions_of_x,directions_of_y};c = new int[]{chosen_x,chosen_y};
                        } else {
                            d = new int[]{directions_of_x};
                            c = new int[]{chosen_y};//chosen == 1
                        }
                        units[digital_transforming(total_numbers_of_directions, 
                                                   d, 
                                                   c, 
                                                   locations)].set_show(player).set_exist(true);
                        }catch(Exception e){
                            Dialog d = new Dialog(edit.this,e.getMessage(),true);
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
                        reflash();
                    });
                }
                //chose.add(empty_menuItem);
                empty_menuItem.addActionListener((_) -> {
                    try{
                        int[] d, c;
                        if(directions_of_x != directions_of_y) {
                            d = new int[]{directions_of_x,directions_of_y};
                            c = new int[]{chosen_x,chosen_y};
                        } else {
                            d = new int[]{directions_of_x};
                            c = new int[]{chosen_y};
                        }
                        units[digital_transforming(total_numbers_of_directions, 
                                                   d, 
                                                   c, 
                                                   locations)].set_show(empty).set_exist(true);
                    }catch(Exception e){
                        Dialog d = new Dialog(edit.this,e.getMessage(),true);
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
                    reflash();
                });
                //chose.add(DNE_menuItem);
                DNE_menuItem.addActionListener((_) -> {
                    try{
                        int[] d, c;
                        if(directions_of_x != directions_of_y) {d = new int[]{directions_of_x,directions_of_y};c = new int[]{chosen_x,chosen_y};
                    } else {
                        d = new int[]{directions_of_x};
                        c = new int[]{chosen_y};
                    }
                        units[digital_transforming(total_numbers_of_directions, 
                                                   d, 
                                                   c, 
                                                   locations)].set_show(DNE).set_exist(false);
                    }catch(Exception e){
                        Dialog d = new Dialog(edit.this,e.getMessage(),true);
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
                    reflash();
                });
                chose.addSeparator();
                chose.add(empty_menuItem);
                chose.add(DNE_menuItem);
                //menuBar.add(chose);
                this.add(chose);
                //System.out.println("000" + first);
                //reflash();
                pack();
                //System.out.println("000");
            }
        }
        private class game_show extends show{
            Button edit_button = new Button("edit");
            edit edit = new edit_in_game(directions_of_x, directions_of_y, number);
            Panel panel3 = new Panel(new BorderLayout());
            //Panel panel4 = new Panel(new BorderLayout());
            @SuppressWarnings("OverridableMethodCallInConstructor")
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
                //lab_locations.setText(lab_locations.getText() + format(" no players:%s, emptyt:%s", empty, DNE));
                this.pack();
                //
                //this.pack();
            }
        }
        @SuppressWarnings("OverridableMethodCallInConstructor")
        private class edit_in_game extends edit{
            Button reflash = new Button("reflash");
            public edit_in_game(int x,int y, int number){
                super(x, y, number);
                reflash.addActionListener((_) -> {game_show.reflash();});
                reflash();
            }
            @Override
            protected void action_after_reflash_in_edit(game game){
                if(!first){
                    panel2.add(reflash);
                    pack();
                }
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
            //game.game_main game_main = game.new game_main();
            game/*_main*/.initialize(new String[]{"x","y","z"},new int[]{2,3,3}, null, players);
            game/*_main*/.game_show.setVisible(true);
            //System.exit(0);
        }catch(Exception _){}
    }
}
