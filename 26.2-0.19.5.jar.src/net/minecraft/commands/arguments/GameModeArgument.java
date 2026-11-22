/*    */ package net.minecraft.commands.arguments;
/*    */ 
/*    */ import com.mojang.brigadier.Message;
/*    */ import com.mojang.brigadier.StringReader;
/*    */ import com.mojang.brigadier.arguments.ArgumentType;
/*    */ import com.mojang.brigadier.context.CommandContext;
/*    */ import com.mojang.brigadier.exceptions.CommandSyntaxException;
/*    */ import com.mojang.brigadier.exceptions.DynamicCommandExceptionType;
/*    */ import com.mojang.brigadier.suggestion.Suggestions;
/*    */ import com.mojang.brigadier.suggestion.SuggestionsBuilder;
/*    */ import java.util.Arrays;
/*    */ import java.util.Collection;
/*    */ import java.util.concurrent.CompletableFuture;
/*    */ import java.util.stream.Collectors;
/*    */ import java.util.stream.Stream;
/*    */ import net.minecraft.commands.CommandSourceStack;
/*    */ import net.minecraft.commands.SharedSuggestionProvider;
/*    */ import net.minecraft.network.chat.Component;
/*    */ import net.minecraft.world.level.GameType;
/*    */ 
/*    */ public class GameModeArgument implements ArgumentType<GameType> {
/* 22 */   private static final Collection<String> EXAMPLES = (Collection<String>)Stream.<GameType>of(new GameType[] { GameType.SURVIVAL, GameType.CREATIVE }).map(GameType::getName).collect(Collectors.toList());
/*    */   
/* 23 */   private static final GameType[] VALUES = GameType.values();
/*    */   
/*    */   private static final DynamicCommandExceptionType ERROR_INVALID;
/*    */   
/*    */   static {
/* 25 */     ERROR_INVALID = new DynamicCommandExceptionType(value -> Component.translatableEscape("argument.gamemode.invalid", new Object[] { value }));
/*    */   }
/*    */   
/*    */   public GameType parse(StringReader reader) throws CommandSyntaxException {
/* 29 */     String gameTypeString = reader.readUnquotedString();
/* 30 */     GameType gameType = GameType.byName(gameTypeString, null);
/* 31 */     if (gameType == null)
/* 32 */       throw ERROR_INVALID.createWithContext(reader, gameTypeString); 
/* 34 */     return gameType;
/*    */   }
/*    */   
/*    */   public <S> CompletableFuture<Suggestions> listSuggestions(CommandContext<S> context, SuggestionsBuilder builder) {
/* 39 */     if (context.getSource() instanceof SharedSuggestionProvider)
/* 40 */       return SharedSuggestionProvider.suggest(Arrays.<GameType>stream(VALUES).map(GameType::getName), builder); 
/* 42 */     return Suggestions.empty();
/*    */   }
/*    */   
/*    */   public Collection<String> getExamples() {
/* 47 */     return EXAMPLES;
/*    */   }
/*    */   
/*    */   public static GameModeArgument gameMode() {
/* 51 */     return new GameModeArgument();
/*    */   }
/*    */   
/*    */   public static GameType getGameMode(CommandContext<CommandSourceStack> context, String name) throws CommandSyntaxException {
/* 55 */     return (GameType)context.getArgument(name, GameType.class);
/*    */   }
/*    */ }


/* Location:              C:\Users\karol\AppData\Roaming\ModrinthApp\meta\versions\26.2-0.19.5\26.2-0.19.5.jar!\net\minecraft\commands\arguments\GameModeArgument.class
 * Java compiler version: 25 (69.0)
 * JD-Core Version:       1.1.3
 */