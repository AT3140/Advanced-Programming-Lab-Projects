import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.TimeUnit;
import java.util.*;
import java.time.*;
import java.io.File;
import java.io.FileWriter;
import com.opencsv.CSVWriter;
public class Main {
    public static void main(String[] args) {
        int N=500, nt=500;
        List<String[]> data=new ArrayList<String[]>();
        int [] res;
        // data.add(new String[] {"Input Size","Multithread","Sequential"});
        // for(int i=1; i<=N; i++){
        //     res=helper(i,nt);
        //     data.add(new String[] {Integer.toString(i), 
        //         Integer.toString(res[0]), Integer.toString(res[1])
        //     });
        //     System.out.print(".");
        // } //04b graph(a)

        data.add(new String[] {"No of Threads","Multithread"});
        for(int i=2; i<=nt; i+=2){
            res=helper(N,i);
            data.add(new String[] {Integer.toString(i), 
                Integer.toString(res[0])
            });
            System.out.print(".");
        } //04b graph(b)
        
        File file=new File("obs_a.csv");
        try {
            FileWriter fw=new FileWriter(file);
            CSVWriter csv_a=new CSVWriter(fw); 
            csv_a.writeAll(data);
            csv_a.close();
        } catch (Exception e) {
            //  handle exception
        }
    }
    static int[] helper(int N, int nt){
        matrix m= new matrix(N);
        Clock clock=Clock.systemDefaultZone();
        ExecutorService executor= Executors.newCachedThreadPool();
        
        long start_time=clock.millis();
        for(int i=0; i<nt; i++)
            executor.execute(new mult_thread(m));
        executor.shutdown();
        // while(!executor.isShutdown()){

        // }
        try {
            if (!executor.awaitTermination(800, TimeUnit.MILLISECONDS)) {
                executor.shutdownNow();
            } 
        } catch (InterruptedException e) {
            executor.shutdownNow();
        }
        long end_time=clock.millis();
        //System.out.println("Start Time: "+start_time+", End Time: "+ end_time);
        int duration_mt=(int)(end_time-start_time);
        //System.out.println("Duration of Multithreading: "+ duration_mt +" ms");
       
        //m.render();

        //sequential multiplication
        int A[][]=m.obtain_A();
        int B[][]=m.obtain_B();
        long C[][]=new long[N][N];

        start_time=clock.millis();
        for(int pi=0; pi<N; pi++){
            for(int pj=0; pj<N; pj++){
                long p_ele=0;
                //System.out.println("hello");
                for(int k=0; k<N; k++){
                    p_ele+=A[pi][k]*B[k][pj];
                }
                C[pi][pj]=p_ele;
            }
        }
        end_time=clock.millis();
        //System.out.println("Start Time: "+start_time+", End Time: "+ end_time);
        int duration_sq=(int)(end_time-start_time);
        //System.out.println("Duration of Sequential: "+ duration_sq +" ms");

        int[] res=new int[2];
        res[0]=duration_mt; res[1]=duration_sq;
        return res;
    }
    
    public static class mult_thread implements Runnable{
        matrix m;
        mult_thread(matrix m){
            this.m=m;
        }
        public void run(){
            while(!m.is_done()){
                int[] target=m.seek();
                int row=target[0], col=target[1];
                //System.out.println("hello");
                m.multABC(row,col);
            }
        }
    }
    
}

class matrix{
    private int pi, pj;
    private int N;
    private int[][] A,B; // AxB=C
    private long[][] C;
    matrix(int N){
        this.N=N;
        this.pi=0; this.pj=0;
        constructMatrixAB();
        C=new long[N][N]; 
    }

    public int[][] obtain_A(){
        return A;
    } 

    public int[][] obtain_B(){
        return B;
    }

    synchronized int[] seek(){
        int[] p=new int[2]; //p [row,column]
        if(pi<N && pj<N){
            p[0]=pi++; p[1]=pj;
        }
        else if(pi==N && pj<N){
            pi=0; p[0]=pi++; p[1]=pj++;
        }
        else {
            //throw new IndexOutOfBoundsException();
            pi=0;
            pj++;
        } 
        return p;
    }

    public boolean is_done(){
        if(pj>=N)
            return true;
        return false;
    }

    void constructMatrixAB(){
        A=new int[N][N]; B=new int[N][N];
        for(int i=0; i<this.N; i++){
            for(int j=0; j<this.N;j++){
                A[i][j]=(int)(Math.random()*101);
                B[i][j]=(int)(Math.random()*101);
                // A[i][j]=2;//test
                // B[i][j]=2;//test
            }
        }
    }

    void multABC(int row, int col){
        long prod=0;
        for(int i=0; i<N; i++){
            prod+=A[row][i]*B[i][col];
        }
        C[row][col]=prod;
    }

    void render(){
        System.out.println("\nA");
        for(int i=0; i<N; i++){
            for(int j=0; j<N; j++){
                System.out.print(A[i][j]+" ");
            }
            System.out.println();
        }
        System.out.println("\nB");
        for(int i=0; i<N; i++){
            for(int j=0; j<N; j++){
                System.out.print(B[i][j]+" ");
            }
            System.out.println();
        }
        System.out.println("\nC");
        for(int i=0; i<N; i++){
            for(int j=0; j<N; j++){
                System.out.print(C[i][j]+" ");
            }
            System.out.println();
        }
    }
}
