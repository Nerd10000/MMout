/*    */ package net.minecraft.util.valueproviders;
/*    */ 
/*    */ import java.util.Arrays;
/*    */ import net.minecraft.util.RandomSource;
/*    */ 
/*    */ public class MultipliedFloats implements SampledFloat {
/*    */   private final SampledFloat[] values;
/*    */   
/*    */   public MultipliedFloats(SampledFloat... values) {
/* 11 */     this.values = values;
/*    */   }
/*    */   
/*    */   public float sample(RandomSource random) {
/* 16 */     float result = 1.0F;
/* 17 */     for (SampledFloat value : this.values)
/* 18 */       result *= value.sample(random); 
/* 20 */     return result;
/*    */   }
/*    */   
/*    */   public String toString() {
/* 25 */     return "MultipliedFloats" + Arrays.toString((Object[])this.values);
/*    */   }
/*    */ }


/* Location:              C:\Users\karol\AppData\Roaming\ModrinthApp\meta\versions\26.2-0.19.5\26.2-0.19.5.jar!\net\minecraf\\util\valueproviders\MultipliedFloats.class
 * Java compiler version: 25 (69.0)
 * JD-Core Version:       1.1.3
 */