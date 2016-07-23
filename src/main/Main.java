package main;

import inter.expr.ArithFactory;
import inter.expr.ArrayConst;
import inter.expr.Constant;
import inter.stmt.Stmt;
import lexer.Lexer;
import parser.Parser;
import runtime.RunStack;
import runtime.VarTable;
import symbols.Array;
import symbols.Type;

import java.io.IOException;

public class Main{
    public static final int     MAJOR_VERSION       = 1;
    public static final int     MINOR_VERSION       = 8;

    private static void usage(){
        System.out.println(
            "Usage of xscript " + MAJOR_VERSION + "." +  MINOR_VERSION + ":\n"+
            "-h --help  This message\n" +
            "-v         Version of xxxscript\n" + 
            "-eo        Enable expression optimizing [disabled at default]\n" +
            "-so        Enable statement optimizing[disabled at default]\n" +
            "-pc        Enable printing code translating result[disabled at default]\n" +
            "-pf        Enable printing function translating result[disabled at default]\n" +
			"-brds --bigrealdivscale set the bigreal division scale[100 at default]" + 
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
        int index = 0 ;
        outer:{
            for(; index < args.length ;index++){
                switch(args[index]){
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
				case "-brds":
				case "--bigrealdivscale":
					index++;
					if(index >= args.length){
						System.err.println("number expected by option `" + args[index-1] + "'");
						return;
					}
					try{
						ArithFactory.setBigRealDivideScale(Integer.parseInt(args[index]));
					} catch(NumberFormatException e) {
						System.err.println("number expected by option `" + args[index-1] + "',but found `" +  args[index] + "'");
						return;
					}
					break;
				case "-h":
                case "--help":
                    usage();
                    return;
                case "-v":
                    System.out.println("Version: xxxscript " + MAJOR_VERSION + "." +  MINOR_VERSION + "\n");
                    return;
                default:
                    if(args[index].charAt(0) == '-'){
                        System.err.println("Unknown option `" + args[index] + "' found.");
                        return;
                    }
                    filepath = args[index];
                    break outer;
                }
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
            e.printStackTrace();
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
                /* push arguments */
                ArrayConst a = new ArrayConst(new Array(Type.Str),args.length - index);

                for(int i = 0;i + index < args.length;i++){
                    a.setElement(i,new Constant(args[i + index]));
                }
                VarTable.pushVar(a);

                s.run();
            } else {
                return;
            }
           
        } catch (RuntimeException e){
            //e.printStackTrace();
            System.err.println("Runtime Error:");
            RunStack.printStackTrace(e.getMessage());
            return;
        }
        System.exit(0);
    }
}