/*     */ package net.minecraft.commands.execution.tasks;
/*     */ 
/*     */ import com.mojang.brigadier.context.ContextChain;
/*     */ import java.util.List;
/*     */ import net.minecraft.commands.ExecutionCommandSource;
/*     */ import net.minecraft.commands.execution.ChainModifiers;
/*     */ import net.minecraft.commands.execution.ExecutionContext;
/*     */ import net.minecraft.commands.execution.Frame;
/*     */ import net.minecraft.commands.execution.UnboundEntryAction;
/*     */ 
/*     */ public class Unbound<T extends ExecutionCommandSource<T>> extends BuildContexts<T> implements UnboundEntryAction<T> {
/*     */   public Unbound(String commandInput, ContextChain<T> command) {
/* 150 */     super(commandInput, command);
/*     */   }
/*     */   
/*     */   public void execute(T sender, ExecutionContext<T> context, Frame frame) {
/* 155 */     traceCommandStart(context, frame);
/* 156 */     execute(sender, List.of(sender), context, frame, ChainModifiers.DEFAULT);
/*     */   }
/*     */ }


/* Location:              C:\Users\karol\AppData\Roaming\ModrinthApp\meta\versions\26.2-0.19.5\26.2-0.19.5.jar!\net\minecraft\commands\execution\tasks\BuildContexts$Unbound.class
 * Java compiler version: 25 (69.0)
 * JD-Core Version:       1.1.3
 */