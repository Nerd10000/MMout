/*     */ package net.minecraft.core.component;
/*     */ 
/*     */ import com.mojang.serialization.Codec;
/*     */ import java.util.List;
/*     */ import java.util.function.UnaryOperator;
/*     */ import net.minecraft.core.Holder;
/*     */ import net.minecraft.core.HolderSet;
/*     */ import net.minecraft.core.Registry;
/*     */ import net.minecraft.core.RegistryCodecs;
/*     */ import net.minecraft.core.registries.BuiltInRegistries;
/*     */ import net.minecraft.core.registries.Registries;
/*     */ import net.minecraft.network.chat.Component;
/*     */ import net.minecraft.network.chat.ComponentSerialization;
/*     */ import net.minecraft.network.codec.ByteBufCodecs;
/*     */ import net.minecraft.resources.Identifier;
/*     */ import net.minecraft.resources.ResourceKey;
/*     */ import net.minecraft.sounds.SoundEvent;
/*     */ import net.minecraft.sounds.SoundEvents;
/*     */ import net.minecraft.util.EncoderCache;
/*     */ import net.minecraft.util.ExtraCodecs;
/*     */ import net.minecraft.util.Unit;
/*     */ import net.minecraft.world.LockCode;
/*     */ import net.minecraft.world.damagesource.DamageType;
/*     */ import net.minecraft.world.entity.EntityType;
/*     */ import net.minecraft.world.entity.animal.axolotl.Axolotl;
/*     */ import net.minecraft.world.entity.animal.chicken.ChickenSoundVariant;
/*     */ import net.minecraft.world.entity.animal.chicken.ChickenVariant;
/*     */ import net.minecraft.world.entity.animal.cow.CowSoundVariant;
/*     */ import net.minecraft.world.entity.animal.cow.CowVariant;
/*     */ import net.minecraft.world.entity.animal.cow.MushroomCow;
/*     */ import net.minecraft.world.entity.animal.equine.Llama;
/*     */ import net.minecraft.world.entity.animal.equine.Variant;
/*     */ import net.minecraft.world.entity.animal.feline.CatSoundVariant;
/*     */ import net.minecraft.world.entity.animal.feline.CatVariant;
/*     */ import net.minecraft.world.entity.animal.fish.Salmon;
/*     */ import net.minecraft.world.entity.animal.fish.TropicalFish;
/*     */ import net.minecraft.world.entity.animal.fox.Fox;
/*     */ import net.minecraft.world.entity.animal.frog.FrogVariant;
/*     */ import net.minecraft.world.entity.animal.nautilus.ZombieNautilusVariant;
/*     */ import net.minecraft.world.entity.animal.parrot.Parrot;
/*     */ import net.minecraft.world.entity.animal.pig.PigSoundVariant;
/*     */ import net.minecraft.world.entity.animal.pig.PigVariant;
/*     */ import net.minecraft.world.entity.animal.rabbit.Rabbit;
/*     */ import net.minecraft.world.entity.animal.wolf.WolfSoundVariant;
/*     */ import net.minecraft.world.entity.animal.wolf.WolfVariant;
/*     */ import net.minecraft.world.entity.decoration.painting.PaintingVariant;
/*     */ import net.minecraft.world.entity.npc.villager.VillagerType;
/*     */ import net.minecraft.world.food.FoodProperties;
/*     */ import net.minecraft.world.item.AdventureModePredicate;
/*     */ import net.minecraft.world.item.DyeColor;
/*     */ import net.minecraft.world.item.JukeboxPlayable;
/*     */ import net.minecraft.world.item.Rarity;
/*     */ import net.minecraft.world.item.alchemy.PotionContents;
/*     */ import net.minecraft.world.item.component.AttackRange;
/*     */ import net.minecraft.world.item.component.Bees;
/*     */ import net.minecraft.world.item.component.BlockItemStateProperties;
/*     */ import net.minecraft.world.item.component.BlocksAttacks;
/*     */ import net.minecraft.world.item.component.BundleContents;
/*     */ import net.minecraft.world.item.component.ChargedProjectiles;
/*     */ import net.minecraft.world.item.component.Consumable;
/*     */ import net.minecraft.world.item.component.CustomData;
/*     */ import net.minecraft.world.item.component.CustomModelData;
/*     */ import net.minecraft.world.item.component.DamageResistant;
/*     */ import net.minecraft.world.item.component.DeathProtection;
/*     */ import net.minecraft.world.item.component.DebugStickState;
/*     */ import net.minecraft.world.item.component.DyedItemColor;
/*     */ import net.minecraft.world.item.component.FireworkExplosion;
/*     */ import net.minecraft.world.item.component.Fireworks;
/*     */ import net.minecraft.world.item.component.InstrumentComponent;
/*     */ import net.minecraft.world.item.component.ItemAttributeModifiers;
/*     */ import net.minecraft.world.item.component.ItemContainerContents;
/*     */ import net.minecraft.world.item.component.ItemLore;
/*     */ import net.minecraft.world.item.component.KineticWeapon;
/*     */ import net.minecraft.world.item.component.LodestoneTracker;
/*     */ import net.minecraft.world.item.component.MapDecorations;
/*     */ import net.minecraft.world.item.component.MapItemColor;
/*     */ import net.minecraft.world.item.component.MapPostProcessing;
/*     */ import net.minecraft.world.item.component.OminousBottleAmplifier;
/*     */ import net.minecraft.world.item.component.PiercingWeapon;
/*     */ import net.minecraft.world.item.component.ResolvableProfile;
/*     */ import net.minecraft.world.item.component.SeededContainerLoot;
/*     */ import net.minecraft.world.item.component.SulfurCubeContent;
/*     */ import net.minecraft.world.item.component.SuspiciousStewEffects;
/*     */ import net.minecraft.world.item.component.SwingAnimation;
/*     */ import net.minecraft.world.item.component.Tool;
/*     */ import net.minecraft.world.item.component.TooltipDisplay;
/*     */ import net.minecraft.world.item.component.TypedEntityData;
/*     */ import net.minecraft.world.item.component.UseCooldown;
/*     */ import net.minecraft.world.item.component.UseEffects;
/*     */ import net.minecraft.world.item.component.UseRemainder;
/*     */ import net.minecraft.world.item.component.Weapon;
/*     */ import net.minecraft.world.item.component.WritableBookContent;
/*     */ import net.minecraft.world.item.component.WrittenBookContent;
/*     */ import net.minecraft.world.item.crafting.Recipe;
/*     */ import net.minecraft.world.item.enchantment.Enchantable;
/*     */ import net.minecraft.world.item.enchantment.ItemEnchantments;
/*     */ import net.minecraft.world.item.enchantment.Repairable;
/*     */ import net.minecraft.world.item.equipment.Equippable;
/*     */ import net.minecraft.world.item.equipment.trim.ArmorTrim;
/*     */ import net.minecraft.world.item.equipment.trim.TrimMaterial;
/*     */ import net.minecraft.world.level.block.entity.BannerPattern;
/*     */ import net.minecraft.world.level.block.entity.BannerPatternLayers;
/*     */ import net.minecraft.world.level.block.entity.BlockEntityType;
/*     */ import net.minecraft.world.level.block.entity.PotDecorations;
/*     */ import net.minecraft.world.level.saveddata.maps.MapId;
/*     */ 
/*     */ public class DataComponents {
/* 113 */   static final EncoderCache ENCODER_CACHE = new EncoderCache(512);
/*     */   
/*     */   public static final DataComponentType<CustomData> CUSTOM_DATA;
/*     */   
/*     */   public static final DataComponentType<Integer> MAX_STACK_SIZE;
/*     */   
/*     */   public static final DataComponentType<Integer> MAX_DAMAGE;
/*     */   
/*     */   public static final DataComponentType<Integer> DAMAGE;
/*     */   
/*     */   public static final DataComponentType<Unit> UNBREAKABLE;
/*     */   
/*     */   public static final DataComponentType<UseEffects> USE_EFFECTS;
/*     */   
/*     */   public static final DataComponentType<Component> CUSTOM_NAME;
/*     */   
/*     */   public static final DataComponentType<Float> MINIMUM_ATTACK_CHARGE;
/*     */   
/*     */   public static final DataComponentType<Holder<DamageType>> DAMAGE_TYPE;
/*     */   
/*     */   public static final DataComponentType<Component> ITEM_NAME;
/*     */   
/*     */   public static final DataComponentType<Identifier> ITEM_MODEL;
/*     */   
/*     */   public static final DataComponentType<ItemLore> LORE;
/*     */   
/*     */   public static final DataComponentType<Rarity> RARITY;
/*     */   
/*     */   public static final DataComponentType<ItemEnchantments> ENCHANTMENTS;
/*     */   
/*     */   public static final DataComponentType<AdventureModePredicate> CAN_PLACE_ON;
/*     */   
/*     */   public static final DataComponentType<AdventureModePredicate> CAN_BREAK;
/*     */   
/*     */   public static final DataComponentType<ItemAttributeModifiers> ATTRIBUTE_MODIFIERS;
/*     */   
/*     */   public static final DataComponentType<CustomModelData> CUSTOM_MODEL_DATA;
/*     */   
/*     */   public static final DataComponentType<TooltipDisplay> TOOLTIP_DISPLAY;
/*     */   
/*     */   public static final DataComponentType<Integer> REPAIR_COST;
/*     */   
/*     */   public static final DataComponentType<Unit> CREATIVE_SLOT_LOCK;
/*     */   
/*     */   public static final DataComponentType<Boolean> ENCHANTMENT_GLINT_OVERRIDE;
/*     */   
/*     */   public static final DataComponentType<Unit> INTANGIBLE_PROJECTILE;
/*     */   
/*     */   public static final DataComponentType<FoodProperties> FOOD;
/*     */   
/*     */   public static final DataComponentType<Consumable> CONSUMABLE;
/*     */   
/*     */   public static final DataComponentType<UseRemainder> USE_REMAINDER;
/*     */   
/*     */   public static final DataComponentType<UseCooldown> USE_COOLDOWN;
/*     */   
/*     */   public static final DataComponentType<DamageResistant> DAMAGE_RESISTANT;
/*     */   
/*     */   public static final DataComponentType<Tool> TOOL;
/*     */   
/*     */   public static final DataComponentType<Weapon> WEAPON;
/*     */   
/*     */   public static final DataComponentType<AttackRange> ATTACK_RANGE;
/*     */   
/*     */   public static final DataComponentType<Enchantable> ENCHANTABLE;
/*     */   
/*     */   public static final DataComponentType<Equippable> EQUIPPABLE;
/*     */   
/*     */   public static final DataComponentType<Repairable> REPAIRABLE;
/*     */   
/*     */   public static final DataComponentType<Unit> GLIDER;
/*     */   
/*     */   public static final DataComponentType<Identifier> TOOLTIP_STYLE;
/*     */   
/*     */   public static final DataComponentType<DeathProtection> DEATH_PROTECTION;
/*     */   
/*     */   public static final DataComponentType<BlocksAttacks> BLOCKS_ATTACKS;
/*     */   
/*     */   public static final DataComponentType<PiercingWeapon> PIERCING_WEAPON;
/*     */   
/*     */   public static final DataComponentType<KineticWeapon> KINETIC_WEAPON;
/*     */   
/*     */   public static final DataComponentType<SwingAnimation> SWING_ANIMATION;
/*     */   
/*     */   public static final DataComponentType<Integer> ADDITIONAL_TRADE_COST;
/*     */   
/*     */   public static final DataComponentType<ItemEnchantments> STORED_ENCHANTMENTS;
/*     */   
/*     */   public static final DataComponentType<DyeColor> DYE;
/*     */   
/*     */   public static final DataComponentType<DyedItemColor> DYED_COLOR;
/*     */   
/*     */   public static final DataComponentType<MapItemColor> MAP_COLOR;
/*     */   
/*     */   public static final DataComponentType<MapId> MAP_ID;
/*     */   
/*     */   public static final DataComponentType<MapDecorations> MAP_DECORATIONS;
/*     */   
/*     */   public static final DataComponentType<MapPostProcessing> MAP_POST_PROCESSING;
/*     */   
/*     */   public static final DataComponentType<ChargedProjectiles> CHARGED_PROJECTILES;
/*     */   
/*     */   public static final DataComponentType<BundleContents> BUNDLE_CONTENTS;
/*     */   
/*     */   public static final DataComponentType<PotionContents> POTION_CONTENTS;
/*     */   
/*     */   public static final DataComponentType<Float> POTION_DURATION_SCALE;
/*     */   
/*     */   public static final DataComponentType<SuspiciousStewEffects> SUSPICIOUS_STEW_EFFECTS;
/*     */   
/*     */   public static final DataComponentType<WritableBookContent> WRITABLE_BOOK_CONTENT;
/*     */   
/*     */   public static final DataComponentType<WrittenBookContent> WRITTEN_BOOK_CONTENT;
/*     */   
/*     */   public static final DataComponentType<ArmorTrim> TRIM;
/*     */   
/*     */   public static final DataComponentType<DebugStickState> DEBUG_STICK_STATE;
/*     */   
/*     */   public static final DataComponentType<TypedEntityData<EntityType<?>>> ENTITY_DATA;
/*     */   
/*     */   public static final DataComponentType<CustomData> BUCKET_ENTITY_DATA;
/*     */   
/*     */   public static final DataComponentType<TypedEntityData<BlockEntityType<?>>> BLOCK_ENTITY_DATA;
/*     */   
/*     */   public static final DataComponentType<InstrumentComponent> INSTRUMENT;
/*     */   
/*     */   public static final DataComponentType<Holder<TrimMaterial>> PROVIDES_TRIM_MATERIAL;
/*     */   
/*     */   public static final DataComponentType<OminousBottleAmplifier> OMINOUS_BOTTLE_AMPLIFIER;
/*     */   
/*     */   public static final DataComponentType<JukeboxPlayable> JUKEBOX_PLAYABLE;
/*     */   
/*     */   public static final DataComponentType<HolderSet<BannerPattern>> PROVIDES_BANNER_PATTERNS;
/*     */   
/*     */   public static final DataComponentType<List<ResourceKey<Recipe<?>>>> RECIPES;
/*     */   
/*     */   public static final DataComponentType<LodestoneTracker> LODESTONE_TRACKER;
/*     */   
/*     */   public static final DataComponentType<FireworkExplosion> FIREWORK_EXPLOSION;
/*     */   
/*     */   public static final DataComponentType<Fireworks> FIREWORKS;
/*     */   
/*     */   public static final DataComponentType<ResolvableProfile> PROFILE;
/*     */   
/*     */   public static final DataComponentType<Identifier> NOTE_BLOCK_SOUND;
/*     */   
/*     */   public static final DataComponentType<BannerPatternLayers> BANNER_PATTERNS;
/*     */   
/*     */   public static final DataComponentType<DyeColor> BASE_COLOR;
/*     */   
/*     */   public static final DataComponentType<PotDecorations> POT_DECORATIONS;
/*     */   
/*     */   public static final DataComponentType<ItemContainerContents> CONTAINER;
/*     */   
/*     */   public static final DataComponentType<BlockItemStateProperties> BLOCK_STATE;
/*     */   
/*     */   public static final DataComponentType<Bees> BEES;
/*     */   
/*     */   public static final DataComponentType<SulfurCubeContent> SULFUR_CUBE_CONTENT;
/*     */   
/*     */   public static final DataComponentType<LockCode> LOCK;
/*     */   
/*     */   public static final DataComponentType<SeededContainerLoot> CONTAINER_LOOT;
/*     */   
/*     */   public static final DataComponentType<Holder<SoundEvent>> BREAK_SOUND;
/*     */   
/*     */   public static final DataComponentType<Holder<VillagerType>> VILLAGER_VARIANT;
/*     */   
/*     */   public static final DataComponentType<Holder<WolfVariant>> WOLF_VARIANT;
/*     */   
/*     */   public static final DataComponentType<Holder<WolfSoundVariant>> WOLF_SOUND_VARIANT;
/*     */   
/*     */   public static final DataComponentType<DyeColor> WOLF_COLLAR;
/*     */   
/*     */   public static final DataComponentType<Fox.Variant> FOX_VARIANT;
/*     */   
/*     */   public static final DataComponentType<Salmon.Variant> SALMON_SIZE;
/*     */   
/*     */   public static final DataComponentType<Parrot.Variant> PARROT_VARIANT;
/*     */   
/*     */   public static final DataComponentType<TropicalFish.Pattern> TROPICAL_FISH_PATTERN;
/*     */   
/*     */   public static final DataComponentType<DyeColor> TROPICAL_FISH_BASE_COLOR;
/*     */   
/*     */   public static final DataComponentType<DyeColor> TROPICAL_FISH_PATTERN_COLOR;
/*     */   
/*     */   public static final DataComponentType<MushroomCow.Variant> MOOSHROOM_VARIANT;
/*     */   
/*     */   public static final DataComponentType<Rabbit.Variant> RABBIT_VARIANT;
/*     */   
/*     */   public static final DataComponentType<Holder<PigVariant>> PIG_VARIANT;
/*     */   
/*     */   public static final DataComponentType<Holder<PigSoundVariant>> PIG_SOUND_VARIANT;
/*     */   
/*     */   public static final DataComponentType<Holder<CowVariant>> COW_VARIANT;
/*     */   
/*     */   public static final DataComponentType<Holder<CowSoundVariant>> COW_SOUND_VARIANT;
/*     */   
/*     */   public static final DataComponentType<Holder<ChickenVariant>> CHICKEN_VARIANT;
/*     */   
/*     */   public static final DataComponentType<Holder<ChickenSoundVariant>> CHICKEN_SOUND_VARIANT;
/*     */   
/*     */   public static final DataComponentType<Holder<ZombieNautilusVariant>> ZOMBIE_NAUTILUS_VARIANT;
/*     */   
/*     */   public static final DataComponentType<Holder<FrogVariant>> FROG_VARIANT;
/*     */   
/*     */   public static final DataComponentType<Variant> HORSE_VARIANT;
/*     */   
/*     */   public static final DataComponentType<Holder<PaintingVariant>> PAINTING_VARIANT;
/*     */   
/*     */   public static final DataComponentType<Llama.Variant> LLAMA_VARIANT;
/*     */   
/*     */   public static final DataComponentType<Axolotl.Variant> AXOLOTL_VARIANT;
/*     */   
/*     */   public static final DataComponentType<Holder<CatVariant>> CAT_VARIANT;
/*     */   
/*     */   public static final DataComponentType<Holder<CatSoundVariant>> CAT_SOUND_VARIANT;
/*     */   
/*     */   public static final DataComponentType<DyeColor> CAT_COLLAR;
/*     */   
/*     */   public static final DataComponentType<DyeColor> SHEEP_COLOR;
/*     */   
/*     */   public static final DataComponentType<DyeColor> SHULKER_COLOR;
/*     */   
/*     */   static {
/* 117 */     CUSTOM_DATA = register("custom_data", b -> b.persistent(CustomData.CODEC));
/* 119 */     MAX_STACK_SIZE = register("max_stack_size", b -> b.persistent(ExtraCodecs.intRange(1, 99)).networkSynchronized(ByteBufCodecs.VAR_INT));
/* 122 */     MAX_DAMAGE = register("max_damage", b -> b.persistent(ExtraCodecs.POSITIVE_INT).networkSynchronized(ByteBufCodecs.VAR_INT));
/* 125 */     DAMAGE = register("damage", b -> b.persistent(ExtraCodecs.NON_NEGATIVE_INT).ignoreSwapAnimation().networkSynchronized(ByteBufCodecs.VAR_INT));
/* 128 */     UNBREAKABLE = register("unbreakable", b -> b.persistent(Unit.CODEC).networkSynchronized(Unit.STREAM_CODEC));
/* 131 */     USE_EFFECTS = register("use_effects", b -> b.persistent(UseEffects.CODEC).networkSynchronized(UseEffects.STREAM_CODEC));
/* 134 */     CUSTOM_NAME = register("custom_name", b -> b.persistent(ComponentSerialization.CODEC).networkSynchronized(ComponentSerialization.STREAM_CODEC).cacheEncoding());
/* 138 */     MINIMUM_ATTACK_CHARGE = register("minimum_attack_charge", b -> b.persistent(ExtraCodecs.floatRange(0.0F, 1.0F)).networkSynchronized(ByteBufCodecs.FLOAT));
/* 141 */     DAMAGE_TYPE = register("damage_type", b -> b.persistent(DamageType.CODEC).networkSynchronized(DamageType.STREAM_CODEC));
/* 145 */     ITEM_NAME = register("item_name", b -> b.persistent(ComponentSerialization.CODEC).networkSynchronized(ComponentSerialization.STREAM_CODEC).cacheEncoding());
/* 149 */     ITEM_MODEL = register("item_model", b -> b.persistent(Identifier.CODEC).networkSynchronized(Identifier.STREAM_CODEC).cacheEncoding());
/* 153 */     LORE = register("lore", b -> b.persistent(ItemLore.CODEC).networkSynchronized(ItemLore.STREAM_CODEC).cacheEncoding());
/* 157 */     RARITY = register("rarity", b -> b.persistent(Rarity.CODEC).networkSynchronized(Rarity.STREAM_CODEC));
/* 160 */     ENCHANTMENTS = register("enchantments", b -> b.persistent(ItemEnchantments.CODEC).networkSynchronized(ItemEnchantments.STREAM_CODEC).cacheEncoding());
/* 164 */     CAN_PLACE_ON = register("can_place_on", b -> b.persistent(AdventureModePredicate.CODEC).networkSynchronized(AdventureModePredicate.STREAM_CODEC).cacheEncoding());
/* 168 */     CAN_BREAK = register("can_break", b -> b.persistent(AdventureModePredicate.CODEC).networkSynchronized(AdventureModePredicate.STREAM_CODEC).cacheEncoding());
/* 172 */     ATTRIBUTE_MODIFIERS = register("attribute_modifiers", b -> b.persistent(ItemAttributeModifiers.CODEC).networkSynchronized(ItemAttributeModifiers.STREAM_CODEC).cacheEncoding());
/* 176 */     CUSTOM_MODEL_DATA = register("custom_model_data", b -> b.persistent(CustomModelData.CODEC).networkSynchronized(CustomModelData.STREAM_CODEC));
/* 179 */     TOOLTIP_DISPLAY = register("tooltip_display", b -> b.persistent(TooltipDisplay.CODEC).networkSynchronized(TooltipDisplay.STREAM_CODEC).cacheEncoding());
/* 183 */     REPAIR_COST = register("repair_cost", b -> b.persistent(ExtraCodecs.NON_NEGATIVE_INT).networkSynchronized(ByteBufCodecs.VAR_INT));
/* 186 */     CREATIVE_SLOT_LOCK = register("creative_slot_lock", b -> b.networkSynchronized(Unit.STREAM_CODEC));
/* 189 */     ENCHANTMENT_GLINT_OVERRIDE = register("enchantment_glint_override", b -> b.persistent((Codec)Codec.BOOL).networkSynchronized(ByteBufCodecs.BOOL));
/* 192 */     INTANGIBLE_PROJECTILE = register("intangible_projectile", b -> b.persistent(Unit.CODEC));
/* 194 */     FOOD = register("food", b -> b.persistent(FoodProperties.DIRECT_CODEC).networkSynchronized(FoodProperties.DIRECT_STREAM_CODEC).cacheEncoding());
/* 198 */     CONSUMABLE = register("consumable", b -> b.persistent(Consumable.CODEC).networkSynchronized(Consumable.STREAM_CODEC).cacheEncoding());
/* 202 */     USE_REMAINDER = register("use_remainder", b -> b.persistent(UseRemainder.CODEC).networkSynchronized(UseRemainder.STREAM_CODEC).cacheEncoding());
/* 206 */     USE_COOLDOWN = register("use_cooldown", b -> b.persistent(UseCooldown.CODEC).networkSynchronized(UseCooldown.STREAM_CODEC).cacheEncoding());
/* 210 */     DAMAGE_RESISTANT = register("damage_resistant", b -> b.persistent(DamageResistant.CODEC).networkSynchronized(DamageResistant.STREAM_CODEC).cacheEncoding());
/* 214 */     TOOL = register("tool", b -> b.persistent(Tool.CODEC).networkSynchronized(Tool.STREAM_CODEC).cacheEncoding());
/* 218 */     WEAPON = register("weapon", b -> b.persistent(Weapon.CODEC).networkSynchronized(Weapon.STREAM_CODEC).cacheEncoding());
/* 222 */     ATTACK_RANGE = register("attack_range", b -> b.persistent(AttackRange.CODEC).networkSynchronized(AttackRange.STREAM_CODEC).cacheEncoding());
/* 226 */     ENCHANTABLE = register("enchantable", b -> b.persistent(Enchantable.CODEC).networkSynchronized(Enchantable.STREAM_CODEC).cacheEncoding());
/* 230 */     EQUIPPABLE = register("equippable", b -> b.persistent(Equippable.CODEC).networkSynchronized(Equippable.STREAM_CODEC).cacheEncoding());
/* 234 */     REPAIRABLE = register("repairable", b -> b.persistent(Repairable.CODEC).networkSynchronized(Repairable.STREAM_CODEC).cacheEncoding());
/* 238 */     GLIDER = register("glider", b -> b.persistent(Unit.CODEC).networkSynchronized(Unit.STREAM_CODEC));
/* 241 */     TOOLTIP_STYLE = register("tooltip_style", b -> b.persistent(Identifier.CODEC).networkSynchronized(Identifier.STREAM_CODEC).cacheEncoding());
/* 245 */     DEATH_PROTECTION = register("death_protection", b -> b.persistent(DeathProtection.CODEC).networkSynchronized(DeathProtection.STREAM_CODEC).cacheEncoding());
/* 249 */     BLOCKS_ATTACKS = register("blocks_attacks", b -> b.persistent(BlocksAttacks.CODEC).networkSynchronized(BlocksAttacks.STREAM_CODEC).cacheEncoding());
/* 253 */     PIERCING_WEAPON = register("piercing_weapon", b -> b.persistent(PiercingWeapon.CODEC).networkSynchronized(PiercingWeapon.STREAM_CODEC).cacheEncoding());
/* 257 */     KINETIC_WEAPON = register("kinetic_weapon", b -> b.persistent(KineticWeapon.CODEC).networkSynchronized(KineticWeapon.STREAM_CODEC).cacheEncoding());
/* 261 */     SWING_ANIMATION = register("swing_animation", b -> b.persistent(SwingAnimation.CODEC).networkSynchronized(SwingAnimation.STREAM_CODEC));
/* 264 */     ADDITIONAL_TRADE_COST = register("additional_trade_cost", b -> b.networkSynchronized(ByteBufCodecs.VAR_INT));
/* 269 */     STORED_ENCHANTMENTS = register("stored_enchantments", b -> b.persistent(ItemEnchantments.CODEC).networkSynchronized(ItemEnchantments.STREAM_CODEC).cacheEncoding());
/* 273 */     DYE = register("dye", b -> b.persistent((Codec)DyeColor.CODEC).networkSynchronized(DyeColor.STREAM_CODEC));
/* 276 */     DYED_COLOR = register("dyed_color", b -> b.persistent(DyedItemColor.CODEC).networkSynchronized(DyedItemColor.STREAM_CODEC));
/* 279 */     MAP_COLOR = register("map_color", b -> b.persistent(MapItemColor.CODEC).networkSynchronized(MapItemColor.STREAM_CODEC));
/* 282 */     MAP_ID = register("map_id", b -> b.persistent(MapId.CODEC).networkSynchronized(MapId.STREAM_CODEC));
/* 285 */     MAP_DECORATIONS = register("map_decorations", b -> b.persistent(MapDecorations.CODEC).cacheEncoding());
/* 288 */     MAP_POST_PROCESSING = register("map_post_processing", b -> b.networkSynchronized(MapPostProcessing.STREAM_CODEC));
/* 290 */     CHARGED_PROJECTILES = register("charged_projectiles", b -> b.persistent(ChargedProjectiles.CODEC).networkSynchronized(ChargedProjectiles.STREAM_CODEC).cacheEncoding());
/* 294 */     BUNDLE_CONTENTS = register("bundle_contents", b -> b.persistent(BundleContents.CODEC).networkSynchronized(BundleContents.STREAM_CODEC).cacheEncoding());
/* 298 */     POTION_CONTENTS = register("potion_contents", b -> b.persistent(PotionContents.CODEC).networkSynchronized(PotionContents.STREAM_CODEC).cacheEncoding());
/* 302 */     POTION_DURATION_SCALE = register("potion_duration_scale", b -> b.persistent(ExtraCodecs.NON_NEGATIVE_FLOAT).networkSynchronized(ByteBufCodecs.FLOAT).cacheEncoding());
/* 306 */     SUSPICIOUS_STEW_EFFECTS = register("suspicious_stew_effects", b -> b.persistent(SuspiciousStewEffects.CODEC).networkSynchronized(SuspiciousStewEffects.STREAM_CODEC).cacheEncoding());
/* 310 */     WRITABLE_BOOK_CONTENT = register("writable_book_content", b -> b.persistent(WritableBookContent.CODEC).networkSynchronized(WritableBookContent.STREAM_CODEC).cacheEncoding());
/* 314 */     WRITTEN_BOOK_CONTENT = register("written_book_content", b -> b.persistent(WrittenBookContent.CODEC).networkSynchronized(WrittenBookContent.STREAM_CODEC).cacheEncoding());
/* 318 */     TRIM = register("trim", b -> b.persistent(ArmorTrim.CODEC).networkSynchronized(ArmorTrim.STREAM_CODEC).cacheEncoding());
/* 322 */     DEBUG_STICK_STATE = register("debug_stick_state", b -> b.persistent(DebugStickState.CODEC).cacheEncoding());
/* 325 */     ENTITY_DATA = register("entity_data", b -> b.persistent(TypedEntityData.codec(EntityType.CODEC)).networkSynchronized(TypedEntityData.streamCodec(EntityType.STREAM_CODEC)));
/* 328 */     BUCKET_ENTITY_DATA = register("bucket_entity_data", b -> b.persistent(CustomData.CODEC).networkSynchronized(CustomData.STREAM_CODEC));
/* 331 */     BLOCK_ENTITY_DATA = register("block_entity_data", b -> b.persistent(TypedEntityData.codec(BuiltInRegistries.BLOCK_ENTITY_TYPE.byNameCodec())).networkSynchronized(TypedEntityData.streamCodec(ByteBufCodecs.registry(Registries.BLOCK_ENTITY_TYPE))));
/* 334 */     INSTRUMENT = register("instrument", b -> b.persistent(InstrumentComponent.CODEC).networkSynchronized(InstrumentComponent.STREAM_CODEC).cacheEncoding());
/* 338 */     PROVIDES_TRIM_MATERIAL = register("provides_trim_material", b -> b.persistent(TrimMaterial.CODEC).networkSynchronized(TrimMaterial.STREAM_CODEC).cacheEncoding());
/* 342 */     OMINOUS_BOTTLE_AMPLIFIER = register("ominous_bottle_amplifier", b -> b.persistent(OminousBottleAmplifier.CODEC).networkSynchronized(OminousBottleAmplifier.STREAM_CODEC));
/* 345 */     JUKEBOX_PLAYABLE = register("jukebox_playable", b -> b.persistent(JukeboxPlayable.CODEC).networkSynchronized(JukeboxPlayable.STREAM_CODEC));
/* 348 */     PROVIDES_BANNER_PATTERNS = register("provides_banner_patterns", b -> b.persistent(RegistryCodecs.homogeneousList(Registries.BANNER_PATTERN)).networkSynchronized(ByteBufCodecs.holderSet(Registries.BANNER_PATTERN)).cacheEncoding());
/* 352 */     RECIPES = register("recipes", b -> b.persistent(Recipe.KEY_CODEC.listOf()).cacheEncoding());
/* 355 */     LODESTONE_TRACKER = register("lodestone_tracker", b -> b.persistent(LodestoneTracker.CODEC).networkSynchronized(LodestoneTracker.STREAM_CODEC).cacheEncoding());
/* 359 */     FIREWORK_EXPLOSION = register("firework_explosion", b -> b.persistent(FireworkExplosion.CODEC).networkSynchronized(FireworkExplosion.STREAM_CODEC).cacheEncoding());
/* 363 */     FIREWORKS = register("fireworks", b -> b.persistent(Fireworks.CODEC).networkSynchronized(Fireworks.STREAM_CODEC).cacheEncoding());
/* 367 */     PROFILE = register("profile", b -> b.persistent(ResolvableProfile.CODEC).networkSynchronized(ResolvableProfile.STREAM_CODEC).cacheEncoding());
/* 371 */     NOTE_BLOCK_SOUND = register("note_block_sound", b -> b.persistent(Identifier.CODEC).networkSynchronized(Identifier.STREAM_CODEC));
/* 374 */     BANNER_PATTERNS = register("banner_patterns", b -> b.persistent(BannerPatternLayers.CODEC).networkSynchronized(BannerPatternLayers.STREAM_CODEC).cacheEncoding());
/* 378 */     BASE_COLOR = register("base_color", b -> b.persistent((Codec)DyeColor.CODEC).networkSynchronized(DyeColor.STREAM_CODEC));
/* 381 */     POT_DECORATIONS = register("pot_decorations", b -> b.persistent(PotDecorations.CODEC).networkSynchronized(PotDecorations.STREAM_CODEC).cacheEncoding());
/* 385 */     CONTAINER = register("container", b -> b.persistent(ItemContainerContents.CODEC).networkSynchronized(ItemContainerContents.STREAM_CODEC).cacheEncoding());
/* 389 */     BLOCK_STATE = register("block_state", b -> b.persistent(BlockItemStateProperties.CODEC).networkSynchronized(BlockItemStateProperties.STREAM_CODEC).cacheEncoding());
/* 393 */     BEES = register("bees", b -> b.persistent(Bees.CODEC).networkSynchronized(Bees.STREAM_CODEC).cacheEncoding());
/* 397 */     SULFUR_CUBE_CONTENT = register("sulfur_cube_content", b -> b.persistent(SulfurCubeContent.CODEC).networkSynchronized(SulfurCubeContent.STREAM_CODEC).cacheEncoding());
/* 401 */     LOCK = register("lock", b -> b.persistent(LockCode.CODEC));
/* 403 */     CONTAINER_LOOT = register("container_loot", b -> b.persistent(SeededContainerLoot.CODEC));
/* 405 */     BREAK_SOUND = register("break_sound", b -> b.persistent(SoundEvent.CODEC).networkSynchronized(SoundEvent.STREAM_CODEC).cacheEncoding());
/* 410 */     VILLAGER_VARIANT = register("villager/variant", b -> b.persistent(VillagerType.CODEC).networkSynchronized(VillagerType.STREAM_CODEC));
/* 414 */     WOLF_VARIANT = register("wolf/variant", b -> b.persistent(WolfVariant.CODEC).networkSynchronized(WolfVariant.STREAM_CODEC));
/* 418 */     WOLF_SOUND_VARIANT = register("wolf/sound_variant", b -> b.persistent(WolfSoundVariant.CODEC).networkSynchronized(WolfSoundVariant.STREAM_CODEC));
/* 422 */     WOLF_COLLAR = register("wolf/collar", b -> b.persistent((Codec)DyeColor.CODEC).networkSynchronized(DyeColor.STREAM_CODEC));
/* 426 */     FOX_VARIANT = register("fox/variant", b -> b.persistent(Fox.Variant.CODEC).networkSynchronized(Fox.Variant.STREAM_CODEC));
/* 430 */     SALMON_SIZE = register("salmon/size", b -> b.persistent(Salmon.Variant.CODEC).networkSynchronized(Salmon.Variant.STREAM_CODEC));
/* 434 */     PARROT_VARIANT = register("parrot/variant", b -> b.persistent(Parrot.Variant.CODEC).networkSynchronized(Parrot.Variant.STREAM_CODEC));
/* 438 */     TROPICAL_FISH_PATTERN = register("tropical_fish/pattern", b -> b.persistent(TropicalFish.Pattern.CODEC).networkSynchronized(TropicalFish.Pattern.STREAM_CODEC));
/* 442 */     TROPICAL_FISH_BASE_COLOR = register("tropical_fish/base_color", b -> b.persistent((Codec)DyeColor.CODEC).networkSynchronized(DyeColor.STREAM_CODEC));
/* 446 */     TROPICAL_FISH_PATTERN_COLOR = register("tropical_fish/pattern_color", b -> b.persistent((Codec)DyeColor.CODEC).networkSynchronized(DyeColor.STREAM_CODEC));
/* 450 */     MOOSHROOM_VARIANT = register("mooshroom/variant", b -> b.persistent(MushroomCow.Variant.CODEC).networkSynchronized(MushroomCow.Variant.STREAM_CODEC));
/* 454 */     RABBIT_VARIANT = register("rabbit/variant", b -> b.persistent(Rabbit.Variant.CODEC).networkSynchronized(Rabbit.Variant.STREAM_CODEC));
/* 458 */     PIG_VARIANT = register("pig/variant", b -> b.persistent(PigVariant.CODEC).networkSynchronized(PigVariant.STREAM_CODEC));
/* 462 */     PIG_SOUND_VARIANT = register("pig/sound_variant", b -> b.persistent(PigSoundVariant.CODEC).networkSynchronized(PigSoundVariant.STREAM_CODEC));
/* 466 */     COW_VARIANT = register("cow/variant", b -> b.persistent(CowVariant.CODEC).networkSynchronized(CowVariant.STREAM_CODEC));
/* 470 */     COW_SOUND_VARIANT = register("cow/sound_variant", b -> b.persistent(CowSoundVariant.CODEC).networkSynchronized(CowSoundVariant.STREAM_CODEC));
/* 474 */     CHICKEN_VARIANT = register("chicken/variant", b -> b.persistent(ChickenVariant.CODEC).networkSynchronized(ChickenVariant.STREAM_CODEC));
/* 478 */     CHICKEN_SOUND_VARIANT = register("chicken/sound_variant", b -> b.persistent(ChickenSoundVariant.CODEC).networkSynchronized(ChickenSoundVariant.STREAM_CODEC));
/* 482 */     ZOMBIE_NAUTILUS_VARIANT = register("zombie_nautilus/variant", b -> b.persistent(ZombieNautilusVariant.CODEC).networkSynchronized(ZombieNautilusVariant.STREAM_CODEC));
/* 486 */     FROG_VARIANT = register("frog/variant", b -> b.persistent(FrogVariant.CODEC).networkSynchronized(FrogVariant.STREAM_CODEC));
/* 490 */     HORSE_VARIANT = register("horse/variant", b -> b.persistent(Variant.CODEC).networkSynchronized(Variant.STREAM_CODEC));
/* 494 */     PAINTING_VARIANT = register("painting/variant", b -> b.persistent(PaintingVariant.CODEC).networkSynchronized(PaintingVariant.STREAM_CODEC));
/* 498 */     LLAMA_VARIANT = register("llama/variant", b -> b.persistent(Llama.Variant.CODEC).networkSynchronized(Llama.Variant.STREAM_CODEC));
/* 502 */     AXOLOTL_VARIANT = register("axolotl/variant", b -> b.persistent(Axolotl.Variant.CODEC).networkSynchronized(Axolotl.Variant.STREAM_CODEC));
/* 506 */     CAT_VARIANT = register("cat/variant", b -> b.persistent(CatVariant.CODEC).networkSynchronized(CatVariant.STREAM_CODEC));
/* 510 */     CAT_SOUND_VARIANT = register("cat/sound_variant", b -> b.persistent(CatSoundVariant.CODEC).networkSynchronized(CatSoundVariant.STREAM_CODEC));
/* 514 */     CAT_COLLAR = register("cat/collar", b -> b.persistent((Codec)DyeColor.CODEC).networkSynchronized(DyeColor.STREAM_CODEC));
/* 518 */     SHEEP_COLOR = register("sheep/color", b -> b.persistent((Codec)DyeColor.CODEC).networkSynchronized(DyeColor.STREAM_CODEC));
/* 522 */     SHULKER_COLOR = register("shulker/color", b -> b.persistent((Codec)DyeColor.CODEC).networkSynchronized(DyeColor.STREAM_CODEC));
/*     */   }
/*     */   
/* 527 */   public static final DataComponentMap COMMON_ITEM_COMPONENTS = DataComponentMap.builder()
/* 528 */     .<Integer>set(MAX_STACK_SIZE, Integer.valueOf(64))
/* 529 */     .<ItemLore>set(LORE, ItemLore.EMPTY)
/* 530 */     .<ItemEnchantments>set(ENCHANTMENTS, ItemEnchantments.EMPTY)
/* 531 */     .<Integer>set(REPAIR_COST, Integer.valueOf(0))
/* 532 */     .<UseEffects>set(USE_EFFECTS, UseEffects.DEFAULT)
/* 533 */     .<ItemAttributeModifiers>set(ATTRIBUTE_MODIFIERS, ItemAttributeModifiers.EMPTY)
/* 534 */     .<Rarity>set(RARITY, Rarity.COMMON)
/* 535 */     .set(BREAK_SOUND, SoundEvents.ITEM_BREAK)
/* 536 */     .<TooltipDisplay>set(TOOLTIP_DISPLAY, TooltipDisplay.DEFAULT)
/* 537 */     .<SwingAnimation>set(SWING_ANIMATION, SwingAnimation.DEFAULT)
/* 538 */     .build();
/*     */   
/*     */   public static DataComponentType<?> bootstrap(Registry<DataComponentType<?>> registry) {
/* 541 */     return CUSTOM_DATA;
/*     */   }
/*     */   
/*     */   private static <T> DataComponentType<T> register(String id, UnaryOperator<DataComponentType.Builder<T>> builder) {
/* 545 */     return (DataComponentType<T>)Registry.register(BuiltInRegistries.DATA_COMPONENT_TYPE, id, ((DataComponentType.Builder)builder.apply(DataComponentType.builder())).build());
/*     */   }
/*     */ }


/* Location:              C:\Users\karol\AppData\Roaming\ModrinthApp\meta\versions\26.2-0.19.5\26.2-0.19.5.jar!\net\minecraft\core\component\DataComponents.class
 * Java compiler version: 25 (69.0)
 * JD-Core Version:       1.1.3
 */