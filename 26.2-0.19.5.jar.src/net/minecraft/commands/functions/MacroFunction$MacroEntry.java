/*     */ package net.minecraft.commands.functions;
/*     */ 
/*     */ import com.mojang.brigadier.CommandDispatcher;
/*     */ import com.mojang.brigadier.StringReader;
/*     */ import com.mojang.brigadier.exceptions.CommandSyntaxException;
/*     */ import it.unimi.dsi.fastutil.ints.IntList;
/*     */ import java.util.List;
/*     */ import net.minecraft.commands.ExecutionCommandSource;
/*     */ import net.minecraft.commands.FunctionInstantiationException;
/*     */ import net.minecraft.commands.execution.UnboundEntryAction;
/*     */ import net.minecraft.network.chat.Component;
/*     */ import net.minecraft.resources.Identifier;
/*     */ 
/*     */ class MacroEntry<T extends ExecutionCommandSource<T>> implements MacroFunction.Entry<T> {
/*     */   private final StringTemplate template;
/*     */   
/*     */   private final IntList parameters;
/*     */   
/*     */   private final T compilationContext;
/*     */   
/*     */   public MacroEntry(StringTemplate template, IntList parameters, T compilationContext) {
/* 146 */     this.template = template;
/* 147 */     this.parameters = parameters;
/* 148 */     this.compilationContext = compilationContext;
/*     */   }
/*     */   
/*     */   public IntList parameters() {
/* 153 */     return this.parameters;
/*     */   }
/*     */   
/*     */   public UnboundEntryAction<T> instantiate(List<String> substitutions, CommandDispatcher<T> dispatcher, Identifier functionId) throws FunctionInstantiationException {
/* 158 */     String command = this.template.substitute(substitutions);
/*     */     try {
/* 160 */       return CommandFunction.parseCommand(dispatcher, this.compilationContext, new StringReader(command));
/* 161 */     } catch (CommandSyntaxException e) {
/* 162 */       throw new FunctionInstantiationException(Component.translatable("commands.function.error.parse", new Object[] { Component.translationArg(functionId), command, e.getMessage() }));
/*     */     } 
/*     */   }
/*     */ }


/* Location:              C:\Users\karol\AppData\Roaming\ModrinthApp\meta\versions\26.2-0.19.5\26.2-0.19.5.jar!\net\minecraft\commands\functions\MacroFunction$MacroEntry.class
 * Java compiler version: 25 (69.0)
 * JD-Core Version:       1.1.3
 */