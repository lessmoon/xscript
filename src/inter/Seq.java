package inter;

public class Seq extends Stmt{
    Stmt stmt1;
    Stmt stmt2;

    public Seq(Stmt s1,Stmt s2){
        stmt1 = s1;
        stmt2 = s2;
    }
    
    public void run(){
        if(stmt1 != null){
            stmt1.run();
        }
        if(stmt2 != null){
            stmt2.run();
        }
    }
}