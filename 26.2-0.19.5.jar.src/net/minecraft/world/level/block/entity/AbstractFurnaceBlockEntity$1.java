/*     */ package net.minecraft.world.level.block.entity;
/*     */ 
/*     */ import net.minecraft.world.inventory.ContainerData;
/*     */ 
/*     */ class null implements ContainerData {
/*     */   public int get(int dataId) {
/*  86 */     switch (dataId) {
/*     */       case 0:
/*  88 */         return AbstractFurnaceBlockEntity.this.litTimeRemaining;
/*     */       case 1:
/*  91 */         return AbstractFurnaceBlockEntity.this.litTotalTime;
/*     */       case 2:
/*  94 */         return AbstractFurnaceBlockEntity.this.cookingTimer;
/*     */       case 3:
/*  97 */         return AbstractFurnaceBlockEntity.this.cookingTotalTime;
/*     */     } 
/* 102 */     return 0;
/*     */   }
/*     */   
/*     */   public void set(int dataId, int value) {
/* 107 */     switch (dataId) {
/*     */       case 0:
/* 108 */         AbstractFurnaceBlockEntity.this.litTimeRemaining = value;
/*     */         break;
/*     */       case 1:
/* 109 */         AbstractFurnaceBlockEntity.this.litTotalTime = value;
/*     */         break;
/*     */       case 2:
/* 110 */         AbstractFurnaceBlockEntity.this.cookingTimer = value;
/*     */         break;
/*     */       case 3:
/* 111 */         AbstractFurnaceBlockEntity.this.cookingTotalTime = value;
/*     */         break;
/*     */     } 
/*     */   }
/*     */   
/*     */   public int getCount() {
/* 119 */     return 4;
/*     */   }
/*     */ }


/* Location:              C:\Users\karol\AppData\Roaming\ModrinthApp\meta\versions\26.2-0.19.5\26.2-0.19.5.jar!\net\minecraft\world\level\block\entity\AbstractFurnaceBlockEntity$1.class
 * Java compiler version: 25 (69.0)
 * JD-Core Version:       1.1.3
 */