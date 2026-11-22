/*     */ package net.minecraft.world.item.trading;
/*     */ 
/*     */ import com.mojang.datafixers.kinds.App;
/*     */ import com.mojang.datafixers.kinds.Applicative;
/*     */ import com.mojang.datafixers.util.Function9;
/*     */ import com.mojang.serialization.Codec;
/*     */ import com.mojang.serialization.codecs.RecordCodecBuilder;
/*     */ import java.util.List;
/*     */ import java.util.Objects;
/*     */ import java.util.Optional;
/*     */ import net.minecraft.core.HolderSet;
/*     */ import net.minecraft.core.RegistryCodecs;
/*     */ import net.minecraft.core.component.DataComponents;
/*     */ import net.minecraft.core.registries.Registries;
/*     */ import net.minecraft.world.item.ItemStack;
/*     */ import net.minecraft.world.item.ItemStackTemplate;
/*     */ import net.minecraft.world.item.enchantment.Enchantment;
/*     */ import net.minecraft.world.item.enchantment.ItemEnchantments;
/*     */ import net.minecraft.world.level.storage.loot.LootContext;
/*     */ import net.minecraft.world.level.storage.loot.Validatable;
/*     */ import net.minecraft.world.level.storage.loot.ValidationContext;
/*     */ import net.minecraft.world.level.storage.loot.functions.LootItemFunction;
/*     */ import net.minecraft.world.level.storage.loot.functions.LootItemFunctions;
/*     */ import net.minecraft.world.level.storage.loot.parameters.LootContextParamSets;
/*     */ import net.minecraft.world.level.storage.loot.predicates.LootItemCondition;
/*     */ import net.minecraft.world.level.storage.loot.providers.number.ConstantValue;
/*     */ import net.minecraft.world.level.storage.loot.providers.number.NumberProvider;
/*     */ import net.minecraft.world.level.storage.loot.providers.number.NumberProviders;
/*     */ 
/*     */ public class VillagerTrade implements Validatable {
/*     */   public static final Codec<VillagerTrade> CODEC;
/*     */   
/*     */   private final TradeCost wants;
/*     */   
/*     */   private final Optional<TradeCost> additionalWants;
/*     */   
/*     */   private final ItemStackTemplate gives;
/*     */   
/*     */   private final Optional<LootItemCondition> merchantPredicate;
/*     */   
/*     */   private final List<LootItemFunction> givenItemModifiers;
/*     */   
/*     */   private final NumberProvider maxUses;
/*     */   
/*     */   private final NumberProvider reputationDiscount;
/*     */   
/*     */   private final NumberProvider xp;
/*     */   
/*     */   private final Optional<HolderSet<Enchantment>> doubleTradePriceEnchantments;
/*     */   
/*     */   static {
/*  40 */     CODEC = RecordCodecBuilder.create(i -> i.group((App)TradeCost.CODEC.fieldOf("wants").forGetter(()), (App)TradeCost.CODEC.optionalFieldOf("additional_wants").forGetter(()), (App)ItemStackTemplate.CODEC.fieldOf("gives").forGetter(()), (App)NumberProviders.CODEC.lenientOptionalFieldOf("max_uses", ConstantValue.exactly(4.0F)).forGetter(()), (App)NumberProviders.CODEC.lenientOptionalFieldOf("reputation_discount", ConstantValue.exactly(0.0F)).forGetter(()), (App)NumberProviders.CODEC.lenientOptionalFieldOf("xp", ConstantValue.exactly(1.0F)).forGetter(()), (App)LootItemCondition.DIRECT_CODEC.optionalFieldOf("merchant_predicate").forGetter(()), (App)LootItemFunctions.ROOT_CODEC.listOf().optionalFieldOf("given_item_modifiers", List.of()).forGetter(()), (App)RegistryCodecs.homogeneousList(Registries.ENCHANTMENT).optionalFieldOf("double_trade_price_enchantments").forGetter(())).apply((Applicative)i, VillagerTrade::new)).validate(Validatable.validatorForContext(LootContextParamSets.VILLAGER_TRADE));
/*     */   }
/*     */   
/*     */   private VillagerTrade(TradeCost wants, Optional<TradeCost> additionalWants, ItemStackTemplate gives, NumberProvider maxUses, NumberProvider reputationDiscount, NumberProvider xp, Optional<LootItemCondition> merchantPredicate, List<LootItemFunction> givenItemModifiers, Optional<HolderSet<Enchantment>> doubleTradePriceEnchantments) {
/*  54 */     this.wants = wants;
/*  55 */     this.additionalWants = additionalWants;
/*  56 */     this.gives = gives;
/*  57 */     this.maxUses = maxUses;
/*  58 */     this.reputationDiscount = reputationDiscount;
/*  59 */     this.xp = xp;
/*  60 */     this.merchantPredicate = merchantPredicate;
/*  61 */     this.givenItemModifiers = givenItemModifiers;
/*  62 */     this.doubleTradePriceEnchantments = doubleTradePriceEnchantments;
/*     */   }
/*     */   
/*     */   public VillagerTrade(TradeCost wants, Optional<TradeCost> additionalWants, ItemStackTemplate gives, int maxUses, int xp, float reputationDiscount, Optional<LootItemCondition> merchantPredicate, List<LootItemFunction> givenItemModifiers, HolderSet<Enchantment> doubleTradePriceEnchantments) {
/*  66 */     this(wants, additionalWants, gives, (NumberProvider)ConstantValue.exactly(maxUses), (NumberProvider)ConstantValue.exactly(reputationDiscount), (NumberProvider)ConstantValue.exactly(xp), merchantPredicate, givenItemModifiers, Optional.of(doubleTradePriceEnchantments));
/*     */   }
/*     */   
/*     */   public VillagerTrade(TradeCost wants, Optional<TradeCost> additionalWants, ItemStackTemplate gives, int maxUses, int xp, float reputationDiscount, Optional<LootItemCondition> merchantPredicate, List<LootItemFunction> givenItemModifiers) {
/*  70 */     this(wants, additionalWants, gives, (NumberProvider)ConstantValue.exactly(maxUses), (NumberProvider)ConstantValue.exactly(reputationDiscount), (NumberProvider)ConstantValue.exactly(xp), merchantPredicate, givenItemModifiers, Optional.empty());
/*     */   }
/*     */   
/*     */   public VillagerTrade(TradeCost wants, ItemStackTemplate gives, int maxUses, int xp, float reputationDiscount, Optional<LootItemCondition> merchantPredicate, List<LootItemFunction> givenItemModifiers) {
/*  74 */     this(wants, Optional.empty(), gives, (NumberProvider)ConstantValue.exactly(maxUses), (NumberProvider)ConstantValue.exactly(reputationDiscount), (NumberProvider)ConstantValue.exactly(xp), merchantPredicate, givenItemModifiers, Optional.empty());
/*     */   }
/*     */   
/*     */   public void validate(ValidationContext context) {
/*  79 */     Validatable.validate(context, "wants", this.wants);
/*  80 */     Validatable.validate(context, "additional_wants", this.additionalWants);
/*  81 */     Validatable.validate(context, "max_uses", (Validatable)this.maxUses);
/*  82 */     Validatable.validate(context, "reputation_discount", (Validatable)this.reputationDiscount);
/*  83 */     Validatable.validate(context, "xp", (Validatable)this.xp);
/*  84 */     Validatable.validate(context, "merchant_predicate", this.merchantPredicate);
/*  85 */     Validatable.validate(context, "given_item_modifiers", this.givenItemModifiers);
/*     */   }
/*     */   
/*     */   public MerchantOffer getOffer(LootContext lootContext) {
/*  89 */     if (this.merchantPredicate.isPresent() && !((LootItemCondition)this.merchantPredicate.get()).test(lootContext))
/*  90 */       return null; 
/*  93 */     ItemStack result = this.gives.create();
/*  94 */     int additionalCost = 0;
/*  95 */     for (LootItemFunction outputItemModifier : this.givenItemModifiers) {
/*  96 */       result = (ItemStack)outputItemModifier.apply(result, lootContext);
/*  97 */       if (result.isEmpty())
/*  98 */         return null; 
/*     */     } 
/* 101 */     Integer additionalTradeCost = (Integer)result.remove(DataComponents.ADDITIONAL_TRADE_COST);
/* 102 */     if (additionalTradeCost != null)
/* 103 */       additionalCost += additionalTradeCost.intValue(); 
/* 105 */     if (this.doubleTradePriceEnchantments.isPresent()) {
/* 106 */       HolderSet<Enchantment> enchantments = this.doubleTradePriceEnchantments.get();
/* 107 */       ItemEnchantments itemEnchantments = (ItemEnchantments)result.get(DataComponents.STORED_ENCHANTMENTS);
/* 108 */       Objects.requireNonNull(enchantments);
/* 108 */       if (itemEnchantments != null && itemEnchantments.keySet().stream().anyMatch(enchantments::contains))
/* 109 */         additionalCost *= 2; 
/*     */     } 
/* 112 */     ItemCost itemCost = this.wants.toItemCost(lootContext, additionalCost);
/* 113 */     if (itemCost.count() < 1)
/* 114 */       return null; 
/* 116 */     Optional<ItemCost> additionalItemCost = this.additionalWants.map(tradeCost -> tradeCost.toItemCost(lootContext, 0));
/* 117 */     if (additionalItemCost.isPresent() && ((ItemCost)additionalItemCost.get()).count() < 1)
/* 118 */       return null; 
/* 120 */     return new MerchantOffer(itemCost, additionalItemCost, result, 
/*     */         
/* 124 */         Math.max(this.maxUses.getInt(lootContext), 1), 
/* 125 */         Math.max(this.xp.getInt(lootContext), 0), 
/* 126 */         Math.max(this.reputationDiscount.getFloat(lootContext), 0.0F));
/*     */   }
/*     */ }


/* Location:              C:\Users\karol\AppData\Roaming\ModrinthApp\meta\versions\26.2-0.19.5\26.2-0.19.5.jar!\net\minecraft\world\item\trading\VillagerTrade.class
 * Java compiler version: 25 (69.0)
 * JD-Core Version:       1.1.3
 */