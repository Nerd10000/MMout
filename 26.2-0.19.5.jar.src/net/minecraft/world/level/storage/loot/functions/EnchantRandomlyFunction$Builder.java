/*     */ package net.minecraft.world.level.storage.loot.functions;
/*     */ 
/*     */ import java.util.Optional;
/*     */ import net.minecraft.core.Holder;
/*     */ import net.minecraft.core.HolderSet;
/*     */ import net.minecraft.world.item.enchantment.Enchantment;
/*     */ 
/*     */ public class Builder extends LootItemConditionalFunction.Builder<EnchantRandomlyFunction.Builder> {
/* 104 */   private Optional<HolderSet<Enchantment>> options = Optional.empty();
/*     */   
/*     */   private boolean onlyCompatible = true;
/*     */   
/*     */   private boolean includeAdditionalCostComponent = false;
/*     */   
/*     */   protected Builder getThis() {
/* 110 */     return this;
/*     */   }
/*     */   
/*     */   public Builder withEnchantment(Holder<Enchantment> enchantment) {
/* 114 */     this.options = Optional.of(HolderSet.direct(new Holder[] { enchantment }));
/* 115 */     return this;
/*     */   }
/*     */   
/*     */   public Builder withOneOf(HolderSet<Enchantment> enchantments) {
/* 119 */     this.options = Optional.of(enchantments);
/* 120 */     return this;
/*     */   }
/*     */   
/*     */   public Builder withOptions(HolderSet<Enchantment> enchantments) {
/* 124 */     this.options = Optional.of(enchantments);
/* 125 */     return this;
/*     */   }
/*     */   
/*     */   public Builder allowingIncompatibleEnchantments() {
/* 129 */     this.onlyCompatible = false;
/* 130 */     return this;
/*     */   }
/*     */   
/*     */   public Builder includeAdditionalCostComponent() {
/* 134 */     this.includeAdditionalCostComponent = true;
/* 135 */     return this;
/*     */   }
/*     */   
/*     */   public LootItemFunction build() {
/* 140 */     return new EnchantRandomlyFunction(getConditions(), this.options, this.onlyCompatible, this.includeAdditionalCostComponent);
/*     */   }
/*     */ }


/* Location:              C:\Users\karol\AppData\Roaming\ModrinthApp\meta\versions\26.2-0.19.5\26.2-0.19.5.jar!\net\minecraft\world\level\storage\loot\functions\EnchantRandomlyFunction$Builder.class
 * Java compiler version: 25 (69.0)
 * JD-Core Version:       1.1.3
 */