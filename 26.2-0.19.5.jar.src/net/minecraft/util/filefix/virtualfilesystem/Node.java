/*    */ package net.minecraft.util.filefix.virtualfilesystem;
/*    */ 
/*    */ abstract class Node {
/*    */   protected DirectoryNode parent;
/*    */   
/*    */   protected CopyOnWriteFSPath path;
/*    */   
/*    */   protected Node(CopyOnWriteFSPath cowPath) {
/* 12 */     setPath(cowPath);
/*    */   }
/*    */   
/*    */   public String name() {
/* 16 */     CopyOnWriteFSPath fileName = this.path.getFileName();
/* 17 */     return (fileName == null) ? null : fileName.toString();
/*    */   }
/*    */   
/*    */   protected void setParent(DirectoryNode parent) {
/* 21 */     this.parent = parent;
/*    */   }
/*    */   
/*    */   protected void setPath(CopyOnWriteFSPath path) {
/* 25 */     this.path = path.normalize().toAbsolutePath();
/*    */   }
/*    */   
/*    */   public CopyOnWriteFSPath path() {
/* 29 */     return this.path;
/*    */   }
/*    */ }


/* Location:              C:\Users\karol\AppData\Roaming\ModrinthApp\meta\versions\26.2-0.19.5\26.2-0.19.5.jar!\net\minecraf\\util\filefix\virtualfilesystem\Node.class
 * Java compiler version: 25 (69.0)
 * JD-Core Version:       1.1.3
 */