import java.util.Arrays;
import java.util.ArrayList;


class Particle{
    // Consts
    public static double W = 0.3; // -0.2
    public static double C1 = 0.6; //1.2
    public static double C2 = 0.6; //1.2

    // define 'global_best's
    public static double[] global_best_x;
    public static double global_best_value;

    private int func_id;
    private int dim;
    private double min;
    private double max;
    private double[] x;
    private double[] v;
    private double value;
    private double[] best_x;
    private double best_value;

    Particle(int func_id, int dim){
        this.func_id = func_id;
        this.dim = dim;
        min = TestFunctions.get_limit_x(func_id)[0];
        max = TestFunctions.get_limit_x(func_id)[1];
        x = TestFunctions.init_x(func_id, dim);
        v = new double[dim];
        value = TestFunctions.get_value(func_id, x);
        best_x = Arrays.copyOf(x,x.length);
        best_value = value;
    }

    double[] get_x(){ return x; }
    double get_value(){ return value; }

    void update() {
        double[] new_x = Arrays.copyOf(x,x.length);
        double[] new_v = Arrays.copyOf(v,v.length);
        double r1 = Math.random();
        double r2 = Math.random();
        for (int i = 0; i < dim; i++) {
            //if(new_x[i]+v[i]>=min && new_x[i]+v[i]<=max){
                new_x[i] += v[i];
            //}
            new_v[i] = W * v[i] + C1 * r1 * (best_x[i] - x[i]) + C2 * r2 * (global_best_x[i] - x[i]);
        }
        x = new_x;
        v = new_v;
        value = TestFunctions.get_value(func_id,x);
    }

    void update_best(){
        if(value<best_value){
            best_value = value;
            best_x = Arrays.copyOf(x,x.length);
        }
        if(value<global_best_value){
            global_best_value = value;
            global_best_x = Arrays.copyOf(x,x.length);

        }
    }

}


public class Pso{
    public static double[] execute(int func_id, int N, int D, int T){
        Particle.global_best_x = new double[N];
        Particle.global_best_value = Double.POSITIVE_INFINITY;

        ArrayList<Particle> particle_list = new ArrayList<Particle>();
        for(int i=0;i<N;i++){
            Particle part = new Particle(func_id,D);
            particle_list.add(part);
            particle_list.get(i).update_best();
            //print debug
            if(i==0){
                System.out.println(Arrays.toString(particle_list.get(i).get_x()));
                System.out.println(particle_list.get(i).get_value());
            }
        }

        double[] result = new double[T+1];
        result[0] = Particle.global_best_value;

        for(int t=1;t<T+1;t++){
            for(int i=0;i<N;i++){
                particle_list.get(i).update();
                //print debug
                if(i==0){
                    System.out.println(Arrays.toString(particle_list.get(i).get_x()));
                    System.out.println(particle_list.get(i).get_value());
                }
                particle_list.get(i).update_best();
            }
            result[t] = Particle.global_best_value;
        }
        System.out.println("func_id="+func_id+" num_particles:"+N+" num_dims:"+D+" num_iters:"+T);
        System.out.println(Arrays.toString(Particle.global_best_x));
        System.out.println(Particle.global_best_value);
        System.out.println();
        return result;
    }

    public static void main(String[] args) {
        // function_id
        int func_id = 6;
        // number of particles
        int N = 1000;
        // dimension
        int D = 5;
        // number of iteration
        int T = 1000;

        double[] result = execute(func_id,N,D,T);
    }

}
