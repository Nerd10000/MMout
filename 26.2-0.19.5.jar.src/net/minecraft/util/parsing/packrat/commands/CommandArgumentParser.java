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
/*    */ import java.util.function.Function;
/*    */ 
/*    */ public interface CommandArgumentParser<T> {
/*    */   T parseForCommands(StringReader paramStringReader) throws CommandSyntaxException;
/*    */   
/*    */   CompletableFuture<Suggestions> parseForSuggestions(SuggestionsBuilder paramSuggestionsBuilder);
/*    */   
/*    */   default <S> CommandArgumentParser<S> mapResult(final Function<T, S> mapper) {
/* 21 */     return new CommandArgumentParser<S>() {
/*    */         public S parseForCommands(StringReader reader) throws CommandSyntaxException {
/* 24 */           return (S)mapper.apply(CommandArgumentParser.this.parseForCommands(reader));
/*    */         }
/*    */         
/*    */         public CompletableFuture<Suggestions> parseForSuggestions(SuggestionsBuilder suggestionsBuilder) {
/* 29 */           return CommandArgumentParser.this.parseForSuggestions(suggestionsBuilder);
/*    */         }
/*    */       };
/*    */   }
/*    */   
/*    */   default <T, O> CommandArgumentParser<T> withCodec(final DynamicOps<O> ops, final CommandArgumentParser<O> valueParser, final Codec<T> codec, final DynamicCommandExceptionType exceptionType) {
/* 35 */     return new CommandArgumentParser<T>() {
/*    */         public T parseForCommands(StringReader reader) throws CommandSyntaxException {
/* 38 */           int cursor = reader.getCursor();
/* 39 */           O tag = valueParser.parseForCommands(reader);
/* 40 */           DataResult<T> result = codec.parse(ops, tag);
/* 41 */           return (T)result.getOrThrow(message -> {
/*    */                 reader.setCursor(cursor);
/*    */                 return exceptionType.createWithContext((ImmutableStringReader)reader, message);
/*    */               });
/*    */         }
/*    */         
/*    */         public CompletableFuture<Suggestions> parseForSuggestions(SuggestionsBuilder suggestionsBuilder) {
/* 49 */           return CommandArgumentParser.this.parseForSuggestions(suggestionsBuilder);
/*    */         }
/*    */       };
/*    */   }
/*    */ }


/* Location:              C:\Users\karol\AppData\Roaming\ModrinthApp\meta\versions\26.2-0.19.5\26.2-0.19.5.jar!\net\minecraf\\util\parsing\packrat\commands\CommandArgumentParser.class
 * Java compiler version: 25 (69.0)
 * JD-Core Version:       1.1.3
 */