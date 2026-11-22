/*    */ package net.minecraft.commands.execution;
/*    */ 
/*    */ class null implements ExecutionControl<T> {
/*    */   public void queueNext(EntryAction<T> action) {
/* 19 */     context.queueNext(new CommandQueueEntry<>(frame, action));
/*    */   }
/*    */   
/*    */   public void tracer(TraceCallbacks tracer) {
/* 24 */     context.tracer(tracer);
/*    */   }
/*    */   
/*    */   public TraceCallbacks tracer() {
/* 29 */     return context.tracer();
/*    */   }
/*    */   
/*    */   public Frame currentFrame() {
/* 34 */     return frame;
/*    */   }
/*    */ }


/* Location:              C:\Users\karol\AppData\Roaming\ModrinthApp\meta\versions\26.2-0.19.5\26.2-0.19.5.jar!\net\minecraft\commands\execution\ExecutionControl$1.class
 * Java compiler version: 25 (69.0)
 * JD-Core Version:       1.1.3
 */