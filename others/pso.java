import java.util.Arrays;
import java.text.DecimalFormat;
import java.io.FileWriter;
import java.io.BufferedWriter;
import java.io.PrintWriter;
import java.io.IOException;

public class pso{

  public static int N = 5; // 次元数
  public static int test = 1; // test関数指定(1~6)
  public static int I = 50; //粒子数
  public static int t; // 現在時間
  public static int T = 50; // 最大時間

  // テスト関数
  public static double func(double x[]){
    double fx = 0;
    int i;
    switch (test){
      case 1: // sphere
        for (i = 0; i < N; i++){
          fx += x[i] * x[i];
        }
        break;
      case 2: // Rastrigin
        for (i = 0; i < N; i++){
          fx += x[i]*x[i] - 10*Math.cos(2*x[i]*Math.PI);
        }
        fx += 10*N;
        break;
      case 3: // Rosenbrock
        for (i = 0; i < N-1; i++){
          fx += 100*(x[i+1] - x[i])*(x[i+1] - x[i]) + (1 - x[i])*(1 - x[i]);
        }
        break;
      case 4: // Griewank
        double sig = 0;
        double pi = 1;
        for (i = 0; i < N; i++){
          sig += x[i]*x[i];
          pi *= Math.cos(x[i]/Math.sqrt(i+1));
        }
        fx = 1 + 1.0/4000*sig - pi;
        break;
      case 5: // Alpine
        for (i = 0; i < N; i++){
          fx += Math.abs(x[i]*Math.sin(x[i]) + 0.1*x[i]);
        }
        break;
      case 6: // 2n_minima
        for (i = 0; i < N; i++){
          fx += x[i]*x[i]*x[i]*x[i] - 16*x[i]*x[i] + 5*x[i];
        }
        // fx += 391.661657037; // 最小値を0にするため（N=5のとき）
        fx += 783.323314074; // 最小値を0にするため（N=10のとき）
        break;
    }
    return fx;
  }

  // 適合度
  public static double fit(double x[]){
    double fx = func(x);
    double fit; // 適合度
    if (fx >= 0){
      fit = 1.0/(1 + fx);
    } else {
      fit = 1 - fx;
    }
    return fit;
  }

  // 粒子座標更新
  public static double[] next_x(double x[], double vx[]){
    int N = x.length;
    double next_x[] = new double[N];
    for (int i = 0; i < N; i++){
      next_x[i] = x[i] + vx[i];
    }
    return next_x;
  }

  // 粒子速度更新
  public static double[] next_v(double x[], double vx[], double p_best_x[], double g_best_x[]){
    double r1 = Math.random()*(-0.5);
    double r2 = Math.random()*1.0;
    double w = Math.random()*1.0;
    int N = x.length;
    double next_v[] = new double[N];
    for (int i = 0; i < N; i++){
        next_v[i] = w*vx[i] + r1*(p_best_x[i]-x[i]) + r2*(g_best_x[i]-x[i]);
    }
    return next_v;
  }


  public static void main(String[] args) {
    int i,j; //for カウンタ
    double xn[][] = new double[I][N]; // N個の粒子の座標
    double vn[][] = new double[I][N]; // N個の粒子の速度
    double p_best_x[][] = new double[I][N]; // 各粒子の自己ベスト座標
    double p_best_score[] = new double[I]; // 各粒子の自己ベストスコア
    double g_best_p; // 最小スコアの粒子
    double g_best_x[] = new double[N]; // 全粒子のベスト座標
    double g_best_score = 1.0e100; // 全粒子のベストスコア 初期値は十分大きい値
    double fx_hist[] = new double[T]; // ベストfx履歴
    double fit_hist[] = new double[T]; // 適合度履歴

    // 粒子初期化
    for (i = 0; i < I; i++){
      // 初期座標ランダム、初速度０
      for (j = 0; j < N; j++){
        switch (test){
          case 1: // Sphere
            xn[i][j] = Math.random()*10-5; // -5<x<5
            break;
          case 2: // Rastrigin
            xn[i][j] = Math.random()*10-5; // -5<x<5
            break;
          case 3: // Rosenbrock
            xn[i][j] = Math.random()*15-5; // -5<x<10
            break;
          case 4: // Griewank
            xn[i][j] = Math.random()*1200-600; // -600<x<600
            break;
          case 5: // Alpine
            xn[i][j] = Math.random()*20-10; // -10<x<10
            break;
          case 6: // 2n_minima
            xn[i][j] = Math.random()*10-5; // -5<x<5
            break;
        }
        vn[i][j] = 0.0; // x速度
      }

      p_best_x[i] = xn[i];
      p_best_score[i] = func(xn[i]);
      if (p_best_score[i] < g_best_score){
        // ベストスコア更新
        g_best_score = p_best_score[i];
        g_best_p = i;
        g_best_x = p_best_x[i];
      }
    }

      // 更新処理
      for (t = 0; t < T ; t++){
        for (i = 0; i < I; i++){
          double x[] = xn[i];
          double vx[] = vn[i];
          double score;
          xn[i] = next_x(x, vx); // 座標更新
          vn[i] = next_v(x, vx, p_best_x[i], g_best_x); //速度更新
          score = func(xn[i]);
          // ベストスコア更新
          if (score < p_best_score[i]){
            p_best_score[i] = score;
            p_best_x[i] = xn[i];
            if (score < g_best_score){
              g_best_score = score;
              g_best_p = i;
              g_best_x = xn[i];
            }
          }
        }
        fx_hist[t] = g_best_score;
        fit_hist[t] = fit(g_best_x);
      }

      // コンソール出力
      System.out.println();
      switch (test){
        case 1: // Sphere
          System.out.println("Sphere");
          break;
        case 2: // Rastrigin
          System.out.println("Rastrigin");
          break;
        case 3: // Rosenbrock
          System.out.println("Rosenbrock");
          break;
        case 4: // Griewank
          System.out.println("Griewank");
          break;
        case 5: // Alpine
          System.out.println("Alpine");
          break;
        case 6: // 2n_minima
          System.out.println("2n_minima");
          break;
      }
      System.out.println(Arrays.toString(g_best_x));
      System.out.println(g_best_score);
      System.out.println();

      // csvファイル出力
      try {
        FileWriter fw = new FileWriter("pso_fx_"+test+".csv");
        for (i = 0; i < T; i++){
          fw.write(fx_hist[i]+"\n");
        }
        fw.close();
      } catch (IOException ex) {
        ex.printStackTrace();
      }
      try {
        FileWriter fw = new FileWriter("pso_fit_"+test+".csv");
        for (i = 0; i < T; i++){
          fw.write(fit_hist[i]+"\n");
        }
        fw.close();
      } catch (IOException ex) {
        ex.printStackTrace();
      }
    }

}
