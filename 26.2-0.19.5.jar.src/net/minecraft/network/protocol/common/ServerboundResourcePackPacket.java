/*    */ package net.minecraft.network.protocol.common;
/*    */ 
/*    */ import java.util.UUID;
/*    */ import net.minecraft.network.FriendlyByteBuf;
/*    */ import net.minecraft.network.PacketListener;
/*    */ import net.minecraft.network.codec.StreamCodec;
/*    */ import net.minecraft.network.codec.StreamDecoder;
/*    */ import net.minecraft.network.protocol.Packet;
/*    */ import net.minecraft.network.protocol.PacketType;
/*    */ 
/*    */ public final class ServerboundResourcePackPacket extends Record implements Packet<ServerCommonPacketListener> {
/*    */   private final UUID id;
/*    */   
/*    */   private final Action action;
/*    */   
/*    */   public ServerboundResourcePackPacket(UUID id, Action action) {
/* 10 */     this.id = id;
/* 10 */     this.action = action;
/*    */   }
/*    */   
/*    */   public final String toString() {
/*    */     // Byte code:
/*    */     //   0: aload_0
/*    */     //   1: <illegal opcode> toString : (Lnet/minecraft/network/protocol/common/ServerboundResourcePackPacket;)Ljava/lang/String;
/*    */     //   6: areturn
/*    */     // Line number table:
/*    */     //   Java source line number -> byte code offset
/*    */     //   #10	-> 0
/*    */     // Local variable table:
/*    */     //   start	length	slot	name	descriptor
/*    */     //   0	7	0	this	Lnet/minecraft/network/protocol/common/ServerboundResourcePackPacket;
/*    */   }
/*    */   
/*    */   public final int hashCode() {
/*    */     // Byte code:
/*    */     //   0: aload_0
/*    */     //   1: <illegal opcode> hashCode : (Lnet/minecraft/network/protocol/common/ServerboundResourcePackPacket;)I
/*    */     //   6: ireturn
/*    */     // Line number table:
/*    */     //   Java source line number -> byte code offset
/*    */     //   #10	-> 0
/*    */     // Local variable table:
/*    */     //   start	length	slot	name	descriptor
/*    */     //   0	7	0	this	Lnet/minecraft/network/protocol/common/ServerboundResourcePackPacket;
/*    */   }
/*    */   
/*    */   public final boolean equals(Object o) {
/*    */     // Byte code:
/*    */     //   0: aload_0
/*    */     //   1: aload_1
/*    */     //   2: <illegal opcode> equals : (Lnet/minecraft/network/protocol/common/ServerboundResourcePackPacket;Ljava/lang/Object;)Z
/*    */     //   7: ireturn
/*    */     // Line number table:
/*    */     //   Java source line number -> byte code offset
/*    */     //   #10	-> 0
/*    */     // Local variable table:
/*    */     //   start	length	slot	name	descriptor
/*    */     //   0	8	0	this	Lnet/minecraft/network/protocol/common/ServerboundResourcePackPacket;
/*    */     //   0	8	1	o	Ljava/lang/Object;
/*    */   }
/*    */   
/*    */   public UUID id() {
/* 10 */     return this.id;
/*    */   }
/*    */   
/*    */   public Action action() {
/* 10 */     return this.action;
/*    */   }
/*    */   
/* 14 */   public static final StreamCodec<FriendlyByteBuf, ServerboundResourcePackPacket> STREAM_CODEC = Packet.codec(ServerboundResourcePackPacket::write, ServerboundResourcePackPacket::new);
/*    */   
/*    */   private ServerboundResourcePackPacket(FriendlyByteBuf input) {
/* 17 */     this(input
/* 18 */         .readUUID(), (Action)input
/* 19 */         .readEnum(Action.class));
/*    */   }
/*    */   
/*    */   private void write(FriendlyByteBuf output) {
/* 24 */     output.writeUUID(this.id);
/* 25 */     output.writeEnum(this.action);
/*    */   }
/*    */   
/*    */   public PacketType<ServerboundResourcePackPacket> type() {
/* 30 */     return CommonPacketTypes.SERVERBOUND_RESOURCE_PACK;
/*    */   }
/*    */   
/*    */   public void handle(ServerCommonPacketListener listener) {
/* 35 */     listener.handleResourcePackResponse(this);
/*    */   }
/*    */   
/*    */   public enum Action {
/* 39 */     SUCCESSFULLY_LOADED, DECLINED, FAILED_DOWNLOAD, ACCEPTED, DOWNLOADED, INVALID_URL, FAILED_RELOAD, DISCARDED;
/*    */     
/*    */     public boolean isTerminal() {
/* 50 */       return (this != ACCEPTED && this != DOWNLOADED);
/*    */     }
/*    */   }
/*    */ }


/* Location:              C:\Users\karol\AppData\Roaming\ModrinthApp\meta\versions\26.2-0.19.5\26.2-0.19.5.jar!\net\minecraft\network\protocol\common\ServerboundResourcePackPacket.class
 * Java compiler version: 25 (69.0)
 * JD-Core Version:       1.1.3
 */