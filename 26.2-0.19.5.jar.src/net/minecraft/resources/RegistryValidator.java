/*    */ package net.minecraft.resources;
/*    */ 
/*    */ import java.util.Map;
/*    */ import net.minecraft.core.Registry;
/*    */ 
/*    */ @FunctionalInterface
/*    */ public interface RegistryValidator<T> {
/*    */   public static final RegistryValidator<?> NONE = (paramRegistry, paramMap) -> {
/*    */     
/*    */     };
/*    */   
/*    */   public static final RegistryValidator<?> NON_EMPTY;
/*    */   
/*    */   static {
/* 10 */     NON_EMPTY = ((registry, loadingErrors) -> {
/*    */         if (registry.size() == 0)
/*    */           loadingErrors.put(registry.key(), new IllegalStateException("Registry must be non-empty: " + String.valueOf(registry.key().identifier()))); 
/*    */       });
/*    */   }
/*    */   
/*    */   static <T> RegistryValidator<T> none() {
/* 18 */     return (RegistryValidator)NONE;
/*    */   }
/*    */   
/*    */   static <T> RegistryValidator<T> nonEmpty() {
/* 23 */     return (RegistryValidator)NON_EMPTY;
/*    */   }
/*    */   
/*    */   void validate(Registry<T> paramRegistry, Map<ResourceKey<?>, Exception> paramMap);
/*    */ }


/* Location:              C:\Users\karol\AppData\Roaming\ModrinthApp\meta\versions\26.2-0.19.5\26.2-0.19.5.jar!\net\minecraft\resources\RegistryValidator.class
 * Java compiler version: 25 (69.0)
 * JD-Core Version:       1.1.3
 */