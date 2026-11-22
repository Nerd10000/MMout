/*     */ package net.minecraft.world.level.block.entity;
/*     */ 
/*     */ import net.minecraft.sounds.SoundEvents;
/*     */ import net.minecraft.world.inventory.BeaconMenu;
/*     */ import net.minecraft.world.inventory.ContainerData;
/*     */ 
/*     */ class null implements ContainerData {
/*     */   public int get(int dataId) {
/*  94 */     switch (dataId) {
/*     */       case 0:
/*     */       
/*     */       case 1:
/*     */       
/*     */       case 2:
/*     */       
/*     */     } 
/*  94 */     return 
/*     */       
/*  98 */       0;
/*     */   }
/*     */   
/*     */   public void set(int dataId, int value) {
/* 104 */     switch (dataId) {
/*     */       case 0:
/* 105 */         BeaconBlockEntity.this.levels = value;
/*     */         break;
/*     */       case 1:
/* 107 */         if (!BeaconBlockEntity.this.level.isClientSide() && !BeaconBlockEntity.this.beamSections.isEmpty())
/* 108 */           BeaconBlockEntity.playSound(BeaconBlockEntity.this.level, BeaconBlockEntity.this.worldPosition, SoundEvents.BEACON_POWER_SELECT); 
/* 110 */         BeaconBlockEntity.this.primaryPower = BeaconBlockEntity.filterEffect(BeaconMenu.decodeEffect(value));
/*     */         break;
/*     */       case 2:
/* 112 */         BeaconBlockEntity.this.secondaryPower = BeaconBlockEntity.filterEffect(BeaconMenu.decodeEffect(value));
/*     */         break;
/*     */     } 
/*     */   }
/*     */   
/*     */   public int getCount() {
/* 118 */     return 3;
/*     */   }
/*     */ }


/* Location:              C:\Users\karol\AppData\Roaming\ModrinthApp\meta\versions\26.2-0.19.5\26.2-0.19.5.jar!\net\minecraft\world\level\block\entity\BeaconBlockEntity$1.class
 * Java compiler version: 25 (69.0)
 * JD-Core Version:       1.1.3
 */