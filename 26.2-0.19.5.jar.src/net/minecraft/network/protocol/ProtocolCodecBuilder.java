/*    */ package net.minecraft.network.protocol;
/*    */ 
/*    */ import io.netty.buffer.ByteBuf;
/*    */ import net.minecraft.network.PacketListener;
/*    */ import net.minecraft.network.codec.IdDispatchCodec;
/*    */ import net.minecraft.network.codec.StreamCodec;
/*    */ 
/*    */ public class ProtocolCodecBuilder<B extends ByteBuf, L extends PacketListener> {
/*    */   private final IdDispatchCodec.Builder<B, Packet<? super L>, PacketType<? extends Packet<? super L>>> dispatchBuilder;
/*    */   
/*    */   private final PacketFlow flow;
/*    */   
/*    */   public ProtocolCodecBuilder(PacketFlow flow) {
/* 13 */     this.dispatchBuilder = IdDispatchCodec.builder(Packet::type);
/* 14 */     this.flow = flow;
/*    */   }
/*    */   
/*    */   public <T extends Packet<? super L>> ProtocolCodecBuilder<B, L> add(PacketType<T> type, StreamCodec<? super B, T> serializer) {
/* 18 */     if (type.flow() != this.flow)
/* 19 */       throw new IllegalArgumentException("Invalid packet flow for packet " + String.valueOf(type) + ", expected " + this.flow.name()); 
/* 21 */     this.dispatchBuilder.add(type, serializer);
/* 22 */     return this;
/*    */   }
/*    */   
/*    */   public StreamCodec<B, Packet<? super L>> build() {
/* 26 */     return (StreamCodec<B, Packet<? super L>>)this.dispatchBuilder.build();
/*    */   }
/*    */ }


/* Location:              C:\Users\karol\AppData\Roaming\ModrinthApp\meta\versions\26.2-0.19.5\26.2-0.19.5.jar!\net\minecraft\network\protocol\ProtocolCodecBuilder.class
 * Java compiler version: 25 (69.0)
 * JD-Core Version:       1.1.3
 */