/*    */ package net.minecraft.world.level.biome;
/*    */ 
/*    */ import com.mojang.serialization.MapCodec;
/*    */ import net.minecraft.core.Registry;
/*    */ 
/*    */ public class BiomeSources {
/*    */   public static MapCodec<? extends BiomeSource> bootstrap(Registry<MapCodec<? extends BiomeSource>> registry) {
/*  8 */     Registry.register(registry, "fixed", FixedBiomeSource.CODEC);
/*  9 */     Registry.register(registry, "multi_noise", MultiNoiseBiomeSource.CODEC);
/* 10 */     Registry.register(registry, "checkerboard", CheckerboardColumnBiomeSource.CODEC);
/* 11 */     return (MapCodec<? extends BiomeSource>)Registry.register(registry, "the_end", TheEndBiomeSource.CODEC);
/*    */   }
/*    */ }


/* Location:              C:\Users\karol\AppData\Roaming\ModrinthApp\meta\versions\26.2-0.19.5\26.2-0.19.5.jar!\net\minecraft\world\level\biome\BiomeSources.class
 * Java compiler version: 25 (69.0)
 * JD-Core Version:       1.1.3
 */