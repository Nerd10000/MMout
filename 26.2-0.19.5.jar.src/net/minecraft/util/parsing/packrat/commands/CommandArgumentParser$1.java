/*    */ package net.minecraft.util.parsing.packrat.commands;
/*    */ 
/*    */ import com.mojang.brigadier.StringReader;
/*    */ import com.mojang.brigadier.exceptions.CommandSyntaxException;
/*    */ import com.mojang.brigadier.suggestion.Suggestions;
/*    */ import com.mojang.brigadier.suggestion.SuggestionsBuilder;
/*    */ import java.util.concurrent.CompletableFuture;
/*    */ import java.util.function.Function;
/*    */ 
/*    */ class null implements CommandArgumentParser<S> {
/*    */   public S parseForCommands(StringReader reader) throws CommandSyntaxException {
/* 24 */     return (S)mapper.apply(CommandArgumentParser.this.parseForCommands(reader));
/*    */   }
/*    */   
/*    */   public CompletableFuture<Suggestions> parseForSuggestions(SuggestionsBuilder suggestionsBuilder) {
/* 29 */     return CommandArgumentParser.this.parseForSuggestions(suggestionsBuilder);
/*    */   }
/*    */ }


/* Location:              C:\Users\karol\AppData\Roaming\ModrinthApp\meta\versions\26.2-0.19.5\26.2-0.19.5.jar!\net\minecraf\\util\parsing\packrat\commands\CommandArgumentParser$1.class
 * Java compiler version: 25 (69.0)
 * JD-Core Version:       1.1.3
 */