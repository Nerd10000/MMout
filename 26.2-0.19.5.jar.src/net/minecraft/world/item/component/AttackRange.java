/*     */ package net.minecraft.world.item.component;
/*     */ 
/*     */ import com.mojang.datafixers.kinds.App;
/*     */ import com.mojang.datafixers.kinds.Applicative;
/*     */ import com.mojang.datafixers.util.Either;
/*     */ import com.mojang.datafixers.util.Function6;
/*     */ import com.mojang.serialization.Codec;
/*     */ import com.mojang.serialization.codecs.RecordCodecBuilder;
/*     */ import io.netty.buffer.ByteBuf;
/*     */ import java.util.Collection;
/*     */ import java.util.Objects;
/*     */ import java.util.function.Predicate;
/*     */ import java.util.function.ToDoubleFunction;
/*     */ import net.minecraft.core.BlockPos;
/*     */ import net.minecraft.core.Direction;
/*     */ import net.minecraft.core.Position;
/*     */ import net.minecraft.network.codec.ByteBufCodecs;
/*     */ import net.minecraft.network.codec.StreamCodec;
/*     */ import net.minecraft.util.ExtraCodecs;
/*     */ import net.minecraft.world.entity.Entity;
/*     */ import net.minecraft.world.entity.LivingEntity;
/*     */ import net.minecraft.world.entity.ai.attributes.Attributes;
/*     */ import net.minecraft.world.entity.player.Player;
/*     */ import net.minecraft.world.entity.projectile.ProjectileUtil;
/*     */ import net.minecraft.world.level.ClipContext;
/*     */ import net.minecraft.world.phys.AABB;
/*     */ import net.minecraft.world.phys.BlockHitResult;
/*     */ import net.minecraft.world.phys.EntityHitResult;
/*     */ import net.minecraft.world.phys.HitResult;
/*     */ import net.minecraft.world.phys.Vec3;
/*     */ 
/*     */ public final class AttackRange extends Record {
/*     */   private final float minReach;
/*     */   
/*     */   private final float maxReach;
/*     */   
/*     */   private final float minCreativeReach;
/*     */   
/*     */   private final float maxCreativeReach;
/*     */   
/*     */   private final float hitboxMargin;
/*     */   
/*     */   private final float mobFactor;
/*     */   
/*     */   public static final Codec<AttackRange> CODEC;
/*     */   
/*     */   public AttackRange(float minReach, float maxReach, float minCreativeReach, float maxCreativeReach, float hitboxMargin, float mobFactor) {
/*  28 */     this.minReach = minReach;
/*  28 */     this.maxReach = maxReach;
/*  28 */     this.minCreativeReach = minCreativeReach;
/*  28 */     this.maxCreativeReach = maxCreativeReach;
/*  28 */     this.hitboxMargin = hitboxMargin;
/*  28 */     this.mobFactor = mobFactor;
/*     */   }
/*     */   
/*     */   public final String toString() {
/*     */     // Byte code:
/*     */     //   0: aload_0
/*     */     //   1: <illegal opcode> toString : (Lnet/minecraft/world/item/component/AttackRange;)Ljava/lang/String;
/*     */     //   6: areturn
/*     */     // Line number table:
/*     */     //   Java source line number -> byte code offset
/*     */     //   #28	-> 0
/*     */     // Local variable table:
/*     */     //   start	length	slot	name	descriptor
/*     */     //   0	7	0	this	Lnet/minecraft/world/item/component/AttackRange;
/*     */   }
/*     */   
/*     */   public final int hashCode() {
/*     */     // Byte code:
/*     */     //   0: aload_0
/*     */     //   1: <illegal opcode> hashCode : (Lnet/minecraft/world/item/component/AttackRange;)I
/*     */     //   6: ireturn
/*     */     // Line number table:
/*     */     //   Java source line number -> byte code offset
/*     */     //   #28	-> 0
/*     */     // Local variable table:
/*     */     //   start	length	slot	name	descriptor
/*     */     //   0	7	0	this	Lnet/minecraft/world/item/component/AttackRange;
/*     */   }
/*     */   
/*     */   public final boolean equals(Object o) {
/*     */     // Byte code:
/*     */     //   0: aload_0
/*     */     //   1: aload_1
/*     */     //   2: <illegal opcode> equals : (Lnet/minecraft/world/item/component/AttackRange;Ljava/lang/Object;)Z
/*     */     //   7: ireturn
/*     */     // Line number table:
/*     */     //   Java source line number -> byte code offset
/*     */     //   #28	-> 0
/*     */     // Local variable table:
/*     */     //   start	length	slot	name	descriptor
/*     */     //   0	8	0	this	Lnet/minecraft/world/item/component/AttackRange;
/*     */     //   0	8	1	o	Ljava/lang/Object;
/*     */   }
/*     */   
/*     */   public float minReach() {
/*  28 */     return this.minReach;
/*     */   }
/*     */   
/*     */   public float maxReach() {
/*  28 */     return this.maxReach;
/*     */   }
/*     */   
/*     */   public float minCreativeReach() {
/*  28 */     return this.minCreativeReach;
/*     */   }
/*     */   
/*     */   public float maxCreativeReach() {
/*  28 */     return this.maxCreativeReach;
/*     */   }
/*     */   
/*     */   public float hitboxMargin() {
/*  28 */     return this.hitboxMargin;
/*     */   }
/*     */   
/*     */   public float mobFactor() {
/*  28 */     return this.mobFactor;
/*     */   }
/*     */   
/*     */   static {
/*  36 */     CODEC = RecordCodecBuilder.create(i -> i.group((App)ExtraCodecs.floatRange(0.0F, 64.0F).optionalFieldOf("min_reach", Float.valueOf(0.0F)).forGetter(AttackRange::minReach), (App)ExtraCodecs.floatRange(0.0F, 64.0F).optionalFieldOf("max_reach", Float.valueOf(3.0F)).forGetter(AttackRange::maxReach), (App)ExtraCodecs.floatRange(0.0F, 64.0F).optionalFieldOf("min_creative_reach", Float.valueOf(0.0F)).forGetter(AttackRange::minCreativeReach), (App)ExtraCodecs.floatRange(0.0F, 64.0F).optionalFieldOf("max_creative_reach", Float.valueOf(5.0F)).forGetter(AttackRange::maxCreativeReach), (App)ExtraCodecs.floatRange(0.0F, 1.0F).optionalFieldOf("hitbox_margin", Float.valueOf(0.3F)).forGetter(AttackRange::hitboxMargin), (App)Codec.floatRange(0.0F, 2.0F).optionalFieldOf("mob_factor", Float.valueOf(1.0F)).forGetter(AttackRange::mobFactor)).apply((Applicative)i, AttackRange::new));
/*     */   }
/*     */   
/*  45 */   public static final StreamCodec<ByteBuf, AttackRange> STREAM_CODEC = StreamCodec.composite(ByteBufCodecs.FLOAT, AttackRange::minReach, ByteBufCodecs.FLOAT, AttackRange::maxReach, ByteBufCodecs.FLOAT, AttackRange::minCreativeReach, ByteBufCodecs.FLOAT, AttackRange::maxCreativeReach, ByteBufCodecs.FLOAT, AttackRange::hitboxMargin, ByteBufCodecs.FLOAT, AttackRange::mobFactor, AttackRange::new);
/*     */   
/*     */   public static AttackRange defaultFor(LivingEntity livingEntity) {
/*  57 */     float interactionRange = (float)livingEntity.getAttributeValue(Attributes.ENTITY_INTERACTION_RANGE);
/*  58 */     return new AttackRange(0.0F, interactionRange, 0.0F, interactionRange, 0.0F, 1.0F);
/*     */   }
/*     */   
/*     */   public HitResult getClosesetHit(Entity attacker, float partial, Predicate<Entity> matching) {
/*  62 */     Either<BlockHitResult, Collection<EntityHitResult>> result = ProjectileUtil.getHitEntitiesAlong(attacker, this, matching, ClipContext.Block.OUTLINE);
/*  63 */     if (result.left().isPresent())
/*  64 */       return result.left().get(); 
/*  67 */     Collection<EntityHitResult> targets = result.right().get();
/*  69 */     EntityHitResult entity = null;
/*  70 */     Vec3 attackerPos = attacker.getEyePosition(partial);
/*  71 */     double closestDistance = Double.MAX_VALUE;
/*  72 */     for (EntityHitResult target : targets) {
/*  73 */       double distance = attackerPos.distanceToSqr(target.getLocation());
/*  74 */       if (distance < closestDistance) {
/*  75 */         closestDistance = distance;
/*  76 */         entity = target;
/*     */       } 
/*     */     } 
/*  80 */     if (entity != null)
/*  81 */       return (HitResult)entity; 
/*  84 */     Vec3 eyeGaze = attacker.getHeadLookAngle();
/*  85 */     Vec3 missPosition = attacker.getEyePosition(partial).add(eyeGaze);
/*  86 */     return (HitResult)BlockHitResult.miss(missPosition, Direction.getApproximateNearest(eyeGaze), BlockPos.containing((Position)missPosition));
/*     */   }
/*     */   
/*     */   public float effectiveMinRange(Entity entity) {
/*  90 */     if (entity instanceof Player) {
/*  90 */       Player player = (Player)entity;
/*  91 */       return player.isCreative() ? this.minCreativeReach : this.minReach;
/*     */     } 
/*  93 */     return this.minReach * this.mobFactor;
/*     */   }
/*     */   
/*     */   public float effectiveMaxRange(Entity entity) {
/*  98 */     if (entity instanceof Player) {
/*  98 */       Player player = (Player)entity;
/*  99 */       return player.isCreative() ? this.maxCreativeReach : this.maxReach;
/*     */     } 
/* 101 */     return this.maxReach * this.mobFactor;
/*     */   }
/*     */   
/*     */   public boolean isInRange(LivingEntity attacker, Vec3 location) {
/* 106 */     Objects.requireNonNull(location);
/* 106 */     return isInRange(attacker, location::distanceToSqr, 0.0D);
/*     */   }
/*     */   
/*     */   public boolean isInRange(LivingEntity attacker, AABB boundingBox, double extraBuffer) {
/* 110 */     Objects.requireNonNull(boundingBox);
/* 110 */     return isInRange(attacker, boundingBox::distanceToSqr, extraBuffer);
/*     */   }
/*     */   
/*     */   private boolean isInRange(LivingEntity attacker, ToDoubleFunction<Vec3> distanceFunction, double extraBuffer) {
/* 114 */     double distance = Math.sqrt(distanceFunction.applyAsDouble(attacker.getEyePosition()));
/* 115 */     double minReach = (effectiveMinRange((Entity)attacker) - this.hitboxMargin) - extraBuffer;
/* 116 */     double maxReach = (effectiveMaxRange((Entity)attacker) + this.hitboxMargin) + extraBuffer;
/* 117 */     return (distance >= minReach && distance <= maxReach);
/*     */   }
/*     */ }


/* Location:              C:\Users\karol\AppData\Roaming\ModrinthApp\meta\versions\26.2-0.19.5\26.2-0.19.5.jar!\net\minecraft\world\item\component\AttackRange.class
 * Java compiler version: 25 (69.0)
 * JD-Core Version:       1.1.3
 */