/*     */ package net.minecraft.world.level.levelgen.synth;
/*     */ 
/*     */ import com.mojang.datafixers.kinds.App;
/*     */ import com.mojang.datafixers.kinds.Applicative;
/*     */ import com.mojang.serialization.Codec;
/*     */ import com.mojang.serialization.codecs.RecordCodecBuilder;
/*     */ import it.unimi.dsi.fastutil.doubles.DoubleArrayList;
/*     */ import it.unimi.dsi.fastutil.doubles.DoubleList;
/*     */ import java.util.List;
/*     */ import java.util.function.BiFunction;
/*     */ import net.minecraft.core.Holder;
/*     */ import net.minecraft.core.registries.Registries;
/*     */ import net.minecraft.resources.RegistryFileCodec;
/*     */ import net.minecraft.util.Util;
/*     */ 
/*     */ public final class NoiseParameters extends Record {
/*     */   private final int firstOctave;
/*     */   
/*     */   private final DoubleList amplitudes;
/*     */   
/*     */   public static final Codec<NoiseParameters> DIRECT_CODEC;
/*     */   
/*     */   public NoiseParameters(int firstOctave, DoubleList amplitudes) {
/* 109 */     this.firstOctave = firstOctave;
/* 109 */     this.amplitudes = amplitudes;
/*     */   }
/*     */   
/*     */   public final String toString() {
/*     */     // Byte code:
/*     */     //   0: aload_0
/*     */     //   1: <illegal opcode> toString : (Lnet/minecraft/world/level/levelgen/synth/NormalNoise$NoiseParameters;)Ljava/lang/String;
/*     */     //   6: areturn
/*     */     // Line number table:
/*     */     //   Java source line number -> byte code offset
/*     */     //   #109	-> 0
/*     */     // Local variable table:
/*     */     //   start	length	slot	name	descriptor
/*     */     //   0	7	0	this	Lnet/minecraft/world/level/levelgen/synth/NormalNoise$NoiseParameters;
/*     */   }
/*     */   
/*     */   public final int hashCode() {
/*     */     // Byte code:
/*     */     //   0: aload_0
/*     */     //   1: <illegal opcode> hashCode : (Lnet/minecraft/world/level/levelgen/synth/NormalNoise$NoiseParameters;)I
/*     */     //   6: ireturn
/*     */     // Line number table:
/*     */     //   Java source line number -> byte code offset
/*     */     //   #109	-> 0
/*     */     // Local variable table:
/*     */     //   start	length	slot	name	descriptor
/*     */     //   0	7	0	this	Lnet/minecraft/world/level/levelgen/synth/NormalNoise$NoiseParameters;
/*     */   }
/*     */   
/*     */   public final boolean equals(Object o) {
/*     */     // Byte code:
/*     */     //   0: aload_0
/*     */     //   1: aload_1
/*     */     //   2: <illegal opcode> equals : (Lnet/minecraft/world/level/levelgen/synth/NormalNoise$NoiseParameters;Ljava/lang/Object;)Z
/*     */     //   7: ireturn
/*     */     // Line number table:
/*     */     //   Java source line number -> byte code offset
/*     */     //   #109	-> 0
/*     */     // Local variable table:
/*     */     //   start	length	slot	name	descriptor
/*     */     //   0	8	0	this	Lnet/minecraft/world/level/levelgen/synth/NormalNoise$NoiseParameters;
/*     */     //   0	8	1	o	Ljava/lang/Object;
/*     */   }
/*     */   
/*     */   public int firstOctave() {
/* 109 */     return this.firstOctave;
/*     */   }
/*     */   
/*     */   public DoubleList amplitudes() {
/* 109 */     return this.amplitudes;
/*     */   }
/*     */   
/*     */   static {
/* 110 */     DIRECT_CODEC = RecordCodecBuilder.create(i -> i.group((App)Codec.INT.fieldOf("firstOctave").forGetter(NoiseParameters::firstOctave), (App)Codec.DOUBLE.listOf().fieldOf("amplitudes").forGetter(NoiseParameters::amplitudes)).apply((Applicative)i, NoiseParameters::new));
/*     */   }
/*     */   
/* 115 */   public static final Codec<Holder<NoiseParameters>> CODEC = (Codec<Holder<NoiseParameters>>)RegistryFileCodec.create(Registries.NOISE, DIRECT_CODEC);
/*     */   
/*     */   public NoiseParameters(int firstOctave, List<Double> amplitudes) {
/* 118 */     this(firstOctave, (DoubleList)new DoubleArrayList(amplitudes));
/*     */   }
/*     */   
/*     */   public NoiseParameters(int firstOctave, double firstAmplitude, double... amplitudes) {
/* 122 */     this(firstOctave, (DoubleList)Util.make(new DoubleArrayList(amplitudes), list -> list.add(0, firstAmplitude)));
/*     */   }
/*     */ }


/* Location:              C:\Users\karol\AppData\Roaming\ModrinthApp\meta\versions\26.2-0.19.5\26.2-0.19.5.jar!\net\minecraft\world\level\levelgen\synth\NormalNoise$NoiseParameters.class
 * Java compiler version: 25 (69.0)
 * JD-Core Version:       1.1.3
 */