/*     */ package net.minecraft.world.level.storage.loot.functions;
/*     */ 
/*     */ import net.minecraft.core.Holder;
/*     */ import net.minecraft.world.item.enchantment.Enchantment;
/*     */ import net.minecraft.world.level.storage.loot.providers.number.NumberProvider;
/*     */ 
/*     */ public class Builder extends LootItemConditionalFunction.Builder<EnchantedCountIncreaseFunction.Builder> {
/*     */   private final Holder<Enchantment> enchantment;
/*     */   
/*     */   private final NumberProvider count;
/*     */   
/*  91 */   private int limit = 0;
/*     */   
/*     */   public Builder(Holder<Enchantment> enchantment, NumberProvider count) {
/*  94 */     this.enchantment = enchantment;
/*  95 */     this.count = count;
/*     */   }
/*     */   
/*     */   protected Builder getThis() {
/* 100 */     return this;
/*     */   }
/*     */   
/*     */   public Builder setLimit(int limit) {
/* 104 */     this.limit = limit;
/* 105 */     return this;
/*     */   }
/*     */   
/*     */   public LootItemFunction build() {
/* 110 */     return new EnchantedCountIncreaseFunction(getConditions(), this.enchantment, this.count, this.limit);
/*     */   }
/*     */ }


/* Location:              C:\Users\karol\AppData\Roaming\ModrinthApp\meta\versions\26.2-0.19.5\26.2-0.19.5.jar!\net\minecraft\world\level\storage\loot\functions\EnchantedCountIncreaseFunction$Builder.class
 * Java compiler version: 25 (69.0)
 * JD-Core Version:       1.1.3
 */