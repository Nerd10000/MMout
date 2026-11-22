/*    */ package net.minecraft.client.searchtree;
/*    */ 
/*    */ import java.util.List;
/*    */ 
/*    */ class null implements IdentifierSearchTree<T> {
/*    */   public List<T> searchNamespace(String namespace) {
/* 44 */     return namespaceTree.search(namespace);
/*    */   }
/*    */   
/*    */   public List<T> searchPath(String path) {
/* 49 */     return pathTree.search(path);
/*    */   }
/*    */ }


/* Location:              C:\Users\karol\AppData\Roaming\ModrinthApp\meta\versions\26.2-0.19.5\26.2-0.19.5.jar!\net\minecraft\client\searchtree\IdentifierSearchTree$2.class
 * Java compiler version: 25 (69.0)
 * JD-Core Version:       1.1.3
 */