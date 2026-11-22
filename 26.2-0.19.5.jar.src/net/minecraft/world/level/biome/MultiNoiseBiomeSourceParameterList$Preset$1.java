/*    */ package net.minecraft.world.level.biome;
/*    */ 
/*    */ import com.mojang.datafixers.util.Pair;
/*    */ import java.util.List;
/*    */ import java.util.function.Function;
/*    */ import net.minecraft.resources.ResourceKey;
/*    */ 
/*    */ class null implements MultiNoiseBiomeSourceParameterList.Preset.SourceProvider {
/*    */   public <T> Climate.ParameterList<T> apply(Function<ResourceKey<Biome>, T> lookup) {
/* 62 */     return new Climate.ParameterList<>(List.of(
/* 63 */           Pair.of(Climate.parameters(0.0F, 0.0F, 0.0F, 0.0F, 0.0F, 0.0F, 0.0F), lookup.apply(Biomes.NETHER_WASTES)), 
/* 64 */           Pair.of(Climate.parameters(0.0F, -0.5F, 0.0F, 0.0F, 0.0F, 0.0F, 0.0F), lookup.apply(Biomes.SOUL_SAND_VALLEY)), 
/* 65 */           Pair.of(Climate.parameters(0.4F, 0.0F, 0.0F, 0.0F, 0.0F, 0.0F, 0.0F), lookup.apply(Biomes.CRIMSON_FOREST)), 
/* 66 */           Pair.of(Climate.parameters(0.0F, 0.5F, 0.0F, 0.0F, 0.0F, 0.0F, 0.375F), lookup.apply(Biomes.WARPED_FOREST)), 
/* 67 */           Pair.of(Climate.parameters(-0.5F, 0.0F, 0.0F, 0.0F, 0.0F, 0.0F, 0.175F), lookup.apply(Biomes.BASALT_DELTAS))));
/*    */   }
/*    */ }


/* Location:              C:\Users\karol\AppData\Roaming\ModrinthApp\meta\versions\26.2-0.19.5\26.2-0.19.5.jar!\net\minecraft\world\level\biome\MultiNoiseBiomeSourceParameterList$Preset$1.class
 * Java compiler version: 25 (69.0)
 * JD-Core Version:       1.1.3
 */