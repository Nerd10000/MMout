/*     */ package net.minecraft.world.item.crafting;
/*     */ 
/*     */ import net.minecraft.world.item.crafting.display.RecipeDisplayEntry;
/*     */ 
/*     */ public final class ServerDisplayInfo extends Record {
/*     */   private final RecipeDisplayEntry display;
/*     */   
/*     */   private final RecipeHolder<?> parent;
/*     */   
/*     */   public ServerDisplayInfo(RecipeDisplayEntry display, RecipeHolder<?> parent) {
/* 271 */     this.display = display;
/* 271 */     this.parent = parent;
/*     */   }
/*     */   
/*     */   public final String toString() {
/*     */     // Byte code:
/*     */     //   0: aload_0
/*     */     //   1: <illegal opcode> toString : (Lnet/minecraft/world/item/crafting/RecipeManager$ServerDisplayInfo;)Ljava/lang/String;
/*     */     //   6: areturn
/*     */     // Line number table:
/*     */     //   Java source line number -> byte code offset
/*     */     //   #271	-> 0
/*     */     // Local variable table:
/*     */     //   start	length	slot	name	descriptor
/*     */     //   0	7	0	this	Lnet/minecraft/world/item/crafting/RecipeManager$ServerDisplayInfo;
/*     */   }
/*     */   
/*     */   public final int hashCode() {
/*     */     // Byte code:
/*     */     //   0: aload_0
/*     */     //   1: <illegal opcode> hashCode : (Lnet/minecraft/world/item/crafting/RecipeManager$ServerDisplayInfo;)I
/*     */     //   6: ireturn
/*     */     // Line number table:
/*     */     //   Java source line number -> byte code offset
/*     */     //   #271	-> 0
/*     */     // Local variable table:
/*     */     //   start	length	slot	name	descriptor
/*     */     //   0	7	0	this	Lnet/minecraft/world/item/crafting/RecipeManager$ServerDisplayInfo;
/*     */   }
/*     */   
/*     */   public final boolean equals(Object o) {
/*     */     // Byte code:
/*     */     //   0: aload_0
/*     */     //   1: aload_1
/*     */     //   2: <illegal opcode> equals : (Lnet/minecraft/world/item/crafting/RecipeManager$ServerDisplayInfo;Ljava/lang/Object;)Z
/*     */     //   7: ireturn
/*     */     // Line number table:
/*     */     //   Java source line number -> byte code offset
/*     */     //   #271	-> 0
/*     */     // Local variable table:
/*     */     //   start	length	slot	name	descriptor
/*     */     //   0	8	0	this	Lnet/minecraft/world/item/crafting/RecipeManager$ServerDisplayInfo;
/*     */     //   0	8	1	o	Ljava/lang/Object;
/*     */   }
/*     */   
/*     */   public RecipeDisplayEntry display() {
/* 271 */     return this.display;
/*     */   }
/*     */   
/*     */   public RecipeHolder<?> parent() {
/* 271 */     return this.parent;
/*     */   }
/*     */ }


/* Location:              C:\Users\karol\AppData\Roaming\ModrinthApp\meta\versions\26.2-0.19.5\26.2-0.19.5.jar!\net\minecraft\world\item\crafting\RecipeManager$ServerDisplayInfo.class
 * Java compiler version: 25 (69.0)
 * JD-Core Version:       1.1.3
 */