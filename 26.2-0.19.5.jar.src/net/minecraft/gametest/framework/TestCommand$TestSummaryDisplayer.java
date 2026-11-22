/*     */ package net.minecraft.gametest.framework;
/*     */ 
/*     */ import net.minecraft.ChatFormatting;
/*     */ import net.minecraft.commands.CommandSourceStack;
/*     */ import net.minecraft.network.chat.Component;
/*     */ 
/*     */ public final class TestSummaryDisplayer extends Record implements GameTestListener {
/*     */   private final CommandSourceStack source;
/*     */   
/*     */   private final MultipleTestTracker tracker;
/*     */   
/*     */   public TestSummaryDisplayer(CommandSourceStack source, MultipleTestTracker tracker) {
/* 501 */     this.source = source;
/* 501 */     this.tracker = tracker;
/*     */   }
/*     */   
/*     */   public final String toString() {
/*     */     // Byte code:
/*     */     //   0: aload_0
/*     */     //   1: <illegal opcode> toString : (Lnet/minecraft/gametest/framework/TestCommand$TestSummaryDisplayer;)Ljava/lang/String;
/*     */     //   6: areturn
/*     */     // Line number table:
/*     */     //   Java source line number -> byte code offset
/*     */     //   #501	-> 0
/*     */     // Local variable table:
/*     */     //   start	length	slot	name	descriptor
/*     */     //   0	7	0	this	Lnet/minecraft/gametest/framework/TestCommand$TestSummaryDisplayer;
/*     */   }
/*     */   
/*     */   public final int hashCode() {
/*     */     // Byte code:
/*     */     //   0: aload_0
/*     */     //   1: <illegal opcode> hashCode : (Lnet/minecraft/gametest/framework/TestCommand$TestSummaryDisplayer;)I
/*     */     //   6: ireturn
/*     */     // Line number table:
/*     */     //   Java source line number -> byte code offset
/*     */     //   #501	-> 0
/*     */     // Local variable table:
/*     */     //   start	length	slot	name	descriptor
/*     */     //   0	7	0	this	Lnet/minecraft/gametest/framework/TestCommand$TestSummaryDisplayer;
/*     */   }
/*     */   
/*     */   public final boolean equals(Object o) {
/*     */     // Byte code:
/*     */     //   0: aload_0
/*     */     //   1: aload_1
/*     */     //   2: <illegal opcode> equals : (Lnet/minecraft/gametest/framework/TestCommand$TestSummaryDisplayer;Ljava/lang/Object;)Z
/*     */     //   7: ireturn
/*     */     // Line number table:
/*     */     //   Java source line number -> byte code offset
/*     */     //   #501	-> 0
/*     */     // Local variable table:
/*     */     //   start	length	slot	name	descriptor
/*     */     //   0	8	0	this	Lnet/minecraft/gametest/framework/TestCommand$TestSummaryDisplayer;
/*     */     //   0	8	1	o	Ljava/lang/Object;
/*     */   }
/*     */   
/*     */   public CommandSourceStack source() {
/* 501 */     return this.source;
/*     */   }
/*     */   
/*     */   public MultipleTestTracker tracker() {
/* 501 */     return this.tracker;
/*     */   }
/*     */   
/*     */   public void testStructureLoaded(GameTestInfo testInfo) {}
/*     */   
/*     */   public void testPassed(GameTestInfo testInfo, GameTestRunner runner) {
/* 508 */     showTestSummaryIfAllDone();
/*     */   }
/*     */   
/*     */   public void testFailed(GameTestInfo testInfo, GameTestRunner runner) {
/* 513 */     showTestSummaryIfAllDone();
/*     */   }
/*     */   
/*     */   public void testAddedForRerun(GameTestInfo original, GameTestInfo copy, GameTestRunner runner) {
/* 518 */     this.tracker.addTestToTrack(copy);
/*     */   }
/*     */   
/*     */   private void showTestSummaryIfAllDone() {
/* 522 */     if (this.tracker.isDone()) {
/* 523 */       this.source.sendSuccess(() -> Component.translatable("commands.test.summary", new Object[] { Integer.valueOf(this.tracker.getTotalCount()) }).withStyle(ChatFormatting.WHITE), true);
/* 524 */       if (this.tracker.hasFailedRequired()) {
/* 525 */         this.source.sendFailure((Component)Component.translatable("commands.test.summary.failed", new Object[] { Integer.valueOf(this.tracker.getFailedRequiredCount()) }));
/*     */       } else {
/* 527 */         this.source.sendSuccess(() -> Component.translatable("commands.test.summary.all_required_passed").withStyle(ChatFormatting.GREEN), true);
/*     */       } 
/* 529 */       if (this.tracker.hasFailedOptional())
/* 530 */         this.source.sendSystemMessage((Component)Component.translatable("commands.test.summary.optional_failed", new Object[] { Integer.valueOf(this.tracker.getFailedOptionalCount()) })); 
/*     */     } 
/*     */   }
/*     */ }


/* Location:              C:\Users\karol\AppData\Roaming\ModrinthApp\meta\versions\26.2-0.19.5\26.2-0.19.5.jar!\net\minecraft\gametest\framework\TestCommand$TestSummaryDisplayer.class
 * Java compiler version: 25 (69.0)
 * JD-Core Version:       1.1.3
 */