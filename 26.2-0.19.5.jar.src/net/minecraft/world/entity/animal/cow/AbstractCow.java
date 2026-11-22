/*     */ package net.minecraft.world.entity.animal.cow;
/*     */ 
/*     */ import net.minecraft.core.BlockPos;
/*     */ import net.minecraft.sounds.SoundEvent;
/*     */ import net.minecraft.sounds.SoundEvents;
/*     */ import net.minecraft.tags.ItemTags;
/*     */ import net.minecraft.world.InteractionHand;
/*     */ import net.minecraft.world.InteractionResult;
/*     */ import net.minecraft.world.damagesource.DamageSource;
/*     */ import net.minecraft.world.entity.EntityDimensions;
/*     */ import net.minecraft.world.entity.EntityType;
/*     */ import net.minecraft.world.entity.EntityTypes;
/*     */ import net.minecraft.world.entity.Mob;
/*     */ import net.minecraft.world.entity.PathfinderMob;
/*     */ import net.minecraft.world.entity.Pose;
/*     */ import net.minecraft.world.entity.ai.attributes.AttributeSupplier;
/*     */ import net.minecraft.world.entity.ai.attributes.Attributes;
/*     */ import net.minecraft.world.entity.ai.goal.BreedGoal;
/*     */ import net.minecraft.world.entity.ai.goal.FloatGoal;
/*     */ import net.minecraft.world.entity.ai.goal.FollowParentGoal;
/*     */ import net.minecraft.world.entity.ai.goal.Goal;
/*     */ import net.minecraft.world.entity.ai.goal.LookAtPlayerGoal;
/*     */ import net.minecraft.world.entity.ai.goal.PanicGoal;
/*     */ import net.minecraft.world.entity.ai.goal.RandomLookAroundGoal;
/*     */ import net.minecraft.world.entity.ai.goal.TemptGoal;
/*     */ import net.minecraft.world.entity.ai.goal.WaterAvoidingRandomStrollGoal;
/*     */ import net.minecraft.world.entity.animal.Animal;
/*     */ import net.minecraft.world.entity.player.Player;
/*     */ import net.minecraft.world.item.ItemStack;
/*     */ import net.minecraft.world.item.ItemUtils;
/*     */ import net.minecraft.world.item.Items;
/*     */ import net.minecraft.world.level.Level;
/*     */ import net.minecraft.world.level.block.state.BlockState;
/*     */ 
/*     */ public abstract class AbstractCow extends Animal {
/*  33 */   private static final EntityDimensions BABY_DIMENSIONS = EntityTypes.COW.getDimensions().scale(0.5F).withEyeHeight(0.665F);
/*     */   
/*     */   public AbstractCow(EntityType<? extends AbstractCow> type, Level level) {
/*  36 */     super(type, level);
/*     */   }
/*     */   
/*     */   protected void registerGoals() {
/*  41 */     this.goalSelector.addGoal(0, (Goal)new FloatGoal((Mob)this));
/*  42 */     this.goalSelector.addGoal(1, (Goal)new PanicGoal((PathfinderMob)this, 2.0D));
/*  43 */     this.goalSelector.addGoal(2, (Goal)new BreedGoal(this, 1.0D));
/*  44 */     this.goalSelector.addGoal(3, (Goal)new TemptGoal((PathfinderMob)this, 1.25D, i -> i.is(ItemTags.COW_FOOD), false));
/*  45 */     this.goalSelector.addGoal(4, (Goal)new FollowParentGoal(this, 1.25D));
/*  46 */     this.goalSelector.addGoal(5, (Goal)new WaterAvoidingRandomStrollGoal((PathfinderMob)this, 1.0D));
/*  47 */     this.goalSelector.addGoal(6, (Goal)new LookAtPlayerGoal((Mob)this, Player.class, 6.0F));
/*  48 */     this.goalSelector.addGoal(7, (Goal)new RandomLookAroundGoal((Mob)this));
/*     */   }
/*     */   
/*     */   public boolean isFood(ItemStack itemStack) {
/*  53 */     return itemStack.is(ItemTags.COW_FOOD);
/*     */   }
/*     */   
/*     */   public static AttributeSupplier.Builder createAttributes() {
/*  57 */     return Animal.createAnimalAttributes()
/*  58 */       .add(Attributes.MAX_HEALTH, 10.0D)
/*  59 */       .add(Attributes.MOVEMENT_SPEED, 0.20000000298023224D);
/*     */   }
/*     */   
/*     */   protected SoundEvent getAmbientSound() {
/*  64 */     return (SoundEvent)getSoundSet().ambientSound().value();
/*     */   }
/*     */   
/*     */   protected SoundEvent getHurtSound(DamageSource source) {
/*  69 */     return (SoundEvent)getSoundSet().hurtSound().value();
/*     */   }
/*     */   
/*     */   protected SoundEvent getDeathSound() {
/*  74 */     return (SoundEvent)getSoundSet().deathSound().value();
/*     */   }
/*     */   
/*     */   protected void playStepSound(BlockPos pos, BlockState blockState) {
/*  79 */     playSound((SoundEvent)getSoundSet().stepSound().value(), 0.15F, 1.0F);
/*     */   }
/*     */   
/*     */   protected float getSoundVolume() {
/*  84 */     return 0.4F;
/*     */   }
/*     */   
/*     */   protected CowSoundVariant getSoundSet() {
/*  88 */     return (CowSoundVariant)SoundEvents.COW_SOUNDS.get(CowSoundVariants.SoundSet.CLASSIC);
/*     */   }
/*     */   
/*     */   public InteractionResult mobInteract(Player player, InteractionHand hand) {
/*  93 */     ItemStack itemStack = player.getItemInHand(hand);
/*  94 */     if (itemStack.is(Items.BUCKET) && !isBaby()) {
/*  95 */       player.playSound(SoundEvents.COW_MILK, 1.0F, 1.0F);
/*  96 */       ItemStack bucketOrMilkBucket = ItemUtils.createFilledResult(itemStack, player, Items.MILK_BUCKET.getDefaultInstance());
/*  97 */       player.setItemInHand(hand, bucketOrMilkBucket);
/*  98 */       return (InteractionResult)InteractionResult.SUCCESS;
/*     */     } 
/* 100 */     return super.mobInteract(player, hand);
/*     */   }
/*     */   
/*     */   public EntityDimensions getDefaultDimensions(Pose pose) {
/* 105 */     return isBaby() ? BABY_DIMENSIONS : super.getDefaultDimensions(pose);
/*     */   }
/*     */ }


/* Location:              C:\Users\karol\AppData\Roaming\ModrinthApp\meta\versions\26.2-0.19.5\26.2-0.19.5.jar!\net\minecraft\world\entity\animal\cow\AbstractCow.class
 * Java compiler version: 25 (69.0)
 * JD-Core Version:       1.1.3
 */