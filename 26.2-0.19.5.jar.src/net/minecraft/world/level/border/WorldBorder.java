/*     */ package net.minecraft.world.level.border;
/*     */ 
/*     */ import com.google.common.collect.Lists;
/*     */ import com.mojang.datafixers.kinds.App;
/*     */ import com.mojang.datafixers.kinds.Applicative;
/*     */ import com.mojang.datafixers.util.Function9;
/*     */ import com.mojang.serialization.Codec;
/*     */ import com.mojang.serialization.codecs.RecordCodecBuilder;
/*     */ import java.util.List;
/*     */ import java.util.function.Function;
/*     */ import java.util.function.Supplier;
/*     */ import net.minecraft.core.BlockPos;
/*     */ import net.minecraft.core.Position;
/*     */ import net.minecraft.resources.Identifier;
/*     */ import net.minecraft.util.Mth;
/*     */ import net.minecraft.util.datafix.DataFixTypes;
/*     */ import net.minecraft.world.entity.Entity;
/*     */ import net.minecraft.world.level.ChunkPos;
/*     */ import net.minecraft.world.level.saveddata.SavedData;
/*     */ import net.minecraft.world.level.saveddata.SavedDataType;
/*     */ import net.minecraft.world.phys.AABB;
/*     */ import net.minecraft.world.phys.Vec3;
/*     */ import net.minecraft.world.phys.shapes.BooleanOp;
/*     */ import net.minecraft.world.phys.shapes.Shapes;
/*     */ import net.minecraft.world.phys.shapes.VoxelShape;
/*     */ 
/*     */ public class WorldBorder extends SavedData {
/*     */   public static final double MAX_SIZE = 5.9999968E7D;
/*     */   
/*     */   public static final double MAX_CENTER_COORDINATE = 2.9999984E7D;
/*     */   
/*     */   private class MovingBorderExtent implements BorderExtent {
/*     */     private final double from;
/*     */     
/*     */     private final double to;
/*     */     
/*     */     private final long lerpEnd;
/*     */     
/*     */     private final long lerpBegin;
/*     */     
/*     */     private final double lerpDuration;
/*     */     
/*     */     private long lerpProgress;
/*     */     
/*     */     private double size;
/*     */     
/*     */     private double previousSize;
/*     */     
/*     */     private MovingBorderExtent(double from, double to, long duration, long gameTime) {
/*  71 */       this.from = from;
/*  72 */       this.to = to;
/*  74 */       this.lerpDuration = duration;
/*  75 */       this.lerpProgress = duration;
/*  76 */       this.lerpBegin = gameTime;
/*  77 */       this.lerpEnd = this.lerpBegin + duration;
/*  78 */       double size = calculateSize();
/*  79 */       this.size = size;
/*  80 */       this.previousSize = size;
/*     */     }
/*     */     
/*     */     public double getMinX(float deltaPartialTick) {
/*  85 */       return Mth.clamp(WorldBorder.this.getCenterX() - Mth.lerp(deltaPartialTick, getPreviousSize(), getSize()) / 2.0D, -WorldBorder.this.absoluteMaxSize, WorldBorder.this.absoluteMaxSize);
/*     */     }
/*     */     
/*     */     public double getMinZ(float deltaPartialTick) {
/*  90 */       return Mth.clamp(WorldBorder.this.getCenterZ() - Mth.lerp(deltaPartialTick, getPreviousSize(), getSize()) / 2.0D, -WorldBorder.this.absoluteMaxSize, WorldBorder.this.absoluteMaxSize);
/*     */     }
/*     */     
/*     */     public double getMaxX(float deltaPartialTick) {
/*  95 */       return Mth.clamp(WorldBorder.this.getCenterX() + Mth.lerp(deltaPartialTick, getPreviousSize(), getSize()) / 2.0D, -WorldBorder.this.absoluteMaxSize, WorldBorder.this.absoluteMaxSize);
/*     */     }
/*     */     
/*     */     public double getMaxZ(float deltaPartialTick) {
/* 100 */       return Mth.clamp(WorldBorder.this.getCenterZ() + Mth.lerp(deltaPartialTick, getPreviousSize(), getSize()) / 2.0D, -WorldBorder.this.absoluteMaxSize, WorldBorder.this.absoluteMaxSize);
/*     */     }
/*     */     
/*     */     public double getSize() {
/* 105 */       return this.size;
/*     */     }
/*     */     
/*     */     public double getPreviousSize() {
/* 109 */       return this.previousSize;
/*     */     }
/*     */     
/*     */     private double calculateSize() {
/* 113 */       double progress = (this.lerpDuration - this.lerpProgress) / this.lerpDuration;
/* 114 */       return (progress < 1.0D) ? Mth.lerp(progress, this.from, this.to) : this.to;
/*     */     }
/*     */     
/*     */     public double getLerpSpeed() {
/* 119 */       return Math.abs(this.from - this.to) / (this.lerpEnd - this.lerpBegin);
/*     */     }
/*     */     
/*     */     public long getLerpTime() {
/* 124 */       return this.lerpProgress;
/*     */     }
/*     */     
/*     */     public double getLerpTarget() {
/* 129 */       return this.to;
/*     */     }
/*     */     
/*     */     public BorderStatus getStatus() {
/* 134 */       return (this.to < this.from) ? BorderStatus.SHRINKING : BorderStatus.GROWING;
/*     */     }
/*     */     
/*     */     public void onCenterChange() {}
/*     */     
/*     */     public void onAbsoluteMaxSizeChange() {}
/*     */     
/*     */     public WorldBorder.BorderExtent update() {
/* 147 */       this.lerpProgress--;
/* 148 */       this.previousSize = this.size;
/* 149 */       this.size = calculateSize();
/* 150 */       WorldBorder.this.setDirty();
/* 151 */       if (this.lerpProgress <= 0L)
/* 152 */         return new WorldBorder.StaticBorderExtent(this.to); 
/* 155 */       return this;
/*     */     }
/*     */     
/*     */     public VoxelShape getCollisionShape() {
/* 160 */       return Shapes.join(Shapes.INFINITY, Shapes.box(
/* 161 */             Math.floor(getMinX(0.0F)), Double.NEGATIVE_INFINITY, Math.floor(getMinZ(0.0F)), 
/* 162 */             Math.ceil(getMaxX(0.0F)), Double.POSITIVE_INFINITY, Math.ceil(getMaxZ(0.0F))), BooleanOp.ONLY_FIRST);
/*     */     }
/*     */   }
/*     */   
/*     */   private static interface BorderExtent {
/*     */     double getMinX(float param1Float);
/*     */     
/*     */     double getMaxX(float param1Float);
/*     */     
/*     */     double getMinZ(float param1Float);
/*     */     
/*     */     double getMaxZ(float param1Float);
/*     */     
/*     */     double getSize();
/*     */     
/*     */     double getLerpSpeed();
/*     */     
/*     */     long getLerpTime();
/*     */     
/*     */     double getLerpTarget();
/*     */     
/*     */     BorderStatus getStatus();
/*     */     
/*     */     void onAbsoluteMaxSizeChange();
/*     */     
/*     */     void onCenterChange();
/*     */     
/*     */     BorderExtent update();
/*     */     
/*     */     VoxelShape getCollisionShape();
/*     */   }
/*     */   
/*     */   private class StaticBorderExtent implements BorderExtent {
/*     */     private final double size;
/*     */     
/*     */     private double minX;
/*     */     
/*     */     private double minZ;
/*     */     
/*     */     private double maxX;
/*     */     
/*     */     private double maxZ;
/*     */     
/*     */     private VoxelShape shape;
/*     */     
/*     */     public StaticBorderExtent(double size) {
/* 178 */       this.size = size;
/* 179 */       updateBox();
/*     */     }
/*     */     
/*     */     public double getMinX(float deltaPartialTick) {
/* 184 */       return this.minX;
/*     */     }
/*     */     
/*     */     public double getMaxX(float deltaPartialTick) {
/* 189 */       return this.maxX;
/*     */     }
/*     */     
/*     */     public double getMinZ(float deltaPartialTick) {
/* 194 */       return this.minZ;
/*     */     }
/*     */     
/*     */     public double getMaxZ(float deltaPartialTick) {
/* 199 */       return this.maxZ;
/*     */     }
/*     */     
/*     */     public double getSize() {
/* 204 */       return this.size;
/*     */     }
/*     */     
/*     */     public BorderStatus getStatus() {
/* 209 */       return BorderStatus.STATIONARY;
/*     */     }
/*     */     
/*     */     public double getLerpSpeed() {
/* 214 */       return 0.0D;
/*     */     }
/*     */     
/*     */     public long getLerpTime() {
/* 219 */       return 0L;
/*     */     }
/*     */     
/*     */     public double getLerpTarget() {
/* 224 */       return this.size;
/*     */     }
/*     */     
/*     */     private void updateBox() {
/* 228 */       this.minX = Mth.clamp(WorldBorder.this.getCenterX() - this.size / 2.0D, -WorldBorder.this.absoluteMaxSize, WorldBorder.this.absoluteMaxSize);
/* 229 */       this.minZ = Mth.clamp(WorldBorder.this.getCenterZ() - this.size / 2.0D, -WorldBorder.this.absoluteMaxSize, WorldBorder.this.absoluteMaxSize);
/* 230 */       this.maxX = Mth.clamp(WorldBorder.this.getCenterX() + this.size / 2.0D, -WorldBorder.this.absoluteMaxSize, WorldBorder.this.absoluteMaxSize);
/* 231 */       this.maxZ = Mth.clamp(WorldBorder.this.getCenterZ() + this.size / 2.0D, -WorldBorder.this.absoluteMaxSize, WorldBorder.this.absoluteMaxSize);
/* 233 */       this.shape = Shapes.join(Shapes.INFINITY, Shapes.box(
/* 234 */             Math.floor(getMinX(0.0F)), Double.NEGATIVE_INFINITY, Math.floor(getMinZ(0.0F)), 
/* 235 */             Math.ceil(getMaxX(0.0F)), Double.POSITIVE_INFINITY, Math.ceil(getMaxZ(0.0F))), BooleanOp.ONLY_FIRST);
/*     */     }
/*     */     
/*     */     public void onAbsoluteMaxSizeChange() {
/* 241 */       updateBox();
/*     */     }
/*     */     
/*     */     public void onCenterChange() {
/* 246 */       updateBox();
/*     */     }
/*     */     
/*     */     public WorldBorder.BorderExtent update() {
/* 251 */       return this;
/*     */     }
/*     */     
/*     */     public VoxelShape getCollisionShape() {
/* 256 */       return this.shape;
/*     */     }
/*     */   }
/*     */   
/* 260 */   public static final Codec<WorldBorder> CODEC = Settings.CODEC.xmap(WorldBorder::new, Settings::new);
/*     */   
/* 262 */   public static final SavedDataType<WorldBorder> TYPE = new SavedDataType(
/* 263 */       Identifier.withDefaultNamespace("world_border"), WorldBorder::new, CODEC, DataFixTypes.SAVED_DATA_WORLD_BORDER);
/*     */   
/*     */   private final Settings settings;
/*     */   
/*     */   private boolean initialized;
/*     */   
/* 272 */   private final List<BorderChangeListener> listeners = Lists.newArrayList();
/*     */   
/* 274 */   private double damagePerBlock = 0.2D;
/*     */   
/* 275 */   private double safeZone = 5.0D;
/*     */   
/* 276 */   private int warningTime = 15;
/*     */   
/* 277 */   private int warningBlocks = 5;
/*     */   
/*     */   private double centerX;
/*     */   
/*     */   private double centerZ;
/*     */   
/* 282 */   private int absoluteMaxSize = 29999984;
/*     */   
/* 284 */   private BorderExtent extent = new StaticBorderExtent(5.9999968E7D);
/*     */   
/*     */   public WorldBorder() {
/* 287 */     this(Settings.DEFAULT);
/*     */   }
/*     */   
/*     */   public WorldBorder(Settings settings) {
/* 291 */     this.settings = settings;
/*     */   }
/*     */   
/*     */   public boolean isWithinBounds(BlockPos pos) {
/* 295 */     return isWithinBounds(pos.getX(), pos.getZ());
/*     */   }
/*     */   
/*     */   public boolean isWithinBounds(Vec3 pos) {
/* 299 */     return isWithinBounds(pos.x, pos.z);
/*     */   }
/*     */   
/*     */   public boolean isWithinBounds(ChunkPos pos) {
/* 303 */     return (isWithinBounds(pos.getMinBlockX(), pos.getMinBlockZ()) && 
/* 304 */       isWithinBounds(pos.getMaxBlockX(), pos.getMaxBlockZ()));
/*     */   }
/*     */   
/*     */   public boolean isWithinBounds(AABB aabb) {
/* 309 */     return isWithinBounds(aabb.minX, aabb.minZ, aabb.maxX - 9.999999747378752E-6D, aabb.maxZ - 9.999999747378752E-6D);
/*     */   }
/*     */   
/*     */   private boolean isWithinBounds(double minX, double minZ, double maxX, double maxZ) {
/* 313 */     return (isWithinBounds(minX, minZ) && isWithinBounds(maxX, maxZ));
/*     */   }
/*     */   
/*     */   public boolean isWithinBounds(double x, double z) {
/* 317 */     return isWithinBounds(x, z, 0.0D);
/*     */   }
/*     */   
/*     */   public boolean isWithinBounds(double x, double z, double margin) {
/* 321 */     return (x >= getMinX() - margin && x < 
/* 322 */       getMaxX() + margin && z >= 
/* 323 */       getMinZ() - margin && z < 
/* 324 */       getMaxZ() + margin);
/*     */   }
/*     */   
/*     */   public BlockPos clampToBounds(BlockPos position) {
/* 328 */     return clampToBounds(position.getX(), position.getY(), position.getZ());
/*     */   }
/*     */   
/*     */   public BlockPos clampToBounds(Vec3 position) {
/* 332 */     return clampToBounds(position.x(), position.y(), position.z());
/*     */   }
/*     */   
/*     */   public BlockPos clampToBounds(double x, double y, double z) {
/* 336 */     return BlockPos.containing((Position)clampVec3ToBound(x, y, z));
/*     */   }
/*     */   
/*     */   public Vec3 clampVec3ToBound(Vec3 position) {
/* 340 */     return clampVec3ToBound(position.x, position.y, position.z);
/*     */   }
/*     */   
/*     */   public Vec3 clampVec3ToBound(double x, double y, double z) {
/* 344 */     return new Vec3(
/* 345 */         Mth.clamp(x, getMinX(), getMaxX() - 9.999999747378752E-6D), y, 
/*     */         
/* 347 */         Mth.clamp(z, getMinZ(), getMaxZ() - 9.999999747378752E-6D));
/*     */   }
/*     */   
/*     */   public double getDistanceToBorder(Entity entity) {
/* 351 */     return getDistanceToBorder(entity.getX(), entity.getZ());
/*     */   }
/*     */   
/*     */   public VoxelShape getCollisionShape() {
/* 355 */     return this.extent.getCollisionShape();
/*     */   }
/*     */   
/*     */   public double getDistanceToBorder(double x, double z) {
/* 359 */     double fromNorth = z - getMinZ();
/* 360 */     double fromSouth = getMaxZ() - z;
/* 361 */     double fromWest = x - getMinX();
/* 362 */     double fromEast = getMaxX() - x;
/* 363 */     double min = Math.min(fromWest, fromEast);
/* 364 */     min = Math.min(min, fromNorth);
/* 365 */     return Math.min(min, fromSouth);
/*     */   }
/*     */   
/*     */   public boolean isInsideCloseToBorder(Entity source, AABB boundingBox) {
/* 369 */     double bbMax = Math.max(Mth.absMax(boundingBox.getXsize(), boundingBox.getZsize()), 1.0D);
/* 370 */     return (getDistanceToBorder(source) < bbMax * 2.0D && isWithinBounds(source.getX(), source.getZ(), bbMax));
/*     */   }
/*     */   
/*     */   public BorderStatus getStatus() {
/* 374 */     return this.extent.getStatus();
/*     */   }
/*     */   
/*     */   public double getMinX() {
/* 378 */     return getMinX(0.0F);
/*     */   }
/*     */   
/*     */   public double getMinX(float deltaPartialTick) {
/* 382 */     return this.extent.getMinX(deltaPartialTick);
/*     */   }
/*     */   
/*     */   public double getMinZ() {
/* 386 */     return getMinZ(0.0F);
/*     */   }
/*     */   
/*     */   public double getMinZ(float deltaPartialTick) {
/* 390 */     return this.extent.getMinZ(deltaPartialTick);
/*     */   }
/*     */   
/*     */   public double getMaxX() {
/* 394 */     return getMaxX(0.0F);
/*     */   }
/*     */   
/*     */   public double getMaxX(float deltaPartialTick) {
/* 398 */     return this.extent.getMaxX(deltaPartialTick);
/*     */   }
/*     */   
/*     */   public double getMaxZ() {
/* 402 */     return getMaxZ(0.0F);
/*     */   }
/*     */   
/*     */   public double getMaxZ(float deltaPartialTick) {
/* 406 */     return this.extent.getMaxZ(deltaPartialTick);
/*     */   }
/*     */   
/*     */   public double getCenterX() {
/* 410 */     return this.centerX;
/*     */   }
/*     */   
/*     */   public double getCenterZ() {
/* 414 */     return this.centerZ;
/*     */   }
/*     */   
/*     */   public void setCenter(double x, double z) {
/* 418 */     this.centerX = x;
/* 419 */     this.centerZ = z;
/* 421 */     this.extent.onCenterChange();
/* 422 */     setDirty();
/* 424 */     for (BorderChangeListener listener : getListeners())
/* 425 */       listener.onSetCenter(this, x, z); 
/*     */   }
/*     */   
/*     */   public double getSize() {
/* 430 */     return this.extent.getSize();
/*     */   }
/*     */   
/*     */   public long getLerpTime() {
/* 434 */     return this.extent.getLerpTime();
/*     */   }
/*     */   
/*     */   public double getLerpTarget() {
/* 438 */     return this.extent.getLerpTarget();
/*     */   }
/*     */   
/*     */   public void setSize(double size) {
/* 442 */     this.extent = new StaticBorderExtent(size);
/* 443 */     setDirty();
/* 445 */     for (BorderChangeListener listener : getListeners())
/* 446 */       listener.onSetSize(this, size); 
/*     */   }
/*     */   
/*     */   public void lerpSizeBetween(double from, double to, long ticks, long gameTime) {
/* 451 */     this.extent = (from == to) ? new StaticBorderExtent(to) : new MovingBorderExtent(from, to, ticks, gameTime);
/* 452 */     setDirty();
/* 454 */     for (BorderChangeListener listener : getListeners())
/* 455 */       listener.onLerpSize(this, from, to, ticks, gameTime); 
/*     */   }
/*     */   
/*     */   protected List<BorderChangeListener> getListeners() {
/* 460 */     return Lists.newArrayList(this.listeners);
/*     */   }
/*     */   
/*     */   public void addListener(BorderChangeListener listener) {
/* 464 */     this.listeners.add(listener);
/*     */   }
/*     */   
/*     */   public void removeListener(BorderChangeListener listener) {
/* 468 */     this.listeners.remove(listener);
/*     */   }
/*     */   
/*     */   public void setAbsoluteMaxSize(int absoluteMaxSize) {
/* 472 */     this.absoluteMaxSize = absoluteMaxSize;
/* 473 */     this.extent.onAbsoluteMaxSizeChange();
/*     */   }
/*     */   
/*     */   public int getAbsoluteMaxSize() {
/* 477 */     return this.absoluteMaxSize;
/*     */   }
/*     */   
/*     */   public double getSafeZone() {
/* 481 */     return this.safeZone;
/*     */   }
/*     */   
/*     */   public void setSafeZone(double safeZone) {
/* 485 */     this.safeZone = safeZone;
/* 486 */     setDirty();
/* 488 */     for (BorderChangeListener listener : getListeners())
/* 489 */       listener.onSetSafeZone(this, safeZone); 
/*     */   }
/*     */   
/*     */   public double getDamagePerBlock() {
/* 494 */     return this.damagePerBlock;
/*     */   }
/*     */   
/*     */   public void setDamagePerBlock(double damagePerBlock) {
/* 498 */     this.damagePerBlock = damagePerBlock;
/* 499 */     setDirty();
/* 501 */     for (BorderChangeListener listener : getListeners())
/* 502 */       listener.onSetDamagePerBlock(this, damagePerBlock); 
/*     */   }
/*     */   
/*     */   public double getLerpSpeed() {
/* 507 */     return this.extent.getLerpSpeed();
/*     */   }
/*     */   
/*     */   public int getWarningTime() {
/* 511 */     return this.warningTime;
/*     */   }
/*     */   
/*     */   public void setWarningTime(int warningTime) {
/* 515 */     this.warningTime = warningTime;
/* 516 */     setDirty();
/* 518 */     for (BorderChangeListener listener : getListeners())
/* 519 */       listener.onSetWarningTime(this, warningTime); 
/*     */   }
/*     */   
/*     */   public int getWarningBlocks() {
/* 524 */     return this.warningBlocks;
/*     */   }
/*     */   
/*     */   public void setWarningBlocks(int warningBlocks) {
/* 528 */     this.warningBlocks = warningBlocks;
/* 529 */     setDirty();
/* 531 */     for (BorderChangeListener listener : getListeners())
/* 532 */       listener.onSetWarningBlocks(this, warningBlocks); 
/*     */   }
/*     */   
/*     */   public void tick() {
/* 537 */     this.extent = this.extent.update();
/*     */   }
/*     */   
/*     */   public void applyInitialSettings(long gameTime) {
/* 541 */     if (!this.initialized) {
/* 542 */       setCenter(this.settings.centerX(), this.settings.centerZ());
/* 543 */       setDamagePerBlock(this.settings.damagePerBlock());
/* 544 */       setSafeZone(this.settings.safeZone());
/* 545 */       setWarningBlocks(this.settings.warningBlocks());
/* 546 */       setWarningTime(this.settings.warningTime());
/* 548 */       if (this.settings.lerpTime() > 0L) {
/* 549 */         lerpSizeBetween(this.settings.size(), this.settings.lerpTarget(), this.settings.lerpTime(), gameTime);
/*     */       } else {
/* 551 */         setSize(this.settings.size());
/*     */       } 
/* 553 */       this.initialized = true;
/*     */     } 
/*     */   }
/*     */   
/*     */   public static final class Settings extends Record {
/*     */     private final double centerX;
/*     */     
/*     */     private final double centerZ;
/*     */     
/*     */     private final double damagePerBlock;
/*     */     
/*     */     private final double safeZone;
/*     */     
/*     */     private final int warningBlocks;
/*     */     
/*     */     private final int warningTime;
/*     */     
/*     */     private final double size;
/*     */     
/*     */     private final long lerpTime;
/*     */     
/*     */     private final double lerpTarget;
/*     */     
/*     */     public Settings(double centerX, double centerZ, double damagePerBlock, double safeZone, int warningBlocks, int warningTime, double size, long lerpTime, double lerpTarget) {
/* 557 */       this.centerX = centerX;
/* 557 */       this.centerZ = centerZ;
/* 557 */       this.damagePerBlock = damagePerBlock;
/* 557 */       this.safeZone = safeZone;
/* 557 */       this.warningBlocks = warningBlocks;
/* 557 */       this.warningTime = warningTime;
/* 557 */       this.size = size;
/* 557 */       this.lerpTime = lerpTime;
/* 557 */       this.lerpTarget = lerpTarget;
/*     */     }
/*     */     
/*     */     public final String toString() {
/*     */       // Byte code:
/*     */       //   0: aload_0
/*     */       //   1: <illegal opcode> toString : (Lnet/minecraft/world/level/border/WorldBorder$Settings;)Ljava/lang/String;
/*     */       //   6: areturn
/*     */       // Line number table:
/*     */       //   Java source line number -> byte code offset
/*     */       //   #557	-> 0
/*     */       // Local variable table:
/*     */       //   start	length	slot	name	descriptor
/*     */       //   0	7	0	this	Lnet/minecraft/world/level/border/WorldBorder$Settings;
/*     */     }
/*     */     
/*     */     public final int hashCode() {
/*     */       // Byte code:
/*     */       //   0: aload_0
/*     */       //   1: <illegal opcode> hashCode : (Lnet/minecraft/world/level/border/WorldBorder$Settings;)I
/*     */       //   6: ireturn
/*     */       // Line number table:
/*     */       //   Java source line number -> byte code offset
/*     */       //   #557	-> 0
/*     */       // Local variable table:
/*     */       //   start	length	slot	name	descriptor
/*     */       //   0	7	0	this	Lnet/minecraft/world/level/border/WorldBorder$Settings;
/*     */     }
/*     */     
/*     */     public final boolean equals(Object o) {
/*     */       // Byte code:
/*     */       //   0: aload_0
/*     */       //   1: aload_1
/*     */       //   2: <illegal opcode> equals : (Lnet/minecraft/world/level/border/WorldBorder$Settings;Ljava/lang/Object;)Z
/*     */       //   7: ireturn
/*     */       // Line number table:
/*     */       //   Java source line number -> byte code offset
/*     */       //   #557	-> 0
/*     */       // Local variable table:
/*     */       //   start	length	slot	name	descriptor
/*     */       //   0	8	0	this	Lnet/minecraft/world/level/border/WorldBorder$Settings;
/*     */       //   0	8	1	o	Ljava/lang/Object;
/*     */     }
/*     */     
/*     */     public double centerX() {
/* 557 */       return this.centerX;
/*     */     }
/*     */     
/*     */     public double centerZ() {
/* 557 */       return this.centerZ;
/*     */     }
/*     */     
/*     */     public double damagePerBlock() {
/* 557 */       return this.damagePerBlock;
/*     */     }
/*     */     
/*     */     public double safeZone() {
/* 557 */       return this.safeZone;
/*     */     }
/*     */     
/*     */     public int warningBlocks() {
/* 557 */       return this.warningBlocks;
/*     */     }
/*     */     
/*     */     public int warningTime() {
/* 557 */       return this.warningTime;
/*     */     }
/*     */     
/*     */     public double size() {
/* 557 */       return this.size;
/*     */     }
/*     */     
/*     */     public long lerpTime() {
/* 557 */       return this.lerpTime;
/*     */     }
/*     */     
/*     */     public double lerpTarget() {
/* 557 */       return this.lerpTarget;
/*     */     }
/*     */     
/* 558 */     public static final Settings DEFAULT = new Settings(0.0D, 0.0D, 0.2D, 5.0D, 5, 300, 5.9999968E7D, 0L, 0.0D);
/*     */     
/*     */     public static final Codec<Settings> CODEC;
/*     */     
/*     */     static {
/* 559 */       CODEC = RecordCodecBuilder.create(i -> i.group((App)Codec.doubleRange(-2.9999984E7D, 2.9999984E7D).fieldOf("center_x").forGetter(Settings::centerX), (App)Codec.doubleRange(-2.9999984E7D, 2.9999984E7D).fieldOf("center_z").forGetter(Settings::centerZ), (App)Codec.DOUBLE.fieldOf("damage_per_block").forGetter(Settings::damagePerBlock), (App)Codec.DOUBLE.fieldOf("safe_zone").forGetter(Settings::safeZone), (App)Codec.INT.fieldOf("warning_blocks").forGetter(Settings::warningBlocks), (App)Codec.INT.fieldOf("warning_time").forGetter(Settings::warningTime), (App)Codec.DOUBLE.fieldOf("size").forGetter(Settings::size), (App)Codec.LONG.fieldOf("lerp_time").forGetter(Settings::lerpTime), (App)Codec.DOUBLE.fieldOf("lerp_target").forGetter(Settings::lerpTarget)).apply((Applicative)i, Settings::new));
/*     */     }
/*     */     
/*     */     public Settings(WorldBorder worldBorder) {
/* 572 */       this(worldBorder.centerX, worldBorder.centerZ, worldBorder.damagePerBlock, worldBorder.safeZone, worldBorder.warningBlocks, worldBorder.warningTime, worldBorder.extent
/*     */           
/* 579 */           .getSize(), worldBorder.extent
/* 580 */           .getLerpTime(), worldBorder.extent
/* 581 */           .getLerpTarget());
/*     */     }
/*     */   }
/*     */ }


/* Location:              C:\Users\karol\AppData\Roaming\ModrinthApp\meta\versions\26.2-0.19.5\26.2-0.19.5.jar!\net\minecraft\world\level\border\WorldBorder.class
 * Java compiler version: 25 (69.0)
 * JD-Core Version:       1.1.3
 */