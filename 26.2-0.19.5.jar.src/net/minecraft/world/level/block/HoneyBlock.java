/*     */ package net.minecraft.world.level.block;
/*     */ 
/*     */ import com.mojang.serialization.MapCodec;
/*     */ import java.util.function.Function;
/*     */ import net.minecraft.advancements.triggers.CriteriaTriggers;
/*     */ import net.minecraft.core.BlockPos;
/*     */ import net.minecraft.core.particles.BlockParticleOption;
/*     */ import net.minecraft.core.particles.ParticleOptions;
/*     */ import net.minecraft.core.particles.ParticleTypes;
/*     */ import net.minecraft.server.level.ServerPlayer;
/*     */ import net.minecraft.sounds.SoundEvents;
/*     */ import net.minecraft.util.RandomSource;
/*     */ import net.minecraft.world.entity.Entity;
/*     */ import net.minecraft.world.entity.InsideBlockEffectApplier;
/*     */ import net.minecraft.world.level.BlockGetter;
/*     */ import net.minecraft.world.level.Level;
/*     */ import net.minecraft.world.level.block.state.BlockBehaviour;
/*     */ import net.minecraft.world.level.block.state.BlockState;
/*     */ import net.minecraft.world.phys.Vec3;
/*     */ import net.minecraft.world.phys.shapes.CollisionContext;
/*     */ import net.minecraft.world.phys.shapes.VoxelShape;
/*     */ 
/*     */ public class HoneyBlock extends HalfTransparentBlock {
/*  36 */   public static final MapCodec<HoneyBlock> CODEC = simpleCodec(HoneyBlock::new);
/*     */   
/*     */   private static final double SLIDE_STARTS_WHEN_VERTICAL_SPEED_IS_AT_LEAST = 0.13D;
/*     */   
/*     */   private static final double MIN_FALL_SPEED_TO_BE_CONSIDERED_SLIDING = 0.08D;
/*     */   
/*     */   private static final double THROTTLE_SLIDE_SPEED_TO = 0.05D;
/*     */   
/*     */   private static final int SLIDE_ADVANCEMENT_CHECK_INTERVAL = 20;
/*     */   
/*     */   public MapCodec<HoneyBlock> codec() {
/*  40 */     return CODEC;
/*     */   }
/*     */   
/*  52 */   private static final VoxelShape SHAPE = Block.column(14.0D, 0.0D, 15.0D);
/*     */   
/*     */   public HoneyBlock(BlockBehaviour.Properties properties) {
/*  55 */     super(properties);
/*     */   }
/*     */   
/*     */   private static boolean doesEntityDoHoneyBlockSlideEffects(Entity entity) {
/*  60 */     return (entity instanceof net.minecraft.world.entity.LivingEntity || entity instanceof net.minecraft.world.entity.vehicle.minecart.AbstractMinecart || entity instanceof net.minecraft.world.entity.item.PrimedTnt || entity instanceof net.minecraft.world.entity.vehicle.boat.AbstractBoat);
/*     */   }
/*     */   
/*     */   protected VoxelShape getCollisionShape(BlockState state, BlockGetter level, BlockPos pos, CollisionContext context) {
/*  68 */     return SHAPE;
/*     */   }
/*     */   
/*     */   public void fallOn(Level level, BlockState state, BlockPos pos, Entity entity, double fallDistance) {
/*  73 */     entity.playSound(SoundEvents.HONEY_BLOCK_SLIDE, 1.0F, 1.0F);
/*  75 */     if (!level.isClientSide())
/*  78 */       level.broadcastEntityEvent(entity, (byte)54); 
/*  81 */     if (entity.causeFallDamage(fallDistance, 0.2F, level.damageSources().fall()))
/*  82 */       entity.playSound(this.soundType.getFallSound(), this.soundType.getVolume() * 0.5F, this.soundType.getPitch() * 0.75F); 
/*     */   }
/*     */   
/*     */   protected void entityInside(BlockState state, Level level, BlockPos pos, Entity entity, InsideBlockEffectApplier effectApplier, boolean isPrecise) {
/*  88 */     if (isSlidingDown(pos, entity)) {
/*  89 */       maybeDoSlideAchievement(entity, pos);
/*  90 */       doSlideMovement(entity);
/*  91 */       maybeDoSlideEffects(level, entity);
/*     */     } 
/*  93 */     super.entityInside(state, level, pos, entity, effectApplier, isPrecise);
/*     */   }
/*     */   
/*     */   private static double getOldDeltaY(double deltaY) {
/*  98 */     return deltaY / 0.9800000190734863D + 0.08D;
/*     */   }
/*     */   
/*     */   private static double getNewDeltaY(double deltaY) {
/* 102 */     return (deltaY - 0.08D) * 0.9800000190734863D;
/*     */   }
/*     */   
/*     */   private boolean isSlidingDown(BlockPos pos, Entity entity) {
/* 106 */     if (entity.onGround())
/* 107 */       return false; 
/* 109 */     if (entity.getY() > pos.getY() + 0.9375D - 1.0E-7D)
/* 111 */       return false; 
/* 113 */     if (getOldDeltaY((entity.getDeltaMovement()).y) >= -0.08D)
/* 114 */       return false; 
/* 117 */     double dx = Math.abs(pos.getX() + 0.5D - entity.getX());
/* 118 */     double dz = Math.abs(pos.getZ() + 0.5D - entity.getZ());
/* 120 */     double overlapDistance = 0.4375D + (entity.getBbWidth() / 2.0F);
/* 122 */     return (dx + 1.0E-7D > overlapDistance || dz + 1.0E-7D > overlapDistance);
/*     */   }
/*     */   
/*     */   private void maybeDoSlideAchievement(Entity entity, BlockPos pos) {
/* 126 */     if (entity instanceof ServerPlayer) {
/* 126 */       ServerPlayer serverPlayer = (ServerPlayer)entity;
/* 126 */       if (entity.level().getGameTime() % 20L == 0L)
/* 128 */         CriteriaTriggers.HONEY_BLOCK_SLIDE.trigger(serverPlayer, entity.level().getBlockState(pos)); 
/*     */     } 
/*     */   }
/*     */   
/*     */   private void doSlideMovement(Entity entity) {
/* 133 */     Vec3 deltaMovement = entity.getDeltaMovement();
/* 134 */     if (getOldDeltaY((entity.getDeltaMovement()).y) < -0.13D) {
/* 136 */       double horizontalReductionFactor = -0.05D / getOldDeltaY((entity.getDeltaMovement()).y);
/* 137 */       entity.setDeltaMovement(new Vec3(deltaMovement.x * horizontalReductionFactor, getNewDeltaY(-0.05D), deltaMovement.z * horizontalReductionFactor));
/*     */     } else {
/* 140 */       entity.setDeltaMovement(new Vec3(deltaMovement.x, getNewDeltaY(-0.05D), deltaMovement.z));
/*     */     } 
/* 142 */     entity.resetFallDistance();
/*     */   }
/*     */   
/*     */   private void maybeDoSlideEffects(Level level, Entity entity) {
/* 146 */     if (doesEntityDoHoneyBlockSlideEffects(entity)) {
/* 147 */       RandomSource random = level.getRandom();
/* 148 */       if (random.nextInt(5) == 0)
/* 149 */         entity.playSound(SoundEvents.HONEY_BLOCK_SLIDE, 1.0F, 1.0F); 
/* 152 */       if (!level.isClientSide() && random.nextInt(5) == 0)
/* 153 */         level.broadcastEntityEvent(entity, (byte)53); 
/*     */     } 
/*     */   }
/*     */   
/*     */   public static void showSlideParticles(Entity entity) {
/* 159 */     showParticles(entity, 5);
/*     */   }
/*     */   
/*     */   public static void showJumpParticles(Entity entity) {
/* 163 */     showParticles(entity, 10);
/*     */   }
/*     */   
/*     */   private static void showParticles(Entity entity, int count) {
/* 167 */     if (!entity.level().isClientSide())
/*     */       return; 
/* 171 */     BlockState blockState = Blocks.HONEY_BLOCK.defaultBlockState();
/* 172 */     for (int i = 0; i < count; i++)
/* 174 */       entity.level().addParticle((ParticleOptions)new BlockParticleOption(ParticleTypes.BLOCK, blockState), entity.getX(), entity.getY(), entity.getZ(), 0.0D, 0.0D, 0.0D); 
/*     */   }
/*     */ }


/* Location:              C:\Users\karol\AppData\Roaming\ModrinthApp\meta\versions\26.2-0.19.5\26.2-0.19.5.jar!\net\minecraft\world\level\block\HoneyBlock.class
 * Java compiler version: 25 (69.0)
 * JD-Core Version:       1.1.3
 */