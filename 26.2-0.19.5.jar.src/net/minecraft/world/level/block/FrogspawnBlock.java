/*     */ package net.minecraft.world.level.block;
/*     */ 
/*     */ import com.google.common.annotations.VisibleForTesting;
/*     */ import com.mojang.serialization.MapCodec;
/*     */ import java.util.function.Function;
/*     */ import net.minecraft.core.BlockPos;
/*     */ import net.minecraft.core.Direction;
/*     */ import net.minecraft.server.level.ServerLevel;
/*     */ import net.minecraft.sounds.SoundEvents;
/*     */ import net.minecraft.sounds.SoundSource;
/*     */ import net.minecraft.tags.BlockTags;
/*     */ import net.minecraft.tags.FluidTags;
/*     */ import net.minecraft.util.Mth;
/*     */ import net.minecraft.util.RandomSource;
/*     */ import net.minecraft.world.entity.Entity;
/*     */ import net.minecraft.world.entity.EntitySpawnReason;
/*     */ import net.minecraft.world.entity.EntityTypes;
/*     */ import net.minecraft.world.entity.InsideBlockEffectApplier;
/*     */ import net.minecraft.world.entity.animal.frog.Tadpole;
/*     */ import net.minecraft.world.level.BlockGetter;
/*     */ import net.minecraft.world.level.Level;
/*     */ import net.minecraft.world.level.LevelReader;
/*     */ import net.minecraft.world.level.ScheduledTickAccess;
/*     */ import net.minecraft.world.level.block.state.BlockBehaviour;
/*     */ import net.minecraft.world.level.block.state.BlockState;
/*     */ import net.minecraft.world.level.material.FluidState;
/*     */ import net.minecraft.world.level.material.Fluids;
/*     */ import net.minecraft.world.phys.shapes.CollisionContext;
/*     */ import net.minecraft.world.phys.shapes.VoxelShape;
/*     */ 
/*     */ public class FrogspawnBlock extends Block {
/*  31 */   public static final MapCodec<FrogspawnBlock> CODEC = simpleCodec(FrogspawnBlock::new);
/*     */   
/*     */   private static final int MIN_TADPOLES_SPAWN = 2;
/*     */   
/*     */   private static final int MAX_TADPOLES_SPAWN = 5;
/*     */   
/*     */   private static final int DEFAULT_MIN_HATCH_TICK_DELAY = 3600;
/*     */   
/*     */   private static final int DEFAULT_MAX_HATCH_TICK_DELAY = 12000;
/*     */   
/*     */   public MapCodec<FrogspawnBlock> codec() {
/*  35 */     return CODEC;
/*     */   }
/*     */   
/*  43 */   private static final VoxelShape SHAPE = Block.column(16.0D, 0.0D, 1.5D);
/*     */   
/*  45 */   private static int minHatchTickDelay = 3600;
/*     */   
/*  46 */   private static int maxHatchTickDelay = 12000;
/*     */   
/*     */   public FrogspawnBlock(BlockBehaviour.Properties properties) {
/*  49 */     super(properties);
/*     */   }
/*     */   
/*     */   protected VoxelShape getShape(BlockState state, BlockGetter level, BlockPos pos, CollisionContext context) {
/*  54 */     return SHAPE;
/*     */   }
/*     */   
/*     */   protected boolean canSurvive(BlockState state, LevelReader level, BlockPos pos) {
/*  59 */     return mayPlaceOn((BlockGetter)level, pos.below());
/*     */   }
/*     */   
/*     */   protected void onPlace(BlockState state, Level level, BlockPos pos, BlockState oldState, boolean movedByPiston) {
/*  64 */     level.scheduleTick(pos, this, getFrogspawnHatchDelay(level.getRandom()));
/*     */   }
/*     */   
/*     */   private static int getFrogspawnHatchDelay(RandomSource random) {
/*  68 */     return random.nextInt(minHatchTickDelay, maxHatchTickDelay);
/*     */   }
/*     */   
/*     */   protected BlockState updateShape(BlockState state, LevelReader level, ScheduledTickAccess ticks, BlockPos pos, Direction directionToNeighbour, BlockPos neighbourPos, BlockState neighbourState, RandomSource random) {
/*  73 */     if (!canSurvive(state, level, pos))
/*  74 */       return Blocks.AIR.defaultBlockState(); 
/*  76 */     return super.updateShape(state, level, ticks, pos, directionToNeighbour, neighbourPos, neighbourState, random);
/*     */   }
/*     */   
/*     */   protected void tick(BlockState state, ServerLevel level, BlockPos pos, RandomSource random) {
/*  81 */     if (!canSurvive(state, (LevelReader)level, pos)) {
/*  82 */       destroyBlock((Level)level, pos);
/*     */       return;
/*     */     } 
/*  85 */     hatchFrogspawn(level, pos, random);
/*     */   }
/*     */   
/*     */   protected void entityInside(BlockState state, Level level, BlockPos pos, Entity entity, InsideBlockEffectApplier effectApplier, boolean isPrecise) {
/*  90 */     if (entity.is(EntityTypes.FALLING_BLOCK))
/*  91 */       destroyBlock(level, pos); 
/*     */   }
/*     */   
/*     */   private static boolean mayPlaceOn(BlockGetter level, BlockPos pos) {
/*  96 */     FluidState fluidState = level.getFluidState(pos);
/*  97 */     FluidState fluidAbove = level.getFluidState(pos.above());
/*  98 */     return ((fluidState.is(FluidTags.SUPPORTS_FROGSPAWN) || level.getBlockState(pos).is(BlockTags.SUPPORTS_FROGSPAWN)) && fluidAbove.is(Fluids.EMPTY));
/*     */   }
/*     */   
/*     */   private void hatchFrogspawn(ServerLevel level, BlockPos pos, RandomSource random) {
/* 102 */     destroyBlock((Level)level, pos);
/* 103 */     level.playSound(null, pos, SoundEvents.FROGSPAWN_HATCH, SoundSource.BLOCKS, 1.0F, 1.0F);
/* 104 */     spawnTadpoles(level, pos, random);
/*     */   }
/*     */   
/*     */   private void destroyBlock(Level level, BlockPos pos) {
/* 108 */     level.destroyBlock(pos, false);
/*     */   }
/*     */   
/*     */   private void spawnTadpoles(ServerLevel level, BlockPos pos, RandomSource random) {
/* 112 */     int tadpoleAmount = random.nextInt(2, 6);
/* 113 */     for (int i = 1; i <= tadpoleAmount; i++) {
/* 114 */       Tadpole tadpole = (Tadpole)EntityTypes.TADPOLE.create((Level)level, EntitySpawnReason.BREEDING);
/* 115 */       if (tadpole != null) {
/* 116 */         double xPos = pos.getX() + getRandomTadpolePositionOffset(random);
/* 117 */         double zPos = pos.getZ() + getRandomTadpolePositionOffset(random);
/* 118 */         int yRot = random.nextInt(1, 361);
/* 119 */         tadpole.snapTo(xPos, pos.getY() - 0.5D, zPos, yRot, 0.0F);
/* 120 */         tadpole.setPersistenceRequired();
/* 121 */         level.addFreshEntity((Entity)tadpole);
/*     */       } 
/*     */     } 
/*     */   }
/*     */   
/*     */   private double getRandomTadpolePositionOffset(RandomSource random) {
/* 127 */     double tadpoleHitboxCenter = 0.20000000298023224D;
/* 128 */     return Mth.clamp(random.nextDouble(), 0.20000000298023224D, 0.7999999970197678D);
/*     */   }
/*     */   
/*     */   @VisibleForTesting
/*     */   public static void setHatchDelay(int minDelay, int maxDelay) {
/* 133 */     minHatchTickDelay = minDelay;
/* 134 */     maxHatchTickDelay = maxDelay;
/*     */   }
/*     */   
/*     */   @VisibleForTesting
/*     */   public static void setDefaultHatchDelay() {
/* 139 */     minHatchTickDelay = 3600;
/* 140 */     maxHatchTickDelay = 12000;
/*     */   }
/*     */ }


/* Location:              C:\Users\karol\AppData\Roaming\ModrinthApp\meta\versions\26.2-0.19.5\26.2-0.19.5.jar!\net\minecraft\world\level\block\FrogspawnBlock.class
 * Java compiler version: 25 (69.0)
 * JD-Core Version:       1.1.3
 */