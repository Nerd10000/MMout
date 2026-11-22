/*     */ package net.minecraft.world.level.storage.loot.entries;
/*     */ 
/*     */ class DummyBuilder extends LootPoolSingletonContainer.Builder<LootPoolSingletonContainer.DummyBuilder> {
/*     */   private final LootPoolSingletonContainer.EntryConstructor constructor;
/*     */   
/*     */   public DummyBuilder(LootPoolSingletonContainer.EntryConstructor constructor) {
/* 121 */     this.constructor = constructor;
/*     */   }
/*     */   
/*     */   protected DummyBuilder getThis() {
/* 126 */     return this;
/*     */   }
/*     */   
/*     */   public LootPoolEntryContainer build() {
/* 131 */     return this.constructor.build(this.weight, this.quality, getConditions(), getFunctions());
/*     */   }
/*     */ }


/* Location:              C:\Users\karol\AppData\Roaming\ModrinthApp\meta\versions\26.2-0.19.5\26.2-0.19.5.jar!\net\minecraft\world\level\storage\loot\entries\LootPoolSingletonContainer$DummyBuilder.class
 * Java compiler version: 25 (69.0)
 * JD-Core Version:       1.1.3
 */