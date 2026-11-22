/*    */ package net.minecraft.util.filefix.virtualfilesystem;
/*    */ 
/*    */ import java.util.Collection;
/*    */ import java.util.Collections;
/*    */ import java.util.HashMap;
/*    */ import java.util.Map;
/*    */ import java.util.Objects;
/*    */ import net.minecraft.util.filefix.virtualfilesystem.exception.CowFSNoSuchFileException;
/*    */ import net.minecraft.util.filefix.virtualfilesystem.exception.CowFSNotDirectoryException;
/*    */ 
/*    */ final class DirectoryNode extends Node {
/* 16 */   private final Map<String, Node> childNodes = new HashMap<>();
/*    */   
/*    */   public DirectoryNode(CopyOnWriteFSPath path) {
/* 19 */     super(path);
/*    */   }
/*    */   
/*    */   public Collection<Node> children() {
/* 23 */     return Collections.unmodifiableCollection(this.childNodes.values());
/*    */   }
/*    */   
/*    */   public void addChild(Node child) {
/* 27 */     String name = Objects.<String>requireNonNull(child.name());
/* 28 */     this.childNodes.put(name, child);
/* 29 */     child.setParent(this);
/*    */   }
/*    */   
/*    */   void removeChild(String name) {
/* 34 */     this.childNodes.remove(name);
/*    */   }
/*    */   
/*    */   public Node getChild(String name) {
/* 38 */     return this.childNodes.get(name);
/*    */   }
/*    */   
/*    */   public DirectoryNode directoryByPath(CopyOnWriteFSPath path) throws CowFSNoSuchFileException, CowFSNotDirectoryException {
/* 42 */     Node node = byPath(path);
/* 43 */     if (node instanceof DirectoryNode) {
/* 43 */       DirectoryNode result = (DirectoryNode)node;
/* 44 */       return result;
/*    */     } 
/* 46 */     throw new CowFSNotDirectoryException(String.valueOf(path) + " was a file, expected directory");
/*    */   }
/*    */   
/*    */   public FileNode fileByPath(CopyOnWriteFSPath path) throws CowFSNoSuchFileException {
/* 50 */     Node node = byPathOrNull(path);
/* 51 */     if (node instanceof FileNode) {
/* 51 */       FileNode result = (FileNode)node;
/* 52 */       return result;
/*    */     } 
/* 54 */     throw new CowFSNoSuchFileException(path.toString());
/*    */   }
/*    */   
/*    */   public Node byPath(CopyOnWriteFSPath path) throws CowFSNoSuchFileException {
/* 58 */     Node node = byPathOrNull(path);
/* 59 */     if (node != null)
/* 60 */       return node; 
/* 62 */     throw new CowFSNoSuchFileException(path.toString());
/*    */   }
/*    */   
/*    */   public Node byPathOrNull(CopyOnWriteFSPath path) {
/* 66 */     int nameCount = path.getNameCount();
/* 67 */     DirectoryNode directory = this;
/* 68 */     for (int i = 0; i < nameCount; i++) {
/* 69 */       String name = path.getName(i).toString();
/* 70 */       if (!name.equals("."))
/* 72 */         if (name.equals("..")) {
/* 73 */           DirectoryNode parent = directory.parent;
/* 74 */           if (parent != null)
/* 75 */             directory = parent; 
/*    */         } else {
/* 79 */           Node nextNode = directory.getChild(name);
/* 80 */           if (nextNode instanceof DirectoryNode) {
/* 80 */             DirectoryNode nextDirectory = (DirectoryNode)nextNode;
/* 81 */             directory = nextDirectory;
/*    */           } else {
/* 84 */             return (i == nameCount - 1) ? nextNode : null;
/*    */           } 
/*    */         }  
/*    */     } 
/* 86 */     return directory;
/*    */   }
/*    */ }


/* Location:              C:\Users\karol\AppData\Roaming\ModrinthApp\meta\versions\26.2-0.19.5\26.2-0.19.5.jar!\net\minecraf\\util\filefix\virtualfilesystem\DirectoryNode.class
 * Java compiler version: 25 (69.0)
 * JD-Core Version:       1.1.3
 */