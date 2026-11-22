/*    */ package net.minecraft.world.level.block;
/*    */ 
/*    */ import com.mojang.serialization.MapCodec;
/*    */ import java.util.function.Function;
/*    */ import net.minecraft.core.BlockPos;
/*    */ import net.minecraft.server.level.ServerLevel;
/*    */ import net.minecraft.util.RandomSource;
/*    */ import net.minecraft.util.valueproviders.ConstantInt;
/*    */ import net.minecraft.util.valueproviders.IntProvider;
/*    */ import net.minecraft.world.item.ItemStack;
/*    */ import net.minecraft.world.level.Level;
/*    */ import net.minecraft.world.level.block.entity.BlockEntity;
/*    */ import net.minecraft.world.level.block.entity.BlockEntityTicker;
/*    */ import net.minecraft.world.level.block.entity.BlockEntityType;
/*    */ import net.minecraft.world.level.block.entity.BlockEntityTypes;
/*    */ import net.minecraft.world.level.block.entity.SculkCatalystBlockEntity;
/*    */ import net.minecraft.world.level.block.state.BlockBehaviour;
/*    */ import net.minecraft.world.level.block.state.BlockState;
/*    */ import net.minecraft.world.level.block.state.StateDefinition;
/*    */ import net.minecraft.world.level.block.state.properties.BlockStateProperties;
/*    */ import net.minecraft.world.level.block.state.properties.BooleanProperty;
/*    */ import net.minecraft.world.level.block.state.properties.Property;
/*    */ 
/*    */ public class SculkCatalystBlock extends BaseEntityBlock {
/* 23 */   public static final MapCodec<SculkCatalystBlock> CODEC = simpleCodec(SculkCatalystBlock::new);
/*    */   
/*    */   public MapCodec<SculkCatalystBlock> codec() {
/* 27 */     return CODEC;
/*    */   }
/*    */   
/* 30 */   public static final BooleanProperty PULSE = BlockStateProperties.BLOOM;
/*    */   
/* 31 */   private final IntProvider xpRange = (IntProvider)ConstantInt.of(5);
/*    */   
/*    */   public SculkCatalystBlock(BlockBehaviour.Properties properties) {
/* 34 */     super(properties);
/* 36 */     registerDefaultState((BlockState)((BlockState)this.stateDefinition.any()).setValue((Property)PULSE, Boolean.valueOf(false)));
/*    */   }
/*    */   
/*    */   protected void createBlockStateDefinition(StateDefinition.Builder<Block, BlockState> builder) {
/* 41 */     builder.add(new Property[] { (Property)PULSE });
/*    */   }
/*    */   
/*    */   protected void tick(BlockState state, ServerLevel level, BlockPos pos, RandomSource random) {
/* 46 */     if (((Boolean)state.getValue((Property)PULSE)).booleanValue())
/* 47 */       level.setBlock(pos, (BlockState)state.setValue((Property)PULSE, Boolean.valueOf(false)), 3); 
/*    */   }
/*    */   
/*    */   public BlockEntity newBlockEntity(BlockPos worldPosition, BlockState blockState) {
/* 53 */     return (BlockEntity)new SculkCatalystBlockEntity(worldPosition, blockState);
/*    */   }
/*    */   
/*    */   public <T extends BlockEntity> BlockEntityTicker<T> getTicker(Level level, BlockState blockState, BlockEntityType<T> type) {
/* 58 */     if (level.isClientSide())
/* 59 */       return null; 
/* 61 */     return createTickerHelper(type, BlockEntityTypes.SCULK_CATALYST, SculkCatalystBlockEntity::serverTick);
/*    */   }
/*    */   
/*    */   protected void spawnAfterBreak(BlockState state, ServerLevel level, BlockPos pos, ItemStack tool, boolean dropExperience) {
/* 66 */     super.spawnAfterBreak(state, level, pos, tool, dropExperience);
/* 67 */     if (dropExperience)
/* 68 */       tryDropExperience(level, pos, tool, this.xpRange); 
/*    */   }
/*    */ }


/* Location:              C:\Users\karol\AppData\Roaming\ModrinthApp\meta\versions\26.2-0.19.5\26.2-0.19.5.jar!\net\minecraft\world\level\block\SculkCatalystBlock.class
 * Java compiler version: 25 (69.0)
 * JD-Core Version:       1.1.3
 */