/*    */ package net.minecraft.core;
/*    */ 
/*    */ import java.util.stream.Stream;
/*    */ import net.minecraft.resources.ResourceKey;
/*    */ import net.minecraft.tags.TagKey;
/*    */ 
/*    */ public interface TypedInstance<T> {
/*    */   Holder<T> typeHolder();
/*    */   
/*    */   default Stream<TagKey<T>> tags() {
/* 13 */     return typeHolder().tags();
/*    */   }
/*    */   
/*    */   default boolean is(TagKey<T> tag) {
/* 17 */     return typeHolder().is(tag);
/*    */   }
/*    */   
/*    */   default boolean is(HolderSet<T> set) {
/* 21 */     return set.contains(typeHolder());
/*    */   }
/*    */   
/*    */   default boolean is(T rawType) {
/* 28 */     return (typeHolder().value() == rawType);
/*    */   }
/*    */   
/*    */   default boolean is(Holder<T> type) {
/* 32 */     return (typeHolder() == type);
/*    */   }
/*    */   
/*    */   default boolean is(ResourceKey<T> type) {
/* 36 */     return typeHolder().is(type);
/*    */   }
/*    */ }


/* Location:              C:\Users\karol\AppData\Roaming\ModrinthApp\meta\versions\26.2-0.19.5\26.2-0.19.5.jar!\net\minecraft\core\TypedInstance.class
 * Java compiler version: 25 (69.0)
 * JD-Core Version:       1.1.3
 */