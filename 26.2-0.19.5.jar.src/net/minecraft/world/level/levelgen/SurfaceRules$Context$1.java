/*     */ package net.minecraft.world.level.levelgen;
/*     */ 
/*     */ import java.util.function.DoubleSupplier;
/*     */ import net.minecraft.world.level.levelgen.synth.NormalNoise;
/*     */ 
/*     */ class null implements DoubleSupplier {
/* 185 */   private long lastUpdateXZ = SurfaceRules.Context.this.lastUpdateXZ - 1L;
/*     */   
/*     */   private double lastNoise;
/*     */   
/*     */   public double getAsDouble() {
/* 190 */     if (this.lastUpdateXZ != SurfaceRules.Context.this.lastUpdateXZ) {
/* 191 */       this.lastNoise = noise.getValue(SurfaceRules.Context.this.blockX, 0.0D, SurfaceRules.Context.this.blockZ);
/* 192 */       this.lastUpdateXZ = SurfaceRules.Context.this.lastUpdateXZ;
/*     */     } 
/* 194 */     return this.lastNoise;
/*     */   }
/*     */ }


/* Location:              C:\Users\karol\AppData\Roaming\ModrinthApp\meta\versions\26.2-0.19.5\26.2-0.19.5.jar!\net\minecraft\world\level\levelgen\SurfaceRules$Context$1.class
 * Java compiler version: 25 (69.0)
 * JD-Core Version:       1.1.3
 */