/*     */ package net.minecraft.world.entity.raid;
/*     */ 
/*     */ import net.minecraft.world.entity.EntityType;
/*     */ import net.minecraft.world.entity.EntityTypes;
/*     */ 
/*     */ enum RaiderType {
/*  92 */   VINDICATOR(EntityTypes.VINDICATOR, new int[] { 0, 0, 2, 0, 1, 4, 2, 5 }),
/*  93 */   EVOKER(EntityTypes.EVOKER, new int[] { 0, 0, 0, 0, 0, 1, 1, 2 }),
/*  95 */   PILLAGER(EntityTypes.PILLAGER, new int[] { 0, 4, 3, 3, 4, 4, 4, 2 }),
/*  96 */   WITCH(EntityTypes.WITCH, new int[] { 0, 0, 0, 0, 3, 0, 0, 1 }),
/*  97 */   RAVAGER(EntityTypes.RAVAGER, new int[] { 0, 0, 0, 1, 0, 1, 0, 2 });
/*     */   
/*     */   private static final RaiderType[] VALUES;
/*     */   
/*     */   private final EntityType<? extends Raider> entityType;
/*     */   
/*     */   private final int[] spawnsPerWaveBeforeBonus;
/*     */   
/*     */   static {
/* 100 */     VALUES = values();
/*     */   }
/*     */   
/*     */   RaiderType(EntityType<? extends Raider> entityType, int[] spawnsPerWaveBeforeBonus) {
/* 106 */     this.entityType = entityType;
/* 107 */     this.spawnsPerWaveBeforeBonus = spawnsPerWaveBeforeBonus;
/*     */   }
/*     */ }


/* Location:              C:\Users\karol\AppData\Roaming\ModrinthApp\meta\versions\26.2-0.19.5\26.2-0.19.5.jar!\net\minecraft\world\entity\raid\Raid$RaiderType.class
 * Java compiler version: 25 (69.0)
 * JD-Core Version:       1.1.3
 */