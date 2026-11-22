/*    */ package net.minecraft.world.attribute.modifier;
/*    */ 
/*    */ import com.mojang.serialization.Codec;
/*    */ import net.minecraft.world.attribute.EnvironmentAttribute;
/*    */ import net.minecraft.world.attribute.LerpFunction;
/*    */ 
/*    */ public interface IntegerModifier<Argument> extends AttributeModifier<Integer, Argument> {
/* 11 */   public static final IntegerModifier<Integer> ADD = Integer::sum;
/*    */   
/*    */   public static final IntegerModifier<Integer> SUBTRACT;
/*    */   
/*    */   public static final IntegerModifier<Integer> MULTIPLY;
/*    */   
/*    */   static {
/* 12 */     SUBTRACT = ((a, b) -> Integer.valueOf(a.intValue() - b.intValue()));
/* 13 */     MULTIPLY = ((a, b) -> Integer.valueOf(a.intValue() * b.intValue()));
/*    */   }
/*    */   
/* 14 */   public static final IntegerModifier<Integer> MINIMUM = Math::min;
/*    */   
/* 15 */   public static final IntegerModifier<Integer> MAXIMUM = Math::max;
/*    */   
/*    */   Integer apply(Integer paramInteger, Argument paramArgument);
/*    */   
/*    */   @FunctionalInterface
/*    */   public static interface Simple extends IntegerModifier<Integer> {
/*    */     default Codec<Integer> argumentCodec(EnvironmentAttribute<Integer> type) {
/* 21 */       return (Codec<Integer>)Codec.INT;
/*    */     }
/*    */     
/*    */     default LerpFunction<Integer> argumentKeyframeLerp(EnvironmentAttribute<Integer> type) {
/* 26 */       return LerpFunction.ofInteger();
/*    */     }
/*    */   }
/*    */ }


/* Location:              C:\Users\karol\AppData\Roaming\ModrinthApp\meta\versions\26.2-0.19.5\26.2-0.19.5.jar!\net\minecraft\world\attribute\modifier\IntegerModifier.class
 * Java compiler version: 25 (69.0)
 * JD-Core Version:       1.1.3
 */