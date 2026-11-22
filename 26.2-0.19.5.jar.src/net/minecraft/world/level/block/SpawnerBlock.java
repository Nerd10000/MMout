/*    */ package net.minecraft.world.level.block;
/*    */ 
/*    */ import com.mojang.serialization.MapCodec;
/*    */ import java.util.function.Function;
/*    */ import net.minecraft.core.BlockPos;
/*    */ import net.minecraft.server.level.ServerLevel;
/*    */ import net.minecraft.util.RandomSource;
/*    */ import net.minecraft.world.item.ItemStack;
/*    */ import net.minecraft.world.level.Level;
/*    */ import net.minecraft.world.level.block.entity.BlockEntity;
/*    */ import net.minecraft.world.level.block.entity.BlockEntityTicker;
/*    */ import net.minecraft.world.level.block.entity.BlockEntityType;
/*    */ import net.minecraft.world.level.block.entity.BlockEntityTypes;
/*    */ import net.minecraft.world.level.block.entity.SpawnerBlockEntity;
/*    */ import net.minecraft.world.level.block.state.BlockBehaviour;
/*    */ import net.minecraft.world.level.block.state.BlockState;
/*    */ 
/*    */ public class SpawnerBlock extends BaseEntityBlock {
/* 18 */   public static final MapCodec<SpawnerBlock> CODEC = simpleCodec(SpawnerBlock::new);
/*    */   
/*    */   public MapCodec<SpawnerBlock> codec() {
/* 22 */     return CODEC;
/*    */   }
/*    */   
/*    */   protected SpawnerBlock(BlockBehaviour.Properties properties) {
/* 26 */     super(properties);
/*    */   }
/*    */   
/*    */   public BlockEntity newBlockEntity(BlockPos worldPosition, BlockState blockState) {
/* 31 */     return (BlockEntity)new SpawnerBlockEntity(worldPosition, blockState);
/*    */   }
/*    */   
/*    */   public <T extends BlockEntity> BlockEntityTicker<T> getTicker(Level level, BlockState blockState, BlockEntityType<T> type) {
/* 36 */     return createTickerHelper(type, BlockEntityTypes.MOB_SPAWNER, level.isClientSide() ? SpawnerBlockEntity::clientTick : SpawnerBlockEntity::serverTick);
/*    */   }
/*    */   
/*    */   protected void spawnAfterBreak(BlockState state, ServerLevel level, BlockPos pos, ItemStack tool, boolean dropExperience) {
/* 41 */     super.spawnAfterBreak(state, level, pos, tool, dropExperience);
/* 43 */     if (dropExperience) {
/* 44 */       RandomSource random = level.getRandom();
/* 45 */       int magicCount = 15 + random.nextInt(15) + random.nextInt(15);
/* 46 */       popExperience(level, pos, magicCount);
/*    */     } 
/*    */   }
/*    */ }


/* Location:              C:\Users\karol\AppData\Roaming\ModrinthApp\meta\versions\26.2-0.19.5\26.2-0.19.5.jar!\net\minecraft\world\level\block\SpawnerBlock.class
 * Java compiler version: 25 (69.0)
 * JD-Core Version:       1.1.3
 */