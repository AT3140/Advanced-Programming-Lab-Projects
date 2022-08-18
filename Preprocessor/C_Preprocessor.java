import java.io.*;
import java.util.*;
import java.util.regex.*;
public class _2002238_C_01d {
    public static void remove_macros(StringBuffer sb){
        try{
            StringBuffer sb1=new StringBuffer();
            Scanner sc=new Scanner(sb.toString());
            Pattern pat= Pattern.compile("#define .*");
            Matcher mat;
            while(sc.hasNext()){
                String s=sc.nextLine();
                mat=pat.matcher(s);
                if(mat.matches()){
                    continue;
                }
                sb1.append(s+'\n');
            }
            sb.replace(0,sb.length(),sb1.toString());
        }
        catch(Exception e){
            System.out.println(e);
        }
    }
    public static String find_replace(String s1, String s2, String s3){
        //eg "X,Y" "2,3" "X*Y-2*X*Y"
        Pattern pat=Pattern.compile("\\w+");
        Matcher mat1=pat.matcher(s1);
        Matcher mat2=pat.matcher(s2);
        while(mat1.find()){
            mat2.find();
            String var=mat1.group();
            String val=mat2.group();
            //System.out.println(var+" "+val);//test
            // Pattern pat0=Pattern.compile(var);
            // Matcher mat0=pat0.matcher(exp);
            s3=s3.replaceAll(var,val);
        }
        return s3;
    }
    public static void function_macros(StringBuffer sb){
        Map<String,String[]> mpf=new HashMap<String,String[]>(); //mpf "F(X,Y)": {group(2)F,group(3)X,Y,group(4)exp}
        Map<Integer,String[]> mpfi=new HashMap<Integer,String[]>(); //mpfi pos: {"F(X,Y)","F(2,3)"}
        Pattern pat=Pattern.compile("#define ((.*)\\((.*)\\)) (.*)"); //group(1): "F" group(2): "X,Y" group(3): function
        Matcher mat=pat.matcher(sb);
        while(mat.find()){
            String[]s0={mat.group(2),mat.group(3),mat.group(4)};
            //System.out.println(s0[0]);//test
            mpf.put(mat.group(1),s0);
            boolean macro_included=false;
            Pattern pat1=Pattern.compile(mat.group(2)+"\\((.*)\\)");
            Matcher mat1=pat1.matcher(sb);
            while(mat1.find()){
                if(!macro_included){
                    macro_included=true;
                    continue;
                }
                String[] s= {mat.group(1),mat1.group(1)};
                mpfi.put(mat1.start(),s);  //mp2 is pos:R
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
            if(mpfi.containsKey(i)){
                String F=mpfi.get(i)[0];
                String[] Func=mpf.get(mpfi.get(i)[0]);
                String exp= find_replace(mpf.get(F)[1],mpfi.get(i)[1],mpf.get(F)[2]); //eg ("2,3", "X*Y-2*X*Y")
                i+=Func[0].length()+1+Func[1].length()+1+1;
                sb1.append(exp);
            }
            sb1.append(sb.charAt(i++));
        }
        sb.replace(0,sb.length(),sb1.toString());
    }
    public static void macros(StringBuffer sb){
        Map<String,String> mp1=new HashMap<String,String>();
        Map<Integer,String> mp2=new HashMap<Integer,String>();
        Pattern pat=Pattern.compile("#define (.+) (.+)");
        Matcher mat=pat.matcher(sb);
        while(mat.find()){
            mp1.put(mat.group(1),mat.group(2));
            String R=mat.group(1);
            //mapping positions of constants to be replaced in mp2
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
        function_macros(sb);
        remove_macros(sb);
    }
        
    
    public static void add_lines(StringBuffer sb, String sh){
        Pattern pat2=Pattern.compile("int\\s+main");
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
                    //System.out.println(hfname1);
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
                //System.out.println(s);
                if(!mat.find())
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

    public static boolean should_remove(String s, int i){
        Pattern pat=Pattern.compile("\\s+");
        Matcher mat=pat.matcher(s);
        while(mat.find()){
            if(i>mat.start() && i<mat.end())
                return true;
        }
        pat=Pattern.compile("\\s");
        mat=pat.matcher(s);
        while(mat.find()){
            if(i==mat.start()){
                if(i+1<s.length()){
                    if(s.charAt(i+1)=='=' || s.charAt(i+1)==';' || s.charAt(i+1)==')'||s.charAt(i+1)=='(')
                        return true;
                    if(s.charAt(i+1)=='[' || s.charAt(i+1)==']' || s.charAt(i+1)=='{' || s.charAt(i+1)=='}')
                        return true;
                }
                if(i>0)
                    if(s.charAt(i-1)==',')
                        return true;
            }
        }
        return false;
    }

    public static void remove_whitespace(StringBuffer sb){
        Scanner sc=new Scanner(sb.toString());
        StringBuffer sb1=new StringBuffer(); //sb1 is temp
        Pattern pat=Pattern.compile(".*\\{.*");
        Matcher mat;
        Pattern pat1=Pattern.compile(".*\\}.*");
        Matcher mat1;
        int spaces=0;
        while(sc.hasNext()){
            String s=sc.nextLine();
            if(s.length()==0){
                continue;
            }
            mat=pat.matcher(s);
            mat1=pat1.matcher(s);
            if(!mat.find() && mat1.find())
                spaces--;
            int i;
            for(i=0; i<spaces; i++)
                sb1.append(' ');
            for(i=0; i<s.length(); i++){
                if(!(s.charAt(i)==' ' || s.charAt(i)=='\t'))
                    break;
            }
            //String s1=s.substring(i, s.length());
            boolean isliteral=false;
            for(; i<s.length(); i++)
                if(s.charAt(i)=='\"'){
                    sb1.append(s.charAt(i));
                    if(isliteral)
                        isliteral=false;
                    else
                        isliteral=true;
                }
                else if(isliteral){
                    sb1.append(s.charAt(i));
                }
                else if(!isliteral && should_remove(s,i)){
                    continue;
                }
                else 
                    sb1.append(s.charAt(i));

            if(!isliteral)
                sb1.append('\n');
            mat=pat.matcher(s);
            mat1=pat1.matcher(s);
            if(mat.find() && !mat1.find())
                spaces++;
            
        }
        sb.replace(0,sb.length(),sb1.toString());
        sc.close();
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
        //cmd
        //cd C:\2002238_C_01
        //javac _2002238_C_01d.java
        //java _2002238_C_01d.java < 01d_test1.c > 01d_test1.out
        try{
            //FileReader fr=new FileReader("test.c");
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
            remove_comments(sb);
            remove_whitespace(sb);           
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
