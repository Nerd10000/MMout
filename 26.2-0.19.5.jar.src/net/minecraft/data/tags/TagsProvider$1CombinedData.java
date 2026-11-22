/*    */ package net.minecraft.data.tags;
/*    */ 
/*    */ import net.minecraft.core.HolderLookup;
/*    */ 
/*    */ final class CombinedData<T> extends Record {
/*    */   private final HolderLookup.Provider contents;
/*    */   
/*    */   private final TagsProvider.TagLookup<T> parent;
/*    */   
/*    */   CombinedData(HolderLookup.Provider contents, TagsProvider.TagLookup<T> parent) {
/* 58 */     this.contents = contents;
/* 58 */     this.parent = parent;
/*    */   }
/*    */   
/*    */   public final String toString() {
/*    */     // Byte code:
/*    */     //   0: aload_0
/*    */     //   1: <illegal opcode> toString : (Lnet/minecraft/data/tags/TagsProvider$1CombinedData;)Ljava/lang/String;
/*    */     //   6: areturn
/*    */     // Line number table:
/*    */     //   Java source line number -> byte code offset
/*    */     //   #58	-> 0
/*    */     // Local variable table:
/*    */     //   start	length	slot	name	descriptor
/*    */     //   0	7	0	this	Lnet/minecraft/data/tags/TagsProvider$1CombinedData;
/*    */     // Local variable type table:
/*    */     //   start	length	slot	name	signature
/*    */     //   0	7	0	this	Lnet/minecraft/data/tags/TagsProvider$1CombinedData<TT;>;
/*    */   }
/*    */   
/*    */   public final int hashCode() {
/*    */     // Byte code:
/*    */     //   0: aload_0
/*    */     //   1: <illegal opcode> hashCode : (Lnet/minecraft/data/tags/TagsProvider$1CombinedData;)I
/*    */     //   6: ireturn
/*    */     // Line number table:
/*    */     //   Java source line number -> byte code offset
/*    */     //   #58	-> 0
/*    */     // Local variable table:
/*    */     //   start	length	slot	name	descriptor
/*    */     //   0	7	0	this	Lnet/minecraft/data/tags/TagsProvider$1CombinedData;
/*    */     // Local variable type table:
/*    */     //   start	length	slot	name	signature
/*    */     //   0	7	0	this	Lnet/minecraft/data/tags/TagsProvider$1CombinedData<TT;>;
/*    */   }
/*    */   
/*    */   public final boolean equals(Object o) {
/*    */     // Byte code:
/*    */     //   0: aload_0
/*    */     //   1: aload_1
/*    */     //   2: <illegal opcode> equals : (Lnet/minecraft/data/tags/TagsProvider$1CombinedData;Ljava/lang/Object;)Z
/*    */     //   7: ireturn
/*    */     // Line number table:
/*    */     //   Java source line number -> byte code offset
/*    */     //   #58	-> 0
/*    */     // Local variable table:
/*    */     //   start	length	slot	name	descriptor
/*    */     //   0	8	0	this	Lnet/minecraft/data/tags/TagsProvider$1CombinedData;
/*    */     //   0	8	1	o	Ljava/lang/Object;
/*    */     // Local variable type table:
/*    */     //   start	length	slot	name	signature
/*    */     //   0	8	0	this	Lnet/minecraft/data/tags/TagsProvider$1CombinedData<TT;>;
/*    */   }
/*    */   
/*    */   public HolderLookup.Provider contents() {
/* 58 */     return this.contents;
/*    */   }
/*    */   
/*    */   public TagsProvider.TagLookup<T> parent() {
/* 58 */     return this.parent;
/*    */   }
/*    */ }


/* Location:              C:\Users\karol\AppData\Roaming\ModrinthApp\meta\versions\26.2-0.19.5\26.2-0.19.5.jar!\net\minecraft\data\tags\TagsProvider$1CombinedData.class
 * Java compiler version: 25 (69.0)
 * JD-Core Version:       1.1.3
 */