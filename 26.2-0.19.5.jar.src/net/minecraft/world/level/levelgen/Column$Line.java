/*     */ package net.minecraft.world.level.levelgen;
/*     */ 
/*     */ import java.util.OptionalInt;
/*     */ 
/*     */ public final class Line extends Column {
/* 161 */   private static final Line INSTANCE = new Line();
/*     */   
/*     */   public OptionalInt getCeiling() {
/* 168 */     return OptionalInt.empty();
/*     */   }
/*     */   
/*     */   public OptionalInt getFloor() {
/* 173 */     return OptionalInt.empty();
/*     */   }
/*     */   
/*     */   public OptionalInt getHeight() {
/* 178 */     return OptionalInt.empty();
/*     */   }
/*     */   
/*     */   public String toString() {
/* 183 */     return "C(-)";
/*     */   }
/*     */ }


/* Location:              C:\Users\karol\AppData\Roaming\ModrinthApp\meta\versions\26.2-0.19.5\26.2-0.19.5.jar!\net\minecraft\world\level\levelgen\Column$Line.class
 * Java compiler version: 25 (69.0)
 * JD-Core Version:       1.1.3
 */