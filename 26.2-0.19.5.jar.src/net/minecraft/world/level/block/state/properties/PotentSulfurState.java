/*    */ package net.minecraft.world.level.block.state.properties;
/*    */ 
/*    */ import net.minecraft.util.StringRepresentable;
/*    */ 
/*    */ public enum PotentSulfurState implements StringRepresentable {
/*  6 */   DRY("dry"),
/*  7 */   WET("wet"),
/*  8 */   DORMANT("dormant"),
/*  9 */   ERUPTING("erupting"),
/* 10 */   CONTINUOUS("continuous");
/*    */   
/*    */   private final String name;
/*    */   
/*    */   PotentSulfurState(String name) {
/* 16 */     this.name = name;
/*    */   }
/*    */   
/*    */   public String toString() {
/* 21 */     return this.name;
/*    */   }
/*    */   
/*    */   public String getSerializedName() {
/* 26 */     return this.name;
/*    */   }
/*    */ }


/* Location:              C:\Users\karol\AppData\Roaming\ModrinthApp\meta\versions\26.2-0.19.5\26.2-0.19.5.jar!\net\minecraft\world\level\block\state\properties\PotentSulfurState.class
 * Java compiler version: 25 (69.0)
 * JD-Core Version:       1.1.3
 */