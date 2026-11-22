/*    */ package net.minecraft.client.multiplayer.chat;
/*    */ 
/*    */ import com.mojang.serialization.MapCodec;
/*    */ import java.util.function.Supplier;
/*    */ import net.minecraft.util.StringRepresentable;
/*    */ 
/*    */ public enum Type implements StringRepresentable {
/* 15 */   PLAYER("player", () -> LoggedChatMessage.Player.CODEC),
/* 16 */   SYSTEM("system", () -> LoggedChatMessage.System.CODEC);
/*    */   
/*    */   private final String serializedName;
/*    */   
/*    */   private final Supplier<MapCodec<? extends LoggedChatEvent>> codec;
/*    */   
/*    */   Type(String serializedName, Supplier<MapCodec<? extends LoggedChatEvent>> codec) {
/* 23 */     this.serializedName = serializedName;
/* 24 */     this.codec = codec;
/*    */   }
/*    */   
/*    */   private MapCodec<? extends LoggedChatEvent> codec() {
/* 28 */     return this.codec.get();
/*    */   }
/*    */   
/*    */   public String getSerializedName() {
/* 33 */     return this.serializedName;
/*    */   }
/*    */ }


/* Location:              C:\Users\karol\AppData\Roaming\ModrinthApp\meta\versions\26.2-0.19.5\26.2-0.19.5.jar!\net\minecraft\client\multiplayer\chat\LoggedChatEvent$Type.class
 * Java compiler version: 25 (69.0)
 * JD-Core Version:       1.1.3
 */