/*    */ package net.minecraft.world.entity.item;
/*    */ 
/*    */ import java.util.Optional;
/*    */ import net.minecraft.core.BlockPos;
/*    */ import net.minecraft.world.level.BlockGetter;
/*    */ import net.minecraft.world.level.Explosion;
/*    */ import net.minecraft.world.level.ExplosionDamageCalculator;
/*    */ import net.minecraft.world.level.block.Blocks;
/*    */ import net.minecraft.world.level.block.state.BlockState;
/*    */ import net.minecraft.world.level.material.FluidState;
/*    */ 
/*    */ class null extends ExplosionDamageCalculator {
/*    */   public boolean shouldBlockExplode(Explosion explosion, BlockGetter level, BlockPos pos, BlockState state, float power) {
/* 50 */     if (state.is(Blocks.NETHER_PORTAL))
/* 51 */       return false; 
/* 53 */     return super.shouldBlockExplode(explosion, level, pos, state, power);
/*    */   }
/*    */   
/*    */   public Optional<Float> getBlockExplosionResistance(Explosion explosion, BlockGetter level, BlockPos pos, BlockState block, FluidState fluid) {
/* 58 */     if (block.is(Blocks.NETHER_PORTAL))
/* 59 */       return Optional.empty(); 
/* 61 */     return super.getBlockExplosionResistance(explosion, level, pos, block, fluid);
/*    */   }
/*    */ }


/* Location:              C:\Users\karol\AppData\Roaming\ModrinthApp\meta\versions\26.2-0.19.5\26.2-0.19.5.jar!\net\minecraft\world\entity\item\PrimedTnt$1.class
 * Java compiler version: 25 (69.0)
 * JD-Core Version:       1.1.3
 */