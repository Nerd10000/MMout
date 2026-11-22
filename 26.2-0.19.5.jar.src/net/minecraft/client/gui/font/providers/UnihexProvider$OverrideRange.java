/*    */ package net.minecraft.client.gui.font.providers;
/*    */ 
/*    */ import com.mojang.datafixers.kinds.App;
/*    */ import com.mojang.datafixers.kinds.Applicative;
/*    */ import com.mojang.datafixers.util.Function3;
/*    */ import com.mojang.serialization.Codec;
/*    */ import com.mojang.serialization.DataResult;
/*    */ import com.mojang.serialization.codecs.RecordCodecBuilder;
/*    */ import net.minecraft.util.ExtraCodecs;
/*    */ 
/*    */ final class OverrideRange extends Record {
/*    */   private final int from;
/*    */   
/*    */   private final int to;
/*    */   
/*    */   private final UnihexProvider.Dimensions dimensions;
/*    */   
/*    */   private static final Codec<OverrideRange> RAW_CODEC;
/*    */   
/*    */   public static final Codec<OverrideRange> CODEC;
/*    */   
/*    */   private OverrideRange(int from, int to, UnihexProvider.Dimensions dimensions) {
/* 66 */     this.from = from;
/* 66 */     this.to = to;
/* 66 */     this.dimensions = dimensions;
/*    */   }
/*    */   
/*    */   public final String toString() {
/*    */     // Byte code:
/*    */     //   0: aload_0
/*    */     //   1: <illegal opcode> toString : (Lnet/minecraft/client/gui/font/providers/UnihexProvider$OverrideRange;)Ljava/lang/String;
/*    */     //   6: areturn
/*    */     // Line number table:
/*    */     //   Java source line number -> byte code offset
/*    */     //   #66	-> 0
/*    */     // Local variable table:
/*    */     //   start	length	slot	name	descriptor
/*    */     //   0	7	0	this	Lnet/minecraft/client/gui/font/providers/UnihexProvider$OverrideRange;
/*    */   }
/*    */   
/*    */   public final int hashCode() {
/*    */     // Byte code:
/*    */     //   0: aload_0
/*    */     //   1: <illegal opcode> hashCode : (Lnet/minecraft/client/gui/font/providers/UnihexProvider$OverrideRange;)I
/*    */     //   6: ireturn
/*    */     // Line number table:
/*    */     //   Java source line number -> byte code offset
/*    */     //   #66	-> 0
/*    */     // Local variable table:
/*    */     //   start	length	slot	name	descriptor
/*    */     //   0	7	0	this	Lnet/minecraft/client/gui/font/providers/UnihexProvider$OverrideRange;
/*    */   }
/*    */   
/*    */   public final boolean equals(Object o) {
/*    */     // Byte code:
/*    */     //   0: aload_0
/*    */     //   1: aload_1
/*    */     //   2: <illegal opcode> equals : (Lnet/minecraft/client/gui/font/providers/UnihexProvider$OverrideRange;Ljava/lang/Object;)Z
/*    */     //   7: ireturn
/*    */     // Line number table:
/*    */     //   Java source line number -> byte code offset
/*    */     //   #66	-> 0
/*    */     // Local variable table:
/*    */     //   start	length	slot	name	descriptor
/*    */     //   0	8	0	this	Lnet/minecraft/client/gui/font/providers/UnihexProvider$OverrideRange;
/*    */     //   0	8	1	o	Ljava/lang/Object;
/*    */   }
/*    */   
/*    */   public int from() {
/* 66 */     return this.from;
/*    */   }
/*    */   
/*    */   public int to() {
/* 66 */     return this.to;
/*    */   }
/*    */   
/*    */   public UnihexProvider.Dimensions dimensions() {
/* 66 */     return this.dimensions;
/*    */   }
/*    */   
/*    */   static {
/* 67 */     RAW_CODEC = RecordCodecBuilder.create(i -> i.group((App)ExtraCodecs.CODEPOINT.fieldOf("from").forGetter(OverrideRange::from), (App)ExtraCodecs.CODEPOINT.fieldOf("to").forGetter(OverrideRange::to), (App)UnihexProvider.Dimensions.MAP_CODEC.forGetter(OverrideRange::dimensions)).apply((Applicative)i, OverrideRange::new));
/* 73 */     CODEC = RAW_CODEC.validate(o -> (o.from >= o.to) ? DataResult.error(()) : DataResult.success(o));
/*    */   }
/*    */ }


/* Location:              C:\Users\karol\AppData\Roaming\ModrinthApp\meta\versions\26.2-0.19.5\26.2-0.19.5.jar!\net\minecraft\client\gui\font\providers\UnihexProvider$OverrideRange.class
 * Java compiler version: 25 (69.0)
 * JD-Core Version:       1.1.3
 */