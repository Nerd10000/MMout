/*    */ package net.minecraft.world.level.levelgen.structure;
/*    */ 
/*    */ import com.mojang.datafixers.kinds.App;
/*    */ import com.mojang.datafixers.kinds.Applicative;
/*    */ import com.mojang.datafixers.util.Function4;
/*    */ import com.mojang.serialization.Codec;
/*    */ import com.mojang.serialization.MapCodec;
/*    */ import com.mojang.serialization.codecs.RecordCodecBuilder;
/*    */ import java.util.Map;
/*    */ import net.minecraft.core.HolderSet;
/*    */ import net.minecraft.core.RegistryCodecs;
/*    */ import net.minecraft.core.registries.Registries;
/*    */ import net.minecraft.util.StringRepresentable;
/*    */ import net.minecraft.world.entity.MobCategory;
/*    */ import net.minecraft.world.level.biome.Biome;
/*    */ import net.minecraft.world.level.levelgen.GenerationStep;
/*    */ 
/*    */ public final class StructureSettings extends Record {
/*    */   private final HolderSet<Biome> biomes;
/*    */   
/*    */   private final Map<MobCategory, StructureSpawnOverride> spawnOverrides;
/*    */   
/*    */   private final GenerationStep.Decoration step;
/*    */   
/*    */   private final TerrainAdjustment terrainAdaptation;
/*    */   
/*    */   public StructureSettings(HolderSet<Biome> biomes, Map<MobCategory, StructureSpawnOverride> spawnOverrides, GenerationStep.Decoration step, TerrainAdjustment terrainAdaptation) {
/* 51 */     this.biomes = biomes;
/* 51 */     this.spawnOverrides = spawnOverrides;
/* 51 */     this.step = step;
/* 51 */     this.terrainAdaptation = terrainAdaptation;
/*    */   }
/*    */   
/*    */   public final String toString() {
/*    */     // Byte code:
/*    */     //   0: aload_0
/*    */     //   1: <illegal opcode> toString : (Lnet/minecraft/world/level/levelgen/structure/Structure$StructureSettings;)Ljava/lang/String;
/*    */     //   6: areturn
/*    */     // Line number table:
/*    */     //   Java source line number -> byte code offset
/*    */     //   #51	-> 0
/*    */     // Local variable table:
/*    */     //   start	length	slot	name	descriptor
/*    */     //   0	7	0	this	Lnet/minecraft/world/level/levelgen/structure/Structure$StructureSettings;
/*    */   }
/*    */   
/*    */   public final int hashCode() {
/*    */     // Byte code:
/*    */     //   0: aload_0
/*    */     //   1: <illegal opcode> hashCode : (Lnet/minecraft/world/level/levelgen/structure/Structure$StructureSettings;)I
/*    */     //   6: ireturn
/*    */     // Line number table:
/*    */     //   Java source line number -> byte code offset
/*    */     //   #51	-> 0
/*    */     // Local variable table:
/*    */     //   start	length	slot	name	descriptor
/*    */     //   0	7	0	this	Lnet/minecraft/world/level/levelgen/structure/Structure$StructureSettings;
/*    */   }
/*    */   
/*    */   public final boolean equals(Object o) {
/*    */     // Byte code:
/*    */     //   0: aload_0
/*    */     //   1: aload_1
/*    */     //   2: <illegal opcode> equals : (Lnet/minecraft/world/level/levelgen/structure/Structure$StructureSettings;Ljava/lang/Object;)Z
/*    */     //   7: ireturn
/*    */     // Line number table:
/*    */     //   Java source line number -> byte code offset
/*    */     //   #51	-> 0
/*    */     // Local variable table:
/*    */     //   start	length	slot	name	descriptor
/*    */     //   0	8	0	this	Lnet/minecraft/world/level/levelgen/structure/Structure$StructureSettings;
/*    */     //   0	8	1	o	Ljava/lang/Object;
/*    */   }
/*    */   
/*    */   public HolderSet<Biome> biomes() {
/* 51 */     return this.biomes;
/*    */   }
/*    */   
/*    */   public Map<MobCategory, StructureSpawnOverride> spawnOverrides() {
/* 51 */     return this.spawnOverrides;
/*    */   }
/*    */   
/*    */   public GenerationStep.Decoration step() {
/* 51 */     return this.step;
/*    */   }
/*    */   
/*    */   public TerrainAdjustment terrainAdaptation() {
/* 51 */     return this.terrainAdaptation;
/*    */   }
/*    */   
/* 57 */   private static final StructureSettings DEFAULT = new StructureSettings(HolderSet.empty(), Map.of(), GenerationStep.Decoration.SURFACE_STRUCTURES, TerrainAdjustment.NONE);
/*    */   
/*    */   public static final MapCodec<StructureSettings> CODEC;
/*    */   
/*    */   static {
/* 58 */     CODEC = RecordCodecBuilder.mapCodec(i -> i.group((App)RegistryCodecs.homogeneousList(Registries.BIOME).fieldOf("biomes").forGetter(StructureSettings::biomes), (App)Codec.simpleMap(MobCategory.CODEC, StructureSpawnOverride.CODEC, StringRepresentable.keys((StringRepresentable[])MobCategory.values())).fieldOf("spawn_overrides").forGetter(StructureSettings::spawnOverrides), (App)GenerationStep.Decoration.CODEC.fieldOf("step").forGetter(StructureSettings::step), (App)TerrainAdjustment.CODEC.optionalFieldOf("terrain_adaptation", DEFAULT.terrainAdaptation).forGetter(StructureSettings::terrainAdaptation)).apply((Applicative)i, StructureSettings::new));
/*    */   }
/*    */   
/*    */   public StructureSettings(HolderSet<Biome> biomes) {
/* 67 */     this(biomes, DEFAULT.spawnOverrides, DEFAULT.step, DEFAULT.terrainAdaptation);
/*    */   }
/*    */   
/*    */   public static class Builder {
/*    */     private final HolderSet<Biome> biomes;
/*    */     
/* 72 */     private Map<MobCategory, StructureSpawnOverride> spawnOverrides = Structure.StructureSettings.DEFAULT.spawnOverrides;
/*    */     
/* 73 */     private GenerationStep.Decoration step = Structure.StructureSettings.DEFAULT.step;
/*    */     
/* 74 */     private TerrainAdjustment terrainAdaption = Structure.StructureSettings.DEFAULT.terrainAdaptation;
/*    */     
/*    */     public Builder(HolderSet<Biome> biomes) {
/* 77 */       this.biomes = biomes;
/*    */     }
/*    */     
/*    */     public Builder spawnOverrides(Map<MobCategory, StructureSpawnOverride> spawnOverrides) {
/* 81 */       this.spawnOverrides = spawnOverrides;
/* 82 */       return this;
/*    */     }
/*    */     
/*    */     public Builder generationStep(GenerationStep.Decoration step) {
/* 86 */       this.step = step;
/* 87 */       return this;
/*    */     }
/*    */     
/*    */     public Builder terrainAdapation(TerrainAdjustment terrainAdaption) {
/* 91 */       this.terrainAdaption = terrainAdaption;
/* 92 */       return this;
/*    */     }
/*    */     
/*    */     public Structure.StructureSettings build() {
/* 96 */       return new Structure.StructureSettings(this.biomes, this.spawnOverrides, this.step, this.terrainAdaption);
/*    */     }
/*    */   }
/*    */ }


/* Location:              C:\Users\karol\AppData\Roaming\ModrinthApp\meta\versions\26.2-0.19.5\26.2-0.19.5.jar!\net\minecraft\world\level\levelgen\structure\Structure$StructureSettings.class
 * Java compiler version: 25 (69.0)
 * JD-Core Version:       1.1.3
 */