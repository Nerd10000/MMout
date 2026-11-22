/*    */ package net.minecraft.references;
/*    */ 
/*    */ import net.minecraft.core.registries.Registries;
/*    */ import net.minecraft.resources.Identifier;
/*    */ import net.minecraft.resources.ResourceKey;
/*    */ import net.minecraft.world.item.Item;
/*    */ import net.minecraft.world.level.block.Block;
/*    */ 
/*    */ public final class BlockItemId extends Record {
/*    */   private final ResourceKey<Block> block;
/*    */   
/*    */   private final ResourceKey<Item> item;
/*    */   
/*    */   public BlockItemId(ResourceKey<Block> block, ResourceKey<Item> item) {
/*  9 */     this.block = block;
/*  9 */     this.item = item;
/*    */   }
/*    */   
/*    */   public final String toString() {
/*    */     // Byte code:
/*    */     //   0: aload_0
/*    */     //   1: <illegal opcode> toString : (Lnet/minecraft/references/BlockItemId;)Ljava/lang/String;
/*    */     //   6: areturn
/*    */     // Line number table:
/*    */     //   Java source line number -> byte code offset
/*    */     //   #9	-> 0
/*    */     // Local variable table:
/*    */     //   start	length	slot	name	descriptor
/*    */     //   0	7	0	this	Lnet/minecraft/references/BlockItemId;
/*    */   }
/*    */   
/*    */   public final int hashCode() {
/*    */     // Byte code:
/*    */     //   0: aload_0
/*    */     //   1: <illegal opcode> hashCode : (Lnet/minecraft/references/BlockItemId;)I
/*    */     //   6: ireturn
/*    */     // Line number table:
/*    */     //   Java source line number -> byte code offset
/*    */     //   #9	-> 0
/*    */     // Local variable table:
/*    */     //   start	length	slot	name	descriptor
/*    */     //   0	7	0	this	Lnet/minecraft/references/BlockItemId;
/*    */   }
/*    */   
/*    */   public final boolean equals(Object o) {
/*    */     // Byte code:
/*    */     //   0: aload_0
/*    */     //   1: aload_1
/*    */     //   2: <illegal opcode> equals : (Lnet/minecraft/references/BlockItemId;Ljava/lang/Object;)Z
/*    */     //   7: ireturn
/*    */     // Line number table:
/*    */     //   Java source line number -> byte code offset
/*    */     //   #9	-> 0
/*    */     // Local variable table:
/*    */     //   start	length	slot	name	descriptor
/*    */     //   0	8	0	this	Lnet/minecraft/references/BlockItemId;
/*    */     //   0	8	1	o	Ljava/lang/Object;
/*    */   }
/*    */   
/*    */   public ResourceKey<Block> block() {
/*  9 */     return this.block;
/*    */   }
/*    */   
/*    */   public ResourceKey<Item> item() {
/*  9 */     return this.item;
/*    */   }
/*    */   
/*    */   public static BlockItemId create(Identifier blockId, Identifier itemId) {
/* 14 */     return new BlockItemId(
/* 15 */         ResourceKey.create(Registries.BLOCK, blockId), 
/* 16 */         ResourceKey.create(Registries.ITEM, itemId));
/*    */   }
/*    */   
/*    */   public static BlockItemId create(String blockName, String itemName) {
/* 21 */     return create(
/* 22 */         Identifier.withDefaultNamespace(blockName), 
/* 23 */         Identifier.withDefaultNamespace(itemName));
/*    */   }
/*    */   
/*    */   public static BlockItemId create(String name) {
/* 28 */     Identifier id = Identifier.withDefaultNamespace(name);
/* 29 */     return create(id, id);
/*    */   }
/*    */ }


/* Location:              C:\Users\karol\AppData\Roaming\ModrinthApp\meta\versions\26.2-0.19.5\26.2-0.19.5.jar!\net\minecraft\references\BlockItemId.class
 * Java compiler version: 25 (69.0)
 * JD-Core Version:       1.1.3
 */