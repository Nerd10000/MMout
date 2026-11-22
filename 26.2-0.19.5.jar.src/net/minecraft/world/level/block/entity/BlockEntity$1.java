/*     */ package net.minecraft.world.level.block.entity;
/*     */ 
/*     */ import java.util.Set;
/*     */ import net.minecraft.core.component.DataComponentGetter;
/*     */ import net.minecraft.core.component.DataComponentMap;
/*     */ import net.minecraft.core.component.DataComponentType;
/*     */ 
/*     */ class null implements DataComponentGetter {
/*     */   null(BlockEntity this$0) {}
/*     */   
/*     */   public <T> T get(DataComponentType<? extends T> type) {
/* 315 */     implicitComponents.add(type);
/* 316 */     return (T)fullView.get(type);
/*     */   }
/*     */   
/*     */   public <T> T getOrDefault(DataComponentType<? extends T> type, T defaultValue) {
/* 321 */     implicitComponents.add(type);
/* 322 */     return (T)fullView.getOrDefault(type, defaultValue);
/*     */   }
/*     */ }


/* Location:              C:\Users\karol\AppData\Roaming\ModrinthApp\meta\versions\26.2-0.19.5\26.2-0.19.5.jar!\net\minecraft\world\level\block\entity\BlockEntity$1.class
 * Java compiler version: 25 (69.0)
 * JD-Core Version:       1.1.3
 */