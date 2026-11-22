/*    */ package net.minecraft.core.component;
/*    */ 
/*    */ import java.util.List;
/*    */ import java.util.function.BiConsumer;
/*    */ import net.minecraft.core.Holder;
/*    */ import net.minecraft.core.Registry;
/*    */ import net.minecraft.resources.ResourceKey;
/*    */ 
/*    */ class null implements DataComponentInitializers.PendingComponents<T> {
/*    */   public ResourceKey<? extends Registry<? extends T>> key() {
/* 80 */     return registryKey;
/*    */   }
/*    */   
/*    */   public void forEach(BiConsumer<Holder.Reference<T>, DataComponentMap> output) {
/* 85 */     entries.forEach(e -> output.accept(e.element, e.components));
/*    */   }
/*    */   
/*    */   public void apply() {
/* 90 */     entries.forEach(DataComponentInitializers.BakedEntry::apply);
/*    */   }
/*    */ }


/* Location:              C:\Users\karol\AppData\Roaming\ModrinthApp\meta\versions\26.2-0.19.5\26.2-0.19.5.jar!\net\minecraft\core\component\DataComponentInitializers$1.class
 * Java compiler version: 25 (69.0)
 * JD-Core Version:       1.1.3
 */