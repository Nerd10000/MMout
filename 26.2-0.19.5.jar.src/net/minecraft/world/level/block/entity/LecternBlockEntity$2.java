/*     */ package net.minecraft.world.level.block.entity;
/*     */ 
/*     */ import net.minecraft.world.inventory.ContainerData;
/*     */ 
/*     */ class null implements ContainerData {
/*     */   public int get(int dataId) {
/* 113 */     return (dataId == 0) ? LecternBlockEntity.this.page : 0;
/*     */   }
/*     */   
/*     */   public void set(int dataId, int value) {
/* 118 */     if (dataId == 0)
/* 119 */       LecternBlockEntity.this.setPage(value); 
/*     */   }
/*     */   
/*     */   public int getCount() {
/* 125 */     return 1;
/*     */   }
/*     */ }


/* Location:              C:\Users\karol\AppData\Roaming\ModrinthApp\meta\versions\26.2-0.19.5\26.2-0.19.5.jar!\net\minecraft\world\level\block\entity\LecternBlockEntity$2.class
 * Java compiler version: 25 (69.0)
 * JD-Core Version:       1.1.3
 */