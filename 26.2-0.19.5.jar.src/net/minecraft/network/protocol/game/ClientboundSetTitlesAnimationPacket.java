/*    */ package net.minecraft.network.protocol.game;
/*    */ 
/*    */ import net.minecraft.network.FriendlyByteBuf;
/*    */ import net.minecraft.network.PacketListener;
/*    */ import net.minecraft.network.codec.StreamCodec;
/*    */ import net.minecraft.network.codec.StreamDecoder;
/*    */ import net.minecraft.network.protocol.Packet;
/*    */ import net.minecraft.network.protocol.PacketType;
/*    */ 
/*    */ public class ClientboundSetTitlesAnimationPacket implements Packet<ClientGamePacketListener> {
/*  9 */   public static final StreamCodec<FriendlyByteBuf, ClientboundSetTitlesAnimationPacket> STREAM_CODEC = Packet.codec(ClientboundSetTitlesAnimationPacket::write, ClientboundSetTitlesAnimationPacket::new);
/*    */   
/*    */   private final int fadeIn;
/*    */   
/*    */   private final int stay;
/*    */   
/*    */   private final int fadeOut;
/*    */   
/*    */   public ClientboundSetTitlesAnimationPacket(int fadeIn, int stay, int fadeOut) {
/* 16 */     this.fadeIn = fadeIn;
/* 17 */     this.stay = stay;
/* 18 */     this.fadeOut = fadeOut;
/*    */   }
/*    */   
/*    */   private ClientboundSetTitlesAnimationPacket(FriendlyByteBuf input) {
/* 22 */     this.fadeIn = input.readInt();
/* 23 */     this.stay = input.readInt();
/* 24 */     this.fadeOut = input.readInt();
/*    */   }
/*    */   
/*    */   private void write(FriendlyByteBuf output) {
/* 28 */     output.writeInt(this.fadeIn);
/* 29 */     output.writeInt(this.stay);
/* 30 */     output.writeInt(this.fadeOut);
/*    */   }
/*    */   
/*    */   public PacketType<ClientboundSetTitlesAnimationPacket> type() {
/* 35 */     return GamePacketTypes.CLIENTBOUND_SET_TITLES_ANIMATION;
/*    */   }
/*    */   
/*    */   public void handle(ClientGamePacketListener listener) {
/* 40 */     listener.setTitlesAnimation(this);
/*    */   }
/*    */   
/*    */   public int getFadeIn() {
/* 44 */     return this.fadeIn;
/*    */   }
/*    */   
/*    */   public int getStay() {
/* 48 */     return this.stay;
/*    */   }
/*    */   
/*    */   public int getFadeOut() {
/* 52 */     return this.fadeOut;
/*    */   }
/*    */ }


/* Location:              C:\Users\karol\AppData\Roaming\ModrinthApp\meta\versions\26.2-0.19.5\26.2-0.19.5.jar!\net\minecraft\network\protocol\game\ClientboundSetTitlesAnimationPacket.class
 * Java compiler version: 25 (69.0)
 * JD-Core Version:       1.1.3
 */