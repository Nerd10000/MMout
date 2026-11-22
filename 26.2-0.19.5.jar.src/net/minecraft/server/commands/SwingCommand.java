/*    */ package net.minecraft.server.commands;
/*    */ 
/*    */ import com.mojang.brigadier.CommandDispatcher;
/*    */ import com.mojang.brigadier.Message;
/*    */ import com.mojang.brigadier.arguments.ArgumentType;
/*    */ import com.mojang.brigadier.builder.LiteralArgumentBuilder;
/*    */ import com.mojang.brigadier.builder.RequiredArgumentBuilder;
/*    */ import com.mojang.brigadier.context.CommandContext;
/*    */ import com.mojang.brigadier.exceptions.CommandSyntaxException;
/*    */ import com.mojang.brigadier.exceptions.SimpleCommandExceptionType;
/*    */ import java.util.Collection;
/*    */ import java.util.List;
/*    */ import java.util.function.Predicate;
/*    */ import net.minecraft.commands.CommandSourceStack;
/*    */ import net.minecraft.commands.Commands;
/*    */ import net.minecraft.commands.arguments.EntityArgument;
/*    */ import net.minecraft.network.chat.Component;
/*    */ import net.minecraft.world.InteractionHand;
/*    */ import net.minecraft.world.entity.Entity;
/*    */ import net.minecraft.world.entity.LivingEntity;
/*    */ 
/*    */ public class SwingCommand {
/* 22 */   private static final SimpleCommandExceptionType ERROR_NO_LIVING_ENTITY = new SimpleCommandExceptionType((Message)Component.translatable("commands.swing.failed.notliving"));
/*    */   
/*    */   public static void register(CommandDispatcher<CommandSourceStack> dispatcher) {
/* 25 */     dispatcher.register(
/* 26 */         (LiteralArgumentBuilder)((LiteralArgumentBuilder)((LiteralArgumentBuilder)Commands.literal("swing")
/* 27 */         .requires((Predicate)Commands.hasPermission(Commands.LEVEL_GAMEMASTERS)))
/* 28 */         .executes(c -> swing((CommandSourceStack)c.getSource(), List.of(((CommandSourceStack)c.getSource()).getEntityOrException()), InteractionHand.MAIN_HAND)))
/* 29 */         .then((
/* 30 */           (RequiredArgumentBuilder)((RequiredArgumentBuilder)Commands.argument("targets", (ArgumentType)EntityArgument.entities())
/* 31 */           .executes(c -> swing((CommandSourceStack)c.getSource(), EntityArgument.getEntities(c, "targets"), InteractionHand.MAIN_HAND)))
/* 32 */           .then(
/* 33 */             Commands.literal("mainhand")
/* 34 */             .executes(c -> swing((CommandSourceStack)c.getSource(), EntityArgument.getEntities(c, "targets"), InteractionHand.MAIN_HAND))))
/*    */           
/* 36 */           .then(
/* 37 */             Commands.literal("offhand")
/* 38 */             .executes(c -> swing((CommandSourceStack)c.getSource(), EntityArgument.getEntities(c, "targets"), InteractionHand.OFF_HAND)))));
/*    */   }
/*    */   
/*    */   private static int swing(CommandSourceStack source, Collection<? extends Entity> targets, InteractionHand hand) throws CommandSyntaxException {
/* 45 */     int livingEntitiesCount = 0;
/* 47 */     for (Entity entity : targets) {
/* 48 */       if (entity instanceof LivingEntity) {
/* 48 */         LivingEntity livingEntity = (LivingEntity)entity;
/* 49 */         livingEntity.swing(hand, true);
/* 50 */         livingEntitiesCount++;
/*    */       } 
/*    */     } 
/* 54 */     if (livingEntitiesCount == 0)
/* 55 */       throw ERROR_NO_LIVING_ENTITY.create(); 
/* 58 */     if (livingEntitiesCount == 1) {
/* 59 */       source.sendSuccess(() -> Component.translatable("commands.swing.success.single", new Object[] { ((Entity)targets.iterator().next()).getDisplayName() }), true);
/*    */     } else {
/* 61 */       int count = livingEntitiesCount;
/* 62 */       source.sendSuccess(() -> Component.translatable("commands.swing.success.multiple", new Object[] { Integer.valueOf(count) }), true);
/*    */     } 
/* 65 */     return livingEntitiesCount;
/*    */   }
/*    */ }


/* Location:              C:\Users\karol\AppData\Roaming\ModrinthApp\meta\versions\26.2-0.19.5\26.2-0.19.5.jar!\net\minecraft\server\commands\SwingCommand.class
 * Java compiler version: 25 (69.0)
 * JD-Core Version:       1.1.3
 */