/*    */ package net.minecraft.network.protocol.common.custom;
/*    */ 
/*    */ import net.minecraft.network.FriendlyByteBuf;
/*    */ import net.minecraft.network.codec.StreamCodec;
/*    */ 
/*    */ public final class TypeAndCodec<B extends FriendlyByteBuf, T extends CustomPacketPayload> extends Record {
/*    */   private final CustomPacketPayload.Type<T> type;
/*    */   
/*    */   private final StreamCodec<B, T> codec;
/*    */   
/*    */   public TypeAndCodec(CustomPacketPayload.Type<T> type, StreamCodec<B, T> codec) {
/* 30 */     this.type = type;
/* 30 */     this.codec = codec;
/*    */   }
/*    */   
/*    */   public final String toString() {
/*    */     // Byte code:
/*    */     //   0: aload_0
/*    */     //   1: <illegal opcode> toString : (Lnet/minecraft/network/protocol/common/custom/CustomPacketPayload$TypeAndCodec;)Ljava/lang/String;
/*    */     //   6: areturn
/*    */     // Line number table:
/*    */     //   Java source line number -> byte code offset
/*    */     //   #30	-> 0
/*    */     // Local variable table:
/*    */     //   start	length	slot	name	descriptor
/*    */     //   0	7	0	this	Lnet/minecraft/network/protocol/common/custom/CustomPacketPayload$TypeAndCodec;
/*    */     // Local variable type table:
/*    */     //   start	length	slot	name	signature
/*    */     //   0	7	0	this	Lnet/minecraft/network/protocol/common/custom/CustomPacketPayload$TypeAndCodec<TB;TT;>;
/*    */   }
/*    */   
/*    */   public final int hashCode() {
/*    */     // Byte code:
/*    */     //   0: aload_0
/*    */     //   1: <illegal opcode> hashCode : (Lnet/minecraft/network/protocol/common/custom/CustomPacketPayload$TypeAndCodec;)I
/*    */     //   6: ireturn
/*    */     // Line number table:
/*    */     //   Java source line number -> byte code offset
/*    */     //   #30	-> 0
/*    */     // Local variable table:
/*    */     //   start	length	slot	name	descriptor
/*    */     //   0	7	0	this	Lnet/minecraft/network/protocol/common/custom/CustomPacketPayload$TypeAndCodec;
/*    */     // Local variable type table:
/*    */     //   start	length	slot	name	signature
/*    */     //   0	7	0	this	Lnet/minecraft/network/protocol/common/custom/CustomPacketPayload$TypeAndCodec<TB;TT;>;
/*    */   }
/*    */   
/*    */   public final boolean equals(Object o) {
/*    */     // Byte code:
/*    */     //   0: aload_0
/*    */     //   1: aload_1
/*    */     //   2: <illegal opcode> equals : (Lnet/minecraft/network/protocol/common/custom/CustomPacketPayload$TypeAndCodec;Ljava/lang/Object;)Z
/*    */     //   7: ireturn
/*    */     // Line number table:
/*    */     //   Java source line number -> byte code offset
/*    */     //   #30	-> 0
/*    */     // Local variable table:
/*    */     //   start	length	slot	name	descriptor
/*    */     //   0	8	0	this	Lnet/minecraft/network/protocol/common/custom/CustomPacketPayload$TypeAndCodec;
/*    */     //   0	8	1	o	Ljava/lang/Object;
/*    */     // Local variable type table:
/*    */     //   start	length	slot	name	signature
/*    */     //   0	8	0	this	Lnet/minecraft/network/protocol/common/custom/CustomPacketPayload$TypeAndCodec<TB;TT;>;
/*    */   }
/*    */   
/*    */   public CustomPacketPayload.Type<T> type() {
/* 30 */     return this.type;
/*    */   }
/*    */   
/*    */   public StreamCodec<B, T> codec() {
/* 30 */     return this.codec;
/*    */   }
/*    */ }


/* Location:              C:\Users\karol\AppData\Roaming\ModrinthApp\meta\versions\26.2-0.19.5\26.2-0.19.5.jar!\net\minecraft\network\protocol\common\custom\CustomPacketPayload$TypeAndCodec.class
 * Java compiler version: 25 (69.0)
 * JD-Core Version:       1.1.3
 */