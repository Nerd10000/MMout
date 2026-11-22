/*     */ package net.minecraft.world.entity.ai.goal;
/*     */ 
/*     */ import net.minecraft.core.BlockPos;
/*     */ import net.minecraft.core.SectionPos;
/*     */ import net.minecraft.core.particles.ItemParticleOption;
/*     */ import net.minecraft.core.particles.ParticleOptions;
/*     */ import net.minecraft.core.particles.ParticleTypes;
/*     */ import net.minecraft.server.level.ServerLevel;
/*     */ import net.minecraft.util.RandomSource;
/*     */ import net.minecraft.world.entity.Entity;
/*     */ import net.minecraft.world.entity.Mob;
/*     */ import net.minecraft.world.entity.PathfinderMob;
/*     */ import net.minecraft.world.item.Items;
/*     */ import net.minecraft.world.level.BlockGetter;
/*     */ import net.minecraft.world.level.Level;
/*     */ import net.minecraft.world.level.LevelAccessor;
/*     */ import net.minecraft.world.level.LevelReader;
/*     */ import net.minecraft.world.level.block.Block;
/*     */ import net.minecraft.world.level.chunk.ChunkAccess;
/*     */ import net.minecraft.world.level.chunk.status.ChunkStatus;
/*     */ import net.minecraft.world.level.gamerules.GameRules;
/*     */ import net.minecraft.world.phys.Vec3;
/*     */ 
/*     */ public class RemoveBlockGoal extends MoveToBlockGoal {
/*     */   private final Block blockToRemove;
/*     */   
/*     */   private final Mob removerMob;
/*     */   
/*     */   private int ticksSinceReachedGoal;
/*     */   
/*     */   private static final int WAIT_AFTER_BLOCK_FOUND = 20;
/*     */   
/*     */   public RemoveBlockGoal(Block blockToRemove, PathfinderMob mob, double speedModifier, int verticalSearchRange) {
/*  31 */     super(mob, speedModifier, 24, verticalSearchRange);
/*  32 */     this.blockToRemove = blockToRemove;
/*  33 */     this.removerMob = (Mob)mob;
/*     */   }
/*     */   
/*     */   public boolean canUse() {
/*  38 */     if (!((Boolean)getServerLevel((Entity)this.removerMob).getGameRules().get(GameRules.MOB_GRIEFING)).booleanValue())
/*  39 */       return false; 
/*  42 */     if (this.nextStartTick > 0) {
/*  43 */       this.nextStartTick--;
/*  44 */       return false;
/*     */     } 
/*  47 */     if (findNearestBlock()) {
/*  49 */       this.nextStartTick = reducedTickDelay(20);
/*  50 */       return true;
/*     */     } 
/*  52 */     this.nextStartTick = nextStartTick(this.mob);
/*  53 */     return false;
/*     */   }
/*     */   
/*     */   public void stop() {
/*  59 */     super.stop();
/*  60 */     this.removerMob.fallDistance = 1.0D;
/*     */   }
/*     */   
/*     */   public void start() {
/*  65 */     super.start();
/*  66 */     this.ticksSinceReachedGoal = 0;
/*     */   }
/*     */   
/*     */   public void playDestroyProgressSound(LevelAccessor level, BlockPos pos) {}
/*     */   
/*     */   public void playBreakSound(Level level, BlockPos pos) {}
/*     */   
/*     */   public void tick() {
/*  77 */     super.tick();
/*  78 */     Level level = this.removerMob.level();
/*  79 */     BlockPos mobPos = this.removerMob.blockPosition();
/*  81 */     BlockPos eatPos = getPosWithBlock(mobPos, (BlockGetter)level);
/*  83 */     RandomSource random = this.removerMob.getRandom();
/*  84 */     if (isReachedTarget() && eatPos != null) {
/*  85 */       if (this.ticksSinceReachedGoal > 0) {
/*  86 */         Vec3 movement = this.removerMob.getDeltaMovement();
/*  87 */         this.removerMob.setDeltaMovement(movement.x, 0.3D, movement.z);
/*  89 */         if (level instanceof ServerLevel) {
/*  89 */           ServerLevel serverLevel = (ServerLevel)level;
/*  90 */           serverLevel.sendParticles((ParticleOptions)new ItemParticleOption(ParticleTypes.ITEM, Items.EGG), eatPos
/*     */               
/*  92 */               .getX() + 0.5D, eatPos
/*  93 */               .getY() + 0.7D, eatPos
/*  94 */               .getZ() + 0.5D, 3, (random
/*     */               
/*  96 */               .nextFloat() - 0.5D) * 0.08D, (random
/*  97 */               .nextFloat() - 0.5D) * 0.08D, (random
/*  98 */               .nextFloat() - 0.5D) * 0.08D, 0.15000000596046448D);
/*     */         } 
/*     */       } 
/* 104 */       if (this.ticksSinceReachedGoal % 2 == 0) {
/* 105 */         Vec3 movement = this.removerMob.getDeltaMovement();
/* 106 */         this.removerMob.setDeltaMovement(movement.x, -0.3D, movement.z);
/* 108 */         if (this.ticksSinceReachedGoal % 6 == 0)
/* 109 */           playDestroyProgressSound((LevelAccessor)level, this.blockPos); 
/*     */       } 
/* 113 */       if (this.ticksSinceReachedGoal > 60) {
/* 114 */         level.removeBlock(eatPos, false);
/* 115 */         if (!level.isClientSide()) {
/* 116 */           for (int i = 0; i < 20; i++) {
/* 117 */             double xa = random.nextGaussian() * 0.02D;
/* 118 */             double ya = random.nextGaussian() * 0.02D;
/* 119 */             double za = random.nextGaussian() * 0.02D;
/* 120 */             ((ServerLevel)level).sendParticles((ParticleOptions)ParticleTypes.POOF, eatPos.getX() + 0.5D, eatPos.getY(), eatPos.getZ() + 0.5D, 1, xa, ya, za, 0.15000000596046448D);
/*     */           } 
/* 122 */           playBreakSound(level, eatPos);
/*     */         } 
/*     */       } 
/* 125 */       this.ticksSinceReachedGoal++;
/*     */     } 
/*     */   }
/*     */   
/*     */   private BlockPos getPosWithBlock(BlockPos pos, BlockGetter level) {
/* 130 */     if (level.getBlockState(pos).is(this.blockToRemove))
/* 131 */       return pos; 
/* 133 */     BlockPos[] neighbours = { pos.below(), pos.west(), pos.east(), pos.north(), pos.south(), pos.below().below() };
/* 134 */     for (BlockPos neighborPos : neighbours) {
/* 135 */       if (level.getBlockState(neighborPos).is(this.blockToRemove))
/* 136 */         return neighborPos; 
/*     */     } 
/* 139 */     return null;
/*     */   }
/*     */   
/*     */   protected boolean isValidTarget(LevelReader level, BlockPos pos) {
/* 144 */     ChunkAccess chunk = level.getChunk(SectionPos.blockToSectionCoord(pos.getX()), SectionPos.blockToSectionCoord(pos.getZ()), ChunkStatus.FULL, false);
/* 145 */     if (chunk != null)
/* 146 */       return (chunk.getBlockState(pos).is(this.blockToRemove) && chunk.getBlockState(pos.above()).isAir() && chunk.getBlockState(pos.above(2)).isAir()); 
/* 148 */     return false;
/*     */   }
/*     */ }


/* Location:              C:\Users\karol\AppData\Roaming\ModrinthApp\meta\versions\26.2-0.19.5\26.2-0.19.5.jar!\net\minecraft\world\entity\ai\goal\RemoveBlockGoal.class
 * Java compiler version: 25 (69.0)
 * JD-Core Version:       1.1.3
 */