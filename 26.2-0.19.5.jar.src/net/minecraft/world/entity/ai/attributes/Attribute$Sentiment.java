/*    */ package net.minecraft.world.entity.ai.attributes;
/*    */ 
/*    */ import net.minecraft.ChatFormatting;
/*    */ 
/*    */ public enum Sentiment {
/* 58 */   POSITIVE, NEUTRAL, NEGATIVE;
/*    */   
/*    */   public ChatFormatting getStyle(boolean valueIncrease) {
/* 64 */     switch (ordinal()) {
/*    */       default:
/* 64 */         throw new MatchException(null, null);
/*    */       case 0:
/* 64 */         return 
/* 65 */           valueIncrease ? ChatFormatting.BLUE : ChatFormatting.RED;
/*    */       case 1:
/*    */       
/*    */       case 2:
/*    */         break;
/*    */     } 
/* 67 */     return valueIncrease ? ChatFormatting.RED : ChatFormatting.BLUE;
/*    */   }
/*    */ }


/* Location:              C:\Users\karol\AppData\Roaming\ModrinthApp\meta\versions\26.2-0.19.5\26.2-0.19.5.jar!\net\minecraft\world\entity\ai\attributes\Attribute$Sentiment.class
 * Java compiler version: 25 (69.0)
 * JD-Core Version:       1.1.3
 */