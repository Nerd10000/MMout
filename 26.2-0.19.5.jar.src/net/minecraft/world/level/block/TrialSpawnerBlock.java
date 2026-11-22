/*    */ package net.minecraft.world.level.block;
/*    */ 
/*    */ import com.mojang.serialization.MapCodec;
/*    */ import java.util.function.Function;
/*    */ import net.minecraft.core.BlockPos;
/*    */ import net.minecraft.server.level.ServerLevel;
/*    */ import net.minecraft.world.level.Level;
/*    */ import net.minecraft.world.level.block.entity.BlockEntity;
/*    */ import net.minecraft.world.level.block.entity.BlockEntityTicker;
/*    */ import net.minecraft.world.level.block.entity.BlockEntityType;
/*    */ import net.minecraft.world.level.block.entity.BlockEntityTypes;
/*    */ import net.minecraft.world.level.block.entity.TrialSpawnerBlockEntity;
/*    */ import net.minecraft.world.level.block.entity.trialspawner.TrialSpawnerState;
/*    */ import net.minecraft.world.level.block.state.BlockBehaviour;
/*    */ import net.minecraft.world.level.block.state.BlockState;
/*    */ import net.minecraft.world.level.block.state.StateDefinition;
/*    */ import net.minecraft.world.level.block.state.properties.BlockStateProperties;
/*    */ import net.minecraft.world.level.block.state.properties.BooleanProperty;
/*    */ import net.minecraft.world.level.block.state.properties.EnumProperty;
/*    */ import net.minecraft.world.level.block.state.properties.Property;
/*    */ 
/*    */ public class TrialSpawnerBlock extends BaseEntityBlock {
/* 21 */   public static final MapCodec<TrialSpawnerBlock> CODEC = simpleCodec(TrialSpawnerBlock::new);
/*    */   
/*    */   public MapCodec<TrialSpawnerBlock> codec() {
/* 25 */     return CODEC;
/*    */   }
/*    */   
/* 28 */   public static final EnumProperty<TrialSpawnerState> STATE = BlockStateProperties.TRIAL_SPAWNER_STATE;
/*    */   
/* 29 */   public static final BooleanProperty OMINOUS = BlockStateProperties.OMINOUS;
/*    */   
/*    */   public TrialSpawnerBlock(BlockBehaviour.Properties properties) {
/* 32 */     super(properties);
/* 33 */     registerDefaultState((BlockState)((BlockState)((BlockState)this.stateDefinition.any()).setValue((Property)STATE, (Comparable)TrialSpawnerState.INACTIVE)).setValue((Property)OMINOUS, Boolean.valueOf(false)));
/*    */   }
/*    */   
/*    */   protected void createBlockStateDefinition(StateDefinition.Builder<Block, BlockState> builder) {
/* 38 */     builder.add(new Property[] { (Property)STATE, (Property)OMINOUS });
/*    */   }
/*    */   
/*    */   public BlockEntity newBlockEntity(BlockPos worldPosition, BlockState blockState) {
/* 43 */     return (BlockEntity)new TrialSpawnerBlockEntity(worldPosition, blockState);
/*    */   }
/*    */   
/*    */   public <T extends BlockEntity> BlockEntityTicker<T> getTicker(Level level, BlockState blockState, BlockEntityType<T> type) {
/* 48 */     ServerLevel serverLevel = (ServerLevel)level;
/* 48 */     return (level instanceof ServerLevel) ? 
/* 49 */       createTickerHelper(type, BlockEntityTypes.TRIAL_SPAWNER, (innerLevel, pos, state, entity) -> entity.getTrialSpawner().tickServer(serverLevel, pos, ((Boolean)state.getOptionalValue((Property)BlockStateProperties.OMINOUS).orElse(Boolean.valueOf(false))).booleanValue())) : 
/* 50 */       createTickerHelper(type, BlockEntityTypes.TRIAL_SPAWNER, (innerLevel, pos, state, entity) -> entity.getTrialSpawner().tickClient(innerLevel, pos, ((Boolean)state.getOptionalValue((Property)BlockStateProperties.OMINOUS).orElse(Boolean.valueOf(false))).booleanValue()));
/*    */   }
/*    */ }


/* Location:              C:\Users\karol\AppData\Roaming\ModrinthApp\meta\versions\26.2-0.19.5\26.2-0.19.5.jar!\net\minecraft\world\level\block\TrialSpawnerBlock.class
 * Java compiler version: 25 (69.0)
 * JD-Core Version:       1.1.3
 */