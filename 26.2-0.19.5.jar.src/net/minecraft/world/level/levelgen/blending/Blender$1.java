/*    */ package net.minecraft.world.level.levelgen.blending;
/*    */ 
/*    */ import it.unimi.dsi.fastutil.longs.Long2ObjectOpenHashMap;
/*    */ import net.minecraft.world.level.biome.BiomeResolver;
/*    */ import net.minecraft.world.level.levelgen.DensityFunction;
/*    */ 
/*    */ class null extends Blender {
/*    */   null(Long2ObjectOpenHashMap<BlendingData> heightAndBiomeBlendingData, Long2ObjectOpenHashMap<BlendingData> densityBlendingData) {
/* 39 */     super(heightAndBiomeBlendingData, densityBlendingData);
/*    */   }
/*    */   
/*    */   public Blender.BlendingOutput blendOffsetAndFactor(int blockX, int blockZ) {
/* 42 */     return new Blender.BlendingOutput(1.0D, 0.0D);
/*    */   }
/*    */   
/*    */   public double blendDensity(DensityFunction.FunctionContext context, double noiseValue) {
/* 47 */     return noiseValue;
/*    */   }
/*    */   
/*    */   public BiomeResolver getBiomeResolver(BiomeResolver biomeResolver) {
/* 52 */     return biomeResolver;
/*    */   }
/*    */ }


/* Location:              C:\Users\karol\AppData\Roaming\ModrinthApp\meta\versions\26.2-0.19.5\26.2-0.19.5.jar!\net\minecraft\world\level\levelgen\blending\Blender$1.class
 * Java compiler version: 25 (69.0)
 * JD-Core Version:       1.1.3
 */