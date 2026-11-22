/*    */ package net.minecraft.server.commands;
/*    */ 
/*    */ import com.mojang.brigadier.CommandDispatcher;
/*    */ import com.mojang.brigadier.Message;
/*    */ import com.mojang.brigadier.arguments.ArgumentType;
/*    */ import com.mojang.brigadier.builder.LiteralArgumentBuilder;
/*    */ import com.mojang.brigadier.context.CommandContext;
/*    */ import com.mojang.brigadier.exceptions.CommandSyntaxException;
/*    */ import com.mojang.brigadier.exceptions.SimpleCommandExceptionType;
/*    */ import com.mojang.brigadier.suggestion.SuggestionsBuilder;
/*    */ import java.util.Collection;
/*    */ import java.util.concurrent.CompletableFuture;
/*    */ import java.util.function.Predicate;
/*    */ import net.minecraft.commands.CommandSourceStack;
/*    */ import net.minecraft.commands.Commands;
/*    */ import net.minecraft.commands.SharedSuggestionProvider;
/*    */ import net.minecraft.commands.arguments.GameProfileArgument;
/*    */ import net.minecraft.network.chat.Component;
/*    */ import net.minecraft.server.level.ServerPlayer;
/*    */ import net.minecraft.server.players.NameAndId;
/*    */ import net.minecraft.server.players.PlayerList;
/*    */ 
/*    */ public class OpCommand {
/* 21 */   private static final SimpleCommandExceptionType ERROR_ALREADY_OP = new SimpleCommandExceptionType((Message)Component.translatable("commands.op.failed"));
/*    */   
/*    */   public static void register(CommandDispatcher<CommandSourceStack> dispatcher) {
/* 24 */     dispatcher.register(
/* 25 */         (LiteralArgumentBuilder)((LiteralArgumentBuilder)Commands.literal("op")
/* 26 */         .requires((Predicate)Commands.hasPermission(Commands.LEVEL_ADMINS)))
/* 27 */         .then(
/* 28 */           Commands.argument("targets", (ArgumentType)GameProfileArgument.gameProfile())
/* 29 */           .suggests((c, p) -> {
/*    */               PlayerList list = ((CommandSourceStack)c.getSource()).getServer().getPlayerList();
/*    */               return SharedSuggestionProvider.suggest(list.getPlayers().stream().filter(()).map(()), p);
/* 33 */             }).executes(c -> opPlayers((CommandSourceStack)c.getSource(), GameProfileArgument.getGameProfiles(c, "targets")))));
/*    */   }
/*    */   
/*    */   private static int opPlayers(CommandSourceStack source, Collection<NameAndId> players) throws CommandSyntaxException {
/* 39 */     PlayerList list = source.getServer().getPlayerList();
/* 40 */     int count = 0;
/* 42 */     for (NameAndId player : players) {
/* 43 */       if (!list.isOp(player)) {
/* 44 */         list.op(player);
/* 45 */         count++;
/* 46 */         source.sendSuccess(() -> Component.translatable("commands.op.success", new Object[] { player.name() }), true);
/*    */       } 
/*    */     } 
/* 50 */     if (count == 0)
/* 51 */       throw ERROR_ALREADY_OP.create(); 
/* 54 */     return count;
/*    */   }
/*    */ }


/* Location:              C:\Users\karol\AppData\Roaming\ModrinthApp\meta\versions\26.2-0.19.5\26.2-0.19.5.jar!\net\minecraft\server\commands\OpCommand.class
 * Java compiler version: 25 (69.0)
 * JD-Core Version:       1.1.3
 */