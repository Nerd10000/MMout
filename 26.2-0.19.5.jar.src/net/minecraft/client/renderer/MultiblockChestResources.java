/*    */ package net.minecraft.client.renderer;
/*    */ 
/*    */ import java.util.function.Function;
/*    */ import net.minecraft.world.level.block.state.properties.ChestType;
/*    */ 
/*    */ public final class MultiblockChestResources<T> extends Record {
/*    */   private final T single;
/*    */   
/*    */   private final T left;
/*    */   
/*    */   private final T right;
/*    */   
/*    */   public MultiblockChestResources(T single, T left, T right) {
/*  7 */     this.single = single;
/*  7 */     this.left = left;
/*  7 */     this.right = right;
/*    */   }
/*    */   
/*    */   public final String toString() {
/*    */     // Byte code:
/*    */     //   0: aload_0
/*    */     //   1: <illegal opcode> toString : (Lnet/minecraft/client/renderer/MultiblockChestResources;)Ljava/lang/String;
/*    */     //   6: areturn
/*    */     // Line number table:
/*    */     //   Java source line number -> byte code offset
/*    */     //   #7	-> 0
/*    */     // Local variable table:
/*    */     //   start	length	slot	name	descriptor
/*    */     //   0	7	0	this	Lnet/minecraft/client/renderer/MultiblockChestResources;
/*    */     // Local variable type table:
/*    */     //   start	length	slot	name	signature
/*    */     //   0	7	0	this	Lnet/minecraft/client/renderer/MultiblockChestResources<TT;>;
/*    */   }
/*    */   
/*    */   public final int hashCode() {
/*    */     // Byte code:
/*    */     //   0: aload_0
/*    */     //   1: <illegal opcode> hashCode : (Lnet/minecraft/client/renderer/MultiblockChestResources;)I
/*    */     //   6: ireturn
/*    */     // Line number table:
/*    */     //   Java source line number -> byte code offset
/*    */     //   #7	-> 0
/*    */     // Local variable table:
/*    */     //   start	length	slot	name	descriptor
/*    */     //   0	7	0	this	Lnet/minecraft/client/renderer/MultiblockChestResources;
/*    */     // Local variable type table:
/*    */     //   start	length	slot	name	signature
/*    */     //   0	7	0	this	Lnet/minecraft/client/renderer/MultiblockChestResources<TT;>;
/*    */   }
/*    */   
/*    */   public final boolean equals(Object o) {
/*    */     // Byte code:
/*    */     //   0: aload_0
/*    */     //   1: aload_1
/*    */     //   2: <illegal opcode> equals : (Lnet/minecraft/client/renderer/MultiblockChestResources;Ljava/lang/Object;)Z
/*    */     //   7: ireturn
/*    */     // Line number table:
/*    */     //   Java source line number -> byte code offset
/*    */     //   #7	-> 0
/*    */     // Local variable table:
/*    */     //   start	length	slot	name	descriptor
/*    */     //   0	8	0	this	Lnet/minecraft/client/renderer/MultiblockChestResources;
/*    */     //   0	8	1	o	Ljava/lang/Object;
/*    */     // Local variable type table:
/*    */     //   start	length	slot	name	signature
/*    */     //   0	8	0	this	Lnet/minecraft/client/renderer/MultiblockChestResources<TT;>;
/*    */   }
/*    */   
/*    */   public T single() {
/*  7 */     return this.single;
/*    */   }
/*    */   
/*    */   public T left() {
/*  7 */     return this.left;
/*    */   }
/*    */   
/*    */   public T right() {
/*  7 */     return this.right;
/*    */   }
/*    */   
/*    */   public T select(ChestType chestType) {
/* 13 */     switch (chestType) {
/*    */       default:
/* 13 */         throw new MatchException(null, null);
/*    */       case SINGLE:
/*    */       
/*    */       case LEFT:
/*    */       
/*    */       case RIGHT:
/*    */         break;
/*    */     } 
/* 13 */     return 
/*    */       
/* 16 */       this.right;
/*    */   }
/*    */   
/*    */   public <S> MultiblockChestResources<S> map(Function<T, S> mapper) {
/* 21 */     return new MultiblockChestResources((T)mapper
/* 22 */         .apply(this.single), (T)mapper
/* 23 */         .apply(this.left), (T)mapper
/* 24 */         .apply(this.right));
/*    */   }
/*    */ }


/* Location:              C:\Users\karol\AppData\Roaming\ModrinthApp\meta\versions\26.2-0.19.5\26.2-0.19.5.jar!\net\minecraft\client\renderer\MultiblockChestResources.class
 * Java compiler version: 25 (69.0)
 * JD-Core Version:       1.1.3
 */