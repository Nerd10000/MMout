/*     */ package net.minecraft.world.item.equipment;
/*     */ 
/*     */ import com.mojang.datafixers.kinds.App;
/*     */ import com.mojang.datafixers.kinds.Applicative;
/*     */ import com.mojang.datafixers.util.Function11;
/*     */ import com.mojang.serialization.Codec;
/*     */ import com.mojang.serialization.codecs.RecordCodecBuilder;
/*     */ import java.util.Optional;
/*     */ import net.minecraft.core.Holder;
/*     */ import net.minecraft.core.HolderGetter;
/*     */ import net.minecraft.core.HolderSet;
/*     */ import net.minecraft.core.Registry;
/*     */ import net.minecraft.core.RegistryCodecs;
/*     */ import net.minecraft.core.registries.BuiltInRegistries;
/*     */ import net.minecraft.core.registries.Registries;
/*     */ import net.minecraft.network.RegistryFriendlyByteBuf;
/*     */ import net.minecraft.network.codec.ByteBufCodecs;
/*     */ import net.minecraft.network.codec.StreamCodec;
/*     */ import net.minecraft.resources.Identifier;
/*     */ import net.minecraft.resources.ResourceKey;
/*     */ import net.minecraft.sounds.SoundEvent;
/*     */ import net.minecraft.sounds.SoundEvents;
/*     */ import net.minecraft.stats.Stats;
/*     */ import net.minecraft.tags.EntityTypeTags;
/*     */ import net.minecraft.world.InteractionResult;
/*     */ import net.minecraft.world.entity.EntityType;
/*     */ import net.minecraft.world.entity.EntityTypes;
/*     */ import net.minecraft.world.entity.EquipmentSlot;
/*     */ import net.minecraft.world.entity.LivingEntity;
/*     */ import net.minecraft.world.entity.Mob;
/*     */ import net.minecraft.world.entity.player.Player;
/*     */ import net.minecraft.world.item.DyeColor;
/*     */ import net.minecraft.world.item.ItemStack;
/*     */ import net.minecraft.world.item.enchantment.EnchantmentEffectComponents;
/*     */ import net.minecraft.world.item.enchantment.EnchantmentHelper;
/*     */ 
/*     */ public final class Equippable extends Record {
/*     */   private final EquipmentSlot slot;
/*     */   
/*     */   private final Holder<SoundEvent> equipSound;
/*     */   
/*     */   private final Optional<ResourceKey<EquipmentAsset>> assetId;
/*     */   
/*     */   private final Optional<Identifier> cameraOverlay;
/*     */   
/*     */   private final Optional<HolderSet<EntityType<?>>> allowedEntities;
/*     */   
/*     */   private final boolean dispensable;
/*     */   
/*     */   private final boolean swappable;
/*     */   
/*     */   private final boolean damageOnHurt;
/*     */   
/*     */   private final boolean equipOnInteract;
/*     */   
/*     */   private final boolean canBeSheared;
/*     */   
/*     */   private final Holder<SoundEvent> shearingSound;
/*     */   
/*     */   public static final Codec<Equippable> CODEC;
/*     */   
/*     */   public Equippable(EquipmentSlot slot, Holder<SoundEvent> equipSound, Optional<ResourceKey<EquipmentAsset>> assetId, Optional<Identifier> cameraOverlay, Optional<HolderSet<EntityType<?>>> allowedEntities, boolean dispensable, boolean swappable, boolean damageOnHurt, boolean equipOnInteract, boolean canBeSheared, Holder<SoundEvent> shearingSound) {
/*  34 */     this.slot = slot;
/*  34 */     this.equipSound = equipSound;
/*  34 */     this.assetId = assetId;
/*  34 */     this.cameraOverlay = cameraOverlay;
/*  34 */     this.allowedEntities = allowedEntities;
/*  34 */     this.dispensable = dispensable;
/*  34 */     this.swappable = swappable;
/*  34 */     this.damageOnHurt = damageOnHurt;
/*  34 */     this.equipOnInteract = equipOnInteract;
/*  34 */     this.canBeSheared = canBeSheared;
/*  34 */     this.shearingSound = shearingSound;
/*     */   }
/*     */   
/*     */   public final String toString() {
/*     */     // Byte code:
/*     */     //   0: aload_0
/*     */     //   1: <illegal opcode> toString : (Lnet/minecraft/world/item/equipment/Equippable;)Ljava/lang/String;
/*     */     //   6: areturn
/*     */     // Line number table:
/*     */     //   Java source line number -> byte code offset
/*     */     //   #34	-> 0
/*     */     // Local variable table:
/*     */     //   start	length	slot	name	descriptor
/*     */     //   0	7	0	this	Lnet/minecraft/world/item/equipment/Equippable;
/*     */   }
/*     */   
/*     */   public final int hashCode() {
/*     */     // Byte code:
/*     */     //   0: aload_0
/*     */     //   1: <illegal opcode> hashCode : (Lnet/minecraft/world/item/equipment/Equippable;)I
/*     */     //   6: ireturn
/*     */     // Line number table:
/*     */     //   Java source line number -> byte code offset
/*     */     //   #34	-> 0
/*     */     // Local variable table:
/*     */     //   start	length	slot	name	descriptor
/*     */     //   0	7	0	this	Lnet/minecraft/world/item/equipment/Equippable;
/*     */   }
/*     */   
/*     */   public final boolean equals(Object o) {
/*     */     // Byte code:
/*     */     //   0: aload_0
/*     */     //   1: aload_1
/*     */     //   2: <illegal opcode> equals : (Lnet/minecraft/world/item/equipment/Equippable;Ljava/lang/Object;)Z
/*     */     //   7: ireturn
/*     */     // Line number table:
/*     */     //   Java source line number -> byte code offset
/*     */     //   #34	-> 0
/*     */     // Local variable table:
/*     */     //   start	length	slot	name	descriptor
/*     */     //   0	8	0	this	Lnet/minecraft/world/item/equipment/Equippable;
/*     */     //   0	8	1	o	Ljava/lang/Object;
/*     */   }
/*     */   
/*     */   public EquipmentSlot slot() {
/*  34 */     return this.slot;
/*     */   }
/*     */   
/*     */   public Holder<SoundEvent> equipSound() {
/*  34 */     return this.equipSound;
/*     */   }
/*     */   
/*     */   public Optional<ResourceKey<EquipmentAsset>> assetId() {
/*  34 */     return this.assetId;
/*     */   }
/*     */   
/*     */   public Optional<Identifier> cameraOverlay() {
/*  34 */     return this.cameraOverlay;
/*     */   }
/*     */   
/*     */   public Optional<HolderSet<EntityType<?>>> allowedEntities() {
/*  34 */     return this.allowedEntities;
/*     */   }
/*     */   
/*     */   public boolean dispensable() {
/*  34 */     return this.dispensable;
/*     */   }
/*     */   
/*     */   public boolean swappable() {
/*  34 */     return this.swappable;
/*     */   }
/*     */   
/*     */   public boolean damageOnHurt() {
/*  34 */     return this.damageOnHurt;
/*     */   }
/*     */   
/*     */   public boolean equipOnInteract() {
/*  34 */     return this.equipOnInteract;
/*     */   }
/*     */   
/*     */   public boolean canBeSheared() {
/*  34 */     return this.canBeSheared;
/*     */   }
/*     */   
/*     */   public Holder<SoundEvent> shearingSound() {
/*  34 */     return this.shearingSound;
/*     */   }
/*     */   
/*     */   static {
/*  47 */     CODEC = RecordCodecBuilder.create(i -> i.group((App)EquipmentSlot.CODEC.fieldOf("slot").forGetter(Equippable::slot), (App)SoundEvent.CODEC.optionalFieldOf("equip_sound", SoundEvents.ARMOR_EQUIP_GENERIC).forGetter(Equippable::equipSound), (App)ResourceKey.codec(EquipmentAssets.ROOT_ID).optionalFieldOf("asset_id").forGetter(Equippable::assetId), (App)Identifier.CODEC.optionalFieldOf("camera_overlay").forGetter(Equippable::cameraOverlay), (App)RegistryCodecs.homogeneousList(Registries.ENTITY_TYPE).optionalFieldOf("allowed_entities").forGetter(Equippable::allowedEntities), (App)Codec.BOOL.optionalFieldOf("dispensable", Boolean.valueOf(true)).forGetter(Equippable::dispensable), (App)Codec.BOOL.optionalFieldOf("swappable", Boolean.valueOf(true)).forGetter(Equippable::swappable), (App)Codec.BOOL.optionalFieldOf("damage_on_hurt", Boolean.valueOf(true)).forGetter(Equippable::damageOnHurt), (App)Codec.BOOL.optionalFieldOf("equip_on_interact", Boolean.valueOf(false)).forGetter(Equippable::equipOnInteract), (App)Codec.BOOL.optionalFieldOf("can_be_sheared", Boolean.valueOf(false)).forGetter(Equippable::canBeSheared), (App)SoundEvent.CODEC.optionalFieldOf("shearing_sound", BuiltInRegistries.SOUND_EVENT.wrapAsHolder(SoundEvents.SHEARS_SNIP)).forGetter(Equippable::shearingSound)).apply((Applicative)i, Equippable::new));
/*     */   }
/*     */   
/*  61 */   public static final StreamCodec<RegistryFriendlyByteBuf, Equippable> STREAM_CODEC = StreamCodec.composite(EquipmentSlot.STREAM_CODEC, Equippable::slot, SoundEvent.STREAM_CODEC, Equippable::equipSound, 
/*     */       
/*  64 */       ResourceKey.streamCodec(EquipmentAssets.ROOT_ID).apply(ByteBufCodecs::optional), Equippable::assetId, Identifier.STREAM_CODEC
/*  65 */       .apply(ByteBufCodecs::optional), Equippable::cameraOverlay, 
/*  66 */       ByteBufCodecs.holderSet(Registries.ENTITY_TYPE).apply(ByteBufCodecs::optional), Equippable::allowedEntities, ByteBufCodecs.BOOL, Equippable::dispensable, ByteBufCodecs.BOOL, Equippable::swappable, ByteBufCodecs.BOOL, Equippable::damageOnHurt, ByteBufCodecs.BOOL, Equippable::equipOnInteract, ByteBufCodecs.BOOL, Equippable::canBeSheared, SoundEvent.STREAM_CODEC, Equippable::shearingSound, Equippable::new);
/*     */   
/*     */   public static Equippable llamaSwag(DyeColor color) {
/*  77 */     return builder(EquipmentSlot.BODY)
/*  78 */       .setEquipSound((Holder<SoundEvent>)SoundEvents.LLAMA_SWAG)
/*  79 */       .setAsset(EquipmentAssets.CARPETS.get(color))
/*  80 */       .setAllowedEntities((EntityType<?>[])new EntityType[] { EntityTypes.LLAMA, EntityTypes.TRADER_LLAMA }).setCanBeSheared(true)
/*  82 */       .setShearingSound((Holder<SoundEvent>)SoundEvents.LLAMA_CARPET_UNEQUIP)
/*  83 */       .build();
/*     */   }
/*     */   
/*     */   public static Equippable saddle() {
/*  87 */     HolderGetter<EntityType<?>> entityGetter = BuiltInRegistries.acquireBootstrapRegistrationLookup((Registry)BuiltInRegistries.ENTITY_TYPE);
/*  88 */     return builder(EquipmentSlot.SADDLE)
/*  89 */       .setEquipSound((Holder<SoundEvent>)SoundEvents.HORSE_SADDLE)
/*  90 */       .setAsset(EquipmentAssets.SADDLE)
/*  91 */       .setAllowedEntities((HolderSet<EntityType<?>>)entityGetter.getOrThrow(EntityTypeTags.CAN_EQUIP_SADDLE))
/*  92 */       .setEquipOnInteract(true)
/*  93 */       .setCanBeSheared(true)
/*  94 */       .setShearingSound((Holder<SoundEvent>)SoundEvents.SADDLE_UNEQUIP)
/*  95 */       .build();
/*     */   }
/*     */   
/*     */   public static Equippable harness(DyeColor color) {
/*  99 */     HolderGetter<EntityType<?>> entityGetter = BuiltInRegistries.acquireBootstrapRegistrationLookup((Registry)BuiltInRegistries.ENTITY_TYPE);
/* 100 */     return builder(EquipmentSlot.BODY)
/* 101 */       .setEquipSound((Holder<SoundEvent>)SoundEvents.HARNESS_EQUIP)
/* 102 */       .setAsset(EquipmentAssets.HARNESSES.get(color))
/* 103 */       .setAllowedEntities((HolderSet<EntityType<?>>)entityGetter.getOrThrow(EntityTypeTags.CAN_EQUIP_HARNESS))
/* 104 */       .setEquipOnInteract(true)
/* 105 */       .setCanBeSheared(true)
/* 106 */       .setShearingSound(BuiltInRegistries.SOUND_EVENT.wrapAsHolder(SoundEvents.HARNESS_UNEQUIP))
/* 107 */       .build();
/*     */   }
/*     */   
/*     */   public static Builder builder(EquipmentSlot slot) {
/* 111 */     return new Builder(slot);
/*     */   }
/*     */   
/*     */   public InteractionResult swapWithEquipmentSlot(ItemStack inHand, Player player) {
/* 115 */     if (!player.canUseSlot(this.slot) || !canBeEquippedBy(player.typeHolder()))
/* 116 */       return (InteractionResult)InteractionResult.PASS; 
/* 119 */     ItemStack inEquipmentSlot = player.getItemBySlot(this.slot);
/* 121 */     if ((EnchantmentHelper.has(inEquipmentSlot, EnchantmentEffectComponents.PREVENT_ARMOR_CHANGE) && !player.isCreative()) || ItemStack.isSameItemSameComponents(inHand, inEquipmentSlot))
/* 122 */       return (InteractionResult)InteractionResult.FAIL; 
/* 125 */     if (!player.level().isClientSide())
/* 126 */       player.awardStat(Stats.ITEM_USED.get(inHand.getItem())); 
/* 129 */     if (inHand.getCount() <= 1) {
/* 131 */       ItemStack swappedToHand = inEquipmentSlot.isEmpty() ? inHand : inEquipmentSlot.copyAndClear();
/* 132 */       ItemStack itemStack1 = player.isCreative() ? inHand.copy() : inHand.copyAndClear();
/* 133 */       player.setItemSlot(this.slot, itemStack1);
/* 134 */       return (InteractionResult)InteractionResult.SUCCESS.heldItemTransformedTo(swappedToHand);
/*     */     } 
/* 137 */     ItemStack swappedToInventory = inEquipmentSlot.copyAndClear();
/* 138 */     ItemStack swappedToEquipment = inHand.consumeAndReturn(1, (LivingEntity)player);
/* 139 */     player.setItemSlot(this.slot, swappedToEquipment);
/* 140 */     if (!player.getInventory().add(swappedToInventory))
/* 141 */       player.drop(swappedToInventory, false); 
/* 143 */     return (InteractionResult)InteractionResult.SUCCESS.heldItemTransformedTo(inHand);
/*     */   }
/*     */   
/*     */   public InteractionResult equipOnTarget(Player player, LivingEntity target, ItemStack itemStack) {
/* 148 */     if (!target.isEquippableInSlot(itemStack, this.slot) || target.hasItemInSlot(this.slot) || !target.isAlive())
/* 149 */       return (InteractionResult)InteractionResult.PASS; 
/* 151 */     if (!player.level().isClientSide()) {
/* 152 */       target.setItemSlot(this.slot, itemStack.split(1));
/* 153 */       if (target instanceof Mob) {
/* 153 */         Mob mob = (Mob)target;
/* 154 */         mob.setGuaranteedDrop(this.slot);
/*     */       } 
/*     */     } 
/* 157 */     return (InteractionResult)InteractionResult.SUCCESS;
/*     */   }
/*     */   
/*     */   public boolean canBeEquippedBy(Holder<EntityType<?>> type) {
/* 161 */     return (this.allowedEntities.isEmpty() || ((HolderSet)this.allowedEntities.get()).contains(type));
/*     */   }
/*     */   
/*     */   public static class Builder {
/*     */     private final EquipmentSlot slot;
/*     */     
/* 166 */     private Holder<SoundEvent> equipSound = SoundEvents.ARMOR_EQUIP_GENERIC;
/*     */     
/* 167 */     private Optional<ResourceKey<EquipmentAsset>> assetId = Optional.empty();
/*     */     
/* 168 */     private Optional<Identifier> cameraOverlay = Optional.empty();
/*     */     
/* 169 */     private Optional<HolderSet<EntityType<?>>> allowedEntities = Optional.empty();
/*     */     
/*     */     private boolean dispensable = true;
/*     */     
/*     */     private boolean swappable = true;
/*     */     
/*     */     private boolean damageOnHurt = true;
/*     */     
/*     */     private boolean equipOnInteract;
/*     */     
/*     */     private boolean canBeSheared;
/*     */     
/* 175 */     private Holder<SoundEvent> shearingSound = BuiltInRegistries.SOUND_EVENT.wrapAsHolder(SoundEvents.SHEARS_SNIP);
/*     */     
/*     */     private Builder(EquipmentSlot slot) {
/* 178 */       this.slot = slot;
/*     */     }
/*     */     
/*     */     public Builder setEquipSound(Holder<SoundEvent> equipSound) {
/* 182 */       this.equipSound = equipSound;
/* 183 */       return this;
/*     */     }
/*     */     
/*     */     public Builder setAsset(ResourceKey<EquipmentAsset> assetId) {
/* 187 */       this.assetId = Optional.of(assetId);
/* 188 */       return this;
/*     */     }
/*     */     
/*     */     public Builder setCameraOverlay(Identifier cameraOverlay) {
/* 192 */       this.cameraOverlay = Optional.of(cameraOverlay);
/* 193 */       return this;
/*     */     }
/*     */     
/*     */     public Builder setAllowedEntities(EntityType<?>... allowedEntities) {
/* 197 */       return setAllowedEntities((HolderSet<EntityType<?>>)HolderSet.direct(EntityType::builtInRegistryHolder, (Object[])allowedEntities));
/*     */     }
/*     */     
/*     */     public Builder setAllowedEntities(HolderSet<EntityType<?>> allowedEntities) {
/* 201 */       this.allowedEntities = Optional.of(allowedEntities);
/* 202 */       return this;
/*     */     }
/*     */     
/*     */     public Builder setDispensable(boolean dispensable) {
/* 206 */       this.dispensable = dispensable;
/* 207 */       return this;
/*     */     }
/*     */     
/*     */     public Builder setSwappable(boolean swappable) {
/* 211 */       this.swappable = swappable;
/* 212 */       return this;
/*     */     }
/*     */     
/*     */     public Builder setDamageOnHurt(boolean damageOnHurt) {
/* 216 */       this.damageOnHurt = damageOnHurt;
/* 217 */       return this;
/*     */     }
/*     */     
/*     */     public Builder setEquipOnInteract(boolean equipOnInteract) {
/* 221 */       this.equipOnInteract = equipOnInteract;
/* 222 */       return this;
/*     */     }
/*     */     
/*     */     public Builder setCanBeSheared(boolean canBeSheared) {
/* 226 */       this.canBeSheared = canBeSheared;
/* 227 */       return this;
/*     */     }
/*     */     
/*     */     public Builder setShearingSound(Holder<SoundEvent> shearingSound) {
/* 231 */       this.shearingSound = shearingSound;
/* 232 */       return this;
/*     */     }
/*     */     
/*     */     public Equippable build() {
/* 236 */       return new Equippable(this.slot, this.equipSound, this.assetId, this.cameraOverlay, this.allowedEntities, this.dispensable, this.swappable, this.damageOnHurt, this.equipOnInteract, this.canBeSheared, this.shearingSound);
/*     */     }
/*     */   }
/*     */ }


/* Location:              C:\Users\karol\AppData\Roaming\ModrinthApp\meta\versions\26.2-0.19.5\26.2-0.19.5.jar!\net\minecraft\world\item\equipment\Equippable.class
 * Java compiler version: 25 (69.0)
 * JD-Core Version:       1.1.3
 */