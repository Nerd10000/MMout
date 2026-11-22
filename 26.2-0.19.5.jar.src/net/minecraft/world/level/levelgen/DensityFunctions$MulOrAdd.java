/*      */ package net.minecraft.world.level.levelgen;
/*      */ 
/*      */ final class MulOrAdd extends Record implements DensityFunctions.TwoArgumentSimpleFunction, DensityFunctions.PureTransformer {
/*      */   private final Type specificType;
/*      */   
/*      */   private final DensityFunction input;
/*      */   
/*      */   private final double minValue;
/*      */   
/*      */   private final double maxValue;
/*      */   
/*      */   private final double argument;
/*      */   
/*      */   private MulOrAdd(Type specificType, DensityFunction input, double minValue, double maxValue, double argument) {
/* 1073 */     this.specificType = specificType;
/* 1073 */     this.input = input;
/* 1073 */     this.minValue = minValue;
/* 1073 */     this.maxValue = maxValue;
/* 1073 */     this.argument = argument;
/*      */   }
/*      */   
/*      */   public final String toString() {
/*      */     // Byte code:
/*      */     //   0: aload_0
/*      */     //   1: <illegal opcode> toString : (Lnet/minecraft/world/level/levelgen/DensityFunctions$MulOrAdd;)Ljava/lang/String;
/*      */     //   6: areturn
/*      */     // Line number table:
/*      */     //   Java source line number -> byte code offset
/*      */     //   #1073	-> 0
/*      */     // Local variable table:
/*      */     //   start	length	slot	name	descriptor
/*      */     //   0	7	0	this	Lnet/minecraft/world/level/levelgen/DensityFunctions$MulOrAdd;
/*      */   }
/*      */   
/*      */   public final int hashCode() {
/*      */     // Byte code:
/*      */     //   0: aload_0
/*      */     //   1: <illegal opcode> hashCode : (Lnet/minecraft/world/level/levelgen/DensityFunctions$MulOrAdd;)I
/*      */     //   6: ireturn
/*      */     // Line number table:
/*      */     //   Java source line number -> byte code offset
/*      */     //   #1073	-> 0
/*      */     // Local variable table:
/*      */     //   start	length	slot	name	descriptor
/*      */     //   0	7	0	this	Lnet/minecraft/world/level/levelgen/DensityFunctions$MulOrAdd;
/*      */   }
/*      */   
/*      */   public final boolean equals(Object o) {
/*      */     // Byte code:
/*      */     //   0: aload_0
/*      */     //   1: aload_1
/*      */     //   2: <illegal opcode> equals : (Lnet/minecraft/world/level/levelgen/DensityFunctions$MulOrAdd;Ljava/lang/Object;)Z
/*      */     //   7: ireturn
/*      */     // Line number table:
/*      */     //   Java source line number -> byte code offset
/*      */     //   #1073	-> 0
/*      */     // Local variable table:
/*      */     //   start	length	slot	name	descriptor
/*      */     //   0	8	0	this	Lnet/minecraft/world/level/levelgen/DensityFunctions$MulOrAdd;
/*      */     //   0	8	1	o	Ljava/lang/Object;
/*      */   }
/*      */   
/*      */   public Type specificType() {
/* 1073 */     return this.specificType;
/*      */   }
/*      */   
/*      */   public DensityFunction input() {
/* 1073 */     return this.input;
/*      */   }
/*      */   
/*      */   public double minValue() {
/* 1073 */     return this.minValue;
/*      */   }
/*      */   
/*      */   public double maxValue() {
/* 1073 */     return this.maxValue;
/*      */   }
/*      */   
/*      */   public double argument() {
/* 1073 */     return this.argument;
/*      */   }
/*      */   
/*      */   public enum Type {
/* 1075 */     MUL, ADD;
/*      */   }
/*      */   
/*      */   public DensityFunctions.TwoArgumentSimpleFunction.Type type() {
/* 1081 */     return (this.specificType == Type.MUL) ? DensityFunctions.TwoArgumentSimpleFunction.Type.MUL : DensityFunctions.TwoArgumentSimpleFunction.Type.ADD;
/*      */   }
/*      */   
/*      */   public DensityFunction argument1() {
/* 1086 */     return DensityFunctions.constant(this.argument);
/*      */   }
/*      */   
/*      */   public DensityFunction argument2() {
/* 1091 */     return this.input;
/*      */   }
/*      */   
/*      */   public double transform(double input) {
/* 1096 */     switch (this.specificType.ordinal()) {
/*      */       default:
/* 1096 */         throw new MatchException(null, null);
/*      */       case 0:
/*      */       
/*      */       case 1:
/*      */         break;
/*      */     } 
/* 1096 */     return 
/*      */       
/* 1098 */       input + this.argument;
/*      */   }
/*      */   
/*      */   public DensityFunction mapChildren(DensityFunction.Visitor visitor) {
/*      */     double minValue, maxValue;
/* 1104 */     DensityFunction function = visitor.apply(this.input);
/* 1105 */     double min = function.minValue();
/* 1106 */     double max = function.maxValue();
/* 1109 */     if (this.specificType == Type.ADD) {
/* 1110 */       minValue = min + this.argument;
/* 1111 */       maxValue = max + this.argument;
/* 1112 */     } else if (this.argument >= 0.0D) {
/* 1113 */       minValue = min * this.argument;
/* 1114 */       maxValue = max * this.argument;
/*      */     } else {
/* 1116 */       minValue = max * this.argument;
/* 1117 */       maxValue = min * this.argument;
/*      */     } 
/* 1119 */     return new MulOrAdd(this.specificType, function, minValue, maxValue, this.argument);
/*      */   }
/*      */ }


/* Location:              C:\Users\karol\AppData\Roaming\ModrinthApp\meta\versions\26.2-0.19.5\26.2-0.19.5.jar!\net\minecraft\world\level\levelgen\DensityFunctions$MulOrAdd.class
 * Java compiler version: 25 (69.0)
 * JD-Core Version:       1.1.3
 */