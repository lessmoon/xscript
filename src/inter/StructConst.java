package inter;

import lexer.*;
import symbols.*;

import java.util.HashMap;
import java.util.Iterator;
import java.util.Map.Entry;

public class StructConst extends Constant {
    public  int size;
    HashMap<Token,Constant> table = new HashMap<Token,Constant>();

    public StructConst( Struct t ){
        super(Word.struct,t);
        /*Initial every member*/
        Iterator<Entry<Token,Type>> iter = t.table.entrySet().iterator();
        while(iter.hasNext()){
            Entry<Token,Type> info = iter.next();
            Constant c = null;
            if(info.getValue() instanceof Array){
                c = new ArrayConst((Array)info.getValue());
            }
            table.put(info.getKey(),c);
        }
    }

    public Constant getValue(){
        return this;
    }

    public Constant setElement(Token i,Constant c){
        assert(table.containsKey(i));
        table.put(i,c);
        return c;
    }

    public Constant getElement(Token i){
        assert(table.containsKey(i));
        return table.get(i);
    }
    
    public int getSize(){
        return table.size();
    }

    boolean isChangeable(){
        return true;
    }

    public String toString(){
        StringBuffer sb = new StringBuffer("{");
        Iterator<Entry<Token,Constant>> iter = table.entrySet().iterator();
        while(iter.hasNext()){
            Entry<Token,Constant> info = iter.next();
            sb.append(info.getKey().toString());
            sb.append("=");
            sb.append("" + info.getValue());
            sb.append(";");
        }
        sb.append("}");
        return sb.toString();
    }

}