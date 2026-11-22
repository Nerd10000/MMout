/*    */ package net.minecraft.network.protocol.game;
/*    */ 
/*    */ import com.mojang.datafixers.util.Function4;
/*    */ import net.minecraft.network.FriendlyByteBuf;
/*    */ import net.minecraft.network.PacketListener;
/*    */ import net.minecraft.network.codec.ByteBufCodecs;
/*    */ import net.minecraft.network.codec.StreamCodec;
/*    */ import net.minecraft.network.protocol.Packet;
/*    */ import net.minecraft.network.protocol.PacketType;
/*    */ 
/*    */ public final class ClientboundPlayerRotationPacket extends Record implements Packet<ClientGamePacketListener> {
/*    */   private final float yRot;
/*    */   
/*    */   private final boolean relativeY;
/*    */   
/*    */   private final float xRot;
/*    */   
/*    */   private final boolean relativeX;
/*    */   
/*    */   public ClientboundPlayerRotationPacket(float yRot, boolean relativeY, float xRot, boolean relativeX) {
/*  9 */     this.yRot = yRot;
/*  9 */     this.relativeY = relativeY;
/*  9 */     this.xRot = xRot;
/*  9 */     this.relativeX = relativeX;
/*    */   }
/*    */   
/*    */   public final String toString() {
/*    */     // Byte code:
/*    */     //   0: aload_0
/*    */     //   1: <illegal opcode> toString : (Lnet/minecraft/network/protocol/game/ClientboundPlayerRotationPacket;)Ljava/lang/String;
/*    */     //   6: areturn
/*    */     // Line number table:
/*    */     //   Java source line number -> byte code offset
/*    */     //   #9	-> 0
/*    */     // Local variable table:
/*    */     //   start	length	slot	name	descriptor
/*    */     //   0	7	0	this	Lnet/minecraft/network/protocol/game/ClientboundPlayerRotationPacket;
/*    */   }
/*    */   
/*    */   public final int hashCode() {
/*    */     // Byte code:
/*    */     //   0: aload_0
/*    */     //   1: <illegal opcode> hashCode : (Lnet/minecraft/network/protocol/game/ClientboundPlayerRotationPacket;)I
/*    */     //   6: ireturn
/*    */     // Line number table:
/*    */     //   Java source line number -> byte code offset
/*    */     //   #9	-> 0
/*    */     // Local variable table:
/*    */     //   start	length	slot	name	descriptor
/*    */     //   0	7	0	this	Lnet/minecraft/network/protocol/game/ClientboundPlayerRotationPacket;
/*    */   }
/*    */   
/*    */   public final boolean equals(Object o) {
/*    */     // Byte code:
/*    */     //   0: aload_0
/*    */     //   1: aload_1
/*    */     //   2: <illegal opcode> equals : (Lnet/minecraft/network/protocol/game/ClientboundPlayerRotationPacket;Ljava/lang/Object;)Z
/*    */     //   7: ireturn
/*    */     // Line number table:
/*    */     //   Java source line number -> byte code offset
/*    */     //   #9	-> 0
/*    */     // Local variable table:
/*    */     //   start	length	slot	name	descriptor
/*    */     //   0	8	0	this	Lnet/minecraft/network/protocol/game/ClientboundPlayerRotationPacket;
/*    */     //   0	8	1	o	Ljava/lang/Object;
/*    */   }
/*    */   
/*    */   public float yRot() {
/*  9 */     return this.yRot;
/*    */   }
/*    */   
/*    */   public boolean relativeY() {
/*  9 */     return this.relativeY;
/*    */   }
/*    */   
/*    */   public float xRot() {
/*  9 */     return this.xRot;
/*    */   }
/*    */   
/*    */   public boolean relativeX() {
/*  9 */     return this.relativeX;
/*    */   }
/*    */   
/* 10 */   public static final StreamCodec<FriendlyByteBuf, ClientboundPlayerRotationPacket> STREAM_CODEC = StreamCodec.composite(ByteBufCodecs.FLOAT, ClientboundPlayerRotationPacket::yRot, ByteBufCodecs.BOOL, ClientboundPlayerRotationPacket::relativeY, ByteBufCodecs.FLOAT, ClientboundPlayerRotationPacket::xRot, ByteBufCodecs.BOOL, ClientboundPlayerRotationPacket::relativeX, ClientboundPlayerRotationPacket::new);
/*    */   
/*    */   public PacketType<ClientboundPlayerRotationPacket> type() {
/* 20 */     return GamePacketTypes.CLIENTBOUND_PLAYER_ROTATION;
/*    */   }
/*    */   
/*    */   public void handle(ClientGamePacketListener listener) {
/* 25 */     listener.handleRotatePlayer(this);
/*    */   }
/*    */ }


/* Location:              C:\Users\karol\AppData\Roaming\ModrinthApp\meta\versions\26.2-0.19.5\26.2-0.19.5.jar!\net\minecraft\network\protocol\game\ClientboundPlayerRotationPacket.class
 * Java compiler version: 25 (69.0)
 * JD-Core Version:       1.1.3
 */