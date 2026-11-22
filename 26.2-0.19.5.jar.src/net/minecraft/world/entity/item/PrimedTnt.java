/*     */ package net.minecraft.world.entity.item;
/*     */ 
/*     */ import java.util.Optional;
/*     */ import net.minecraft.core.BlockPos;
/*     */ import net.minecraft.core.particles.ParticleOptions;
/*     */ import net.minecraft.core.particles.ParticleTypes;
/*     */ import net.minecraft.network.syncher.EntityDataAccessor;
/*     */ import net.minecraft.network.syncher.EntityDataSerializers;
/*     */ import net.minecraft.network.syncher.SynchedEntityData;
/*     */ import net.minecraft.server.level.ServerLevel;
/*     */ import net.minecraft.util.Mth;
/*     */ import net.minecraft.util.RandomSource;
/*     */ import net.minecraft.world.damagesource.DamageSource;
/*     */ import net.minecraft.world.entity.Entity;
/*     */ import net.minecraft.world.entity.EntityReference;
/*     */ import net.minecraft.world.entity.EntityType;
/*     */ import net.minecraft.world.entity.EntityTypes;
/*     */ import net.minecraft.world.entity.LivingEntity;
/*     */ import net.minecraft.world.entity.MoverType;
/*     */ import net.minecraft.world.entity.TraceableEntity;
/*     */ import net.minecraft.world.level.BlockGetter;
/*     */ import net.minecraft.world.level.Explosion;
/*     */ import net.minecraft.world.level.ExplosionDamageCalculator;
/*     */ import net.minecraft.world.level.Level;
/*     */ import net.minecraft.world.level.block.Blocks;
/*     */ import net.minecraft.world.level.block.state.BlockState;
/*     */ import net.minecraft.world.level.entity.UniquelyIdentifyable;
/*     */ import net.minecraft.world.level.gamerules.GameRules;
/*     */ import net.minecraft.world.level.material.FluidState;
/*     */ import net.minecraft.world.level.portal.TeleportTransition;
/*     */ import net.minecraft.world.level.storage.ValueInput;
/*     */ import net.minecraft.world.level.storage.ValueOutput;
/*     */ 
/*     */ public class PrimedTnt extends Entity implements TraceableEntity {
/*  36 */   private static final EntityDataAccessor<Integer> DATA_FUSE_ID = SynchedEntityData.defineId(PrimedTnt.class, EntityDataSerializers.INT);
/*     */   
/*  37 */   private static final EntityDataAccessor<BlockState> DATA_BLOCK_STATE_ID = SynchedEntityData.defineId(PrimedTnt.class, EntityDataSerializers.BLOCK_STATE);
/*     */   
/*     */   public static final short DEFAULT_FUSE_TIME = 80;
/*     */   
/*     */   public static final int NO_FUSE = -1;
/*     */   
/*     */   private static final float DEFAULT_EXPLOSION_POWER = 4.0F;
/*     */   
/*  41 */   private static final BlockState DEFAULT_BLOCK_STATE = Blocks.TNT.defaultBlockState();
/*     */   
/*     */   private static final String TAG_BLOCK_STATE = "block_state";
/*     */   
/*     */   public static final String TAG_FUSE = "fuse";
/*     */   
/*     */   private static final String TAG_EXPLOSION_POWER = "explosion_power";
/*     */   
/*  47 */   public static final ExplosionDamageCalculator USED_PORTAL_DAMAGE_CALCULATOR = new ExplosionDamageCalculator() {
/*     */       public boolean shouldBlockExplode(Explosion explosion, BlockGetter level, BlockPos pos, BlockState state, float power) {
/*  50 */         if (state.is(Blocks.NETHER_PORTAL))
/*  51 */           return false; 
/*  53 */         return super.shouldBlockExplode(explosion, level, pos, state, power);
/*     */       }
/*     */       
/*     */       public Optional<Float> getBlockExplosionResistance(Explosion explosion, BlockGetter level, BlockPos pos, BlockState block, FluidState fluid) {
/*  58 */         if (block.is(Blocks.NETHER_PORTAL))
/*  59 */           return Optional.empty(); 
/*  61 */         return super.getBlockExplosionResistance(explosion, level, pos, block, fluid);
/*     */       }
/*     */     };
/*     */   
/*     */   private EntityReference<LivingEntity> owner;
/*     */   
/*     */   private boolean usedPortal;
/*     */   
/*  67 */   private float explosionPower = 4.0F;
/*     */   
/*     */   public PrimedTnt(EntityType<? extends PrimedTnt> type, Level level) {
/*  70 */     super(type, level);
/*  71 */     this.blocksBuilding = true;
/*     */   }
/*     */   
/*     */   public PrimedTnt(Level level, double x, double y, double z, LivingEntity owner) {
/*  75 */     this(EntityTypes.TNT, level);
/*  77 */     setPos(x, y, z);
/*  79 */     double rot = level.getRandom().nextDouble() * 6.2831854820251465D;
/*  81 */     setDeltaMovement(
/*  82 */         -Math.sin(rot) * 0.02D, 0.20000000298023224D, 
/*     */         
/*  84 */         -Math.cos(rot) * 0.02D);
/*  87 */     setFuse(80);
/*  89 */     this.xo = x;
/*  90 */     this.yo = y;
/*  91 */     this.zo = z;
/*  92 */     this.owner = EntityReference.of((UniquelyIdentifyable)owner);
/*     */   }
/*     */   
/*     */   protected void defineSynchedData(SynchedEntityData.Builder entityData) {
/*  97 */     entityData.define(DATA_FUSE_ID, Integer.valueOf(80));
/*  98 */     entityData.define(DATA_BLOCK_STATE_ID, DEFAULT_BLOCK_STATE);
/*     */   }
/*     */   
/*     */   protected Entity.MovementEmission getMovementEmission() {
/* 103 */     return Entity.MovementEmission.NONE;
/*     */   }
/*     */   
/*     */   public boolean isPickable() {
/* 108 */     return !isRemoved();
/*     */   }
/*     */   
/*     */   protected double getDefaultGravity() {
/* 113 */     return 0.04D;
/*     */   }
/*     */   
/*     */   public void tick() {
/* 118 */     handlePortal();
/* 119 */     applyGravity();
/* 120 */     move(MoverType.SELF, getDeltaMovement());
/* 121 */     applyEffectsFromBlocks();
/* 122 */     setDeltaMovement(getDeltaMovement().scale(getAirDrag()));
/* 124 */     if (onGround())
/* 126 */       setDeltaMovement(getDeltaMovement().multiply(0.7D, -0.5D, 0.7D)); 
/* 129 */     int fuse = getFuse() - 1;
/* 130 */     setFuse(fuse);
/* 131 */     if (fuse <= 0) {
/* 132 */       discard();
/* 133 */       if (!level().isClientSide())
/* 134 */         explode(); 
/*     */     } else {
/* 137 */       updateFluidInteraction();
/* 138 */       if (level().isClientSide())
/* 139 */         level().addParticle((ParticleOptions)ParticleTypes.SMOKE, getX(), getY() + 0.5D, getZ(), 0.0D, 0.0D, 0.0D); 
/*     */     } 
/*     */   }
/*     */   
/*     */   private void explode() {
/* 145 */     Level level = level();
/* 145 */     if (level instanceof ServerLevel) {
/* 145 */       ServerLevel serverLevel = (ServerLevel)level;
/* 145 */       if (((Boolean)serverLevel.getGameRules().get(GameRules.TNT_EXPLODES)).booleanValue())
/* 146 */         level().explode(this, Explosion.getDefaultDamageSource(level(), this), this.usedPortal ? USED_PORTAL_DAMAGE_CALCULATOR : null, getX(), getY(0.0625D), getZ(), this.explosionPower, false, Level.ExplosionInteraction.TNT); 
/*     */     } 
/*     */   }
/*     */   
/*     */   protected void addAdditionalSaveData(ValueOutput output) {
/* 152 */     output.putShort("fuse", (short)getFuse());
/* 153 */     output.store("block_state", BlockState.CODEC, getBlockState());
/* 154 */     if (this.explosionPower != 4.0F)
/* 155 */       output.putFloat("explosion_power", this.explosionPower); 
/* 157 */     EntityReference.store(this.owner, output, "owner");
/*     */   }
/*     */   
/*     */   protected void readAdditionalSaveData(ValueInput input) {
/* 162 */     setFuse(input.getShortOr("fuse", (short)80));
/* 163 */     setBlockState(input.read("block_state", BlockState.CODEC).orElse(DEFAULT_BLOCK_STATE));
/* 164 */     this.explosionPower = Mth.clamp(input.getFloatOr("explosion_power", 4.0F), 0.0F, 128.0F);
/* 165 */     this.owner = EntityReference.read(input, "owner");
/*     */   }
/*     */   
/*     */   public LivingEntity getOwner() {
/* 170 */     return EntityReference.getLivingEntity(this.owner, level());
/*     */   }
/*     */   
/*     */   public void restoreFrom(Entity oldEntity) {
/* 175 */     super.restoreFrom(oldEntity);
/* 176 */     if (oldEntity instanceof PrimedTnt) {
/* 176 */       PrimedTnt primedTnt = (PrimedTnt)oldEntity;
/* 177 */       this.owner = primedTnt.owner;
/*     */     } 
/*     */   }
/*     */   
/*     */   public void setFuse(int time) {
/* 182 */     this.entityData.set(DATA_FUSE_ID, Integer.valueOf(time));
/*     */   }
/*     */   
/*     */   public int getFuse() {
/* 186 */     return ((Integer)this.entityData.get(DATA_FUSE_ID)).intValue();
/*     */   }
/*     */   
/*     */   public static int getRandomShortFuse(int fuse, RandomSource random) {
/* 190 */     return random.nextInt(Math.max(1, fuse / 4)) + fuse / 8;
/*     */   }
/*     */   
/*     */   public void setBlockState(BlockState blockState) {
/* 194 */     this.entityData.set(DATA_BLOCK_STATE_ID, blockState);
/*     */   }
/*     */   
/*     */   public BlockState getBlockState() {
/* 198 */     return (BlockState)this.entityData.get(DATA_BLOCK_STATE_ID);
/*     */   }
/*     */   
/*     */   private void setUsedPortal(boolean usedPortal) {
/* 202 */     this.usedPortal = usedPortal;
/*     */   }
/*     */   
/*     */   public Entity teleport(TeleportTransition transition) {
/* 207 */     Entity newEntity = super.teleport(transition);
/* 208 */     if (newEntity instanceof PrimedTnt) {
/* 208 */       PrimedTnt tnt = (PrimedTnt)newEntity;
/* 209 */       tnt.setUsedPortal(true);
/*     */     } 
/* 211 */     return newEntity;
/*     */   }
/*     */   
/*     */   public final boolean hurtServer(ServerLevel level, DamageSource source, float damage) {
/* 216 */     return false;
/*     */   }
/*     */ }


/* Location:              C:\Users\karol\AppData\Roaming\ModrinthApp\meta\versions\26.2-0.19.5\26.2-0.19.5.jar!\net\minecraft\world\entity\item\PrimedTnt.class
 * Java compiler version: 25 (69.0)
 * JD-Core Version:       1.1.3
 */