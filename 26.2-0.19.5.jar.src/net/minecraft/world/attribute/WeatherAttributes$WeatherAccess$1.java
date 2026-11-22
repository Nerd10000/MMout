/*    */ package net.minecraft.world.attribute;
/*    */ 
/*    */ import net.minecraft.world.level.Level;
/*    */ 
/*    */ class null implements WeatherAttributes.WeatherAccess {
/*    */   public float rainLevel() {
/* 70 */     return level.getRainLevel(1.0F);
/*    */   }
/*    */   
/*    */   public float thunderLevel() {
/* 75 */     return level.getThunderLevel(1.0F);
/*    */   }
/*    */ }


/* Location:              C:\Users\karol\AppData\Roaming\ModrinthApp\meta\versions\26.2-0.19.5\26.2-0.19.5.jar!\net\minecraft\world\attribute\WeatherAttributes$WeatherAccess$1.class
 * Java compiler version: 25 (69.0)
 * JD-Core Version:       1.1.3
 */