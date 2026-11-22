/*     */ package net.minecraft.world.item.enchantment;
/*     */ 
/*     */ import com.google.common.collect.Lists;
/*     */ import com.mojang.datafixers.util.Pair;
/*     */ import it.unimi.dsi.fastutil.objects.Object2IntMap;
/*     */ import java.util.ArrayList;
/*     */ import java.util.Collection;
/*     */ import java.util.List;
/*     */ import java.util.Objects;
/*     */ import java.util.Optional;
/*     */ import java.util.function.BiConsumer;
/*     */ import java.util.function.Consumer;
/*     */ import java.util.function.Predicate;
/*     */ import java.util.stream.Stream;
/*     */ import net.minecraft.core.Holder;
/*     */ import net.minecraft.core.HolderSet;
/*     */ import net.minecraft.core.RegistryAccess;
/*     */ import net.minecraft.core.component.DataComponentType;
/*     */ import net.minecraft.core.component.DataComponents;
/*     */ import net.minecraft.core.registries.Registries;
/*     */ import net.minecraft.resources.ResourceKey;
/*     */ import net.minecraft.server.level.ServerLevel;
/*     */ import net.minecraft.tags.TagKey;
/*     */ import net.minecraft.util.Mth;
/*     */ import net.minecraft.util.RandomSource;
/*     */ import net.minecraft.util.StringRepresentable;
/*     */ import net.minecraft.util.Util;
/*     */ import net.minecraft.util.random.WeightedRandom;
/*     */ import net.minecraft.world.DifficultyInstance;
/*     */ import net.minecraft.world.damagesource.DamageSource;
/*     */ import net.minecraft.world.entity.Entity;
/*     */ import net.minecraft.world.entity.EquipmentSlot;
/*     */ import net.minecraft.world.entity.EquipmentSlotGroup;
/*     */ import net.minecraft.world.entity.LivingEntity;
/*     */ import net.minecraft.world.entity.ai.attributes.Attribute;
/*     */ import net.minecraft.world.entity.ai.attributes.AttributeModifier;
/*     */ import net.minecraft.world.entity.projectile.Projectile;
/*     */ import net.minecraft.world.item.Item;
/*     */ import net.minecraft.world.item.ItemInstance;
/*     */ import net.minecraft.world.item.ItemStack;
/*     */ import net.minecraft.world.item.Items;
/*     */ import net.minecraft.world.item.enchantment.effects.EnchantmentAttributeEffect;
/*     */ import net.minecraft.world.item.enchantment.effects.EnchantmentValueEffect;
/*     */ import net.minecraft.world.item.enchantment.providers.EnchantmentProvider;
/*     */ import net.minecraft.world.level.ItemLike;
/*     */ import net.minecraft.world.level.block.state.BlockState;
/*     */ import net.minecraft.world.level.storage.loot.LootContext;
/*     */ import net.minecraft.world.phys.Vec3;
/*     */ import org.apache.commons.lang3.mutable.MutableBoolean;
/*     */ import org.apache.commons.lang3.mutable.MutableFloat;
/*     */ import org.apache.commons.lang3.mutable.MutableObject;
/*     */ 
/*     */ public class EnchantmentHelper {
/*     */   public static int getItemEnchantmentLevel(Holder<Enchantment> enchantment, ItemInstance piece) {
/*  52 */     ItemEnchantments enchantments = (ItemEnchantments)piece.getOrDefault(DataComponents.ENCHANTMENTS, ItemEnchantments.EMPTY);
/*  53 */     return enchantments.getLevel(enchantment);
/*     */   }
/*     */   
/*     */   public static ItemEnchantments updateEnchantments(ItemStack itemStack, Consumer<ItemEnchantments.Mutable> consumer) {
/*  57 */     DataComponentType<ItemEnchantments> componentType = getComponentType(itemStack);
/*  58 */     ItemEnchantments oldEnchantments = (ItemEnchantments)itemStack.get(componentType);
/*  59 */     if (oldEnchantments == null)
/*  60 */       return ItemEnchantments.EMPTY; 
/*  62 */     ItemEnchantments.Mutable mutableEnchantments = new ItemEnchantments.Mutable(oldEnchantments);
/*  63 */     consumer.accept(mutableEnchantments);
/*  64 */     ItemEnchantments newEnchantments = mutableEnchantments.toImmutable();
/*  65 */     itemStack.set(componentType, newEnchantments);
/*  66 */     return newEnchantments;
/*     */   }
/*     */   
/*     */   public static boolean canStoreEnchantments(ItemStack itemStack) {
/*  70 */     return itemStack.has(getComponentType(itemStack));
/*     */   }
/*     */   
/*     */   public static void setEnchantments(ItemStack itemStack, ItemEnchantments enchantments) {
/*  74 */     itemStack.set(getComponentType(itemStack), enchantments);
/*     */   }
/*     */   
/*     */   public static ItemEnchantments getEnchantmentsForCrafting(ItemStack itemStack) {
/*  78 */     return (ItemEnchantments)itemStack.getOrDefault(getComponentType(itemStack), ItemEnchantments.EMPTY);
/*     */   }
/*     */   
/*     */   private static DataComponentType<ItemEnchantments> getComponentType(ItemStack itemStack) {
/*  82 */     return itemStack.is(Items.ENCHANTED_BOOK) ? DataComponents.STORED_ENCHANTMENTS : DataComponents.ENCHANTMENTS;
/*     */   }
/*     */   
/*     */   public static boolean hasAnyEnchantments(ItemStack itemStack) {
/*  86 */     return (!((ItemEnchantments)itemStack.getOrDefault(DataComponents.ENCHANTMENTS, ItemEnchantments.EMPTY)).isEmpty() || 
/*  87 */       !((ItemEnchantments)itemStack.getOrDefault(DataComponents.STORED_ENCHANTMENTS, ItemEnchantments.EMPTY)).isEmpty());
/*     */   }
/*     */   
/*     */   public static int processDurabilityChange(ServerLevel serverLevel, ItemStack itemStack, int amount) {
/*  91 */     MutableFloat modifiedAmount = new MutableFloat(amount);
/*  92 */     runIterationOnItem(itemStack, (enchantment, level) -> ((Enchantment)enchantment.value()).modifyDurabilityChange(serverLevel, level, itemStack, modifiedAmount));
/*  93 */     return modifiedAmount.intValue();
/*     */   }
/*     */   
/*     */   public static int processAmmoUse(ServerLevel serverLevel, ItemStack weapon, ItemStack ammo, int amount) {
/*  97 */     MutableFloat modifiedAmount = new MutableFloat(amount);
/*  98 */     runIterationOnItem(weapon, (enchantment, level) -> ((Enchantment)enchantment.value()).modifyAmmoCount(serverLevel, level, ammo, modifiedAmount));
/*  99 */     return modifiedAmount.intValue();
/*     */   }
/*     */   
/*     */   public static int processBlockExperience(ServerLevel serverLevel, ItemStack itemStack, int amount) {
/* 103 */     MutableFloat modifiedAmount = new MutableFloat(amount);
/* 104 */     runIterationOnItem(itemStack, (enchantment, level) -> ((Enchantment)enchantment.value()).modifyBlockExperience(serverLevel, level, itemStack, modifiedAmount));
/* 105 */     return modifiedAmount.intValue();
/*     */   }
/*     */   
/*     */   public static int processMobExperience(ServerLevel serverLevel, Entity killer, Entity killed, int amount) {
/* 109 */     if (killer instanceof LivingEntity) {
/* 109 */       LivingEntity livingKiller = (LivingEntity)killer;
/* 110 */       MutableFloat modifiedAmount = new MutableFloat(amount);
/* 111 */       runIterationOnEquipment(livingKiller, (enchantment, level, item) -> ((Enchantment)enchantment.value()).modifyMobExperience(serverLevel, level, item.itemStack(), killed, modifiedAmount));
/* 112 */       return modifiedAmount.intValue();
/*     */     } 
/* 114 */     return amount;
/*     */   }
/*     */   
/*     */   public static ItemStack createBook(EnchantmentInstance enchant) {
/* 118 */     ItemStack itemStack = new ItemStack((ItemLike)Items.ENCHANTED_BOOK);
/* 119 */     itemStack.enchant(enchant.enchantment(), enchant.level());
/* 120 */     return itemStack;
/*     */   }
/*     */   
/*     */   private static void runIterationOnItem(ItemStack piece, EnchantmentVisitor method) {
/* 129 */     ItemEnchantments enchantments = (ItemEnchantments)piece.getOrDefault(DataComponents.ENCHANTMENTS, ItemEnchantments.EMPTY);
/* 130 */     for (Object2IntMap.Entry<Holder<Enchantment>> entry : enchantments.entrySet())
/* 131 */       method.accept((Holder<Enchantment>)entry.getKey(), entry.getIntValue()); 
/*     */   }
/*     */   
/*     */   private static void runIterationOnItem(ItemStack piece, EquipmentSlot slot, LivingEntity owner, EnchantmentInSlotVisitor method) {
/* 141 */     if (piece.isEmpty())
/*     */       return; 
/* 145 */     ItemEnchantments itemEnchantments = (ItemEnchantments)piece.get(DataComponents.ENCHANTMENTS);
/* 146 */     if (itemEnchantments == null || itemEnchantments.isEmpty())
/*     */       return; 
/* 149 */     EnchantedItemInUse itemInUse = new EnchantedItemInUse(piece, slot, owner);
/* 150 */     for (Object2IntMap.Entry<Holder<Enchantment>> entry : itemEnchantments.entrySet()) {
/* 151 */       Holder<Enchantment> enchantment = (Holder<Enchantment>)entry.getKey();
/* 152 */       if (((Enchantment)enchantment.value()).matchingSlot(slot))
/* 153 */         method.accept(enchantment, entry.getIntValue(), itemInUse); 
/*     */     } 
/*     */   }
/*     */   
/*     */   private static void runIterationOnEquipment(LivingEntity owner, EnchantmentInSlotVisitor method) {
/* 159 */     for (EquipmentSlot slot : EquipmentSlot.VALUES)
/* 160 */       runIterationOnItem(owner.getItemBySlot(slot), slot, owner, method); 
/*     */   }
/*     */   
/*     */   public static boolean isImmuneToDamage(ServerLevel serverLevel, LivingEntity victim, DamageSource source) {
/* 165 */     MutableBoolean result = new MutableBoolean();
/* 166 */     runIterationOnEquipment(victim, (enchantment, level, item) -> result.setValue((result.isTrue() || ((Enchantment)enchantment.value()).isImmuneToDamage(serverLevel, level, (Entity)victim, source))));
/* 167 */     return result.isTrue();
/*     */   }
/*     */   
/*     */   public static float getDamageProtection(ServerLevel serverLevel, LivingEntity victim, DamageSource source) {
/* 171 */     MutableFloat result = new MutableFloat(0.0F);
/* 172 */     runIterationOnEquipment(victim, (enchantment, level, item) -> ((Enchantment)enchantment.value()).modifyDamageProtection(serverLevel, level, item.itemStack(), (Entity)victim, source, result));
/* 173 */     return result.floatValue();
/*     */   }
/*     */   
/*     */   public static float modifyDamage(ServerLevel serverLevel, ItemStack itemStack, Entity victim, DamageSource damageSource, float damage) {
/* 177 */     MutableFloat result = new MutableFloat(damage);
/* 178 */     runIterationOnItem(itemStack, (enchantment, level) -> ((Enchantment)enchantment.value()).modifyDamage(serverLevel, level, itemStack, victim, damageSource, result));
/* 179 */     return result.floatValue();
/*     */   }
/*     */   
/*     */   public static float modifyFallBasedDamage(ServerLevel serverLevel, ItemStack itemStack, Entity victim, DamageSource damageSource, float damage) {
/* 183 */     MutableFloat result = new MutableFloat(damage);
/* 184 */     runIterationOnItem(itemStack, (enchantment, level) -> ((Enchantment)enchantment.value()).modifyFallBasedDamage(serverLevel, level, itemStack, victim, damageSource, result));
/* 185 */     return result.floatValue();
/*     */   }
/*     */   
/*     */   public static float modifyArmorEffectiveness(ServerLevel serverLevel, ItemStack itemStack, Entity victim, DamageSource damageSource, float armorFraction) {
/* 189 */     MutableFloat result = new MutableFloat(armorFraction);
/* 190 */     runIterationOnItem(itemStack, (enchantment, level) -> ((Enchantment)enchantment.value()).modifyArmorEffectivness(serverLevel, level, itemStack, victim, damageSource, result));
/* 191 */     return result.floatValue();
/*     */   }
/*     */   
/*     */   public static float modifyKnockback(ServerLevel serverLevel, ItemStack itemStack, Entity victim, DamageSource damageSource, float knockback) {
/* 195 */     MutableFloat result = new MutableFloat(knockback);
/* 196 */     runIterationOnItem(itemStack, (enchantment, level) -> ((Enchantment)enchantment.value()).modifyKnockback(serverLevel, level, itemStack, victim, damageSource, result));
/* 197 */     return result.floatValue();
/*     */   }
/*     */   
/*     */   public static void doPostAttackEffects(ServerLevel serverLevel, Entity victim, DamageSource damageSource) {
/* 201 */     Entity entity = damageSource.getEntity();
/* 201 */     if (entity instanceof LivingEntity) {
/* 201 */       LivingEntity attacker = (LivingEntity)entity;
/* 202 */       doPostAttackEffectsWithItemSource(serverLevel, victim, damageSource, attacker.getWeaponItem());
/*     */     } else {
/* 204 */       doPostAttackEffectsWithItemSource(serverLevel, victim, damageSource, null);
/*     */     } 
/*     */   }
/*     */   
/*     */   public static void doPostPiercingAttackEffects(ServerLevel serverLevel, LivingEntity user) {
/* 209 */     runIterationOnItem(user.getWeaponItem(), EquipmentSlot.MAINHAND, user, (enchantment, level, item) -> ((Enchantment)enchantment.value()).doPostPiercingAttack(serverLevel, level, item, (Entity)user));
/*     */   }
/*     */   
/*     */   public static void doPostAttackEffectsWithItemSource(ServerLevel serverLevel, Entity victim, DamageSource damageSource, ItemStack source) {
/* 213 */     doPostAttackEffectsWithItemSourceOnBreak(serverLevel, victim, damageSource, source, null);
/*     */   }
/*     */   
/*     */   public static void doPostAttackEffectsWithItemSourceOnBreak(ServerLevel serverLevel, Entity victim, DamageSource damageSource, ItemStack source, Consumer<Item> attackerlessOnBreak) {
/* 217 */     if (victim instanceof LivingEntity) {
/* 217 */       LivingEntity livingVictim = (LivingEntity)victim;
/* 218 */       runIterationOnEquipment(livingVictim, (enchantment, level, item) -> ((Enchantment)enchantment.value()).doPostAttack(serverLevel, level, item, EnchantmentTarget.VICTIM, victim, damageSource));
/*     */     } 
/* 220 */     if (source != null) {
/* 221 */       Entity entity = damageSource.getEntity();
/* 221 */       if (entity instanceof LivingEntity) {
/* 221 */         LivingEntity attacker = (LivingEntity)entity;
/* 222 */         runIterationOnItem(source, EquipmentSlot.MAINHAND, attacker, (enchantment, level, item) -> ((Enchantment)enchantment.value()).doPostAttack(serverLevel, level, item, EnchantmentTarget.ATTACKER, victim, damageSource));
/* 223 */       } else if (attackerlessOnBreak != null) {
/* 224 */         EnchantedItemInUse item = new EnchantedItemInUse(source, null, null, attackerlessOnBreak);
/* 225 */         runIterationOnItem(source, (enchantment, level) -> ((Enchantment)enchantment.value()).doPostAttack(serverLevel, level, item, EnchantmentTarget.ATTACKER, victim, damageSource));
/*     */       } 
/*     */     } 
/*     */   }
/*     */   
/*     */   public static void runLocationChangedEffects(ServerLevel serverLevel, LivingEntity entity) {
/* 231 */     runIterationOnEquipment(entity, (enchantment, level, item) -> ((Enchantment)enchantment.value()).runLocationChangedEffects(serverLevel, level, item, entity));
/*     */   }
/*     */   
/*     */   public static void runLocationChangedEffects(ServerLevel serverLevel, ItemStack stack, LivingEntity entity, EquipmentSlot slot) {
/* 235 */     runIterationOnItem(stack, slot, entity, (enchantment, level, item) -> ((Enchantment)enchantment.value()).runLocationChangedEffects(serverLevel, level, item, entity));
/*     */   }
/*     */   
/*     */   public static void stopLocationBasedEffects(LivingEntity entity) {
/* 239 */     runIterationOnEquipment(entity, (enchantment, level, item) -> ((Enchantment)enchantment.value()).stopLocationBasedEffects(level, item, entity));
/*     */   }
/*     */   
/*     */   public static void stopLocationBasedEffects(ItemStack stack, LivingEntity entity, EquipmentSlot slot) {
/* 243 */     runIterationOnItem(stack, slot, entity, (enchantment, level, item) -> ((Enchantment)enchantment.value()).stopLocationBasedEffects(level, item, entity));
/*     */   }
/*     */   
/*     */   public static void tickEffects(ServerLevel serverLevel, LivingEntity entity) {
/* 247 */     runIterationOnEquipment(entity, (enchantment, level, item) -> ((Enchantment)enchantment.value()).tick(serverLevel, level, item, (Entity)entity));
/*     */   }
/*     */   
/*     */   public static int getEnchantmentLevel(Holder<Enchantment> enchantment, LivingEntity entity) {
/* 251 */     Iterable<ItemStack> allowedSlots = ((Enchantment)enchantment.value()).getSlotItems(entity).values();
/* 252 */     int bestLevel = 0;
/* 253 */     for (ItemStack piece : allowedSlots) {
/* 254 */       int newLevel = getItemEnchantmentLevel(enchantment, (ItemInstance)piece);
/* 255 */       if (newLevel > bestLevel)
/* 256 */         bestLevel = newLevel; 
/*     */     } 
/* 259 */     return bestLevel;
/*     */   }
/*     */   
/*     */   public static int processProjectileCount(ServerLevel serverLevel, ItemStack weapon, Entity shooter, int count) {
/* 263 */     MutableFloat modifiedCount = new MutableFloat(count);
/* 264 */     runIterationOnItem(weapon, (enchantment, level) -> ((Enchantment)enchantment.value()).modifyProjectileCount(serverLevel, level, weapon, shooter, modifiedCount));
/* 265 */     return Math.max(0, modifiedCount.intValue());
/*     */   }
/*     */   
/*     */   public static float processProjectileSpread(ServerLevel serverLevel, ItemStack weapon, Entity shooter, float angle) {
/* 269 */     MutableFloat modifiedAngle = new MutableFloat(angle);
/* 270 */     runIterationOnItem(weapon, (enchantment, level) -> ((Enchantment)enchantment.value()).modifyProjectileSpread(serverLevel, level, weapon, shooter, modifiedAngle));
/* 271 */     return Math.max(0.0F, modifiedAngle.floatValue());
/*     */   }
/*     */   
/*     */   public static int getPiercingCount(ServerLevel serverLevel, ItemStack weapon, ItemStack ammo) {
/* 275 */     MutableFloat modifiedAmount = new MutableFloat(0.0F);
/* 276 */     runIterationOnItem(weapon, (enchantment, level) -> ((Enchantment)enchantment.value()).modifyPiercingCount(serverLevel, level, ammo, modifiedAmount));
/* 277 */     return Math.max(0, modifiedAmount.intValue());
/*     */   }
/*     */   
/*     */   public static void onProjectileSpawned(ServerLevel serverLevel, ItemStack weapon, Projectile projectileEntity, Consumer<Item> onBreak) {
/* 281 */     Entity entity = projectileEntity.getOwner();
/* 281 */     LivingEntity le = (LivingEntity)entity, owner = (entity instanceof LivingEntity) ? le : null;
/* 282 */     EnchantedItemInUse item = new EnchantedItemInUse(weapon, null, owner, onBreak);
/* 283 */     runIterationOnItem(weapon, (enchantment, level) -> ((Enchantment)enchantment.value()).onProjectileSpawned(serverLevel, level, item, (Entity)projectileEntity));
/*     */   }
/*     */   
/*     */   public static void onHitBlock(ServerLevel serverLevel, ItemStack weapon, LivingEntity owner, Entity entity, EquipmentSlot slot, Vec3 hitLocation, BlockState hitBlock, Consumer<Item> onBreak) {
/* 287 */     EnchantedItemInUse item = new EnchantedItemInUse(weapon, slot, owner, onBreak);
/* 288 */     runIterationOnItem(weapon, (enchantment, level) -> ((Enchantment)enchantment.value()).onHitBlock(serverLevel, level, item, entity, hitLocation, hitBlock));
/*     */   }
/*     */   
/*     */   public static int modifyDurabilityToRepairFromXp(ServerLevel serverLevel, ItemStack item, int durability) {
/* 292 */     MutableFloat modifiedDurability = new MutableFloat(durability);
/* 293 */     runIterationOnItem(item, (enchantment, level) -> ((Enchantment)enchantment.value()).modifyDurabilityToRepairFromXp(serverLevel, level, item, modifiedDurability));
/* 294 */     return Math.max(0, modifiedDurability.intValue());
/*     */   }
/*     */   
/*     */   public static float processEquipmentDropChance(ServerLevel serverLevel, LivingEntity entity, DamageSource killingBlow, float chance) {
/* 298 */     MutableFloat modifiedChance = new MutableFloat(chance);
/* 299 */     RandomSource random = entity.getRandom();
/* 300 */     runIterationOnEquipment(entity, (enchantment, level, item) -> {
/*     */           LootContext context = Enchantment.damageContext(serverLevel, level, (Entity)entity, killingBlow);
/*     */           ((Enchantment)enchantment.value()).<TargetedConditionalEffect<EnchantmentValueEffect>>getEffects(EnchantmentEffectComponents.EQUIPMENT_DROPS).forEach(());
/*     */         });
/* 310 */     Entity attacker = killingBlow.getEntity();
/* 311 */     if (attacker instanceof LivingEntity) {
/* 311 */       LivingEntity livingAttacker = (LivingEntity)attacker;
/* 312 */       runIterationOnEquipment(livingAttacker, (enchantment, level, item) -> {
/*     */             LootContext context = Enchantment.damageContext(serverLevel, level, (Entity)entity, killingBlow);
/*     */             ((Enchantment)enchantment.value()).<TargetedConditionalEffect<EnchantmentValueEffect>>getEffects(EnchantmentEffectComponents.EQUIPMENT_DROPS).forEach(());
/*     */           });
/*     */     } 
/* 324 */     return modifiedChance.floatValue();
/*     */   }
/*     */   
/*     */   public static void forEachModifier(ItemStack itemStack, EquipmentSlotGroup slot, BiConsumer<Holder<Attribute>, AttributeModifier> consumer) {
/* 328 */     runIterationOnItem(itemStack, (enchantment, level) -> ((Enchantment)enchantment.value()).<EnchantmentAttributeEffect>getEffects(EnchantmentEffectComponents.ATTRIBUTES).forEach(()));
/*     */   }
/*     */   
/*     */   public static void forEachModifier(ItemStack itemStack, EquipmentSlot slot, BiConsumer<Holder<Attribute>, AttributeModifier> consumer) {
/* 341 */     runIterationOnItem(itemStack, (enchantment, level) -> ((Enchantment)enchantment.value()).<EnchantmentAttributeEffect>getEffects(EnchantmentEffectComponents.ATTRIBUTES).forEach(()));
/*     */   }
/*     */   
/*     */   public static int getFishingLuckBonus(ServerLevel serverLevel, ItemStack rod, Entity fisher) {
/* 354 */     MutableFloat modifiedSpeed = new MutableFloat(0.0F);
/* 355 */     runIterationOnItem(rod, (enchantment, level) -> ((Enchantment)enchantment.value()).modifyFishingLuckBonus(serverLevel, level, rod, fisher, modifiedSpeed));
/* 356 */     return Math.max(0, modifiedSpeed.intValue());
/*     */   }
/*     */   
/*     */   public static float getFishingTimeReduction(ServerLevel serverLevel, ItemStack rod, Entity fisher) {
/* 360 */     MutableFloat modifiedSpeed = new MutableFloat(0.0F);
/* 361 */     runIterationOnItem(rod, (enchantment, level) -> ((Enchantment)enchantment.value()).modifyFishingTimeReduction(serverLevel, level, rod, fisher, modifiedSpeed));
/* 362 */     return Math.max(0.0F, modifiedSpeed.floatValue());
/*     */   }
/*     */   
/*     */   public static int getTridentReturnToOwnerAcceleration(ServerLevel serverLevel, ItemStack weapon, Entity trident) {
/* 366 */     MutableFloat modifiedAcceleration = new MutableFloat(0.0F);
/* 367 */     runIterationOnItem(weapon, (enchantment, level) -> ((Enchantment)enchantment.value()).modifyTridentReturnToOwnerAcceleration(serverLevel, level, weapon, trident, modifiedAcceleration));
/* 368 */     return Math.max(0, modifiedAcceleration.intValue());
/*     */   }
/*     */   
/*     */   public static float modifyCrossbowChargingTime(ItemStack crossbow, LivingEntity holder, float time) {
/* 372 */     MutableFloat modifiedTime = new MutableFloat(time);
/* 373 */     runIterationOnItem(crossbow, (enchantment, level) -> ((Enchantment)enchantment.value()).modifyCrossbowChargeTime(holder.getRandom(), level, modifiedTime));
/* 374 */     return Math.max(0.0F, modifiedTime.floatValue());
/*     */   }
/*     */   
/*     */   public static float getTridentSpinAttackStrength(ItemStack trident, LivingEntity holder) {
/* 378 */     MutableFloat strength = new MutableFloat(0.0F);
/* 379 */     runIterationOnItem(trident, (enchantment, level) -> ((Enchantment)enchantment.value()).modifyTridentSpinAttackStrength(holder.getRandom(), level, strength));
/* 380 */     return strength.floatValue();
/*     */   }
/*     */   
/*     */   public static boolean hasTag(ItemStack item, TagKey<Enchantment> tag) {
/* 384 */     ItemEnchantments enchantments = (ItemEnchantments)item.getOrDefault(DataComponents.ENCHANTMENTS, ItemEnchantments.EMPTY);
/* 385 */     for (Object2IntMap.Entry<Holder<Enchantment>> entry : enchantments.entrySet()) {
/* 386 */       Holder<Enchantment> enchantment = (Holder<Enchantment>)entry.getKey();
/* 387 */       if (enchantment.is(tag))
/* 388 */         return true; 
/*     */     } 
/* 391 */     return false;
/*     */   }
/*     */   
/*     */   public static boolean has(ItemStack item, DataComponentType<?> effectType) {
/* 395 */     MutableBoolean found = new MutableBoolean(false);
/* 396 */     runIterationOnItem(item, (enchantment, level) -> {
/*     */           if (((Enchantment)enchantment.value()).effects().has(effectType))
/*     */             found.setTrue(); 
/*     */         });
/* 401 */     return found.booleanValue();
/*     */   }
/*     */   
/*     */   public static <T> Optional<T> pickHighestLevel(ItemStack itemStack, DataComponentType<List<T>> componentType) {
/* 405 */     Pair<List<T>, Integer> picked = getHighestLevel(itemStack, componentType);
/* 406 */     if (picked != null) {
/* 407 */       List<T> list = (List<T>)picked.getFirst();
/* 408 */       int enchantmentLevel = ((Integer)picked.getSecond()).intValue();
/* 409 */       return Optional.of(list.get(Math.min(enchantmentLevel, list.size()) - 1));
/*     */     } 
/* 411 */     return Optional.empty();
/*     */   }
/*     */   
/*     */   public static <T> Pair<T, Integer> getHighestLevel(ItemStack item, DataComponentType<T> effectType) {
/* 415 */     MutableObject<Pair<T, Integer>> found = new MutableObject();
/* 416 */     runIterationOnItem(item, (enchantment, level) -> {
/*     */           if (found.get() == null || ((Integer)((Pair)found.get()).getSecond()).intValue() < level) {
/*     */             T effect = (T)((Enchantment)enchantment.value()).effects().get(effectType);
/*     */             if (effect != null)
/*     */               found.setValue(Pair.of(effect, Integer.valueOf(level))); 
/*     */           } 
/*     */         });
/* 424 */     return (Pair<T, Integer>)found.get();
/*     */   }
/*     */   
/*     */   public static Optional<EnchantedItemInUse> getRandomItemWith(DataComponentType<?> componentType, LivingEntity source, Predicate<ItemStack> predicate) {
/* 428 */     List<EnchantedItemInUse> items = new ArrayList<>();
/* 429 */     for (EquipmentSlot slot : EquipmentSlot.VALUES) {
/* 430 */       ItemStack item = source.getItemBySlot(slot);
/* 431 */       if (!predicate.test(item))
/*     */         continue; 
/* 434 */       ItemEnchantments enchantments = (ItemEnchantments)item.getOrDefault(DataComponents.ENCHANTMENTS, ItemEnchantments.EMPTY);
/* 435 */       for (Object2IntMap.Entry<Holder<Enchantment>> entry : enchantments.entrySet()) {
/* 436 */         Holder<Enchantment> enchantment = (Holder<Enchantment>)entry.getKey();
/* 437 */         if (((Enchantment)enchantment.value()).effects().has(componentType) && ((Enchantment)enchantment.value()).matchingSlot(slot))
/* 438 */           items.add(new EnchantedItemInUse(item, slot, source)); 
/*     */       } 
/*     */     } 
/* 443 */     return Util.getRandomSafe(items, source.getRandom());
/*     */   }
/*     */   
/*     */   public static int getEnchantmentCost(RandomSource random, int slot, int bookcases, ItemStack itemStack) {
/* 452 */     Enchantable enchantable = (Enchantable)itemStack.get(DataComponents.ENCHANTABLE);
/* 453 */     if (enchantable == null)
/* 455 */       return 0; 
/* 458 */     if (bookcases > 15)
/* 459 */       bookcases = 15; 
/* 461 */     int selected = random.nextInt(8) + 1 + (bookcases >> 1) + random.nextInt(bookcases + 1);
/* 462 */     if (slot == 0)
/* 463 */       return Math.max(selected / 3, 1); 
/* 465 */     if (slot == 1)
/* 466 */       return selected * 2 / 3 + 1; 
/* 468 */     return Math.max(selected, bookcases * 2);
/*     */   }
/*     */   
/*     */   public static ItemStack enchantItem(RandomSource random, ItemStack itemStack, int enchantmentCost, RegistryAccess registryAccess, Optional<? extends HolderSet<Enchantment>> set) {
/* 472 */     return enchantItem(random, itemStack, enchantmentCost, set.<Stream<Holder<Enchantment>>>map(HolderSet::stream).orElseGet(() -> registryAccess.lookupOrThrow(Registries.ENCHANTMENT).listElements().map(())));
/*     */   }
/*     */   
/*     */   public static ItemStack enchantItem(RandomSource random, ItemStack itemStack, int enchantmentCost, Stream<Holder<Enchantment>> source) {
/* 476 */     List<EnchantmentInstance> enchants = selectEnchantment(random, itemStack, enchantmentCost, source);
/* 477 */     if (itemStack.is(Items.BOOK))
/* 478 */       itemStack = new ItemStack((ItemLike)Items.ENCHANTED_BOOK); 
/* 481 */     for (EnchantmentInstance enchant : enchants)
/* 482 */       itemStack.enchant(enchant.enchantment(), enchant.level()); 
/* 485 */     return itemStack;
/*     */   }
/*     */   
/*     */   public static List<EnchantmentInstance> selectEnchantment(RandomSource random, ItemStack itemStack, int enchantmentCost, Stream<Holder<Enchantment>> source) {
/* 489 */     List<EnchantmentInstance> results = Lists.newArrayList();
/* 492 */     Enchantable enchantable = (Enchantable)itemStack.get(DataComponents.ENCHANTABLE);
/* 493 */     if (enchantable == null)
/* 494 */       return results; 
/* 497 */     enchantmentCost += 1 + random.nextInt(enchantable.value() / 4 + 1) + random.nextInt(enchantable.value() / 4 + 1);
/* 500 */     float randomSpan = (random.nextFloat() + random.nextFloat() - 1.0F) * 0.15F;
/* 501 */     enchantmentCost = Mth.clamp(Math.round(enchantmentCost + enchantmentCost * randomSpan), 1, 2147483647);
/* 503 */     List<EnchantmentInstance> enchantments = getAvailableEnchantmentResults(enchantmentCost, itemStack, source);
/* 504 */     if (!enchantments.isEmpty()) {
/* 505 */       Objects.requireNonNull(results);
/* 505 */       WeightedRandom.getRandomItem(random, enchantments, EnchantmentInstance::weight).ifPresent(results::add);
/* 507 */       while (random.nextInt(50) <= enchantmentCost) {
/* 508 */         if (!results.isEmpty())
/* 509 */           filterCompatibleEnchantments(enchantments, results.getLast()); 
/* 512 */         if (enchantments.isEmpty())
/*     */           break; 
/* 516 */         Objects.requireNonNull(results);
/* 516 */         WeightedRandom.getRandomItem(random, enchantments, EnchantmentInstance::weight).ifPresent(results::add);
/* 517 */         enchantmentCost /= 2;
/*     */       } 
/*     */     } 
/* 520 */     return results;
/*     */   }
/*     */   
/*     */   public static void filterCompatibleEnchantments(List<EnchantmentInstance> enchants, EnchantmentInstance target) {
/* 524 */     enchants.removeIf(e -> !Enchantment.areCompatible(target.enchantment(), e.enchantment()));
/*     */   }
/*     */   
/*     */   public static boolean isEnchantmentCompatible(Collection<Holder<Enchantment>> enchants, Holder<Enchantment> target) {
/* 528 */     for (Holder<Enchantment> existing : enchants) {
/* 529 */       if (!Enchantment.areCompatible(existing, target))
/* 530 */         return false; 
/*     */     } 
/* 533 */     return true;
/*     */   }
/*     */   
/*     */   public static List<EnchantmentInstance> getAvailableEnchantmentResults(int value, ItemStack itemStack, Stream<Holder<Enchantment>> source) {
/* 537 */     List<EnchantmentInstance> results = Lists.newArrayList();
/* 539 */     boolean isBook = itemStack.is(Items.BOOK);
/* 540 */     source
/*     */       
/* 542 */       .filter(enchantment -> (((Enchantment)enchantment.value()).isPrimaryItem(itemStack) || isBook))
/* 543 */       .forEach(holder -> {
/*     */           Enchantment enchantment = (Enchantment)holder.value();
/*     */           for (int level = enchantment.getMaxLevel(); level >= enchantment.getMinLevel(); level--) {
/*     */             if (value >= enchantment.getMinCost(level) && value <= enchantment.getMaxCost(level)) {
/*     */               results.add(new EnchantmentInstance(holder, level));
/*     */               break;
/*     */             } 
/*     */           } 
/*     */         });
/* 553 */     return results;
/*     */   }
/*     */   
/*     */   public static void enchantItemFromProvider(ItemStack itemStack, RegistryAccess registryAccess, ResourceKey<EnchantmentProvider> providerKey, DifficultyInstance difficulty, RandomSource random) {
/* 557 */     EnchantmentProvider provider = (EnchantmentProvider)registryAccess.lookupOrThrow(Registries.ENCHANTMENT_PROVIDER).getValue(providerKey);
/* 558 */     if (provider != null)
/* 559 */       updateEnchantments(itemStack, enchantments -> provider.enchant(itemStack, enchantments, random, difficulty)); 
/*     */   }
/*     */   
/*     */   @FunctionalInterface
/*     */   private static interface EnchantmentVisitor {
/*     */     void accept(Holder<Enchantment> param1Holder, int param1Int);
/*     */   }
/*     */   
/*     */   @FunctionalInterface
/*     */   private static interface EnchantmentInSlotVisitor {
/*     */     void accept(Holder<Enchantment> param1Holder, int param1Int, EnchantedItemInUse param1EnchantedItemInUse);
/*     */   }
/*     */ }


/* Location:              C:\Users\karol\AppData\Roaming\ModrinthApp\meta\versions\26.2-0.19.5\26.2-0.19.5.jar!\net\minecraft\world\item\enchantment\EnchantmentHelper.class
 * Java compiler version: 25 (69.0)
 * JD-Core Version:       1.1.3
 */