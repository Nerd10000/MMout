/*     */ package net.minecraft.world.entity.boss.enderdragon;
/*     */ 
/*     */ import java.util.Optional;
/*     */ import net.minecraft.core.BlockPos;
/*     */ import net.minecraft.network.syncher.EntityDataAccessor;
/*     */ import net.minecraft.network.syncher.EntityDataSerializers;
/*     */ import net.minecraft.network.syncher.SynchedEntityData;
/*     */ import net.minecraft.server.level.ServerLevel;
/*     */ import net.minecraft.tags.DamageTypeTags;
/*     */ import net.minecraft.world.damagesource.DamageSource;
/*     */ import net.minecraft.world.entity.Entity;
/*     */ import net.minecraft.world.entity.EntityType;
/*     */ import net.minecraft.world.entity.EntityTypes;
/*     */ import net.minecraft.world.item.ItemStack;
/*     */ import net.minecraft.world.item.Items;
/*     */ import net.minecraft.world.level.BlockGetter;
/*     */ import net.minecraft.world.level.ItemLike;
/*     */ import net.minecraft.world.level.Level;
/*     */ import net.minecraft.world.level.block.BaseFireBlock;
/*     */ import net.minecraft.world.level.dimension.end.EnderDragonFight;
/*     */ import net.minecraft.world.level.storage.ValueInput;
/*     */ import net.minecraft.world.level.storage.ValueOutput;
/*     */ 
/*     */ public class EndCrystal extends Entity {
/*  25 */   private static final EntityDataAccessor<Optional<BlockPos>> DATA_BEAM_TARGET = SynchedEntityData.defineId(EndCrystal.class, EntityDataSerializers.OPTIONAL_BLOCK_POS);
/*     */   
/*  26 */   private static final EntityDataAccessor<Boolean> DATA_SHOW_BOTTOM = SynchedEntityData.defineId(EndCrystal.class, EntityDataSerializers.BOOLEAN);
/*     */   
/*     */   private static final boolean DEFAULT_SHOW_BOTTOM = true;
/*     */   
/*     */   public int time;
/*     */   
/*     */   public EndCrystal(EntityType<? extends EndCrystal> type, Level level) {
/*  32 */     super(type, level);
/*  33 */     this.blocksBuilding = true;
/*  35 */     this.time = this.random.nextInt(100000);
/*     */   }
/*     */   
/*     */   public EndCrystal(Level level, double x, double y, double z) {
/*  39 */     this(EntityTypes.END_CRYSTAL, level);
/*  40 */     setPos(x, y, z);
/*     */   }
/*     */   
/*     */   protected Entity.MovementEmission getMovementEmission() {
/*  45 */     return Entity.MovementEmission.NONE;
/*     */   }
/*     */   
/*     */   protected void defineSynchedData(SynchedEntityData.Builder entityData) {
/*  50 */     entityData.define(DATA_BEAM_TARGET, Optional.empty());
/*  51 */     entityData.define(DATA_SHOW_BOTTOM, Boolean.valueOf(true));
/*     */   }
/*     */   
/*     */   public void tick() {
/*  56 */     this.time++;
/*  57 */     applyEffectsFromBlocks();
/*  58 */     handlePortal();
/*  60 */     if (level() instanceof ServerLevel) {
/*  61 */       BlockPos pos = blockPosition();
/*  62 */       if (((ServerLevel)level()).getDragonFight() != null && level().getBlockState(pos).isAir())
/*  63 */         level().setBlockAndUpdate(pos, BaseFireBlock.getState((BlockGetter)level(), pos)); 
/*     */     } 
/*     */   }
/*     */   
/*     */   protected void addAdditionalSaveData(ValueOutput output) {
/*  70 */     output.storeNullable("beam_target", BlockPos.CODEC, getBeamTarget());
/*  71 */     output.putBoolean("ShowBottom", showsBottom());
/*     */   }
/*     */   
/*     */   protected void readAdditionalSaveData(ValueInput input) {
/*  76 */     setBeamTarget(input.read("beam_target", BlockPos.CODEC).orElse(null));
/*  77 */     setShowBottom(input.getBooleanOr("ShowBottom", true));
/*     */   }
/*     */   
/*     */   public boolean isPickable() {
/*  82 */     return true;
/*     */   }
/*     */   
/*     */   public final boolean hurtClient(DamageSource source) {
/*  87 */     if (isInvulnerableToBase(source))
/*  88 */       return false; 
/*  90 */     if (source.getEntity() instanceof EnderDragon)
/*  91 */       return false; 
/*  93 */     return true;
/*     */   }
/*     */   
/*     */   public final boolean hurtServer(ServerLevel level, DamageSource source, float damage) {
/*  98 */     if (isInvulnerableToBase(source))
/*  99 */       return false; 
/* 101 */     if (source.getEntity() instanceof EnderDragon)
/* 102 */       return false; 
/* 104 */     if (!isRemoved()) {
/* 105 */       remove(Entity.RemovalReason.KILLED);
/* 107 */       if (!source.is(DamageTypeTags.IS_EXPLOSION)) {
/* 108 */         DamageSource damageSource = (source.getEntity() != null) ? damageSources().explosion(this, source.getEntity()) : null;
/* 109 */         level.explode(this, damageSource, null, getX(), getY(), getZ(), 6.0F, false, Level.ExplosionInteraction.BLOCK);
/*     */       } 
/* 112 */       onDestroyedBy(level, source);
/*     */     } 
/* 114 */     return true;
/*     */   }
/*     */   
/*     */   public void kill(ServerLevel level) {
/* 119 */     onDestroyedBy(level, damageSources().generic());
/* 120 */     super.kill(level);
/*     */   }
/*     */   
/*     */   private void onDestroyedBy(ServerLevel level, DamageSource source) {
/* 124 */     EnderDragonFight fight = level.getDragonFight();
/* 125 */     if (fight != null)
/* 126 */       fight.onCrystalDestroyed(this, source); 
/*     */   }
/*     */   
/*     */   public void setBeamTarget(BlockPos target) {
/* 131 */     getEntityData().set(DATA_BEAM_TARGET, Optional.ofNullable(target));
/*     */   }
/*     */   
/*     */   public BlockPos getBeamTarget() {
/* 135 */     return ((Optional<BlockPos>)getEntityData().get(DATA_BEAM_TARGET)).orElse(null);
/*     */   }
/*     */   
/*     */   public void setShowBottom(boolean showBottom) {
/* 139 */     getEntityData().set(DATA_SHOW_BOTTOM, Boolean.valueOf(showBottom));
/*     */   }
/*     */   
/*     */   public boolean showsBottom() {
/* 143 */     return ((Boolean)getEntityData().get(DATA_SHOW_BOTTOM)).booleanValue();
/*     */   }
/*     */   
/*     */   public boolean shouldRenderAtSqrDistance(double distance) {
/* 148 */     return (super.shouldRenderAtSqrDistance(distance) || getBeamTarget() != null);
/*     */   }
/*     */   
/*     */   public ItemStack getPickResult() {
/* 153 */     return new ItemStack((ItemLike)Items.END_CRYSTAL);
/*     */   }
/*     */ }


/* Location:              C:\Users\karol\AppData\Roaming\ModrinthApp\meta\versions\26.2-0.19.5\26.2-0.19.5.jar!\net\minecraft\world\entity\boss\enderdragon\EndCrystal.class
 * Java compiler version: 25 (69.0)
 * JD-Core Version:       1.1.3
 */