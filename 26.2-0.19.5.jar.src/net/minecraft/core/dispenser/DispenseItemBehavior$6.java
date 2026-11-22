/*     */ package net.minecraft.core.dispenser;
/*     */ 
/*     */ import net.minecraft.core.BlockPos;
/*     */ import net.minecraft.core.Direction;
/*     */ import net.minecraft.core.Holder;
/*     */ import net.minecraft.server.level.ServerLevel;
/*     */ import net.minecraft.sounds.SoundEvents;
/*     */ import net.minecraft.sounds.SoundSource;
/*     */ import net.minecraft.world.entity.Entity;
/*     */ import net.minecraft.world.entity.item.PrimedTnt;
/*     */ import net.minecraft.world.item.ItemStack;
/*     */ import net.minecraft.world.level.Level;
/*     */ import net.minecraft.world.level.block.DispenserBlock;
/*     */ import net.minecraft.world.level.block.state.properties.Property;
/*     */ import net.minecraft.world.level.gameevent.GameEvent;
/*     */ import net.minecraft.world.level.gamerules.GameRules;
/*     */ 
/*     */ class null extends OptionalDispenseItemBehavior {
/*     */   protected ItemStack execute(BlockSource source, ItemStack dispensed) {
/* 220 */     ServerLevel level = source.level();
/* 221 */     if (!((Boolean)level.getGameRules().get(GameRules.TNT_EXPLODES)).booleanValue()) {
/* 222 */       setSuccess(false);
/* 223 */       return dispensed;
/*     */     } 
/* 226 */     BlockPos target = source.pos().relative((Direction)source.state().getValue((Property)DispenserBlock.FACING));
/* 228 */     if (SulfurCubeBlockDispenseItemBehavior.dispenseBlock(level, target, dispensed))
/* 229 */       return dispensed; 
/* 232 */     PrimedTnt tnt = new PrimedTnt((Level)level, target.getX() + 0.5D, target.getY(), target.getZ() + 0.5D, null);
/* 233 */     level.addFreshEntity((Entity)tnt);
/* 234 */     level.playSound(null, tnt.getX(), tnt.getY(), tnt.getZ(), SoundEvents.TNT_PRIMED, SoundSource.BLOCKS, 1.0F, 1.0F);
/* 235 */     level.gameEvent(null, (Holder)GameEvent.ENTITY_PLACE, target);
/* 237 */     dispensed.shrink(1);
/* 238 */     setSuccess(true);
/* 239 */     return dispensed;
/*     */   }
/*     */ }


/* Location:              C:\Users\karol\AppData\Roaming\ModrinthApp\meta\versions\26.2-0.19.5\26.2-0.19.5.jar!\net\minecraft\core\dispenser\DispenseItemBehavior$6.class
 * Java compiler version: 25 (69.0)
 * JD-Core Version:       1.1.3
 */