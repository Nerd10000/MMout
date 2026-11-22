/*    */ package net.minecraft.server.commands;
/*    */ 
/*    */ import com.mojang.brigadier.CommandDispatcher;
/*    */ import com.mojang.brigadier.arguments.ArgumentType;
/*    */ import com.mojang.brigadier.builder.LiteralArgumentBuilder;
/*    */ import com.mojang.brigadier.builder.RequiredArgumentBuilder;
/*    */ import com.mojang.brigadier.context.CommandContext;
/*    */ import com.mojang.brigadier.exceptions.CommandSyntaxException;
/*    */ import java.util.Collection;
/*    */ import java.util.Collections;
/*    */ import java.util.function.Predicate;
/*    */ import net.minecraft.commands.CommandSourceStack;
/*    */ import net.minecraft.commands.Commands;
/*    */ import net.minecraft.commands.arguments.EntityArgument;
/*    */ import net.minecraft.commands.arguments.GameModeArgument;
/*    */ import net.minecraft.network.chat.Component;
/*    */ import net.minecraft.network.chat.MutableComponent;
/*    */ import net.minecraft.server.MinecraftServer;
/*    */ import net.minecraft.server.level.ServerPlayer;
/*    */ import net.minecraft.server.permissions.PermissionCheck;
/*    */ import net.minecraft.server.permissions.Permissions;
/*    */ import net.minecraft.world.level.GameType;
/*    */ import net.minecraft.world.level.gamerules.GameRules;
/*    */ 
/*    */ public class GameModeCommand {
/* 26 */   public static final PermissionCheck PERMISSION_CHECK = (PermissionCheck)new PermissionCheck.Require(Permissions.COMMANDS_GAMEMASTER);
/*    */   
/*    */   public static void register(CommandDispatcher<CommandSourceStack> dispatcher) {
/* 29 */     dispatcher.register(
/* 30 */         (LiteralArgumentBuilder)((LiteralArgumentBuilder)Commands.literal("gamemode")
/* 31 */         .requires((Predicate)Commands.hasPermission(PERMISSION_CHECK)))
/* 32 */         .then((
/* 33 */           (RequiredArgumentBuilder)Commands.argument("gamemode", (ArgumentType)GameModeArgument.gameMode())
/* 34 */           .executes(c -> setMode(c, Collections.singleton(((CommandSourceStack)c.getSource()).getPlayerOrException()), GameModeArgument.getGameMode(c, "gamemode"))))
/* 35 */           .then(
/* 36 */             Commands.argument("target", (ArgumentType)EntityArgument.players())
/* 37 */             .executes(c -> setMode(c, EntityArgument.getPlayers(c, "target"), GameModeArgument.getGameMode(c, "gamemode"))))));
/*    */   }
/*    */   
/*    */   private static void logGamemodeChange(CommandSourceStack source, ServerPlayer target, GameType newType) {
/* 44 */     MutableComponent mutableComponent = Component.translatable("gameMode." + newType.getName());
/* 45 */     if (source.getEntity() == target) {
/* 46 */       source.sendSuccess(() -> Component.translatable("commands.gamemode.success.self", new Object[] { mode }), true);
/*    */     } else {
/* 48 */       if (((Boolean)source.getLevel().getGameRules().get(GameRules.SEND_COMMAND_FEEDBACK)).booleanValue())
/* 49 */         target.sendSystemMessage((Component)Component.translatable("gameMode.changed", new Object[] { mutableComponent })); 
/* 52 */       source.sendSuccess(() -> Component.translatable("commands.gamemode.success.other", new Object[] { target.getDisplayName(), mode }), true);
/*    */     } 
/*    */   }
/*    */   
/*    */   private static int setMode(CommandContext<CommandSourceStack> context, Collection<ServerPlayer> players, GameType type) {
/* 57 */     int count = 0;
/* 58 */     MinecraftServer server = ((CommandSourceStack)context.getSource()).getServer();
/* 59 */     for (ServerPlayer player : players) {
/* 60 */       if (server.isSingleplayerOwner(player.nameAndId()))
/* 61 */         server.setDefaultGameType(type); 
/* 63 */       if (setGameMode((CommandSourceStack)context.getSource(), player, type))
/* 64 */         count++; 
/*    */     } 
/* 67 */     return count;
/*    */   }
/*    */   
/*    */   public static void setGameMode(ServerPlayer player, GameType type) {
/* 71 */     setGameMode(player.createCommandSourceStack(), player, type);
/*    */   }
/*    */   
/*    */   private static boolean setGameMode(CommandSourceStack source, ServerPlayer player, GameType type) {
/* 75 */     if (player.setGameMode(type)) {
/* 76 */       MinecraftServer server = source.getServer();
/* 77 */       if (server.isSingleplayerOwner(player.nameAndId()))
/* 78 */         server.setDefaultGameType(type); 
/* 80 */       logGamemodeChange(source, player, type);
/* 81 */       return true;
/*    */     } 
/* 83 */     return false;
/*    */   }
/*    */ }


/* Location:              C:\Users\karol\AppData\Roaming\ModrinthApp\meta\versions\26.2-0.19.5\26.2-0.19.5.jar!\net\minecraft\server\commands\GameModeCommand.class
 * Java compiler version: 25 (69.0)
 * JD-Core Version:       1.1.3
 */