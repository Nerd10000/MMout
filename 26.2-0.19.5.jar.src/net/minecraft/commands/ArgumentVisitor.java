/*    */ package net.minecraft.commands;
/*    */ 
/*    */ import com.mojang.brigadier.ParseResults;
/*    */ import com.mojang.brigadier.context.CommandContextBuilder;
/*    */ import com.mojang.brigadier.context.ParsedArgument;
/*    */ import com.mojang.brigadier.context.ParsedCommandNode;
/*    */ import com.mojang.brigadier.tree.ArgumentCommandNode;
/*    */ import com.mojang.brigadier.tree.CommandNode;
/*    */ import java.util.Map;
/*    */ 
/*    */ public class ArgumentVisitor {
/*    */   public static <S> void visitArguments(ParseResults<S> command, Output<S> output, boolean rejectRootRedirects) {
/* 15 */     CommandContextBuilder<S> rootContext = command.getContext();
/* 17 */     CommandContextBuilder<S> context = rootContext;
/* 19 */     visitNodeArguments(context, output);
/*    */     CommandContextBuilder<S> child;
/* 22 */     while ((child = context.getChild()) != null) {
/* 25 */       if (rejectRootRedirects && child.getRootNode() == rootContext.getRootNode())
/*    */         break; 
/* 29 */       visitNodeArguments(child, output);
/* 30 */       context = child;
/*    */     } 
/*    */   }
/*    */   
/*    */   private static <S> void visitNodeArguments(CommandContextBuilder<S> context, Output<S> output) {
/* 35 */     Map<String, ParsedArgument<S, ?>> values = context.getArguments();
/* 36 */     for (ParsedCommandNode<S> node : (Iterable<ParsedCommandNode<S>>)context.getNodes()) {
/* 37 */       CommandNode commandNode = node.getNode();
/* 37 */       if (commandNode instanceof ArgumentCommandNode) {
/* 37 */         ArgumentCommandNode<S, ?> argument = (ArgumentCommandNode<S, ?>)commandNode;
/* 38 */         ParsedArgument<S, ?> value = values.get(argument.getName());
/* 39 */         callVisitor(context, output, argument, value);
/*    */       } 
/*    */     } 
/*    */   }
/*    */   
/*    */   private static <S, T> void callVisitor(CommandContextBuilder<S> context, Output<S> output, ArgumentCommandNode<S, T> argument, ParsedArgument<S, ?> value) {
/* 46 */     output.accept(context, argument, (ParsedArgument)value);
/*    */   }
/*    */   
/*    */   @FunctionalInterface
/*    */   public static interface Output<S> {
/*    */     <T> void accept(CommandContextBuilder<S> param1CommandContextBuilder, ArgumentCommandNode<S, T> param1ArgumentCommandNode, ParsedArgument<S, T> param1ParsedArgument);
/*    */   }
/*    */ }


/* Location:              C:\Users\karol\AppData\Roaming\ModrinthApp\meta\versions\26.2-0.19.5\26.2-0.19.5.jar!\net\minecraft\commands\ArgumentVisitor.class
 * Java compiler version: 25 (69.0)
 * JD-Core Version:       1.1.3
 */