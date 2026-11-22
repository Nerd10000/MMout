/*     */ package net.minecraft.commands.arguments;
/*     */ 
/*     */ import com.mojang.brigadier.exceptions.CommandSyntaxException;
/*     */ import net.minecraft.commands.CommandSourceStack;
/*     */ import net.minecraft.commands.arguments.selector.EntitySelector;
/*     */ import net.minecraft.network.chat.Component;
/*     */ 
/*     */ public final class Part extends Record {
/*     */   private final int start;
/*     */   
/*     */   private final int end;
/*     */   
/*     */   private final EntitySelector selector;
/*     */   
/*     */   public Part(int start, int end, EntitySelector selector) {
/* 169 */     this.start = start;
/* 169 */     this.end = end;
/* 169 */     this.selector = selector;
/*     */   }
/*     */   
/*     */   public final String toString() {
/*     */     // Byte code:
/*     */     //   0: aload_0
/*     */     //   1: <illegal opcode> toString : (Lnet/minecraft/commands/arguments/MessageArgument$Part;)Ljava/lang/String;
/*     */     //   6: areturn
/*     */     // Line number table:
/*     */     //   Java source line number -> byte code offset
/*     */     //   #169	-> 0
/*     */     // Local variable table:
/*     */     //   start	length	slot	name	descriptor
/*     */     //   0	7	0	this	Lnet/minecraft/commands/arguments/MessageArgument$Part;
/*     */   }
/*     */   
/*     */   public final int hashCode() {
/*     */     // Byte code:
/*     */     //   0: aload_0
/*     */     //   1: <illegal opcode> hashCode : (Lnet/minecraft/commands/arguments/MessageArgument$Part;)I
/*     */     //   6: ireturn
/*     */     // Line number table:
/*     */     //   Java source line number -> byte code offset
/*     */     //   #169	-> 0
/*     */     // Local variable table:
/*     */     //   start	length	slot	name	descriptor
/*     */     //   0	7	0	this	Lnet/minecraft/commands/arguments/MessageArgument$Part;
/*     */   }
/*     */   
/*     */   public final boolean equals(Object o) {
/*     */     // Byte code:
/*     */     //   0: aload_0
/*     */     //   1: aload_1
/*     */     //   2: <illegal opcode> equals : (Lnet/minecraft/commands/arguments/MessageArgument$Part;Ljava/lang/Object;)Z
/*     */     //   7: ireturn
/*     */     // Line number table:
/*     */     //   Java source line number -> byte code offset
/*     */     //   #169	-> 0
/*     */     // Local variable table:
/*     */     //   start	length	slot	name	descriptor
/*     */     //   0	8	0	this	Lnet/minecraft/commands/arguments/MessageArgument$Part;
/*     */     //   0	8	1	o	Ljava/lang/Object;
/*     */   }
/*     */   
/*     */   public int start() {
/* 169 */     return this.start;
/*     */   }
/*     */   
/*     */   public int end() {
/* 169 */     return this.end;
/*     */   }
/*     */   
/*     */   public EntitySelector selector() {
/* 169 */     return this.selector;
/*     */   }
/*     */   
/*     */   public Component toComponent(CommandSourceStack sender) throws CommandSyntaxException {
/* 171 */     return EntitySelector.joinNames(this.selector.findEntities(sender));
/*     */   }
/*     */ }


/* Location:              C:\Users\karol\AppData\Roaming\ModrinthApp\meta\versions\26.2-0.19.5\26.2-0.19.5.jar!\net\minecraft\commands\arguments\MessageArgument$Part.class
 * Java compiler version: 25 (69.0)
 * JD-Core Version:       1.1.3
 */