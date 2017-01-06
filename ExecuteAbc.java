import java.io.FileWriter;
import java.io.BufferedWriter;
import java.io.PrintWriter;
import java.io.IOException;
import java.util.*;

public class ExecuteAbc{
    public static double[] evaluate(int f, int N, int D, int T, int L, int trial_num){
        double[] eval_result = new double[trial_num];
        for(int t=0;t<trial_num;t++) {
            eval_result[t]=Abc.execute(f,N,D,T,L);
            System.out.println(f+""+)
        }

        return eval_result;
    }


    public static void main(String[] args){
        try {
            // number of functions
            int func_num = 6;
            // number of trials
            int trial_num = 10;

            FileWriter fw = new FileWriter("ABC_result.csv", true);
            PrintWriter pw = new PrintWriter(new BufferedWriter(fw));
            pw.print("func,N,D,T,L,");
            for(int t=0;t<trial_num;t++){
                pw.print((t+1)+",");
            }
            pw.println();


            // parameters(defaults)
            int N = 100;
            int D = 5;
            int T = 200;
            int L =20;

            int[] ts={200};
            int[] ls={20};

            for(int f=0;f<func_num;f++){
                for(int i=0;i<ts.length;i++){
                    for(int j=0;j<ls.length;j++){
                        double[] results = evaluate(f,N,D,ts[i],ls[j],trial_num);
                        pw.print((f+1)+","+N+","+D+","+ts[i]+","+ls[j]+",");
                        for(int t=0;t<trial_num;t++){
                            System.out.println(Arrays.toString(results));
                            pw.print(results[t]+",");
                        }
                    }
                    pw.println();
                }
            }

            pw.close();
            System.out.println("finished");
        } catch (IOException ex) {
            ex.printStackTrace();
        }


    }
}