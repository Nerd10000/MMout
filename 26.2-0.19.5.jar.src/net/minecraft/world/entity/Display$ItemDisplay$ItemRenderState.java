/*     */ package net.minecraft.world.entity;
/*     */ 
/*     */ import net.minecraft.world.item.ItemDisplayContext;
/*     */ import net.minecraft.world.item.ItemStack;
/*     */ 
/*     */ public final class ItemRenderState extends Record {
/*     */   private final ItemStack itemStack;
/*     */   
/*     */   private final ItemDisplayContext itemTransform;
/*     */   
/*     */   public ItemRenderState(ItemStack itemStack, ItemDisplayContext itemTransform) {
/* 589 */     this.itemStack = itemStack;
/* 589 */     this.itemTransform = itemTransform;
/*     */   }
/*     */   
/*     */   public final String toString() {
/*     */     // Byte code:
/*     */     //   0: aload_0
/*     */     //   1: <illegal opcode> toString : (Lnet/minecraft/world/entity/Display$ItemDisplay$ItemRenderState;)Ljava/lang/String;
/*     */     //   6: areturn
/*     */     // Line number table:
/*     */     //   Java source line number -> byte code offset
/*     */     //   #589	-> 0
/*     */     // Local variable table:
/*     */     //   start	length	slot	name	descriptor
/*     */     //   0	7	0	this	Lnet/minecraft/world/entity/Display$ItemDisplay$ItemRenderState;
/*     */   }
/*     */   
/*     */   public final int hashCode() {
/*     */     // Byte code:
/*     */     //   0: aload_0
/*     */     //   1: <illegal opcode> hashCode : (Lnet/minecraft/world/entity/Display$ItemDisplay$ItemRenderState;)I
/*     */     //   6: ireturn
/*     */     // Line number table:
/*     */     //   Java source line number -> byte code offset
/*     */     //   #589	-> 0
/*     */     // Local variable table:
/*     */     //   start	length	slot	name	descriptor
/*     */     //   0	7	0	this	Lnet/minecraft/world/entity/Display$ItemDisplay$ItemRenderState;
/*     */   }
/*     */   
/*     */   public final boolean equals(Object o) {
/*     */     // Byte code:
/*     */     //   0: aload_0
/*     */     //   1: aload_1
/*     */     //   2: <illegal opcode> equals : (Lnet/minecraft/world/entity/Display$ItemDisplay$ItemRenderState;Ljava/lang/Object;)Z
/*     */     //   7: ireturn
/*     */     // Line number table:
/*     */     //   Java source line number -> byte code offset
/*     */     //   #589	-> 0
/*     */     // Local variable table:
/*     */     //   start	length	slot	name	descriptor
/*     */     //   0	8	0	this	Lnet/minecraft/world/entity/Display$ItemDisplay$ItemRenderState;
/*     */     //   0	8	1	o	Ljava/lang/Object;
/*     */   }
/*     */   
/*     */   public ItemStack itemStack() {
/* 589 */     return this.itemStack;
/*     */   }
/*     */   
/*     */   public ItemDisplayContext itemTransform() {
/* 589 */     return this.itemTransform;
/*     */   }
/*     */ }


/* Location:              C:\Users\karol\AppData\Roaming\ModrinthApp\meta\versions\26.2-0.19.5\26.2-0.19.5.jar!\net\minecraft\world\entity\Display$ItemDisplay$ItemRenderState.class
 * Java compiler version: 25 (69.0)
 * JD-Core Version:       1.1.3
 */