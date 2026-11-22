/*    */ package net.minecraft.world.attribute.modifier;
/*    */ 
/*    */ import com.mojang.serialization.Codec;
/*    */ import net.minecraft.world.attribute.EnvironmentAttribute;
/*    */ import net.minecraft.world.attribute.LerpFunction;
/*    */ 
/*    */ public enum BooleanModifier implements AttributeModifier<Boolean, Boolean> {
/*  8 */   AND, NAND, OR, NOR, XOR, XNOR;
/*    */   
/*    */   public Boolean apply(Boolean subject, Boolean argument) {
/* 18 */     switch (ordinal()) {
/*    */       default:
/* 18 */         throw new MatchException(null, null);
/*    */       case 0:
/*    */       
/*    */       case 1:
/*    */       
/*    */       case 2:
/*    */       
/*    */       case 3:
/*    */       
/*    */       case 4:
/*    */       
/*    */       case 5:
/*    */         break;
/*    */     } 
/* 18 */     return 
/*    */       
/* 24 */       Boolean.valueOf((argument == subject));
/*    */   }
/*    */   
/*    */   public Codec<Boolean> argumentCodec(EnvironmentAttribute<Boolean> type) {
/* 30 */     return (Codec<Boolean>)Codec.BOOL;
/*    */   }
/*    */   
/*    */   public LerpFunction<Boolean> argumentKeyframeLerp(EnvironmentAttribute<Boolean> type) {
/* 35 */     return LerpFunction.ofConstant();
/*    */   }
/*    */ }


/* Location:              C:\Users\karol\AppData\Roaming\ModrinthApp\meta\versions\26.2-0.19.5\26.2-0.19.5.jar!\net\minecraft\world\attribute\modifier\BooleanModifier.class
 * Java compiler version: 25 (69.0)
 * JD-Core Version:       1.1.3
 */