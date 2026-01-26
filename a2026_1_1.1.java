
import java.lang.reflect.Array;
import java.util.ArrayList;

public class a2026_1_1 {
    @SuppressWarnings("Convert2Diamond")
    static int[] get_number(int[] radix, int[][] symbols) throws Exception{
        for (int i = 0; i < symbols.length; i++) {if(symbols[i].length != radix.length){throw new Exception(String.valueOf(i));}}
        //if (radix.length != symbols.length) {throw new Exception("radix.length != symbols.length");}
        int frame_score = 0,lambda = 1,transition_score = 0;
        @SuppressWarnings("unchecked")
        ArrayList<Integer>[] detal = new ArrayList[16];//0 times,1 s,
        for (int i = 0; i < detal.length; i++) {detal[i] = new ArrayList<Integer>();}
        for (int i = 0; i < symbols.length; i++) {
            int j = symbols[i][0];
            outer:do {
                for (int k = 0; k < symbols[i].length; k++) {
                    if (k != j) {
                        if(detal[0].getLast() != 0){detal[0].add(0);}
                        break outer;
                    }
                }
                detal[0].set(detal[0].size()-1,detal[0].getLast()+1);
            } while (false);
            for (int k = 0; k < (int)(symbols[i].length/2); k++) {
                if(symbols[i][k] == symbols[i][(symbols[i].length-1)-k]){};
            }
        }
        return new int[]{frame_score + lambda*transition_score};
    }
}
