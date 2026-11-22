/*    */ package net.minecraft.world.level.block.entity;
/*    */ 
/*    */ import net.minecraft.core.BlockPos;
/*    */ import net.minecraft.network.chat.Component;
/*    */ import net.minecraft.world.entity.player.Inventory;
/*    */ import net.minecraft.world.inventory.AbstractContainerMenu;
/*    */ import net.minecraft.world.inventory.FurnaceMenu;
/*    */ import net.minecraft.world.item.crafting.RecipeType;
/*    */ import net.minecraft.world.level.block.state.BlockState;
/*    */ 
/*    */ public class FurnaceBlockEntity extends AbstractFurnaceBlockEntity {
/* 12 */   private static final Component DEFAULT_NAME = (Component)Component.translatable("container.furnace");
/*    */   
/*    */   public FurnaceBlockEntity(BlockPos worldPosition, BlockState blockState) {
/* 15 */     super(BlockEntityTypes.FURNACE, worldPosition, blockState, RecipeType.SMELTING);
/*    */   }
/*    */   
/*    */   protected Component getDefaultName() {
/* 20 */     return DEFAULT_NAME;
/*    */   }
/*    */   
/*    */   protected AbstractContainerMenu createMenu(int containerId, Inventory inventory) {
/* 25 */     return (AbstractContainerMenu)new FurnaceMenu(containerId, inventory, this, this.dataAccess);
/*    */   }
/*    */ }


/* Location:              C:\Users\karol\AppData\Roaming\ModrinthApp\meta\versions\26.2-0.19.5\26.2-0.19.5.jar!\net\minecraft\world\level\block\entity\FurnaceBlockEntity.class
 * Java compiler version: 25 (69.0)
 * JD-Core Version:       1.1.3
 */