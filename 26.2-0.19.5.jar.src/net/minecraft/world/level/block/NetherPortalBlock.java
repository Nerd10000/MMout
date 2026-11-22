/*     */ package net.minecraft.world.level.block;
/*     */ 
/*     */ import com.mojang.logging.LogUtils;
/*     */ import com.mojang.serialization.MapCodec;
/*     */ import java.util.Map;
/*     */ import java.util.Optional;
/*     */ import java.util.Set;
/*     */ import java.util.function.Function;
/*     */ import net.minecraft.core.BlockPos;
/*     */ import net.minecraft.core.Direction;
/*     */ import net.minecraft.core.particles.ParticleOptions;
/*     */ import net.minecraft.core.particles.ParticleTypes;
/*     */ import net.minecraft.resources.ResourceKey;
/*     */ import net.minecraft.server.level.ServerLevel;
/*     */ import net.minecraft.sounds.SoundEvents;
/*     */ import net.minecraft.sounds.SoundSource;
/*     */ import net.minecraft.util.BlockUtil;
/*     */ import net.minecraft.util.RandomSource;
/*     */ import net.minecraft.world.Difficulty;
/*     */ import net.minecraft.world.attribute.EnvironmentAttributes;
/*     */ import net.minecraft.world.entity.Entity;
/*     */ import net.minecraft.world.entity.EntityDimensions;
/*     */ import net.minecraft.world.entity.EntitySpawnReason;
/*     */ import net.minecraft.world.entity.EntityTypes;
/*     */ import net.minecraft.world.entity.InsideBlockEffectApplier;
/*     */ import net.minecraft.world.entity.Relative;
/*     */ import net.minecraft.world.entity.player.Player;
/*     */ import net.minecraft.world.item.ItemStack;
/*     */ import net.minecraft.world.level.BlockGetter;
/*     */ import net.minecraft.world.level.Level;
/*     */ import net.minecraft.world.level.LevelReader;
/*     */ import net.minecraft.world.level.ScheduledTickAccess;
/*     */ import net.minecraft.world.level.block.state.BlockBehaviour;
/*     */ import net.minecraft.world.level.block.state.BlockState;
/*     */ import net.minecraft.world.level.block.state.StateDefinition;
/*     */ import net.minecraft.world.level.block.state.properties.BlockStateProperties;
/*     */ import net.minecraft.world.level.block.state.properties.EnumProperty;
/*     */ import net.minecraft.world.level.block.state.properties.Property;
/*     */ import net.minecraft.world.level.border.WorldBorder;
/*     */ import net.minecraft.world.level.dimension.DimensionType;
/*     */ import net.minecraft.world.level.gamerules.GameRules;
/*     */ import net.minecraft.world.level.portal.PortalShape;
/*     */ import net.minecraft.world.level.portal.TeleportTransition;
/*     */ import net.minecraft.world.phys.Vec3;
/*     */ import net.minecraft.world.phys.shapes.CollisionContext;
/*     */ import net.minecraft.world.phys.shapes.Shapes;
/*     */ import net.minecraft.world.phys.shapes.VoxelShape;
/*     */ import org.slf4j.Logger;
/*     */ 
/*     */ public class NetherPortalBlock extends Block implements Portal {
/*  48 */   private static final Logger LOGGER = LogUtils.getLogger();
/*     */   
/*  49 */   public static final MapCodec<NetherPortalBlock> CODEC = simpleCodec(NetherPortalBlock::new);
/*     */   
/*     */   public MapCodec<NetherPortalBlock> codec() {
/*  53 */     return CODEC;
/*     */   }
/*     */   
/*  56 */   public static final EnumProperty<Direction.Axis> AXIS = BlockStateProperties.HORIZONTAL_AXIS;
/*     */   
/*  58 */   private static final Map<Direction.Axis, VoxelShape> SHAPES = Shapes.rotateHorizontalAxis(Block.column(4.0D, 16.0D, 0.0D, 16.0D));
/*     */   
/*     */   public NetherPortalBlock(BlockBehaviour.Properties properties) {
/*  61 */     super(properties);
/*  62 */     registerDefaultState((BlockState)((BlockState)this.stateDefinition.any()).setValue((Property)AXIS, (Comparable)Direction.Axis.X));
/*     */   }
/*     */   
/*     */   protected VoxelShape getShape(BlockState state, BlockGetter level, BlockPos pos, CollisionContext context) {
/*  67 */     return SHAPES.get(state.getValue((Property)AXIS));
/*     */   }
/*     */   
/*     */   protected void randomTick(BlockState state, ServerLevel level, BlockPos pos, RandomSource random) {
/*  72 */     if (level.isSpawningMonsters() && level.getLevelData().getDifficulty() != Difficulty.PEACEFUL && ((Boolean)level.environmentAttributes().getValue(EnvironmentAttributes.NETHER_PORTAL_SPAWNS_PIGLINS, pos)).booleanValue() && random.nextInt(2000) < level.getDifficulty().getId() && level.anyPlayerCloseEnoughForSpawning(pos)) {
/*  74 */       while (level.getBlockState(pos).is(this))
/*  75 */         pos = pos.below(); 
/*  77 */       if (level.getBlockState(pos).isValidSpawn((BlockGetter)level, pos, EntityTypes.ZOMBIFIED_PIGLIN)) {
/*  78 */         Entity entity = EntityTypes.ZOMBIFIED_PIGLIN.spawn(level, pos.above(), EntitySpawnReason.STRUCTURE);
/*  79 */         if (entity != null) {
/*  80 */           entity.setPortalCooldown();
/*  81 */           Entity vehicle = entity.getVehicle();
/*  82 */           if (vehicle != null)
/*  83 */             vehicle.setPortalCooldown(); 
/*     */         } 
/*     */       } 
/*     */     } 
/*     */   }
/*     */   
/*     */   protected BlockState updateShape(BlockState state, LevelReader level, ScheduledTickAccess ticks, BlockPos pos, Direction directionToNeighbour, BlockPos neighbourPos, BlockState neighbourState, RandomSource random) {
/*  92 */     Direction.Axis updateAxis = directionToNeighbour.getAxis();
/*  93 */     Direction.Axis axis = (Direction.Axis)state.getValue((Property)AXIS);
/*  95 */     boolean wrongAxis = (axis != updateAxis && updateAxis.isHorizontal());
/*  96 */     if (wrongAxis || neighbourState.is(this) || PortalShape.findAnyShape((BlockGetter)level, pos, axis).isComplete())
/*  97 */       return super.updateShape(state, level, ticks, pos, directionToNeighbour, neighbourPos, neighbourState, random); 
/* 100 */     return Blocks.AIR.defaultBlockState();
/*     */   }
/*     */   
/*     */   protected void entityInside(BlockState state, Level level, BlockPos pos, Entity entity, InsideBlockEffectApplier effectApplier, boolean isPrecise) {
/* 105 */     if (entity.canUsePortal(false))
/* 106 */       entity.setAsInsidePortal(this, pos); 
/*     */   }
/*     */   
/*     */   public int getPortalTransitionTime(ServerLevel level, Entity entity) {
/* 112 */     if (entity instanceof Player) {
/* 112 */       Player player = (Player)entity;
/* 113 */       return Math.max(0, ((Integer)level.getGameRules().get(
/* 114 */             (player.getAbilities()).invulnerable ? 
/* 115 */             GameRules.PLAYERS_NETHER_PORTAL_CREATIVE_DELAY : 
/* 116 */             GameRules.PLAYERS_NETHER_PORTAL_DEFAULT_DELAY)).intValue());
/*     */     } 
/* 118 */     return 0;
/*     */   }
/*     */   
/*     */   public TeleportTransition getPortalDestination(ServerLevel currentLevel, Entity entity, BlockPos portalEntryPos) {
/* 123 */     ResourceKey<Level> newDimension = (currentLevel.dimension() == Level.NETHER) ? Level.OVERWORLD : Level.NETHER;
/* 124 */     ServerLevel newLevel = currentLevel.getServer().getLevel(newDimension);
/* 125 */     if (newLevel == null)
/* 126 */       return null; 
/* 129 */     boolean toNether = (newLevel.dimension() == Level.NETHER);
/* 131 */     WorldBorder newWorldBorder = newLevel.getWorldBorder();
/* 132 */     double teleportationScale = DimensionType.getTeleportationScale(currentLevel.dimensionType(), newLevel.dimensionType());
/* 133 */     BlockPos approximateExitPos = newWorldBorder.clampToBounds(entity
/* 134 */         .getX() * teleportationScale, entity
/* 135 */         .getY(), entity
/* 136 */         .getZ() * teleportationScale);
/* 139 */     return getExitPortal(newLevel, entity, portalEntryPos, approximateExitPos, toNether, newWorldBorder);
/*     */   }
/*     */   
/*     */   private TeleportTransition getExitPortal(ServerLevel newLevel, Entity entity, BlockPos portalEntryPos, BlockPos approximateExitPos, boolean toNether, WorldBorder worldBorder) {
/*     */     BlockUtil.FoundRectangle exitPortal;
/*     */     TeleportTransition.PostTeleportTransition post;
/* 143 */     Optional<BlockPos> exitPortalPos = newLevel.getPortalForcer().findClosestPortalPosition(approximateExitPos, toNether, worldBorder);
/* 147 */     if (exitPortalPos.isPresent()) {
/* 148 */       BlockPos pos = exitPortalPos.get();
/* 149 */       BlockState portalState = newLevel.getBlockState(pos);
/* 151 */       exitPortal = BlockUtil.getLargestRectangleAround(pos, (Direction.Axis)portalState.getValue((Property)BlockStateProperties.HORIZONTAL_AXIS), 21, Direction.Axis.Y, 21, blockPos -> (newLevel.getBlockState(blockPos) == portalState));
/* 152 */       post = TeleportTransition.PLAY_PORTAL_SOUND.then(e -> e.placePortalTicket(pos));
/*     */     } else {
/* 154 */       Direction.Axis sourcePortalAxis = entity.level().getBlockState(portalEntryPos).getOptionalValue((Property)AXIS).orElse(Direction.Axis.X);
/* 155 */       Optional<BlockUtil.FoundRectangle> createdExit = newLevel.getPortalForcer().createPortal(approximateExitPos, sourcePortalAxis);
/* 156 */       if (createdExit.isEmpty()) {
/* 157 */         LOGGER.error("Unable to create a portal, likely target out of worldborder");
/* 158 */         return null;
/*     */       } 
/* 161 */       exitPortal = createdExit.get();
/* 162 */       post = TeleportTransition.PLAY_PORTAL_SOUND.then(TeleportTransition.PLACE_PORTAL_TICKET);
/*     */     } 
/* 165 */     return getDimensionTransitionFromExit(entity, portalEntryPos, exitPortal, newLevel, post);
/*     */   }
/*     */   
/*     */   private static TeleportTransition getDimensionTransitionFromExit(Entity entity, BlockPos portalEntryPos, BlockUtil.FoundRectangle exitPortal, ServerLevel newLevel, TeleportTransition.PostTeleportTransition postTeleportTransition) {
/*     */     Direction.Axis axis;
/*     */     Vec3 offset;
/* 172 */     BlockState blockState = entity.level().getBlockState(portalEntryPos);
/* 173 */     if (blockState.hasProperty((Property)BlockStateProperties.HORIZONTAL_AXIS)) {
/* 174 */       axis = (Direction.Axis)blockState.getValue((Property)BlockStateProperties.HORIZONTAL_AXIS);
/* 177 */       BlockUtil.FoundRectangle portalArea = BlockUtil.getLargestRectangleAround(portalEntryPos, axis, 21, Direction.Axis.Y, 21, pos -> (entity.level().getBlockState(pos) == blockState));
/* 178 */       offset = entity.getRelativePortalPosition(axis, portalArea);
/*     */     } else {
/* 181 */       axis = Direction.Axis.X;
/* 182 */       offset = new Vec3(0.5D, 0.0D, 0.0D);
/*     */     } 
/* 184 */     return createDimensionTransition(newLevel, exitPortal, axis, offset, entity, postTeleportTransition);
/*     */   }
/*     */   
/*     */   private static TeleportTransition createDimensionTransition(ServerLevel newLevel, BlockUtil.FoundRectangle foundRectangle, Direction.Axis portalAxis, Vec3 offset, Entity entity, TeleportTransition.PostTeleportTransition postTeleportTransition) {
/* 188 */     BlockPos bottomLeft = foundRectangle.minCorner;
/* 189 */     BlockState blockState = newLevel.getBlockState(bottomLeft);
/* 190 */     Direction.Axis axis = blockState.getOptionalValue((Property)BlockStateProperties.HORIZONTAL_AXIS).orElse(Direction.Axis.X);
/* 191 */     double width = foundRectangle.axis1Size;
/* 192 */     double height = foundRectangle.axis2Size;
/* 193 */     EntityDimensions dimensions = entity.getDimensions(entity.getPose());
/* 195 */     int outputRotation = (portalAxis == axis) ? 0 : 90;
/* 197 */     double offsetRight = dimensions.width() / 2.0D + (width - dimensions.width()) * offset.x();
/* 198 */     double offsetUp = (height - dimensions.height()) * offset.y();
/* 199 */     double offsetForward = 0.5D + offset.z();
/* 201 */     boolean xAligned = (axis == Direction.Axis.X);
/* 205 */     Vec3 targetPos = new Vec3(bottomLeft.getX() + (xAligned ? offsetRight : offsetForward), bottomLeft.getY() + offsetUp, bottomLeft.getZ() + (xAligned ? offsetForward : offsetRight));
/* 208 */     Vec3 collisionFreePos = PortalShape.findCollisionFreePosition(targetPos, newLevel, entity, dimensions);
/* 209 */     return new TeleportTransition(newLevel, collisionFreePos, Vec3.ZERO, outputRotation, 0.0F, 
/*     */         
/* 215 */         Relative.union(new Set[] { Relative.DELTA, Relative.ROTATION }, ), postTeleportTransition);
/*     */   }
/*     */   
/*     */   public Portal.Transition getLocalTransition() {
/* 221 */     return Portal.Transition.CONFUSION;
/*     */   }
/*     */   
/*     */   public void animateTick(BlockState state, Level level, BlockPos pos, RandomSource random) {
/* 226 */     if (random.nextInt(100) == 0)
/* 227 */       level.playLocalSound(pos.getX() + 0.5D, pos.getY() + 0.5D, pos.getZ() + 0.5D, SoundEvents.PORTAL_AMBIENT, SoundSource.BLOCKS, 0.5F, random.nextFloat() * 0.4F + 0.8F, false); 
/* 230 */     for (int i = 0; i < 4; i++) {
/* 231 */       double x = pos.getX() + random.nextDouble();
/* 232 */       double y = pos.getY() + random.nextDouble();
/* 233 */       double z = pos.getZ() + random.nextDouble();
/* 234 */       double xa = (random.nextFloat() - 0.5D) * 0.5D;
/* 235 */       double ya = (random.nextFloat() - 0.5D) * 0.5D;
/* 236 */       double za = (random.nextFloat() - 0.5D) * 0.5D;
/* 238 */       int flip = random.nextInt(2) * 2 - 1;
/* 239 */       if (level.getBlockState(pos.west()).is(this) || level.getBlockState(pos.east()).is(this)) {
/* 240 */         z = pos.getZ() + 0.5D + 0.25D * flip;
/* 241 */         za = (random.nextFloat() * 2.0F * flip);
/*     */       } else {
/* 243 */         x = pos.getX() + 0.5D + 0.25D * flip;
/* 244 */         xa = (random.nextFloat() * 2.0F * flip);
/*     */       } 
/* 247 */       level.addParticle((ParticleOptions)ParticleTypes.PORTAL, x, y, z, xa, ya, za);
/*     */     } 
/*     */   }
/*     */   
/*     */   protected ItemStack getCloneItemStack(LevelReader level, BlockPos pos, BlockState state, boolean includeData) {
/* 253 */     return ItemStack.EMPTY;
/*     */   }
/*     */   
/*     */   protected BlockState rotate(BlockState state, Rotation rotation) {
/* 258 */     switch (rotation) {
/*     */       case COUNTERCLOCKWISE_90:
/*     */       case CLOCKWISE_90:
/* 260 */         switch ((Direction.Axis)state.getValue((Property)AXIS)) {
/*     */           case COUNTERCLOCKWISE_90:
/* 262 */             return (BlockState)state.setValue((Property)AXIS, (Comparable)Direction.Axis.Z);
/*     */           case CLOCKWISE_90:
/* 264 */             return (BlockState)state.setValue((Property)AXIS, (Comparable)Direction.Axis.X);
/*     */         } 
/* 266 */         return state;
/*     */     } 
/* 270 */     return state;
/*     */   }
/*     */   
/*     */   protected void createBlockStateDefinition(StateDefinition.Builder<Block, BlockState> builder) {
/* 277 */     builder.add(new Property[] { (Property)AXIS });
/*     */   }
/*     */ }


/* Location:              C:\Users\karol\AppData\Roaming\ModrinthApp\meta\versions\26.2-0.19.5\26.2-0.19.5.jar!\net\minecraft\world\level\block\NetherPortalBlock.class
 * Java compiler version: 25 (69.0)
 * JD-Core Version:       1.1.3
 */