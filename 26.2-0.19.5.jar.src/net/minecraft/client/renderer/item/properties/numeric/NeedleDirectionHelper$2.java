/*    */ package net.minecraft.client.renderer.item.properties.numeric;
/*    */ 
/*    */ class null implements NeedleDirectionHelper.Wobbler {
/*    */   private float targetValue;
/*    */   
/*    */   public float rotation() {
/* 84 */     return this.targetValue;
/*    */   }
/*    */   
/*    */   public boolean shouldUpdate(long tick) {
/* 89 */     return true;
/*    */   }
/*    */   
/*    */   public void update(long tick, float targetRotation) {
/* 94 */     this.targetValue = targetRotation;
/*    */   }
/*    */ }


/* Location:              C:\Users\karol\AppData\Roaming\ModrinthApp\meta\versions\26.2-0.19.5\26.2-0.19.5.jar!\net\minecraft\client\renderer\item\properties\numeric\NeedleDirectionHelper$2.class
 * Java compiler version: 25 (69.0)
 * JD-Core Version:       1.1.3
 */