/*     */ package net.minecraft.world.entity.projectile.hurtingprojectile.windcharge;
/*     */ 
/*     */ import java.util.Optional;
/*     */ import java.util.function.Function;
/*     */ import net.minecraft.core.Vec3i;
/*     */ import net.minecraft.core.particles.ParticleOptions;
/*     */ import net.minecraft.core.registries.BuiltInRegistries;
/*     */ import net.minecraft.server.level.ServerLevel;
/*     */ import net.minecraft.tags.BlockTags;
/*     */ import net.minecraft.world.damagesource.DamageSource;
/*     */ import net.minecraft.world.entity.Entity;
/*     */ import net.minecraft.world.entity.EntityType;
/*     */ import net.minecraft.world.entity.EntityTypes;
/*     */ import net.minecraft.world.entity.LivingEntity;
/*     */ import net.minecraft.world.entity.projectile.ItemSupplier;
/*     */ import net.minecraft.world.entity.projectile.hurtingprojectile.AbstractHurtingProjectile;
/*     */ import net.minecraft.world.item.ItemStack;
/*     */ import net.minecraft.world.item.enchantment.EnchantmentHelper;
/*     */ import net.minecraft.world.level.ExplosionDamageCalculator;
/*     */ import net.minecraft.world.level.Level;
/*     */ import net.minecraft.world.level.SimpleExplosionDamageCalculator;
/*     */ import net.minecraft.world.phys.AABB;
/*     */ import net.minecraft.world.phys.BlockHitResult;
/*     */ import net.minecraft.world.phys.EntityHitResult;
/*     */ import net.minecraft.world.phys.HitResult;
/*     */ import net.minecraft.world.phys.Vec3;
/*     */ 
/*     */ public abstract class AbstractWindCharge extends AbstractHurtingProjectile implements ItemSupplier {
/*  31 */   public static final ExplosionDamageCalculator EXPLOSION_DAMAGE_CALCULATOR = (ExplosionDamageCalculator)new SimpleExplosionDamageCalculator(true, false, 
/*     */       
/*  34 */       Optional.empty(), BuiltInRegistries.BLOCK
/*  35 */       .get(BlockTags.BLOCKS_WIND_CHARGE_EXPLOSIONS).map(Function.identity()));
/*     */   
/*     */   public static final double JUMP_SCALE = 0.25D;
/*     */   
/*     */   public AbstractWindCharge(EntityType<? extends AbstractWindCharge> type, Level level) {
/*  41 */     super(type, level);
/*  42 */     this.accelerationPower = 0.0D;
/*     */   }
/*     */   
/*     */   public AbstractWindCharge(EntityType<? extends AbstractWindCharge> type, Level level, Entity owner, double x, double y, double z) {
/*  46 */     super(type, x, y, z, level);
/*  47 */     setOwner(owner);
/*  48 */     this.accelerationPower = 0.0D;
/*     */   }
/*     */   
/*     */   protected AbstractWindCharge(EntityType<? extends AbstractWindCharge> type, double x, double y, double z, Vec3 direction, Level level) {
/*  52 */     super(type, x, y, z, direction, level);
/*  53 */     this.accelerationPower = 0.0D;
/*     */   }
/*     */   
/*     */   protected AABB makeBoundingBox(Vec3 position) {
/*  58 */     float width = getType().getDimensions().width() / 2.0F;
/*  59 */     float height = getType().getDimensions().height();
/*  60 */     float offset = 0.15F;
/*  62 */     return new AABB(position.x - width, position.y - 0.15000000596046448D, position.z - width, position.x + width, position.y - 0.15000000596046448D + height, position.z + width);
/*     */   }
/*     */   
/*     */   public boolean canCollideWith(Entity entity) {
/*  67 */     if (entity instanceof AbstractWindCharge)
/*  68 */       return false; 
/*  70 */     return super.canCollideWith(entity);
/*     */   }
/*     */   
/*     */   protected boolean canHitEntity(Entity entity) {
/*  75 */     if (entity instanceof AbstractWindCharge)
/*  76 */       return false; 
/*  79 */     if (entity.is(EntityTypes.END_CRYSTAL))
/*  80 */       return false; 
/*  83 */     return super.canHitEntity(entity);
/*     */   }
/*     */   
/*     */   protected void onHitEntity(EntityHitResult hitResult) {
/*     */     ServerLevel serverLevel;
/*  88 */     super.onHitEntity(hitResult);
/*  89 */     Level level = level();
/*  89 */     if (level instanceof ServerLevel) {
/*  89 */       serverLevel = (ServerLevel)level;
/*     */     } else {
/*     */       return;
/*     */     } 
/*  93 */     Entity entity1 = getOwner();
/*  93 */     LivingEntity livingEntity1 = (LivingEntity)entity1, owner = (entity1 instanceof LivingEntity) ? livingEntity1 : null;
/*  94 */     Entity entity = hitResult.getEntity();
/*  96 */     if (owner != null)
/*  97 */       owner.setLastHurtMob(entity); 
/* 100 */     DamageSource source = damageSources().windCharge((Entity)this, owner);
/* 101 */     if (entity.hurtServer(serverLevel, source, 1.0F) && 
/* 102 */       entity instanceof LivingEntity) {
/* 102 */       LivingEntity mob = (LivingEntity)entity;
/* 103 */       EnchantmentHelper.doPostAttackEffects(serverLevel, (Entity)mob, source);
/*     */     } 
/* 106 */     explode(position());
/*     */   }
/*     */   
/*     */   public void push(double xa, double ya, double za) {}
/*     */   
/*     */   protected abstract void explode(Vec3 paramVec3);
/*     */   
/*     */   protected void onHitBlock(BlockHitResult hitResult) {
/* 118 */     super.onHitBlock(hitResult);
/* 120 */     if (!level().isClientSide()) {
/* 121 */       Vec3i collisionNormal = hitResult.getDirection().getUnitVec3i();
/* 122 */       Vec3 scaledNormal = Vec3.atLowerCornerOf(collisionNormal).multiply(0.25D, 0.25D, 0.25D);
/* 123 */       Vec3 explosionPos = hitResult.getLocation().add(scaledNormal);
/* 125 */       explode(explosionPos);
/* 126 */       discard();
/*     */     } 
/*     */   }
/*     */   
/*     */   protected void onHit(HitResult hitResult) {
/* 132 */     super.onHit(hitResult);
/* 133 */     if (!level().isClientSide())
/* 134 */       discard(); 
/*     */   }
/*     */   
/*     */   protected boolean shouldBurn() {
/* 140 */     return false;
/*     */   }
/*     */   
/*     */   public ItemStack getItem() {
/* 145 */     return ItemStack.EMPTY;
/*     */   }
/*     */   
/*     */   protected float getInertia() {
/* 152 */     return 1.0F;
/*     */   }
/*     */   
/*     */   protected float getLiquidInertia() {
/* 157 */     return getInertia();
/*     */   }
/*     */   
/*     */   protected ParticleOptions getTrailParticle() {
/* 162 */     return null;
/*     */   }
/*     */   
/*     */   public void tick() {
/* 167 */     if (!level().isClientSide() && getBlockY() > level().getMaxY() + 30) {
/* 168 */       explode(position());
/* 169 */       discard();
/*     */     } else {
/* 171 */       super.tick();
/*     */     } 
/*     */   }
/*     */ }


/* Location:              C:\Users\karol\AppData\Roaming\ModrinthApp\meta\versions\26.2-0.19.5\26.2-0.19.5.jar!\net\minecraft\world\entity\projectile\hurtingprojectile\windcharge\AbstractWindCharge.class
 * Java compiler version: 25 (69.0)
 * JD-Core Version:       1.1.3
 */