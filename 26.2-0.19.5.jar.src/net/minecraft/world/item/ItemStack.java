/*      */ package net.minecraft.world.item;
/*      */ 
/*      */ import com.google.common.collect.Lists;
/*      */ import com.mojang.datafixers.kinds.App;
/*      */ import com.mojang.datafixers.kinds.Applicative;
/*      */ import com.mojang.datafixers.util.Function3;
/*      */ import com.mojang.logging.LogUtils;
/*      */ import com.mojang.serialization.Codec;
/*      */ import com.mojang.serialization.DataResult;
/*      */ import com.mojang.serialization.DynamicOps;
/*      */ import com.mojang.serialization.MapCodec;
/*      */ import com.mojang.serialization.codecs.RecordCodecBuilder;
/*      */ import io.netty.handler.codec.DecoderException;
/*      */ import io.netty.handler.codec.EncoderException;
/*      */ import java.util.Iterator;
/*      */ import java.util.List;
/*      */ import java.util.Objects;
/*      */ import java.util.Optional;
/*      */ import java.util.function.BiConsumer;
/*      */ import java.util.function.BiFunction;
/*      */ import java.util.function.Consumer;
/*      */ import java.util.function.Function;
/*      */ import java.util.function.Predicate;
/*      */ import java.util.function.UnaryOperator;
/*      */ import net.minecraft.ChatFormatting;
/*      */ import net.minecraft.advancements.triggers.CriteriaTriggers;
/*      */ import net.minecraft.core.BlockPos;
/*      */ import net.minecraft.core.Holder;
/*      */ import net.minecraft.core.NonNullList;
/*      */ import net.minecraft.core.component.DataComponentGetter;
/*      */ import net.minecraft.core.component.DataComponentHolder;
/*      */ import net.minecraft.core.component.DataComponentMap;
/*      */ import net.minecraft.core.component.DataComponentPatch;
/*      */ import net.minecraft.core.component.DataComponentType;
/*      */ import net.minecraft.core.component.DataComponents;
/*      */ import net.minecraft.core.component.PatchedDataComponentMap;
/*      */ import net.minecraft.core.component.TypedDataComponent;
/*      */ import net.minecraft.core.registries.BuiltInRegistries;
/*      */ import net.minecraft.network.RegistryFriendlyByteBuf;
/*      */ import net.minecraft.network.chat.CommonComponents;
/*      */ import net.minecraft.network.chat.Component;
/*      */ import net.minecraft.network.chat.ComponentUtils;
/*      */ import net.minecraft.network.chat.HoverEvent;
/*      */ import net.minecraft.network.chat.MutableComponent;
/*      */ import net.minecraft.network.chat.Style;
/*      */ import net.minecraft.network.codec.ByteBufCodecs;
/*      */ import net.minecraft.network.codec.StreamCodec;
/*      */ import net.minecraft.resources.RegistryOps;
/*      */ import net.minecraft.server.level.ServerLevel;
/*      */ import net.minecraft.server.level.ServerPlayer;
/*      */ import net.minecraft.stats.Stats;
/*      */ import net.minecraft.util.ExtraCodecs;
/*      */ import net.minecraft.util.Mth;
/*      */ import net.minecraft.util.NullOps;
/*      */ import net.minecraft.util.StringUtil;
/*      */ import net.minecraft.util.Unit;
/*      */ import net.minecraft.world.InteractionHand;
/*      */ import net.minecraft.world.InteractionResult;
/*      */ import net.minecraft.world.damagesource.DamageSource;
/*      */ import net.minecraft.world.entity.Entity;
/*      */ import net.minecraft.world.entity.EquipmentSlot;
/*      */ import net.minecraft.world.entity.EquipmentSlotGroup;
/*      */ import net.minecraft.world.entity.LivingEntity;
/*      */ import net.minecraft.world.entity.SlotAccess;
/*      */ import net.minecraft.world.entity.ai.attributes.Attribute;
/*      */ import net.minecraft.world.entity.ai.attributes.AttributeModifier;
/*      */ import net.minecraft.world.entity.item.ItemEntity;
/*      */ import net.minecraft.world.entity.player.Player;
/*      */ import net.minecraft.world.flag.FeatureFlagSet;
/*      */ import net.minecraft.world.inventory.ClickAction;
/*      */ import net.minecraft.world.inventory.Slot;
/*      */ import net.minecraft.world.inventory.tooltip.TooltipComponent;
/*      */ import net.minecraft.world.item.component.BundleContents;
/*      */ import net.minecraft.world.item.component.ChargedProjectiles;
/*      */ import net.minecraft.world.item.component.Consumable;
/*      */ import net.minecraft.world.item.component.DamageResistant;
/*      */ import net.minecraft.world.item.component.ItemAttributeModifiers;
/*      */ import net.minecraft.world.item.component.ItemContainerContents;
/*      */ import net.minecraft.world.item.component.KineticWeapon;
/*      */ import net.minecraft.world.item.component.SwingAnimation;
/*      */ import net.minecraft.world.item.component.TooltipDisplay;
/*      */ import net.minecraft.world.item.component.TooltipProvider;
/*      */ import net.minecraft.world.item.component.TypedEntityData;
/*      */ import net.minecraft.world.item.component.UseCooldown;
/*      */ import net.minecraft.world.item.component.UseEffects;
/*      */ import net.minecraft.world.item.component.UseRemainder;
/*      */ import net.minecraft.world.item.component.Weapon;
/*      */ import net.minecraft.world.item.component.WrittenBookContent;
/*      */ import net.minecraft.world.item.context.UseOnContext;
/*      */ import net.minecraft.world.item.enchantment.Enchantment;
/*      */ import net.minecraft.world.item.enchantment.EnchantmentHelper;
/*      */ import net.minecraft.world.item.enchantment.ItemEnchantments;
/*      */ import net.minecraft.world.item.enchantment.Repairable;
/*      */ import net.minecraft.world.item.equipment.Equippable;
/*      */ import net.minecraft.world.level.ItemLike;
/*      */ import net.minecraft.world.level.Level;
/*      */ import net.minecraft.world.level.LevelReader;
/*      */ import net.minecraft.world.level.Spawner;
/*      */ import net.minecraft.world.level.block.entity.BlockEntityType;
/*      */ import net.minecraft.world.level.block.state.BlockState;
/*      */ import net.minecraft.world.level.block.state.pattern.BlockInWorld;
/*      */ import net.minecraft.world.level.gameevent.GameEvent;
/*      */ import org.apache.commons.lang3.function.TriConsumer;
/*      */ import org.apache.commons.lang3.mutable.MutableBoolean;
/*      */ import org.slf4j.Logger;
/*      */ 
/*      */ public final class ItemStack implements DataComponentHolder, ItemInstance {
/*  104 */   private static final List<Component> OP_NBT_WARNING = (List)List.of(
/*  105 */       Component.translatable("item.op_warning.line1").withStyle(new ChatFormatting[] { ChatFormatting.RED, ChatFormatting.BOLD }, ), Component.translatable("item.op_warning.line2").withStyle(ChatFormatting.RED), 
/*  107 */       Component.translatable("item.op_warning.line3").withStyle(ChatFormatting.RED));
/*      */   
/*  109 */   private static final Component UNBREAKABLE_TOOLTIP = (Component)Component.translatable("item.unbreakable").withStyle(ChatFormatting.BLUE);
/*      */   
/*  110 */   private static final Component INTANGIBLE_TOOLTIP = (Component)Component.translatable("item.intangible").withStyle(ChatFormatting.GRAY);
/*      */   
/*  112 */   public static final MapCodec<ItemStack> MAP_CODEC = MapCodec.recursive("ItemStack", subCodec -> RecordCodecBuilder.mapCodec(()));
/*      */   
/*  119 */   public static final Codec<ItemStack> CODEC = Codec.lazyInitialized(MAP_CODEC::codec);
/*      */   
/*      */   public static final Codec<ItemStack> OPTIONAL_CODEC;
/*      */   
/*      */   static {
/*  119 */     Objects.requireNonNull(MAP_CODEC);
/*      */   }
/*      */   
/*      */   public static DataResult<ItemStack> validateStrict(ItemStack itemStack) {
/*  122 */     DataResult<?> result = validateComponents(itemStack.getComponents());
/*  123 */     if (result.isError())
/*  124 */       return result.map(unit -> itemStack); 
/*  126 */     if (itemStack.getCount() > itemStack.getMaxStackSize())
/*  127 */       return DataResult.error(() -> "Item stack with stack size of " + itemStack.getCount() + " was larger than maximum: " + itemStack.getMaxStackSize()); 
/*  129 */     return DataResult.success(itemStack);
/*      */   }
/*      */   
/*      */   static {
/*  132 */     OPTIONAL_CODEC = ExtraCodecs.optionalEmptyMap(CODEC).xmap(itemStack -> (ItemStack)itemStack.orElse(EMPTY), itemStack -> itemStack.isEmpty() ? Optional.empty() : Optional.<ItemStack>of(itemStack));
/*      */   }
/*      */   
/*  137 */   public static final StreamCodec<RegistryFriendlyByteBuf, ItemStack> OPTIONAL_STREAM_CODEC = createOptionalStreamCodec(DataComponentPatch.STREAM_CODEC);
/*      */   
/*  140 */   public static final StreamCodec<RegistryFriendlyByteBuf, ItemStack> OPTIONAL_UNTRUSTED_STREAM_CODEC = createOptionalStreamCodec(DataComponentPatch.DELIMITED_STREAM_CODEC);
/*      */   
/*      */   private static StreamCodec<RegistryFriendlyByteBuf, ItemStack> createOptionalStreamCodec(final StreamCodec<RegistryFriendlyByteBuf, DataComponentPatch> patchCodec) {
/*  143 */     return new StreamCodec<RegistryFriendlyByteBuf, ItemStack>() {
/*      */         public ItemStack decode(RegistryFriendlyByteBuf input) {
/*  146 */           int count = input.readVarInt();
/*  147 */           if (count <= 0)
/*  148 */             return ItemStack.EMPTY; 
/*  151 */           Holder<Item> item = (Holder<Item>)Item.STREAM_CODEC.decode(input);
/*  152 */           DataComponentPatch patch = (DataComponentPatch)patchCodec.decode(input);
/*  153 */           return new ItemStack(item, count, patch);
/*      */         }
/*      */         
/*      */         public void encode(RegistryFriendlyByteBuf output, ItemStack itemStack) {
/*  158 */           if (itemStack.isEmpty()) {
/*  159 */             output.writeVarInt(0);
/*      */             return;
/*      */           } 
/*  163 */           output.writeVarInt(itemStack.getCount());
/*  164 */           Item.STREAM_CODEC.encode(output, itemStack.typeHolder());
/*  165 */           patchCodec.encode(output, itemStack.components.asPatch());
/*      */         }
/*      */       };
/*      */   }
/*      */   
/*  170 */   public static final StreamCodec<RegistryFriendlyByteBuf, ItemStack> STREAM_CODEC = new StreamCodec<RegistryFriendlyByteBuf, ItemStack>() {
/*      */       public ItemStack decode(RegistryFriendlyByteBuf input) {
/*  173 */         ItemStack itemStack = (ItemStack)ItemStack.OPTIONAL_STREAM_CODEC.decode(input);
/*  174 */         if (itemStack.isEmpty())
/*  175 */           throw new DecoderException("Empty ItemStack not allowed"); 
/*  177 */         return itemStack;
/*      */       }
/*      */       
/*      */       public void encode(RegistryFriendlyByteBuf output, ItemStack itemStack) {
/*  182 */         if (itemStack.isEmpty())
/*  183 */           throw new EncoderException("Empty ItemStack not allowed"); 
/*  185 */         ItemStack.OPTIONAL_STREAM_CODEC.encode(output, itemStack);
/*      */       }
/*      */     };
/*      */   
/*  189 */   public static final StreamCodec<RegistryFriendlyByteBuf, List<ItemStack>> OPTIONAL_LIST_STREAM_CODEC = OPTIONAL_STREAM_CODEC.apply(ByteBufCodecs.collection(NonNullList::createWithCapacity));
/*      */   
/*      */   public static StreamCodec<RegistryFriendlyByteBuf, ItemStack> validatedStreamCodec(final StreamCodec<RegistryFriendlyByteBuf, ItemStack> codec) {
/*  196 */     return new StreamCodec<RegistryFriendlyByteBuf, ItemStack>() {
/*      */         public ItemStack decode(RegistryFriendlyByteBuf input) {
/*  199 */           ItemStack itemStack = (ItemStack)codec.decode(input);
/*  200 */           if (!itemStack.isEmpty()) {
/*  201 */             RegistryOps<Unit> ops = input.registryAccess().createSerializationContext((DynamicOps)NullOps.INSTANCE);
/*  202 */             ItemStack.CODEC.encodeStart((DynamicOps)ops, itemStack).getOrThrow(DecoderException::new);
/*      */           } 
/*  204 */           return itemStack;
/*      */         }
/*      */         
/*      */         public void encode(RegistryFriendlyByteBuf output, ItemStack value) {
/*  209 */           codec.encode(output, value);
/*      */         }
/*      */       };
/*      */   }
/*      */   
/*  214 */   private static final Logger LOGGER = LogUtils.getLogger();
/*      */   
/*  215 */   public static final ItemStack EMPTY = new ItemStack((Void)null);
/*      */   
/*  217 */   private static final Component DISABLED_ITEM_TOOLTIP = (Component)Component.translatable("item.disabled").withStyle(ChatFormatting.RED);
/*      */   
/*      */   private int count;
/*      */   
/*      */   private int popTime;
/*      */   
/*      */   @Deprecated
/*      */   private final Holder<Item> item;
/*      */   
/*      */   private final PatchedDataComponentMap components;
/*      */   
/*      */   public Optional<TooltipComponent> getTooltipImage() {
/*  220 */     return getItem().getTooltipImage(this);
/*      */   }
/*      */   
/*      */   public DataComponentMap getComponents() {
/*  225 */     return !isEmpty() ? (DataComponentMap)this.components : DataComponentMap.EMPTY;
/*      */   }
/*      */   
/*      */   public DataComponentMap getPrototype() {
/*  229 */     return !isEmpty() ? typeHolder().components() : DataComponentMap.EMPTY;
/*      */   }
/*      */   
/*      */   public DataComponentPatch getComponentsPatch() {
/*  233 */     return !isEmpty() ? this.components.asPatch() : DataComponentPatch.EMPTY;
/*      */   }
/*      */   
/*      */   public DataComponentMap immutableComponents() {
/*  237 */     return !isEmpty() ? this.components.toImmutableMap() : DataComponentMap.EMPTY;
/*      */   }
/*      */   
/*      */   public boolean hasNonDefault(DataComponentType<?> type) {
/*  241 */     return (!isEmpty() && this.components.hasNonDefault(type));
/*      */   }
/*      */   
/*      */   public ItemStack(ItemLike item, int count) {
/*  255 */     this((Holder<Item>)item.asItem().builtInRegistryHolder(), count);
/*      */   }
/*      */   
/*      */   public ItemStack(ItemLike item) {
/*  259 */     this((Holder<Item>)item.asItem().builtInRegistryHolder(), 1);
/*      */   }
/*      */   
/*      */   public ItemStack(Holder<Item> item, int count) {
/*  265 */     this(item, count, new PatchedDataComponentMap(item.components()));
/*      */   }
/*      */   
/*      */   public ItemStack(Holder<Item> item) {
/*  269 */     this(item, 1);
/*      */   }
/*      */   
/*      */   public ItemStack(Holder<Item> item, int count, DataComponentPatch components) {
/*  275 */     this(item, count, PatchedDataComponentMap.fromPatch(item.components(), components));
/*      */   }
/*      */   
/*      */   private ItemStack(Holder<Item> item, int count, PatchedDataComponentMap components) {
/*  279 */     this.item = item;
/*  280 */     this.count = count;
/*  281 */     this.components = components;
/*      */   }
/*      */   
/*      */   private ItemStack(Void nullMarker) {
/*  285 */     this.item = null;
/*  286 */     this.components = new PatchedDataComponentMap(DataComponentMap.EMPTY);
/*      */   }
/*      */   
/*      */   private static DataResult<?> validateComponents(DataComponentMap components) {
/*  290 */     if (components.has(DataComponents.MAX_DAMAGE) && ((Integer)components.getOrDefault(DataComponents.MAX_STACK_SIZE, Integer.valueOf(1))).intValue() > 1)
/*  291 */       return DataResult.error(() -> "Item cannot be both damageable and stackable"); 
/*  293 */     ItemContainerContents container = (ItemContainerContents)components.get(DataComponents.CONTAINER);
/*  294 */     if (container != null) {
/*  295 */       DataResult<?> validationContents = validateContainedItemSizes(container.nonEmptyItems());
/*  296 */       if (validationContents.isError())
/*  297 */         return validationContents; 
/*      */     } 
/*  300 */     BundleContents bundle = (BundleContents)components.get(DataComponents.BUNDLE_CONTENTS);
/*  301 */     if (bundle != null) {
/*  302 */       DataResult<?> validationResult = validateContainedItemSizes(bundle.items());
/*  303 */       if (validationResult.isError())
/*  304 */         return validationResult; 
/*  306 */       validationResult = bundle.weight();
/*  307 */       if (validationResult.isError())
/*  308 */         return validationResult; 
/*      */     } 
/*  311 */     ChargedProjectiles chargedProjectiles = (ChargedProjectiles)components.get(DataComponents.CHARGED_PROJECTILES);
/*  312 */     if (chargedProjectiles != null) {
/*  313 */       DataResult<?> validationResult = validateContainedItemSizes(chargedProjectiles.items());
/*  314 */       if (validationResult.isError())
/*  315 */         return validationResult; 
/*      */     } 
/*  318 */     return DataResult.success(Unit.INSTANCE);
/*      */   }
/*      */   
/*      */   private static DataResult<?> validateContainedItemSizes(Iterable<? extends ItemInstance> items) {
/*  322 */     for (Iterator<? extends ItemInstance> iterator = items.iterator(); iterator.hasNext(); ) {
/*  322 */       ItemInstance item = iterator.next();
/*  323 */       int itemCount = item.count();
/*  324 */       int maxStackSize = item.getMaxStackSize();
/*  325 */       if (itemCount > maxStackSize)
/*  326 */         return DataResult.error(() -> "Item stack with count of " + itemCount + " was larger than maximum: " + maxStackSize); 
/*      */     } 
/*  329 */     return DataResult.success(Unit.INSTANCE);
/*      */   }
/*      */   
/*      */   public boolean isEmpty() {
/*  333 */     return (this == EMPTY || this.item.value() == Items.AIR || this.count <= 0);
/*      */   }
/*      */   
/*      */   public boolean isItemEnabled(FeatureFlagSet enabledFeatures) {
/*  337 */     return (isEmpty() || getItem().isEnabled(enabledFeatures));
/*      */   }
/*      */   
/*      */   public ItemStack split(int amount) {
/*  343 */     int realAmount = Math.min(amount, getCount());
/*  345 */     ItemStack result = copyWithCount(realAmount);
/*  346 */     shrink(realAmount);
/*  348 */     return result;
/*      */   }
/*      */   
/*      */   public ItemStack copyAndClear() {
/*  352 */     if (isEmpty())
/*  353 */       return EMPTY; 
/*  355 */     ItemStack result = copy();
/*  356 */     setCount(0);
/*  357 */     return result;
/*      */   }
/*      */   
/*      */   public Item getItem() {
/*  361 */     return (Item)typeHolder().value();
/*      */   }
/*      */   
/*      */   public Holder<Item> typeHolder() {
/*  367 */     return isEmpty() ? (Holder<Item>)Items.AIR.builtInRegistryHolder() : this.item;
/*      */   }
/*      */   
/*      */   public boolean is(Predicate<Holder<Item>> item) {
/*  371 */     return item.test(typeHolder());
/*      */   }
/*      */   
/*      */   public InteractionResult useOn(UseOnContext context) {
/*  375 */     Player player = context.getPlayer();
/*  376 */     BlockPos pos = context.getClickedPos();
/*  377 */     if (player != null && !(player.getAbilities()).mayBuild && !canPlaceOnBlockInAdventureMode(new BlockInWorld((LevelReader)context.getLevel(), pos, false)))
/*  378 */       return (InteractionResult)InteractionResult.PASS; 
/*  381 */     Item usedItem = getItem();
/*  382 */     InteractionResult result = usedItem.useOn(context);
/*  384 */     if (player != null && result instanceof InteractionResult.Success) {
/*  384 */       InteractionResult.Success success = (InteractionResult.Success)result;
/*  384 */       if (success.wasItemInteraction())
/*  385 */         player.awardStat(Stats.ITEM_USED.get(usedItem)); 
/*      */     } 
/*  387 */     return result;
/*      */   }
/*      */   
/*      */   public float getDestroySpeed(BlockState state) {
/*  391 */     return getItem().getDestroySpeed(this, state);
/*      */   }
/*      */   
/*      */   public InteractionResult use(Level level, Player player, InteractionHand hand) {
/*  395 */     ItemStack stackBeforeUse = copy();
/*  396 */     boolean isInstantlyUsed = (getUseDuration((LivingEntity)player) <= 0);
/*  397 */     InteractionResult result = getItem().use(level, player, hand);
/*  399 */     if (isInstantlyUsed && result instanceof InteractionResult.Success) {
/*  399 */       InteractionResult.Success success = (InteractionResult.Success)result;
/*  400 */       return (InteractionResult)success.heldItemTransformedTo((success.heldItemTransformedTo() == null) ? 
/*  401 */           applyAfterUseComponentSideEffects((LivingEntity)player, stackBeforeUse) : 
/*  402 */           success.heldItemTransformedTo().applyAfterUseComponentSideEffects((LivingEntity)player, stackBeforeUse));
/*      */     } 
/*  405 */     return result;
/*      */   }
/*      */   
/*      */   public ItemStack finishUsingItem(Level level, LivingEntity livingEntity) {
/*  409 */     ItemStack stackBeforeUse = copy();
/*  410 */     ItemStack result = getItem().finishUsingItem(this, level, livingEntity);
/*  411 */     return result.applyAfterUseComponentSideEffects(livingEntity, stackBeforeUse);
/*      */   }
/*      */   
/*      */   private ItemStack applyAfterUseComponentSideEffects(LivingEntity user, ItemStack stackBeforeUsing) {
/*  416 */     UseRemainder useRemainder = (UseRemainder)stackBeforeUsing.get(DataComponents.USE_REMAINDER);
/*  417 */     UseCooldown useCooldown = (UseCooldown)stackBeforeUsing.get(DataComponents.USE_COOLDOWN);
/*  418 */     int stackCountBeforeUsing = stackBeforeUsing.getCount();
/*  420 */     ItemStack result = this;
/*  421 */     if (useRemainder != null) {
/*  422 */       Objects.requireNonNull(user);
/*  422 */       result = useRemainder.convertIntoRemainder(result, stackCountBeforeUsing, user.hasInfiniteMaterials(), user::handleExtraItemsCreatedOnUse);
/*      */     } 
/*  425 */     if (useCooldown != null)
/*  426 */       useCooldown.apply(stackBeforeUsing, user); 
/*  429 */     return result;
/*      */   }
/*      */   
/*      */   public boolean isStackable() {
/*  433 */     return (getMaxStackSize() > 1 && (!isDamageableItem() || !isDamaged()));
/*      */   }
/*      */   
/*      */   public boolean isDamageableItem() {
/*  437 */     return (has(DataComponents.MAX_DAMAGE) && !has(DataComponents.UNBREAKABLE) && has(DataComponents.DAMAGE));
/*      */   }
/*      */   
/*      */   public boolean isDamaged() {
/*  441 */     return (isDamageableItem() && getDamageValue() > 0);
/*      */   }
/*      */   
/*      */   public int getDamageValue() {
/*  445 */     return Mth.clamp(((Integer)getOrDefault(DataComponents.DAMAGE, Integer.valueOf(0))).intValue(), 0, getMaxDamage());
/*      */   }
/*      */   
/*      */   public void setDamageValue(int value) {
/*  449 */     set(DataComponents.DAMAGE, Integer.valueOf(Mth.clamp(value, 0, getMaxDamage())));
/*      */   }
/*      */   
/*      */   public int getMaxDamage() {
/*  453 */     return ((Integer)getOrDefault(DataComponents.MAX_DAMAGE, Integer.valueOf(0))).intValue();
/*      */   }
/*      */   
/*      */   public boolean isBroken() {
/*  457 */     return (isDamageableItem() && getDamageValue() >= getMaxDamage());
/*      */   }
/*      */   
/*      */   public boolean nextDamageWillBreak() {
/*  461 */     return (isDamageableItem() && getDamageValue() >= getMaxDamage() - 1);
/*      */   }
/*      */   
/*      */   public void hurtAndBreak(int amount, ServerLevel level, ServerPlayer player, Consumer<Item> onBreak) {
/*  465 */     int newAmount = processDurabilityChange(amount, level, player);
/*  466 */     if (newAmount != 0)
/*  467 */       applyDamage(getDamageValue() + newAmount, player, onBreak); 
/*      */   }
/*      */   
/*      */   private int processDurabilityChange(int amount, ServerLevel level, ServerPlayer player) {
/*  472 */     if (!isDamageableItem())
/*  473 */       return 0; 
/*  475 */     if (player != null && player.hasInfiniteMaterials())
/*  476 */       return 0; 
/*  478 */     if (amount > 0)
/*  479 */       return EnchantmentHelper.processDurabilityChange(level, this, amount); 
/*  481 */     return amount;
/*      */   }
/*      */   
/*      */   private void applyDamage(int newDamage, ServerPlayer player, Consumer<Item> onBreak) {
/*  485 */     if (player != null)
/*  486 */       CriteriaTriggers.ITEM_DURABILITY_CHANGED.trigger(player, this, newDamage); 
/*  489 */     setDamageValue(newDamage);
/*  491 */     if (isBroken()) {
/*  492 */       Item item = getItem();
/*  493 */       shrink(1);
/*  494 */       onBreak.accept(item);
/*      */     } 
/*      */   }
/*      */   
/*      */   public void hurtWithoutBreaking(int amount, Player player) {
/*  499 */     if (player instanceof ServerPlayer) {
/*  499 */       ServerPlayer serverPlayer = (ServerPlayer)player;
/*  500 */       int newAmount = processDurabilityChange(amount, serverPlayer.level(), serverPlayer);
/*  501 */       if (newAmount == 0)
/*      */         return; 
/*  504 */       int newDamage = Math.min(getDamageValue() + newAmount, getMaxDamage() - 1);
/*  505 */       applyDamage(newDamage, serverPlayer, i -> {
/*      */           
/*      */           });
/*      */     } 
/*      */   }
/*      */   
/*      */   public void hurtAndBreak(int amount, LivingEntity owner, InteractionHand hand) {
/*  510 */     hurtAndBreak(amount, owner, hand.asEquipmentSlot());
/*      */   }
/*      */   
/*      */   public void hurtAndBreak(int amount, LivingEntity owner, EquipmentSlot slot) {
/*  514 */     Level level = owner.level();
/*  514 */     if (level instanceof ServerLevel) {
/*  514 */       ServerLevel serverLevel = (ServerLevel)level;
/*  515 */       ServerPlayer player = (ServerPlayer)owner;
/*  515 */       hurtAndBreak(amount, serverLevel, (owner instanceof ServerPlayer) ? player : null, brokenItem -> owner.onEquippedItemBroken(brokenItem, slot));
/*      */     } 
/*      */   }
/*      */   
/*      */   public ItemStack hurtAndConvertOnBreak(int amount, ItemLike newItem, LivingEntity owner, EquipmentSlot slot) {
/*  520 */     hurtAndBreak(amount, owner, slot);
/*  521 */     if (isEmpty()) {
/*  522 */       ItemStack replacement = transmuteCopyIgnoreEmpty(newItem, 1);
/*  523 */       if (replacement.isDamageableItem())
/*  524 */         replacement.setDamageValue(0); 
/*  526 */       return replacement;
/*      */     } 
/*  528 */     return this;
/*      */   }
/*      */   
/*      */   public boolean isBarVisible() {
/*  532 */     return getItem().isBarVisible(this);
/*      */   }
/*      */   
/*      */   public int getBarWidth() {
/*  536 */     return getItem().getBarWidth(this);
/*      */   }
/*      */   
/*      */   public int getBarColor() {
/*  540 */     return getItem().getBarColor(this);
/*      */   }
/*      */   
/*      */   public boolean overrideStackedOnOther(Slot slot, ClickAction clickAction, Player player) {
/*  544 */     return getItem().overrideStackedOnOther(this, slot, clickAction, player);
/*      */   }
/*      */   
/*      */   public boolean overrideOtherStackedOnMe(ItemStack other, Slot slot, ClickAction clickAction, Player player, SlotAccess carriedItem) {
/*  548 */     return getItem().overrideOtherStackedOnMe(this, other, slot, clickAction, player, carriedItem);
/*      */   }
/*      */   
/*      */   public boolean hurtEnemy(LivingEntity mob, LivingEntity attacker) {
/*  552 */     Item usedItem = getItem();
/*  553 */     usedItem.hurtEnemy(this, mob, attacker);
/*  554 */     if (has(DataComponents.WEAPON)) {
/*  555 */       if (attacker instanceof Player) {
/*  555 */         Player player = (Player)attacker;
/*  556 */         player.awardStat(Stats.ITEM_USED.get(usedItem));
/*      */       } 
/*  558 */       return true;
/*      */     } 
/*  560 */     return false;
/*      */   }
/*      */   
/*      */   public void postHurtEnemy(LivingEntity mob, LivingEntity attacker) {
/*  564 */     getItem().postHurtEnemy(this, mob, attacker);
/*  565 */     Weapon weapon = (Weapon)get(DataComponents.WEAPON);
/*  566 */     if (weapon != null)
/*  567 */       hurtAndBreak(weapon.itemDamagePerAttack(), attacker, EquipmentSlot.MAINHAND); 
/*      */   }
/*      */   
/*      */   public void mineBlock(Level level, BlockState state, BlockPos pos, Player owner) {
/*  572 */     Item usedItem = getItem();
/*  573 */     if (usedItem.mineBlock(this, level, state, pos, (LivingEntity)owner))
/*  574 */       owner.awardStat(Stats.ITEM_USED.get(usedItem)); 
/*      */   }
/*      */   
/*      */   public boolean isCorrectToolForDrops(BlockState state) {
/*  579 */     return getItem().isCorrectToolForDrops(this, state);
/*      */   }
/*      */   
/*      */   public InteractionResult interactLivingEntity(Player player, LivingEntity target, InteractionHand hand) {
/*  583 */     Equippable equippable = (Equippable)get(DataComponents.EQUIPPABLE);
/*  584 */     if (equippable != null && equippable.equipOnInteract()) {
/*  585 */       InteractionResult result = equippable.equipOnTarget(player, target, this);
/*  586 */       if (result != InteractionResult.PASS)
/*  587 */         return result; 
/*      */     } 
/*  590 */     return getItem().interactLivingEntity(this, player, target, hand);
/*      */   }
/*      */   
/*      */   public ItemStack copy() {
/*  594 */     if (isEmpty())
/*  595 */       return EMPTY; 
/*  597 */     ItemStack copy = new ItemStack(typeHolder(), this.count, this.components.copy());
/*  598 */     copy.setPopTime(getPopTime());
/*  599 */     return copy;
/*      */   }
/*      */   
/*      */   public ItemStack copyWithCount(int count) {
/*  603 */     if (isEmpty())
/*  604 */       return EMPTY; 
/*  606 */     ItemStack copy = copy();
/*  607 */     copy.setCount(count);
/*  608 */     return copy;
/*      */   }
/*      */   
/*      */   public ItemStack transmuteCopy(ItemLike newItem) {
/*  612 */     return transmuteCopy(newItem, getCount());
/*      */   }
/*      */   
/*      */   public ItemStack transmuteCopy(ItemLike newItem, int newCount) {
/*  616 */     if (isEmpty())
/*  617 */       return EMPTY; 
/*  619 */     return transmuteCopyIgnoreEmpty(newItem, newCount);
/*      */   }
/*      */   
/*      */   private ItemStack transmuteCopyIgnoreEmpty(ItemLike newItem, int newCount) {
/*  623 */     return new ItemStack((Holder<Item>)newItem.asItem().builtInRegistryHolder(), newCount, this.components.asPatch());
/*      */   }
/*      */   
/*      */   public static boolean matches(ItemStack a, ItemStack b) {
/*  627 */     if (a == b)
/*  628 */       return true; 
/*  630 */     if (a.getCount() != b.getCount())
/*  631 */       return false; 
/*  633 */     return isSameItemSameComponents(a, b);
/*      */   }
/*      */   
/*      */   @Deprecated
/*      */   public static boolean listMatches(List<ItemStack> left, List<ItemStack> right) {
/*  639 */     if (left.size() != right.size())
/*  640 */       return false; 
/*  642 */     for (int i = 0; i < left.size(); i++) {
/*  643 */       if (!matches(left.get(i), right.get(i)))
/*  644 */         return false; 
/*      */     } 
/*  647 */     return true;
/*      */   }
/*      */   
/*      */   public static boolean isSameItem(ItemStack a, ItemStack b) {
/*  651 */     return a.is(b.getItem());
/*      */   }
/*      */   
/*      */   public static boolean isSameItemSameComponents(ItemStack a, ItemStack b) {
/*  655 */     if (!a.is(b.getItem()))
/*  656 */       return false; 
/*  658 */     if (a.isEmpty() && b.isEmpty())
/*  659 */       return true; 
/*  661 */     return Objects.equals(a.components, b.components);
/*      */   }
/*      */   
/*      */   public static boolean matchesIgnoringComponents(ItemStack a, ItemStack b, Predicate<DataComponentType<?>> ignoredPredicate) {
/*  665 */     if (a == b)
/*  666 */       return true; 
/*  668 */     if (a.getCount() != b.getCount())
/*  669 */       return false; 
/*  671 */     if (!a.is(b.getItem()))
/*  672 */       return false; 
/*  674 */     if (a.isEmpty() && b.isEmpty())
/*  675 */       return true; 
/*  677 */     if (a.components.size() != b.components.size())
/*  678 */       return false; 
/*  680 */     for (DataComponentType<?> type : (Iterable<DataComponentType<?>>)a.components.keySet()) {
/*  681 */       Object componentA = a.components.get(type);
/*  682 */       Object componentB = b.components.get(type);
/*  683 */       if (componentA == null || componentB == null)
/*  684 */         return false; 
/*  686 */       if (!Objects.equals(componentA, componentB) && !ignoredPredicate.test(type))
/*  687 */         return false; 
/*      */     } 
/*  690 */     return true;
/*      */   }
/*      */   
/*      */   public static MapCodec<ItemStack> lenientOptionalFieldOf(String name) {
/*  694 */     return CODEC.lenientOptionalFieldOf(name).xmap(itemStack -> (ItemStack)itemStack.orElse(EMPTY), itemStack -> itemStack.isEmpty() ? Optional.empty() : Optional.<ItemStack>of(itemStack));
/*      */   }
/*      */   
/*      */   public static int hashItemAndComponents(ItemStack item) {
/*  701 */     if (item != null) {
/*  702 */       int result = 31 + item.getItem().hashCode();
/*  703 */       return 31 * result + item.getComponents().hashCode();
/*      */     } 
/*  705 */     return 0;
/*      */   }
/*      */   
/*      */   @Deprecated
/*      */   public static int hashStackList(List<ItemStack> items) {
/*  711 */     int result = 0;
/*  712 */     for (ItemStack item : items)
/*  713 */       result = result * 31 + hashItemAndComponents(item); 
/*  715 */     return result;
/*      */   }
/*      */   
/*      */   public String toString() {
/*  720 */     return "" + getCount() + " " + getCount();
/*      */   }
/*      */   
/*      */   public void inventoryTick(Level level, Entity owner, EquipmentSlot slot) {
/*  724 */     if (this.popTime > 0)
/*  725 */       this.popTime--; 
/*  727 */     if (level instanceof ServerLevel) {
/*  727 */       ServerLevel serverLevel = (ServerLevel)level;
/*  728 */       getItem().inventoryTick(this, serverLevel, owner, slot);
/*      */     } 
/*      */   }
/*      */   
/*      */   public void onCraftedBy(Player player, int craftCount) {
/*  733 */     player.awardStat(Stats.ITEM_CRAFTED.get(getItem()), craftCount);
/*  734 */     getItem().onCraftedBy(this, player);
/*      */   }
/*      */   
/*      */   public void onCraftedBySystem(Level level) {
/*  738 */     getItem().onCraftedPostProcess(this, level);
/*      */   }
/*      */   
/*      */   public int getUseDuration(LivingEntity user) {
/*  742 */     return getItem().getUseDuration(this, user);
/*      */   }
/*      */   
/*      */   public ItemUseAnimation getUseAnimation() {
/*  746 */     return getItem().getUseAnimation(this);
/*      */   }
/*      */   
/*      */   public void releaseUsing(Level level, LivingEntity entity, int remainingTime) {
/*  750 */     ItemStack stackBeforeUsing = copy();
/*  751 */     if (getItem().releaseUsing(this, level, entity, remainingTime)) {
/*  752 */       ItemStack withSideEffects = applyAfterUseComponentSideEffects(entity, stackBeforeUsing);
/*  753 */       if (withSideEffects != this)
/*  754 */         entity.setItemInHand(entity.getUsedItemHand(), withSideEffects); 
/*      */     } 
/*      */   }
/*      */   
/*      */   public void causeUseVibration(Entity causer, Holder.Reference<GameEvent> event) {
/*  760 */     UseEffects useEffects = (UseEffects)get(DataComponents.USE_EFFECTS);
/*  761 */     if (useEffects != null && useEffects.interactVibrations())
/*  762 */       causer.gameEvent((Holder)event); 
/*      */   }
/*      */   
/*      */   public boolean useOnRelease() {
/*  767 */     return getItem().useOnRelease(this);
/*      */   }
/*      */   
/*      */   public <T> T set(DataComponentType<T> type, T value) {
/*  771 */     return (T)this.components.set(type, value);
/*      */   }
/*      */   
/*      */   public <T> T set(TypedDataComponent<T> value) {
/*  775 */     return (T)this.components.set(value);
/*      */   }
/*      */   
/*      */   public <T> void copyFrom(DataComponentType<T> type, DataComponentGetter source) {
/*  779 */     set(type, (T)source.get(type));
/*      */   }
/*      */   
/*      */   public <T, U> T update(DataComponentType<T> type, T defaultValue, U value, BiFunction<T, U, T> combiner) {
/*  783 */     return set(type, combiner.apply((T)getOrDefault(type, defaultValue), value));
/*      */   }
/*      */   
/*      */   public <T> T update(DataComponentType<T> type, T defaultValue, UnaryOperator<T> function) {
/*  787 */     T value = (T)getOrDefault(type, defaultValue);
/*  788 */     return set(type, function.apply(value));
/*      */   }
/*      */   
/*      */   public <T> T remove(DataComponentType<? extends T> type) {
/*  792 */     return (T)this.components.remove(type);
/*      */   }
/*      */   
/*      */   public void applyComponentsAndValidate(DataComponentPatch patch) {
/*  796 */     DataComponentPatch oldPatch = this.components.asPatch();
/*  797 */     this.components.applyPatch(patch);
/*  798 */     Optional<DataResult.Error<ItemStack>> validationError = validateStrict(this).error();
/*  799 */     if (validationError.isPresent()) {
/*  800 */       LOGGER.error("Failed to apply component patch '{}' to item: '{}'", patch, ((DataResult.Error)validationError.get()).message());
/*  801 */       this.components.restorePatch(oldPatch);
/*      */     } 
/*      */   }
/*      */   
/*      */   public void applyComponents(DataComponentPatch patch) {
/*  806 */     this.components.applyPatch(patch);
/*      */   }
/*      */   
/*      */   public void applyComponents(DataComponentMap components) {
/*  810 */     this.components.setAll(components);
/*      */   }
/*      */   
/*      */   public Component getHoverName() {
/*  814 */     Component customName = getCustomName();
/*  815 */     if (customName != null)
/*  816 */       return customName; 
/*  818 */     return getItemName();
/*      */   }
/*      */   
/*      */   public Component getCustomName() {
/*  822 */     Component customName = (Component)get(DataComponents.CUSTOM_NAME);
/*  823 */     if (customName != null)
/*  824 */       return customName; 
/*  826 */     WrittenBookContent content = (WrittenBookContent)get(DataComponents.WRITTEN_BOOK_CONTENT);
/*  827 */     if (content != null) {
/*  828 */       String title = (String)content.title().raw();
/*  829 */       if (!StringUtil.isBlank(title))
/*  830 */         return (Component)Component.literal(title); 
/*      */     } 
/*  833 */     return null;
/*      */   }
/*      */   
/*      */   public Component getItemName() {
/*  837 */     return getItem().getName(this);
/*      */   }
/*      */   
/*      */   public Component getStyledHoverName() {
/*  841 */     MutableComponent hoverName = Component.empty().append(getHoverName()).withStyle(getRarity().color());
/*  842 */     if (has(DataComponents.CUSTOM_NAME))
/*  843 */       hoverName.withStyle(ChatFormatting.ITALIC); 
/*  845 */     return (Component)hoverName;
/*      */   }
/*      */   
/*      */   public <T extends TooltipProvider> void addToTooltip(DataComponentType<T> type, Item.TooltipContext context, TooltipDisplay display, Consumer<Component> consumer, TooltipFlag flag) {
/*  849 */     TooltipProvider tooltipProvider = (TooltipProvider)get(type);
/*  850 */     if (tooltipProvider != null && display.shows(type))
/*  851 */       tooltipProvider.addToTooltip(context, consumer, flag, (DataComponentGetter)this.components); 
/*      */   }
/*      */   
/*      */   public List<Component> getTooltipLines(Item.TooltipContext context, Player player, TooltipFlag tooltipFlag) {
/*  856 */     TooltipDisplay display = (TooltipDisplay)getOrDefault(DataComponents.TOOLTIP_DISPLAY, TooltipDisplay.DEFAULT);
/*  857 */     if (!tooltipFlag.isCreative() && display.hideTooltip()) {
/*  858 */       boolean shouldPrintOpWarning = getItem().shouldPrintOpWarning(this, player);
/*  859 */       return shouldPrintOpWarning ? OP_NBT_WARNING : List.<Component>of();
/*      */     } 
/*  862 */     List<Component> lines = Lists.newArrayList();
/*  863 */     lines.add(getStyledHoverName());
/*  864 */     Objects.requireNonNull(lines);
/*  864 */     addDetailsToTooltip(context, display, player, tooltipFlag, lines::add);
/*  865 */     return lines;
/*      */   }
/*      */   
/*      */   public void addDetailsToTooltip(Item.TooltipContext context, TooltipDisplay display, Player player, TooltipFlag tooltipFlag, Consumer<Component> builder) {
/*  869 */     getItem().appendHoverText(this, context, display, builder, tooltipFlag);
/*  871 */     addToTooltip(DataComponents.TROPICAL_FISH_PATTERN, context, display, builder, tooltipFlag);
/*  872 */     addToTooltip(DataComponents.INSTRUMENT, context, display, builder, tooltipFlag);
/*  873 */     addToTooltip(DataComponents.MAP_ID, context, display, builder, tooltipFlag);
/*  874 */     addToTooltip(DataComponents.BEES, context, display, builder, tooltipFlag);
/*  875 */     addToTooltip(DataComponents.CONTAINER_LOOT, context, display, builder, tooltipFlag);
/*  876 */     addToTooltip(DataComponents.CONTAINER, context, display, builder, tooltipFlag);
/*  877 */     addToTooltip(DataComponents.BANNER_PATTERNS, context, display, builder, tooltipFlag);
/*  878 */     addToTooltip(DataComponents.POT_DECORATIONS, context, display, builder, tooltipFlag);
/*  879 */     addToTooltip(DataComponents.WRITTEN_BOOK_CONTENT, context, display, builder, tooltipFlag);
/*  880 */     addToTooltip(DataComponents.CHARGED_PROJECTILES, context, display, builder, tooltipFlag);
/*  881 */     addToTooltip(DataComponents.FIREWORKS, context, display, builder, tooltipFlag);
/*  882 */     addToTooltip(DataComponents.FIREWORK_EXPLOSION, context, display, builder, tooltipFlag);
/*  883 */     addToTooltip(DataComponents.POTION_CONTENTS, context, display, builder, tooltipFlag);
/*  884 */     addToTooltip(DataComponents.JUKEBOX_PLAYABLE, context, display, builder, tooltipFlag);
/*  885 */     addToTooltip(DataComponents.TRIM, context, display, builder, tooltipFlag);
/*  886 */     addToTooltip(DataComponents.STORED_ENCHANTMENTS, context, display, builder, tooltipFlag);
/*  887 */     addToTooltip(DataComponents.ENCHANTMENTS, context, display, builder, tooltipFlag);
/*  888 */     addToTooltip(DataComponents.DYED_COLOR, context, display, builder, tooltipFlag);
/*  889 */     addToTooltip(DataComponents.PROFILE, context, display, builder, tooltipFlag);
/*  890 */     addToTooltip(DataComponents.LORE, context, display, builder, tooltipFlag);
/*  891 */     addToTooltip(DataComponents.SULFUR_CUBE_CONTENT, context, display, builder, tooltipFlag);
/*  892 */     addAttributeTooltips(builder, display, player);
/*  894 */     addUnitComponentToTooltip(DataComponents.INTANGIBLE_PROJECTILE, INTANGIBLE_TOOLTIP, display, builder);
/*  895 */     addUnitComponentToTooltip(DataComponents.UNBREAKABLE, UNBREAKABLE_TOOLTIP, display, builder);
/*  897 */     addToTooltip(DataComponents.OMINOUS_BOTTLE_AMPLIFIER, context, display, builder, tooltipFlag);
/*  898 */     addToTooltip(DataComponents.SUSPICIOUS_STEW_EFFECTS, context, display, builder, tooltipFlag);
/*  899 */     addToTooltip(DataComponents.BLOCK_STATE, context, display, builder, tooltipFlag);
/*  900 */     addToTooltip(DataComponents.ENTITY_DATA, context, display, builder, tooltipFlag);
/*  903 */     if ((is(Items.SPAWNER) || is(Items.TRIAL_SPAWNER)) && display.shows(DataComponents.BLOCK_ENTITY_DATA)) {
/*  904 */       TypedEntityData<BlockEntityType<?>> blockEntityData = (TypedEntityData<BlockEntityType<?>>)get(DataComponents.BLOCK_ENTITY_DATA);
/*  905 */       Spawner.appendHoverText(blockEntityData, builder, "SpawnData");
/*      */     } 
/*  908 */     AdventureModePredicate canBreak = (AdventureModePredicate)get(DataComponents.CAN_BREAK);
/*  909 */     if (canBreak != null && display.shows(DataComponents.CAN_BREAK)) {
/*  910 */       builder.accept(CommonComponents.EMPTY);
/*  911 */       builder.accept(AdventureModePredicate.CAN_BREAK_HEADER);
/*  912 */       canBreak.addToTooltip(builder);
/*      */     } 
/*  914 */     AdventureModePredicate canPlaceOn = (AdventureModePredicate)get(DataComponents.CAN_PLACE_ON);
/*  915 */     if (canPlaceOn != null && display.shows(DataComponents.CAN_PLACE_ON)) {
/*  916 */       builder.accept(CommonComponents.EMPTY);
/*  917 */       builder.accept(AdventureModePredicate.CAN_PLACE_HEADER);
/*  918 */       canPlaceOn.addToTooltip(builder);
/*      */     } 
/*  921 */     if (tooltipFlag.isAdvanced()) {
/*  922 */       if (isDamaged() && display.shows(DataComponents.DAMAGE))
/*  923 */         builder.accept(Component.translatable("item.durability", new Object[] { Integer.valueOf(getMaxDamage() - getDamageValue()), Integer.valueOf(getMaxDamage()) })); 
/*  925 */       builder.accept(Component.literal(BuiltInRegistries.ITEM.getKey(getItem()).toString()).withStyle(ChatFormatting.DARK_GRAY));
/*  926 */       int count = this.components.size();
/*  927 */       if (count > 0)
/*  928 */         builder.accept(Component.translatable("item.components", new Object[] { Integer.valueOf(count) }).withStyle(ChatFormatting.DARK_GRAY)); 
/*      */     } 
/*  932 */     if (player != null && !getItem().isEnabled(player.level().enabledFeatures()))
/*  933 */       builder.accept(DISABLED_ITEM_TOOLTIP); 
/*  936 */     boolean shouldPrintOpWarning = getItem().shouldPrintOpWarning(this, player);
/*  937 */     if (shouldPrintOpWarning)
/*  938 */       OP_NBT_WARNING.forEach(builder); 
/*      */   }
/*      */   
/*      */   private void addUnitComponentToTooltip(DataComponentType<?> dataComponentType, Component component, TooltipDisplay display, Consumer<Component> builder) {
/*  943 */     if (has(dataComponentType) && display.shows(dataComponentType))
/*  944 */       builder.accept(component); 
/*      */   }
/*      */   
/*      */   private void addAttributeTooltips(Consumer<Component> consumer, TooltipDisplay display, Player player) {
/*  949 */     if (!display.shows(DataComponents.ATTRIBUTE_MODIFIERS))
/*      */       return; 
/*  952 */     for (EquipmentSlotGroup slot : EquipmentSlotGroup.values()) {
/*  953 */       MutableBoolean first = new MutableBoolean(true);
/*  954 */       forEachModifier(slot, (attribute, modifier, tooltip) -> {
/*      */             if (tooltip == ItemAttributeModifiers.Display.hidden())
/*      */               return; 
/*      */             if (first.isTrue()) {
/*      */               consumer.accept(CommonComponents.EMPTY);
/*      */               consumer.accept(Component.translatable("item.modifiers." + slot.getSerializedName()).withStyle(ChatFormatting.GRAY));
/*      */               first.setFalse();
/*      */             } 
/*      */             tooltip.apply(consumer, player, attribute, modifier);
/*      */           });
/*      */     } 
/*      */   }
/*      */   
/*      */   public boolean hasFoil() {
/*  970 */     Boolean enchantmentGlintOverride = (Boolean)get(DataComponents.ENCHANTMENT_GLINT_OVERRIDE);
/*  971 */     if (enchantmentGlintOverride != null)
/*  972 */       return enchantmentGlintOverride.booleanValue(); 
/*  974 */     return getItem().isFoil(this);
/*      */   }
/*      */   
/*      */   public Rarity getRarity() {
/*  978 */     Rarity baseRarity = (Rarity)getOrDefault(DataComponents.RARITY, Rarity.COMMON);
/*  979 */     if (!isEnchanted())
/*  980 */       return baseRarity; 
/*  982 */     switch (baseRarity) {
/*      */       case COMMON:
/*      */       case UNCOMMON:
/*      */       
/*      */       case RARE:
/*      */       
/*      */     } 
/*  982 */     return 
/*      */       
/*  985 */       baseRarity;
/*      */   }
/*      */   
/*      */   public boolean isEnchantable() {
/*  990 */     if (!has(DataComponents.ENCHANTABLE))
/*  991 */       return false; 
/*  993 */     ItemEnchantments enchantments = (ItemEnchantments)get(DataComponents.ENCHANTMENTS);
/*  994 */     return (enchantments != null && enchantments.isEmpty());
/*      */   }
/*      */   
/*      */   public void enchant(Holder<Enchantment> enchantment, int level) {
/*  998 */     EnchantmentHelper.updateEnchantments(this, enchantments -> enchantments.upgrade(enchantment, level));
/*      */   }
/*      */   
/*      */   public boolean isEnchanted() {
/* 1002 */     return !((ItemEnchantments)getOrDefault(DataComponents.ENCHANTMENTS, ItemEnchantments.EMPTY)).isEmpty();
/*      */   }
/*      */   
/*      */   public ItemEnchantments getEnchantments() {
/* 1006 */     return (ItemEnchantments)getOrDefault(DataComponents.ENCHANTMENTS, ItemEnchantments.EMPTY);
/*      */   }
/*      */   
/*      */   public void forEachModifier(EquipmentSlotGroup slot, TriConsumer<Holder<Attribute>, AttributeModifier, ItemAttributeModifiers.Display> consumer) {
/* 1010 */     ItemAttributeModifiers modifiers = (ItemAttributeModifiers)getOrDefault(DataComponents.ATTRIBUTE_MODIFIERS, ItemAttributeModifiers.EMPTY);
/* 1011 */     modifiers.forEach(slot, consumer);
/* 1012 */     EnchantmentHelper.forEachModifier(this, slot, (a, b) -> consumer.accept(a, b, ItemAttributeModifiers.Display.attributeModifiers()));
/*      */   }
/*      */   
/*      */   public void forEachModifier(EquipmentSlot slot, BiConsumer<Holder<Attribute>, AttributeModifier> consumer) {
/* 1016 */     ItemAttributeModifiers modifiers = (ItemAttributeModifiers)getOrDefault(DataComponents.ATTRIBUTE_MODIFIERS, ItemAttributeModifiers.EMPTY);
/* 1017 */     modifiers.forEach(slot, consumer);
/* 1018 */     EnchantmentHelper.forEachModifier(this, slot, consumer);
/*      */   }
/*      */   
/*      */   public Component getDisplayName() {
/* 1022 */     MutableComponent hoverName = Component.empty().append(getHoverName());
/* 1023 */     if (has(DataComponents.CUSTOM_NAME))
/* 1024 */       hoverName.withStyle(ChatFormatting.ITALIC); 
/* 1027 */     MutableComponent result = ComponentUtils.wrapInSquareBrackets((Component)hoverName);
/* 1029 */     if (!isEmpty())
/* 1030 */       result.withStyle(getRarity().color()).withStyle(s -> s.withHoverEvent((HoverEvent)new HoverEvent.ShowItem(ItemStackTemplate.fromNonEmptyStack(this)))); 
/* 1033 */     return (Component)result;
/*      */   }
/*      */   
/*      */   public SwingAnimation getSwingAnimation() {
/* 1037 */     return (SwingAnimation)getOrDefault(DataComponents.SWING_ANIMATION, SwingAnimation.DEFAULT);
/*      */   }
/*      */   
/*      */   public boolean canPlaceOnBlockInAdventureMode(BlockInWorld blockInWorld) {
/* 1041 */     AdventureModePredicate canPlaceOn = (AdventureModePredicate)get(DataComponents.CAN_PLACE_ON);
/* 1042 */     return (canPlaceOn != null && canPlaceOn.test(blockInWorld));
/*      */   }
/*      */   
/*      */   public boolean canBreakBlockInAdventureMode(BlockInWorld blockInWorld) {
/* 1046 */     AdventureModePredicate canBreak = (AdventureModePredicate)get(DataComponents.CAN_BREAK);
/* 1047 */     return (canBreak != null && canBreak.test(blockInWorld));
/*      */   }
/*      */   
/*      */   public int getPopTime() {
/* 1051 */     return this.popTime;
/*      */   }
/*      */   
/*      */   public void setPopTime(int popTime) {
/* 1055 */     this.popTime = popTime;
/*      */   }
/*      */   
/*      */   public int getCount() {
/* 1059 */     return isEmpty() ? 0 : this.count;
/*      */   }
/*      */   
/*      */   public int count() {
/* 1064 */     return getCount();
/*      */   }
/*      */   
/*      */   public void setCount(int count) {
/* 1068 */     this.count = count;
/*      */   }
/*      */   
/*      */   public void limitSize(int maxStackSize) {
/* 1072 */     if (!isEmpty() && getCount() > maxStackSize)
/* 1073 */       setCount(maxStackSize); 
/*      */   }
/*      */   
/*      */   public void grow(int amount) {
/* 1078 */     setCount(getCount() + amount);
/*      */   }
/*      */   
/*      */   public void shrink(int amount) {
/* 1082 */     grow(-amount);
/*      */   }
/*      */   
/*      */   public void consume(int amount, LivingEntity owner) {
/* 1086 */     if (owner == null || !owner.hasInfiniteMaterials())
/* 1087 */       shrink(amount); 
/*      */   }
/*      */   
/*      */   public ItemStack consumeAndReturn(int amount, LivingEntity owner) {
/* 1092 */     ItemStack split = copyWithCount(amount);
/* 1093 */     consume(amount, owner);
/* 1094 */     return split;
/*      */   }
/*      */   
/*      */   public void onUseTick(Level level, LivingEntity livingEntity, int ticksRemaining) {
/* 1098 */     Consumable consumable = (Consumable)get(DataComponents.CONSUMABLE);
/* 1099 */     if (consumable != null && consumable.shouldEmitParticlesAndSounds(ticksRemaining))
/* 1100 */       consumable.emitParticlesAndSounds(livingEntity.getRandom(), livingEntity, this, 5); 
/* 1102 */     KineticWeapon kineticWeapon = (KineticWeapon)get(DataComponents.KINETIC_WEAPON);
/* 1103 */     if (kineticWeapon != null && !level.isClientSide()) {
/* 1104 */       kineticWeapon.damageEntities(this, ticksRemaining, livingEntity, livingEntity.getUsedItemHand().asEquipmentSlot());
/*      */       return;
/*      */     } 
/* 1108 */     getItem().onUseTick(level, livingEntity, this, ticksRemaining);
/*      */   }
/*      */   
/*      */   public void onDestroyed(ItemEntity itemEntity) {
/* 1112 */     getItem().onDestroyed(itemEntity);
/*      */   }
/*      */   
/*      */   public boolean canBeHurtBy(DamageSource source) {
/* 1116 */     DamageResistant damageResistant = (DamageResistant)get(DataComponents.DAMAGE_RESISTANT);
/* 1117 */     return (damageResistant == null || !damageResistant.isResistantTo(source));
/*      */   }
/*      */   
/*      */   public boolean isValidRepairItem(ItemStack repairItem) {
/* 1121 */     Repairable repairable = (Repairable)get(DataComponents.REPAIRABLE);
/* 1122 */     return (repairable != null && repairable.isValidRepairItem(repairItem));
/*      */   }
/*      */   
/*      */   public boolean canDestroyBlock(BlockState state, Level level, BlockPos pos, Player player) {
/* 1126 */     return getItem().canDestroyBlock(this, state, level, pos, (LivingEntity)player);
/*      */   }
/*      */   
/*      */   public DamageSource getDamageSource(LivingEntity attacker) {
/* 1133 */     Objects.requireNonNull(attacker);
/* 1133 */     return Optional.<Holder>ofNullable((Holder)get(DataComponents.DAMAGE_TYPE)).map(type -> new DamageSource(type, (Entity)attacker)).or(() -> Optional.ofNullable(getItem().getItemDamageSource(attacker))).orElseGet(attacker::createDamageSource);
/*      */   }
/*      */ }


/* Location:              C:\Users\karol\AppData\Roaming\ModrinthApp\meta\versions\26.2-0.19.5\26.2-0.19.5.jar!\net\minecraft\world\item\ItemStack.class
 * Java compiler version: 25 (69.0)
 * JD-Core Version:       1.1.3
 */