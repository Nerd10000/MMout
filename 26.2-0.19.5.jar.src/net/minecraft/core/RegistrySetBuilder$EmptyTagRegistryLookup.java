/*    */ package net.minecraft.core;
/*    */ 
/*    */ import java.util.stream.Stream;
/*    */ 
/*    */ abstract class EmptyTagRegistryLookup<T> extends RegistrySetBuilder.EmptyTagLookup<T> implements HolderLookup.RegistryLookup<T> {
/*    */   protected EmptyTagRegistryLookup(HolderOwner<T> owner) {
/* 68 */     super(owner);
/*    */   }
/*    */   
/*    */   public Stream<HolderSet.Named<T>> listTags() {
/* 73 */     throw new UnsupportedOperationException("Tags are not available in datagen");
/*    */   }
/*    */ }


/* Location:              C:\Users\karol\AppData\Roaming\ModrinthApp\meta\versions\26.2-0.19.5\26.2-0.19.5.jar!\net\minecraft\core\RegistrySetBuilder$EmptyTagRegistryLookup.class
 * Java compiler version: 25 (69.0)
 * JD-Core Version:       1.1.3
 */