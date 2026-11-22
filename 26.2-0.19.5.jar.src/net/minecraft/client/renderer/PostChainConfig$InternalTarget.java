/*    */ package net.minecraft.client.renderer;
/*    */ 
/*    */ import com.mojang.datafixers.kinds.App;
/*    */ import com.mojang.datafixers.kinds.Applicative;
/*    */ import com.mojang.datafixers.util.Function4;
/*    */ import com.mojang.serialization.Codec;
/*    */ import com.mojang.serialization.codecs.RecordCodecBuilder;
/*    */ import java.util.Optional;
/*    */ import net.minecraft.util.ExtraCodecs;
/*    */ 
/*    */ public final class InternalTarget extends Record {
/*    */   private final Optional<Integer> width;
/*    */   
/*    */   private final Optional<Integer> height;
/*    */   
/*    */   private final boolean persistent;
/*    */   
/*    */   private final int clearColor;
/*    */   
/*    */   public static final Codec<InternalTarget> CODEC;
/*    */   
/*    */   public InternalTarget(Optional<Integer> width, Optional<Integer> height, boolean persistent, int clearColor) {
/* 27 */     this.width = width;
/* 27 */     this.height = height;
/* 27 */     this.persistent = persistent;
/* 27 */     this.clearColor = clearColor;
/*    */   }
/*    */   
/*    */   public final String toString() {
/*    */     // Byte code:
/*    */     //   0: aload_0
/*    */     //   1: <illegal opcode> toString : (Lnet/minecraft/client/renderer/PostChainConfig$InternalTarget;)Ljava/lang/String;
/*    */     //   6: areturn
/*    */     // Line number table:
/*    */     //   Java source line number -> byte code offset
/*    */     //   #27	-> 0
/*    */     // Local variable table:
/*    */     //   start	length	slot	name	descriptor
/*    */     //   0	7	0	this	Lnet/minecraft/client/renderer/PostChainConfig$InternalTarget;
/*    */   }
/*    */   
/*    */   public final int hashCode() {
/*    */     // Byte code:
/*    */     //   0: aload_0
/*    */     //   1: <illegal opcode> hashCode : (Lnet/minecraft/client/renderer/PostChainConfig$InternalTarget;)I
/*    */     //   6: ireturn
/*    */     // Line number table:
/*    */     //   Java source line number -> byte code offset
/*    */     //   #27	-> 0
/*    */     // Local variable table:
/*    */     //   start	length	slot	name	descriptor
/*    */     //   0	7	0	this	Lnet/minecraft/client/renderer/PostChainConfig$InternalTarget;
/*    */   }
/*    */   
/*    */   public final boolean equals(Object o) {
/*    */     // Byte code:
/*    */     //   0: aload_0
/*    */     //   1: aload_1
/*    */     //   2: <illegal opcode> equals : (Lnet/minecraft/client/renderer/PostChainConfig$InternalTarget;Ljava/lang/Object;)Z
/*    */     //   7: ireturn
/*    */     // Line number table:
/*    */     //   Java source line number -> byte code offset
/*    */     //   #27	-> 0
/*    */     // Local variable table:
/*    */     //   start	length	slot	name	descriptor
/*    */     //   0	8	0	this	Lnet/minecraft/client/renderer/PostChainConfig$InternalTarget;
/*    */     //   0	8	1	o	Ljava/lang/Object;
/*    */   }
/*    */   
/*    */   public Optional<Integer> width() {
/* 27 */     return this.width;
/*    */   }
/*    */   
/*    */   public Optional<Integer> height() {
/* 27 */     return this.height;
/*    */   }
/*    */   
/*    */   public boolean persistent() {
/* 27 */     return this.persistent;
/*    */   }
/*    */   
/*    */   public int clearColor() {
/* 27 */     return this.clearColor;
/*    */   }
/*    */   
/*    */   static {
/* 33 */     CODEC = RecordCodecBuilder.create(i -> i.group((App)ExtraCodecs.POSITIVE_INT.optionalFieldOf("width").forGetter(InternalTarget::width), (App)ExtraCodecs.POSITIVE_INT.optionalFieldOf("height").forGetter(InternalTarget::height), (App)Codec.BOOL.optionalFieldOf("persistent", Boolean.valueOf(false)).forGetter(InternalTarget::persistent), (App)ExtraCodecs.ARGB_COLOR_CODEC.optionalFieldOf("clear_color", Integer.valueOf(0)).forGetter(InternalTarget::clearColor)).apply((Applicative)i, InternalTarget::new));
/*    */   }
/*    */ }


/* Location:              C:\Users\karol\AppData\Roaming\ModrinthApp\meta\versions\26.2-0.19.5\26.2-0.19.5.jar!\net\minecraft\client\renderer\PostChainConfig$InternalTarget.class
 * Java compiler version: 25 (69.0)
 * JD-Core Version:       1.1.3
 */