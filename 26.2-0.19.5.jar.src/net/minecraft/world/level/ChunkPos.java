/*     */ package net.minecraft.world.level;
/*     */ 
/*     */ import com.mojang.serialization.Codec;
/*     */ import com.mojang.serialization.DataResult;
/*     */ import io.netty.buffer.ByteBuf;
/*     */ import java.util.Spliterators;
/*     */ import java.util.function.Consumer;
/*     */ import java.util.stream.IntStream;
/*     */ import java.util.stream.Stream;
/*     */ import java.util.stream.StreamSupport;
/*     */ import net.minecraft.core.BlockPos;
/*     */ import net.minecraft.core.SectionPos;
/*     */ import net.minecraft.network.FriendlyByteBuf;
/*     */ import net.minecraft.network.codec.StreamCodec;
/*     */ import net.minecraft.util.Mth;
/*     */ import net.minecraft.util.Util;
/*     */ import net.minecraft.world.level.chunk.status.ChunkPyramid;
/*     */ 
/*     */ public final class ChunkPos extends Record {
/*     */   private final int x;
/*     */   
/*     */   private final int z;
/*     */   
/*     */   public static final Codec<ChunkPos> CODEC;
/*     */   
/*     */   public ChunkPos(int x, int z) {
/*  22 */     this.x = x;
/*  22 */     this.z = z;
/*     */   }
/*     */   
/*     */   public final boolean equals(Object o) {
/*     */     // Byte code:
/*     */     //   0: aload_0
/*     */     //   1: aload_1
/*     */     //   2: <illegal opcode> equals : (Lnet/minecraft/world/level/ChunkPos;Ljava/lang/Object;)Z
/*     */     //   7: ireturn
/*     */     // Line number table:
/*     */     //   Java source line number -> byte code offset
/*     */     //   #22	-> 0
/*     */     // Local variable table:
/*     */     //   start	length	slot	name	descriptor
/*     */     //   0	8	0	this	Lnet/minecraft/world/level/ChunkPos;
/*     */     //   0	8	1	o	Ljava/lang/Object;
/*     */   }
/*     */   
/*     */   public int x() {
/*  22 */     return this.x;
/*     */   }
/*     */   
/*     */   public int z() {
/*  22 */     return this.z;
/*     */   }
/*     */   
/*     */   static {
/*  27 */     CODEC = Codec.INT_STREAM.comapFlatMap(input -> Util.fixedSize(input, 2).map(()), pos -> IntStream.of(new int[] { pos.x, pos.z })).stable();
/*     */   }
/*     */   
/*  29 */   public static final StreamCodec<ByteBuf, ChunkPos> STREAM_CODEC = new StreamCodec<ByteBuf, ChunkPos>() {
/*     */       public ChunkPos decode(ByteBuf input) {
/*  32 */         return FriendlyByteBuf.readChunkPos(input);
/*     */       }
/*     */       
/*     */       public void encode(ByteBuf output, ChunkPos value) {
/*  37 */         FriendlyByteBuf.writeChunkPos(output, value);
/*     */       }
/*     */     };
/*     */   
/*     */   private static final int SAFETY_MARGIN = 1056;
/*     */   
/*  42 */   public static final long INVALID_CHUNK_POS = pack(1875066, 1875066);
/*     */   
/*  44 */   public static final ChunkPos ZERO = new ChunkPos(0, 0);
/*     */   
/*     */   private static final long COORD_BITS = 32L;
/*     */   
/*     */   private static final long COORD_MASK = 4294967295L;
/*     */   
/*     */   private static final int REGION_BITS = 5;
/*     */   
/*     */   public static final int REGION_SIZE = 32;
/*     */   
/*     */   private static final int REGION_MASK = 31;
/*     */   
/*     */   public static final int REGION_MAX_INDEX = 31;
/*     */   
/*     */   private static final int HASH_A = 1664525;
/*     */   
/*     */   private static final int HASH_C = 1013904223;
/*     */   
/*     */   private static final int HASH_Z_XOR = -559038737;
/*     */   
/*     */   public static ChunkPos containing(BlockPos pos) {
/*  54 */     return new ChunkPos(SectionPos.blockToSectionCoord(pos.getX()), SectionPos.blockToSectionCoord(pos.getZ()));
/*     */   }
/*     */   
/*     */   public static ChunkPos unpack(long key) {
/*  58 */     return new ChunkPos((int)key, (int)(key >> 32L));
/*     */   }
/*     */   
/*     */   public static ChunkPos minFromRegion(int regionX, int regionZ) {
/*  62 */     return new ChunkPos(regionX << 5, regionZ << 5);
/*     */   }
/*     */   
/*     */   public static ChunkPos maxFromRegion(int regionX, int regionZ) {
/*  66 */     return new ChunkPos((regionX << 5) + 31, (regionZ << 5) + 31);
/*     */   }
/*     */   
/*     */   public boolean isValid() {
/*  70 */     return isValid(this.x, this.z);
/*     */   }
/*     */   
/*     */   public static boolean isValid(int x, int z) {
/*  74 */     return (Mth.absMax(x, z) <= ChunkPyramid.MAX_CHUNK_COORDINATE_VALUE);
/*     */   }
/*     */   
/*     */   public long pack() {
/*  78 */     return pack(this.x, this.z);
/*     */   }
/*     */   
/*     */   public static long pack(int x, int z) {
/*  82 */     return x & 0xFFFFFFFFL | (z & 0xFFFFFFFFL) << 32L;
/*     */   }
/*     */   
/*     */   public static long fromSectionNode(long sectionNode) {
/*  86 */     return pack(SectionPos.x(sectionNode), SectionPos.z(sectionNode));
/*     */   }
/*     */   
/*     */   public static long pack(BlockPos pos) {
/*  90 */     return pack(SectionPos.blockToSectionCoord(pos.getX()), SectionPos.blockToSectionCoord(pos.getZ()));
/*     */   }
/*     */   
/*     */   public static int getX(long pos) {
/*  94 */     return (int)(pos & 0xFFFFFFFFL);
/*     */   }
/*     */   
/*     */   public static int getZ(long pos) {
/*  98 */     return (int)(pos >>> 32L & 0xFFFFFFFFL);
/*     */   }
/*     */   
/*     */   public int hashCode() {
/* 107 */     return hash(this.x, this.z);
/*     */   }
/*     */   
/*     */   public static int hash(int x, int z) {
/* 112 */     int xTransform = 1664525 * x + 1013904223;
/* 113 */     int zTransform = 1664525 * (z ^ 0xDEADBEEF) + 1013904223;
/* 114 */     return xTransform ^ zTransform;
/*     */   }
/*     */   
/*     */   public int getMiddleBlockX() {
/* 118 */     return getBlockX(8);
/*     */   }
/*     */   
/*     */   public int getMiddleBlockZ() {
/* 122 */     return getBlockZ(8);
/*     */   }
/*     */   
/*     */   public int getMinBlockX() {
/* 126 */     return SectionPos.sectionToBlockCoord(this.x);
/*     */   }
/*     */   
/*     */   public int getMinBlockZ() {
/* 130 */     return SectionPos.sectionToBlockCoord(this.z);
/*     */   }
/*     */   
/*     */   public int getMaxBlockX() {
/* 134 */     return getBlockX(15);
/*     */   }
/*     */   
/*     */   public int getMaxBlockZ() {
/* 138 */     return getBlockZ(15);
/*     */   }
/*     */   
/*     */   public int getRegionX() {
/* 142 */     return this.x >> 5;
/*     */   }
/*     */   
/*     */   public int getRegionZ() {
/* 146 */     return this.z >> 5;
/*     */   }
/*     */   
/*     */   public static int getRegionX(long pos) {
/* 150 */     return getX(pos) >> 5;
/*     */   }
/*     */   
/*     */   public static int getRegionZ(long pos) {
/* 154 */     return getZ(pos) >> 5;
/*     */   }
/*     */   
/*     */   public int getRegionLocalX() {
/* 158 */     return this.x & 0x1F;
/*     */   }
/*     */   
/*     */   public int getRegionLocalZ() {
/* 162 */     return this.z & 0x1F;
/*     */   }
/*     */   
/*     */   public BlockPos getBlockAt(int x, int y, int z) {
/* 166 */     return new BlockPos(getBlockX(x), y, getBlockZ(z));
/*     */   }
/*     */   
/*     */   public int getBlockX(int offset) {
/* 170 */     return SectionPos.sectionToBlockCoord(this.x, offset);
/*     */   }
/*     */   
/*     */   public int getBlockZ(int offset) {
/* 174 */     return SectionPos.sectionToBlockCoord(this.z, offset);
/*     */   }
/*     */   
/*     */   public BlockPos getMiddleBlockPosition(int y) {
/* 178 */     return new BlockPos(getMiddleBlockX(), y, getMiddleBlockZ());
/*     */   }
/*     */   
/*     */   public boolean contains(BlockPos pos) {
/* 182 */     return (pos.getX() >= getMinBlockX() && pos.getZ() >= getMinBlockZ() && pos
/* 183 */       .getX() <= getMaxBlockX() && pos.getZ() <= getMaxBlockZ());
/*     */   }
/*     */   
/*     */   public String toString() {
/* 188 */     return "[" + this.x + ", " + this.z + "]";
/*     */   }
/*     */   
/*     */   public BlockPos getWorldPosition() {
/* 192 */     return new BlockPos(getMinBlockX(), 0, getMinBlockZ());
/*     */   }
/*     */   
/*     */   public int getChessboardDistance(ChunkPos pos) {
/* 196 */     return getChessboardDistance(pos.x, pos.z);
/*     */   }
/*     */   
/*     */   public int getChessboardDistance(int x, int z) {
/* 200 */     return Mth.chessboardDistance(x, z, this.x, this.z);
/*     */   }
/*     */   
/*     */   public int distanceSquared(ChunkPos pos) {
/* 204 */     return distanceSquared(pos.x, pos.z);
/*     */   }
/*     */   
/*     */   public int distanceSquared(long pos) {
/* 208 */     return distanceSquared(getX(pos), getZ(pos));
/*     */   }
/*     */   
/*     */   private int distanceSquared(int x, int z) {
/* 212 */     int deltaX = x - this.x;
/* 213 */     int deltaZ = z - this.z;
/* 214 */     return deltaX * deltaX + deltaZ * deltaZ;
/*     */   }
/*     */   
/*     */   public static Stream<ChunkPos> rangeClosed(ChunkPos center, int radius) {
/* 218 */     return rangeClosed(new ChunkPos(center.x - radius, center.z - radius), new ChunkPos(center.x + radius, center.z + radius));
/*     */   }
/*     */   
/*     */   public static Stream<ChunkPos> rangeClosed(final ChunkPos from, final ChunkPos to) {
/* 222 */     int xSize = Math.abs(from.x - to.x) + 1;
/* 223 */     int zSize = Math.abs(from.z - to.z) + 1;
/* 224 */     final int xDiff = (from.x < to.x) ? 1 : -1;
/* 225 */     final int zDiff = (from.z < to.z) ? 1 : -1;
/* 226 */     return StreamSupport.stream(new Spliterators.AbstractSpliterator<ChunkPos>((xSize * zSize), 64) {
/*     */           private ChunkPos pos;
/*     */           
/*     */           public boolean tryAdvance(Consumer<? super ChunkPos> action) {
/* 231 */             if (this.pos == null) {
/* 232 */               this.pos = from;
/*     */             } else {
/* 234 */               int x = this.pos.x;
/* 235 */               int z = this.pos.z;
/* 236 */               if (x == to.x) {
/* 237 */                 if (z == to.z)
/* 238 */                   return false; 
/* 240 */                 this.pos = new ChunkPos(from.x, z + zDiff);
/*     */               } else {
/* 242 */                 this.pos = new ChunkPos(x + xDiff, z);
/*     */               } 
/*     */             } 
/* 245 */             action.accept(this.pos);
/* 246 */             return true;
/*     */           }
/*     */         }false);
/*     */   }
/*     */ }


/* Location:              C:\Users\karol\AppData\Roaming\ModrinthApp\meta\versions\26.2-0.19.5\26.2-0.19.5.jar!\net\minecraft\world\level\ChunkPos.class
 * Java compiler version: 25 (69.0)
 * JD-Core Version:       1.1.3
 */