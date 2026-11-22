/*    */ package net.minecraft.util.filefix.operations;
/*    */ 
/*    */ import java.io.IOException;
/*    */ import java.io.UncheckedIOException;
/*    */ import java.nio.file.Path;
/*    */ import net.minecraft.util.filefix.access.FileAccessProvider;
/*    */ import net.minecraft.util.worldupdate.UpgradeProgress;
/*    */ 
/*    */ public class ModifyContent implements FileFixOperation {
/*    */   private final FileAccessProvider fileAccessProvider;
/*    */   
/*    */   private final FixFunction fixFunction;
/*    */   
/*    */   public ModifyContent(FileAccessProvider fileAccessProvider, FixFunction fixFunction) {
/* 25 */     this.fileAccessProvider = fileAccessProvider;
/* 26 */     this.fixFunction = fixFunction;
/*    */   }
/*    */   
/*    */   public void fix(Path baseDirectory, UpgradeProgress upgradeProgress) throws IOException {
/*    */     try {
/* 32 */       ScopedValue.<Path>where(this.fileAccessProvider
/* 33 */           .baseDirectory(), baseDirectory)
/*    */         
/* 35 */         .run(() -> {
/*    */             try {
/*    */               this.fixFunction.run(upgradeProgress);
/* 39 */             } catch (IOException e) {
/*    */               throw new UncheckedIOException(e);
/*    */             } 
/*    */           });
/* 44 */     } catch (UncheckedIOException e) {
/* 45 */       throw e.getCause();
/*    */     } finally {
/* 47 */       this.fileAccessProvider.close();
/*    */     } 
/*    */   }
/*    */   
/*    */   @FunctionalInterface
/*    */   public static interface FixFunction {
/*    */     void run(UpgradeProgress param1UpgradeProgress) throws IOException;
/*    */   }
/*    */   
/*    */   @FunctionalInterface
/*    */   public static interface FileAccessFunction {
/*    */     ModifyContent.FixFunction make(FileAccessProvider param1FileAccessProvider);
/*    */   }
/*    */ }


/* Location:              C:\Users\karol\AppData\Roaming\ModrinthApp\meta\versions\26.2-0.19.5\26.2-0.19.5.jar!\net\minecraf\\util\filefix\operations\ModifyContent.class
 * Java compiler version: 25 (69.0)
 * JD-Core Version:       1.1.3
 */