/*     */ package net.minecraft.network.protocol.game;
/*     */ 
/*     */ import it.unimi.dsi.fastutil.ints.IntSet;
/*     */ import net.minecraft.network.FriendlyByteBuf;
/*     */ 
/*     */ final class Entry extends Record {
/*     */   private final ClientboundCommandsPacket.NodeStub stub;
/*     */   
/*     */   private final int flags;
/*     */   
/*     */   private final int redirect;
/*     */   
/*     */   private final int[] children;
/*     */   
/*     */   private Entry(ClientboundCommandsPacket.NodeStub stub, int flags, int redirect, int[] children) {
/* 259 */     this.stub = stub;
/* 259 */     this.flags = flags;
/* 259 */     this.redirect = redirect;
/* 259 */     this.children = children;
/*     */   }
/*     */   
/*     */   public final String toString() {
/*     */     // Byte code:
/*     */     //   0: aload_0
/*     */     //   1: <illegal opcode> toString : (Lnet/minecraft/network/protocol/game/ClientboundCommandsPacket$Entry;)Ljava/lang/String;
/*     */     //   6: areturn
/*     */     // Line number table:
/*     */     //   Java source line number -> byte code offset
/*     */     //   #259	-> 0
/*     */     // Local variable table:
/*     */     //   start	length	slot	name	descriptor
/*     */     //   0	7	0	this	Lnet/minecraft/network/protocol/game/ClientboundCommandsPacket$Entry;
/*     */   }
/*     */   
/*     */   public final int hashCode() {
/*     */     // Byte code:
/*     */     //   0: aload_0
/*     */     //   1: <illegal opcode> hashCode : (Lnet/minecraft/network/protocol/game/ClientboundCommandsPacket$Entry;)I
/*     */     //   6: ireturn
/*     */     // Line number table:
/*     */     //   Java source line number -> byte code offset
/*     */     //   #259	-> 0
/*     */     // Local variable table:
/*     */     //   start	length	slot	name	descriptor
/*     */     //   0	7	0	this	Lnet/minecraft/network/protocol/game/ClientboundCommandsPacket$Entry;
/*     */   }
/*     */   
/*     */   public final boolean equals(Object o) {
/*     */     // Byte code:
/*     */     //   0: aload_0
/*     */     //   1: aload_1
/*     */     //   2: <illegal opcode> equals : (Lnet/minecraft/network/protocol/game/ClientboundCommandsPacket$Entry;Ljava/lang/Object;)Z
/*     */     //   7: ireturn
/*     */     // Line number table:
/*     */     //   Java source line number -> byte code offset
/*     */     //   #259	-> 0
/*     */     // Local variable table:
/*     */     //   start	length	slot	name	descriptor
/*     */     //   0	8	0	this	Lnet/minecraft/network/protocol/game/ClientboundCommandsPacket$Entry;
/*     */     //   0	8	1	o	Ljava/lang/Object;
/*     */   }
/*     */   
/*     */   public ClientboundCommandsPacket.NodeStub stub() {
/* 259 */     return this.stub;
/*     */   }
/*     */   
/*     */   public int flags() {
/* 259 */     return this.flags;
/*     */   }
/*     */   
/*     */   public int redirect() {
/* 259 */     return this.redirect;
/*     */   }
/*     */   
/*     */   public int[] children() {
/* 259 */     return this.children;
/*     */   }
/*     */   
/*     */   public void write(FriendlyByteBuf output) {
/* 266 */     output.writeByte(this.flags);
/* 267 */     output.writeVarIntArray(this.children);
/* 268 */     if ((this.flags & 0x8) != 0)
/* 269 */       output.writeVarInt(this.redirect); 
/* 271 */     if (this.stub != null)
/* 272 */       this.stub.write(output); 
/*     */   }
/*     */   
/*     */   public boolean canBuild(IntSet unbuiltNodes) {
/* 277 */     if ((this.flags & 0x8) != 0)
/* 278 */       return !unbuiltNodes.contains(this.redirect); 
/* 280 */     return true;
/*     */   }
/*     */   
/*     */   public boolean canResolve(IntSet unresolvedNodes) {
/* 284 */     for (int child : this.children) {
/* 285 */       if (unresolvedNodes.contains(child))
/* 286 */         return false; 
/*     */     } 
/* 289 */     return true;
/*     */   }
/*     */ }


/* Location:              C:\Users\karol\AppData\Roaming\ModrinthApp\meta\versions\26.2-0.19.5\26.2-0.19.5.jar!\net\minecraft\network\protocol\game\ClientboundCommandsPacket$Entry.class
 * Java compiler version: 25 (69.0)
 * JD-Core Version:       1.1.3
 */