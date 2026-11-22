/*     */ package net.minecraft.world.level.storage.loot.functions;
/*     */ 
/*     */ import com.mojang.datafixers.kinds.App;
/*     */ import com.mojang.datafixers.kinds.Applicative;
/*     */ import com.mojang.datafixers.util.Function4;
/*     */ import com.mojang.logging.LogUtils;
/*     */ import com.mojang.serialization.Codec;
/*     */ import com.mojang.serialization.MapCodec;
/*     */ import com.mojang.serialization.codecs.RecordCodecBuilder;
/*     */ import java.util.List;
/*     */ import java.util.Optional;
/*     */ import java.util.Set;
/*     */ import java.util.function.Function;
/*     */ import java.util.stream.Stream;
/*     */ import net.minecraft.core.Holder;
/*     */ import net.minecraft.core.HolderLookup;
/*     */ import net.minecraft.core.HolderSet;
/*     */ import net.minecraft.core.RegistryCodecs;
/*     */ import net.minecraft.core.component.DataComponents;
/*     */ import net.minecraft.core.registries.Registries;
/*     */ import net.minecraft.tags.EnchantmentTags;
/*     */ import net.minecraft.util.Mth;
/*     */ import net.minecraft.util.RandomSource;
/*     */ import net.minecraft.util.Util;
/*     */ import net.minecraft.util.context.ContextKey;
/*     */ import net.minecraft.world.item.ItemStack;
/*     */ import net.minecraft.world.item.Items;
/*     */ import net.minecraft.world.item.enchantment.Enchantment;
/*     */ import net.minecraft.world.level.ItemLike;
/*     */ import net.minecraft.world.level.storage.loot.LootContext;
/*     */ import net.minecraft.world.level.storage.loot.parameters.LootContextParams;
/*     */ import net.minecraft.world.level.storage.loot.predicates.LootItemCondition;
/*     */ import org.slf4j.Logger;
/*     */ 
/*     */ public class EnchantRandomlyFunction extends LootItemConditionalFunction {
/*  33 */   private static final Logger LOGGER = LogUtils.getLogger();
/*     */   
/*     */   public static final MapCodec<EnchantRandomlyFunction> MAP_CODEC;
/*     */   
/*     */   private final Optional<HolderSet<Enchantment>> options;
/*     */   
/*     */   private final boolean onlyCompatible;
/*     */   
/*     */   private final boolean includeAdditionalCostComponent;
/*     */   
/*     */   static {
/*  35 */     MAP_CODEC = RecordCodecBuilder.mapCodec(i -> commonFields(i).and(i.group((App)RegistryCodecs.homogeneousList(Registries.ENCHANTMENT).optionalFieldOf("options").forGetter(()), (App)Codec.BOOL.optionalFieldOf("only_compatible", Boolean.valueOf(true)).forGetter(()), (App)Codec.BOOL.optionalFieldOf("include_additional_cost_component", Boolean.valueOf(false)).forGetter(()))).apply((Applicative)i, EnchantRandomlyFunction::new));
/*     */   }
/*     */   
/*     */   private EnchantRandomlyFunction(List<LootItemCondition> predicates, Optional<HolderSet<Enchantment>> options, boolean onlyCompatible, boolean includeAdditionalCostComponent) {
/*  48 */     super(predicates);
/*  49 */     this.options = options;
/*  50 */     this.onlyCompatible = onlyCompatible;
/*  51 */     this.includeAdditionalCostComponent = includeAdditionalCostComponent;
/*     */   }
/*     */   
/*     */   public MapCodec<EnchantRandomlyFunction> codec() {
/*  56 */     return MAP_CODEC;
/*     */   }
/*     */   
/*     */   public Set<ContextKey<?>> getReferencedContextParams() {
/*  61 */     if (this.includeAdditionalCostComponent)
/*  62 */       return Set.of(LootContextParams.ADDITIONAL_COST_COMPONENT_ALLOWED); 
/*  64 */     return Set.of();
/*     */   }
/*     */   
/*     */   public ItemStack run(ItemStack itemStack, LootContext context) {
/*  70 */     RandomSource random = context.getRandom();
/*  71 */     boolean targetIsBook = itemStack.is(Items.BOOK);
/*  72 */     boolean shouldCheckCompatibility = (!targetIsBook && this.onlyCompatible);
/*  79 */     Stream<Holder<Enchantment>> compatibleEnchantmentsStream = ((Stream<Holder<Enchantment>>)this.options.<Stream<Holder<Enchantment>>>map(HolderSet::stream).orElseGet(() -> context.getLevel().registryAccess().lookupOrThrow(Registries.ENCHANTMENT).listElements().map(Function.identity()))).filter(candidate -> (!shouldCheckCompatibility || ((Enchantment)candidate.value()).canEnchant(itemStack)));
/*  80 */     List<Holder<Enchantment>> compatibleEnchantments = compatibleEnchantmentsStream.toList();
/*  81 */     Optional<Holder<Enchantment>> enchantment = Util.getRandomSafe(compatibleEnchantments, random);
/*  82 */     if (enchantment.isEmpty()) {
/*  83 */       LOGGER.warn("Couldn't find a compatible enchantment for {}", itemStack);
/*  84 */       return itemStack;
/*     */     } 
/*  87 */     return enchantItem(itemStack, enchantment.get(), context);
/*     */   }
/*     */   
/*     */   private ItemStack enchantItem(ItemStack itemStack, Holder<Enchantment> enchantment, LootContext context) {
/*  91 */     RandomSource random = context.getRandom();
/*  92 */     int level = Mth.nextInt(random, ((Enchantment)enchantment.value()).getMinLevel(), ((Enchantment)enchantment.value()).getMaxLevel());
/*  93 */     if (itemStack.is(Items.BOOK))
/*  94 */       itemStack = new ItemStack((ItemLike)Items.ENCHANTED_BOOK); 
/*  96 */     itemStack.enchant(enchantment, level);
/*  97 */     if (this.includeAdditionalCostComponent && context.hasParameter(LootContextParams.ADDITIONAL_COST_COMPONENT_ALLOWED))
/*  98 */       itemStack.set(DataComponents.ADDITIONAL_TRADE_COST, Integer.valueOf(2 + random.nextInt(5 + level * 10) + 3 * level)); 
/* 100 */     return itemStack;
/*     */   }
/*     */   
/*     */   public static class Builder extends LootItemConditionalFunction.Builder<Builder> {
/* 104 */     private Optional<HolderSet<Enchantment>> options = Optional.empty();
/*     */     
/*     */     private boolean onlyCompatible = true;
/*     */     
/*     */     private boolean includeAdditionalCostComponent = false;
/*     */     
/*     */     protected Builder getThis() {
/* 110 */       return this;
/*     */     }
/*     */     
/*     */     public Builder withEnchantment(Holder<Enchantment> enchantment) {
/* 114 */       this.options = Optional.of(HolderSet.direct(new Holder[] { enchantment }));
/* 115 */       return this;
/*     */     }
/*     */     
/*     */     public Builder withOneOf(HolderSet<Enchantment> enchantments) {
/* 119 */       this.options = Optional.of(enchantments);
/* 120 */       return this;
/*     */     }
/*     */     
/*     */     public Builder withOptions(HolderSet<Enchantment> enchantments) {
/* 124 */       this.options = Optional.of(enchantments);
/* 125 */       return this;
/*     */     }
/*     */     
/*     */     public Builder allowingIncompatibleEnchantments() {
/* 129 */       this.onlyCompatible = false;
/* 130 */       return this;
/*     */     }
/*     */     
/*     */     public Builder includeAdditionalCostComponent() {
/* 134 */       this.includeAdditionalCostComponent = true;
/* 135 */       return this;
/*     */     }
/*     */     
/*     */     public LootItemFunction build() {
/* 140 */       return new EnchantRandomlyFunction(getConditions(), this.options, this.onlyCompatible, this.includeAdditionalCostComponent);
/*     */     }
/*     */   }
/*     */   
/*     */   public static Builder randomEnchantment() {
/* 145 */     return new Builder();
/*     */   }
/*     */   
/*     */   public static Builder randomApplicableEnchantment(HolderLookup.Provider registries) {
/* 149 */     return randomEnchantment().withOneOf((HolderSet<Enchantment>)registries.lookupOrThrow(Registries.ENCHANTMENT).getOrThrow(EnchantmentTags.ON_RANDOM_LOOT));
/*     */   }
/*     */ }


/* Location:              C:\Users\karol\AppData\Roaming\ModrinthApp\meta\versions\26.2-0.19.5\26.2-0.19.5.jar!\net\minecraft\world\level\storage\loot\functions\EnchantRandomlyFunction.class
 * Java compiler version: 25 (69.0)
 * JD-Core Version:       1.1.3
 */