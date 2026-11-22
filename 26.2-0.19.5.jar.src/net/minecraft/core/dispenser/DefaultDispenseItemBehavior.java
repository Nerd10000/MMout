/*    */ package net.minecraft.core.dispenser;
/*    */ 
/*    */ import net.minecraft.core.Direction;
/*    */ import net.minecraft.core.Position;
/*    */ import net.minecraft.util.RandomSource;
/*    */ import net.minecraft.world.entity.Entity;
/*    */ import net.minecraft.world.entity.item.ItemEntity;
/*    */ import net.minecraft.world.item.ItemStack;
/*    */ import net.minecraft.world.level.Level;
/*    */ import net.minecraft.world.level.block.DispenserBlock;
/*    */ import net.minecraft.world.level.block.state.properties.Property;
/*    */ 
/*    */ public class DefaultDispenseItemBehavior implements DispenseItemBehavior {
/*    */   private static final int DEFAULT_ACCURACY = 6;
/*    */   
/*    */   public final ItemStack dispense(BlockSource source, ItemStack dispensed) {
/* 17 */     ItemStack result = execute(source, dispensed);
/* 19 */     playSound(source);
/* 20 */     playAnimation(source, (Direction)source.state().getValue((Property)DispenserBlock.FACING));
/* 22 */     return result;
/*    */   }
/*    */   
/*    */   protected ItemStack execute(BlockSource source, ItemStack dispensed) {
/* 26 */     Direction direction = (Direction)source.state().getValue((Property)DispenserBlock.FACING);
/* 27 */     Position position = DispenserBlock.getDispensePosition(source);
/* 29 */     ItemStack itemStack = dispensed.split(1);
/* 31 */     spawnItem((Level)source.level(), itemStack, 6, direction, position);
/* 33 */     return dispensed;
/*    */   }
/*    */   
/*    */   public static void spawnItem(Level level, ItemStack itemStack, int accuracy, Direction direction, Position position) {
/* 37 */     double spawnX = position.x();
/* 38 */     double spawnY = position.y();
/* 39 */     double spawnZ = position.z();
/* 41 */     if (direction.getAxis() == Direction.Axis.Y) {
/* 43 */       spawnY -= 0.125D;
/*    */     } else {
/* 46 */       spawnY -= 0.15625D;
/*    */     } 
/* 49 */     ItemEntity itemEntity = new ItemEntity(level, spawnX, spawnY, spawnZ, itemStack);
/* 51 */     RandomSource random = level.getRandom();
/* 52 */     double pow = random.nextDouble() * 0.1D + 0.2D;
/* 53 */     itemEntity.setDeltaMovement(random
/* 54 */         .triangle(direction.getStepX() * pow, 0.0172275D * accuracy), random
/* 55 */         .triangle(0.2D, 0.0172275D * accuracy), random
/* 56 */         .triangle(direction.getStepZ() * pow, 0.0172275D * accuracy));
/* 59 */     level.addFreshEntity((Entity)itemEntity);
/*    */   }
/*    */   
/*    */   protected void playSound(BlockSource source) {
/* 63 */     playDefaultSound(source);
/*    */   }
/*    */   
/*    */   protected void playAnimation(BlockSource source, Direction direction) {
/* 67 */     playDefaultAnimation(source, direction);
/*    */   }
/*    */   
/*    */   private static void playDefaultSound(BlockSource source) {
/* 71 */     source.level().levelEvent(1000, source.pos(), 0);
/*    */   }
/*    */   
/*    */   private static void playDefaultAnimation(BlockSource source, Direction direction) {
/* 75 */     source.level().levelEvent(2000, source.pos(), direction.get3DDataValue());
/*    */   }
/*    */   
/*    */   protected ItemStack consumeWithRemainder(BlockSource source, ItemStack dispensed, ItemStack remainder) {
/* 79 */     dispensed.shrink(1);
/* 80 */     if (dispensed.isEmpty())
/* 82 */       return remainder; 
/* 84 */     addToInventoryOrDispense(source, remainder);
/* 85 */     return dispensed;
/*    */   }
/*    */   
/*    */   private void addToInventoryOrDispense(BlockSource source, ItemStack itemStack) {
/* 89 */     ItemStack remainder = source.blockEntity().insertItem(itemStack);
/* 90 */     if (remainder.isEmpty())
/*    */       return; 
/* 94 */     Direction direction = (Direction)source.state().getValue((Property)DispenserBlock.FACING);
/* 95 */     spawnItem((Level)source.level(), remainder, 6, direction, DispenserBlock.getDispensePosition(source));
/* 97 */     playDefaultSound(source);
/* 98 */     playDefaultAnimation(source, direction);
/*    */   }
/*    */ }


/* Location:              C:\Users\karol\AppData\Roaming\ModrinthApp\meta\versions\26.2-0.19.5\26.2-0.19.5.jar!\net\minecraft\core\dispenser\DefaultDispenseItemBehavior.class
 * Java compiler version: 25 (69.0)
 * JD-Core Version:       1.1.3
 */