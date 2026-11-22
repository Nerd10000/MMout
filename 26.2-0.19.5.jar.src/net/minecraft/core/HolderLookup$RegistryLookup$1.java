/*    */ package net.minecraft.core;
/*    */ 
/*    */ import java.util.Optional;
/*    */ import java.util.function.Predicate;
/*    */ import java.util.stream.Stream;
/*    */ import net.minecraft.resources.ResourceKey;
/*    */ 
/*    */ class null implements HolderLookup.RegistryLookup.Delegate<T> {
/*    */   public HolderLookup.RegistryLookup<T> parent() {
/* 50 */     return HolderLookup.RegistryLookup.this;
/*    */   }
/*    */   
/*    */   public Optional<Holder.Reference<T>> get(ResourceKey<T> id) {
/* 55 */     return parent().get(id).filter(holder -> filter.test(holder.value()));
/*    */   }
/*    */   
/*    */   public Stream<Holder.Reference<T>> listElements() {
/* 60 */     return parent().listElements().filter(e -> filter.test(e.value()));
/*    */   }
/*    */ }


/* Location:              C:\Users\karol\AppData\Roaming\ModrinthApp\meta\versions\26.2-0.19.5\26.2-0.19.5.jar!\net\minecraft\core\HolderLookup$RegistryLookup$1.class
 * Java compiler version: 25 (69.0)
 * JD-Core Version:       1.1.3
 */