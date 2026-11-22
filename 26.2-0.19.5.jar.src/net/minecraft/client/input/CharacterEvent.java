/*    */ package net.minecraft.client.input;
/*    */ 
/*    */ import net.minecraft.util.StringUtil;
/*    */ 
/*    */ public final class CharacterEvent extends Record {
/*    */   private final int codepoint;
/*    */   
/*    */   public CharacterEvent(int codepoint) {
/*  7 */     this.codepoint = codepoint;
/*    */   }
/*    */   
/*    */   public final String toString() {
/*    */     // Byte code:
/*    */     //   0: aload_0
/*    */     //   1: <illegal opcode> toString : (Lnet/minecraft/client/input/CharacterEvent;)Ljava/lang/String;
/*    */     //   6: areturn
/*    */     // Line number table:
/*    */     //   Java source line number -> byte code offset
/*    */     //   #7	-> 0
/*    */     // Local variable table:
/*    */     //   start	length	slot	name	descriptor
/*    */     //   0	7	0	this	Lnet/minecraft/client/input/CharacterEvent;
/*    */   }
/*    */   
/*    */   public final int hashCode() {
/*    */     // Byte code:
/*    */     //   0: aload_0
/*    */     //   1: <illegal opcode> hashCode : (Lnet/minecraft/client/input/CharacterEvent;)I
/*    */     //   6: ireturn
/*    */     // Line number table:
/*    */     //   Java source line number -> byte code offset
/*    */     //   #7	-> 0
/*    */     // Local variable table:
/*    */     //   start	length	slot	name	descriptor
/*    */     //   0	7	0	this	Lnet/minecraft/client/input/CharacterEvent;
/*    */   }
/*    */   
/*    */   public final boolean equals(Object o) {
/*    */     // Byte code:
/*    */     //   0: aload_0
/*    */     //   1: aload_1
/*    */     //   2: <illegal opcode> equals : (Lnet/minecraft/client/input/CharacterEvent;Ljava/lang/Object;)Z
/*    */     //   7: ireturn
/*    */     // Line number table:
/*    */     //   Java source line number -> byte code offset
/*    */     //   #7	-> 0
/*    */     // Local variable table:
/*    */     //   start	length	slot	name	descriptor
/*    */     //   0	8	0	this	Lnet/minecraft/client/input/CharacterEvent;
/*    */     //   0	8	1	o	Ljava/lang/Object;
/*    */   }
/*    */   
/*    */   public int codepoint() {
/*  7 */     return this.codepoint;
/*    */   }
/*    */   
/*    */   public String codepointAsString() {
/* 11 */     return Character.toString(this.codepoint);
/*    */   }
/*    */   
/*    */   public boolean isAllowedChatCharacter() {
/* 15 */     return StringUtil.isAllowedChatCharacter(this.codepoint);
/*    */   }
/*    */ }


/* Location:              C:\Users\karol\AppData\Roaming\ModrinthApp\meta\versions\26.2-0.19.5\26.2-0.19.5.jar!\net\minecraft\client\input\CharacterEvent.class
 * Java compiler version: 25 (69.0)
 * JD-Core Version:       1.1.3
 */