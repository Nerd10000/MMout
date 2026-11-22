/*     */ package net.minecraft.world.level.levelgen;
/*     */ 
/*     */ import net.minecraft.world.level.chunk.ChunkAccess;
/*     */ 
/*     */ class SteepMaterialCondition extends SurfaceRules.LazyXZCondition {
/*     */   private SteepMaterialCondition(SurfaceRules.Context context) {
/* 247 */     super(context);
/*     */   }
/*     */   
/*     */   protected boolean compute() {
/* 252 */     int chunkBlockX = this.context.blockX & 0xF;
/* 253 */     int chunkBlockZ = this.context.blockZ & 0xF;
/* 255 */     int zNorth = Math.max(chunkBlockZ - 1, 0);
/* 256 */     int zSouth = Math.min(chunkBlockZ + 1, 15);
/* 258 */     ChunkAccess chunk = this.context.chunk;
/* 259 */     int heightNorth = chunk.getHeight(Heightmap.Types.WORLD_SURFACE_WG, chunkBlockX, zNorth);
/* 260 */     int heightSouth = chunk.getHeight(Heightmap.Types.WORLD_SURFACE_WG, chunkBlockX, zSouth);
/* 262 */     if (heightSouth >= heightNorth + 4)
/* 263 */       return true; 
/* 266 */     int xWest = Math.max(chunkBlockX - 1, 0);
/* 267 */     int xEast = Math.min(chunkBlockX + 1, 15);
/* 268 */     int heightWest = chunk.getHeight(Heightmap.Types.WORLD_SURFACE_WG, xWest, chunkBlockZ);
/* 269 */     int heightEast = chunk.getHeight(Heightmap.Types.WORLD_SURFACE_WG, xEast, chunkBlockZ);
/* 271 */     return (heightWest >= heightEast + 4);
/*     */   }
/*     */ }


/* Location:              C:\Users\karol\AppData\Roaming\ModrinthApp\meta\versions\26.2-0.19.5\26.2-0.19.5.jar!\net\minecraft\world\level\levelgen\SurfaceRules$Context$SteepMaterialCondition.class
 * Java compiler version: 25 (69.0)
 * JD-Core Version:       1.1.3
 */