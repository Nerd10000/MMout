/*     */ package net.minecraft.world.level.levelgen.structure;
/*     */ 
/*     */ import com.google.common.base.MoreObjects;
/*     */ import com.mojang.logging.LogUtils;
/*     */ import com.mojang.serialization.Codec;
/*     */ import com.mojang.serialization.DataResult;
/*     */ import io.netty.buffer.ByteBuf;
/*     */ import java.util.Iterator;
/*     */ import java.util.Objects;
/*     */ import java.util.Optional;
/*     */ import java.util.function.Consumer;
/*     */ import java.util.stream.IntStream;
/*     */ import java.util.stream.Stream;
/*     */ import net.minecraft.core.BlockPos;
/*     */ import net.minecraft.core.Direction;
/*     */ import net.minecraft.core.SectionPos;
/*     */ import net.minecraft.core.Vec3i;
/*     */ import net.minecraft.network.codec.StreamCodec;
/*     */ import net.minecraft.util.Util;
/*     */ import net.minecraft.world.level.ChunkPos;
/*     */ import org.slf4j.Logger;
/*     */ 
/*     */ public class BoundingBox {
/*  24 */   private static final Logger LOGGER = LogUtils.getLogger();
/*     */   
/*     */   public static final Codec<BoundingBox> CODEC;
/*     */   
/*     */   public static final StreamCodec<ByteBuf, BoundingBox> STREAM_CODEC;
/*     */   
/*     */   private int minX;
/*     */   
/*     */   private int minY;
/*     */   
/*     */   private int minZ;
/*     */   
/*     */   private int maxX;
/*     */   
/*     */   private int maxY;
/*     */   
/*     */   private int maxZ;
/*     */   
/*     */   static {
/*  29 */     CODEC = Codec.INT_STREAM.comapFlatMap(input -> Util.fixedSize(input, 6).map(()), bb -> IntStream.of(new int[] { bb.minX, bb.minY, bb.minZ, bb.maxX, bb.maxY, bb.maxZ })).stable();
/*  31 */     STREAM_CODEC = StreamCodec.composite(BlockPos.STREAM_CODEC, box -> new BlockPos(box.minX, box.minY, box.minZ), BlockPos.STREAM_CODEC, box -> new BlockPos(box.maxX, box.maxY, box.maxZ), (min, max) -> new BoundingBox(min.getX(), min.getY(), min.getZ(), max.getX(), max.getY(), max.getZ()));
/*     */   }
/*     */   
/*     */   public BoundingBox(BlockPos content) {
/*  45 */     this(content.getX(), content.getY(), content.getZ(), content.getX(), content.getY(), content.getZ());
/*     */   }
/*     */   
/*     */   public BoundingBox(int minX, int minY, int minZ, int maxX, int maxY, int maxZ) {
/*  49 */     this.minX = minX;
/*  50 */     this.minY = minY;
/*  51 */     this.minZ = minZ;
/*  52 */     this.maxX = maxX;
/*  53 */     this.maxY = maxY;
/*  54 */     this.maxZ = maxZ;
/*  56 */     if (maxX < minX || maxY < minY || maxZ < minZ) {
/*  57 */       Util.logAndPauseIfInIde("Invalid bounding box data, inverted bounds for: " + String.valueOf(this));
/*  59 */       this.minX = Math.min(minX, maxX);
/*  60 */       this.minY = Math.min(minY, maxY);
/*  61 */       this.minZ = Math.min(minZ, maxZ);
/*  62 */       this.maxX = Math.max(minX, maxX);
/*  63 */       this.maxY = Math.max(minY, maxY);
/*  64 */       this.maxZ = Math.max(minZ, maxZ);
/*     */     } 
/*     */   }
/*     */   
/*     */   public static BoundingBox fromCorners(Vec3i pos0, Vec3i pos1) {
/*  69 */     return new BoundingBox(Math.min(pos0.getX(), pos1.getX()), Math.min(pos0.getY(), pos1.getY()), Math.min(pos0.getZ(), pos1.getZ()), Math.max(pos0.getX(), pos1.getX()), Math.max(pos0.getY(), pos1.getY()), Math.max(pos0.getZ(), pos1.getZ()));
/*     */   }
/*     */   
/*     */   public static BoundingBox infinite() {
/*  73 */     return new BoundingBox(-2147483648, -2147483648, -2147483648, 2147483647, 2147483647, 2147483647);
/*     */   }
/*     */   
/*     */   public static BoundingBox orientBox(int footX, int footY, int footZ, int offX, int offY, int offZ, int width, int height, int depth, Direction direction) {
/*  77 */     switch (direction) {
/*     */       default:
/*  81 */         return new BoundingBox(footX + offX, footY + offY, footZ + offZ, footX + width - 1 + offX, footY + height - 1 + offY, footZ + depth - 1 + offZ);
/*     */       case NORTH:
/*  84 */         return new BoundingBox(footX + offX, footY + offY, footZ - depth + 1 + offZ, footX + width - 1 + offX, footY + height - 1 + offY, footZ + offZ);
/*     */       case WEST:
/*  87 */         return new BoundingBox(footX - depth + 1 + offZ, footY + offY, footZ + offX, footX + offZ, footY + height - 1 + offY, footZ + width - 1 + offX);
/*     */       case EAST:
/*     */         break;
/*     */     } 
/*  90 */     return new BoundingBox(footX + offZ, footY + offY, footZ + offX, footX + depth - 1 + offZ, footY + height - 1 + offY, footZ + width - 1 + offX);
/*     */   }
/*     */   
/*     */   public Stream<ChunkPos> intersectingChunks() {
/*  95 */     int minChunkX = SectionPos.blockToSectionCoord(minX());
/*  96 */     int minChunkZ = SectionPos.blockToSectionCoord(minZ());
/*  97 */     int maxChunkX = SectionPos.blockToSectionCoord(maxX());
/*  98 */     int maxChunkZ = SectionPos.blockToSectionCoord(maxZ());
/*  99 */     return ChunkPos.rangeClosed(new ChunkPos(minChunkX, minChunkZ), new ChunkPos(maxChunkX, maxChunkZ));
/*     */   }
/*     */   
/*     */   public boolean intersects(BoundingBox other) {
/* 103 */     return (this.maxX >= other.minX && this.minX <= other.maxX && this.maxZ >= other.minZ && this.minZ <= other.maxZ && this.maxY >= other.minY && this.minY <= other.maxY);
/*     */   }
/*     */   
/*     */   public boolean intersects(int minX, int minZ, int maxX, int maxZ) {
/* 107 */     return (this.maxX >= minX && this.minX <= maxX && this.maxZ >= minZ && this.minZ <= maxZ);
/*     */   }
/*     */   
/*     */   public static Optional<BoundingBox> encapsulatingPositions(Iterable<BlockPos> iterable) {
/* 111 */     Iterator<BlockPos> iterator = iterable.iterator();
/* 112 */     if (!iterator.hasNext())
/* 113 */       return Optional.empty(); 
/* 116 */     BoundingBox result = new BoundingBox(iterator.next());
/* 117 */     Objects.requireNonNull(result);
/* 117 */     iterator.forEachRemaining(result::encapsulate);
/* 118 */     return Optional.of(result);
/*     */   }
/*     */   
/*     */   public static Optional<BoundingBox> encapsulatingBoxes(Iterable<BoundingBox> iterable) {
/* 122 */     Iterator<BoundingBox> iterator = iterable.iterator();
/* 123 */     if (!iterator.hasNext())
/* 124 */       return Optional.empty(); 
/* 127 */     BoundingBox first = iterator.next();
/* 128 */     BoundingBox result = new BoundingBox(first.minX, first.minY, first.minZ, first.maxX, first.maxY, first.maxZ);
/* 129 */     Objects.requireNonNull(result);
/* 129 */     iterator.forEachRemaining(result::encapsulate);
/* 130 */     return Optional.of(result);
/*     */   }
/*     */   
/*     */   @Deprecated
/*     */   public BoundingBox encapsulate(BoundingBox other) {
/* 136 */     this.minX = Math.min(this.minX, other.minX);
/* 137 */     this.minY = Math.min(this.minY, other.minY);
/* 138 */     this.minZ = Math.min(this.minZ, other.minZ);
/* 139 */     this.maxX = Math.max(this.maxX, other.maxX);
/* 140 */     this.maxY = Math.max(this.maxY, other.maxY);
/* 141 */     this.maxZ = Math.max(this.maxZ, other.maxZ);
/* 142 */     return this;
/*     */   }
/*     */   
/*     */   public static BoundingBox encapsulating(BoundingBox a, BoundingBox b) {
/* 146 */     return new BoundingBox(
/* 147 */         Math.min(a.minX, b.minX), 
/* 148 */         Math.min(a.minY, b.minY), 
/* 149 */         Math.min(a.minZ, b.minZ), 
/* 150 */         Math.max(a.maxX, b.maxX), 
/* 151 */         Math.max(a.maxY, b.maxY), 
/* 152 */         Math.max(a.maxZ, b.maxZ));
/*     */   }
/*     */   
/*     */   @Deprecated
/*     */   public BoundingBox encapsulate(BlockPos pos) {
/* 159 */     this.minX = Math.min(this.minX, pos.getX());
/* 160 */     this.minY = Math.min(this.minY, pos.getY());
/* 161 */     this.minZ = Math.min(this.minZ, pos.getZ());
/* 162 */     this.maxX = Math.max(this.maxX, pos.getX());
/* 163 */     this.maxY = Math.max(this.maxY, pos.getY());
/* 164 */     this.maxZ = Math.max(this.maxZ, pos.getZ());
/* 165 */     return this;
/*     */   }
/*     */   
/*     */   @Deprecated
/*     */   public BoundingBox move(int dx, int dy, int dz) {
/* 171 */     this.minX += dx;
/* 172 */     this.minY += dy;
/* 173 */     this.minZ += dz;
/* 174 */     this.maxX += dx;
/* 175 */     this.maxY += dy;
/* 176 */     this.maxZ += dz;
/* 177 */     return this;
/*     */   }
/*     */   
/*     */   @Deprecated
/*     */   public BoundingBox move(Vec3i amount) {
/* 183 */     return move(amount.getX(), amount.getY(), amount.getZ());
/*     */   }
/*     */   
/*     */   public BoundingBox moved(int dx, int dy, int dz) {
/* 187 */     return new BoundingBox(this.minX + dx, this.minY + dy, this.minZ + dz, this.maxX + dx, this.maxY + dy, this.maxZ + dz);
/*     */   }
/*     */   
/*     */   public BoundingBox inflatedBy(int amountToAddAllDirections) {
/* 198 */     return inflatedBy(amountToAddAllDirections, amountToAddAllDirections, amountToAddAllDirections);
/*     */   }
/*     */   
/*     */   public BoundingBox inflatedBy(int inflateX, int inflateY, int inflateZ) {
/* 202 */     return new BoundingBox(
/* 203 */         minX() - inflateX, 
/* 204 */         minY() - inflateY, 
/* 205 */         minZ() - inflateZ, 
/* 206 */         maxX() + inflateX, 
/* 207 */         maxY() + inflateY, 
/* 208 */         maxZ() + inflateZ);
/*     */   }
/*     */   
/*     */   public boolean isInside(Vec3i pos) {
/* 213 */     return isInside(pos.getX(), pos.getY(), pos.getZ());
/*     */   }
/*     */   
/*     */   public boolean isInside(int x, int y, int z) {
/* 217 */     return (x >= this.minX && x <= this.maxX && z >= this.minZ && z <= this.maxZ && y >= this.minY && y <= this.maxY);
/*     */   }
/*     */   
/*     */   public Vec3i getLength() {
/* 221 */     return new Vec3i(this.maxX - this.minX, this.maxY - this.minY, this.maxZ - this.minZ);
/*     */   }
/*     */   
/*     */   public int getXSpan() {
/* 225 */     return this.maxX - this.minX + 1;
/*     */   }
/*     */   
/*     */   public int getYSpan() {
/* 229 */     return this.maxY - this.minY + 1;
/*     */   }
/*     */   
/*     */   public int getZSpan() {
/* 233 */     return this.maxZ - this.minZ + 1;
/*     */   }
/*     */   
/*     */   public BlockPos getCenter() {
/* 237 */     return new BlockPos(this.minX + (this.maxX - this.minX + 1) / 2, this.minY + (this.maxY - this.minY + 1) / 2, this.minZ + (this.maxZ - this.minZ + 1) / 2);
/*     */   }
/*     */   
/*     */   public void forAllCorners(Consumer<BlockPos> consumer) {
/* 241 */     BlockPos.MutableBlockPos pos = new BlockPos.MutableBlockPos();
/* 242 */     consumer.accept(pos.set(this.maxX, this.maxY, this.maxZ));
/* 243 */     consumer.accept(pos.set(this.minX, this.maxY, this.maxZ));
/* 244 */     consumer.accept(pos.set(this.maxX, this.minY, this.maxZ));
/* 245 */     consumer.accept(pos.set(this.minX, this.minY, this.maxZ));
/* 246 */     consumer.accept(pos.set(this.maxX, this.maxY, this.minZ));
/* 247 */     consumer.accept(pos.set(this.minX, this.maxY, this.minZ));
/* 248 */     consumer.accept(pos.set(this.maxX, this.minY, this.minZ));
/* 249 */     consumer.accept(pos.set(this.minX, this.minY, this.minZ));
/*     */   }
/*     */   
/*     */   public String toString() {
/* 254 */     return MoreObjects.toStringHelper(this)
/* 255 */       .add("minX", this.minX)
/* 256 */       .add("minY", this.minY)
/* 257 */       .add("minZ", this.minZ)
/* 258 */       .add("maxX", this.maxX)
/* 259 */       .add("maxY", this.maxY)
/* 260 */       .add("maxZ", this.maxZ)
/* 261 */       .toString();
/*     */   }
/*     */   
/*     */   public boolean equals(Object o) {
/* 266 */     if (this == o)
/* 267 */       return true; 
/* 269 */     if (o instanceof BoundingBox) {
/* 269 */       BoundingBox that = (BoundingBox)o;
/* 270 */       return (this.minX == that.minX && this.minY == that.minY && this.minZ == that.minZ && this.maxX == that.maxX && this.maxY == that.maxY && this.maxZ == that.maxZ);
/*     */     } 
/* 272 */     return false;
/*     */   }
/*     */   
/*     */   public int hashCode() {
/* 277 */     return Objects.hash(new Object[] { Integer.valueOf(this.minX), Integer.valueOf(this.minY), Integer.valueOf(this.minZ), Integer.valueOf(this.maxX), Integer.valueOf(this.maxY), Integer.valueOf(this.maxZ) });
/*     */   }
/*     */   
/*     */   public int minX() {
/* 281 */     return this.minX;
/*     */   }
/*     */   
/*     */   public int minY() {
/* 285 */     return this.minY;
/*     */   }
/*     */   
/*     */   public int minZ() {
/* 289 */     return this.minZ;
/*     */   }
/*     */   
/*     */   public int maxX() {
/* 293 */     return this.maxX;
/*     */   }
/*     */   
/*     */   public int maxY() {
/* 297 */     return this.maxY;
/*     */   }
/*     */   
/*     */   public int maxZ() {
/* 301 */     return this.maxZ;
/*     */   }
/*     */ }


/* Location:              C:\Users\karol\AppData\Roaming\ModrinthApp\meta\versions\26.2-0.19.5\26.2-0.19.5.jar!\net\minecraft\world\level\levelgen\structure\BoundingBox.class
 * Java compiler version: 25 (69.0)
 * JD-Core Version:       1.1.3
 */