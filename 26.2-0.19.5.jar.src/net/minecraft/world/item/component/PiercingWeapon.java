/*    */ package net.minecraft.world.item.component;
/*    */ 
/*    */ import com.mojang.datafixers.kinds.App;
/*    */ import com.mojang.datafixers.kinds.Applicative;
/*    */ import com.mojang.datafixers.util.Function4;
/*    */ import com.mojang.serialization.Codec;
/*    */ import com.mojang.serialization.codecs.RecordCodecBuilder;
/*    */ import java.util.Collection;
/*    */ import java.util.List;
/*    */ import java.util.Optional;
/*    */ import net.minecraft.core.Holder;
/*    */ import net.minecraft.network.RegistryFriendlyByteBuf;
/*    */ import net.minecraft.network.codec.ByteBufCodecs;
/*    */ import net.minecraft.network.codec.StreamCodec;
/*    */ import net.minecraft.sounds.SoundEvent;
/*    */ import net.minecraft.world.InteractionHand;
/*    */ import net.minecraft.world.entity.Entity;
/*    */ import net.minecraft.world.entity.EquipmentSlot;
/*    */ import net.minecraft.world.entity.LivingEntity;
/*    */ import net.minecraft.world.entity.ai.attributes.Attributes;
/*    */ import net.minecraft.world.entity.player.Player;
/*    */ import net.minecraft.world.entity.projectile.ProjectileUtil;
/*    */ import net.minecraft.world.item.ItemStack;
/*    */ import net.minecraft.world.level.ClipContext;
/*    */ import net.minecraft.world.phys.BlockHitResult;
/*    */ import net.minecraft.world.phys.EntityHitResult;
/*    */ 
/*    */ public final class PiercingWeapon extends Record {
/*    */   private final boolean dealsKnockback;
/*    */   
/*    */   private final boolean dismounts;
/*    */   
/*    */   private final Optional<Holder<SoundEvent>> sound;
/*    */   
/*    */   private final Optional<Holder<SoundEvent>> hitSound;
/*    */   
/*    */   public static final Codec<PiercingWeapon> CODEC;
/*    */   
/*    */   public PiercingWeapon(boolean dealsKnockback, boolean dismounts, Optional<Holder<SoundEvent>> sound, Optional<Holder<SoundEvent>> hitSound) {
/* 25 */     this.dealsKnockback = dealsKnockback;
/* 25 */     this.dismounts = dismounts;
/* 25 */     this.sound = sound;
/* 25 */     this.hitSound = hitSound;
/*    */   }
/*    */   
/*    */   public final String toString() {
/*    */     // Byte code:
/*    */     //   0: aload_0
/*    */     //   1: <illegal opcode> toString : (Lnet/minecraft/world/item/component/PiercingWeapon;)Ljava/lang/String;
/*    */     //   6: areturn
/*    */     // Line number table:
/*    */     //   Java source line number -> byte code offset
/*    */     //   #25	-> 0
/*    */     // Local variable table:
/*    */     //   start	length	slot	name	descriptor
/*    */     //   0	7	0	this	Lnet/minecraft/world/item/component/PiercingWeapon;
/*    */   }
/*    */   
/*    */   public final int hashCode() {
/*    */     // Byte code:
/*    */     //   0: aload_0
/*    */     //   1: <illegal opcode> hashCode : (Lnet/minecraft/world/item/component/PiercingWeapon;)I
/*    */     //   6: ireturn
/*    */     // Line number table:
/*    */     //   Java source line number -> byte code offset
/*    */     //   #25	-> 0
/*    */     // Local variable table:
/*    */     //   start	length	slot	name	descriptor
/*    */     //   0	7	0	this	Lnet/minecraft/world/item/component/PiercingWeapon;
/*    */   }
/*    */   
/*    */   public final boolean equals(Object o) {
/*    */     // Byte code:
/*    */     //   0: aload_0
/*    */     //   1: aload_1
/*    */     //   2: <illegal opcode> equals : (Lnet/minecraft/world/item/component/PiercingWeapon;Ljava/lang/Object;)Z
/*    */     //   7: ireturn
/*    */     // Line number table:
/*    */     //   Java source line number -> byte code offset
/*    */     //   #25	-> 0
/*    */     // Local variable table:
/*    */     //   start	length	slot	name	descriptor
/*    */     //   0	8	0	this	Lnet/minecraft/world/item/component/PiercingWeapon;
/*    */     //   0	8	1	o	Ljava/lang/Object;
/*    */   }
/*    */   
/*    */   public boolean dealsKnockback() {
/* 25 */     return this.dealsKnockback;
/*    */   }
/*    */   
/*    */   public boolean dismounts() {
/* 25 */     return this.dismounts;
/*    */   }
/*    */   
/*    */   public Optional<Holder<SoundEvent>> sound() {
/* 25 */     return this.sound;
/*    */   }
/*    */   
/*    */   public Optional<Holder<SoundEvent>> hitSound() {
/* 25 */     return this.hitSound;
/*    */   }
/*    */   
/*    */   static {
/* 31 */     CODEC = RecordCodecBuilder.create(i -> i.group((App)Codec.BOOL.optionalFieldOf("deals_knockback", Boolean.valueOf(true)).forGetter(PiercingWeapon::dealsKnockback), (App)Codec.BOOL.optionalFieldOf("dismounts", Boolean.valueOf(false)).forGetter(PiercingWeapon::dismounts), (App)SoundEvent.CODEC.optionalFieldOf("sound").forGetter(PiercingWeapon::sound), (App)SoundEvent.CODEC.optionalFieldOf("hit_sound").forGetter(PiercingWeapon::hitSound)).apply((Applicative)i, PiercingWeapon::new));
/*    */   }
/*    */   
/* 38 */   public static final StreamCodec<RegistryFriendlyByteBuf, PiercingWeapon> STREAM_CODEC = StreamCodec.composite(ByteBufCodecs.BOOL, PiercingWeapon::dealsKnockback, ByteBufCodecs.BOOL, PiercingWeapon::dismounts, SoundEvent.STREAM_CODEC
/*    */       
/* 41 */       .apply(ByteBufCodecs::optional), PiercingWeapon::sound, SoundEvent.STREAM_CODEC
/* 42 */       .apply(ByteBufCodecs::optional), PiercingWeapon::hitSound, PiercingWeapon::new);
/*    */   
/*    */   public void makeSound(Entity causer) {
/* 47 */     this.sound.ifPresent(s -> causer.level().playSound(causer, causer.getX(), causer.getY(), causer.getZ(), s, causer.getSoundSource(), 1.0F, 1.0F));
/*    */   }
/*    */   
/*    */   public void makeHitSound(Entity causer) {
/* 51 */     this.hitSound.ifPresent(s -> causer.level().playSound(null, causer.getX(), causer.getY(), causer.getZ(), s, causer.getSoundSource(), 1.0F, 1.0F));
/*    */   }
/*    */   
/*    */   public static boolean canHitEntity(Entity jabber, Entity target) {
/* 55 */     if (target.isInvulnerableToPiercingWeapon() || !target.isAlive())
/* 56 */       return false; 
/* 59 */     if (target instanceof net.minecraft.world.entity.Interaction)
/* 60 */       return true; 
/* 62 */     if (!target.canBeHitByProjectile())
/* 63 */       return false; 
/* 65 */     if (target instanceof Player) {
/* 65 */       Player targetPlayer = (Player)target;
/* 65 */       if (jabber instanceof Player) {
/* 65 */         Player jabbingPlayer = (Player)jabber;
/* 65 */         if (!jabbingPlayer.canHarmPlayer(targetPlayer))
/* 66 */           return false; 
/*    */       } 
/*    */     } 
/* 68 */     return !jabber.isPassengerOfSameVehicle(target);
/*    */   }
/*    */   
/*    */   public void attack(LivingEntity attacker, EquipmentSlot hand) {
/* 72 */     float damage = (float)attacker.getAttributeValue(Attributes.ATTACK_DAMAGE);
/* 74 */     ItemStack weaponItem = attacker.getItemBySlot(hand);
/* 75 */     AttackRange attackRange = attacker.getAttackRangeWith(weaponItem);
/* 77 */     boolean hitSomething = false;
/* 79 */     for (EntityHitResult hitResult : ProjectileUtil.getHitEntitiesAlong((Entity)attacker, attackRange, e1 -> canHitEntity((Entity)attacker, e1), ClipContext.Block.COLLIDER).map(a -> List.of(), e -> e))
/* 80 */       hitSomething |= attacker.stabAttack(hand, hitResult.getEntity(), damage, true, this.dealsKnockback, this.dismounts); 
/* 83 */     attacker.onAttack();
/* 84 */     attacker.postPiercingAttack();
/* 85 */     if (hitSomething)
/* 86 */       makeHitSound((Entity)attacker); 
/* 88 */     makeSound((Entity)attacker);
/* 90 */     attacker.swing(InteractionHand.MAIN_HAND, false);
/*    */   }
/*    */ }


/* Location:              C:\Users\karol\AppData\Roaming\ModrinthApp\meta\versions\26.2-0.19.5\26.2-0.19.5.jar!\net\minecraft\world\item\component\PiercingWeapon.class
 * Java compiler version: 25 (69.0)
 * JD-Core Version:       1.1.3
 */