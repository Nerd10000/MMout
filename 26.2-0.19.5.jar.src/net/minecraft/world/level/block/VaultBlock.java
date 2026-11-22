/*    */ package net.minecraft.world.level.block;
/*    */ 
/*    */ import com.mojang.serialization.MapCodec;
/*    */ import java.util.function.Function;
/*    */ import net.minecraft.core.BlockPos;
/*    */ import net.minecraft.core.Direction;
/*    */ import net.minecraft.server.level.ServerLevel;
/*    */ import net.minecraft.world.InteractionHand;
/*    */ import net.minecraft.world.InteractionResult;
/*    */ import net.minecraft.world.entity.player.Player;
/*    */ import net.minecraft.world.item.ItemStack;
/*    */ import net.minecraft.world.item.context.BlockPlaceContext;
/*    */ import net.minecraft.world.level.Level;
/*    */ import net.minecraft.world.level.block.entity.BlockEntity;
/*    */ import net.minecraft.world.level.block.entity.BlockEntityTicker;
/*    */ import net.minecraft.world.level.block.entity.BlockEntityType;
/*    */ import net.minecraft.world.level.block.entity.BlockEntityTypes;
/*    */ import net.minecraft.world.level.block.entity.vault.VaultBlockEntity;
/*    */ import net.minecraft.world.level.block.entity.vault.VaultState;
/*    */ import net.minecraft.world.level.block.state.BlockBehaviour;
/*    */ import net.minecraft.world.level.block.state.BlockState;
/*    */ import net.minecraft.world.level.block.state.StateDefinition;
/*    */ import net.minecraft.world.level.block.state.properties.BlockStateProperties;
/*    */ import net.minecraft.world.level.block.state.properties.BooleanProperty;
/*    */ import net.minecraft.world.level.block.state.properties.EnumProperty;
/*    */ import net.minecraft.world.level.block.state.properties.Property;
/*    */ import net.minecraft.world.phys.BlockHitResult;
/*    */ 
/*    */ public class VaultBlock extends BaseEntityBlock {
/* 29 */   public static final MapCodec<VaultBlock> CODEC = simpleCodec(VaultBlock::new);
/*    */   
/* 30 */   public static final Property<VaultState> STATE = (Property<VaultState>)BlockStateProperties.VAULT_STATE;
/*    */   
/* 31 */   public static final EnumProperty<Direction> FACING = HorizontalDirectionalBlock.FACING;
/*    */   
/* 32 */   public static final BooleanProperty OMINOUS = BlockStateProperties.OMINOUS;
/*    */   
/*    */   public MapCodec<VaultBlock> codec() {
/* 36 */     return CODEC;
/*    */   }
/*    */   
/*    */   public VaultBlock(BlockBehaviour.Properties properties) {
/* 40 */     super(properties);
/* 41 */     registerDefaultState((BlockState)((BlockState)((BlockState)((BlockState)this.stateDefinition.any()).setValue((Property)FACING, (Comparable)Direction.NORTH)).setValue(STATE, (Comparable)VaultState.INACTIVE)).setValue((Property)OMINOUS, Boolean.valueOf(false)));
/*    */   }
/*    */   
/*    */   public InteractionResult useItemOn(ItemStack itemStack, BlockState state, Level level, BlockPos pos, Player player, InteractionHand hand, BlockHitResult hitResult) {
/* 46 */     if (itemStack.isEmpty() || state.getValue(STATE) != VaultState.ACTIVE)
/* 47 */       return (InteractionResult)InteractionResult.TRY_WITH_EMPTY_HAND; 
/* 50 */     if (level instanceof ServerLevel) {
/*    */       VaultBlockEntity vault;
/* 50 */       ServerLevel serverLevel = (ServerLevel)level;
/* 51 */       BlockEntity blockEntity = serverLevel.getBlockEntity(pos);
/* 51 */       if (blockEntity instanceof VaultBlockEntity) {
/* 51 */         vault = (VaultBlockEntity)blockEntity;
/*    */       } else {
/* 52 */         return (InteractionResult)InteractionResult.TRY_WITH_EMPTY_HAND;
/*    */       } 
/* 54 */       VaultBlockEntity.Server.tryInsertKey(serverLevel, pos, state, vault.getConfig(), vault.getServerData(), vault.getSharedData(), player, itemStack);
/*    */     } 
/* 56 */     return (InteractionResult)InteractionResult.SUCCESS_SERVER;
/*    */   }
/*    */   
/*    */   public BlockEntity newBlockEntity(BlockPos pos, BlockState state) {
/* 61 */     return (BlockEntity)new VaultBlockEntity(pos, state);
/*    */   }
/*    */   
/*    */   protected void createBlockStateDefinition(StateDefinition.Builder<Block, BlockState> builder) {
/* 66 */     builder.add(new Property[] { (Property)FACING, STATE, (Property)OMINOUS });
/*    */   }
/*    */   
/*    */   public <T extends BlockEntity> BlockEntityTicker<T> getTicker(Level level, BlockState blockState, BlockEntityType<T> type) {
/* 71 */     ServerLevel serverLevel = (ServerLevel)level;
/* 71 */     return (level instanceof ServerLevel) ? 
/* 72 */       createTickerHelper(type, BlockEntityTypes.VAULT, (innerLevel, pos, state, entity) -> VaultBlockEntity.Server.tick(serverLevel, pos, state, entity.getConfig(), entity.getServerData(), entity.getSharedData())) : 
/* 73 */       createTickerHelper(type, BlockEntityTypes.VAULT, (innerLevel, pos, state, entity) -> VaultBlockEntity.Client.tick(innerLevel, pos, state, entity.getClientData(), entity.getSharedData()));
/*    */   }
/*    */   
/*    */   public BlockState getStateForPlacement(BlockPlaceContext context) {
/* 78 */     return (BlockState)defaultBlockState().setValue((Property)FACING, (Comparable)context.getHorizontalDirection().getOpposite());
/*    */   }
/*    */   
/*    */   public BlockState rotate(BlockState state, Rotation rotation) {
/* 83 */     return (BlockState)state.setValue((Property)FACING, (Comparable)rotation.rotate((Direction)state.getValue((Property)FACING)));
/*    */   }
/*    */   
/*    */   public BlockState mirror(BlockState state, Mirror mirror) {
/* 88 */     return state.rotate(mirror.getRotation((Direction)state.getValue((Property)FACING)));
/*    */   }
/*    */ }


/* Location:              C:\Users\karol\AppData\Roaming\ModrinthApp\meta\versions\26.2-0.19.5\26.2-0.19.5.jar!\net\minecraft\world\level\block\VaultBlock.class
 * Java compiler version: 25 (69.0)
 * JD-Core Version:       1.1.3
 */