/*      */ package net.minecraft.world.entity.animal.panda;
/*      */ 
/*      */ import com.mojang.serialization.Codec;
/*      */ import java.util.EnumSet;
/*      */ import java.util.List;
/*      */ import java.util.function.IntFunction;
/*      */ import net.minecraft.core.BlockPos;
/*      */ import net.minecraft.core.Holder;
/*      */ import net.minecraft.core.Vec3i;
/*      */ import net.minecraft.core.particles.ItemParticleOption;
/*      */ import net.minecraft.core.particles.ParticleOptions;
/*      */ import net.minecraft.core.particles.ParticleTypes;
/*      */ import net.minecraft.network.syncher.EntityDataAccessor;
/*      */ import net.minecraft.network.syncher.EntityDataSerializers;
/*      */ import net.minecraft.network.syncher.SynchedEntityData;
/*      */ import net.minecraft.server.level.ServerLevel;
/*      */ import net.minecraft.sounds.SoundEvent;
/*      */ import net.minecraft.sounds.SoundEvents;
/*      */ import net.minecraft.tags.DamageTypeTags;
/*      */ import net.minecraft.tags.ItemTags;
/*      */ import net.minecraft.util.ByIdMap;
/*      */ import net.minecraft.util.Mth;
/*      */ import net.minecraft.util.RandomSource;
/*      */ import net.minecraft.util.StringRepresentable;
/*      */ import net.minecraft.world.DifficultyInstance;
/*      */ import net.minecraft.world.InteractionHand;
/*      */ import net.minecraft.world.InteractionResult;
/*      */ import net.minecraft.world.damagesource.DamageSource;
/*      */ import net.minecraft.world.entity.AgeableMob;
/*      */ import net.minecraft.world.entity.Entity;
/*      */ import net.minecraft.world.entity.EntityAttachment;
/*      */ import net.minecraft.world.entity.EntityAttachments;
/*      */ import net.minecraft.world.entity.EntityDimensions;
/*      */ import net.minecraft.world.entity.EntitySelector;
/*      */ import net.minecraft.world.entity.EntitySpawnReason;
/*      */ import net.minecraft.world.entity.EntityType;
/*      */ import net.minecraft.world.entity.EntityTypes;
/*      */ import net.minecraft.world.entity.EquipmentSlot;
/*      */ import net.minecraft.world.entity.LivingEntity;
/*      */ import net.minecraft.world.entity.Mob;
/*      */ import net.minecraft.world.entity.PathfinderMob;
/*      */ import net.minecraft.world.entity.Pose;
/*      */ import net.minecraft.world.entity.SpawnGroupData;
/*      */ import net.minecraft.world.entity.ai.attributes.AttributeSupplier;
/*      */ import net.minecraft.world.entity.ai.attributes.Attributes;
/*      */ import net.minecraft.world.entity.ai.control.MoveControl;
/*      */ import net.minecraft.world.entity.ai.goal.AvoidEntityGoal;
/*      */ import net.minecraft.world.entity.ai.goal.BreedGoal;
/*      */ import net.minecraft.world.entity.ai.goal.FloatGoal;
/*      */ import net.minecraft.world.entity.ai.goal.FollowParentGoal;
/*      */ import net.minecraft.world.entity.ai.goal.Goal;
/*      */ import net.minecraft.world.entity.ai.goal.LookAtPlayerGoal;
/*      */ import net.minecraft.world.entity.ai.goal.MeleeAttackGoal;
/*      */ import net.minecraft.world.entity.ai.goal.PanicGoal;
/*      */ import net.minecraft.world.entity.ai.goal.RandomLookAroundGoal;
/*      */ import net.minecraft.world.entity.ai.goal.TemptGoal;
/*      */ import net.minecraft.world.entity.ai.goal.WaterAvoidingRandomStrollGoal;
/*      */ import net.minecraft.world.entity.ai.goal.target.HurtByTargetGoal;
/*      */ import net.minecraft.world.entity.ai.targeting.TargetingConditions;
/*      */ import net.minecraft.world.entity.animal.Animal;
/*      */ import net.minecraft.world.entity.item.ItemEntity;
/*      */ import net.minecraft.world.entity.monster.Monster;
/*      */ import net.minecraft.world.entity.player.Player;
/*      */ import net.minecraft.world.item.ItemStack;
/*      */ import net.minecraft.world.item.ItemStackTemplate;
/*      */ import net.minecraft.world.item.Items;
/*      */ import net.minecraft.world.level.ItemLike;
/*      */ import net.minecraft.world.level.Level;
/*      */ import net.minecraft.world.level.ServerLevelAccessor;
/*      */ import net.minecraft.world.level.block.Blocks;
/*      */ import net.minecraft.world.level.block.state.BlockState;
/*      */ import net.minecraft.world.level.gameevent.GameEvent;
/*      */ import net.minecraft.world.level.gamerules.GameRules;
/*      */ import net.minecraft.world.level.storage.ValueInput;
/*      */ import net.minecraft.world.level.storage.ValueOutput;
/*      */ import net.minecraft.world.level.storage.loot.BuiltInLootTables;
/*      */ import net.minecraft.world.phys.Vec3;
/*      */ 
/*      */ public class Panda extends Animal {
/*   78 */   private static final EntityDataAccessor<Integer> UNHAPPY_COUNTER = SynchedEntityData.defineId(Panda.class, EntityDataSerializers.INT);
/*      */   
/*   79 */   private static final EntityDataAccessor<Integer> SNEEZE_COUNTER = SynchedEntityData.defineId(Panda.class, EntityDataSerializers.INT);
/*      */   
/*   80 */   private static final EntityDataAccessor<Integer> EAT_COUNTER = SynchedEntityData.defineId(Panda.class, EntityDataSerializers.INT);
/*      */   
/*   81 */   private static final EntityDataAccessor<Byte> MAIN_GENE_ID = SynchedEntityData.defineId(Panda.class, EntityDataSerializers.BYTE);
/*      */   
/*   82 */   private static final EntityDataAccessor<Byte> HIDDEN_GENE_ID = SynchedEntityData.defineId(Panda.class, EntityDataSerializers.BYTE);
/*      */   
/*   84 */   private static final EntityDataAccessor<Byte> DATA_ID_FLAGS = SynchedEntityData.defineId(Panda.class, EntityDataSerializers.BYTE);
/*      */   
/*   86 */   private static final TargetingConditions BREED_TARGETING = TargetingConditions.forNonCombat().range(8.0D);
/*      */   
/*   88 */   private static final EntityDimensions BABY_DIMENSIONS = EntityTypes.PANDA.getDimensions().scale(0.5F)
/*   89 */     .withEyeHeight(0.28125F)
/*   90 */     .withAttachments(EntityAttachments.builder()
/*   91 */       .attach(EntityAttachment.PASSENGER, 0.0F, 0.5625F, 0.0F));
/*      */   
/*      */   private static final int FLAG_SNEEZE = 2;
/*      */   
/*      */   private static final int FLAG_ROLL = 4;
/*      */   
/*      */   private static final int FLAG_SIT = 8;
/*      */   
/*      */   private static final int FLAG_ON_BACK = 16;
/*      */   
/*      */   private static final int EAT_TICK_INTERVAL = 5;
/*      */   
/*      */   public static final int TOTAL_ROLL_STEPS = 32;
/*      */   
/*      */   private static final int TOTAL_UNHAPPY_TIME = 32;
/*      */   
/*      */   private boolean gotBamboo;
/*      */   
/*      */   private boolean didBite;
/*      */   
/*      */   public int rollCounter;
/*      */   
/*      */   private Vec3 rollDelta;
/*      */   
/*      */   private float sitAmount;
/*      */   
/*      */   private float sitAmountO;
/*      */   
/*      */   private float onBackAmount;
/*      */   
/*      */   private float onBackAmountO;
/*      */   
/*      */   private float rollAmount;
/*      */   
/*      */   private float rollAmountO;
/*      */   
/*      */   private PandaLookAtPlayerGoal lookAtPlayerGoal;
/*      */   
/*      */   public Panda(EntityType<? extends Panda> type, Level level) {
/*  118 */     super(type, level);
/*  120 */     this.moveControl = new PandaMoveControl<>(this);
/*  122 */     if (!isBaby())
/*  123 */       setCanPickUpLoot(true); 
/*      */   }
/*      */   
/*      */   protected boolean canDispenserEquipIntoSlot(EquipmentSlot slot) {
/*  129 */     return (slot == EquipmentSlot.MAINHAND && canPickUpLoot());
/*      */   }
/*      */   
/*      */   public int getUnhappyCounter() {
/*  133 */     return ((Integer)this.entityData.get(UNHAPPY_COUNTER)).intValue();
/*      */   }
/*      */   
/*      */   public void setUnhappyCounter(int value) {
/*  137 */     this.entityData.set(UNHAPPY_COUNTER, Integer.valueOf(value));
/*      */   }
/*      */   
/*      */   public boolean isSneezing() {
/*  141 */     return getFlag(2);
/*      */   }
/*      */   
/*      */   public boolean isSitting() {
/*  145 */     return getFlag(8);
/*      */   }
/*      */   
/*      */   public void sit(boolean value) {
/*  149 */     setFlag(8, value);
/*      */   }
/*      */   
/*      */   public boolean isOnBack() {
/*  153 */     return getFlag(16);
/*      */   }
/*      */   
/*      */   public void setOnBack(boolean value) {
/*  157 */     setFlag(16, value);
/*      */   }
/*      */   
/*      */   public boolean isEating() {
/*  161 */     return (((Integer)this.entityData.get(EAT_COUNTER)).intValue() > 0);
/*      */   }
/*      */   
/*      */   public void eat(boolean value) {
/*  165 */     this.entityData.set(EAT_COUNTER, Integer.valueOf(value ? 1 : 0));
/*      */   }
/*      */   
/*      */   private int getEatCounter() {
/*  169 */     return ((Integer)this.entityData.get(EAT_COUNTER)).intValue();
/*      */   }
/*      */   
/*      */   private void setEatCounter(int value) {
/*  173 */     this.entityData.set(EAT_COUNTER, Integer.valueOf(value));
/*      */   }
/*      */   
/*      */   public void sneeze(boolean value) {
/*  177 */     setFlag(2, value);
/*  179 */     if (!value)
/*  180 */       setSneezeCounter(0); 
/*      */   }
/*      */   
/*      */   public int getSneezeCounter() {
/*  185 */     return ((Integer)this.entityData.get(SNEEZE_COUNTER)).intValue();
/*      */   }
/*      */   
/*      */   public void setSneezeCounter(int value) {
/*  189 */     this.entityData.set(SNEEZE_COUNTER, Integer.valueOf(value));
/*      */   }
/*      */   
/*      */   public Gene getMainGene() {
/*  193 */     return Gene.byId(((Byte)this.entityData.get(MAIN_GENE_ID)).byteValue());
/*      */   }
/*      */   
/*      */   public void setMainGene(Gene gene) {
/*  197 */     if (gene.getId() > 6)
/*  198 */       gene = Gene.getRandom(this.random); 
/*  201 */     this.entityData.set(MAIN_GENE_ID, Byte.valueOf((byte)gene.getId()));
/*      */   }
/*      */   
/*      */   public Gene getHiddenGene() {
/*  205 */     return Gene.byId(((Byte)this.entityData.get(HIDDEN_GENE_ID)).byteValue());
/*      */   }
/*      */   
/*      */   public void setHiddenGene(Gene gene) {
/*  209 */     if (gene.getId() > 6)
/*  210 */       gene = Gene.getRandom(this.random); 
/*  213 */     this.entityData.set(HIDDEN_GENE_ID, Byte.valueOf((byte)gene.getId()));
/*      */   }
/*      */   
/*      */   public boolean isRolling() {
/*  217 */     return getFlag(4);
/*      */   }
/*      */   
/*      */   public void roll(boolean value) {
/*  221 */     setFlag(4, value);
/*      */   }
/*      */   
/*      */   protected void defineSynchedData(SynchedEntityData.Builder entityData) {
/*  226 */     super.defineSynchedData(entityData);
/*  227 */     entityData.define(UNHAPPY_COUNTER, Integer.valueOf(0));
/*  228 */     entityData.define(SNEEZE_COUNTER, Integer.valueOf(0));
/*  229 */     entityData.define(MAIN_GENE_ID, Byte.valueOf((byte)0));
/*  230 */     entityData.define(HIDDEN_GENE_ID, Byte.valueOf((byte)0));
/*  231 */     entityData.define(DATA_ID_FLAGS, Byte.valueOf((byte)0));
/*  232 */     entityData.define(EAT_COUNTER, Integer.valueOf(0));
/*      */   }
/*      */   
/*      */   private boolean getFlag(int flag) {
/*  236 */     return ((((Byte)this.entityData.get(DATA_ID_FLAGS)).byteValue() & flag) != 0);
/*      */   }
/*      */   
/*      */   private void setFlag(int flag, boolean value) {
/*  240 */     byte current = ((Byte)this.entityData.get(DATA_ID_FLAGS)).byteValue();
/*  241 */     if (value) {
/*  242 */       this.entityData.set(DATA_ID_FLAGS, Byte.valueOf((byte)(current | flag)));
/*      */     } else {
/*  244 */       this.entityData.set(DATA_ID_FLAGS, Byte.valueOf((byte)(current & (flag ^ 0xFFFFFFFF))));
/*      */     } 
/*      */   }
/*      */   
/*      */   protected void addAdditionalSaveData(ValueOutput output) {
/*  250 */     super.addAdditionalSaveData(output);
/*  252 */     output.store("MainGene", Gene.CODEC, getMainGene());
/*  253 */     output.store("HiddenGene", Gene.CODEC, getHiddenGene());
/*      */   }
/*      */   
/*      */   protected void readAdditionalSaveData(ValueInput input) {
/*  258 */     super.readAdditionalSaveData(input);
/*  260 */     setMainGene(input.read("MainGene", Gene.CODEC).orElse(Gene.NORMAL));
/*  261 */     setHiddenGene(input.read("HiddenGene", Gene.CODEC).orElse(Gene.NORMAL));
/*      */   }
/*      */   
/*      */   public AgeableMob getBreedOffspring(ServerLevel level, AgeableMob partner) {
/*  266 */     Panda baby = (Panda)EntityTypes.PANDA.create((Level)level, EntitySpawnReason.BREEDING);
/*  267 */     if (baby != null) {
/*  268 */       if (partner instanceof Panda) {
/*  268 */         Panda partnerPanda = (Panda)partner;
/*  269 */         baby.setGeneFromParents(this, partnerPanda);
/*      */       } 
/*  272 */       baby.setAttributes();
/*      */     } 
/*  275 */     return (AgeableMob)baby;
/*      */   }
/*      */   
/*      */   protected void registerGoals() {
/*  280 */     this.goalSelector.addGoal(0, (Goal)new FloatGoal((Mob)this));
/*  281 */     this.goalSelector.addGoal(2, (Goal)new PandaPanicGoal(this, 2.0D));
/*  282 */     this.goalSelector.addGoal(2, (Goal)new PandaBreedGoal(this, 1.0D));
/*  283 */     this.goalSelector.addGoal(3, (Goal)new PandaAttackGoal(this, 1.2000000476837158D, true));
/*  284 */     this.goalSelector.addGoal(4, (Goal)new TemptGoal((PathfinderMob)this, 1.0D, i -> i.is(ItemTags.PANDA_FOOD), false));
/*  285 */     this.goalSelector.addGoal(6, (Goal)new PandaAvoidGoal<>(this, Player.class, 8.0F, 2.0D, 2.0D));
/*  286 */     this.goalSelector.addGoal(6, (Goal)new PandaAvoidGoal<>(this, Monster.class, 4.0F, 2.0D, 2.0D));
/*  287 */     this.goalSelector.addGoal(7, new PandaSitGoal());
/*  288 */     this.goalSelector.addGoal(8, new PandaLieOnBackGoal(this));
/*  289 */     this.goalSelector.addGoal(8, new PandaSneezeGoal(this));
/*  290 */     this.lookAtPlayerGoal = new PandaLookAtPlayerGoal(this, (Class)Player.class, 6.0F);
/*  291 */     this.goalSelector.addGoal(9, (Goal)this.lookAtPlayerGoal);
/*  292 */     this.goalSelector.addGoal(10, (Goal)new RandomLookAroundGoal((Mob)this));
/*  293 */     this.goalSelector.addGoal(12, new PandaRollGoal(this));
/*  294 */     this.goalSelector.addGoal(13, (Goal)new FollowParentGoal(this, 1.25D));
/*  295 */     this.goalSelector.addGoal(14, (Goal)new WaterAvoidingRandomStrollGoal((PathfinderMob)this, 1.0D));
/*  297 */     this.targetSelector.addGoal(1, (Goal)(new PandaHurtByTargetGoal(this, new Class[0])).setAlertOthers(new Class[0]));
/*      */   }
/*      */   
/*      */   public static AttributeSupplier.Builder createAttributes() {
/*  301 */     return Animal.createAnimalAttributes()
/*  302 */       .add(Attributes.MOVEMENT_SPEED, 0.15000000596046448D)
/*  303 */       .add(Attributes.ATTACK_DAMAGE, 6.0D);
/*      */   }
/*      */   
/*      */   public enum Gene implements StringRepresentable {
/*  309 */     NORMAL(0, "normal", false),
/*  310 */     LAZY(1, "lazy", false),
/*  311 */     WORRIED(2, "worried", false),
/*  312 */     PLAYFUL(3, "playful", false),
/*  313 */     BROWN(4, "brown", true),
/*  314 */     WEAK(5, "weak", true),
/*  315 */     AGGRESSIVE(6, "aggressive", false);
/*      */     
/*  317 */     public static final Codec<Gene> CODEC = (Codec<Gene>)StringRepresentable.fromEnum(Gene::values);
/*      */     
/*  319 */     private static final IntFunction<Gene> BY_ID = ByIdMap.continuous(Gene::getId, (Object[])values(), ByIdMap.OutOfBoundsStrategy.ZERO);
/*      */     
/*      */     private static final int MAX_GENE = 6;
/*      */     
/*      */     private final int id;
/*      */     
/*      */     private final String name;
/*      */     
/*      */     private final boolean isRecessive;
/*      */     
/*      */     static {
/*      */     
/*      */     }
/*      */     
/*      */     Gene(int id, String name, boolean isRecessive) {
/*  327 */       this.id = id;
/*  328 */       this.name = name;
/*  329 */       this.isRecessive = isRecessive;
/*      */     }
/*      */     
/*      */     public int getId() {
/*  333 */       return this.id;
/*      */     }
/*      */     
/*      */     public String getSerializedName() {
/*  338 */       return this.name;
/*      */     }
/*      */     
/*      */     public boolean isRecessive() {
/*  342 */       return this.isRecessive;
/*      */     }
/*      */     
/*      */     private static Gene getVariantFromGenes(Gene mainGene, Gene hiddenGene) {
/*  346 */       if (mainGene.isRecessive()) {
/*  347 */         if (mainGene == hiddenGene)
/*  348 */           return mainGene; 
/*  350 */         return NORMAL;
/*      */       } 
/*  354 */       return mainGene;
/*      */     }
/*      */     
/*      */     public static Gene byId(int id) {
/*  358 */       return BY_ID.apply(id);
/*      */     }
/*      */     
/*      */     public static Gene getRandom(RandomSource random) {
/*  362 */       int nextInt = random.nextInt(16);
/*  363 */       if (nextInt == 0)
/*  364 */         return LAZY; 
/*  366 */       if (nextInt == 1)
/*  367 */         return WORRIED; 
/*  369 */       if (nextInt == 2)
/*  370 */         return PLAYFUL; 
/*  372 */       if (nextInt == 4)
/*  373 */         return AGGRESSIVE; 
/*  375 */       if (nextInt < 9)
/*  376 */         return WEAK; 
/*  378 */       if (nextInt < 11)
/*  379 */         return BROWN; 
/*  382 */       return NORMAL;
/*      */     }
/*      */   }
/*      */   
/*      */   public Gene getVariant() {
/*  387 */     return Gene.getVariantFromGenes(getMainGene(), getHiddenGene());
/*      */   }
/*      */   
/*      */   public boolean isLazy() {
/*  391 */     return (getVariant() == Gene.LAZY);
/*      */   }
/*      */   
/*      */   public boolean isWorried() {
/*  395 */     return (getVariant() == Gene.WORRIED);
/*      */   }
/*      */   
/*      */   public boolean isPlayful() {
/*  399 */     return (getVariant() == Gene.PLAYFUL);
/*      */   }
/*      */   
/*      */   public boolean isBrown() {
/*  403 */     return (getVariant() == Gene.BROWN);
/*      */   }
/*      */   
/*      */   public boolean isWeak() {
/*  407 */     return (getVariant() == Gene.WEAK);
/*      */   }
/*      */   
/*      */   public boolean isAggressive() {
/*  412 */     return (getVariant() == Gene.AGGRESSIVE);
/*      */   }
/*      */   
/*      */   public boolean canBeLeashed() {
/*  417 */     return false;
/*      */   }
/*      */   
/*      */   public boolean doHurtTarget(ServerLevel level, Entity target) {
/*  422 */     if (!isAggressive())
/*  423 */       this.didBite = true; 
/*  425 */     return super.doHurtTarget(level, target);
/*      */   }
/*      */   
/*      */   public void playAttackSound() {
/*  430 */     playSound(SoundEvents.PANDA_BITE, 1.0F, 1.0F);
/*      */   }
/*      */   
/*      */   public void tick() {
/*  435 */     super.tick();
/*  437 */     if (isWorried())
/*  438 */       if (level().isThundering() && !isInWater()) {
/*  439 */         sit(true);
/*  440 */         eat(false);
/*  441 */       } else if (!isEating()) {
/*  442 */         sit(false);
/*      */       }  
/*  446 */     LivingEntity target = getTarget();
/*  447 */     if (target == null) {
/*  448 */       this.gotBamboo = false;
/*  449 */       this.didBite = false;
/*      */     } 
/*  452 */     if (getUnhappyCounter() > 0) {
/*  453 */       if (target != null)
/*  454 */         lookAt((Entity)target, 90.0F, 90.0F); 
/*  457 */       if (getUnhappyCounter() == 29 || getUnhappyCounter() == 14)
/*  458 */         playSound(SoundEvents.PANDA_CANT_BREED, 1.0F, 1.0F); 
/*  461 */       setUnhappyCounter(getUnhappyCounter() - 1);
/*      */     } 
/*  464 */     if (isSneezing()) {
/*  465 */       setSneezeCounter(getSneezeCounter() + 1);
/*  466 */       if (getSneezeCounter() > 20) {
/*  467 */         sneeze(false);
/*  468 */         afterSneeze();
/*  469 */       } else if (getSneezeCounter() == 1) {
/*  470 */         playSound(SoundEvents.PANDA_PRE_SNEEZE, 1.0F, 1.0F);
/*      */       } 
/*      */     } 
/*  474 */     if (isRolling()) {
/*  475 */       handleRoll();
/*      */     } else {
/*  477 */       this.rollCounter = 0;
/*      */     } 
/*  480 */     if (isSitting())
/*  481 */       setXRot(0.0F); 
/*  484 */     updateSitAmount();
/*  485 */     handleEating();
/*  486 */     updateOnBackAnimation();
/*  487 */     updateRollAmount();
/*      */   }
/*      */   
/*      */   public boolean isScared() {
/*  491 */     return (isWorried() && level().isThundering());
/*      */   }
/*      */   
/*      */   private void handleEating() {
/*  495 */     if (!isEating() && isSitting() && !isScared() && !getItemBySlot(EquipmentSlot.MAINHAND).isEmpty() && this.random.nextInt(80) == 1) {
/*  496 */       eat(true);
/*  497 */     } else if (getItemBySlot(EquipmentSlot.MAINHAND).isEmpty() || !isSitting()) {
/*  498 */       eat(false);
/*      */     } 
/*  501 */     if (isEating()) {
/*  502 */       addEatingParticles();
/*  504 */       if (!level().isClientSide() && getEatCounter() > 80 && this.random.nextInt(20) == 1) {
/*  505 */         if (getEatCounter() > 100 && getItemBySlot(EquipmentSlot.MAINHAND).is(ItemTags.PANDA_EATS_FROM_GROUND)) {
/*  507 */           if (!level().isClientSide()) {
/*  508 */             setItemSlot(EquipmentSlot.MAINHAND, ItemStack.EMPTY);
/*  509 */             gameEvent((Holder)GameEvent.EAT);
/*      */           } 
/*  512 */           sit(false);
/*      */         } 
/*  514 */         eat(false);
/*      */         return;
/*      */       } 
/*  518 */       setEatCounter(getEatCounter() + 1);
/*      */     } 
/*      */   }
/*      */   
/*      */   private void addEatingParticles() {
/*  523 */     if (getEatCounter() % 5 == 0) {
/*  524 */       playSound(SoundEvents.PANDA_EAT, 0.5F + 0.5F * this.random.nextInt(2), (this.random.nextFloat() - this.random.nextFloat()) * 0.2F + 1.0F);
/*  526 */       ItemStack heldItem = getItemBySlot(EquipmentSlot.MAINHAND);
/*  527 */       if (!heldItem.isEmpty()) {
/*  528 */         ItemParticleOption breakParticle = new ItemParticleOption(ParticleTypes.ITEM, ItemStackTemplate.fromNonEmptyStack(heldItem));
/*  529 */         for (int i = 0; i < 6; i++) {
/*  532 */           Vec3 velocity = (new Vec3((this.random.nextFloat() - 0.5D) * 0.1D, this.random.nextFloat() * 0.1D + 0.1D, (this.random.nextFloat() - 0.5D) * 0.1D)).xRot(-getXRot() * 0.017453292F).yRot(-getYRot() * 0.017453292F);
/*  536 */           Vec3 position = (new Vec3((this.random.nextFloat() - 0.5D) * 0.8D, -this.random.nextFloat() * 0.6D - 0.3D, 1.0D + (this.random.nextFloat() - 0.5D) * 0.4D)).yRot(-this.yBodyRot * 0.017453292F).add(getX(), getEyeY() + 1.0D, getZ());
/*  538 */           level().addParticle((ParticleOptions)breakParticle, position.x, position.y, position.z, velocity.x, velocity.y + 0.05D, velocity.z);
/*      */         } 
/*      */       } 
/*      */     } 
/*      */   }
/*      */   
/*      */   private void updateSitAmount() {
/*  545 */     this.sitAmountO = this.sitAmount;
/*  546 */     if (isSitting()) {
/*  547 */       this.sitAmount = Math.min(1.0F, this.sitAmount + 0.15F);
/*      */     } else {
/*  549 */       this.sitAmount = Math.max(0.0F, this.sitAmount - 0.19F);
/*      */     } 
/*      */   }
/*      */   
/*      */   private void updateOnBackAnimation() {
/*  554 */     this.onBackAmountO = this.onBackAmount;
/*  555 */     if (isOnBack()) {
/*  556 */       this.onBackAmount = Math.min(1.0F, this.onBackAmount + 0.15F);
/*      */     } else {
/*  558 */       this.onBackAmount = Math.max(0.0F, this.onBackAmount - 0.19F);
/*      */     } 
/*      */   }
/*      */   
/*      */   private void updateRollAmount() {
/*  563 */     this.rollAmountO = this.rollAmount;
/*  564 */     if (isRolling()) {
/*  565 */       this.rollAmount = Math.min(1.0F, this.rollAmount + 0.15F);
/*      */     } else {
/*  567 */       this.rollAmount = Math.max(0.0F, this.rollAmount - 0.19F);
/*      */     } 
/*      */   }
/*      */   
/*      */   public float getSitAmount(float a) {
/*  572 */     return Mth.lerp(a, this.sitAmountO, this.sitAmount);
/*      */   }
/*      */   
/*      */   public float getLieOnBackAmount(float a) {
/*  576 */     return Mth.lerp(a, this.onBackAmountO, this.onBackAmount);
/*      */   }
/*      */   
/*      */   public float getRollAmount(float a) {
/*  580 */     return Mth.lerp(a, this.rollAmountO, this.rollAmount);
/*      */   }
/*      */   
/*      */   private void handleRoll() {
/*  584 */     this.rollCounter++;
/*  585 */     if (this.rollCounter > 32) {
/*  586 */       roll(false);
/*      */       return;
/*      */     } 
/*  590 */     if (!level().isClientSide()) {
/*  591 */       Vec3 movement = getDeltaMovement();
/*  592 */       if (this.rollCounter == 1) {
/*  593 */         float angle = getYRot() * 0.017453292F;
/*  594 */         float multiplier = isBaby() ? 0.1F : 0.2F;
/*  595 */         this
/*      */           
/*  598 */           .rollDelta = new Vec3(movement.x + (-Mth.sin(angle) * multiplier), 0.0D, movement.z + (Mth.cos(angle) * multiplier));
/*  600 */         setDeltaMovement(this.rollDelta.add(0.0D, 0.27D, 0.0D));
/*  601 */       } else if (this.rollCounter == 7.0F || this.rollCounter == 15.0F || this.rollCounter == 23.0F) {
/*  602 */         setDeltaMovement(0.0D, onGround() ? 0.27D : movement.y, 0.0D);
/*      */       } else {
/*  604 */         setDeltaMovement(this.rollDelta.x, movement.y, this.rollDelta.z);
/*      */       } 
/*      */     } 
/*      */   }
/*      */   
/*      */   private void afterSneeze() {
/*  610 */     Vec3 movement = getDeltaMovement();
/*  611 */     Level level = level();
/*  612 */     level.addParticle((ParticleOptions)ParticleTypes.SNEEZE, getX() - (getBbWidth() + 1.0F) * 0.5D * Mth.sin((this.yBodyRot * 0.017453292F)), getEyeY() - 0.10000000149011612D, getZ() + (getBbWidth() + 1.0F) * 0.5D * Mth.cos((this.yBodyRot * 0.017453292F)), movement.x, 0.0D, movement.z);
/*  613 */     playSound(SoundEvents.PANDA_SNEEZE, 1.0F, 1.0F);
/*  616 */     List<Panda> pandas = level.getEntitiesOfClass(Panda.class, getBoundingBox().inflate(10.0D));
/*  617 */     for (Panda panda : pandas) {
/*  618 */       if (!panda.isBaby() && panda.onGround() && !panda.isInWater() && panda.canPerformAction())
/*  619 */         panda.jumpFromGround(); 
/*      */     } 
/*  624 */     Level level1 = level();
/*  624 */     if (level1 instanceof ServerLevel) {
/*  624 */       ServerLevel serverLevel = (ServerLevel)level1;
/*  624 */       if (((Boolean)serverLevel.getGameRules().get(GameRules.MOB_DROPS)).booleanValue())
/*  625 */         dropFromGiftLootTable(serverLevel, BuiltInLootTables.PANDA_SNEEZE, this::spawnAtLocation); 
/*      */     } 
/*      */   }
/*      */   
/*      */   protected void pickUpItem(ServerLevel level, ItemEntity entity) {
/*  631 */     if (getItemBySlot(EquipmentSlot.MAINHAND).isEmpty() && canPickUpAndEat(entity)) {
/*  632 */       onItemPickup(entity);
/*  633 */       ItemStack itemStack = entity.getItem();
/*  634 */       setItemSlot(EquipmentSlot.MAINHAND, itemStack);
/*  635 */       setGuaranteedDrop(EquipmentSlot.MAINHAND);
/*  636 */       take((Entity)entity, itemStack.getCount());
/*  637 */       entity.discard();
/*      */     } 
/*      */   }
/*      */   
/*      */   public boolean hurtServer(ServerLevel level, DamageSource source, float damage) {
/*  643 */     sit(false);
/*  644 */     return super.hurtServer(level, source, damage);
/*      */   }
/*      */   
/*      */   public SpawnGroupData finalizeSpawn(ServerLevelAccessor level, DifficultyInstance difficulty, EntitySpawnReason spawnReason, SpawnGroupData groupData) {
/*      */     AgeableMob.AgeableMobGroupData ageableMobGroupData;
/*  649 */     RandomSource random = level.getRandom();
/*  650 */     setMainGene(Gene.getRandom(random));
/*  651 */     setHiddenGene(Gene.getRandom(random));
/*  653 */     setAttributes();
/*  655 */     if (groupData == null)
/*  656 */       ageableMobGroupData = new AgeableMob.AgeableMobGroupData(0.2F); 
/*  659 */     return super.finalizeSpawn(level, difficulty, spawnReason, (SpawnGroupData)ageableMobGroupData);
/*      */   }
/*      */   
/*      */   public void setGeneFromParents(Panda parent1, Panda parent2) {
/*  663 */     if (parent2 == null) {
/*  664 */       if (this.random.nextBoolean()) {
/*  665 */         setMainGene(parent1.getOneOfGenesRandomly());
/*  666 */         setHiddenGene(Gene.getRandom(this.random));
/*      */       } else {
/*  668 */         setMainGene(Gene.getRandom(this.random));
/*  669 */         setHiddenGene(parent1.getOneOfGenesRandomly());
/*      */       } 
/*  672 */     } else if (this.random.nextBoolean()) {
/*  673 */       setMainGene(parent1.getOneOfGenesRandomly());
/*  674 */       setHiddenGene(parent2.getOneOfGenesRandomly());
/*      */     } else {
/*  676 */       setMainGene(parent2.getOneOfGenesRandomly());
/*  677 */       setHiddenGene(parent1.getOneOfGenesRandomly());
/*      */     } 
/*  682 */     if (this.random.nextInt(32) == 0)
/*  683 */       setMainGene(Gene.getRandom(this.random)); 
/*  686 */     if (this.random.nextInt(32) == 0)
/*  687 */       setHiddenGene(Gene.getRandom(this.random)); 
/*      */   }
/*      */   
/*      */   private Gene getOneOfGenesRandomly() {
/*  692 */     if (this.random.nextBoolean())
/*  693 */       return getMainGene(); 
/*  696 */     return getHiddenGene();
/*      */   }
/*      */   
/*      */   public void setAttributes() {
/*  700 */     if (isWeak())
/*  701 */       getAttribute(Attributes.MAX_HEALTH).setBaseValue(10.0D); 
/*  704 */     if (isLazy())
/*  705 */       getAttribute(Attributes.MOVEMENT_SPEED).setBaseValue(0.07000000029802322D); 
/*      */   }
/*      */   
/*      */   private void tryToSit() {
/*  710 */     if (!isInWater()) {
/*  711 */       setZza(0.0F);
/*  712 */       getNavigation().stop();
/*  713 */       sit(true);
/*      */     } 
/*      */   }
/*      */   
/*      */   public InteractionResult mobInteract(Player player, InteractionHand hand) {
/*  719 */     ItemStack interactionItemStack = player.getItemInHand(hand);
/*  721 */     if (isScared())
/*  722 */       return (InteractionResult)InteractionResult.PASS; 
/*  725 */     if (isOnBack()) {
/*  726 */       setOnBack(false);
/*  727 */       return (InteractionResult)InteractionResult.SUCCESS;
/*      */     } 
/*  730 */     if (isFood(interactionItemStack)) {
/*  731 */       if (getTarget() != null)
/*  732 */         this.gotBamboo = true; 
/*  735 */       if (canAgeUp()) {
/*  736 */         usePlayerItem(player, hand, interactionItemStack);
/*  737 */         ageUp((int)((-getAge() / 20) * 0.1F), true);
/*      */       } else {
/*  738 */         if (isBaby())
/*  739 */           return (InteractionResult)InteractionResult.PASS; 
/*  740 */         if (!level().isClientSide() && getAge() == 0 && canFallInLove()) {
/*  741 */           usePlayerItem(player, hand, interactionItemStack);
/*  742 */           setInLove(player);
/*      */         } else {
/*  743 */           Level level = level();
/*  743 */           if (level instanceof ServerLevel) {
/*  743 */             ServerLevel serverLevel = (ServerLevel)level;
/*  743 */             if (!isSitting() && !isInWater()) {
/*  744 */               tryToSit();
/*  745 */               eat(true);
/*  747 */               ItemStack pandasCurrentItem = getItemBySlot(EquipmentSlot.MAINHAND);
/*  748 */               if (!pandasCurrentItem.isEmpty() && !player.hasInfiniteMaterials())
/*  749 */                 spawnAtLocation(serverLevel, pandasCurrentItem); 
/*  751 */               setItemSlot(EquipmentSlot.MAINHAND, new ItemStack((ItemLike)interactionItemStack.getItem(), 1));
/*  753 */               usePlayerItem(player, hand, interactionItemStack);
/*      */             } else {
/*  755 */               return (InteractionResult)InteractionResult.PASS;
/*      */             } 
/*      */           } else {
/*  755 */             return (InteractionResult)InteractionResult.PASS;
/*      */           } 
/*      */         } 
/*      */       } 
/*  758 */       return (InteractionResult)InteractionResult.SUCCESS_SERVER;
/*      */     } 
/*  761 */     if (isBaby() && player.isHolding(Items.GOLDEN_DANDELION))
/*  762 */       return super.mobInteract(player, hand); 
/*  765 */     return (InteractionResult)InteractionResult.PASS;
/*      */   }
/*      */   
/*      */   protected SoundEvent getAmbientSound() {
/*  770 */     if (isAggressive())
/*  771 */       return SoundEvents.PANDA_AGGRESSIVE_AMBIENT; 
/*  772 */     if (isWorried())
/*  773 */       return SoundEvents.PANDA_WORRIED_AMBIENT; 
/*  775 */     return SoundEvents.PANDA_AMBIENT;
/*      */   }
/*      */   
/*      */   protected void playStepSound(BlockPos pos, BlockState blockState) {
/*  781 */     playSound(SoundEvents.PANDA_STEP, 0.15F, 1.0F);
/*      */   }
/*      */   
/*      */   public boolean isFood(ItemStack itemStack) {
/*  786 */     return itemStack.is(ItemTags.PANDA_FOOD);
/*      */   }
/*      */   
/*      */   protected SoundEvent getDeathSound() {
/*  791 */     return SoundEvents.PANDA_DEATH;
/*      */   }
/*      */   
/*      */   protected SoundEvent getHurtSound(DamageSource source) {
/*  796 */     return SoundEvents.PANDA_HURT;
/*      */   }
/*      */   
/*      */   public boolean canPerformAction() {
/*  800 */     return (!isOnBack() && !isScared() && !isEating() && !isRolling() && !isSitting());
/*      */   }
/*      */   
/*      */   public EntityDimensions getDefaultDimensions(Pose pose) {
/*  805 */     return isBaby() ? BABY_DIMENSIONS : super.getDefaultDimensions(pose);
/*      */   }
/*      */   
/*      */   private static class PandaMoveControl<T extends Panda> extends MoveControl<T> {
/*      */     public PandaMoveControl(T mob) {
/*  811 */       super((Mob)mob);
/*      */     }
/*      */     
/*      */     public void tick() {
/*  816 */       if (!((Panda)this.mob).canPerformAction())
/*      */         return; 
/*  820 */       super.tick();
/*      */     }
/*      */   }
/*      */   
/*      */   private static class PandaAttackGoal extends MeleeAttackGoal {
/*      */     private final Panda panda;
/*      */     
/*      */     public PandaAttackGoal(Panda mob, double speedModifier, boolean trackTarget) {
/*  828 */       super((PathfinderMob)mob, speedModifier, trackTarget);
/*  829 */       this.panda = mob;
/*      */     }
/*      */     
/*      */     public boolean canUse() {
/*  834 */       return (this.panda.canPerformAction() && super.canUse());
/*      */     }
/*      */   }
/*      */   
/*      */   private static class PandaLookAtPlayerGoal extends LookAtPlayerGoal {
/*      */     private final Panda panda;
/*      */     
/*      */     public PandaLookAtPlayerGoal(Panda mob, Class<? extends LivingEntity> lookAtType, float lookDistance) {
/*  842 */       super((Mob)mob, lookAtType, lookDistance);
/*  843 */       this.panda = mob;
/*      */     }
/*      */     
/*      */     public void setTarget(LivingEntity entity) {
/*  847 */       this.lookAt = (Entity)entity;
/*      */     }
/*      */     
/*      */     public boolean canContinueToUse() {
/*  852 */       return (this.lookAt != null && super.canContinueToUse());
/*      */     }
/*      */     
/*      */     public boolean canUse() {
/*  857 */       if (this.mob.getRandom().nextFloat() >= this.probability)
/*  858 */         return false; 
/*  863 */       if (this.lookAt == null) {
/*  864 */         ServerLevel level = getServerLevel((Entity)this.mob);
/*  865 */         if (this.lookAtType == Player.class) {
/*  866 */           this.lookAt = (Entity)level.getNearestPlayer(this.lookAtContext, (LivingEntity)this.mob, this.mob.getX(), this.mob.getEyeY(), this.mob.getZ());
/*      */         } else {
/*  868 */           this.lookAt = (Entity)level.getNearestEntity(this.mob.level().getEntitiesOfClass(this.lookAtType, this.mob.getBoundingBox().inflate(this.lookDistance, 3.0D, this.lookDistance), entity -> true), this.lookAtContext, (LivingEntity)this.mob, this.mob.getX(), this.mob.getEyeY(), this.mob.getZ());
/*      */         } 
/*      */       } 
/*  872 */       return (this.panda.canPerformAction() && this.lookAt != null);
/*      */     }
/*      */     
/*      */     public void tick() {
/*  877 */       if (this.lookAt != null)
/*  878 */         super.tick(); 
/*      */     }
/*      */   }
/*      */   
/*      */   private static class PandaRollGoal extends Goal {
/*      */     private final Panda panda;
/*      */     
/*      */     public PandaRollGoal(Panda panda) {
/*  887 */       this.panda = panda;
/*  888 */       setFlags(EnumSet.of(Goal.Flag.MOVE, Goal.Flag.LOOK, Goal.Flag.JUMP));
/*      */     }
/*      */     
/*      */     public boolean canUse() {
/*  893 */       if ((!this.panda.isBaby() && !this.panda.isPlayful()) || !this.panda.onGround())
/*  894 */         return false; 
/*  897 */       if (!this.panda.canPerformAction())
/*  898 */         return false; 
/*  901 */       float angle = this.panda.getYRot() * 0.017453292F;
/*  902 */       float xDir = -Mth.sin(angle);
/*  903 */       float zDir = Mth.cos(angle);
/*  904 */       int xStep = (Math.abs(xDir) > 0.5D) ? Mth.sign(xDir) : 0;
/*  905 */       int zStep = (Math.abs(zDir) > 0.5D) ? Mth.sign(zDir) : 0;
/*  907 */       if (this.panda.level().getBlockState(this.panda.blockPosition().offset(xStep, -1, zStep)).isAir())
/*  908 */         return true; 
/*  911 */       if (this.panda.isPlayful() && this.panda.random.nextInt(reducedTickDelay(60)) == 1)
/*  912 */         return true; 
/*  915 */       return (this.panda.random.nextInt(reducedTickDelay(500)) == 1);
/*      */     }
/*      */     
/*      */     public boolean canContinueToUse() {
/*  920 */       return false;
/*      */     }
/*      */     
/*      */     public void start() {
/*  925 */       this.panda.roll(true);
/*      */     }
/*      */     
/*      */     public boolean isInterruptable() {
/*  930 */       return false;
/*      */     }
/*      */   }
/*      */   
/*      */   private static class PandaSneezeGoal extends Goal {
/*      */     private final Panda panda;
/*      */     
/*      */     public PandaSneezeGoal(Panda panda) {
/*  939 */       this.panda = panda;
/*      */     }
/*      */     
/*      */     public boolean canUse() {
/*  944 */       if (!this.panda.isBaby() || !this.panda.canPerformAction())
/*  945 */         return false; 
/*  948 */       if (this.panda.isWeak() && this.panda.random.nextInt(reducedTickDelay(500)) == 1)
/*  949 */         return true; 
/*  952 */       return (this.panda.random.nextInt(reducedTickDelay(6000)) == 1);
/*      */     }
/*      */     
/*      */     public boolean canContinueToUse() {
/*  957 */       return false;
/*      */     }
/*      */     
/*      */     public void start() {
/*  962 */       this.panda.sneeze(true);
/*      */     }
/*      */   }
/*      */   
/*      */   private static class PandaBreedGoal extends BreedGoal {
/*      */     private final Panda panda;
/*      */     
/*      */     private int unhappyCooldown;
/*      */     
/*      */     public PandaBreedGoal(Panda panda, double speedModifier) {
/*  971 */       super(panda, speedModifier);
/*  972 */       this.panda = panda;
/*      */     }
/*      */     
/*      */     public boolean canUse() {
/*  977 */       if (super.canUse() && this.panda.getUnhappyCounter() == 0) {
/*  978 */         if (!canFindBamboo()) {
/*  979 */           if (this.unhappyCooldown <= this.panda.tickCount) {
/*  980 */             this.panda.setUnhappyCounter(32);
/*  981 */             this.unhappyCooldown = this.panda.tickCount + 600;
/*  982 */             if (this.panda.isEffectiveAi()) {
/*  983 */               Player player = this.level.getNearestPlayer(Panda.BREED_TARGETING, (LivingEntity)this.panda);
/*  984 */               this.panda.lookAtPlayerGoal.setTarget((LivingEntity)player);
/*      */             } 
/*      */           } 
/*  988 */           return false;
/*      */         } 
/*  991 */         return true;
/*      */       } 
/*  994 */       return false;
/*      */     }
/*      */     
/*      */     private boolean canFindBamboo() {
/*  998 */       BlockPos pandaPos = this.panda.blockPosition();
/*  999 */       BlockPos.MutableBlockPos pos = new BlockPos.MutableBlockPos();
/* 1000 */       for (int yOff = 0; yOff < 3; yOff++) {
/* 1001 */         for (int r = 0; r < 8; r++) {
/*      */           int x;
/* 1002 */           for (x = 0; x <= r; x = (x > 0) ? -x : (1 - x)) {
/* 1003 */             int z = (x < r && x > -r) ? r : 0;
/* 1004 */             for (; z <= r; z = (z > 0) ? -z : (1 - z)) {
/* 1005 */               pos.setWithOffset((Vec3i)pandaPos, x, yOff, z);
/* 1006 */               if (this.level.getBlockState((BlockPos)pos).is(Blocks.BAMBOO))
/* 1007 */                 return true; 
/*      */             } 
/*      */           } 
/*      */         } 
/*      */       } 
/* 1013 */       return false;
/*      */     }
/*      */   }
/*      */   
/*      */   private static class PandaAvoidGoal<T extends LivingEntity> extends AvoidEntityGoal<T> {
/*      */     private final Panda panda;
/*      */     
/*      */     public PandaAvoidGoal(Panda panda, Class<T> avoidClass, float maxDist, double walkSpeedModifier, double sprintSpeedModifier) {
/* 1021 */       super((PathfinderMob)panda, avoidClass, maxDist, walkSpeedModifier, sprintSpeedModifier, EntitySelector.NO_SPECTATORS);
/* 1023 */       this.panda = panda;
/*      */     }
/*      */     
/*      */     public boolean canUse() {
/* 1028 */       return (this.panda.isWorried() && this.panda.canPerformAction() && super.canUse());
/*      */     }
/*      */   }
/*      */   
/*      */   private static boolean canPickUpAndEat(ItemEntity entity) {
/* 1033 */     return (entity.getItem().is(ItemTags.PANDA_EATS_FROM_GROUND) && entity.isAlive() && !entity.hasPickUpDelay());
/*      */   }
/*      */   
/*      */   private class PandaSitGoal extends Goal {
/*      */     private int cooldown;
/*      */     
/*      */     public PandaSitGoal() {
/* 1040 */       setFlags(EnumSet.of(Goal.Flag.MOVE));
/*      */     }
/*      */     
/*      */     public boolean canUse() {
/* 1045 */       if (this.cooldown > Panda.this.tickCount || Panda.this.isBaby() || Panda.this.isInWater() || !Panda.this.canPerformAction() || Panda.this.getUnhappyCounter() > 0)
/* 1046 */         return false; 
/* 1050 */       if (!Panda.this.getItemBySlot(EquipmentSlot.MAINHAND).isEmpty())
/* 1051 */         return true; 
/* 1054 */       return !Panda.this.level().getEntitiesOfClass(ItemEntity.class, Panda.this.getBoundingBox().inflate(6.0D, 6.0D, 6.0D), Panda::canPickUpAndEat).isEmpty();
/*      */     }
/*      */     
/*      */     public boolean canContinueToUse() {
/* 1059 */       if (Panda.this.isInWater() || (!Panda.this.isLazy() && Panda.this.random.nextInt(reducedTickDelay(600)) == 1))
/* 1060 */         return false; 
/* 1063 */       if (Panda.this.random.nextInt(reducedTickDelay(2000)) == 1)
/* 1064 */         return false; 
/* 1067 */       return true;
/*      */     }
/*      */     
/*      */     public void tick() {
/* 1072 */       if (!Panda.this.isSitting() && !Panda.this.getItemBySlot(EquipmentSlot.MAINHAND).isEmpty())
/* 1073 */         Panda.this.tryToSit(); 
/*      */     }
/*      */     
/*      */     public void start() {
/* 1079 */       if (Panda.this.getItemBySlot(EquipmentSlot.MAINHAND).isEmpty()) {
/* 1080 */         List<ItemEntity> items = Panda.this.level().getEntitiesOfClass(ItemEntity.class, Panda.this.getBoundingBox().inflate(8.0D, 8.0D, 8.0D), Panda::canPickUpAndEat);
/* 1081 */         if (!items.isEmpty())
/* 1082 */           Panda.this.getNavigation().moveTo((Entity)items.getFirst(), 1.2000000476837158D); 
/*      */       } else {
/* 1085 */         Panda.this.tryToSit();
/*      */       } 
/* 1088 */       this.cooldown = 0;
/*      */     }
/*      */     
/*      */     public void stop() {
/* 1093 */       ItemStack itemStack = Panda.this.getItemBySlot(EquipmentSlot.MAINHAND);
/* 1094 */       if (!itemStack.isEmpty()) {
/* 1095 */         Panda.this.spawnAtLocation(getServerLevel(Panda.this.level()), itemStack);
/* 1096 */         Panda.this.setItemSlot(EquipmentSlot.MAINHAND, ItemStack.EMPTY);
/* 1097 */         int waitSeconds = Panda.this.isLazy() ? (Panda.this.random.nextInt(50) + 10) : (Panda.this.random.nextInt(150) + 10);
/* 1098 */         this.cooldown = Panda.this.tickCount + waitSeconds * 20;
/*      */       } 
/* 1101 */       Panda.this.sit(false);
/*      */     }
/*      */   }
/*      */   
/*      */   private static class PandaLieOnBackGoal extends Goal {
/*      */     private final Panda panda;
/*      */     
/*      */     private int cooldown;
/*      */     
/*      */     public PandaLieOnBackGoal(Panda panda) {
/* 1110 */       this.panda = panda;
/*      */     }
/*      */     
/*      */     public boolean canUse() {
/* 1115 */       return (this.cooldown < this.panda.tickCount && this.panda.isLazy() && this.panda.canPerformAction() && this.panda.random.nextInt(reducedTickDelay(400)) == 1);
/*      */     }
/*      */     
/*      */     public boolean canContinueToUse() {
/* 1120 */       if (this.panda.isInWater() || (!this.panda.isLazy() && this.panda.random.nextInt(reducedTickDelay(600)) == 1))
/* 1121 */         return false; 
/* 1124 */       if (this.panda.random.nextInt(reducedTickDelay(2000)) == 1)
/* 1125 */         return false; 
/* 1128 */       return true;
/*      */     }
/*      */     
/*      */     public void start() {
/* 1133 */       this.panda.setOnBack(true);
/* 1134 */       this.cooldown = 0;
/*      */     }
/*      */     
/*      */     public void stop() {
/* 1139 */       this.panda.setOnBack(false);
/* 1140 */       this.cooldown = this.panda.tickCount + 200;
/*      */     }
/*      */   }
/*      */   
/*      */   private static class PandaHurtByTargetGoal extends HurtByTargetGoal {
/*      */     private final Panda panda;
/*      */     
/*      */     public PandaHurtByTargetGoal(Panda mob, Class<?>... ignoreDamageFromTheseTypes) {
/* 1148 */       super((PathfinderMob)mob, ignoreDamageFromTheseTypes);
/* 1149 */       this.panda = mob;
/*      */     }
/*      */     
/*      */     public boolean canContinueToUse() {
/* 1154 */       if (this.panda.gotBamboo || this.panda.didBite) {
/* 1155 */         this.panda.setTarget(null);
/* 1156 */         return false;
/*      */       } 
/* 1158 */       return super.canContinueToUse();
/*      */     }
/*      */     
/*      */     protected void alertOther(Mob other, LivingEntity hurtByMob) {
/* 1163 */       if (other instanceof Panda && other.isAggressive())
/* 1164 */         other.setTarget(hurtByMob); 
/*      */     }
/*      */   }
/*      */   
/*      */   private static class PandaPanicGoal extends PanicGoal {
/*      */     private final Panda panda;
/*      */     
/*      */     public PandaPanicGoal(Panda mob, double speedModifier) {
/* 1174 */       super((PathfinderMob)mob, speedModifier, DamageTypeTags.PANIC_ENVIRONMENTAL_CAUSES);
/* 1175 */       this.panda = mob;
/*      */     }
/*      */     
/*      */     public boolean canContinueToUse() {
/* 1180 */       if (this.panda.isSitting()) {
/* 1181 */         this.panda.getNavigation().stop();
/* 1182 */         return false;
/*      */       } 
/* 1184 */       return super.canContinueToUse();
/*      */     }
/*      */   }
/*      */ }


/* Location:              C:\Users\karol\AppData\Roaming\ModrinthApp\meta\versions\26.2-0.19.5\26.2-0.19.5.jar!\net\minecraft\world\entity\animal\panda\Panda.class
 * Java compiler version: 25 (69.0)
 * JD-Core Version:       1.1.3
 */