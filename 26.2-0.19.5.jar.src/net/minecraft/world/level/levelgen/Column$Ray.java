/*     */ package net.minecraft.world.level.levelgen;
/*     */ 
/*     */ import java.util.OptionalInt;
/*     */ 
/*     */ public final class Ray extends Column {
/*     */   private final int edge;
/*     */   
/*     */   private final boolean pointingUp;
/*     */   
/*     */   public Ray(int edge, boolean pointingUp) {
/* 193 */     this.edge = edge;
/* 194 */     this.pointingUp = pointingUp;
/*     */   }
/*     */   
/*     */   public OptionalInt getCeiling() {
/* 199 */     return this.pointingUp ? OptionalInt.empty() : OptionalInt.of(this.edge);
/*     */   }
/*     */   
/*     */   public OptionalInt getFloor() {
/* 204 */     return this.pointingUp ? OptionalInt.of(this.edge) : OptionalInt.empty();
/*     */   }
/*     */   
/*     */   public OptionalInt getHeight() {
/* 209 */     return OptionalInt.empty();
/*     */   }
/*     */   
/*     */   public String toString() {
/* 214 */     return this.pointingUp ? ("C(" + this.edge + "-)") : ("C(-" + this.edge + ")");
/*     */   }
/*     */ }


/* Location:              C:\Users\karol\AppData\Roaming\ModrinthApp\meta\versions\26.2-0.19.5\26.2-0.19.5.jar!\net\minecraft\world\level\levelgen\Column$Ray.class
 * Java compiler version: 25 (69.0)
 * JD-Core Version:       1.1.3
 */