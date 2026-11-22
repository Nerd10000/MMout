/*     */ package net.minecraft.world.entity.projectile;
/*     */ 
/*     */ import com.google.common.base.MoreObjects;
/*     */ import com.google.common.collect.Lists;
/*     */ import java.util.List;
/*     */ import net.minecraft.core.BlockPos;
/*     */ import net.minecraft.core.Direction;
/*     */ import net.minecraft.core.Holder;
/*     */ import net.minecraft.core.Position;
/*     */ import net.minecraft.core.UUIDUtil;
/*     */ import net.minecraft.core.particles.ParticleOptions;
/*     */ import net.minecraft.core.particles.ParticleTypes;
/*     */ import net.minecraft.network.protocol.game.ClientboundAddEntityPacket;
/*     */ import net.minecraft.network.syncher.SynchedEntityData;
/*     */ import net.minecraft.server.level.ServerLevel;
/*     */ import net.minecraft.sounds.SoundEvents;
/*     */ import net.minecraft.sounds.SoundSource;
/*     */ import net.minecraft.util.Mth;
/*     */ import net.minecraft.world.Difficulty;
/*     */ import net.minecraft.world.damagesource.DamageSource;
/*     */ import net.minecraft.world.effect.MobEffectInstance;
/*     */ import net.minecraft.world.effect.MobEffects;
/*     */ import net.minecraft.world.entity.Entity;
/*     */ import net.minecraft.world.entity.EntityReference;
/*     */ import net.minecraft.world.entity.EntityType;
/*     */ import net.minecraft.world.entity.EntityTypes;
/*     */ import net.minecraft.world.entity.LivingEntity;
/*     */ import net.minecraft.world.item.enchantment.EnchantmentHelper;
/*     */ import net.minecraft.world.level.Level;
/*     */ import net.minecraft.world.level.entity.UniquelyIdentifyable;
/*     */ import net.minecraft.world.level.gameevent.GameEvent;
/*     */ import net.minecraft.world.level.storage.ValueInput;
/*     */ import net.minecraft.world.level.storage.ValueOutput;
/*     */ import net.minecraft.world.phys.BlockHitResult;
/*     */ import net.minecraft.world.phys.EntityHitResult;
/*     */ import net.minecraft.world.phys.HitResult;
/*     */ import net.minecraft.world.phys.Vec3;
/*     */ 
/*     */ public class ShulkerBullet extends Projectile {
/*     */   private static final double SPEED = 0.15D;
/*     */   
/*     */   private EntityReference<Entity> finalTarget;
/*     */   
/*     */   private Direction currentMoveDirection;
/*     */   
/*     */   private int flightSteps;
/*     */   
/*     */   private double targetDeltaX;
/*     */   
/*     */   private double targetDeltaY;
/*     */   
/*     */   private double targetDeltaZ;
/*     */   
/*     */   public ShulkerBullet(EntityType<? extends ShulkerBullet> type, Level level) {
/*  51 */     super((EntityType)type, level);
/*  53 */     this.noPhysics = true;
/*     */   }
/*     */   
/*     */   public ShulkerBullet(Level level, LivingEntity owner, Entity target, Direction.Axis invalidStartAxis) {
/*  57 */     this(EntityTypes.SHULKER_BULLET, level);
/*  58 */     setOwner((Entity)owner);
/*  60 */     Vec3 position = owner.getBoundingBox().getCenter();
/*  61 */     snapTo(position.x, position.y, position.z, getYRot(), getXRot());
/*  63 */     this.finalTarget = EntityReference.of((UniquelyIdentifyable)target);
/*  65 */     this.currentMoveDirection = Direction.UP;
/*  66 */     selectNextMoveDirection(invalidStartAxis, target);
/*     */   }
/*     */   
/*     */   public SoundSource getSoundSource() {
/*  71 */     return SoundSource.HOSTILE;
/*     */   }
/*     */   
/*     */   protected void addAdditionalSaveData(ValueOutput output) {
/*  76 */     super.addAdditionalSaveData(output);
/*  77 */     if (this.finalTarget != null)
/*  78 */       output.store("Target", UUIDUtil.CODEC, this.finalTarget.getUUID()); 
/*  80 */     output.storeNullable("Dir", Direction.LEGACY_ID_CODEC, this.currentMoveDirection);
/*  81 */     output.putInt("Steps", this.flightSteps);
/*  82 */     output.putDouble("TXD", this.targetDeltaX);
/*  83 */     output.putDouble("TYD", this.targetDeltaY);
/*  84 */     output.putDouble("TZD", this.targetDeltaZ);
/*     */   }
/*     */   
/*     */   protected void readAdditionalSaveData(ValueInput input) {
/*  89 */     super.readAdditionalSaveData(input);
/*  90 */     this.flightSteps = input.getIntOr("Steps", 0);
/*  91 */     this.targetDeltaX = input.getDoubleOr("TXD", 0.0D);
/*  92 */     this.targetDeltaY = input.getDoubleOr("TYD", 0.0D);
/*  93 */     this.targetDeltaZ = input.getDoubleOr("TZD", 0.0D);
/*  94 */     this.currentMoveDirection = input.read("Dir", Direction.LEGACY_ID_CODEC).orElse(null);
/*  95 */     this.finalTarget = EntityReference.read(input, "Target");
/*     */   }
/*     */   
/*     */   protected void defineSynchedData(SynchedEntityData.Builder entityData) {}
/*     */   
/*     */   private Direction getMoveDirection() {
/* 103 */     return this.currentMoveDirection;
/*     */   }
/*     */   
/*     */   private void setMoveDirection(Direction direction) {
/* 107 */     this.currentMoveDirection = direction;
/*     */   }
/*     */   
/*     */   private void selectNextMoveDirection(Direction.Axis avoidAxis, Entity target) {
/*     */     BlockPos targetPos;
/* 112 */     double yOffset = 0.5D;
/* 113 */     if (target == null) {
/* 114 */       targetPos = blockPosition().below();
/*     */     } else {
/* 116 */       yOffset = target.getBbHeight() * 0.5D;
/* 117 */       targetPos = BlockPos.containing(target.getX(), target.getY() + yOffset, target.getZ());
/*     */     } 
/* 120 */     double targetX = targetPos.getX() + 0.5D;
/* 121 */     double targetY = targetPos.getY() + yOffset;
/* 122 */     double targetZ = targetPos.getZ() + 0.5D;
/* 124 */     Direction selection = null;
/* 125 */     if (!targetPos.closerToCenterThan((Position)position(), 2.0D)) {
/* 126 */       BlockPos current = blockPosition();
/* 127 */       List<Direction> options = Lists.newArrayList();
/* 129 */       if (avoidAxis != Direction.Axis.X)
/* 130 */         if (current.getX() < targetPos.getX() && level().isEmptyBlock(current.east())) {
/* 131 */           options.add(Direction.EAST);
/* 132 */         } else if (current.getX() > targetPos.getX() && level().isEmptyBlock(current.west())) {
/* 133 */           options.add(Direction.WEST);
/*     */         }  
/* 136 */       if (avoidAxis != Direction.Axis.Y)
/* 137 */         if (current.getY() < targetPos.getY() && level().isEmptyBlock(current.above())) {
/* 138 */           options.add(Direction.UP);
/* 139 */         } else if (current.getY() > targetPos.getY() && level().isEmptyBlock(current.below())) {
/* 140 */           options.add(Direction.DOWN);
/*     */         }  
/* 143 */       if (avoidAxis != Direction.Axis.Z)
/* 144 */         if (current.getZ() < targetPos.getZ() && level().isEmptyBlock(current.south())) {
/* 145 */           options.add(Direction.SOUTH);
/* 146 */         } else if (current.getZ() > targetPos.getZ() && level().isEmptyBlock(current.north())) {
/* 147 */           options.add(Direction.NORTH);
/*     */         }  
/* 151 */       selection = Direction.getRandom(this.random);
/* 152 */       if (options.isEmpty()) {
/* 153 */         int attempts = 5;
/* 154 */         while (!level().isEmptyBlock(current.relative(selection)) && attempts > 0) {
/* 155 */           selection = Direction.getRandom(this.random);
/* 156 */           attempts--;
/*     */         } 
/*     */       } else {
/* 159 */         selection = options.get(this.random.nextInt(options.size()));
/*     */       } 
/* 162 */       targetX = getX() + selection.getStepX();
/* 163 */       targetY = getY() + selection.getStepY();
/* 164 */       targetZ = getZ() + selection.getStepZ();
/*     */     } 
/* 167 */     setMoveDirection(selection);
/* 169 */     double xa = targetX - getX();
/* 170 */     double ya = targetY - getY();
/* 171 */     double za = targetZ - getZ();
/* 173 */     double distance = Math.sqrt(xa * xa + ya * ya + za * za);
/* 174 */     if (distance == 0.0D) {
/* 175 */       this.targetDeltaX = 0.0D;
/* 176 */       this.targetDeltaY = 0.0D;
/* 177 */       this.targetDeltaZ = 0.0D;
/*     */     } else {
/* 179 */       this.targetDeltaX = xa / distance * 0.15D;
/* 180 */       this.targetDeltaY = ya / distance * 0.15D;
/* 181 */       this.targetDeltaZ = za / distance * 0.15D;
/*     */     } 
/* 184 */     this.needsSync = true;
/* 185 */     this.flightSteps = 10 + this.random.nextInt(5) * 10;
/*     */   }
/*     */   
/*     */   public void checkDespawn() {
/* 190 */     if (level().getDifficulty() == Difficulty.PEACEFUL)
/* 191 */       discard(); 
/*     */   }
/*     */   
/*     */   protected double getDefaultGravity() {
/* 197 */     return 0.04D;
/*     */   }
/*     */   
/*     */   public void tick() {
/* 202 */     super.tick();
/* 204 */     Entity finalTarget = !level().isClientSide() ? EntityReference.getEntity(this.finalTarget, level()) : null;
/* 205 */     HitResult hitResult = null;
/* 206 */     if (!level().isClientSide()) {
/* 207 */       if (finalTarget == null)
/* 208 */         this.finalTarget = null; 
/* 211 */       if (finalTarget != null && finalTarget.isAlive() && (!(finalTarget instanceof net.minecraft.world.entity.player.Player) || !finalTarget.isSpectator())) {
/* 212 */         this.targetDeltaX = Mth.clamp(this.targetDeltaX * 1.025D, -1.0D, 1.0D);
/* 213 */         this.targetDeltaY = Mth.clamp(this.targetDeltaY * 1.025D, -1.0D, 1.0D);
/* 214 */         this.targetDeltaZ = Mth.clamp(this.targetDeltaZ * 1.025D, -1.0D, 1.0D);
/* 216 */         Vec3 vec3 = getDeltaMovement();
/* 217 */         setDeltaMovement(vec3.add((this.targetDeltaX - vec3.x) * 0.2D, (this.targetDeltaY - vec3.y) * 0.2D, (this.targetDeltaZ - vec3.z) * 0.2D));
/*     */       } else {
/* 223 */         applyGravity();
/*     */       } 
/* 226 */       hitResult = ProjectileUtil.getHitResultOnMoveVector(this, this::canHitEntity);
/*     */     } 
/* 229 */     Vec3 movement = getDeltaMovement();
/* 230 */     setPos(position().add(movement));
/* 231 */     applyEffectsFromBlocks();
/* 232 */     if (this.portalProcess != null && this.portalProcess.isInsidePortalThisTick())
/* 233 */       handlePortal(); 
/* 236 */     if (hitResult != null && isAlive() && hitResult.getType() != HitResult.Type.MISS)
/* 237 */       hitTargetOrDeflectSelf(hitResult); 
/* 240 */     ProjectileUtil.rotateTowardsMovement(this, 0.5F);
/* 242 */     if (level().isClientSide()) {
/* 243 */       level().addParticle((ParticleOptions)ParticleTypes.END_ROD, getX() - movement.x, getY() - movement.y + 0.15D, getZ() - movement.z, 0.0D, 0.0D, 0.0D);
/* 244 */     } else if (finalTarget != null) {
/* 245 */       if (this.flightSteps > 0) {
/* 246 */         this.flightSteps--;
/* 247 */         if (this.flightSteps == 0)
/* 248 */           selectNextMoveDirection((this.currentMoveDirection == null) ? null : this.currentMoveDirection.getAxis(), finalTarget); 
/*     */       } 
/* 252 */       if (this.currentMoveDirection != null) {
/* 254 */         BlockPos current = blockPosition();
/* 255 */         Direction.Axis axis = this.currentMoveDirection.getAxis();
/* 256 */         if (level().loadedAndEntityCanStandOn(current.relative(this.currentMoveDirection), this)) {
/* 257 */           selectNextMoveDirection(axis, finalTarget);
/*     */         } else {
/* 259 */           BlockPos targetPos = finalTarget.blockPosition();
/* 260 */           if ((axis == Direction.Axis.X && current.getX() == targetPos.getX()) || (axis == Direction.Axis.Z && current
/* 261 */             .getZ() == targetPos.getZ()) || (axis == Direction.Axis.Y && current
/* 262 */             .getY() == targetPos.getY()))
/* 264 */             selectNextMoveDirection(axis, finalTarget); 
/*     */         } 
/*     */       } 
/*     */     } 
/*     */   }
/*     */   
/*     */   protected boolean isAffectedByBlocks() {
/* 274 */     return !isRemoved();
/*     */   }
/*     */   
/*     */   protected boolean canHitEntity(Entity entity) {
/* 279 */     return (super.canHitEntity(entity) && !entity.noPhysics);
/*     */   }
/*     */   
/*     */   public boolean isOnFire() {
/* 284 */     return false;
/*     */   }
/*     */   
/*     */   public boolean shouldRenderAtSqrDistance(double distance) {
/* 289 */     return (distance < 16384.0D);
/*     */   }
/*     */   
/*     */   public float getLightLevelDependentMagicValue() {
/* 294 */     return 1.0F;
/*     */   }
/*     */   
/*     */   protected void onHitEntity(EntityHitResult hitResult) {
/* 299 */     super.onHitEntity(hitResult);
/* 300 */     Entity target = hitResult.getEntity();
/* 301 */     Entity owner = getOwner();
/* 302 */     LivingEntity livingEntity = (LivingEntity)owner, livingOwner = (owner instanceof LivingEntity) ? livingEntity : null;
/* 303 */     DamageSource damageSource = damageSources().mobProjectile(this, livingOwner);
/* 304 */     boolean wasHurt = target.hurtOrSimulate(damageSource, 4.0F);
/* 305 */     if (wasHurt) {
/* 306 */       Level level = level();
/* 306 */       if (level instanceof ServerLevel) {
/* 306 */         ServerLevel serverLevel = (ServerLevel)level;
/* 307 */         EnchantmentHelper.doPostAttackEffects(serverLevel, target, damageSource);
/*     */       } 
/* 309 */       if (target instanceof LivingEntity) {
/* 309 */         LivingEntity livingTarget = (LivingEntity)target;
/* 310 */         livingTarget.addEffect(new MobEffectInstance(MobEffects.LEVITATION, 200), (Entity)MoreObjects.firstNonNull(owner, this));
/*     */       } 
/*     */     } 
/*     */   }
/*     */   
/*     */   protected void onHitBlock(BlockHitResult hitResult) {
/* 317 */     super.onHitBlock(hitResult);
/* 318 */     ((ServerLevel)level()).sendParticles((ParticleOptions)ParticleTypes.EXPLOSION, getX(), getY(), getZ(), 2, 0.2D, 0.2D, 0.2D, 0.0D);
/* 319 */     playSound(SoundEvents.SHULKER_BULLET_HIT, 1.0F, 1.0F);
/*     */   }
/*     */   
/*     */   private void destroy() {
/* 323 */     discard();
/* 324 */     level().gameEvent((Holder)GameEvent.ENTITY_DAMAGE, position(), GameEvent.Context.of(this));
/*     */   }
/*     */   
/*     */   protected void onHit(HitResult hitResult) {
/* 329 */     super.onHit(hitResult);
/* 330 */     destroy();
/*     */   }
/*     */   
/*     */   public boolean isPickable() {
/* 335 */     return true;
/*     */   }
/*     */   
/*     */   public boolean hurtClient(DamageSource source) {
/* 340 */     return true;
/*     */   }
/*     */   
/*     */   public boolean hurtServer(ServerLevel level, DamageSource source, float damage) {
/* 345 */     playSound(SoundEvents.SHULKER_BULLET_HURT, 1.0F, 1.0F);
/* 346 */     level.sendParticles((ParticleOptions)ParticleTypes.CRIT, getX(), getY(), getZ(), 15, 0.2D, 0.2D, 0.2D, 0.0D);
/* 347 */     destroy();
/* 348 */     return true;
/*     */   }
/*     */   
/*     */   public void recreateFromPacket(ClientboundAddEntityPacket packet) {
/* 353 */     super.recreateFromPacket(packet);
/* 354 */     setDeltaMovement(packet.getMovement());
/*     */   }
/*     */ }


/* Location:              C:\Users\karol\AppData\Roaming\ModrinthApp\meta\versions\26.2-0.19.5\26.2-0.19.5.jar!\net\minecraft\world\entity\projectile\ShulkerBullet.class
 * Java compiler version: 25 (69.0)
 * JD-Core Version:       1.1.3
 */