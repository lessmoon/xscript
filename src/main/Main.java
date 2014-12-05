package main;

import java.io.*;import lexer.*; import parser.*;

public class Main{
    public static void main(String[] args)throws IOException{
        Lexer lex = new Lexer();
        Parser parser = new Parser(lex);
        parser.block();
        System.out.write('\n');
    }
}