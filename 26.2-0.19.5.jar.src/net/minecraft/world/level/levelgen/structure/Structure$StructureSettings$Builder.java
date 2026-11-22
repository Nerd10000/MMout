/*    */ package net.minecraft.world.level.levelgen.structure;
/*    */ 
/*    */ import java.util.Map;
/*    */ import net.minecraft.core.HolderSet;
/*    */ import net.minecraft.world.entity.MobCategory;
/*    */ import net.minecraft.world.level.biome.Biome;
/*    */ import net.minecraft.world.level.levelgen.GenerationStep;
/*    */ 
/*    */ public class Builder {
/*    */   private final HolderSet<Biome> biomes;
/*    */   
/* 72 */   private Map<MobCategory, StructureSpawnOverride> spawnOverrides = Structure.StructureSettings.DEFAULT.spawnOverrides;
/*    */   
/* 73 */   private GenerationStep.Decoration step = Structure.StructureSettings.DEFAULT.step;
/*    */   
/* 74 */   private TerrainAdjustment terrainAdaption = Structure.StructureSettings.DEFAULT.terrainAdaptation;
/*    */   
/*    */   public Builder(HolderSet<Biome> biomes) {
/* 77 */     this.biomes = biomes;
/*    */   }
/*    */   
/*    */   public Builder spawnOverrides(Map<MobCategory, StructureSpawnOverride> spawnOverrides) {
/* 81 */     this.spawnOverrides = spawnOverrides;
/* 82 */     return this;
/*    */   }
/*    */   
/*    */   public Builder generationStep(GenerationStep.Decoration step) {
/* 86 */     this.step = step;
/* 87 */     return this;
/*    */   }
/*    */   
/*    */   public Builder terrainAdapation(TerrainAdjustment terrainAdaption) {
/* 91 */     this.terrainAdaption = terrainAdaption;
/* 92 */     return this;
/*    */   }
/*    */   
/*    */   public Structure.StructureSettings build() {
/* 96 */     return new Structure.StructureSettings(this.biomes, this.spawnOverrides, this.step, this.terrainAdaption);
/*    */   }
/*    */ }


/* Location:              C:\Users\karol\AppData\Roaming\ModrinthApp\meta\versions\26.2-0.19.5\26.2-0.19.5.jar!\net\minecraft\world\level\levelgen\structure\Structure$StructureSettings$Builder.class
 * Java compiler version: 25 (69.0)
 * JD-Core Version:       1.1.3
 */