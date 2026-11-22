/*    */ package net.minecraft.network.protocol.game;
/*    */ 
/*    */ import io.netty.buffer.ByteBuf;
/*    */ import java.util.List;
/*    */ import java.util.function.BiFunction;
/*    */ import java.util.function.Function;
/*    */ import net.minecraft.core.registries.Registries;
/*    */ import net.minecraft.network.PacketListener;
/*    */ import net.minecraft.network.codec.ByteBufCodecs;
/*    */ import net.minecraft.network.codec.StreamCodec;
/*    */ import net.minecraft.network.protocol.Packet;
/*    */ import net.minecraft.network.protocol.PacketType;
/*    */ import net.minecraft.resources.ResourceKey;
/*    */ import net.minecraft.world.level.gamerules.GameRule;
/*    */ 
/*    */ public final class ServerboundSetGameRulePacket extends Record implements Packet<ServerGamePacketListener> {
/*    */   private final List<Entry> entries;
/*    */   
/*    */   public ServerboundSetGameRulePacket(List<Entry> entries) {
/* 14 */     this.entries = entries;
/*    */   }
/*    */   
/*    */   public final String toString() {
/*    */     // Byte code:
/*    */     //   0: aload_0
/*    */     //   1: <illegal opcode> toString : (Lnet/minecraft/network/protocol/game/ServerboundSetGameRulePacket;)Ljava/lang/String;
/*    */     //   6: areturn
/*    */     // Line number table:
/*    */     //   Java source line number -> byte code offset
/*    */     //   #14	-> 0
/*    */     // Local variable table:
/*    */     //   start	length	slot	name	descriptor
/*    */     //   0	7	0	this	Lnet/minecraft/network/protocol/game/ServerboundSetGameRulePacket;
/*    */   }
/*    */   
/*    */   public final int hashCode() {
/*    */     // Byte code:
/*    */     //   0: aload_0
/*    */     //   1: <illegal opcode> hashCode : (Lnet/minecraft/network/protocol/game/ServerboundSetGameRulePacket;)I
/*    */     //   6: ireturn
/*    */     // Line number table:
/*    */     //   Java source line number -> byte code offset
/*    */     //   #14	-> 0
/*    */     // Local variable table:
/*    */     //   start	length	slot	name	descriptor
/*    */     //   0	7	0	this	Lnet/minecraft/network/protocol/game/ServerboundSetGameRulePacket;
/*    */   }
/*    */   
/*    */   public final boolean equals(Object o) {
/*    */     // Byte code:
/*    */     //   0: aload_0
/*    */     //   1: aload_1
/*    */     //   2: <illegal opcode> equals : (Lnet/minecraft/network/protocol/game/ServerboundSetGameRulePacket;Ljava/lang/Object;)Z
/*    */     //   7: ireturn
/*    */     // Line number table:
/*    */     //   Java source line number -> byte code offset
/*    */     //   #14	-> 0
/*    */     // Local variable table:
/*    */     //   start	length	slot	name	descriptor
/*    */     //   0	8	0	this	Lnet/minecraft/network/protocol/game/ServerboundSetGameRulePacket;
/*    */     //   0	8	1	o	Ljava/lang/Object;
/*    */   }
/*    */   
/*    */   public List<Entry> entries() {
/* 14 */     return this.entries;
/*    */   }
/*    */   
/* 15 */   public static final StreamCodec<ByteBuf, ServerboundSetGameRulePacket> STREAM_CODEC = StreamCodec.composite(Entry.STREAM_CODEC
/* 16 */       .apply(ByteBufCodecs.list()), ServerboundSetGameRulePacket::entries, ServerboundSetGameRulePacket::new);
/*    */   
/*    */   public PacketType<ServerboundSetGameRulePacket> type() {
/* 22 */     return GamePacketTypes.SERVERBOUND_SET_GAME_RULE;
/*    */   }
/*    */   
/*    */   public void handle(ServerGamePacketListener listener) {
/* 27 */     listener.handleSetGameRule(this);
/*    */   }
/*    */   
/*    */   public static final class Entry extends Record {
/*    */     private final ResourceKey<GameRule<?>> gameRuleKey;
/*    */     
/*    */     private final String value;
/*    */     
/*    */     public Entry(ResourceKey<GameRule<?>> gameRuleKey, String value) {
/* 30 */       this.gameRuleKey = gameRuleKey;
/* 30 */       this.value = value;
/*    */     }
/*    */     
/*    */     public final String toString() {
/*    */       // Byte code:
/*    */       //   0: aload_0
/*    */       //   1: <illegal opcode> toString : (Lnet/minecraft/network/protocol/game/ServerboundSetGameRulePacket$Entry;)Ljava/lang/String;
/*    */       //   6: areturn
/*    */       // Line number table:
/*    */       //   Java source line number -> byte code offset
/*    */       //   #30	-> 0
/*    */       // Local variable table:
/*    */       //   start	length	slot	name	descriptor
/*    */       //   0	7	0	this	Lnet/minecraft/network/protocol/game/ServerboundSetGameRulePacket$Entry;
/*    */     }
/*    */     
/*    */     public final int hashCode() {
/*    */       // Byte code:
/*    */       //   0: aload_0
/*    */       //   1: <illegal opcode> hashCode : (Lnet/minecraft/network/protocol/game/ServerboundSetGameRulePacket$Entry;)I
/*    */       //   6: ireturn
/*    */       // Line number table:
/*    */       //   Java source line number -> byte code offset
/*    */       //   #30	-> 0
/*    */       // Local variable table:
/*    */       //   start	length	slot	name	descriptor
/*    */       //   0	7	0	this	Lnet/minecraft/network/protocol/game/ServerboundSetGameRulePacket$Entry;
/*    */     }
/*    */     
/*    */     public final boolean equals(Object o) {
/*    */       // Byte code:
/*    */       //   0: aload_0
/*    */       //   1: aload_1
/*    */       //   2: <illegal opcode> equals : (Lnet/minecraft/network/protocol/game/ServerboundSetGameRulePacket$Entry;Ljava/lang/Object;)Z
/*    */       //   7: ireturn
/*    */       // Line number table:
/*    */       //   Java source line number -> byte code offset
/*    */       //   #30	-> 0
/*    */       // Local variable table:
/*    */       //   start	length	slot	name	descriptor
/*    */       //   0	8	0	this	Lnet/minecraft/network/protocol/game/ServerboundSetGameRulePacket$Entry;
/*    */       //   0	8	1	o	Ljava/lang/Object;
/*    */     }
/*    */     
/*    */     public ResourceKey<GameRule<?>> gameRuleKey() {
/* 30 */       return this.gameRuleKey;
/*    */     }
/*    */     
/*    */     public String value() {
/* 30 */       return this.value;
/*    */     }
/*    */     
/* 31 */     public static final StreamCodec<ByteBuf, Entry> STREAM_CODEC = StreamCodec.composite(
/* 32 */         ResourceKey.streamCodec(Registries.GAME_RULE), Entry::gameRuleKey, ByteBufCodecs.STRING_UTF8, Entry::value, Entry::new);
/*    */   }
/*    */ }


/* Location:              C:\Users\karol\AppData\Roaming\ModrinthApp\meta\versions\26.2-0.19.5\26.2-0.19.5.jar!\net\minecraft\network\protocol\game\ServerboundSetGameRulePacket.class
 * Java compiler version: 25 (69.0)
 * JD-Core Version:       1.1.3
 */