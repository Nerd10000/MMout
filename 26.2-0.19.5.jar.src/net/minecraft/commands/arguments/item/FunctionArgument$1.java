/*    */ package net.minecraft.commands.arguments.item;
/*    */ 
/*    */ import com.mojang.brigadier.context.CommandContext;
/*    */ import com.mojang.brigadier.exceptions.CommandSyntaxException;
/*    */ import com.mojang.datafixers.util.Either;
/*    */ import com.mojang.datafixers.util.Pair;
/*    */ import java.util.Collection;
/*    */ import net.minecraft.commands.CommandSourceStack;
/*    */ import net.minecraft.commands.functions.CommandFunction;
/*    */ import net.minecraft.resources.Identifier;
/*    */ 
/*    */ class null implements FunctionArgument.Result {
/*    */   null(FunctionArgument this$0) {}
/*    */   
/*    */   public Collection<CommandFunction<CommandSourceStack>> create(CommandContext<CommandSourceStack> c) throws CommandSyntaxException {
/* 36 */     return FunctionArgument.getFunctionTag(c, id);
/*    */   }
/*    */   
/*    */   public Pair<Identifier, Either<CommandFunction<CommandSourceStack>, Collection<CommandFunction<CommandSourceStack>>>> unwrap(CommandContext<CommandSourceStack> context) throws CommandSyntaxException {
/* 41 */     return Pair.of(id, Either.right(FunctionArgument.getFunctionTag(context, id)));
/*    */   }
/*    */   
/*    */   public Pair<Identifier, Collection<CommandFunction<CommandSourceStack>>> unwrapToCollection(CommandContext<CommandSourceStack> context) throws CommandSyntaxException {
/* 46 */     return Pair.of(id, FunctionArgument.getFunctionTag(context, id));
/*    */   }
/*    */ }


/* Location:              C:\Users\karol\AppData\Roaming\ModrinthApp\meta\versions\26.2-0.19.5\26.2-0.19.5.jar!\net\minecraft\commands\arguments\item\FunctionArgument$1.class
 * Java compiler version: 25 (69.0)
 * JD-Core Version:       1.1.3
 */