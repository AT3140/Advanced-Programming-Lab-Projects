package LnS;

import java.util.Scanner;
import java.util.List;

public class GenList<T extends List<String>> {
    public int countCommon(T al1, T al2){
        int count=0;
        int i1=0, i2=0;
        while(i1<al1.size() && i2<al2.size()){
            if(al1.get(i1).compareTo(al2.get(i2))==0) {count++;i1++;i2++;}
            else if(al1.get(i1).compareTo(al2.get(i2))<0)i1++;
            else i2++;
        }
        return count;
    }
    public void extractDistinct(Scanner fr, T al){
        fr.useDelimiter("[^A-Za-z]|\\b|\\s");
        //ArrayList<String> al=new ArrayList<>();
        al.add(fr.next().toLowerCase());
        int count=1;
        
        while(fr.hasNext()){
            String s=fr.next();
            s=s.toLowerCase();
            if(s.length()==0)
                continue;
            count++;
            //insertion sort
            int i;
            for(i=al.size()-1; i>=0; i--){
                if(s.compareTo(al.get(i))>0){
                    if(i==al.size()-1)
                        al.add(s);
                    else al.add(i+1, s);
                    break;
                } 
                else if(s.compareTo(al.get(i))==0){
                    break;
                }
            }
            if(i==-1)
                al.add(0,s);
        }
        System.out.print(count+" Words... ");
    }
}
