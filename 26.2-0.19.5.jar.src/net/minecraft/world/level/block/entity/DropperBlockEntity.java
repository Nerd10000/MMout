/*    */ package net.minecraft.world.level.block.entity;
/*    */ 
/*    */ import net.minecraft.core.BlockPos;
/*    */ import net.minecraft.network.chat.Component;
/*    */ import net.minecraft.world.level.block.state.BlockState;
/*    */ 
/*    */ public class DropperBlockEntity extends DispenserBlockEntity {
/*  8 */   private static final Component DEFAULT_NAME = (Component)Component.translatable("container.dropper");
/*    */   
/*    */   public DropperBlockEntity(BlockPos worldPosition, BlockState blockState) {
/* 11 */     super(BlockEntityTypes.DROPPER, worldPosition, blockState);
/*    */   }
/*    */   
/*    */   protected Component getDefaultName() {
/* 16 */     return DEFAULT_NAME;
/*    */   }
/*    */ }


/* Location:              C:\Users\karol\AppData\Roaming\ModrinthApp\meta\versions\26.2-0.19.5\26.2-0.19.5.jar!\net\minecraft\world\level\block\entity\DropperBlockEntity.class
 * Java compiler version: 25 (69.0)
 * JD-Core Version:       1.1.3
 */