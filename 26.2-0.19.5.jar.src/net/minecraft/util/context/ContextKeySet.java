/*    */ package net.minecraft.util.context;
/*    */ 
/*    */ import com.google.common.base.Joiner;
/*    */ import com.google.common.collect.Sets;
/*    */ import java.util.Collection;
/*    */ import java.util.Set;
/*    */ 
/*    */ public class ContextKeySet {
/*    */   private final Set<ContextKey<?>> required;
/*    */   
/*    */   private final Set<ContextKey<?>> allowed;
/*    */   
/*    */   private ContextKeySet(Set<ContextKey<?>> required, Set<ContextKey<?>> optional) {
/* 14 */     this.required = Set.copyOf(required);
/* 15 */     this.allowed = Set.copyOf((Collection<? extends ContextKey<?>>)Sets.union(required, optional));
/*    */   }
/*    */   
/*    */   public Set<ContextKey<?>> required() {
/* 19 */     return this.required;
/*    */   }
/*    */   
/*    */   public Set<ContextKey<?>> allowed() {
/* 23 */     return this.allowed;
/*    */   }
/*    */   
/*    */   public String toString() {
/* 28 */     return "[" + Joiner.on(", ").join(this.allowed.stream().map(k -> (this.required.contains(k) ? "!" : "") + (this.required.contains(k) ? "!" : "")).iterator()) + "]";
/*    */   }
/*    */   
/*    */   public static class Builder {
/* 32 */     private final Set<ContextKey<?>> required = Sets.newIdentityHashSet();
/*    */     
/* 33 */     private final Set<ContextKey<?>> optional = Sets.newIdentityHashSet();
/*    */     
/*    */     public Builder required(ContextKey<?> param) {
/* 36 */       if (this.optional.contains(param))
/* 37 */         throw new IllegalArgumentException("Parameter " + String.valueOf(param.name()) + " is already optional"); 
/* 39 */       this.required.add(param);
/* 40 */       return this;
/*    */     }
/*    */     
/*    */     public Builder optional(ContextKey<?> param) {
/* 44 */       if (this.required.contains(param))
/* 45 */         throw new IllegalArgumentException("Parameter " + String.valueOf(param.name()) + " is already required"); 
/* 47 */       this.optional.add(param);
/* 48 */       return this;
/*    */     }
/*    */     
/*    */     public ContextKeySet build() {
/* 52 */       return new ContextKeySet(this.required, this.optional);
/*    */     }
/*    */   }
/*    */ }


/* Location:              C:\Users\karol\AppData\Roaming\ModrinthApp\meta\versions\26.2-0.19.5\26.2-0.19.5.jar!\net\minecraf\\util\context\ContextKeySet.class
 * Java compiler version: 25 (69.0)
 * JD-Core Version:       1.1.3
 */