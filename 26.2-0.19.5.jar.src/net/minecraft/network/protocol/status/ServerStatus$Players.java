/*    */ package net.minecraft.network.protocol.status;
/*    */ 
/*    */ import com.mojang.datafixers.kinds.App;
/*    */ import com.mojang.datafixers.kinds.Applicative;
/*    */ import com.mojang.datafixers.util.Function3;
/*    */ import com.mojang.serialization.Codec;
/*    */ import com.mojang.serialization.codecs.RecordCodecBuilder;
/*    */ import java.util.List;
/*    */ import net.minecraft.server.players.NameAndId;
/*    */ 
/*    */ public final class Players extends Record {
/*    */   private final int max;
/*    */   
/*    */   private final int online;
/*    */   
/*    */   private final List<NameAndId> sample;
/*    */   
/*    */   public static final Codec<Players> CODEC;
/*    */   
/*    */   public Players(int max, int online, List<NameAndId> sample) {
/* 28 */     this.max = max;
/* 28 */     this.online = online;
/* 28 */     this.sample = sample;
/*    */   }
/*    */   
/*    */   public final String toString() {
/*    */     // Byte code:
/*    */     //   0: aload_0
/*    */     //   1: <illegal opcode> toString : (Lnet/minecraft/network/protocol/status/ServerStatus$Players;)Ljava/lang/String;
/*    */     //   6: areturn
/*    */     // Line number table:
/*    */     //   Java source line number -> byte code offset
/*    */     //   #28	-> 0
/*    */     // Local variable table:
/*    */     //   start	length	slot	name	descriptor
/*    */     //   0	7	0	this	Lnet/minecraft/network/protocol/status/ServerStatus$Players;
/*    */   }
/*    */   
/*    */   public final int hashCode() {
/*    */     // Byte code:
/*    */     //   0: aload_0
/*    */     //   1: <illegal opcode> hashCode : (Lnet/minecraft/network/protocol/status/ServerStatus$Players;)I
/*    */     //   6: ireturn
/*    */     // Line number table:
/*    */     //   Java source line number -> byte code offset
/*    */     //   #28	-> 0
/*    */     // Local variable table:
/*    */     //   start	length	slot	name	descriptor
/*    */     //   0	7	0	this	Lnet/minecraft/network/protocol/status/ServerStatus$Players;
/*    */   }
/*    */   
/*    */   public final boolean equals(Object o) {
/*    */     // Byte code:
/*    */     //   0: aload_0
/*    */     //   1: aload_1
/*    */     //   2: <illegal opcode> equals : (Lnet/minecraft/network/protocol/status/ServerStatus$Players;Ljava/lang/Object;)Z
/*    */     //   7: ireturn
/*    */     // Line number table:
/*    */     //   Java source line number -> byte code offset
/*    */     //   #28	-> 0
/*    */     // Local variable table:
/*    */     //   start	length	slot	name	descriptor
/*    */     //   0	8	0	this	Lnet/minecraft/network/protocol/status/ServerStatus$Players;
/*    */     //   0	8	1	o	Ljava/lang/Object;
/*    */   }
/*    */   
/*    */   public int max() {
/* 28 */     return this.max;
/*    */   }
/*    */   
/*    */   public int online() {
/* 28 */     return this.online;
/*    */   }
/*    */   
/*    */   public List<NameAndId> sample() {
/* 28 */     return this.sample;
/*    */   }
/*    */   
/*    */   static {
/* 29 */     CODEC = RecordCodecBuilder.create(i -> i.group((App)Codec.INT.fieldOf("max").forGetter(Players::max), (App)Codec.INT.fieldOf("online").forGetter(Players::online), (App)NameAndId.CODEC.listOf().lenientOptionalFieldOf("sample", List.of()).forGetter(Players::sample)).apply((Applicative)i, Players::new));
/*    */   }
/*    */ }


/* Location:              C:\Users\karol\AppData\Roaming\ModrinthApp\meta\versions\26.2-0.19.5\26.2-0.19.5.jar!\net\minecraft\network\protocol\status\ServerStatus$Players.class
 * Java compiler version: 25 (69.0)
 * JD-Core Version:       1.1.3
 */