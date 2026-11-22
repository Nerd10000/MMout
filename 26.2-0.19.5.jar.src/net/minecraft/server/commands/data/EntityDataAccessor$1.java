/*    */ package net.minecraft.server.commands.data;
/*    */ 
/*    */ import com.mojang.brigadier.arguments.ArgumentType;
/*    */ import com.mojang.brigadier.builder.ArgumentBuilder;
/*    */ import com.mojang.brigadier.context.CommandContext;
/*    */ import com.mojang.brigadier.exceptions.CommandSyntaxException;
/*    */ import java.util.function.Function;
/*    */ import net.minecraft.commands.CommandSourceStack;
/*    */ import net.minecraft.commands.Commands;
/*    */ import net.minecraft.commands.arguments.EntityArgument;
/*    */ 
/*    */ class null implements DataCommands.DataProvider {
/*    */   public DataAccessor access(CommandContext<CommandSourceStack> context) throws CommandSyntaxException {
/* 38 */     return new EntityDataAccessor(EntityArgument.getEntity(context, arg));
/*    */   }
/*    */   
/*    */   public ArgumentBuilder<CommandSourceStack, ?> wrap(ArgumentBuilder<CommandSourceStack, ?> parent, Function<ArgumentBuilder<CommandSourceStack, ?>, ArgumentBuilder<CommandSourceStack, ?>> function) {
/* 43 */     return parent.then(Commands.literal("entity").then(function.apply(Commands.argument(arg, (ArgumentType)EntityArgument.entity()))));
/*    */   }
/*    */ }


/* Location:              C:\Users\karol\AppData\Roaming\ModrinthApp\meta\versions\26.2-0.19.5\26.2-0.19.5.jar!\net\minecraft\server\commands\data\EntityDataAccessor$1.class
 * Java compiler version: 25 (69.0)
 * JD-Core Version:       1.1.3
 */