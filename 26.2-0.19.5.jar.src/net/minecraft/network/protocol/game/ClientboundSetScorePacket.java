/*    */ package net.minecraft.network.protocol.game;
/*    */ 
/*    */ import com.mojang.datafixers.util.Function5;
/*    */ import java.util.Optional;
/*    */ import net.minecraft.network.PacketListener;
/*    */ import net.minecraft.network.RegistryFriendlyByteBuf;
/*    */ import net.minecraft.network.chat.Component;
/*    */ import net.minecraft.network.chat.ComponentSerialization;
/*    */ import net.minecraft.network.chat.numbers.NumberFormat;
/*    */ import net.minecraft.network.chat.numbers.NumberFormatTypes;
/*    */ import net.minecraft.network.codec.ByteBufCodecs;
/*    */ import net.minecraft.network.codec.StreamCodec;
/*    */ import net.minecraft.network.protocol.Packet;
/*    */ import net.minecraft.network.protocol.PacketType;
/*    */ 
/*    */ public final class ClientboundSetScorePacket extends Record implements Packet<ClientGamePacketListener> {
/*    */   private final String owner;
/*    */   
/*    */   private final String objectiveName;
/*    */   
/*    */   private final int score;
/*    */   
/*    */   private final Optional<Component> display;
/*    */   
/*    */   private final Optional<NumberFormat> numberFormat;
/*    */   
/*    */   public ClientboundSetScorePacket(String owner, String objectiveName, int score, Optional<Component> display, Optional<NumberFormat> numberFormat) {
/* 15 */     this.owner = owner;
/* 15 */     this.objectiveName = objectiveName;
/* 15 */     this.score = score;
/* 15 */     this.display = display;
/* 15 */     this.numberFormat = numberFormat;
/*    */   }
/*    */   
/*    */   public final String toString() {
/*    */     // Byte code:
/*    */     //   0: aload_0
/*    */     //   1: <illegal opcode> toString : (Lnet/minecraft/network/protocol/game/ClientboundSetScorePacket;)Ljava/lang/String;
/*    */     //   6: areturn
/*    */     // Line number table:
/*    */     //   Java source line number -> byte code offset
/*    */     //   #15	-> 0
/*    */     // Local variable table:
/*    */     //   start	length	slot	name	descriptor
/*    */     //   0	7	0	this	Lnet/minecraft/network/protocol/game/ClientboundSetScorePacket;
/*    */   }
/*    */   
/*    */   public final int hashCode() {
/*    */     // Byte code:
/*    */     //   0: aload_0
/*    */     //   1: <illegal opcode> hashCode : (Lnet/minecraft/network/protocol/game/ClientboundSetScorePacket;)I
/*    */     //   6: ireturn
/*    */     // Line number table:
/*    */     //   Java source line number -> byte code offset
/*    */     //   #15	-> 0
/*    */     // Local variable table:
/*    */     //   start	length	slot	name	descriptor
/*    */     //   0	7	0	this	Lnet/minecraft/network/protocol/game/ClientboundSetScorePacket;
/*    */   }
/*    */   
/*    */   public final boolean equals(Object o) {
/*    */     // Byte code:
/*    */     //   0: aload_0
/*    */     //   1: aload_1
/*    */     //   2: <illegal opcode> equals : (Lnet/minecraft/network/protocol/game/ClientboundSetScorePacket;Ljava/lang/Object;)Z
/*    */     //   7: ireturn
/*    */     // Line number table:
/*    */     //   Java source line number -> byte code offset
/*    */     //   #15	-> 0
/*    */     // Local variable table:
/*    */     //   start	length	slot	name	descriptor
/*    */     //   0	8	0	this	Lnet/minecraft/network/protocol/game/ClientboundSetScorePacket;
/*    */     //   0	8	1	o	Ljava/lang/Object;
/*    */   }
/*    */   
/*    */   public String owner() {
/* 15 */     return this.owner;
/*    */   }
/*    */   
/*    */   public String objectiveName() {
/* 15 */     return this.objectiveName;
/*    */   }
/*    */   
/*    */   public int score() {
/* 15 */     return this.score;
/*    */   }
/*    */   
/*    */   public Optional<Component> display() {
/* 15 */     return this.display;
/*    */   }
/*    */   
/*    */   public Optional<NumberFormat> numberFormat() {
/* 15 */     return this.numberFormat;
/*    */   }
/*    */   
/* 22 */   public static final StreamCodec<RegistryFriendlyByteBuf, ClientboundSetScorePacket> STREAM_CODEC = StreamCodec.composite(ByteBufCodecs.STRING_UTF8, ClientboundSetScorePacket::owner, ByteBufCodecs.STRING_UTF8, ClientboundSetScorePacket::objectiveName, ByteBufCodecs.VAR_INT, ClientboundSetScorePacket::score, ComponentSerialization.TRUSTED_OPTIONAL_STREAM_CODEC, ClientboundSetScorePacket::display, NumberFormatTypes.OPTIONAL_STREAM_CODEC, ClientboundSetScorePacket::numberFormat, ClientboundSetScorePacket::new);
/*    */   
/*    */   public PacketType<ClientboundSetScorePacket> type() {
/* 33 */     return GamePacketTypes.CLIENTBOUND_SET_SCORE;
/*    */   }
/*    */   
/*    */   public void handle(ClientGamePacketListener listener) {
/* 38 */     listener.handleSetScore(this);
/*    */   }
/*    */ }


/* Location:              C:\Users\karol\AppData\Roaming\ModrinthApp\meta\versions\26.2-0.19.5\26.2-0.19.5.jar!\net\minecraft\network\protocol\game\ClientboundSetScorePacket.class
 * Java compiler version: 25 (69.0)
 * JD-Core Version:       1.1.3
 */