/*      */ package net.minecraft.world.level.levelgen;
/*      */ 
/*      */ final class Ap2 extends Record implements DensityFunctions.TwoArgumentSimpleFunction {
/*      */   private final DensityFunctions.TwoArgumentSimpleFunction.Type type;
/*      */   
/*      */   private final DensityFunction argument1;
/*      */   
/*      */   private final DensityFunction argument2;
/*      */   
/*      */   private final double minValue;
/*      */   
/*      */   private final double maxValue;
/*      */   
/*      */   private Ap2(DensityFunctions.TwoArgumentSimpleFunction.Type type, DensityFunction argument1, DensityFunction argument2, double minValue, double maxValue) {
/* 1123 */     this.type = type;
/* 1123 */     this.argument1 = argument1;
/* 1123 */     this.argument2 = argument2;
/* 1123 */     this.minValue = minValue;
/* 1123 */     this.maxValue = maxValue;
/*      */   }
/*      */   
/*      */   public final String toString() {
/*      */     // Byte code:
/*      */     //   0: aload_0
/*      */     //   1: <illegal opcode> toString : (Lnet/minecraft/world/level/levelgen/DensityFunctions$Ap2;)Ljava/lang/String;
/*      */     //   6: areturn
/*      */     // Line number table:
/*      */     //   Java source line number -> byte code offset
/*      */     //   #1123	-> 0
/*      */     // Local variable table:
/*      */     //   start	length	slot	name	descriptor
/*      */     //   0	7	0	this	Lnet/minecraft/world/level/levelgen/DensityFunctions$Ap2;
/*      */   }
/*      */   
/*      */   public final int hashCode() {
/*      */     // Byte code:
/*      */     //   0: aload_0
/*      */     //   1: <illegal opcode> hashCode : (Lnet/minecraft/world/level/levelgen/DensityFunctions$Ap2;)I
/*      */     //   6: ireturn
/*      */     // Line number table:
/*      */     //   Java source line number -> byte code offset
/*      */     //   #1123	-> 0
/*      */     // Local variable table:
/*      */     //   start	length	slot	name	descriptor
/*      */     //   0	7	0	this	Lnet/minecraft/world/level/levelgen/DensityFunctions$Ap2;
/*      */   }
/*      */   
/*      */   public final boolean equals(Object o) {
/*      */     // Byte code:
/*      */     //   0: aload_0
/*      */     //   1: aload_1
/*      */     //   2: <illegal opcode> equals : (Lnet/minecraft/world/level/levelgen/DensityFunctions$Ap2;Ljava/lang/Object;)Z
/*      */     //   7: ireturn
/*      */     // Line number table:
/*      */     //   Java source line number -> byte code offset
/*      */     //   #1123	-> 0
/*      */     // Local variable table:
/*      */     //   start	length	slot	name	descriptor
/*      */     //   0	8	0	this	Lnet/minecraft/world/level/levelgen/DensityFunctions$Ap2;
/*      */     //   0	8	1	o	Ljava/lang/Object;
/*      */   }
/*      */   
/*      */   public DensityFunctions.TwoArgumentSimpleFunction.Type type() {
/* 1123 */     return this.type;
/*      */   }
/*      */   
/*      */   public DensityFunction argument1() {
/* 1123 */     return this.argument1;
/*      */   }
/*      */   
/*      */   public DensityFunction argument2() {
/* 1123 */     return this.argument2;
/*      */   }
/*      */   
/*      */   public double compute(DensityFunction.FunctionContext context) {
/* 1126 */     double v1 = this.argument1.compute(context);
/* 1127 */     switch (this.type.ordinal()) {
/*      */       default:
/* 1127 */         throw new MatchException(null, null);
/*      */       case 0:
/*      */       
/*      */       case 1:
/* 1127 */         return 
/*      */           
/* 1130 */           (v1 == 0.0D) ? 
/* 1131 */           0.0D : (
/*      */           
/* 1133 */           v1 * this.argument2.compute(context));
/*      */       case 2:
/* 1136 */         return (v1 < this.argument2.minValue()) ? 
/* 1137 */           v1 : 
/*      */           
/* 1139 */           Math.min(v1, this.argument2.compute(context));
/*      */       case 3:
/*      */         break;
/*      */     } 
/* 1142 */     return (v1 > this.argument2.maxValue()) ? 
/* 1143 */       v1 : 
/*      */       
/* 1145 */       Math.max(v1, this.argument2.compute(context));
/*      */   }
/*      */   
/*      */   public void fillArray(double[] output, DensityFunction.ContextProvider contextProvider) {
/*      */     double[] v2;
/*      */     int i;
/*      */     double min, max;
/*      */     int j, k;
/* 1152 */     this.argument1.fillArray(output, contextProvider);
/* 1153 */     switch (this.type.ordinal()) {
/*      */       case 0:
/* 1155 */         v2 = new double[output.length];
/* 1156 */         this.argument2.fillArray(v2, contextProvider);
/* 1157 */         for (j = 0; j < output.length; j++)
/* 1158 */           output[j] = output[j] + v2[j]; 
/*      */         break;
/*      */       case 1:
/* 1162 */         for (i = 0; i < output.length; i++) {
/* 1163 */           double v = output[i];
/* 1164 */           output[i] = (v == 0.0D) ? 0.0D : (v * this.argument2.compute(contextProvider.forIndex(i)));
/*      */         } 
/*      */         break;
/*      */       case 2:
/* 1168 */         min = this.argument2.minValue();
/* 1169 */         for (k = 0; k < output.length; k++) {
/* 1170 */           double v = output[k];
/* 1171 */           output[k] = (v < min) ? v : Math.min(v, this.argument2.compute(contextProvider.forIndex(k)));
/*      */         } 
/*      */         break;
/*      */       case 3:
/* 1175 */         max = this.argument2.maxValue();
/* 1176 */         for (k = 0; k < output.length; k++) {
/* 1177 */           double v = output[k];
/* 1178 */           output[k] = (v > max) ? v : Math.max(v, this.argument2.compute(contextProvider.forIndex(k)));
/*      */         } 
/*      */         break;
/*      */     } 
/*      */   }
/*      */   
/*      */   public DensityFunction mapChildren(DensityFunction.Visitor visitor) {
/* 1186 */     return DensityFunctions.TwoArgumentSimpleFunction.create(this.type, visitor.apply(this.argument1), visitor.apply(this.argument2));
/*      */   }
/*      */   
/*      */   public double minValue() {
/* 1191 */     return this.minValue;
/*      */   }
/*      */   
/*      */   public double maxValue() {
/* 1196 */     return this.maxValue;
/*      */   }
/*      */ }


/* Location:              C:\Users\karol\AppData\Roaming\ModrinthApp\meta\versions\26.2-0.19.5\26.2-0.19.5.jar!\net\minecraft\world\level\levelgen\DensityFunctions$Ap2.class
 * Java compiler version: 25 (69.0)
 * JD-Core Version:       1.1.3
 */