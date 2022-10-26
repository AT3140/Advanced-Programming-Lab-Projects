package Lookup;

import LnS.GenList;
import java.util.List;
import java.util.Scanner;
import java.io.BufferedReader;
import java.io.FileReader;

public class LookList<T extends List<String>> {
    public int usingList(String file1, String file2, T t1, T t2){
        try {
            Scanner fr1= new Scanner(new BufferedReader(new FileReader(file1)));
            Scanner fr2= new Scanner(new BufferedReader(new FileReader(file2)));

            //ArrayList
            GenList<T> gen=new GenList<T>();

            gen.extractDistinct(fr1,t1);
            System.out.println("Book1 extracted!");//test
            gen.extractDistinct(fr2,t2);
            System.out.println("Book2 extracted!");//test

                //test commands
                //for(String s:al1) System.out.print(s+" ");
                //System.out.println(al1.size());
            fr1.close();
            fr2.close();
            return gen.countCommon(t1,t2);

        } catch (Exception e) {
            System.out.println(e);
        }
        return 0;
    }
}
