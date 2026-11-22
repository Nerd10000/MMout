/*     */ package net.minecraft.core;
/*     */ 
/*     */ import io.netty.buffer.ByteBuf;
/*     */ import it.unimi.dsi.fastutil.longs.LongConsumer;
/*     */ import java.util.Spliterators;
/*     */ import java.util.function.Consumer;
/*     */ import java.util.stream.Stream;
/*     */ import java.util.stream.StreamSupport;
/*     */ import net.minecraft.network.codec.ByteBufCodecs;
/*     */ import net.minecraft.network.codec.StreamCodec;
/*     */ import net.minecraft.util.Mth;
/*     */ import net.minecraft.world.level.ChunkPos;
/*     */ import net.minecraft.world.level.chunk.ChunkAccess;
/*     */ import net.minecraft.world.level.entity.EntityAccess;
/*     */ 
/*     */ public class SectionPos extends Vec3i {
/*     */   public static final int SECTION_BITS = 4;
/*     */   
/*     */   public static final int SECTION_SIZE = 16;
/*     */   
/*     */   public static final int SECTION_BLOCK_COUNT = 4096;
/*     */   
/*     */   public static final int SECTION_MASK = 15;
/*     */   
/*     */   public static final int SECTION_HALF_SIZE = 8;
/*     */   
/*     */   public static final int SECTION_MAX_INDEX = 15;
/*     */   
/*     */   private static final int PACKED_X_LENGTH = 22;
/*     */   
/*     */   private static final int PACKED_Y_LENGTH = 20;
/*     */   
/*     */   private static final int PACKED_Z_LENGTH = 22;
/*     */   
/*     */   private static final long PACKED_X_MASK = 4194303L;
/*     */   
/*     */   private static final long PACKED_Y_MASK = 1048575L;
/*     */   
/*     */   private static final long PACKED_Z_MASK = 4194303L;
/*     */   
/*     */   private static final int Y_OFFSET = 0;
/*     */   
/*     */   private static final int Z_OFFSET = 20;
/*     */   
/*     */   private static final int X_OFFSET = 42;
/*     */   
/*     */   private static final int RELATIVE_X_SHIFT = 8;
/*     */   
/*     */   private static final int RELATIVE_Y_SHIFT = 0;
/*     */   
/*     */   private static final int RELATIVE_Z_SHIFT = 4;
/*     */   
/*  49 */   public static final StreamCodec<ByteBuf, SectionPos> STREAM_CODEC = ByteBufCodecs.LONG.map(SectionPos::of, SectionPos::asLong);
/*     */   
/*     */   private SectionPos(int x, int y, int z) {
/*  52 */     super(x, y, z);
/*     */   }
/*     */   
/*     */   public static SectionPos of(int x, int y, int z) {
/*  56 */     return new SectionPos(x, y, z);
/*     */   }
/*     */   
/*     */   public static SectionPos of(BlockPos pos) {
/*  60 */     return new SectionPos(blockToSectionCoord(pos.getX()), blockToSectionCoord(pos.getY()), blockToSectionCoord(pos.getZ()));
/*     */   }
/*     */   
/*     */   public static SectionPos of(ChunkPos pos, int sectionY) {
/*  64 */     return new SectionPos(pos.x(), sectionY, pos.z());
/*     */   }
/*     */   
/*     */   public static SectionPos of(EntityAccess entity) {
/*  68 */     return of(entity.blockPosition());
/*     */   }
/*     */   
/*     */   public static SectionPos of(Position pos) {
/*  72 */     return new SectionPos(
/*  73 */         blockToSectionCoord(pos.x()), 
/*  74 */         blockToSectionCoord(pos.y()), 
/*  75 */         blockToSectionCoord(pos.z()));
/*     */   }
/*     */   
/*     */   public static SectionPos of(long sectionNode) {
/*  80 */     return new SectionPos(x(sectionNode), y(sectionNode), z(sectionNode));
/*     */   }
/*     */   
/*     */   public static SectionPos bottomOf(ChunkAccess chunk) {
/*  84 */     return of(chunk.getPos(), chunk.getMinSectionY());
/*     */   }
/*     */   
/*     */   public static long offset(long sectionNode, Direction offset) {
/*  88 */     return offset(sectionNode, offset.getStepX(), offset.getStepY(), offset.getStepZ());
/*     */   }
/*     */   
/*     */   public static long offset(long sectionNode, int stepX, int stepY, int stepZ) {
/*  92 */     return asLong(x(sectionNode) + stepX, y(sectionNode) + stepY, z(sectionNode) + stepZ);
/*     */   }
/*     */   
/*     */   public static int posToSectionCoord(double pos) {
/*  96 */     return blockToSectionCoord(Mth.floor(pos));
/*     */   }
/*     */   
/*     */   public static int blockToSectionCoord(int blockCoord) {
/* 100 */     return blockCoord >> 4;
/*     */   }
/*     */   
/*     */   public static int blockToSectionCoord(double coord) {
/* 104 */     return Mth.floor(coord) >> 4;
/*     */   }
/*     */   
/*     */   public static int sectionRelative(int blockCoord) {
/* 108 */     return blockCoord & 0xF;
/*     */   }
/*     */   
/*     */   public static short sectionRelativePos(BlockPos pos) {
/* 112 */     int x = sectionRelative(pos.getX());
/* 113 */     int y = sectionRelative(pos.getY());
/* 114 */     int z = sectionRelative(pos.getZ());
/* 115 */     return (short)(x << 8 | z << 4 | y << 0);
/*     */   }
/*     */   
/*     */   public static int sectionRelativeX(short relative) {
/* 119 */     return relative >>> 8 & 0xF;
/*     */   }
/*     */   
/*     */   public static int sectionRelativeY(short relative) {
/* 123 */     return relative >>> 0 & 0xF;
/*     */   }
/*     */   
/*     */   public static int sectionRelativeZ(short relative) {
/* 127 */     return relative >>> 4 & 0xF;
/*     */   }
/*     */   
/*     */   public int relativeToBlockX(short relative) {
/* 131 */     return minBlockX() + sectionRelativeX(relative);
/*     */   }
/*     */   
/*     */   public int relativeToBlockY(short relative) {
/* 135 */     return minBlockY() + sectionRelativeY(relative);
/*     */   }
/*     */   
/*     */   public int relativeToBlockZ(short relative) {
/* 139 */     return minBlockZ() + sectionRelativeZ(relative);
/*     */   }
/*     */   
/*     */   public BlockPos relativeToBlockPos(short relative) {
/* 143 */     return new BlockPos(relativeToBlockX(relative), relativeToBlockY(relative), relativeToBlockZ(relative));
/*     */   }
/*     */   
/*     */   public static int sectionToBlockCoord(int sectionCoord) {
/* 149 */     return sectionCoord << 4;
/*     */   }
/*     */   
/*     */   public static int sectionToBlockCoord(int sectionCoord, int offset) {
/* 153 */     return sectionToBlockCoord(sectionCoord) + offset;
/*     */   }
/*     */   
/*     */   public static int x(long sectionNode) {
/* 157 */     return (int)(sectionNode << 0L >> 42L);
/*     */   }
/*     */   
/*     */   public static int y(long sectionNode) {
/* 161 */     return (int)(sectionNode << 44L >> 44L);
/*     */   }
/*     */   
/*     */   public static int z(long sectionNode) {
/* 165 */     return (int)(sectionNode << 22L >> 42L);
/*     */   }
/*     */   
/*     */   public int x() {
/* 169 */     return getX();
/*     */   }
/*     */   
/*     */   public int y() {
/* 173 */     return getY();
/*     */   }
/*     */   
/*     */   public int z() {
/* 177 */     return getZ();
/*     */   }
/*     */   
/*     */   public int minBlockX() {
/* 181 */     return sectionToBlockCoord(x());
/*     */   }
/*     */   
/*     */   public int minBlockY() {
/* 185 */     return sectionToBlockCoord(y());
/*     */   }
/*     */   
/*     */   public int minBlockZ() {
/* 189 */     return sectionToBlockCoord(z());
/*     */   }
/*     */   
/*     */   public int maxBlockX() {
/* 193 */     return sectionToBlockCoord(x(), 15);
/*     */   }
/*     */   
/*     */   public int maxBlockY() {
/* 197 */     return sectionToBlockCoord(y(), 15);
/*     */   }
/*     */   
/*     */   public int maxBlockZ() {
/* 201 */     return sectionToBlockCoord(z(), 15);
/*     */   }
/*     */   
/*     */   public static long blockToSection(long blockNode) {
/* 205 */     return asLong(
/* 206 */         blockToSectionCoord(BlockPos.getX(blockNode)), 
/* 207 */         blockToSectionCoord(BlockPos.getY(blockNode)), 
/* 208 */         blockToSectionCoord(BlockPos.getZ(blockNode)));
/*     */   }
/*     */   
/*     */   public static long getZeroNode(int x, int z) {
/* 213 */     return getZeroNode(asLong(x, 0, z));
/*     */   }
/*     */   
/*     */   public static long getZeroNode(long sectionNode) {
/* 218 */     return sectionNode & 0xFFFFFFFFFFF00000L;
/*     */   }
/*     */   
/*     */   public static long sectionToChunk(long sectionNode) {
/* 222 */     return ChunkPos.pack(x(sectionNode), z(sectionNode));
/*     */   }
/*     */   
/*     */   public BlockPos origin() {
/* 226 */     return new BlockPos(sectionToBlockCoord(x()), sectionToBlockCoord(y()), sectionToBlockCoord(z()));
/*     */   }
/*     */   
/*     */   public BlockPos center() {
/* 230 */     int delta = 8;
/* 231 */     return origin().offset(8, 8, 8);
/*     */   }
/*     */   
/*     */   public ChunkPos chunk() {
/* 235 */     return new ChunkPos(x(), z());
/*     */   }
/*     */   
/*     */   public static long asLong(BlockPos pos) {
/* 239 */     return asLong(blockToSectionCoord(pos.getX()), blockToSectionCoord(pos.getY()), blockToSectionCoord(pos.getZ()));
/*     */   }
/*     */   
/*     */   public static long asLong(int x, int y, int z) {
/* 243 */     long node = 0L;
/* 244 */     node |= (x & 0x3FFFFFL) << 42L;
/* 245 */     node |= (y & 0xFFFFFL) << 0L;
/* 246 */     node |= (z & 0x3FFFFFL) << 20L;
/* 247 */     return node;
/*     */   }
/*     */   
/*     */   public long asLong() {
/* 251 */     return asLong(x(), y(), z());
/*     */   }
/*     */   
/*     */   public SectionPos offset(int x, int y, int z) {
/* 256 */     if (x == 0 && y == 0 && z == 0)
/* 257 */       return this; 
/* 259 */     return new SectionPos(x() + x, y() + y, z() + z);
/*     */   }
/*     */   
/*     */   public Stream<BlockPos> blocksInside() {
/* 263 */     return BlockPos.betweenClosedStream(minBlockX(), minBlockY(), minBlockZ(), maxBlockX(), maxBlockY(), maxBlockZ());
/*     */   }
/*     */   
/*     */   public static Stream<SectionPos> cube(SectionPos center, int radius) {
/* 267 */     int x = center.x();
/* 268 */     int y = center.y();
/* 269 */     int z = center.z();
/* 270 */     return betweenClosedStream(x - radius, y - radius, z - radius, x + radius, y + radius, z + radius);
/*     */   }
/*     */   
/*     */   public static Stream<SectionPos> aroundChunk(ChunkPos center, int radius, int minSection, int maxSection) {
/* 274 */     int x = center.x();
/* 275 */     int z = center.z();
/* 276 */     return betweenClosedStream(x - radius, minSection, z - radius, x + radius, maxSection, z + radius);
/*     */   }
/*     */   
/*     */   public static Stream<SectionPos> betweenClosedStream(final int minX, final int minY, final int minZ, final int maxX, final int maxY, final int maxZ) {
/* 280 */     return StreamSupport.stream(new Spliterators.AbstractSpliterator<SectionPos>(((maxX - minX + 1) * (maxY - minY + 1) * (maxZ - minZ + 1)), 64) {
/* 281 */           private final Cursor3D cursor = new Cursor3D(minX, minY, minZ, maxX, maxY, maxZ);
/*     */           
/*     */           public boolean tryAdvance(Consumer<? super SectionPos> action) {
/* 285 */             if (this.cursor.advance()) {
/* 286 */               action.accept(new SectionPos(this.cursor.nextX(), this.cursor.nextY(), this.cursor.nextZ()));
/* 287 */               return true;
/*     */             } 
/* 289 */             return false;
/*     */           }
/*     */         }false);
/*     */   }
/*     */   
/*     */   public static void aroundAndAtBlockPos(BlockPos blockPos, LongConsumer sectionConsumer) {
/* 295 */     aroundAndAtBlockPos(blockPos.getX(), blockPos.getY(), blockPos.getZ(), sectionConsumer);
/*     */   }
/*     */   
/*     */   public static void aroundAndAtBlockPos(long blockPos, LongConsumer sectionConsumer) {
/* 299 */     aroundAndAtBlockPos(BlockPos.getX(blockPos), BlockPos.getY(blockPos), BlockPos.getZ(blockPos), sectionConsumer);
/*     */   }
/*     */   
/*     */   public static void aroundAndAtBlockPos(int blockX, int blockY, int blockZ, LongConsumer sectionConsumer) {
/* 309 */     int minSectionX = blockToSectionCoord(blockX - 1);
/* 310 */     int maxSectionX = blockToSectionCoord(blockX + 1);
/* 312 */     int minSectionY = blockToSectionCoord(blockY - 1);
/* 313 */     int maxSectionY = blockToSectionCoord(blockY + 1);
/* 315 */     int minSectionZ = blockToSectionCoord(blockZ - 1);
/* 316 */     int maxSectionZ = blockToSectionCoord(blockZ + 1);
/* 318 */     if (minSectionX == maxSectionX && minSectionY == maxSectionY && minSectionZ == maxSectionZ) {
/* 319 */       sectionConsumer.accept(asLong(minSectionX, minSectionY, minSectionZ));
/*     */     } else {
/* 321 */       for (int sectionX = minSectionX; sectionX <= maxSectionX; sectionX++) {
/* 322 */         for (int sectionY = minSectionY; sectionY <= maxSectionY; sectionY++) {
/* 323 */           for (int sectionZ = minSectionZ; sectionZ <= maxSectionZ; sectionZ++)
/* 324 */             sectionConsumer.accept(asLong(sectionX, sectionY, sectionZ)); 
/*     */         } 
/*     */       } 
/*     */     } 
/*     */   }
/*     */ }


/* Location:              C:\Users\karol\AppData\Roaming\ModrinthApp\meta\versions\26.2-0.19.5\26.2-0.19.5.jar!\net\minecraft\core\SectionPos.class
 * Java compiler version: 25 (69.0)
 * JD-Core Version:       1.1.3
 */