/*    */ package net.minecraft.client.renderer.block.dispatch.multipart;
/*    */ 
/*    */ import java.util.List;
/*    */ import java.util.function.Predicate;
/*    */ import net.minecraft.util.Util;
/*    */ 
/*    */ enum null {
/*    */   public <V> Predicate<V> apply(List<Predicate<V>> terms) {
/* 28 */     return Util.anyOf(terms);
/*    */   }
/*    */ }


/* Location:              C:\Users\karol\AppData\Roaming\ModrinthApp\meta\versions\26.2-0.19.5\26.2-0.19.5.jar!\net\minecraft\client\renderer\block\dispatch\multipart\CombinedCondition$Operation$2.class
 * Java compiler version: 25 (69.0)
 * JD-Core Version:       1.1.3
 */