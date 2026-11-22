/*     */ package net.minecraft.commands.arguments.selector.options;
/*     */ 
/*     */ import java.util.function.Predicate;
/*     */ import net.minecraft.commands.arguments.selector.EntitySelectorParser;
/*     */ import net.minecraft.network.chat.Component;
/*     */ 
/*     */ final class Option extends Record {
/*     */   private final EntitySelectorOptions.Modifier modifier;
/*     */   
/*     */   private final Predicate<EntitySelectorParser> canUse;
/*     */   
/*     */   private final Component description;
/*     */   
/*     */   private Option(EntitySelectorOptions.Modifier modifier, Predicate<EntitySelectorParser> canUse, Component description) {
/* 517 */     this.modifier = modifier;
/* 517 */     this.canUse = canUse;
/* 517 */     this.description = description;
/*     */   }
/*     */   
/*     */   public final String toString() {
/*     */     // Byte code:
/*     */     //   0: aload_0
/*     */     //   1: <illegal opcode> toString : (Lnet/minecraft/commands/arguments/selector/options/EntitySelectorOptions$Option;)Ljava/lang/String;
/*     */     //   6: areturn
/*     */     // Line number table:
/*     */     //   Java source line number -> byte code offset
/*     */     //   #517	-> 0
/*     */     // Local variable table:
/*     */     //   start	length	slot	name	descriptor
/*     */     //   0	7	0	this	Lnet/minecraft/commands/arguments/selector/options/EntitySelectorOptions$Option;
/*     */   }
/*     */   
/*     */   public final int hashCode() {
/*     */     // Byte code:
/*     */     //   0: aload_0
/*     */     //   1: <illegal opcode> hashCode : (Lnet/minecraft/commands/arguments/selector/options/EntitySelectorOptions$Option;)I
/*     */     //   6: ireturn
/*     */     // Line number table:
/*     */     //   Java source line number -> byte code offset
/*     */     //   #517	-> 0
/*     */     // Local variable table:
/*     */     //   start	length	slot	name	descriptor
/*     */     //   0	7	0	this	Lnet/minecraft/commands/arguments/selector/options/EntitySelectorOptions$Option;
/*     */   }
/*     */   
/*     */   public final boolean equals(Object o) {
/*     */     // Byte code:
/*     */     //   0: aload_0
/*     */     //   1: aload_1
/*     */     //   2: <illegal opcode> equals : (Lnet/minecraft/commands/arguments/selector/options/EntitySelectorOptions$Option;Ljava/lang/Object;)Z
/*     */     //   7: ireturn
/*     */     // Line number table:
/*     */     //   Java source line number -> byte code offset
/*     */     //   #517	-> 0
/*     */     // Local variable table:
/*     */     //   start	length	slot	name	descriptor
/*     */     //   0	8	0	this	Lnet/minecraft/commands/arguments/selector/options/EntitySelectorOptions$Option;
/*     */     //   0	8	1	o	Ljava/lang/Object;
/*     */   }
/*     */   
/*     */   public EntitySelectorOptions.Modifier modifier() {
/* 517 */     return this.modifier;
/*     */   }
/*     */   
/*     */   public Predicate<EntitySelectorParser> canUse() {
/* 517 */     return this.canUse;
/*     */   }
/*     */   
/*     */   public Component description() {
/* 517 */     return this.description;
/*     */   }
/*     */ }


/* Location:              C:\Users\karol\AppData\Roaming\ModrinthApp\meta\versions\26.2-0.19.5\26.2-0.19.5.jar!\net\minecraft\commands\arguments\selector\options\EntitySelectorOptions$Option.class
 * Java compiler version: 25 (69.0)
 * JD-Core Version:       1.1.3
 */