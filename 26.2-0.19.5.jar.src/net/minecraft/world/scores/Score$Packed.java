/*    */ package net.minecraft.world.scores;
/*    */ 
/*    */ import com.mojang.datafixers.kinds.App;
/*    */ import com.mojang.datafixers.kinds.Applicative;
/*    */ import com.mojang.datafixers.util.Function4;
/*    */ import com.mojang.serialization.Codec;
/*    */ import com.mojang.serialization.MapCodec;
/*    */ import com.mojang.serialization.codecs.RecordCodecBuilder;
/*    */ import java.util.Optional;
/*    */ import net.minecraft.network.chat.Component;
/*    */ import net.minecraft.network.chat.ComponentSerialization;
/*    */ import net.minecraft.network.chat.numbers.NumberFormat;
/*    */ import net.minecraft.network.chat.numbers.NumberFormatTypes;
/*    */ 
/*    */ public final class Packed extends Record {
/*    */   private final int value;
/*    */   
/*    */   private final boolean locked;
/*    */   
/*    */   private final Optional<Component> display;
/*    */   
/*    */   private final Optional<NumberFormat> numberFormat;
/*    */   
/*    */   public static final MapCodec<Packed> MAP_CODEC;
/*    */   
/*    */   public Packed(int value, boolean locked, Optional<Component> display, Optional<NumberFormat> numberFormat) {
/* 74 */     this.value = value;
/* 74 */     this.locked = locked;
/* 74 */     this.display = display;
/* 74 */     this.numberFormat = numberFormat;
/*    */   }
/*    */   
/*    */   public final String toString() {
/*    */     // Byte code:
/*    */     //   0: aload_0
/*    */     //   1: <illegal opcode> toString : (Lnet/minecraft/world/scores/Score$Packed;)Ljava/lang/String;
/*    */     //   6: areturn
/*    */     // Line number table:
/*    */     //   Java source line number -> byte code offset
/*    */     //   #74	-> 0
/*    */     // Local variable table:
/*    */     //   start	length	slot	name	descriptor
/*    */     //   0	7	0	this	Lnet/minecraft/world/scores/Score$Packed;
/*    */   }
/*    */   
/*    */   public final int hashCode() {
/*    */     // Byte code:
/*    */     //   0: aload_0
/*    */     //   1: <illegal opcode> hashCode : (Lnet/minecraft/world/scores/Score$Packed;)I
/*    */     //   6: ireturn
/*    */     // Line number table:
/*    */     //   Java source line number -> byte code offset
/*    */     //   #74	-> 0
/*    */     // Local variable table:
/*    */     //   start	length	slot	name	descriptor
/*    */     //   0	7	0	this	Lnet/minecraft/world/scores/Score$Packed;
/*    */   }
/*    */   
/*    */   public final boolean equals(Object o) {
/*    */     // Byte code:
/*    */     //   0: aload_0
/*    */     //   1: aload_1
/*    */     //   2: <illegal opcode> equals : (Lnet/minecraft/world/scores/Score$Packed;Ljava/lang/Object;)Z
/*    */     //   7: ireturn
/*    */     // Line number table:
/*    */     //   Java source line number -> byte code offset
/*    */     //   #74	-> 0
/*    */     // Local variable table:
/*    */     //   start	length	slot	name	descriptor
/*    */     //   0	8	0	this	Lnet/minecraft/world/scores/Score$Packed;
/*    */     //   0	8	1	o	Ljava/lang/Object;
/*    */   }
/*    */   
/*    */   public int value() {
/* 74 */     return this.value;
/*    */   }
/*    */   
/*    */   public boolean locked() {
/* 74 */     return this.locked;
/*    */   }
/*    */   
/*    */   public Optional<Component> display() {
/* 74 */     return this.display;
/*    */   }
/*    */   
/*    */   public Optional<NumberFormat> numberFormat() {
/* 74 */     return this.numberFormat;
/*    */   }
/*    */   
/*    */   static {
/* 80 */     MAP_CODEC = RecordCodecBuilder.mapCodec(i -> i.group((App)Codec.INT.optionalFieldOf("Score", Integer.valueOf(0)).forGetter(Packed::value), (App)Codec.BOOL.optionalFieldOf("Locked", Boolean.valueOf(false)).forGetter(Packed::locked), (App)ComponentSerialization.CODEC.optionalFieldOf("display").forGetter(Packed::display), (App)NumberFormatTypes.CODEC.optionalFieldOf("format").forGetter(Packed::numberFormat)).apply((Applicative)i, Packed::new));
/*    */   }
/*    */ }


/* Location:              C:\Users\karol\AppData\Roaming\ModrinthApp\meta\versions\26.2-0.19.5\26.2-0.19.5.jar!\net\minecraft\world\scores\Score$Packed.class
 * Java compiler version: 25 (69.0)
 * JD-Core Version:       1.1.3
 */