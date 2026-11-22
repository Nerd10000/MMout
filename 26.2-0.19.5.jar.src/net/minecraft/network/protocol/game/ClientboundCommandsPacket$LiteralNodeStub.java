/*     */ package net.minecraft.network.protocol.game;
/*     */ 
/*     */ import com.mojang.brigadier.builder.ArgumentBuilder;
/*     */ import net.minecraft.commands.CommandBuildContext;
/*     */ import net.minecraft.network.FriendlyByteBuf;
/*     */ 
/*     */ final class LiteralNodeStub extends Record implements ClientboundCommandsPacket.NodeStub {
/*     */   private final String id;
/*     */   
/*     */   private LiteralNodeStub(String id) {
/* 139 */     this.id = id;
/*     */   }
/*     */   
/*     */   public final String toString() {
/*     */     // Byte code:
/*     */     //   0: aload_0
/*     */     //   1: <illegal opcode> toString : (Lnet/minecraft/network/protocol/game/ClientboundCommandsPacket$LiteralNodeStub;)Ljava/lang/String;
/*     */     //   6: areturn
/*     */     // Line number table:
/*     */     //   Java source line number -> byte code offset
/*     */     //   #139	-> 0
/*     */     // Local variable table:
/*     */     //   start	length	slot	name	descriptor
/*     */     //   0	7	0	this	Lnet/minecraft/network/protocol/game/ClientboundCommandsPacket$LiteralNodeStub;
/*     */   }
/*     */   
/*     */   public final int hashCode() {
/*     */     // Byte code:
/*     */     //   0: aload_0
/*     */     //   1: <illegal opcode> hashCode : (Lnet/minecraft/network/protocol/game/ClientboundCommandsPacket$LiteralNodeStub;)I
/*     */     //   6: ireturn
/*     */     // Line number table:
/*     */     //   Java source line number -> byte code offset
/*     */     //   #139	-> 0
/*     */     // Local variable table:
/*     */     //   start	length	slot	name	descriptor
/*     */     //   0	7	0	this	Lnet/minecraft/network/protocol/game/ClientboundCommandsPacket$LiteralNodeStub;
/*     */   }
/*     */   
/*     */   public final boolean equals(Object o) {
/*     */     // Byte code:
/*     */     //   0: aload_0
/*     */     //   1: aload_1
/*     */     //   2: <illegal opcode> equals : (Lnet/minecraft/network/protocol/game/ClientboundCommandsPacket$LiteralNodeStub;Ljava/lang/Object;)Z
/*     */     //   7: ireturn
/*     */     // Line number table:
/*     */     //   Java source line number -> byte code offset
/*     */     //   #139	-> 0
/*     */     // Local variable table:
/*     */     //   start	length	slot	name	descriptor
/*     */     //   0	8	0	this	Lnet/minecraft/network/protocol/game/ClientboundCommandsPacket$LiteralNodeStub;
/*     */     //   0	8	1	o	Ljava/lang/Object;
/*     */   }
/*     */   
/*     */   public String id() {
/* 139 */     return this.id;
/*     */   }
/*     */   
/*     */   public <S> ArgumentBuilder<S, ?> build(CommandBuildContext context, ClientboundCommandsPacket.NodeBuilder<S> builder) {
/* 142 */     return builder.createLiteral(this.id);
/*     */   }
/*     */   
/*     */   public void write(FriendlyByteBuf output) {
/* 147 */     output.writeUtf(this.id);
/*     */   }
/*     */ }


/* Location:              C:\Users\karol\AppData\Roaming\ModrinthApp\meta\versions\26.2-0.19.5\26.2-0.19.5.jar!\net\minecraft\network\protocol\game\ClientboundCommandsPacket$LiteralNodeStub.class
 * Java compiler version: 25 (69.0)
 * JD-Core Version:       1.1.3
 */