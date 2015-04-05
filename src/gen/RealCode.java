package gen;

public class RealCode extends CodeBasic {
   final Reference<Float> ref;

   public RealCode(float i){
        ref  = new Reference<Float>();
        ref.setValue(new Float(i));
   }

   public RealCode(Reference<Float> x){
        ref = x;
   }

   public void writeCode(WriteBinaryCode wbc){
        wbc.write(ref.getValue().floatValue());
   }

   public int size(){
        return Float.SIZE;/*size of float code*/
   }
}