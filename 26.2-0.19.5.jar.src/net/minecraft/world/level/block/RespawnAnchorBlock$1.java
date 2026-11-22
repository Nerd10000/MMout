/*     */ package net.minecraft.world.level.block;
/*     */ 
/*     */ import java.util.Optional;
/*     */ import net.minecraft.core.BlockPos;
/*     */ import net.minecraft.world.level.BlockGetter;
/*     */ import net.minecraft.world.level.Explosion;
/*     */ import net.minecraft.world.level.ExplosionDamageCalculator;
/*     */ import net.minecraft.world.level.block.state.BlockState;
/*     */ import net.minecraft.world.level.material.FluidState;
/*     */ 
/*     */ class null extends ExplosionDamageCalculator {
/*     */   null(RespawnAnchorBlock this$0) {}
/*     */   
/*     */   public Optional<Float> getBlockExplosionResistance(Explosion explosion, BlockGetter level, BlockPos testPos, BlockState block, FluidState fluid) {
/* 158 */     if (testPos.equals(pos) && inWater)
/* 160 */       return Optional.of(Float.valueOf(Blocks.WATER.getExplosionResistance())); 
/* 162 */     return super.getBlockExplosionResistance(explosion, level, testPos, block, fluid);
/*     */   }
/*     */ }


/* Location:              C:\Users\karol\AppData\Roaming\ModrinthApp\meta\versions\26.2-0.19.5\26.2-0.19.5.jar!\net\minecraft\world\level\block\RespawnAnchorBlock$1.class
 * Java compiler version: 25 (69.0)
 * JD-Core Version:       1.1.3
 */