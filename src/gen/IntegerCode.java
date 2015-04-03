package gen;

public class IntegerCode extends CodeBasic {
   final Reference<Integer> ref;

   public IntegerCode(int i){
        ref  = new Reference<Integer>();
        ref.setValue(new Integer(i));
   }

   public IntegerCode(Reference<Integer> x){
        ref = x;
   }

   public void writeCode(WriteBinaryCode wbc){
        wbc.write(ref.getValue().intValue());
   }
   
   public int size(){
        return 4;
   }
}