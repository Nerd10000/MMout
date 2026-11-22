/*     */ package net.minecraft.client.gui.components;
/*     */ 
/*     */ import net.minecraft.client.multiplayer.chat.GuiMessage;
/*     */ import net.minecraft.util.Mth;
/*     */ 
/*     */ @FunctionalInterface
/*     */ interface AlphaCalculator {
/*     */   public static final AlphaCalculator FULLY_VISIBLE = paramLine -> 1.0F;
/*     */   
/*     */   static AlphaCalculator timeBased(int currentTickTime) {
/* 583 */     return message -> {
/*     */         int tickDelta = currentTickTime - message.addedTime();
/*     */         double t = tickDelta / 200.0D;
/*     */         t = 1.0D - t;
/*     */         t *= 10.0D;
/*     */         t = Mth.clamp(t, 0.0D, 1.0D);
/*     */         t *= t;
/*     */         return (float)t;
/*     */       };
/*     */   }
/*     */   
/*     */   float calculate(GuiMessage.Line paramLine);
/*     */ }


/* Location:              C:\Users\karol\AppData\Roaming\ModrinthApp\meta\versions\26.2-0.19.5\26.2-0.19.5.jar!\net\minecraft\client\gui\components\ChatComponent$AlphaCalculator.class
 * Java compiler version: 25 (69.0)
 * JD-Core Version:       1.1.3
 */