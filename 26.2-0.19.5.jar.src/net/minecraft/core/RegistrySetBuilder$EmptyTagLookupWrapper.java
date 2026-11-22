/*    */ package net.minecraft.core;
/*    */ 
/*    */ class EmptyTagLookupWrapper<T> extends RegistrySetBuilder.EmptyTagRegistryLookup<T> implements HolderLookup.RegistryLookup.Delegate<T> {
/*    */   private final HolderLookup.RegistryLookup<T> parent;
/*    */   
/*    */   private EmptyTagLookupWrapper(HolderOwner<T> owner, HolderLookup.RegistryLookup<T> parent) {
/* 81 */     super(owner);
/* 82 */     this.parent = parent;
/*    */   }
/*    */   
/*    */   public HolderLookup.RegistryLookup<T> parent() {
/* 87 */     return this.parent;
/*    */   }
/*    */ }


/* Location:              C:\Users\karol\AppData\Roaming\ModrinthApp\meta\versions\26.2-0.19.5\26.2-0.19.5.jar!\net\minecraft\core\RegistrySetBuilder$EmptyTagLookupWrapper.class
 * Java compiler version: 25 (69.0)
 * JD-Core Version:       1.1.3
 */