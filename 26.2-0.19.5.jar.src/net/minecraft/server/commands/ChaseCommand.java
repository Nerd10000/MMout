/*     */ package net.minecraft.server.commands;
/*     */ 
/*     */ import com.google.common.collect.BiMap;
/*     */ import com.google.common.collect.ImmutableBiMap;
/*     */ import com.mojang.brigadier.CommandDispatcher;
/*     */ import com.mojang.brigadier.arguments.ArgumentType;
/*     */ import com.mojang.brigadier.arguments.IntegerArgumentType;
/*     */ import com.mojang.brigadier.arguments.StringArgumentType;
/*     */ import com.mojang.brigadier.builder.LiteralArgumentBuilder;
/*     */ import com.mojang.brigadier.builder.RequiredArgumentBuilder;
/*     */ import com.mojang.brigadier.context.CommandContext;
/*     */ import com.mojang.brigadier.exceptions.CommandSyntaxException;
/*     */ import com.mojang.logging.LogUtils;
/*     */ import java.io.IOException;
/*     */ import net.minecraft.commands.CommandSourceStack;
/*     */ import net.minecraft.commands.Commands;
/*     */ import net.minecraft.network.chat.Component;
/*     */ import net.minecraft.resources.ResourceKey;
/*     */ import net.minecraft.server.chase.ChaseClient;
/*     */ import net.minecraft.server.chase.ChaseServer;
/*     */ import net.minecraft.world.level.Level;
/*     */ import org.slf4j.Logger;
/*     */ 
/*     */ public class ChaseCommand {
/*  33 */   private static final Logger LOGGER = LogUtils.getLogger();
/*     */   
/*     */   private static final String DEFAULT_CONNECT_HOST = "localhost";
/*     */   
/*     */   private static final String DEFAULT_BIND_ADDRESS = "0.0.0.0";
/*     */   
/*     */   private static final int DEFAULT_PORT = 10000;
/*     */   
/*     */   private static final int BROADCAST_INTERVAL_MS = 100;
/*     */   
/*  40 */   public static final BiMap<String, ResourceKey<Level>> DIMENSION_NAMES = (BiMap<String, ResourceKey<Level>>)ImmutableBiMap.of("o", Level.OVERWORLD, "n", Level.NETHER, "e", Level.END);
/*     */   
/*     */   private static ChaseServer chaseServer;
/*     */   
/*     */   private static ChaseClient chaseClient;
/*     */   
/*     */   public static void register(CommandDispatcher<CommandSourceStack> dispatcher) {
/*  50 */     dispatcher.register(
/*  51 */         (LiteralArgumentBuilder)((LiteralArgumentBuilder)((LiteralArgumentBuilder)Commands.literal("chase")
/*  52 */         .then((
/*  53 */           (LiteralArgumentBuilder)Commands.literal("follow")
/*  54 */           .then(((RequiredArgumentBuilder)Commands.argument("host", (ArgumentType)StringArgumentType.string())
/*  55 */             .executes(c -> follow((CommandSourceStack)c.getSource(), StringArgumentType.getString(c, "host"), 10000)))
/*  56 */             .then(Commands.argument("port", (ArgumentType)IntegerArgumentType.integer(1, 65535))
/*  57 */               .executes(c -> follow((CommandSourceStack)c.getSource(), StringArgumentType.getString(c, "host"), IntegerArgumentType.getInteger(c, "port"))))))
/*     */           
/*  59 */           .executes(c -> follow((CommandSourceStack)c.getSource(), "localhost", 10000))))
/*     */         
/*  61 */         .then((
/*  62 */           (LiteralArgumentBuilder)Commands.literal("lead")
/*  63 */           .then(((RequiredArgumentBuilder)Commands.argument("bind_address", (ArgumentType)StringArgumentType.string())
/*  64 */             .executes(c -> lead((CommandSourceStack)c.getSource(), StringArgumentType.getString(c, "bind_address"), 10000)))
/*  65 */             .then(Commands.argument("port", (ArgumentType)IntegerArgumentType.integer(1024, 65535))
/*  66 */               .executes(c -> lead((CommandSourceStack)c.getSource(), StringArgumentType.getString(c, "bind_address"), IntegerArgumentType.getInteger(c, "port"))))))
/*     */           
/*  68 */           .executes(c -> lead((CommandSourceStack)c.getSource(), "0.0.0.0", 10000))))
/*     */         
/*  70 */         .then(
/*  71 */           Commands.literal("stop")
/*  72 */           .executes(c -> stop((CommandSourceStack)c.getSource()))));
/*     */   }
/*     */   
/*     */   private static int stop(CommandSourceStack source) {
/*  78 */     if (chaseClient != null) {
/*  79 */       chaseClient.stop();
/*  80 */       source.sendSuccess(() -> Component.literal("You have now stopped chasing"), false);
/*  81 */       chaseClient = null;
/*     */     } 
/*  83 */     if (chaseServer != null) {
/*  84 */       chaseServer.stop();
/*  85 */       source.sendSuccess(() -> Component.literal("You are no longer being chased"), false);
/*  86 */       chaseServer = null;
/*     */     } 
/*  88 */     return 0;
/*     */   }
/*     */   
/*     */   private static boolean alreadyRunning(CommandSourceStack source) {
/*  92 */     if (chaseServer != null) {
/*  93 */       source.sendFailure((Component)Component.literal("Chase server is already running. Stop it using /chase stop"));
/*  94 */       return true;
/*     */     } 
/*  96 */     if (chaseClient != null) {
/*  97 */       source.sendFailure((Component)Component.literal("You are already chasing someone. Stop it using /chase stop"));
/*  98 */       return true;
/*     */     } 
/* 100 */     return false;
/*     */   }
/*     */   
/*     */   private static int lead(CommandSourceStack source, String serverBindAddress, int port) {
/* 104 */     if (alreadyRunning(source))
/* 105 */       return 0; 
/* 108 */     chaseServer = new ChaseServer(serverBindAddress, port, source.getServer().getPlayerList(), 100);
/*     */     try {
/* 110 */       chaseServer.start();
/* 111 */       source.sendSuccess(() -> Component.literal("Chase server is now running on port " + port + ". Clients can follow you using /chase follow <ip> <port>"), false);
/* 112 */     } catch (IOException e) {
/* 113 */       LOGGER.error("Failed to start chase server", e);
/* 114 */       source.sendFailure((Component)Component.literal("Failed to start chase server on port " + port));
/* 115 */       chaseServer = null;
/*     */     } 
/* 117 */     return 0;
/*     */   }
/*     */   
/*     */   private static int follow(CommandSourceStack source, String host, int port) {
/* 121 */     if (alreadyRunning(source))
/* 122 */       return 0; 
/* 125 */     chaseClient = new ChaseClient(host, port, source.getServer());
/* 126 */     chaseClient.start();
/* 127 */     source.sendSuccess(() -> Component.literal("You are now chasing " + host + ":" + port + ". If that server does '/chase lead' then you will automatically go to the same position. Use '/chase stop' to stop chasing."), false);
/* 128 */     return 0;
/*     */   }
/*     */ }


/* Location:              C:\Users\karol\AppData\Roaming\ModrinthApp\meta\versions\26.2-0.19.5\26.2-0.19.5.jar!\net\minecraft\server\commands\ChaseCommand.class
 * Java compiler version: 25 (69.0)
 * JD-Core Version:       1.1.3
 */