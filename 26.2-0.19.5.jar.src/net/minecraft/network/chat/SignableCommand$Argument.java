/*    */ package net.minecraft.network.chat;
/*    */ 
/*    */ import com.mojang.brigadier.tree.ArgumentCommandNode;
/*    */ 
/*    */ public final class Argument<S> extends Record {
/*    */   private final ArgumentCommandNode<S, ?> node;
/*    */   
/*    */   private final String value;
/*    */   
/*    */   public Argument(ArgumentCommandNode<S, ?> node, String value) {
/* 44 */     this.node = node;
/* 44 */     this.value = value;
/*    */   }
/*    */   
/*    */   public final String toString() {
/*    */     // Byte code:
/*    */     //   0: aload_0
/*    */     //   1: <illegal opcode> toString : (Lnet/minecraft/network/chat/SignableCommand$Argument;)Ljava/lang/String;
/*    */     //   6: areturn
/*    */     // Line number table:
/*    */     //   Java source line number -> byte code offset
/*    */     //   #44	-> 0
/*    */     // Local variable table:
/*    */     //   start	length	slot	name	descriptor
/*    */     //   0	7	0	this	Lnet/minecraft/network/chat/SignableCommand$Argument;
/*    */     // Local variable type table:
/*    */     //   start	length	slot	name	signature
/*    */     //   0	7	0	this	Lnet/minecraft/network/chat/SignableCommand$Argument<TS;>;
/*    */   }
/*    */   
/*    */   public final int hashCode() {
/*    */     // Byte code:
/*    */     //   0: aload_0
/*    */     //   1: <illegal opcode> hashCode : (Lnet/minecraft/network/chat/SignableCommand$Argument;)I
/*    */     //   6: ireturn
/*    */     // Line number table:
/*    */     //   Java source line number -> byte code offset
/*    */     //   #44	-> 0
/*    */     // Local variable table:
/*    */     //   start	length	slot	name	descriptor
/*    */     //   0	7	0	this	Lnet/minecraft/network/chat/SignableCommand$Argument;
/*    */     // Local variable type table:
/*    */     //   start	length	slot	name	signature
/*    */     //   0	7	0	this	Lnet/minecraft/network/chat/SignableCommand$Argument<TS;>;
/*    */   }
/*    */   
/*    */   public final boolean equals(Object o) {
/*    */     // Byte code:
/*    */     //   0: aload_0
/*    */     //   1: aload_1
/*    */     //   2: <illegal opcode> equals : (Lnet/minecraft/network/chat/SignableCommand$Argument;Ljava/lang/Object;)Z
/*    */     //   7: ireturn
/*    */     // Line number table:
/*    */     //   Java source line number -> byte code offset
/*    */     //   #44	-> 0
/*    */     // Local variable table:
/*    */     //   start	length	slot	name	descriptor
/*    */     //   0	8	0	this	Lnet/minecraft/network/chat/SignableCommand$Argument;
/*    */     //   0	8	1	o	Ljava/lang/Object;
/*    */     // Local variable type table:
/*    */     //   start	length	slot	name	signature
/*    */     //   0	8	0	this	Lnet/minecraft/network/chat/SignableCommand$Argument<TS;>;
/*    */   }
/*    */   
/*    */   public ArgumentCommandNode<S, ?> node() {
/* 44 */     return this.node;
/*    */   }
/*    */   
/*    */   public String value() {
/* 44 */     return this.value;
/*    */   }
/*    */   
/*    */   public String name() {
/* 46 */     return this.node.getName();
/*    */   }
/*    */ }


/* Location:              C:\Users\karol\AppData\Roaming\ModrinthApp\meta\versions\26.2-0.19.5\26.2-0.19.5.jar!\net\minecraft\network\chat\SignableCommand$Argument.class
 * Java compiler version: 25 (69.0)
 * JD-Core Version:       1.1.3
 */