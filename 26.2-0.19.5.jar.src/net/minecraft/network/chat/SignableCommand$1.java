/*    */ package net.minecraft.network.chat;
/*    */ 
/*    */ import com.mojang.brigadier.context.CommandContextBuilder;
/*    */ import com.mojang.brigadier.context.ParsedArgument;
/*    */ import com.mojang.brigadier.tree.ArgumentCommandNode;
/*    */ import java.util.List;
/*    */ import net.minecraft.commands.ArgumentVisitor;
/*    */ 
/*    */ class null implements ArgumentVisitor.Output<S> {
/*    */   public <T> void accept(CommandContextBuilder<S> context, ArgumentCommandNode<S, T> argument, ParsedArgument<S, T> value) {
/* 26 */     if (value != null && argument.getType() instanceof net.minecraft.commands.arguments.SignedArgument) {
/* 27 */       String stringValue = value.getRange().get(commandString);
/* 28 */       arguments.add(new SignableCommand.Argument<>(argument, stringValue));
/*    */     } 
/*    */   }
/*    */ }


/* Location:              C:\Users\karol\AppData\Roaming\ModrinthApp\meta\versions\26.2-0.19.5\26.2-0.19.5.jar!\net\minecraft\network\chat\SignableCommand$1.class
 * Java compiler version: 25 (69.0)
 * JD-Core Version:       1.1.3
 */