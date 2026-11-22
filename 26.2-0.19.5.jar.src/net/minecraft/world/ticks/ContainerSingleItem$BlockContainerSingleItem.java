/*    */ package net.minecraft.world.ticks;
/*    */ 
/*    */ import net.minecraft.world.Container;
/*    */ import net.minecraft.world.entity.player.Player;
/*    */ import net.minecraft.world.level.block.entity.BlockEntity;
/*    */ 
/*    */ public interface BlockContainerSingleItem extends ContainerSingleItem {
/*    */   BlockEntity getContainerBlockEntity();
/*    */   
/*    */   default boolean stillValid(Player player) {
/* 66 */     return Container.stillValidBlockEntity(getContainerBlockEntity(), player);
/*    */   }
/*    */ }


/* Location:              C:\Users\karol\AppData\Roaming\ModrinthApp\meta\versions\26.2-0.19.5\26.2-0.19.5.jar!\net\minecraft\world\ticks\ContainerSingleItem$BlockContainerSingleItem.class
 * Java compiler version: 25 (69.0)
 * JD-Core Version:       1.1.3
 */