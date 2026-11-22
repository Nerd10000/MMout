/*    */ package net.minecraft.commands.arguments;
/*    */ 
/*    */ import com.mojang.brigadier.exceptions.CommandSyntaxException;
/*    */ import java.util.ArrayList;
/*    */ import java.util.Collection;
/*    */ import java.util.List;
/*    */ import net.minecraft.commands.CommandSourceStack;
/*    */ import net.minecraft.commands.arguments.selector.EntitySelector;
/*    */ import net.minecraft.server.level.ServerPlayer;
/*    */ import net.minecraft.server.players.NameAndId;
/*    */ 
/*    */ public class SelectorResult implements GameProfileArgument.Result {
/*    */   private final EntitySelector selector;
/*    */   
/*    */   public SelectorResult(EntitySelector selector) {
/* 80 */     this.selector = selector;
/*    */   }
/*    */   
/*    */   public Collection<NameAndId> getNames(CommandSourceStack sender) throws CommandSyntaxException {
/* 85 */     List<ServerPlayer> players = this.selector.findPlayers(sender);
/* 86 */     if (players.isEmpty())
/* 87 */       throw EntityArgument.NO_PLAYERS_FOUND.create(); 
/* 89 */     List<NameAndId> result = new ArrayList<>();
/* 90 */     for (ServerPlayer entity : players)
/* 91 */       result.add(entity.nameAndId()); 
/* 93 */     return result;
/*    */   }
/*    */ }


/* Location:              C:\Users\karol\AppData\Roaming\ModrinthApp\meta\versions\26.2-0.19.5\26.2-0.19.5.jar!\net\minecraft\commands\arguments\GameProfileArgument$SelectorResult.class
 * Java compiler version: 25 (69.0)
 * JD-Core Version:       1.1.3
 */