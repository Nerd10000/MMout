/*    */ package net.minecraft.core;
/*    */ 
/*    */ import java.util.function.Supplier;
/*    */ import net.minecraft.resources.ResourceKey;
/*    */ 
/*    */ class LazyHolder<T> extends Holder.Reference<T> {
/*    */   private Supplier<T> supplier;
/*    */   
/*    */   protected LazyHolder(HolderOwner<T> owner, ResourceKey<T> key) {
/* 35 */     super(Holder.Reference.Type.STAND_ALONE, owner, key, null);
/*    */   }
/*    */   
/*    */   protected void bindValue(T value) {
/* 40 */     super.bindValue(value);
/* 41 */     this.supplier = null;
/*    */   }
/*    */   
/*    */   public T value() {
/* 46 */     if (this.supplier != null)
/* 47 */       bindValue(this.supplier.get()); 
/* 49 */     return super.value();
/*    */   }
/*    */ }


/* Location:              C:\Users\karol\AppData\Roaming\ModrinthApp\meta\versions\26.2-0.19.5\26.2-0.19.5.jar!\net\minecraft\core\RegistrySetBuilder$LazyHolder.class
 * Java compiler version: 25 (69.0)
 * JD-Core Version:       1.1.3
 */