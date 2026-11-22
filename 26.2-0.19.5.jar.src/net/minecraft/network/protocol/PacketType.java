/*    */ package net.minecraft.network.protocol;
/*    */ 
/*    */ import net.minecraft.resources.Identifier;
/*    */ 
/*    */ public final class PacketType<T extends Packet<?>> extends Record {
/*    */   private final PacketFlow flow;
/*    */   
/*    */   private final Identifier id;
/*    */   
/*    */   public PacketType(PacketFlow flow, Identifier id) {
/*  5 */     this.flow = flow;
/*  5 */     this.id = id;
/*    */   }
/*    */   
/*    */   public final int hashCode() {
/*    */     // Byte code:
/*    */     //   0: aload_0
/*    */     //   1: <illegal opcode> hashCode : (Lnet/minecraft/network/protocol/PacketType;)I
/*    */     //   6: ireturn
/*    */     // Line number table:
/*    */     //   Java source line number -> byte code offset
/*    */     //   #5	-> 0
/*    */     // Local variable table:
/*    */     //   start	length	slot	name	descriptor
/*    */     //   0	7	0	this	Lnet/minecraft/network/protocol/PacketType;
/*    */     // Local variable type table:
/*    */     //   start	length	slot	name	signature
/*    */     //   0	7	0	this	Lnet/minecraft/network/protocol/PacketType<TT;>;
/*    */   }
/*    */   
/*    */   public final boolean equals(Object o) {
/*    */     // Byte code:
/*    */     //   0: aload_0
/*    */     //   1: aload_1
/*    */     //   2: <illegal opcode> equals : (Lnet/minecraft/network/protocol/PacketType;Ljava/lang/Object;)Z
/*    */     //   7: ireturn
/*    */     // Line number table:
/*    */     //   Java source line number -> byte code offset
/*    */     //   #5	-> 0
/*    */     // Local variable table:
/*    */     //   start	length	slot	name	descriptor
/*    */     //   0	8	0	this	Lnet/minecraft/network/protocol/PacketType;
/*    */     //   0	8	1	o	Ljava/lang/Object;
/*    */     // Local variable type table:
/*    */     //   start	length	slot	name	signature
/*    */     //   0	8	0	this	Lnet/minecraft/network/protocol/PacketType<TT;>;
/*    */   }
/*    */   
/*    */   public PacketFlow flow() {
/*  5 */     return this.flow;
/*    */   }
/*    */   
/*    */   public Identifier id() {
/*  5 */     return this.id;
/*    */   }
/*    */   
/*    */   public String toString() {
/* 11 */     return this.flow.id() + "/" + this.flow.id();
/*    */   }
/*    */ }


/* Location:              C:\Users\karol\AppData\Roaming\ModrinthApp\meta\versions\26.2-0.19.5\26.2-0.19.5.jar!\net\minecraft\network\protocol\PacketType.class
 * Java compiler version: 25 (69.0)
 * JD-Core Version:       1.1.3
 */