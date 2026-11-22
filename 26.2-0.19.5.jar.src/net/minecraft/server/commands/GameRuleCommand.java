/*    */ package net.minecraft.server.commands;
/*    */ 
/*    */ import com.mojang.brigadier.CommandDispatcher;
/*    */ import com.mojang.brigadier.builder.ArgumentBuilder;
/*    */ import com.mojang.brigadier.builder.LiteralArgumentBuilder;
/*    */ import com.mojang.brigadier.context.CommandContext;
/*    */ import com.mojang.brigadier.exceptions.CommandSyntaxException;
/*    */ import java.util.function.Predicate;
/*    */ import net.minecraft.commands.CommandBuildContext;
/*    */ import net.minecraft.commands.CommandSourceStack;
/*    */ import net.minecraft.commands.Commands;
/*    */ import net.minecraft.network.chat.Component;
/*    */ import net.minecraft.world.level.gamerules.GameRule;
/*    */ import net.minecraft.world.level.gamerules.GameRuleTypeVisitor;
/*    */ import net.minecraft.world.level.gamerules.GameRules;
/*    */ 
/*    */ public class GameRuleCommand {
/*    */   public static void register(CommandDispatcher<CommandSourceStack> dispatcher, CommandBuildContext context) {
/* 19 */     final LiteralArgumentBuilder<CommandSourceStack> base = (LiteralArgumentBuilder<CommandSourceStack>)Commands.literal("gamerule").requires((Predicate)Commands.hasPermission(Commands.LEVEL_GAMEMASTERS));
/* 21 */     (new GameRules(context.enabledFeatures())).visitGameRuleTypes(new GameRuleTypeVisitor() {
/*    */           public <T> void visit(GameRule<T> gameRule) {
/* 24 */             LiteralArgumentBuilder<CommandSourceStack> unqualified = Commands.literal(gameRule.id());
/* 25 */             LiteralArgumentBuilder<CommandSourceStack> qualified = Commands.literal(gameRule.getIdentifier().toString());
/* 26 */             ((LiteralArgumentBuilder)base
/* 27 */               .then((ArgumentBuilder)GameRuleCommand.buildRuleArguments(gameRule, unqualified)))
/* 28 */               .then((ArgumentBuilder)GameRuleCommand.buildRuleArguments(gameRule, qualified));
/*    */           }
/*    */         });
/* 32 */     dispatcher.register(base);
/*    */   }
/*    */   
/*    */   private static <T> LiteralArgumentBuilder<CommandSourceStack> buildRuleArguments(GameRule<T> gameRule, LiteralArgumentBuilder<CommandSourceStack> ruleLiteral) {
/* 36 */     return (LiteralArgumentBuilder<CommandSourceStack>)((LiteralArgumentBuilder)ruleLiteral
/* 37 */       .executes(c -> queryRule((CommandSourceStack)c.getSource(), gameRule)))
/* 38 */       .then(
/* 39 */         Commands.argument("value", gameRule.argument())
/* 40 */         .executes(c -> setRule(c, gameRule)));
/*    */   }
/*    */   
/*    */   private static <T> int setRule(CommandContext<CommandSourceStack> context, GameRule<T> gameRule) {
/* 45 */     CommandSourceStack source = (CommandSourceStack)context.getSource();
/* 46 */     T value = (T)context.getArgument("value", gameRule.valueClass());
/* 47 */     source.getLevel().getGameRules().set(gameRule, value, ((CommandSourceStack)context.getSource()).getServer());
/* 48 */     source.sendSuccess(() -> Component.translatable("commands.gamerule.set", new Object[] { gameRule.id(), gameRule.serialize(value) }), true);
/* 49 */     return gameRule.getCommandResult(value);
/*    */   }
/*    */   
/*    */   private static <T> int queryRule(CommandSourceStack source, GameRule<T> gameRule) {
/* 53 */     T value = (T)source.getLevel().getGameRules().get(gameRule);
/* 54 */     source.sendSuccess(() -> Component.translatable("commands.gamerule.query", new Object[] { gameRule.id(), gameRule.serialize(value) }), false);
/* 55 */     return gameRule.getCommandResult(value);
/*    */   }
/*    */ }


/* Location:              C:\Users\karol\AppData\Roaming\ModrinthApp\meta\versions\26.2-0.19.5\26.2-0.19.5.jar!\net\minecraft\server\commands\GameRuleCommand.class
 * Java compiler version: 25 (69.0)
 * JD-Core Version:       1.1.3
 */