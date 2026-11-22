/*    */ package net.minecraft.server.commands;
/*    */ 
/*    */ import com.google.common.collect.ImmutableList;
/*    */ import com.mojang.brigadier.CommandDispatcher;
/*    */ import com.mojang.brigadier.arguments.ArgumentType;
/*    */ import com.mojang.brigadier.builder.LiteralArgumentBuilder;
/*    */ import com.mojang.brigadier.context.CommandContext;
/*    */ import com.mojang.brigadier.exceptions.CommandSyntaxException;
/*    */ import java.util.Collection;
/*    */ import java.util.function.Predicate;
/*    */ import net.minecraft.commands.CommandSourceStack;
/*    */ import net.minecraft.commands.Commands;
/*    */ import net.minecraft.commands.arguments.EntityArgument;
/*    */ import net.minecraft.network.chat.Component;
/*    */ import net.minecraft.world.entity.Entity;
/*    */ 
/*    */ public class KillCommand {
/*    */   public static void register(CommandDispatcher<CommandSourceStack> dispatcher) {
/* 19 */     dispatcher.register(
/* 20 */         (LiteralArgumentBuilder)((LiteralArgumentBuilder)((LiteralArgumentBuilder)Commands.literal("kill")
/* 21 */         .requires((Predicate)Commands.hasPermission(Commands.LEVEL_GAMEMASTERS)))
/* 22 */         .executes(c -> kill((CommandSourceStack)c.getSource(), (Collection<? extends Entity>)ImmutableList.of(((CommandSourceStack)c.getSource()).getEntityOrException()))))
/* 23 */         .then(
/* 24 */           Commands.argument("targets", (ArgumentType)EntityArgument.entities())
/* 25 */           .executes(c -> kill((CommandSourceStack)c.getSource(), EntityArgument.getEntities(c, "targets")))));
/*    */   }
/*    */   
/*    */   private static int kill(CommandSourceStack source, Collection<? extends Entity> victims) {
/* 31 */     for (Entity entity : victims)
/* 32 */       entity.kill(source.getLevel()); 
/* 35 */     if (victims.size() == 1) {
/* 36 */       source.sendSuccess(() -> Component.translatable("commands.kill.success.single", new Object[] { ((Entity)victims.iterator().next()).getDisplayName() }), true);
/*    */     } else {
/* 38 */       source.sendSuccess(() -> Component.translatable("commands.kill.success.multiple", new Object[] { Integer.valueOf(victims.size()) }), true);
/*    */     } 
/* 41 */     return victims.size();
/*    */   }
/*    */ }


/* Location:              C:\Users\karol\AppData\Roaming\ModrinthApp\meta\versions\26.2-0.19.5\26.2-0.19.5.jar!\net\minecraft\server\commands\KillCommand.class
 * Java compiler version: 25 (69.0)
 * JD-Core Version:       1.1.3
 */