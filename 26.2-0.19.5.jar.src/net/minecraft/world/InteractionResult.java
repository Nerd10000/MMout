/*    */ package net.minecraft.world;
/*    */ 
/*    */ import net.minecraft.world.item.ItemStack;
/*    */ 
/*    */ public interface InteractionResult {
/*    */   public enum SwingSource {
/*  8 */     NONE, CLIENT, SERVER;
/*    */   }
/*    */   
/* 16 */   public static final Success SUCCESS = new Success(SwingSource.CLIENT, ItemContext.DEFAULT);
/*    */   
/* 21 */   public static final Success SUCCESS_SERVER = new Success(SwingSource.SERVER, ItemContext.DEFAULT);
/*    */   
/* 23 */   public static final Success CONSUME = new Success(SwingSource.NONE, ItemContext.DEFAULT);
/*    */   
/* 27 */   public static final Fail FAIL = new Fail();
/*    */   
/* 31 */   public static final Pass PASS = new Pass();
/*    */   
/* 33 */   public static final TryEmptyHandInteraction TRY_WITH_EMPTY_HAND = new TryEmptyHandInteraction();
/*    */   
/*    */   default boolean consumesAction() {
/* 36 */     return false;
/*    */   }
/*    */   
/*    */   public static final class Success extends Record implements InteractionResult {
/*    */     private final InteractionResult.SwingSource swingSource;
/*    */     
/*    */     private final InteractionResult.ItemContext itemContext;
/*    */     
/*    */     public Success(InteractionResult.SwingSource swingSource, InteractionResult.ItemContext itemContext) {
/* 39 */       this.swingSource = swingSource;
/* 39 */       this.itemContext = itemContext;
/*    */     }
/*    */     
/*    */     public final String toString() {
/*    */       // Byte code:
/*    */       //   0: aload_0
/*    */       //   1: <illegal opcode> toString : (Lnet/minecraft/world/InteractionResult$Success;)Ljava/lang/String;
/*    */       //   6: areturn
/*    */       // Line number table:
/*    */       //   Java source line number -> byte code offset
/*    */       //   #39	-> 0
/*    */       // Local variable table:
/*    */       //   start	length	slot	name	descriptor
/*    */       //   0	7	0	this	Lnet/minecraft/world/InteractionResult$Success;
/*    */     }
/*    */     
/*    */     public final int hashCode() {
/*    */       // Byte code:
/*    */       //   0: aload_0
/*    */       //   1: <illegal opcode> hashCode : (Lnet/minecraft/world/InteractionResult$Success;)I
/*    */       //   6: ireturn
/*    */       // Line number table:
/*    */       //   Java source line number -> byte code offset
/*    */       //   #39	-> 0
/*    */       // Local variable table:
/*    */       //   start	length	slot	name	descriptor
/*    */       //   0	7	0	this	Lnet/minecraft/world/InteractionResult$Success;
/*    */     }
/*    */     
/*    */     public final boolean equals(Object o) {
/*    */       // Byte code:
/*    */       //   0: aload_0
/*    */       //   1: aload_1
/*    */       //   2: <illegal opcode> equals : (Lnet/minecraft/world/InteractionResult$Success;Ljava/lang/Object;)Z
/*    */       //   7: ireturn
/*    */       // Line number table:
/*    */       //   Java source line number -> byte code offset
/*    */       //   #39	-> 0
/*    */       // Local variable table:
/*    */       //   start	length	slot	name	descriptor
/*    */       //   0	8	0	this	Lnet/minecraft/world/InteractionResult$Success;
/*    */       //   0	8	1	o	Ljava/lang/Object;
/*    */     }
/*    */     
/*    */     public InteractionResult.SwingSource swingSource() {
/* 39 */       return this.swingSource;
/*    */     }
/*    */     
/*    */     public InteractionResult.ItemContext itemContext() {
/* 39 */       return this.itemContext;
/*    */     }
/*    */     
/*    */     public boolean consumesAction() {
/* 43 */       return true;
/*    */     }
/*    */     
/*    */     public Success heldItemTransformedTo(ItemStack itemStack) {
/* 47 */       return new Success(this.swingSource, new InteractionResult.ItemContext(true, itemStack));
/*    */     }
/*    */     
/*    */     public Success withoutItem() {
/* 51 */       return new Success(this.swingSource, InteractionResult.ItemContext.NONE);
/*    */     }
/*    */     
/*    */     public boolean wasItemInteraction() {
/* 55 */       return this.itemContext.wasItemInteraction;
/*    */     }
/*    */     
/*    */     public ItemStack heldItemTransformedTo() {
/* 59 */       return this.itemContext.heldItemTransformedTo;
/*    */     }
/*    */   }
/*    */   
/*    */   public static final class ItemContext extends Record {
/*    */     private final boolean wasItemInteraction;
/*    */     
/*    */     private final ItemStack heldItemTransformedTo;
/*    */     
/*    */     public ItemContext(boolean wasItemInteraction, ItemStack heldItemTransformedTo) {
/* 63 */       this.wasItemInteraction = wasItemInteraction;
/* 63 */       this.heldItemTransformedTo = heldItemTransformedTo;
/*    */     }
/*    */     
/*    */     public final String toString() {
/*    */       // Byte code:
/*    */       //   0: aload_0
/*    */       //   1: <illegal opcode> toString : (Lnet/minecraft/world/InteractionResult$ItemContext;)Ljava/lang/String;
/*    */       //   6: areturn
/*    */       // Line number table:
/*    */       //   Java source line number -> byte code offset
/*    */       //   #63	-> 0
/*    */       // Local variable table:
/*    */       //   start	length	slot	name	descriptor
/*    */       //   0	7	0	this	Lnet/minecraft/world/InteractionResult$ItemContext;
/*    */     }
/*    */     
/*    */     public final int hashCode() {
/*    */       // Byte code:
/*    */       //   0: aload_0
/*    */       //   1: <illegal opcode> hashCode : (Lnet/minecraft/world/InteractionResult$ItemContext;)I
/*    */       //   6: ireturn
/*    */       // Line number table:
/*    */       //   Java source line number -> byte code offset
/*    */       //   #63	-> 0
/*    */       // Local variable table:
/*    */       //   start	length	slot	name	descriptor
/*    */       //   0	7	0	this	Lnet/minecraft/world/InteractionResult$ItemContext;
/*    */     }
/*    */     
/*    */     public final boolean equals(Object o) {
/*    */       // Byte code:
/*    */       //   0: aload_0
/*    */       //   1: aload_1
/*    */       //   2: <illegal opcode> equals : (Lnet/minecraft/world/InteractionResult$ItemContext;Ljava/lang/Object;)Z
/*    */       //   7: ireturn
/*    */       // Line number table:
/*    */       //   Java source line number -> byte code offset
/*    */       //   #63	-> 0
/*    */       // Local variable table:
/*    */       //   start	length	slot	name	descriptor
/*    */       //   0	8	0	this	Lnet/minecraft/world/InteractionResult$ItemContext;
/*    */       //   0	8	1	o	Ljava/lang/Object;
/*    */     }
/*    */     
/*    */     public boolean wasItemInteraction() {
/* 63 */       return this.wasItemInteraction;
/*    */     }
/*    */     
/*    */     public ItemStack heldItemTransformedTo() {
/* 63 */       return this.heldItemTransformedTo;
/*    */     }
/*    */     
/* 64 */     public static final ItemContext NONE = new ItemContext(false, null);
/*    */     
/* 65 */     public static final ItemContext DEFAULT = new ItemContext(true, null);
/*    */   }
/*    */   
/*    */   public static final class Fail extends Record implements InteractionResult {
/*    */     public final String toString() {
/*    */       // Byte code:
/*    */       //   0: aload_0
/*    */       //   1: <illegal opcode> toString : (Lnet/minecraft/world/InteractionResult$Fail;)Ljava/lang/String;
/*    */       //   6: areturn
/*    */       // Line number table:
/*    */       //   Java source line number -> byte code offset
/*    */       //   #68	-> 0
/*    */       // Local variable table:
/*    */       //   start	length	slot	name	descriptor
/*    */       //   0	7	0	this	Lnet/minecraft/world/InteractionResult$Fail;
/*    */     }
/*    */     
/*    */     public final int hashCode() {
/*    */       // Byte code:
/*    */       //   0: aload_0
/*    */       //   1: <illegal opcode> hashCode : (Lnet/minecraft/world/InteractionResult$Fail;)I
/*    */       //   6: ireturn
/*    */       // Line number table:
/*    */       //   Java source line number -> byte code offset
/*    */       //   #68	-> 0
/*    */       // Local variable table:
/*    */       //   start	length	slot	name	descriptor
/*    */       //   0	7	0	this	Lnet/minecraft/world/InteractionResult$Fail;
/*    */     }
/*    */     
/*    */     public final boolean equals(Object o) {
/*    */       // Byte code:
/*    */       //   0: aload_0
/*    */       //   1: aload_1
/*    */       //   2: <illegal opcode> equals : (Lnet/minecraft/world/InteractionResult$Fail;Ljava/lang/Object;)Z
/*    */       //   7: ireturn
/*    */       // Line number table:
/*    */       //   Java source line number -> byte code offset
/*    */       //   #68	-> 0
/*    */       // Local variable table:
/*    */       //   start	length	slot	name	descriptor
/*    */       //   0	8	0	this	Lnet/minecraft/world/InteractionResult$Fail;
/*    */       //   0	8	1	o	Ljava/lang/Object;
/*    */     }
/*    */   }
/*    */   
/*    */   public static final class Pass extends Record implements InteractionResult {
/*    */     public final String toString() {
/*    */       // Byte code:
/*    */       //   0: aload_0
/*    */       //   1: <illegal opcode> toString : (Lnet/minecraft/world/InteractionResult$Pass;)Ljava/lang/String;
/*    */       //   6: areturn
/*    */       // Line number table:
/*    */       //   Java source line number -> byte code offset
/*    */       //   #71	-> 0
/*    */       // Local variable table:
/*    */       //   start	length	slot	name	descriptor
/*    */       //   0	7	0	this	Lnet/minecraft/world/InteractionResult$Pass;
/*    */     }
/*    */     
/*    */     public final int hashCode() {
/*    */       // Byte code:
/*    */       //   0: aload_0
/*    */       //   1: <illegal opcode> hashCode : (Lnet/minecraft/world/InteractionResult$Pass;)I
/*    */       //   6: ireturn
/*    */       // Line number table:
/*    */       //   Java source line number -> byte code offset
/*    */       //   #71	-> 0
/*    */       // Local variable table:
/*    */       //   start	length	slot	name	descriptor
/*    */       //   0	7	0	this	Lnet/minecraft/world/InteractionResult$Pass;
/*    */     }
/*    */     
/*    */     public final boolean equals(Object o) {
/*    */       // Byte code:
/*    */       //   0: aload_0
/*    */       //   1: aload_1
/*    */       //   2: <illegal opcode> equals : (Lnet/minecraft/world/InteractionResult$Pass;Ljava/lang/Object;)Z
/*    */       //   7: ireturn
/*    */       // Line number table:
/*    */       //   Java source line number -> byte code offset
/*    */       //   #71	-> 0
/*    */       // Local variable table:
/*    */       //   start	length	slot	name	descriptor
/*    */       //   0	8	0	this	Lnet/minecraft/world/InteractionResult$Pass;
/*    */       //   0	8	1	o	Ljava/lang/Object;
/*    */     }
/*    */   }
/*    */   
/*    */   public static final class TryEmptyHandInteraction extends Record implements InteractionResult {
/*    */     public final String toString() {
/*    */       // Byte code:
/*    */       //   0: aload_0
/*    */       //   1: <illegal opcode> toString : (Lnet/minecraft/world/InteractionResult$TryEmptyHandInteraction;)Ljava/lang/String;
/*    */       //   6: areturn
/*    */       // Line number table:
/*    */       //   Java source line number -> byte code offset
/*    */       //   #74	-> 0
/*    */       // Local variable table:
/*    */       //   start	length	slot	name	descriptor
/*    */       //   0	7	0	this	Lnet/minecraft/world/InteractionResult$TryEmptyHandInteraction;
/*    */     }
/*    */     
/*    */     public final int hashCode() {
/*    */       // Byte code:
/*    */       //   0: aload_0
/*    */       //   1: <illegal opcode> hashCode : (Lnet/minecraft/world/InteractionResult$TryEmptyHandInteraction;)I
/*    */       //   6: ireturn
/*    */       // Line number table:
/*    */       //   Java source line number -> byte code offset
/*    */       //   #74	-> 0
/*    */       // Local variable table:
/*    */       //   start	length	slot	name	descriptor
/*    */       //   0	7	0	this	Lnet/minecraft/world/InteractionResult$TryEmptyHandInteraction;
/*    */     }
/*    */     
/*    */     public final boolean equals(Object o) {
/*    */       // Byte code:
/*    */       //   0: aload_0
/*    */       //   1: aload_1
/*    */       //   2: <illegal opcode> equals : (Lnet/minecraft/world/InteractionResult$TryEmptyHandInteraction;Ljava/lang/Object;)Z
/*    */       //   7: ireturn
/*    */       // Line number table:
/*    */       //   Java source line number -> byte code offset
/*    */       //   #74	-> 0
/*    */       // Local variable table:
/*    */       //   start	length	slot	name	descriptor
/*    */       //   0	8	0	this	Lnet/minecraft/world/InteractionResult$TryEmptyHandInteraction;
/*    */       //   0	8	1	o	Ljava/lang/Object;
/*    */     }
/*    */   }
/*    */ }


/* Location:              C:\Users\karol\AppData\Roaming\ModrinthApp\meta\versions\26.2-0.19.5\26.2-0.19.5.jar!\net\minecraft\world\InteractionResult.class
 * Java compiler version: 25 (69.0)
 * JD-Core Version:       1.1.3
 */