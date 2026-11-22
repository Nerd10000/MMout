/*     */ package net.minecraft.world.level.block;
/*     */ 
/*     */ import com.mojang.serialization.MapCodec;
/*     */ import java.util.function.Function;
/*     */ import net.minecraft.core.BlockPos;
/*     */ import net.minecraft.core.Direction;
/*     */ import net.minecraft.core.component.DataComponents;
/*     */ import net.minecraft.server.level.ServerLevel;
/*     */ import net.minecraft.world.Containers;
/*     */ import net.minecraft.world.InteractionHand;
/*     */ import net.minecraft.world.InteractionResult;
/*     */ import net.minecraft.world.entity.LivingEntity;
/*     */ import net.minecraft.world.entity.player.Player;
/*     */ import net.minecraft.world.item.ItemStack;
/*     */ import net.minecraft.world.item.JukeboxPlayable;
/*     */ import net.minecraft.world.item.component.TypedEntityData;
/*     */ import net.minecraft.world.level.BlockGetter;
/*     */ import net.minecraft.world.level.Level;
/*     */ import net.minecraft.world.level.block.entity.BlockEntity;
/*     */ import net.minecraft.world.level.block.entity.BlockEntityTicker;
/*     */ import net.minecraft.world.level.block.entity.BlockEntityType;
/*     */ import net.minecraft.world.level.block.entity.BlockEntityTypes;
/*     */ import net.minecraft.world.level.block.entity.JukeboxBlockEntity;
/*     */ import net.minecraft.world.level.block.state.BlockBehaviour;
/*     */ import net.minecraft.world.level.block.state.BlockState;
/*     */ import net.minecraft.world.level.block.state.StateDefinition;
/*     */ import net.minecraft.world.level.block.state.properties.BlockStateProperties;
/*     */ import net.minecraft.world.level.block.state.properties.BooleanProperty;
/*     */ import net.minecraft.world.level.block.state.properties.Property;
/*     */ import net.minecraft.world.phys.BlockHitResult;
/*     */ 
/*     */ public class JukeboxBlock extends BaseEntityBlock {
/*  32 */   public static final MapCodec<JukeboxBlock> CODEC = simpleCodec(JukeboxBlock::new);
/*     */   
/*     */   public MapCodec<JukeboxBlock> codec() {
/*  36 */     return CODEC;
/*     */   }
/*     */   
/*  39 */   public static final BooleanProperty HAS_RECORD = BlockStateProperties.HAS_RECORD;
/*     */   
/*     */   protected JukeboxBlock(BlockBehaviour.Properties properties) {
/*  42 */     super(properties);
/*  43 */     registerDefaultState((BlockState)((BlockState)this.stateDefinition.any()).setValue((Property)HAS_RECORD, Boolean.valueOf(false)));
/*     */   }
/*     */   
/*     */   public void setPlacedBy(Level level, BlockPos pos, BlockState state, LivingEntity by, ItemStack itemStack) {
/*  48 */     super.setPlacedBy(level, pos, state, by, itemStack);
/*  49 */     TypedEntityData<BlockEntityType<?>> blockEntityData = (TypedEntityData<BlockEntityType<?>>)itemStack.get(DataComponents.BLOCK_ENTITY_DATA);
/*  50 */     if (blockEntityData != null && blockEntityData.contains("RecordItem"))
/*  51 */       level.setBlock(pos, (BlockState)state.setValue((Property)HAS_RECORD, Boolean.valueOf(true)), 2); 
/*     */   }
/*     */   
/*     */   protected InteractionResult useWithoutItem(BlockState state, Level level, BlockPos pos, Player player, BlockHitResult hitResult) {
/*  57 */     if (((Boolean)state.getValue((Property)HAS_RECORD)).booleanValue()) {
/*  57 */       BlockEntity blockEntity = level.getBlockEntity(pos);
/*  57 */       if (blockEntity instanceof JukeboxBlockEntity) {
/*  57 */         JukeboxBlockEntity jukebox = (JukeboxBlockEntity)blockEntity;
/*  58 */         jukebox.popOutTheItem();
/*  59 */         return (InteractionResult)InteractionResult.SUCCESS;
/*     */       } 
/*     */     } 
/*  62 */     return (InteractionResult)InteractionResult.PASS;
/*     */   }
/*     */   
/*     */   protected InteractionResult useItemOn(ItemStack itemStack, BlockState state, Level level, BlockPos pos, Player player, InteractionHand hand, BlockHitResult hitResult) {
/*  67 */     if (((Boolean)state.getValue((Property)HAS_RECORD)).booleanValue())
/*  68 */       return (InteractionResult)InteractionResult.TRY_WITH_EMPTY_HAND; 
/*  71 */     ItemStack toInsert = player.getItemInHand(hand);
/*  72 */     InteractionResult result = JukeboxPlayable.tryInsertIntoJukebox(level, pos, toInsert, player);
/*  74 */     if (!result.consumesAction())
/*  75 */       return (InteractionResult)InteractionResult.TRY_WITH_EMPTY_HAND; 
/*  78 */     return result;
/*     */   }
/*     */   
/*     */   protected void affectNeighborsAfterRemoval(BlockState state, ServerLevel level, BlockPos pos, boolean movedByPiston) {
/*  83 */     Containers.updateNeighboursAfterDestroy(state, (Level)level, pos);
/*     */   }
/*     */   
/*     */   public BlockEntity newBlockEntity(BlockPos worldPosition, BlockState blockState) {
/*  88 */     return (BlockEntity)new JukeboxBlockEntity(worldPosition, blockState);
/*     */   }
/*     */   
/*     */   public boolean isSignalSource(BlockState state) {
/*  93 */     return true;
/*     */   }
/*     */   
/*     */   protected int ownSignal(BlockState state, BlockGetter level, BlockPos pos) {
/*  98 */     BlockEntity blockEntity = level.getBlockEntity(pos);
/*  98 */     if (blockEntity instanceof JukeboxBlockEntity) {
/*  98 */       JukeboxBlockEntity jukebox = (JukeboxBlockEntity)blockEntity;
/*  98 */       if (jukebox.getSongPlayer().isPlaying())
/*  99 */         return 15; 
/*     */     } 
/* 101 */     return 0;
/*     */   }
/*     */   
/*     */   protected boolean hasAnalogOutputSignal(BlockState state) {
/* 106 */     return true;
/*     */   }
/*     */   
/*     */   protected int getAnalogOutputSignal(BlockState state, Level level, BlockPos pos, Direction direction) {
/* 111 */     BlockEntity blockEntity = level.getBlockEntity(pos);
/* 111 */     if (blockEntity instanceof JukeboxBlockEntity) {
/* 111 */       JukeboxBlockEntity jukebox = (JukeboxBlockEntity)blockEntity;
/* 112 */       return jukebox.getComparatorOutput();
/*     */     } 
/* 115 */     return 0;
/*     */   }
/*     */   
/*     */   protected void createBlockStateDefinition(StateDefinition.Builder<Block, BlockState> builder) {
/* 120 */     builder.add(new Property[] { (Property)HAS_RECORD });
/*     */   }
/*     */   
/*     */   public <T extends BlockEntity> BlockEntityTicker<T> getTicker(Level level, BlockState blockState, BlockEntityType<T> type) {
/* 125 */     if (((Boolean)blockState.getValue((Property)HAS_RECORD)).booleanValue())
/* 126 */       return createTickerHelper(type, BlockEntityTypes.JUKEBOX, JukeboxBlockEntity::tick); 
/* 128 */     return null;
/*     */   }
/*     */ }


/* Location:              C:\Users\karol\AppData\Roaming\ModrinthApp\meta\versions\26.2-0.19.5\26.2-0.19.5.jar!\net\minecraft\world\level\block\JukeboxBlock.class
 * Java compiler version: 25 (69.0)
 * JD-Core Version:       1.1.3
 */