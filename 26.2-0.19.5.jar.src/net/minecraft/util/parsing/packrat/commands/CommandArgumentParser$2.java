/*    */ package net.minecraft.util.parsing.packrat.commands;
/*    */ 
/*    */ import com.mojang.brigadier.ImmutableStringReader;
/*    */ import com.mojang.brigadier.StringReader;
/*    */ import com.mojang.brigadier.exceptions.CommandSyntaxException;
/*    */ import com.mojang.brigadier.exceptions.DynamicCommandExceptionType;
/*    */ import com.mojang.brigadier.suggestion.Suggestions;
/*    */ import com.mojang.brigadier.suggestion.SuggestionsBuilder;
/*    */ import com.mojang.serialization.Codec;
/*    */ import com.mojang.serialization.DataResult;
/*    */ import com.mojang.serialization.DynamicOps;
/*    */ import java.util.concurrent.CompletableFuture;
/*    */ 
/*    */ class null implements CommandArgumentParser<T> {
/*    */   public T parseForCommands(StringReader reader) throws CommandSyntaxException {
/* 38 */     int cursor = reader.getCursor();
/* 39 */     O tag = valueParser.parseForCommands(reader);
/* 40 */     DataResult<T> result = codec.parse(ops, tag);
/* 41 */     return (T)result.getOrThrow(message -> {
/*    */           reader.setCursor(cursor);
/*    */           return exceptionType.createWithContext((ImmutableStringReader)reader, message);
/*    */         });
/*    */   }
/*    */   
/*    */   public CompletableFuture<Suggestions> parseForSuggestions(SuggestionsBuilder suggestionsBuilder) {
/* 49 */     return CommandArgumentParser.this.parseForSuggestions(suggestionsBuilder);
/*    */   }
/*    */ }


/* Location:              C:\Users\karol\AppData\Roaming\ModrinthApp\meta\versions\26.2-0.19.5\26.2-0.19.5.jar!\net\minecraf\\util\parsing\packrat\commands\CommandArgumentParser$2.class
 * Java compiler version: 25 (69.0)
 * JD-Core Version:       1.1.3
 */