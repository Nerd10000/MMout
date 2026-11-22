/*    */ package net.minecraft.util;
/*    */ 
/*    */ import com.mojang.brigadier.Message;
/*    */ import com.mojang.brigadier.StringReader;
/*    */ import com.mojang.brigadier.exceptions.CommandSyntaxException;
/*    */ import com.mojang.brigadier.exceptions.DynamicCommandExceptionType;
/*    */ import com.mojang.serialization.Codec;
/*    */ import com.mojang.serialization.DataResult;
/*    */ import java.util.Objects;
/*    */ import java.util.function.Function;
/*    */ import net.minecraft.network.chat.Component;
/*    */ 
/*    */ public class CompilableString<T> {
/*    */   private final String source;
/*    */   
/*    */   private final T compiled;
/*    */   
/*    */   private CompilableString(String source, T compiled) {
/* 23 */     this.source = source;
/* 24 */     this.compiled = compiled;
/*    */   }
/*    */   
/*    */   public static <T> Codec<CompilableString<T>> codec(Function<String, DataResult<T>> compiler) {
/* 28 */     return Codec.STRING.comapFlatMap(s -> ((DataResult)compiler.apply(s)).map(()), CompilableString::source);
/*    */   }
/*    */   
/*    */   public String source() {
/* 35 */     return this.source;
/*    */   }
/*    */   
/*    */   public T compiled() {
/* 39 */     return this.compiled;
/*    */   }
/*    */   
/*    */   public boolean equals(Object o) {
/* 44 */     if (o instanceof CompilableString) {
/* 44 */       CompilableString<?> that = (CompilableString)o;
/* 44 */       if (Objects.equals(this.source, that.source));
/*    */     } 
/* 44 */     return false;
/*    */   }
/*    */   
/*    */   public int hashCode() {
/* 49 */     return this.source.hashCode();
/*    */   }
/*    */   
/*    */   public String toString() {
/* 54 */     return this.source;
/*    */   }
/*    */   
/*    */   public static abstract class CommandParserHelper<T> implements Function<String, DataResult<T>> {
/*    */     private static final DynamicCommandExceptionType TRAILING_DATA;
/*    */     
/*    */     static {
/* 58 */       TRAILING_DATA = new DynamicCommandExceptionType(commandAndRemainder -> Component.translatableEscape("command.trailing_data", new Object[] { commandAndRemainder }));
/*    */     }
/*    */     
/*    */     public final DataResult<T> apply(String contents) {
/* 62 */       StringReader reader = new StringReader(contents);
/*    */       try {
/* 64 */         T result = parse(reader);
/* 65 */         if (reader.canRead()) {
/* 66 */           String parsed = reader.getString().substring(0, reader.getCursor());
/* 67 */           String leftovers = reader.getString().substring(reader.getCursor());
/* 68 */           throw TRAILING_DATA.create(parsed + "[" + parsed + "]");
/*    */         } 
/* 70 */         return DataResult.success(result);
/* 71 */       } catch (CommandSyntaxException ex) {
/* 72 */         return DataResult.error(() -> errorMessage(contents, ex));
/*    */       } 
/*    */     }
/*    */     
/*    */     protected abstract T parse(StringReader param1StringReader) throws CommandSyntaxException;
/*    */     
/*    */     protected abstract String errorMessage(String param1String, CommandSyntaxException param1CommandSyntaxException);
/*    */   }
/*    */ }


/* Location:              C:\Users\karol\AppData\Roaming\ModrinthApp\meta\versions\26.2-0.19.5\26.2-0.19.5.jar!\net\minecraf\\util\CompilableString.class
 * Java compiler version: 25 (69.0)
 * JD-Core Version:       1.1.3
 */