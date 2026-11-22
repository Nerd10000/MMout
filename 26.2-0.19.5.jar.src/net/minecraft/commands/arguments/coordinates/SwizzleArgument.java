/*    */ package net.minecraft.commands.arguments.coordinates;
/*    */ 
/*    */ import com.mojang.brigadier.Message;
/*    */ import com.mojang.brigadier.StringReader;
/*    */ import com.mojang.brigadier.arguments.ArgumentType;
/*    */ import com.mojang.brigadier.context.CommandContext;
/*    */ import com.mojang.brigadier.exceptions.CommandSyntaxException;
/*    */ import com.mojang.brigadier.exceptions.SimpleCommandExceptionType;
/*    */ import java.util.Arrays;
/*    */ import java.util.Collection;
/*    */ import java.util.EnumSet;
/*    */ import net.minecraft.commands.CommandSourceStack;
/*    */ import net.minecraft.core.Direction;
/*    */ import net.minecraft.network.chat.Component;
/*    */ 
/*    */ public class SwizzleArgument implements ArgumentType<EnumSet<Direction.Axis>> {
/* 17 */   private static final Collection<String> EXAMPLES = Arrays.asList(new String[] { "xyz", "x" });
/*    */   
/* 18 */   private static final SimpleCommandExceptionType ERROR_INVALID = new SimpleCommandExceptionType((Message)Component.translatable("arguments.swizzle.invalid"));
/*    */   
/*    */   public static SwizzleArgument swizzle() {
/* 21 */     return new SwizzleArgument();
/*    */   }
/*    */   
/*    */   public static EnumSet<Direction.Axis> getSwizzle(CommandContext<CommandSourceStack> context, String name) {
/* 26 */     return (EnumSet<Direction.Axis>)context.getArgument(name, EnumSet.class);
/*    */   }
/*    */   
/*    */   public EnumSet<Direction.Axis> parse(StringReader reader) throws CommandSyntaxException {
/*    */     // Byte code:
/*    */     //   0: ldc net/minecraft/core/Direction$Axis
/*    */     //   2: invokestatic noneOf : (Ljava/lang/Class;)Ljava/util/EnumSet;
/*    */     //   5: astore_2
/*    */     //   6: aload_1
/*    */     //   7: invokevirtual canRead : ()Z
/*    */     //   10: ifeq -> 111
/*    */     //   13: aload_1
/*    */     //   14: invokevirtual peek : ()C
/*    */     //   17: bipush #32
/*    */     //   19: if_icmpeq -> 111
/*    */     //   22: aload_1
/*    */     //   23: invokevirtual read : ()C
/*    */     //   26: istore_3
/*    */     //   27: iload_3
/*    */     //   28: tableswitch default -> 74, 120 -> 56, 121 -> 62, 122 -> 68
/*    */     //   56: getstatic net/minecraft/core/Direction$Axis.X : Lnet/minecraft/core/Direction$Axis;
/*    */     //   59: goto -> 82
/*    */     //   62: getstatic net/minecraft/core/Direction$Axis.Y : Lnet/minecraft/core/Direction$Axis;
/*    */     //   65: goto -> 82
/*    */     //   68: getstatic net/minecraft/core/Direction$Axis.Z : Lnet/minecraft/core/Direction$Axis;
/*    */     //   71: goto -> 82
/*    */     //   74: getstatic net/minecraft/commands/arguments/coordinates/SwizzleArgument.ERROR_INVALID : Lcom/mojang/brigadier/exceptions/SimpleCommandExceptionType;
/*    */     //   77: aload_1
/*    */     //   78: invokevirtual createWithContext : (Lcom/mojang/brigadier/ImmutableStringReader;)Lcom/mojang/brigadier/exceptions/CommandSyntaxException;
/*    */     //   81: athrow
/*    */     //   82: astore #4
/*    */     //   84: aload_2
/*    */     //   85: aload #4
/*    */     //   87: invokevirtual contains : (Ljava/lang/Object;)Z
/*    */     //   90: ifeq -> 101
/*    */     //   93: getstatic net/minecraft/commands/arguments/coordinates/SwizzleArgument.ERROR_INVALID : Lcom/mojang/brigadier/exceptions/SimpleCommandExceptionType;
/*    */     //   96: aload_1
/*    */     //   97: invokevirtual createWithContext : (Lcom/mojang/brigadier/ImmutableStringReader;)Lcom/mojang/brigadier/exceptions/CommandSyntaxException;
/*    */     //   100: athrow
/*    */     //   101: aload_2
/*    */     //   102: aload #4
/*    */     //   104: invokevirtual add : (Ljava/lang/Object;)Z
/*    */     //   107: pop
/*    */     //   108: goto -> 6
/*    */     //   111: aload_2
/*    */     //   112: areturn
/*    */     // Line number table:
/*    */     //   Java source line number -> byte code offset
/*    */     //   #31	-> 0
/*    */     //   #33	-> 6
/*    */     //   #34	-> 22
/*    */     //   #36	-> 27
/*    */     //   #37	-> 56
/*    */     //   #38	-> 62
/*    */     //   #39	-> 68
/*    */     //   #40	-> 74
/*    */     //   #41	-> 82
/*    */     //   #43	-> 84
/*    */     //   #44	-> 93
/*    */     //   #46	-> 101
/*    */     //   #47	-> 108
/*    */     //   #49	-> 111
/*    */     // Local variable table:
/*    */     //   start	length	slot	name	descriptor
/*    */     //   27	81	3	c	C
/*    */     //   84	24	4	axis	Lnet/minecraft/core/Direction$Axis;
/*    */     //   0	113	0	this	Lnet/minecraft/commands/arguments/coordinates/SwizzleArgument;
/*    */     //   0	113	1	reader	Lcom/mojang/brigadier/StringReader;
/*    */     //   6	107	2	result	Ljava/util/EnumSet;
/*    */     // Local variable type table:
/*    */     //   start	length	slot	name	signature
/*    */     //   6	107	2	result	Ljava/util/EnumSet<Lnet/minecraft/core/Direction$Axis;>;
/*    */   }
/*    */   
/*    */   public Collection<String> getExamples() {
/* 54 */     return EXAMPLES;
/*    */   }
/*    */ }


/* Location:              C:\Users\karol\AppData\Roaming\ModrinthApp\meta\versions\26.2-0.19.5\26.2-0.19.5.jar!\net\minecraft\commands\arguments\coordinates\SwizzleArgument.class
 * Java compiler version: 25 (69.0)
 * JD-Core Version:       1.1.3
 */