/*    */ package net.minecraft.server.commands;
/*    */ 
/*    */ import com.mojang.brigadier.CommandDispatcher;
/*    */ import com.mojang.brigadier.arguments.ArgumentType;
/*    */ import com.mojang.brigadier.arguments.IntegerArgumentType;
/*    */ import com.mojang.brigadier.builder.LiteralArgumentBuilder;
/*    */ import com.mojang.brigadier.builder.RequiredArgumentBuilder;
/*    */ import com.mojang.brigadier.context.CommandContext;
/*    */ import com.mojang.brigadier.exceptions.CommandSyntaxException;
/*    */ import java.util.Collection;
/*    */ import java.util.function.Predicate;
/*    */ import net.minecraft.commands.CommandBuildContext;
/*    */ import net.minecraft.commands.CommandSourceStack;
/*    */ import net.minecraft.commands.Commands;
/*    */ import net.minecraft.commands.arguments.EntityArgument;
/*    */ import net.minecraft.commands.arguments.item.ItemArgument;
/*    */ import net.minecraft.commands.arguments.item.ItemInput;
/*    */ import net.minecraft.network.chat.Component;
/*    */ import net.minecraft.server.level.ServerPlayer;
/*    */ import net.minecraft.sounds.SoundEvents;
/*    */ import net.minecraft.sounds.SoundSource;
/*    */ import net.minecraft.world.entity.item.ItemEntity;
/*    */ import net.minecraft.world.item.ItemStack;
/*    */ 
/*    */ public class GiveCommand {
/*    */   public static final int MAX_ALLOWED_ITEMSTACKS = 100;
/*    */   
/*    */   public static void register(CommandDispatcher<CommandSourceStack> dispatcher, CommandBuildContext context) {
/* 32 */     dispatcher.register(
/* 33 */         (LiteralArgumentBuilder)((LiteralArgumentBuilder)Commands.literal("give")
/* 34 */         .requires((Predicate)Commands.hasPermission(Commands.LEVEL_GAMEMASTERS)))
/* 35 */         .then(
/* 36 */           Commands.argument("targets", (ArgumentType)EntityArgument.players())
/* 37 */           .then((
/* 38 */             (RequiredArgumentBuilder)Commands.argument("item", (ArgumentType)ItemArgument.item(context))
/* 39 */             .executes(c -> giveItem((CommandSourceStack)c.getSource(), ItemArgument.getItem(c, "item"), EntityArgument.getPlayers(c, "targets"), 1)))
/* 40 */             .then(
/* 41 */               Commands.argument("count", (ArgumentType)IntegerArgumentType.integer(1))
/* 42 */               .executes(c -> giveItem((CommandSourceStack)c.getSource(), ItemArgument.getItem(c, "item"), EntityArgument.getPlayers(c, "targets"), IntegerArgumentType.getInteger(c, "count")))))));
/*    */   }
/*    */   
/*    */   private static int giveItem(CommandSourceStack source, ItemInput input, Collection<ServerPlayer> players, int count) throws CommandSyntaxException {
/* 50 */     ItemStack prototypeItemStack = input.createItemStack(1);
/* 51 */     int maxStackSize = prototypeItemStack.getMaxStackSize();
/* 52 */     int maxAllowedCount = maxStackSize * 100;
/* 53 */     if (count > maxAllowedCount) {
/* 54 */       source.sendFailure((Component)Component.translatable("commands.give.failed.toomanyitems", new Object[] { Integer.valueOf(maxAllowedCount), prototypeItemStack.getDisplayName() }));
/* 55 */       return 0;
/*    */     } 
/* 57 */     for (ServerPlayer player : players) {
/* 58 */       int remaining = count;
/* 59 */       while (remaining > 0) {
/* 60 */         int size = Math.min(maxStackSize, remaining);
/* 61 */         remaining -= size;
/* 63 */         ItemStack copyToDrop = prototypeItemStack.copyWithCount(size);
/* 64 */         boolean added = player.getInventory().add(copyToDrop);
/* 66 */         if (!added || !copyToDrop.isEmpty()) {
/* 67 */           ItemEntity itemEntity = player.drop(copyToDrop, false);
/* 68 */           if (itemEntity != null) {
/* 69 */             itemEntity.setNoPickUpDelay();
/* 70 */             itemEntity.setTarget(player.getUUID());
/*    */           } 
/*    */           continue;
/*    */         } 
/* 74 */         ItemEntity drop = player.drop(prototypeItemStack.copy(), false);
/* 75 */         if (drop != null)
/* 76 */           drop.makeFakeItem(); 
/* 78 */         player.level().playSound(null, player.getX(), player.getY(), player.getZ(), SoundEvents.ITEM_PICKUP, SoundSource.PLAYERS, 0.2F, ((player.getRandom().nextFloat() - player.getRandom().nextFloat()) * 0.7F + 1.0F) * 2.0F);
/* 79 */         player.containerMenu.broadcastChanges();
/*    */       } 
/*    */     } 
/* 84 */     if (players.size() == 1) {
/* 85 */       source.sendSuccess(() -> Component.translatable("commands.give.success.single", new Object[] { Integer.valueOf(count), prototypeItemStack.getDisplayName(), ((ServerPlayer)players.iterator().next()).getDisplayName() }), true);
/*    */     } else {
/* 87 */       source.sendSuccess(() -> Component.translatable("commands.give.success.single", new Object[] { Integer.valueOf(count), prototypeItemStack.getDisplayName(), Integer.valueOf(players.size()) }), true);
/*    */     } 
/* 90 */     return players.size();
/*    */   }
/*    */ }


/* Location:              C:\Users\karol\AppData\Roaming\ModrinthApp\meta\versions\26.2-0.19.5\26.2-0.19.5.jar!\net\minecraft\server\commands\GiveCommand.class
 * Java compiler version: 25 (69.0)
 * JD-Core Version:       1.1.3
 */