/*     */ package net.minecraft.gametest.framework;
/*     */ 
/*     */ import net.minecraft.commands.CommandSourceStack;
/*     */ import net.minecraft.network.chat.Component;
/*     */ 
/*     */ final class TestBatchSummaryDisplayer extends Record implements GameTestBatchListener {
/*     */   private final CommandSourceStack source;
/*     */   
/*     */   private TestBatchSummaryDisplayer(CommandSourceStack source) {
/* 536 */     this.source = source;
/*     */   }
/*     */   
/*     */   public final String toString() {
/*     */     // Byte code:
/*     */     //   0: aload_0
/*     */     //   1: <illegal opcode> toString : (Lnet/minecraft/gametest/framework/TestCommand$TestBatchSummaryDisplayer;)Ljava/lang/String;
/*     */     //   6: areturn
/*     */     // Line number table:
/*     */     //   Java source line number -> byte code offset
/*     */     //   #536	-> 0
/*     */     // Local variable table:
/*     */     //   start	length	slot	name	descriptor
/*     */     //   0	7	0	this	Lnet/minecraft/gametest/framework/TestCommand$TestBatchSummaryDisplayer;
/*     */   }
/*     */   
/*     */   public final int hashCode() {
/*     */     // Byte code:
/*     */     //   0: aload_0
/*     */     //   1: <illegal opcode> hashCode : (Lnet/minecraft/gametest/framework/TestCommand$TestBatchSummaryDisplayer;)I
/*     */     //   6: ireturn
/*     */     // Line number table:
/*     */     //   Java source line number -> byte code offset
/*     */     //   #536	-> 0
/*     */     // Local variable table:
/*     */     //   start	length	slot	name	descriptor
/*     */     //   0	7	0	this	Lnet/minecraft/gametest/framework/TestCommand$TestBatchSummaryDisplayer;
/*     */   }
/*     */   
/*     */   public final boolean equals(Object o) {
/*     */     // Byte code:
/*     */     //   0: aload_0
/*     */     //   1: aload_1
/*     */     //   2: <illegal opcode> equals : (Lnet/minecraft/gametest/framework/TestCommand$TestBatchSummaryDisplayer;Ljava/lang/Object;)Z
/*     */     //   7: ireturn
/*     */     // Line number table:
/*     */     //   Java source line number -> byte code offset
/*     */     //   #536	-> 0
/*     */     // Local variable table:
/*     */     //   start	length	slot	name	descriptor
/*     */     //   0	8	0	this	Lnet/minecraft/gametest/framework/TestCommand$TestBatchSummaryDisplayer;
/*     */     //   0	8	1	o	Ljava/lang/Object;
/*     */   }
/*     */   
/*     */   public CommandSourceStack source() {
/* 536 */     return this.source;
/*     */   }
/*     */   
/*     */   public void testBatchStarting(GameTestBatch batch) {
/* 539 */     this.source.sendSuccess(() -> Component.translatable("commands.test.batch.starting", new Object[] { batch.environment().getRegisteredName(), Integer.valueOf(batch.index()) }), true);
/*     */   }
/*     */   
/*     */   public void testBatchFinished(GameTestBatch batch) {}
/*     */ }


/* Location:              C:\Users\karol\AppData\Roaming\ModrinthApp\meta\versions\26.2-0.19.5\26.2-0.19.5.jar!\net\minecraft\gametest\framework\TestCommand$TestBatchSummaryDisplayer.class
 * Java compiler version: 25 (69.0)
 * JD-Core Version:       1.1.3
 */