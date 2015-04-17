package main;

import java.io.*;
import lexer.*; 
import parser.*;
import inter.Stmt;

public class Main{
    public static final int     MAJOR_VERSION       = 1;
    public static final int     MINOR_VERSION       = 3;

    public static void usage(){
        System.out.println(
            "Usage of xxxscript " + MAJOR_VERSION + "." +  MINOR_VERSION + ":\n"+
            "-h --help  This message\n" +
            "-v         Version of xxxscript\n" + 
            "-eo        Enable expression optimizing [disabled at default]\n" +
            "-so        Enable statement optimizing[disabled at default]\n" +
            "-pc        Enable printing code translating result[disabled at default]\n" +
            "-pf        Enable printing function translating result[disabled at default]\n" +
            "path       The source file path you want to execute\n"+
            "           There should be only one source file\n"
        );
    }
    
    public static void main(String[] args)throws IOException{
        
        String filepath = null;
        boolean expr_opt = false;
        boolean stmt_opt  = false;
        boolean print_code_translate = false;
        boolean print_func_translate = false;

        for(int i = 0 ; i < args.length ;i++){
            switch(args[i]){
            case "-pc":
                print_code_translate = true;
                break;
            case "-pf":
                print_func_translate = true;
                break;
            case "-eo":
                expr_opt = true;
                break;
            case "-so":
                stmt_opt = true;
                break;
            case "-h":
            case "--help":
                usage();
                return;
            case "-v":
                System.out.println("Version: xxxscript " + MAJOR_VERSION + "." +  MINOR_VERSION + "\n");
                return;
            default:
                if(args[i].charAt(0) == '-'){
                    System.err.println("Unknown option `" + args[i] + "' found.");
                    return;
                }
                if(filepath != null){
                    System.err.println("Multiple file-names found.");
                    return;
                }
                filepath = args[i];
                break;
            }
        }
        Lexer lex = new Lexer();
        
        if(filepath == null ){
            System.err.println("Source file name wanted.");
            return;
        } 
        try {
            lex.open(filepath);
        } catch(Exception e) {
            System.err.println("File `" + filepath + "' open failed .");
            return;
        }

        Parser parser = new Parser(lex,expr_opt,stmt_opt);
        if(print_func_translate)
            parser.enablePrintFuncTranslate();
        Stmt s ;
        try {
            s = parser.program();
        } catch (RuntimeException e){
            //e.printStackTrace();
            System.err.println("Compile Error:");
            System.err.println(e.getMessage());
            return;
        } catch( IOException e){
            System.err.println("Compile Error(IO):");
            System.err.println(e.getMessage());
            return;
        }
        try {
            if(print_code_translate){
                System.out.print(s.toString());
                return; 
            } else if(!print_func_translate) {
                s.run();
            } else {
                return;
            }
           
        } catch (RuntimeException e){
            e.printStackTrace();
            System.err.println("Runtime Error:");
            System.err.println(e.getMessage());
            return;
        }
        System.exit(0);
    }
}