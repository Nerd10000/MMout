/*    */ package net.minecraft.commands.execution;
/*    */ 
/*    */ public final class ChainModifiers extends Record {
/*    */   private final byte flags;
/*    */   
/*    */   public ChainModifiers(byte flags) {
/*  3 */     this.flags = flags;
/*    */   }
/*    */   
/*    */   public final String toString() {
/*    */     // Byte code:
/*    */     //   0: aload_0
/*    */     //   1: <illegal opcode> toString : (Lnet/minecraft/commands/execution/ChainModifiers;)Ljava/lang/String;
/*    */     //   6: areturn
/*    */     // Line number table:
/*    */     //   Java source line number -> byte code offset
/*    */     //   #3	-> 0
/*    */     // Local variable table:
/*    */     //   start	length	slot	name	descriptor
/*    */     //   0	7	0	this	Lnet/minecraft/commands/execution/ChainModifiers;
/*    */   }
/*    */   
/*    */   public final int hashCode() {
/*    */     // Byte code:
/*    */     //   0: aload_0
/*    */     //   1: <illegal opcode> hashCode : (Lnet/minecraft/commands/execution/ChainModifiers;)I
/*    */     //   6: ireturn
/*    */     // Line number table:
/*    */     //   Java source line number -> byte code offset
/*    */     //   #3	-> 0
/*    */     // Local variable table:
/*    */     //   start	length	slot	name	descriptor
/*    */     //   0	7	0	this	Lnet/minecraft/commands/execution/ChainModifiers;
/*    */   }
/*    */   
/*    */   public final boolean equals(Object o) {
/*    */     // Byte code:
/*    */     //   0: aload_0
/*    */     //   1: aload_1
/*    */     //   2: <illegal opcode> equals : (Lnet/minecraft/commands/execution/ChainModifiers;Ljava/lang/Object;)Z
/*    */     //   7: ireturn
/*    */     // Line number table:
/*    */     //   Java source line number -> byte code offset
/*    */     //   #3	-> 0
/*    */     // Local variable table:
/*    */     //   start	length	slot	name	descriptor
/*    */     //   0	8	0	this	Lnet/minecraft/commands/execution/ChainModifiers;
/*    */     //   0	8	1	o	Ljava/lang/Object;
/*    */   }
/*    */   
/*    */   public byte flags() {
/*  3 */     return this.flags;
/*    */   }
/*    */   
/*  4 */   public static final ChainModifiers DEFAULT = new ChainModifiers((byte)0);
/*    */   
/*    */   private static final byte FLAG_FORKED = 1;
/*    */   
/*    */   private static final byte FLAG_IS_RETURN = 2;
/*    */   
/*    */   private ChainModifiers setFlag(byte flag) {
/* 10 */     int newFlags = this.flags | flag;
/* 11 */     return (newFlags != this.flags) ? new ChainModifiers((byte)newFlags) : this;
/*    */   }
/*    */   
/*    */   public boolean isForked() {
/* 16 */     return ((this.flags & 0x1) != 0);
/*    */   }
/*    */   
/*    */   public ChainModifiers setForked() {
/* 20 */     return setFlag((byte)1);
/*    */   }
/*    */   
/*    */   public boolean isReturn() {
/* 32 */     return ((this.flags & 0x2) != 0);
/*    */   }
/*    */   
/*    */   public ChainModifiers setReturn() {
/* 36 */     return setFlag((byte)2);
/*    */   }
/*    */ }


/* Location:              C:\Users\karol\AppData\Roaming\ModrinthApp\meta\versions\26.2-0.19.5\26.2-0.19.5.jar!\net\minecraft\commands\execution\ChainModifiers.class
 * Java compiler version: 25 (69.0)
 * JD-Core Version:       1.1.3
 */