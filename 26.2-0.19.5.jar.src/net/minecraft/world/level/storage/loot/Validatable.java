/*    */ package net.minecraft.world.level.storage.loot;
/*    */ 
/*    */ import com.mojang.serialization.DataResult;
/*    */ import java.util.List;
/*    */ import java.util.Optional;
/*    */ import java.util.function.Function;
/*    */ import net.minecraft.core.Holder;
/*    */ import net.minecraft.resources.ResourceKey;
/*    */ import net.minecraft.util.ProblemReporter;
/*    */ import net.minecraft.util.context.ContextKeySet;
/*    */ 
/*    */ public interface Validatable {
/*    */   static void validate(ValidationContext context, String name, Validatable v) {
/* 17 */     v.validate(context.forField(name));
/*    */   }
/*    */   
/*    */   static void validate(ValidationContext context, String name, Optional<? extends Validatable> optional) {
/* 21 */     optional.ifPresent(v -> v.validate(context.forField(name)));
/*    */   }
/*    */   
/*    */   static void validate(ValidationContext context, String name, List<? extends Validatable> list) {
/* 25 */     for (int i = 0; i < list.size(); i++)
/* 26 */       ((Validatable)list.get(i)).validate(context.forIndexedField(name, i)); 
/*    */   }
/*    */   
/*    */   static void validate(ValidationContext context, List<? extends Validatable> list) {
/* 31 */     for (int i = 0; i < list.size(); i++)
/* 32 */       ((Validatable)list.get(i)).validate(context.forChild((ProblemReporter.PathElement)new ProblemReporter.IndexedPathElement(i))); 
/*    */   }
/*    */   
/*    */   static <T extends Validatable> void validateReference(ValidationContext context, ResourceKey<T> id) {
/* 37 */     if (!context.allowsReferences()) {
/* 38 */       context.reportProblem(new ValidationContext.ReferenceNotAllowedProblem(id));
/*    */       return;
/*    */     } 
/* 42 */     if (context.hasVisitedElement(id)) {
/* 43 */       context.reportProblem(new ValidationContext.RecursiveReferenceProblem(id));
/*    */       return;
/*    */     } 
/* 47 */     context.resolver().get(id).ifPresentOrElse(element -> ((Validatable)element.value()).validate(context.enterElement((ProblemReporter.PathElement)new ProblemReporter.ElementReferencePathElement(id), id)), () -> context.reportProblem(new ValidationContext.MissingReferenceProblem(id)));
/*    */   }
/*    */   
/*    */   static <T extends Validatable> Function<T, DataResult<T>> validatorForContext(ContextKeySet params) {
/* 54 */     return v -> {
/*    */         ProblemReporter.Collector problemCollector = new ProblemReporter.Collector();
/*    */         ValidationContext validationContext = new ValidationContext((ProblemReporter)problemCollector, params);
/*    */         v.validate(validationContext);
/*    */         return !problemCollector.isEmpty() ? DataResult.error(()) : DataResult.success(v);
/*    */       };
/*    */   }
/*    */   
/*    */   static <T extends Validatable> Function<List<T>, DataResult<List<T>>> listValidatorForContext(ContextKeySet params) {
/* 66 */     return v -> {
/*    */         ProblemReporter.Collector problemCollector = new ProblemReporter.Collector();
/*    */         ValidationContext validationContext = new ValidationContext((ProblemReporter)problemCollector, params);
/*    */         validate(validationContext, v);
/*    */         return !problemCollector.isEmpty() ? DataResult.error(()) : DataResult.success(v);
/*    */       };
/*    */   }
/*    */   
/*    */   void validate(ValidationContext paramValidationContext);
/*    */ }


/* Location:              C:\Users\karol\AppData\Roaming\ModrinthApp\meta\versions\26.2-0.19.5\26.2-0.19.5.jar!\net\minecraft\world\level\storage\loot\Validatable.class
 * Java compiler version: 25 (69.0)
 * JD-Core Version:       1.1.3
 */