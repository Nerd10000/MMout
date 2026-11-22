/*    */ package net.minecraft.commands.execution;
/*    */ 
/*    */ import net.minecraft.commands.CommandResultCallback;
/*    */ 
/*    */ public final class Frame extends Record {
/*    */   private final int depth;
/*    */   
/*    */   private final CommandResultCallback returnValueConsumer;
/*    */   
/*    */   private final FrameControl frameControl;
/*    */   
/*    */   public Frame(int depth, CommandResultCallback returnValueConsumer, FrameControl frameControl) {
/*  5 */     this.depth = depth;
/*  5 */     this.returnValueConsumer = returnValueConsumer;
/*  5 */     this.frameControl = frameControl;
/*    */   }
/*    */   
/*    */   public final String toString() {
/*    */     // Byte code:
/*    */     //   0: aload_0
/*    */     //   1: <illegal opcode> toString : (Lnet/minecraft/commands/execution/Frame;)Ljava/lang/String;
/*    */     //   6: areturn
/*    */     // Line number table:
/*    */     //   Java source line number -> byte code offset
/*    */     //   #5	-> 0
/*    */     // Local variable table:
/*    */     //   start	length	slot	name	descriptor
/*    */     //   0	7	0	this	Lnet/minecraft/commands/execution/Frame;
/*    */   }
/*    */   
/*    */   public final int hashCode() {
/*    */     // Byte code:
/*    */     //   0: aload_0
/*    */     //   1: <illegal opcode> hashCode : (Lnet/minecraft/commands/execution/Frame;)I
/*    */     //   6: ireturn
/*    */     // Line number table:
/*    */     //   Java source line number -> byte code offset
/*    */     //   #5	-> 0
/*    */     // Local variable table:
/*    */     //   start	length	slot	name	descriptor
/*    */     //   0	7	0	this	Lnet/minecraft/commands/execution/Frame;
/*    */   }
/*    */   
/*    */   public final boolean equals(Object o) {
/*    */     // Byte code:
/*    */     //   0: aload_0
/*    */     //   1: aload_1
/*    */     //   2: <illegal opcode> equals : (Lnet/minecraft/commands/execution/Frame;Ljava/lang/Object;)Z
/*    */     //   7: ireturn
/*    */     // Line number table:
/*    */     //   Java source line number -> byte code offset
/*    */     //   #5	-> 0
/*    */     // Local variable table:
/*    */     //   start	length	slot	name	descriptor
/*    */     //   0	8	0	this	Lnet/minecraft/commands/execution/Frame;
/*    */     //   0	8	1	o	Ljava/lang/Object;
/*    */   }
/*    */   
/*    */   public int depth() {
/*  5 */     return this.depth;
/*    */   }
/*    */   
/*    */   public CommandResultCallback returnValueConsumer() {
/*  5 */     return this.returnValueConsumer;
/*    */   }
/*    */   
/*    */   public FrameControl frameControl() {
/*  5 */     return this.frameControl;
/*    */   }
/*    */   
/*    */   public void returnSuccess(int value) {
/* 11 */     this.returnValueConsumer.onSuccess(value);
/*    */   }
/*    */   
/*    */   public void returnFailure() {
/* 15 */     this.returnValueConsumer.onFailure();
/*    */   }
/*    */   
/*    */   public void discard() {
/* 19 */     this.frameControl.discard();
/*    */   }
/*    */   
/*    */   @FunctionalInterface
/*    */   public static interface FrameControl {
/*    */     void discard();
/*    */   }
/*    */ }


/* Location:              C:\Users\karol\AppData\Roaming\ModrinthApp\meta\versions\26.2-0.19.5\26.2-0.19.5.jar!\net\minecraft\commands\execution\Frame.class
 * Java compiler version: 25 (69.0)
 * JD-Core Version:       1.1.3
 */