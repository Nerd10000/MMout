/*     */ package net.minecraft.world.level.storage.loot;
/*     */ 
/*     */ import net.minecraft.util.StringRepresentable;
/*     */ import net.minecraft.util.context.ContextKey;
/*     */ import net.minecraft.world.item.ItemInstance;
/*     */ import net.minecraft.world.level.storage.loot.parameters.LootContextParams;
/*     */ 
/*     */ public enum ItemStackTarget implements StringRepresentable, LootContextArg.SimpleGetter<ItemInstance> {
/* 179 */   TOOL("tool", LootContextParams.TOOL);
/*     */   
/*     */   private final ContextKey<? extends ItemInstance> param;
/*     */   
/*     */   private final String name;
/*     */   
/*     */   ItemStackTarget(String name, ContextKey<? extends ItemInstance> param) {
/* 187 */     this.name = name;
/* 188 */     this.param = param;
/*     */   }
/*     */   
/*     */   public ContextKey<? extends ItemInstance> contextParam() {
/* 193 */     return this.param;
/*     */   }
/*     */   
/*     */   public String getSerializedName() {
/* 198 */     return this.name;
/*     */   }
/*     */ }


/* Location:              C:\Users\karol\AppData\Roaming\ModrinthApp\meta\versions\26.2-0.19.5\26.2-0.19.5.jar!\net\minecraft\world\level\storage\loot\LootContext$ItemStackTarget.class
 * Java compiler version: 25 (69.0)
 * JD-Core Version:       1.1.3
 */