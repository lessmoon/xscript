package main;

import java.io.*;
import lexer.*; 
import parser.*;
import inter.Stmt;

public class Main{
    public static void main(String[] args)throws IOException{
        Lexer lex = new Lexer();
        if(args.length > 0){
            try {
                lex.open(args[0]);
            } catch(Exception e){
                System.err.println("File `" + args[0] + "' not found.");
                return;
            }
        } else {
            System.err.println("Source file name wanted.");
            return;
        }
        Parser parser = new Parser(lex);
        Stmt s ;
        try {
            s = parser.program();
        } catch (RuntimeException e){
            System.err.println("Compile Error:");
            
            System.err.println(e.getMessage());
            return;
        } catch( IOException e){
            System.err.println("Compile Error(IO):");
            System.err.println(e.getMessage());
            return;
        }
        try {
            s.run();
        } catch (RuntimeException e){
            System.err.println("Runtime Error:");
            e.printStackTrace();
            System.err.println(e.getMessage());
            return;
        }
    }
}