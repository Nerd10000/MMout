/*    */ package net.minecraft.world.level.block;
/*    */ 
/*    */ import com.mojang.serialization.MapCodec;
/*    */ import java.util.function.Function;
/*    */ import net.minecraft.core.BlockPos;
/*    */ import net.minecraft.stats.Stats;
/*    */ import net.minecraft.world.InteractionResult;
/*    */ import net.minecraft.world.MenuProvider;
/*    */ import net.minecraft.world.entity.player.Player;
/*    */ import net.minecraft.world.item.DyeColor;
/*    */ import net.minecraft.world.level.Level;
/*    */ import net.minecraft.world.level.block.entity.BeaconBlockEntity;
/*    */ import net.minecraft.world.level.block.entity.BlockEntity;
/*    */ import net.minecraft.world.level.block.entity.BlockEntityTicker;
/*    */ import net.minecraft.world.level.block.entity.BlockEntityType;
/*    */ import net.minecraft.world.level.block.entity.BlockEntityTypes;
/*    */ import net.minecraft.world.level.block.state.BlockBehaviour;
/*    */ import net.minecraft.world.level.block.state.BlockState;
/*    */ import net.minecraft.world.phys.BlockHitResult;
/*    */ 
/*    */ public class BeaconBlock extends BaseEntityBlock implements BeaconBeamBlock {
/* 20 */   public static final MapCodec<BeaconBlock> CODEC = simpleCodec(BeaconBlock::new);
/*    */   
/*    */   public MapCodec<BeaconBlock> codec() {
/* 24 */     return CODEC;
/*    */   }
/*    */   
/*    */   public BeaconBlock(BlockBehaviour.Properties properties) {
/* 28 */     super(properties);
/*    */   }
/*    */   
/*    */   public DyeColor getColor() {
/* 33 */     return DyeColor.WHITE;
/*    */   }
/*    */   
/*    */   public BlockEntity newBlockEntity(BlockPos worldPosition, BlockState blockState) {
/* 38 */     return (BlockEntity)new BeaconBlockEntity(worldPosition, blockState);
/*    */   }
/*    */   
/*    */   public <T extends BlockEntity> BlockEntityTicker<T> getTicker(Level level, BlockState blockState, BlockEntityType<T> type) {
/* 43 */     return createTickerHelper(type, BlockEntityTypes.BEACON, BeaconBlockEntity::tick);
/*    */   }
/*    */   
/*    */   protected InteractionResult useWithoutItem(BlockState state, Level level, BlockPos pos, Player player, BlockHitResult hitResult) {
/* 48 */     if (!level.isClientSide()) {
/* 48 */       BlockEntity blockEntity = level.getBlockEntity(pos);
/* 48 */       if (blockEntity instanceof BeaconBlockEntity) {
/* 48 */         BeaconBlockEntity beacon = (BeaconBlockEntity)blockEntity;
/* 49 */         player.openMenu((MenuProvider)beacon);
/* 50 */         player.awardStat(Stats.INTERACT_WITH_BEACON);
/*    */       } 
/*    */     } 
/* 52 */     return (InteractionResult)InteractionResult.SUCCESS;
/*    */   }
/*    */ }


/* Location:              C:\Users\karol\AppData\Roaming\ModrinthApp\meta\versions\26.2-0.19.5\26.2-0.19.5.jar!\net\minecraft\world\level\block\BeaconBlock.class
 * Java compiler version: 25 (69.0)
 * JD-Core Version:       1.1.3
 */