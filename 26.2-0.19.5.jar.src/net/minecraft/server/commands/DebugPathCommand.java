/*    */ package net.minecraft.server.commands;
/*    */ 
/*    */ import com.mojang.brigadier.CommandDispatcher;
/*    */ import com.mojang.brigadier.Message;
/*    */ import com.mojang.brigadier.arguments.ArgumentType;
/*    */ import com.mojang.brigadier.builder.LiteralArgumentBuilder;
/*    */ import com.mojang.brigadier.context.CommandContext;
/*    */ import com.mojang.brigadier.exceptions.CommandSyntaxException;
/*    */ import com.mojang.brigadier.exceptions.SimpleCommandExceptionType;
/*    */ import java.util.function.Predicate;
/*    */ import net.minecraft.commands.CommandSourceStack;
/*    */ import net.minecraft.commands.Commands;
/*    */ import net.minecraft.commands.arguments.coordinates.BlockPosArgument;
/*    */ import net.minecraft.core.BlockPos;
/*    */ import net.minecraft.network.chat.Component;
/*    */ import net.minecraft.world.entity.Entity;
/*    */ import net.minecraft.world.entity.Mob;
/*    */ import net.minecraft.world.entity.ai.navigation.GroundPathNavigation;
/*    */ import net.minecraft.world.level.Level;
/*    */ import net.minecraft.world.level.pathfinder.Path;
/*    */ 
/*    */ public class DebugPathCommand {
/* 22 */   private static final SimpleCommandExceptionType ERROR_NOT_MOB = new SimpleCommandExceptionType((Message)Component.literal("Source is not a mob"));
/*    */   
/* 23 */   private static final SimpleCommandExceptionType ERROR_NO_PATH = new SimpleCommandExceptionType((Message)Component.literal("Path not found"));
/*    */   
/* 24 */   private static final SimpleCommandExceptionType ERROR_NOT_COMPLETE = new SimpleCommandExceptionType((Message)Component.literal("Target not reached"));
/*    */   
/*    */   public static void register(CommandDispatcher<CommandSourceStack> dispatcher) {
/* 27 */     dispatcher.register(
/* 28 */         (LiteralArgumentBuilder)((LiteralArgumentBuilder)Commands.literal("debugpath")
/* 29 */         .requires((Predicate)Commands.hasPermission(Commands.LEVEL_GAMEMASTERS)))
/* 30 */         .then(
/* 31 */           Commands.argument("to", (ArgumentType)BlockPosArgument.blockPos())
/* 32 */           .executes(c -> fillBlocks((CommandSourceStack)c.getSource(), BlockPosArgument.getLoadedBlockPos(c, "to")))));
/*    */   }
/*    */   
/*    */   private static int fillBlocks(CommandSourceStack source, BlockPos target) throws CommandSyntaxException {
/*    */     Mob mob;
/* 38 */     Entity entity = source.getEntity();
/* 39 */     if (entity instanceof Mob) {
/* 39 */       mob = (Mob)entity;
/*    */     } else {
/* 40 */       throw ERROR_NOT_MOB.create();
/*    */     } 
/* 43 */     GroundPathNavigation groundPathNavigation = new GroundPathNavigation(mob, (Level)source.getLevel());
/* 44 */     Path path = groundPathNavigation.createPath(target, 0);
/* 46 */     if (path == null)
/* 47 */       throw ERROR_NO_PATH.create(); 
/* 49 */     if (!path.canReach())
/* 50 */       throw ERROR_NOT_COMPLETE.create(); 
/* 53 */     source.sendSuccess(() -> Component.literal("Made path"), true);
/* 54 */     return 1;
/*    */   }
/*    */ }


/* Location:              C:\Users\karol\AppData\Roaming\ModrinthApp\meta\versions\26.2-0.19.5\26.2-0.19.5.jar!\net\minecraft\server\commands\DebugPathCommand.class
 * Java compiler version: 25 (69.0)
 * JD-Core Version:       1.1.3
 */