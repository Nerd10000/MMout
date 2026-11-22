/*    */ package net.minecraft.util.filefix.operations;
/*    */ 
/*    */ import java.io.IOException;
/*    */ import java.nio.file.Path;
/*    */ import java.util.List;
/*    */ import java.util.Map;
/*    */ import net.minecraft.util.worldupdate.UpgradeProgress;
/*    */ 
/*    */ public final class GroupMove extends Record implements FileFixOperation {
/*    */   private final Map<String, String> fromTo;
/*    */   
/*    */   private final List<Move> fixers;
/*    */   
/*    */   public GroupMove(Map<String, String> fromTo, List<Move> fixers) {
/* 10 */     this.fromTo = fromTo;
/* 10 */     this.fixers = fixers;
/*    */   }
/*    */   
/*    */   public final String toString() {
/*    */     // Byte code:
/*    */     //   0: aload_0
/*    */     //   1: <illegal opcode> toString : (Lnet/minecraft/util/filefix/operations/GroupMove;)Ljava/lang/String;
/*    */     //   6: areturn
/*    */     // Line number table:
/*    */     //   Java source line number -> byte code offset
/*    */     //   #10	-> 0
/*    */     // Local variable table:
/*    */     //   start	length	slot	name	descriptor
/*    */     //   0	7	0	this	Lnet/minecraft/util/filefix/operations/GroupMove;
/*    */   }
/*    */   
/*    */   public final int hashCode() {
/*    */     // Byte code:
/*    */     //   0: aload_0
/*    */     //   1: <illegal opcode> hashCode : (Lnet/minecraft/util/filefix/operations/GroupMove;)I
/*    */     //   6: ireturn
/*    */     // Line number table:
/*    */     //   Java source line number -> byte code offset
/*    */     //   #10	-> 0
/*    */     // Local variable table:
/*    */     //   start	length	slot	name	descriptor
/*    */     //   0	7	0	this	Lnet/minecraft/util/filefix/operations/GroupMove;
/*    */   }
/*    */   
/*    */   public final boolean equals(Object o) {
/*    */     // Byte code:
/*    */     //   0: aload_0
/*    */     //   1: aload_1
/*    */     //   2: <illegal opcode> equals : (Lnet/minecraft/util/filefix/operations/GroupMove;Ljava/lang/Object;)Z
/*    */     //   7: ireturn
/*    */     // Line number table:
/*    */     //   Java source line number -> byte code offset
/*    */     //   #10	-> 0
/*    */     // Local variable table:
/*    */     //   start	length	slot	name	descriptor
/*    */     //   0	8	0	this	Lnet/minecraft/util/filefix/operations/GroupMove;
/*    */     //   0	8	1	o	Ljava/lang/Object;
/*    */   }
/*    */   
/*    */   public Map<String, String> fromTo() {
/* 10 */     return this.fromTo;
/*    */   }
/*    */   
/*    */   public List<Move> fixers() {
/* 10 */     return this.fixers;
/*    */   }
/*    */   
/*    */   public void fix(Path baseDirectory, UpgradeProgress upgradeProgress) throws IOException {
/* 13 */     for (Map.Entry<String, String> entry : this.fromTo.entrySet()) {
/* 14 */       for (Move moveOperation : this.fixers) {
/* 15 */         Move relative = moveOperation.relative(entry.getKey(), entry.getValue());
/* 16 */         relative.fix(baseDirectory, upgradeProgress);
/*    */       } 
/*    */     } 
/*    */   }
/*    */ }


/* Location:              C:\Users\karol\AppData\Roaming\ModrinthApp\meta\versions\26.2-0.19.5\26.2-0.19.5.jar!\net\minecraf\\util\filefix\operations\GroupMove.class
 * Java compiler version: 25 (69.0)
 * JD-Core Version:       1.1.3
 */