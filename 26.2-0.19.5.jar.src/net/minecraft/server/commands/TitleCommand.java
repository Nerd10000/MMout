/*     */ package net.minecraft.server.commands;
/*     */ 
/*     */ import com.mojang.brigadier.CommandDispatcher;
/*     */ import com.mojang.brigadier.arguments.ArgumentType;
/*     */ import com.mojang.brigadier.arguments.IntegerArgumentType;
/*     */ import com.mojang.brigadier.builder.LiteralArgumentBuilder;
/*     */ import com.mojang.brigadier.builder.RequiredArgumentBuilder;
/*     */ import com.mojang.brigadier.context.CommandContext;
/*     */ import com.mojang.brigadier.exceptions.CommandSyntaxException;
/*     */ import java.util.Collection;
/*     */ import java.util.function.Function;
/*     */ import java.util.function.Predicate;
/*     */ import net.minecraft.commands.CommandBuildContext;
/*     */ import net.minecraft.commands.CommandSourceStack;
/*     */ import net.minecraft.commands.Commands;
/*     */ import net.minecraft.commands.arguments.ComponentArgument;
/*     */ import net.minecraft.commands.arguments.EntityArgument;
/*     */ import net.minecraft.commands.arguments.TimeArgument;
/*     */ import net.minecraft.network.chat.Component;
/*     */ import net.minecraft.network.chat.ComponentUtils;
/*     */ import net.minecraft.network.chat.ResolutionContext;
/*     */ import net.minecraft.network.protocol.Packet;
/*     */ import net.minecraft.network.protocol.game.ClientboundClearTitlesPacket;
/*     */ import net.minecraft.network.protocol.game.ClientboundSetTitlesAnimationPacket;
/*     */ import net.minecraft.server.level.ServerPlayer;
/*     */ import net.minecraft.world.entity.Entity;
/*     */ 
/*     */ public class TitleCommand {
/*     */   public static void register(CommandDispatcher<CommandSourceStack> dispatcher, CommandBuildContext context) {
/*  33 */     dispatcher.register(
/*  34 */         (LiteralArgumentBuilder)((LiteralArgumentBuilder)Commands.literal("title")
/*  35 */         .requires((Predicate)Commands.hasPermission(Commands.LEVEL_GAMEMASTERS)))
/*  36 */         .then((
/*  37 */           (RequiredArgumentBuilder)((RequiredArgumentBuilder)((RequiredArgumentBuilder)((RequiredArgumentBuilder)((RequiredArgumentBuilder)Commands.argument("targets", (ArgumentType)EntityArgument.players())
/*  38 */           .then(
/*  39 */             Commands.literal("clear")
/*  40 */             .executes(c -> clearTitle((CommandSourceStack)c.getSource(), EntityArgument.getPlayers(c, "targets")))))
/*     */           
/*  42 */           .then(
/*  43 */             Commands.literal("reset")
/*  44 */             .executes(c -> resetTitle((CommandSourceStack)c.getSource(), EntityArgument.getPlayers(c, "targets")))))
/*     */           
/*  46 */           .then(
/*  47 */             Commands.literal("title")
/*  48 */             .then(
/*  49 */               Commands.argument("title", (ArgumentType)ComponentArgument.textComponent(context))
/*  50 */               .executes(c -> showTitle((CommandSourceStack)c.getSource(), EntityArgument.getPlayers(c, "targets"), ComponentArgument.getRawComponent(c, "title"), "title", net.minecraft.network.protocol.game.ClientboundSetTitleTextPacket::new)))))
/*     */           
/*  53 */           .then(
/*  54 */             Commands.literal("subtitle")
/*  55 */             .then(
/*  56 */               Commands.argument("title", (ArgumentType)ComponentArgument.textComponent(context))
/*  57 */               .executes(c -> showTitle((CommandSourceStack)c.getSource(), EntityArgument.getPlayers(c, "targets"), ComponentArgument.getRawComponent(c, "title"), "subtitle", net.minecraft.network.protocol.game.ClientboundSetSubtitleTextPacket::new)))))
/*     */           
/*  60 */           .then(
/*  61 */             Commands.literal("actionbar")
/*  62 */             .then(
/*  63 */               Commands.argument("title", (ArgumentType)ComponentArgument.textComponent(context))
/*  64 */               .executes(c -> showTitle((CommandSourceStack)c.getSource(), EntityArgument.getPlayers(c, "targets"), ComponentArgument.getRawComponent(c, "title"), "actionbar", net.minecraft.network.protocol.game.ClientboundSetActionBarTextPacket::new)))))
/*     */           
/*  67 */           .then(
/*  68 */             Commands.literal("times")
/*  69 */             .then(
/*  70 */               Commands.argument("fadeIn", (ArgumentType)TimeArgument.time())
/*  71 */               .then(
/*  72 */                 Commands.argument("stay", (ArgumentType)TimeArgument.time())
/*  73 */                 .then(
/*  74 */                   Commands.argument("fadeOut", (ArgumentType)TimeArgument.time())
/*  75 */                   .executes(c -> setTimes((CommandSourceStack)c.getSource(), EntityArgument.getPlayers(c, "targets"), IntegerArgumentType.getInteger(c, "fadeIn"), IntegerArgumentType.getInteger(c, "stay"), IntegerArgumentType.getInteger(c, "fadeOut")))))))));
/*     */   }
/*     */   
/*     */   private static int clearTitle(CommandSourceStack source, Collection<ServerPlayer> targets) {
/*  85 */     ClientboundClearTitlesPacket packet = new ClientboundClearTitlesPacket(false);
/*  86 */     for (ServerPlayer player : targets)
/*  87 */       player.connection.send((Packet)packet); 
/*  90 */     if (targets.size() == 1) {
/*  91 */       source.sendSuccess(() -> Component.translatable("commands.title.cleared.single", new Object[] { ((ServerPlayer)targets.iterator().next()).getDisplayName() }), true);
/*     */     } else {
/*  93 */       source.sendSuccess(() -> Component.translatable("commands.title.cleared.multiple", new Object[] { Integer.valueOf(targets.size()) }), true);
/*     */     } 
/*  96 */     return targets.size();
/*     */   }
/*     */   
/*     */   private static int resetTitle(CommandSourceStack source, Collection<ServerPlayer> targets) {
/* 100 */     ClientboundClearTitlesPacket packet = new ClientboundClearTitlesPacket(true);
/* 101 */     for (ServerPlayer player : targets)
/* 102 */       player.connection.send((Packet)packet); 
/* 105 */     if (targets.size() == 1) {
/* 106 */       source.sendSuccess(() -> Component.translatable("commands.title.reset.single", new Object[] { ((ServerPlayer)targets.iterator().next()).getDisplayName() }), true);
/*     */     } else {
/* 108 */       source.sendSuccess(() -> Component.translatable("commands.title.reset.multiple", new Object[] { Integer.valueOf(targets.size()) }), true);
/*     */     } 
/* 111 */     return targets.size();
/*     */   }
/*     */   
/*     */   private static int showTitle(CommandSourceStack source, Collection<ServerPlayer> targets, Component title, String type, Function<Component, Packet<?>> factory) throws CommandSyntaxException {
/* 115 */     for (ServerPlayer player : targets)
/* 116 */       player.connection.send(factory.apply(ComponentUtils.resolve(ResolutionContext.builder().withSource(source).withEntityOverride((Entity)player).build(), title))); 
/* 119 */     if (targets.size() == 1) {
/* 120 */       source.sendSuccess(() -> Component.translatable("commands.title.show." + type + ".single", new Object[] { ((ServerPlayer)targets.iterator().next()).getDisplayName() }), true);
/*     */     } else {
/* 122 */       source.sendSuccess(() -> Component.translatable("commands.title.show." + type + ".multiple", new Object[] { Integer.valueOf(targets.size()) }), true);
/*     */     } 
/* 125 */     return targets.size();
/*     */   }
/*     */   
/*     */   private static int setTimes(CommandSourceStack source, Collection<ServerPlayer> targets, int fadeIn, int stay, int fadeOut) {
/* 129 */     ClientboundSetTitlesAnimationPacket packet = new ClientboundSetTitlesAnimationPacket(fadeIn, stay, fadeOut);
/* 130 */     for (ServerPlayer player : targets)
/* 131 */       player.connection.send((Packet)packet); 
/* 134 */     if (targets.size() == 1) {
/* 135 */       source.sendSuccess(() -> Component.translatable("commands.title.times.single", new Object[] { ((ServerPlayer)targets.iterator().next()).getDisplayName() }), true);
/*     */     } else {
/* 137 */       source.sendSuccess(() -> Component.translatable("commands.title.times.multiple", new Object[] { Integer.valueOf(targets.size()) }), true);
/*     */     } 
/* 140 */     return targets.size();
/*     */   }
/*     */ }


/* Location:              C:\Users\karol\AppData\Roaming\ModrinthApp\meta\versions\26.2-0.19.5\26.2-0.19.5.jar!\net\minecraft\server\commands\TitleCommand.class
 * Java compiler version: 25 (69.0)
 * JD-Core Version:       1.1.3
 */