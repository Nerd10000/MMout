/*    */ package net.minecraft.network.protocol.game;
/*    */ 
/*    */ import io.netty.buffer.ByteBuf;
/*    */ import java.util.function.BiFunction;
/*    */ import net.minecraft.core.registries.Registries;
/*    */ import net.minecraft.network.codec.ByteBufCodecs;
/*    */ import net.minecraft.network.codec.StreamCodec;
/*    */ import net.minecraft.resources.ResourceKey;
/*    */ import net.minecraft.world.level.gamerules.GameRule;
/*    */ 
/*    */ public final class Entry extends Record {
/*    */   private final ResourceKey<GameRule<?>> gameRuleKey;
/*    */   
/*    */   private final String value;
/*    */   
/*    */   public Entry(ResourceKey<GameRule<?>> gameRuleKey, String value) {
/* 30 */     this.gameRuleKey = gameRuleKey;
/* 30 */     this.value = value;
/*    */   }
/*    */   
/*    */   public final String toString() {
/*    */     // Byte code:
/*    */     //   0: aload_0
/*    */     //   1: <illegal opcode> toString : (Lnet/minecraft/network/protocol/game/ServerboundSetGameRulePacket$Entry;)Ljava/lang/String;
/*    */     //   6: areturn
/*    */     // Line number table:
/*    */     //   Java source line number -> byte code offset
/*    */     //   #30	-> 0
/*    */     // Local variable table:
/*    */     //   start	length	slot	name	descriptor
/*    */     //   0	7	0	this	Lnet/minecraft/network/protocol/game/ServerboundSetGameRulePacket$Entry;
/*    */   }
/*    */   
/*    */   public final int hashCode() {
/*    */     // Byte code:
/*    */     //   0: aload_0
/*    */     //   1: <illegal opcode> hashCode : (Lnet/minecraft/network/protocol/game/ServerboundSetGameRulePacket$Entry;)I
/*    */     //   6: ireturn
/*    */     // Line number table:
/*    */     //   Java source line number -> byte code offset
/*    */     //   #30	-> 0
/*    */     // Local variable table:
/*    */     //   start	length	slot	name	descriptor
/*    */     //   0	7	0	this	Lnet/minecraft/network/protocol/game/ServerboundSetGameRulePacket$Entry;
/*    */   }
/*    */   
/*    */   public final boolean equals(Object o) {
/*    */     // Byte code:
/*    */     //   0: aload_0
/*    */     //   1: aload_1
/*    */     //   2: <illegal opcode> equals : (Lnet/minecraft/network/protocol/game/ServerboundSetGameRulePacket$Entry;Ljava/lang/Object;)Z
/*    */     //   7: ireturn
/*    */     // Line number table:
/*    */     //   Java source line number -> byte code offset
/*    */     //   #30	-> 0
/*    */     // Local variable table:
/*    */     //   start	length	slot	name	descriptor
/*    */     //   0	8	0	this	Lnet/minecraft/network/protocol/game/ServerboundSetGameRulePacket$Entry;
/*    */     //   0	8	1	o	Ljava/lang/Object;
/*    */   }
/*    */   
/*    */   public ResourceKey<GameRule<?>> gameRuleKey() {
/* 30 */     return this.gameRuleKey;
/*    */   }
/*    */   
/*    */   public String value() {
/* 30 */     return this.value;
/*    */   }
/*    */   
/* 31 */   public static final StreamCodec<ByteBuf, Entry> STREAM_CODEC = StreamCodec.composite(
/* 32 */       ResourceKey.streamCodec(Registries.GAME_RULE), Entry::gameRuleKey, ByteBufCodecs.STRING_UTF8, Entry::value, Entry::new);
/*    */ }


/* Location:              C:\Users\karol\AppData\Roaming\ModrinthApp\meta\versions\26.2-0.19.5\26.2-0.19.5.jar!\net\minecraft\network\protocol\game\ServerboundSetGameRulePacket$Entry.class
 * Java compiler version: 25 (69.0)
 * JD-Core Version:       1.1.3
 */