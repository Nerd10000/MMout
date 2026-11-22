/*    */ package net.minecraft.data;
/*    */ 
/*    */ import com.google.common.hash.HashCode;
/*    */ import java.io.IOException;
/*    */ import java.nio.file.Files;
/*    */ import java.nio.file.Path;
/*    */ import net.minecraft.util.FileUtil;
/*    */ 
/*    */ public interface CachedOutput {
/*    */   public static final CachedOutput NO_CACHE;
/*    */   
/*    */   static {
/* 11 */     NO_CACHE = ((path, input, hash) -> {
/*    */         FileUtil.createDirectoriesSafe(path.getParent());
/*    */         Files.write(path, input, new java.nio.file.OpenOption[0]);
/*    */       });
/*    */   }
/*    */   
/*    */   void writeIfNeeded(Path paramPath, byte[] paramArrayOfbyte, HashCode paramHashCode) throws IOException;
/*    */ }


/* Location:              C:\Users\karol\AppData\Roaming\ModrinthApp\meta\versions\26.2-0.19.5\26.2-0.19.5.jar!\net\minecraft\data\CachedOutput.class
 * Java compiler version: 25 (69.0)
 * JD-Core Version:       1.1.3
 */