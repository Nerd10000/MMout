/*     */ package net.minecraft.world.level.block;
/*     */ 
/*     */ import com.google.common.collect.Lists;
/*     */ import com.google.common.collect.Sets;
/*     */ import com.mojang.datafixers.kinds.App;
/*     */ import com.mojang.datafixers.kinds.Applicative;
/*     */ import com.mojang.datafixers.util.Function5;
/*     */ import com.mojang.serialization.Codec;
/*     */ import com.mojang.serialization.codecs.RecordCodecBuilder;
/*     */ import it.unimi.dsi.fastutil.objects.ObjectArrayList;
/*     */ import java.util.List;
/*     */ import java.util.Objects;
/*     */ import java.util.Optional;
/*     */ import java.util.Set;
/*     */ import net.minecraft.core.BlockPos;
/*     */ import net.minecraft.core.Direction;
/*     */ import net.minecraft.core.Vec3i;
/*     */ import net.minecraft.server.level.ServerLevel;
/*     */ import net.minecraft.sounds.SoundEvents;
/*     */ import net.minecraft.sounds.SoundSource;
/*     */ import net.minecraft.util.RandomSource;
/*     */ import net.minecraft.util.Util;
/*     */ import net.minecraft.world.level.BlockGetter;
/*     */ import net.minecraft.world.level.LevelAccessor;
/*     */ import net.minecraft.world.level.block.state.BlockState;
/*     */ 
/*     */ public class ChargeCursor {
/*     */   private static final ObjectArrayList<Vec3i> NON_CORNER_NEIGHBOURS;
/*     */   
/*     */   public static final int MAX_CURSOR_DECAY_DELAY = 1;
/*     */   
/*     */   private BlockPos pos;
/*     */   
/*     */   private int charge;
/*     */   
/*     */   private int updateDelay;
/*     */   
/*     */   private int decayDelay;
/*     */   
/*     */   private Set<Direction> facings;
/*     */   
/*     */   private static final Codec<Set<Direction>> DIRECTION_SET;
/*     */   
/*     */   public static final Codec<ChargeCursor> CODEC;
/*     */   
/*     */   static {
/* 134 */     NON_CORNER_NEIGHBOURS = (ObjectArrayList<Vec3i>)Util.make(new ObjectArrayList(18), list -> {
/*     */           Objects.requireNonNull(list);
/*     */           BlockPos.betweenClosedStream(new BlockPos(-1, -1, -1), new BlockPos(1, 1, 1)).filter(()).map(BlockPos::immutable).forEach(list::add);
/*     */         });
/* 151 */     DIRECTION_SET = Direction.CODEC.listOf().xmap(l -> Sets.newEnumSet(l, Direction.class), Lists::newArrayList);
/* 153 */     CODEC = RecordCodecBuilder.create(i -> i.group((App)BlockPos.CODEC.fieldOf("pos").forGetter(ChargeCursor::getPos), (App)Codec.intRange(0, 1000).optionalFieldOf("charge", Integer.valueOf(0)).forGetter(ChargeCursor::getCharge), (App)Codec.intRange(0, 1).optionalFieldOf("decay_delay", Integer.valueOf(1)).forGetter(ChargeCursor::getDecayDelay), (App)Codec.intRange(0, 2147483647).optionalFieldOf("update_delay", Integer.valueOf(0)).forGetter(()), (App)DIRECTION_SET.lenientOptionalFieldOf("facings").forGetter(())).apply((Applicative)i, ChargeCursor::new));
/*     */   }
/*     */   
/*     */   private ChargeCursor(BlockPos pos, int charge, int decayDelay, int updateDelay, Optional<Set<Direction>> facings) {
/* 162 */     this.pos = pos;
/* 163 */     this.charge = charge;
/* 164 */     this.decayDelay = decayDelay;
/* 165 */     this.updateDelay = updateDelay;
/* 166 */     this.facings = facings.orElse(null);
/*     */   }
/*     */   
/*     */   public ChargeCursor(BlockPos pos, int charge) {
/* 170 */     this(pos, charge, 1, 0, Optional.empty());
/*     */   }
/*     */   
/*     */   public BlockPos getPos() {
/* 174 */     return this.pos;
/*     */   }
/*     */   
/*     */   private boolean isPosUnreasonable(BlockPos originPos) {
/* 178 */     return (this.pos.distChessboard((Vec3i)originPos) > 1024);
/*     */   }
/*     */   
/*     */   public int getCharge() {
/* 182 */     return this.charge;
/*     */   }
/*     */   
/*     */   public int getDecayDelay() {
/* 186 */     return this.decayDelay;
/*     */   }
/*     */   
/*     */   public Set<Direction> getFacingData() {
/* 190 */     return this.facings;
/*     */   }
/*     */   
/*     */   private boolean shouldUpdate(LevelAccessor level, BlockPos pos, boolean isWorldGen) {
/* 194 */     if (this.charge <= 0)
/* 195 */       return false; 
/* 197 */     if (isWorldGen)
/* 198 */       return true; 
/* 200 */     if (level instanceof ServerLevel) {
/* 200 */       ServerLevel serverLevel = (ServerLevel)level;
/* 201 */       return serverLevel.shouldTickBlocksAt(pos);
/*     */     } 
/* 203 */     return false;
/*     */   }
/*     */   
/*     */   public void update(LevelAccessor level, BlockPos originPos, RandomSource random, SculkSpreader spreader, boolean spreadVeins) {
/* 207 */     if (!shouldUpdate(level, originPos, spreader.isWorldGeneration))
/*     */       return; 
/* 211 */     if (this.updateDelay > 0) {
/* 212 */       this.updateDelay--;
/*     */       return;
/*     */     } 
/* 216 */     BlockState currentState = level.getBlockState(this.pos);
/* 217 */     SculkBehaviour sculkBehaviour = getBlockBehaviour(currentState);
/* 220 */     if (spreadVeins && sculkBehaviour.attemptSpreadVein(level, this.pos, currentState, this.facings, spreader.isWorldGeneration())) {
/* 221 */       if (sculkBehaviour.canChangeBlockStateOnSpread()) {
/* 222 */         currentState = level.getBlockState(this.pos);
/* 223 */         sculkBehaviour = getBlockBehaviour(currentState);
/*     */       } 
/* 225 */       level.playSound(null, this.pos, SoundEvents.SCULK_BLOCK_SPREAD, SoundSource.BLOCKS, 1.0F, 1.0F);
/*     */     } 
/* 229 */     this.charge = sculkBehaviour.attemptUseCharge(this, level, originPos, random, spreader, spreadVeins);
/* 231 */     if (this.charge <= 0) {
/* 232 */       sculkBehaviour.onDischarged(level, currentState, this.pos, random);
/*     */       return;
/*     */     } 
/* 237 */     BlockPos transferPos = getValidMovementPos(level, this.pos, random);
/* 238 */     if (transferPos != null) {
/* 239 */       sculkBehaviour.onDischarged(level, currentState, this.pos, random);
/* 240 */       this.pos = transferPos.immutable();
/* 241 */       if (spreader.isWorldGeneration() && !this.pos.closerThan(new Vec3i(originPos.getX(), this.pos.getY(), originPos.getZ()), 15.0D)) {
/* 242 */         this.charge = 0;
/*     */         return;
/*     */       } 
/* 245 */       currentState = level.getBlockState(transferPos);
/*     */     } 
/* 248 */     if (currentState.getBlock() instanceof SculkBehaviour)
/* 249 */       this.facings = MultifaceBlock.availableFaces(currentState); 
/* 251 */     this.decayDelay = sculkBehaviour.updateDecayDelay(this.decayDelay);
/* 252 */     this.updateDelay = sculkBehaviour.getSculkSpreadDelay();
/*     */   }
/*     */   
/*     */   private void mergeWith(ChargeCursor other) {
/* 256 */     this.charge += other.charge;
/* 257 */     other.charge = 0;
/* 258 */     this.updateDelay = Math.min(this.updateDelay, other.updateDelay);
/*     */   }
/*     */   
/*     */   private static SculkBehaviour getBlockBehaviour(BlockState state) {
/* 262 */     Block block = state.getBlock();
/* 262 */     SculkBehaviour behaviour = (SculkBehaviour)block;
/* 262 */     return (block instanceof SculkBehaviour) ? behaviour : SculkBehaviour.DEFAULT;
/*     */   }
/*     */   
/*     */   private static List<Vec3i> getRandomizedNonCornerNeighbourOffsets(RandomSource random) {
/* 266 */     return Util.shuffledCopy(NON_CORNER_NEIGHBOURS, random);
/*     */   }
/*     */   
/*     */   private static BlockPos getValidMovementPos(LevelAccessor level, BlockPos pos, RandomSource random) {
/* 270 */     BlockPos.MutableBlockPos sculkPosition = pos.mutable();
/* 271 */     BlockPos.MutableBlockPos neighbour = pos.mutable();
/* 273 */     for (Vec3i offset : getRandomizedNonCornerNeighbourOffsets(random)) {
/* 274 */       neighbour.setWithOffset((Vec3i)pos, offset);
/* 275 */       BlockState transferee = level.getBlockState((BlockPos)neighbour);
/* 277 */       if (transferee.getBlock() instanceof SculkBehaviour && isMovementUnobstructed(level, pos, (BlockPos)neighbour)) {
/* 278 */         sculkPosition.set((Vec3i)neighbour);
/* 280 */         if (SculkVeinBlock.hasSubstrateAccess(level, transferee, (BlockPos)neighbour))
/*     */           break; 
/*     */       } 
/*     */     } 
/* 285 */     return sculkPosition.equals(pos) ? null : (BlockPos)sculkPosition;
/*     */   }
/*     */   
/*     */   private static boolean isMovementUnobstructed(LevelAccessor level, BlockPos from, BlockPos to) {
/* 289 */     if (from.distManhattan((Vec3i)to) == 1)
/* 290 */       return true; 
/* 294 */     BlockPos delta = to.subtract((Vec3i)from);
/* 295 */     Direction directionX = Direction.fromAxisAndDirection(Direction.Axis.X, (delta.getX() < 0) ? Direction.AxisDirection.NEGATIVE : Direction.AxisDirection.POSITIVE);
/* 296 */     Direction directionY = Direction.fromAxisAndDirection(Direction.Axis.Y, (delta.getY() < 0) ? Direction.AxisDirection.NEGATIVE : Direction.AxisDirection.POSITIVE);
/* 297 */     Direction directionZ = Direction.fromAxisAndDirection(Direction.Axis.Z, (delta.getZ() < 0) ? Direction.AxisDirection.NEGATIVE : Direction.AxisDirection.POSITIVE);
/* 299 */     if (delta.getX() == 0)
/* 300 */       return (isUnobstructed(level, from, directionY) || isUnobstructed(level, from, directionZ)); 
/* 301 */     if (delta.getY() == 0)
/* 302 */       return (isUnobstructed(level, from, directionX) || isUnobstructed(level, from, directionZ)); 
/* 304 */     return (isUnobstructed(level, from, directionX) || isUnobstructed(level, from, directionY));
/*     */   }
/*     */   
/*     */   private static boolean isUnobstructed(LevelAccessor level, BlockPos from, Direction direction) {
/* 309 */     BlockPos testPos = from.relative(direction);
/* 310 */     return !level.getBlockState(testPos).isFaceSturdy((BlockGetter)level, testPos, direction.getOpposite());
/*     */   }
/*     */ }


/* Location:              C:\Users\karol\AppData\Roaming\ModrinthApp\meta\versions\26.2-0.19.5\26.2-0.19.5.jar!\net\minecraft\world\level\block\SculkSpreader$ChargeCursor.class
 * Java compiler version: 25 (69.0)
 * JD-Core Version:       1.1.3
 */