/*    */ package net.minecraft.util.context;
/*    */ 
/*    */ import com.google.common.collect.Sets;
/*    */ import java.util.IdentityHashMap;
/*    */ import java.util.Map;
/*    */ import java.util.NoSuchElementException;
/*    */ 
/*    */ public class Builder {
/* 44 */   private final Map<ContextKey<?>, Object> params = new IdentityHashMap<>();
/*    */   
/*    */   public <T> Builder withParameter(ContextKey<T> param, T value) {
/* 47 */     this.params.put(param, value);
/* 48 */     return this;
/*    */   }
/*    */   
/*    */   public <T> Builder withOptionalParameter(ContextKey<T> param, T value) {
/* 52 */     if (value == null) {
/* 53 */       this.params.remove(param);
/*    */     } else {
/* 55 */       this.params.put(param, value);
/*    */     } 
/* 57 */     return this;
/*    */   }
/*    */   
/*    */   public <T> T getParameter(ContextKey<T> param) {
/* 61 */     T value = (T)this.params.get(param);
/* 62 */     if (value == null)
/* 63 */       throw new NoSuchElementException(param.name().toString()); 
/* 66 */     return value;
/*    */   }
/*    */   
/*    */   public <T> T getOptionalParameter(ContextKey<T> param) {
/* 71 */     return (T)this.params.get(param);
/*    */   }
/*    */   
/*    */   public ContextMap create(ContextKeySet paramSet) {
/* 75 */     Sets.SetView setView1 = Sets.difference(this.params.keySet(), paramSet.allowed());
/* 76 */     if (!setView1.isEmpty())
/* 77 */       throw new IllegalArgumentException("Parameters not allowed in this parameter set: " + String.valueOf(setView1)); 
/* 80 */     Sets.SetView setView2 = Sets.difference(paramSet.required(), this.params.keySet());
/* 81 */     if (!setView2.isEmpty())
/* 82 */       throw new IllegalArgumentException("Missing required parameters: " + String.valueOf(setView2)); 
/* 85 */     return new ContextMap(this.params);
/*    */   }
/*    */ }


/* Location:              C:\Users\karol\AppData\Roaming\ModrinthApp\meta\versions\26.2-0.19.5\26.2-0.19.5.jar!\net\minecraf\\util\context\ContextMap$Builder.class
 * Java compiler version: 25 (69.0)
 * JD-Core Version:       1.1.3
 */