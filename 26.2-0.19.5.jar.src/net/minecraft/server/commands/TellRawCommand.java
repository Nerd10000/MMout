/*    */ package net.minecraft.server.commands;
/*    */ 
/*    */ import com.mojang.brigadier.CommandDispatcher;
/*    */ import com.mojang.brigadier.arguments.ArgumentType;
/*    */ import com.mojang.brigadier.builder.LiteralArgumentBuilder;
/*    */ import com.mojang.brigadier.context.CommandContext;
/*    */ import com.mojang.brigadier.exceptions.CommandSyntaxException;
/*    */ import java.util.function.Predicate;
/*    */ import net.minecraft.commands.CommandBuildContext;
/*    */ import net.minecraft.commands.CommandSourceStack;
/*    */ import net.minecraft.commands.Commands;
/*    */ import net.minecraft.commands.arguments.ComponentArgument;
/*    */ import net.minecraft.commands.arguments.EntityArgument;
/*    */ import net.minecraft.server.level.ServerPlayer;
/*    */ import net.minecraft.world.entity.Entity;
/*    */ 
/*    */ public class TellRawCommand {
/*    */   public static void register(CommandDispatcher<CommandSourceStack> dispatcher, CommandBuildContext context) {
/* 18 */     dispatcher.register(
/* 19 */         (LiteralArgumentBuilder)((LiteralArgumentBuilder)Commands.literal("tellraw")
/* 20 */         .requires((Predicate)Commands.hasPermission(Commands.LEVEL_GAMEMASTERS)))
/* 21 */         .then(
/* 22 */           Commands.argument("targets", (ArgumentType)EntityArgument.players())
/* 23 */           .then(
/* 24 */             Commands.argument("message", (ArgumentType)ComponentArgument.textComponent(context))
/* 25 */             .executes(c -> {
/*    */                 int result = 0;
/*    */                 for (ServerPlayer player : EntityArgument.getPlayers(c, "targets")) {
/*    */                   player.sendSystemMessage(ComponentArgument.getResolvedComponent(c, "message", (Entity)player));
/*    */                   result++;
/*    */                 } 
/*    */                 return result;
/*    */               }))));
/*    */   }
/*    */ }


/* Location:              C:\Users\karol\AppData\Roaming\ModrinthApp\meta\versions\26.2-0.19.5\26.2-0.19.5.jar!\net\minecraft\server\commands\TellRawCommand.class
 * Java compiler version: 25 (69.0)
 * JD-Core Version:       1.1.3
 */