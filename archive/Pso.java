import java.util.Arrays;

public class Pso{
    // consts
    public static double W = 0.8;
    public static double C1 = 2.0;
    public static double C2 = 2.0;

    public static double[][] get_x(double[][] x, double[][] v){
        double[][] new_x = x;
        for(int i=0; i<x.length; i++){
            for(int j=0; j<x[i].length;j++){
                new_x[i][j] += v[i][j];
            }
        }
        return new_x;
    }

    public static double[][] get_v(double[][] x, double[][] v, double[][] pbx, double[] gbx){
        double[][] new_v = v;
        for(int i=0; i<v.length; i++){
            double r1 = Math.random();
            double r2 = Math.random();
            for(int j=0; j<x[i].length;j++){
                new_v[i][j] = W*v[i][j] + C1*r1*(pbx[i][j]-x[i][j]) + C2*r2*(gbx[j]-x[i][j]);
            }
        }
        return new_v;
    }

    public static double[] execute(int func_id, int N, int D, int T){
        // initialize x(place)
        double[][] x = new double[N][D];
        x = TestFunctions.initialize_x(func_id, N, D);
        // initialize v(velocity)
        double[][] v = new double[N][D];
        // initialize 'best's
        double[][] particle_best_x = new double[N][D];
        for(int i=0;i<N;i++){
            particle_best_x[i] = Arrays.copyOf(x[i],x[i].length);
        }
        double[] values = new double[N];
        for (int i = 0; i<N; i++){
            values[i] = TestFunctions.get_value(func_id, x[i]);
        }
        double[] particle_best_value = Arrays.copyOf(values,values.length);
        int best_particle_idx = ArrayTools.get_min_index(values);
        double[] global_best_x = Arrays.copyOf(x[best_particle_idx],x[best_particle_idx].length);
        double global_best_value = values[best_particle_idx];

        double[] result = new double[T];
        for(int t=0;t<T;t++){
            double[][] new_x = get_x(x,v);
            double[][] new_v = get_v(x, v, particle_best_x, global_best_x);
            x = new_x;
            v = new_v;
            for(int i=0; i<N; i++){
                double value = TestFunction.get_value(func_id, x[i]);
                if(value < particle_best_value[i]){
                    particle_best_value[i] = value;
                    particle_best_x[i] = Arrays.copyOf(x[i], x[i].length);
                    if(value < global_best_value){
                        global_best_value = value;
                        global_best_x = Arrays.copyOf(x[i], x[i].length);
                    }
                }
            }
            result[t] = global_best_value;
        }
        System.out.println("func_id="+func_id+" num_particles:"+N+" num_dims:"+D+" num_iters:"+T);
        System.out.println(Arrays.toString(global_best_x));
        System.out.println(global_best_value);
        System.out.println();
        return result;
    }

    public static void main(String[] args) {
        // function_id
        int func_id = 1;
        // number of particles
        int N = 100;
        // dimension
        int D = 5;
        // maximum number of iteration
        int T = 100;

        double[] result = execute(func_id,N,D,T);
    }

}
