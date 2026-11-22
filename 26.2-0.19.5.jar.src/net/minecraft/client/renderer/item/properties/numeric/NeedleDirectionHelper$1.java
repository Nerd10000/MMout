/*    */ package net.minecraft.client.renderer.item.properties.numeric;
/*    */ 
/*    */ import net.minecraft.util.Mth;
/*    */ 
/*    */ class null implements NeedleDirectionHelper.Wobbler {
/*    */   private float rotation;
/*    */   
/*    */   private float deltaRotation;
/*    */   
/*    */   private long lastUpdateTick;
/*    */   
/*    */   public float rotation() {
/* 58 */     return this.rotation;
/*    */   }
/*    */   
/*    */   public boolean shouldUpdate(long tick) {
/* 63 */     return (this.lastUpdateTick != tick);
/*    */   }
/*    */   
/*    */   public void update(long tick, float targetRotation) {
/* 68 */     this.lastUpdateTick = tick;
/* 69 */     float tempDeltaRotation = Mth.positiveModulo(targetRotation - this.rotation + 0.5F, 1.0F) - 0.5F;
/* 71 */     this.deltaRotation += tempDeltaRotation * 0.1F;
/* 72 */     this.deltaRotation *= factor;
/* 73 */     this.rotation = Mth.positiveModulo(this.rotation + this.deltaRotation, 1.0F);
/*    */   }
/*    */ }


/* Location:              C:\Users\karol\AppData\Roaming\ModrinthApp\meta\versions\26.2-0.19.5\26.2-0.19.5.jar!\net\minecraft\client\renderer\item\properties\numeric\NeedleDirectionHelper$1.class
 * Java compiler version: 25 (69.0)
 * JD-Core Version:       1.1.3
 */