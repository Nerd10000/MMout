/*    */ package net.minecraft.world.level.block;
/*    */ 
/*    */ import com.mojang.serialization.MapCodec;
/*    */ import net.minecraft.core.BlockPos;
/*    */ import net.minecraft.world.item.DyeColor;
/*    */ import net.minecraft.world.item.ItemStack;
/*    */ import net.minecraft.world.level.LevelReader;
/*    */ import net.minecraft.world.level.block.entity.BannerBlockEntity;
/*    */ import net.minecraft.world.level.block.entity.BlockEntity;
/*    */ import net.minecraft.world.level.block.state.BlockBehaviour;
/*    */ import net.minecraft.world.level.block.state.BlockState;
/*    */ 
/*    */ public abstract class AbstractBannerBlock extends BaseEntityBlock {
/*    */   private final DyeColor color;
/*    */   
/*    */   protected AbstractBannerBlock(DyeColor color, BlockBehaviour.Properties properties) {
/* 16 */     super(properties);
/* 17 */     this.color = color;
/*    */   }
/*    */   
/*    */   protected abstract MapCodec<? extends AbstractBannerBlock> codec();
/*    */   
/*    */   public boolean isPossibleToRespawnInThis(BlockState state) {
/* 25 */     return true;
/*    */   }
/*    */   
/*    */   public BlockEntity newBlockEntity(BlockPos worldPosition, BlockState blockState) {
/* 30 */     return (BlockEntity)new BannerBlockEntity(worldPosition, blockState, this.color);
/*    */   }
/*    */   
/*    */   protected ItemStack getCloneItemStack(LevelReader level, BlockPos pos, BlockState state, boolean includeData) {
/* 35 */     BlockEntity blockEntity = level.getBlockEntity(pos);
/* 35 */     if (blockEntity instanceof BannerBlockEntity) {
/* 35 */       BannerBlockEntity banner = (BannerBlockEntity)blockEntity;
/* 36 */       return banner.getItem();
/*    */     } 
/* 39 */     return super.getCloneItemStack(level, pos, state, includeData);
/*    */   }
/*    */   
/*    */   public DyeColor getColor() {
/* 43 */     return this.color;
/*    */   }
/*    */ }


/* Location:              C:\Users\karol\AppData\Roaming\ModrinthApp\meta\versions\26.2-0.19.5\26.2-0.19.5.jar!\net\minecraft\world\level\block\AbstractBannerBlock.class
 * Java compiler version: 25 (69.0)
 * JD-Core Version:       1.1.3
 */