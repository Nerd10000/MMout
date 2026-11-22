/*    */ package net.minecraft.client.main;
/*    */ 
/*    */ import java.io.File;
/*    */ import java.nio.file.Path;
/*    */ import net.minecraft.client.resources.IndexedAssetSource;
/*    */ 
/*    */ public class FolderData {
/*    */   public final File gameDirectory;
/*    */   
/*    */   public final File resourcePackDirectory;
/*    */   
/*    */   public final File assetDirectory;
/*    */   
/*    */   public final String assetIndex;
/*    */   
/*    */   public FolderData(File gameDirectory, File resourcePackDirectory, File assetDirectory, String assetIndex) {
/* 72 */     this.gameDirectory = gameDirectory;
/* 73 */     this.resourcePackDirectory = resourcePackDirectory;
/* 74 */     this.assetDirectory = assetDirectory;
/* 75 */     this.assetIndex = assetIndex;
/*    */   }
/*    */   
/*    */   public Path getExternalAssetSource() {
/* 79 */     return (this.assetIndex == null) ? this.assetDirectory.toPath() : IndexedAssetSource.createIndexFs(this.assetDirectory.toPath(), this.assetIndex);
/*    */   }
/*    */ }


/* Location:              C:\Users\karol\AppData\Roaming\ModrinthApp\meta\versions\26.2-0.19.5\26.2-0.19.5.jar!\net\minecraft\client\main\GameConfig$FolderData.class
 * Java compiler version: 25 (69.0)
 * JD-Core Version:       1.1.3
 */