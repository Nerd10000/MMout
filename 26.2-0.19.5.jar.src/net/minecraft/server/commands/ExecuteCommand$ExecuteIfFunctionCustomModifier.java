/*     */ package net.minecraft.server.commands;
/*     */ 
/*     */ import com.mojang.brigadier.context.CommandContext;
/*     */ import com.mojang.brigadier.context.ContextChain;
/*     */ import com.mojang.brigadier.exceptions.CommandSyntaxException;
/*     */ import java.util.Collection;
/*     */ import java.util.List;
/*     */ import java.util.function.IntPredicate;
/*     */ import net.minecraft.commands.CommandSourceStack;
/*     */ import net.minecraft.commands.arguments.item.FunctionArgument;
/*     */ import net.minecraft.commands.execution.ChainModifiers;
/*     */ import net.minecraft.commands.execution.CustomModifierExecutor;
/*     */ import net.minecraft.commands.execution.ExecutionControl;
/*     */ 
/*     */ class ExecuteIfFunctionCustomModifier implements CustomModifierExecutor.ModifierAdapter<CommandSourceStack> {
/*     */   private final IntPredicate check;
/*     */   
/*     */   private ExecuteIfFunctionCustomModifier(boolean check) {
/* 992 */     this.check = check ? (value -> (value != 0)) : (value -> (value == 0));
/*     */   }
/*     */   
/*     */   public void apply(CommandSourceStack originalSource, List<CommandSourceStack> currentSources, ContextChain<CommandSourceStack> currentStep, ChainModifiers modifiers, ExecutionControl<CommandSourceStack> output) {
/* 997 */     ExecuteCommand.scheduleFunctionConditionsAndTest(originalSource, currentSources, FunctionCommand::modifySenderForExecution, this.check, currentStep, null, output, c -> FunctionArgument.getFunctions(c, "name"), modifiers);
/*     */   }
/*     */ }


/* Location:              C:\Users\karol\AppData\Roaming\ModrinthApp\meta\versions\26.2-0.19.5\26.2-0.19.5.jar!\net\minecraft\server\commands\ExecuteCommand$ExecuteIfFunctionCustomModifier.class
 * Java compiler version: 25 (69.0)
 * JD-Core Version:       1.1.3
 */