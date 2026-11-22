/*    */ package net.minecraft.world.level;
/*    */ 
/*    */ import java.util.Optional;
/*    */ import net.minecraft.core.BlockPos;
/*    */ import net.minecraft.world.entity.Entity;
/*    */ import net.minecraft.world.level.block.state.BlockState;
/*    */ import net.minecraft.world.level.material.FluidState;
/*    */ 
/*    */ public class EntityBasedExplosionDamageCalculator extends ExplosionDamageCalculator {
/*    */   private final Entity source;
/*    */   
/*    */   public EntityBasedExplosionDamageCalculator(Entity source) {
/* 14 */     this.source = source;
/*    */   }
/*    */   
/*    */   public Optional<Float> getBlockExplosionResistance(Explosion explosion, BlockGetter level, BlockPos pos, BlockState block, FluidState fluid) {
/* 19 */     return super.getBlockExplosionResistance(explosion, level, pos, block, fluid).map(resistance -> Float.valueOf(this.source.getBlockExplosionResistance(explosion, level, pos, block, fluid, resistance.floatValue())));
/*    */   }
/*    */   
/*    */   public boolean shouldBlockExplode(Explosion explosion, BlockGetter level, BlockPos pos, BlockState state, float power) {
/* 24 */     return this.source.shouldBlockExplode(explosion, level, pos, state, power);
/*    */   }
/*    */ }


/* Location:              C:\Users\karol\AppData\Roaming\ModrinthApp\meta\versions\26.2-0.19.5\26.2-0.19.5.jar!\net\minecraft\world\level\EntityBasedExplosionDamageCalculator.class
 * Java compiler version: 25 (69.0)
 * JD-Core Version:       1.1.3
 */