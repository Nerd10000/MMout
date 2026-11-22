/*    */ package net.minecraft.util;
/*    */ 
/*    */ import com.mojang.datafixers.kinds.App;
/*    */ import com.mojang.datafixers.kinds.Applicative;
/*    */ import com.mojang.serialization.Codec;
/*    */ import com.mojang.serialization.codecs.RecordCodecBuilder;
/*    */ import java.util.function.BiFunction;
/*    */ 
/*    */ public final class Brightness extends Record {
/*    */   private final int block;
/*    */   
/*    */   private final int sky;
/*    */   
/*    */   public Brightness(int block, int sky) {
/*  6 */     this.block = block;
/*  6 */     this.sky = sky;
/*    */   }
/*    */   
/*    */   public final String toString() {
/*    */     // Byte code:
/*    */     //   0: aload_0
/*    */     //   1: <illegal opcode> toString : (Lnet/minecraft/util/Brightness;)Ljava/lang/String;
/*    */     //   6: areturn
/*    */     // Line number table:
/*    */     //   Java source line number -> byte code offset
/*    */     //   #6	-> 0
/*    */     // Local variable table:
/*    */     //   start	length	slot	name	descriptor
/*    */     //   0	7	0	this	Lnet/minecraft/util/Brightness;
/*    */   }
/*    */   
/*    */   public final int hashCode() {
/*    */     // Byte code:
/*    */     //   0: aload_0
/*    */     //   1: <illegal opcode> hashCode : (Lnet/minecraft/util/Brightness;)I
/*    */     //   6: ireturn
/*    */     // Line number table:
/*    */     //   Java source line number -> byte code offset
/*    */     //   #6	-> 0
/*    */     // Local variable table:
/*    */     //   start	length	slot	name	descriptor
/*    */     //   0	7	0	this	Lnet/minecraft/util/Brightness;
/*    */   }
/*    */   
/*    */   public final boolean equals(Object o) {
/*    */     // Byte code:
/*    */     //   0: aload_0
/*    */     //   1: aload_1
/*    */     //   2: <illegal opcode> equals : (Lnet/minecraft/util/Brightness;Ljava/lang/Object;)Z
/*    */     //   7: ireturn
/*    */     // Line number table:
/*    */     //   Java source line number -> byte code offset
/*    */     //   #6	-> 0
/*    */     // Local variable table:
/*    */     //   start	length	slot	name	descriptor
/*    */     //   0	8	0	this	Lnet/minecraft/util/Brightness;
/*    */     //   0	8	1	o	Ljava/lang/Object;
/*    */   }
/*    */   
/*    */   public int block() {
/*  6 */     return this.block;
/*    */   }
/*    */   
/*    */   public int sky() {
/*  6 */     return this.sky;
/*    */   }
/*    */   
/*  7 */   public static final Codec<Integer> LIGHT_VALUE_CODEC = ExtraCodecs.intRange(0, 15);
/*    */   
/*    */   public static final Codec<Brightness> CODEC;
/*    */   
/*    */   static {
/*  9 */     CODEC = RecordCodecBuilder.create(i -> i.group((App)LIGHT_VALUE_CODEC.fieldOf("block").forGetter(Brightness::block), (App)LIGHT_VALUE_CODEC.fieldOf("sky").forGetter(Brightness::sky)).apply((Applicative)i, Brightness::new));
/*    */   }
/*    */   
/* 14 */   public static final Brightness FULL_BRIGHT = new Brightness(15, 15);
/*    */   
/*    */   public int pack() {
/* 17 */     return LightCoordsUtil.pack(this.block, this.sky);
/*    */   }
/*    */   
/*    */   public static Brightness unpack(int packed) {
/* 21 */     return new Brightness(
/* 22 */         LightCoordsUtil.block(packed), 
/* 23 */         LightCoordsUtil.sky(packed));
/*    */   }
/*    */ }


/* Location:              C:\Users\karol\AppData\Roaming\ModrinthApp\meta\versions\26.2-0.19.5\26.2-0.19.5.jar!\net\minecraf\\util\Brightness.class
 * Java compiler version: 25 (69.0)
 * JD-Core Version:       1.1.3
 */