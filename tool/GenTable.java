package tool;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.io.IOException;

public class GenTable{
    public static void main(String[] argv) throws IOException {
        String item;
        
        BufferedReader in
        = new BufferedReader(new InputStreamReader(System.in));
        
        if(argv.length < 1){
            System.err.println("Raws number not found");
            return;
        }
        
        int raws = Integer.parseInt(argv[0]);
        System.out.println("<table>");
        
        outter:{
            while(true){
                System.out.print("<tr>");
                for(int i = 0 ; i < raws;i++){
                    item = in.readLine();
                    if(item == null){
                        break outter;
                    }
                    System.out.print("<td>" + item + "</td>");
                }
                System.out.println("</tr>");
            }
        }
        System.out.println("</tr>");
        System.out.println("</table>");
        return;
    }
}