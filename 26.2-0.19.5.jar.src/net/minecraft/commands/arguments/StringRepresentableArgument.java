/*    */ package net.minecraft.commands.arguments;
/*    */ 
/*    */ import com.google.gson.JsonPrimitive;
/*    */ import com.mojang.brigadier.ImmutableStringReader;
/*    */ import com.mojang.brigadier.Message;
/*    */ import com.mojang.brigadier.StringReader;
/*    */ import com.mojang.brigadier.arguments.ArgumentType;
/*    */ import com.mojang.brigadier.context.CommandContext;
/*    */ import com.mojang.brigadier.exceptions.CommandSyntaxException;
/*    */ import com.mojang.brigadier.exceptions.DynamicCommandExceptionType;
/*    */ import com.mojang.brigadier.suggestion.Suggestions;
/*    */ import com.mojang.brigadier.suggestion.SuggestionsBuilder;
/*    */ import com.mojang.serialization.Codec;
/*    */ import com.mojang.serialization.DynamicOps;
/*    */ import com.mojang.serialization.JsonOps;
/*    */ import java.util.Arrays;
/*    */ import java.util.Collection;
/*    */ import java.util.concurrent.CompletableFuture;
/*    */ import java.util.function.Supplier;
/*    */ import java.util.stream.Collectors;
/*    */ import net.minecraft.commands.SharedSuggestionProvider;
/*    */ import net.minecraft.network.chat.Component;
/*    */ import net.minecraft.util.StringRepresentable;
/*    */ 
/*    */ public class StringRepresentableArgument<T extends Enum<T> & StringRepresentable> implements ArgumentType<T> {
/*    */   private static final DynamicCommandExceptionType ERROR_INVALID_VALUE;
/*    */   
/*    */   private final Codec<T> codec;
/*    */   
/*    */   private final Supplier<T[]> values;
/*    */   
/*    */   static {
/* 24 */     ERROR_INVALID_VALUE = new DynamicCommandExceptionType(value -> Component.translatableEscape("argument.enum.invalid", new Object[] { value }));
/*    */   }
/*    */   
/*    */   protected StringRepresentableArgument(Codec<T> codec, Supplier<T[]> values) {
/* 29 */     this.codec = codec;
/* 30 */     this.values = values;
/*    */   }
/*    */   
/*    */   public T parse(StringReader reader) throws CommandSyntaxException {
/* 35 */     String id = reader.readUnquotedString();
/* 36 */     return (T)this.codec.parse((DynamicOps)JsonOps.INSTANCE, new JsonPrimitive(id)).result().orElseThrow(() -> ERROR_INVALID_VALUE.createWithContext((ImmutableStringReader)reader, id));
/*    */   }
/*    */   
/*    */   public <S> CompletableFuture<Suggestions> listSuggestions(CommandContext<S> context, SuggestionsBuilder builder) {
/* 41 */     return SharedSuggestionProvider.suggest((Iterable)Arrays.<Enum>stream((Enum[])this.values.get()).map(rec$ -> ((StringRepresentable)rec$).getSerializedName()).map(this::convertId).collect(Collectors.toList()), builder);
/*    */   }
/*    */   
/*    */   public Collection<String> getExamples() {
/* 46 */     return (Collection<String>)Arrays.<Enum>stream((Enum[])this.values.get()).map(rec$ -> ((StringRepresentable)rec$).getSerializedName()).map(this::convertId).limit(2L).collect(Collectors.toList());
/*    */   }
/*    */   
/*    */   protected String convertId(String id) {
/* 50 */     return id;
/*    */   }
/*    */ }


/* Location:              C:\Users\karol\AppData\Roaming\ModrinthApp\meta\versions\26.2-0.19.5\26.2-0.19.5.jar!\net\minecraft\commands\arguments\StringRepresentableArgument.class
 * Java compiler version: 25 (69.0)
 * JD-Core Version:       1.1.3
 */