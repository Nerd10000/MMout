/*     */ package net.minecraft.network.protocol;
/*     */ 
/*     */ import io.netty.buffer.ByteBuf;
/*     */ import java.util.List;
/*     */ import java.util.function.Function;
/*     */ import net.minecraft.network.ProtocolInfo;
/*     */ 
/*     */ class null implements SimpleUnboundProtocol<T, B> {
/*     */   public ProtocolInfo<T> bind(Function<ByteBuf, B> contextWrapper) {
/* 105 */     return new ProtocolInfoBuilder.Implementation<>(ProtocolInfoBuilder.this.protocol, ProtocolInfoBuilder.this.flow, ProtocolInfoBuilder.this.buildPacketCodec(contextWrapper, codecs, context), bundlerInfo);
/*     */   }
/*     */   
/*     */   public ProtocolInfo.Details details() {
/* 110 */     return details;
/*     */   }
/*     */ }


/* Location:              C:\Users\karol\AppData\Roaming\ModrinthApp\meta\versions\26.2-0.19.5\26.2-0.19.5.jar!\net\minecraft\network\protocol\ProtocolInfoBuilder$2.class
 * Java compiler version: 25 (69.0)
 * JD-Core Version:       1.1.3
 */