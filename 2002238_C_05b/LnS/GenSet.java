package LnS;

import java.util.Scanner;
import java.util.Set;

public class GenSet<T extends Set<String>> {
    public void extractDistinct(Scanner fr, T t){
        try {
            fr.useDelimiter("[^A-Za-z]|\\b|\\s");
            t.add(fr.next().toLowerCase());
            int count=1;
            
            while(fr.hasNext()){
                String s=fr.next();
                s=s.toLowerCase();
                if(s.length()==0)
                    continue;
                count++;
                t.add(s);
            }
            System.out.print(count+" Words... ");
            
        } catch (Exception e) {
            // TODO: handle exception
        }
    }
    public int countCommon(T t1, T t2){
        int count=0;
        for(String s: t1){
            if(t2.contains(s))
                count++;
        }
        return count;
    }
}
