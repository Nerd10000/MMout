/*    */ package net.minecraft.world.effect;
/*    */ 
/*    */ import com.google.common.collect.Sets;
/*    */ import java.util.Set;
/*    */ import java.util.function.ToIntFunction;
/*    */ import net.minecraft.core.BlockPos;
/*    */ import net.minecraft.core.Direction;
/*    */ import net.minecraft.core.particles.ParticleOptions;
/*    */ import net.minecraft.core.particles.ParticleTypes;
/*    */ import net.minecraft.server.level.ServerLevel;
/*    */ import net.minecraft.util.RandomSource;
/*    */ import net.minecraft.world.entity.Entity;
/*    */ import net.minecraft.world.entity.LivingEntity;
/*    */ import net.minecraft.world.level.BlockGetter;
/*    */ import net.minecraft.world.level.block.Blocks;
/*    */ import net.minecraft.world.level.gamerules.GameRules;
/*    */ 
/*    */ class WeavingMobEffect extends MobEffect {
/*    */   private final ToIntFunction<RandomSource> maxCobwebs;
/*    */   
/*    */   protected WeavingMobEffect(MobEffectCategory category, int color, ToIntFunction<RandomSource> maxCobwebs) {
/* 26 */     super(category, color, (ParticleOptions)ParticleTypes.ITEM_COBWEB);
/* 27 */     this.maxCobwebs = maxCobwebs;
/*    */   }
/*    */   
/*    */   public void onMobRemoved(ServerLevel level, LivingEntity mob, int amplifier, Entity.RemovalReason reason) {
/* 32 */     if (reason == Entity.RemovalReason.KILLED && (
/* 33 */       mob instanceof net.minecraft.world.entity.player.Player || ((Boolean)level.getGameRules().get(GameRules.MOB_GRIEFING)).booleanValue()))
/* 34 */       spawnCobwebsRandomlyAround(level, mob.getRandom(), mob.blockPosition()); 
/*    */   }
/*    */   
/*    */   private void spawnCobwebsRandomlyAround(ServerLevel level, RandomSource random, BlockPos pos) {
/* 40 */     Set<BlockPos> positionsToTransform = Sets.newHashSet();
/* 41 */     int cobwebCount = this.maxCobwebs.applyAsInt(random);
/* 42 */     for (BlockPos blockPos : BlockPos.randomInCube(random, 15, pos, 1)) {
/* 43 */       BlockPos below = blockPos.below();
/* 45 */       if (positionsToTransform.contains(blockPos))
/*    */         continue; 
/* 49 */       if (level.getBlockState(blockPos).canBeReplaced() && level.getBlockState(below).isFaceSturdy((BlockGetter)level, below, Direction.UP)) {
/* 50 */         positionsToTransform.add(blockPos.immutable());
/* 51 */         if (positionsToTransform.size() >= cobwebCount)
/*    */           break; 
/*    */       } 
/*    */     } 
/* 57 */     for (BlockPos blockPos : positionsToTransform) {
/* 58 */       level.setBlock(blockPos, Blocks.COBWEB.defaultBlockState(), 3);
/* 59 */       level.levelEvent(3018, blockPos, 0);
/*    */     } 
/*    */   }
/*    */ }


/* Location:              C:\Users\karol\AppData\Roaming\ModrinthApp\meta\versions\26.2-0.19.5\26.2-0.19.5.jar!\net\minecraft\world\effect\WeavingMobEffect.class
 * Java compiler version: 25 (69.0)
 * JD-Core Version:       1.1.3
 */