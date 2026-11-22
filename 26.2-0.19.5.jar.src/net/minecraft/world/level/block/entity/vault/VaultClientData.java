/*    */ package net.minecraft.world.level.block.entity.vault;
/*    */ 
/*    */ import net.minecraft.util.Mth;
/*    */ 
/*    */ public class VaultClientData {
/*    */   public static final float ROTATION_SPEED = 10.0F;
/*    */   
/*    */   private float currentSpin;
/*    */   
/*    */   private float previousSpin;
/*    */   
/*    */   public float currentSpin() {
/* 14 */     return this.currentSpin;
/*    */   }
/*    */   
/*    */   public float previousSpin() {
/* 18 */     return this.previousSpin;
/*    */   }
/*    */   
/*    */   public void updateDisplayItemSpin() {
/* 22 */     this.previousSpin = this.currentSpin;
/* 23 */     this.currentSpin = Mth.wrapDegrees(this.currentSpin + 10.0F);
/*    */   }
/*    */ }


/* Location:              C:\Users\karol\AppData\Roaming\ModrinthApp\meta\versions\26.2-0.19.5\26.2-0.19.5.jar!\net\minecraft\world\level\block\entity\vault\VaultClientData.class
 * Java compiler version: 25 (69.0)
 * JD-Core Version:       1.1.3
 */