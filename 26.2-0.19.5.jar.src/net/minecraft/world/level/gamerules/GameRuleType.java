/*    */ package net.minecraft.world.level.gamerules;
/*    */ 
/*    */ import net.minecraft.util.StringRepresentable;
/*    */ 
/*    */ public enum GameRuleType implements StringRepresentable {
/*  6 */   INT("integer"),
/*  7 */   BOOL("boolean");
/*    */   
/*    */   private final String name;
/*    */   
/*    */   GameRuleType(String name) {
/* 11 */     this.name = name;
/*    */   }
/*    */   
/*    */   public String getSerializedName() {
/* 16 */     return this.name;
/*    */   }
/*    */ }


/* Location:              C:\Users\karol\AppData\Roaming\ModrinthApp\meta\versions\26.2-0.19.5\26.2-0.19.5.jar!\net\minecraft\world\level\gamerules\GameRuleType.class
 * Java compiler version: 25 (69.0)
 * JD-Core Version:       1.1.3
 */