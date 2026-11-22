/*   */ package net.minecraft.core;
/*   */ 
/*   */ public interface HolderOwner<T> {
/*   */   default boolean canSerializeIn(HolderOwner<T> context) {
/* 5 */     return (context == this);
/*   */   }
/*   */ }


/* Location:              C:\Users\karol\AppData\Roaming\ModrinthApp\meta\versions\26.2-0.19.5\26.2-0.19.5.jar!\net\minecraft\core\HolderOwner.class
 * Java compiler version: 25 (69.0)
 * JD-Core Version:       1.1.3
 */