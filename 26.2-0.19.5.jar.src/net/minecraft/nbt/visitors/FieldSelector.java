/*    */ package net.minecraft.nbt.visitors;
/*    */ 
/*    */ import java.util.List;
/*    */ import net.minecraft.nbt.TagType;
/*    */ 
/*    */ public final class FieldSelector extends Record {
/*    */   private final List<String> path;
/*    */   
/*    */   private final TagType<?> type;
/*    */   
/*    */   private final String name;
/*    */   
/*    */   public FieldSelector(List<String> path, TagType<?> type, String name) {
/*  7 */     this.path = path;
/*  7 */     this.type = type;
/*  7 */     this.name = name;
/*    */   }
/*    */   
/*    */   public final String toString() {
/*    */     // Byte code:
/*    */     //   0: aload_0
/*    */     //   1: <illegal opcode> toString : (Lnet/minecraft/nbt/visitors/FieldSelector;)Ljava/lang/String;
/*    */     //   6: areturn
/*    */     // Line number table:
/*    */     //   Java source line number -> byte code offset
/*    */     //   #7	-> 0
/*    */     // Local variable table:
/*    */     //   start	length	slot	name	descriptor
/*    */     //   0	7	0	this	Lnet/minecraft/nbt/visitors/FieldSelector;
/*    */   }
/*    */   
/*    */   public final int hashCode() {
/*    */     // Byte code:
/*    */     //   0: aload_0
/*    */     //   1: <illegal opcode> hashCode : (Lnet/minecraft/nbt/visitors/FieldSelector;)I
/*    */     //   6: ireturn
/*    */     // Line number table:
/*    */     //   Java source line number -> byte code offset
/*    */     //   #7	-> 0
/*    */     // Local variable table:
/*    */     //   start	length	slot	name	descriptor
/*    */     //   0	7	0	this	Lnet/minecraft/nbt/visitors/FieldSelector;
/*    */   }
/*    */   
/*    */   public final boolean equals(Object o) {
/*    */     // Byte code:
/*    */     //   0: aload_0
/*    */     //   1: aload_1
/*    */     //   2: <illegal opcode> equals : (Lnet/minecraft/nbt/visitors/FieldSelector;Ljava/lang/Object;)Z
/*    */     //   7: ireturn
/*    */     // Line number table:
/*    */     //   Java source line number -> byte code offset
/*    */     //   #7	-> 0
/*    */     // Local variable table:
/*    */     //   start	length	slot	name	descriptor
/*    */     //   0	8	0	this	Lnet/minecraft/nbt/visitors/FieldSelector;
/*    */     //   0	8	1	o	Ljava/lang/Object;
/*    */   }
/*    */   
/*    */   public List<String> path() {
/*  7 */     return this.path;
/*    */   }
/*    */   
/*    */   public TagType<?> type() {
/*  7 */     return this.type;
/*    */   }
/*    */   
/*    */   public String name() {
/*  7 */     return this.name;
/*    */   }
/*    */   
/*    */   public FieldSelector(TagType<?> type, String name) {
/*  9 */     this(List.of(), type, name);
/*    */   }
/*    */   
/*    */   public FieldSelector(String parent, TagType<?> type, String name) {
/* 13 */     this(List.of(parent), type, name);
/*    */   }
/*    */   
/*    */   public FieldSelector(String grandparent, String parent, TagType<?> type, String name) {
/* 17 */     this(List.of(grandparent, parent), type, name);
/*    */   }
/*    */ }


/* Location:              C:\Users\karol\AppData\Roaming\ModrinthApp\meta\versions\26.2-0.19.5\26.2-0.19.5.jar!\net\minecraft\nbt\visitors\FieldSelector.class
 * Java compiler version: 25 (69.0)
 * JD-Core Version:       1.1.3
 */