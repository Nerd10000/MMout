/*    */ package net.minecraft.world.entity.vehicle.boat;
/*    */ 
/*    */ import java.util.function.Supplier;
/*    */ import net.minecraft.world.entity.EntityDimensions;
/*    */ import net.minecraft.world.entity.EntityType;
/*    */ import net.minecraft.world.item.Item;
/*    */ import net.minecraft.world.level.Level;
/*    */ 
/*    */ public class Boat extends AbstractBoat {
/*    */   public Boat(EntityType<? extends Boat> type, Level level, Supplier<Item> dropItem) {
/* 12 */     super((EntityType)type, level, dropItem);
/*    */   }
/*    */   
/*    */   protected double rideHeight(EntityDimensions dimensions) {
/* 17 */     return (dimensions.height() / 3.0F);
/*    */   }
/*    */ }


/* Location:              C:\Users\karol\AppData\Roaming\ModrinthApp\meta\versions\26.2-0.19.5\26.2-0.19.5.jar!\net\minecraft\world\entity\vehicle\boat\Boat.class
 * Java compiler version: 25 (69.0)
 * JD-Core Version:       1.1.3
 */