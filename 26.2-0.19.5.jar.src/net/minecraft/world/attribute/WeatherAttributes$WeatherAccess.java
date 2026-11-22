/*    */ package net.minecraft.world.attribute;
/*    */ 
/*    */ import net.minecraft.world.level.Level;
/*    */ 
/*    */ public interface WeatherAccess {
/*    */   static WeatherAccess from(final Level level) {
/* 67 */     return new WeatherAccess() {
/*    */         public float rainLevel() {
/* 70 */           return level.getRainLevel(1.0F);
/*    */         }
/*    */         
/*    */         public float thunderLevel() {
/* 75 */           return level.getThunderLevel(1.0F);
/*    */         }
/*    */       };
/*    */   }
/*    */   
/*    */   float rainLevel();
/*    */   
/*    */   float thunderLevel();
/*    */ }


/* Location:              C:\Users\karol\AppData\Roaming\ModrinthApp\meta\versions\26.2-0.19.5\26.2-0.19.5.jar!\net\minecraft\world\attribute\WeatherAttributes$WeatherAccess.class
 * Java compiler version: 25 (69.0)
 * JD-Core Version:       1.1.3
 */