/*     */ package net.minecraft.client.gui.font.providers;
/*     */ 
/*     */ import com.mojang.datafixers.kinds.App;
/*     */ import com.mojang.datafixers.kinds.Applicative;
/*     */ import com.mojang.serialization.Codec;
/*     */ import com.mojang.serialization.MapCodec;
/*     */ import com.mojang.serialization.codecs.RecordCodecBuilder;
/*     */ import java.util.function.BiFunction;
/*     */ 
/*     */ public final class Dimensions extends Record {
/*     */   private final int left;
/*     */   
/*     */   private final int right;
/*     */   
/*     */   public static final MapCodec<Dimensions> MAP_CODEC;
/*     */   
/*     */   public Dimensions(int left, int right) {
/*  86 */     this.left = left;
/*  86 */     this.right = right;
/*     */   }
/*     */   
/*     */   public final String toString() {
/*     */     // Byte code:
/*     */     //   0: aload_0
/*     */     //   1: <illegal opcode> toString : (Lnet/minecraft/client/gui/font/providers/UnihexProvider$Dimensions;)Ljava/lang/String;
/*     */     //   6: areturn
/*     */     // Line number table:
/*     */     //   Java source line number -> byte code offset
/*     */     //   #86	-> 0
/*     */     // Local variable table:
/*     */     //   start	length	slot	name	descriptor
/*     */     //   0	7	0	this	Lnet/minecraft/client/gui/font/providers/UnihexProvider$Dimensions;
/*     */   }
/*     */   
/*     */   public final int hashCode() {
/*     */     // Byte code:
/*     */     //   0: aload_0
/*     */     //   1: <illegal opcode> hashCode : (Lnet/minecraft/client/gui/font/providers/UnihexProvider$Dimensions;)I
/*     */     //   6: ireturn
/*     */     // Line number table:
/*     */     //   Java source line number -> byte code offset
/*     */     //   #86	-> 0
/*     */     // Local variable table:
/*     */     //   start	length	slot	name	descriptor
/*     */     //   0	7	0	this	Lnet/minecraft/client/gui/font/providers/UnihexProvider$Dimensions;
/*     */   }
/*     */   
/*     */   public final boolean equals(Object o) {
/*     */     // Byte code:
/*     */     //   0: aload_0
/*     */     //   1: aload_1
/*     */     //   2: <illegal opcode> equals : (Lnet/minecraft/client/gui/font/providers/UnihexProvider$Dimensions;Ljava/lang/Object;)Z
/*     */     //   7: ireturn
/*     */     // Line number table:
/*     */     //   Java source line number -> byte code offset
/*     */     //   #86	-> 0
/*     */     // Local variable table:
/*     */     //   start	length	slot	name	descriptor
/*     */     //   0	8	0	this	Lnet/minecraft/client/gui/font/providers/UnihexProvider$Dimensions;
/*     */     //   0	8	1	o	Ljava/lang/Object;
/*     */   }
/*     */   
/*     */   public int left() {
/*  86 */     return this.left;
/*     */   }
/*     */   
/*     */   public int right() {
/*  86 */     return this.right;
/*     */   }
/*     */   
/*     */   static {
/*  87 */     MAP_CODEC = RecordCodecBuilder.mapCodec(i -> i.group((App)Codec.INT.fieldOf("left").forGetter(Dimensions::left), (App)Codec.INT.fieldOf("right").forGetter(Dimensions::right)).apply((Applicative)i, Dimensions::new));
/*     */   }
/*     */   
/*  92 */   public static final Codec<Dimensions> CODEC = MAP_CODEC.codec();
/*     */   
/*     */   public int pack() {
/*  95 */     return pack(this.left, this.right);
/*     */   }
/*     */   
/*     */   public static int pack(int left, int right) {
/*  99 */     return (left & 0xFF) << 8 | right & 0xFF;
/*     */   }
/*     */   
/*     */   public static int left(int packed) {
/* 104 */     return (byte)(packed >> 8);
/*     */   }
/*     */   
/*     */   public static int right(int packed) {
/* 109 */     return (byte)packed;
/*     */   }
/*     */ }


/* Location:              C:\Users\karol\AppData\Roaming\ModrinthApp\meta\versions\26.2-0.19.5\26.2-0.19.5.jar!\net\minecraft\client\gui\font\providers\UnihexProvider$Dimensions.class
 * Java compiler version: 25 (69.0)
 * JD-Core Version:       1.1.3
 */