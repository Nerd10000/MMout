/*    */ package net.minecraft.world.level.chunk;
/*    */ 
/*    */ import com.mojang.serialization.MapCodec;
/*    */ import net.minecraft.core.Registry;
/*    */ import net.minecraft.world.level.levelgen.DebugLevelSource;
/*    */ import net.minecraft.world.level.levelgen.FlatLevelSource;
/*    */ import net.minecraft.world.level.levelgen.NoiseBasedChunkGenerator;
/*    */ 
/*    */ public class ChunkGenerators {
/*    */   public static MapCodec<? extends ChunkGenerator> bootstrap(Registry<MapCodec<? extends ChunkGenerator>> registry) {
/* 11 */     Registry.register(registry, "noise", NoiseBasedChunkGenerator.CODEC);
/* 12 */     Registry.register(registry, "flat", FlatLevelSource.CODEC);
/* 13 */     return (MapCodec<? extends ChunkGenerator>)Registry.register(registry, "debug", DebugLevelSource.CODEC);
/*    */   }
/*    */ }


/* Location:              C:\Users\karol\AppData\Roaming\ModrinthApp\meta\versions\26.2-0.19.5\26.2-0.19.5.jar!\net\minecraft\world\level\chunk\ChunkGenerators.class
 * Java compiler version: 25 (69.0)
 * JD-Core Version:       1.1.3
 */