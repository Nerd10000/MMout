/*    */ package net.minecraft.server.commands;
/*    */ 
/*    */ import com.mojang.brigadier.CommandDispatcher;
/*    */ import com.mojang.brigadier.Message;
/*    */ import com.mojang.brigadier.builder.LiteralArgumentBuilder;
/*    */ import com.mojang.brigadier.context.CommandContext;
/*    */ import com.mojang.brigadier.exceptions.CommandSyntaxException;
/*    */ import com.mojang.brigadier.exceptions.DynamicCommandExceptionType;
/*    */ import java.util.function.Predicate;
/*    */ import net.minecraft.commands.CommandSourceStack;
/*    */ import net.minecraft.commands.Commands;
/*    */ import net.minecraft.network.chat.Component;
/*    */ import net.minecraft.server.MinecraftServer;
/*    */ import net.minecraft.world.Difficulty;
/*    */ 
/*    */ public class DifficultyCommand {
/*    */   private static final DynamicCommandExceptionType ERROR_ALREADY_SAME_DIFFICULTY;
/*    */   
/*    */   static {
/* 16 */     ERROR_ALREADY_SAME_DIFFICULTY = new DynamicCommandExceptionType(difficulty -> Component.translatableEscape("commands.difficulty.failure", new Object[] { difficulty }));
/*    */   }
/*    */   
/*    */   public static void register(CommandDispatcher<CommandSourceStack> dispatcher) {
/* 19 */     LiteralArgumentBuilder<CommandSourceStack> command = Commands.literal("difficulty");
/* 21 */     for (Difficulty difficulty : Difficulty.values())
/* 22 */       command.then(Commands.literal(difficulty.getSerializedName()).executes(c -> setDifficulty((CommandSourceStack)c.getSource(), difficulty))); 
/* 25 */     dispatcher.register((LiteralArgumentBuilder)((LiteralArgumentBuilder)command
/*    */         
/* 27 */         .requires((Predicate)Commands.hasPermission(Commands.LEVEL_GAMEMASTERS)))
/* 28 */         .executes(c -> {
/*    */             Difficulty difficulty = ((CommandSourceStack)c.getSource()).getLevel().getDifficulty();
/*    */             ((CommandSourceStack)c.getSource()).sendSuccess((), false);
/*    */             return difficulty.getId();
/*    */           }));
/*    */   }
/*    */   
/*    */   public static int setDifficulty(CommandSourceStack source, Difficulty difficulty) throws CommandSyntaxException {
/* 37 */     MinecraftServer server = source.getServer();
/* 38 */     if (server.getWorldData().getDifficulty() == difficulty)
/* 39 */       throw ERROR_ALREADY_SAME_DIFFICULTY.create(difficulty.getDisplayName()); 
/* 42 */     server.setDifficulty(difficulty, true);
/* 43 */     source.sendSuccess(() -> Component.translatable("commands.difficulty.success", new Object[] { difficulty.getDisplayName() }), true);
/* 45 */     return 0;
/*    */   }
/*    */ }


/* Location:              C:\Users\karol\AppData\Roaming\ModrinthApp\meta\versions\26.2-0.19.5\26.2-0.19.5.jar!\net\minecraft\server\commands\DifficultyCommand.class
 * Java compiler version: 25 (69.0)
 * JD-Core Version:       1.1.3
 */