/*     */ package net.minecraft.world.entity.projectile.arrow;
/*     */ 
/*     */ import java.util.Collection;
/*     */ import java.util.List;
/*     */ import net.minecraft.network.syncher.EntityDataAccessor;
/*     */ import net.minecraft.network.syncher.EntityDataSerializers;
/*     */ import net.minecraft.network.syncher.SynchedEntityData;
/*     */ import net.minecraft.server.level.ServerLevel;
/*     */ import net.minecraft.sounds.SoundEvent;
/*     */ import net.minecraft.sounds.SoundEvents;
/*     */ import net.minecraft.util.Mth;
/*     */ import net.minecraft.world.damagesource.DamageSource;
/*     */ import net.minecraft.world.entity.Entity;
/*     */ import net.minecraft.world.entity.EntityType;
/*     */ import net.minecraft.world.entity.EntityTypes;
/*     */ import net.minecraft.world.entity.LivingEntity;
/*     */ import net.minecraft.world.entity.player.Player;
/*     */ import net.minecraft.world.entity.projectile.ProjectileDeflection;
/*     */ import net.minecraft.world.item.Item;
/*     */ import net.minecraft.world.item.ItemStack;
/*     */ import net.minecraft.world.item.Items;
/*     */ import net.minecraft.world.item.enchantment.EnchantmentHelper;
/*     */ import net.minecraft.world.level.ItemLike;
/*     */ import net.minecraft.world.level.Level;
/*     */ import net.minecraft.world.level.storage.ValueInput;
/*     */ import net.minecraft.world.level.storage.ValueOutput;
/*     */ import net.minecraft.world.phys.BlockHitResult;
/*     */ import net.minecraft.world.phys.EntityHitResult;
/*     */ import net.minecraft.world.phys.Vec3;
/*     */ 
/*     */ public class ThrownTrident extends AbstractArrow {
/*  34 */   private static final EntityDataAccessor<Byte> ID_LOYALTY = SynchedEntityData.defineId(ThrownTrident.class, EntityDataSerializers.BYTE);
/*     */   
/*  35 */   private static final EntityDataAccessor<Boolean> ID_FOIL = SynchedEntityData.defineId(ThrownTrident.class, EntityDataSerializers.BOOLEAN);
/*     */   
/*     */   private static final float WATER_INERTIA = 0.99F;
/*     */   
/*     */   private static final boolean DEFAULT_DEALT_DAMAGE = false;
/*     */   
/*     */   private boolean dealtDamage = false;
/*     */   
/*     */   public int clientSideReturnTridentTickCount;
/*     */   
/*     */   public ThrownTrident(EntityType<? extends ThrownTrident> type, Level level) {
/*  44 */     super((EntityType)type, level);
/*     */   }
/*     */   
/*     */   public ThrownTrident(Level level, LivingEntity owner, ItemStack tridentItem) {
/*  48 */     super(EntityTypes.TRIDENT, owner, level, tridentItem, (ItemStack)null);
/*  49 */     this.entityData.set(ID_LOYALTY, Byte.valueOf(getLoyaltyFromItem(tridentItem)));
/*  50 */     this.entityData.set(ID_FOIL, Boolean.valueOf(tridentItem.hasFoil()));
/*     */   }
/*     */   
/*     */   public ThrownTrident(Level level, double x, double y, double z, ItemStack tridentItem) {
/*  54 */     super(EntityTypes.TRIDENT, x, y, z, level, tridentItem, tridentItem);
/*  55 */     this.entityData.set(ID_LOYALTY, Byte.valueOf(getLoyaltyFromItem(tridentItem)));
/*  56 */     this.entityData.set(ID_FOIL, Boolean.valueOf(tridentItem.hasFoil()));
/*     */   }
/*     */   
/*     */   protected void defineSynchedData(SynchedEntityData.Builder entityData) {
/*  61 */     super.defineSynchedData(entityData);
/*  63 */     entityData.define(ID_LOYALTY, Byte.valueOf((byte)0));
/*  64 */     entityData.define(ID_FOIL, Boolean.valueOf(false));
/*     */   }
/*     */   
/*     */   public void tick() {
/*  69 */     if (this.inGroundTime > 4)
/*  70 */       this.dealtDamage = true; 
/*  73 */     Entity currentOwner = getOwner();
/*  74 */     int loyalty = ((Byte)this.entityData.get(ID_LOYALTY)).byteValue();
/*  76 */     if (loyalty > 0 && (this.dealtDamage || isNoPhysics()) && currentOwner != null)
/*  77 */       if (!isAcceptibleReturnOwner()) {
/*  78 */         Level level = level();
/*  78 */         if (level instanceof ServerLevel) {
/*  78 */           ServerLevel serverLevel = (ServerLevel)level;
/*  78 */           if (this.pickup == AbstractArrow.Pickup.ALLOWED)
/*  79 */             spawnAtLocation(serverLevel, getPickupItem(), 0.1F); 
/*     */         } 
/*  81 */         discard();
/*     */       } else {
/*  83 */         if (!(currentOwner instanceof Player) && position().distanceTo(currentOwner.getEyePosition()) < currentOwner.getBbWidth() + 1.0D) {
/*  84 */           discard();
/*     */           return;
/*     */         } 
/*  87 */         setNoPhysics(true);
/*  88 */         Vec3 vec = currentOwner.getEyePosition().subtract(position());
/*  89 */         setPosRaw(getX(), getY() + vec.y * 0.015D * loyalty, getZ());
/*  90 */         double accel = 0.05D * loyalty;
/*  91 */         setDeltaMovement(getDeltaMovement().scale(0.95D).add(vec.normalize().scale(accel)));
/*  93 */         if (this.clientSideReturnTridentTickCount == 0)
/*  94 */           playSound(SoundEvents.TRIDENT_RETURN, 10.0F, 1.0F); 
/*  97 */         this.clientSideReturnTridentTickCount++;
/*     */       }  
/* 101 */     super.tick();
/*     */   }
/*     */   
/*     */   private boolean isAcceptibleReturnOwner() {
/* 105 */     Entity currentOwner = getOwner();
/* 106 */     if (currentOwner == null || !currentOwner.isAlive())
/* 107 */       return false; 
/* 109 */     if (currentOwner instanceof net.minecraft.server.level.ServerPlayer && currentOwner.isSpectator())
/* 110 */       return false; 
/* 112 */     return true;
/*     */   }
/*     */   
/*     */   public boolean isFoil() {
/* 116 */     return ((Boolean)this.entityData.get(ID_FOIL)).booleanValue();
/*     */   }
/*     */   
/*     */   protected EntityHitResult findHitEntity(Vec3 from, Vec3 to) {
/* 121 */     if (this.dealtDamage)
/* 122 */       return null; 
/* 124 */     return super.findHitEntity(from, to);
/*     */   }
/*     */   
/*     */   protected Collection<EntityHitResult> findHitEntities(Vec3 from, Vec3 to) {
/* 129 */     EntityHitResult e = findHitEntity(from, to);
/* 130 */     if (e != null)
/* 131 */       return List.of(e); 
/* 133 */     return List.of();
/*     */   }
/*     */   
/*     */   protected void onHitEntity(EntityHitResult hitResult) {
/* 138 */     Entity entity = hitResult.getEntity();
/* 139 */     float dmg = 8.0F;
/* 141 */     Entity currentOwner = getOwner();
/* 142 */     DamageSource damageSource = damageSources().trident((Entity)this, (currentOwner == null) ? (Entity)this : currentOwner);
/* 143 */     Level level = level();
/* 143 */     if (level instanceof ServerLevel) {
/* 143 */       ServerLevel serverLevel = (ServerLevel)level;
/* 144 */       dmg = EnchantmentHelper.modifyDamage(serverLevel, getWeaponItem(), entity, damageSource, dmg);
/*     */     } 
/* 147 */     this.dealtDamage = true;
/* 149 */     if (entity.hurtOrSimulate(damageSource, dmg)) {
/* 150 */       if (entity.is(EntityTypes.ENDERMAN))
/*     */         return; 
/* 153 */       level = level();
/* 153 */       if (level instanceof ServerLevel) {
/* 153 */         ServerLevel serverLevel = (ServerLevel)level;
/* 154 */         EnchantmentHelper.doPostAttackEffectsWithItemSourceOnBreak(serverLevel, entity, damageSource, getWeaponItem(), weapon -> kill(serverLevel));
/*     */       } 
/* 156 */       if (entity instanceof LivingEntity) {
/* 156 */         LivingEntity mob = (LivingEntity)entity;
/* 157 */         doKnockback(mob, damageSource);
/* 158 */         doPostHurtEffects(mob);
/*     */       } 
/*     */     } 
/* 161 */     deflect(ProjectileDeflection.REVERSE, entity, this.owner, false);
/* 162 */     setDeltaMovement(getDeltaMovement().multiply(0.02D, 0.2D, 0.02D));
/* 164 */     playSound(SoundEvents.TRIDENT_HIT, 1.0F, 1.0F);
/*     */   }
/*     */   
/*     */   protected void hitBlockEnchantmentEffects(ServerLevel level, BlockHitResult hitResult, ItemStack weapon) {
/* 169 */     Vec3 compensatedHitPosition = hitResult.getBlockPos().clampLocationWithin(hitResult.getLocation());
/* 170 */     Entity entity = getOwner();
/* 170 */     LivingEntity livingOwner = (LivingEntity)entity;
/* 170 */     EnchantmentHelper.onHitBlock(level, weapon, (entity instanceof LivingEntity) ? livingOwner : null, (Entity)this, null, compensatedHitPosition, level.getBlockState(hitResult.getBlockPos()), item -> kill(level));
/*     */   }
/*     */   
/*     */   public ItemStack getWeaponItem() {
/* 175 */     return getPickupItemStackOrigin();
/*     */   }
/*     */   
/*     */   protected boolean tryPickup(Player player) {
/* 180 */     return (super.tryPickup(player) || (isNoPhysics() && ownedBy((Entity)player) && player.getInventory().add(getPickupItem())));
/*     */   }
/*     */   
/*     */   protected ItemStack getDefaultPickupItem() {
/* 185 */     return new ItemStack((ItemLike)Items.TRIDENT);
/*     */   }
/*     */   
/*     */   protected SoundEvent getDefaultHitGroundSoundEvent() {
/* 190 */     return SoundEvents.TRIDENT_HIT_GROUND;
/*     */   }
/*     */   
/*     */   public void playerTouch(Player player) {
/* 195 */     if (ownedBy((Entity)player) || getOwner() == null)
/* 196 */       super.playerTouch(player); 
/*     */   }
/*     */   
/*     */   protected void readAdditionalSaveData(ValueInput input) {
/* 202 */     super.readAdditionalSaveData(input);
/* 204 */     this.dealtDamage = input.getBooleanOr("DealtDamage", false);
/* 206 */     this.entityData.set(ID_LOYALTY, Byte.valueOf(getLoyaltyFromItem(getPickupItemStackOrigin())));
/*     */   }
/*     */   
/*     */   protected void addAdditionalSaveData(ValueOutput output) {
/* 211 */     super.addAdditionalSaveData(output);
/* 213 */     output.putBoolean("DealtDamage", this.dealtDamage);
/*     */   }
/*     */   
/*     */   private byte getLoyaltyFromItem(ItemStack tridentItem) {
/* 217 */     Level level = level();
/* 217 */     if (level instanceof ServerLevel) {
/* 217 */       ServerLevel serverLevel = (ServerLevel)level;
/* 218 */       return (byte)Mth.clamp(EnchantmentHelper.getTridentReturnToOwnerAcceleration(serverLevel, tridentItem, (Entity)this), 0, 127);
/*     */     } 
/* 220 */     return 0;
/*     */   }
/*     */   
/*     */   public void tickDespawn() {
/* 225 */     int loyalty = ((Byte)this.entityData.get(ID_LOYALTY)).byteValue();
/* 227 */     if (this.pickup != AbstractArrow.Pickup.ALLOWED || loyalty <= 0)
/* 228 */       super.tickDespawn(); 
/*     */   }
/*     */   
/*     */   protected float getWaterInertia() {
/* 234 */     return 0.99F;
/*     */   }
/*     */   
/*     */   public boolean shouldRender(double camX, double camY, double camZ) {
/* 239 */     return true;
/*     */   }
/*     */ }


/* Location:              C:\Users\karol\AppData\Roaming\ModrinthApp\meta\versions\26.2-0.19.5\26.2-0.19.5.jar!\net\minecraft\world\entity\projectile\arrow\ThrownTrident.class
 * Java compiler version: 25 (69.0)
 * JD-Core Version:       1.1.3
 */