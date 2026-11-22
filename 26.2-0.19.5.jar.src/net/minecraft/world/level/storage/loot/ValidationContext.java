/*     */ package net.minecraft.world.level.storage.loot;
/*     */ 
/*     */ import com.google.common.collect.ImmutableSet;
/*     */ import com.google.common.collect.Sets;
/*     */ import java.util.Optional;
/*     */ import java.util.Set;
/*     */ import net.minecraft.core.HolderGetter;
/*     */ import net.minecraft.resources.ResourceKey;
/*     */ import net.minecraft.util.ProblemReporter;
/*     */ import net.minecraft.util.context.ContextKey;
/*     */ import net.minecraft.util.context.ContextKeySet;
/*     */ 
/*     */ public class ValidationContext {
/*     */   private final ProblemReporter reporter;
/*     */   
/*     */   private final ContextKeySet contextKeySet;
/*     */   
/*     */   private final Optional<HolderGetter.Provider> resolver;
/*     */   
/*     */   private final Set<ResourceKey<?>> visitedElements;
/*     */   
/*     */   public ValidationContext(ProblemReporter reporter, ContextKeySet contextKeySet, HolderGetter.Provider resolver) {
/*  22 */     this(reporter, contextKeySet, Optional.of(resolver), Set.of());
/*     */   }
/*     */   
/*     */   public ValidationContext(ProblemReporter reporter, ContextKeySet contextKeySet) {
/*  26 */     this(reporter, contextKeySet, Optional.empty(), Set.of());
/*     */   }
/*     */   
/*     */   private ValidationContext(ProblemReporter reporter, ContextKeySet contextKeySet, Optional<HolderGetter.Provider> resolver, Set<ResourceKey<?>> visitedElements) {
/*  30 */     this.reporter = reporter;
/*  31 */     this.contextKeySet = contextKeySet;
/*  32 */     this.resolver = resolver;
/*  33 */     this.visitedElements = visitedElements;
/*     */   }
/*     */   
/*     */   public ValidationContext forChild(ProblemReporter.PathElement subContext) {
/*  37 */     return new ValidationContext(this.reporter.forChild(subContext), this.contextKeySet, this.resolver, this.visitedElements);
/*     */   }
/*     */   
/*     */   public ValidationContext forField(String name) {
/*  41 */     return forChild((ProblemReporter.PathElement)new ProblemReporter.FieldPathElement(name));
/*     */   }
/*     */   
/*     */   public ValidationContext forIndexedField(String name, int index) {
/*  45 */     return forChild((ProblemReporter.PathElement)new ProblemReporter.IndexedFieldPathElement(name, index));
/*     */   }
/*     */   
/*     */   public ValidationContext forMapField(String name, String key) {
/*  49 */     return forChild((ProblemReporter.PathElement)new ProblemReporter.MapEntryPathElement(name, key));
/*     */   }
/*     */   
/*     */   public ValidationContext enterElement(ProblemReporter.PathElement subContext, ResourceKey<?> element) {
/*  53 */     ImmutableSet immutableSet = ImmutableSet.builder().addAll(this.visitedElements).add(element).build();
/*  54 */     return new ValidationContext(this.reporter.forChild(subContext), this.contextKeySet, this.resolver, (Set<ResourceKey<?>>)immutableSet);
/*     */   }
/*     */   
/*     */   public boolean hasVisitedElement(ResourceKey<?> element) {
/*  58 */     return this.visitedElements.contains(element);
/*     */   }
/*     */   
/*     */   public void reportProblem(ProblemReporter.Problem description) {
/*  62 */     this.reporter.report(description);
/*     */   }
/*     */   
/*     */   public void validateContextUsage(LootContextUser lootContextUser) {
/*  66 */     Set<ContextKey<?>> allReferenced = lootContextUser.getReferencedContextParams();
/*  67 */     Sets.SetView setView = Sets.difference(allReferenced, this.contextKeySet.allowed());
/*  68 */     if (!setView.isEmpty())
/*  69 */       this.reporter.report(new ParametersNotProvidedProblem((Set<ContextKey<?>>)setView)); 
/*     */   }
/*     */   
/*     */   public HolderGetter.Provider resolver() {
/*  74 */     return this.resolver.<Throwable>orElseThrow(() -> new UnsupportedOperationException("References not allowed"));
/*     */   }
/*     */   
/*     */   public boolean allowsReferences() {
/*  78 */     return this.resolver.isPresent();
/*     */   }
/*     */   
/*     */   public ProblemReporter reporter() {
/*  82 */     return this.reporter;
/*     */   }
/*     */   
/*     */   public static final class ParametersNotProvidedProblem extends Record implements ProblemReporter.Problem {
/*     */     private final Set<ContextKey<?>> notProvided;
/*     */     
/*     */     public ParametersNotProvidedProblem(Set<ContextKey<?>> notProvided) {
/*  85 */       this.notProvided = notProvided;
/*     */     }
/*     */     
/*     */     public final String toString() {
/*     */       // Byte code:
/*     */       //   0: aload_0
/*     */       //   1: <illegal opcode> toString : (Lnet/minecraft/world/level/storage/loot/ValidationContext$ParametersNotProvidedProblem;)Ljava/lang/String;
/*     */       //   6: areturn
/*     */       // Line number table:
/*     */       //   Java source line number -> byte code offset
/*     */       //   #85	-> 0
/*     */       // Local variable table:
/*     */       //   start	length	slot	name	descriptor
/*     */       //   0	7	0	this	Lnet/minecraft/world/level/storage/loot/ValidationContext$ParametersNotProvidedProblem;
/*     */     }
/*     */     
/*     */     public final int hashCode() {
/*     */       // Byte code:
/*     */       //   0: aload_0
/*     */       //   1: <illegal opcode> hashCode : (Lnet/minecraft/world/level/storage/loot/ValidationContext$ParametersNotProvidedProblem;)I
/*     */       //   6: ireturn
/*     */       // Line number table:
/*     */       //   Java source line number -> byte code offset
/*     */       //   #85	-> 0
/*     */       // Local variable table:
/*     */       //   start	length	slot	name	descriptor
/*     */       //   0	7	0	this	Lnet/minecraft/world/level/storage/loot/ValidationContext$ParametersNotProvidedProblem;
/*     */     }
/*     */     
/*     */     public final boolean equals(Object o) {
/*     */       // Byte code:
/*     */       //   0: aload_0
/*     */       //   1: aload_1
/*     */       //   2: <illegal opcode> equals : (Lnet/minecraft/world/level/storage/loot/ValidationContext$ParametersNotProvidedProblem;Ljava/lang/Object;)Z
/*     */       //   7: ireturn
/*     */       // Line number table:
/*     */       //   Java source line number -> byte code offset
/*     */       //   #85	-> 0
/*     */       // Local variable table:
/*     */       //   start	length	slot	name	descriptor
/*     */       //   0	8	0	this	Lnet/minecraft/world/level/storage/loot/ValidationContext$ParametersNotProvidedProblem;
/*     */       //   0	8	1	o	Ljava/lang/Object;
/*     */     }
/*     */     
/*     */     public Set<ContextKey<?>> notProvided() {
/*  85 */       return this.notProvided;
/*     */     }
/*     */     
/*     */     public String description() {
/*  88 */       return "Parameters " + String.valueOf(this.notProvided) + " are not provided in this context";
/*     */     }
/*     */   }
/*     */   
/*     */   public static final class ReferenceNotAllowedProblem extends Record implements ProblemReporter.Problem {
/*     */     private final ResourceKey<?> referenced;
/*     */     
/*     */     public ReferenceNotAllowedProblem(ResourceKey<?> referenced) {
/*  92 */       this.referenced = referenced;
/*     */     }
/*     */     
/*     */     public final String toString() {
/*     */       // Byte code:
/*     */       //   0: aload_0
/*     */       //   1: <illegal opcode> toString : (Lnet/minecraft/world/level/storage/loot/ValidationContext$ReferenceNotAllowedProblem;)Ljava/lang/String;
/*     */       //   6: areturn
/*     */       // Line number table:
/*     */       //   Java source line number -> byte code offset
/*     */       //   #92	-> 0
/*     */       // Local variable table:
/*     */       //   start	length	slot	name	descriptor
/*     */       //   0	7	0	this	Lnet/minecraft/world/level/storage/loot/ValidationContext$ReferenceNotAllowedProblem;
/*     */     }
/*     */     
/*     */     public final int hashCode() {
/*     */       // Byte code:
/*     */       //   0: aload_0
/*     */       //   1: <illegal opcode> hashCode : (Lnet/minecraft/world/level/storage/loot/ValidationContext$ReferenceNotAllowedProblem;)I
/*     */       //   6: ireturn
/*     */       // Line number table:
/*     */       //   Java source line number -> byte code offset
/*     */       //   #92	-> 0
/*     */       // Local variable table:
/*     */       //   start	length	slot	name	descriptor
/*     */       //   0	7	0	this	Lnet/minecraft/world/level/storage/loot/ValidationContext$ReferenceNotAllowedProblem;
/*     */     }
/*     */     
/*     */     public final boolean equals(Object o) {
/*     */       // Byte code:
/*     */       //   0: aload_0
/*     */       //   1: aload_1
/*     */       //   2: <illegal opcode> equals : (Lnet/minecraft/world/level/storage/loot/ValidationContext$ReferenceNotAllowedProblem;Ljava/lang/Object;)Z
/*     */       //   7: ireturn
/*     */       // Line number table:
/*     */       //   Java source line number -> byte code offset
/*     */       //   #92	-> 0
/*     */       // Local variable table:
/*     */       //   start	length	slot	name	descriptor
/*     */       //   0	8	0	this	Lnet/minecraft/world/level/storage/loot/ValidationContext$ReferenceNotAllowedProblem;
/*     */       //   0	8	1	o	Ljava/lang/Object;
/*     */     }
/*     */     
/*     */     public ResourceKey<?> referenced() {
/*  92 */       return this.referenced;
/*     */     }
/*     */     
/*     */     public String description() {
/*  95 */       return "Reference to " + String.valueOf(this.referenced.identifier()) + " of type " + String.valueOf(this.referenced.registry()) + " was used, but references are not allowed";
/*     */     }
/*     */   }
/*     */   
/*     */   public static final class RecursiveReferenceProblem extends Record implements ProblemReporter.Problem {
/*     */     private final ResourceKey<?> referenced;
/*     */     
/*     */     public RecursiveReferenceProblem(ResourceKey<?> referenced) {
/*  99 */       this.referenced = referenced;
/*     */     }
/*     */     
/*     */     public final String toString() {
/*     */       // Byte code:
/*     */       //   0: aload_0
/*     */       //   1: <illegal opcode> toString : (Lnet/minecraft/world/level/storage/loot/ValidationContext$RecursiveReferenceProblem;)Ljava/lang/String;
/*     */       //   6: areturn
/*     */       // Line number table:
/*     */       //   Java source line number -> byte code offset
/*     */       //   #99	-> 0
/*     */       // Local variable table:
/*     */       //   start	length	slot	name	descriptor
/*     */       //   0	7	0	this	Lnet/minecraft/world/level/storage/loot/ValidationContext$RecursiveReferenceProblem;
/*     */     }
/*     */     
/*     */     public final int hashCode() {
/*     */       // Byte code:
/*     */       //   0: aload_0
/*     */       //   1: <illegal opcode> hashCode : (Lnet/minecraft/world/level/storage/loot/ValidationContext$RecursiveReferenceProblem;)I
/*     */       //   6: ireturn
/*     */       // Line number table:
/*     */       //   Java source line number -> byte code offset
/*     */       //   #99	-> 0
/*     */       // Local variable table:
/*     */       //   start	length	slot	name	descriptor
/*     */       //   0	7	0	this	Lnet/minecraft/world/level/storage/loot/ValidationContext$RecursiveReferenceProblem;
/*     */     }
/*     */     
/*     */     public final boolean equals(Object o) {
/*     */       // Byte code:
/*     */       //   0: aload_0
/*     */       //   1: aload_1
/*     */       //   2: <illegal opcode> equals : (Lnet/minecraft/world/level/storage/loot/ValidationContext$RecursiveReferenceProblem;Ljava/lang/Object;)Z
/*     */       //   7: ireturn
/*     */       // Line number table:
/*     */       //   Java source line number -> byte code offset
/*     */       //   #99	-> 0
/*     */       // Local variable table:
/*     */       //   start	length	slot	name	descriptor
/*     */       //   0	8	0	this	Lnet/minecraft/world/level/storage/loot/ValidationContext$RecursiveReferenceProblem;
/*     */       //   0	8	1	o	Ljava/lang/Object;
/*     */     }
/*     */     
/*     */     public ResourceKey<?> referenced() {
/*  99 */       return this.referenced;
/*     */     }
/*     */     
/*     */     public String description() {
/* 102 */       return String.valueOf(this.referenced.identifier()) + " of type " + String.valueOf(this.referenced.identifier()) + " is recursively called";
/*     */     }
/*     */   }
/*     */   
/*     */   public static final class MissingReferenceProblem extends Record implements ProblemReporter.Problem {
/*     */     private final ResourceKey<?> referenced;
/*     */     
/*     */     public MissingReferenceProblem(ResourceKey<?> referenced) {
/* 106 */       this.referenced = referenced;
/*     */     }
/*     */     
/*     */     public final String toString() {
/*     */       // Byte code:
/*     */       //   0: aload_0
/*     */       //   1: <illegal opcode> toString : (Lnet/minecraft/world/level/storage/loot/ValidationContext$MissingReferenceProblem;)Ljava/lang/String;
/*     */       //   6: areturn
/*     */       // Line number table:
/*     */       //   Java source line number -> byte code offset
/*     */       //   #106	-> 0
/*     */       // Local variable table:
/*     */       //   start	length	slot	name	descriptor
/*     */       //   0	7	0	this	Lnet/minecraft/world/level/storage/loot/ValidationContext$MissingReferenceProblem;
/*     */     }
/*     */     
/*     */     public final int hashCode() {
/*     */       // Byte code:
/*     */       //   0: aload_0
/*     */       //   1: <illegal opcode> hashCode : (Lnet/minecraft/world/level/storage/loot/ValidationContext$MissingReferenceProblem;)I
/*     */       //   6: ireturn
/*     */       // Line number table:
/*     */       //   Java source line number -> byte code offset
/*     */       //   #106	-> 0
/*     */       // Local variable table:
/*     */       //   start	length	slot	name	descriptor
/*     */       //   0	7	0	this	Lnet/minecraft/world/level/storage/loot/ValidationContext$MissingReferenceProblem;
/*     */     }
/*     */     
/*     */     public final boolean equals(Object o) {
/*     */       // Byte code:
/*     */       //   0: aload_0
/*     */       //   1: aload_1
/*     */       //   2: <illegal opcode> equals : (Lnet/minecraft/world/level/storage/loot/ValidationContext$MissingReferenceProblem;Ljava/lang/Object;)Z
/*     */       //   7: ireturn
/*     */       // Line number table:
/*     */       //   Java source line number -> byte code offset
/*     */       //   #106	-> 0
/*     */       // Local variable table:
/*     */       //   start	length	slot	name	descriptor
/*     */       //   0	8	0	this	Lnet/minecraft/world/level/storage/loot/ValidationContext$MissingReferenceProblem;
/*     */       //   0	8	1	o	Ljava/lang/Object;
/*     */     }
/*     */     
/*     */     public ResourceKey<?> referenced() {
/* 106 */       return this.referenced;
/*     */     }
/*     */     
/*     */     public String description() {
/* 109 */       return "Missing element " + String.valueOf(this.referenced.identifier()) + " of type " + String.valueOf(this.referenced.registry());
/*     */     }
/*     */   }
/*     */ }


/* Location:              C:\Users\karol\AppData\Roaming\ModrinthApp\meta\versions\26.2-0.19.5\26.2-0.19.5.jar!\net\minecraft\world\level\storage\loot\ValidationContext.class
 * Java compiler version: 25 (69.0)
 * JD-Core Version:       1.1.3
 */