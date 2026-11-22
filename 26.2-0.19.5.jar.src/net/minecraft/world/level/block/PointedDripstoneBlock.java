/*     */ package net.minecraft.world.level.block;
/*     */ 
/*     */ import com.google.common.annotations.VisibleForTesting;
/*     */ import com.mojang.datafixers.kinds.App;
/*     */ import com.mojang.datafixers.kinds.Applicative;
/*     */ import com.mojang.serialization.MapCodec;
/*     */ import com.mojang.serialization.codecs.RecordCodecBuilder;
/*     */ import java.util.Optional;
/*     */ import java.util.function.BiFunction;
/*     */ import java.util.function.BiPredicate;
/*     */ import java.util.function.Predicate;
/*     */ import net.minecraft.core.BlockPos;
/*     */ import net.minecraft.core.Direction;
/*     */ import net.minecraft.core.Holder;
/*     */ import net.minecraft.core.particles.ParticleOptions;
/*     */ import net.minecraft.core.particles.ParticleTypes;
/*     */ import net.minecraft.server.level.ServerLevel;
/*     */ import net.minecraft.tags.FluidTags;
/*     */ import net.minecraft.util.RandomSource;
/*     */ import net.minecraft.world.attribute.EnvironmentAttributes;
/*     */ import net.minecraft.world.entity.Entity;
/*     */ import net.minecraft.world.level.BlockGetter;
/*     */ import net.minecraft.world.level.Level;
/*     */ import net.minecraft.world.level.LevelAccessor;
/*     */ import net.minecraft.world.level.LevelReader;
/*     */ import net.minecraft.world.level.block.state.BlockBehaviour;
/*     */ import net.minecraft.world.level.block.state.BlockState;
/*     */ import net.minecraft.world.level.block.state.properties.Property;
/*     */ import net.minecraft.world.level.block.state.properties.SpeleothemThickness;
/*     */ import net.minecraft.world.level.gameevent.GameEvent;
/*     */ import net.minecraft.world.level.material.FlowingFluid;
/*     */ import net.minecraft.world.level.material.Fluid;
/*     */ import net.minecraft.world.level.material.FluidState;
/*     */ import net.minecraft.world.level.material.Fluids;
/*     */ import net.minecraft.world.phys.Vec3;
/*     */ import net.minecraft.world.phys.shapes.BooleanOp;
/*     */ import net.minecraft.world.phys.shapes.Shapes;
/*     */ import net.minecraft.world.phys.shapes.VoxelShape;
/*     */ 
/*     */ public class PointedDripstoneBlock extends SpeleothemBlock {
/*     */   public static final MapCodec<PointedDripstoneBlock> CODEC;
/*     */   
/*     */   private static final int MAX_SEARCH_LENGTH_WHEN_CHECKING_DRIP_TYPE = 11;
/*     */   
/*     */   private static final float DRIP_PROBABILITY_PER_ANIMATE_TICK = 0.02F;
/*     */   
/*     */   private static final float DRIP_PROBABILITY_PER_ANIMATE_TICK_IF_UNDER_LIQUID_SOURCE = 0.12F;
/*     */   
/*     */   private static final int MAX_SEARCH_LENGTH_BETWEEN_STALACTITE_TIP_AND_CAULDRON = 11;
/*     */   
/*     */   private static final float WATER_TRANSFER_PROBABILITY_PER_RANDOM_TICK = 0.17578125F;
/*     */   
/*     */   private static final float LAVA_TRANSFER_PROBABILITY_PER_RANDOM_TICK = 0.05859375F;
/*     */   
/*     */   private static final float STALAGMITE_FALL_DISTANCE_OFFSET = 2.5F;
/*     */   
/*     */   private static final int STALAGMITE_FALL_DAMAGE_MODIFIER = 2;
/*     */   
/*     */   static {
/*  36 */     CODEC = RecordCodecBuilder.mapCodec(i -> i.group((App)BlockState.CODEC.fieldOf("block_to_grow_on").forGetter(()), (App)propertiesCodec()).apply((Applicative)i, PointedDripstoneBlock::new));
/*     */   }
/*     */   
/*     */   public MapCodec<PointedDripstoneBlock> codec() {
/*  43 */     return CODEC;
/*     */   }
/*     */   
/*  61 */   private static final double STALACTITE_DRIP_START_PIXEL = SHAPE_TIP_DOWN.min(Direction.Axis.Y);
/*     */   
/*  63 */   private static final VoxelShape REQUIRED_SPACE_TO_DRIP_THROUGH_NON_SOLID_BLOCK = Block.column(4.0D, 0.0D, 16.0D);
/*     */   
/*     */   public PointedDripstoneBlock(BlockState blockToGrowOn, BlockBehaviour.Properties properties) {
/*  66 */     super(blockToGrowOn, properties);
/*     */   }
/*     */   
/*     */   protected int getStalactiteLandingSound() {
/*  71 */     return 1045;
/*     */   }
/*     */   
/*     */   public void fallOn(Level level, BlockState state, BlockPos pos, Entity entity, double fallDistance) {
/*  84 */     if (state.getValue((Property)TIP_DIRECTION) == Direction.UP && state.getValue((Property)THICKNESS) == SpeleothemThickness.TIP) {
/*  85 */       entity.causeFallDamage(fallDistance + 2.5D, 2.0F, level.damageSources().stalagmite());
/*     */     } else {
/*  87 */       super.fallOn(level, state, pos, entity, fallDistance);
/*     */     } 
/*     */   }
/*     */   
/*     */   public void animateTick(BlockState state, Level level, BlockPos pos, RandomSource random) {
/*  93 */     if (!isFreeHangingStalactite(state))
/*     */       return; 
/*  98 */     float randomValue = random.nextFloat();
/*  99 */     if (randomValue > 0.12F)
/*     */       return; 
/* 103 */     getFluidAboveStalactite(level, pos, state)
/*     */       
/* 105 */       .filter(fluidAbove -> (randomValue < 0.02F || canFillCauldron(fluidAbove.fluid)))
/* 106 */       .ifPresent(fluidAbove -> spawnDripParticle(level, pos, state, fluidAbove.fluid, fluidAbove.pos));
/*     */   }
/*     */   
/*     */   protected void randomTick(BlockState state, ServerLevel level, BlockPos pos, RandomSource random) {
/* 111 */     maybeTransferFluid(state, level, pos, random.nextFloat());
/* 112 */     super.randomTick(state, level, pos, random);
/*     */   }
/*     */   
/*     */   @VisibleForTesting
/*     */   public static void maybeTransferFluid(BlockState state, ServerLevel level, BlockPos pos, float randomValue) {
/*     */     float transferProbability;
/* 117 */     if (randomValue > 0.17578125F && randomValue > 0.05859375F)
/*     */       return; 
/* 121 */     if (!isStalactiteStartPos(state, (LevelReader)level, pos))
/*     */       return; 
/* 125 */     Optional<FluidInfo> fluidInfo = getFluidAboveStalactite((Level)level, pos, state);
/* 126 */     if (fluidInfo.isEmpty())
/*     */       return; 
/* 129 */     Fluid fluid = ((FluidInfo)fluidInfo.get()).fluid;
/* 132 */     if (fluid == Fluids.WATER) {
/* 133 */       transferProbability = 0.17578125F;
/* 134 */     } else if (fluid == Fluids.LAVA) {
/* 135 */       transferProbability = 0.05859375F;
/*     */     } else {
/*     */       return;
/*     */     } 
/* 139 */     if (randomValue >= transferProbability)
/*     */       return; 
/* 143 */     BlockPos stalactiteTipPos = findTip(state, (LevelAccessor)level, pos, 11, false);
/* 144 */     if (stalactiteTipPos == null)
/*     */       return; 
/* 148 */     if (((FluidInfo)fluidInfo.get()).sourceState.is(Blocks.MUD) && fluid == Fluids.WATER) {
/* 149 */       BlockState newState = Blocks.CLAY.defaultBlockState();
/* 150 */       level.setBlockAndUpdate(((FluidInfo)fluidInfo.get()).pos, newState);
/* 151 */       Block.pushEntitiesUp(((FluidInfo)fluidInfo.get()).sourceState, newState, (LevelAccessor)level, ((FluidInfo)fluidInfo.get()).pos);
/* 152 */       level.gameEvent((Holder)GameEvent.BLOCK_CHANGE, ((FluidInfo)fluidInfo.get()).pos, GameEvent.Context.of(newState));
/* 153 */       level.levelEvent(1504, stalactiteTipPos, 0);
/*     */       return;
/*     */     } 
/* 157 */     BlockPos cauldronPos = findFillableCauldronBelowStalactiteTip((Level)level, stalactiteTipPos, fluid);
/* 158 */     if (cauldronPos == null)
/*     */       return; 
/* 163 */     level.levelEvent(1504, stalactiteTipPos, 0);
/* 166 */     int fallDistance = stalactiteTipPos.getY() - cauldronPos.getY();
/* 167 */     int delay = 50 + fallDistance;
/* 168 */     BlockState cauldronState = level.getBlockState(cauldronPos);
/* 169 */     level.scheduleTick(cauldronPos, cauldronState.getBlock(), delay);
/*     */   }
/*     */   
/*     */   public static void spawnDripParticle(Level level, BlockPos stalactiteTipPos, BlockState stalactiteTipState) {
/* 173 */     getFluidAboveStalactite(level, stalactiteTipPos, stalactiteTipState).ifPresent(fluidAbove -> spawnDripParticle(level, stalactiteTipPos, stalactiteTipState, fluidAbove.fluid, fluidAbove.pos));
/*     */   }
/*     */   
/*     */   private static void spawnDripParticle(Level level, BlockPos stalactiteTipPos, BlockState stalactiteTipState, Fluid fluidAbove, BlockPos posAbove) {
/* 177 */     Vec3 offset = stalactiteTipState.getOffset(stalactiteTipPos);
/* 178 */     double PIXEL_SIZE = 0.0625D;
/* 179 */     double x = stalactiteTipPos.getX() + 0.5D + offset.x;
/* 180 */     double y = stalactiteTipPos.getY() + STALACTITE_DRIP_START_PIXEL - 0.0625D;
/* 181 */     double z = stalactiteTipPos.getZ() + 0.5D + offset.z;
/* 183 */     ParticleOptions dripParticle = getDripParticle(level, fluidAbove, posAbove);
/* 184 */     level.addParticle(dripParticle, x, y, z, 0.0D, 0.0D, 0.0D);
/*     */   }
/*     */   
/*     */   private static Optional<BlockPos> findRootBlock(Level level, BlockPos pos, BlockState dripStoneState, int maxSearchLength) {
/* 190 */     Direction tipDirection = (Direction)dripStoneState.getValue((Property)TIP_DIRECTION);
/* 191 */     BiPredicate<BlockPos, BlockState> pathPredicate = (pathPos, state) -> (state.is(dripStoneState.getBlock()) && state.getValue((Property)TIP_DIRECTION) == tipDirection);
/* 192 */     return findBlockVertical((LevelAccessor)level, pos, tipDirection.getOpposite().getAxisDirection(), pathPredicate, state -> !state.is(dripStoneState.getBlock()), maxSearchLength);
/*     */   }
/*     */   
/*     */   private static BlockPos findFillableCauldronBelowStalactiteTip(Level level, BlockPos stalactiteTipPos, Fluid fluid) {
/* 196 */     Predicate<BlockState> cauldronPredicate = state -> (state.getBlock() instanceof AbstractCauldronBlock && ((AbstractCauldronBlock)state.getBlock()).canReceiveStalactiteDrip(fluid));
/* 197 */     BiPredicate<BlockPos, BlockState> pathPredicate = (pos, state) -> canDripThrough((BlockGetter)level, pos, state);
/* 198 */     return findBlockVertical((LevelAccessor)level, stalactiteTipPos, Direction.DOWN.getAxisDirection(), pathPredicate, cauldronPredicate, 11).orElse(null);
/*     */   }
/*     */   
/*     */   public static BlockPos findStalactiteTipAboveCauldron(Level level, BlockPos cauldronPos) {
/* 202 */     BiPredicate<BlockPos, BlockState> pathPredicate = (pos, state) -> canDripThrough((BlockGetter)level, pos, state);
/* 203 */     return findBlockVertical((LevelAccessor)level, cauldronPos, Direction.UP.getAxisDirection(), pathPredicate, SpeleothemBlock::isFreeHangingStalactite, 11).orElse(null);
/*     */   }
/*     */   
/*     */   public static Fluid getCauldronFillFluidType(ServerLevel level, BlockPos stalactitePos) {
/* 207 */     return getFluidAboveStalactite((Level)level, stalactitePos, level.getBlockState(stalactitePos))
/* 208 */       .<Fluid>map(fluidSource -> fluidSource.fluid)
/* 209 */       .filter(PointedDripstoneBlock::canFillCauldron)
/* 210 */       .orElse(Fluids.EMPTY);
/*     */   }
/*     */   
/*     */   private static Optional<FluidInfo> getFluidAboveStalactite(Level level, BlockPos stalactitePos, BlockState stalactiteState) {
/* 214 */     if (!isStalactite(stalactiteState))
/* 215 */       return Optional.empty(); 
/* 218 */     return findRootBlock(level, stalactitePos, stalactiteState, 11).map(rootPos -> {
/*     */           Fluid fluid;
/*     */           BlockPos abovePos = rootPos.above();
/*     */           BlockState aboveState = level.getBlockState(abovePos);
/*     */           if (aboveState.is(Blocks.MUD) && !((Boolean)level.environmentAttributes().getValue(EnvironmentAttributes.WATER_EVAPORATES, abovePos)).booleanValue()) {
/*     */             FlowingFluid flowingFluid = Fluids.WATER;
/*     */           } else {
/*     */             fluid = level.getFluidState(abovePos).getType();
/*     */           } 
/*     */           return new FluidInfo(abovePos, fluid, aboveState);
/*     */         });
/*     */   }
/*     */   
/*     */   private static boolean canFillCauldron(Fluid fluidAbove) {
/* 232 */     return (fluidAbove == Fluids.LAVA || fluidAbove == Fluids.WATER);
/*     */   }
/*     */   
/*     */   protected boolean canGrow(LevelReader level, BlockPos pos) {
/* 237 */     FluidState fluidState = level.getBlockState(pos.above(2)).getFluidState();
/* 238 */     return (super.canGrow(level, pos) && fluidState.is(Fluids.WATER) && fluidState.isSource());
/*     */   }
/*     */   
/*     */   private static ParticleOptions getDripParticle(Level level, Fluid fluidAbove, BlockPos posAbove) {
/* 242 */     if (fluidAbove.isSame(Fluids.EMPTY))
/* 243 */       return (ParticleOptions)level.environmentAttributes().getValue(EnvironmentAttributes.DEFAULT_DRIPSTONE_PARTICLE, posAbove); 
/* 245 */     return fluidAbove.is(FluidTags.LAVA) ? (ParticleOptions)ParticleTypes.DRIPPING_DRIPSTONE_LAVA : (ParticleOptions)ParticleTypes.DRIPPING_DRIPSTONE_WATER;
/*     */   }
/*     */   
/*     */   protected boolean blocksStalagmiteScan(LevelReader level, BlockPos pos, BlockState state) {
/* 250 */     return !canDripThrough((BlockGetter)level, pos, state);
/*     */   }
/*     */   
/*     */   private static boolean canDripThrough(BlockGetter level, BlockPos pos, BlockState state) {
/* 257 */     if (state.isAir())
/* 258 */       return true; 
/* 260 */     if (state.isSolidRender())
/* 261 */       return false; 
/* 263 */     if (!state.getFluidState().isEmpty())
/* 264 */       return false; 
/* 266 */     VoxelShape collisionShape = state.getCollisionShape(level, pos);
/* 267 */     return !Shapes.joinIsNotEmpty(REQUIRED_SPACE_TO_DRIP_THROUGH_NON_SOLID_BLOCK, collisionShape, BooleanOp.AND);
/*     */   }
/*     */   
/*     */   private static final class FluidInfo extends Record {
/*     */     private final BlockPos pos;
/*     */     
/*     */     private final Fluid fluid;
/*     */     
/*     */     private final BlockState sourceState;
/*     */     
/*     */     private FluidInfo(BlockPos pos, Fluid fluid, BlockState sourceState) {
/* 270 */       this.pos = pos;
/* 270 */       this.fluid = fluid;
/* 270 */       this.sourceState = sourceState;
/*     */     }
/*     */     
/*     */     public final String toString() {
/*     */       // Byte code:
/*     */       //   0: aload_0
/*     */       //   1: <illegal opcode> toString : (Lnet/minecraft/world/level/block/PointedDripstoneBlock$FluidInfo;)Ljava/lang/String;
/*     */       //   6: areturn
/*     */       // Line number table:
/*     */       //   Java source line number -> byte code offset
/*     */       //   #270	-> 0
/*     */       // Local variable table:
/*     */       //   start	length	slot	name	descriptor
/*     */       //   0	7	0	this	Lnet/minecraft/world/level/block/PointedDripstoneBlock$FluidInfo;
/*     */     }
/*     */     
/*     */     public final int hashCode() {
/*     */       // Byte code:
/*     */       //   0: aload_0
/*     */       //   1: <illegal opcode> hashCode : (Lnet/minecraft/world/level/block/PointedDripstoneBlock$FluidInfo;)I
/*     */       //   6: ireturn
/*     */       // Line number table:
/*     */       //   Java source line number -> byte code offset
/*     */       //   #270	-> 0
/*     */       // Local variable table:
/*     */       //   start	length	slot	name	descriptor
/*     */       //   0	7	0	this	Lnet/minecraft/world/level/block/PointedDripstoneBlock$FluidInfo;
/*     */     }
/*     */     
/*     */     public final boolean equals(Object o) {
/*     */       // Byte code:
/*     */       //   0: aload_0
/*     */       //   1: aload_1
/*     */       //   2: <illegal opcode> equals : (Lnet/minecraft/world/level/block/PointedDripstoneBlock$FluidInfo;Ljava/lang/Object;)Z
/*     */       //   7: ireturn
/*     */       // Line number table:
/*     */       //   Java source line number -> byte code offset
/*     */       //   #270	-> 0
/*     */       // Local variable table:
/*     */       //   start	length	slot	name	descriptor
/*     */       //   0	8	0	this	Lnet/minecraft/world/level/block/PointedDripstoneBlock$FluidInfo;
/*     */       //   0	8	1	o	Ljava/lang/Object;
/*     */     }
/*     */     
/*     */     public BlockPos pos() {
/* 270 */       return this.pos;
/*     */     }
/*     */     
/*     */     public Fluid fluid() {
/* 270 */       return this.fluid;
/*     */     }
/*     */     
/*     */     public BlockState sourceState() {
/* 270 */       return this.sourceState;
/*     */     }
/*     */   }
/*     */ }


/* Location:              C:\Users\karol\AppData\Roaming\ModrinthApp\meta\versions\26.2-0.19.5\26.2-0.19.5.jar!\net\minecraft\world\level\block\PointedDripstoneBlock.class
 * Java compiler version: 25 (69.0)
 * JD-Core Version:       1.1.3
 */