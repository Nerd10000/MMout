/*     */ package net.minecraft.util;
/*     */ 
/*     */ import com.google.common.collect.HashMultimap;
/*     */ import java.util.ArrayList;
/*     */ import java.util.Collection;
/*     */ import java.util.LinkedHashMap;
/*     */ import java.util.LinkedHashSet;
/*     */ import java.util.List;
/*     */ import java.util.Map;
/*     */ import java.util.Objects;
/*     */ import java.util.Set;
/*     */ import java.util.function.BiConsumer;
/*     */ import java.util.function.Function;
/*     */ import java.util.stream.Collectors;
/*     */ 
/*     */ public class Collector implements ProblemReporter {
/*     */   private static final class Entry extends Record {
/*     */     private final ProblemReporter.Collector source;
/*     */     
/*     */     private final ProblemReporter.Problem problem;
/*     */     
/*     */     private Entry(ProblemReporter.Collector source, ProblemReporter.Problem problem) {
/*  93 */       this.source = source;
/*  93 */       this.problem = problem;
/*     */     }
/*     */     
/*     */     public final String toString() {
/*     */       // Byte code:
/*     */       //   0: aload_0
/*     */       //   1: <illegal opcode> toString : (Lnet/minecraft/util/ProblemReporter$Collector$Entry;)Ljava/lang/String;
/*     */       //   6: areturn
/*     */       // Line number table:
/*     */       //   Java source line number -> byte code offset
/*     */       //   #93	-> 0
/*     */       // Local variable table:
/*     */       //   start	length	slot	name	descriptor
/*     */       //   0	7	0	this	Lnet/minecraft/util/ProblemReporter$Collector$Entry;
/*     */     }
/*     */     
/*     */     public final int hashCode() {
/*     */       // Byte code:
/*     */       //   0: aload_0
/*     */       //   1: <illegal opcode> hashCode : (Lnet/minecraft/util/ProblemReporter$Collector$Entry;)I
/*     */       //   6: ireturn
/*     */       // Line number table:
/*     */       //   Java source line number -> byte code offset
/*     */       //   #93	-> 0
/*     */       // Local variable table:
/*     */       //   start	length	slot	name	descriptor
/*     */       //   0	7	0	this	Lnet/minecraft/util/ProblemReporter$Collector$Entry;
/*     */     }
/*     */     
/*     */     public final boolean equals(Object o) {
/*     */       // Byte code:
/*     */       //   0: aload_0
/*     */       //   1: aload_1
/*     */       //   2: <illegal opcode> equals : (Lnet/minecraft/util/ProblemReporter$Collector$Entry;Ljava/lang/Object;)Z
/*     */       //   7: ireturn
/*     */       // Line number table:
/*     */       //   Java source line number -> byte code offset
/*     */       //   #93	-> 0
/*     */       // Local variable table:
/*     */       //   start	length	slot	name	descriptor
/*     */       //   0	8	0	this	Lnet/minecraft/util/ProblemReporter$Collector$Entry;
/*     */       //   0	8	1	o	Ljava/lang/Object;
/*     */     }
/*     */     
/*     */     public ProblemReporter.Collector source() {
/*  93 */       return this.source;
/*     */     }
/*     */     
/*     */     public ProblemReporter.Problem problem() {
/*  93 */       return this.problem;
/*     */     }
/*     */   }
/*     */   
/*     */   public static final ProblemReporter.PathElement EMPTY_ROOT = () -> "";
/*     */   
/*     */   private final Collector parent;
/*     */   
/*     */   private final ProblemReporter.PathElement element;
/*     */   
/*     */   private final Set<Entry> problems;
/*     */   
/*     */   public Collector() {
/* 104 */     this(EMPTY_ROOT);
/*     */   }
/*     */   
/*     */   public Collector(ProblemReporter.PathElement root) {
/* 108 */     this.parent = null;
/* 109 */     this.problems = new LinkedHashSet<>();
/* 110 */     this.element = root;
/*     */   }
/*     */   
/*     */   private Collector(Collector parent, ProblemReporter.PathElement path) {
/* 114 */     this.problems = parent.problems;
/* 115 */     this.parent = parent;
/* 116 */     this.element = path;
/*     */   }
/*     */   
/*     */   public ProblemReporter forChild(ProblemReporter.PathElement path) {
/* 121 */     return new Collector(this, path);
/*     */   }
/*     */   
/*     */   public void report(ProblemReporter.Problem problem) {
/* 126 */     this.problems.add(new Entry(this, problem));
/*     */   }
/*     */   
/*     */   public boolean isEmpty() {
/* 130 */     return this.problems.isEmpty();
/*     */   }
/*     */   
/*     */   public void forEach(BiConsumer<String, ProblemReporter.Problem> output) {
/* 134 */     List<ProblemReporter.PathElement> pathElements = new ArrayList<>();
/* 135 */     StringBuilder pathString = new StringBuilder();
/* 137 */     for (Entry entry : this.problems) {
/* 138 */       Collector current = entry.source;
/* 139 */       while (current != null) {
/* 140 */         pathElements.add(current.element);
/* 141 */         current = current.parent;
/*     */       } 
/* 144 */       for (int i = pathElements.size() - 1; i >= 0; i--)
/* 145 */         pathString.append(((ProblemReporter.PathElement)pathElements.get(i)).get()); 
/* 147 */       output.accept(pathString.toString(), entry.problem());
/* 149 */       pathString.setLength(0);
/* 150 */       pathElements.clear();
/*     */     } 
/*     */   }
/*     */   
/*     */   public String getReport() {
/* 155 */     HashMultimap hashMultimap = HashMultimap.create();
/* 156 */     Objects.requireNonNull(hashMultimap);
/* 156 */     forEach(hashMultimap::put);
/* 158 */     return hashMultimap.asMap().entrySet().stream()
/* 159 */       .map(entry -> " at " + (String)entry.getKey() + ": " + (String)((Collection)entry.getValue()).stream().map(ProblemReporter.Problem::description).collect(Collectors.joining("; ")))
/* 160 */       .collect(Collectors.joining("\n"));
/*     */   }
/*     */   
/*     */   public String getTreeReport() {
/* 164 */     List<ProblemReporter.PathElement> pathElements = new ArrayList<>();
/* 166 */     ProblemTreeNode root = new ProblemTreeNode(this.element);
/* 168 */     for (Entry entry : this.problems) {
/* 169 */       Collector current = entry.source;
/* 170 */       while (current != this) {
/* 171 */         pathElements.add(current.element);
/* 172 */         current = current.parent;
/*     */       } 
/* 175 */       ProblemTreeNode node = root;
/* 176 */       for (int i = pathElements.size() - 1; i >= 0; i--)
/* 177 */         node = node.child(pathElements.get(i)); 
/* 179 */       pathElements.clear();
/* 181 */       node.problems.add(entry.problem);
/*     */     } 
/* 184 */     return String.join("\n", (Iterable)root.getLines());
/*     */   }
/*     */   
/*     */   private static final class ProblemTreeNode extends Record {
/*     */     private final ProblemReporter.PathElement element;
/*     */     
/*     */     private final List<ProblemReporter.Problem> problems;
/*     */     
/*     */     private final Map<ProblemReporter.PathElement, ProblemTreeNode> children;
/*     */     
/*     */     private ProblemTreeNode(ProblemReporter.PathElement element, List<ProblemReporter.Problem> problems, Map<ProblemReporter.PathElement, ProblemTreeNode> children) {
/* 187 */       this.element = element;
/* 187 */       this.problems = problems;
/* 187 */       this.children = children;
/*     */     }
/*     */     
/*     */     public final String toString() {
/*     */       // Byte code:
/*     */       //   0: aload_0
/*     */       //   1: <illegal opcode> toString : (Lnet/minecraft/util/ProblemReporter$Collector$ProblemTreeNode;)Ljava/lang/String;
/*     */       //   6: areturn
/*     */       // Line number table:
/*     */       //   Java source line number -> byte code offset
/*     */       //   #187	-> 0
/*     */       // Local variable table:
/*     */       //   start	length	slot	name	descriptor
/*     */       //   0	7	0	this	Lnet/minecraft/util/ProblemReporter$Collector$ProblemTreeNode;
/*     */     }
/*     */     
/*     */     public final int hashCode() {
/*     */       // Byte code:
/*     */       //   0: aload_0
/*     */       //   1: <illegal opcode> hashCode : (Lnet/minecraft/util/ProblemReporter$Collector$ProblemTreeNode;)I
/*     */       //   6: ireturn
/*     */       // Line number table:
/*     */       //   Java source line number -> byte code offset
/*     */       //   #187	-> 0
/*     */       // Local variable table:
/*     */       //   start	length	slot	name	descriptor
/*     */       //   0	7	0	this	Lnet/minecraft/util/ProblemReporter$Collector$ProblemTreeNode;
/*     */     }
/*     */     
/*     */     public final boolean equals(Object o) {
/*     */       // Byte code:
/*     */       //   0: aload_0
/*     */       //   1: aload_1
/*     */       //   2: <illegal opcode> equals : (Lnet/minecraft/util/ProblemReporter$Collector$ProblemTreeNode;Ljava/lang/Object;)Z
/*     */       //   7: ireturn
/*     */       // Line number table:
/*     */       //   Java source line number -> byte code offset
/*     */       //   #187	-> 0
/*     */       // Local variable table:
/*     */       //   start	length	slot	name	descriptor
/*     */       //   0	8	0	this	Lnet/minecraft/util/ProblemReporter$Collector$ProblemTreeNode;
/*     */       //   0	8	1	o	Ljava/lang/Object;
/*     */     }
/*     */     
/*     */     public ProblemReporter.PathElement element() {
/* 187 */       return this.element;
/*     */     }
/*     */     
/*     */     public List<ProblemReporter.Problem> problems() {
/* 187 */       return this.problems;
/*     */     }
/*     */     
/*     */     public Map<ProblemReporter.PathElement, ProblemTreeNode> children() {
/* 187 */       return this.children;
/*     */     }
/*     */     
/*     */     public ProblemTreeNode(ProblemReporter.PathElement pathElement) {
/* 193 */       this(pathElement, new ArrayList<>(), new LinkedHashMap<>());
/*     */     }
/*     */     
/*     */     public ProblemTreeNode child(ProblemReporter.PathElement id) {
/* 201 */       return this.children.computeIfAbsent(id, ProblemTreeNode::new);
/*     */     }
/*     */     
/*     */     public List<String> getLines() {
/* 206 */       int problemCount = this.problems.size();
/* 207 */       int childrenCount = this.children.size();
/* 209 */       if (problemCount == 0 && childrenCount == 0)
/* 210 */         return List.of(); 
/* 212 */       if (problemCount == 0 && childrenCount == 1) {
/* 213 */         List<String> list = new ArrayList<>();
/* 214 */         this.children.forEach((element, child) -> lines.addAll(child.getLines()));
/* 215 */         list.set(0, this.element.get() + this.element.get());
/* 216 */         return list;
/*     */       } 
/* 218 */       if (problemCount == 1 && childrenCount == 0)
/* 219 */         return List.of(this.element.get() + ": " + this.element.get()); 
/* 222 */       List<String> lines = new ArrayList<>();
/* 223 */       this.children.forEach((element, child) -> lines.addAll(child.getLines()));
/* 224 */       lines.replaceAll(s -> "  " + s);
/* 226 */       for (ProblemReporter.Problem problem : this.problems)
/* 227 */         lines.add("  " + problem.description()); 
/* 230 */       lines.addFirst(this.element.get() + ":");
/* 231 */       return lines;
/*     */     }
/*     */   }
/*     */ }


/* Location:              C:\Users\karol\AppData\Roaming\ModrinthApp\meta\versions\26.2-0.19.5\26.2-0.19.5.jar!\net\minecraf\\util\ProblemReporter$Collector.class
 * Java compiler version: 25 (69.0)
 * JD-Core Version:       1.1.3
 */