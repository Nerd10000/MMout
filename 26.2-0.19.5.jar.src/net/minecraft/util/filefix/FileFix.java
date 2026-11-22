/*    */ package net.minecraft.util.filefix;
/*    */ 
/*    */ import com.mojang.datafixers.DataFixUtils;
/*    */ import com.mojang.datafixers.schemas.Schema;
/*    */ import java.io.IOException;
/*    */ import java.nio.file.Path;
/*    */ import java.util.ArrayList;
/*    */ import java.util.List;
/*    */ import net.minecraft.util.filefix.access.FileAccessProvider;
/*    */ import net.minecraft.util.filefix.operations.FileFixOperation;
/*    */ import net.minecraft.util.filefix.operations.ModifyContent;
/*    */ import net.minecraft.util.worldupdate.UpgradeProgress;
/*    */ 
/*    */ public abstract class FileFix {
/*    */   private final Schema schema;
/*    */   
/* 17 */   private final List<FileFixOperation> fileFixOperations = new ArrayList<>();
/*    */   
/*    */   public FileFix(Schema schema) {
/* 20 */     this.schema = schema;
/* 21 */     makeFixer();
/*    */   }
/*    */   
/*    */   public final Schema getSchema() {
/* 25 */     return this.schema;
/*    */   }
/*    */   
/*    */   public final int getVersion() {
/* 29 */     return DataFixUtils.getVersion(this.schema.getVersionKey());
/*    */   }
/*    */   
/*    */   public void addFileFixOperation(FileFixOperation fileFixOperation) {
/* 33 */     this.fileFixOperations.add(fileFixOperation);
/*    */   }
/*    */   
/*    */   public void addFileContentFix(ModifyContent.FileAccessFunction fileAccessFunction) {
/* 37 */     FileAccessProvider fileAccessProvider = new FileAccessProvider(getVersion());
/* 38 */     ModifyContent.FixFunction fixFunction = fileAccessFunction.make(fileAccessProvider);
/* 39 */     fileAccessProvider.freeze();
/* 40 */     this.fileFixOperations.add(new ModifyContent(fileAccessProvider, fixFunction));
/*    */   }
/*    */   
/*    */   public int countFileOperations() {
/* 44 */     return this.fileFixOperations.size();
/*    */   }
/*    */   
/*    */   public void runFixOperations(Path baseFolder, UpgradeProgress upgradeProgress) throws IOException {
/* 48 */     for (FileFixOperation fileFixOperation : this.fileFixOperations) {
/* 49 */       if (upgradeProgress.isCanceled())
/* 50 */         throw new CanceledFileFixException(); 
/* 52 */       fileFixOperation.fix(baseFolder, upgradeProgress);
/* 53 */       upgradeProgress.incrementFinishedOperations();
/*    */     } 
/*    */   }
/*    */   
/*    */   public abstract void makeFixer();
/*    */ }


/* Location:              C:\Users\karol\AppData\Roaming\ModrinthApp\meta\versions\26.2-0.19.5\26.2-0.19.5.jar!\net\minecraf\\util\filefix\FileFix.class
 * Java compiler version: 25 (69.0)
 * JD-Core Version:       1.1.3
 */