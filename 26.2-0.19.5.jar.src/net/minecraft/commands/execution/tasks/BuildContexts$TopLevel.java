/*     */ package net.minecraft.commands.execution.tasks;
/*     */ 
/*     */ import com.mojang.brigadier.context.ContextChain;
/*     */ import java.util.List;
/*     */ import net.minecraft.commands.ExecutionCommandSource;
/*     */ import net.minecraft.commands.execution.ChainModifiers;
/*     */ import net.minecraft.commands.execution.EntryAction;
/*     */ import net.minecraft.commands.execution.ExecutionContext;
/*     */ import net.minecraft.commands.execution.Frame;
/*     */ 
/*     */ public class TopLevel<T extends ExecutionCommandSource<T>> extends BuildContexts<T> implements EntryAction<T> {
/*     */   private final T source;
/*     */   
/*     */   public TopLevel(String commandInput, ContextChain<T> command, T source) {
/* 182 */     super(commandInput, command);
/* 183 */     this.source = source;
/*     */   }
/*     */   
/*     */   public void execute(ExecutionContext<T> context, Frame frame) {
/* 188 */     traceCommandStart(context, frame);
/* 189 */     execute(this.source, List.of(this.source), context, frame, ChainModifiers.DEFAULT);
/*     */   }
/*     */ }


/* Location:              C:\Users\karol\AppData\Roaming\ModrinthApp\meta\versions\26.2-0.19.5\26.2-0.19.5.jar!\net\minecraft\commands\execution\tasks\BuildContexts$TopLevel.class
 * Java compiler version: 25 (69.0)
 * JD-Core Version:       1.1.3
 */