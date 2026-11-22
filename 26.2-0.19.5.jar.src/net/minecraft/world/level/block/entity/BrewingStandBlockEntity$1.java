/*    */ package net.minecraft.world.level.block.entity;
/*    */ 
/*    */ import net.minecraft.world.inventory.ContainerData;
/*    */ 
/*    */ class null implements ContainerData {
/*    */   public int get(int dataId) {
/* 66 */     switch (dataId) {
/*    */       case 0:
/*    */       
/*    */       case 1:
/*    */       
/*    */     } 
/* 66 */     return 
/*    */       
/* 69 */       0;
/*    */   }
/*    */   
/*    */   public void set(int dataId, int value) {
/* 75 */     switch (dataId) {
/*    */       case 0:
/* 76 */         BrewingStandBlockEntity.this.brewTime = value;
/*    */         break;
/*    */       case 1:
/* 77 */         BrewingStandBlockEntity.this.fuel = value;
/*    */         break;
/*    */     } 
/*    */   }
/*    */   
/*    */   public int getCount() {
/* 83 */     return 2;
/*    */   }
/*    */ }


/* Location:              C:\Users\karol\AppData\Roaming\ModrinthApp\meta\versions\26.2-0.19.5\26.2-0.19.5.jar!\net\minecraft\world\level\block\entity\BrewingStandBlockEntity$1.class
 * Java compiler version: 25 (69.0)
 * JD-Core Version:       1.1.3
 */