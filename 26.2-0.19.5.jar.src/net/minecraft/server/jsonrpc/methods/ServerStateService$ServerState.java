/*    */ package net.minecraft.server.jsonrpc.methods;
/*    */ 
/*    */ import com.mojang.datafixers.kinds.App;
/*    */ import com.mojang.datafixers.kinds.Applicative;
/*    */ import com.mojang.datafixers.util.Function3;
/*    */ import com.mojang.serialization.Codec;
/*    */ import com.mojang.serialization.codecs.RecordCodecBuilder;
/*    */ import java.util.List;
/*    */ import net.minecraft.network.protocol.status.ServerStatus;
/*    */ import net.minecraft.server.jsonrpc.api.PlayerDto;
/*    */ 
/*    */ public final class ServerState extends Record {
/*    */   private final boolean started;
/*    */   
/*    */   private final List<PlayerDto> players;
/*    */   
/*    */   private final ServerStatus.Version version;
/*    */   
/*    */   public static final Codec<ServerState> CODEC;
/*    */   
/*    */   public ServerState(boolean started, List<PlayerDto> players, ServerStatus.Version version) {
/* 16 */     this.started = started;
/* 16 */     this.players = players;
/* 16 */     this.version = version;
/*    */   }
/*    */   
/*    */   public final String toString() {
/*    */     // Byte code:
/*    */     //   0: aload_0
/*    */     //   1: <illegal opcode> toString : (Lnet/minecraft/server/jsonrpc/methods/ServerStateService$ServerState;)Ljava/lang/String;
/*    */     //   6: areturn
/*    */     // Line number table:
/*    */     //   Java source line number -> byte code offset
/*    */     //   #16	-> 0
/*    */     // Local variable table:
/*    */     //   start	length	slot	name	descriptor
/*    */     //   0	7	0	this	Lnet/minecraft/server/jsonrpc/methods/ServerStateService$ServerState;
/*    */   }
/*    */   
/*    */   public final int hashCode() {
/*    */     // Byte code:
/*    */     //   0: aload_0
/*    */     //   1: <illegal opcode> hashCode : (Lnet/minecraft/server/jsonrpc/methods/ServerStateService$ServerState;)I
/*    */     //   6: ireturn
/*    */     // Line number table:
/*    */     //   Java source line number -> byte code offset
/*    */     //   #16	-> 0
/*    */     // Local variable table:
/*    */     //   start	length	slot	name	descriptor
/*    */     //   0	7	0	this	Lnet/minecraft/server/jsonrpc/methods/ServerStateService$ServerState;
/*    */   }
/*    */   
/*    */   public final boolean equals(Object o) {
/*    */     // Byte code:
/*    */     //   0: aload_0
/*    */     //   1: aload_1
/*    */     //   2: <illegal opcode> equals : (Lnet/minecraft/server/jsonrpc/methods/ServerStateService$ServerState;Ljava/lang/Object;)Z
/*    */     //   7: ireturn
/*    */     // Line number table:
/*    */     //   Java source line number -> byte code offset
/*    */     //   #16	-> 0
/*    */     // Local variable table:
/*    */     //   start	length	slot	name	descriptor
/*    */     //   0	8	0	this	Lnet/minecraft/server/jsonrpc/methods/ServerStateService$ServerState;
/*    */     //   0	8	1	o	Ljava/lang/Object;
/*    */   }
/*    */   
/*    */   public boolean started() {
/* 16 */     return this.started;
/*    */   }
/*    */   
/*    */   public List<PlayerDto> players() {
/* 16 */     return this.players;
/*    */   }
/*    */   
/*    */   public ServerStatus.Version version() {
/* 16 */     return this.version;
/*    */   }
/*    */   
/*    */   static {
/* 17 */     CODEC = RecordCodecBuilder.create(i -> i.group((App)Codec.BOOL.fieldOf("started").forGetter(ServerState::started), (App)PlayerDto.CODEC.codec().listOf().lenientOptionalFieldOf("players", List.of()).forGetter(ServerState::players), (App)ServerStatus.Version.CODEC.fieldOf("version").forGetter(ServerState::version)).apply((Applicative)i, ServerState::new));
/*    */   }
/*    */   
/* 23 */   public static final ServerState NOT_STARTED = new ServerState(false, List.of(), ServerStatus.Version.current());
/*    */ }


/* Location:              C:\Users\karol\AppData\Roaming\ModrinthApp\meta\versions\26.2-0.19.5\26.2-0.19.5.jar!\net\minecraft\server\jsonrpc\methods\ServerStateService$ServerState.class
 * Java compiler version: 25 (69.0)
 * JD-Core Version:       1.1.3
 */