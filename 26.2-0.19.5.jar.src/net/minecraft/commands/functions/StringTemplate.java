/*    */ package net.minecraft.commands.functions;
/*    */ 
/*    */ import com.google.common.collect.ImmutableList;
/*    */ import java.util.List;
/*    */ 
/*    */ public final class StringTemplate extends Record {
/*    */   private final List<String> segments;
/*    */   
/*    */   private final List<String> variables;
/*    */   
/*    */   public StringTemplate(List<String> segments, List<String> variables) {
/*  7 */     this.segments = segments;
/*  7 */     this.variables = variables;
/*    */   }
/*    */   
/*    */   public final String toString() {
/*    */     // Byte code:
/*    */     //   0: aload_0
/*    */     //   1: <illegal opcode> toString : (Lnet/minecraft/commands/functions/StringTemplate;)Ljava/lang/String;
/*    */     //   6: areturn
/*    */     // Line number table:
/*    */     //   Java source line number -> byte code offset
/*    */     //   #7	-> 0
/*    */     // Local variable table:
/*    */     //   start	length	slot	name	descriptor
/*    */     //   0	7	0	this	Lnet/minecraft/commands/functions/StringTemplate;
/*    */   }
/*    */   
/*    */   public final int hashCode() {
/*    */     // Byte code:
/*    */     //   0: aload_0
/*    */     //   1: <illegal opcode> hashCode : (Lnet/minecraft/commands/functions/StringTemplate;)I
/*    */     //   6: ireturn
/*    */     // Line number table:
/*    */     //   Java source line number -> byte code offset
/*    */     //   #7	-> 0
/*    */     // Local variable table:
/*    */     //   start	length	slot	name	descriptor
/*    */     //   0	7	0	this	Lnet/minecraft/commands/functions/StringTemplate;
/*    */   }
/*    */   
/*    */   public final boolean equals(Object o) {
/*    */     // Byte code:
/*    */     //   0: aload_0
/*    */     //   1: aload_1
/*    */     //   2: <illegal opcode> equals : (Lnet/minecraft/commands/functions/StringTemplate;Ljava/lang/Object;)Z
/*    */     //   7: ireturn
/*    */     // Line number table:
/*    */     //   Java source line number -> byte code offset
/*    */     //   #7	-> 0
/*    */     // Local variable table:
/*    */     //   start	length	slot	name	descriptor
/*    */     //   0	8	0	this	Lnet/minecraft/commands/functions/StringTemplate;
/*    */     //   0	8	1	o	Ljava/lang/Object;
/*    */   }
/*    */   
/*    */   public List<String> segments() {
/*  7 */     return this.segments;
/*    */   }
/*    */   
/*    */   public List<String> variables() {
/*  7 */     return this.variables;
/*    */   }
/*    */   
/*    */   public static StringTemplate fromString(String input) {
/* 10 */     ImmutableList.Builder<String> segments = ImmutableList.builder();
/* 11 */     ImmutableList.Builder<String> variables = ImmutableList.builder();
/* 13 */     int length = input.length();
/* 14 */     int start = 0;
/* 15 */     int index = input.indexOf('$');
/* 16 */     while (index != -1) {
/* 17 */       if (index == length - 1 || input.charAt(index + 1) != '(') {
/* 18 */         index = input.indexOf('$', index + 1);
/*    */         continue;
/*    */       } 
/* 22 */       segments.add(input.substring(start, index));
/* 23 */       int variableEnd = input.indexOf(')', index + 1);
/* 24 */       if (variableEnd == -1)
/* 25 */         throw new IllegalArgumentException("Unterminated macro variable"); 
/* 27 */       String variable = input.substring(index + 2, variableEnd);
/* 28 */       if (!isValidVariableName(variable))
/* 29 */         throw new IllegalArgumentException("Invalid macro variable name '" + variable + "'"); 
/* 31 */       variables.add(variable);
/* 32 */       start = variableEnd + 1;
/* 33 */       index = input.indexOf('$', start);
/*    */     } 
/* 35 */     if (start == 0)
/* 36 */       throw new IllegalArgumentException("No variables in macro"); 
/* 38 */     if (start != length)
/* 39 */       segments.add(input.substring(start)); 
/* 41 */     return new StringTemplate((List<String>)segments.build(), (List<String>)variables.build());
/*    */   }
/*    */   
/*    */   public static boolean isValidVariableName(String variable) {
/* 45 */     for (int i = 0; i < variable.length(); i++) {
/* 46 */       char character = variable.charAt(i);
/* 47 */       if (!Character.isLetterOrDigit(character) && character != '_')
/* 48 */         return false; 
/*    */     } 
/* 51 */     return true;
/*    */   }
/*    */   
/*    */   public String substitute(List<String> arguments) {
/* 55 */     StringBuilder builder = new StringBuilder();
/* 56 */     for (int i = 0; i < this.variables.size(); i++) {
/* 57 */       builder.append(this.segments.get(i)).append(arguments.get(i));
/* 58 */       CommandFunction.checkCommandLineLength(builder);
/*    */     } 
/* 61 */     if (this.segments.size() > this.variables.size())
/* 62 */       builder.append(this.segments.getLast()); 
/* 64 */     CommandFunction.checkCommandLineLength(builder);
/* 65 */     return builder.toString();
/*    */   }
/*    */ }


/* Location:              C:\Users\karol\AppData\Roaming\ModrinthApp\meta\versions\26.2-0.19.5\26.2-0.19.5.jar!\net\minecraft\commands\functions\StringTemplate.class
 * Java compiler version: 25 (69.0)
 * JD-Core Version:       1.1.3
 */