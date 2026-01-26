
//ximport java.lang.reflect.Array;
//import java.util.ArrayList;
//import java.util.HashMap;
//import com.sun.source.tree.Tree;
//import java.lang.reflect.Array;
import java.util.*;

public class a2026_1_1 {
    final static int EMPTY = -1;
    @SuppressWarnings("unchecked")
    static ArrayList<int[]>[] detail = new ArrayList[14];//0 times,1 s,
    @SuppressWarnings("Convert2Diamond")
    static int[] get_number(int[] radix, int[][] symbols) throws Exception{
        for (int i = 0; i < symbols.length; i++) {if(symbols[i].length != radix.length){throw new Exception(String.valueOf(i));}}
        //if (radix.length != symbols.length) {throw new Exception("radix.length != symbols.length");}
        int frame_score = 0,lambda = 1,transition_score = 0;
        for (int i = 0; i < detail.length; i++) {detail[i] = new ArrayList<>();detail[i].add(new int[2]);}
        for (int i = 0; i < symbols.length; i++) {
            double j = symbols[i][0]/radix[i];
            outer:do {
                for (int k = 0; k < symbols[i].length; k++) {
                    if (symbols[i][k]/radix[i] - j <= 0.1) {
                        if(detail[0].getLast()[1] != 0){detail[0].add(new int[]{0,EMPTY});}
                        break outer;
                    }
                }
                plus(0,0);
            } while (false);
            //
            for (int k = 0; k < (int)(symbols[i].length/2); k++) {
                detail[1].add(new int[]{EMPTY,symbols[i][k]});
                if(symbols[i][k]/radix[i] - symbols[i][(symbols[i].length-1)-k]/radix[i] <= 0.1){plus(1,(symbols[i][k] == radix[i])?1:0);}
            }
            //
            //for()
            for (int k = 0; k < symbols[i].length-2; k++) {
                detail[2].add(new int[]{EMPTY,symbols[i][k]});
                if(symbols[i][k]/radix[i] - symbols[i][k+2]/radix[i] <= 0.1){plus(2,0);}
            }
            //s
            do{
                ArrayList<Integer> ArrayList = new ArrayList<>();
                for (int k = 0; k < symbols[i].length; k++) {
                    ////EMPTY,symbols[i][k]
                    if(symbols[i][k] == radix[i]){ArrayList.add(k);}
                }
                //int s = ;
                ArrayList<Integer> d = new ArrayList<>();
                for (int k = 0; k < ArrayList.size()-1; k++) {
                    if(!d.contains(ArrayList.get(k+1)-ArrayList.get(k))){
                        d.add(ArrayList.get(k+1)-ArrayList.get(k));
                    }
                }
                detail[3].add(new int[]{d.size(),0});
            }while(false);
            //
            for (int k = 0; k < symbols[i].length; k++) {
                //
                ArrayList<Integer> ArrayList = new ArrayList<>();
                for (int l = 0; l < symbols[i].length-1; l++) {
                    ////EMPTY,symbols[i][k]
                    if(symbols[i][l] != symbols[i][l+1]){ArrayList.add(l);}
                }
                //int s = ;
                ArrayList<Integer> d = new ArrayList<>();
                for (int l = 0; l < ArrayList.size()-1; l++) {
                    if(!d.contains(ArrayList.get(l+1)-ArrayList.get(l))){
                        d.add(ArrayList.get(l+1)-ArrayList.get(l));
                    }
                }
                detail[3].add(new int[]{d.size(),0});
                //
            }
        }
        return new int[]{frame_score + lambda*transition_score};
    }
    static void plus(int i,int j){detail[i].set(detail[i].size()-1,new int[]{detail[i].getLast()[0]+1,detail[i].getLast()[1]+j});}
}
