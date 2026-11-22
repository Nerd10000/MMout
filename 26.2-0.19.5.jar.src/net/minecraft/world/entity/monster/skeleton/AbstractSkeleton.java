/*     */ package net.minecraft.world.entity.monster.skeleton;
/*     */ 
/*     */ import net.minecraft.core.BlockPos;
/*     */ import net.minecraft.server.level.ServerLevel;
/*     */ import net.minecraft.sounds.SoundEvent;
/*     */ import net.minecraft.sounds.SoundEvents;
/*     */ import net.minecraft.tags.ItemTags;
/*     */ import net.minecraft.tags.TagKey;
/*     */ import net.minecraft.util.RandomSource;
/*     */ import net.minecraft.util.SpecialDates;
/*     */ import net.minecraft.world.Difficulty;
/*     */ import net.minecraft.world.DifficultyInstance;
/*     */ import net.minecraft.world.entity.Entity;
/*     */ import net.minecraft.world.entity.EntitySpawnReason;
/*     */ import net.minecraft.world.entity.EntityType;
/*     */ import net.minecraft.world.entity.EquipmentSlot;
/*     */ import net.minecraft.world.entity.LivingEntity;
/*     */ import net.minecraft.world.entity.Mob;
/*     */ import net.minecraft.world.entity.PathfinderMob;
/*     */ import net.minecraft.world.entity.SpawnGroupData;
/*     */ import net.minecraft.world.entity.ai.attributes.AttributeSupplier;
/*     */ import net.minecraft.world.entity.ai.attributes.Attributes;
/*     */ import net.minecraft.world.entity.ai.goal.AvoidEntityGoal;
/*     */ import net.minecraft.world.entity.ai.goal.FleeSunGoal;
/*     */ import net.minecraft.world.entity.ai.goal.Goal;
/*     */ import net.minecraft.world.entity.ai.goal.LookAtPlayerGoal;
/*     */ import net.minecraft.world.entity.ai.goal.MeleeAttackGoal;
/*     */ import net.minecraft.world.entity.ai.goal.RandomLookAroundGoal;
/*     */ import net.minecraft.world.entity.ai.goal.RangedBowAttackGoal;
/*     */ import net.minecraft.world.entity.ai.goal.RestrictSunGoal;
/*     */ import net.minecraft.world.entity.ai.goal.WaterAvoidingRandomStrollGoal;
/*     */ import net.minecraft.world.entity.ai.goal.target.HurtByTargetGoal;
/*     */ import net.minecraft.world.entity.ai.goal.target.NearestAttackableTargetGoal;
/*     */ import net.minecraft.world.entity.animal.golem.IronGolem;
/*     */ import net.minecraft.world.entity.animal.turtle.Turtle;
/*     */ import net.minecraft.world.entity.animal.wolf.Wolf;
/*     */ import net.minecraft.world.entity.monster.Monster;
/*     */ import net.minecraft.world.entity.monster.RangedAttackMob;
/*     */ import net.minecraft.world.entity.player.Player;
/*     */ import net.minecraft.world.entity.projectile.Projectile;
/*     */ import net.minecraft.world.entity.projectile.ProjectileUtil;
/*     */ import net.minecraft.world.entity.projectile.arrow.AbstractArrow;
/*     */ import net.minecraft.world.item.Item;
/*     */ import net.minecraft.world.item.ItemStack;
/*     */ import net.minecraft.world.item.Items;
/*     */ import net.minecraft.world.level.ItemLike;
/*     */ import net.minecraft.world.level.Level;
/*     */ import net.minecraft.world.level.ServerLevelAccessor;
/*     */ import net.minecraft.world.level.block.Blocks;
/*     */ import net.minecraft.world.level.block.state.BlockState;
/*     */ import net.minecraft.world.level.storage.ValueInput;
/*     */ 
/*     */ public abstract class AbstractSkeleton extends Monster implements RangedAttackMob {
/*     */   private static final int HARD_ATTACK_INTERVAL = 20;
/*     */   
/*     */   private static final int NORMAL_ATTACK_INTERVAL = 40;
/*     */   
/*     */   protected static final int INCREASED_HARD_ATTACK_INTERVAL = 50;
/*     */   
/*     */   protected static final int INCREASED_NORMAL_ATTACK_INTERVAL = 70;
/*     */   
/*  60 */   private final RangedBowAttackGoal<AbstractSkeleton> bowGoal = new RangedBowAttackGoal(this, 1.0D, 20, 15.0F);
/*     */   
/*  61 */   private final MeleeAttackGoal meleeGoal = new MeleeAttackGoal((PathfinderMob)this, 1.2D, false) {
/*     */       public void stop() {
/*  64 */         super.stop();
/*  65 */         AbstractSkeleton.this.setAggressive(false);
/*     */       }
/*     */       
/*     */       public void start() {
/*  70 */         super.start();
/*  71 */         AbstractSkeleton.this.setAggressive(true);
/*     */       }
/*     */     };
/*     */   
/*     */   protected AbstractSkeleton(EntityType<? extends AbstractSkeleton> type, Level level) {
/*  76 */     super(type, level);
/*  78 */     reassessWeaponGoal();
/*     */   }
/*     */   
/*     */   protected void registerGoals() {
/*  83 */     this.goalSelector.addGoal(2, (Goal)new RestrictSunGoal((PathfinderMob)this));
/*  84 */     this.goalSelector.addGoal(3, (Goal)new FleeSunGoal((PathfinderMob)this, 1.0D));
/*  85 */     this.goalSelector.addGoal(3, (Goal)new AvoidEntityGoal((PathfinderMob)this, Wolf.class, 6.0F, 1.0D, 1.2D));
/*  86 */     this.goalSelector.addGoal(5, (Goal)new WaterAvoidingRandomStrollGoal((PathfinderMob)this, 1.0D));
/*  87 */     this.goalSelector.addGoal(6, (Goal)new LookAtPlayerGoal((Mob)this, Player.class, 8.0F));
/*  88 */     this.goalSelector.addGoal(6, (Goal)new RandomLookAroundGoal((Mob)this));
/*  90 */     this.targetSelector.addGoal(1, (Goal)new HurtByTargetGoal((PathfinderMob)this, new Class[0]));
/*  91 */     this.targetSelector.addGoal(2, (Goal)new NearestAttackableTargetGoal((Mob)this, Player.class, true));
/*  92 */     this.targetSelector.addGoal(3, (Goal)new NearestAttackableTargetGoal((Mob)this, IronGolem.class, true));
/*  93 */     this.targetSelector.addGoal(3, (Goal)new NearestAttackableTargetGoal((Mob)this, Turtle.class, 10, true, false, Turtle.BABY_ON_LAND_SELECTOR));
/*     */   }
/*     */   
/*     */   public static AttributeSupplier.Builder createAttributes() {
/*  97 */     return Monster.createMonsterAttributes()
/*  98 */       .add(Attributes.MOVEMENT_SPEED, 0.25D);
/*     */   }
/*     */   
/*     */   protected void playStepSound(BlockPos pos, BlockState blockState) {
/* 103 */     playSound(getStepSound(), 0.15F, 1.0F);
/*     */   }
/*     */   
/*     */   protected abstract SoundEvent getStepSound();
/*     */   
/*     */   public void rideTick() {
/* 110 */     super.rideTick();
/* 112 */     Entity entity = getControlledVehicle();
/* 112 */     if (entity instanceof PathfinderMob) {
/* 112 */       PathfinderMob pathfinderMob = (PathfinderMob)entity;
/* 113 */       this.yBodyRot = pathfinderMob.yBodyRot;
/*     */     } 
/*     */   }
/*     */   
/*     */   protected void populateDefaultEquipmentSlots(RandomSource random, DifficultyInstance difficulty) {
/* 119 */     super.populateDefaultEquipmentSlots(random, difficulty);
/* 121 */     setItemSlot(EquipmentSlot.MAINHAND, new ItemStack((ItemLike)Items.BOW));
/*     */   }
/*     */   
/*     */   public SpawnGroupData finalizeSpawn(ServerLevelAccessor level, DifficultyInstance difficulty, EntitySpawnReason spawnReason, SpawnGroupData groupData) {
/* 126 */     groupData = super.finalizeSpawn(level, difficulty, spawnReason, groupData);
/* 128 */     RandomSource random = level.getRandom();
/* 129 */     populateDefaultEquipmentSlots(random, difficulty);
/* 130 */     populateDefaultEquipmentEnchantments(level, random, difficulty);
/* 131 */     reassessWeaponGoal();
/* 133 */     setCanPickUpLoot((random.nextFloat() < 0.55F * difficulty.getSpecialMultiplier()));
/* 135 */     if (getItemBySlot(EquipmentSlot.HEAD).isEmpty() && 
/* 136 */       SpecialDates.isHalloween() && random.nextFloat() < 0.25F) {
/* 138 */       setItemSlot(EquipmentSlot.HEAD, new ItemStack((random.nextFloat() < 0.1F) ? (ItemLike)Blocks.JACK_O_LANTERN : (ItemLike)Blocks.CARVED_PUMPKIN));
/* 139 */       setDropChance(EquipmentSlot.HEAD, 0.0F);
/*     */     } 
/* 142 */     return groupData;
/*     */   }
/*     */   
/*     */   public void reassessWeaponGoal() {
/* 146 */     if (level() == null || level().isClientSide())
/*     */       return; 
/* 150 */     this.goalSelector.removeGoal((Goal)this.meleeGoal);
/* 151 */     this.goalSelector.removeGoal((Goal)this.bowGoal);
/* 153 */     ItemStack usedWeapon = getItemInHand(ProjectileUtil.getWeaponHoldingHand((LivingEntity)this, Items.BOW));
/* 154 */     if (usedWeapon.is(Items.BOW)) {
/* 156 */       int minAttackInterval = getHardAttackInterval();
/* 157 */       if (level().getDifficulty() != Difficulty.HARD)
/* 158 */         minAttackInterval = getAttackInterval(); 
/* 160 */       this.bowGoal.setMinAttackInterval(minAttackInterval);
/* 161 */       this.goalSelector.addGoal(4, (Goal)this.bowGoal);
/*     */     } else {
/* 163 */       this.goalSelector.addGoal(4, (Goal)this.meleeGoal);
/*     */     } 
/*     */   }
/*     */   
/*     */   protected int getHardAttackInterval() {
/* 168 */     return 20;
/*     */   }
/*     */   
/*     */   protected int getAttackInterval() {
/* 172 */     return 40;
/*     */   }
/*     */   
/*     */   public void performRangedAttack(LivingEntity target, float power) {
/* 177 */     ItemStack bowItem = getItemInHand(ProjectileUtil.getWeaponHoldingHand((LivingEntity)this, Items.BOW));
/* 178 */     ItemStack projectile = getProjectile(bowItem);
/* 179 */     AbstractArrow arrow = getArrow(projectile, power, bowItem);
/* 181 */     double xd = target.getX() - getX();
/* 182 */     double yd = target.getY(0.3333333333333333D) - arrow.getY();
/* 183 */     double zd = target.getZ() - getZ();
/* 184 */     double distanceToTarget = Math.sqrt(xd * xd + zd * zd);
/* 185 */     Level level = level();
/* 185 */     if (level instanceof ServerLevel) {
/* 185 */       ServerLevel serverLevel = (ServerLevel)level;
/* 186 */       Projectile.spawnProjectileUsingShoot((Projectile)arrow, serverLevel, projectile, xd, yd + distanceToTarget * 0.20000000298023224D, zd, 1.6F, (14 - serverLevel
/*     */           
/* 190 */           .getDifficulty().getId() * 4));
/*     */     } 
/* 192 */     playSound(SoundEvents.SKELETON_SHOOT, 1.0F, 1.0F / (getRandom().nextFloat() * 0.4F + 0.8F));
/*     */   }
/*     */   
/*     */   protected AbstractArrow getArrow(ItemStack projectile, float power, ItemStack firingWeapon) {
/* 196 */     return ProjectileUtil.getMobArrow((LivingEntity)this, projectile, power, firingWeapon);
/*     */   }
/*     */   
/*     */   public boolean canUseNonMeleeWeapon(ItemStack item) {
/* 201 */     return (item.getItem() == Items.BOW);
/*     */   }
/*     */   
/*     */   public TagKey<Item> getPreferredWeaponType() {
/* 206 */     return ItemTags.SKELETON_PREFERRED_WEAPONS;
/*     */   }
/*     */   
/*     */   protected void readAdditionalSaveData(ValueInput input) {
/* 211 */     super.readAdditionalSaveData(input);
/* 213 */     reassessWeaponGoal();
/*     */   }
/*     */   
/*     */   public void onEquipItem(EquipmentSlot slot, ItemStack oldStack, ItemStack stack) {
/* 218 */     super.onEquipItem(slot, oldStack, stack);
/* 220 */     if (!level().isClientSide())
/* 221 */       reassessWeaponGoal(); 
/*     */   }
/*     */   
/*     */   public boolean isShaking() {
/* 226 */     return isFullyFrozen();
/*     */   }
/*     */   
/*     */   public boolean wantsToPickUp(ServerLevel level, ItemStack itemStack) {
/* 231 */     if (itemStack.is(ItemTags.SPEARS))
/* 232 */       return false; 
/* 234 */     return super.wantsToPickUp(level, itemStack);
/*     */   }
/*     */ }


/* Location:              C:\Users\karol\AppData\Roaming\ModrinthApp\meta\versions\26.2-0.19.5\26.2-0.19.5.jar!\net\minecraft\world\entity\monster\skeleton\AbstractSkeleton.class
 * Java compiler version: 25 (69.0)
 * JD-Core Version:       1.1.3
 */