/*    */ package net.minecraft.world.level.block.entity;
/*    */ 
/*    */ import net.minecraft.world.inventory.ContainerData;
/*    */ 
/*    */ class null implements ContainerData {
/* 46 */   private final int[] slotStates = new int[9];
/*    */   
/* 47 */   private int triggered = 0;
/*    */   
/*    */   null(CrafterBlockEntity this$0) {}
/*    */   
/*    */   public int get(int dataId) {
/* 51 */     return (dataId == 9) ? this.triggered : this.slotStates[dataId];
/*    */   }
/*    */   
/*    */   public void set(int dataId, int value) {
/* 56 */     if (dataId == 9) {
/* 57 */       this.triggered = value;
/*    */     } else {
/* 59 */       this.slotStates[dataId] = value;
/*    */     } 
/*    */   }
/*    */   
/*    */   public int getCount() {
/* 65 */     return 10;
/*    */   }
/*    */ }


/* Location:              C:\Users\karol\AppData\Roaming\ModrinthApp\meta\versions\26.2-0.19.5\26.2-0.19.5.jar!\net\minecraft\world\level\block\entity\CrafterBlockEntity$1.class
 * Java compiler version: 25 (69.0)
 * JD-Core Version:       1.1.3
 */