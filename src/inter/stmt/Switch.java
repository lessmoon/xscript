package inter.stmt;

import inter.expr.Constant;
import inter.expr.ConversionFactory;
import inter.expr.Expr;
import lexer.Char;
import lexer.Num;
import lexer.Str;
import symbols.Type;

import java.util.*;


public abstract class Switch<T> extends Stmt {
    private Expr condition;
    private boolean isdefaultset = false;
    private int indexOfDefault = 0;
    private final Map<Integer, List<T>> mapidx2case = new HashMap<>();
    protected final Map<T, Integer> map = new HashMap<>();

    private LinkedSeq stmts = new LinkedSeq();

    public Switch(Expr c) {
        condition = c;
    }

    public final void appendCase(Constant c, Stmt s){
        this.appendCaseImp(c,s);
        if(!isDefaultSet()){
            indexOfDefault = stmts.length();
        }
    }
    public abstract void appendCaseImp(Constant c, Stmt s);

    public abstract boolean isCaseSet(Constant c);

    void updateMap(T caze, Integer index) {
        mapidx2case.computeIfAbsent(index, k -> new ArrayList<>()).add(caze);
    }

    public boolean isDefaultSet() {
        return isdefaultset;
    }

    public void setDefault(Stmt s) {
        indexOfDefault = push(s);
        isdefaultset = true;
    }

    public static Switch getSwitch(Expr c) {
        if (c.type == Type.Str) {
            return new StrSwitch(c);
        } else if (c.type == Type.Char) {
            return new CharSwitch(c);
        } else if (c.type == Type.Int) {
            return new IntSwitch(c);
        } else {
            c.error("switch expression should be `" + Type.Int + "',`" + Type.Str + "' or `" + Type.Char + "',but `" + c.type + "' found");
            return new IntSwitch(c);
        }
    }

    protected final void run(Integer i) {
        try {
            if (i != null && i < stmts.length()) {
                stmts.runAt(i);
            } else {
                stmts.runAt(indexOfDefault);
            }
        } catch (RuntimeException e) {
            if (e.getCause() != Break.BreakCause)
                throw e;
        }
    }

    @Override
    public Stmt optimize() {
        LinkedSeq newStmts = new LinkedSeq();
        Iterator<Stmt> iter = stmts.iterator();
        int index = 0;
        while (iter.hasNext()) {
            final int newIndex = newStmts.length();
            Stmt tmp = iter.next();
            tmp = tmp.optimize();
            if (tmp != Stmt.Null) {
                newStmts.append(tmp);
            } else if (newIndex != index) {
                if(index == indexOfDefault){
                    indexOfDefault = newIndex;
                }

                final List<T> list = mapidx2case.remove(index);
                if (list != null) {
                    list.forEach(id -> map.put(id, newIndex));

                    List<T> t = mapidx2case.putIfAbsent(newIndex, list);
                    if (t != null) {
                        t.addAll(list);
                    }
                }
            }
            index++;
        }
        final int newIndex = newStmts.length();
        final List<T> list = mapidx2case.remove(index);
        if(index == indexOfDefault){
            indexOfDefault = newIndex;
        }
        if (list != null) {
            list.forEach(id -> map.put(id, newIndex));
            List<T> t = mapidx2case.put(newIndex, list);
            if (t != null) {
                t.addAll(list);
            }
        }

        stmts = newStmts;
        return this;
    }

    @Override
    public final void run() {
        this.run(getIndex(condition.getValue()));
    }

    public abstract Integer getIndex(Constant c);

    final int push(Stmt s) {
        int nextPos = stmts.length();
        if (s != Stmt.Null) {
            stmts.append(s);
        }
        return nextPos;
    }

