/*    */ package net.minecraft.server.jsonrpc.websocket;
/*    */ 
/*    */ import com.google.gson.JsonElement;
/*    */ import com.google.gson.JsonParser;
/*    */ import io.netty.channel.ChannelHandlerContext;
/*    */ import io.netty.handler.codec.MessageToMessageDecoder;
/*    */ import io.netty.handler.codec.http.websocketx.TextWebSocketFrame;
/*    */ import java.util.List;
/*    */ 
/*    */ public class WebSocketToJsonCodec extends MessageToMessageDecoder<TextWebSocketFrame> {
/*    */   protected void decode(ChannelHandlerContext ctx, TextWebSocketFrame msg, List<Object> out) {
/* 14 */     String message = msg.text();
/* 15 */     JsonElement parsedMessage = JsonParser.parseString(message);
/* 16 */     out.add(parsedMessage);
/*    */   }
/*    */ }


/* Location:              C:\Users\karol\AppData\Roaming\ModrinthApp\meta\versions\26.2-0.19.5\26.2-0.19.5.jar!\net\minecraft\server\jsonrpc\websocket\WebSocketToJsonCodec.class
 * Java compiler version: 25 (69.0)
 * JD-Core Version:       1.1.3
 */