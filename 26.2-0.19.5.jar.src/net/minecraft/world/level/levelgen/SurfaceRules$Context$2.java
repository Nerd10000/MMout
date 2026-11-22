/*     */ package net.minecraft.world.level.levelgen;
/*     */ 
/*     */ import java.util.function.DoubleSupplier;
/*     */ import net.minecraft.world.level.levelgen.synth.NormalNoise;
/*     */ 
/*     */ class null implements DoubleSupplier {
/* 202 */   private long lastUpdateY = SurfaceRules.Context.this.lastUpdateY - 1L;
/*     */   
/*     */   private double lastNoise;
/*     */   
/*     */   public double getAsDouble() {
/* 207 */     if (this.lastUpdateY != SurfaceRules.Context.this.lastUpdateY) {
/* 208 */       this.lastNoise = noise.getValue(SurfaceRules.Context.this.blockX, SurfaceRules.Context.this.blockY, SurfaceRules.Context.this.blockZ);
/* 209 */       this.lastUpdateY = SurfaceRules.Context.this.lastUpdateY;
/*     */     } 
/* 211 */     return this.lastNoise;
/*     */   }
/*     */ }


/* Location:              C:\Users\karol\AppData\Roaming\ModrinthApp\meta\versions\26.2-0.19.5\26.2-0.19.5.jar!\net\minecraft\world\level\levelgen\SurfaceRules$Context$2.class
 * Java compiler version: 25 (69.0)
 * JD-Core Version:       1.1.3
 */