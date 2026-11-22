/*     */ package net.minecraft.core.dispenser;
/*     */ 
/*     */ import java.util.List;
/*     */ import net.minecraft.core.BlockPos;
/*     */ import net.minecraft.core.Direction;
/*     */ import net.minecraft.world.entity.SlotAccess;
/*     */ import net.minecraft.world.entity.animal.equine.AbstractChestedHorse;
/*     */ import net.minecraft.world.item.ItemStack;
/*     */ import net.minecraft.world.level.block.DispenserBlock;
/*     */ import net.minecraft.world.level.block.state.properties.Property;
/*     */ import net.minecraft.world.phys.AABB;
/*     */ 
/*     */ class null extends OptionalDispenseItemBehavior {
/*     */   public ItemStack execute(BlockSource source, ItemStack dispensed) {
/* 101 */     BlockPos pos = source.pos().relative((Direction)source.state().getValue((Property)DispenserBlock.FACING));
/* 102 */     List<AbstractChestedHorse> entities = source.level().getEntitiesOfClass(AbstractChestedHorse.class, new AABB(pos), entity -> (entity.isAlive() && !entity.hasChest()));
/* 104 */     for (AbstractChestedHorse abstractChestedHorse : entities) {
/* 105 */       if (abstractChestedHorse.isTamed()) {
/* 106 */         SlotAccess slot = abstractChestedHorse.getSlot(499);
/* 107 */         if (slot != null && slot.set(dispensed)) {
/* 108 */           dispensed.shrink(1);
/* 109 */           setSuccess(true);
/* 110 */           return dispensed;
/*     */         } 
/*     */       } 
/*     */     } 
/* 115 */     return super.execute(source, dispensed);
/*     */   }
/*     */ }


/* Location:              C:\Users\karol\AppData\Roaming\ModrinthApp\meta\versions\26.2-0.19.5\26.2-0.19.5.jar!\net\minecraft\core\dispenser\DispenseItemBehavior$2.class
 * Java compiler version: 25 (69.0)
 * JD-Core Version:       1.1.3
 */