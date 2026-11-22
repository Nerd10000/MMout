/*     */ package net.minecraft.world.entity.projectile.arrow;
/*     */ 
/*     */ import net.minecraft.core.component.DataComponents;
/*     */ import net.minecraft.core.particles.ColorParticleOption;
/*     */ import net.minecraft.core.particles.ParticleOptions;
/*     */ import net.minecraft.core.particles.ParticleTypes;
/*     */ import net.minecraft.network.syncher.EntityDataAccessor;
/*     */ import net.minecraft.network.syncher.EntityDataSerializers;
/*     */ import net.minecraft.network.syncher.SynchedEntityData;
/*     */ import net.minecraft.world.effect.MobEffectInstance;
/*     */ import net.minecraft.world.entity.Entity;
/*     */ import net.minecraft.world.entity.EntityType;
/*     */ import net.minecraft.world.entity.EntityTypes;
/*     */ import net.minecraft.world.entity.LivingEntity;
/*     */ import net.minecraft.world.item.ItemStack;
/*     */ import net.minecraft.world.item.Items;
/*     */ import net.minecraft.world.item.alchemy.PotionContents;
/*     */ import net.minecraft.world.level.ItemLike;
/*     */ import net.minecraft.world.level.Level;
/*     */ 
/*     */ public class Arrow extends AbstractArrow {
/*     */   private static final int EXPOSED_POTION_DECAY_TIME = 600;
/*     */   
/*     */   private static final int NO_EFFECT_COLOR = -1;
/*     */   
/*  25 */   private static final EntityDataAccessor<Integer> ID_EFFECT_COLOR = SynchedEntityData.defineId(Arrow.class, EntityDataSerializers.INT);
/*     */   
/*     */   private static final byte EVENT_POTION_PUFF = 0;
/*     */   
/*     */   public Arrow(EntityType<? extends Arrow> type, Level level) {
/*  30 */     super((EntityType)type, level);
/*     */   }
/*     */   
/*     */   public Arrow(Level level, double x, double y, double z, ItemStack pickupItemStack, ItemStack firedFromWeapon) {
/*  34 */     super(EntityTypes.ARROW, x, y, z, level, pickupItemStack, firedFromWeapon);
/*  35 */     updateColor();
/*     */   }
/*     */   
/*     */   public Arrow(Level level, LivingEntity owner, ItemStack pickupItemStack, ItemStack firedFromWeapon) {
/*  39 */     super(EntityTypes.ARROW, owner, level, pickupItemStack, firedFromWeapon);
/*  40 */     updateColor();
/*     */   }
/*     */   
/*     */   private PotionContents getPotionContents() {
/*  44 */     return (PotionContents)getPickupItemStackOrigin().getOrDefault(DataComponents.POTION_CONTENTS, PotionContents.EMPTY);
/*     */   }
/*     */   
/*     */   private float getPotionDurationScale() {
/*  48 */     return ((Float)getPickupItemStackOrigin().getOrDefault(DataComponents.POTION_DURATION_SCALE, Float.valueOf(1.0F))).floatValue();
/*     */   }
/*     */   
/*     */   private void setPotionContents(PotionContents potionContents) {
/*  52 */     getPickupItemStackOrigin().set(DataComponents.POTION_CONTENTS, potionContents);
/*  53 */     updateColor();
/*     */   }
/*     */   
/*     */   protected void setPickupItemStack(ItemStack itemStack) {
/*  58 */     super.setPickupItemStack(itemStack);
/*  59 */     updateColor();
/*     */   }
/*     */   
/*     */   private void updateColor() {
/*  63 */     PotionContents potionContents = getPotionContents();
/*  64 */     this.entityData.set(ID_EFFECT_COLOR, Integer.valueOf(potionContents.equals(PotionContents.EMPTY) ? -1 : potionContents.getColor()));
/*     */   }
/*     */   
/*     */   public void addEffect(MobEffectInstance effect) {
/*  68 */     setPotionContents(getPotionContents().withEffectAdded(effect));
/*     */   }
/*     */   
/*     */   protected void defineSynchedData(SynchedEntityData.Builder entityData) {
/*  73 */     super.defineSynchedData(entityData);
/*  74 */     entityData.define(ID_EFFECT_COLOR, Integer.valueOf(-1));
/*     */   }
/*     */   
/*     */   public void tick() {
/*  79 */     super.tick();
/*  81 */     if (level().isClientSide()) {
/*  82 */       if (isInGround()) {
/*  83 */         if (this.inGroundTime % 5 == 0)
/*  84 */           makeParticle(1); 
/*     */       } else {
/*  87 */         makeParticle(2);
/*     */       } 
/*  90 */     } else if (isInGround() && this.inGroundTime != 0 && 
/*  91 */       !getPotionContents().equals(PotionContents.EMPTY) && this.inGroundTime >= 600) {
/*  92 */       level().broadcastEntityEvent((Entity)this, (byte)0);
/*  93 */       setPickupItemStack(new ItemStack((ItemLike)Items.ARROW));
/*     */     } 
/*     */   }
/*     */   
/*     */   private void makeParticle(int amount) {
/* 100 */     int colorValue = getColor();
/* 101 */     if (colorValue == -1 || amount <= 0)
/*     */       return; 
/* 104 */     for (int i = 0; i < amount; i++)
/* 105 */       level().addParticle((ParticleOptions)ColorParticleOption.create(ParticleTypes.ENTITY_EFFECT, colorValue), getRandomX(0.5D), getRandomY(), getRandomZ(0.5D), 0.0D, 0.0D, 0.0D); 
/*     */   }
/*     */   
/*     */   public int getColor() {
/* 110 */     return ((Integer)this.entityData.get(ID_EFFECT_COLOR)).intValue();
/*     */   }
/*     */   
/*     */   protected void doPostHurtEffects(LivingEntity mob) {
/* 115 */     super.doPostHurtEffects(mob);
/* 117 */     Entity effectSource = getEffectSource();
/* 119 */     PotionContents potionContents = getPotionContents();
/* 120 */     float durationScale = getPotionDurationScale();
/* 121 */     potionContents.forEachEffect(effect -> mob.addEffect(effect, effectSource), durationScale);
/*     */   }
/*     */   
/*     */   protected ItemStack getDefaultPickupItem() {
/* 126 */     return new ItemStack((ItemLike)Items.ARROW);
/*     */   }
/*     */   
/*     */   public void handleEntityEvent(byte id) {
/* 131 */     if (id == 0) {
/* 132 */       int colorValue = getColor();
/* 133 */       if (colorValue != -1) {
/* 134 */         float red = (colorValue >> 16 & 0xFF) / 255.0F;
/* 135 */         float green = (colorValue >> 8 & 0xFF) / 255.0F;
/* 136 */         float blue = (colorValue >> 0 & 0xFF) / 255.0F;
/* 138 */         for (int i = 0; i < 20; i++)
/* 139 */           level().addParticle((ParticleOptions)ColorParticleOption.create(ParticleTypes.ENTITY_EFFECT, red, green, blue), getRandomX(0.5D), getRandomY(), getRandomZ(0.5D), 0.0D, 0.0D, 0.0D); 
/*     */       } 
/*     */     } else {
/* 143 */       super.handleEntityEvent(id);
/*     */     } 
/*     */   }
/*     */ }


/* Location:              C:\Users\karol\AppData\Roaming\ModrinthApp\meta\versions\26.2-0.19.5\26.2-0.19.5.jar!\net\minecraft\world\entity\projectile\arrow\Arrow.class
 * Java compiler version: 25 (69.0)
 * JD-Core Version:       1.1.3
 */