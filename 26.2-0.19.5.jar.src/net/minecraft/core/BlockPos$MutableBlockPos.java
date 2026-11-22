/*     */ package net.minecraft.core;
/*     */ 
/*     */ import net.minecraft.util.Mth;
/*     */ import net.minecraft.world.level.block.Rotation;
/*     */ 
/*     */ public class MutableBlockPos extends BlockPos {
/*     */   public MutableBlockPos() {
/* 290 */     this(0, 0, 0);
/*     */   }
/*     */   
/*     */   public MutableBlockPos(int x, int y, int z) {
/* 294 */     super(x, y, z);
/*     */   }
/*     */   
/*     */   public MutableBlockPos(double x, double y, double z) {
/* 298 */     this(Mth.floor(x), Mth.floor(y), Mth.floor(z));
/*     */   }
/*     */   
/*     */   public BlockPos offset(int x, int y, int z) {
/* 303 */     return super.offset(x, y, z).immutable();
/*     */   }
/*     */   
/*     */   public BlockPos multiply(int scale) {
/* 308 */     return super.multiply(scale).immutable();
/*     */   }
/*     */   
/*     */   public BlockPos relative(Direction direction, int steps) {
/* 313 */     return super.relative(direction, steps).immutable();
/*     */   }
/*     */   
/*     */   public BlockPos relative(Direction.Axis axis, int steps) {
/* 318 */     return super.relative(axis, steps).immutable();
/*     */   }
/*     */   
/*     */   public BlockPos rotate(Rotation rotation) {
/* 323 */     return super.rotate(rotation).immutable();
/*     */   }
/*     */   
/*     */   public MutableBlockPos set(int x, int y, int z) {
/* 327 */     setX(x);
/* 328 */     setY(y);
/* 329 */     setZ(z);
/* 330 */     return this;
/*     */   }
/*     */   
/*     */   public MutableBlockPos set(double x, double y, double z) {
/* 334 */     return set(Mth.floor(x), Mth.floor(y), Mth.floor(z));
/*     */   }
/*     */   
/*     */   public MutableBlockPos set(Vec3i vec) {
/* 338 */     return set(vec.getX(), vec.getY(), vec.getZ());
/*     */   }
/*     */   
/*     */   public MutableBlockPos set(long pos) {
/* 342 */     return set(getX(pos), getY(pos), getZ(pos));
/*     */   }
/*     */   
/*     */   public MutableBlockPos set(AxisCycle transform, int x, int y, int z) {
/* 346 */     return set(transform
/* 347 */         .cycle(x, y, z, Direction.Axis.X), transform
/* 348 */         .cycle(x, y, z, Direction.Axis.Y), transform
/* 349 */         .cycle(x, y, z, Direction.Axis.Z));
/*     */   }
/*     */   
/*     */   public MutableBlockPos setWithOffset(Vec3i pos, Direction direction) {
/* 354 */     return set(pos.getX() + direction.getStepX(), pos.getY() + direction.getStepY(), pos.getZ() + direction.getStepZ());
/*     */   }
/*     */   
/*     */   public MutableBlockPos setWithOffset(Vec3i pos, int x, int y, int z) {
/* 358 */     return set(pos.getX() + x, pos.getY() + y, pos.getZ() + z);
/*     */   }
/*     */   
/*     */   public MutableBlockPos setWithOffset(Vec3i pos, Vec3i offset) {
/* 362 */     return set(pos.getX() + offset.getX(), pos.getY() + offset.getY(), pos.getZ() + offset.getZ());
/*     */   }
/*     */   
/*     */   public MutableBlockPos move(Direction direction) {
/* 366 */     return move(direction, 1);
/*     */   }
/*     */   
/*     */   public MutableBlockPos move(Direction direction, int steps) {
/* 370 */     return set(getX() + direction.getStepX() * steps, getY() + direction.getStepY() * steps, getZ() + direction.getStepZ() * steps);
/*     */   }
/*     */   
/*     */   public MutableBlockPos move(int x, int y, int z) {
/* 374 */     return set(getX() + x, getY() + y, getZ() + z);
/*     */   }
/*     */   
/*     */   public MutableBlockPos move(Vec3i pos) {
/* 378 */     return set(getX() + pos.getX(), getY() + pos.getY(), getZ() + pos.getZ());
/*     */   }
/*     */   
/*     */   public MutableBlockPos clamp(Direction.Axis axis, int minimum, int maximum) {
/* 382 */     switch (BlockPos.null.$SwitchMap$net$minecraft$core$Direction$Axis[axis.ordinal()]) {
/*     */       default:
/* 382 */         throw new MatchException(null, null);
/*     */       case 1:
/*     */       
/*     */       case 2:
/*     */       
/*     */       case 3:
/*     */         break;
/*     */     } 
/* 382 */     return 
/*     */       
/* 385 */       set(getX(), getY(), Mth.clamp(getZ(), minimum, maximum));
/*     */   }
/*     */   
/*     */   public MutableBlockPos setX(int x) {
/* 391 */     super.setX(x);
/* 392 */     return this;
/*     */   }
/*     */   
/*     */   public MutableBlockPos setY(int y) {
/* 397 */     super.setY(y);
/* 398 */     return this;
/*     */   }
/*     */   
/*     */   public MutableBlockPos setZ(int z) {
/* 403 */     super.setZ(z);
/* 404 */     return this;
/*     */   }
/*     */   
/*     */   public BlockPos immutable() {
/* 409 */     return new BlockPos(this);
/*     */   }
/*     */ }


/* Location:              C:\Users\karol\AppData\Roaming\ModrinthApp\meta\versions\26.2-0.19.5\26.2-0.19.5.jar!\net\minecraft\core\BlockPos$MutableBlockPos.class
 * Java compiler version: 25 (69.0)
 * JD-Core Version:       1.1.3
 */