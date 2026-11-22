/*     */ package net.minecraft.world.item.component;
/*     */ 
/*     */ import com.mojang.datafixers.kinds.App;
/*     */ import com.mojang.datafixers.kinds.Applicative;
/*     */ import com.mojang.datafixers.util.Function3;
/*     */ import com.mojang.datafixers.util.Function4;
/*     */ import com.mojang.datafixers.util.Function7;
/*     */ import com.mojang.serialization.Codec;
/*     */ import com.mojang.serialization.codecs.RecordCodecBuilder;
/*     */ import io.netty.buffer.ByteBuf;
/*     */ import java.util.List;
/*     */ import java.util.Optional;
/*     */ import net.minecraft.core.Holder;
/*     */ import net.minecraft.core.HolderSet;
/*     */ import net.minecraft.core.RegistryCodecs;
/*     */ import net.minecraft.core.registries.Registries;
/*     */ import net.minecraft.network.RegistryFriendlyByteBuf;
/*     */ import net.minecraft.network.codec.ByteBufCodecs;
/*     */ import net.minecraft.network.codec.StreamCodec;
/*     */ import net.minecraft.server.level.ServerLevel;
/*     */ import net.minecraft.sounds.SoundEvent;
/*     */ import net.minecraft.stats.Stats;
/*     */ import net.minecraft.util.ExtraCodecs;
/*     */ import net.minecraft.util.Mth;
/*     */ import net.minecraft.world.InteractionHand;
/*     */ import net.minecraft.world.damagesource.DamageSource;
/*     */ import net.minecraft.world.damagesource.DamageType;
/*     */ import net.minecraft.world.entity.LivingEntity;
/*     */ import net.minecraft.world.entity.player.Player;
/*     */ import net.minecraft.world.item.ItemStack;
/*     */ import net.minecraft.world.level.Level;
/*     */ 
/*     */ public final class BlocksAttacks extends Record {
/*     */   private final float blockDelaySeconds;
/*     */   
/*     */   private final float disableCooldownScale;
/*     */   
/*     */   private final List<DamageReduction> damageReductions;
/*     */   
/*     */   private final ItemDamageFunction itemDamage;
/*     */   
/*     */   private final Optional<HolderSet<DamageType>> bypassedBy;
/*     */   
/*     */   private final Optional<Holder<SoundEvent>> blockSound;
/*     */   
/*     */   private final Optional<Holder<SoundEvent>> disableSound;
/*     */   
/*     */   public static final Codec<BlocksAttacks> CODEC;
/*     */   
/*     */   public BlocksAttacks(float blockDelaySeconds, float disableCooldownScale, List<DamageReduction> damageReductions, ItemDamageFunction itemDamage, Optional<HolderSet<DamageType>> bypassedBy, Optional<Holder<SoundEvent>> blockSound, Optional<Holder<SoundEvent>> disableSound) {
/*  30 */     this.blockDelaySeconds = blockDelaySeconds;
/*  30 */     this.disableCooldownScale = disableCooldownScale;
/*  30 */     this.damageReductions = damageReductions;
/*  30 */     this.itemDamage = itemDamage;
/*  30 */     this.bypassedBy = bypassedBy;
/*  30 */     this.blockSound = blockSound;
/*  30 */     this.disableSound = disableSound;
/*     */   }
/*     */   
/*     */   public final String toString() {
/*     */     // Byte code:
/*     */     //   0: aload_0
/*     */     //   1: <illegal opcode> toString : (Lnet/minecraft/world/item/component/BlocksAttacks;)Ljava/lang/String;
/*     */     //   6: areturn
/*     */     // Line number table:
/*     */     //   Java source line number -> byte code offset
/*     */     //   #30	-> 0
/*     */     // Local variable table:
/*     */     //   start	length	slot	name	descriptor
/*     */     //   0	7	0	this	Lnet/minecraft/world/item/component/BlocksAttacks;
/*     */   }
/*     */   
/*     */   public final int hashCode() {
/*     */     // Byte code:
/*     */     //   0: aload_0
/*     */     //   1: <illegal opcode> hashCode : (Lnet/minecraft/world/item/component/BlocksAttacks;)I
/*     */     //   6: ireturn
/*     */     // Line number table:
/*     */     //   Java source line number -> byte code offset
/*     */     //   #30	-> 0
/*     */     // Local variable table:
/*     */     //   start	length	slot	name	descriptor
/*     */     //   0	7	0	this	Lnet/minecraft/world/item/component/BlocksAttacks;
/*     */   }
/*     */   
/*     */   public final boolean equals(Object o) {
/*     */     // Byte code:
/*     */     //   0: aload_0
/*     */     //   1: aload_1
/*     */     //   2: <illegal opcode> equals : (Lnet/minecraft/world/item/component/BlocksAttacks;Ljava/lang/Object;)Z
/*     */     //   7: ireturn
/*     */     // Line number table:
/*     */     //   Java source line number -> byte code offset
/*     */     //   #30	-> 0
/*     */     // Local variable table:
/*     */     //   start	length	slot	name	descriptor
/*     */     //   0	8	0	this	Lnet/minecraft/world/item/component/BlocksAttacks;
/*     */     //   0	8	1	o	Ljava/lang/Object;
/*     */   }
/*     */   
/*     */   public float blockDelaySeconds() {
/*  30 */     return this.blockDelaySeconds;
/*     */   }
/*     */   
/*     */   public float disableCooldownScale() {
/*  30 */     return this.disableCooldownScale;
/*     */   }
/*     */   
/*     */   public List<DamageReduction> damageReductions() {
/*  30 */     return this.damageReductions;
/*     */   }
/*     */   
/*     */   public ItemDamageFunction itemDamage() {
/*  30 */     return this.itemDamage;
/*     */   }
/*     */   
/*     */   public Optional<HolderSet<DamageType>> bypassedBy() {
/*  30 */     return this.bypassedBy;
/*     */   }
/*     */   
/*     */   public Optional<Holder<SoundEvent>> blockSound() {
/*  30 */     return this.blockSound;
/*     */   }
/*     */   
/*     */   public Optional<Holder<SoundEvent>> disableSound() {
/*  30 */     return this.disableSound;
/*     */   }
/*     */   
/*     */   static {
/*  39 */     CODEC = RecordCodecBuilder.create(i -> i.group((App)ExtraCodecs.NON_NEGATIVE_FLOAT.optionalFieldOf("block_delay_seconds", Float.valueOf(0.0F)).forGetter(BlocksAttacks::blockDelaySeconds), (App)ExtraCodecs.NON_NEGATIVE_FLOAT.optionalFieldOf("disable_cooldown_scale", Float.valueOf(1.0F)).forGetter(BlocksAttacks::disableCooldownScale), (App)DamageReduction.CODEC.listOf().optionalFieldOf("damage_reductions", List.of(new DamageReduction(90.0F, Optional.empty(), 0.0F, 1.0F))).forGetter(BlocksAttacks::damageReductions), (App)ItemDamageFunction.CODEC.optionalFieldOf("item_damage", ItemDamageFunction.DEFAULT).forGetter(BlocksAttacks::itemDamage), (App)RegistryCodecs.homogeneousList(Registries.DAMAGE_TYPE).optionalFieldOf("bypassed_by").forGetter(BlocksAttacks::bypassedBy), (App)SoundEvent.CODEC.optionalFieldOf("block_sound").forGetter(BlocksAttacks::blockSound), (App)SoundEvent.CODEC.optionalFieldOf("disabled_sound").forGetter(BlocksAttacks::disableSound)).apply((Applicative)i, BlocksAttacks::new));
/*     */   }
/*     */   
/*  48 */   public static final StreamCodec<RegistryFriendlyByteBuf, BlocksAttacks> STREAM_CODEC = StreamCodec.composite(ByteBufCodecs.FLOAT, BlocksAttacks::blockDelaySeconds, ByteBufCodecs.FLOAT, BlocksAttacks::disableCooldownScale, DamageReduction.STREAM_CODEC
/*     */       
/*  51 */       .apply(ByteBufCodecs.list()), BlocksAttacks::damageReductions, ItemDamageFunction.STREAM_CODEC, BlocksAttacks::itemDamage, 
/*     */       
/*  53 */       ByteBufCodecs.holderSet(Registries.DAMAGE_TYPE).apply(ByteBufCodecs::optional), BlocksAttacks::bypassedBy, SoundEvent.STREAM_CODEC
/*  54 */       .apply(ByteBufCodecs::optional), BlocksAttacks::blockSound, SoundEvent.STREAM_CODEC
/*  55 */       .apply(ByteBufCodecs::optional), BlocksAttacks::disableSound, BlocksAttacks::new);
/*     */   
/*     */   public void onBlocked(ServerLevel level, LivingEntity user) {
/*  60 */     this.blockSound.ifPresent(sound -> level.playSound(null, user.getX(), user.getY(), user.getZ(), sound, user.getSoundSource(), 1.0F, 0.8F + level.getRandom().nextFloat() * 0.4F));
/*     */   }
/*     */   
/*     */   public void disable(ServerLevel level, LivingEntity user, float baseSeconds, ItemStack blockingWith) {
/*  66 */     int cooldownTicks = disableBlockingForTicks(baseSeconds);
/*  67 */     if (cooldownTicks > 0) {
/*  68 */       if (user instanceof Player) {
/*  68 */         Player player = (Player)user;
/*  69 */         player.getCooldowns().addCooldown(blockingWith, cooldownTicks);
/*     */       } 
/*  71 */       user.stopUsingItem();
/*  72 */       this.disableSound.ifPresent(sound -> level.playSound(null, user.getX(), user.getY(), user.getZ(), sound, user.getSoundSource(), 0.8F, 0.8F + level.getRandom().nextFloat() * 0.4F));
/*     */     } 
/*     */   }
/*     */   
/*     */   public void hurtBlockingItem(Level level, ItemStack item, LivingEntity user, InteractionHand hand, float damage) {
/*     */     Player player;
/*  80 */     if (user instanceof Player) {
/*  80 */       player = (Player)user;
/*     */     } else {
/*     */       return;
/*     */     } 
/*  84 */     if (!level.isClientSide())
/*  85 */       player.awardStat(Stats.ITEM_USED.get(item.getItem())); 
/*  88 */     int itemDamage = this.itemDamage.apply(damage);
/*  89 */     if (itemDamage > 0)
/*  90 */       item.hurtAndBreak(itemDamage, user, hand.asEquipmentSlot()); 
/*     */   }
/*     */   
/*     */   private int disableBlockingForTicks(float baseSeconds) {
/*  95 */     float seconds = baseSeconds * this.disableCooldownScale;
/*  96 */     if (seconds > 0.0F)
/*  97 */       return Math.round(seconds * 20.0F); 
/*  99 */     return 0;
/*     */   }
/*     */   
/*     */   public int blockDelayTicks() {
/* 103 */     return Math.round(this.blockDelaySeconds * 20.0F);
/*     */   }
/*     */   
/*     */   public float resolveBlockedDamage(DamageSource source, float dealtDamage, double angle) {
/* 107 */     float blockedDamage = 0.0F;
/* 108 */     for (DamageReduction reduction : this.damageReductions)
/* 109 */       blockedDamage += reduction.resolve(source, dealtDamage, angle); 
/* 111 */     return Mth.clamp(blockedDamage, 0.0F, dealtDamage);
/*     */   }
/*     */   
/*     */   public static final class DamageReduction extends Record {
/*     */     private final float horizontalBlockingAngle;
/*     */     
/*     */     private final Optional<HolderSet<DamageType>> type;
/*     */     
/*     */     private final float base;
/*     */     
/*     */     private final float factor;
/*     */     
/*     */     public static final Codec<DamageReduction> CODEC;
/*     */     
/*     */     public DamageReduction(float horizontalBlockingAngle, Optional<HolderSet<DamageType>> type, float base, float factor) {
/* 114 */       this.horizontalBlockingAngle = horizontalBlockingAngle;
/* 114 */       this.type = type;
/* 114 */       this.base = base;
/* 114 */       this.factor = factor;
/*     */     }
/*     */     
/*     */     public final String toString() {
/*     */       // Byte code:
/*     */       //   0: aload_0
/*     */       //   1: <illegal opcode> toString : (Lnet/minecraft/world/item/component/BlocksAttacks$DamageReduction;)Ljava/lang/String;
/*     */       //   6: areturn
/*     */       // Line number table:
/*     */       //   Java source line number -> byte code offset
/*     */       //   #114	-> 0
/*     */       // Local variable table:
/*     */       //   start	length	slot	name	descriptor
/*     */       //   0	7	0	this	Lnet/minecraft/world/item/component/BlocksAttacks$DamageReduction;
/*     */     }
/*     */     
/*     */     public final int hashCode() {
/*     */       // Byte code:
/*     */       //   0: aload_0
/*     */       //   1: <illegal opcode> hashCode : (Lnet/minecraft/world/item/component/BlocksAttacks$DamageReduction;)I
/*     */       //   6: ireturn
/*     */       // Line number table:
/*     */       //   Java source line number -> byte code offset
/*     */       //   #114	-> 0
/*     */       // Local variable table:
/*     */       //   start	length	slot	name	descriptor
/*     */       //   0	7	0	this	Lnet/minecraft/world/item/component/BlocksAttacks$DamageReduction;
/*     */     }
/*     */     
/*     */     public final boolean equals(Object o) {
/*     */       // Byte code:
/*     */       //   0: aload_0
/*     */       //   1: aload_1
/*     */       //   2: <illegal opcode> equals : (Lnet/minecraft/world/item/component/BlocksAttacks$DamageReduction;Ljava/lang/Object;)Z
/*     */       //   7: ireturn
/*     */       // Line number table:
/*     */       //   Java source line number -> byte code offset
/*     */       //   #114	-> 0
/*     */       // Local variable table:
/*     */       //   start	length	slot	name	descriptor
/*     */       //   0	8	0	this	Lnet/minecraft/world/item/component/BlocksAttacks$DamageReduction;
/*     */       //   0	8	1	o	Ljava/lang/Object;
/*     */     }
/*     */     
/*     */     public float horizontalBlockingAngle() {
/* 114 */       return this.horizontalBlockingAngle;
/*     */     }
/*     */     
/*     */     public Optional<HolderSet<DamageType>> type() {
/* 114 */       return this.type;
/*     */     }
/*     */     
/*     */     public float base() {
/* 114 */       return this.base;
/*     */     }
/*     */     
/*     */     public float factor() {
/* 114 */       return this.factor;
/*     */     }
/*     */     
/*     */     static {
/* 121 */       CODEC = RecordCodecBuilder.create(i -> i.group((App)ExtraCodecs.POSITIVE_FLOAT.optionalFieldOf("horizontal_blocking_angle", Float.valueOf(90.0F)).forGetter(DamageReduction::horizontalBlockingAngle), (App)RegistryCodecs.homogeneousList(Registries.DAMAGE_TYPE).optionalFieldOf("type").forGetter(DamageReduction::type), (App)Codec.FLOAT.fieldOf("base").forGetter(DamageReduction::base), (App)Codec.FLOAT.fieldOf("factor").forGetter(DamageReduction::factor)).apply((Applicative)i, DamageReduction::new));
/*     */     }
/*     */     
/* 127 */     public static final StreamCodec<RegistryFriendlyByteBuf, DamageReduction> STREAM_CODEC = StreamCodec.composite(ByteBufCodecs.FLOAT, DamageReduction::horizontalBlockingAngle, 
/*     */         
/* 129 */         ByteBufCodecs.holderSet(Registries.DAMAGE_TYPE).apply(ByteBufCodecs::optional), DamageReduction::type, ByteBufCodecs.FLOAT, DamageReduction::base, ByteBufCodecs.FLOAT, DamageReduction::factor, DamageReduction::new);
/*     */     
/*     */     public float resolve(DamageSource source, float dealtDamage, double angle) {
/* 136 */       if (angle > (0.017453292F * this.horizontalBlockingAngle))
/* 137 */         return 0.0F; 
/* 140 */       if (this.type.isPresent() && !((HolderSet)this.type.get()).contains(source.typeHolder()))
/* 141 */         return 0.0F; 
/* 143 */       return Mth.clamp(this.base + this.factor * dealtDamage, 0.0F, dealtDamage);
/*     */     }
/*     */   }
/*     */   
/*     */   public static final class ItemDamageFunction extends Record {
/*     */     private final float threshold;
/*     */     
/*     */     private final float base;
/*     */     
/*     */     private final float factor;
/*     */     
/*     */     public static final Codec<ItemDamageFunction> CODEC;
/*     */     
/*     */     public ItemDamageFunction(float threshold, float base, float factor) {
/* 147 */       this.threshold = threshold;
/* 147 */       this.base = base;
/* 147 */       this.factor = factor;
/*     */     }
/*     */     
/*     */     public final String toString() {
/*     */       // Byte code:
/*     */       //   0: aload_0
/*     */       //   1: <illegal opcode> toString : (Lnet/minecraft/world/item/component/BlocksAttacks$ItemDamageFunction;)Ljava/lang/String;
/*     */       //   6: areturn
/*     */       // Line number table:
/*     */       //   Java source line number -> byte code offset
/*     */       //   #147	-> 0
/*     */       // Local variable table:
/*     */       //   start	length	slot	name	descriptor
/*     */       //   0	7	0	this	Lnet/minecraft/world/item/component/BlocksAttacks$ItemDamageFunction;
/*     */     }
/*     */     
/*     */     public final int hashCode() {
/*     */       // Byte code:
/*     */       //   0: aload_0
/*     */       //   1: <illegal opcode> hashCode : (Lnet/minecraft/world/item/component/BlocksAttacks$ItemDamageFunction;)I
/*     */       //   6: ireturn
/*     */       // Line number table:
/*     */       //   Java source line number -> byte code offset
/*     */       //   #147	-> 0
/*     */       // Local variable table:
/*     */       //   start	length	slot	name	descriptor
/*     */       //   0	7	0	this	Lnet/minecraft/world/item/component/BlocksAttacks$ItemDamageFunction;
/*     */     }
/*     */     
/*     */     public final boolean equals(Object o) {
/*     */       // Byte code:
/*     */       //   0: aload_0
/*     */       //   1: aload_1
/*     */       //   2: <illegal opcode> equals : (Lnet/minecraft/world/item/component/BlocksAttacks$ItemDamageFunction;Ljava/lang/Object;)Z
/*     */       //   7: ireturn
/*     */       // Line number table:
/*     */       //   Java source line number -> byte code offset
/*     */       //   #147	-> 0
/*     */       // Local variable table:
/*     */       //   start	length	slot	name	descriptor
/*     */       //   0	8	0	this	Lnet/minecraft/world/item/component/BlocksAttacks$ItemDamageFunction;
/*     */       //   0	8	1	o	Ljava/lang/Object;
/*     */     }
/*     */     
/*     */     public float threshold() {
/* 147 */       return this.threshold;
/*     */     }
/*     */     
/*     */     public float base() {
/* 147 */       return this.base;
/*     */     }
/*     */     
/*     */     public float factor() {
/* 147 */       return this.factor;
/*     */     }
/*     */     
/*     */     static {
/* 148 */       CODEC = RecordCodecBuilder.create(i -> i.group((App)ExtraCodecs.NON_NEGATIVE_FLOAT.fieldOf("threshold").forGetter(ItemDamageFunction::threshold), (App)Codec.FLOAT.fieldOf("base").forGetter(ItemDamageFunction::base), (App)Codec.FLOAT.fieldOf("factor").forGetter(ItemDamageFunction::factor)).apply((Applicative)i, ItemDamageFunction::new));
/*     */     }
/*     */     
/* 153 */     public static final StreamCodec<ByteBuf, ItemDamageFunction> STREAM_CODEC = StreamCodec.composite(ByteBufCodecs.FLOAT, ItemDamageFunction::threshold, ByteBufCodecs.FLOAT, ItemDamageFunction::base, ByteBufCodecs.FLOAT, ItemDamageFunction::factor, ItemDamageFunction::new);
/*     */     
/* 160 */     public static final ItemDamageFunction DEFAULT = new ItemDamageFunction(1.0F, 0.0F, 1.0F);
/*     */     
/*     */     public int apply(float dealtDamage) {
/* 163 */       if (dealtDamage < this.threshold)
/* 164 */         return 0; 
/* 166 */       return Mth.floor(this.base + this.factor * dealtDamage);
/*     */     }
/*     */   }
/*     */ }


/* Location:              C:\Users\karol\AppData\Roaming\ModrinthApp\meta\versions\26.2-0.19.5\26.2-0.19.5.jar!\net\minecraft\world\item\component\BlocksAttacks.class
 * Java compiler version: 25 (69.0)
 * JD-Core Version:       1.1.3
 */