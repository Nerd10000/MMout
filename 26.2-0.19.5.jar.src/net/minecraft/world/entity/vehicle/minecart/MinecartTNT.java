/*     */ package net.minecraft.world.entity.vehicle.minecart;
/*     */ 
/*     */ import net.minecraft.core.BlockPos;
/*     */ import net.minecraft.core.particles.ParticleOptions;
/*     */ import net.minecraft.core.particles.ParticleTypes;
/*     */ import net.minecraft.server.level.ServerLevel;
/*     */ import net.minecraft.sounds.SoundEvents;
/*     */ import net.minecraft.sounds.SoundSource;
/*     */ import net.minecraft.tags.BlockTags;
/*     */ import net.minecraft.tags.DamageTypeTags;
/*     */ import net.minecraft.util.Mth;
/*     */ import net.minecraft.world.damagesource.DamageSource;
/*     */ import net.minecraft.world.entity.Entity;
/*     */ import net.minecraft.world.entity.EntityType;
/*     */ import net.minecraft.world.entity.projectile.Projectile;
/*     */ import net.minecraft.world.entity.projectile.arrow.AbstractArrow;
/*     */ import net.minecraft.world.item.Item;
/*     */ import net.minecraft.world.item.ItemStack;
/*     */ import net.minecraft.world.item.Items;
/*     */ import net.minecraft.world.level.BlockGetter;
/*     */ import net.minecraft.world.level.Explosion;
/*     */ import net.minecraft.world.level.ItemLike;
/*     */ import net.minecraft.world.level.Level;
/*     */ import net.minecraft.world.level.block.Blocks;
/*     */ import net.minecraft.world.level.block.state.BlockState;
/*     */ import net.minecraft.world.level.gamerules.GameRules;
/*     */ import net.minecraft.world.level.material.FluidState;
/*     */ import net.minecraft.world.level.storage.ValueInput;
/*     */ import net.minecraft.world.level.storage.ValueOutput;
/*     */ 
/*     */ public class MinecartTNT extends AbstractMinecart {
/*     */   private static final String TAG_EXPLOSION_POWER = "explosion_power";
/*     */   
/*     */   private static final String TAG_EXPLOSION_SPEED_FACTOR = "explosion_speed_factor";
/*     */   
/*     */   private static final float DEFAULT_EXPLOSION_POWER_BASE = 4.0F;
/*     */   
/*     */   private static final float DEFAULT_EXPLOSION_SPEED_FACTOR = 1.0F;
/*     */   
/*     */   private DamageSource ignitionSource;
/*     */   
/*  39 */   private int fuse = -1;
/*     */   
/*  40 */   private float explosionPowerBase = 4.0F;
/*     */   
/*  41 */   private float explosionSpeedFactor = 1.0F;
/*     */   
/*     */   public MinecartTNT(EntityType<? extends MinecartTNT> type, Level level) {
/*  44 */     super(type, level);
/*     */   }
/*     */   
/*     */   public BlockState getDefaultDisplayBlockState() {
/*  49 */     return Blocks.TNT.defaultBlockState();
/*     */   }
/*     */   
/*     */   public void tick() {
/*  54 */     super.tick();
/*  56 */     if (this.fuse > 0) {
/*  57 */       this.fuse--;
/*  58 */       level().addParticle((ParticleOptions)ParticleTypes.SMOKE, getX(), getY() + 0.5D, getZ(), 0.0D, 0.0D, 0.0D);
/*  59 */     } else if (this.fuse == 0) {
/*  60 */       explode(this.ignitionSource, getDeltaMovement().horizontalDistanceSqr());
/*     */     } 
/*  63 */     if (this.horizontalCollision) {
/*  64 */       double speedSqr = getDeltaMovement().horizontalDistanceSqr();
/*  65 */       if (speedSqr >= 0.009999999776482582D)
/*  66 */         explode(this.ignitionSource, speedSqr); 
/*     */     } 
/*     */   }
/*     */   
/*     */   public boolean hurtServer(ServerLevel level, DamageSource source, float damage) {
/*  73 */     Entity sourceEntity = source.getDirectEntity();
/*  75 */     if (sourceEntity instanceof AbstractArrow) {
/*  75 */       AbstractArrow projectile = (AbstractArrow)sourceEntity;
/*  75 */       if (projectile.isOnFire()) {
/*  76 */         DamageSource damageSource = damageSources().explosion((Entity)this, source.getEntity());
/*  77 */         explode(damageSource, projectile.getDeltaMovement().lengthSqr());
/*     */       } 
/*     */     } 
/*  79 */     return super.hurtServer(level, source, damage);
/*     */   }
/*     */   
/*     */   public void destroy(ServerLevel level, DamageSource source) {
/*  84 */     double speedSqr = getDeltaMovement().horizontalDistanceSqr();
/*  86 */     if (damageSourceIgnitesTnt(source) || speedSqr >= 0.009999999776482582D) {
/*  87 */       if (this.fuse < 0) {
/*  88 */         primeFuse(source);
/*  89 */         this.fuse = this.random.nextInt(20) + this.random.nextInt(20);
/*     */       } 
/*     */       return;
/*     */     } 
/*  93 */     destroy(level, getDropItem());
/*     */   }
/*     */   
/*     */   protected Item getDropItem() {
/*  98 */     return Items.TNT_MINECART;
/*     */   }
/*     */   
/*     */   public ItemStack getPickResult() {
/* 103 */     return new ItemStack((ItemLike)Items.TNT_MINECART);
/*     */   }
/*     */   
/*     */   protected void explode(DamageSource damageSource, double speedSqr) {
/* 107 */     Level level = level();
/* 107 */     if (level instanceof ServerLevel) {
/* 107 */       ServerLevel serverLevel = (ServerLevel)level;
/* 108 */       if (((Boolean)serverLevel.getGameRules().get(GameRules.TNT_EXPLODES)).booleanValue()) {
/* 109 */         double speed = Math.min(Math.sqrt(speedSqr), 5.0D);
/* 110 */         serverLevel.explode((Entity)this, damageSource, null, getX(), getY(), getZ(), (float)(this.explosionPowerBase + this.explosionSpeedFactor * this.random.nextDouble() * 1.5D * speed), false, Level.ExplosionInteraction.TNT);
/* 111 */         discard();
/* 112 */       } else if (isPrimed()) {
/* 113 */         discard();
/*     */       } 
/*     */     } 
/*     */   }
/*     */   
/*     */   public boolean causeFallDamage(double fallDistance, float damageModifier, DamageSource damageSource) {
/* 120 */     if (fallDistance >= 3.0D) {
/* 121 */       double power = fallDistance / 10.0D;
/* 122 */       explode(this.ignitionSource, power * power);
/*     */     } 
/* 125 */     return super.causeFallDamage(fallDistance, damageModifier, damageSource);
/*     */   }
/*     */   
/*     */   public void activateMinecart(ServerLevel level, int xt, int yt, int zt, boolean state) {
/* 130 */     if (state && this.fuse < 0)
/* 131 */       primeFuse((DamageSource)null); 
/*     */   }
/*     */   
/*     */   public void handleEntityEvent(byte id) {
/* 137 */     if (id == 70) {
/* 138 */       primeFuse((DamageSource)null);
/*     */     } else {
/* 140 */       super.handleEntityEvent(id);
/*     */     } 
/*     */   }
/*     */   
/*     */   public void primeFuse(DamageSource source) {
/* 145 */     Level level = level();
/* 145 */     if (level instanceof ServerLevel) {
/* 145 */       ServerLevel serverLevel = (ServerLevel)level;
/* 145 */       if (!((Boolean)serverLevel.getGameRules().get(GameRules.TNT_EXPLODES)).booleanValue())
/*     */         return; 
/*     */     } 
/* 149 */     this.fuse = 80;
/* 151 */     if (!level().isClientSide()) {
/* 152 */       if (source != null && this.ignitionSource == null)
/* 153 */         this.ignitionSource = damageSources().explosion((Entity)this, source.getEntity()); 
/* 155 */       level().broadcastEntityEvent((Entity)this, (byte)70);
/* 156 */       if (!isSilent())
/* 157 */         level().playSound(null, getX(), getY(), getZ(), SoundEvents.TNT_PRIMED, SoundSource.BLOCKS, 1.0F, 1.0F); 
/*     */     } 
/*     */   }
/*     */   
/*     */   public int getFuse() {
/* 163 */     return this.fuse;
/*     */   }
/*     */   
/*     */   public boolean isPrimed() {
/* 167 */     return (this.fuse > -1);
/*     */   }
/*     */   
/*     */   public float getBlockExplosionResistance(Explosion explosion, BlockGetter level, BlockPos pos, BlockState block, FluidState fluid, float resistance) {
/* 172 */     if (isPrimed() && (block.is(BlockTags.RAILS) || level.getBlockState(pos.above()).is(BlockTags.RAILS)))
/* 173 */       return 0.0F; 
/* 176 */     return super.getBlockExplosionResistance(explosion, level, pos, block, fluid, resistance);
/*     */   }
/*     */   
/*     */   public boolean shouldBlockExplode(Explosion explosion, BlockGetter level, BlockPos pos, BlockState state, float power) {
/* 181 */     if (isPrimed() && (state.is(BlockTags.RAILS) || level.getBlockState(pos.above()).is(BlockTags.RAILS)))
/* 182 */       return false; 
/* 185 */     return super.shouldBlockExplode(explosion, level, pos, state, power);
/*     */   }
/*     */   
/*     */   protected void readAdditionalSaveData(ValueInput input) {
/* 190 */     super.readAdditionalSaveData(input);
/* 191 */     this.fuse = input.getIntOr("fuse", -1);
/* 192 */     this.explosionPowerBase = Mth.clamp(input.getFloatOr("explosion_power", 4.0F), 0.0F, 128.0F);
/* 193 */     this.explosionSpeedFactor = Mth.clamp(input.getFloatOr("explosion_speed_factor", 1.0F), 0.0F, 128.0F);
/*     */   }
/*     */   
/*     */   protected void addAdditionalSaveData(ValueOutput output) {
/* 198 */     super.addAdditionalSaveData(output);
/* 199 */     output.putInt("fuse", this.fuse);
/* 201 */     if (this.explosionPowerBase != 4.0F)
/* 202 */       output.putFloat("explosion_power", this.explosionPowerBase); 
/* 204 */     if (this.explosionSpeedFactor != 1.0F)
/* 205 */       output.putFloat("explosion_speed_factor", this.explosionSpeedFactor); 
/*     */   }
/*     */   
/*     */   protected boolean shouldSourceDestroy(DamageSource source) {
/* 211 */     return damageSourceIgnitesTnt(source);
/*     */   }
/*     */   
/*     */   private static boolean damageSourceIgnitesTnt(DamageSource source) {
/* 215 */     Entity entity = source.getDirectEntity();
/* 215 */     if (entity instanceof Projectile) {
/* 215 */       Projectile projectile = (Projectile)entity;
/* 216 */       return projectile.isOnFire();
/*     */     } 
/* 218 */     return (source.is(DamageTypeTags.IS_FIRE) || source.is(DamageTypeTags.IS_EXPLOSION));
/*     */   }
/*     */ }


/* Location:              C:\Users\karol\AppData\Roaming\ModrinthApp\meta\versions\26.2-0.19.5\26.2-0.19.5.jar!\net\minecraft\world\entity\vehicle\minecart\MinecartTNT.class
 * Java compiler version: 25 (69.0)
 * JD-Core Version:       1.1.3
 */