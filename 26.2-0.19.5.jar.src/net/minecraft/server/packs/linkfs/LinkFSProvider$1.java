/*    */ package net.minecraft.server.packs.linkfs;
/*    */ 
/*    */ import java.io.IOException;
/*    */ import java.nio.file.DirectoryIteratorException;
/*    */ import java.nio.file.DirectoryStream;
/*    */ import java.nio.file.Path;
/*    */ import java.util.Iterator;
/*    */ 
/*    */ class null implements DirectoryStream<Path> {
/*    */   null(LinkFSProvider this$0) {}
/*    */   
/*    */   public Iterator<Path> iterator() {
/* 84 */     return directoryContents.children().values()
/* 85 */       .stream()
/* 86 */       .filter(path -> {
/*    */           try {
/*    */             return filter.accept(path);
/* 89 */           } catch (IOException e) {
/*    */             throw new DirectoryIteratorException(e);
/*    */           } 
/* 93 */         }).map(path -> path)
/* 94 */       .iterator();
/*    */   }
/*    */   
/*    */   public void close() {}
/*    */ }


/* Location:              C:\Users\karol\AppData\Roaming\ModrinthApp\meta\versions\26.2-0.19.5\26.2-0.19.5.jar!\net\minecraft\server\packs\linkfs\LinkFSProvider$1.class
 * Java compiler version: 25 (69.0)
 * JD-Core Version:       1.1.3
 */