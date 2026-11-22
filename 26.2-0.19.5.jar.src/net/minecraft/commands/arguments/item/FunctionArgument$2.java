/*    */ package net.minecraft.commands.arguments.item;
/*    */ 
/*    */ import com.mojang.brigadier.context.CommandContext;
/*    */ import com.mojang.brigadier.exceptions.CommandSyntaxException;
/*    */ import com.mojang.datafixers.util.Either;
/*    */ import com.mojang.datafixers.util.Pair;
/*    */ import java.util.Collection;
/*    */ import java.util.Collections;
/*    */ import net.minecraft.commands.CommandSourceStack;
/*    */ import net.minecraft.commands.functions.CommandFunction;
/*    */ import net.minecraft.resources.Identifier;
/*    */ 
/*    */ class null implements FunctionArgument.Result {
/*    */   null(FunctionArgument this$0) {}
/*    */   
/*    */   public Collection<CommandFunction<CommandSourceStack>> create(CommandContext<CommandSourceStack> c) throws CommandSyntaxException {
/* 55 */     return Collections.singleton(FunctionArgument.getFunction(c, id));
/*    */   }
/*    */   
/*    */   public Pair<Identifier, Either<CommandFunction<CommandSourceStack>, Collection<CommandFunction<CommandSourceStack>>>> unwrap(CommandContext<CommandSourceStack> context) throws CommandSyntaxException {
/* 60 */     return Pair.of(id, Either.left(FunctionArgument.getFunction(context, id)));
/*    */   }
/*    */   
/*    */   public Pair<Identifier, Collection<CommandFunction<CommandSourceStack>>> unwrapToCollection(CommandContext<CommandSourceStack> context) throws CommandSyntaxException {
/* 65 */     return Pair.of(id, Collections.singleton(FunctionArgument.getFunction(context, id)));
/*    */   }
/*    */ }


/* Location:              C:\Users\karol\AppData\Roaming\ModrinthApp\meta\versions\26.2-0.19.5\26.2-0.19.5.jar!\net\minecraft\commands\arguments\item\FunctionArgument$2.class
 * Java compiler version: 25 (69.0)
 * JD-Core Version:       1.1.3
 */