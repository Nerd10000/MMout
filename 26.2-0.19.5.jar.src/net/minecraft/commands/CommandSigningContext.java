/*    */ package net.minecraft.commands;
/*    */ 
/*    */ import java.util.Map;
/*    */ import net.minecraft.network.chat.PlayerChatMessage;
/*    */ 
/*    */ public interface CommandSigningContext {
/*  9 */   public static final CommandSigningContext ANONYMOUS = new CommandSigningContext() {
/*    */       public PlayerChatMessage getArgument(String name) {
/* 12 */         return null;
/*    */       }
/*    */     };
/*    */   
/*    */   PlayerChatMessage getArgument(String paramString);
/*    */   
/*    */   public static final class SignedArguments extends Record implements CommandSigningContext {
/*    */     private final Map<String, PlayerChatMessage> arguments;
/*    */     
/*    */     public SignedArguments(Map<String, PlayerChatMessage> arguments) {
/* 18 */       this.arguments = arguments;
/*    */     }
/*    */     
/*    */     public final String toString() {
/*    */       // Byte code:
/*    */       //   0: aload_0
/*    */       //   1: <illegal opcode> toString : (Lnet/minecraft/commands/CommandSigningContext$SignedArguments;)Ljava/lang/String;
/*    */       //   6: areturn
/*    */       // Line number table:
/*    */       //   Java source line number -> byte code offset
/*    */       //   #18	-> 0
/*    */       // Local variable table:
/*    */       //   start	length	slot	name	descriptor
/*    */       //   0	7	0	this	Lnet/minecraft/commands/CommandSigningContext$SignedArguments;
/*    */     }
/*    */     
/*    */     public final int hashCode() {
/*    */       // Byte code:
/*    */       //   0: aload_0
/*    */       //   1: <illegal opcode> hashCode : (Lnet/minecraft/commands/CommandSigningContext$SignedArguments;)I
/*    */       //   6: ireturn
/*    */       // Line number table:
/*    */       //   Java source line number -> byte code offset
/*    */       //   #18	-> 0
/*    */       // Local variable table:
/*    */       //   start	length	slot	name	descriptor
/*    */       //   0	7	0	this	Lnet/minecraft/commands/CommandSigningContext$SignedArguments;
/*    */     }
/*    */     
/*    */     public final boolean equals(Object o) {
/*    */       // Byte code:
/*    */       //   0: aload_0
/*    */       //   1: aload_1
/*    */       //   2: <illegal opcode> equals : (Lnet/minecraft/commands/CommandSigningContext$SignedArguments;Ljava/lang/Object;)Z
/*    */       //   7: ireturn
/*    */       // Line number table:
/*    */       //   Java source line number -> byte code offset
/*    */       //   #18	-> 0
/*    */       // Local variable table:
/*    */       //   start	length	slot	name	descriptor
/*    */       //   0	8	0	this	Lnet/minecraft/commands/CommandSigningContext$SignedArguments;
/*    */       //   0	8	1	o	Ljava/lang/Object;
/*    */     }
/*    */     
/*    */     public Map<String, PlayerChatMessage> arguments() {
/* 18 */       return this.arguments;
/*    */     }
/*    */     
/*    */     public PlayerChatMessage getArgument(String name) {
/* 21 */       return this.arguments.get(name);
/*    */     }
/*    */   }
/*    */ }


/* Location:              C:\Users\karol\AppData\Roaming\ModrinthApp\meta\versions\26.2-0.19.5\26.2-0.19.5.jar!\net\minecraft\commands\CommandSigningContext.class
 * Java compiler version: 25 (69.0)
 * JD-Core Version:       1.1.3
 */