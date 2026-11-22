/*     */ package net.minecraft.world.level.block;
/*     */ 
/*     */ import com.google.common.collect.Lists;
/*     */ import com.mojang.serialization.MapCodec;
/*     */ import java.util.List;
/*     */ import java.util.Map;
/*     */ import java.util.WeakHashMap;
/*     */ import java.util.function.Function;
/*     */ import net.minecraft.core.BlockPos;
/*     */ import net.minecraft.core.Direction;
/*     */ import net.minecraft.core.particles.DustParticleOptions;
/*     */ import net.minecraft.core.particles.ParticleOptions;
/*     */ import net.minecraft.server.level.ServerLevel;
/*     */ import net.minecraft.util.RandomSource;
/*     */ import net.minecraft.world.level.BlockGetter;
/*     */ import net.minecraft.world.level.Level;
/*     */ import net.minecraft.world.level.block.state.BlockBehaviour;
/*     */ import net.minecraft.world.level.block.state.BlockState;
/*     */ import net.minecraft.world.level.block.state.StateDefinition;
/*     */ import net.minecraft.world.level.block.state.properties.BlockStateProperties;
/*     */ import net.minecraft.world.level.block.state.properties.BooleanProperty;
/*     */ import net.minecraft.world.level.block.state.properties.Property;
/*     */ import net.minecraft.world.level.redstone.ExperimentalRedstoneUtils;
/*     */ import net.minecraft.world.level.redstone.Orientation;
/*     */ 
/*     */ public class RedstoneTorchBlock extends BaseTorchBlock {
/*  26 */   public static final MapCodec<RedstoneTorchBlock> CODEC = simpleCodec(RedstoneTorchBlock::new);
/*     */   
/*     */   public MapCodec<? extends RedstoneTorchBlock> codec() {
/*  30 */     return CODEC;
/*     */   }
/*     */   
/*  33 */   public static final BooleanProperty LIT = BlockStateProperties.LIT;
/*     */   
/*  36 */   private static final Map<BlockGetter, List<Toggle>> RECENT_TOGGLES = new WeakHashMap<>();
/*     */   
/*     */   public static final int RECENT_TOGGLE_TIMER = 60;
/*     */   
/*     */   public static final int MAX_RECENT_TOGGLES = 8;
/*     */   
/*     */   public static final int RESTART_DELAY = 160;
/*     */   
/*     */   private static final int TOGGLE_DELAY = 2;
/*     */   
/*     */   protected RedstoneTorchBlock(BlockBehaviour.Properties properties) {
/*  44 */     super(properties);
/*  45 */     registerDefaultState((BlockState)((BlockState)this.stateDefinition.any()).setValue((Property)LIT, Boolean.valueOf(true)));
/*     */   }
/*     */   
/*     */   protected void onPlace(BlockState state, Level level, BlockPos pos, BlockState oldState, boolean movedByPiston) {
/*  50 */     notifyNeighbors(level, pos, state);
/*     */   }
/*     */   
/*     */   private void notifyNeighbors(Level level, BlockPos pos, BlockState state) {
/*  54 */     Orientation orientation = randomOrientation(level, state);
/*  55 */     for (Direction direction : Direction.values())
/*  56 */       level.updateNeighborsAt(pos.relative(direction), this, ExperimentalRedstoneUtils.withFront(orientation, direction)); 
/*     */   }
/*     */   
/*     */   protected void affectNeighborsAfterRemoval(BlockState state, ServerLevel level, BlockPos pos, boolean movedByPiston) {
/*  62 */     if (!movedByPiston)
/*  63 */       notifyNeighbors((Level)level, pos, state); 
/*     */   }
/*     */   
/*     */   protected boolean hasNeighborSignal(Level level, BlockPos pos, BlockState state) {
/*  68 */     return level.hasSignal(pos.below(), Direction.DOWN);
/*     */   }
/*     */   
/*     */   protected void tick(BlockState state, ServerLevel level, BlockPos pos, RandomSource random) {
/*  73 */     boolean neighborSignal = hasNeighborSignal((Level)level, pos, state);
/*  75 */     List<Toggle> toggles = RECENT_TOGGLES.get(level);
/*  76 */     while (toggles != null && !toggles.isEmpty() && level.getGameTime() - ((Toggle)toggles.get(0)).when > 60L)
/*  77 */       toggles.remove(0); 
/*  80 */     if (((Boolean)state.getValue((Property)LIT)).booleanValue()) {
/*  81 */       if (neighborSignal) {
/*  82 */         level.setBlock(pos, (BlockState)state.setValue((Property)LIT, Boolean.valueOf(false)), 3);
/*  84 */         if (isToggledTooFrequently((Level)level, pos, true)) {
/*  85 */           level.levelEvent(1502, pos, 0);
/*  86 */           level.scheduleTick(pos, level.getBlockState(pos).getBlock(), 160);
/*     */         } 
/*     */       } 
/*  90 */     } else if (!neighborSignal && !isToggledTooFrequently((Level)level, pos, false)) {
/*  91 */       level.setBlock(pos, (BlockState)state.setValue((Property)LIT, Boolean.valueOf(true)), 3);
/*     */     } 
/*     */   }
/*     */   
/*     */   protected void neighborChanged(BlockState state, Level level, BlockPos pos, Block block, Orientation orientation, boolean movedByPiston) {
/*  98 */     if (((Boolean)state.getValue((Property)LIT)).booleanValue() == hasNeighborSignal(level, pos, state) && !level.getBlockTicks().willTickThisTick(pos, this))
/*  99 */       level.scheduleTick(pos, this, 2); 
/*     */   }
/*     */   
/*     */   protected int getDirectSignal(BlockState state, BlockGetter level, BlockPos pos, Direction direction) {
/* 105 */     if (direction == Direction.DOWN)
/* 106 */       return state.getSignal(level, pos, direction); 
/* 108 */     return 0;
/*     */   }
/*     */   
/*     */   protected boolean isSignalSource(BlockState state) {
/* 113 */     return true;
/*     */   }
/*     */   
/*     */   protected int ownSignal(BlockState state, BlockGetter level, BlockPos pos) {
/* 118 */     return ((Boolean)state.getValue((Property)LIT)).booleanValue() ? 15 : 0;
/*     */   }
/*     */   
/*     */   protected int getSignal(BlockState state, BlockGetter level, BlockPos pos, Direction direction) {
/* 123 */     return (Direction.UP != direction) ? ownSignal(state, level, pos) : 0;
/*     */   }
/*     */   
/*     */   public void animateTick(BlockState state, Level level, BlockPos pos, RandomSource random) {
/* 128 */     if (!((Boolean)state.getValue((Property)LIT)).booleanValue())
/*     */       return; 
/* 132 */     double x = pos.getX() + 0.5D + (random.nextDouble() - 0.5D) * 0.2D;
/* 133 */     double y = pos.getY() + 0.7D + (random.nextDouble() - 0.5D) * 0.2D;
/* 134 */     double z = pos.getZ() + 0.5D + (random.nextDouble() - 0.5D) * 0.2D;
/* 136 */     level.addParticle((ParticleOptions)DustParticleOptions.REDSTONE, x, y, z, 0.0D, 0.0D, 0.0D);
/*     */   }
/*     */   
/*     */   protected void createBlockStateDefinition(StateDefinition.Builder<Block, BlockState> builder) {
/* 141 */     builder.add(new Property[] { (Property)LIT });
/*     */   }
/*     */   
/*     */   public static class Toggle {
/*     */     private final BlockPos pos;
/*     */     
/*     */     private final long when;
/*     */     
/*     */     public Toggle(BlockPos pos, long when) {
/* 149 */       this.pos = pos;
/* 150 */       this.when = when;
/*     */     }
/*     */   }
/*     */   
/*     */   private static boolean isToggledTooFrequently(Level level, BlockPos pos, boolean add) {
/* 155 */     List<Toggle> toggles = RECENT_TOGGLES.computeIfAbsent(level, k -> Lists.newArrayList());
/* 157 */     if (add)
/* 158 */       toggles.add(new Toggle(pos.immutable(), level.getGameTime())); 
/* 161 */     int count = 0;
/* 162 */     for (Toggle toggle : toggles) {
/* 164 */       count++;
/* 165 */       if (toggle.pos.equals(pos) && count >= 8)
/* 166 */         return true; 
/*     */     } 
/* 170 */     return false;
/*     */   }
/*     */   
/*     */   protected Orientation randomOrientation(Level level, BlockState state) {
/* 174 */     return ExperimentalRedstoneUtils.initialOrientation(level, null, Direction.UP);
/*     */   }
/*     */ }


/* Location:              C:\Users\karol\AppData\Roaming\ModrinthApp\meta\versions\26.2-0.19.5\26.2-0.19.5.jar!\net\minecraft\world\level\block\RedstoneTorchBlock.class
 * Java compiler version: 25 (69.0)
 * JD-Core Version:       1.1.3
 */