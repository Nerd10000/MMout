/*    */ package net.minecraft.tags;
/*    */ 
/*    */ import net.minecraft.core.registries.Registries;
/*    */ import net.minecraft.resources.Identifier;
/*    */ import net.minecraft.world.item.Item;
/*    */ import net.minecraft.world.level.block.Block;
/*    */ 
/*    */ public final class BlockItemTagId extends Record {
/*    */   private final TagKey<Block> block;
/*    */   
/*    */   private final TagKey<Item> item;
/*    */   
/*    */   public BlockItemTagId(TagKey<Block> block, TagKey<Item> item) {
/*  8 */     this.block = block;
/*  8 */     this.item = item;
/*    */   }
/*    */   
/*    */   public final String toString() {
/*    */     // Byte code:
/*    */     //   0: aload_0
/*    */     //   1: <illegal opcode> toString : (Lnet/minecraft/tags/BlockItemTagId;)Ljava/lang/String;
/*    */     //   6: areturn
/*    */     // Line number table:
/*    */     //   Java source line number -> byte code offset
/*    */     //   #8	-> 0
/*    */     // Local variable table:
/*    */     //   start	length	slot	name	descriptor
/*    */     //   0	7	0	this	Lnet/minecraft/tags/BlockItemTagId;
/*    */   }
/*    */   
/*    */   public final int hashCode() {
/*    */     // Byte code:
/*    */     //   0: aload_0
/*    */     //   1: <illegal opcode> hashCode : (Lnet/minecraft/tags/BlockItemTagId;)I
/*    */     //   6: ireturn
/*    */     // Line number table:
/*    */     //   Java source line number -> byte code offset
/*    */     //   #8	-> 0
/*    */     // Local variable table:
/*    */     //   start	length	slot	name	descriptor
/*    */     //   0	7	0	this	Lnet/minecraft/tags/BlockItemTagId;
/*    */   }
/*    */   
/*    */   public final boolean equals(Object o) {
/*    */     // Byte code:
/*    */     //   0: aload_0
/*    */     //   1: aload_1
/*    */     //   2: <illegal opcode> equals : (Lnet/minecraft/tags/BlockItemTagId;Ljava/lang/Object;)Z
/*    */     //   7: ireturn
/*    */     // Line number table:
/*    */     //   Java source line number -> byte code offset
/*    */     //   #8	-> 0
/*    */     // Local variable table:
/*    */     //   start	length	slot	name	descriptor
/*    */     //   0	8	0	this	Lnet/minecraft/tags/BlockItemTagId;
/*    */     //   0	8	1	o	Ljava/lang/Object;
/*    */   }
/*    */   
/*    */   public TagKey<Block> block() {
/*  8 */     return this.block;
/*    */   }
/*    */   
/*    */   public TagKey<Item> item() {
/*  8 */     return this.item;
/*    */   }
/*    */   
/*    */   public static BlockItemTagId create(Identifier blockId, Identifier itemId) {
/* 13 */     return new BlockItemTagId(
/* 14 */         TagKey.create(Registries.BLOCK, blockId), 
/* 15 */         TagKey.create(Registries.ITEM, itemId));
/*    */   }
/*    */   
/*    */   public static BlockItemTagId create(String blockName, String itemName) {
/* 20 */     return create(
/* 21 */         Identifier.withDefaultNamespace(blockName), 
/* 22 */         Identifier.withDefaultNamespace(itemName));
/*    */   }
/*    */   
/*    */   public static BlockItemTagId create(String name) {
/* 27 */     Identifier id = Identifier.withDefaultNamespace(name);
/* 28 */     return create(id, id);
/*    */   }
/*    */ }


/* Location:              C:\Users\karol\AppData\Roaming\ModrinthApp\meta\versions\26.2-0.19.5\26.2-0.19.5.jar!\net\minecraft\tags\BlockItemTagId.class
 * Java compiler version: 25 (69.0)
 * JD-Core Version:       1.1.3
 */