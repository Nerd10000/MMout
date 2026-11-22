/*    */ package net.minecraft.util;
/*    */ 
/*    */ import com.mojang.brigadier.Message;
/*    */ import com.mojang.brigadier.StringReader;
/*    */ import com.mojang.brigadier.exceptions.CommandSyntaxException;
/*    */ import com.mojang.brigadier.exceptions.DynamicCommandExceptionType;
/*    */ import com.mojang.serialization.DataResult;
/*    */ import java.util.function.Function;
/*    */ import net.minecraft.network.chat.Component;
/*    */ 
/*    */ public abstract class CommandParserHelper<T> implements Function<String, DataResult<T>> {
/*    */   private static final DynamicCommandExceptionType TRAILING_DATA;
/*    */   
/*    */   static {
/* 58 */     TRAILING_DATA = new DynamicCommandExceptionType(commandAndRemainder -> Component.translatableEscape("command.trailing_data", new Object[] { commandAndRemainder }));
/*    */   }
/*    */   
/*    */   public final DataResult<T> apply(String contents) {
/* 62 */     StringReader reader = new StringReader(contents);
/*    */     try {
/* 64 */       T result = parse(reader);
/* 65 */       if (reader.canRead()) {
/* 66 */         String parsed = reader.getString().substring(0, reader.getCursor());
/* 67 */         String leftovers = reader.getString().substring(reader.getCursor());
/* 68 */         throw TRAILING_DATA.create(parsed + "[" + parsed + "]");
/*    */       } 
/* 70 */       return DataResult.success(result);
/* 71 */     } catch (CommandSyntaxException ex) {
/* 72 */       return DataResult.error(() -> errorMessage(contents, ex));
/*    */     } 
/*    */   }
/*    */   
/*    */   protected abstract T parse(StringReader paramStringReader) throws CommandSyntaxException;
/*    */   
/*    */   protected abstract String errorMessage(String paramString, CommandSyntaxException paramCommandSyntaxException);
/*    */ }


/* Location:              C:\Users\karol\AppData\Roaming\ModrinthApp\meta\versions\26.2-0.19.5\26.2-0.19.5.jar!\net\minecraf\\util\CompilableString$CommandParserHelper.class
 * Java compiler version: 25 (69.0)
 * JD-Core Version:       1.1.3
 */