/*     */ package net.minecraft.world.level.levelgen;
/*     */ 
/*     */ import com.mojang.serialization.Codec;
/*     */ import net.minecraft.core.Holder;
/*     */ import net.minecraft.core.registries.Registries;
/*     */ import net.minecraft.resources.RegistryFileCodec;
/*     */ import net.minecraft.util.KeyDispatchDataCodec;
/*     */ import net.minecraft.world.level.levelgen.synth.NormalNoise;
/*     */ 
/*     */ public interface DensityFunction {
/*  12 */   public static final Codec<DensityFunction> CODEC = RegistryFileCodec.create(Registries.DENSITY_FUNCTION, DensityFunctions.DIRECT_CODEC).xmap(holder -> {
/*     */         // Byte code:
/*     */         //   0: aload_0
/*     */         //   1: dup
/*     */         //   2: invokestatic requireNonNull : (Ljava/lang/Object;)Ljava/lang/Object;
/*     */         //   5: pop
/*     */         //   6: astore_1
/*     */         //   7: iconst_0
/*     */         //   8: istore_2
/*     */         //   9: aload_1
/*     */         //   10: iload_2
/*     */         //   11: <illegal opcode> typeSwitch : (Lnet/minecraft/core/Holder;I)I
/*     */         //   16: lookupswitch default -> 44, 0 -> 54, 1 -> 69
/*     */         //   44: new java/lang/MatchException
/*     */         //   47: dup
/*     */         //   48: aconst_null
/*     */         //   49: aconst_null
/*     */         //   50: invokespecial <init> : (Ljava/lang/String;Ljava/lang/Throwable;)V
/*     */         //   53: athrow
/*     */         //   54: aload_1
/*     */         //   55: checkcast net/minecraft/core/Holder$Direct
/*     */         //   58: astore_3
/*     */         //   59: aload_3
/*     */         //   60: invokevirtual value : ()Ljava/lang/Object;
/*     */         //   63: checkcast net/minecraft/world/level/levelgen/DensityFunction
/*     */         //   66: goto -> 84
/*     */         //   69: aload_1
/*     */         //   70: checkcast net/minecraft/core/Holder$Reference
/*     */         //   73: astore #4
/*     */         //   75: new net/minecraft/world/level/levelgen/DensityFunctions$HolderHolder
/*     */         //   78: dup
/*     */         //   79: aload #4
/*     */         //   81: invokespecial <init> : (Lnet/minecraft/core/Holder;)V
/*     */         //   84: areturn
/*     */         // Line number table:
/*     */         //   Java source line number -> byte code offset
/*     */         //   #13	-> 0
/*     */         //   #14	-> 54
/*     */         //   #15	-> 69
/*     */         // Local variable table:
/*     */         //   start	length	slot	name	descriptor
/*     */         //   59	10	3	direct	Lnet/minecraft/core/Holder$Direct;
/*     */         //   75	9	4	reference	Lnet/minecraft/core/Holder$Reference;
/*     */         //   7	77	1	selector0$temp	Lnet/minecraft/core/Holder;
/*     */         //   9	75	2	index$1	I
/*     */         //   0	85	0	holder	Lnet/minecraft/core/Holder;
/*     */         // Local variable type table:
/*     */         //   start	length	slot	name	signature
/*     */         //   59	10	3	direct	Lnet/minecraft/core/Holder$Direct<Lnet/minecraft/world/level/levelgen/DensityFunction;>;
/*     */         //   75	9	4	reference	Lnet/minecraft/core/Holder$Reference<Lnet/minecraft/world/level/levelgen/DensityFunction;>;
/*     */       }value -> {
/*     */         // Byte code:
/*     */         //   0: aload_0
/*     */         //   1: dup
/*     */         //   2: invokestatic requireNonNull : (Ljava/lang/Object;)Ljava/lang/Object;
/*     */         //   5: pop
/*     */         //   6: astore_1
/*     */         //   7: iconst_0
/*     */         //   8: istore_2
/*     */         //   9: aload_1
/*     */         //   10: iload_2
/*     */         //   11: <illegal opcode> typeSwitch : (Lnet/minecraft/world/level/levelgen/DensityFunction;I)I
/*     */         //   16: lookupswitch default -> 56, 0 -> 36
/*     */         //   36: aload_1
/*     */         //   37: checkcast net/minecraft/world/level/levelgen/DensityFunctions$HolderHolder
/*     */         //   40: astore_3
/*     */         //   41: aload_3
/*     */         //   42: invokevirtual function : ()Lnet/minecraft/core/Holder;
/*     */         //   45: astore #5
/*     */         //   47: aload #5
/*     */         //   49: astore #4
/*     */         //   51: aload #4
/*     */         //   53: goto -> 60
/*     */         //   56: aload_0
/*     */         //   57: invokestatic direct : (Ljava/lang/Object;)Lnet/minecraft/core/Holder;
/*     */         //   60: areturn
/*     */         //   61: astore_1
/*     */         //   62: new java/lang/MatchException
/*     */         //   65: dup
/*     */         //   66: aload_1
/*     */         //   67: invokevirtual toString : ()Ljava/lang/String;
/*     */         //   70: aload_1
/*     */         //   71: invokespecial <init> : (Ljava/lang/String;Ljava/lang/Throwable;)V
/*     */         //   74: athrow
/*     */         // Line number table:
/*     */         //   Java source line number -> byte code offset
/*     */         //   #17	-> 0
/*     */         //   #18	-> 36
/*     */         //   #19	-> 56
/*     */         //   #18	-> 60
/*     */         // Local variable table:
/*     */         //   start	length	slot	name	descriptor
/*     */         //   47	4	5	patt3$temp	Lnet/minecraft/core/Holder;
/*     */         //   41	15	3	$b$0	Lnet/minecraft/world/level/levelgen/DensityFunctions$HolderHolder;
/*     */         //   51	5	4	function	Lnet/minecraft/core/Holder;
/*     */         //   7	53	1	selector1$temp	Lnet/minecraft/world/level/levelgen/DensityFunction;
/*     */         //   9	51	2	index$2	I
/*     */         //   0	75	0	value	Lnet/minecraft/world/level/levelgen/DensityFunction;
/*     */         // Local variable type table:
/*     */         //   start	length	slot	name	signature
/*     */         //   51	5	4	function	Lnet/minecraft/core/Holder<Lnet/minecraft/world/level/levelgen/DensityFunction;>;
/*     */         // Exception table:
/*     */         //   from	to	target	type
/*     */         //   42	45	61	java/lang/Throwable
/*     */       });
/*     */   
/*     */   default DensityFunction mapAll(final Visitor visitor) {
/*     */     class RecursiveVisitor implements Visitor {
/*     */       RecursiveVisitor(DensityFunction this$0) {}
/*     */       
/*     */       public DensityFunction apply(DensityFunction input) {
/*  36 */         return visitor.apply(input.mapChildren(this));
/*     */       }
/*     */       
/*     */       public DensityFunction.NoiseHolder visitNoise(DensityFunction.NoiseHolder noise) {
/*  41 */         return visitor.visitNoise(noise);
/*     */       }
/*     */     };
/*  44 */     return (new RecursiveVisitor(this)).apply(this);
/*     */   }
/*     */   
/*     */   public static final class NoiseHolder extends Record {
/*     */     private final Holder<NormalNoise.NoiseParameters> noiseData;
/*     */     
/*     */     private final NormalNoise noise;
/*     */     
/*     */     public static final Codec<NoiseHolder> CODEC;
/*     */     
/*     */     public NoiseHolder(Holder<NormalNoise.NoiseParameters> noiseData, NormalNoise noise) {
/*  59 */       this.noiseData = noiseData;
/*  59 */       this.noise = noise;
/*     */     }
/*     */     
/*     */     public final String toString() {
/*     */       // Byte code:
/*     */       //   0: aload_0
/*     */       //   1: <illegal opcode> toString : (Lnet/minecraft/world/level/levelgen/DensityFunction$NoiseHolder;)Ljava/lang/String;
/*     */       //   6: areturn
/*     */       // Line number table:
/*     */       //   Java source line number -> byte code offset
/*     */       //   #59	-> 0
/*     */       // Local variable table:
/*     */       //   start	length	slot	name	descriptor
/*     */       //   0	7	0	this	Lnet/minecraft/world/level/levelgen/DensityFunction$NoiseHolder;
/*     */     }
/*     */     
/*     */     public final int hashCode() {
/*     */       // Byte code:
/*     */       //   0: aload_0
/*     */       //   1: <illegal opcode> hashCode : (Lnet/minecraft/world/level/levelgen/DensityFunction$NoiseHolder;)I
/*     */       //   6: ireturn
/*     */       // Line number table:
/*     */       //   Java source line number -> byte code offset
/*     */       //   #59	-> 0
/*     */       // Local variable table:
/*     */       //   start	length	slot	name	descriptor
/*     */       //   0	7	0	this	Lnet/minecraft/world/level/levelgen/DensityFunction$NoiseHolder;
/*     */     }
/*     */     
/*     */     public final boolean equals(Object o) {
/*     */       // Byte code:
/*     */       //   0: aload_0
/*     */       //   1: aload_1
/*     */       //   2: <illegal opcode> equals : (Lnet/minecraft/world/level/levelgen/DensityFunction$NoiseHolder;Ljava/lang/Object;)Z
/*     */       //   7: ireturn
/*     */       // Line number table:
/*     */       //   Java source line number -> byte code offset
/*     */       //   #59	-> 0
/*     */       // Local variable table:
/*     */       //   start	length	slot	name	descriptor
/*     */       //   0	8	0	this	Lnet/minecraft/world/level/levelgen/DensityFunction$NoiseHolder;
/*     */       //   0	8	1	o	Ljava/lang/Object;
/*     */     }
/*     */     
/*     */     public Holder<NormalNoise.NoiseParameters> noiseData() {
/*  59 */       return this.noiseData;
/*     */     }
/*     */     
/*     */     public NormalNoise noise() {
/*  59 */       return this.noise;
/*     */     }
/*     */     
/*     */     public NoiseHolder(Holder<NormalNoise.NoiseParameters> noiseData) {
/*  64 */       this(noiseData, null);
/*     */     }
/*     */     
/*     */     static {
/*  67 */       CODEC = NormalNoise.NoiseParameters.CODEC.xmap(data -> new NoiseHolder(data, null), NoiseHolder::noiseData);
/*     */     }
/*     */     
/*     */     public double getValue(double x, double y, double z) {
/*  70 */       return (this.noise == null) ? 0.0D : this.noise.getValue(x, y, z);
/*     */     }
/*     */     
/*     */     public double maxValue() {
/*  74 */       return (this.noise == null) ? 2.0D : this.noise.maxValue();
/*     */     }
/*     */   }
/*     */   
/*     */   public static interface Visitor {
/*     */     DensityFunction apply(DensityFunction param1DensityFunction);
/*     */     
/*     */     default DensityFunction.NoiseHolder visitNoise(DensityFunction.NoiseHolder noise) {
/*  82 */       return noise;
/*     */     }
/*     */   }
/*     */   
/*     */   public static interface SimpleFunction extends DensityFunction {
/*     */     default void fillArray(double[] output, DensityFunction.ContextProvider contextProvider) {
/*  89 */       contextProvider.fillAllDirectly(output, this);
/*     */     }
/*     */     
/*     */     default DensityFunction mapChildren(DensityFunction.Visitor visitor) {
/*  94 */       return this;
/*     */     }
/*     */   }
/*     */   
/*     */   public static final class SinglePointContext extends Record implements FunctionContext {
/*     */     private final int blockX;
/*     */     
/*     */     private final int blockY;
/*     */     
/*     */     private final int blockZ;
/*     */     
/*     */     public SinglePointContext(int blockX, int blockY, int blockZ) {
/* 106 */       this.blockX = blockX;
/* 106 */       this.blockY = blockY;
/* 106 */       this.blockZ = blockZ;
/*     */     }
/*     */     
/*     */     public final String toString() {
/*     */       // Byte code:
/*     */       //   0: aload_0
/*     */       //   1: <illegal opcode> toString : (Lnet/minecraft/world/level/levelgen/DensityFunction$SinglePointContext;)Ljava/lang/String;
/*     */       //   6: areturn
/*     */       // Line number table:
/*     */       //   Java source line number -> byte code offset
/*     */       //   #106	-> 0
/*     */       // Local variable table:
/*     */       //   start	length	slot	name	descriptor
/*     */       //   0	7	0	this	Lnet/minecraft/world/level/levelgen/DensityFunction$SinglePointContext;
/*     */     }
/*     */     
/*     */     public final int hashCode() {
/*     */       // Byte code:
/*     */       //   0: aload_0
/*     */       //   1: <illegal opcode> hashCode : (Lnet/minecraft/world/level/levelgen/DensityFunction$SinglePointContext;)I
/*     */       //   6: ireturn
/*     */       // Line number table:
/*     */       //   Java source line number -> byte code offset
/*     */       //   #106	-> 0
/*     */       // Local variable table:
/*     */       //   start	length	slot	name	descriptor
/*     */       //   0	7	0	this	Lnet/minecraft/world/level/levelgen/DensityFunction$SinglePointContext;
/*     */     }
/*     */     
/*     */     public final boolean equals(Object o) {
/*     */       // Byte code:
/*     */       //   0: aload_0
/*     */       //   1: aload_1
/*     */       //   2: <illegal opcode> equals : (Lnet/minecraft/world/level/levelgen/DensityFunction$SinglePointContext;Ljava/lang/Object;)Z
/*     */       //   7: ireturn
/*     */       // Line number table:
/*     */       //   Java source line number -> byte code offset
/*     */       //   #106	-> 0
/*     */       // Local variable table:
/*     */       //   start	length	slot	name	descriptor
/*     */       //   0	8	0	this	Lnet/minecraft/world/level/levelgen/DensityFunction$SinglePointContext;
/*     */       //   0	8	1	o	Ljava/lang/Object;
/*     */     }
/*     */     
/*     */     public int blockX() {
/* 106 */       return this.blockX;
/*     */     }
/*     */     
/*     */     public int blockY() {
/* 106 */       return this.blockY;
/*     */     }
/*     */     
/*     */     public int blockZ() {
/* 106 */       return this.blockZ;
/*     */     }
/*     */   }
/*     */   
/*     */   default DensityFunction clamp(double min, double max) {
/* 109 */     return new DensityFunctions.Clamp(this, min, max);
/*     */   }
/*     */   
/*     */   default DensityFunction abs() {
/* 113 */     return DensityFunctions.map(this, DensityFunctions.Mapped.Type.ABS);
/*     */   }
/*     */   
/*     */   default DensityFunction square() {
/* 117 */     return DensityFunctions.map(this, DensityFunctions.Mapped.Type.SQUARE);
/*     */   }
/*     */   
/*     */   default DensityFunction cube() {
/* 121 */     return DensityFunctions.map(this, DensityFunctions.Mapped.Type.CUBE);
/*     */   }
/*     */   
/*     */   default DensityFunction halfNegative() {
/* 125 */     return DensityFunctions.map(this, DensityFunctions.Mapped.Type.HALF_NEGATIVE);
/*     */   }
/*     */   
/*     */   default DensityFunction quarterNegative() {
/* 129 */     return DensityFunctions.map(this, DensityFunctions.Mapped.Type.QUARTER_NEGATIVE);
/*     */   }
/*     */   
/*     */   default DensityFunction invert() {
/* 133 */     return DensityFunctions.map(this, DensityFunctions.Mapped.Type.INVERT);
/*     */   }
/*     */   
/*     */   default DensityFunction squeeze() {
/* 140 */     return DensityFunctions.map(this, DensityFunctions.Mapped.Type.SQUEEZE);
/*     */   }
/*     */   
/*     */   double compute(FunctionContext paramFunctionContext);
/*     */   
/*     */   void fillArray(double[] paramArrayOfdouble, ContextProvider paramContextProvider);
/*     */   
/*     */   DensityFunction mapChildren(Visitor paramVisitor);
/*     */   
/*     */   double minValue();
/*     */   
/*     */   double maxValue();
/*     */   
/*     */   KeyDispatchDataCodec<? extends DensityFunction> codec();
/*     */   
/*     */   public static interface FunctionContext {
/*     */     int blockX();
/*     */     
/*     */     int blockY();
/*     */     
/*     */     int blockZ();
/*     */   }
/*     */   
/*     */   public static interface ContextProvider {
/*     */     DensityFunction.FunctionContext forIndex(int param1Int);
/*     */     
/*     */     void fillAllDirectly(double[] param1ArrayOfdouble, DensityFunction param1DensityFunction);
/*     */   }
/*     */ }


/* Location:              C:\Users\karol\AppData\Roaming\ModrinthApp\meta\versions\26.2-0.19.5\26.2-0.19.5.jar!\net\minecraft\world\level\levelgen\DensityFunction.class
 * Java compiler version: 25 (69.0)
 * JD-Core Version:       1.1.3
 */