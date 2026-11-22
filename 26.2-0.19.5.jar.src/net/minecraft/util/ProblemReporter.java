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
/*     */ import net.minecraft.resources.ResourceKey;
/*     */ import org.slf4j.Logger;
/*     */ 
/*     */ public interface ProblemReporter {
/*  19 */   public static final ProblemReporter DISCARDING = new ProblemReporter() {
/*     */       public ProblemReporter forChild(ProblemReporter.PathElement path) {
/*  22 */         return this;
/*     */       }
/*     */       
/*     */       public void report(ProblemReporter.Problem problem) {}
/*     */     };
/*     */   
/*     */   ProblemReporter forChild(PathElement paramPathElement);
/*     */   
/*     */   void report(Problem paramProblem);
/*     */   
/*     */   public static final class RootFieldPathElement extends Record implements PathElement {
/*     */     private final String name;
/*     */     
/*     */     public RootFieldPathElement(String name) {
/*  43 */       this.name = name;
/*     */     }
/*     */     
/*     */     public final String toString() {
/*     */       // Byte code:
/*     */       //   0: aload_0
/*     */       //   1: <illegal opcode> toString : (Lnet/minecraft/util/ProblemReporter$RootFieldPathElement;)Ljava/lang/String;
/*     */       //   6: areturn
/*     */       // Line number table:
/*     */       //   Java source line number -> byte code offset
/*     */       //   #43	-> 0
/*     */       // Local variable table:
/*     */       //   start	length	slot	name	descriptor
/*     */       //   0	7	0	this	Lnet/minecraft/util/ProblemReporter$RootFieldPathElement;
/*     */     }
/*     */     
/*     */     public final int hashCode() {
/*     */       // Byte code:
/*     */       //   0: aload_0
/*     */       //   1: <illegal opcode> hashCode : (Lnet/minecraft/util/ProblemReporter$RootFieldPathElement;)I
/*     */       //   6: ireturn
/*     */       // Line number table:
/*     */       //   Java source line number -> byte code offset
/*     */       //   #43	-> 0
/*     */       // Local variable table:
/*     */       //   start	length	slot	name	descriptor
/*     */       //   0	7	0	this	Lnet/minecraft/util/ProblemReporter$RootFieldPathElement;
/*     */     }
/*     */     
/*     */     public final boolean equals(Object o) {
/*     */       // Byte code:
/*     */       //   0: aload_0
/*     */       //   1: aload_1
/*     */       //   2: <illegal opcode> equals : (Lnet/minecraft/util/ProblemReporter$RootFieldPathElement;Ljava/lang/Object;)Z
/*     */       //   7: ireturn
/*     */       // Line number table:
/*     */       //   Java source line number -> byte code offset
/*     */       //   #43	-> 0
/*     */       // Local variable table:
/*     */       //   start	length	slot	name	descriptor
/*     */       //   0	8	0	this	Lnet/minecraft/util/ProblemReporter$RootFieldPathElement;
/*     */       //   0	8	1	o	Ljava/lang/Object;
/*     */     }
/*     */     
/*     */     public String name() {
/*  43 */       return this.name;
/*     */     }
/*     */     
/*     */     public String get() {
/*  46 */       return this.name;
/*     */     }
/*     */   }
/*     */   
/*     */   public static final class RootElementPathElement extends Record implements PathElement {
/*     */     private final ResourceKey<?> id;
/*     */     
/*     */     public RootElementPathElement(ResourceKey<?> id) {
/*  50 */       this.id = id;
/*     */     }
/*     */     
/*     */     public final String toString() {
/*     */       // Byte code:
/*     */       //   0: aload_0
/*     */       //   1: <illegal opcode> toString : (Lnet/minecraft/util/ProblemReporter$RootElementPathElement;)Ljava/lang/String;
/*     */       //   6: areturn
/*     */       // Line number table:
/*     */       //   Java source line number -> byte code offset
/*     */       //   #50	-> 0
/*     */       // Local variable table:
/*     */       //   start	length	slot	name	descriptor
/*     */       //   0	7	0	this	Lnet/minecraft/util/ProblemReporter$RootElementPathElement;
/*     */     }
/*     */     
/*     */     public final int hashCode() {
/*     */       // Byte code:
/*     */       //   0: aload_0
/*     */       //   1: <illegal opcode> hashCode : (Lnet/minecraft/util/ProblemReporter$RootElementPathElement;)I
/*     */       //   6: ireturn
/*     */       // Line number table:
/*     */       //   Java source line number -> byte code offset
/*     */       //   #50	-> 0
/*     */       // Local variable table:
/*     */       //   start	length	slot	name	descriptor
/*     */       //   0	7	0	this	Lnet/minecraft/util/ProblemReporter$RootElementPathElement;
/*     */     }
/*     */     
/*     */     public final boolean equals(Object o) {
/*     */       // Byte code:
/*     */       //   0: aload_0
/*     */       //   1: aload_1
/*     */       //   2: <illegal opcode> equals : (Lnet/minecraft/util/ProblemReporter$RootElementPathElement;Ljava/lang/Object;)Z
/*     */       //   7: ireturn
/*     */       // Line number table:
/*     */       //   Java source line number -> byte code offset
/*     */       //   #50	-> 0
/*     */       // Local variable table:
/*     */       //   start	length	slot	name	descriptor
/*     */       //   0	8	0	this	Lnet/minecraft/util/ProblemReporter$RootElementPathElement;
/*     */       //   0	8	1	o	Ljava/lang/Object;
/*     */     }
/*     */     
/*     */     public ResourceKey<?> id() {
/*  50 */       return this.id;
/*     */     }
/*     */     
/*     */     public String get() {
/*  53 */       return "{" + String.valueOf(this.id.identifier()) + "@" + String.valueOf(this.id.registry()) + "}";
/*     */     }
/*     */   }
/*     */   
/*     */   public static final class FieldPathElement extends Record implements PathElement {
/*     */     private final String name;
/*     */     
/*     */     public FieldPathElement(String name) {
/*  57 */       this.name = name;
/*     */     }
/*     */     
/*     */     public final String toString() {
/*     */       // Byte code:
/*     */       //   0: aload_0
/*     */       //   1: <illegal opcode> toString : (Lnet/minecraft/util/ProblemReporter$FieldPathElement;)Ljava/lang/String;
/*     */       //   6: areturn
/*     */       // Line number table:
/*     */       //   Java source line number -> byte code offset
/*     */       //   #57	-> 0
/*     */       // Local variable table:
/*     */       //   start	length	slot	name	descriptor
/*     */       //   0	7	0	this	Lnet/minecraft/util/ProblemReporter$FieldPathElement;
/*     */     }
/*     */     
/*     */     public final int hashCode() {
/*     */       // Byte code:
/*     */       //   0: aload_0
/*     */       //   1: <illegal opcode> hashCode : (Lnet/minecraft/util/ProblemReporter$FieldPathElement;)I
/*     */       //   6: ireturn
/*     */       // Line number table:
/*     */       //   Java source line number -> byte code offset
/*     */       //   #57	-> 0
/*     */       // Local variable table:
/*     */       //   start	length	slot	name	descriptor
/*     */       //   0	7	0	this	Lnet/minecraft/util/ProblemReporter$FieldPathElement;
/*     */     }
/*     */     
/*     */     public final boolean equals(Object o) {
/*     */       // Byte code:
/*     */       //   0: aload_0
/*     */       //   1: aload_1
/*     */       //   2: <illegal opcode> equals : (Lnet/minecraft/util/ProblemReporter$FieldPathElement;Ljava/lang/Object;)Z
/*     */       //   7: ireturn
/*     */       // Line number table:
/*     */       //   Java source line number -> byte code offset
/*     */       //   #57	-> 0
/*     */       // Local variable table:
/*     */       //   start	length	slot	name	descriptor
/*     */       //   0	8	0	this	Lnet/minecraft/util/ProblemReporter$FieldPathElement;
/*     */       //   0	8	1	o	Ljava/lang/Object;
/*     */     }
/*     */     
/*     */     public String name() {
/*  57 */       return this.name;
/*     */     }
/*     */     
/*     */     public String get() {
/*  60 */       return "." + this.name;
/*     */     }
/*     */   }
/*     */   
/*     */   public static final class IndexedFieldPathElement extends Record implements PathElement {
/*     */     private final String name;
/*     */     
/*     */     private final int index;
/*     */     
/*     */     public IndexedFieldPathElement(String name, int index) {
/*  64 */       this.name = name;
/*  64 */       this.index = index;
/*     */     }
/*     */     
/*     */     public final String toString() {
/*     */       // Byte code:
/*     */       //   0: aload_0
/*     */       //   1: <illegal opcode> toString : (Lnet/minecraft/util/ProblemReporter$IndexedFieldPathElement;)Ljava/lang/String;
/*     */       //   6: areturn
/*     */       // Line number table:
/*     */       //   Java source line number -> byte code offset
/*     */       //   #64	-> 0
/*     */       // Local variable table:
/*     */       //   start	length	slot	name	descriptor
/*     */       //   0	7	0	this	Lnet/minecraft/util/ProblemReporter$IndexedFieldPathElement;
/*     */     }
/*     */     
/*     */     public final int hashCode() {
/*     */       // Byte code:
/*     */       //   0: aload_0
/*     */       //   1: <illegal opcode> hashCode : (Lnet/minecraft/util/ProblemReporter$IndexedFieldPathElement;)I
/*     */       //   6: ireturn
/*     */       // Line number table:
/*     */       //   Java source line number -> byte code offset
/*     */       //   #64	-> 0
/*     */       // Local variable table:
/*     */       //   start	length	slot	name	descriptor
/*     */       //   0	7	0	this	Lnet/minecraft/util/ProblemReporter$IndexedFieldPathElement;
/*     */     }
/*     */     
/*     */     public final boolean equals(Object o) {
/*     */       // Byte code:
/*     */       //   0: aload_0
/*     */       //   1: aload_1
/*     */       //   2: <illegal opcode> equals : (Lnet/minecraft/util/ProblemReporter$IndexedFieldPathElement;Ljava/lang/Object;)Z
/*     */       //   7: ireturn
/*     */       // Line number table:
/*     */       //   Java source line number -> byte code offset
/*     */       //   #64	-> 0
/*     */       // Local variable table:
/*     */       //   start	length	slot	name	descriptor
/*     */       //   0	8	0	this	Lnet/minecraft/util/ProblemReporter$IndexedFieldPathElement;
/*     */       //   0	8	1	o	Ljava/lang/Object;
/*     */     }
/*     */     
/*     */     public String name() {
/*  64 */       return this.name;
/*     */     }
/*     */     
/*     */     public int index() {
/*  64 */       return this.index;
/*     */     }
/*     */     
/*     */     public String get() {
/*  67 */       return "." + this.name + "[" + this.index + "]";
/*     */     }
/*     */   }
/*     */   
/*     */   public static final class IndexedPathElement extends Record implements PathElement {
/*     */     private final int index;
/*     */     
/*     */     public IndexedPathElement(int index) {
/*  71 */       this.index = index;
/*     */     }
/*     */     
/*     */     public final String toString() {
/*     */       // Byte code:
/*     */       //   0: aload_0
/*     */       //   1: <illegal opcode> toString : (Lnet/minecraft/util/ProblemReporter$IndexedPathElement;)Ljava/lang/String;
/*     */       //   6: areturn
/*     */       // Line number table:
/*     */       //   Java source line number -> byte code offset
/*     */       //   #71	-> 0
/*     */       // Local variable table:
/*     */       //   start	length	slot	name	descriptor
/*     */       //   0	7	0	this	Lnet/minecraft/util/ProblemReporter$IndexedPathElement;
/*     */     }
/*     */     
/*     */     public final int hashCode() {
/*     */       // Byte code:
/*     */       //   0: aload_0
/*     */       //   1: <illegal opcode> hashCode : (Lnet/minecraft/util/ProblemReporter$IndexedPathElement;)I
/*     */       //   6: ireturn
/*     */       // Line number table:
/*     */       //   Java source line number -> byte code offset
/*     */       //   #71	-> 0
/*     */       // Local variable table:
/*     */       //   start	length	slot	name	descriptor
/*     */       //   0	7	0	this	Lnet/minecraft/util/ProblemReporter$IndexedPathElement;
/*     */     }
/*     */     
/*     */     public final boolean equals(Object o) {
/*     */       // Byte code:
/*     */       //   0: aload_0
/*     */       //   1: aload_1
/*     */       //   2: <illegal opcode> equals : (Lnet/minecraft/util/ProblemReporter$IndexedPathElement;Ljava/lang/Object;)Z
/*     */       //   7: ireturn
/*     */       // Line number table:
/*     */       //   Java source line number -> byte code offset
/*     */       //   #71	-> 0
/*     */       // Local variable table:
/*     */       //   start	length	slot	name	descriptor
/*     */       //   0	8	0	this	Lnet/minecraft/util/ProblemReporter$IndexedPathElement;
/*     */       //   0	8	1	o	Ljava/lang/Object;
/*     */     }
/*     */     
/*     */     public int index() {
/*  71 */       return this.index;
/*     */     }
/*     */     
/*     */     public String get() {
/*  74 */       return "[" + this.index + "]";
/*     */     }
/*     */   }
/*     */   
/*     */   public static final class MapEntryPathElement extends Record implements PathElement {
/*     */     private final String name;
/*     */     
/*     */     private final String key;
/*     */     
/*     */     public MapEntryPathElement(String name, String key) {
/*  78 */       this.name = name;
/*  78 */       this.key = key;
/*     */     }
/*     */     
/*     */     public final String toString() {
/*     */       // Byte code:
/*     */       //   0: aload_0
/*     */       //   1: <illegal opcode> toString : (Lnet/minecraft/util/ProblemReporter$MapEntryPathElement;)Ljava/lang/String;
/*     */       //   6: areturn
/*     */       // Line number table:
/*     */       //   Java source line number -> byte code offset
/*     */       //   #78	-> 0
/*     */       // Local variable table:
/*     */       //   start	length	slot	name	descriptor
/*     */       //   0	7	0	this	Lnet/minecraft/util/ProblemReporter$MapEntryPathElement;
/*     */     }
/*     */     
/*     */     public final int hashCode() {
/*     */       // Byte code:
/*     */       //   0: aload_0
/*     */       //   1: <illegal opcode> hashCode : (Lnet/minecraft/util/ProblemReporter$MapEntryPathElement;)I
/*     */       //   6: ireturn
/*     */       // Line number table:
/*     */       //   Java source line number -> byte code offset
/*     */       //   #78	-> 0
/*     */       // Local variable table:
/*     */       //   start	length	slot	name	descriptor
/*     */       //   0	7	0	this	Lnet/minecraft/util/ProblemReporter$MapEntryPathElement;
/*     */     }
/*     */     
/*     */     public final boolean equals(Object o) {
/*     */       // Byte code:
/*     */       //   0: aload_0
/*     */       //   1: aload_1
/*     */       //   2: <illegal opcode> equals : (Lnet/minecraft/util/ProblemReporter$MapEntryPathElement;Ljava/lang/Object;)Z
/*     */       //   7: ireturn
/*     */       // Line number table:
/*     */       //   Java source line number -> byte code offset
/*     */       //   #78	-> 0
/*     */       // Local variable table:
/*     */       //   start	length	slot	name	descriptor
/*     */       //   0	8	0	this	Lnet/minecraft/util/ProblemReporter$MapEntryPathElement;
/*     */       //   0	8	1	o	Ljava/lang/Object;
/*     */     }
/*     */     
/*     */     public String name() {
/*  78 */       return this.name;
/*     */     }
/*     */     
/*     */     public String key() {
/*  78 */       return this.key;
/*     */     }
/*     */     
/*     */     public String get() {
/*  81 */       return "." + this.name + "[" + this.key + "]";
/*     */     }
/*     */   }
/*     */   
/*     */   public static final class ElementReferencePathElement extends Record implements PathElement {
/*     */     private final ResourceKey<?> id;
/*     */     
/*     */     public ElementReferencePathElement(ResourceKey<?> id) {
/*  85 */       this.id = id;
/*     */     }
/*     */     
/*     */     public final String toString() {
/*     */       // Byte code:
/*     */       //   0: aload_0
/*     */       //   1: <illegal opcode> toString : (Lnet/minecraft/util/ProblemReporter$ElementReferencePathElement;)Ljava/lang/String;
/*     */       //   6: areturn
/*     */       // Line number table:
/*     */       //   Java source line number -> byte code offset
/*     */       //   #85	-> 0
/*     */       // Local variable table:
/*     */       //   start	length	slot	name	descriptor
/*     */       //   0	7	0	this	Lnet/minecraft/util/ProblemReporter$ElementReferencePathElement;
/*     */     }
/*     */     
/*     */     public final int hashCode() {
/*     */       // Byte code:
/*     */       //   0: aload_0
/*     */       //   1: <illegal opcode> hashCode : (Lnet/minecraft/util/ProblemReporter$ElementReferencePathElement;)I
/*     */       //   6: ireturn
/*     */       // Line number table:
/*     */       //   Java source line number -> byte code offset
/*     */       //   #85	-> 0
/*     */       // Local variable table:
/*     */       //   start	length	slot	name	descriptor
/*     */       //   0	7	0	this	Lnet/minecraft/util/ProblemReporter$ElementReferencePathElement;
/*     */     }
/*     */     
/*     */     public final boolean equals(Object o) {
/*     */       // Byte code:
/*     */       //   0: aload_0
/*     */       //   1: aload_1
/*     */       //   2: <illegal opcode> equals : (Lnet/minecraft/util/ProblemReporter$ElementReferencePathElement;Ljava/lang/Object;)Z
/*     */       //   7: ireturn
/*     */       // Line number table:
/*     */       //   Java source line number -> byte code offset
/*     */       //   #85	-> 0
/*     */       // Local variable table:
/*     */       //   start	length	slot	name	descriptor
/*     */       //   0	8	0	this	Lnet/minecraft/util/ProblemReporter$ElementReferencePathElement;
/*     */       //   0	8	1	o	Ljava/lang/Object;
/*     */     }
/*     */     
/*     */     public ResourceKey<?> id() {
/*  85 */       return this.id;
/*     */     }
/*     */     
/*     */     public String get() {
/*  88 */       return "->{" + String.valueOf(this.id.identifier()) + "@" + String.valueOf(this.id.registry()) + "}";
/*     */     }
/*     */   }
/*     */   
/*     */   public static class Collector implements ProblemReporter {
/*     */     private static final class Entry extends Record {
/*     */       private final ProblemReporter.Collector source;
/*     */       
/*     */       private final ProblemReporter.Problem problem;
/*     */       
/*     */       private Entry(ProblemReporter.Collector source, ProblemReporter.Problem problem) {
/*  93 */         this.source = source;
/*  93 */         this.problem = problem;
/*     */       }
/*     */       
/*     */       public final String toString() {
/*     */         // Byte code:
/*     */         //   0: aload_0
/*     */         //   1: <illegal opcode> toString : (Lnet/minecraft/util/ProblemReporter$Collector$Entry;)Ljava/lang/String;
/*     */         //   6: areturn
/*     */         // Line number table:
/*     */         //   Java source line number -> byte code offset
/*     */         //   #93	-> 0
/*     */         // Local variable table:
/*     */         //   start	length	slot	name	descriptor
/*     */         //   0	7	0	this	Lnet/minecraft/util/ProblemReporter$Collector$Entry;
/*     */       }
/*     */       
/*     */       public final int hashCode() {
/*     */         // Byte code:
/*     */         //   0: aload_0
/*     */         //   1: <illegal opcode> hashCode : (Lnet/minecraft/util/ProblemReporter$Collector$Entry;)I
/*     */         //   6: ireturn
/*     */         // Line number table:
/*     */         //   Java source line number -> byte code offset
/*     */         //   #93	-> 0
/*     */         // Local variable table:
/*     */         //   start	length	slot	name	descriptor
/*     */         //   0	7	0	this	Lnet/minecraft/util/ProblemReporter$Collector$Entry;
/*     */       }
/*     */       
/*     */       public final boolean equals(Object o) {
/*     */         // Byte code:
/*     */         //   0: aload_0
/*     */         //   1: aload_1
/*     */         //   2: <illegal opcode> equals : (Lnet/minecraft/util/ProblemReporter$Collector$Entry;Ljava/lang/Object;)Z
/*     */         //   7: ireturn
/*     */         // Line number table:
/*     */         //   Java source line number -> byte code offset
/*     */         //   #93	-> 0
/*     */         // Local variable table:
/*     */         //   start	length	slot	name	descriptor
/*     */         //   0	8	0	this	Lnet/minecraft/util/ProblemReporter$Collector$Entry;
/*     */         //   0	8	1	o	Ljava/lang/Object;
/*     */       }
/*     */       
/*     */       public ProblemReporter.Collector source() {
/*  93 */         return this.source;
/*     */       }
/*     */       
/*     */       public ProblemReporter.Problem problem() {
/*  93 */         return this.problem;
/*     */       }
/*     */     }
/*     */     
/*     */     public static final ProblemReporter.PathElement EMPTY_ROOT = () -> "";
/*     */     
/*     */     private final Collector parent;
/*     */     
/*     */     private final ProblemReporter.PathElement element;
/*     */     
/*     */     private final Set<Entry> problems;
/*     */     
/*     */     public Collector() {
/* 104 */       this(EMPTY_ROOT);
/*     */     }
/*     */     
/*     */     public Collector(ProblemReporter.PathElement root) {
/* 108 */       this.parent = null;
/* 109 */       this.problems = new LinkedHashSet<>();
/* 110 */       this.element = root;
/*     */     }
/*     */     
/*     */     private Collector(Collector parent, ProblemReporter.PathElement path) {
/* 114 */       this.problems = parent.problems;
/* 115 */       this.parent = parent;
/* 116 */       this.element = path;
/*     */     }
/*     */     
/*     */     public ProblemReporter forChild(ProblemReporter.PathElement path) {
/* 121 */       return new Collector(this, path);
/*     */     }
/*     */     
/*     */     public void report(ProblemReporter.Problem problem) {
/* 126 */       this.problems.add(new Entry(this, problem));
/*     */     }
/*     */     
/*     */     public boolean isEmpty() {
/* 130 */       return this.problems.isEmpty();
/*     */     }
/*     */     
/*     */     public void forEach(BiConsumer<String, ProblemReporter.Problem> output) {
/* 134 */       List<ProblemReporter.PathElement> pathElements = new ArrayList<>();
/* 135 */       StringBuilder pathString = new StringBuilder();
/* 137 */       for (Entry entry : this.problems) {
/* 138 */         Collector current = entry.source;
/* 139 */         while (current != null) {
/* 140 */           pathElements.add(current.element);
/* 141 */           current = current.parent;
/*     */         } 
/* 144 */         for (int i = pathElements.size() - 1; i >= 0; i--)
/* 145 */           pathString.append(((ProblemReporter.PathElement)pathElements.get(i)).get()); 
/* 147 */         output.accept(pathString.toString(), entry.problem());
/* 149 */         pathString.setLength(0);
/* 150 */         pathElements.clear();
/*     */       } 
/*     */     }
/*     */     
/*     */     public String getReport() {
/* 155 */       HashMultimap hashMultimap = HashMultimap.create();
/* 156 */       Objects.requireNonNull(hashMultimap);
/* 156 */       forEach(hashMultimap::put);
/* 158 */       return hashMultimap.asMap().entrySet().stream()
/* 159 */         .map(entry -> " at " + (String)entry.getKey() + ": " + (String)((Collection)entry.getValue()).stream().map(ProblemReporter.Problem::description).collect(Collectors.joining("; ")))
/* 160 */         .collect(Collectors.joining("\n"));
/*     */     }
/*     */     
/*     */     public String getTreeReport() {
/* 164 */       List<ProblemReporter.PathElement> pathElements = new ArrayList<>();
/* 166 */       ProblemTreeNode root = new ProblemTreeNode(this.element);
/* 168 */       for (Entry entry : this.problems) {
/* 169 */         Collector current = entry.source;
/* 170 */         while (current != this) {
/* 171 */           pathElements.add(current.element);
/* 172 */           current = current.parent;
/*     */         } 
/* 175 */         ProblemTreeNode node = root;
/* 176 */         for (int i = pathElements.size() - 1; i >= 0; i--)
/* 177 */           node = node.child(pathElements.get(i)); 
/* 179 */         pathElements.clear();
/* 181 */         node.problems.add(entry.problem);
/*     */       } 
/* 184 */       return String.join("\n", (Iterable)root.getLines());
/*     */     }
/*     */     
/*     */     private static final class ProblemTreeNode extends Record {
/*     */       private final ProblemReporter.PathElement element;
/*     */       
/*     */       private final List<ProblemReporter.Problem> problems;
/*     */       
/*     */       private final Map<ProblemReporter.PathElement, ProblemTreeNode> children;
/*     */       
/*     */       private ProblemTreeNode(ProblemReporter.PathElement element, List<ProblemReporter.Problem> problems, Map<ProblemReporter.PathElement, ProblemTreeNode> children) {
/* 187 */         this.element = element;
/* 187 */         this.problems = problems;
/* 187 */         this.children = children;
/*     */       }
/*     */       
/*     */       public final String toString() {
/*     */         // Byte code:
/*     */         //   0: aload_0
/*     */         //   1: <illegal opcode> toString : (Lnet/minecraft/util/ProblemReporter$Collector$ProblemTreeNode;)Ljava/lang/String;
/*     */         //   6: areturn
/*     */         // Line number table:
/*     */         //   Java source line number -> byte code offset
/*     */         //   #187	-> 0
/*     */         // Local variable table:
/*     */         //   start	length	slot	name	descriptor
/*     */         //   0	7	0	this	Lnet/minecraft/util/ProblemReporter$Collector$ProblemTreeNode;
/*     */       }
/*     */       
/*     */       public final int hashCode() {
/*     */         // Byte code:
/*     */         //   0: aload_0
/*     */         //   1: <illegal opcode> hashCode : (Lnet/minecraft/util/ProblemReporter$Collector$ProblemTreeNode;)I
/*     */         //   6: ireturn
/*     */         // Line number table:
/*     */         //   Java source line number -> byte code offset
/*     */         //   #187	-> 0
/*     */         // Local variable table:
/*     */         //   start	length	slot	name	descriptor
/*     */         //   0	7	0	this	Lnet/minecraft/util/ProblemReporter$Collector$ProblemTreeNode;
/*     */       }
/*     */       
/*     */       public final boolean equals(Object o) {
/*     */         // Byte code:
/*     */         //   0: aload_0
/*     */         //   1: aload_1
/*     */         //   2: <illegal opcode> equals : (Lnet/minecraft/util/ProblemReporter$Collector$ProblemTreeNode;Ljava/lang/Object;)Z
/*     */         //   7: ireturn
/*     */         // Line number table:
/*     */         //   Java source line number -> byte code offset
/*     */         //   #187	-> 0
/*     */         // Local variable table:
/*     */         //   start	length	slot	name	descriptor
/*     */         //   0	8	0	this	Lnet/minecraft/util/ProblemReporter$Collector$ProblemTreeNode;
/*     */         //   0	8	1	o	Ljava/lang/Object;
/*     */       }
/*     */       
/*     */       public ProblemReporter.PathElement element() {
/* 187 */         return this.element;
/*     */       }
/*     */       
/*     */       public List<ProblemReporter.Problem> problems() {
/* 187 */         return this.problems;
/*     */       }
/*     */       
/*     */       public Map<ProblemReporter.PathElement, ProblemTreeNode> children() {
/* 187 */         return this.children;
/*     */       }
/*     */       
/*     */       public ProblemTreeNode(ProblemReporter.PathElement pathElement) {
/* 193 */         this(pathElement, new ArrayList<>(), new LinkedHashMap<>());
/*     */       }
/*     */       
/*     */       public ProblemTreeNode child(ProblemReporter.PathElement id) {
/* 201 */         return this.children.computeIfAbsent(id, ProblemTreeNode::new);
/*     */       }
/*     */       
/*     */       public List<String> getLines() {
/* 206 */         int problemCount = this.problems.size();
/* 207 */         int childrenCount = this.children.size();
/* 209 */         if (problemCount == 0 && childrenCount == 0)
/* 210 */           return List.of(); 
/* 212 */         if (problemCount == 0 && childrenCount == 1) {
/* 213 */           List<String> list = new ArrayList<>();
/* 214 */           this.children.forEach((element, child) -> lines.addAll(child.getLines()));
/* 215 */           list.set(0, this.element.get() + this.element.get());
/* 216 */           return list;
/*     */         } 
/* 218 */         if (problemCount == 1 && childrenCount == 0)
/* 219 */           return List.of(this.element.get() + ": " + this.element.get()); 
/* 222 */         List<String> lines = new ArrayList<>();
/* 223 */         this.children.forEach((element, child) -> lines.addAll(child.getLines()));
/* 224 */         lines.replaceAll(s -> "  " + s);
/* 226 */         for (ProblemReporter.Problem problem : this.problems)
/* 227 */           lines.add("  " + problem.description()); 
/* 230 */         lines.addFirst(this.element.get() + ":");
/* 231 */         return lines;
/*     */       }
/*     */     }
/*     */   }
/*     */   
/*     */   private static final class Entry extends Record {
/*     */     private final ProblemReporter.Collector source;
/*     */     
/*     */     private final ProblemReporter.Problem problem;
/*     */     
/*     */     private Entry(ProblemReporter.Collector source, ProblemReporter.Problem problem) {
/*     */       this.source = source;
/*     */       this.problem = problem;
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
/*     */       return this.source;
/*     */     }
/*     */     
/*     */     public ProblemReporter.Problem problem() {
/*     */       return this.problem;
/*     */     }
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
/*     */       this.element = element;
/*     */       this.problems = problems;
/*     */       this.children = children;
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
/*     */       return this.element;
/*     */     }
/*     */     
/*     */     public List<ProblemReporter.Problem> problems() {
/*     */       return this.problems;
/*     */     }
/*     */     
/*     */     public Map<ProblemReporter.PathElement, ProblemTreeNode> children() {
/*     */       return this.children;
/*     */     }
/*     */     
/*     */     public ProblemTreeNode(ProblemReporter.PathElement pathElement) {
/*     */       this(pathElement, new ArrayList<>(), new LinkedHashMap<>());
/*     */     }
/*     */     
/*     */     public ProblemTreeNode child(ProblemReporter.PathElement id) {
/*     */       return this.children.computeIfAbsent(id, ProblemTreeNode::new);
/*     */     }
/*     */     
/*     */     public List<String> getLines() {
/*     */       int problemCount = this.problems.size();
/*     */       int childrenCount = this.children.size();
/*     */       if (problemCount == 0 && childrenCount == 0)
/*     */         return List.of(); 
/*     */       if (problemCount == 0 && childrenCount == 1) {
/*     */         List<String> list = new ArrayList<>();
/*     */         this.children.forEach((element, child) -> lines.addAll(child.getLines()));
/*     */         list.set(0, this.element.get() + this.element.get());
/*     */         return list;
/*     */       } 
/*     */       if (problemCount == 1 && childrenCount == 0)
/*     */         return List.of(this.element.get() + ": " + this.element.get()); 
/*     */       List<String> lines = new ArrayList<>();
/*     */       this.children.forEach((element, child) -> lines.addAll(child.getLines()));
/*     */       lines.replaceAll(s -> "  " + s);
/*     */       for (ProblemReporter.Problem problem : this.problems)
/*     */         lines.add("  " + problem.description()); 
/*     */       lines.addFirst(this.element.get() + ":");
/* 231 */       return lines;
/*     */     }
/*     */   }
/*     */   
/*     */   public static class ScopedCollector extends Collector implements AutoCloseable {
/*     */     private final Logger logger;
/*     */     
/*     */     public ScopedCollector(Logger logger) {
/* 240 */       this.logger = logger;
/*     */     }
/*     */     
/*     */     public ScopedCollector(ProblemReporter.PathElement root, Logger logger) {
/* 244 */       super(root);
/* 245 */       this.logger = logger;
/*     */     }
/*     */     
/*     */     public void close() {
/* 250 */       if (!isEmpty())
/* 252 */         this.logger.warn("[{}] Serialization errors:\n{}", this.logger.getName(), getTreeReport()); 
/*     */     }
/*     */   }
/*     */   
/*     */   @FunctionalInterface
/*     */   public static interface PathElement {
/*     */     String get();
/*     */   }
/*     */   
/*     */   public static interface Problem {
/*     */     String description();
/*     */   }
/*     */ }


/* Location:              C:\Users\karol\AppData\Roaming\ModrinthApp\meta\versions\26.2-0.19.5\26.2-0.19.5.jar!\net\minecraf\\util\ProblemReporter.class
 * Java compiler version: 25 (69.0)
 * JD-Core Version:       1.1.3
 */