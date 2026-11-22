/*    */ package net.minecraft.network;
/*    */ 
/*    */ import io.netty.channel.ChannelHandlerContext;
/*    */ import io.netty.handler.codec.MessageToMessageEncoder;
/*    */ import java.util.List;
/*    */ import java.util.Objects;
/*    */ import net.minecraft.network.protocol.BundlerInfo;
/*    */ import net.minecraft.network.protocol.Packet;
/*    */ 
/*    */ public class PacketBundleUnpacker extends MessageToMessageEncoder<Packet<?>> {
/*    */   private final BundlerInfo bundlerInfo;
/*    */   
/*    */   public PacketBundleUnpacker(BundlerInfo bundlerInfo) {
/* 14 */     this.bundlerInfo = bundlerInfo;
/*    */   }
/*    */   
/*    */   protected void encode(ChannelHandlerContext ctx, Packet<?> msg, List<Object> out) throws Exception {
/* 19 */     Objects.requireNonNull(out);
/* 19 */     this.bundlerInfo.unbundlePacket(msg, out::add);
/* 20 */     if (msg.isTerminal())
/* 21 */       ctx.pipeline().remove(ctx.name()); 
/*    */   }
/*    */ }


/* Location:              C:\Users\karol\AppData\Roaming\ModrinthApp\meta\versions\26.2-0.19.5\26.2-0.19.5.jar!\net\minecraft\network\PacketBundleUnpacker.class
 * Java compiler version: 25 (69.0)
 * JD-Core Version:       1.1.3
 */