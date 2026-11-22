/*     */ package net.minecraft.client.renderer.item.properties.numeric;
/*     */ 
/*     */ import net.minecraft.client.multiplayer.ClientLevel;
/*     */ import net.minecraft.core.GlobalPos;
/*     */ import net.minecraft.core.component.DataComponents;
/*     */ import net.minecraft.world.entity.ItemOwner;
/*     */ import net.minecraft.world.item.ItemStack;
/*     */ import net.minecraft.world.item.component.LodestoneTracker;
/*     */ 
/*     */ enum null {
/*     */   public GlobalPos get(ClientLevel level, ItemStack itemStack, ItemOwner owner) {
/* 114 */     LodestoneTracker tracker = (LodestoneTracker)itemStack.get(DataComponents.LODESTONE_TRACKER);
/* 115 */     return (tracker != null) ? tracker.target().orElse(null) : null;
/*     */   }
/*     */ }


/* Location:              C:\Users\karol\AppData\Roaming\ModrinthApp\meta\versions\26.2-0.19.5\26.2-0.19.5.jar!\net\minecraft\client\renderer\item\properties\numeric\CompassAngleState$CompassTarget$2.class
 * Java compiler version: 25 (69.0)
 * JD-Core Version:       1.1.3
 */