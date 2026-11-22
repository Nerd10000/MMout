/*    */ package net.minecraft.world.level.block.entity;
/*    */ 
/*    */ import java.util.List;
/*    */ 
/*    */ public interface BeaconBeamOwner {
/*    */   List<Section> getBeamSections();
/*    */   
/*    */   public static class Section {
/*    */     private final int color;
/*    */     
/*    */     private int height;
/*    */     
/*    */     public Section(int color) {
/* 11 */       this.color = color;
/* 12 */       this.height = 1;
/*    */     }
/*    */     
/*    */     public void increaseHeight() {
/* 16 */       this.height++;
/*    */     }
/*    */     
/*    */     public int getColor() {
/* 20 */       return this.color;
/*    */     }
/*    */     
/*    */     public int getHeight() {
/* 24 */       return this.height;
/*    */     }
/*    */   }
/*    */ }


/* Location:              C:\Users\karol\AppData\Roaming\ModrinthApp\meta\versions\26.2-0.19.5\26.2-0.19.5.jar!\net\minecraft\world\level\block\entity\BeaconBeamOwner.class
 * Java compiler version: 25 (69.0)
 * JD-Core Version:       1.1.3
 */