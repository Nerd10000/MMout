/*    */ package net.minecraft.world.level.block;
/*    */ 
/*    */ import com.mojang.serialization.MapCodec;
/*    */ import java.util.function.Function;
/*    */ import net.minecraft.core.BlockPos;
/*    */ import net.minecraft.world.entity.Entity;
/*    */ import net.minecraft.world.level.Level;
/*    */ import net.minecraft.world.level.block.state.BlockBehaviour;
/*    */ import net.minecraft.world.level.block.state.BlockState;
/*    */ 
/*    */ public class SlimeBlock extends HalfTransparentBlock {
/* 13 */   public static final MapCodec<SlimeBlock> CODEC = simpleCodec(SlimeBlock::new);
/*    */   
/*    */   public MapCodec<SlimeBlock> codec() {
/* 17 */     return CODEC;
/*    */   }
/*    */   
/*    */   public SlimeBlock(BlockBehaviour.Properties properties) {
/* 21 */     super(properties);
/*    */   }
/*    */   
/*    */   public void fallOn(Level level, BlockState state, BlockPos pos, Entity entity, double fallDistance) {
/* 26 */     if (!entity.isSuppressingBounce())
/* 28 */       entity.causeFallDamage(fallDistance, 0.0F, level.damageSources().fall()); 
/*    */   }
/*    */   
/*    */   public void stepOn(Level level, BlockPos pos, BlockState onState, Entity entity) {
/* 34 */     double absDeltaY = Math.abs((entity.getDeltaMovement()).y);
/* 35 */     if (absDeltaY < 0.1D && !entity.isSteppingCarefully()) {
/* 36 */       double scale = 0.4D + absDeltaY * 0.2D;
/* 37 */       entity.setDeltaMovement(entity.getDeltaMovement().multiply(scale, 1.0D, scale));
/*    */     } 
/* 39 */     super.stepOn(level, pos, onState, entity);
/*    */   }
/*    */ }


/* Location:              C:\Users\karol\AppData\Roaming\ModrinthApp\meta\versions\26.2-0.19.5\26.2-0.19.5.jar!\net\minecraft\world\level\block\SlimeBlock.class
 * Java compiler version: 25 (69.0)
 * JD-Core Version:       1.1.3
 */