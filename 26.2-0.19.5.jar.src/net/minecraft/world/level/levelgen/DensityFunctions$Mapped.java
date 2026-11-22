/*     */ package net.minecraft.world.level.levelgen;
/*     */ 
/*     */ import net.minecraft.util.KeyDispatchDataCodec;
/*     */ import net.minecraft.util.Mth;
/*     */ import net.minecraft.util.StringRepresentable;
/*     */ 
/*     */ public final class Mapped extends Record implements DensityFunctions.PureTransformer {
/*     */   private final Type type;
/*     */   
/*     */   private final DensityFunction input;
/*     */   
/*     */   private final double minValue;
/*     */   
/*     */   private final double maxValue;
/*     */   
/*     */   protected Mapped(Type type, DensityFunction input, double minValue, double maxValue) {
/* 893 */     this.type = type;
/* 893 */     this.input = input;
/* 893 */     this.minValue = minValue;
/* 893 */     this.maxValue = maxValue;
/*     */   }
/*     */   
/*     */   public final String toString() {
/*     */     // Byte code:
/*     */     //   0: aload_0
/*     */     //   1: <illegal opcode> toString : (Lnet/minecraft/world/level/levelgen/DensityFunctions$Mapped;)Ljava/lang/String;
/*     */     //   6: areturn
/*     */     // Line number table:
/*     */     //   Java source line number -> byte code offset
/*     */     //   #893	-> 0
/*     */     // Local variable table:
/*     */     //   start	length	slot	name	descriptor
/*     */     //   0	7	0	this	Lnet/minecraft/world/level/levelgen/DensityFunctions$Mapped;
/*     */   }
/*     */   
/*     */   public final int hashCode() {
/*     */     // Byte code:
/*     */     //   0: aload_0
/*     */     //   1: <illegal opcode> hashCode : (Lnet/minecraft/world/level/levelgen/DensityFunctions$Mapped;)I
/*     */     //   6: ireturn
/*     */     // Line number table:
/*     */     //   Java source line number -> byte code offset
/*     */     //   #893	-> 0
/*     */     // Local variable table:
/*     */     //   start	length	slot	name	descriptor
/*     */     //   0	7	0	this	Lnet/minecraft/world/level/levelgen/DensityFunctions$Mapped;
/*     */   }
/*     */   
/*     */   public final boolean equals(Object o) {
/*     */     // Byte code:
/*     */     //   0: aload_0
/*     */     //   1: aload_1
/*     */     //   2: <illegal opcode> equals : (Lnet/minecraft/world/level/levelgen/DensityFunctions$Mapped;Ljava/lang/Object;)Z
/*     */     //   7: ireturn
/*     */     // Line number table:
/*     */     //   Java source line number -> byte code offset
/*     */     //   #893	-> 0
/*     */     // Local variable table:
/*     */     //   start	length	slot	name	descriptor
/*     */     //   0	8	0	this	Lnet/minecraft/world/level/levelgen/DensityFunctions$Mapped;
/*     */     //   0	8	1	o	Ljava/lang/Object;
/*     */   }
/*     */   
/*     */   public Type type() {
/* 893 */     return this.type;
/*     */   }
/*     */   
/*     */   public DensityFunction input() {
/* 893 */     return this.input;
/*     */   }
/*     */   
/*     */   public double minValue() {
/* 893 */     return this.minValue;
/*     */   }
/*     */   
/*     */   public double maxValue() {
/* 893 */     return this.maxValue;
/*     */   }
/*     */   
/*     */   public static Mapped create(Type type, DensityFunction input) {
/* 895 */     double minValue = input.minValue();
/* 896 */     double maxValue = input.maxValue();
/* 897 */     double minImage = transform(type, minValue);
/* 898 */     double maxImage = transform(type, maxValue);
/* 899 */     if (type == Type.INVERT) {
/* 900 */       if (minValue < 0.0D && maxValue > 0.0D)
/* 901 */         return new Mapped(type, input, Double.NEGATIVE_INFINITY, Double.POSITIVE_INFINITY); 
/* 904 */       return new Mapped(type, input, maxImage, minImage);
/*     */     } 
/* 906 */     if (type == Type.ABS || type == Type.SQUARE)
/* 908 */       return new Mapped(type, input, Math.max(0.0D, minValue), Math.max(minImage, maxImage)); 
/* 911 */     return new Mapped(type, input, minImage, maxImage);
/*     */   }
/*     */   
/*     */   public enum Type implements StringRepresentable {
/* 915 */     ABS("abs"),
/* 916 */     SQUARE("square"),
/* 917 */     CUBE("cube"),
/* 918 */     HALF_NEGATIVE("half_negative"),
/* 919 */     QUARTER_NEGATIVE("quarter_negative"),
/* 920 */     INVERT("invert"),
/* 921 */     SQUEEZE("squeeze");
/*     */     
/*     */     private final String name;
/*     */     
/*     */     private final KeyDispatchDataCodec<DensityFunctions.Mapped> codec;
/*     */     
/*     */     Type(String name) {
/* 926 */       this.codec = DensityFunctions.singleFunctionArgumentCodec(input -> DensityFunctions.Mapped.create(this, input), DensityFunctions.Mapped::input);
/* 929 */       this.name = name;
/*     */     }
/*     */     
/*     */     public String getSerializedName() {
/* 934 */       return this.name;
/*     */     }
/*     */   }
/*     */   
/*     */   private static double transform(Type type, double input) {
/* 939 */     switch (type.ordinal()) {
/*     */       default:
/* 939 */         throw new MatchException(null, null);
/*     */       case 0:
/*     */       
/*     */       case 1:
/*     */       
/*     */       case 2:
/*     */       
/*     */       case 3:
/* 939 */         return 
/*     */           
/* 943 */           (input > 0.0D) ? input : (input * 0.5D);
/*     */       case 4:
/* 944 */         return (input > 0.0D) ? input : (input * 0.25D);
/*     */       case 5:
/*     */       
/*     */       case 6:
/*     */         break;
/*     */     } 
/* 948 */     double c = Mth.clamp(input, -1.0D, 1.0D);
/*     */   }
/*     */   
/*     */   public double transform(double input) {
/* 956 */     return transform(this.type, input);
/*     */   }
/*     */   
/*     */   public Mapped mapChildren(DensityFunction.Visitor visitor) {
/* 961 */     return create(this.type, visitor.apply(this.input));
/*     */   }
/*     */   
/*     */   public KeyDispatchDataCodec<? extends DensityFunction> codec() {
/* 966 */     return (KeyDispatchDataCodec)this.type.codec;
/*     */   }
/*     */ }


/* Location:              C:\Users\karol\AppData\Roaming\ModrinthApp\meta\versions\26.2-0.19.5\26.2-0.19.5.jar!\net\minecraft\world\level\levelgen\DensityFunctions$Mapped.class
 * Java compiler version: 25 (69.0)
 * JD-Core Version:       1.1.3
 */