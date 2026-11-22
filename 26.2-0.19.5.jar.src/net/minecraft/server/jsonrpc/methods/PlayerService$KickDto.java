/*    */ package net.minecraft.server.jsonrpc.methods;
/*    */ 
/*    */ import com.mojang.datafixers.kinds.App;
/*    */ import com.mojang.datafixers.kinds.Applicative;
/*    */ import com.mojang.serialization.MapCodec;
/*    */ import com.mojang.serialization.codecs.RecordCodecBuilder;
/*    */ import java.util.Optional;
/*    */ import java.util.function.BiFunction;
/*    */ import net.minecraft.server.jsonrpc.api.PlayerDto;
/*    */ 
/*    */ public final class KickDto extends Record {
/*    */   private final PlayerDto player;
/*    */   
/*    */   private final Optional<Message> message;
/*    */   
/*    */   public static final MapCodec<KickDto> CODEC;
/*    */   
/*    */   public KickDto(PlayerDto player, Optional<Message> message) {
/* 21 */     this.player = player;
/* 21 */     this.message = message;
/*    */   }
/*    */   
/*    */   public final String toString() {
/*    */     // Byte code:
/*    */     //   0: aload_0
/*    */     //   1: <illegal opcode> toString : (Lnet/minecraft/server/jsonrpc/methods/PlayerService$KickDto;)Ljava/lang/String;
/*    */     //   6: areturn
/*    */     // Line number table:
/*    */     //   Java source line number -> byte code offset
/*    */     //   #21	-> 0
/*    */     // Local variable table:
/*    */     //   start	length	slot	name	descriptor
/*    */     //   0	7	0	this	Lnet/minecraft/server/jsonrpc/methods/PlayerService$KickDto;
/*    */   }
/*    */   
/*    */   public final int hashCode() {
/*    */     // Byte code:
/*    */     //   0: aload_0
/*    */     //   1: <illegal opcode> hashCode : (Lnet/minecraft/server/jsonrpc/methods/PlayerService$KickDto;)I
/*    */     //   6: ireturn
/*    */     // Line number table:
/*    */     //   Java source line number -> byte code offset
/*    */     //   #21	-> 0
/*    */     // Local variable table:
/*    */     //   start	length	slot	name	descriptor
/*    */     //   0	7	0	this	Lnet/minecraft/server/jsonrpc/methods/PlayerService$KickDto;
/*    */   }
/*    */   
/*    */   public final boolean equals(Object o) {
/*    */     // Byte code:
/*    */     //   0: aload_0
/*    */     //   1: aload_1
/*    */     //   2: <illegal opcode> equals : (Lnet/minecraft/server/jsonrpc/methods/PlayerService$KickDto;Ljava/lang/Object;)Z
/*    */     //   7: ireturn
/*    */     // Line number table:
/*    */     //   Java source line number -> byte code offset
/*    */     //   #21	-> 0
/*    */     // Local variable table:
/*    */     //   start	length	slot	name	descriptor
/*    */     //   0	8	0	this	Lnet/minecraft/server/jsonrpc/methods/PlayerService$KickDto;
/*    */     //   0	8	1	o	Ljava/lang/Object;
/*    */   }
/*    */   
/*    */   public PlayerDto player() {
/* 21 */     return this.player;
/*    */   }
/*    */   
/*    */   public Optional<Message> message() {
/* 21 */     return this.message;
/*    */   }
/*    */   
/*    */   static {
/* 22 */     CODEC = RecordCodecBuilder.mapCodec(i -> i.group((App)PlayerDto.CODEC.codec().fieldOf("player").forGetter(KickDto::player), (App)Message.CODEC.optionalFieldOf("message").forGetter(KickDto::message)).apply((Applicative)i, KickDto::new));
/*    */   }
/*    */ }


/* Location:              C:\Users\karol\AppData\Roaming\ModrinthApp\meta\versions\26.2-0.19.5\26.2-0.19.5.jar!\net\minecraft\server\jsonrpc\methods\PlayerService$KickDto.class
 * Java compiler version: 25 (69.0)
 * JD-Core Version:       1.1.3
 */