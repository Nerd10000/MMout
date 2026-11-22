/*     */ package net.minecraft.util.datafix.schemas;
/*     */ 
/*     */ import com.mojang.datafixers.types.templates.Hook;
/*     */ import com.mojang.serialization.Dynamic;
/*     */ import com.mojang.serialization.DynamicOps;
/*     */ 
/*     */ class null implements Hook.HookFunction {
/*     */   public <T> T apply(DynamicOps<T> ops, T value) {
/* 339 */     return V99.addNames(new Dynamic(ops, value), V704.ITEM_TO_BLOCKENTITY, V705.ITEM_TO_ENTITY);
/*     */   }
/*     */ }


/* Location:              C:\Users\karol\AppData\Roaming\ModrinthApp\meta\versions\26.2-0.19.5\26.2-0.19.5.jar!\net\minecraf\\util\datafix\schemas\V705$1.class
 * Java compiler version: 25 (69.0)
 * JD-Core Version:       1.1.3
 */