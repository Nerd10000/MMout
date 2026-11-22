/*     */ package net.minecraft.server.jsonrpc;
/*     */ 
/*     */ import io.netty.channel.Channel;
/*     */ import io.netty.channel.ChannelException;
/*     */ import io.netty.channel.ChannelHandler;
/*     */ import io.netty.channel.ChannelInitializer;
/*     */ import io.netty.channel.ChannelOption;
/*     */ import io.netty.channel.ChannelPipeline;
/*     */ import io.netty.handler.codec.http.HttpObjectAggregator;
/*     */ import io.netty.handler.codec.http.HttpServerCodec;
/*     */ import io.netty.handler.codec.http.websocketx.WebSocketFrameAggregator;
/*     */ import io.netty.handler.codec.http.websocketx.WebSocketServerProtocolHandler;
/*     */ import io.netty.handler.ssl.SslContext;
/*     */ import net.minecraft.server.jsonrpc.internalapi.MinecraftApi;
/*     */ import net.minecraft.server.jsonrpc.websocket.JsonToWebSocketEncoder;
/*     */ import net.minecraft.server.jsonrpc.websocket.WebSocketToJsonCodec;
/*     */ 
/*     */ class null extends ChannelInitializer<Channel> {
/*     */   protected void initChannel(Channel channel) {
/*     */     try {
/* 112 */       channel.config().setOption(ChannelOption.TCP_NODELAY, Boolean.valueOf(true));
/* 113 */     } catch (ChannelException channelException) {}
/* 116 */     ChannelPipeline pipeline = channel.pipeline();
/* 117 */     if (sslContext != null)
/* 118 */       pipeline.addLast(new ChannelHandler[] { (ChannelHandler)this.val$sslContext.newHandler(channel.alloc()) }); 
/* 120 */     pipeline.addLast(new ChannelHandler[] { (ChannelHandler)new HttpServerCodec() }).addLast(new ChannelHandler[] { (ChannelHandler)new HttpObjectAggregator(65536) }).addLast(new ChannelHandler[] { (ChannelHandler)this.this$0.authenticationHandler }).addLast(new ChannelHandler[] { (ChannelHandler)new WebSocketServerProtocolHandler("/") }).addLast(new ChannelHandler[] { (ChannelHandler)new WebSocketFrameAggregator(65536) }).addLast(new ChannelHandler[] { (ChannelHandler)new WebSocketToJsonCodec() }).addLast(new ChannelHandler[] { (ChannelHandler)new JsonToWebSocketEncoder() }).addLast(new ChannelHandler[] { (ChannelHandler)new Connection(channel, this.this$0, this.val$minecraftApi, this.val$jsonrpcLogger) });
/*     */   }
/*     */ }


/* Location:              C:\Users\karol\AppData\Roaming\ModrinthApp\meta\versions\26.2-0.19.5\26.2-0.19.5.jar!\net\minecraft\server\jsonrpc\ManagementServer$1.class
 * Java compiler version: 25 (69.0)
 * JD-Core Version:       1.1.3
 */