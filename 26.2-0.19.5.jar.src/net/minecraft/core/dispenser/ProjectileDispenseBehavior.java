/*    */ package net.minecraft.core.dispenser;
/*    */ 
/*    */ import net.minecraft.core.Direction;
/*    */ import net.minecraft.core.Position;
/*    */ import net.minecraft.server.level.ServerLevel;
/*    */ import net.minecraft.world.entity.projectile.Projectile;
/*    */ import net.minecraft.world.item.Item;
/*    */ import net.minecraft.world.item.ItemStack;
/*    */ import net.minecraft.world.item.ProjectileItem;
/*    */ import net.minecraft.world.level.Level;
/*    */ import net.minecraft.world.level.block.DispenserBlock;
/*    */ import net.minecraft.world.level.block.state.properties.Property;
/*    */ 
/*    */ public class ProjectileDispenseBehavior extends DefaultDispenseItemBehavior {
/*    */   private final ProjectileItem projectileItem;
/*    */   
/*    */   private final ProjectileItem.DispenseConfig dispenseConfig;
/*    */   
/*    */   public ProjectileDispenseBehavior(Item item) {
/*    */     ProjectileItem projectileItem;
/* 19 */     if (item instanceof ProjectileItem) {
/* 19 */       projectileItem = (ProjectileItem)item;
/*    */     } else {
/* 20 */       throw new IllegalArgumentException(String.valueOf(item) + " not instance of " + String.valueOf(item));
/*    */     } 
/* 22 */     this.projectileItem = projectileItem;
/* 23 */     this.dispenseConfig = projectileItem.createDispenseConfig();
/*    */   }
/*    */   
/*    */   public ItemStack execute(BlockSource source, ItemStack dispensed) {
/* 28 */     ServerLevel level = source.level();
/* 29 */     Direction direction = (Direction)source.state().getValue((Property)DispenserBlock.FACING);
/* 30 */     Position position = this.dispenseConfig.positionFunction().getDispensePosition(source, direction);
/* 32 */     Projectile.spawnProjectileUsingShoot(this.projectileItem
/* 33 */         .asProjectile((Level)level, position, dispensed, direction), level, dispensed, direction
/*    */         
/* 35 */         .getStepX(), direction.getStepY(), direction.getStepZ(), this.dispenseConfig
/* 36 */         .power(), this.dispenseConfig.uncertainty());
/* 38 */     dispensed.shrink(1);
/* 40 */     return dispensed;
/*    */   }
/*    */   
/*    */   protected void playSound(BlockSource source) {
/* 45 */     source.level().levelEvent(this.dispenseConfig.overrideDispenseEvent().orElse(1002), source.pos(), 0);
/*    */   }
/*    */ }


/* Location:              C:\Users\karol\AppData\Roaming\ModrinthApp\meta\versions\26.2-0.19.5\26.2-0.19.5.jar!\net\minecraft\core\dispenser\ProjectileDispenseBehavior.class
 * Java compiler version: 25 (69.0)
 * JD-Core Version:       1.1.3
 */