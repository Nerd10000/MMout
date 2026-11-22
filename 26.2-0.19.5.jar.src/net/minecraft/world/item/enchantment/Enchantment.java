/*     */ package net.minecraft.world.item.enchantment;
/*     */ 
/*     */ import com.google.common.collect.Maps;
/*     */ import com.mojang.datafixers.kinds.App;
/*     */ import com.mojang.datafixers.kinds.Applicative;
/*     */ import com.mojang.datafixers.util.Function4;
/*     */ import com.mojang.datafixers.util.Function8;
/*     */ import com.mojang.serialization.Codec;
/*     */ import com.mojang.serialization.MapCodec;
/*     */ import com.mojang.serialization.codecs.RecordCodecBuilder;
/*     */ import it.unimi.dsi.fastutil.objects.ObjectArraySet;
/*     */ import java.util.ArrayList;
/*     */ import java.util.HashMap;
/*     */ import java.util.List;
/*     */ import java.util.Map;
/*     */ import java.util.Optional;
/*     */ import java.util.Set;
/*     */ import java.util.function.BiFunction;
/*     */ import net.minecraft.ChatFormatting;
/*     */ import net.minecraft.core.Holder;
/*     */ import net.minecraft.core.HolderSet;
/*     */ import net.minecraft.core.RegistryCodecs;
/*     */ import net.minecraft.core.component.DataComponentMap;
/*     */ import net.minecraft.core.component.DataComponentType;
/*     */ import net.minecraft.core.registries.Registries;
/*     */ import net.minecraft.network.RegistryFriendlyByteBuf;
/*     */ import net.minecraft.network.chat.CommonComponents;
/*     */ import net.minecraft.network.chat.Component;
/*     */ import net.minecraft.network.chat.ComponentSerialization;
/*     */ import net.minecraft.network.chat.ComponentUtils;
/*     */ import net.minecraft.network.chat.MutableComponent;
/*     */ import net.minecraft.network.chat.Style;
/*     */ import net.minecraft.network.codec.ByteBufCodecs;
/*     */ import net.minecraft.network.codec.StreamCodec;
/*     */ import net.minecraft.resources.Identifier;
/*     */ import net.minecraft.resources.RegistryFixedCodec;
/*     */ import net.minecraft.server.level.ServerLevel;
/*     */ import net.minecraft.tags.EnchantmentTags;
/*     */ import net.minecraft.util.ExtraCodecs;
/*     */ import net.minecraft.util.RandomSource;
/*     */ import net.minecraft.util.Unit;
/*     */ import net.minecraft.util.Util;
/*     */ import net.minecraft.world.damagesource.DamageSource;
/*     */ import net.minecraft.world.entity.Entity;
/*     */ import net.minecraft.world.entity.EquipmentSlot;
/*     */ import net.minecraft.world.entity.EquipmentSlotGroup;
/*     */ import net.minecraft.world.entity.LivingEntity;
/*     */ import net.minecraft.world.item.Item;
/*     */ import net.minecraft.world.item.ItemInstance;
/*     */ import net.minecraft.world.item.ItemStack;
/*     */ import net.minecraft.world.item.enchantment.effects.DamageImmunity;
/*     */ import net.minecraft.world.item.enchantment.effects.EnchantmentAttributeEffect;
/*     */ import net.minecraft.world.item.enchantment.effects.EnchantmentEntityEffect;
/*     */ import net.minecraft.world.item.enchantment.effects.EnchantmentLocationBasedEffect;
/*     */ import net.minecraft.world.item.enchantment.effects.EnchantmentValueEffect;
/*     */ import net.minecraft.world.level.block.state.BlockState;
/*     */ import net.minecraft.world.level.storage.loot.LootContext;
/*     */ import net.minecraft.world.level.storage.loot.LootParams;
/*     */ import net.minecraft.world.level.storage.loot.parameters.LootContextParamSets;
/*     */ import net.minecraft.world.level.storage.loot.parameters.LootContextParams;
/*     */ import net.minecraft.world.level.storage.loot.predicates.LootItemCondition;
/*     */ import net.minecraft.world.phys.Vec3;
/*     */ import org.apache.commons.lang3.mutable.MutableFloat;
/*     */ 
/*     */ public final class Enchantment extends Record {
/*     */   private final Component description;
/*     */   
/*     */   private final EnchantmentDefinition definition;
/*     */   
/*     */   private final HolderSet<Enchantment> exclusiveSet;
/*     */   
/*     */   private final DataComponentMap effects;
/*     */   
/*     */   public static final int MAX_LEVEL = 255;
/*     */   
/*     */   public static final Codec<Enchantment> DIRECT_CODEC;
/*     */   
/*     */   public Enchantment(Component description, EnchantmentDefinition definition, HolderSet<Enchantment> exclusiveSet, DataComponentMap effects) {
/*  61 */     this.description = description;
/*  61 */     this.definition = definition;
/*  61 */     this.exclusiveSet = exclusiveSet;
/*  61 */     this.effects = effects;
/*     */   }
/*     */   
/*     */   public final int hashCode() {
/*     */     // Byte code:
/*     */     //   0: aload_0
/*     */     //   1: <illegal opcode> hashCode : (Lnet/minecraft/world/item/enchantment/Enchantment;)I
/*     */     //   6: ireturn
/*     */     // Line number table:
/*     */     //   Java source line number -> byte code offset
/*     */     //   #61	-> 0
/*     */     // Local variable table:
/*     */     //   start	length	slot	name	descriptor
/*     */     //   0	7	0	this	Lnet/minecraft/world/item/enchantment/Enchantment;
/*     */   }
/*     */   
/*     */   public final boolean equals(Object o) {
/*     */     // Byte code:
/*     */     //   0: aload_0
/*     */     //   1: aload_1
/*     */     //   2: <illegal opcode> equals : (Lnet/minecraft/world/item/enchantment/Enchantment;Ljava/lang/Object;)Z
/*     */     //   7: ireturn
/*     */     // Line number table:
/*     */     //   Java source line number -> byte code offset
/*     */     //   #61	-> 0
/*     */     // Local variable table:
/*     */     //   start	length	slot	name	descriptor
/*     */     //   0	8	0	this	Lnet/minecraft/world/item/enchantment/Enchantment;
/*     */     //   0	8	1	o	Ljava/lang/Object;
/*     */   }
/*     */   
/*     */   public Component description() {
/*  61 */     return this.description;
/*     */   }
/*     */   
/*     */   public EnchantmentDefinition definition() {
/*  61 */     return this.definition;
/*     */   }
/*     */   
/*     */   public HolderSet<Enchantment> exclusiveSet() {
/*  61 */     return this.exclusiveSet;
/*     */   }
/*     */   
/*     */   public DataComponentMap effects() {
/*  61 */     return this.effects;
/*     */   }
/*     */   
/*     */   public static final class Cost extends Record {
/*     */     private final int base;
/*     */     
/*     */     private final int perLevelAboveFirst;
/*     */     
/*     */     public static final Codec<Cost> CODEC;
/*     */     
/*     */     public Cost(int base, int perLevelAboveFirst) {
/*  69 */       this.base = base;
/*  69 */       this.perLevelAboveFirst = perLevelAboveFirst;
/*     */     }
/*     */     
/*     */     public final String toString() {
/*     */       // Byte code:
/*     */       //   0: aload_0
/*     */       //   1: <illegal opcode> toString : (Lnet/minecraft/world/item/enchantment/Enchantment$Cost;)Ljava/lang/String;
/*     */       //   6: areturn
/*     */       // Line number table:
/*     */       //   Java source line number -> byte code offset
/*     */       //   #69	-> 0
/*     */       // Local variable table:
/*     */       //   start	length	slot	name	descriptor
/*     */       //   0	7	0	this	Lnet/minecraft/world/item/enchantment/Enchantment$Cost;
/*     */     }
/*     */     
/*     */     public final int hashCode() {
/*     */       // Byte code:
/*     */       //   0: aload_0
/*     */       //   1: <illegal opcode> hashCode : (Lnet/minecraft/world/item/enchantment/Enchantment$Cost;)I
/*     */       //   6: ireturn
/*     */       // Line number table:
/*     */       //   Java source line number -> byte code offset
/*     */       //   #69	-> 0
/*     */       // Local variable table:
/*     */       //   start	length	slot	name	descriptor
/*     */       //   0	7	0	this	Lnet/minecraft/world/item/enchantment/Enchantment$Cost;
/*     */     }
/*     */     
/*     */     public final boolean equals(Object o) {
/*     */       // Byte code:
/*     */       //   0: aload_0
/*     */       //   1: aload_1
/*     */       //   2: <illegal opcode> equals : (Lnet/minecraft/world/item/enchantment/Enchantment$Cost;Ljava/lang/Object;)Z
/*     */       //   7: ireturn
/*     */       // Line number table:
/*     */       //   Java source line number -> byte code offset
/*     */       //   #69	-> 0
/*     */       // Local variable table:
/*     */       //   start	length	slot	name	descriptor
/*     */       //   0	8	0	this	Lnet/minecraft/world/item/enchantment/Enchantment$Cost;
/*     */       //   0	8	1	o	Ljava/lang/Object;
/*     */     }
/*     */     
/*     */     public int base() {
/*  69 */       return this.base;
/*     */     }
/*     */     
/*     */     public int perLevelAboveFirst() {
/*  69 */       return this.perLevelAboveFirst;
/*     */     }
/*     */     
/*     */     static {
/*  70 */       CODEC = RecordCodecBuilder.create(i -> i.group((App)Codec.INT.fieldOf("base").forGetter(Cost::base), (App)Codec.INT.fieldOf("per_level_above_first").forGetter(Cost::perLevelAboveFirst)).apply((Applicative)i, Cost::new));
/*     */     }
/*     */     
/*     */     public int calculate(int level) {
/*  76 */       return this.base + this.perLevelAboveFirst * (level - 1);
/*     */     }
/*     */   }
/*     */   
/*     */   public static Cost constantCost(int base) {
/*  81 */     return new Cost(base, 0);
/*     */   }
/*     */   
/*     */   public static Cost dynamicCost(int base, int perLevel) {
/*  85 */     return new Cost(base, perLevel);
/*     */   }
/*     */   
/*     */   public static final class EnchantmentDefinition extends Record {
/*     */     private final HolderSet<Item> supportedItems;
/*     */     
/*     */     private final Optional<HolderSet<Item>> primaryItems;
/*     */     
/*     */     private final int weight;
/*     */     
/*     */     private final int maxLevel;
/*     */     
/*     */     private final Enchantment.Cost minCost;
/*     */     
/*     */     private final Enchantment.Cost maxCost;
/*     */     
/*     */     private final int anvilCost;
/*     */     
/*     */     private final List<EquipmentSlotGroup> slots;
/*     */     
/*     */     public static final MapCodec<EnchantmentDefinition> CODEC;
/*     */     
/*     */     public EnchantmentDefinition(HolderSet<Item> supportedItems, Optional<HolderSet<Item>> primaryItems, int weight, int maxLevel, Enchantment.Cost minCost, Enchantment.Cost maxCost, int anvilCost, List<EquipmentSlotGroup> slots) {
/*  88 */       this.supportedItems = supportedItems;
/*  88 */       this.primaryItems = primaryItems;
/*  88 */       this.weight = weight;
/*  88 */       this.maxLevel = maxLevel;
/*  88 */       this.minCost = minCost;
/*  88 */       this.maxCost = maxCost;
/*  88 */       this.anvilCost = anvilCost;
/*  88 */       this.slots = slots;
/*     */     }
/*     */     
/*     */     public final String toString() {
/*     */       // Byte code:
/*     */       //   0: aload_0
/*     */       //   1: <illegal opcode> toString : (Lnet/minecraft/world/item/enchantment/Enchantment$EnchantmentDefinition;)Ljava/lang/String;
/*     */       //   6: areturn
/*     */       // Line number table:
/*     */       //   Java source line number -> byte code offset
/*     */       //   #88	-> 0
/*     */       // Local variable table:
/*     */       //   start	length	slot	name	descriptor
/*     */       //   0	7	0	this	Lnet/minecraft/world/item/enchantment/Enchantment$EnchantmentDefinition;
/*     */     }
/*     */     
/*     */     public final int hashCode() {
/*     */       // Byte code:
/*     */       //   0: aload_0
/*     */       //   1: <illegal opcode> hashCode : (Lnet/minecraft/world/item/enchantment/Enchantment$EnchantmentDefinition;)I
/*     */       //   6: ireturn
/*     */       // Line number table:
/*     */       //   Java source line number -> byte code offset
/*     */       //   #88	-> 0
/*     */       // Local variable table:
/*     */       //   start	length	slot	name	descriptor
/*     */       //   0	7	0	this	Lnet/minecraft/world/item/enchantment/Enchantment$EnchantmentDefinition;
/*     */     }
/*     */     
/*     */     public final boolean equals(Object o) {
/*     */       // Byte code:
/*     */       //   0: aload_0
/*     */       //   1: aload_1
/*     */       //   2: <illegal opcode> equals : (Lnet/minecraft/world/item/enchantment/Enchantment$EnchantmentDefinition;Ljava/lang/Object;)Z
/*     */       //   7: ireturn
/*     */       // Line number table:
/*     */       //   Java source line number -> byte code offset
/*     */       //   #88	-> 0
/*     */       // Local variable table:
/*     */       //   start	length	slot	name	descriptor
/*     */       //   0	8	0	this	Lnet/minecraft/world/item/enchantment/Enchantment$EnchantmentDefinition;
/*     */       //   0	8	1	o	Ljava/lang/Object;
/*     */     }
/*     */     
/*     */     public HolderSet<Item> supportedItems() {
/*  88 */       return this.supportedItems;
/*     */     }
/*     */     
/*     */     public Optional<HolderSet<Item>> primaryItems() {
/*  88 */       return this.primaryItems;
/*     */     }
/*     */     
/*     */     public int weight() {
/*  88 */       return this.weight;
/*     */     }
/*     */     
/*     */     public int maxLevel() {
/*  88 */       return this.maxLevel;
/*     */     }
/*     */     
/*     */     public Enchantment.Cost minCost() {
/*  88 */       return this.minCost;
/*     */     }
/*     */     
/*     */     public Enchantment.Cost maxCost() {
/*  88 */       return this.maxCost;
/*     */     }
/*     */     
/*     */     public int anvilCost() {
/*  88 */       return this.anvilCost;
/*     */     }
/*     */     
/*     */     public List<EquipmentSlotGroup> slots() {
/*  88 */       return this.slots;
/*     */     }
/*     */     
/*     */     static {
/*  98 */       CODEC = RecordCodecBuilder.mapCodec(i -> i.group((App)RegistryCodecs.homogeneousList(Registries.ITEM).fieldOf("supported_items").forGetter(EnchantmentDefinition::supportedItems), (App)RegistryCodecs.homogeneousList(Registries.ITEM).optionalFieldOf("primary_items").forGetter(EnchantmentDefinition::primaryItems), (App)ExtraCodecs.intRange(1, 1024).fieldOf("weight").forGetter(EnchantmentDefinition::weight), (App)ExtraCodecs.intRange(1, 255).fieldOf("max_level").forGetter(EnchantmentDefinition::maxLevel), (App)Enchantment.Cost.CODEC.fieldOf("min_cost").forGetter(EnchantmentDefinition::minCost), (App)Enchantment.Cost.CODEC.fieldOf("max_cost").forGetter(EnchantmentDefinition::maxCost), (App)ExtraCodecs.NON_NEGATIVE_INT.fieldOf("anvil_cost").forGetter(EnchantmentDefinition::anvilCost), (App)EquipmentSlotGroup.CODEC.listOf().fieldOf("slots").forGetter(EnchantmentDefinition::slots)).apply((Applicative)i, EnchantmentDefinition::new));
/*     */     }
/*     */   }
/*     */   
/*     */   public static EnchantmentDefinition definition(HolderSet<Item> supportedItems, HolderSet<Item> primaryItems, int weight, int maxLevel, Cost minCost, Cost maxCost, int anvilCost, EquipmentSlotGroup... slots) {
/* 111 */     return new EnchantmentDefinition(supportedItems, Optional.of(primaryItems), weight, maxLevel, minCost, maxCost, anvilCost, List.of(slots));
/*     */   }
/*     */   
/*     */   public static EnchantmentDefinition definition(HolderSet<Item> supportedItems, int weight, int maxLevel, Cost minCost, Cost maxCost, int anvilCost, EquipmentSlotGroup... slots) {
/* 115 */     return new EnchantmentDefinition(supportedItems, Optional.empty(), weight, maxLevel, minCost, maxCost, anvilCost, List.of(slots));
/*     */   }
/*     */   
/*     */   static {
/* 118 */     DIRECT_CODEC = RecordCodecBuilder.create(i -> i.group((App)ComponentSerialization.CODEC.fieldOf("description").forGetter(Enchantment::description), (App)EnchantmentDefinition.CODEC.forGetter(Enchantment::definition), (App)RegistryCodecs.homogeneousList(Registries.ENCHANTMENT).optionalFieldOf("exclusive_set", HolderSet.empty()).forGetter(Enchantment::exclusiveSet), (App)EnchantmentEffectComponents.CODEC.optionalFieldOf("effects", DataComponentMap.EMPTY).forGetter(Enchantment::effects)).apply((Applicative)i, Enchantment::new));
/*     */   }
/*     */   
/* 125 */   public static final Codec<Holder<Enchantment>> CODEC = (Codec<Holder<Enchantment>>)RegistryFixedCodec.create(Registries.ENCHANTMENT);
/*     */   
/* 126 */   public static final StreamCodec<RegistryFriendlyByteBuf, Holder<Enchantment>> STREAM_CODEC = ByteBufCodecs.holderRegistry(Registries.ENCHANTMENT);
/*     */   
/*     */   public Map<EquipmentSlot, ItemStack> getSlotItems(LivingEntity entity) {
/* 129 */     Map<EquipmentSlot, ItemStack> itemStacks = Maps.newEnumMap(EquipmentSlot.class);
/* 130 */     for (EquipmentSlot slot : EquipmentSlot.VALUES) {
/* 131 */       if (matchingSlot(slot)) {
/* 132 */         ItemStack itemStack = entity.getItemBySlot(slot);
/* 133 */         if (!itemStack.isEmpty())
/* 134 */           itemStacks.put(slot, itemStack); 
/*     */       } 
/*     */     } 
/* 138 */     return itemStacks;
/*     */   }
/*     */   
/*     */   public HolderSet<Item> getSupportedItems() {
/* 142 */     return this.definition.supportedItems();
/*     */   }
/*     */   
/*     */   public boolean matchingSlot(EquipmentSlot slot) {
/* 146 */     return this.definition.slots().stream().anyMatch(group -> group.test(slot));
/*     */   }
/*     */   
/*     */   public boolean isPrimaryItem(ItemStack item) {
/* 150 */     return (isSupportedItem(item) && (this.definition.primaryItems.isEmpty() || item.is(this.definition.primaryItems.get())));
/*     */   }
/*     */   
/*     */   public boolean isSupportedItem(ItemStack item) {
/* 154 */     return item.is(this.definition.supportedItems);
/*     */   }
/*     */   
/*     */   public int getWeight() {
/* 158 */     return this.definition.weight();
/*     */   }
/*     */   
/*     */   public int getAnvilCost() {
/* 162 */     return this.definition.anvilCost();
/*     */   }
/*     */   
/*     */   public int getMinLevel() {
/* 166 */     return 1;
/*     */   }
/*     */   
/*     */   public int getMaxLevel() {
/* 170 */     return this.definition.maxLevel();
/*     */   }
/*     */   
/*     */   public int getMinCost(int level) {
/* 174 */     return this.definition.minCost().calculate(level);
/*     */   }
/*     */   
/*     */   public int getMaxCost(int level) {
/* 178 */     return this.definition.maxCost().calculate(level);
/*     */   }
/*     */   
/*     */   public String toString() {
/* 184 */     return "Enchantment " + this.description.getString();
/*     */   }
/*     */   
/*     */   public static boolean areCompatible(Holder<Enchantment> enchantment, Holder<Enchantment> other) {
/* 188 */     return (!enchantment.equals(other) && !((Enchantment)enchantment.value()).exclusiveSet.contains(other) && !((Enchantment)other.value()).exclusiveSet.contains(enchantment));
/*     */   }
/*     */   
/*     */   public static Component getFullname(Holder<Enchantment> enchantment, int level) {
/* 192 */     MutableComponent result = ((Enchantment)enchantment.value()).description.copy();
/* 193 */     if (enchantment.is(EnchantmentTags.CURSE)) {
/* 194 */       result = ComponentUtils.mergeStyles(result, Style.EMPTY.withColor(ChatFormatting.RED));
/*     */     } else {
/* 196 */       result = ComponentUtils.mergeStyles(result, Style.EMPTY.withColor(ChatFormatting.GRAY));
/*     */     } 
/* 198 */     if (level != 1 || ((Enchantment)enchantment.value()).getMaxLevel() != 1)
/* 199 */       result.append(CommonComponents.SPACE).append((Component)Component.translatable("enchantment.level." + level)); 
/* 201 */     return (Component)result;
/*     */   }
/*     */   
/*     */   public boolean canEnchant(ItemStack itemStack) {
/* 205 */     return this.definition.supportedItems().contains(itemStack.typeHolder());
/*     */   }
/*     */   
/*     */   public <T> List<T> getEffects(DataComponentType<List<T>> type) {
/* 209 */     return (List<T>)this.effects.getOrDefault(type, List.of());
/*     */   }
/*     */   
/*     */   public boolean isImmuneToDamage(ServerLevel serverLevel, int enchantmentLevel, Entity victim, DamageSource source) {
/* 213 */     LootContext context = damageContext(serverLevel, enchantmentLevel, victim, source);
/* 214 */     for (ConditionalEffect<DamageImmunity> filteredEffect : getEffects(EnchantmentEffectComponents.DAMAGE_IMMUNITY)) {
/* 215 */       if (filteredEffect.matches(context))
/* 216 */         return true; 
/*     */     } 
/* 219 */     return false;
/*     */   }
/*     */   
/*     */   public void modifyDamageProtection(ServerLevel serverLevel, int enchantmentLevel, ItemStack item, Entity victim, DamageSource source, MutableFloat protection) {
/* 223 */     applyEffects(
/* 224 */         getEffects(EnchantmentEffectComponents.DAMAGE_PROTECTION), 
/* 225 */         damageContext(serverLevel, enchantmentLevel, victim, source), protection, (e, v) -> e.process(enchantmentLevel, victim.getRandom(), v));
/*     */   }
/*     */   
/*     */   public void modifyDurabilityChange(ServerLevel serverLevel, int enchantmentLevel, ItemStack itemStack, MutableFloat change) {
/* 232 */     modifyItemFilteredCount(EnchantmentEffectComponents.ITEM_DAMAGE, serverLevel, enchantmentLevel, (ItemInstance)itemStack, change);
/*     */   }
/*     */   
/*     */   public void modifyAmmoCount(ServerLevel serverLevel, int enchantmentLevel, ItemStack itemStack, MutableFloat change) {
/* 236 */     modifyItemFilteredCount(EnchantmentEffectComponents.AMMO_USE, serverLevel, enchantmentLevel, (ItemInstance)itemStack, change);
/*     */   }
/*     */   
/*     */   public void modifyPiercingCount(ServerLevel serverLevel, int enchantmentLevel, ItemStack itemStack, MutableFloat count) {
/* 240 */     modifyItemFilteredCount(EnchantmentEffectComponents.PROJECTILE_PIERCING, serverLevel, enchantmentLevel, (ItemInstance)itemStack, count);
/*     */   }
/*     */   
/*     */   public void modifyBlockExperience(ServerLevel serverLevel, int enchantmentLevel, ItemStack itemStack, MutableFloat count) {
/* 244 */     modifyItemFilteredCount(EnchantmentEffectComponents.BLOCK_EXPERIENCE, serverLevel, enchantmentLevel, (ItemInstance)itemStack, count);
/*     */   }
/*     */   
/*     */   public void modifyMobExperience(ServerLevel serverLevel, int enchantmentLevel, ItemStack itemStack, Entity killer, MutableFloat experience) {
/* 248 */     modifyEntityFilteredValue(EnchantmentEffectComponents.MOB_EXPERIENCE, serverLevel, enchantmentLevel, itemStack, killer, experience);
/*     */   }
/*     */   
/*     */   public void modifyDurabilityToRepairFromXp(ServerLevel serverLevel, int enchantmentLevel, ItemStack itemStack, MutableFloat change) {
/* 252 */     modifyItemFilteredCount(EnchantmentEffectComponents.REPAIR_WITH_XP, serverLevel, enchantmentLevel, (ItemInstance)itemStack, change);
/*     */   }
/*     */   
/*     */   public void modifyTridentReturnToOwnerAcceleration(ServerLevel serverLevel, int enchantmentLevel, ItemStack itemStack, Entity trident, MutableFloat count) {
/* 256 */     modifyEntityFilteredValue(EnchantmentEffectComponents.TRIDENT_RETURN_ACCELERATION, serverLevel, enchantmentLevel, itemStack, trident, count);
/*     */   }
/*     */   
/*     */   public void modifyTridentSpinAttackStrength(RandomSource random, int enchantmentLevel, MutableFloat strength) {
/* 260 */     modifyUnfilteredValue(EnchantmentEffectComponents.TRIDENT_SPIN_ATTACK_STRENGTH, random, enchantmentLevel, strength);
/*     */   }
/*     */   
/*     */   public void modifyFishingTimeReduction(ServerLevel serverLevel, int enchantmentLevel, ItemStack itemStack, Entity fisher, MutableFloat timeReduction) {
/* 264 */     modifyEntityFilteredValue(EnchantmentEffectComponents.FISHING_TIME_REDUCTION, serverLevel, enchantmentLevel, itemStack, fisher, timeReduction);
/*     */   }
/*     */   
/*     */   public void modifyFishingLuckBonus(ServerLevel serverLevel, int enchantmentLevel, ItemStack itemStack, Entity fisher, MutableFloat luck) {
/* 268 */     modifyEntityFilteredValue(EnchantmentEffectComponents.FISHING_LUCK_BONUS, serverLevel, enchantmentLevel, itemStack, fisher, luck);
/*     */   }
/*     */   
/*     */   public void modifyDamage(ServerLevel serverLevel, int enchantmentLevel, ItemStack itemStack, Entity victim, DamageSource damageSource, MutableFloat amount) {
/* 272 */     modifyDamageFilteredValue(EnchantmentEffectComponents.DAMAGE, serverLevel, enchantmentLevel, itemStack, victim, damageSource, amount);
/*     */   }
/*     */   
/*     */   public void modifyFallBasedDamage(ServerLevel serverLevel, int enchantmentLevel, ItemStack itemStack, Entity victim, DamageSource damageSource, MutableFloat amount) {
/* 276 */     modifyDamageFilteredValue(EnchantmentEffectComponents.SMASH_DAMAGE_PER_FALLEN_BLOCK, serverLevel, enchantmentLevel, itemStack, victim, damageSource, amount);
/*     */   }
/*     */   
/*     */   public void modifyKnockback(ServerLevel serverLevel, int enchantmentLevel, ItemStack itemStack, Entity victim, DamageSource damageSource, MutableFloat amount) {
/* 280 */     modifyDamageFilteredValue(EnchantmentEffectComponents.KNOCKBACK, serverLevel, enchantmentLevel, itemStack, victim, damageSource, amount);
/*     */   }
/*     */   
/*     */   public void modifyArmorEffectivness(ServerLevel serverLevel, int enchantmentLevel, ItemStack itemStack, Entity victim, DamageSource damageSource, MutableFloat amount) {
/* 284 */     modifyDamageFilteredValue(EnchantmentEffectComponents.ARMOR_EFFECTIVENESS, serverLevel, enchantmentLevel, itemStack, victim, damageSource, amount);
/*     */   }
/*     */   
/*     */   public void doPostAttack(ServerLevel serverLevel, int enchantmentLevel, EnchantedItemInUse item, EnchantmentTarget forTarget, Entity victim, DamageSource damageSource) {
/* 288 */     for (TargetedConditionalEffect<EnchantmentEntityEffect> effect : getEffects(EnchantmentEffectComponents.POST_ATTACK)) {
/* 289 */       if (forTarget == effect.enchanted())
/* 290 */         doPostAttack(effect, serverLevel, enchantmentLevel, item, victim, damageSource); 
/*     */     } 
/*     */   }
/*     */   
/*     */   public static void doPostAttack(TargetedConditionalEffect<EnchantmentEntityEffect> effect, ServerLevel serverLevel, int enchantmentLevel, EnchantedItemInUse item, Entity victim, DamageSource damageSource) {
/* 296 */     if (effect.matches(damageContext(serverLevel, enchantmentLevel, victim, damageSource))) {
/* 297 */       switch (effect.affected()) {
/*     */         default:
/* 297 */           throw new MatchException(null, null);
/*     */         case ATTACKER:
/*     */         
/*     */         case DAMAGING_ENTITY:
/*     */         
/*     */         case VICTIM:
/*     */           break;
/*     */       } 
/* 297 */       Entity target = 
/*     */         
/* 300 */         victim;
/* 302 */       if (target != null)
/* 303 */         ((EnchantmentEntityEffect)effect.effect()).apply(serverLevel, enchantmentLevel, item, target, target.position()); 
/*     */     } 
/*     */   }
/*     */   
/*     */   public void doPostPiercingAttack(ServerLevel serverLevel, int enchantmentLevel, EnchantedItemInUse item, Entity user) {
/* 309 */     applyEffects(
/* 310 */         getEffects(EnchantmentEffectComponents.POST_PIERCING_ATTACK), 
/* 311 */         entityContext(serverLevel, enchantmentLevel, user, user.position()), e -> e.apply(serverLevel, enchantmentLevel, item, user, user.position()));
/*     */   }
/*     */   
/*     */   public void modifyProjectileCount(ServerLevel serverLevel, int enchantmentLevel, ItemStack weapon, Entity shooter, MutableFloat count) {
/* 317 */     modifyEntityFilteredValue(EnchantmentEffectComponents.PROJECTILE_COUNT, serverLevel, enchantmentLevel, weapon, shooter, count);
/*     */   }
/*     */   
/*     */   public void modifyProjectileSpread(ServerLevel serverLevel, int enchantmentLevel, ItemStack weapon, Entity shooter, MutableFloat angle) {
/* 321 */     modifyEntityFilteredValue(EnchantmentEffectComponents.PROJECTILE_SPREAD, serverLevel, enchantmentLevel, weapon, shooter, angle);
/*     */   }
/*     */   
/*     */   public void modifyCrossbowChargeTime(RandomSource random, int enchantmentLevel, MutableFloat time) {
/* 325 */     modifyUnfilteredValue(EnchantmentEffectComponents.CROSSBOW_CHARGE_TIME, random, enchantmentLevel, time);
/*     */   }
/*     */   
/*     */   public void modifyUnfilteredValue(DataComponentType<EnchantmentValueEffect> component, RandomSource random, int enchantmentLevel, MutableFloat value) {
/* 329 */     EnchantmentValueEffect effect = (EnchantmentValueEffect)this.effects.get(component);
/* 330 */     if (effect != null)
/* 331 */       value.setValue(effect.process(enchantmentLevel, random, value.floatValue())); 
/*     */   }
/*     */   
/*     */   public void tick(ServerLevel serverLevel, int enchantmentLevel, EnchantedItemInUse item, Entity entity) {
/* 336 */     applyEffects(
/* 337 */         getEffects(EnchantmentEffectComponents.TICK), 
/* 338 */         entityContext(serverLevel, enchantmentLevel, entity, entity.position()), e -> e.apply(serverLevel, enchantmentLevel, item, entity, entity.position()));
/*     */   }
/*     */   
/*     */   public void onProjectileSpawned(ServerLevel serverLevel, int enchantmentLevel, EnchantedItemInUse weapon, Entity projectile) {
/* 344 */     applyEffects(
/* 345 */         getEffects(EnchantmentEffectComponents.PROJECTILE_SPAWNED), 
/* 346 */         entityContext(serverLevel, enchantmentLevel, projectile, projectile.position()), e -> e.apply(serverLevel, enchantmentLevel, weapon, projectile, projectile.position()));
/*     */   }
/*     */   
/*     */   public void onHitBlock(ServerLevel serverLevel, int enchantmentLevel, EnchantedItemInUse weapon, Entity projectile, Vec3 position, BlockState hitBlock) {
/* 352 */     applyEffects(
/* 353 */         getEffects(EnchantmentEffectComponents.HIT_BLOCK), 
/* 354 */         blockHitContext(serverLevel, enchantmentLevel, projectile, position, hitBlock), e -> e.apply(serverLevel, enchantmentLevel, weapon, projectile, position));
/*     */   }
/*     */   
/*     */   private void modifyItemFilteredCount(DataComponentType<List<ConditionalEffect<EnchantmentValueEffect>>> effectType, ServerLevel serverLevel, int enchantmentLevel, ItemInstance itemStack, MutableFloat value) {
/* 360 */     applyEffects(
/* 361 */         getEffects(effectType), 
/* 362 */         itemContext(serverLevel, enchantmentLevel, itemStack), value, (e, v) -> e.process(enchantmentLevel, serverLevel.getRandom(), v));
/*     */   }
/*     */   
/*     */   private void modifyEntityFilteredValue(DataComponentType<List<ConditionalEffect<EnchantmentValueEffect>>> effectType, ServerLevel serverLevel, int enchantmentLevel, ItemStack itemStack, Entity entity, MutableFloat value) {
/* 369 */     applyEffects(
/* 370 */         getEffects(effectType), 
/* 371 */         entityContext(serverLevel, enchantmentLevel, entity, entity.position()), value, (e, v) -> e.process(enchantmentLevel, entity.getRandom(), v));
/*     */   }
/*     */   
/*     */   private void modifyDamageFilteredValue(DataComponentType<List<ConditionalEffect<EnchantmentValueEffect>>> effectType, ServerLevel serverLevel, int enchantmentLevel, ItemStack itemStack, Entity victim, DamageSource damageSource, MutableFloat value) {
/* 378 */     applyEffects(
/* 379 */         getEffects(effectType), 
/* 380 */         damageContext(serverLevel, enchantmentLevel, victim, damageSource), value, (e, v) -> e.process(enchantmentLevel, victim.getRandom(), v));
/*     */   }
/*     */   
/*     */   public static LootContext damageContext(ServerLevel serverLevel, int enchantmentLevel, Entity victim, DamageSource source) {
/* 394 */     LootParams params = (new LootParams.Builder(serverLevel)).withParameter(LootContextParams.THIS_ENTITY, victim).withParameter(LootContextParams.ENCHANTMENT_LEVEL, Integer.valueOf(enchantmentLevel)).withParameter(LootContextParams.ORIGIN, victim.position()).withParameter(LootContextParams.DAMAGE_SOURCE, source).withOptionalParameter(LootContextParams.ATTACKING_ENTITY, source.getEntity()).withOptionalParameter(LootContextParams.DIRECT_ATTACKING_ENTITY, source.getDirectEntity()).create(LootContextParamSets.ENCHANTED_DAMAGE);
/* 395 */     return (new LootContext.Builder(params)).create(Optional.empty());
/*     */   }
/*     */   
/*     */   private static LootContext itemContext(ServerLevel serverLevel, int enchantmentLevel, ItemInstance itemStack) {
/* 402 */     LootParams params = (new LootParams.Builder(serverLevel)).withParameter(LootContextParams.TOOL, itemStack).withParameter(LootContextParams.ENCHANTMENT_LEVEL, Integer.valueOf(enchantmentLevel)).create(LootContextParamSets.ENCHANTED_ITEM);
/* 403 */     return (new LootContext.Builder(params)).create(Optional.empty());
/*     */   }
/*     */   
/*     */   private static LootContext locationContext(ServerLevel serverLevel, int enchantmentLevel, Entity entity, boolean active) {
/* 412 */     LootParams params = (new LootParams.Builder(serverLevel)).withParameter(LootContextParams.THIS_ENTITY, entity).withParameter(LootContextParams.ENCHANTMENT_LEVEL, Integer.valueOf(enchantmentLevel)).withParameter(LootContextParams.ORIGIN, entity.position()).withParameter(LootContextParams.ENCHANTMENT_ACTIVE, Boolean.valueOf(active)).create(LootContextParamSets.ENCHANTED_LOCATION);
/* 413 */     return (new LootContext.Builder(params)).create(Optional.empty());
/*     */   }
/*     */   
/*     */   private static LootContext entityContext(ServerLevel serverLevel, int enchantmentLevel, Entity entity, Vec3 position) {
/* 421 */     LootParams params = (new LootParams.Builder(serverLevel)).withParameter(LootContextParams.THIS_ENTITY, entity).withParameter(LootContextParams.ENCHANTMENT_LEVEL, Integer.valueOf(enchantmentLevel)).withParameter(LootContextParams.ORIGIN, position).create(LootContextParamSets.ENCHANTED_ENTITY);
/* 422 */     return (new LootContext.Builder(params)).create(Optional.empty());
/*     */   }
/*     */   
/*     */   private static LootContext blockHitContext(ServerLevel serverLevel, int enchantmentLevel, Entity entity, Vec3 position, BlockState hitBlock) {
/* 431 */     LootParams params = (new LootParams.Builder(serverLevel)).withParameter(LootContextParams.THIS_ENTITY, entity).withParameter(LootContextParams.ENCHANTMENT_LEVEL, Integer.valueOf(enchantmentLevel)).withParameter(LootContextParams.ORIGIN, position).withParameter(LootContextParams.BLOCK_STATE, hitBlock).create(LootContextParamSets.HIT_BLOCK);
/* 432 */     return (new LootContext.Builder(params)).create(Optional.empty());
/*     */   }
/*     */   
/*     */   private static <T> void applyEffects(List<ConditionalEffect<T>> effects, LootContext filterData, GenericAction<T> action) {
/* 436 */     for (ConditionalEffect<T> conditionalEffect : effects) {
/* 437 */       if (conditionalEffect.matches(filterData))
/* 438 */         action.apply(conditionalEffect.effect()); 
/*     */     } 
/*     */   }
/*     */   
/*     */   private static <T> void applyEffects(List<ConditionalEffect<T>> effects, LootContext filterData, MutableFloat value, FloatAction<T> action) {
/* 444 */     applyEffects(effects, filterData, action.asGeneric(value));
/*     */   }
/*     */   
/*     */   public void runLocationChangedEffects(ServerLevel serverLevel, int enchantmentLevel, EnchantedItemInUse item, LivingEntity entity) {
/*     */     ObjectArraySet<EnchantmentLocationBasedEffect> objectArraySet;
/* 448 */     EquipmentSlot slot = item.inSlot();
/* 449 */     if (slot == null)
/*     */       return; 
/* 452 */     Map<Enchantment, Set<EnchantmentLocationBasedEffect>> activeLocationDependentEffects = entity.activeLocationDependentEnchantments(slot);
/* 453 */     if (!matchingSlot(slot)) {
/* 454 */       Set<EnchantmentLocationBasedEffect> set = activeLocationDependentEffects.remove(this);
/* 455 */       if (set != null)
/* 456 */         set.forEach(effect -> effect.onDeactivated(item, (Entity)entity, entity.position(), enchantmentLevel)); 
/*     */       return;
/*     */     } 
/* 461 */     Set<EnchantmentLocationBasedEffect> activeEffects = activeLocationDependentEffects.get(this);
/* 462 */     for (ConditionalEffect<EnchantmentLocationBasedEffect> filteredEffect : getEffects(EnchantmentEffectComponents.LOCATION_CHANGED)) {
/* 463 */       EnchantmentLocationBasedEffect effect = filteredEffect.effect();
/* 464 */       boolean wasActive = (activeEffects != null && activeEffects.contains(effect));
/* 465 */       if (filteredEffect.matches(locationContext(serverLevel, enchantmentLevel, (Entity)entity, wasActive))) {
/* 466 */         if (!wasActive) {
/* 467 */           if (activeEffects == null) {
/* 468 */             objectArraySet = new ObjectArraySet();
/* 469 */             activeLocationDependentEffects.put(this, objectArraySet);
/*     */           } 
/* 471 */           objectArraySet.add(effect);
/*     */         } 
/* 473 */         effect.onChangedBlock(serverLevel, enchantmentLevel, item, (Entity)entity, entity.position(), !wasActive);
/*     */         continue;
/*     */       } 
/* 474 */       if (objectArraySet != null && objectArraySet.remove(effect))
/* 475 */         effect.onDeactivated(item, (Entity)entity, entity.position(), enchantmentLevel); 
/*     */     } 
/* 478 */     if (objectArraySet != null && objectArraySet.isEmpty())
/* 479 */       activeLocationDependentEffects.remove(this); 
/*     */   }
/*     */   
/*     */   public void stopLocationBasedEffects(int enchantmentLevel, EnchantedItemInUse item, LivingEntity entity) {
/* 484 */     EquipmentSlot slot = item.inSlot();
/* 485 */     if (slot == null)
/*     */       return; 
/* 488 */     Set<EnchantmentLocationBasedEffect> activeEffects = (Set<EnchantmentLocationBasedEffect>)entity.activeLocationDependentEnchantments(slot).remove(this);
/* 489 */     if (activeEffects == null)
/*     */       return; 
/* 493 */     for (EnchantmentLocationBasedEffect effect : activeEffects)
/* 494 */       effect.onDeactivated(item, (Entity)entity, entity.position(), enchantmentLevel); 
/*     */   }
/*     */   
/*     */   public static class Builder {
/*     */     private final Enchantment.EnchantmentDefinition definition;
/*     */     
/* 500 */     private HolderSet<Enchantment> exclusiveSet = HolderSet.empty();
/*     */     
/* 501 */     private final Map<DataComponentType<?>, List<?>> effectLists = new HashMap<>();
/*     */     
/* 502 */     private final DataComponentMap.Builder effectMapBuilder = DataComponentMap.builder();
/*     */     
/*     */     public Builder(Enchantment.EnchantmentDefinition definition) {
/* 505 */       this.definition = definition;
/*     */     }
/*     */     
/*     */     public Builder exclusiveWith(HolderSet<Enchantment> set) {
/* 509 */       this.exclusiveSet = set;
/* 510 */       return this;
/*     */     }
/*     */     
/*     */     public <E> Builder withEffect(DataComponentType<List<ConditionalEffect<E>>> type, E effect, LootItemCondition.Builder condition) {
/* 514 */       getEffectsList(type).add(new ConditionalEffect<>(effect, Optional.of(condition.build())));
/* 515 */       return this;
/*     */     }
/*     */     
/*     */     public <E> Builder withEffect(DataComponentType<List<ConditionalEffect<E>>> type, E effect) {
/* 519 */       getEffectsList(type).add(new ConditionalEffect<>(effect, Optional.empty()));
/* 520 */       return this;
/*     */     }
/*     */     
/*     */     public <E> Builder withEffect(DataComponentType<List<TargetedConditionalEffect<E>>> type, EnchantmentTarget enchanted, EnchantmentTarget affected, E effect, LootItemCondition.Builder condition) {
/* 524 */       getEffectsList(type).add(new TargetedConditionalEffect<>(enchanted, affected, effect, Optional.of(condition.build())));
/* 525 */       return this;
/*     */     }
/*     */     
/*     */     public <E> Builder withEffect(DataComponentType<List<TargetedConditionalEffect<E>>> type, EnchantmentTarget enchanted, EnchantmentTarget affected, E effect) {
/* 529 */       getEffectsList(type).add(new TargetedConditionalEffect<>(enchanted, affected, effect, Optional.empty()));
/* 530 */       return this;
/*     */     }
/*     */     
/*     */     public Builder withEffect(DataComponentType<List<EnchantmentAttributeEffect>> type, EnchantmentAttributeEffect effect) {
/* 534 */       getEffectsList(type).add(effect);
/* 535 */       return this;
/*     */     }
/*     */     
/*     */     public <E> Builder withSpecialEffect(DataComponentType<E> type, E effect) {
/* 540 */       this.effectMapBuilder.set(type, effect);
/* 541 */       return this;
/*     */     }
/*     */     
/*     */     public Builder withEffect(DataComponentType<Unit> type) {
/* 545 */       this.effectMapBuilder.set(type, Unit.INSTANCE);
/* 546 */       return this;
/*     */     }
/*     */     
/*     */     private <E> List<E> getEffectsList(DataComponentType<List<E>> type) {
/* 551 */       return (List<E>)this.effectLists.computeIfAbsent(type, k -> {
/*     */             ArrayList<E> newList = new ArrayList<>();
/*     */             this.effectMapBuilder.set(type, newList);
/*     */             return newList;
/*     */           });
/*     */     }
/*     */     
/*     */     public Enchantment build(Identifier descriptionKey) {
/* 559 */       return new Enchantment((Component)Component.translatable(Util.makeDescriptionId("enchantment", descriptionKey)), this.definition, this.exclusiveSet, this.effectMapBuilder.build());
/*     */     }
/*     */   }
/*     */   
/*     */   public static Builder enchantment(EnchantmentDefinition definition) {
/* 564 */     return new Builder(definition);
/*     */   }
/*     */   
/*     */   @FunctionalInterface
/*     */   private static interface FloatAction<T> {
/*     */     float apply(T param1T, float param1Float);
/*     */     
/*     */     default Enchantment.GenericAction<T> asGeneric(MutableFloat v) {
/* 577 */       return effect -> v.setValue(apply((T)effect, v.floatValue()));
/*     */     }
/*     */   }
/*     */   
/*     */   @FunctionalInterface
/*     */   private static interface GenericAction<T> {
/*     */     void apply(T param1T);
/*     */   }
/*     */ }


/* Location:              C:\Users\karol\AppData\Roaming\ModrinthApp\meta\versions\26.2-0.19.5\26.2-0.19.5.jar!\net\minecraft\world\item\enchantment\Enchantment.class
 * Java compiler version: 25 (69.0)
 * JD-Core Version:       1.1.3
 */