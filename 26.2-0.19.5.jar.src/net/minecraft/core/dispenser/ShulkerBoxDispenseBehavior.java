/*    */ package net.minecraft.core.dispenser;
/*    */ 
/*    */ import com.mojang.logging.LogUtils;
/*    */ import net.minecraft.core.BlockPos;
/*    */ import net.minecraft.core.Direction;
/*    */ import net.minecraft.world.item.BlockItem;
/*    */ import net.minecraft.world.item.Item;
/*    */ import net.minecraft.world.item.ItemStack;
/*    */ import net.minecraft.world.item.context.BlockPlaceContext;
/*    */ import net.minecraft.world.item.context.DirectionalPlaceContext;
/*    */ import net.minecraft.world.level.Level;
/*    */ import net.minecraft.world.level.block.DispenserBlock;
/*    */ import net.minecraft.world.level.block.state.properties.Property;
/*    */ import org.slf4j.Logger;
/*    */ 
/*    */ public class ShulkerBoxDispenseBehavior extends OptionalDispenseItemBehavior {
/* 14 */   private static final Logger LOGGER = LogUtils.getLogger();
/*    */   
/*    */   protected ItemStack execute(BlockSource source, ItemStack dispensed) {
/* 18 */     setSuccess(false);
/* 20 */     Item item = dispensed.getItem();
/* 21 */     if (item instanceof BlockItem) {
/* 21 */       BlockItem blockItem = (BlockItem)item;
/* 22 */       Direction facing = (Direction)source.state().getValue((Property)DispenserBlock.FACING);
/* 23 */       BlockPos relativePos = source.pos().relative(facing);
/* 25 */       Direction clickedFace = source.level().isEmptyBlock(relativePos.below()) ? facing : Direction.UP;
/*    */       try {
/* 27 */         setSuccess(blockItem.place((BlockPlaceContext)new DirectionalPlaceContext((Level)source.level(), relativePos, facing, dispensed, clickedFace)).consumesAction());
/* 28 */       } catch (Exception e) {
/* 29 */         LOGGER.error("Error trying to place shulker box at {}", relativePos, e);
/*    */       } 
/*    */     } 
/* 32 */     return dispensed;
/*    */   }
/*    */ }


/* Location:              C:\Users\karol\AppData\Roaming\ModrinthApp\meta\versions\26.2-0.19.5\26.2-0.19.5.jar!\net\minecraft\core\dispenser\ShulkerBoxDispenseBehavior.class
 * Java compiler version: 25 (69.0)
 * JD-Core Version:       1.1.3
 */