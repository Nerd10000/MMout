/*     */ package net.minecraft.util;
/*     */ 
/*     */ import java.util.ArrayList;
/*     */ import java.util.LinkedHashMap;
/*     */ import java.util.List;
/*     */ import java.util.Map;
/*     */ import java.util.function.Function;
/*     */ 
/*     */ final class ProblemTreeNode extends Record {
/*     */   private final ProblemReporter.PathElement element;
/*     */   
/*     */   private final List<ProblemReporter.Problem> problems;
/*     */   
/*     */   private final Map<ProblemReporter.PathElement, ProblemTreeNode> children;
/*     */   
/*     */   private ProblemTreeNode(ProblemReporter.PathElement element, List<ProblemReporter.Problem> problems, Map<ProblemReporter.PathElement, ProblemTreeNode> children) {
/* 187 */     this.element = element;
/* 187 */     this.problems = problems;
/* 187 */     this.children = children;
/*     */   }
/*     */   
/*     */   public final String toString() {
/*     */     // Byte code:
/*     */     //   0: aload_0
/*     */     //   1: <illegal opcode> toString : (Lnet/minecraft/util/ProblemReporter$Collector$ProblemTreeNode;)Ljava/lang/String;
/*     */     //   6: areturn
/*     */     // Line number table:
/*     */     //   Java source line number -> byte code offset
/*     */     //   #187	-> 0
/*     */     // Local variable table:
/*     */     //   start	length	slot	name	descriptor
/*     */     //   0	7	0	this	Lnet/minecraft/util/ProblemReporter$Collector$ProblemTreeNode;
/*     */   }
/*     */   
/*     */   public final int hashCode() {
/*     */     // Byte code:
/*     */     //   0: aload_0
/*     */     //   1: <illegal opcode> hashCode : (Lnet/minecraft/util/ProblemReporter$Collector$ProblemTreeNode;)I
/*     */     //   6: ireturn
/*     */     // Line number table:
/*     */     //   Java source line number -> byte code offset
/*     */     //   #187	-> 0
/*     */     // Local variable table:
/*     */     //   start	length	slot	name	descriptor
/*     */     //   0	7	0	this	Lnet/minecraft/util/ProblemReporter$Collector$ProblemTreeNode;
/*     */   }
/*     */   
/*     */   public final boolean equals(Object o) {
/*     */     // Byte code:
/*     */     //   0: aload_0
/*     */     //   1: aload_1
/*     */     //   2: <illegal opcode> equals : (Lnet/minecraft/util/ProblemReporter$Collector$ProblemTreeNode;Ljava/lang/Object;)Z
/*     */     //   7: ireturn
/*     */     // Line number table:
/*     */     //   Java source line number -> byte code offset
/*     */     //   #187	-> 0
/*     */     // Local variable table:
/*     */     //   start	length	slot	name	descriptor
/*     */     //   0	8	0	this	Lnet/minecraft/util/ProblemReporter$Collector$ProblemTreeNode;
/*     */     //   0	8	1	o	Ljava/lang/Object;
/*     */   }
/*     */   
/*     */   public ProblemReporter.PathElement element() {
/* 187 */     return this.element;
/*     */   }
/*     */   
/*     */   public List<ProblemReporter.Problem> problems() {
/* 187 */     return this.problems;
/*     */   }
/*     */   
/*     */   public Map<ProblemReporter.PathElement, ProblemTreeNode> children() {
/* 187 */     return this.children;
/*     */   }
/*     */   
/*     */   public ProblemTreeNode(ProblemReporter.PathElement pathElement) {
/* 193 */     this(pathElement, new ArrayList<>(), new LinkedHashMap<>());
/*     */   }
/*     */   
/*     */   public ProblemTreeNode child(ProblemReporter.PathElement id) {
/* 201 */     return this.children.computeIfAbsent(id, ProblemTreeNode::new);
/*     */   }
/*     */   
/*     */   public List<String> getLines() {
/* 206 */     int problemCount = this.problems.size();
/* 207 */     int childrenCount = this.children.size();
/* 209 */     if (problemCount == 0 && childrenCount == 0)
/* 210 */       return List.of(); 
/* 212 */     if (problemCount == 0 && childrenCount == 1) {
/* 213 */       List<String> list = new ArrayList<>();
/* 214 */       this.children.forEach((element, child) -> lines.addAll(child.getLines()));
/* 215 */       list.set(0, this.element.get() + this.element.get());
/* 216 */       return list;
/*     */     } 
/* 218 */     if (problemCount == 1 && childrenCount == 0)
/* 219 */       return List.of(this.element.get() + ": " + this.element.get()); 
/* 222 */     List<String> lines = new ArrayList<>();
/* 223 */     this.children.forEach((element, child) -> lines.addAll(child.getLines()));
/* 224 */     lines.replaceAll(s -> "  " + s);
/* 226 */     for (ProblemReporter.Problem problem : this.problems)
/* 227 */       lines.add("  " + problem.description()); 
/* 230 */     lines.addFirst(this.element.get() + ":");
/* 231 */     return lines;
/*     */   }
/*     */ }


/* Location:              C:\Users\karol\AppData\Roaming\ModrinthApp\meta\versions\26.2-0.19.5\26.2-0.19.5.jar!\net\minecraf\\util\ProblemReporter$Collector$ProblemTreeNode.class
 * Java compiler version: 25 (69.0)
 * JD-Core Version:       1.1.3
 */