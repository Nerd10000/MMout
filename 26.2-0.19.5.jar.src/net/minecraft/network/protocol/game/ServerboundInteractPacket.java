/*    */ package net.minecraft.network.protocol.game;
/*    */ 
/*    */ import com.mojang.datafixers.util.Function4;
/*    */ import io.netty.buffer.ByteBuf;
/*    */ import net.minecraft.network.PacketListener;
/*    */ import net.minecraft.network.codec.ByteBufCodecs;
/*    */ import net.minecraft.network.codec.StreamCodec;
/*    */ import net.minecraft.network.protocol.Packet;
/*    */ import net.minecraft.network.protocol.PacketType;
/*    */ import net.minecraft.world.InteractionHand;
/*    */ import net.minecraft.world.phys.Vec3;
/*    */ 
/*    */ public final class ServerboundInteractPacket extends Record implements Packet<ServerGamePacketListener> {
/*    */   private final int entityId;
/*    */   
/*    */   private final InteractionHand hand;
/*    */   
/*    */   private final Vec3 location;
/*    */   
/*    */   private final boolean usingSecondaryAction;
/*    */   
/*    */   public ServerboundInteractPacket(int entityId, InteractionHand hand, Vec3 location, boolean usingSecondaryAction) {
/* 11 */     this.entityId = entityId;
/* 11 */     this.hand = hand;
/* 11 */     this.location = location;
/* 11 */     this.usingSecondaryAction = usingSecondaryAction;
/*    */   }
/*    */   
/*    */   public final String toString() {
/*    */     // Byte code:
/*    */     //   0: aload_0
/*    */     //   1: <illegal opcode> toString : (Lnet/minecraft/network/protocol/game/ServerboundInteractPacket;)Ljava/lang/String;
/*    */     //   6: areturn
/*    */     // Line number table:
/*    */     //   Java source line number -> byte code offset
/*    */     //   #11	-> 0
/*    */     // Local variable table:
/*    */     //   start	length	slot	name	descriptor
/*    */     //   0	7	0	this	Lnet/minecraft/network/protocol/game/ServerboundInteractPacket;
/*    */   }
/*    */   
/*    */   public final int hashCode() {
/*    */     // Byte code:
/*    */     //   0: aload_0
/*    */     //   1: <illegal opcode> hashCode : (Lnet/minecraft/network/protocol/game/ServerboundInteractPacket;)I
/*    */     //   6: ireturn
/*    */     // Line number table:
/*    */     //   Java source line number -> byte code offset
/*    */     //   #11	-> 0
/*    */     // Local variable table:
/*    */     //   start	length	slot	name	descriptor
/*    */     //   0	7	0	this	Lnet/minecraft/network/protocol/game/ServerboundInteractPacket;
/*    */   }
/*    */   
/*    */   public final boolean equals(Object o) {
/*    */     // Byte code:
/*    */     //   0: aload_0
/*    */     //   1: aload_1
/*    */     //   2: <illegal opcode> equals : (Lnet/minecraft/network/protocol/game/ServerboundInteractPacket;Ljava/lang/Object;)Z
/*    */     //   7: ireturn
/*    */     // Line number table:
/*    */     //   Java source line number -> byte code offset
/*    */     //   #11	-> 0
/*    */     // Local variable table:
/*    */     //   start	length	slot	name	descriptor
/*    */     //   0	8	0	this	Lnet/minecraft/network/protocol/game/ServerboundInteractPacket;
/*    */     //   0	8	1	o	Ljava/lang/Object;
/*    */   }
/*    */   
/*    */   public int entityId() {
/* 11 */     return this.entityId;
/*    */   }
/*    */   
/*    */   public InteractionHand hand() {
/* 11 */     return this.hand;
/*    */   }
/*    */   
/*    */   public Vec3 location() {
/* 11 */     return this.location;
/*    */   }
/*    */   
/*    */   public boolean usingSecondaryAction() {
/* 11 */     return this.usingSecondaryAction;
/*    */   }
/*    */   
/* 17 */   public static final StreamCodec<ByteBuf, ServerboundInteractPacket> STREAM_CODEC = StreamCodec.composite(ByteBufCodecs.VAR_INT, ServerboundInteractPacket::entityId, InteractionHand.STREAM_CODEC, ServerboundInteractPacket::hand, Vec3.LP_STREAM_CODEC, ServerboundInteractPacket::location, ByteBufCodecs.BOOL, ServerboundInteractPacket::usingSecondaryAction, ServerboundInteractPacket::new);
/*    */   
/*    */   public PacketType<ServerboundInteractPacket> type() {
/* 27 */     return GamePacketTypes.SERVERBOUND_INTERACT;
/*    */   }
/*    */   
/*    */   public void handle(ServerGamePacketListener listener) {
/* 32 */     listener.handleInteract(this);
/*    */   }
/*    */ }


/* Location:              C:\Users\karol\AppData\Roaming\ModrinthApp\meta\versions\26.2-0.19.5\26.2-0.19.5.jar!\net\minecraft\network\protocol\game\ServerboundInteractPacket.class
 * Java compiler version: 25 (69.0)
 * JD-Core Version:       1.1.3
 */