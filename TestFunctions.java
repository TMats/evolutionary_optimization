public class TestFunctions{

    // return limit of x
    public static double[] get_limit_x(int func_id){
        double[] limit = new double[2];

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
        limit[0] = min;
        limit[1] = max;
        return limit;
    }


    // generate initial x
    public static double[] init_x(int func_id, int dim) {
        double[] x = new double[dim];

        // set maxmum & minimum of x
        double min = get_limit_x(func_id)[0];
        double max = get_limit_x(func_id)[1];

        // generate initial x
        for (int i = 0; i < dim; i++) {
            x[i] = Math.random() * (max - min) + min;
        }

        return x;
    }


    // calcuate function value
    public static double get_value(int func_id, double[] x){
        int dim = x.length;
        double val = 0;
        switch(func_id){
            case 1:
                // Sphere Function
                for(int i=0; i<dim; i++){
                    val += Math.pow(x[i],2);
                }
                break;
            case 2:
                // Rastrigin Function
                val += 10*dim;
                for(int i=0; i<dim; i++){
                    val += (Math.pow(x[i],2) - 10*Math.cos(2*Math.PI*x[i]));
                }
                break;
            case 3:
                // Rosenbrock Function
                for(int i=0; i<dim-1; i++){
                    val += (100*Math.pow((x[i+1]-Math.pow(x[i],2)),2) + Math.pow(1 - x[i],2));
                }
                break;
            case 4:
                // Griewank Function
                double sigma = 0;
                double pi = 1;
                for(int i = 0; i < dim; i++){
                    sigma += Math.pow(x[i],2);
                    pi *= Math.cos(x[i]/Math.sqrt(i+1));
                }
                val = 1 + sigma/4000 - pi;
                break;
            case 5:
                // Alpine Function
                for(int i=0; i<dim; i++){
                    val += Math.abs(x[i]*Math.sin(x[i])+0.1*x[i]);
                }
                break;
            case 6:
                // 2^n minima Function
                for(int i=0; i<dim; i++){
                    val += Math.pow(x[i],4) - 16*Math.pow(x[i],2) + 5*x[i];
                    // approx. minimum of 2^n minima
                    val += 78.3323314075428309277863395;
                }
                break;
        }

        return val;
    }


    // calucate fitness
    public static double get_fitness(int func_id, double[] x){
        double fit;
        double val = get_value(func_id, x);
        if(val>=0){
            fit = 1.0/(1.0+val);
        }else{
            fit = 1.0 - val;
        }
        return fit;
    }
}
