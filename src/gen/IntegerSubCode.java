package gen;

public class IntegerSubCode extends CodeBasic {
   private final Reference<Integer> refa;
   private final Reference<Integer> refb;

   public IntegerSubCode(Reference<Integer> a,Reference<Integer> b){
        refa = a;
        refb = b;
   }

   public void writeCode(WriteBinaryCode wbc){
        int a = refa.getValue().intValue();
        int b = refb.getValue().intValue();
        wbc.write(a-b);
   }

   public int size(){
        return 4;
   }
}