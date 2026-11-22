/*    */ package net.minecraft.commands.arguments;
/*    */ 
/*    */ import com.mojang.brigadier.StringReader;
/*    */ import com.mojang.brigadier.context.CommandContext;
/*    */ import com.mojang.brigadier.exceptions.CommandSyntaxException;
/*    */ import java.util.Arrays;
/*    */ import java.util.Collection;
/*    */ import net.minecraft.advancements.predicates.MinMaxBounds;
/*    */ import net.minecraft.commands.CommandSourceStack;
/*    */ 
/*    */ public class Floats implements RangeArgument<MinMaxBounds.Doubles> {
/* 33 */   private static final Collection<String> EXAMPLES = Arrays.asList(new String[] { "0..5.2", "0", "-5.4", "-100.76..", "..100" });
/*    */   
/*    */   public static MinMaxBounds.Doubles getRange(CommandContext<CommandSourceStack> context, String name) {
/* 36 */     return (MinMaxBounds.Doubles)context.getArgument(name, MinMaxBounds.Doubles.class);
/*    */   }
/*    */   
/*    */   public MinMaxBounds.Doubles parse(StringReader reader) throws CommandSyntaxException {
/* 41 */     return MinMaxBounds.Doubles.fromReader(reader);
/*    */   }
/*    */   
/*    */   public Collection<String> getExamples() {
/* 46 */     return EXAMPLES;
/*    */   }
/*    */ }


/* Location:              C:\Users\karol\AppData\Roaming\ModrinthApp\meta\versions\26.2-0.19.5\26.2-0.19.5.jar!\net\minecraft\commands\arguments\RangeArgument$Floats.class
 * Java compiler version: 25 (69.0)
 * JD-Core Version:       1.1.3
 */