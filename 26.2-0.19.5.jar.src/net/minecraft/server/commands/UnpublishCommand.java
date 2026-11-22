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
/*    */ public class UnpublishCommand {
/*    */   public static void register(CommandDispatcher<CommandSourceStack> dispatcher) {
/* 12 */     dispatcher.register(
/* 13 */         (LiteralArgumentBuilder)((LiteralArgumentBuilder)Commands.literal("unpublish")
/* 14 */         .requires((Predicate)Commands.hasPermission(Commands.LEVEL_OWNERS)))
/* 15 */         .executes(c -> unpublish((CommandSourceStack)c.getSource())));
/*    */   }
/*    */   
/*    */   private static int unpublish(CommandSourceStack source) {
/* 20 */     if (source.getServer().unpublishServer()) {
/* 21 */       source.sendSuccess(() -> Component.translatable("commands.unpublish.success"), true);
/* 22 */       return 1;
/*    */     } 
/* 24 */     source.sendFailure((Component)Component.translatable("commands.unpublish.notPublished"));
/* 25 */     return 0;
/*    */   }
/*    */ }


/* Location:              C:\Users\karol\AppData\Roaming\ModrinthApp\meta\versions\26.2-0.19.5\26.2-0.19.5.jar!\net\minecraft\server\commands\UnpublishCommand.class
 * Java compiler version: 25 (69.0)
 * JD-Core Version:       1.1.3
 */