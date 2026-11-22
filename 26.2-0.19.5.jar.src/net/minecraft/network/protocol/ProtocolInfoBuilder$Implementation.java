/*     */ package net.minecraft.network.protocol;
/*     */ 
/*     */ import io.netty.buffer.ByteBuf;
/*     */ import net.minecraft.network.ConnectionProtocol;
/*     */ import net.minecraft.network.PacketListener;
/*     */ import net.minecraft.network.ProtocolInfo;
/*     */ import net.minecraft.network.codec.StreamCodec;
/*     */ 
/*     */ final class Implementation<L extends PacketListener> extends Record implements ProtocolInfo<L> {
/*     */   private final ConnectionProtocol id;
/*     */   
/*     */   private final PacketFlow flow;
/*     */   
/*     */   private final StreamCodec<ByteBuf, Packet<? super L>> codec;
/*     */   
/*     */   private final BundlerInfo bundlerInfo;
/*     */   
/*     */   private Implementation(ConnectionProtocol id, PacketFlow flow, StreamCodec<ByteBuf, Packet<? super L>> codec, BundlerInfo bundlerInfo) {
/* 162 */     this.id = id;
/* 162 */     this.flow = flow;
/* 162 */     this.codec = codec;
/* 162 */     this.bundlerInfo = bundlerInfo;
/*     */   }
/*     */   
/*     */   public final String toString() {
/*     */     // Byte code:
/*     */     //   0: aload_0
/*     */     //   1: <illegal opcode> toString : (Lnet/minecraft/network/protocol/ProtocolInfoBuilder$Implementation;)Ljava/lang/String;
/*     */     //   6: areturn
/*     */     // Line number table:
/*     */     //   Java source line number -> byte code offset
/*     */     //   #162	-> 0
/*     */     // Local variable table:
/*     */     //   start	length	slot	name	descriptor
/*     */     //   0	7	0	this	Lnet/minecraft/network/protocol/ProtocolInfoBuilder$Implementation;
/*     */     // Local variable type table:
/*     */     //   start	length	slot	name	signature
/*     */     //   0	7	0	this	Lnet/minecraft/network/protocol/ProtocolInfoBuilder$Implementation<TL;>;
/*     */   }
/*     */   
/*     */   public final int hashCode() {
/*     */     // Byte code:
/*     */     //   0: aload_0
/*     */     //   1: <illegal opcode> hashCode : (Lnet/minecraft/network/protocol/ProtocolInfoBuilder$Implementation;)I
/*     */     //   6: ireturn
/*     */     // Line number table:
/*     */     //   Java source line number -> byte code offset
/*     */     //   #162	-> 0
/*     */     // Local variable table:
/*     */     //   start	length	slot	name	descriptor
/*     */     //   0	7	0	this	Lnet/minecraft/network/protocol/ProtocolInfoBuilder$Implementation;
/*     */     // Local variable type table:
/*     */     //   start	length	slot	name	signature
/*     */     //   0	7	0	this	Lnet/minecraft/network/protocol/ProtocolInfoBuilder$Implementation<TL;>;
/*     */   }
/*     */   
/*     */   public final boolean equals(Object o) {
/*     */     // Byte code:
/*     */     //   0: aload_0
/*     */     //   1: aload_1
/*     */     //   2: <illegal opcode> equals : (Lnet/minecraft/network/protocol/ProtocolInfoBuilder$Implementation;Ljava/lang/Object;)Z
/*     */     //   7: ireturn
/*     */     // Line number table:
/*     */     //   Java source line number -> byte code offset
/*     */     //   #162	-> 0
/*     */     // Local variable table:
/*     */     //   start	length	slot	name	descriptor
/*     */     //   0	8	0	this	Lnet/minecraft/network/protocol/ProtocolInfoBuilder$Implementation;
/*     */     //   0	8	1	o	Ljava/lang/Object;
/*     */     // Local variable type table:
/*     */     //   start	length	slot	name	signature
/*     */     //   0	8	0	this	Lnet/minecraft/network/protocol/ProtocolInfoBuilder$Implementation<TL;>;
/*     */   }
/*     */   
/*     */   public ConnectionProtocol id() {
/* 162 */     return this.id;
/*     */   }
/*     */   
/*     */   public PacketFlow flow() {
/* 162 */     return this.flow;
/*     */   }
/*     */   
/*     */   public StreamCodec<ByteBuf, Packet<? super L>> codec() {
/* 162 */     return this.codec;
/*     */   }
/*     */   
/*     */   public BundlerInfo bundlerInfo() {
/* 162 */     return this.bundlerInfo;
/*     */   }
/*     */ }


/* Location:              C:\Users\karol\AppData\Roaming\ModrinthApp\meta\versions\26.2-0.19.5\26.2-0.19.5.jar!\net\minecraft\network\protocol\ProtocolInfoBuilder$Implementation.class
 * Java compiler version: 25 (69.0)
 * JD-Core Version:       1.1.3
 */