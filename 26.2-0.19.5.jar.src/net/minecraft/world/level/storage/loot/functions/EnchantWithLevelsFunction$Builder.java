/*     */ package net.minecraft.world.level.storage.loot.functions;
/*     */ 
/*     */ import java.util.Optional;
/*     */ import net.minecraft.core.HolderSet;
/*     */ import net.minecraft.world.item.enchantment.Enchantment;
/*     */ import net.minecraft.world.level.storage.loot.providers.number.NumberProvider;
/*     */ 
/*     */ public class Builder extends LootItemConditionalFunction.Builder<EnchantWithLevelsFunction.Builder> {
/*     */   private final NumberProvider levels;
/*     */   
/*  85 */   private Optional<HolderSet<Enchantment>> options = Optional.empty();
/*     */   
/*     */   private boolean includeAdditionalCostComponent = false;
/*     */   
/*     */   public Builder(NumberProvider levels) {
/*  89 */     this.levels = levels;
/*     */   }
/*     */   
/*     */   protected Builder getThis() {
/*  94 */     return this;
/*     */   }
/*     */   
/*     */   public Builder withOptions(HolderSet<Enchantment> tag) {
/*  98 */     this.options = Optional.of(tag);
/*  99 */     return this;
/*     */   }
/*     */   
/*     */   public Builder withOptions(Optional<HolderSet<Enchantment>> options) {
/* 103 */     this.options = options;
/* 104 */     return this;
/*     */   }
/*     */   
/*     */   public Builder includeAdditionalCostComponent() {
/* 108 */     this.includeAdditionalCostComponent = true;
/* 109 */     return this;
/*     */   }
/*     */   
/*     */   public LootItemFunction build() {
/* 114 */     return new EnchantWithLevelsFunction(getConditions(), this.levels, this.options, this.includeAdditionalCostComponent);
/*     */   }
/*     */ }


/* Location:              C:\Users\karol\AppData\Roaming\ModrinthApp\meta\versions\26.2-0.19.5\26.2-0.19.5.jar!\net\minecraft\world\level\storage\loot\functions\EnchantWithLevelsFunction$Builder.class
 * Java compiler version: 25 (69.0)
 * JD-Core Version:       1.1.3
 */