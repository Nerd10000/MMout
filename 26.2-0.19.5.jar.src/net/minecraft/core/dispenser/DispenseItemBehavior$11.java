/*     */ package net.minecraft.core.dispenser;
/*     */ 
/*     */ import java.util.List;
/*     */ import net.minecraft.core.BlockPos;
/*     */ import net.minecraft.core.Direction;
/*     */ import net.minecraft.server.level.ServerLevel;
/*     */ import net.minecraft.world.entity.EntitySelector;
/*     */ import net.minecraft.world.entity.animal.armadillo.Armadillo;
/*     */ import net.minecraft.world.item.Item;
/*     */ import net.minecraft.world.item.ItemStack;
/*     */ import net.minecraft.world.level.block.DispenserBlock;
/*     */ import net.minecraft.world.level.block.state.properties.Property;
/*     */ import net.minecraft.world.phys.AABB;
/*     */ 
/*     */ class null extends OptionalDispenseItemBehavior {
/*     */   protected ItemStack execute(BlockSource source, ItemStack dispensed) {
/* 347 */     ServerLevel level = source.level();
/* 349 */     BlockPos pos = source.pos().relative((Direction)source.state().getValue((Property)DispenserBlock.FACING));
/* 351 */     List<Armadillo> armadillos = level.getEntitiesOfClass(Armadillo.class, new AABB(pos), EntitySelector.NO_SPECTATORS);
/* 352 */     if (armadillos.isEmpty()) {
/* 353 */       setSuccess(false);
/* 354 */       return dispensed;
/*     */     } 
/* 356 */     for (Armadillo armadillo : armadillos) {
/* 357 */       if (armadillo.brushOffScute(null, dispensed)) {
/* 358 */         dispensed.hurtAndBreak(16, level, null, item -> {
/*     */             
/*     */             });
/* 359 */         return dispensed;
/*     */       } 
/*     */     } 
/* 362 */     setSuccess(false);
/* 363 */     return dispensed;
/*     */   }
/*     */ }


/* Location:              C:\Users\karol\AppData\Roaming\ModrinthApp\meta\versions\26.2-0.19.5\26.2-0.19.5.jar!\net\minecraft\core\dispenser\DispenseItemBehavior$11.class
 * Java compiler version: 25 (69.0)
 * JD-Core Version:       1.1.3
 */