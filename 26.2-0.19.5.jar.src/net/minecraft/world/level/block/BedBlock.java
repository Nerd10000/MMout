/*     */ package net.minecraft.world.level.block;
/*     */ 
/*     */ import com.mojang.datafixers.kinds.App;
/*     */ import com.mojang.datafixers.kinds.Applicative;
/*     */ import com.mojang.math.OctahedralGroup;
/*     */ import com.mojang.serialization.MapCodec;
/*     */ import com.mojang.serialization.codecs.RecordCodecBuilder;
/*     */ import java.util.List;
/*     */ import java.util.Map;
/*     */ import java.util.Objects;
/*     */ import java.util.Optional;
/*     */ import java.util.function.BiFunction;
/*     */ import net.minecraft.core.BlockPos;
/*     */ import net.minecraft.core.Direction;
/*     */ import net.minecraft.core.Vec3i;
/*     */ import net.minecraft.network.chat.Component;
/*     */ import net.minecraft.util.Mth;
/*     */ import net.minecraft.util.RandomSource;
/*     */ import net.minecraft.util.Util;
/*     */ import net.minecraft.world.InteractionResult;
/*     */ import net.minecraft.world.attribute.BedRule;
/*     */ import net.minecraft.world.attribute.EnvironmentAttributes;
/*     */ import net.minecraft.world.entity.Entity;
/*     */ import net.minecraft.world.entity.EntityType;
/*     */ import net.minecraft.world.entity.LivingEntity;
/*     */ import net.minecraft.world.entity.npc.villager.Villager;
/*     */ import net.minecraft.world.entity.player.Player;
/*     */ import net.minecraft.world.entity.vehicle.DismountHelper;
/*     */ import net.minecraft.world.item.DyeColor;
/*     */ import net.minecraft.world.item.ItemStack;
/*     */ import net.minecraft.world.item.context.BlockPlaceContext;
/*     */ import net.minecraft.world.level.BlockGetter;
/*     */ import net.minecraft.world.level.CollisionGetter;
/*     */ import net.minecraft.world.level.Level;
/*     */ import net.minecraft.world.level.LevelReader;
/*     */ import net.minecraft.world.level.ScheduledTickAccess;
/*     */ import net.minecraft.world.level.block.state.BlockBehaviour;
/*     */ import net.minecraft.world.level.block.state.BlockState;
/*     */ import net.minecraft.world.level.block.state.StateDefinition;
/*     */ import net.minecraft.world.level.block.state.properties.BedPart;
/*     */ import net.minecraft.world.level.block.state.properties.BlockStateProperties;
/*     */ import net.minecraft.world.level.block.state.properties.BooleanProperty;
/*     */ import net.minecraft.world.level.block.state.properties.EnumProperty;
/*     */ import net.minecraft.world.level.block.state.properties.Property;
/*     */ import net.minecraft.world.level.pathfinder.PathComputationType;
/*     */ import net.minecraft.world.phys.AABB;
/*     */ import net.minecraft.world.phys.BlockHitResult;
/*     */ import net.minecraft.world.phys.Vec3;
/*     */ import net.minecraft.world.phys.shapes.CollisionContext;
/*     */ import net.minecraft.world.phys.shapes.Shapes;
/*     */ import net.minecraft.world.phys.shapes.VoxelShape;
/*     */ import org.apache.commons.lang3.ArrayUtils;
/*     */ 
/*     */ public class BedBlock extends HorizontalDirectionalBlock {
/*     */   public static final MapCodec<BedBlock> CODEC;
/*     */   
/*     */   static {
/*  50 */     CODEC = RecordCodecBuilder.mapCodec(i -> i.group((App)DyeColor.CODEC.fieldOf("color").forGetter(BedBlock::getColor), (App)propertiesCodec()).apply((Applicative)i, BedBlock::new));
/*     */   }
/*     */   
/*     */   public MapCodec<BedBlock> codec() {
/*  57 */     return CODEC;
/*     */   }
/*     */   
/*  60 */   public static final EnumProperty<BedPart> PART = BlockStateProperties.BED_PART;
/*     */   
/*  61 */   public static final BooleanProperty OCCUPIED = BlockStateProperties.OCCUPIED;
/*     */   
/*     */   private static final Map<Direction, VoxelShape> SHAPES;
/*     */   
/*     */   private final DyeColor color;
/*     */   
/*     */   static {
/*  63 */     SHAPES = (Map<Direction, VoxelShape>)Util.make(() -> {
/*     */           VoxelShape northWestLeg = Block.box(0.0D, 0.0D, 0.0D, 3.0D, 3.0D, 3.0D);
/*     */           VoxelShape northEastLeg = Shapes.rotate(northWestLeg, OctahedralGroup.BLOCK_ROT_Y_90);
/*     */           return Shapes.rotateHorizontal(Shapes.or(Block.column(16.0D, 3.0D, 9.0D), new VoxelShape[] { northWestLeg, northEastLeg }));
/*     */         });
/*     */   }
/*     */   
/*     */   public BedBlock(DyeColor color, BlockBehaviour.Properties properties) {
/*  77 */     super(properties);
/*  78 */     this.color = color;
/*  79 */     registerDefaultState((BlockState)((BlockState)((BlockState)this.stateDefinition.any()).setValue((Property)PART, (Comparable)BedPart.FOOT)).setValue((Property)OCCUPIED, Boolean.valueOf(false)));
/*     */   }
/*     */   
/*     */   public static Direction getBedOrientation(BlockGetter level, BlockPos pos) {
/*  83 */     BlockState blockState = level.getBlockState(pos);
/*  84 */     return (blockState.getBlock() instanceof BedBlock) ? (Direction)blockState.getValue((Property)FACING) : null;
/*     */   }
/*     */   
/*     */   protected InteractionResult useWithoutItem(BlockState state, Level level, BlockPos pos, Player player, BlockHitResult hitResult) {
/*  89 */     if (level.isClientSide())
/*  90 */       return (InteractionResult)InteractionResult.SUCCESS_SERVER; 
/*  93 */     if (state.getValue((Property)PART) != BedPart.HEAD) {
/*  95 */       pos = pos.relative((Direction)state.getValue((Property)FACING));
/*  96 */       state = level.getBlockState(pos);
/*  97 */       if (!state.is(this))
/*  98 */         return (InteractionResult)InteractionResult.CONSUME; 
/*     */     } 
/* 102 */     BedRule bedRule = (BedRule)level.environmentAttributes().getValue(EnvironmentAttributes.BED_RULE, pos);
/* 103 */     if (bedRule.explodes()) {
/* 104 */       Objects.requireNonNull(player);
/* 104 */       bedRule.errorMessage().ifPresent(player::sendOverlayMessage);
/* 107 */       level.removeBlock(pos, false);
/* 110 */       BlockPos blockPos = pos.relative(((Direction)state.getValue((Property)FACING)).getOpposite());
/* 111 */       if (level.getBlockState(blockPos).is(this))
/* 112 */         level.removeBlock(blockPos, false); 
/* 115 */       Vec3 boomPos = Vec3.atCenterOf((Vec3i)pos);
/* 116 */       level.explode(null, level.damageSources().badRespawnPointExplosion(boomPos), null, boomPos, 5.0F, true, Level.ExplosionInteraction.BLOCK);
/* 117 */       return (InteractionResult)InteractionResult.SUCCESS_SERVER;
/*     */     } 
/* 120 */     if (((Boolean)state.getValue((Property)OCCUPIED)).booleanValue()) {
/* 121 */       if (!kickVillagerOutOfBed(level, pos))
/* 122 */         player.sendOverlayMessage((Component)Component.translatable("block.minecraft.bed.occupied")); 
/* 124 */       return (InteractionResult)InteractionResult.SUCCESS_SERVER;
/*     */     } 
/* 127 */     player.startSleepInBed(pos)
/* 128 */       .ifLeft(problem -> {
/*     */           if (problem.message() != null)
/*     */             player.sendOverlayMessage(problem.message()); 
/*     */         });
/* 133 */     return (InteractionResult)InteractionResult.SUCCESS_SERVER;
/*     */   }
/*     */   
/*     */   private boolean kickVillagerOutOfBed(Level level, BlockPos pos) {
/* 138 */     List<Villager> villagers = level.getEntitiesOfClass(Villager.class, new AABB(pos), LivingEntity::isSleeping);
/* 139 */     if (villagers.isEmpty())
/* 140 */       return false; 
/* 142 */     ((Villager)villagers.get(0)).stopSleeping();
/* 143 */     return true;
/*     */   }
/*     */   
/*     */   public void fallOn(Level level, BlockState state, BlockPos pos, Entity entity, double fallDistance) {
/* 148 */     super.fallOn(level, state, pos, entity, fallDistance * 0.5D);
/*     */   }
/*     */   
/*     */   protected BlockState updateShape(BlockState state, LevelReader level, ScheduledTickAccess ticks, BlockPos pos, Direction directionToNeighbour, BlockPos neighbourPos, BlockState neighbourState, RandomSource random) {
/* 153 */     if (directionToNeighbour == getNeighbourDirection((BedPart)state.getValue((Property)PART), (Direction)state.getValue((Property)FACING))) {
/* 154 */       if (neighbourState.is(this) && neighbourState.getValue((Property)PART) != state.getValue((Property)PART))
/* 155 */         return (BlockState)state.setValue((Property)OCCUPIED, neighbourState.getValue((Property)OCCUPIED)); 
/* 157 */       return Blocks.AIR.defaultBlockState();
/*     */     } 
/* 161 */     return super.updateShape(state, level, ticks, pos, directionToNeighbour, neighbourPos, neighbourState, random);
/*     */   }
/*     */   
/*     */   private static Direction getNeighbourDirection(BedPart part, Direction facing) {
/* 165 */     return (part == BedPart.FOOT) ? facing : facing.getOpposite();
/*     */   }
/*     */   
/*     */   public BlockState playerWillDestroy(Level level, BlockPos pos, BlockState state, Player player) {
/* 170 */     if (!level.isClientSide() && player.preventsBlockDrops()) {
/* 171 */       BedPart part = (BedPart)state.getValue((Property)PART);
/* 172 */       if (part == BedPart.FOOT) {
/* 173 */         BlockPos headPos = pos.relative(getNeighbourDirection(part, (Direction)state.getValue((Property)FACING)));
/* 174 */         BlockState headState = level.getBlockState(headPos);
/* 175 */         if (headState.is(this) && headState.getValue((Property)PART) == BedPart.HEAD) {
/* 177 */           level.setBlock(headPos, Blocks.AIR.defaultBlockState(), 35);
/* 178 */           level.levelEvent((Entity)player, 2001, headPos, Block.getId(headState));
/*     */         } 
/*     */       } 
/*     */     } 
/* 183 */     return super.playerWillDestroy(level, pos, state, player);
/*     */   }
/*     */   
/*     */   public BlockState getStateForPlacement(BlockPlaceContext context) {
/* 188 */     Direction facing = context.getHorizontalDirection();
/* 190 */     BlockPos pos = context.getClickedPos();
/* 191 */     BlockPos relative = pos.relative(facing);
/* 192 */     Level level = context.getLevel();
/* 193 */     if (level.getBlockState(relative).canBeReplaced(context) && level.getWorldBorder().isWithinBounds(relative))
/* 194 */       return (BlockState)defaultBlockState().setValue((Property)FACING, (Comparable)facing); 
/* 197 */     return null;
/*     */   }
/*     */   
/*     */   protected VoxelShape getShape(BlockState state, BlockGetter level, BlockPos pos, CollisionContext context) {
/* 202 */     return SHAPES.get(getConnectedDirection(state).getOpposite());
/*     */   }
/*     */   
/*     */   public static Direction getConnectedDirection(BlockState state) {
/* 206 */     Direction facing = (Direction)state.getValue((Property)FACING);
/* 207 */     return (state.getValue((Property)PART) == BedPart.HEAD) ? facing.getOpposite() : facing;
/*     */   }
/*     */   
/*     */   public static DoubleBlockCombiner.BlockType getBlockType(BlockState state) {
/* 211 */     BedPart part = (BedPart)state.getValue((Property)PART);
/* 212 */     if (part == BedPart.HEAD)
/* 213 */       return DoubleBlockCombiner.BlockType.FIRST; 
/* 215 */     return DoubleBlockCombiner.BlockType.SECOND;
/*     */   }
/*     */   
/*     */   private static boolean isBunkBed(BlockGetter level, BlockPos pos) {
/* 219 */     return level.getBlockState(pos.below()).getBlock() instanceof BedBlock;
/*     */   }
/*     */   
/*     */   public static Optional<Vec3> findStandUpPosition(EntityType<?> type, CollisionGetter level, BlockPos pos, Direction forward, float yaw) {
/* 223 */     Direction right = forward.getClockWise();
/* 224 */     Direction side = right.isFacingAngle(yaw) ? right.getOpposite() : right;
/* 226 */     if (isBunkBed((BlockGetter)level, pos))
/* 227 */       return findBunkBedStandUpPosition(type, level, pos, forward, side); 
/* 230 */     int[][] offsets = bedStandUpOffsets(forward, side);
/* 232 */     Optional<Vec3> safePosition = findStandUpPositionAtOffset(type, level, pos, offsets, true);
/* 233 */     if (safePosition.isPresent())
/* 234 */       return safePosition; 
/* 236 */     return findStandUpPositionAtOffset(type, level, pos, offsets, false);
/*     */   }
/*     */   
/*     */   private static Optional<Vec3> findBunkBedStandUpPosition(EntityType<?> type, CollisionGetter level, BlockPos pos, Direction forward, Direction side) {
/* 240 */     int[][] offsets = bedSurroundStandUpOffsets(forward, side);
/* 242 */     Optional<Vec3> safePosition = findStandUpPositionAtOffset(type, level, pos, offsets, true);
/* 243 */     if (safePosition.isPresent())
/* 244 */       return safePosition; 
/* 247 */     BlockPos below = pos.below();
/* 249 */     Optional<Vec3> belowSafePosition = findStandUpPositionAtOffset(type, level, below, offsets, true);
/* 250 */     if (belowSafePosition.isPresent())
/* 251 */       return belowSafePosition; 
/* 254 */     int[][] aboveOffsets = bedAboveStandUpOffsets(forward);
/* 256 */     Optional<Vec3> aboveSafePosition = findStandUpPositionAtOffset(type, level, pos, aboveOffsets, true);
/* 257 */     if (aboveSafePosition.isPresent())
/* 258 */       return aboveSafePosition; 
/* 261 */     Optional<Vec3> unsafePosition = findStandUpPositionAtOffset(type, level, pos, offsets, false);
/* 262 */     if (unsafePosition.isPresent())
/* 263 */       return unsafePosition; 
/* 266 */     Optional<Vec3> belowUnsafePosition = findStandUpPositionAtOffset(type, level, below, offsets, false);
/* 267 */     if (belowUnsafePosition.isPresent())
/* 268 */       return belowUnsafePosition; 
/* 271 */     return findStandUpPositionAtOffset(type, level, pos, aboveOffsets, false);
/*     */   }
/*     */   
/*     */   private static Optional<Vec3> findStandUpPositionAtOffset(EntityType<?> type, CollisionGetter level, BlockPos pos, int[][] offsets, boolean checkDangerous) {
/* 275 */     BlockPos.MutableBlockPos blockPos = new BlockPos.MutableBlockPos();
/* 276 */     for (int[] offset : offsets) {
/* 277 */       blockPos.set(pos.getX() + offset[0], pos.getY(), pos.getZ() + offset[1]);
/* 279 */       Vec3 position = DismountHelper.findSafeDismountLocation(type, level, (BlockPos)blockPos, checkDangerous);
/* 280 */       if (position != null)
/* 281 */         return Optional.of(position); 
/*     */     } 
/* 284 */     return Optional.empty();
/*     */   }
/*     */   
/*     */   protected void createBlockStateDefinition(StateDefinition.Builder<Block, BlockState> builder) {
/* 289 */     builder.add(new Property[] { (Property)FACING, (Property)PART, (Property)OCCUPIED });
/*     */   }
/*     */   
/*     */   public void setPlacedBy(Level level, BlockPos pos, BlockState state, LivingEntity by, ItemStack itemStack) {
/* 294 */     super.setPlacedBy(level, pos, state, by, itemStack);
/* 295 */     BlockPos otherPos = pos.relative((Direction)state.getValue((Property)FACING));
/* 296 */     level.setBlockAndUpdate(otherPos, (BlockState)state.setValue((Property)PART, (Comparable)BedPart.HEAD));
/*     */   }
/*     */   
/*     */   public DyeColor getColor() {
/* 300 */     return this.color;
/*     */   }
/*     */   
/*     */   protected long getSeed(BlockState state, BlockPos pos) {
/* 305 */     BlockPos sourcePos = pos.relative((Direction)state.getValue((Property)FACING), (state.getValue((Property)PART) == BedPart.HEAD) ? 0 : 1);
/* 306 */     return Mth.getSeed(sourcePos.getX(), pos.getY(), sourcePos.getZ());
/*     */   }
/*     */   
/*     */   protected boolean isPathfindable(BlockState state, PathComputationType type) {
/* 311 */     return false;
/*     */   }
/*     */   
/*     */   private static int[][] bedStandUpOffsets(Direction forward, Direction side) {
/* 318 */     return (int[][])ArrayUtils.addAll((Object[])bedSurroundStandUpOffsets(forward, side), (Object[])bedAboveStandUpOffsets(forward));
/*     */   }
/*     */   
/*     */   private static int[][] bedSurroundStandUpOffsets(Direction forward, Direction side) {
/* 326 */     return new int[][] { { side
/* 327 */           .getStepX(), side.getStepZ() }, { side
/* 328 */           .getStepX() - forward.getStepX(), side.getStepZ() - forward.getStepZ() }, { side
/* 329 */           .getStepX() - forward.getStepX() * 2, side.getStepZ() - forward.getStepZ() * 2 }, { -forward.getStepX() * 2, -forward.getStepZ() * 2 }, { -side.getStepX() - forward.getStepX() * 2, -side.getStepZ() - forward.getStepZ() * 2 }, { -side.getStepX() - forward.getStepX(), -side.getStepZ() - forward.getStepZ() }, { -side.getStepX(), -side.getStepZ() }, { -side.getStepX() + forward.getStepX(), -side.getStepZ() + forward.getStepZ() }, { forward
/* 335 */           .getStepX(), forward.getStepZ() }, { side
/* 336 */           .getStepX() + forward.getStepX(), side.getStepZ() + forward.getStepZ() } };
/*     */   }
/*     */   
/*     */   private static int[][] bedAboveStandUpOffsets(Direction forward) {
/* 342 */     return new int[][] { { 0, 0 }, { -forward.getStepX(), -forward.getStepZ() } };
/*     */   }
/*     */ }


/* Location:              C:\Users\karol\AppData\Roaming\ModrinthApp\meta\versions\26.2-0.19.5\26.2-0.19.5.jar!\net\minecraft\world\level\block\BedBlock.class
 * Java compiler version: 25 (69.0)
 * JD-Core Version:       1.1.3
 */