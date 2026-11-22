/*    */ package net.minecraft.nbt.visitors;
/*    */ 
/*    */ import java.util.HashMap;
/*    */ import java.util.Map;
/*    */ import net.minecraft.nbt.TagType;
/*    */ 
/*    */ public final class FieldTree extends Record {
/*    */   private final int depth;
/*    */   
/*    */   private final Map<String, TagType<?>> selectedFields;
/*    */   
/*    */   private final Map<String, FieldTree> fieldsToRecurse;
/*    */   
/*    */   public FieldTree(int depth, Map<String, TagType<?>> selectedFields, Map<String, FieldTree> fieldsToRecurse) {
/*  8 */     this.depth = depth;
/*  8 */     this.selectedFields = selectedFields;
/*  8 */     this.fieldsToRecurse = fieldsToRecurse;
/*    */   }
/*    */   
/*    */   public final String toString() {
/*    */     // Byte code:
/*    */     //   0: aload_0
/*    */     //   1: <illegal opcode> toString : (Lnet/minecraft/nbt/visitors/FieldTree;)Ljava/lang/String;
/*    */     //   6: areturn
/*    */     // Line number table:
/*    */     //   Java source line number -> byte code offset
/*    */     //   #8	-> 0
/*    */     // Local variable table:
/*    */     //   start	length	slot	name	descriptor
/*    */     //   0	7	0	this	Lnet/minecraft/nbt/visitors/FieldTree;
/*    */   }
/*    */   
/*    */   public final int hashCode() {
/*    */     // Byte code:
/*    */     //   0: aload_0
/*    */     //   1: <illegal opcode> hashCode : (Lnet/minecraft/nbt/visitors/FieldTree;)I
/*    */     //   6: ireturn
/*    */     // Line number table:
/*    */     //   Java source line number -> byte code offset
/*    */     //   #8	-> 0
/*    */     // Local variable table:
/*    */     //   start	length	slot	name	descriptor
/*    */     //   0	7	0	this	Lnet/minecraft/nbt/visitors/FieldTree;
/*    */   }
/*    */   
/*    */   public final boolean equals(Object o) {
/*    */     // Byte code:
/*    */     //   0: aload_0
/*    */     //   1: aload_1
/*    */     //   2: <illegal opcode> equals : (Lnet/minecraft/nbt/visitors/FieldTree;Ljava/lang/Object;)Z
/*    */     //   7: ireturn
/*    */     // Line number table:
/*    */     //   Java source line number -> byte code offset
/*    */     //   #8	-> 0
/*    */     // Local variable table:
/*    */     //   start	length	slot	name	descriptor
/*    */     //   0	8	0	this	Lnet/minecraft/nbt/visitors/FieldTree;
/*    */     //   0	8	1	o	Ljava/lang/Object;
/*    */   }
/*    */   
/*    */   public int depth() {
/*  8 */     return this.depth;
/*    */   }
/*    */   
/*    */   public Map<String, TagType<?>> selectedFields() {
/*  8 */     return this.selectedFields;
/*    */   }
/*    */   
/*    */   public Map<String, FieldTree> fieldsToRecurse() {
/*  8 */     return this.fieldsToRecurse;
/*    */   }
/*    */   
/*    */   private FieldTree(int depth) {
/* 10 */     this(depth, new HashMap<>(), new HashMap<>());
/*    */   }
/*    */   
/*    */   public static FieldTree createRoot() {
/* 14 */     return new FieldTree(1);
/*    */   }
/*    */   
/*    */   public void addEntry(FieldSelector field) {
/* 18 */     if (this.depth <= field.path().size()) {
/* 19 */       ((FieldTree)this.fieldsToRecurse.computeIfAbsent(field.path().get(this.depth - 1), s -> new FieldTree(this.depth + 1))).addEntry(field);
/*    */     } else {
/* 21 */       this.selectedFields.put(field.name(), field.type());
/*    */     } 
/*    */   }
/*    */   
/*    */   public boolean isSelected(TagType<?> type, String id) {
/* 26 */     return type.equals(selectedFields().get(id));
/*    */   }
/*    */ }


/* Location:              C:\Users\karol\AppData\Roaming\ModrinthApp\meta\versions\26.2-0.19.5\26.2-0.19.5.jar!\net\minecraft\nbt\visitors\FieldTree.class
 * Java compiler version: 25 (69.0)
 * JD-Core Version:       1.1.3
 */