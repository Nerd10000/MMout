/*     */ package net.minecraft.world.level.storage.loot.functions;
/*     */ 
/*     */ import com.mojang.datafixers.kinds.App;
/*     */ import com.mojang.datafixers.kinds.Applicative;
/*     */ import com.mojang.datafixers.util.Function4;
/*     */ import com.mojang.serialization.Codec;
/*     */ import com.mojang.serialization.MapCodec;
/*     */ import com.mojang.serialization.codecs.RecordCodecBuilder;
/*     */ import java.util.List;
/*     */ import java.util.Optional;
/*     */ import java.util.Set;
/*     */ import net.minecraft.core.HolderLookup;
/*     */ import net.minecraft.core.HolderSet;
/*     */ import net.minecraft.core.RegistryAccess;
/*     */ import net.minecraft.core.RegistryCodecs;
/*     */ import net.minecraft.core.component.DataComponents;
/*     */ import net.minecraft.core.registries.Registries;
/*     */ import net.minecraft.tags.EnchantmentTags;
/*     */ import net.minecraft.util.RandomSource;
/*     */ import net.minecraft.util.context.ContextKey;
/*     */ import net.minecraft.world.item.ItemStack;
/*     */ import net.minecraft.world.item.enchantment.Enchantment;
/*     */ import net.minecraft.world.item.enchantment.EnchantmentHelper;
/*     */ import net.minecraft.world.level.storage.loot.LootContext;
/*     */ import net.minecraft.world.level.storage.loot.Validatable;
/*     */ import net.minecraft.world.level.storage.loot.ValidationContext;
/*     */ import net.minecraft.world.level.storage.loot.parameters.LootContextParams;
/*     */ import net.minecraft.world.level.storage.loot.predicates.LootItemCondition;
/*     */ import net.minecraft.world.level.storage.loot.providers.number.NumberProvider;
/*     */ import net.minecraft.world.level.storage.loot.providers.number.NumberProviders;
/*     */ 
/*     */ public class EnchantWithLevelsFunction extends LootItemConditionalFunction {
/*     */   public static final MapCodec<EnchantWithLevelsFunction> MAP_CODEC;
/*     */   
/*     */   private final NumberProvider levels;
/*     */   
/*     */   private final Optional<HolderSet<Enchantment>> options;
/*     */   
/*     */   private final boolean includeAdditionalCostComponent;
/*     */   
/*     */   static {
/*  31 */     MAP_CODEC = RecordCodecBuilder.mapCodec(i -> commonFields(i).and(i.group((App)NumberProviders.CODEC.fieldOf("levels").forGetter(()), (App)RegistryCodecs.homogeneousList(Registries.ENCHANTMENT).optionalFieldOf("options").forGetter(()), (App)Codec.BOOL.optionalFieldOf("include_additional_cost_component", Boolean.valueOf(false)).forGetter(()))).apply((Applicative)i, EnchantWithLevelsFunction::new));
/*     */   }
/*     */   
/*     */   private EnchantWithLevelsFunction(List<LootItemCondition> predicates, NumberProvider levels, Optional<HolderSet<Enchantment>> options, boolean includeAdditionalCostComponent) {
/*  44 */     super(predicates);
/*  45 */     this.levels = levels;
/*  46 */     this.options = options;
/*  47 */     this.includeAdditionalCostComponent = includeAdditionalCostComponent;
/*     */   }
/*     */   
/*     */   public MapCodec<EnchantWithLevelsFunction> codec() {
/*  52 */     return MAP_CODEC;
/*     */   }
/*     */   
/*     */   public Set<ContextKey<?>> getReferencedContextParams() {
/*  57 */     if (this.includeAdditionalCostComponent)
/*  58 */       return Set.of(LootContextParams.ADDITIONAL_COST_COMPONENT_ALLOWED); 
/*  60 */     return Set.of();
/*     */   }
/*     */   
/*     */   public void validate(ValidationContext context) {
/*  66 */     super.validate(context);
/*  68 */     Validatable.validate(context, "levels", (Validatable)this.levels);
/*     */   }
/*     */   
/*     */   public ItemStack run(ItemStack itemStack, LootContext context) {
/*  73 */     RandomSource random = context.getRandom();
/*  74 */     RegistryAccess registryAccess = context.getLevel().registryAccess();
/*  75 */     int enchantmentCost = this.levels.getInt(context);
/*  76 */     ItemStack result = EnchantmentHelper.enchantItem(random, itemStack, enchantmentCost, registryAccess, this.options);
/*  77 */     if (this.includeAdditionalCostComponent && context.hasParameter(LootContextParams.ADDITIONAL_COST_COMPONENT_ALLOWED) && !result.isEmpty() && enchantmentCost > 0)
/*  78 */       result.set(DataComponents.ADDITIONAL_TRADE_COST, Integer.valueOf(enchantmentCost)); 
/*  80 */     return result;
/*     */   }
/*     */   
/*     */   public static class Builder extends LootItemConditionalFunction.Builder<Builder> {
/*     */     private final NumberProvider levels;
/*     */     
/*  85 */     private Optional<HolderSet<Enchantment>> options = Optional.empty();
/*     */     
/*     */     private boolean includeAdditionalCostComponent = false;
/*     */     
/*     */     public Builder(NumberProvider levels) {
/*  89 */       this.levels = levels;
/*     */     }
/*     */     
/*     */     protected Builder getThis() {
/*  94 */       return this;
/*     */     }
/*     */     
/*     */     public Builder withOptions(HolderSet<Enchantment> tag) {
/*  98 */       this.options = Optional.of(tag);
/*  99 */       return this;
/*     */     }
/*     */     
/*     */     public Builder withOptions(Optional<HolderSet<Enchantment>> options) {
/* 103 */       this.options = options;
/* 104 */       return this;
/*     */     }
/*     */     
/*     */     public Builder includeAdditionalCostComponent() {
/* 108 */       this.includeAdditionalCostComponent = true;
/* 109 */       return this;
/*     */     }
/*     */     
/*     */     public LootItemFunction build() {
/* 114 */       return new EnchantWithLevelsFunction(getConditions(), this.levels, this.options, this.includeAdditionalCostComponent);
/*     */     }
/*     */   }
/*     */   
/*     */   public static Builder enchantWithLevels(HolderLookup.Provider registries, NumberProvider levels) {
/* 119 */     return (new Builder(levels)).withOptions((HolderSet<Enchantment>)registries.lookupOrThrow(Registries.ENCHANTMENT).getOrThrow(EnchantmentTags.ON_RANDOM_LOOT));
/*     */   }
/*     */ }


/* Location:              C:\Users\karol\AppData\Roaming\ModrinthApp\meta\versions\26.2-0.19.5\26.2-0.19.5.jar!\net\minecraft\world\level\storage\loot\functions\EnchantWithLevelsFunction.class
 * Java compiler version: 25 (69.0)
 * JD-Core Version:       1.1.3
 */