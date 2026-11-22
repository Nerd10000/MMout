/*     */ package net.minecraft.core.dispenser;
/*     */ 
/*     */ import net.minecraft.core.BlockPos;
/*     */ import net.minecraft.core.Direction;
/*     */ import net.minecraft.server.level.ServerLevel;
/*     */ import net.minecraft.world.item.DispensibleContainerItem;
/*     */ import net.minecraft.world.item.ItemStack;
/*     */ import net.minecraft.world.item.Items;
/*     */ import net.minecraft.world.level.ItemLike;
/*     */ import net.minecraft.world.level.Level;
/*     */ import net.minecraft.world.level.block.DispenserBlock;
/*     */ import net.minecraft.world.level.block.state.properties.Property;
/*     */ 
/*     */ class null extends DefaultDispenseItemBehavior {
/* 142 */   private final DefaultDispenseItemBehavior defaultDispenseItemBehavior = new DefaultDispenseItemBehavior();
/*     */   
/*     */   public ItemStack execute(BlockSource source, ItemStack dispensed) {
/* 146 */     DispensibleContainerItem bucket = (DispensibleContainerItem)dispensed.getItem();
/* 147 */     BlockPos target = source.pos().relative((Direction)source.state().getValue((Property)DispenserBlock.FACING));
/* 149 */     ServerLevel serverLevel = source.level();
/* 150 */     if (bucket.emptyContents(null, (Level)serverLevel, target, null)) {
/* 151 */       bucket.checkExtraContent(null, (Level)serverLevel, dispensed, target);
/* 152 */       return consumeWithRemainder(source, dispensed, new ItemStack((ItemLike)Items.BUCKET));
/*     */     } 
/* 155 */     return this.defaultDispenseItemBehavior.dispense(source, dispensed);
/*     */   }
/*     */ }


/* Location:              C:\Users\karol\AppData\Roaming\ModrinthApp\meta\versions\26.2-0.19.5\26.2-0.19.5.jar!\net\minecraft\core\dispenser\DispenseItemBehavior$3.class
 * Java compiler version: 25 (69.0)
 * JD-Core Version:       1.1.3
 */