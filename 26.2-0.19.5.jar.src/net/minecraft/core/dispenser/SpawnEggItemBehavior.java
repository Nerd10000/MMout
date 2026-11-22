/*    */ package net.minecraft.core.dispenser;
/*    */ 
/*    */ import net.minecraft.core.Direction;
/*    */ import net.minecraft.core.Holder;
/*    */ import net.minecraft.world.entity.Entity;
/*    */ import net.minecraft.world.entity.EntitySpawnReason;
/*    */ import net.minecraft.world.entity.EntityType;
/*    */ import net.minecraft.world.item.ItemStack;
/*    */ import net.minecraft.world.item.SpawnEggItem;
/*    */ import net.minecraft.world.level.block.DispenserBlock;
/*    */ import net.minecraft.world.level.block.state.properties.Property;
/*    */ import net.minecraft.world.level.gameevent.GameEvent;
/*    */ 
/*    */ public class SpawnEggItemBehavior extends DefaultDispenseItemBehavior {
/* 13 */   public static final SpawnEggItemBehavior INSTANCE = new SpawnEggItemBehavior();
/*    */   
/*    */   public ItemStack execute(BlockSource source, ItemStack dispensed) {
/* 17 */     Direction direction = (Direction)source.state().getValue((Property)DispenserBlock.FACING);
/* 19 */     EntityType<?> type = SpawnEggItem.getType(dispensed);
/* 20 */     if (type == null)
/* 21 */       return dispensed; 
/*    */     try {
/* 24 */       Entity spawned = type.spawn(source.level(), dispensed, null, source.pos().relative(direction), EntitySpawnReason.DISPENSER, (direction != Direction.UP), false);
/* 25 */       if (spawned == null)
/* 26 */         return dispensed; 
/* 28 */     } catch (Exception e) {
/* 29 */       LOGGER.error("Error while dispensing spawn egg from dispenser at {}", source.pos(), e);
/* 30 */       return ItemStack.EMPTY;
/*    */     } 
/* 32 */     dispensed.shrink(1);
/* 33 */     source.level().gameEvent(null, (Holder)GameEvent.ENTITY_PLACE, source.pos());
/* 34 */     return dispensed;
/*    */   }
/*    */ }


/* Location:              C:\Users\karol\AppData\Roaming\ModrinthApp\meta\versions\26.2-0.19.5\26.2-0.19.5.jar!\net\minecraft\core\dispenser\SpawnEggItemBehavior.class
 * Java compiler version: 25 (69.0)
 * JD-Core Version:       1.1.3
 */