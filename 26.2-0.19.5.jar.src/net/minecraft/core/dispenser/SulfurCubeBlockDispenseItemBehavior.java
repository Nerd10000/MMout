/*    */ package net.minecraft.core.dispenser;
/*    */ 
/*    */ import net.minecraft.core.BlockPos;
/*    */ import net.minecraft.core.Direction;
/*    */ import net.minecraft.server.level.ServerLevel;
/*    */ import net.minecraft.world.entity.monster.cubemob.SulfurCube;
/*    */ import net.minecraft.world.item.ItemStack;
/*    */ import net.minecraft.world.level.block.DispenserBlock;
/*    */ import net.minecraft.world.level.block.state.properties.Property;
/*    */ import net.minecraft.world.phys.AABB;
/*    */ 
/*    */ public class SulfurCubeBlockDispenseItemBehavior extends DefaultDispenseItemBehavior {
/* 11 */   public static final SulfurCubeBlockDispenseItemBehavior INSTANCE = new SulfurCubeBlockDispenseItemBehavior();
/*    */   
/*    */   protected ItemStack execute(BlockSource source, ItemStack dispensed) {
/* 15 */     return dispenseBlock(source.level(), source.pos().relative((Direction)source.state().getValue((Property)DispenserBlock.FACING)), dispensed) ? dispensed : super.execute(source, dispensed);
/*    */   }
/*    */   
/*    */   public static boolean dispenseBlock(ServerLevel level, BlockPos pos, ItemStack dispensed) {
/* 19 */     for (SulfurCube entity : level.getEntitiesOfClass(SulfurCube.class, new AABB(pos))) {
/* 20 */       if (entity.equipItem(dispensed)) {
/* 21 */         dispensed.shrink(1);
/* 22 */         return true;
/*    */       } 
/*    */     } 
/* 25 */     return false;
/*    */   }
/*    */ }


/* Location:              C:\Users\karol\AppData\Roaming\ModrinthApp\meta\versions\26.2-0.19.5\26.2-0.19.5.jar!\net\minecraft\core\dispenser\SulfurCubeBlockDispenseItemBehavior.class
 * Java compiler version: 25 (69.0)
 * JD-Core Version:       1.1.3
 */