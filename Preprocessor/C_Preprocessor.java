import java.io.*;
import java.util.*;
import java.util.regex.*;
public class preprocessor_1d {
    public static void macros(StringBuffer sb){
        Map<String,String> mp1=new HashMap<String,String>();
        Map<Integer,String> mp2=new HashMap<Integer,String>();
        Pattern pat=Pattern.compile("#define (.+) (.+)");
        Matcher mat=pat.matcher(sb);
        while(mat.find()){
            mp1.put(mat.group(1),mat.group(2));
            String R=mat.group(1);
            Pattern pat1=Pattern.compile("\\b"+R+"\\b");
            Matcher mat1=pat1.matcher(sb);
            boolean macro_included=false;
            while(mat1.find()){
                if(!macro_included){
                    macro_included=true;
                    continue;
                }
                mp2.put(mat1.start(),R);  //mp2 is pos:R
            }
        }

        boolean isliteral=false;
        StringBuffer sb1=new StringBuffer();
        for(int i=0; i<sb.length();){
            if(sb.charAt(i)=='\\'){ //escape sequence
                sb1.append(sb.charAt(i++));
                sb1.append(sb.charAt(i++));
                continue;
            }
            if(sb.charAt(i)=='\"'){
                sb1.append(sb.charAt(i++));
                if(isliteral)
                    isliteral=false;
                else isliteral=true;
                continue;
            }
            if(isliteral){
                sb1.append(sb.charAt(i++));
                continue;
            }
            if(mp2.containsKey(i)){
                String R=mp2.get(i);
                i+=R.length();
                sb1.append(mp1.get(R));
            }
            sb1.append(sb.charAt(i++));
        }
        sb.replace(0,sb.length(),sb1.toString());
    }
        
    
    public static void add_lines(StringBuffer sb, String sh){
        Pattern pat2=Pattern.compile("int main");
        Matcher mat2=pat2.matcher(sb);
        if(mat2.find())
            sb.insert(mat2.start(), sh+"\n");
    }
    public static void add_file(StringBuffer sb, Set<String> hfs,String hfname) {
        try{
            File hf=new File(hfname);
            Scanner sch=new Scanner(hf);
            String sh;
            Pattern pat1=Pattern.compile("^#include \"(.*)\"");
            Matcher mat1;
            while(sch.hasNext()){
                sh=sch.nextLine();
                mat1=pat1.matcher(sh); 
                if(mat1.matches()){//line is #include type 
                    String hfname1=mat1.group(1);
                    if(!hfs.contains(hfname1))
                        {hfs.add(hfname1);add_file(sb,hfs,hfname1);}
                    continue;
                }
                add_lines(sb,sh);
            }
            sch.close();
        }
        catch(Exception e){
            System.out.println("add_file() method error: "+e);
        }
        
    }

    public static void header_file(StringBuffer sb){
        Set<String> hfs=new HashSet<String>();
        try{
            Scanner sc=new Scanner(sb.toString());
            Pattern pat=Pattern.compile("^#include \"(.*)\"");
            Matcher mat;
            while(sc.hasNext()){
                String s=sc.nextLine(); 
                mat=pat.matcher(s);
                if(!mat.matches())
                    continue;
                
                String hfname=mat.group(1); 
                
                if(hfs.contains(hfname)){//hfname already in hfs
                    continue;
                } 
                hfs.add(hfname);//push hfname to set hfs 
                add_file(sb,hfs,hfname);
            }
            sc.close();

            //removing #include line
            mat=pat.matcher(sb);
            while(mat.find()){
                sb.delete(mat.start(),mat.end()+1);
                mat=pat.matcher(sb);
            }
        }
        catch(Exception e){
            System.out.println(e);
        }
    }   

    public static void remove_whitespace(StringBuffer sb){
        Scanner sc=new Scanner(sb.toString());
        StringBuffer sb1=new StringBuffer();
        while(sc.hasNext()){
            String s=sc.nextLine();
            if(s.length()==0){
                continue;
            }
            sb1.append(s+'\n');
        }
        sb.replace(0,sb.length(),sb1.toString());
        sc.close();

        Pattern pat=Pattern.compile("\\s{3,}+");
        Matcher mat= pat.matcher(sb);
        ArrayList<Integer> alr=new ArrayList<>();
        while(mat.find()){
            alr.add(mat.start());
            alr.add(mat.end()-2);
        } 
        //ArrayList alr contains start,end,start,end,... format for indicating characters to delete
        sb1=new StringBuffer();
        boolean isliteral=false;
        int p=0;
        for(int i=0; i<sb.length();){
            if(sb.charAt(i)=='\"')
                if(isliteral){
                    isliteral=true;
                    i++;continue;
                }
                else{
                    isliteral=false;
                    i++;continue;
                }
            if(p<alr.size()){
                while(i<alr.get(p))
                    sb1.append(sb.charAt(i++));
                p++;
                while(i<=alr.get(p)){
                    i++;
                }
                p++;
            }
            else sb1.append(sb.charAt(i++));
        }
        sb.replace(0,sb.length(),sb1.toString());
    }
    

    public static void remove_comments(StringBuffer sb){
        Pattern pat=Pattern.compile("/\\*[^*]*\\*+(?:[^/*][^*]*\\*+)*/");
        Matcher mat=pat.matcher(sb);
        while(mat.find()){
            sb.delete(mat.start(), mat.end());
            mat=pat.matcher(sb);
        }
    }
    public static void main(String[] args) {
        //input C file via IO Redirection
        //cmd
        //cd C:\2002238_C_01
        //javac preprocessor_1d.java
        //java preprocessor_1d.java < 01d_test1.c
        try{
            //FileReader fr=new FileReader("01d_test1.c");
            Scanner sci= new Scanner(System.in);
            
            StringBuffer sb=new StringBuffer();

            // int chi;
            // while((chi=fr.read())!=-1){
            //     sb.append((char)chi);
            // }//correct

            while(sci.hasNext()){
                sb.append(sci.nextLine());
                sb.append('\n');
            }
             
            remove_comments(sb);
            remove_whitespace(sb);
            header_file(sb);
            macros(sb);
            
            System.out.println(sb);           
            
            //fr.close();
            sci.close();
        }
        catch(Exception e){
            System.out.println(e);
        }
    }
}
