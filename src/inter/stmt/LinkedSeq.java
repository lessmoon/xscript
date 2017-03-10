package inter.stmt;

import java.util.Iterator;
import java.util.LinkedList;
import java.util.List;
import java.util.ListIterator;

/**
 * Created by lessmoon on 2017/3/7.
 */
public class LinkedSeq extends Stmt {
    private final List<Stmt> stmts = new LinkedList<>();

    public LinkedSeq() {
    }

    @Override
    public String toString() {
        StringBuilder sb = new StringBuilder();
        stmts.stream().map(Object::toString).forEach(sb::append);
        return sb.toString();
    }

    void appendStmt(Stmt s) {
        stmts.add(s);
    }

    public void append(Stmt s) {
        s.appendToSeq(this);
    }

    /**
     * merge seq2 to seq1,seq1 U seq2 => seq1
     */
    private static void merge(LinkedSeq seq1, LinkedSeq seq2) {
        seq1.stmts.addAll(seq2.stmts);
    }

    @Override
    public void run() {
        stmts.forEach(Stmt::run);
    }

    public void runAt(int beg){
        stmts.listIterator(beg).forEachRemaining(Stmt::run);
    }

    public int length(){
        return stmts.size();
    }

    public Iterator<Stmt> iterator(){
        return stmts.iterator();
    }

    @Override
    public Stmt optimize() {
        ListIterator<Stmt> iter = stmts.listIterator();
        while (iter.hasNext()) {
            iter.set(iter.next().optimize());
        }
        iter = stmts.listIterator();
        while (iter.hasNext()) {
            Stmt stmt = iter.next();
            if (stmt == Stmt.Null) {
                iter.remove();
            }
            if (stmt.isLastStmt()) {
                while (iter.hasNext()) {
                    iter.next();
                    iter.remove();
                }
                break;
            }
        }
        switch (stmts.size()) {
            case 0:
                return Stmt.Null;
            case 1:
                return stmts.get(0);
            case 2:
                return new Seq(stmts.get(0), stmts.get(1));
            default:
                return this;
        }
    }

    @Override
    public boolean isLastStmt() {
        return stmts.stream().anyMatch(Stmt::isLastStmt);
    }

    @Override
    public void appendToSeq(LinkedSeq s) {
        merge(s, this);
    }
}
