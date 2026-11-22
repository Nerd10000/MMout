/*    */ package net.minecraft.network;
/*    */ 
/*    */ import com.mojang.logging.LogUtils;
/*    */ import io.netty.buffer.ByteBuf;
/*    */ import io.netty.channel.ChannelHandlerContext;
/*    */ import io.netty.handler.codec.ByteToMessageDecoder;
/*    */ import java.io.IOException;
/*    */ import java.util.List;
/*    */ import net.minecraft.network.protocol.Packet;
/*    */ import net.minecraft.network.protocol.PacketType;
/*    */ import net.minecraft.util.profiling.jfr.JvmProfiler;
/*    */ import org.slf4j.Logger;
/*    */ 
/*    */ public class PacketDecoder<T extends PacketListener> extends ByteToMessageDecoder implements ProtocolSwapHandler {
/* 16 */   private static final Logger LOGGER = LogUtils.getLogger();
/*    */   
/*    */   private final ProtocolInfo<T> protocolInfo;
/*    */   
/*    */   public PacketDecoder(ProtocolInfo<T> protocolInfo) {
/* 21 */     this.protocolInfo = protocolInfo;
/*    */   }
/*    */   
/*    */   protected void decode(ChannelHandlerContext ctx, ByteBuf input, List<Object> out) throws Exception {
/*    */     Packet<? super T> packet;
/* 26 */     int readableBytes = input.readableBytes();
/*    */     try {
/* 30 */       packet = (Packet<? super T>)this.protocolInfo.codec().decode(input);
/* 31 */     } catch (Exception e) {
/* 32 */       if (e instanceof SkipPacketException)
/* 33 */         input.skipBytes(input.readableBytes()); 
/* 35 */       throw e;
/*    */     } 
/* 38 */     PacketType<? extends Packet<? super T>> packetId = packet.type();
/* 40 */     JvmProfiler.INSTANCE.onPacketReceived(this.protocolInfo
/* 41 */         .id(), packetId, ctx
/*    */         
/* 43 */         .channel().remoteAddress(), readableBytes);
/* 47 */     if (input.readableBytes() > 0)
/* 48 */       throw new IOException("Packet " + this.protocolInfo.id().id() + "/" + String.valueOf(packetId) + " (" + packet.getClass().getSimpleName() + ") was larger than I expected, found " + input.readableBytes() + " bytes extra whilst reading packet " + String.valueOf(packetId)); 
/* 50 */     out.add(packet);
/* 53 */     if (LOGGER.isDebugEnabled())
/* 54 */       LOGGER.debug(Connection.PACKET_RECEIVED_MARKER, " IN: [{}:{}] {} -> {} bytes", new Object[] { this.protocolInfo.id().id(), packetId, packet.getClass().getName(), Integer.valueOf(readableBytes) }); 
/* 57 */     ProtocolSwapHandler.handleInboundTerminalPacket(ctx, packet);
/*    */   }
/*    */ }


/* Location:              C:\Users\karol\AppData\Roaming\ModrinthApp\meta\versions\26.2-0.19.5\26.2-0.19.5.jar!\net\minecraft\network\PacketDecoder.class
 * Java compiler version: 25 (69.0)
 * JD-Core Version:       1.1.3
 */