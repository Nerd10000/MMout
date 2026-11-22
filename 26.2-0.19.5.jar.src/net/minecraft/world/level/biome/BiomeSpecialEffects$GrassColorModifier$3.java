/*    */ package net.minecraft.world.level.biome;
/*    */ 
/*    */ enum null {
/*    */   public int modifyColor(double x, double z, int baseColor) {
/* 86 */     double groundValue = Biome.BIOME_INFO_NOISE.getValue(x * 0.0225D, z * 0.0225D, false);
/* 87 */     if (groundValue < -0.1D)
/* 88 */       return -11766212; 
/* 90 */     return -9801671;
/*    */   }
/*    */ }


/* Location:              C:\Users\karol\AppData\Roaming\ModrinthApp\meta\versions\26.2-0.19.5\26.2-0.19.5.jar!\net\minecraft\world\level\biome\BiomeSpecialEffects$GrassColorModifier$3.class
 * Java compiler version: 25 (69.0)
 * JD-Core Version:       1.1.3
 */