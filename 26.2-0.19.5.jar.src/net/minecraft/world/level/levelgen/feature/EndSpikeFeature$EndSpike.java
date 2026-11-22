/*     */ package net.minecraft.world.level.levelgen.feature;
/*     */ 
/*     */ import com.mojang.datafixers.kinds.App;
/*     */ import com.mojang.datafixers.kinds.Applicative;
/*     */ import com.mojang.datafixers.util.Function5;
/*     */ import com.mojang.serialization.Codec;
/*     */ import com.mojang.serialization.codecs.RecordCodecBuilder;
/*     */ import net.minecraft.core.BlockPos;
/*     */ import net.minecraft.core.SectionPos;
/*     */ import net.minecraft.world.level.dimension.DimensionType;
/*     */ import net.minecraft.world.phys.AABB;
/*     */ 
/*     */ public class EndSpike {
/*     */   public static final Codec<EndSpike> CODEC;
/*     */   
/*     */   private final int centerX;
/*     */   
/*     */   private final int centerZ;
/*     */   
/*     */   private final int radius;
/*     */   
/*     */   private final int height;
/*     */   
/*     */   private final boolean guarded;
/*     */   
/*     */   private final AABB topBoundingBox;
/*     */   
/*     */   static {
/* 121 */     CODEC = RecordCodecBuilder.create(i -> i.group((App)Codec.INT.optionalFieldOf("centerX", Integer.valueOf(0)).forGetter(()), (App)Codec.INT.optionalFieldOf("centerZ", Integer.valueOf(0)).forGetter(()), (App)Codec.INT.optionalFieldOf("radius", Integer.valueOf(0)).forGetter(()), (App)Codec.INT.optionalFieldOf("height", Integer.valueOf(0)).forGetter(()), (App)Codec.BOOL.optionalFieldOf("guarded", Boolean.valueOf(false)).forGetter(())).apply((Applicative)i, EndSpike::new));
/*     */   }
/*     */   
/*     */   public EndSpike(int centerX, int centerZ, int radius, int height, boolean guarded) {
/* 137 */     this.centerX = centerX;
/* 138 */     this.centerZ = centerZ;
/* 139 */     this.radius = radius;
/* 140 */     this.height = height;
/* 141 */     this.guarded = guarded;
/* 143 */     this.topBoundingBox = new AABB((centerX - radius), DimensionType.MIN_Y, (centerZ - radius), (centerX + radius), DimensionType.MAX_Y, (centerZ + radius));
/*     */   }
/*     */   
/*     */   public boolean isCenterWithinChunk(BlockPos chunkOrigin) {
/* 147 */     return (SectionPos.blockToSectionCoord(chunkOrigin.getX()) == SectionPos.blockToSectionCoord(this.centerX) && 
/* 148 */       SectionPos.blockToSectionCoord(chunkOrigin.getZ()) == SectionPos.blockToSectionCoord(this.centerZ));
/*     */   }
/*     */   
/*     */   public int getCenterX() {
/* 152 */     return this.centerX;
/*     */   }
/*     */   
/*     */   public int getCenterZ() {
/* 156 */     return this.centerZ;
/*     */   }
/*     */   
/*     */   public int getRadius() {
/* 160 */     return this.radius;
/*     */   }
/*     */   
/*     */   public int getHeight() {
/* 164 */     return this.height;
/*     */   }
/*     */   
/*     */   public boolean isGuarded() {
/* 168 */     return this.guarded;
/*     */   }
/*     */   
/*     */   public AABB getTopBoundingBox() {
/* 172 */     return this.topBoundingBox;
/*     */   }
/*     */ }


/* Location:              C:\Users\karol\AppData\Roaming\ModrinthApp\meta\versions\26.2-0.19.5\26.2-0.19.5.jar!\net\minecraft\world\level\levelgen\feature\EndSpikeFeature$EndSpike.class
 * Java compiler version: 25 (69.0)
 * JD-Core Version:       1.1.3
 */