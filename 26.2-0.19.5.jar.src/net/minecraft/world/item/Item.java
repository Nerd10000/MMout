/*     */ package net.minecraft.world.item;
/*     */ 
/*     */ import com.google.common.annotations.VisibleForTesting;
/*     */ import com.google.common.collect.Maps;
/*     */ import com.mojang.logging.LogUtils;
/*     */ import com.mojang.serialization.Codec;
/*     */ import com.mojang.serialization.DataResult;
/*     */ import java.util.Map;
/*     */ import java.util.Objects;
/*     */ import java.util.Optional;
/*     */ import java.util.function.Consumer;
/*     */ import net.minecraft.SharedConstants;
/*     */ import net.minecraft.core.BlockPos;
/*     */ import net.minecraft.core.Holder;
/*     */ import net.minecraft.core.HolderGetter;
/*     */ import net.minecraft.core.HolderLookup;
/*     */ import net.minecraft.core.HolderSet;
/*     */ import net.minecraft.core.Registry;
/*     */ import net.minecraft.core.component.DataComponentInitializers;
/*     */ import net.minecraft.core.component.DataComponentMap;
/*     */ import net.minecraft.core.component.DataComponentType;
/*     */ import net.minecraft.core.component.DataComponents;
/*     */ import net.minecraft.core.registries.BuiltInRegistries;
/*     */ import net.minecraft.core.registries.Registries;
/*     */ import net.minecraft.nbt.CompoundTag;
/*     */ import net.minecraft.network.RegistryFriendlyByteBuf;
/*     */ import net.minecraft.network.chat.CommonComponents;
/*     */ import net.minecraft.network.chat.Component;
/*     */ import net.minecraft.network.codec.ByteBufCodecs;
/*     */ import net.minecraft.network.codec.StreamCodec;
/*     */ import net.minecraft.resources.DependantName;
/*     */ import net.minecraft.resources.Identifier;
/*     */ import net.minecraft.resources.ResourceKey;
/*     */ import net.minecraft.server.level.ServerLevel;
/*     */ import net.minecraft.sounds.SoundEvents;
/*     */ import net.minecraft.tags.BlockTags;
/*     */ import net.minecraft.tags.DamageTypeTags;
/*     */ import net.minecraft.tags.EntityTypeTags;
/*     */ import net.minecraft.tags.TagKey;
/*     */ import net.minecraft.util.Mth;
/*     */ import net.minecraft.util.Util;
/*     */ import net.minecraft.world.Difficulty;
/*     */ import net.minecraft.world.InteractionHand;
/*     */ import net.minecraft.world.InteractionResult;
/*     */ import net.minecraft.world.damagesource.DamageSource;
/*     */ import net.minecraft.world.damagesource.DamageTypes;
/*     */ import net.minecraft.world.entity.Entity;
/*     */ import net.minecraft.world.entity.EntityType;
/*     */ import net.minecraft.world.entity.EntityTypes;
/*     */ import net.minecraft.world.entity.EquipmentSlot;
/*     */ import net.minecraft.world.entity.EquipmentSlotGroup;
/*     */ import net.minecraft.world.entity.LivingEntity;
/*     */ import net.minecraft.world.entity.SlotAccess;
/*     */ import net.minecraft.world.entity.ai.attributes.AttributeModifier;
/*     */ import net.minecraft.world.entity.ai.attributes.Attributes;
/*     */ import net.minecraft.world.entity.item.ItemEntity;
/*     */ import net.minecraft.world.entity.player.Player;
/*     */ import net.minecraft.world.flag.FeatureElement;
/*     */ import net.minecraft.world.flag.FeatureFlag;
/*     */ import net.minecraft.world.flag.FeatureFlagSet;
/*     */ import net.minecraft.world.flag.FeatureFlags;
/*     */ import net.minecraft.world.food.FoodProperties;
/*     */ import net.minecraft.world.inventory.ClickAction;
/*     */ import net.minecraft.world.inventory.Slot;
/*     */ import net.minecraft.world.inventory.tooltip.TooltipComponent;
/*     */ import net.minecraft.world.item.component.AttackRange;
/*     */ import net.minecraft.world.item.component.Consumable;
/*     */ import net.minecraft.world.item.component.Consumables;
/*     */ import net.minecraft.world.item.component.DamageResistant;
/*     */ import net.minecraft.world.item.component.ItemAttributeModifiers;
/*     */ import net.minecraft.world.item.component.KineticWeapon;
/*     */ import net.minecraft.world.item.component.PiercingWeapon;
/*     */ import net.minecraft.world.item.component.SwingAnimation;
/*     */ import net.minecraft.world.item.component.Tool;
/*     */ import net.minecraft.world.item.component.TooltipDisplay;
/*     */ import net.minecraft.world.item.component.TypedEntityData;
/*     */ import net.minecraft.world.item.component.UseCooldown;
/*     */ import net.minecraft.world.item.component.UseEffects;
/*     */ import net.minecraft.world.item.component.UseRemainder;
/*     */ import net.minecraft.world.item.component.Weapon;
/*     */ import net.minecraft.world.item.context.UseOnContext;
/*     */ import net.minecraft.world.item.enchantment.Enchantable;
/*     */ import net.minecraft.world.item.enchantment.Repairable;
/*     */ import net.minecraft.world.item.equipment.ArmorMaterial;
/*     */ import net.minecraft.world.item.equipment.ArmorType;
/*     */ import net.minecraft.world.item.equipment.Equippable;
/*     */ import net.minecraft.world.item.equipment.trim.TrimMaterial;
/*     */ import net.minecraft.world.level.BlockGetter;
/*     */ import net.minecraft.world.level.ClipContext;
/*     */ import net.minecraft.world.level.ItemLike;
/*     */ import net.minecraft.world.level.Level;
/*     */ import net.minecraft.world.level.block.Block;
/*     */ import net.minecraft.world.level.block.state.BlockState;
/*     */ import net.minecraft.world.level.saveddata.maps.MapId;
/*     */ import net.minecraft.world.level.saveddata.maps.MapItemSavedData;
/*     */ import net.minecraft.world.phys.BlockHitResult;
/*     */ import net.minecraft.world.phys.Vec3;
/*     */ import org.slf4j.Logger;
/*     */ 
/*     */ public class Item implements ItemLike, FeatureElement {
/*     */   public static final Codec<Holder<Item>> CODEC;
/*     */   
/*     */   static {
/* 101 */     CODEC = BuiltInRegistries.ITEM.holderByNameCodec().validate(item -> item.is((Holder)Items.AIR.builtInRegistryHolder()) ? DataResult.error(()) : DataResult.success(item));
/*     */   }
/*     */   
/* 104 */   public static final StreamCodec<RegistryFriendlyByteBuf, Holder<Item>> STREAM_CODEC = ByteBufCodecs.holderRegistry(Registries.ITEM);
/*     */   
/*     */   public static final Codec<Holder<Item>> CODEC_WITH_BOUND_COMPONENTS;
/*     */   
/*     */   static {
/* 106 */     CODEC_WITH_BOUND_COMPONENTS = CODEC.validate(item -> !item.areComponentsBound() ? DataResult.error(()) : DataResult.success(item));
/*     */   }
/*     */   
/* 113 */   private static final Logger LOGGER = LogUtils.getLogger();
/*     */   
/* 114 */   public static final Map<Block, Item> BY_BLOCK = Maps.newHashMap();
/*     */   
/* 116 */   public static final Identifier BASE_ATTACK_DAMAGE_ID = Identifier.withDefaultNamespace("base_attack_damage");
/*     */   
/* 117 */   public static final Identifier BASE_ATTACK_SPEED_ID = Identifier.withDefaultNamespace("base_attack_speed");
/*     */   
/*     */   public static final int DEFAULT_MAX_STACK_SIZE = 64;
/*     */   
/*     */   public static final int ABSOLUTE_MAX_STACK_SIZE = 99;
/*     */   
/*     */   public static final int MAX_BAR_WIDTH = 13;
/*     */   
/*     */   protected static final int APPROXIMATELY_INFINITE_USE_DURATION = 72000;
/*     */   
/* 127 */   private final Holder.Reference<Item> builtInRegistryHolder = BuiltInRegistries.ITEM.createIntrusiveHolder(this);
/*     */   
/*     */   private final ItemStackTemplate craftingRemainingItem;
/*     */   
/*     */   protected final String descriptionId;
/*     */   
/*     */   private final FeatureFlagSet requiredFeatures;
/*     */   
/*     */   public static int getId(Item item) {
/* 130 */     return (item == null) ? 0 : BuiltInRegistries.ITEM.getId(item);
/*     */   }
/*     */   
/*     */   public static Item byId(int id) {
/* 134 */     return (Item)BuiltInRegistries.ITEM.byId(id);
/*     */   }
/*     */   
/*     */   @Deprecated
/*     */   public static Item byBlock(Block block) {
/* 140 */     return BY_BLOCK.getOrDefault(block, Items.AIR);
/*     */   }
/*     */   
/*     */   public Item(Properties properties) {
/* 150 */     this.descriptionId = properties.effectiveDescriptionId();
/* 151 */     DataComponentInitializers.Initializer<Item> componentInitializer = properties.finalizeInitializer((Component)Component.translatable(this.descriptionId), properties.effectiveModel());
/* 152 */     BuiltInRegistries.DATA_COMPONENT_INITIALIZERS.add(properties.itemIdOrThrow(), componentInitializer);
/* 154 */     this.craftingRemainingItem = properties.craftingRemainingItem;
/* 155 */     this.requiredFeatures = properties.requiredFeatures;
/* 157 */     if (SharedConstants.IS_RUNNING_IN_IDE) {
/* 158 */       String className = getClass().getSimpleName();
/* 159 */       if (!className.endsWith("Item"))
/* 160 */         LOGGER.error("Item classes should end with Item and {} doesn't.", className); 
/*     */     } 
/*     */   }
/*     */   
/*     */   @Deprecated
/*     */   public Holder.Reference<Item> builtInRegistryHolder() {
/* 168 */     return this.builtInRegistryHolder;
/*     */   }
/*     */   
/*     */   public DataComponentMap components() {
/* 172 */     return this.builtInRegistryHolder.components();
/*     */   }
/*     */   
/*     */   public int getDefaultMaxStackSize() {
/* 176 */     return ((Integer)components().getOrDefault(DataComponents.MAX_STACK_SIZE, Integer.valueOf(1))).intValue();
/*     */   }
/*     */   
/*     */   public static class Properties {
/*     */     private static final DependantName<Item, String> BLOCK_DESCRIPTION_ID;
/*     */     
/*     */     private static final DependantName<Item, String> ITEM_DESCRIPTION_ID;
/*     */     
/*     */     private DataComponentInitializers.Initializer<Item> componentInitializer;
/*     */     
/*     */     private ItemStackTemplate craftingRemainingItem;
/*     */     
/*     */     private FeatureFlagSet requiredFeatures;
/*     */     
/*     */     private ResourceKey<Item> id;
/*     */     
/*     */     private DependantName<Item, String> descriptionId;
/*     */     
/*     */     private final DependantName<Item, Identifier> model;
/*     */     
/*     */     public Properties() {
/* 183 */       this.componentInitializer = ((builder, context, id) -> builder.addAll(DataComponents.COMMON_ITEM_COMPONENTS));
/* 185 */       this.requiredFeatures = FeatureFlags.VANILLA_SET;
/* 187 */       this.descriptionId = ITEM_DESCRIPTION_ID;
/* 188 */       this.model = ResourceKey::identifier;
/*     */     }
/*     */     
/*     */     static {
/*     */       BLOCK_DESCRIPTION_ID = (id -> Util.makeDescriptionId("block", id.identifier()));
/*     */       ITEM_DESCRIPTION_ID = (id -> Util.makeDescriptionId("item", id.identifier()));
/*     */     }
/*     */     
/*     */     public Properties food(FoodProperties foodProperties) {
/* 191 */       return food(foodProperties, Consumables.DEFAULT_FOOD);
/*     */     }
/*     */     
/*     */     public Properties food(FoodProperties foodProperties, Consumable consumable) {
/* 195 */       return component(DataComponents.FOOD, foodProperties).component(DataComponents.CONSUMABLE, consumable);
/*     */     }
/*     */     
/*     */     public Properties usingConvertsTo(Item item) {
/* 199 */       return component(DataComponents.USE_REMAINDER, new UseRemainder(new ItemStackTemplate(item)));
/*     */     }
/*     */     
/*     */     public Properties useCooldown(float seconds) {
/* 203 */       return component(DataComponents.USE_COOLDOWN, new UseCooldown(seconds));
/*     */     }
/*     */     
/*     */     public Properties stacksTo(int max) {
/* 207 */       return component(DataComponents.MAX_STACK_SIZE, Integer.valueOf(max));
/*     */     }
/*     */     
/*     */     public Properties durability(int maxDamage) {
/* 211 */       component(DataComponents.MAX_DAMAGE, Integer.valueOf(maxDamage));
/* 212 */       component(DataComponents.MAX_STACK_SIZE, Integer.valueOf(1));
/* 213 */       component(DataComponents.DAMAGE, Integer.valueOf(0));
/* 214 */       return this;
/*     */     }
/*     */     
/*     */     public Properties craftRemainder(Item craftingRemainingItem) {
/* 218 */       return craftRemainder(new ItemStackTemplate(craftingRemainingItem));
/*     */     }
/*     */     
/*     */     public Properties craftRemainder(ItemStackTemplate craftingRemainingItem) {
/* 222 */       this.craftingRemainingItem = craftingRemainingItem;
/* 223 */       return this;
/*     */     }
/*     */     
/*     */     public Properties rarity(Rarity rarity) {
/* 227 */       return component(DataComponents.RARITY, rarity);
/*     */     }
/*     */     
/*     */     public Properties fireResistant() {
/* 231 */       return delayedComponent(DataComponents.DAMAGE_RESISTANT, context -> new DamageResistant((HolderSet)context.getOrThrow(DamageTypeTags.IS_FIRE)));
/*     */     }
/*     */     
/*     */     public Properties jukeboxPlayable(ResourceKey<JukeboxSong> song) {
/* 235 */       return delayedComponent(DataComponents.JUKEBOX_PLAYABLE, context -> new JukeboxPlayable((Holder<JukeboxSong>)context.getOrThrow(song)));
/*     */     }
/*     */     
/*     */     public Properties enchantable(int value) {
/* 239 */       return component(DataComponents.ENCHANTABLE, new Enchantable(value));
/*     */     }
/*     */     
/*     */     public Properties repairable(Item repairItem) {
/* 243 */       return component(DataComponents.REPAIRABLE, new Repairable((HolderSet)HolderSet.direct(new Holder[] { (Holder)repairItem.builtInRegistryHolder() })));
/*     */     }
/*     */     
/*     */     public Properties repairable(TagKey<Item> repairItems) {
/* 248 */       HolderGetter<Item> registrationLookup = BuiltInRegistries.acquireBootstrapRegistrationLookup((Registry)BuiltInRegistries.ITEM);
/* 249 */       return component(DataComponents.REPAIRABLE, new Repairable((HolderSet)registrationLookup.getOrThrow(repairItems)));
/*     */     }
/*     */     
/*     */     public Properties equippable(EquipmentSlot slot) {
/* 253 */       return component(DataComponents.EQUIPPABLE, Equippable.builder(slot).build());
/*     */     }
/*     */     
/*     */     public Properties equippableUnswappable(EquipmentSlot slot) {
/* 257 */       return component(DataComponents.EQUIPPABLE, Equippable.builder(slot).setSwappable(false).build());
/*     */     }
/*     */     
/*     */     public Properties tool(ToolMaterial material, TagKey<Block> minesEfficiently, float attackDamageBaseline, float attackSpeedBaseline, float disableBlockingSeconds) {
/* 261 */       return material.applyToolProperties(this, minesEfficiently, attackDamageBaseline, attackSpeedBaseline, disableBlockingSeconds);
/*     */     }
/*     */     
/*     */     public Properties pickaxe(ToolMaterial material, float attackDamageBaseline, float attackSpeedBaseline) {
/* 265 */       return tool(material, BlockTags.MINEABLE_WITH_PICKAXE, attackDamageBaseline, attackSpeedBaseline, 0.0F);
/*     */     }
/*     */     
/*     */     public Properties axe(ToolMaterial material, float attackDamageBaseline, float attackSpeedBaseline) {
/* 269 */       return tool(material, BlockTags.MINEABLE_WITH_AXE, attackDamageBaseline, attackSpeedBaseline, 5.0F);
/*     */     }
/*     */     
/*     */     public Properties hoe(ToolMaterial material, float attackDamageBaseline, float attackSpeedBaseline) {
/* 273 */       return tool(material, BlockTags.MINEABLE_WITH_HOE, attackDamageBaseline, attackSpeedBaseline, 0.0F);
/*     */     }
/*     */     
/*     */     public Properties shovel(ToolMaterial material, float attackDamageBaseline, float attackSpeedBaseline) {
/* 277 */       return tool(material, BlockTags.MINEABLE_WITH_SHOVEL, attackDamageBaseline, attackSpeedBaseline, 0.0F);
/*     */     }
/*     */     
/*     */     public Properties sword(ToolMaterial material, float attackDamageBaseline, float attackSpeedBaseline) {
/* 281 */       return material.applySwordProperties(this, attackDamageBaseline, attackSpeedBaseline);
/*     */     }
/*     */     
/*     */     public Properties spear(ToolMaterial material, float attackDuration, float damageMultiplier, float delay, float dismountTime, float dismountThreshold, float knockbackTime, float knockbackThreshold, float damageTime, float damageThreshold) {
/* 285 */       return durability(material.durability())
/* 286 */         .repairable(material.repairItems())
/* 287 */         .enchantable(material.enchantmentValue())
/* 288 */         .delayedHolderComponent(DataComponents.DAMAGE_TYPE, DamageTypes.SPEAR)
/* 289 */         .<KineticWeapon>component(DataComponents.KINETIC_WEAPON, new KineticWeapon(10, (int)(delay * 20.0F), 
/*     */             
/* 292 */             KineticWeapon.Condition.ofAttackerSpeed((int)(dismountTime * 20.0F), dismountThreshold), 
/* 293 */             KineticWeapon.Condition.ofAttackerSpeed((int)(knockbackTime * 20.0F), knockbackThreshold), 
/* 294 */             KineticWeapon.Condition.ofRelativeSpeed((int)(damageTime * 20.0F), damageThreshold), 0.38F, damageMultiplier, 
/*     */             
/* 297 */             Optional.of((material == ToolMaterial.WOOD) ? SoundEvents.SPEAR_WOOD_USE : SoundEvents.SPEAR_USE), 
/* 298 */             Optional.of((material == ToolMaterial.WOOD) ? SoundEvents.SPEAR_WOOD_HIT : SoundEvents.SPEAR_HIT)))
/*     */         
/* 300 */         .<PiercingWeapon>component(DataComponents.PIERCING_WEAPON, new PiercingWeapon(true, false, 
/*     */             
/* 303 */             Optional.of((material == ToolMaterial.WOOD) ? SoundEvents.SPEAR_WOOD_ATTACK : SoundEvents.SPEAR_ATTACK), 
/* 304 */             Optional.of((material == ToolMaterial.WOOD) ? SoundEvents.SPEAR_WOOD_HIT : SoundEvents.SPEAR_HIT)))
/*     */         
/* 306 */         .<AttackRange>component(DataComponents.ATTACK_RANGE, new AttackRange(2.0F, 4.5F, 2.0F, 6.5F, 0.125F, 0.5F))
/* 307 */         .<Float>component(DataComponents.MINIMUM_ATTACK_CHARGE, Float.valueOf(1.0F))
/* 308 */         .<SwingAnimation>component(DataComponents.SWING_ANIMATION, new SwingAnimation(SwingAnimationType.STAB, (int)(attackDuration * 20.0F)))
/*     */         
/* 312 */         .attributes(ItemAttributeModifiers.builder()
/* 313 */           .add(Attributes.ATTACK_DAMAGE, new AttributeModifier(Item.BASE_ATTACK_DAMAGE_ID, (0.0F + material.attackDamageBonus()), AttributeModifier.Operation.ADD_VALUE), EquipmentSlotGroup.MAINHAND)
/* 314 */           .add(Attributes.ATTACK_SPEED, new AttributeModifier(Item.BASE_ATTACK_SPEED_ID, (1.0F / attackDuration) - 4.0D, AttributeModifier.Operation.ADD_VALUE), EquipmentSlotGroup.MAINHAND)
/* 315 */           .build())
/* 316 */         .<UseEffects>component(DataComponents.USE_EFFECTS, new UseEffects(true, false, 1.0F))
/* 317 */         .component(DataComponents.WEAPON, new Weapon(1));
/*     */     }
/*     */     
/*     */     public Properties spawnEgg(EntityType<?> type) {
/* 321 */       return component(DataComponents.ENTITY_DATA, TypedEntityData.of(type, new CompoundTag()))
/* 322 */         .requiredFeatures(type.requiredFeatures());
/*     */     }
/*     */     
/*     */     public Properties humanoidArmor(ArmorMaterial material, ArmorType type) {
/* 326 */       return durability(type.getDurability(material.durability()))
/* 327 */         .attributes(material.createAttributes(type))
/* 328 */         .enchantable(material.enchantmentValue())
/* 329 */         .<Equippable>component(DataComponents.EQUIPPABLE, Equippable.builder(type.getSlot()).setEquipSound(material.equipSound()).setAsset(material.assetId()).build())
/* 330 */         .repairable(material.repairIngredient());
/*     */     }
/*     */     
/*     */     public Properties wolfArmor(ArmorMaterial material) {
/* 334 */       return durability(ArmorType.BODY.getDurability(material.durability()))
/* 335 */         .attributes(material.createAttributes(ArmorType.BODY))
/* 336 */         .repairable(material.repairIngredient())
/* 337 */         .<Equippable>component(DataComponents.EQUIPPABLE, 
/* 338 */           Equippable.builder(EquipmentSlot.BODY)
/* 339 */           .setEquipSound(material.equipSound())
/* 340 */           .setAsset(material.assetId())
/* 341 */           .setAllowedEntities((HolderSet)HolderSet.direct(new Holder[] { (Holder)EntityTypes.WOLF.builtInRegistryHolder() })).setCanBeSheared(true)
/* 343 */           .setShearingSound(BuiltInRegistries.SOUND_EVENT.wrapAsHolder(SoundEvents.ARMOR_UNEQUIP_WOLF))
/* 344 */           .build())
/*     */         
/* 346 */         .<Holder.Reference>component(DataComponents.BREAK_SOUND, SoundEvents.WOLF_ARMOR_BREAK)
/* 347 */         .stacksTo(1);
/*     */     }
/*     */     
/*     */     public Properties horseArmor(ArmorMaterial material) {
/* 351 */       HolderGetter<EntityType<?>> entityGetter = BuiltInRegistries.acquireBootstrapRegistrationLookup((Registry)BuiltInRegistries.ENTITY_TYPE);
/* 352 */       return attributes(material.createAttributes(ArmorType.BODY))
/* 353 */         .<Equippable>component(DataComponents.EQUIPPABLE, 
/* 354 */           Equippable.builder(EquipmentSlot.BODY)
/* 355 */           .setEquipSound((Holder)SoundEvents.HORSE_ARMOR)
/* 356 */           .setAsset(material.assetId())
/* 357 */           .setAllowedEntities((HolderSet)entityGetter.getOrThrow(EntityTypeTags.CAN_WEAR_HORSE_ARMOR))
/* 358 */           .setDamageOnHurt(false)
/* 359 */           .setCanBeSheared(true)
/* 360 */           .setShearingSound((Holder)SoundEvents.HORSE_ARMOR_UNEQUIP)
/* 361 */           .build())
/*     */         
/* 363 */         .stacksTo(1);
/*     */     }
/*     */     
/*     */     public Properties nautilusArmor(ArmorMaterial material) {
/* 367 */       HolderGetter<EntityType<?>> entityGetter = BuiltInRegistries.acquireBootstrapRegistrationLookup((Registry)BuiltInRegistries.ENTITY_TYPE);
/* 368 */       return attributes(material.createAttributes(ArmorType.BODY))
/* 369 */         .<Equippable>component(DataComponents.EQUIPPABLE, 
/* 370 */           Equippable.builder(EquipmentSlot.BODY)
/* 371 */           .setEquipSound((Holder)SoundEvents.ARMOR_EQUIP_NAUTILUS)
/* 372 */           .setAsset(material.assetId())
/* 373 */           .setAllowedEntities((HolderSet)entityGetter.getOrThrow(EntityTypeTags.CAN_WEAR_NAUTILUS_ARMOR))
/* 374 */           .setDamageOnHurt(false)
/* 375 */           .setEquipOnInteract(true)
/* 376 */           .setCanBeSheared(true)
/* 377 */           .setShearingSound((Holder)SoundEvents.ARMOR_UNEQUIP_NAUTILUS)
/* 378 */           .build())
/*     */         
/* 380 */         .stacksTo(1);
/*     */     }
/*     */     
/*     */     public Properties trimMaterial(ResourceKey<TrimMaterial> material) {
/* 384 */       return delayedHolderComponent(DataComponents.PROVIDES_TRIM_MATERIAL, material);
/*     */     }
/*     */     
/*     */     public Properties requiredFeatures(FeatureFlag... flags) {
/* 388 */       this.requiredFeatures = FeatureFlags.REGISTRY.subset(flags);
/* 389 */       return this;
/*     */     }
/*     */     
/*     */     public Properties requiredFeatures(FeatureFlagSet flags) {
/* 393 */       if (!FeatureFlags.REGISTRY.isSubset(flags))
/* 394 */         throw new IllegalArgumentException("Mismatched flag sets"); 
/* 396 */       this.requiredFeatures = flags;
/* 397 */       return this;
/*     */     }
/*     */     
/*     */     public Properties setId(ResourceKey<Item> id) {
/* 401 */       this.id = id;
/* 402 */       return this;
/*     */     }
/*     */     
/*     */     public Properties overrideDescription(String descriptionId) {
/* 406 */       this.descriptionId = DependantName.fixed(descriptionId);
/* 407 */       return this;
/*     */     }
/*     */     
/*     */     public Properties useBlockDescriptionPrefix() {
/* 411 */       this.descriptionId = BLOCK_DESCRIPTION_ID;
/* 412 */       return this;
/*     */     }
/*     */     
/*     */     public Properties useItemDescriptionPrefix() {
/* 417 */       this.descriptionId = ITEM_DESCRIPTION_ID;
/* 418 */       return this;
/*     */     }
/*     */     
/*     */     private ResourceKey<Item> itemIdOrThrow() {
/* 422 */       return Objects.<ResourceKey<Item>>requireNonNull(this.id, "Item id not set");
/*     */     }
/*     */     
/*     */     protected String effectiveDescriptionId() {
/* 426 */       return (String)this.descriptionId.get(itemIdOrThrow());
/*     */     }
/*     */     
/*     */     public Identifier effectiveModel() {
/* 430 */       return (Identifier)this.model.get(itemIdOrThrow());
/*     */     }
/*     */     
/*     */     public <T> Properties component(DataComponentType<T> type, T value) {
/* 434 */       this.componentInitializer = this.componentInitializer.add(type, value);
/* 435 */       return this;
/*     */     }
/*     */     
/*     */     public <T> Properties delayedComponent(DataComponentType<T> type, DataComponentInitializers.SingleComponentInitializer<T> initializer) {
/* 439 */       this.componentInitializer = this.componentInitializer.andThen(initializer.asInitializer(type));
/* 440 */       return this;
/*     */     }
/*     */     
/*     */     public <T> Properties delayedHolderComponent(DataComponentType<Holder<T>> type, ResourceKey<T> valueKey) {
/* 444 */       this.componentInitializer = this.componentInitializer.andThen((components, context, key) -> components.set(type, context.getOrThrow(valueKey)));
/* 445 */       return this;
/*     */     }
/*     */     
/*     */     public Properties attributes(ItemAttributeModifiers attributes) {
/* 449 */       return component(DataComponents.ATTRIBUTE_MODIFIERS, attributes);
/*     */     }
/*     */     
/*     */     private DataComponentInitializers.Initializer<Item> finalizeInitializer(Component name, Identifier model) {
/* 453 */       return this.componentInitializer.andThen((components, context, key) -> components.set(DataComponents.ITEM_NAME, name).set(DataComponents.ITEM_MODEL, model).addValidator(()));
/*     */     }
/*     */   }
/*     */   
/*     */   public void onUseTick(Level level, LivingEntity livingEntity, ItemStack itemStack, int ticksRemaining) {}
/*     */   
/*     */   public void onDestroyed(ItemEntity itemEntity) {}
/*     */   
/*     */   public boolean canDestroyBlock(ItemStack itemStack, BlockState state, Level level, BlockPos pos, LivingEntity user) {
/* 474 */     Tool tool = (Tool)itemStack.get(DataComponents.TOOL);
/* 475 */     if (tool != null && !tool.canDestroyBlocksInCreative()) {
/* 476 */       if (user instanceof Player) {
/* 476 */         Player player = (Player)user;
/* 476 */         if (!(player.getAbilities()).instabuild);
/* 476 */         return false;
/*     */       } 
/*     */     } else {
/* 478 */       return true;
/*     */     } 
/*     */   }
/*     */   
/*     */   public Item asItem() {
/* 483 */     return this;
/*     */   }
/*     */   
/*     */   public InteractionResult useOn(UseOnContext context) {
/* 487 */     return (InteractionResult)InteractionResult.PASS;
/*     */   }
/*     */   
/*     */   public float getDestroySpeed(ItemStack itemStack, BlockState state) {
/* 491 */     Tool tool = (Tool)itemStack.get(DataComponents.TOOL);
/* 492 */     return (tool != null) ? tool.getMiningSpeed(state) : 1.0F;
/*     */   }
/*     */   
/*     */   public InteractionResult use(Level level, Player player, InteractionHand hand) {
/* 496 */     ItemStack stack = player.getItemInHand(hand);
/* 498 */     Consumable consumable = (Consumable)stack.get(DataComponents.CONSUMABLE);
/* 499 */     if (consumable != null)
/* 500 */       return consumable.startConsuming((LivingEntity)player, stack, hand); 
/* 503 */     Equippable equippable = (Equippable)stack.get(DataComponents.EQUIPPABLE);
/* 504 */     if (equippable != null && equippable.swappable())
/* 505 */       return equippable.swapWithEquipmentSlot(stack, player); 
/* 508 */     if (stack.has(DataComponents.BLOCKS_ATTACKS)) {
/* 509 */       player.startUsingItem(hand);
/* 510 */       return (InteractionResult)InteractionResult.CONSUME;
/*     */     } 
/* 513 */     KineticWeapon kineticWeapon = (KineticWeapon)stack.get(DataComponents.KINETIC_WEAPON);
/* 514 */     if (kineticWeapon != null) {
/* 515 */       player.startUsingItem(hand);
/* 516 */       kineticWeapon.makeSound((Entity)player);
/* 517 */       return (InteractionResult)InteractionResult.CONSUME;
/*     */     } 
/* 520 */     return (InteractionResult)InteractionResult.PASS;
/*     */   }
/*     */   
/*     */   public ItemStack finishUsingItem(ItemStack itemStack, Level level, LivingEntity entity) {
/* 524 */     Consumable consumable = (Consumable)itemStack.get(DataComponents.CONSUMABLE);
/* 525 */     if (consumable != null)
/* 526 */       return consumable.onConsume(level, entity, itemStack); 
/* 528 */     return itemStack;
/*     */   }
/*     */   
/*     */   public boolean isBarVisible(ItemStack stack) {
/* 532 */     return stack.isDamaged();
/*     */   }
/*     */   
/*     */   public int getBarWidth(ItemStack stack) {
/* 536 */     return Mth.clamp(Math.round(13.0F - stack.getDamageValue() * 13.0F / stack.getMaxDamage()), 0, 13);
/*     */   }
/*     */   
/*     */   public int getBarColor(ItemStack stack) {
/* 540 */     int maxDamage = stack.getMaxDamage();
/* 541 */     float healthPercentage = Math.max(0.0F, (maxDamage - stack.getDamageValue()) / maxDamage);
/* 544 */     return Mth.hsvToRgb(healthPercentage / 3.0F, 1.0F, 1.0F);
/*     */   }
/*     */   
/*     */   public boolean overrideStackedOnOther(ItemStack self, Slot slot, ClickAction clickAction, Player player) {
/* 549 */     return false;
/*     */   }
/*     */   
/*     */   public boolean overrideOtherStackedOnMe(ItemStack self, ItemStack other, Slot slot, ClickAction clickAction, Player player, SlotAccess carriedItem) {
/* 554 */     return false;
/*     */   }
/*     */   
/*     */   public float getAttackDamageBonus(Entity victim, float damage, DamageSource damageSource) {
/* 558 */     return 0.0F;
/*     */   }
/*     */   
/*     */   @Deprecated
/*     */   public DamageSource getItemDamageSource(LivingEntity attacker) {
/* 564 */     return null;
/*     */   }
/*     */   
/*     */   public void hurtEnemy(ItemStack itemStack, LivingEntity mob, LivingEntity attacker) {}
/*     */   
/*     */   public void postHurtEnemy(ItemStack itemStack, LivingEntity mob, LivingEntity attacker) {}
/*     */   
/*     */   public boolean mineBlock(ItemStack itemStack, Level level, BlockState state, BlockPos pos, LivingEntity owner) {
/* 575 */     Tool tool = (Tool)itemStack.get(DataComponents.TOOL);
/* 576 */     if (tool == null)
/* 577 */       return false; 
/* 580 */     if (!level.isClientSide() && state.getDestroySpeed((BlockGetter)level, pos) != 0.0F && 
/* 581 */       tool.damagePerBlock() > 0)
/* 582 */       itemStack.hurtAndBreak(tool.damagePerBlock(), owner, EquipmentSlot.MAINHAND); 
/* 585 */     return true;
/*     */   }
/*     */   
/*     */   public boolean isCorrectToolForDrops(ItemStack itemStack, BlockState state) {
/* 589 */     Tool tool = (Tool)itemStack.get(DataComponents.TOOL);
/* 590 */     return (tool != null && tool.isCorrectForDrops(state));
/*     */   }
/*     */   
/*     */   public InteractionResult interactLivingEntity(ItemStack itemStack, Player player, LivingEntity target, InteractionHand type) {
/* 594 */     return (InteractionResult)InteractionResult.PASS;
/*     */   }
/*     */   
/*     */   public String toString() {
/* 599 */     return BuiltInRegistries.ITEM.wrapAsHolder(this).getRegisteredName();
/*     */   }
/*     */   
/*     */   public final ItemStackTemplate getCraftingRemainder() {
/* 603 */     return this.craftingRemainingItem;
/*     */   }
/*     */   
/*     */   public void inventoryTick(ItemStack itemStack, ServerLevel level, Entity owner, EquipmentSlot slot) {}
/*     */   
/*     */   public void onCraftedBy(ItemStack itemStack, Player player) {
/* 611 */     onCraftedPostProcess(itemStack, player.level());
/*     */   }
/*     */   
/*     */   public void onCraftedPostProcess(ItemStack itemStack, Level level) {}
/*     */   
/*     */   public ItemUseAnimation getUseAnimation(ItemStack itemStack) {
/* 618 */     Consumable consumable = (Consumable)itemStack.get(DataComponents.CONSUMABLE);
/* 619 */     if (consumable != null)
/* 620 */       return consumable.animation(); 
/* 622 */     if (itemStack.has(DataComponents.BLOCKS_ATTACKS))
/* 623 */       return ItemUseAnimation.BLOCK; 
/* 625 */     if (itemStack.has(DataComponents.KINETIC_WEAPON))
/* 626 */       return ItemUseAnimation.SPEAR; 
/* 628 */     return ItemUseAnimation.NONE;
/*     */   }
/*     */   
/*     */   public int getUseDuration(ItemStack itemStack, LivingEntity user) {
/* 632 */     Consumable consumable = (Consumable)itemStack.get(DataComponents.CONSUMABLE);
/* 633 */     if (consumable != null)
/* 634 */       return consumable.consumeTicks(); 
/* 636 */     if (itemStack.has(DataComponents.BLOCKS_ATTACKS) || itemStack.has(DataComponents.KINETIC_WEAPON))
/* 637 */       return 72000; 
/* 639 */     return 0;
/*     */   }
/*     */   
/*     */   public boolean releaseUsing(ItemStack itemStack, Level level, LivingEntity entity, int remainingTime) {
/* 644 */     return false;
/*     */   }
/*     */   
/*     */   @Deprecated
/*     */   public void appendHoverText(ItemStack itemStack, TooltipContext context, TooltipDisplay display, Consumer<Component> builder, TooltipFlag tooltipFlag) {}
/*     */   
/*     */   public Optional<TooltipComponent> getTooltipImage(ItemStack itemStack) {
/* 653 */     return Optional.empty();
/*     */   }
/*     */   
/*     */   @VisibleForTesting
/*     */   public final String getDescriptionId() {
/* 658 */     return this.descriptionId;
/*     */   }
/*     */   
/*     */   public Component getName(ItemStack itemStack) {
/* 662 */     return (Component)itemStack.getComponents().getOrDefault(DataComponents.ITEM_NAME, CommonComponents.EMPTY);
/*     */   }
/*     */   
/*     */   public boolean isFoil(ItemStack itemStack) {
/* 666 */     return itemStack.isEnchanted();
/*     */   }
/*     */   
/*     */   protected static BlockHitResult getPlayerPOVHitResult(Level level, Player player, ClipContext.Fluid fluid) {
/* 670 */     Vec3 from = player.getEyePosition();
/* 672 */     Vec3 to = from.add(player.calculateViewVector(player.getXRot(), player.getYRot()).scale(player.blockInteractionRange()));
/* 674 */     return level.clip(new ClipContext(from, to, ClipContext.Block.OUTLINE, fluid, (Entity)player));
/*     */   }
/*     */   
/*     */   public boolean useOnRelease(ItemStack itemStack) {
/* 678 */     return false;
/*     */   }
/*     */   
/*     */   public ItemStack getDefaultInstance() {
/* 682 */     return new ItemStack(this);
/*     */   }
/*     */   
/*     */   public boolean canFitInsideContainerItems() {
/* 686 */     return true;
/*     */   }
/*     */   
/*     */   public final FeatureFlagSet requiredFeatures() {
/* 691 */     return this.requiredFeatures;
/*     */   }
/*     */   
/*     */   public boolean shouldPrintOpWarning(ItemStack stack, Player player) {
/* 695 */     return false;
/*     */   }
/*     */   
/*     */   class null implements TooltipContext {
/*     */     public HolderLookup.Provider registries() {
/* 705 */       return null;
/*     */     }
/*     */     
/*     */     public float tickRate() {
/* 710 */       return 20.0F;
/*     */     }
/*     */     
/*     */     public MapItemSavedData mapData(MapId id) {
/* 715 */       return null;
/*     */     }
/*     */     
/*     */     public boolean isPeaceful() {
/* 720 */       return false;
/*     */     }
/*     */   }
/*     */   
/*     */   public static interface TooltipContext {
/*     */     public static final TooltipContext EMPTY = new TooltipContext() {
/*     */         public HolderLookup.Provider registries() {
/*     */           return null;
/*     */         }
/*     */         
/*     */         public float tickRate() {
/*     */           return 20.0F;
/*     */         }
/*     */         
/*     */         public MapItemSavedData mapData(MapId id) {
/*     */           return null;
/*     */         }
/*     */         
/*     */         public boolean isPeaceful() {
/* 720 */           return false;
/*     */         }
/*     */       };
/*     */     
/*     */     HolderLookup.Provider registries();
/*     */     
/*     */     float tickRate();
/*     */     
/*     */     MapItemSavedData mapData(MapId param1MapId);
/*     */     
/*     */     boolean isPeaceful();
/*     */     
/*     */     static TooltipContext of(final Level level) {
/* 733 */       if (level == null)
/* 734 */         return EMPTY; 
/* 737 */       return new TooltipContext() {
/*     */           public HolderLookup.Provider registries() {
/* 740 */             return (HolderLookup.Provider)level.registryAccess();
/*     */           }
/*     */           
/*     */           public float tickRate() {
/* 745 */             return level.tickRateManager().tickrate();
/*     */           }
/*     */           
/*     */           public MapItemSavedData mapData(MapId id) {
/* 750 */             return level.getMapData(id);
/*     */           }
/*     */           
/*     */           public boolean isPeaceful() {
/* 755 */             return (level.getDifficulty() == Difficulty.PEACEFUL);
/*     */           }
/*     */         };
/*     */     }
/*     */     
/*     */     static TooltipContext of(final HolderLookup.Provider registries) {
/* 761 */       return new TooltipContext() {
/*     */           public HolderLookup.Provider registries() {
/* 764 */             return registries;
/*     */           }
/*     */           
/*     */           public float tickRate() {
/* 769 */             return 20.0F;
/*     */           }
/*     */           
/*     */           public MapItemSavedData mapData(MapId id) {
/* 774 */             return null;
/*     */           }
/*     */           
/*     */           public boolean isPeaceful() {
/* 779 */             return false;
/*     */           }
/*     */         };
/*     */     }
/*     */   }
/*     */   
/*     */   class null implements TooltipContext {
/*     */     public HolderLookup.Provider registries() {
/*     */       return (HolderLookup.Provider)level.registryAccess();
/*     */     }
/*     */     
/*     */     public float tickRate() {
/*     */       return level.tickRateManager().tickrate();
/*     */     }
/*     */     
/*     */     public MapItemSavedData mapData(MapId id) {
/*     */       return level.getMapData(id);
/*     */     }
/*     */     
/*     */     public boolean isPeaceful() {
/*     */       return (level.getDifficulty() == Difficulty.PEACEFUL);
/*     */     }
/*     */   }
/*     */   
/*     */   class null implements TooltipContext {
/*     */     public HolderLookup.Provider registries() {
/*     */       return registries;
/*     */     }
/*     */     
/*     */     public float tickRate() {
/*     */       return 20.0F;
/*     */     }
/*     */     
/*     */     public MapItemSavedData mapData(MapId id) {
/*     */       return null;
/*     */     }
/*     */     
/*     */     public boolean isPeaceful() {
/* 779 */       return false;
/*     */     }
/*     */   }
/*     */ }


/* Location:              C:\Users\karol\AppData\Roaming\ModrinthApp\meta\versions\26.2-0.19.5\26.2-0.19.5.jar!\net\minecraft\world\item\Item.class
 * Java compiler version: 25 (69.0)
 * JD-Core Version:       1.1.3
 */