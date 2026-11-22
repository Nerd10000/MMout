/*     */ package net.minecraft.world.level.block;
/*     */ 
/*     */ import com.google.common.base.MoreObjects;
/*     */ import com.mojang.serialization.MapCodec;
/*     */ import java.util.Map;
/*     */ import java.util.Optional;
/*     */ import java.util.function.Function;
/*     */ import net.minecraft.core.BlockPos;
/*     */ import net.minecraft.core.Direction;
/*     */ import net.minecraft.core.Holder;
/*     */ import net.minecraft.server.level.ServerLevel;
/*     */ import net.minecraft.sounds.SoundEvents;
/*     */ import net.minecraft.sounds.SoundSource;
/*     */ import net.minecraft.util.RandomSource;
/*     */ import net.minecraft.world.entity.LivingEntity;
/*     */ import net.minecraft.world.item.ItemStack;
/*     */ import net.minecraft.world.item.context.BlockPlaceContext;
/*     */ import net.minecraft.world.level.BlockGetter;
/*     */ import net.minecraft.world.level.Level;
/*     */ import net.minecraft.world.level.LevelReader;
/*     */ import net.minecraft.world.level.ScheduledTickAccess;
/*     */ import net.minecraft.world.level.block.state.BlockBehaviour;
/*     */ import net.minecraft.world.level.block.state.BlockState;
/*     */ import net.minecraft.world.level.block.state.StateDefinition;
/*     */ import net.minecraft.world.level.block.state.properties.BlockStateProperties;
/*     */ import net.minecraft.world.level.block.state.properties.BooleanProperty;
/*     */ import net.minecraft.world.level.block.state.properties.EnumProperty;
/*     */ import net.minecraft.world.level.block.state.properties.Property;
/*     */ import net.minecraft.world.level.gameevent.GameEvent;
/*     */ import net.minecraft.world.level.redstone.ExperimentalRedstoneUtils;
/*     */ import net.minecraft.world.level.redstone.Orientation;
/*     */ import net.minecraft.world.phys.shapes.CollisionContext;
/*     */ import net.minecraft.world.phys.shapes.Shapes;
/*     */ import net.minecraft.world.phys.shapes.VoxelShape;
/*     */ 
/*     */ public class TripWireHookBlock extends Block {
/*  36 */   public static final MapCodec<TripWireHookBlock> CODEC = simpleCodec(TripWireHookBlock::new);
/*     */   
/*     */   public MapCodec<TripWireHookBlock> codec() {
/*  40 */     return CODEC;
/*     */   }
/*     */   
/*  43 */   public static final EnumProperty<Direction> FACING = HorizontalDirectionalBlock.FACING;
/*     */   
/*  44 */   public static final BooleanProperty POWERED = BlockStateProperties.POWERED;
/*     */   
/*  45 */   public static final BooleanProperty ATTACHED = BlockStateProperties.ATTACHED;
/*     */   
/*     */   protected static final int WIRE_DIST_MIN = 1;
/*     */   
/*     */   protected static final int WIRE_DIST_MAX = 42;
/*     */   
/*     */   private static final int RECHECK_PERIOD = 10;
/*     */   
/*  51 */   private static final Map<Direction, VoxelShape> SHAPES = Shapes.rotateHorizontal(Block.boxZ(6.0D, 0.0D, 10.0D, 10.0D, 16.0D));
/*     */   
/*     */   public TripWireHookBlock(BlockBehaviour.Properties properties) {
/*  54 */     super(properties);
/*  55 */     registerDefaultState((BlockState)((BlockState)((BlockState)((BlockState)this.stateDefinition.any()).setValue((Property)FACING, (Comparable)Direction.NORTH)).setValue((Property)POWERED, Boolean.valueOf(false))).setValue((Property)ATTACHED, Boolean.valueOf(false)));
/*     */   }
/*     */   
/*     */   protected VoxelShape getShape(BlockState state, BlockGetter level, BlockPos pos, CollisionContext context) {
/*  60 */     return SHAPES.get(state.getValue((Property)FACING));
/*     */   }
/*     */   
/*     */   protected boolean canSurvive(BlockState state, LevelReader level, BlockPos pos) {
/*  65 */     Direction direction = (Direction)state.getValue((Property)FACING);
/*  66 */     BlockPos relative = pos.relative(direction.getOpposite());
/*  67 */     BlockState blockState = level.getBlockState(relative);
/*  68 */     return (direction.getAxis().isHorizontal() && blockState.isFaceSturdy((BlockGetter)level, relative, direction));
/*     */   }
/*     */   
/*     */   protected BlockState updateShape(BlockState state, LevelReader level, ScheduledTickAccess ticks, BlockPos pos, Direction directionToNeighbour, BlockPos neighbourPos, BlockState neighbourState, RandomSource random) {
/*  73 */     if (directionToNeighbour.getOpposite() == state.getValue((Property)FACING) && !state.canSurvive(level, pos))
/*  74 */       return Blocks.AIR.defaultBlockState(); 
/*  76 */     return super.updateShape(state, level, ticks, pos, directionToNeighbour, neighbourPos, neighbourState, random);
/*     */   }
/*     */   
/*     */   public BlockState getStateForPlacement(BlockPlaceContext context) {
/*  81 */     BlockState state = (BlockState)((BlockState)defaultBlockState().setValue((Property)POWERED, Boolean.valueOf(false))).setValue((Property)ATTACHED, Boolean.valueOf(false));
/*  83 */     Level level = context.getLevel();
/*  84 */     BlockPos pos = context.getClickedPos();
/*  86 */     Direction[] directions = context.getNearestLookingDirections();
/*  87 */     for (Direction direction : directions) {
/*  88 */       if (direction.getAxis().isHorizontal()) {
/*  92 */         Direction facing = direction.getOpposite();
/*  94 */         state = (BlockState)state.setValue((Property)FACING, (Comparable)facing);
/*  95 */         if (state.canSurvive((LevelReader)level, pos))
/*  96 */           return state; 
/*     */       } 
/*     */     } 
/* 100 */     return null;
/*     */   }
/*     */   
/*     */   public void setPlacedBy(Level level, BlockPos pos, BlockState state, LivingEntity by, ItemStack itemStack) {
/* 105 */     calculateState(level, pos, state, false, false, -1, (BlockState)null);
/*     */   }
/*     */   
/*     */   public static void calculateState(Level level, BlockPos pos, BlockState state, boolean isBeingDestroyed, boolean canUpdate, int wireSource, BlockState wireSourceState) {
/*     */     int k;
/* 109 */     Optional<Direction> facingOptional = state.getOptionalValue((Property)FACING);
/* 110 */     if (!facingOptional.isPresent())
/*     */       return; 
/* 114 */     Direction direction = facingOptional.get();
/* 115 */     int wasAttached = ((Boolean)state.getOptionalValue((Property)ATTACHED).orElse(Boolean.valueOf(false))).booleanValue();
/* 116 */     boolean wasPowered = ((Boolean)state.getOptionalValue((Property)POWERED).orElse(Boolean.valueOf(false))).booleanValue();
/* 118 */     Block block = state.getBlock();
/* 119 */     boolean attached = !isBeingDestroyed;
/* 120 */     boolean powered = false;
/* 121 */     int receiverPos = 0;
/* 123 */     BlockState[] wireStates = new BlockState[42];
/* 124 */     for (int i = 1; i < 42; i++) {
/* 125 */       BlockPos testPos = pos.relative(direction, i);
/* 126 */       BlockState wireState = level.getBlockState(testPos);
/* 128 */       if (wireState.is(Blocks.TRIPWIRE_HOOK)) {
/* 129 */         if (wireState.getValue((Property)FACING) == direction.getOpposite())
/* 130 */           receiverPos = i; 
/*     */         break;
/*     */       } 
/* 134 */       if (wireState.is(Blocks.TRIPWIRE) || i == wireSource) {
/* 135 */         if (i == wireSource)
/* 136 */           wireState = (BlockState)MoreObjects.firstNonNull(wireSourceState, wireState); 
/* 138 */         boolean wireArmed = !((Boolean)wireState.getValue((Property)TripWireBlock.DISARMED)).booleanValue();
/* 139 */         boolean wirePowered = ((Boolean)wireState.getValue((Property)TripWireBlock.POWERED)).booleanValue();
/* 140 */         k = powered | ((wireArmed && wirePowered) ? 1 : 0);
/* 142 */         wireStates[i] = wireState;
/* 144 */         if (i == wireSource) {
/* 145 */           level.scheduleTick(pos, block, 10);
/* 146 */           attached &= wireArmed;
/*     */         } 
/*     */       } else {
/* 149 */         wireStates[i] = null;
/* 150 */         attached = false;
/*     */       } 
/*     */     } 
/* 154 */     int j = attached & ((receiverPos > 1) ? 1 : 0);
/* 155 */     k &= j;
/* 156 */     BlockState newState = (BlockState)((BlockState)block.defaultBlockState().trySetValue((Property)ATTACHED, Boolean.valueOf(j))).trySetValue((Property)POWERED, Boolean.valueOf(k));
/* 158 */     if (receiverPos > 0) {
/* 159 */       BlockPos testPos = pos.relative(direction, receiverPos);
/* 160 */       Direction opposite = direction.getOpposite();
/* 161 */       level.setBlock(testPos, (BlockState)newState.setValue((Property)FACING, (Comparable)opposite), 3);
/* 162 */       notifyNeighbors(block, level, testPos, opposite);
/* 165 */       if (!level.getBlockState(pos).is(Blocks.TRIPWIRE_HOOK)) {
/* 166 */         onRemoved(newState, level, pos);
/*     */         return;
/*     */       } 
/* 170 */       emitState(level, testPos, j, k, wasAttached, wasPowered);
/*     */     } 
/* 173 */     emitState(level, pos, j, k, wasAttached, wasPowered);
/* 175 */     if (!isBeingDestroyed) {
/* 176 */       level.setBlock(pos, (BlockState)newState.setValue((Property)FACING, (Comparable)direction), 3);
/* 177 */       if (canUpdate)
/* 178 */         notifyNeighbors(block, level, pos, direction); 
/*     */     } 
/* 182 */     if (wasAttached != j)
/* 183 */       for (int m = 1; m < receiverPos; m++) {
/* 184 */         BlockPos testPos = pos.relative(direction, m);
/* 185 */         BlockState wireData = wireStates[m];
/* 186 */         if (wireData != null) {
/* 190 */           BlockState testPosState = level.getBlockState(testPos);
/* 191 */           if (testPosState.is(Blocks.TRIPWIRE) || testPosState.is(Blocks.TRIPWIRE_HOOK))
/* 192 */             level.setBlock(testPos, (BlockState)wireData.trySetValue((Property)ATTACHED, Boolean.valueOf(j)), 3); 
/*     */         } 
/*     */       }  
/*     */   }
/*     */   
/*     */   protected void tick(BlockState state, ServerLevel level, BlockPos pos, RandomSource random) {
/* 200 */     calculateState((Level)level, pos, state, false, true, -1, (BlockState)null);
/*     */   }
/*     */   
/*     */   private static void emitState(Level level, BlockPos pos, boolean attached, boolean powered, boolean wasAttached, boolean wasPowered) {
/* 204 */     if (powered && !wasPowered) {
/* 205 */       level.playSound(null, pos, SoundEvents.TRIPWIRE_CLICK_ON, SoundSource.BLOCKS, 0.4F, 0.6F);
/* 206 */       level.gameEvent(null, (Holder)GameEvent.BLOCK_ACTIVATE, pos);
/* 207 */     } else if (!powered && wasPowered) {
/* 208 */       level.playSound(null, pos, SoundEvents.TRIPWIRE_CLICK_OFF, SoundSource.BLOCKS, 0.4F, 0.5F);
/* 209 */       level.gameEvent(null, (Holder)GameEvent.BLOCK_DEACTIVATE, pos);
/* 210 */     } else if (attached && !wasAttached) {
/* 211 */       level.playSound(null, pos, SoundEvents.TRIPWIRE_ATTACH, SoundSource.BLOCKS, 0.4F, 0.7F);
/* 212 */       level.gameEvent(null, (Holder)GameEvent.BLOCK_ATTACH, pos);
/* 213 */     } else if (!attached && wasAttached) {
/* 214 */       level.playSound(null, pos, SoundEvents.TRIPWIRE_DETACH, SoundSource.BLOCKS, 0.4F, 1.2F / (level.getRandom().nextFloat() * 0.2F + 0.9F));
/* 215 */       level.gameEvent(null, (Holder)GameEvent.BLOCK_DETACH, pos);
/*     */     } 
/*     */   }
/*     */   
/*     */   private static void notifyNeighbors(Block block, Level level, BlockPos pos, Direction direction) {
/* 220 */     Direction front = direction.getOpposite();
/* 221 */     Orientation orientation = ExperimentalRedstoneUtils.initialOrientation(level, front, Direction.UP);
/* 222 */     level.updateNeighborsAt(pos, block, orientation);
/* 223 */     level.updateNeighborsAt(pos.relative(front), block, orientation);
/*     */   }
/*     */   
/*     */   protected void affectNeighborsAfterRemoval(BlockState state, ServerLevel level, BlockPos pos, boolean movedByPiston) {
/* 228 */     if (movedByPiston)
/*     */       return; 
/* 231 */     onRemoved(state, (Level)level, pos);
/*     */   }
/*     */   
/*     */   private static void onRemoved(BlockState state, Level level, BlockPos pos) {
/* 235 */     boolean attached = ((Boolean)state.getValue((Property)ATTACHED)).booleanValue();
/* 236 */     boolean powered = ((Boolean)state.getValue((Property)POWERED)).booleanValue();
/* 238 */     if (attached || powered)
/* 239 */       calculateState(level, pos, state, true, false, -1, (BlockState)null); 
/* 242 */     if (powered)
/* 243 */       notifyNeighbors(state.getBlock(), level, pos, (Direction)state.getValue((Property)FACING)); 
/*     */   }
/*     */   
/*     */   protected int ownSignal(BlockState state, BlockGetter level, BlockPos pos) {
/* 249 */     return ((Boolean)state.getValue((Property)POWERED)).booleanValue() ? 15 : 0;
/*     */   }
/*     */   
/*     */   protected int getDirectSignal(BlockState state, BlockGetter level, BlockPos pos, Direction direction) {
/* 254 */     if (!((Boolean)state.getValue((Property)POWERED)).booleanValue())
/* 255 */       return 0; 
/* 258 */     if (state.getValue((Property)FACING) == direction)
/* 259 */       return 15; 
/* 262 */     return 0;
/*     */   }
/*     */   
/*     */   protected boolean isSignalSource(BlockState state) {
/* 267 */     return true;
/*     */   }
/*     */   
/*     */   protected BlockState rotate(BlockState state, Rotation rotation) {
/* 272 */     return (BlockState)state.setValue((Property)FACING, (Comparable)rotation.rotate((Direction)state.getValue((Property)FACING)));
/*     */   }
/*     */   
/*     */   protected BlockState mirror(BlockState state, Mirror mirror) {
/* 277 */     return state.rotate(mirror.getRotation((Direction)state.getValue((Property)FACING)));
/*     */   }
/*     */   
/*     */   protected void createBlockStateDefinition(StateDefinition.Builder<Block, BlockState> builder) {
/* 282 */     builder.add(new Property[] { (Property)FACING, (Property)POWERED, (Property)ATTACHED });
/*     */   }
/*     */ }


/* Location:              C:\Users\karol\AppData\Roaming\ModrinthApp\meta\versions\26.2-0.19.5\26.2-0.19.5.jar!\net\minecraft\world\level\block\TripWireHookBlock.class
 * Java compiler version: 25 (69.0)
 * JD-Core Version:       1.1.3
 */