/*     */ package net.minecraft.world.level.block;
/*     */ 
/*     */ import com.mojang.datafixers.kinds.App;
/*     */ import com.mojang.datafixers.kinds.Applicative;
/*     */ import com.mojang.serialization.MapCodec;
/*     */ import com.mojang.serialization.codecs.RecordCodecBuilder;
/*     */ import java.util.Map;
/*     */ import java.util.function.BiConsumer;
/*     */ import java.util.function.BiFunction;
/*     */ import net.minecraft.core.BlockPos;
/*     */ import net.minecraft.core.Direction;
/*     */ import net.minecraft.core.Holder;
/*     */ import net.minecraft.server.level.ServerLevel;
/*     */ import net.minecraft.sounds.SoundSource;
/*     */ import net.minecraft.util.RandomSource;
/*     */ import net.minecraft.world.InteractionResult;
/*     */ import net.minecraft.world.entity.Entity;
/*     */ import net.minecraft.world.entity.player.Player;
/*     */ import net.minecraft.world.item.ItemStack;
/*     */ import net.minecraft.world.item.context.BlockPlaceContext;
/*     */ import net.minecraft.world.level.BlockGetter;
/*     */ import net.minecraft.world.level.Explosion;
/*     */ import net.minecraft.world.level.Level;
/*     */ import net.minecraft.world.level.LevelReader;
/*     */ import net.minecraft.world.level.ScheduledTickAccess;
/*     */ import net.minecraft.world.level.block.state.BlockBehaviour;
/*     */ import net.minecraft.world.level.block.state.BlockState;
/*     */ import net.minecraft.world.level.block.state.StateDefinition;
/*     */ import net.minecraft.world.level.block.state.properties.BlockSetType;
/*     */ import net.minecraft.world.level.block.state.properties.BlockStateProperties;
/*     */ import net.minecraft.world.level.block.state.properties.BooleanProperty;
/*     */ import net.minecraft.world.level.block.state.properties.EnumProperty;
/*     */ import net.minecraft.world.level.block.state.properties.Half;
/*     */ import net.minecraft.world.level.block.state.properties.Property;
/*     */ import net.minecraft.world.level.gameevent.GameEvent;
/*     */ import net.minecraft.world.level.material.Fluid;
/*     */ import net.minecraft.world.level.material.FluidState;
/*     */ import net.minecraft.world.level.material.Fluids;
/*     */ import net.minecraft.world.level.pathfinder.PathComputationType;
/*     */ import net.minecraft.world.level.redstone.Orientation;
/*     */ import net.minecraft.world.phys.BlockHitResult;
/*     */ import net.minecraft.world.phys.shapes.CollisionContext;
/*     */ import net.minecraft.world.phys.shapes.Shapes;
/*     */ import net.minecraft.world.phys.shapes.VoxelShape;
/*     */ 
/*     */ public class TrapDoorBlock extends HorizontalDirectionalBlock implements SimpleWaterloggedBlock {
/*     */   public static final MapCodec<TrapDoorBlock> CODEC;
/*     */   
/*     */   static {
/*  41 */     CODEC = RecordCodecBuilder.mapCodec(i -> i.group((App)BlockSetType.CODEC.fieldOf("block_set_type").forGetter(()), (App)propertiesCodec()).apply((Applicative)i, TrapDoorBlock::new));
/*     */   }
/*     */   
/*     */   public MapCodec<? extends TrapDoorBlock> codec() {
/*  48 */     return CODEC;
/*     */   }
/*     */   
/*  51 */   public static final BooleanProperty OPEN = BlockStateProperties.OPEN;
/*     */   
/*  52 */   public static final EnumProperty<Half> HALF = BlockStateProperties.HALF;
/*     */   
/*  53 */   public static final BooleanProperty POWERED = BlockStateProperties.POWERED;
/*     */   
/*  54 */   public static final BooleanProperty WATERLOGGED = BlockStateProperties.WATERLOGGED;
/*     */   
/*  56 */   private static final Map<Direction, VoxelShape> SHAPES = Shapes.rotateAll(Block.boxZ(16.0D, 13.0D, 16.0D));
/*     */   
/*     */   private final BlockSetType type;
/*     */   
/*     */   protected TrapDoorBlock(BlockSetType type, BlockBehaviour.Properties properties) {
/*  61 */     super(properties.sound(type.soundType()));
/*  62 */     this.type = type;
/*  63 */     registerDefaultState((BlockState)((BlockState)((BlockState)((BlockState)((BlockState)((BlockState)this.stateDefinition.any()).setValue((Property)FACING, (Comparable)Direction.NORTH)).setValue((Property)OPEN, Boolean.valueOf(false))).setValue((Property)HALF, (Comparable)Half.BOTTOM)).setValue((Property)POWERED, Boolean.valueOf(false))).setValue((Property)WATERLOGGED, Boolean.valueOf(false)));
/*     */   }
/*     */   
/*     */   protected VoxelShape getShape(BlockState state, BlockGetter level, BlockPos pos, CollisionContext context) {
/*  68 */     return SHAPES.get(((Boolean)state.getValue((Property)OPEN)).booleanValue() ? 
/*  69 */         state.getValue((Property)FACING) : (
/*  70 */         (state.getValue((Property)HALF) == Half.TOP) ? Direction.DOWN : Direction.UP));
/*     */   }
/*     */   
/*     */   protected boolean isPathfindable(BlockState state, PathComputationType type) {
/*  76 */     switch (type) {
/*     */       case LAND:
/*     */       
/*     */       case WATER:
/*     */       
/*     */       case AIR:
/*     */       
/*     */     } 
/*  76 */     return false;
/*     */   }
/*     */   
/*     */   protected InteractionResult useWithoutItem(BlockState state, Level level, BlockPos pos, Player player, BlockHitResult hitResult) {
/*  86 */     if (!this.type.canOpenByHand())
/*  87 */       return (InteractionResult)InteractionResult.PASS; 
/*  90 */     toggle(state, level, pos, player);
/*  91 */     return (InteractionResult)InteractionResult.SUCCESS;
/*     */   }
/*     */   
/*     */   protected void onExplosionHit(BlockState state, ServerLevel level, BlockPos pos, Explosion explosion, BiConsumer<ItemStack, BlockPos> onHit) {
/*  96 */     if (explosion.canTriggerBlocks() && this.type.canOpenByWindCharge() && !((Boolean)state.getValue((Property)POWERED)).booleanValue())
/*  97 */       toggle(state, (Level)level, pos, (Player)null); 
/*  99 */     super.onExplosionHit(state, level, pos, explosion, onHit);
/*     */   }
/*     */   
/*     */   private void toggle(BlockState state, Level level, BlockPos pos, Player player) {
/* 103 */     BlockState updated = (BlockState)state.cycle((Property)OPEN);
/* 104 */     level.setBlock(pos, updated, 2);
/* 106 */     if (((Boolean)updated.getValue((Property)WATERLOGGED)).booleanValue())
/* 107 */       level.scheduleTick(pos, (Fluid)Fluids.WATER, Fluids.WATER.getTickDelay((LevelReader)level)); 
/* 110 */     playSound(player, level, pos, ((Boolean)updated.getValue((Property)OPEN)).booleanValue());
/*     */   }
/*     */   
/*     */   protected void playSound(Player player, Level level, BlockPos pos, boolean opening) {
/* 114 */     level.playSound((Entity)player, pos, opening ? this.type.trapdoorOpen() : this.type.trapdoorClose(), SoundSource.BLOCKS, 1.0F, level.getRandom().nextFloat() * 0.1F + 0.9F);
/* 115 */     level.gameEvent((Entity)player, opening ? (Holder)GameEvent.BLOCK_OPEN : (Holder)GameEvent.BLOCK_CLOSE, pos);
/*     */   }
/*     */   
/*     */   protected void neighborChanged(BlockState state, Level level, BlockPos pos, Block block, Orientation orientation, boolean movedByPiston) {
/* 120 */     if (level.isClientSide())
/*     */       return; 
/* 124 */     boolean signal = level.hasNeighborSignal(pos);
/* 125 */     if (signal != ((Boolean)state.getValue((Property)POWERED)).booleanValue()) {
/* 126 */       if (((Boolean)state.getValue((Property)OPEN)).booleanValue() != signal) {
/* 127 */         state = (BlockState)state.setValue((Property)OPEN, Boolean.valueOf(signal));
/* 128 */         playSound((Player)null, level, pos, signal);
/*     */       } 
/* 130 */       level.setBlock(pos, (BlockState)state.setValue((Property)POWERED, Boolean.valueOf(signal)), 2);
/* 132 */       if (((Boolean)state.getValue((Property)WATERLOGGED)).booleanValue())
/* 133 */         level.scheduleTick(pos, (Fluid)Fluids.WATER, Fluids.WATER.getTickDelay((LevelReader)level)); 
/*     */     } 
/*     */   }
/*     */   
/*     */   public BlockState getStateForPlacement(BlockPlaceContext context) {
/* 140 */     BlockState state = defaultBlockState();
/* 141 */     FluidState replacedFluidState = context.getLevel().getFluidState(context.getClickedPos());
/* 143 */     Direction clickedFace = context.getClickedFace();
/* 144 */     if (context.replacingClickedOnBlock() || !clickedFace.getAxis().isHorizontal()) {
/* 145 */       state = (BlockState)((BlockState)state.setValue((Property)FACING, (Comparable)context.getHorizontalDirection().getOpposite())).setValue((Property)HALF, (clickedFace == Direction.UP) ? (Comparable)Half.BOTTOM : (Comparable)Half.TOP);
/*     */     } else {
/* 147 */       state = (BlockState)((BlockState)state.setValue((Property)FACING, (Comparable)clickedFace)).setValue((Property)HALF, ((context.getClickLocation()).y - context.getClickedPos().getY() > 0.5D) ? (Comparable)Half.TOP : (Comparable)Half.BOTTOM);
/*     */     } 
/* 149 */     if (context.getLevel().hasNeighborSignal(context.getClickedPos()))
/* 150 */       state = (BlockState)((BlockState)state.setValue((Property)OPEN, Boolean.valueOf(true))).setValue((Property)POWERED, Boolean.valueOf(true)); 
/* 152 */     return (BlockState)state.setValue((Property)WATERLOGGED, Boolean.valueOf(replacedFluidState.is(Fluids.WATER)));
/*     */   }
/*     */   
/*     */   protected void createBlockStateDefinition(StateDefinition.Builder<Block, BlockState> builder) {
/* 157 */     builder.add(new Property[] { (Property)FACING, (Property)OPEN, (Property)HALF, (Property)POWERED, (Property)WATERLOGGED });
/*     */   }
/*     */   
/*     */   protected FluidState getFluidState(BlockState state) {
/* 162 */     if (((Boolean)state.getValue((Property)WATERLOGGED)).booleanValue())
/* 163 */       return Fluids.WATER.getSource(false); 
/* 165 */     return super.getFluidState(state);
/*     */   }
/*     */   
/*     */   protected BlockState updateShape(BlockState state, LevelReader level, ScheduledTickAccess ticks, BlockPos pos, Direction directionToNeighbour, BlockPos neighbourPos, BlockState neighbourState, RandomSource random) {
/* 170 */     if (((Boolean)state.getValue((Property)WATERLOGGED)).booleanValue())
/* 171 */       ticks.scheduleTick(pos, (Fluid)Fluids.WATER, Fluids.WATER.getTickDelay(level)); 
/* 174 */     return super.updateShape(state, level, ticks, pos, directionToNeighbour, neighbourPos, neighbourState, random);
/*     */   }
/*     */   
/*     */   protected BlockSetType getType() {
/* 178 */     return this.type;
/*     */   }
/*     */ }


/* Location:              C:\Users\karol\AppData\Roaming\ModrinthApp\meta\versions\26.2-0.19.5\26.2-0.19.5.jar!\net\minecraft\world\level\block\TrapDoorBlock.class
 * Java compiler version: 25 (69.0)
 * JD-Core Version:       1.1.3
 */