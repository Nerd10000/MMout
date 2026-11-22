/*    */ package net.minecraft.commands.arguments;
/*    */ 
/*    */ import com.mojang.brigadier.StringReader;
/*    */ import com.mojang.brigadier.arguments.ArgumentType;
/*    */ import com.mojang.brigadier.context.CommandContext;
/*    */ import com.mojang.brigadier.exceptions.CommandSyntaxException;
/*    */ import java.util.Arrays;
/*    */ import java.util.Collection;
/*    */ import net.minecraft.commands.CommandSourceStack;
/*    */ import net.minecraft.resources.Identifier;
/*    */ 
/*    */ public class IdentifierArgument implements ArgumentType<Identifier> {
/* 14 */   private static final Collection<String> EXAMPLES = Arrays.asList(new String[] { "foo", "foo:bar", "012" });
/*    */   
/*    */   public static IdentifierArgument id() {
/* 20 */     return new IdentifierArgument();
/*    */   }
/*    */   
/*    */   public static Identifier getId(CommandContext<CommandSourceStack> context, String name) {
/* 24 */     return (Identifier)context.getArgument(name, Identifier.class);
/*    */   }
/*    */   
/*    */   public Identifier parse(StringReader reader) throws CommandSyntaxException {
/* 29 */     return Identifier.read(reader);
/*    */   }
/*    */   
/*    */   public Collection<String> getExamples() {
/* 34 */     return EXAMPLES;
/*    */   }
/*    */ }


/* Location:              C:\Users\karol\AppData\Roaming\ModrinthApp\meta\versions\26.2-0.19.5\26.2-0.19.5.jar!\net\minecraft\commands\arguments\IdentifierArgument.class
 * Java compiler version: 25 (69.0)
 * JD-Core Version:       1.1.3
 */