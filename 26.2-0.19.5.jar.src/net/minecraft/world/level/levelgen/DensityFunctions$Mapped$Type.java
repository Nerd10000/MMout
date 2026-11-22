/*     */ package net.minecraft.world.level.levelgen;
/*     */ 
/*     */ import net.minecraft.util.KeyDispatchDataCodec;
/*     */ import net.minecraft.util.StringRepresentable;
/*     */ 
/*     */ public enum Type implements StringRepresentable {
/* 915 */   ABS("abs"),
/* 916 */   SQUARE("square"),
/* 917 */   CUBE("cube"),
/* 918 */   HALF_NEGATIVE("half_negative"),
/* 919 */   QUARTER_NEGATIVE("quarter_negative"),
/* 920 */   INVERT("invert"),
/* 921 */   SQUEEZE("squeeze");
/*     */   
/*     */   private final String name;
/*     */   
/*     */   private final KeyDispatchDataCodec<DensityFunctions.Mapped> codec;
/*     */   
/*     */   Type(String name) {
/* 926 */     this.codec = DensityFunctions.singleFunctionArgumentCodec(input -> DensityFunctions.Mapped.create(this, input), DensityFunctions.Mapped::input);
/* 929 */     this.name = name;
/*     */   }
/*     */   
/*     */   public String getSerializedName() {
/* 934 */     return this.name;
/*     */   }
/*     */ }


/* Location:              C:\Users\karol\AppData\Roaming\ModrinthApp\meta\versions\26.2-0.19.5\26.2-0.19.5.jar!\net\minecraft\world\level\levelgen\DensityFunctions$Mapped$Type.class
 * Java compiler version: 25 (69.0)
 * JD-Core Version:       1.1.3
 */