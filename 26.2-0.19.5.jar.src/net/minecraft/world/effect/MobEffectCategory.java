/*    */ package net.minecraft.world.effect;
/*    */ 
/*    */ import net.minecraft.ChatFormatting;
/*    */ 
/*    */ public enum MobEffectCategory {
/*  6 */   BENEFICIAL(ChatFormatting.BLUE),
/*  7 */   HARMFUL(ChatFormatting.RED),
/*  8 */   NEUTRAL(ChatFormatting.BLUE);
/*    */   
/*    */   private final ChatFormatting tooltipFormatting;
/*    */   
/*    */   MobEffectCategory(ChatFormatting tooltipFormatting) {
/* 13 */     this.tooltipFormatting = tooltipFormatting;
/*    */   }
/*    */   
/*    */   public ChatFormatting getTooltipFormatting() {
/* 17 */     return this.tooltipFormatting;
/*    */   }
/*    */ }


/* Location:              C:\Users\karol\AppData\Roaming\ModrinthApp\meta\versions\26.2-0.19.5\26.2-0.19.5.jar!\net\minecraft\world\effect\MobEffectCategory.class
 * Java compiler version: 25 (69.0)
 * JD-Core Version:       1.1.3
 */