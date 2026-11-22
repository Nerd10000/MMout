/*    */ package net.minecraft.world;
/*    */ 
/*    */ import net.minecraft.world.item.ItemStack;
/*    */ 
/*    */ public final class ItemContext extends Record {
/*    */   private final boolean wasItemInteraction;
/*    */   
/*    */   private final ItemStack heldItemTransformedTo;
/*    */   
/*    */   public ItemContext(boolean wasItemInteraction, ItemStack heldItemTransformedTo) {
/* 63 */     this.wasItemInteraction = wasItemInteraction;
/* 63 */     this.heldItemTransformedTo = heldItemTransformedTo;
/*    */   }
/*    */   
/*    */   public final String toString() {
/*    */     // Byte code:
/*    */     //   0: aload_0
/*    */     //   1: <illegal opcode> toString : (Lnet/minecraft/world/InteractionResult$ItemContext;)Ljava/lang/String;
/*    */     //   6: areturn
/*    */     // Line number table:
/*    */     //   Java source line number -> byte code offset
/*    */     //   #63	-> 0
/*    */     // Local variable table:
/*    */     //   start	length	slot	name	descriptor
/*    */     //   0	7	0	this	Lnet/minecraft/world/InteractionResult$ItemContext;
/*    */   }
/*    */   
/*    */   public final int hashCode() {
/*    */     // Byte code:
/*    */     //   0: aload_0
/*    */     //   1: <illegal opcode> hashCode : (Lnet/minecraft/world/InteractionResult$ItemContext;)I
/*    */     //   6: ireturn
/*    */     // Line number table:
/*    */     //   Java source line number -> byte code offset
/*    */     //   #63	-> 0
/*    */     // Local variable table:
/*    */     //   start	length	slot	name	descriptor
/*    */     //   0	7	0	this	Lnet/minecraft/world/InteractionResult$ItemContext;
/*    */   }
/*    */   
/*    */   public final boolean equals(Object o) {
/*    */     // Byte code:
/*    */     //   0: aload_0
/*    */     //   1: aload_1
/*    */     //   2: <illegal opcode> equals : (Lnet/minecraft/world/InteractionResult$ItemContext;Ljava/lang/Object;)Z
/*    */     //   7: ireturn
/*    */     // Line number table:
/*    */     //   Java source line number -> byte code offset
/*    */     //   #63	-> 0
/*    */     // Local variable table:
/*    */     //   start	length	slot	name	descriptor
/*    */     //   0	8	0	this	Lnet/minecraft/world/InteractionResult$ItemContext;
/*    */     //   0	8	1	o	Ljava/lang/Object;
/*    */   }
/*    */   
/*    */   public boolean wasItemInteraction() {
/* 63 */     return this.wasItemInteraction;
/*    */   }
/*    */   
/*    */   public ItemStack heldItemTransformedTo() {
/* 63 */     return this.heldItemTransformedTo;
/*    */   }
/*    */   
/* 64 */   public static final ItemContext NONE = new ItemContext(false, null);
/*    */   
/* 65 */   public static final ItemContext DEFAULT = new ItemContext(true, null);
/*    */ }


/* Location:              C:\Users\karol\AppData\Roaming\ModrinthApp\meta\versions\26.2-0.19.5\26.2-0.19.5.jar!\net\minecraft\world\InteractionResult$ItemContext.class
 * Java compiler version: 25 (69.0)
 * JD-Core Version:       1.1.3
 */