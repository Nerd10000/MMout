/*    */ package net.minecraft.world.level.block.entity;
/*    */ 
/*    */ import net.minecraft.util.Mth;
/*    */ 
/*    */ public class ChestLidController {
/*    */   private boolean shouldBeOpen;
/*    */   
/*    */   private float openness;
/*    */   
/*    */   private float oOpenness;
/*    */   
/*    */   public void tickLid() {
/* 11 */     this.oOpenness = this.openness;
/* 13 */     float speed = 0.1F;
/* 15 */     if (!this.shouldBeOpen && this.openness > 0.0F) {
/* 16 */       this.openness = Math.max(this.openness - 0.1F, 0.0F);
/* 17 */     } else if (this.shouldBeOpen && this.openness < 1.0F) {
/* 18 */       this.openness = Math.min(this.openness + 0.1F, 1.0F);
/*    */     } 
/*    */   }
/*    */   
/*    */   public float getOpenness(float a) {
/* 23 */     return Mth.lerp(a, this.oOpenness, this.openness);
/*    */   }
/*    */   
/*    */   public void shouldBeOpen(boolean shouldBeOpen) {
/* 27 */     this.shouldBeOpen = shouldBeOpen;
/*    */   }
/*    */ }


/* Location:              C:\Users\karol\AppData\Roaming\ModrinthApp\meta\versions\26.2-0.19.5\26.2-0.19.5.jar!\net\minecraft\world\level\block\entity\ChestLidController.class
 * Java compiler version: 25 (69.0)
 * JD-Core Version:       1.1.3
 */