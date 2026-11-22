/*    */ package net.minecraft.core.component;
/*    */ 
/*    */ import com.google.common.collect.Sets;
/*    */ import java.util.Set;
/*    */ 
/*    */ class null implements DataComponentMap {
/*    */   public <T> T get(DataComponentType<? extends T> type) {
/* 79 */     T value = overrides.get(type);
/* 80 */     if (value != null)
/* 81 */       return value; 
/* 83 */     return prototype.get(type);
/*    */   }
/*    */   
/*    */   public Set<DataComponentType<?>> keySet() {
/* 88 */     return (Set<DataComponentType<?>>)Sets.union(prototype.keySet(), overrides.keySet());
/*    */   }
/*    */ }


/* Location:              C:\Users\karol\AppData\Roaming\ModrinthApp\meta\versions\26.2-0.19.5\26.2-0.19.5.jar!\net\minecraft\core\component\DataComponentMap$2.class
 * Java compiler version: 25 (69.0)
 * JD-Core Version:       1.1.3
 */