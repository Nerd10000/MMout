/*     */ package net.minecraft.client.renderer.item.properties.numeric;
/*     */ 
/*     */ import net.minecraft.client.multiplayer.ClientLevel;
/*     */ import net.minecraft.core.GlobalPos;
/*     */ import net.minecraft.world.entity.ItemOwner;
/*     */ import net.minecraft.world.entity.LivingEntity;
/*     */ import net.minecraft.world.entity.player.Player;
/*     */ import net.minecraft.world.item.ItemStack;
/*     */ 
/*     */ enum null {
/*     */   public GlobalPos get(ClientLevel level, ItemStack itemStack, ItemOwner owner) {
/* 127 */     LivingEntity entity = (owner == null) ? null : owner.asLivingEntity();
/* 128 */     Player player = (Player)entity;
/* 128 */     return (entity instanceof Player) ? player.getLastDeathLocation().orElse(null) : null;
/*     */   }
/*     */ }


/* Location:              C:\Users\karol\AppData\Roaming\ModrinthApp\meta\versions\26.2-0.19.5\26.2-0.19.5.jar!\net\minecraft\client\renderer\item\properties\numeric\CompassAngleState$CompassTarget$4.class
 * Java compiler version: 25 (69.0)
 * JD-Core Version:       1.1.3
 */