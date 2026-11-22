/*     */ package net.minecraft.world.level.block.entity;
/*     */ 
/*     */ import java.util.List;
/*     */ import java.util.function.Function;
/*     */ import java.util.function.Predicate;
/*     */ import net.minecraft.core.BlockPos;
/*     */ import net.minecraft.core.Direction;
/*     */ import net.minecraft.core.Holder;
/*     */ import net.minecraft.core.Position;
/*     */ import net.minecraft.core.Vec3i;
/*     */ import net.minecraft.core.particles.GeyserParticleOptions;
/*     */ import net.minecraft.core.particles.ParticleOptions;
/*     */ import net.minecraft.core.particles.ParticleTypes;
/*     */ import net.minecraft.server.level.ServerLevel;
/*     */ import net.minecraft.sounds.SoundEvent;
/*     */ import net.minecraft.sounds.SoundSource;
/*     */ import net.minecraft.util.RandomSource;
/*     */ import net.minecraft.world.effect.MobEffectInstance;
/*     */ import net.minecraft.world.effect.MobEffects;
/*     */ import net.minecraft.world.entity.Entity;
/*     */ import net.minecraft.world.entity.EntitySelector;
/*     */ import net.minecraft.world.entity.LivingEntity;
/*     */ import net.minecraft.world.level.BlockGetter;
/*     */ import net.minecraft.world.level.ClipContext;
/*     */ import net.minecraft.world.level.Level;
/*     */ import net.minecraft.world.level.block.Blocks;
/*     */ import net.minecraft.world.level.block.PotentSulfurBlock;
/*     */ import net.minecraft.world.level.block.state.BlockState;
/*     */ import net.minecraft.world.level.block.state.properties.PotentSulfurState;
/*     */ import net.minecraft.world.level.block.state.properties.Property;
/*     */ import net.minecraft.world.level.gameevent.GameEvent;
/*     */ import net.minecraft.world.level.levelgen.XoroshiroRandomSource;
/*     */ import net.minecraft.world.level.material.Fluid;
/*     */ import net.minecraft.world.level.material.Fluids;
/*     */ import net.minecraft.world.level.storage.ValueInput;
/*     */ import net.minecraft.world.level.storage.ValueOutput;
/*     */ import net.minecraft.world.phys.AABB;
/*     */ import net.minecraft.world.phys.BlockHitResult;
/*     */ import net.minecraft.world.phys.HitResult;
/*     */ import net.minecraft.world.phys.Vec3;
/*     */ import net.minecraft.world.phys.shapes.CollisionContext;
/*     */ 
/*     */ public class PotentSulfurBlockEntity extends BlockEntity {
/*     */   private static final int EFFECT_APPLICATION_FREQUENCY_TICKS = 10;
/*     */   
/*     */   private static final float EFFECT_DURATION_IN_SECONDS = 4.0F;
/*     */   
/*     */   private static final int EFFECT_DURATION_IN_TICKS = 80;
/*     */   
/*     */   public static final float EFFECT_RANGE = 3.0F;
/*     */   
/*  46 */   private static final Predicate<Entity> EFFECT_PREDICATE = EntitySelector.NO_SPECTATORS.and(EntitySelector.ENTITY_STILL_ALIVE);
/*     */   
/*     */   public static final int PARTICLE_FREQUENCY_TICKS = 20;
/*     */   
/*     */   public static final int SOUND_FREQUENCY_TICKS = 40;
/*     */   
/*     */   private static final float GEYSER_BASE_LAUNCH_SPEED = 0.3F;
/*     */   
/*     */   private static final float GEYSER_LAUNCH_FORCE = 0.2F;
/*     */   
/*  53 */   public int waitingCountdown = -1;
/*     */   
/*  54 */   public long eruptionTick = -1L;
/*     */   
/*     */   public static BlockEntityTicker<PotentSulfurBlockEntity> SERVER_NAUSEA_EFFECT_TICKER;
/*     */   
/*     */   public static BlockEntityTicker<PotentSulfurBlockEntity> CLIENT_NOXIOUS_GAS_TICKER;
/*     */   
/*     */   public PotentSulfurBlockEntity(BlockPos worldPosition, BlockState blockState) {
/*  57 */     super(BlockEntityTypes.POTENT_SULFUR, worldPosition, blockState);
/*     */   }
/*     */   
/*     */   protected void saveAdditional(ValueOutput output) {
/*  62 */     super.saveAdditional(output);
/*  63 */     output.putInt("countdown", this.waitingCountdown);
/*     */   }
/*     */   
/*     */   protected void loadAdditional(ValueInput input) {
/*  69 */     super.loadAdditional(input);
/*  70 */     input.getInt("countdown").ifPresent(value -> this.waitingCountdown = value.intValue());
/*     */   }
/*     */   
/*     */   public void setLevel(Level level) {
/*  75 */     super.setLevel(level);
/*  76 */     if (this.eruptionTick == -1L)
/*  77 */       this.eruptionTick = level.getGameTime(); 
/*     */   }
/*     */   
/*     */   public void resetCountdown() {
/*  82 */     this.waitingCountdown = -1;
/*     */   }
/*     */   
/*     */   static {
/*  85 */     SERVER_NAUSEA_EFFECT_TICKER = ((level, pos, state, potentSulfur) -> {
/*     */         if (level.getGameTime() % 10L != 0L)
/*     */           return; 
/*     */         BlockPos sourceBlock = findNoxiousGasSourceBlock(level, pos);
/*     */         if (sourceBlock == null)
/*     */           return; 
/*     */         for (LivingEntity entity : getNearbyLivingEntities(level, sourceBlock)) {
/*     */           if (canBeReachedByNoxiousGas(level, sourceBlock, entity.getEyePosition()))
/*     */             applyNauseaEffect(entity); 
/*     */         } 
/*     */       });
/* 110 */     CLIENT_NOXIOUS_GAS_TICKER = ((level, pos, state, entity) -> {
/*     */         if (level.getGameTime() % 20L != 0L)
/*     */           return; 
/*     */         BlockPos sourceBlock = findNoxiousGasSourceBlock(level, pos);
/*     */         if (sourceBlock != null)
/*     */           spawnNoxiousGasCloudParticle(level, Vec3.atCenterOf((Vec3i)sourceBlock)); 
/*     */       });
/*     */   }
/*     */   
/*     */   private static void applyNauseaEffect(LivingEntity entity) {
/*     */     entity.addEffect(new MobEffectInstance(MobEffects.NAUSEA, 80, 0, true, true));
/*     */   }
/*     */   
/*     */   private static List<LivingEntity> getNearbyLivingEntities(Level level, BlockPos pos) {
/*     */     AABB aabb = (new AABB(pos)).inflate(2.5D, 0.0D, 2.5D);
/*     */     return level.getEntitiesOfClass(LivingEntity.class, aabb, EFFECT_PREDICATE);
/*     */   }
/*     */   
/*     */   public static Function<SoundEvent, BlockEntityTicker<PotentSulfurBlockEntity>> CLIENT_GEYSER_PLUME_TICKER = sound -> ();
/*     */   
/*     */   public static BlockEntityTicker<PotentSulfurBlockEntity> SERVER_WAITING_COUNTDOWN_TICKER;
/*     */   
/*     */   public static final long GEYSER_SALT = -904011478L;
/*     */   
/*     */   static {
/* 135 */     SERVER_WAITING_COUNTDOWN_TICKER = ((level, pos, state, entity) -> {
/*     */         if (level.getGameTime() % 20L != 0L)
/*     */           return; 
/*     */         BlockPos sourceBlock = findNoxiousGasSourceBlock(level, pos);
/*     */         if (sourceBlock == null)
/*     */           return; 
/*     */         if (entity.waitingCountdown <= 0) {
/*     */           int waterBlocks = sourceBlock.getY() - pos.getY() - 1;
/*     */           RandomSource geyserPositional = geyserPositional((ServerLevel)level, pos);
/*     */           if (state.getValue((Property)PotentSulfurBlock.STATE) == PotentSulfurState.DORMANT) {
/*     */             entity.waitingCountdown = 10 * (waterBlocks - 1) + geyserPositional.nextIntBetweenInclusive(15, 30);
/*     */           } else {
/*     */             geyserPositional.nextInt();
/*     */             entity.waitingCountdown = waterBlocks - 1 + geyserPositional.nextIntBetweenInclusive(1, 2);
/*     */           } 
/*     */         } 
/*     */         if (entity.waitingCountdown > 0)
/*     */           entity.waitingCountdown--; 
/*     */         if (entity.waitingCountdown == 0) {
/*     */           PotentSulfurState stateToSet = (state.getValue((Property)PotentSulfurBlock.STATE) == PotentSulfurState.DORMANT) ? PotentSulfurState.ERUPTING : PotentSulfurState.DORMANT;
/*     */           level.setBlock(pos, (BlockState)state.setValue((Property)PotentSulfurBlock.STATE, (Comparable)stateToSet), 3);
/*     */           if (stateToSet == PotentSulfurState.DORMANT)
/*     */             level.gameEvent((Holder)GameEvent.BLOCK_DEACTIVATE, pos, GameEvent.Context.of(state)); 
/*     */         } 
/*     */       });
/*     */   }
/*     */   
/*     */   public static RandomSource geyserPositional(ServerLevel level, BlockPos pos) {
/* 172 */     return (new XoroshiroRandomSource(level.getSeed() ^ 0xFFFFFFFFCA1DE12AL)).forkPositional().at(pos);
/*     */   }
/*     */   
/*     */   public static BlockEntityTicker<PotentSulfurBlockEntity> LAUNCH_ENTITY_TICKER = (level, pos, state, entity) -> {
/*     */       // Byte code:
/*     */       //   0: aload_0
/*     */       //   1: aload_1
/*     */       //   2: invokestatic findNoxiousGasSourceBlock : (Lnet/minecraft/world/level/Level;Lnet/minecraft/core/BlockPos;)Lnet/minecraft/core/BlockPos;
/*     */       //   5: astore #4
/*     */       //   7: aload #4
/*     */       //   9: ifnonnull -> 13
/*     */       //   12: return
/*     */       //   13: aload #4
/*     */       //   15: invokevirtual getY : ()I
/*     */       //   18: aload_1
/*     */       //   19: invokevirtual getY : ()I
/*     */       //   22: isub
/*     */       //   23: iconst_1
/*     */       //   24: isub
/*     */       //   25: istore #5
/*     */       //   27: aload_3
/*     */       //   28: pop
/*     */       //   29: aload_0
/*     */       //   30: aload_1
/*     */       //   31: invokevirtual above : ()Lnet/minecraft/core/BlockPos;
/*     */       //   34: iload #5
/*     */       //   36: invokestatic getUnobstructedBlockCount : (Lnet/minecraft/world/level/Level;Lnet/minecraft/core/BlockPos;I)I
/*     */       //   39: istore #6
/*     */       //   41: new net/minecraft/world/phys/AABB
/*     */       //   44: dup
/*     */       //   45: aload_1
/*     */       //   46: invokevirtual above : ()Lnet/minecraft/core/BlockPos;
/*     */       //   49: invokespecial <init> : (Lnet/minecraft/core/BlockPos;)V
/*     */       //   52: dconst_0
/*     */       //   53: iload #6
/*     */       //   55: iconst_1
/*     */       //   56: isub
/*     */       //   57: i2d
/*     */       //   58: dconst_0
/*     */       //   59: invokevirtual expandTowards : (DDD)Lnet/minecraft/world/phys/AABB;
/*     */       //   62: astore #7
/*     */       //   64: aload_0
/*     */       //   65: ldc_w net/minecraft/world/entity/Entity
/*     */       //   68: aload #7
/*     */       //   70: getstatic net/minecraft/world/level/block/entity/PotentSulfurBlockEntity.EFFECT_PREDICATE : Ljava/util/function/Predicate;
/*     */       //   73: invokevirtual getEntitiesOfClass : (Ljava/lang/Class;Lnet/minecraft/world/phys/AABB;Ljava/util/function/Predicate;)Ljava/util/List;
/*     */       //   76: astore #8
/*     */       //   78: aload #8
/*     */       //   80: invokeinterface iterator : ()Ljava/util/Iterator;
/*     */       //   85: astore #9
/*     */       //   87: aload #9
/*     */       //   89: invokeinterface hasNext : ()Z
/*     */       //   94: ifeq -> 226
/*     */       //   97: aload #9
/*     */       //   99: invokeinterface next : ()Ljava/lang/Object;
/*     */       //   104: checkcast net/minecraft/world/entity/Entity
/*     */       //   107: astore #10
/*     */       //   109: aload #10
/*     */       //   111: invokevirtual getDeltaMovement : ()Lnet/minecraft/world/phys/Vec3;
/*     */       //   114: astore #11
/*     */       //   116: aload #10
/*     */       //   118: invokevirtual checkFallDistanceAccumulation : ()V
/*     */       //   121: aload #10
/*     */       //   123: invokevirtual canSimulateMovement : ()Z
/*     */       //   126: ifne -> 132
/*     */       //   129: goto -> 87
/*     */       //   132: aload #10
/*     */       //   134: instanceof net/minecraft/world/entity/player/Player
/*     */       //   137: ifeq -> 158
/*     */       //   140: aload #10
/*     */       //   142: checkcast net/minecraft/world/entity/player/Player
/*     */       //   145: astore #12
/*     */       //   147: aload #12
/*     */       //   149: invokevirtual getAbilities : ()Lnet/minecraft/world/entity/player/Abilities;
/*     */       //   152: getfield flying : Z
/*     */       //   155: ifne -> 87
/*     */       //   158: aload #10
/*     */       //   160: invokevirtual isPassenger : ()Z
/*     */       //   163: ifne -> 87
/*     */       //   166: aload #10
/*     */       //   168: getstatic net/minecraft/tags/EntityTypeTags.NOT_AFFECTED_BY_GEYSERS : Lnet/minecraft/tags/TagKey;
/*     */       //   171: invokevirtual is : (Lnet/minecraft/tags/TagKey;)Z
/*     */       //   174: ifeq -> 180
/*     */       //   177: goto -> 87
/*     */       //   180: aload #11
/*     */       //   182: getfield y : D
/*     */       //   185: ldc2_w 0.30000001192092896
/*     */       //   188: iload #5
/*     */       //   190: i2d
/*     */       //   191: ldc2_w 0.1
/*     */       //   194: dmul
/*     */       //   195: dadd
/*     */       //   196: dcmpg
/*     */       //   197: ifge -> 223
/*     */       //   200: aload #10
/*     */       //   202: new net/minecraft/world/phys/Vec3
/*     */       //   205: dup
/*     */       //   206: dconst_0
/*     */       //   207: ldc2_w 0.20000000298023224
/*     */       //   210: dconst_0
/*     */       //   211: invokespecial <init> : (DDD)V
/*     */       //   214: invokevirtual addDeltaMovement : (Lnet/minecraft/world/phys/Vec3;)V
/*     */       //   217: aload #10
/*     */       //   219: iconst_1
/*     */       //   220: putfield needsSync : Z
/*     */       //   223: goto -> 87
/*     */       //   226: return
/*     */       // Line number table:
/*     */       //   Java source line number -> byte code offset
/*     */       //   #176	-> 0
/*     */       //   #177	-> 7
/*     */       //   #178	-> 12
/*     */       //   #180	-> 13
/*     */       //   #181	-> 27
/*     */       //   #182	-> 41
/*     */       //   #183	-> 64
/*     */       //   #184	-> 78
/*     */       //   #185	-> 109
/*     */       //   #186	-> 116
/*     */       //   #190	-> 121
/*     */       //   #191	-> 129
/*     */       //   #192	-> 132
/*     */       //   #193	-> 177
/*     */       //   #195	-> 180
/*     */       //   #196	-> 200
/*     */       //   #197	-> 217
/*     */       //   #199	-> 223
/*     */       //   #200	-> 226
/*     */       // Local variable table:
/*     */       //   start	length	slot	name	descriptor
/*     */       //   147	11	12	player	Lnet/minecraft/world/entity/player/Player;
/*     */       //   116	107	11	entityVelocity	Lnet/minecraft/world/phys/Vec3;
/*     */       //   109	114	10	entityToBeLaunched	Lnet/minecraft/world/entity/Entity;
/*     */       //   87	139	9	i$	Ljava/util/Iterator;
/*     */       //   0	227	0	level	Lnet/minecraft/world/level/Level;
/*     */       //   0	227	1	pos	Lnet/minecraft/core/BlockPos;
/*     */       //   0	227	2	state	Lnet/minecraft/world/level/block/state/BlockState;
/*     */       //   0	227	3	entity	Lnet/minecraft/world/level/block/entity/PotentSulfurBlockEntity;
/*     */       //   7	220	4	sourceBlock	Lnet/minecraft/core/BlockPos;
/*     */       //   27	200	5	waterBlocks	I
/*     */       //   41	186	6	geyserForceHeight	I
/*     */       //   64	163	7	aabb	Lnet/minecraft/world/phys/AABB;
/*     */       //   78	149	8	entities	Ljava/util/List;
/*     */       // Local variable type table:
/*     */       //   start	length	slot	name	signature
/*     */       //   78	149	8	entities	Ljava/util/List<Lnet/minecraft/world/entity/Entity;>;
/*     */     };
/*     */   
/*     */   private static void spawnGeyserParticle(Level level, BlockPos sulfurPos, BlockPos sourcePos) {
/* 203 */     int waterBlocks = sourcePos.getY() - sulfurPos.getY() - 1;
/* 204 */     level.addParticle((ParticleOptions)new GeyserParticleOptions(ParticleTypes.GEYSER, waterBlocks), sourcePos.getX() + 0.5D, sourcePos.getY(), sourcePos.getZ() + 0.5D, 0.0D, 0.0D, 0.0D);
/*     */   }
/*     */   
/*     */   private static void spawnNoxiousGasCloudParticle(Level level, Vec3 pos) {
/* 208 */     level.addParticle((ParticleOptions)ParticleTypes.NOXIOUS_GAS_CLOUD, pos.x, pos.y, pos.z, 0.0D, 0.0D, 0.0D);
/*     */   }
/*     */   
/*     */   private static int getUnobstructedBlockCount(Level level, BlockPos pos, int waterBlocks) {
/* 215 */     int geyserForceHeight = 6 * waterBlocks;
/* 216 */     CollisionContext geyserPositionContext = CollisionContext.positionContext(pos.below().getY());
/* 217 */     for (int i = 0; i < geyserForceHeight; i++) {
/* 218 */       BlockPos currentPos = pos.above(i);
/* 219 */       BlockState state = level.getBlockState(currentPos);
/* 220 */       if (!isGeyserPassableBlock(state, level, currentPos, geyserPositionContext))
/* 221 */         return i; 
/*     */     } 
/* 224 */     return geyserForceHeight;
/*     */   }
/*     */   
/*     */   private static boolean isGeyserPassableBlock(BlockState state, Level level, BlockPos pos, CollisionContext context) {
/* 229 */     if (state.isAir() || state.is(Blocks.WATER))
/* 230 */       return true; 
/* 233 */     return state.getCollisionShape((BlockGetter)level, pos, context).isEmpty();
/*     */   }
/*     */   
/*     */   private static BlockPos findNoxiousGasSourceBlock(Level level, BlockPos origin) {
/* 238 */     int maxY = origin.getY() + 4 + 1;
/* 239 */     CollisionContext geyserPositionContext = CollisionContext.positionContext(origin.getY());
/* 240 */     for (BlockPos.MutableBlockPos pos = origin.above(1).mutable(); pos.getY() <= maxY; ) {
/* 241 */       BlockState state = level.getBlockState((BlockPos)pos);
/* 242 */       boolean isWaterLogged = level.getFluidState((BlockPos)pos).isSourceOfType((Fluid)Fluids.WATER);
/* 243 */       if (isWaterLogged && (state.is(Blocks.WATER) || isGeyserPassableBlock(state, level, (BlockPos)pos, geyserPositionContext))) {
/*     */         pos.move(Direction.UP);
/*     */         continue;
/*     */       } 
/* 246 */       if (state.isAir() || isGeyserPassableBlock(state, level, (BlockPos)pos, geyserPositionContext))
/* 247 */         return pos.immutable(); 
/*     */     } 
/* 251 */     return null;
/*     */   }
/*     */   
/*     */   public static boolean canBeReachedByNoxiousGas(Level level, BlockPos sourceBlock, Vec3 pos) {
/* 255 */     BlockPos blockPos = BlockPos.containing((Position)pos);
/* 256 */     CollisionContext geyserPositionContext = CollisionContext.positionContext(blockPos.below().getY());
/* 257 */     if (!isGeyserPassableBlock(level.getBlockState(blockPos), level, blockPos, geyserPositionContext))
/* 258 */       return false; 
/* 262 */     if (pos.distanceToSqr(Vec3.atCenterOf((Vec3i)sourceBlock)) > 9.0D)
/* 263 */       return false; 
/* 266 */     Vec3 belowSource = Vec3.atCenterOf((Vec3i)sourceBlock.below());
/* 267 */     Vec3 belowPos = pos.with(Direction.Axis.Y, pos.y - 1.0D);
/* 268 */     return (isWater(level, belowPos) && haveLineOfSight(level, belowSource, belowPos));
/*     */   }
/*     */   
/*     */   private static boolean isWater(Level level, Vec3 pos) {
/* 272 */     return level.getFluidState(BlockPos.containing((Position)pos)).isSourceOfType((Fluid)Fluids.WATER);
/*     */   }
/*     */   
/*     */   private static boolean haveLineOfSight(Level level, Vec3 a, Vec3 b) {
/* 276 */     BlockHitResult blockHitResult = level.clip(new ClipContext(a, b, ClipContext.Block.COLLIDER, ClipContext.Fluid.NONE, CollisionContext.empty()));
/* 277 */     return (blockHitResult.getType() != HitResult.Type.BLOCK);
/*     */   }
/*     */ }


/* Location:              C:\Users\karol\AppData\Roaming\ModrinthApp\meta\versions\26.2-0.19.5\26.2-0.19.5.jar!\net\minecraft\world\level\block\entity\PotentSulfurBlockEntity.class
 * Java compiler version: 25 (69.0)
 * JD-Core Version:       1.1.3
 */