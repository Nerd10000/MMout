/*    */ package net.minecraft.server.commands;
/*    */ 
/*    */ import com.mojang.brigadier.CommandDispatcher;
/*    */ import com.mojang.brigadier.Message;
/*    */ import com.mojang.brigadier.arguments.ArgumentType;
/*    */ import com.mojang.brigadier.arguments.BoolArgumentType;
/*    */ import com.mojang.brigadier.arguments.IntegerArgumentType;
/*    */ import com.mojang.brigadier.builder.LiteralArgumentBuilder;
/*    */ import com.mojang.brigadier.builder.RequiredArgumentBuilder;
/*    */ import com.mojang.brigadier.context.CommandContext;
/*    */ import com.mojang.brigadier.exceptions.CommandSyntaxException;
/*    */ import com.mojang.brigadier.exceptions.DynamicCommandExceptionType;
/*    */ import com.mojang.brigadier.exceptions.SimpleCommandExceptionType;
/*    */ import java.util.function.Predicate;
/*    */ import net.minecraft.commands.CommandSourceStack;
/*    */ import net.minecraft.commands.Commands;
/*    */ import net.minecraft.commands.arguments.GameModeArgument;
/*    */ import net.minecraft.network.chat.Component;
/*    */ import net.minecraft.network.chat.ComponentUtils;
/*    */ import net.minecraft.network.chat.MutableComponent;
/*    */ import net.minecraft.server.MinecraftServer;
/*    */ import net.minecraft.util.HttpUtil;
/*    */ import net.minecraft.world.level.GameType;
/*    */ 
/*    */ public class PublishCommand {
/* 27 */   private static final SimpleCommandExceptionType ERROR_FAILED = new SimpleCommandExceptionType((Message)Component.translatable("commands.publish.failed"));
/*    */   
/*    */   private static final DynamicCommandExceptionType ERROR_ALREADY_PUBLISHED_LAN;
/*    */   
/*    */   static {
/* 28 */     ERROR_ALREADY_PUBLISHED_LAN = new DynamicCommandExceptionType(port -> Component.translatableEscape("commands.publish.alreadyPublished.lan", new Object[] { port }));
/*    */   }
/*    */   
/*    */   public static void register(CommandDispatcher<CommandSourceStack> dispatcher) {
/* 31 */     dispatcher.register(
/* 32 */         (LiteralArgumentBuilder)((LiteralArgumentBuilder)((LiteralArgumentBuilder)Commands.literal("publish")
/* 33 */         .requires((Predicate)Commands.hasPermission(Commands.LEVEL_OWNERS)))
/* 34 */         .executes(c -> publish((CommandSourceStack)c.getSource(), HttpUtil.getAvailablePort(), false, null)))
/* 35 */         .then((
/* 36 */           (RequiredArgumentBuilder)Commands.argument("allowCommands", (ArgumentType)BoolArgumentType.bool())
/* 37 */           .executes(c -> publish((CommandSourceStack)c.getSource(), HttpUtil.getAvailablePort(), BoolArgumentType.getBool(c, "allowCommands"), null)))
/* 38 */           .then((
/* 39 */             (RequiredArgumentBuilder)Commands.argument("gamemode", (ArgumentType)GameModeArgument.gameMode())
/* 40 */             .executes(c -> publish((CommandSourceStack)c.getSource(), HttpUtil.getAvailablePort(), BoolArgumentType.getBool(c, "allowCommands"), GameModeArgument.getGameMode(c, "gamemode"))))
/* 41 */             .then(
/* 42 */               Commands.argument("port", (ArgumentType)IntegerArgumentType.integer(0, 65535))
/* 43 */               .executes(c -> publish((CommandSourceStack)c.getSource(), IntegerArgumentType.getInteger(c, "port"), BoolArgumentType.getBool(c, "allowCommands"), GameModeArgument.getGameMode(c, "gamemode")))))));
/*    */   }
/*    */   
/*    */   private static int publish(CommandSourceStack source, int port, boolean allowCommands, GameType type) throws CommandSyntaxException {
/* 51 */     MinecraftServer server = source.getServer();
/* 52 */     if (server.isPublished() && 
/* 53 */       server.getPort() > -1)
/* 54 */       throw ERROR_ALREADY_PUBLISHED_LAN.create(Integer.valueOf(server.getPort())); 
/* 57 */     if (!server.publishServer(MinecraftServer.MultiplayerScope.LAN, type, allowCommands, port))
/* 58 */       throw ERROR_FAILED.create(); 
/* 60 */     source.sendSuccess(() -> getSuccessMessage(port), true);
/* 61 */     return port;
/*    */   }
/*    */   
/*    */   public static MutableComponent getSuccessMessage(int port) {
/* 65 */     MutableComponent mutableComponent = ComponentUtils.copyOnClickText(String.valueOf(port));
/* 66 */     return Component.translatable("commands.publish.started.lan", new Object[] { mutableComponent });
/*    */   }
/*    */ }


/* Location:              C:\Users\karol\AppData\Roaming\ModrinthApp\meta\versions\26.2-0.19.5\26.2-0.19.5.jar!\net\minecraft\server\commands\PublishCommand.class
 * Java compiler version: 25 (69.0)
 * JD-Core Version:       1.1.3
 */