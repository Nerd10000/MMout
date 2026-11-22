/*    */ package net.minecraft.server.commands;
/*    */ 
/*    */ import com.mojang.brigadier.CommandDispatcher;
/*    */ import com.mojang.brigadier.Message;
/*    */ import com.mojang.brigadier.builder.LiteralArgumentBuilder;
/*    */ import com.mojang.brigadier.context.CommandContext;
/*    */ import com.mojang.brigadier.exceptions.CommandSyntaxException;
/*    */ import com.mojang.brigadier.exceptions.SimpleCommandExceptionType;
/*    */ import java.util.function.Predicate;
/*    */ import net.minecraft.commands.CommandSourceStack;
/*    */ import net.minecraft.commands.Commands;
/*    */ import net.minecraft.network.chat.Component;
/*    */ 
/*    */ public class SaveOffCommand {
/* 12 */   private static final SimpleCommandExceptionType ERROR_ALREADY_OFF = new SimpleCommandExceptionType((Message)Component.translatable("commands.save.alreadyOff"));
/*    */   
/*    */   public static void register(CommandDispatcher<CommandSourceStack> dispatcher) {
/* 15 */     dispatcher.register(
/* 16 */         (LiteralArgumentBuilder)((LiteralArgumentBuilder)Commands.literal("save-off")
/* 17 */         .requires((Predicate)Commands.hasPermission(Commands.LEVEL_OWNERS)))
/* 18 */         .executes(c -> {
/*    */             CommandSourceStack source = (CommandSourceStack)c.getSource();
/*    */             boolean success = source.getServer().setAutoSave(false);
/*    */             if (!success)
/*    */               throw ERROR_ALREADY_OFF.create(); 
/*    */             source.sendSuccess((), true);
/*    */             return 1;
/*    */           }));
/*    */   }
/*    */ }


/* Location:              C:\Users\karol\AppData\Roaming\ModrinthApp\meta\versions\26.2-0.19.5\26.2-0.19.5.jar!\net\minecraft\server\commands\SaveOffCommand.class
 * Java compiler version: 25 (69.0)
 * JD-Core Version:       1.1.3
 */