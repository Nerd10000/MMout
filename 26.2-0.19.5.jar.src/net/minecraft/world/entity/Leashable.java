/*     */ package net.minecraft.world.entity;
/*     */ 
/*     */ import com.google.common.collect.ImmutableList;
/*     */ import com.mojang.datafixers.util.Either;
/*     */ import com.mojang.serialization.Codec;
/*     */ import java.util.ArrayList;
/*     */ import java.util.List;
/*     */ import java.util.Objects;
/*     */ import java.util.Optional;
/*     */ import java.util.UUID;
/*     */ import java.util.function.Function;
/*     */ import java.util.function.Predicate;
/*     */ import net.minecraft.core.BlockPos;
/*     */ import net.minecraft.core.UUIDUtil;
/*     */ import net.minecraft.network.protocol.Packet;
/*     */ import net.minecraft.network.protocol.game.ClientboundSetEntityLinkPacket;
/*     */ import net.minecraft.server.level.ServerLevel;
/*     */ import net.minecraft.sounds.SoundEvents;
/*     */ import net.minecraft.sounds.SoundSource;
/*     */ import net.minecraft.world.entity.decoration.LeashFenceKnotEntity;
/*     */ import net.minecraft.world.item.Items;
/*     */ import net.minecraft.world.level.ItemLike;
/*     */ import net.minecraft.world.level.Level;
/*     */ import net.minecraft.world.level.gamerules.GameRules;
/*     */ import net.minecraft.world.level.storage.ValueInput;
/*     */ import net.minecraft.world.level.storage.ValueOutput;
/*     */ import net.minecraft.world.phys.AABB;
/*     */ import net.minecraft.world.phys.Vec3;
/*     */ 
/*     */ public interface Leashable {
/*     */   public static final String LEASH_TAG = "leash";
/*     */   
/*     */   public static final double LEASH_TOO_FAR_DIST = 12.0D;
/*     */   
/*     */   public static final double LEASH_ELASTIC_DIST = 6.0D;
/*     */   
/*     */   public static final double MAXIMUM_ALLOWED_LEASHED_DIST = 16.0D;
/*     */   
/*     */   default boolean isLeashed() {
/*  42 */     return (getLeashData() != null && (getLeashData()).leashHolder != null);
/*     */   }
/*     */   
/*     */   default boolean mayBeLeashed() {
/*  46 */     return (getLeashData() != null);
/*     */   }
/*     */   
/*     */   default boolean canHaveALeashAttachedTo(Entity entity) {
/*  50 */     if (this == entity)
/*  51 */       return false; 
/*  53 */     if (leashDistanceTo(entity) > leashSnapDistance())
/*  54 */       return false; 
/*  56 */     return canBeLeashed();
/*     */   }
/*     */   
/*     */   default double leashDistanceTo(Entity entity) {
/*  60 */     return entity.getBoundingBox().getCenter().distanceTo(((Entity)this).getBoundingBox().getCenter());
/*     */   }
/*     */   
/*     */   default boolean canBeLeashed() {
/*  64 */     return true;
/*     */   }
/*     */   
/*     */   default void setDelayedLeashHolderId(int entityId) {
/*  68 */     setLeashData(new LeashData(entityId));
/*  69 */     dropLeash((Entity)this, false, false);
/*     */   }
/*     */   
/*     */   default void readLeashData(ValueInput input) {
/*  73 */     LeashData newLeashData = input.read("leash", LeashData.CODEC).orElse(null);
/*  74 */     if (getLeashData() != null && newLeashData == null)
/*  75 */       removeLeash(); 
/*  77 */     setLeashData(newLeashData);
/*     */   }
/*     */   
/*     */   default void writeLeashData(ValueOutput output, LeashData leashData) {
/*  81 */     output.storeNullable("leash", LeashData.CODEC, leashData);
/*     */   }
/*     */   
/*     */   private static <E extends Entity & Leashable> void restoreLeashFromSave(E entity, LeashData leashData) {
/*  87 */     if (leashData.delayedLeashInfo != null) {
/*  87 */       Level level = entity.level();
/*  87 */       if (level instanceof ServerLevel) {
/*  87 */         ServerLevel serverLevel = (ServerLevel)level;
/*  88 */         Optional<UUID> leashUuid = leashData.delayedLeashInfo.left();
/*  89 */         Optional<BlockPos> pos = leashData.delayedLeashInfo.right();
/*  90 */         if (leashUuid.isPresent()) {
/*  91 */           Entity leasher = serverLevel.getEntity(leashUuid.get());
/*  92 */           if (leasher != null) {
/*  93 */             setLeashedTo(entity, leasher, true);
/*     */             return;
/*     */           } 
/*  96 */         } else if (pos.isPresent()) {
/*  97 */           setLeashedTo(entity, (Entity)LeashFenceKnotEntity.getOrCreateKnot((Level)serverLevel, pos.get()), true);
/*     */           return;
/*     */         } 
/* 101 */         if (((Entity)entity).tickCount > 100) {
/* 102 */           entity.spawnAtLocation(serverLevel, (ItemLike)Items.LEAD);
/* 103 */           ((Leashable)entity).setLeashData(null);
/*     */         } 
/*     */       } 
/*     */     } 
/*     */   }
/*     */   
/*     */   default void dropLeash() {
/* 109 */     dropLeash((Entity)this, true, true);
/*     */   }
/*     */   
/*     */   default void removeLeash() {
/* 113 */     dropLeash((Entity)this, true, false);
/*     */   }
/*     */   
/*     */   default void onLeashRemoved() {}
/*     */   
/*     */   private static <E extends Entity & Leashable> void dropLeash(E entity, boolean sendPacket, boolean dropLead) {
/* 121 */     LeashData leashData = ((Leashable)entity).getLeashData();
/* 122 */     if (leashData != null && leashData.leashHolder != null) {
/* 123 */       ((Leashable)entity).setLeashData(null);
/* 124 */       ((Leashable)entity).onLeashRemoved();
/* 125 */       Level level = entity.level();
/* 125 */       if (level instanceof ServerLevel) {
/* 125 */         ServerLevel serverLevel = (ServerLevel)level;
/* 126 */         if (dropLead)
/* 127 */           entity.spawnAtLocation(serverLevel, (ItemLike)Items.LEAD); 
/* 130 */         if (sendPacket)
/* 131 */           serverLevel.getChunkSource().sendToTrackingPlayers((Entity)entity, (Packet)new ClientboundSetEntityLinkPacket((Entity)entity, null)); 
/* 133 */         leashData.leashHolder.notifyLeasheeRemoved((Leashable)entity);
/*     */       } 
/*     */     } 
/*     */   }
/*     */   
/*     */   static <E extends Entity & Leashable> void tickLeash(ServerLevel level, E entity) {
/* 139 */     LeashData leashData = ((Leashable)entity).getLeashData();
/* 140 */     if (leashData != null && leashData.delayedLeashInfo != null)
/* 141 */       restoreLeashFromSave(entity, leashData); 
/* 143 */     if (leashData == null || leashData.leashHolder == null)
/*     */       return; 
/* 146 */     if (!entity.canInteractWithLevel() || !leashData.leashHolder.canInteractWithLevel())
/* 147 */       if (((Boolean)level.getGameRules().get(GameRules.ENTITY_DROPS)).booleanValue()) {
/* 148 */         ((Leashable)entity).dropLeash();
/*     */       } else {
/* 150 */         ((Leashable)entity).removeLeash();
/*     */       }  
/* 153 */     Entity leashHolder = ((Leashable)entity).getLeashHolder();
/* 154 */     if (leashHolder != null && leashHolder.level() == entity.level()) {
/* 155 */       double distanceTo = ((Leashable)entity).leashDistanceTo(leashHolder);
/* 157 */       ((Leashable)entity).whenLeashedTo(leashHolder);
/* 159 */       if (distanceTo > ((Leashable)entity).leashSnapDistance()) {
/* 160 */         level.playSound(null, leashHolder.getX(), leashHolder.getY(), leashHolder.getZ(), SoundEvents.LEAD_BREAK, SoundSource.NEUTRAL, 1.0F, 1.0F);
/* 161 */         ((Leashable)entity).leashTooFarBehaviour();
/* 162 */       } else if (distanceTo > ((Leashable)entity).leashElasticDistance() - leashHolder.getBbWidth() - entity.getBbWidth() && ((Leashable)entity).checkElasticInteractions(leashHolder, leashData)) {
/* 163 */         ((Leashable)entity).onElasticLeashPull();
/*     */       } else {
/* 165 */         ((Leashable)entity).closeRangeLeashBehaviour(leashHolder);
/*     */       } 
/* 168 */       entity.setYRot((float)(entity.getYRot() - leashData.angularMomentum));
/* 169 */       leashData.angularMomentum *= angularFriction(entity);
/*     */     } 
/*     */   }
/*     */   
/*     */   default void onElasticLeashPull() {
/* 174 */     Entity entity = (Entity)this;
/* 175 */     entity.checkFallDistanceAccumulation();
/*     */   }
/*     */   
/*     */   default double leashSnapDistance() {
/* 179 */     return 12.0D;
/*     */   }
/*     */   
/*     */   default double leashElasticDistance() {
/* 183 */     return 6.0D;
/*     */   }
/*     */   
/*     */   static <E extends Entity & Leashable> float angularFriction(E entity) {
/* 187 */     if (entity.onGround())
/* 188 */       return entity.level().getBlockState(entity.getBlockPosBelowThatAffectsMyMovement()).getBlock().getFriction() * 0.91F; 
/* 191 */     if (entity.isInLiquid())
/* 192 */       return 0.8F; 
/* 194 */     return 0.91F;
/*     */   }
/*     */   
/*     */   default void whenLeashedTo(Entity leashHolder) {
/* 198 */     leashHolder.notifyLeashHolder(this);
/*     */   }
/*     */   
/*     */   default void leashTooFarBehaviour() {
/* 202 */     dropLeash();
/*     */   }
/*     */   
/*     */   default void closeRangeLeashBehaviour(Entity leashHolder) {}
/*     */   
/* 210 */   public static final Vec3 AXIS_SPECIFIC_ELASTICITY = new Vec3(0.8D, 0.2D, 0.8D);
/*     */   
/*     */   public static final float SPRING_DAMPENING = 0.7F;
/*     */   
/*     */   public static final double TORSIONAL_ELASTICITY = 10.0D;
/*     */   
/*     */   public static final double STIFFNESS = 0.11D;
/*     */   
/* 221 */   public static final List<Vec3> ENTITY_ATTACHMENT_POINT = (List<Vec3>)ImmutableList.of(new Vec3(0.0D, 0.5D, 0.5D));
/*     */   
/* 222 */   public static final List<Vec3> LEASHER_ATTACHMENT_POINT = (List<Vec3>)ImmutableList.of(new Vec3(0.0D, 0.5D, 0.0D));
/*     */   
/* 223 */   public static final List<Vec3> SHARED_QUAD_ATTACHMENT_POINTS = (List<Vec3>)ImmutableList.of(new Vec3(-0.5D, 0.5D, 0.5D), new Vec3(-0.5D, 0.5D, -0.5D), new Vec3(0.5D, 0.5D, -0.5D), new Vec3(0.5D, 0.5D, 0.5D));
/*     */   
/*     */   default boolean checkElasticInteractions(Entity leashHolder, LeashData leashData) {
/* 226 */     boolean quadConnection = (leashHolder.supportQuadLeashAsHolder() && supportQuadLeash());
/* 227 */     List<Wrench> wrenches = computeElasticInteraction((Entity)this, leashHolder, 
/* 228 */         quadConnection ? SHARED_QUAD_ATTACHMENT_POINTS : ENTITY_ATTACHMENT_POINT, 
/* 229 */         quadConnection ? SHARED_QUAD_ATTACHMENT_POINTS : LEASHER_ATTACHMENT_POINT);
/* 231 */     if (wrenches.isEmpty())
/* 233 */       return false; 
/* 235 */     Wrench result = Wrench.accumulate(wrenches).scale(quadConnection ? 0.25D : 1.0D);
/* 236 */     leashData.angularMomentum += 10.0D * result.torque();
/* 237 */     Vec3 relativeVelocityToLeasher = getHolderMovement(leashHolder).subtract(((Entity)this).getKnownMovement());
/* 238 */     ((Entity)this).addDeltaMovement(result.force().multiply(AXIS_SPECIFIC_ELASTICITY).add(relativeVelocityToLeasher.scale(0.11D)));
/* 239 */     return true;
/*     */   }
/*     */   
/*     */   private static Vec3 getHolderMovement(Entity leashHolder) {
/* 244 */     if (leashHolder instanceof Mob) {
/* 244 */       Mob mob = (Mob)leashHolder;
/* 244 */       if (mob.isNoAi())
/* 245 */         return Vec3.ZERO; 
/*     */     } 
/* 247 */     return leashHolder.getKnownMovement();
/*     */   }
/*     */   
/*     */   private static <E extends Entity & Leashable> List<Wrench> computeElasticInteraction(E entity, Entity leashHolder, List<Vec3> entityAttachmentPoints, List<Vec3> leasherAttachmentPoints) {
/* 251 */     double slackDistance = ((Leashable)entity).leashElasticDistance();
/* 252 */     Vec3 currentMovement = getHolderMovement((Entity)entity);
/* 254 */     float entityYRot = entity.getYRot() * 0.017453292F;
/* 255 */     Vec3 entityDimensions = new Vec3(entity.getBbWidth(), entity.getBbHeight(), entity.getBbWidth());
/* 257 */     float leashHolderYRot = leashHolder.getYRot() * 0.017453292F;
/* 258 */     Vec3 leasherDimensions = new Vec3(leashHolder.getBbWidth(), leashHolder.getBbHeight(), leashHolder.getBbWidth());
/* 260 */     List<Wrench> wrenches = new ArrayList<>();
/* 261 */     for (int i = 0; i < entityAttachmentPoints.size(); i++) {
/* 262 */       Vec3 entityAttachVector = ((Vec3)entityAttachmentPoints.get(i)).multiply(entityDimensions).yRot(-entityYRot);
/* 263 */       Vec3 entityAttachPos = entity.position().add(entityAttachVector);
/* 264 */       Vec3 leasherAttachVector = ((Vec3)leasherAttachmentPoints.get(i)).multiply(leasherDimensions).yRot(-leashHolderYRot);
/* 265 */       Vec3 leasherAttachPos = leashHolder.position().add(leasherAttachVector);
/* 267 */       Objects.requireNonNull(wrenches);
/* 267 */       computeDampenedSpringInteraction(leasherAttachPos, entityAttachPos, slackDistance, currentMovement, entityAttachVector).ifPresent(wrenches::add);
/*     */     } 
/* 269 */     return wrenches;
/*     */   }
/*     */   
/*     */   private static Optional<Wrench> computeDampenedSpringInteraction(Vec3 pivotPoint, Vec3 objectPosition, double springSlack, Vec3 objectMotion, Vec3 leverArm) {
/* 273 */     double distance = objectPosition.distanceTo(pivotPoint);
/* 274 */     if (distance < springSlack)
/* 275 */       return Optional.empty(); 
/* 277 */     Vec3 displacement = pivotPoint.subtract(objectPosition).normalize().scale(distance - springSlack);
/* 278 */     double torque = Wrench.torqueFromForce(leverArm, displacement);
/* 280 */     boolean sameDirectionToMovement = (objectMotion.dot(displacement) >= 0.0D);
/* 281 */     if (sameDirectionToMovement)
/* 282 */       displacement = displacement.scale(0.30000001192092896D); 
/* 285 */     return Optional.of(new Wrench(displacement, torque));
/*     */   }
/*     */   
/*     */   default boolean supportQuadLeash() {
/* 289 */     return false;
/*     */   }
/*     */   
/*     */   default Vec3[] getQuadLeashOffsets() {
/* 293 */     return createQuadLeashOffsets((Entity)this, 0.0D, 0.5D, 0.5D, 0.5D);
/*     */   }
/*     */   
/*     */   static Vec3[] createQuadLeashOffsets(Entity entity, double frontOffset, double frontBack, double leftRight, double height) {
/* 297 */     float width = entity.getBbWidth();
/* 298 */     double frontOffsetScaled = frontOffset * width;
/* 299 */     double frontBackScaled = frontBack * width;
/* 300 */     double leftRightScaled = leftRight * width;
/* 301 */     double heightScaled = height * entity.getBbHeight();
/* 303 */     return new Vec3[] { new Vec3(-leftRightScaled, heightScaled, frontBackScaled + frontOffsetScaled), new Vec3(-leftRightScaled, heightScaled, -frontBackScaled + frontOffsetScaled), new Vec3(leftRightScaled, heightScaled, -frontBackScaled + frontOffsetScaled), new Vec3(leftRightScaled, heightScaled, frontBackScaled + frontOffsetScaled) };
/*     */   }
/*     */   
/*     */   default Vec3 getLeashOffset(float partialTicks) {
/* 316 */     return getLeashOffset();
/*     */   }
/*     */   
/*     */   default Vec3 getLeashOffset() {
/* 320 */     Entity entity = (Entity)this;
/* 321 */     return new Vec3(0.0D, entity.getEyeHeight(), (entity.getBbWidth() * 0.4F));
/*     */   }
/*     */   
/*     */   default void setLeashedTo(Entity holder, boolean synch) {
/* 325 */     if (this == holder)
/*     */       return; 
/* 328 */     setLeashedTo((Entity)this, holder, synch);
/*     */   }
/*     */   
/*     */   private static <E extends Entity & Leashable> void setLeashedTo(E entity, Entity holder, boolean synch) {
/* 332 */     LeashData leashData = ((Leashable)entity).getLeashData();
/* 333 */     if (leashData == null) {
/* 334 */       leashData = new LeashData(holder);
/* 335 */       ((Leashable)entity).setLeashData(leashData);
/*     */     } else {
/* 337 */       Entity oldHolder = leashData.leashHolder;
/* 338 */       leashData.setLeashHolder(holder);
/* 339 */       if (oldHolder != null && oldHolder != holder)
/* 340 */         oldHolder.notifyLeasheeRemoved((Leashable)entity); 
/*     */     } 
/* 344 */     if (synch) {
/* 344 */       Level level = entity.level();
/* 344 */       if (level instanceof ServerLevel) {
/* 344 */         ServerLevel serverLevel = (ServerLevel)level;
/* 345 */         serverLevel.getChunkSource().sendToTrackingPlayers((Entity)entity, (Packet)new ClientboundSetEntityLinkPacket((Entity)entity, holder));
/*     */       } 
/*     */     } 
/* 348 */     if (entity.isPassenger())
/* 349 */       entity.stopRiding(); 
/*     */   }
/*     */   
/*     */   default Entity getLeashHolder() {
/* 355 */     return getLeashHolder((Entity)this);
/*     */   }
/*     */   
/*     */   private static <E extends Entity & Leashable> Entity getLeashHolder(E entity) {
/* 359 */     LeashData leashData = ((Leashable)entity).getLeashData();
/* 360 */     if (leashData == null)
/* 361 */       return null; 
/* 364 */     Entity ntt = entity.level().getEntity(leashData.delayedLeashHolderId);
/* 365 */     if (leashData.delayedLeashHolderId != 0 && entity.level().isClientSide() && ntt != null)
/* 366 */       leashData.setLeashHolder(ntt); 
/* 368 */     return leashData.leashHolder;
/*     */   }
/*     */   
/*     */   static List<Leashable> leashableLeashedTo(Entity entity) {
/* 372 */     return leashableInArea(entity, l -> (l.getLeashHolder() == entity));
/*     */   }
/*     */   
/*     */   static List<Leashable> leashableInArea(Entity entity, Predicate<Leashable> test) {
/* 376 */     return leashableInArea(entity.level(), entity.getBoundingBox().getCenter(), test);
/*     */   }
/*     */   
/*     */   static List<Leashable> leashableInArea(Level level, Vec3 pos, Predicate<Leashable> test) {
/* 380 */     double size = 32.0D;
/* 381 */     AABB scanArea = AABB.ofSize(pos, 32.0D, 32.0D, 32.0D);
/* 382 */     Objects.requireNonNull(Leashable.class);
/* 382 */     return level.getEntitiesOfClass(Entity.class, scanArea, e -> {
/*     */           if (e instanceof Leashable) {
/*     */             Leashable leashable = (Leashable)e;
/*     */             if (test.test(leashable));
/*     */           } 
/*     */           return false;
/* 382 */         }).stream().map(Leashable.class::cast).toList();
/*     */   }
/*     */   
/*     */   LeashData getLeashData();
/*     */   
/*     */   void setLeashData(LeashData paramLeashData);
/*     */   
/*     */   public static final class LeashData {
/*     */     public static final Codec<LeashData> CODEC;
/*     */     
/*     */     private int delayedLeashHolderId;
/*     */     
/*     */     public Entity leashHolder;
/*     */     
/*     */     public Either<UUID, BlockPos> delayedLeashInfo;
/*     */     
/*     */     public double angularMomentum;
/*     */     
/*     */     static {
/* 389 */       CODEC = Codec.xor(UUIDUtil.CODEC.fieldOf("UUID").codec(), BlockPos.CODEC).xmap(LeashData::new, data -> {
/*     */             Entity patt0$temp = data.leashHolder;
/*     */             if (patt0$temp instanceof LeashFenceKnotEntity) {
/*     */               LeashFenceKnotEntity leashKnot = (LeashFenceKnotEntity)patt0$temp;
/*     */               return Either.right(leashKnot.getPos());
/*     */             } 
/*     */             return (data.leashHolder != null) ? Either.left(data.leashHolder.getUUID()) : Objects.<Either>requireNonNull(data.delayedLeashInfo, "Invalid LeashData had no attachment");
/*     */           });
/*     */     }
/*     */     
/*     */     private LeashData(Either<UUID, BlockPos> delayedLeashInfo) {
/* 401 */       this.delayedLeashInfo = delayedLeashInfo;
/*     */     }
/*     */     
/*     */     private LeashData(Entity entity) {
/* 405 */       this.leashHolder = entity;
/*     */     }
/*     */     
/*     */     private LeashData(int entityId) {
/* 409 */       this.delayedLeashHolderId = entityId;
/*     */     }
/*     */     
/*     */     public void setLeashHolder(Entity leashHolder) {
/* 413 */       this.leashHolder = leashHolder;
/* 414 */       this.delayedLeashInfo = null;
/* 415 */       this.delayedLeashHolderId = 0;
/*     */     }
/*     */   }
/*     */   
/*     */   public static final class Wrench extends Record {
/*     */     private final Vec3 force;
/*     */     
/*     */     private final double torque;
/*     */     
/*     */     public Wrench(Vec3 force, double torque) {
/* 429 */       this.force = force;
/* 429 */       this.torque = torque;
/*     */     }
/*     */     
/*     */     public final String toString() {
/*     */       // Byte code:
/*     */       //   0: aload_0
/*     */       //   1: <illegal opcode> toString : (Lnet/minecraft/world/entity/Leashable$Wrench;)Ljava/lang/String;
/*     */       //   6: areturn
/*     */       // Line number table:
/*     */       //   Java source line number -> byte code offset
/*     */       //   #429	-> 0
/*     */       // Local variable table:
/*     */       //   start	length	slot	name	descriptor
/*     */       //   0	7	0	this	Lnet/minecraft/world/entity/Leashable$Wrench;
/*     */     }
/*     */     
/*     */     public final int hashCode() {
/*     */       // Byte code:
/*     */       //   0: aload_0
/*     */       //   1: <illegal opcode> hashCode : (Lnet/minecraft/world/entity/Leashable$Wrench;)I
/*     */       //   6: ireturn
/*     */       // Line number table:
/*     */       //   Java source line number -> byte code offset
/*     */       //   #429	-> 0
/*     */       // Local variable table:
/*     */       //   start	length	slot	name	descriptor
/*     */       //   0	7	0	this	Lnet/minecraft/world/entity/Leashable$Wrench;
/*     */     }
/*     */     
/*     */     public final boolean equals(Object o) {
/*     */       // Byte code:
/*     */       //   0: aload_0
/*     */       //   1: aload_1
/*     */       //   2: <illegal opcode> equals : (Lnet/minecraft/world/entity/Leashable$Wrench;Ljava/lang/Object;)Z
/*     */       //   7: ireturn
/*     */       // Line number table:
/*     */       //   Java source line number -> byte code offset
/*     */       //   #429	-> 0
/*     */       // Local variable table:
/*     */       //   start	length	slot	name	descriptor
/*     */       //   0	8	0	this	Lnet/minecraft/world/entity/Leashable$Wrench;
/*     */       //   0	8	1	o	Ljava/lang/Object;
/*     */     }
/*     */     
/*     */     public Vec3 force() {
/* 429 */       return this.force;
/*     */     }
/*     */     
/*     */     public double torque() {
/* 429 */       return this.torque;
/*     */     }
/*     */     
/* 433 */     public static final Wrench ZERO = new Wrench(Vec3.ZERO, 0.0D);
/*     */     
/*     */     public static double torqueFromForce(Vec3 leverArm, Vec3 force) {
/* 437 */       return leverArm.z * force.x - leverArm.x * force.z;
/*     */     }
/*     */     
/*     */     public static Wrench accumulate(List<Wrench> wrenches) {
/* 441 */       if (wrenches.isEmpty())
/* 442 */         return ZERO; 
/* 444 */       double x = 0.0D;
/* 445 */       double y = 0.0D;
/* 446 */       double z = 0.0D;
/* 447 */       double t = 0.0D;
/* 448 */       for (Wrench wrench : wrenches) {
/* 449 */         Vec3 force = wrench.force;
/* 450 */         x += force.x;
/* 451 */         y += force.y;
/* 452 */         z += force.z;
/* 453 */         t += wrench.torque;
/*     */       } 
/* 455 */       return new Wrench(new Vec3(x, y, z), t);
/*     */     }
/*     */     
/*     */     public Wrench scale(double scale) {
/* 459 */       return new Wrench(this.force.scale(scale), this.torque * scale);
/*     */     }
/*     */   }
/*     */ }


/* Location:              C:\Users\karol\AppData\Roaming\ModrinthApp\meta\versions\26.2-0.19.5\26.2-0.19.5.jar!\net\minecraft\world\entity\Leashable.class
 * Java compiler version: 25 (69.0)
 * JD-Core Version:       1.1.3
 */