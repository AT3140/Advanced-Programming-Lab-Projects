import Lookup.LookList;
import Lookup.LookSet;
import java.util.ArrayList;
import java.util.LinkedList;
import java.util.HashSet;
import java.util.TreeSet;
public class _2002238_C_05b {
    public static void main(String[] args) {
        final String file1=args[0];
        final String file2=args[1];
        //final String file1="book1.txt";
        //final String file2="book2.txt";
        
        long start_time, end_time;
        int duration_sec, duration_ms;
        try {
            //ArrayList
            System.out.println();
            System.out.println("ArrayList");
            LookList<ArrayList<String>> al=new LookList<>();
            start_time=System.currentTimeMillis();
            System.out.println(al.usingList(file1, file2,new ArrayList<String>(), new ArrayList<String>()));
            end_time=System.currentTimeMillis();
            duration_ms=(int)(end_time-start_time);
            duration_sec=(int)(end_time-start_time)/1000;
            System.out.println("Time Elapsed (ms): "+ duration_ms);
            System.out.println("Time Elapsed (sec): "+ duration_sec);

            //LinkedList (Takes too long 3 min and 6 min resp)
            System.out.println();
            System.out.println("LinkedList");
            LookList<LinkedList<String>> ll=new LookList<>();
            start_time=System.currentTimeMillis();
            System.out.println(ll.usingList(file1, file2,new LinkedList<String>(), new LinkedList<String>()));
            end_time=System.currentTimeMillis();
            duration_ms=(int)(end_time-start_time);
            duration_sec=(int)(end_time-start_time)/1000;
            System.out.println("Time Elapsed (ms): "+ duration_ms);
            System.out.println("Time Elapsed (sec): "+ duration_sec);

            //HashSet
            System.out.println();
            System.out.println("HashSet");
            LookSet<HashSet<String>> hs=new LookSet<>();
            start_time=System.currentTimeMillis();
            System.out.println(hs.usingSet(file1, file2,new HashSet<String>(), new HashSet<String>()));
            end_time=System.currentTimeMillis();
            duration_ms=(int)(end_time-start_time);
            duration_sec=(int)(end_time-start_time)/1000;
            System.out.println("Time Elapsed (ms): "+ duration_ms);
            System.out.println("Time Elapsed (sec): "+ duration_sec);

            //TreeSet
            System.out.println();
            System.out.println("TreeSet");
            LookSet<TreeSet<String>> ts=new LookSet<>();
            start_time=System.currentTimeMillis();
            System.out.println(ts.usingSet(file1, file2,new TreeSet<String>(), new TreeSet<String>()));
            end_time=System.currentTimeMillis();
            duration_ms=(int)(end_time-start_time);
            duration_sec=(int)(end_time-start_time)/1000;
            System.out.println("Time Elapsed (ms): "+ duration_ms);
            System.out.println("Time Elapsed (sec): "+ duration_sec);
        } catch (Exception e) {
            System.out.println(e);
        }
    }
}
