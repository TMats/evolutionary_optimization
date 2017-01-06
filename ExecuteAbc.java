import java.io.FileWriter;
import java.io.BufferedWriter;
import java.io.PrintWriter;
import java.io.IOException;
import java.util.*;

public class ExecuteAbc{
    public static double[] evaluate(int f, int N, int D, int T, int L, int trial_num){
        double[] eval_result = new double[trial_num];
        for(int i=0;i<trial_num;i++) {
            eval_result[i]=Abc.execute(f,N,D,T,L);
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

            int[] ns={10,50,100,200};
            int[] ts={100,200,500};
            int[] ls={5,10,20,50};

            for(int f=0;f<func_num;f++){
                for(int i=0;i<ns.length;i++){
                    for(int j=0;j<ts.length;j++){
                        for(int k=0;k<ls.length;k++) {
                            double[] results = evaluate(f, ns[i], D, ts[j], ls[k], trial_num);
                            System.out.println((f + 1) + "," + D + "," + ns[i] + "," + ts[j] + "," + ls[k]);
                            pw.print((f + 1) + "," + ns[i]+ "," + D + "," + ts[j] + "," + ls[k] + ",");
                            for (int t = 0; t < trial_num; t++) {
                                pw.print(results[t] + ",");
                            }
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