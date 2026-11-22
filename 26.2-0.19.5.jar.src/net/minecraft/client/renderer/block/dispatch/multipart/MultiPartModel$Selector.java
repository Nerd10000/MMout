/*    */ package net.minecraft.client.renderer.block.dispatch.multipart;
/*    */ 
/*    */ import java.util.function.Predicate;
/*    */ import net.minecraft.world.level.block.state.BlockState;
/*    */ 
/*    */ public final class Selector<T> extends Record {
/*    */   private final Predicate<BlockState> condition;
/*    */   
/*    */   private final T model;
/*    */   
/*    */   public Selector(Predicate<BlockState> condition, T model) {
/* 55 */     this.condition = condition;
/* 55 */     this.model = model;
/*    */   }
/*    */   
/*    */   public final String toString() {
/*    */     // Byte code:
/*    */     //   0: aload_0
/*    */     //   1: <illegal opcode> toString : (Lnet/minecraft/client/renderer/block/dispatch/multipart/MultiPartModel$Selector;)Ljava/lang/String;
/*    */     //   6: areturn
/*    */     // Line number table:
/*    */     //   Java source line number -> byte code offset
/*    */     //   #55	-> 0
/*    */     // Local variable table:
/*    */     //   start	length	slot	name	descriptor
/*    */     //   0	7	0	this	Lnet/minecraft/client/renderer/block/dispatch/multipart/MultiPartModel$Selector;
/*    */     // Local variable type table:
/*    */     //   start	length	slot	name	signature
/*    */     //   0	7	0	this	Lnet/minecraft/client/renderer/block/dispatch/multipart/MultiPartModel$Selector<TT;>;
/*    */   }
/*    */   
/*    */   public final int hashCode() {
/*    */     // Byte code:
/*    */     //   0: aload_0
/*    */     //   1: <illegal opcode> hashCode : (Lnet/minecraft/client/renderer/block/dispatch/multipart/MultiPartModel$Selector;)I
/*    */     //   6: ireturn
/*    */     // Line number table:
/*    */     //   Java source line number -> byte code offset
/*    */     //   #55	-> 0
/*    */     // Local variable table:
/*    */     //   start	length	slot	name	descriptor
/*    */     //   0	7	0	this	Lnet/minecraft/client/renderer/block/dispatch/multipart/MultiPartModel$Selector;
/*    */     // Local variable type table:
/*    */     //   start	length	slot	name	signature
/*    */     //   0	7	0	this	Lnet/minecraft/client/renderer/block/dispatch/multipart/MultiPartModel$Selector<TT;>;
/*    */   }
/*    */   
/*    */   public final boolean equals(Object o) {
/*    */     // Byte code:
/*    */     //   0: aload_0
/*    */     //   1: aload_1
/*    */     //   2: <illegal opcode> equals : (Lnet/minecraft/client/renderer/block/dispatch/multipart/MultiPartModel$Selector;Ljava/lang/Object;)Z
/*    */     //   7: ireturn
/*    */     // Line number table:
/*    */     //   Java source line number -> byte code offset
/*    */     //   #55	-> 0
/*    */     // Local variable table:
/*    */     //   start	length	slot	name	descriptor
/*    */     //   0	8	0	this	Lnet/minecraft/client/renderer/block/dispatch/multipart/MultiPartModel$Selector;
/*    */     //   0	8	1	o	Ljava/lang/Object;
/*    */     // Local variable type table:
/*    */     //   start	length	slot	name	signature
/*    */     //   0	8	0	this	Lnet/minecraft/client/renderer/block/dispatch/multipart/MultiPartModel$Selector<TT;>;
/*    */   }
/*    */   
/*    */   public Predicate<BlockState> condition() {
/* 55 */     return this.condition;
/*    */   }
/*    */   
/*    */   public T model() {
/* 55 */     return this.model;
/*    */   }
/*    */   
/*    */   public <S> Selector<S> with(S newModel) {
/* 57 */     return new Selector(this.condition, (T)newModel);
/*    */   }
/*    */ }


/* Location:              C:\Users\karol\AppData\Roaming\ModrinthApp\meta\versions\26.2-0.19.5\26.2-0.19.5.jar!\net\minecraft\client\renderer\block\dispatch\multipart\MultiPartModel$Selector.class
 * Java compiler version: 25 (69.0)
 * JD-Core Version:       1.1.3
 */