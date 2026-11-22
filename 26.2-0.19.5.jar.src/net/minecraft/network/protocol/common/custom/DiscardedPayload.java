/*    */ package net.minecraft.network.protocol.common.custom;
/*    */ 
/*    */ import net.minecraft.network.FriendlyByteBuf;
/*    */ import net.minecraft.network.codec.StreamCodec;
/*    */ import net.minecraft.resources.Identifier;
/*    */ 
/*    */ public final class DiscardedPayload extends Record implements CustomPacketPayload {
/*    */   private final Identifier id;
/*    */   
/*    */   public DiscardedPayload(Identifier id) {
/*  7 */     this.id = id;
/*    */   }
/*    */   
/*    */   public final String toString() {
/*    */     // Byte code:
/*    */     //   0: aload_0
/*    */     //   1: <illegal opcode> toString : (Lnet/minecraft/network/protocol/common/custom/DiscardedPayload;)Ljava/lang/String;
/*    */     //   6: areturn
/*    */     // Line number table:
/*    */     //   Java source line number -> byte code offset
/*    */     //   #7	-> 0
/*    */     // Local variable table:
/*    */     //   start	length	slot	name	descriptor
/*    */     //   0	7	0	this	Lnet/minecraft/network/protocol/common/custom/DiscardedPayload;
/*    */   }
/*    */   
/*    */   public final int hashCode() {
/*    */     // Byte code:
/*    */     //   0: aload_0
/*    */     //   1: <illegal opcode> hashCode : (Lnet/minecraft/network/protocol/common/custom/DiscardedPayload;)I
/*    */     //   6: ireturn
/*    */     // Line number table:
/*    */     //   Java source line number -> byte code offset
/*    */     //   #7	-> 0
/*    */     // Local variable table:
/*    */     //   start	length	slot	name	descriptor
/*    */     //   0	7	0	this	Lnet/minecraft/network/protocol/common/custom/DiscardedPayload;
/*    */   }
/*    */   
/*    */   public final boolean equals(Object o) {
/*    */     // Byte code:
/*    */     //   0: aload_0
/*    */     //   1: aload_1
/*    */     //   2: <illegal opcode> equals : (Lnet/minecraft/network/protocol/common/custom/DiscardedPayload;Ljava/lang/Object;)Z
/*    */     //   7: ireturn
/*    */     // Line number table:
/*    */     //   Java source line number -> byte code offset
/*    */     //   #7	-> 0
/*    */     // Local variable table:
/*    */     //   start	length	slot	name	descriptor
/*    */     //   0	8	0	this	Lnet/minecraft/network/protocol/common/custom/DiscardedPayload;
/*    */     //   0	8	1	o	Ljava/lang/Object;
/*    */   }
/*    */   
/*    */   public Identifier id() {
/*  7 */     return this.id;
/*    */   }
/*    */   
/*    */   public static <T extends FriendlyByteBuf> StreamCodec<T, DiscardedPayload> codec(Identifier id, int maxPayloadSize) {
/*  9 */     return (StreamCodec)CustomPacketPayload.codec((payload, buf) -> {
/*    */         
/*    */         }buf -> {
/*    */           int length = buf.readableBytes();
/*    */           if (length < 0 || length > maxPayloadSize)
/*    */             throw new IllegalArgumentException("Payload may not be larger than " + maxPayloadSize + " bytes"); 
/*    */           buf.skipBytes(length);
/*    */           return new DiscardedPayload(id);
/*    */         });
/*    */   }
/*    */   
/*    */   public CustomPacketPayload.Type<DiscardedPayload> type() {
/* 24 */     return new CustomPacketPayload.Type<>(this.id);
/*    */   }
/*    */ }


/* Location:              C:\Users\karol\AppData\Roaming\ModrinthApp\meta\versions\26.2-0.19.5\26.2-0.19.5.jar!\net\minecraft\network\protocol\common\custom\DiscardedPayload.class
 * Java compiler version: 25 (69.0)
 * JD-Core Version:       1.1.3
 */