/*    */ package net.minecraft.server.commands;
/*    */ 
/*    */ import com.mojang.brigadier.CommandDispatcher;
/*    */ import com.mojang.brigadier.builder.LiteralArgumentBuilder;
/*    */ import com.mojang.brigadier.context.CommandContext;
/*    */ import com.mojang.brigadier.exceptions.CommandSyntaxException;
/*    */ import java.util.function.Predicate;
/*    */ import net.minecraft.commands.CommandSourceStack;
/*    */ import net.minecraft.commands.Commands;
/*    */ import net.minecraft.network.chat.Component;
/*    */ 
/*    */ public class StopCommand {
/*    */   public static void register(CommandDispatcher<CommandSourceStack> dispatcher) {
/* 12 */     dispatcher.register(
/* 13 */         (LiteralArgumentBuilder)((LiteralArgumentBuilder)Commands.literal("stop")
/* 14 */         .requires((Predicate)Commands.hasPermission(Commands.LEVEL_OWNERS)))
/* 15 */         .executes(c -> {
/*    */             ((CommandSourceStack)c.getSource()).sendSuccess((), true);
/*    */             ((CommandSourceStack)c.getSource()).getServer().halt(false);
/*    */             return 1;
/*    */           }));
/*    */   }
/*    */ }


/* Location:              C:\Users\karol\AppData\Roaming\ModrinthApp\meta\versions\26.2-0.19.5\26.2-0.19.5.jar!\net\minecraft\server\commands\StopCommand.class
 * Java compiler version: 25 (69.0)
 * JD-Core Version:       1.1.3
 */