/*    */ package net.minecraft.world;
/*    */ 
/*    */ import net.minecraft.world.item.ItemStack;
/*    */ 
/*    */ public final class Success extends Record implements InteractionResult {
/*    */   private final InteractionResult.SwingSource swingSource;
/*    */   
/*    */   private final InteractionResult.ItemContext itemContext;
/*    */   
/*    */   public Success(InteractionResult.SwingSource swingSource, InteractionResult.ItemContext itemContext) {
/* 39 */     this.swingSource = swingSource;
/* 39 */     this.itemContext = itemContext;
/*    */   }
/*    */   
/*    */   public final String toString() {
/*    */     // Byte code:
/*    */     //   0: aload_0
/*    */     //   1: <illegal opcode> toString : (Lnet/minecraft/world/InteractionResult$Success;)Ljava/lang/String;
/*    */     //   6: areturn
/*    */     // Line number table:
/*    */     //   Java source line number -> byte code offset
/*    */     //   #39	-> 0
/*    */     // Local variable table:
/*    */     //   start	length	slot	name	descriptor
/*    */     //   0	7	0	this	Lnet/minecraft/world/InteractionResult$Success;
/*    */   }
/*    */   
/*    */   public final int hashCode() {
/*    */     // Byte code:
/*    */     //   0: aload_0
/*    */     //   1: <illegal opcode> hashCode : (Lnet/minecraft/world/InteractionResult$Success;)I
/*    */     //   6: ireturn
/*    */     // Line number table:
/*    */     //   Java source line number -> byte code offset
/*    */     //   #39	-> 0
/*    */     // Local variable table:
/*    */     //   start	length	slot	name	descriptor
/*    */     //   0	7	0	this	Lnet/minecraft/world/InteractionResult$Success;
/*    */   }
/*    */   
/*    */   public final boolean equals(Object o) {
/*    */     // Byte code:
/*    */     //   0: aload_0
/*    */     //   1: aload_1
/*    */     //   2: <illegal opcode> equals : (Lnet/minecraft/world/InteractionResult$Success;Ljava/lang/Object;)Z
/*    */     //   7: ireturn
/*    */     // Line number table:
/*    */     //   Java source line number -> byte code offset
/*    */     //   #39	-> 0
/*    */     // Local variable table:
/*    */     //   start	length	slot	name	descriptor
/*    */     //   0	8	0	this	Lnet/minecraft/world/InteractionResult$Success;
/*    */     //   0	8	1	o	Ljava/lang/Object;
/*    */   }
/*    */   
/*    */   public InteractionResult.SwingSource swingSource() {
/* 39 */     return this.swingSource;
/*    */   }
/*    */   
/*    */   public InteractionResult.ItemContext itemContext() {
/* 39 */     return this.itemContext;
/*    */   }
/*    */   
/*    */   public boolean consumesAction() {
/* 43 */     return true;
/*    */   }
/*    */   
/*    */   public Success heldItemTransformedTo(ItemStack itemStack) {
/* 47 */     return new Success(this.swingSource, new InteractionResult.ItemContext(true, itemStack));
/*    */   }
/*    */   
/*    */   public Success withoutItem() {
/* 51 */     return new Success(this.swingSource, InteractionResult.ItemContext.NONE);
/*    */   }
/*    */   
/*    */   public boolean wasItemInteraction() {
/* 55 */     return this.itemContext.wasItemInteraction;
/*    */   }
/*    */   
/*    */   public ItemStack heldItemTransformedTo() {
/* 59 */     return this.itemContext.heldItemTransformedTo;
/*    */   }
/*    */ }


/* Location:              C:\Users\karol\AppData\Roaming\ModrinthApp\meta\versions\26.2-0.19.5\26.2-0.19.5.jar!\net\minecraft\world\InteractionResult$Success.class
 * Java compiler version: 25 (69.0)
 * JD-Core Version:       1.1.3
 */