    @Override
    public String toString() {
        StringBuilder sb = new StringBuilder(this.getClass().getSimpleName());
        sb.append("(")
                .append(condition)
                .append(")")
                .append("{\n");

        Iterator<Stmt> iter = stmts.iterator();
        int i = 0;
        boolean needClose = false;
        List<T> l;
        while (iter.hasNext()) {
            l = mapidx2case.get(i);
            if (l != null) {
                if (needClose) {
                    sb.append("}\n");
                }
                sb.append("case");
                l.forEach(o -> sb.append(" ").append(o));

                if(isDefaultSet()&&indexOfDefault == i){
                    sb.append(" default");
                }
                sb.append("=>{\n");
                needClose = true;
            } else if(isDefaultSet()&&indexOfDefault == i){
                sb.append("default=>{\n");
                needClose = true;
            }

            sb.append(iter.next());
            i++;
        }

        if (needClose) {
            sb.append("}\n");
        }

        if ((l = mapidx2case.get(i)) != null) {
            sb.append("case");
            l.forEach(o -> sb.append(" ").append(o));
            if(isDefaultSet()&&indexOfDefault == i){
                sb.append(" default");
            }
            sb.append("=> {\n}\n");
        } else if(isDefaultSet()&&indexOfDefault == i){
            sb.append("default{\n}\n");
        }

        sb.append("}\n");
        return sb.toString();
    }
}

class IntSwitch extends Switch<Integer> {

    IntSwitch(Expr e) {
        super(e);
    }

    @Override
    public void appendCaseImp(Constant c, Stmt s) {
        c = ConversionFactory.getConversion(c, Type.Int).getValue();
        int i = ((Num) c.op).value;
        int idx = push(s);
        updateMap(i, idx);
        map.put(i, idx);
    }

    @Override
    public boolean isCaseSet(Constant c) {
        if (Type.max(Type.Int, c.type) != Type.Int) {
            c.error("case type should be `" + Type.Int +
                    "',but `" + c.type + "' found.");
        }
        c = ConversionFactory.getConversion(c, Type.Int).getValue();
        int i = ((Num) c.op).value;
        return map.get(i) != null;
    }

    @Override
    public Integer getIndex(Constant c) {
        return map.get(((Num) c.op).value);
    }


}

class CharSwitch extends Switch<Character> {

    CharSwitch(Expr e) {
        super(e);
    }

    @Override
    public void appendCaseImp(Constant c, Stmt s) {
        c = ConversionFactory.getConversion(c, Type.Char).getValue();
        char i = ((Char) c.op).value;
        int idx = push(s);
        map.put(i, idx);
        updateMap(i, idx);
    }

    @Override
    public boolean isCaseSet(Constant c) {
        if (Type.max(Type.Int, c.type) != Type.Int) {
            c.error("case type should be `" + Type.Char +
                    "',but `" + c.type + "' found.");
        }
        if (Type.Int == c.type) {
            int i = ((Num) c.op).value;
            if (i > Character.MAX_VALUE) {
                c.error("case value " + i + " great than the max value of `" + Type.Char + "' ");
            } else if (i < Character.MIN_VALUE) {
                c.error("case value " + i + " less than the min value of `" + Type.Char + "' ");
            }
        }

        c = ConversionFactory.getConversion(c, Type.Char).getValue();
        char i = ((Char) c.op).value;
        return map.get(i) != null;
    }

    @Override
    public Integer getIndex(Constant c) {
        return map.get(((Char) c.op).value);
    }
}

class StrSwitch extends Switch<String> {

    StrSwitch(Expr e) {
        super(e);
    }

    @Override
    public void appendCaseImp(Constant c, Stmt s) {
        String i = ((Str) c.op).value;
        int idx = push(s);
        updateMap(i, idx);
        map.put(i, idx);
    }

    @Override
    public boolean isCaseSet(Constant c) {
        if (c.type != Type.Str) {
            c.error("case type should be `" + Type.Str +
                    "',but `" + c.type + "' found.");
        }
        String i = ((Str) c.op).value;
        return map.get(i) != null;
    }


    @Override
    public Integer getIndex(Constant c) {
        return map.get(((Str) c.op).value);
    }

}
