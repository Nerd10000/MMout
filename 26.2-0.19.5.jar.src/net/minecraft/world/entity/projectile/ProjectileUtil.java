/*     */ package net.minecraft.world.entity.projectile;
/*     */ 
/*     */ import com.mojang.datafixers.util.Either;
/*     */ import java.util.ArrayList;
/*     */ import java.util.Collection;
/*     */ import java.util.List;
/*     */ import java.util.Optional;
/*     */ import java.util.function.Predicate;
/*     */ import net.minecraft.util.Mth;
/*     */ import net.minecraft.world.InteractionHand;
/*     */ import net.minecraft.world.entity.Entity;
/*     */ import net.minecraft.world.entity.LivingEntity;
/*     */ import net.minecraft.world.entity.projectile.arrow.AbstractArrow;
/*     */ import net.minecraft.world.item.ArrowItem;
/*     */ import net.minecraft.world.item.Item;
/*     */ import net.minecraft.world.item.ItemStack;
/*     */ import net.minecraft.world.item.Items;
/*     */ import net.minecraft.world.item.component.AttackRange;
/*     */ import net.minecraft.world.level.ClipContext;
/*     */ import net.minecraft.world.level.Level;
/*     */ import net.minecraft.world.phys.AABB;
/*     */ import net.minecraft.world.phys.BlockHitResult;
/*     */ import net.minecraft.world.phys.EntityHitResult;
/*     */ import net.minecraft.world.phys.HitResult;
/*     */ import net.minecraft.world.phys.Vec3;
/*     */ 
/*     */ public final class ProjectileUtil {
/*     */   public static final float DEFAULT_ENTITY_HIT_RESULT_MARGIN = 0.3F;
/*     */   
/*     */   public static HitResult getHitResultOnMoveVector(Entity source, Predicate<Entity> matching) {
/*  35 */     Vec3 movement = source.getDeltaMovement();
/*  36 */     Level level = source.level();
/*  38 */     Vec3 from = source.position();
/*  39 */     return getHitResult(from, source, matching, movement, level, computeMargin(source), ClipContext.Block.COLLIDER);
/*     */   }
/*     */   
/*     */   public static Either<BlockHitResult, Collection<EntityHitResult>> getHitEntitiesAlong(Entity attacker, AttackRange attackRange, Predicate<Entity> matching, ClipContext.Block blockClipType) {
/*  44 */     Vec3 look = attacker.getHeadLookAngle();
/*  45 */     Vec3 eyePosition = attacker.getEyePosition();
/*  46 */     Vec3 from = eyePosition.add(look.scale(attackRange.effectiveMinRange(attacker)));
/*  47 */     double movementComponent = attacker.getKnownMovement().dot(look);
/*  48 */     Vec3 to = eyePosition.add(look.scale(attackRange.effectiveMaxRange(attacker) + Math.max(0.0D, movementComponent)));
/*  49 */     return getHitEntitiesAlong(attacker, eyePosition, from, matching, to, attackRange.hitboxMargin(), blockClipType);
/*     */   }
/*     */   
/*     */   public static HitResult getHitResultOnMoveVector(Entity source, Predicate<Entity> matching, ClipContext.Block clipType) {
/*  54 */     Vec3 movement = source.getDeltaMovement();
/*  55 */     Level level = source.level();
/*  57 */     Vec3 from = source.position();
/*  58 */     return getHitResult(from, source, matching, movement, level, computeMargin(source), clipType);
/*     */   }
/*     */   
/*     */   public static HitResult getHitResultOnViewVector(Entity source, Predicate<Entity> matching, double distance) {
/*  62 */     Vec3 viewVector = source.getViewVector(0.0F).scale(distance);
/*  63 */     Level level = source.level();
/*  65 */     Vec3 from = source.getEyePosition();
/*  66 */     return getHitResult(from, source, matching, viewVector, level, 0.0F, ClipContext.Block.COLLIDER);
/*     */   }
/*     */   
/*     */   private static HitResult getHitResult(Vec3 from, Entity source, Predicate<Entity> matching, Vec3 delta, Level level, float entityMargin, ClipContext.Block clipType) {
/*     */     EntityHitResult entityHitResult1;
/*  70 */     Vec3 to = from.add(delta);
/*  71 */     BlockHitResult blockHitResult = level.clipIncludingBorder(new ClipContext(from, to, clipType, ClipContext.Fluid.NONE, source));
/*  73 */     if (blockHitResult.getType() != HitResult.Type.MISS)
/*  74 */       to = blockHitResult.getLocation(); 
/*  76 */     EntityHitResult entityHitResult2 = getEntityHitResult(level, source, from, to, source.getBoundingBox().expandTowards(delta).inflate(1.0D), matching, entityMargin);
/*  78 */     if (entityHitResult2 != null)
/*  79 */       entityHitResult1 = entityHitResult2; 
/*  82 */     return (HitResult)entityHitResult1;
/*     */   }
/*     */   
/*     */   private static Either<BlockHitResult, Collection<EntityHitResult>> getHitEntitiesAlong(Entity source, Vec3 origin, Vec3 from, Predicate<Entity> matching, Vec3 to, float entityMargin, ClipContext.Block clipType) {
/*  86 */     Level level = source.level();
/*  88 */     BlockHitResult hitResult = level.clipIncludingBorder(new ClipContext(origin, to, clipType, ClipContext.Fluid.NONE, source));
/*  90 */     if (hitResult.getType() != HitResult.Type.MISS) {
/*  91 */       to = hitResult.getLocation();
/*  92 */       if (origin.distanceToSqr(to) < origin.distanceToSqr(from))
/*  94 */         return Either.left(hitResult); 
/*     */     } 
/*  99 */     AABB searchArea = AABB.ofSize(from, entityMargin, entityMargin, entityMargin).expandTowards(to.subtract(from)).inflate(1.0D);
/* 100 */     Collection<EntityHitResult> entityHit = getManyEntityHitResult(level, source, from, to, searchArea, matching, entityMargin, clipType, true);
/* 102 */     if (!entityHit.isEmpty())
/* 103 */       return Either.right(entityHit); 
/* 106 */     return Either.left(hitResult);
/*     */   }
/*     */   
/*     */   public static EntityHitResult getEntityHitResult(Entity except, Vec3 from, Vec3 to, AABB box, Predicate<Entity> matching, double maxValue) {
/* 110 */     Level level = except.level();
/* 111 */     double nearest = maxValue;
/* 112 */     Entity hovered = null;
/* 113 */     Vec3 hoveredPos = null;
/* 115 */     for (Entity entity : level.getEntities(except, box, matching)) {
/* 116 */       AABB bb = entity.getBoundingBox().inflate(entity.getPickRadius());
/* 117 */       Optional<Vec3> clipPoint = bb.clip(from, to);
/* 118 */       if (bb.contains(from)) {
/* 119 */         if (nearest >= 0.0D && entity.canBePickedFromInside()) {
/* 120 */           hovered = entity;
/* 121 */           hoveredPos = clipPoint.orElse(from);
/* 122 */           nearest = 0.0D;
/*     */         } 
/*     */         continue;
/*     */       } 
/* 125 */       if (clipPoint.isPresent()) {
/* 126 */         Vec3 location = clipPoint.get();
/* 127 */         double dd = from.distanceToSqr(location);
/* 128 */         if (dd < nearest || nearest == 0.0D) {
/* 129 */           if (entity.getRootVehicle() == except.getRootVehicle()) {
/* 130 */             if (nearest == 0.0D) {
/* 131 */               hovered = entity;
/* 132 */               hoveredPos = location;
/*     */             } 
/*     */             continue;
/*     */           } 
/* 135 */           hovered = entity;
/* 136 */           hoveredPos = location;
/* 137 */           nearest = dd;
/*     */         } 
/*     */       } 
/*     */     } 
/* 144 */     if (hovered == null)
/* 145 */       return null; 
/* 147 */     return new EntityHitResult(hovered, hoveredPos);
/*     */   }
/*     */   
/*     */   public static EntityHitResult getEntityHitResult(Level level, Projectile source, Vec3 from, Vec3 to, AABB targetSearchArea, Predicate<Entity> matching) {
/* 151 */     return getEntityHitResult(level, source, from, to, targetSearchArea, matching, computeMargin(source));
/*     */   }
/*     */   
/*     */   public static float computeMargin(Entity source) {
/* 155 */     return Math.max(0.0F, Math.min(0.3F, (source.tickCount - 2) / 20.0F));
/*     */   }
/*     */   
/*     */   public static EntityHitResult getEntityHitResult(Level level, Entity source, Vec3 from, Vec3 to, AABB targetSearchArea, Predicate<Entity> matching, float entityMargin) {
/* 159 */     double nearest = Double.MAX_VALUE;
/* 160 */     Optional<Vec3> nearestLocation = Optional.empty();
/* 161 */     Entity hitEntity = null;
/* 163 */     for (Entity entity : level.getEntities(source, targetSearchArea, matching)) {
/* 164 */       AABB bb = entity.getBoundingBox().inflate(entityMargin);
/* 165 */       Optional<Vec3> location = bb.clip(from, to);
/* 166 */       if (location.isPresent()) {
/* 167 */         double dd = from.distanceToSqr(location.get());
/* 168 */         if (dd < nearest) {
/* 169 */           hitEntity = entity;
/* 170 */           nearest = dd;
/* 171 */           nearestLocation = location;
/*     */         } 
/*     */       } 
/*     */     } 
/* 176 */     if (hitEntity == null)
/* 177 */       return null; 
/* 179 */     return new EntityHitResult(hitEntity, nearestLocation.get());
/*     */   }
/*     */   
/*     */   public static Collection<EntityHitResult> getManyEntityHitResult(Level level, Entity source, Vec3 from, Vec3 to, AABB targetSearchArea, Predicate<Entity> matching, boolean includeFromEntity) {
/* 183 */     return getManyEntityHitResult(level, source, from, to, targetSearchArea, matching, computeMargin(source), ClipContext.Block.COLLIDER, includeFromEntity);
/*     */   }
/*     */   
/*     */   public static Collection<EntityHitResult> getManyEntityHitResult(Level level, Entity source, Vec3 from, Vec3 to, AABB targetSearchArea, Predicate<Entity> matching, float entityMargin, ClipContext.Block clipType, boolean includeFromEntity) {
/* 187 */     List<EntityHitResult> collector = new ArrayList<>();
/* 189 */     for (Entity entity : level.getEntities(source, targetSearchArea, matching)) {
/* 190 */       AABB entityBB = entity.getBoundingBox();
/* 193 */       if (includeFromEntity && entityBB.contains(from)) {
/* 194 */         collector.add(new EntityHitResult(entity, from));
/*     */         continue;
/*     */       } 
/* 197 */       Optional<Vec3> exactHit = entityBB.clip(from, to);
/* 198 */       if (exactHit.isPresent()) {
/* 199 */         collector.add(new EntityHitResult(entity, exactHit.get()));
/*     */         continue;
/*     */       } 
/* 202 */       if (entityMargin <= 0.0D)
/*     */         continue; 
/* 205 */       Optional<Vec3> outsideHit = entityBB.inflate(entityMargin).clip(from, to);
/* 206 */       if (outsideHit.isEmpty())
/*     */         continue; 
/* 209 */       Vec3 outsideHitPosition = outsideHit.get();
/* 210 */       Vec3 towardsTarget = entityBB.getCenter();
/* 211 */       BlockHitResult hitResult = level.clipIncludingBorder(new ClipContext(outsideHitPosition, towardsTarget, clipType, ClipContext.Fluid.NONE, source));
/* 213 */       if (hitResult.getType() != HitResult.Type.MISS)
/* 214 */         towardsTarget = hitResult.getLocation(); 
/* 216 */       Optional<Vec3> surfaceHit = entity.getBoundingBox().clip(outsideHitPosition, towardsTarget);
/* 217 */       if (surfaceHit.isPresent())
/* 218 */         collector.add(new EntityHitResult(entity, surfaceHit.get())); 
/*     */     } 
/* 222 */     return collector;
/*     */   }
/*     */   
/*     */   public static void rotateTowardsMovement(Entity projectile, float rotationSpeed) {
/* 227 */     Vec3 movement = projectile.getDeltaMovement();
/* 229 */     if (movement.lengthSqr() == 0.0D)
/*     */       return; 
/* 233 */     double sd = movement.horizontalDistance();
/* 234 */     projectile.setYRot((float)(Mth.atan2(movement.z, movement.x) * 57.2957763671875D) + 90.0F);
/* 235 */     projectile.setXRot((float)(Mth.atan2(sd, movement.y) * 57.2957763671875D) - 90.0F);
/* 237 */     while (projectile.getXRot() - projectile.xRotO < -180.0F)
/* 238 */       projectile.xRotO -= 360.0F; 
/* 240 */     while (projectile.getXRot() - projectile.xRotO >= 180.0F)
/* 241 */       projectile.xRotO += 360.0F; 
/* 244 */     while (projectile.getYRot() - projectile.yRotO < -180.0F)
/* 245 */       projectile.yRotO -= 360.0F; 
/* 247 */     while (projectile.getYRot() - projectile.yRotO >= 180.0F)
/* 248 */       projectile.yRotO += 360.0F; 
/* 251 */     projectile.setXRot(Mth.lerp(rotationSpeed, projectile.xRotO, projectile.getXRot()));
/* 252 */     projectile.setYRot(Mth.lerp(rotationSpeed, projectile.yRotO, projectile.getYRot()));
/*     */   }
/*     */   
/*     */   public static InteractionHand getWeaponHoldingHand(LivingEntity mob, Item weaponItem) {
/* 256 */     return mob.getMainHandItem().is(weaponItem) ? InteractionHand.MAIN_HAND : InteractionHand.OFF_HAND;
/*     */   }
/*     */   
/*     */   public static AbstractArrow getMobArrow(LivingEntity mob, ItemStack projectile, float power, ItemStack firedFromWeapon) {
/* 260 */     ArrowItem arrowItem = (projectile.getItem() instanceof ArrowItem) ? (ArrowItem)projectile.getItem() : (ArrowItem)Items.ARROW;
/* 261 */     AbstractArrow arrow = arrowItem.createArrow(mob.level(), projectile, mob, firedFromWeapon);
/* 262 */     arrow.setBaseDamageFromMob(power);
/* 264 */     return arrow;
/*     */   }
/*     */ }


/* Location:              C:\Users\karol\AppData\Roaming\ModrinthApp\meta\versions\26.2-0.19.5\26.2-0.19.5.jar!\net\minecraft\world\entity\projectile\ProjectileUtil.class
 * Java compiler version: 25 (69.0)
 * JD-Core Version:       1.1.3
 */