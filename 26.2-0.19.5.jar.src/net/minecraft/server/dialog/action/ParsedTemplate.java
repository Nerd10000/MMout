/*    */ package net.minecraft.server.dialog.action;
/*    */ 
/*    */ import com.mojang.serialization.Codec;
/*    */ import com.mojang.serialization.DataResult;
/*    */ import java.util.List;
/*    */ import java.util.Map;
/*    */ import net.minecraft.commands.functions.StringTemplate;
/*    */ 
/*    */ public class ParsedTemplate {
/*    */   public static final Codec<ParsedTemplate> CODEC;
/*    */   
/*    */   public static final Codec<String> VARIABLE_CODEC;
/*    */   
/*    */   private final String raw;
/*    */   
/*    */   private final StringTemplate parsed;
/*    */   
/*    */   static {
/* 11 */     CODEC = Codec.STRING.comapFlatMap(ParsedTemplate::parse, t -> t.raw);
/* 16 */     VARIABLE_CODEC = Codec.STRING.validate(s -> StringTemplate.isValidVariableName(s) ? DataResult.success(s) : DataResult.error(()));
/*    */   }
/*    */   
/*    */   private ParsedTemplate(String raw, StringTemplate parsed) {
/* 22 */     this.raw = raw;
/* 23 */     this.parsed = parsed;
/*    */   }
/*    */   
/*    */   private static DataResult<ParsedTemplate> parse(String value) {
/*    */     StringTemplate template;
/*    */     try {
/* 29 */       template = StringTemplate.fromString(value);
/* 30 */     } catch (Exception e) {
/* 31 */       return DataResult.error(() -> "Failed to parse template " + value + ": " + e.getMessage());
/*    */     } 
/* 34 */     return DataResult.success(new ParsedTemplate(value, template));
/*    */   }
/*    */   
/*    */   public String instantiate(Map<String, String> arguments) {
/* 38 */     List<String> values = this.parsed.variables().stream().map(k -> (String)arguments.getOrDefault(k, "")).toList();
/* 39 */     return this.parsed.substitute(values);
/*    */   }
/*    */ }


/* Location:              C:\Users\karol\AppData\Roaming\ModrinthApp\meta\versions\26.2-0.19.5\26.2-0.19.5.jar!\net\minecraft\server\dialog\action\ParsedTemplate.class
 * Java compiler version: 25 (69.0)
 * JD-Core Version:       1.1.3
 */