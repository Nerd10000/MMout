/*    */ package net.minecraft.server.commands;
/*    */ 
/*    */ import com.mojang.brigadier.arguments.IntegerArgumentType;
/*    */ import com.mojang.brigadier.context.ContextChain;
/*    */ import net.minecraft.commands.ExecutionCommandSource;
/*    */ import net.minecraft.commands.execution.ChainModifiers;
/*    */ import net.minecraft.commands.execution.CustomCommandExecutor;
/*    */ import net.minecraft.commands.execution.ExecutionControl;
/*    */ import net.minecraft.commands.execution.Frame;
/*    */ 
/*    */ class ReturnValueCustomExecutor<T extends ExecutionCommandSource<T>> implements CustomCommandExecutor.CommandAdapter<T> {
/*    */   public void run(T sender, ContextChain<T> currentStep, ChainModifiers modifiers, ExecutionControl<T> output) {
/* 46 */     int returnValue = IntegerArgumentType.getInteger(currentStep.getTopContext(), "value");
/* 47 */     sender.callback().onSuccess(returnValue);
/* 48 */     Frame frame = output.currentFrame();
/* 49 */     frame.returnSuccess(returnValue);
/* 50 */     frame.discard();
/*    */   }
/*    */ }


/* Location:              C:\Users\karol\AppData\Roaming\ModrinthApp\meta\versions\26.2-0.19.5\26.2-0.19.5.jar!\net\minecraft\server\commands\ReturnCommand$ReturnValueCustomExecutor.class
 * Java compiler version: 25 (69.0)
 * JD-Core Version:       1.1.3
 */