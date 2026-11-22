/*    */ package net.minecraft.world.level.biome;
/*    */ 
/*    */ import net.minecraft.util.ARGB;
/*    */ 
/*    */ enum null {
/*    */   public int modifyColor(double x, double z, int baseColor) {
/* 80 */     return ARGB.opaque((baseColor & 0xFEFEFE) + 2634762 >> 1);
/*    */   }
/*    */ }


/* Location:              C:\Users\karol\AppData\Roaming\ModrinthApp\meta\versions\26.2-0.19.5\26.2-0.19.5.jar!\net\minecraft\world\level\biome\BiomeSpecialEffects$GrassColorModifier$2.class
 * Java compiler version: 25 (69.0)
 * JD-Core Version:       1.1.3
 */