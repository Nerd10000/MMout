/*    */ package net.minecraft.commands.functions;
/*    */ 
/*    */ import com.mojang.brigadier.CommandDispatcher;
/*    */ import java.util.List;
/*    */ import net.minecraft.commands.FunctionInstantiationException;
/*    */ import net.minecraft.commands.execution.UnboundEntryAction;
/*    */ import net.minecraft.nbt.CompoundTag;
/*    */ import net.minecraft.resources.Identifier;
/*    */ 
/*    */ public final class PlainTextFunction<T> extends Record implements CommandFunction<T>, InstantiatedFunction<T> {
/*    */   private final Identifier id;
/*    */   
/*    */   private final List<UnboundEntryAction<T>> entries;
/*    */   
/*    */   public PlainTextFunction(Identifier id, List<UnboundEntryAction<T>> entries) {
/* 12 */     this.id = id;
/* 12 */     this.entries = entries;
/*    */   }
/*    */   
/*    */   public final String toString() {
/*    */     // Byte code:
/*    */     //   0: aload_0
/*    */     //   1: <illegal opcode> toString : (Lnet/minecraft/commands/functions/PlainTextFunction;)Ljava/lang/String;
/*    */     //   6: areturn
/*    */     // Line number table:
/*    */     //   Java source line number -> byte code offset
/*    */     //   #12	-> 0
/*    */     // Local variable table:
/*    */     //   start	length	slot	name	descriptor
/*    */     //   0	7	0	this	Lnet/minecraft/commands/functions/PlainTextFunction;
/*    */     // Local variable type table:
/*    */     //   start	length	slot	name	signature
/*    */     //   0	7	0	this	Lnet/minecraft/commands/functions/PlainTextFunction<TT;>;
/*    */   }
/*    */   
/*    */   public final int hashCode() {
/*    */     // Byte code:
/*    */     //   0: aload_0
/*    */     //   1: <illegal opcode> hashCode : (Lnet/minecraft/commands/functions/PlainTextFunction;)I
/*    */     //   6: ireturn
/*    */     // Line number table:
/*    */     //   Java source line number -> byte code offset
/*    */     //   #12	-> 0
/*    */     // Local variable table:
/*    */     //   start	length	slot	name	descriptor
/*    */     //   0	7	0	this	Lnet/minecraft/commands/functions/PlainTextFunction;
/*    */     // Local variable type table:
/*    */     //   start	length	slot	name	signature
/*    */     //   0	7	0	this	Lnet/minecraft/commands/functions/PlainTextFunction<TT;>;
/*    */   }
/*    */   
/*    */   public final boolean equals(Object o) {
/*    */     // Byte code:
/*    */     //   0: aload_0
/*    */     //   1: aload_1
/*    */     //   2: <illegal opcode> equals : (Lnet/minecraft/commands/functions/PlainTextFunction;Ljava/lang/Object;)Z
/*    */     //   7: ireturn
/*    */     // Line number table:
/*    */     //   Java source line number -> byte code offset
/*    */     //   #12	-> 0
/*    */     // Local variable table:
/*    */     //   start	length	slot	name	descriptor
/*    */     //   0	8	0	this	Lnet/minecraft/commands/functions/PlainTextFunction;
/*    */     //   0	8	1	o	Ljava/lang/Object;
/*    */     // Local variable type table:
/*    */     //   start	length	slot	name	signature
/*    */     //   0	8	0	this	Lnet/minecraft/commands/functions/PlainTextFunction<TT;>;
/*    */   }
/*    */   
/*    */   public Identifier id() {
/* 12 */     return this.id;
/*    */   }
/*    */   
/*    */   public List<UnboundEntryAction<T>> entries() {
/* 12 */     return this.entries;
/*    */   }
/*    */   
/*    */   public InstantiatedFunction<T> instantiate(CompoundTag arguments, CommandDispatcher<T> dispatcher) throws FunctionInstantiationException {
/* 20 */     return this;
/*    */   }
/*    */ }


/* Location:              C:\Users\karol\AppData\Roaming\ModrinthApp\meta\versions\26.2-0.19.5\26.2-0.19.5.jar!\net\minecraft\commands\functions\PlainTextFunction.class
 * Java compiler version: 25 (69.0)
 * JD-Core Version:       1.1.3
 */