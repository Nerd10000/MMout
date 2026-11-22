/*    */ package net.minecraft.tags;
/*    */ 
/*    */ import java.util.List;
/*    */ import java.util.function.Consumer;
/*    */ import net.minecraft.resources.Identifier;
/*    */ import net.minecraft.util.DependencySorter;
/*    */ 
/*    */ final class SortingEntry extends Record implements DependencySorter.Entry<Identifier> {
/*    */   private final List<TagLoader.EntryWithSource> entries;
/*    */   
/*    */   private SortingEntry(List<TagLoader.EntryWithSource> entries) {
/* 83 */     this.entries = entries;
/*    */   }
/*    */   
/*    */   public final String toString() {
/*    */     // Byte code:
/*    */     //   0: aload_0
/*    */     //   1: <illegal opcode> toString : (Lnet/minecraft/tags/TagLoader$SortingEntry;)Ljava/lang/String;
/*    */     //   6: areturn
/*    */     // Line number table:
/*    */     //   Java source line number -> byte code offset
/*    */     //   #83	-> 0
/*    */     // Local variable table:
/*    */     //   start	length	slot	name	descriptor
/*    */     //   0	7	0	this	Lnet/minecraft/tags/TagLoader$SortingEntry;
/*    */   }
/*    */   
/*    */   public final int hashCode() {
/*    */     // Byte code:
/*    */     //   0: aload_0
/*    */     //   1: <illegal opcode> hashCode : (Lnet/minecraft/tags/TagLoader$SortingEntry;)I
/*    */     //   6: ireturn
/*    */     // Line number table:
/*    */     //   Java source line number -> byte code offset
/*    */     //   #83	-> 0
/*    */     // Local variable table:
/*    */     //   start	length	slot	name	descriptor
/*    */     //   0	7	0	this	Lnet/minecraft/tags/TagLoader$SortingEntry;
/*    */   }
/*    */   
/*    */   public final boolean equals(Object o) {
/*    */     // Byte code:
/*    */     //   0: aload_0
/*    */     //   1: aload_1
/*    */     //   2: <illegal opcode> equals : (Lnet/minecraft/tags/TagLoader$SortingEntry;Ljava/lang/Object;)Z
/*    */     //   7: ireturn
/*    */     // Line number table:
/*    */     //   Java source line number -> byte code offset
/*    */     //   #83	-> 0
/*    */     // Local variable table:
/*    */     //   start	length	slot	name	descriptor
/*    */     //   0	8	0	this	Lnet/minecraft/tags/TagLoader$SortingEntry;
/*    */     //   0	8	1	o	Ljava/lang/Object;
/*    */   }
/*    */   
/*    */   public List<TagLoader.EntryWithSource> entries() {
/* 83 */     return this.entries;
/*    */   }
/*    */   
/*    */   public void visitRequiredDependencies(Consumer<Identifier> output) {
/* 86 */     this.entries.forEach(e -> e.entry.visitRequiredDependencies(output));
/*    */   }
/*    */   
/*    */   public void visitOptionalDependencies(Consumer<Identifier> output) {
/* 91 */     this.entries.forEach(e -> e.entry.visitOptionalDependencies(output));
/*    */   }
/*    */ }


/* Location:              C:\Users\karol\AppData\Roaming\ModrinthApp\meta\versions\26.2-0.19.5\26.2-0.19.5.jar!\net\minecraft\tags\TagLoader$SortingEntry.class
 * Java compiler version: 25 (69.0)
 * JD-Core Version:       1.1.3
 */