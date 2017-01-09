import java.util.Arrays;
import java.util.ArrayList;
import java.util.Random;
import java.io.FileWriter;
import java.io.BufferedWriter;
import java.io.PrintWriter;
import java.io.IOException;

class Employed{
    public static int num_employed;
    public static int func_id;

    // define 'global_best's
    public static double[] global_best_x;
    public static double global_best_fitness;

    private int id;
    private int dim;
    private double[] x;
    private double fitness;
    private int num_stay;

    Employed(int id, int dim){
        this.id = id;
        this.dim = dim;
        x = TestFunctions.init_x(func_id,dim);
        fitness = TestFunctions.get_fitness(func_id,x);
        num_stay = 0;
    }

    double[] get_x(){ return x; }
    double get_fitness() { return fitness; }
    int get_num_stay() { return num_stay; }

    void update(){
        Random rnd = new Random();
        // choose dimension
        int d = rnd.nextInt(dim);
        // choose other employed bee
        int m = rnd.nextInt(num_employed);
        while (m==id){
            m = rnd.nextInt(num_employed);
        }
        double[] xm = Abc.employed_list.get(m).get_x();

        // generate candidate x
        double[] v = Arrays.copyOf(x,x.length);
        double r = 2* Math.random() - 1.0;
        v[d] = x[d] + r*(x[d]-xm[d]);
        double fitness_v = TestFunctions.get_fitness(func_id,v);
        if(fitness_v > fitness){
            fitness = fitness_v;
            x = v;
            num_stay = 0;
        }else{
            num_stay += 1;
        }
    }

    void scout(){
        x = TestFunctions.init_x(func_id,dim);
        fitness = TestFunctions.get_fitness(func_id,x);
        num_stay =0;
    }
}


public class Abc{
    public static ArrayList<Employed> employed_list = new ArrayList<Employed>();

    public static double execute(int func_id, int N, int D, int T, int L){
        Employed.global_best_x = new double[N];
        Employed.global_best_fitness = 0;
        Employed.func_id = func_id;

        // init emoployed bee
        Employed.num_employed = N;
        double[] fits = new double[N];
        for(int i=0;i<N;i++){
            Employed emp = new Employed(i, D);
            employed_list.add(emp);
            fits[i] = employed_list.get(i).get_fitness();
            if(employed_list.get(i).get_fitness()>Employed.global_best_fitness){
                Employed.global_best_fitness = employed_list.get(i).get_fitness();
                Employed.global_best_x = Arrays.copyOf(employed_list.get(i).get_x(),employed_list.get(i).get_x().length);
            }
        }

        // print ---
//        try {
//            FileWriter fw = new FileWriter("abclog_1.csv", true);
//            PrintWriter pw = new PrintWriter(new BufferedWriter(fw));
//            pw.print("iter_num,value,");
//            pw.println();
            // --- print
            for (int t = 1; t < T + 1; t++) {
                // print ---
//                pw.print((t-1)+",");
//                pw.print(TestFunctions.get_value(func_id, Employed.global_best_x)+",");
//                pw.println();
                // ---print
                for (int i = 0; i < N; i++) {
                    employed_list.get(i).update();
                    fits[i] = employed_list.get(i).get_fitness();
                }
                // onlooker
                for (int i = 0; i < N; i++) {
                    int idx = ArrayTools.get_roulette_index(fits);
                    employed_list.get(idx).update();
                    fits[idx] = employed_list.get(idx).get_fitness();
                }
                // scout
                for (int i = 0; i < N; i++) {
                    if (employed_list.get(i).get_num_stay() > L) {
                        employed_list.get(i).scout();
                        fits[i] = employed_list.get(i).get_fitness();
                    }
                }
                // renew 'best's
                for (int i = 0; i < N; i++) {
                    if (employed_list.get(i).get_fitness() > Employed.global_best_fitness) {
                        Employed.global_best_fitness = employed_list.get(i).get_fitness();
                        Employed.global_best_x = Arrays.copyOf(employed_list.get(i).get_x(), employed_list.get(i).get_x().length);
                    }
                }
            }
        // print ---
//            pw.close();
//        }catch (IOException ex){
//            ex.printStackTrace();
//        }
        // ---print

        double result = TestFunctions.get_value(func_id, Employed.global_best_x);
        return result;
    }

    public static void main(String[] args) {
        // function_id
        int func_id = 1;
        // number of employed bee
        int N = 100;
        // dimension
        int D = 5;
        // number of iteration
        int T = 200;
        // limit number of stay
        int L = 20;
        // execute
        double result = execute(func_id,N,D,T,L);
        System.out.println(result);
    }

}