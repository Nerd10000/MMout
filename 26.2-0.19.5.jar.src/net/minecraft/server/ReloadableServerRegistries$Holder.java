/*     */ package net.minecraft.server;
/*     */ 
/*     */ import java.util.Optional;
/*     */ import net.minecraft.core.HolderLookup;
/*     */ import net.minecraft.core.registries.Registries;
/*     */ import net.minecraft.resources.ResourceKey;
/*     */ import net.minecraft.world.level.storage.loot.LootTable;
/*     */ 
/*     */ public class Holder {
/*     */   private final HolderLookup.Provider registries;
/*     */   
/*     */   public Holder(HolderLookup.Provider registries) {
/* 110 */     this.registries = registries;
/*     */   }
/*     */   
/*     */   public HolderLookup.Provider lookup() {
/* 114 */     return this.registries;
/*     */   }
/*     */   
/*     */   public LootTable getLootTable(ResourceKey<LootTable> id) {
/* 118 */     return this.registries.lookup(Registries.LOOT_TABLE).flatMap(r -> r.get(id)).map(net.minecraft.core.Holder::value).orElse(LootTable.EMPTY);
/*     */   }
/*     */ }


/* Location:              C:\Users\karol\AppData\Roaming\ModrinthApp\meta\versions\26.2-0.19.5\26.2-0.19.5.jar!\net\minecraft\server\ReloadableServerRegistries$Holder.class
 * Java compiler version: 25 (69.0)
 * JD-Core Version:       1.1.3
 */