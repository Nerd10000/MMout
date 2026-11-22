/*    */ package net.minecraft.server.jsonrpc.websocket;
/*    */ 
/*    */ import com.google.gson.JsonElement;
/*    */ import io.netty.channel.ChannelHandlerContext;
/*    */ import io.netty.handler.codec.MessageToMessageEncoder;
/*    */ import io.netty.handler.codec.http.websocketx.TextWebSocketFrame;
/*    */ import java.util.List;
/*    */ 
/*    */ public class JsonToWebSocketEncoder extends MessageToMessageEncoder<JsonElement> {
/*    */   protected void encode(ChannelHandlerContext ctx, JsonElement msg, List<Object> out) {
/* 13 */     out.add(new TextWebSocketFrame(msg.toString()));
/*    */   }
/*    */ }


/* Location:              C:\Users\karol\AppData\Roaming\ModrinthApp\meta\versions\26.2-0.19.5\26.2-0.19.5.jar!\net\minecraft\server\jsonrpc\websocket\JsonToWebSocketEncoder.class
 * Java compiler version: 25 (69.0)
 * JD-Core Version:       1.1.3
 */