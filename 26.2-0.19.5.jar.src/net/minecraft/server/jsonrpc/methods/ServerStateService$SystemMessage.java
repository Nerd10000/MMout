/*    */ package net.minecraft.server.jsonrpc.methods;
/*    */ 
/*    */ import com.mojang.datafixers.kinds.App;
/*    */ import com.mojang.datafixers.kinds.Applicative;
/*    */ import com.mojang.datafixers.util.Function3;
/*    */ import com.mojang.serialization.Codec;
/*    */ import com.mojang.serialization.codecs.RecordCodecBuilder;
/*    */ import java.util.List;
/*    */ import java.util.Optional;
/*    */ import net.minecraft.server.jsonrpc.api.PlayerDto;
/*    */ 
/*    */ public final class SystemMessage extends Record {
/*    */   private final Message message;
/*    */   
/*    */   private final boolean overlay;
/*    */   
/*    */   private final Optional<List<PlayerDto>> receivingPlayers;
/*    */   
/*    */   public static final Codec<SystemMessage> CODEC;
/*    */   
/*    */   public SystemMessage(Message message, boolean overlay, Optional<List<PlayerDto>> receivingPlayers) {
/* 45 */     this.message = message;
/* 45 */     this.overlay = overlay;
/* 45 */     this.receivingPlayers = receivingPlayers;
/*    */   }
/*    */   
/*    */   public final String toString() {
/*    */     // Byte code:
/*    */     //   0: aload_0
/*    */     //   1: <illegal opcode> toString : (Lnet/minecraft/server/jsonrpc/methods/ServerStateService$SystemMessage;)Ljava/lang/String;
/*    */     //   6: areturn
/*    */     // Line number table:
/*    */     //   Java source line number -> byte code offset
/*    */     //   #45	-> 0
/*    */     // Local variable table:
/*    */     //   start	length	slot	name	descriptor
/*    */     //   0	7	0	this	Lnet/minecraft/server/jsonrpc/methods/ServerStateService$SystemMessage;
/*    */   }
/*    */   
/*    */   public final int hashCode() {
/*    */     // Byte code:
/*    */     //   0: aload_0
/*    */     //   1: <illegal opcode> hashCode : (Lnet/minecraft/server/jsonrpc/methods/ServerStateService$SystemMessage;)I
/*    */     //   6: ireturn
/*    */     // Line number table:
/*    */     //   Java source line number -> byte code offset
/*    */     //   #45	-> 0
/*    */     // Local variable table:
/*    */     //   start	length	slot	name	descriptor
/*    */     //   0	7	0	this	Lnet/minecraft/server/jsonrpc/methods/ServerStateService$SystemMessage;
/*    */   }
/*    */   
/*    */   public final boolean equals(Object o) {
/*    */     // Byte code:
/*    */     //   0: aload_0
/*    */     //   1: aload_1
/*    */     //   2: <illegal opcode> equals : (Lnet/minecraft/server/jsonrpc/methods/ServerStateService$SystemMessage;Ljava/lang/Object;)Z
/*    */     //   7: ireturn
/*    */     // Line number table:
/*    */     //   Java source line number -> byte code offset
/*    */     //   #45	-> 0
/*    */     // Local variable table:
/*    */     //   start	length	slot	name	descriptor
/*    */     //   0	8	0	this	Lnet/minecraft/server/jsonrpc/methods/ServerStateService$SystemMessage;
/*    */     //   0	8	1	o	Ljava/lang/Object;
/*    */   }
/*    */   
/*    */   public Message message() {
/* 45 */     return this.message;
/*    */   }
/*    */   
/*    */   public boolean overlay() {
/* 45 */     return this.overlay;
/*    */   }
/*    */   
/*    */   public Optional<List<PlayerDto>> receivingPlayers() {
/* 45 */     return this.receivingPlayers;
/*    */   }
/*    */   
/*    */   static {
/* 46 */     CODEC = RecordCodecBuilder.create(i -> i.group((App)Message.CODEC.fieldOf("message").forGetter(SystemMessage::message), (App)Codec.BOOL.fieldOf("overlay").forGetter(SystemMessage::overlay), (App)PlayerDto.CODEC.codec().listOf().lenientOptionalFieldOf("receivingPlayers").forGetter(SystemMessage::receivingPlayers)).apply((Applicative)i, SystemMessage::new));
/*    */   }
/*    */ }


/* Location:              C:\Users\karol\AppData\Roaming\ModrinthApp\meta\versions\26.2-0.19.5\26.2-0.19.5.jar!\net\minecraft\server\jsonrpc\methods\ServerStateService$SystemMessage.class
 * Java compiler version: 25 (69.0)
 * JD-Core Version:       1.1.3
 */