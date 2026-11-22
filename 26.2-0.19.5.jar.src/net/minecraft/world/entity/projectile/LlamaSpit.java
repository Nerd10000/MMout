/*     */ package net.minecraft.world.entity.projectile;
/*     */ 
/*     */ import net.minecraft.core.particles.ParticleOptions;
/*     */ import net.minecraft.core.particles.ParticleTypes;
/*     */ import net.minecraft.network.protocol.game.ClientboundAddEntityPacket;
/*     */ import net.minecraft.network.syncher.SynchedEntityData;
/*     */ import net.minecraft.server.level.ServerLevel;
/*     */ import net.minecraft.util.Mth;
/*     */ import net.minecraft.world.damagesource.DamageSource;
/*     */ import net.minecraft.world.entity.Entity;
/*     */ import net.minecraft.world.entity.EntityType;
/*     */ import net.minecraft.world.entity.EntityTypes;
/*     */ import net.minecraft.world.entity.LivingEntity;
/*     */ import net.minecraft.world.entity.animal.equine.Llama;
/*     */ import net.minecraft.world.item.enchantment.EnchantmentHelper;
/*     */ import net.minecraft.world.level.Level;
/*     */ import net.minecraft.world.level.block.state.BlockBehaviour;
/*     */ import net.minecraft.world.phys.BlockHitResult;
/*     */ import net.minecraft.world.phys.EntityHitResult;
/*     */ import net.minecraft.world.phys.HitResult;
/*     */ import net.minecraft.world.phys.Vec3;
/*     */ 
/*     */ public class LlamaSpit extends Projectile {
/*     */   public LlamaSpit(EntityType<? extends LlamaSpit> type, Level level) {
/*  24 */     super((EntityType)type, level);
/*     */   }
/*     */   
/*     */   public LlamaSpit(Level level, Llama owner) {
/*  28 */     this(EntityTypes.LLAMA_SPIT, level);
/*  29 */     setOwner((Entity)owner);
/*  30 */     setPos(owner.getX() - (owner.getBbWidth() + 1.0F) * 0.5D * Mth.sin((owner.yBodyRot * 0.017453292F)), owner.getEyeY() - 0.10000000149011612D, owner.getZ() + (owner.getBbWidth() + 1.0F) * 0.5D * Mth.cos((owner.yBodyRot * 0.017453292F)));
/*     */   }
/*     */   
/*     */   protected double getDefaultGravity() {
/*  35 */     return 0.06D;
/*     */   }
/*     */   
/*     */   public void tick() {
/*  40 */     super.tick();
/*  42 */     Vec3 movement = getDeltaMovement();
/*  43 */     HitResult hitResult = ProjectileUtil.getHitResultOnMoveVector(this, this::canHitEntity);
/*  44 */     hitTargetOrDeflectSelf(hitResult);
/*  46 */     double x = getX() + movement.x;
/*  47 */     double y = getY() + movement.y;
/*  48 */     double z = getZ() + movement.z;
/*  50 */     updateRotation();
/*  52 */     if (level().getBlockStates(getBoundingBox()).noneMatch(BlockBehaviour.BlockStateBase::isAir)) {
/*  53 */       discard();
/*     */       return;
/*     */     } 
/*  57 */     if (isInWater()) {
/*  58 */       discard();
/*     */       return;
/*     */     } 
/*  62 */     setDeltaMovement(movement.scale(getAirDrag()));
/*  63 */     applyGravity();
/*  65 */     setPos(x, y, z);
/*     */   }
/*     */   
/*     */   protected float getAirDrag() {
/*  70 */     return 0.99F;
/*     */   }
/*     */   
/*     */   protected void onHitEntity(EntityHitResult hitResult) {
/*  75 */     super.onHitEntity(hitResult);
/*  76 */     Entity entity = getOwner();
/*  76 */     if (entity instanceof LivingEntity) {
/*  76 */       LivingEntity livingOwner = (LivingEntity)entity;
/*  77 */       Entity target = hitResult.getEntity();
/*  78 */       DamageSource damageSource = damageSources().spit(this, livingOwner);
/*  79 */       Level level = level();
/*  79 */       if (level instanceof ServerLevel) {
/*  79 */         ServerLevel serverLevel = (ServerLevel)level;
/*  80 */         if (target.hurtServer(serverLevel, damageSource, 1.0F))
/*  81 */           EnchantmentHelper.doPostAttackEffects(serverLevel, target, damageSource); 
/*     */       } 
/*     */     } 
/*     */   }
/*     */   
/*     */   protected void onHitBlock(BlockHitResult hitResult) {
/*  89 */     super.onHitBlock(hitResult);
/*  91 */     if (!level().isClientSide())
/*  92 */       discard(); 
/*     */   }
/*     */   
/*     */   protected void defineSynchedData(SynchedEntityData.Builder entityData) {}
/*     */   
/*     */   public void recreateFromPacket(ClientboundAddEntityPacket packet) {
/* 102 */     super.recreateFromPacket(packet);
/* 103 */     Vec3 movement = packet.getMovement();
/* 105 */     for (int i = 0; i < 7; i++) {
/* 106 */       double k = 0.4D + 0.1D * i;
/* 107 */       level().addParticle((ParticleOptions)ParticleTypes.SPIT, getX(), getY(), getZ(), movement.x * k, movement.y, movement.z * k);
/*     */     } 
/* 110 */     setDeltaMovement(movement);
/*     */   }
/*     */ }


/* Location:              C:\Users\karol\AppData\Roaming\ModrinthApp\meta\versions\26.2-0.19.5\26.2-0.19.5.jar!\net\minecraft\world\entity\projectile\LlamaSpit.class
 * Java compiler version: 25 (69.0)
 * JD-Core Version:       1.1.3
 */