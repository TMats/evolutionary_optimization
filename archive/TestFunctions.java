public class TestFunctions{
    // generate initial x
    public static double[][] initialize_x(int func_id, int num_particle, int dim) {
        double[][] x = new double[num_particle][dim];

        // set maxmum & minimum of x
        double max = 0.0;
        double min = 0.0;
        switch (func_id) {
            case 1:
                // Sphere Function
                max = 5.0;
                min = -5.0;
                break;
            case 2:
                // Rastrigin Function
                max = 5.0;
                min = -5.0;
                break;
            case 3:
                // Rosenbrock Function
                max = 10.0;
                min = -5.0;
                break;
            case 4:
                // Griewank Function
                max = 600.0;
                min = -600.0;
                break;
            case 5:
                // Alpine Function
                max = 10.0;
                min = -10.0;
                break;
            case 6:
                // 2^n minima Function
                max = 5.0;
                min = -5.0;
                break;
        }

        // generate initial x
        for (int i = 0; i < num_particle; i++) {
            for (int j = 0; j < dim; j++) {
                x[i][j] = Math.random() * (max - min) + min;
            }
        }

        return x;
    }
}