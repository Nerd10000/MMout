/*    */ package net.minecraft.util.filefix.fixes;
/*    */ 
/*    */ import com.mojang.serialization.Dynamic;
/*    */ import it.unimi.dsi.fastutil.longs.LongArrayList;
/*    */ import it.unimi.dsi.fastutil.longs.LongList;
/*    */ import java.util.HashMap;
/*    */ import java.util.Map;
/*    */ import net.minecraft.nbt.Tag;
/*    */ 
/*    */ public final class LegacyStructureData extends Record {
/*    */   private final Map<String, Dynamic<?>> starts;
/*    */   
/*    */   private final Map<String, LongList> indexes;
/*    */   
/*    */   public LegacyStructureData(Map<String, Dynamic<?>> starts, Map<String, LongList> indexes) {
/* 77 */     this.starts = starts;
/* 77 */     this.indexes = indexes;
/*    */   }
/*    */   
/*    */   public final String toString() {
/*    */     // Byte code:
/*    */     //   0: aload_0
/*    */     //   1: <illegal opcode> toString : (Lnet/minecraft/util/filefix/fixes/LegacyStructureFileFix$LegacyStructureData;)Ljava/lang/String;
/*    */     //   6: areturn
/*    */     // Line number table:
/*    */     //   Java source line number -> byte code offset
/*    */     //   #77	-> 0
/*    */     // Local variable table:
/*    */     //   start	length	slot	name	descriptor
/*    */     //   0	7	0	this	Lnet/minecraft/util/filefix/fixes/LegacyStructureFileFix$LegacyStructureData;
/*    */   }
/*    */   
/*    */   public final int hashCode() {
/*    */     // Byte code:
/*    */     //   0: aload_0
/*    */     //   1: <illegal opcode> hashCode : (Lnet/minecraft/util/filefix/fixes/LegacyStructureFileFix$LegacyStructureData;)I
/*    */     //   6: ireturn
/*    */     // Line number table:
/*    */     //   Java source line number -> byte code offset
/*    */     //   #77	-> 0
/*    */     // Local variable table:
/*    */     //   start	length	slot	name	descriptor
/*    */     //   0	7	0	this	Lnet/minecraft/util/filefix/fixes/LegacyStructureFileFix$LegacyStructureData;
/*    */   }
/*    */   
/*    */   public final boolean equals(Object o) {
/*    */     // Byte code:
/*    */     //   0: aload_0
/*    */     //   1: aload_1
/*    */     //   2: <illegal opcode> equals : (Lnet/minecraft/util/filefix/fixes/LegacyStructureFileFix$LegacyStructureData;Ljava/lang/Object;)Z
/*    */     //   7: ireturn
/*    */     // Line number table:
/*    */     //   Java source line number -> byte code offset
/*    */     //   #77	-> 0
/*    */     // Local variable table:
/*    */     //   start	length	slot	name	descriptor
/*    */     //   0	8	0	this	Lnet/minecraft/util/filefix/fixes/LegacyStructureFileFix$LegacyStructureData;
/*    */     //   0	8	1	o	Ljava/lang/Object;
/*    */   }
/*    */   
/*    */   public Map<String, Dynamic<?>> starts() {
/* 77 */     return this.starts;
/*    */   }
/*    */   
/*    */   public Map<String, LongList> indexes() {
/* 77 */     return this.indexes;
/*    */   }
/*    */   
/*    */   public LegacyStructureData() {
/* 82 */     this(new HashMap<>(), new HashMap<>());
/*    */   }
/*    */   
/*    */   public void addStart(String id, Dynamic<Tag> data) {
/* 86 */     this.starts.put(id, data);
/*    */   }
/*    */   
/*    */   public void addIndex(String id, long sourcePos) {
/* 90 */     ((LongList)this.indexes.computeIfAbsent(id, l -> new LongArrayList())).add(sourcePos);
/*    */   }
/*    */ }


/* Location:              C:\Users\karol\AppData\Roaming\ModrinthApp\meta\versions\26.2-0.19.5\26.2-0.19.5.jar!\net\minecraf\\util\filefix\fixes\LegacyStructureFileFix$LegacyStructureData.class
 * Java compiler version: 25 (69.0)
 * JD-Core Version:       1.1.3
 */