/*     */ package net.minecraft.world.item.component;
/*     */ 
/*     */ import com.mojang.datafixers.kinds.App;
/*     */ import com.mojang.datafixers.kinds.Applicative;
/*     */ import com.mojang.datafixers.util.Function3;
/*     */ import com.mojang.datafixers.util.Function9;
/*     */ import com.mojang.serialization.Codec;
/*     */ import com.mojang.serialization.codecs.RecordCodecBuilder;
/*     */ import io.netty.buffer.ByteBuf;
/*     */ import java.util.Collection;
/*     */ import java.util.List;
/*     */ import java.util.Optional;
/*     */ import net.minecraft.advancements.triggers.CriteriaTriggers;
/*     */ import net.minecraft.core.Holder;
/*     */ import net.minecraft.network.RegistryFriendlyByteBuf;
/*     */ import net.minecraft.network.codec.ByteBufCodecs;
/*     */ import net.minecraft.network.codec.StreamCodec;
/*     */ import net.minecraft.server.level.ServerPlayer;
/*     */ import net.minecraft.sounds.SoundEvent;
/*     */ import net.minecraft.util.ExtraCodecs;
/*     */ import net.minecraft.util.Mth;
/*     */ import net.minecraft.world.entity.Entity;
/*     */ import net.minecraft.world.entity.EquipmentSlot;
/*     */ import net.minecraft.world.entity.LivingEntity;
/*     */ import net.minecraft.world.entity.ai.attributes.Attributes;
/*     */ import net.minecraft.world.entity.boss.enderdragon.EnderDragon;
/*     */ import net.minecraft.world.entity.boss.enderdragon.EnderDragonPart;
/*     */ import net.minecraft.world.entity.projectile.ProjectileUtil;
/*     */ import net.minecraft.world.item.ItemStack;
/*     */ import net.minecraft.world.level.ClipContext;
/*     */ import net.minecraft.world.phys.BlockHitResult;
/*     */ import net.minecraft.world.phys.EntityHitResult;
/*     */ import net.minecraft.world.phys.Vec3;
/*     */ 
/*     */ public final class KineticWeapon extends Record {
/*     */   private final int contactCooldownTicks;
/*     */   
/*     */   private final int delayTicks;
/*     */   
/*     */   private final Optional<Condition> dismountConditions;
/*     */   
/*     */   private final Optional<Condition> knockbackConditions;
/*     */   
/*     */   private final Optional<Condition> damageConditions;
/*     */   
/*     */   private final float forwardMovement;
/*     */   
/*     */   private final float damageMultiplier;
/*     */   
/*     */   private final Optional<Holder<SoundEvent>> sound;
/*     */   
/*     */   private final Optional<Holder<SoundEvent>> hitSound;
/*     */   
/*     */   public static final int HIT_FEEDBACK_TICKS = 10;
/*     */   
/*     */   public static final Codec<KineticWeapon> CODEC;
/*     */   
/*     */   public KineticWeapon(int contactCooldownTicks, int delayTicks, Optional<Condition> dismountConditions, Optional<Condition> knockbackConditions, Optional<Condition> damageConditions, float forwardMovement, float damageMultiplier, Optional<Holder<SoundEvent>> sound, Optional<Holder<SoundEvent>> hitSound) {
/*  32 */     this.contactCooldownTicks = contactCooldownTicks;
/*  32 */     this.delayTicks = delayTicks;
/*  32 */     this.dismountConditions = dismountConditions;
/*  32 */     this.knockbackConditions = knockbackConditions;
/*  32 */     this.damageConditions = damageConditions;
/*  32 */     this.forwardMovement = forwardMovement;
/*  32 */     this.damageMultiplier = damageMultiplier;
/*  32 */     this.sound = sound;
/*  32 */     this.hitSound = hitSound;
/*     */   }
/*     */   
/*     */   public final String toString() {
/*     */     // Byte code:
/*     */     //   0: aload_0
/*     */     //   1: <illegal opcode> toString : (Lnet/minecraft/world/item/component/KineticWeapon;)Ljava/lang/String;
/*     */     //   6: areturn
/*     */     // Line number table:
/*     */     //   Java source line number -> byte code offset
/*     */     //   #32	-> 0
/*     */     // Local variable table:
/*     */     //   start	length	slot	name	descriptor
/*     */     //   0	7	0	this	Lnet/minecraft/world/item/component/KineticWeapon;
/*     */   }
/*     */   
/*     */   public final int hashCode() {
/*     */     // Byte code:
/*     */     //   0: aload_0
/*     */     //   1: <illegal opcode> hashCode : (Lnet/minecraft/world/item/component/KineticWeapon;)I
/*     */     //   6: ireturn
/*     */     // Line number table:
/*     */     //   Java source line number -> byte code offset
/*     */     //   #32	-> 0
/*     */     // Local variable table:
/*     */     //   start	length	slot	name	descriptor
/*     */     //   0	7	0	this	Lnet/minecraft/world/item/component/KineticWeapon;
/*     */   }
/*     */   
/*     */   public final boolean equals(Object o) {
/*     */     // Byte code:
/*     */     //   0: aload_0
/*     */     //   1: aload_1
/*     */     //   2: <illegal opcode> equals : (Lnet/minecraft/world/item/component/KineticWeapon;Ljava/lang/Object;)Z
/*     */     //   7: ireturn
/*     */     // Line number table:
/*     */     //   Java source line number -> byte code offset
/*     */     //   #32	-> 0
/*     */     // Local variable table:
/*     */     //   start	length	slot	name	descriptor
/*     */     //   0	8	0	this	Lnet/minecraft/world/item/component/KineticWeapon;
/*     */     //   0	8	1	o	Ljava/lang/Object;
/*     */   }
/*     */   
/*     */   public int contactCooldownTicks() {
/*  32 */     return this.contactCooldownTicks;
/*     */   }
/*     */   
/*     */   public int delayTicks() {
/*  32 */     return this.delayTicks;
/*     */   }
/*     */   
/*     */   public Optional<Condition> dismountConditions() {
/*  32 */     return this.dismountConditions;
/*     */   }
/*     */   
/*     */   public Optional<Condition> knockbackConditions() {
/*  32 */     return this.knockbackConditions;
/*     */   }
/*     */   
/*     */   public Optional<Condition> damageConditions() {
/*  32 */     return this.damageConditions;
/*     */   }
/*     */   
/*     */   public float forwardMovement() {
/*  32 */     return this.forwardMovement;
/*     */   }
/*     */   
/*     */   public float damageMultiplier() {
/*  32 */     return this.damageMultiplier;
/*     */   }
/*     */   
/*     */   public Optional<Holder<SoundEvent>> sound() {
/*  32 */     return this.sound;
/*     */   }
/*     */   
/*     */   public Optional<Holder<SoundEvent>> hitSound() {
/*  32 */     return this.hitSound;
/*     */   }
/*     */   
/*     */   static {
/*  45 */     CODEC = RecordCodecBuilder.create(i -> i.group((App)ExtraCodecs.NON_NEGATIVE_INT.optionalFieldOf("contact_cooldown_ticks", Integer.valueOf(10)).forGetter(KineticWeapon::contactCooldownTicks), (App)ExtraCodecs.NON_NEGATIVE_INT.optionalFieldOf("delay_ticks", Integer.valueOf(0)).forGetter(KineticWeapon::delayTicks), (App)Condition.CODEC.optionalFieldOf("dismount_conditions").forGetter(KineticWeapon::dismountConditions), (App)Condition.CODEC.optionalFieldOf("knockback_conditions").forGetter(KineticWeapon::knockbackConditions), (App)Condition.CODEC.optionalFieldOf("damage_conditions").forGetter(KineticWeapon::damageConditions), (App)Codec.FLOAT.optionalFieldOf("forward_movement", Float.valueOf(0.0F)).forGetter(KineticWeapon::forwardMovement), (App)Codec.FLOAT.optionalFieldOf("damage_multiplier", Float.valueOf(1.0F)).forGetter(KineticWeapon::damageMultiplier), (App)SoundEvent.CODEC.optionalFieldOf("sound").forGetter(KineticWeapon::sound), (App)SoundEvent.CODEC.optionalFieldOf("hit_sound").forGetter(KineticWeapon::hitSound)).apply((Applicative)i, KineticWeapon::new));
/*     */   }
/*     */   
/*  56 */   public static final StreamCodec<RegistryFriendlyByteBuf, KineticWeapon> STREAM_CODEC = StreamCodec.composite(ByteBufCodecs.VAR_INT, KineticWeapon::contactCooldownTicks, ByteBufCodecs.VAR_INT, KineticWeapon::delayTicks, Condition.STREAM_CODEC
/*     */       
/*  59 */       .apply(ByteBufCodecs::optional), KineticWeapon::dismountConditions, Condition.STREAM_CODEC
/*  60 */       .apply(ByteBufCodecs::optional), KineticWeapon::knockbackConditions, Condition.STREAM_CODEC
/*  61 */       .apply(ByteBufCodecs::optional), KineticWeapon::damageConditions, ByteBufCodecs.FLOAT, KineticWeapon::forwardMovement, ByteBufCodecs.FLOAT, KineticWeapon::damageMultiplier, SoundEvent.STREAM_CODEC
/*     */       
/*  64 */       .apply(ByteBufCodecs::optional), KineticWeapon::sound, SoundEvent.STREAM_CODEC
/*  65 */       .apply(ByteBufCodecs::optional), KineticWeapon::hitSound, KineticWeapon::new);
/*     */   
/*     */   public static Vec3 getMotion(Entity livingEntity) {
/*  70 */     if (!(livingEntity instanceof net.minecraft.world.entity.player.Player) && livingEntity.isPassenger())
/*  71 */       livingEntity = livingEntity.getRootVehicle(); 
/*  73 */     return livingEntity.getKnownSpeed().scale(20.0D);
/*     */   }
/*     */   
/*     */   public void makeSound(Entity causer) {
/*  77 */     this.sound.ifPresent(s -> causer.level().playSound(causer, causer.getX(), causer.getY(), causer.getZ(), s, causer.getSoundSource(), 1.0F, 1.0F));
/*     */   }
/*     */   
/*     */   public void makeLocalHitSound(Entity causer) {
/*  81 */     this.hitSound.ifPresent(s -> causer.level().playLocalSound(causer, (SoundEvent)s.value(), causer.getSoundSource(), 1.0F, 1.0F));
/*     */   }
/*     */   
/*     */   public int computeDamageUseDuration() {
/*  85 */     return this.delayTicks + ((Integer)this.damageConditions.<Integer>map(Condition::maxDurationTicks).orElse(Integer.valueOf(0))).intValue();
/*     */   }
/*     */   
/*     */   public void damageEntities(ItemStack stack, int ticksRemaining, LivingEntity livingEntity, EquipmentSlot equipmentSlot) {
/*  89 */     int ticksUsed = stack.getUseDuration(livingEntity) - ticksRemaining;
/*  90 */     if (ticksUsed < this.delayTicks)
/*     */       return; 
/*  93 */     ticksUsed -= this.delayTicks;
/*  95 */     Vec3 attackerLookVector = livingEntity.getLookAngle();
/*  96 */     double attackerSpeedProjection = attackerLookVector.dot(getMotion((Entity)livingEntity));
/*  97 */     float actionFactor = (livingEntity instanceof net.minecraft.world.entity.player.Player) ? 1.0F : 0.2F;
/*  98 */     AttackRange attackRange = livingEntity.getAttackRangeWith(stack);
/* 102 */     double baseMobDamage = livingEntity.getAttributeBaseValue(Attributes.ATTACK_DAMAGE);
/* 104 */     boolean affected = false;
/* 105 */     for (EntityHitResult hitResult : ProjectileUtil.getHitEntitiesAlong((Entity)livingEntity, attackRange, e -> PiercingWeapon.canHitEntity((Entity)livingEntity, e), ClipContext.Block.COLLIDER).map(a -> List.of(), e -> e)) {
/*     */       EnderDragon enderDragon;
/* 106 */       Entity otherEntity = hitResult.getEntity();
/* 107 */       if (otherEntity instanceof EnderDragonPart) {
/* 107 */         EnderDragonPart dragonPart = (EnderDragonPart)otherEntity;
/* 108 */         enderDragon = dragonPart.parentMob;
/*     */       } 
/* 110 */       boolean wasStabbed = livingEntity.wasRecentlyStabbed((Entity)enderDragon, this.contactCooldownTicks);
/* 111 */       if (wasStabbed)
/*     */         continue; 
/* 114 */       livingEntity.rememberStabbedEntity((Entity)enderDragon);
/* 115 */       double targetSpeedProjection = attackerLookVector.dot(getMotion((Entity)enderDragon));
/* 116 */       double relativeSpeed = Math.max(0.0D, attackerSpeedProjection - targetSpeedProjection);
/* 118 */       boolean dealsDismount = (this.dismountConditions.isPresent() && ((Condition)this.dismountConditions.get()).test(ticksUsed, attackerSpeedProjection, relativeSpeed, actionFactor));
/* 119 */       boolean dealsKnockback = (this.knockbackConditions.isPresent() && ((Condition)this.knockbackConditions.get()).test(ticksUsed, attackerSpeedProjection, relativeSpeed, actionFactor));
/* 120 */       boolean dealsDamage = (this.damageConditions.isPresent() && ((Condition)this.damageConditions.get()).test(ticksUsed, attackerSpeedProjection, relativeSpeed, actionFactor));
/* 122 */       if (!dealsDismount && !dealsKnockback && !dealsDamage)
/*     */         continue; 
/* 126 */       float damageDealt = (float)baseMobDamage + Mth.floor(relativeSpeed * this.damageMultiplier);
/* 128 */       affected |= livingEntity.stabAttack(equipmentSlot, (Entity)enderDragon, damageDealt, dealsDamage, dealsKnockback, dealsDismount);
/*     */     } 
/* 131 */     if (affected) {
/* 132 */       livingEntity.level().broadcastEntityEvent((Entity)livingEntity, (byte)2);
/* 133 */       if (livingEntity instanceof ServerPlayer) {
/* 133 */         ServerPlayer player = (ServerPlayer)livingEntity;
/* 134 */         CriteriaTriggers.SPEAR_MOBS_TRIGGER.trigger(player, livingEntity.stabbedEntities(e -> e instanceof LivingEntity));
/*     */       } 
/*     */     } 
/*     */   }
/*     */   
/*     */   public static final class Condition extends Record {
/*     */     private final int maxDurationTicks;
/*     */     
/*     */     private final float minSpeed;
/*     */     
/*     */     private final float minRelativeSpeed;
/*     */     
/*     */     public static final Codec<Condition> CODEC;
/*     */     
/*     */     public Condition(int maxDurationTicks, float minSpeed, float minRelativeSpeed) {
/* 139 */       this.maxDurationTicks = maxDurationTicks;
/* 139 */       this.minSpeed = minSpeed;
/* 139 */       this.minRelativeSpeed = minRelativeSpeed;
/*     */     }
/*     */     
/*     */     public final String toString() {
/*     */       // Byte code:
/*     */       //   0: aload_0
/*     */       //   1: <illegal opcode> toString : (Lnet/minecraft/world/item/component/KineticWeapon$Condition;)Ljava/lang/String;
/*     */       //   6: areturn
/*     */       // Line number table:
/*     */       //   Java source line number -> byte code offset
/*     */       //   #139	-> 0
/*     */       // Local variable table:
/*     */       //   start	length	slot	name	descriptor
/*     */       //   0	7	0	this	Lnet/minecraft/world/item/component/KineticWeapon$Condition;
/*     */     }
/*     */     
/*     */     public final int hashCode() {
/*     */       // Byte code:
/*     */       //   0: aload_0
/*     */       //   1: <illegal opcode> hashCode : (Lnet/minecraft/world/item/component/KineticWeapon$Condition;)I
/*     */       //   6: ireturn
/*     */       // Line number table:
/*     */       //   Java source line number -> byte code offset
/*     */       //   #139	-> 0
/*     */       // Local variable table:
/*     */       //   start	length	slot	name	descriptor
/*     */       //   0	7	0	this	Lnet/minecraft/world/item/component/KineticWeapon$Condition;
/*     */     }
/*     */     
/*     */     public final boolean equals(Object o) {
/*     */       // Byte code:
/*     */       //   0: aload_0
/*     */       //   1: aload_1
/*     */       //   2: <illegal opcode> equals : (Lnet/minecraft/world/item/component/KineticWeapon$Condition;Ljava/lang/Object;)Z
/*     */       //   7: ireturn
/*     */       // Line number table:
/*     */       //   Java source line number -> byte code offset
/*     */       //   #139	-> 0
/*     */       // Local variable table:
/*     */       //   start	length	slot	name	descriptor
/*     */       //   0	8	0	this	Lnet/minecraft/world/item/component/KineticWeapon$Condition;
/*     */       //   0	8	1	o	Ljava/lang/Object;
/*     */     }
/*     */     
/*     */     public int maxDurationTicks() {
/* 139 */       return this.maxDurationTicks;
/*     */     }
/*     */     
/*     */     public float minSpeed() {
/* 139 */       return this.minSpeed;
/*     */     }
/*     */     
/*     */     public float minRelativeSpeed() {
/* 139 */       return this.minRelativeSpeed;
/*     */     }
/*     */     
/*     */     static {
/* 144 */       CODEC = RecordCodecBuilder.create(i -> i.group((App)ExtraCodecs.NON_NEGATIVE_INT.fieldOf("max_duration_ticks").forGetter(Condition::maxDurationTicks), (App)Codec.FLOAT.optionalFieldOf("min_speed", Float.valueOf(0.0F)).forGetter(Condition::minSpeed), (App)Codec.FLOAT.optionalFieldOf("min_relative_speed", Float.valueOf(0.0F)).forGetter(Condition::minRelativeSpeed)).apply((Applicative)i, Condition::new));
/*     */     }
/*     */     
/* 150 */     public static final StreamCodec<ByteBuf, Condition> STREAM_CODEC = StreamCodec.composite(ByteBufCodecs.VAR_INT, Condition::maxDurationTicks, ByteBufCodecs.FLOAT, Condition::minSpeed, ByteBufCodecs.FLOAT, Condition::minRelativeSpeed, Condition::new);
/*     */     
/*     */     public boolean test(int ticksUsed, double attackerSpeed, double relativeSpeed, double entityFactor) {
/* 158 */       return (ticksUsed <= this.maxDurationTicks && attackerSpeed >= this.minSpeed * entityFactor && relativeSpeed >= this.minRelativeSpeed * entityFactor);
/*     */     }
/*     */     
/*     */     public static Optional<Condition> ofAttackerSpeed(int untilTicks, float minAttackerSpeed) {
/* 162 */       return Optional.of(new Condition(untilTicks, minAttackerSpeed, 0.0F));
/*     */     }
/*     */     
/*     */     public static Optional<Condition> ofRelativeSpeed(int untilTicks, float minRelativeSpeed) {
/* 166 */       return Optional.of(new Condition(untilTicks, 0.0F, minRelativeSpeed));
/*     */     }
/*     */   }
/*     */ }


/* Location:              C:\Users\karol\AppData\Roaming\ModrinthApp\meta\versions\26.2-0.19.5\26.2-0.19.5.jar!\net\minecraft\world\item\component\KineticWeapon.class
 * Java compiler version: 25 (69.0)
 * JD-Core Version:       1.1.3
 */