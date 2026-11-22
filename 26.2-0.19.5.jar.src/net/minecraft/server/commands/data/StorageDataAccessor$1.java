/*    */ package net.minecraft.server.commands.data;
/*    */ 
/*    */ import com.mojang.brigadier.arguments.ArgumentType;
/*    */ import com.mojang.brigadier.builder.ArgumentBuilder;
/*    */ import com.mojang.brigadier.context.CommandContext;
/*    */ import java.util.function.Function;
/*    */ import net.minecraft.commands.CommandSourceStack;
/*    */ import net.minecraft.commands.Commands;
/*    */ import net.minecraft.commands.arguments.IdentifierArgument;
/*    */ 
/*    */ class null implements DataCommands.DataProvider {
/*    */   public DataAccessor access(CommandContext<CommandSourceStack> context) {
/* 31 */     return new StorageDataAccessor(StorageDataAccessor.getGlobalTags(context), IdentifierArgument.getId(context, arg));
/*    */   }
/*    */   
/*    */   public ArgumentBuilder<CommandSourceStack, ?> wrap(ArgumentBuilder<CommandSourceStack, ?> parent, Function<ArgumentBuilder<CommandSourceStack, ?>, ArgumentBuilder<CommandSourceStack, ?>> function) {
/* 36 */     return parent.then(Commands.literal("storage").then(function.apply(Commands.argument(arg, (ArgumentType)IdentifierArgument.id()).suggests(StorageDataAccessor.SUGGEST_STORAGE))));
/*    */   }
/*    */ }


/* Location:              C:\Users\karol\AppData\Roaming\ModrinthApp\meta\versions\26.2-0.19.5\26.2-0.19.5.jar!\net\minecraft\server\commands\data\StorageDataAccessor$1.class
 * Java compiler version: 25 (69.0)
 * JD-Core Version:       1.1.3
 */