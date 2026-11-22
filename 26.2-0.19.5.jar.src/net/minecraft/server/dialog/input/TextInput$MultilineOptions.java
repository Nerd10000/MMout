/*    */ package net.minecraft.server.dialog.input;
/*    */ 
/*    */ import com.mojang.datafixers.kinds.App;
/*    */ import com.mojang.datafixers.kinds.Applicative;
/*    */ import com.mojang.serialization.Codec;
/*    */ import com.mojang.serialization.codecs.RecordCodecBuilder;
/*    */ import java.util.Optional;
/*    */ import java.util.function.BiFunction;
/*    */ import net.minecraft.util.ExtraCodecs;
/*    */ 
/*    */ public final class MultilineOptions extends Record {
/*    */   private final Optional<Integer> maxLines;
/*    */   
/*    */   private final Optional<Integer> height;
/*    */   
/*    */   public static final int MAX_HEIGHT = 512;
/*    */   
/*    */   public static final Codec<MultilineOptions> CODEC;
/*    */   
/*    */   public MultilineOptions(Optional<Integer> maxLines, Optional<Integer> height) {
/* 40 */     this.maxLines = maxLines;
/* 40 */     this.height = height;
/*    */   }
/*    */   
/*    */   public final String toString() {
/*    */     // Byte code:
/*    */     //   0: aload_0
/*    */     //   1: <illegal opcode> toString : (Lnet/minecraft/server/dialog/input/TextInput$MultilineOptions;)Ljava/lang/String;
/*    */     //   6: areturn
/*    */     // Line number table:
/*    */     //   Java source line number -> byte code offset
/*    */     //   #40	-> 0
/*    */     // Local variable table:
/*    */     //   start	length	slot	name	descriptor
/*    */     //   0	7	0	this	Lnet/minecraft/server/dialog/input/TextInput$MultilineOptions;
/*    */   }
/*    */   
/*    */   public final int hashCode() {
/*    */     // Byte code:
/*    */     //   0: aload_0
/*    */     //   1: <illegal opcode> hashCode : (Lnet/minecraft/server/dialog/input/TextInput$MultilineOptions;)I
/*    */     //   6: ireturn
/*    */     // Line number table:
/*    */     //   Java source line number -> byte code offset
/*    */     //   #40	-> 0
/*    */     // Local variable table:
/*    */     //   start	length	slot	name	descriptor
/*    */     //   0	7	0	this	Lnet/minecraft/server/dialog/input/TextInput$MultilineOptions;
/*    */   }
/*    */   
/*    */   public final boolean equals(Object o) {
/*    */     // Byte code:
/*    */     //   0: aload_0
/*    */     //   1: aload_1
/*    */     //   2: <illegal opcode> equals : (Lnet/minecraft/server/dialog/input/TextInput$MultilineOptions;Ljava/lang/Object;)Z
/*    */     //   7: ireturn
/*    */     // Line number table:
/*    */     //   Java source line number -> byte code offset
/*    */     //   #40	-> 0
/*    */     // Local variable table:
/*    */     //   start	length	slot	name	descriptor
/*    */     //   0	8	0	this	Lnet/minecraft/server/dialog/input/TextInput$MultilineOptions;
/*    */     //   0	8	1	o	Ljava/lang/Object;
/*    */   }
/*    */   
/*    */   public Optional<Integer> maxLines() {
/* 40 */     return this.maxLines;
/*    */   }
/*    */   
/*    */   public Optional<Integer> height() {
/* 40 */     return this.height;
/*    */   }
/*    */   
/*    */   static {
/* 45 */     CODEC = RecordCodecBuilder.create(i -> i.group((App)ExtraCodecs.POSITIVE_INT.optionalFieldOf("max_lines").forGetter(MultilineOptions::maxLines), (App)ExtraCodecs.intRange(1, 512).optionalFieldOf("height").forGetter(MultilineOptions::height)).apply((Applicative)i, MultilineOptions::new));
/*    */   }
/*    */ }


/* Location:              C:\Users\karol\AppData\Roaming\ModrinthApp\meta\versions\26.2-0.19.5\26.2-0.19.5.jar!\net\minecraft\server\dialog\input\TextInput$MultilineOptions.class
 * Java compiler version: 25 (69.0)
 * JD-Core Version:       1.1.3
 */