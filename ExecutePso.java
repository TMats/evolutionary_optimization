import java.io.FileWriter;
import java.io.BufferedWriter;
import java.io.PrintWriter;
import java.io.IOException;
import java.util.*;

public class ExecutePso{
    public static double[] evaluate(int f, int N, int D, int T, double[] params, int trial_num){
        double[] eval_result = new double[trial_num];
        for(int t=0;t<trial_num;t++) {
            eval_result[t]=Pso.execute(f,N,D,T,params);
        }

        return eval_result;
    }


    public static void main(String[] args){
        try {
            // number of functions
            int func_num = 6;
            // number of trials
            int trial_num = 10;

            FileWriter fw = new FileWriter("PSO_result.csv", true);
            PrintWriter pw = new PrintWriter(new BufferedWriter(fw));
            pw.print("func,D,N,T,W,C1,C2,");
            for(int t=0;t<trial_num;t++){
                pw.print((t+1)+",");
            }
            pw.println();


            // parameters(defaults)
            int N = 4000;
            int D = 5;
            int T = 250;
            double[] params = {0.7,0.4,0.3};

            //change w, c1, c2
//            double[] ws={0.1,0.2,0.3,0.4,0.5,0.6,0.7,0.8,0.9,1.0};
//            double[] c1s={0.1,0.2,0.3,0.4,0.5,0.6,0.7,0.8,0.9,1.0};
//            double[] c2s={0.1,0.2,0.3,0.4,0.5,0.6,0.7,0.8,0.9,1.0};
            double[] ws={0.1,0.3,0.5,0.7,0.9};
            double[] c1s={0.1,0.3,0.5,0.7,0.9};
            double[] c2s={0.1,0.3,0.5,0.7,0.9};

            for(int f=0;f<func_num;f++){
                for(int i=0;i<ws.length;i++){
                    for(int j=0;j<c1s.length;j++){
                        for(int k=0;k<c2s.length;k++){
                            double p[] = {ws[i],c1s[j],c2s[k]};
                            System.out.println((f+1)+","+D+","+N+","+T+","+ws[i]+","+c1s[j]+","+c2s[k]);
                            double[] results = evaluate(f,N,D,T,p,trial_num);
                            pw.print((f+1)+","+D+","+N+","+T+","+ws[i]+","+c1s[j]+","+c2s[k]+",");
                            for(int t=0;t<trial_num;t++){
                                pw.print(results[t]+",");
                            }
                        }
                            pw.println();
                        }
                }
            }

            pw.close();
            System.out.println("finished");
        } catch (IOException ex) {
            ex.printStackTrace();
        }


    }
}