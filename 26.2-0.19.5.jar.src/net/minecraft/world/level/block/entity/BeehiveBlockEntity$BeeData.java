/*     */ package net.minecraft.world.level.block.entity;
/*     */ 
/*     */ class BeeData {
/*     */   private final BeehiveBlockEntity.Occupant occupant;
/*     */   
/*     */   private int ticksInHive;
/*     */   
/*     */   private BeeData(BeehiveBlockEntity.Occupant occupant) {
/* 347 */     this.occupant = occupant;
/* 348 */     this.ticksInHive = occupant.ticksInHive();
/*     */   }
/*     */   
/*     */   public boolean tick() {
/* 352 */     return (this.ticksInHive++ > this.occupant.minTicksInHive);
/*     */   }
/*     */   
/*     */   public BeehiveBlockEntity.Occupant toOccupant() {
/* 356 */     return new BeehiveBlockEntity.Occupant(this.occupant.entityData, this.ticksInHive, this.occupant.minTicksInHive);
/*     */   }
/*     */   
/*     */   public boolean hasNectar() {
/* 360 */     return this.occupant.entityData.getUnsafe().getBooleanOr("HasNectar", false);
/*     */   }
/*     */ }


/* Location:              C:\Users\karol\AppData\Roaming\ModrinthApp\meta\versions\26.2-0.19.5\26.2-0.19.5.jar!\net\minecraft\world\level\block\entity\BeehiveBlockEntity$BeeData.class
 * Java compiler version: 25 (69.0)
 * JD-Core Version:       1.1.3
 */