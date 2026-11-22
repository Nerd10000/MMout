/*    */ package net.minecraft.world.level.block;
/*    */ 
/*    */ import com.mojang.serialization.MapCodec;
/*    */ import net.minecraft.core.BlockPos;
/*    */ import net.minecraft.world.item.context.BlockPlaceContext;
/*    */ import net.minecraft.world.level.Level;
/*    */ import net.minecraft.world.level.block.entity.BlockEntity;
/*    */ import net.minecraft.world.level.block.entity.BlockEntityTicker;
/*    */ import net.minecraft.world.level.block.entity.BlockEntityType;
/*    */ import net.minecraft.world.level.block.entity.BlockEntityTypes;
/*    */ import net.minecraft.world.level.block.entity.SkullBlockEntity;
/*    */ import net.minecraft.world.level.block.state.BlockBehaviour;
/*    */ import net.minecraft.world.level.block.state.BlockState;
/*    */ import net.minecraft.world.level.block.state.StateDefinition;
/*    */ import net.minecraft.world.level.block.state.properties.BlockStateProperties;
/*    */ import net.minecraft.world.level.block.state.properties.BooleanProperty;
/*    */ import net.minecraft.world.level.block.state.properties.Property;
/*    */ import net.minecraft.world.level.pathfinder.PathComputationType;
/*    */ import net.minecraft.world.level.redstone.Orientation;
/*    */ 
/*    */ public abstract class AbstractSkullBlock extends BaseEntityBlock {
/* 22 */   public static final BooleanProperty POWERED = BlockStateProperties.POWERED;
/*    */   
/*    */   private final SkullBlock.Type type;
/*    */   
/*    */   public AbstractSkullBlock(SkullBlock.Type type, BlockBehaviour.Properties properties) {
/* 26 */     super(properties);
/* 27 */     this.type = type;
/* 28 */     registerDefaultState((BlockState)((BlockState)this.stateDefinition.any()).setValue((Property)POWERED, Boolean.valueOf(false)));
/*    */   }
/*    */   
/*    */   protected abstract MapCodec<? extends AbstractSkullBlock> codec();
/*    */   
/*    */   public BlockEntity newBlockEntity(BlockPos worldPosition, BlockState blockState) {
/* 36 */     return (BlockEntity)new SkullBlockEntity(worldPosition, blockState);
/*    */   }
/*    */   
/*    */   public <T extends BlockEntity> BlockEntityTicker<T> getTicker(Level level, BlockState blockState, BlockEntityType<T> type) {
/* 41 */     if (level.isClientSide()) {
/* 45 */       boolean isAnimated = (blockState.is(Blocks.DRAGON_HEAD) || blockState.is(Blocks.DRAGON_WALL_HEAD) || blockState.is(Blocks.PIGLIN_HEAD) || blockState.is(Blocks.PIGLIN_WALL_HEAD));
/* 47 */       if (isAnimated)
/* 48 */         return createTickerHelper(type, BlockEntityTypes.SKULL, SkullBlockEntity::animation); 
/*    */     } 
/* 51 */     return null;
/*    */   }
/*    */   
/*    */   public SkullBlock.Type getType() {
/* 55 */     return this.type;
/*    */   }
/*    */   
/*    */   protected boolean isPathfindable(BlockState state, PathComputationType type) {
/* 60 */     return false;
/*    */   }
/*    */   
/*    */   protected void createBlockStateDefinition(StateDefinition.Builder<Block, BlockState> builder) {
/* 65 */     builder.add(new Property[] { (Property)POWERED });
/*    */   }
/*    */   
/*    */   public BlockState getStateForPlacement(BlockPlaceContext context) {
/* 70 */     return (BlockState)defaultBlockState()
/* 71 */       .setValue((Property)POWERED, Boolean.valueOf(context.getLevel().hasNeighborSignal(context.getClickedPos())));
/*    */   }
/*    */   
/*    */   protected void neighborChanged(BlockState state, Level level, BlockPos pos, Block block, Orientation orientation, boolean movedByPiston) {
/* 76 */     if (level.isClientSide())
/*    */       return; 
/* 80 */     boolean signal = level.hasNeighborSignal(pos);
/* 81 */     if (signal != ((Boolean)state.getValue((Property)POWERED)).booleanValue())
/* 82 */       level.setBlock(pos, (BlockState)state.setValue((Property)POWERED, Boolean.valueOf(signal)), 2); 
/*    */   }
/*    */ }


/* Location:              C:\Users\karol\AppData\Roaming\ModrinthApp\meta\versions\26.2-0.19.5\26.2-0.19.5.jar!\net\minecraft\world\level\block\AbstractSkullBlock.class
 * Java compiler version: 25 (69.0)
 * JD-Core Version:       1.1.3
 */