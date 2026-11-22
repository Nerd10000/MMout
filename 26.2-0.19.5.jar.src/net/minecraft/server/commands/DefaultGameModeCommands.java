/*    */ package net.minecraft.server.commands;
/*    */ 
/*    */ import com.mojang.brigadier.CommandDispatcher;
/*    */ import com.mojang.brigadier.arguments.ArgumentType;
/*    */ import com.mojang.brigadier.builder.LiteralArgumentBuilder;
/*    */ import com.mojang.brigadier.context.CommandContext;
/*    */ import com.mojang.brigadier.exceptions.CommandSyntaxException;
/*    */ import java.util.function.Predicate;
/*    */ import net.minecraft.commands.CommandSourceStack;
/*    */ import net.minecraft.commands.Commands;
/*    */ import net.minecraft.commands.arguments.GameModeArgument;
/*    */ import net.minecraft.network.chat.Component;
/*    */ import net.minecraft.server.MinecraftServer;
/*    */ import net.minecraft.world.level.GameType;
/*    */ 
/*    */ public class DefaultGameModeCommands {
/*    */   public static void register(CommandDispatcher<CommandSourceStack> dispatcher) {
/* 17 */     dispatcher.register(
/* 18 */         (LiteralArgumentBuilder)((LiteralArgumentBuilder)Commands.literal("defaultgamemode")
/* 19 */         .requires((Predicate)Commands.hasPermission(Commands.LEVEL_GAMEMASTERS)))
/* 20 */         .then(
/* 21 */           Commands.argument("gamemode", (ArgumentType)GameModeArgument.gameMode())
/* 22 */           .executes(c -> setMode((CommandSourceStack)c.getSource(), GameModeArgument.getGameMode(c, "gamemode")))));
/*    */   }
/*    */   
/*    */   private static int setMode(CommandSourceStack source, GameType type) {
/* 28 */     MinecraftServer server = source.getServer();
/* 29 */     server.setDefaultGameType(type);
/* 30 */     int count = server.enforceGameTypeForPlayers(server.getForcedGameType());
/* 31 */     source.sendSuccess(() -> Component.translatable("commands.defaultgamemode.success", new Object[] { type.getLongDisplayName() }), true);
/* 32 */     return count;
/*    */   }
/*    */ }


/* Location:              C:\Users\karol\AppData\Roaming\ModrinthApp\meta\versions\26.2-0.19.5\26.2-0.19.5.jar!\net\minecraft\server\commands\DefaultGameModeCommands.class
 * Java compiler version: 25 (69.0)
 * JD-Core Version:       1.1.3
 */