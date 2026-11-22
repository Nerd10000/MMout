/*     */ package net.minecraft.world.level.biome;
/*     */ 
/*     */ import com.google.common.collect.ImmutableMap;
/*     */ import com.google.common.collect.Maps;
/*     */ import java.util.Map;
/*     */ import net.minecraft.util.Util;
/*     */ import net.minecraft.util.random.WeightedList;
/*     */ import net.minecraft.world.entity.EntityType;
/*     */ import net.minecraft.world.entity.MobCategory;
/*     */ 
/*     */ public class Builder {
/*  97 */   private final Map<MobCategory, WeightedList.Builder<MobSpawnSettings.SpawnerData>> spawners = Util.makeEnumMap(MobCategory.class, c -> WeightedList.builder());
/*     */   
/*  98 */   private final Map<EntityType<?>, MobSpawnSettings.MobSpawnCost> mobSpawnCosts = Maps.newLinkedHashMap();
/*     */   
/*  99 */   private float creatureGenerationProbability = 0.1F;
/*     */   
/*     */   public Builder addSpawn(MobCategory category, int weight, MobSpawnSettings.SpawnerData spawnerData) {
/* 102 */     ((WeightedList.Builder)this.spawners.get(category)).add(spawnerData, weight);
/* 103 */     return this;
/*     */   }
/*     */   
/*     */   public Builder addMobCharge(EntityType<?> type, double charge, double energyBudget) {
/* 127 */     this.mobSpawnCosts.put(type, new MobSpawnSettings.MobSpawnCost(energyBudget, charge));
/* 128 */     return this;
/*     */   }
/*     */   
/*     */   public Builder creatureGenerationProbability(float creatureGenerationProbability) {
/* 132 */     this.creatureGenerationProbability = creatureGenerationProbability;
/* 133 */     return this;
/*     */   }
/*     */   
/*     */   public MobSpawnSettings build() {
/* 137 */     return new MobSpawnSettings(this.creatureGenerationProbability, (Map<MobCategory, WeightedList<MobSpawnSettings.SpawnerData>>)this.spawners
/*     */         
/* 139 */         .entrySet().stream().collect(ImmutableMap.toImmutableMap(Map.Entry::getKey, e -> ((WeightedList.Builder)e.getValue()).build())), 
/* 140 */         (Map<EntityType<?>, MobSpawnSettings.MobSpawnCost>)ImmutableMap.copyOf(this.mobSpawnCosts));
/*     */   }
/*     */ }


/* Location:              C:\Users\karol\AppData\Roaming\ModrinthApp\meta\versions\26.2-0.19.5\26.2-0.19.5.jar!\net\minecraft\world\level\biome\MobSpawnSettings$Builder.class
 * Java compiler version: 25 (69.0)
 * JD-Core Version:       1.1.3
 */