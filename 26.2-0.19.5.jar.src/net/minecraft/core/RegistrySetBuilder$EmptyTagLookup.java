/*    */ package net.minecraft.core;
/*    */ 
/*    */ import java.util.Optional;
/*    */ import net.minecraft.tags.TagKey;
/*    */ 
/*    */ abstract class EmptyTagLookup<T> implements HolderGetter<T> {
/*    */   protected final HolderOwner<T> owner;
/*    */   
/*    */   protected EmptyTagLookup(HolderOwner<T> owner) {
/* 57 */     this.owner = owner;
/*    */   }
/*    */   
/*    */   public Optional<HolderSet.Named<T>> get(TagKey<T> id) {
/* 62 */     return Optional.of(HolderSet.emptyNamed(this.owner, id));
/*    */   }
/*    */ }


/* Location:              C:\Users\karol\AppData\Roaming\ModrinthApp\meta\versions\26.2-0.19.5\26.2-0.19.5.jar!\net\minecraft\core\RegistrySetBuilder$EmptyTagLookup.class
 * Java compiler version: 25 (69.0)
 * JD-Core Version:       1.1.3
 */