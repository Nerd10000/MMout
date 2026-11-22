/*    */ package net.minecraft.server;
/*    */ 
/*    */ import com.mojang.logging.LogUtils;
/*    */ import java.io.InputStream;
/*    */ import java.io.OutputStream;
/*    */ import java.nio.file.Files;
/*    */ import java.nio.file.Path;
/*    */ import java.util.Properties;
/*    */ import net.minecraft.SharedConstants;
/*    */ import net.minecraft.util.CommonLinks;
/*    */ import org.slf4j.Logger;
/*    */ 
/*    */ public class Eula {
/* 15 */   private static final Logger LOGGER = LogUtils.getLogger();
/*    */   
/*    */   private final Path file;
/*    */   
/*    */   private final boolean agreed;
/*    */   
/*    */   public Eula(Path file) {
/* 21 */     this.file = file;
/* 22 */     this.agreed = (SharedConstants.IS_RUNNING_IN_IDE || readFile());
/*    */   }
/*    */   
/*    */   private boolean readFile() {
/*    */     try {
/* 26 */       InputStream input = Files.newInputStream(this.file, new java.nio.file.OpenOption[0]);
/*    */       try {
/* 27 */         Properties properties = new Properties();
/* 28 */         properties.load(input);
/* 29 */         boolean bool = Boolean.parseBoolean(properties.getProperty("eula", "false"));
/* 30 */         if (input != null)
/* 30 */           input.close(); 
/*    */         return bool;
/*    */       } catch (Throwable throwable) {
/*    */         if (input != null)
/*    */           try {
/*    */             input.close();
/*    */           } catch (Throwable throwable1) {
/*    */             throwable.addSuppressed(throwable1);
/*    */           }  
/*    */         throw throwable;
/*    */       } 
/* 30 */     } catch (Exception ignored) {
/* 31 */       LOGGER.warn("Failed to load {}", this.file);
/* 32 */       saveDefaults();
/* 34 */       return false;
/*    */     } 
/*    */   }
/*    */   
/*    */   public boolean hasAgreedToEULA() {
/* 38 */     return this.agreed;
/*    */   }
/*    */   
/*    */   private void saveDefaults() {
/* 42 */     if (SharedConstants.IS_RUNNING_IN_IDE)
/*    */       return; 
/*    */     try {
/* 45 */       OutputStream output = Files.newOutputStream(this.file, new java.nio.file.OpenOption[0]);
/*    */       try {
/* 46 */         Properties properties = new Properties();
/* 47 */         properties.setProperty("eula", "false");
/* 48 */         properties.store(output, "By changing the setting below to TRUE you are indicating your agreement to our EULA (" + String.valueOf(CommonLinks.EULA) + ").");
/* 49 */         if (output != null)
/* 49 */           output.close(); 
/*    */       } catch (Throwable throwable) {
/*    */         if (output != null)
/*    */           try {
/*    */             output.close();
/*    */           } catch (Throwable throwable1) {
/*    */             throwable.addSuppressed(throwable1);
/*    */           }  
/*    */         throw throwable;
/*    */       } 
/* 49 */     } catch (Exception e) {
/* 50 */       LOGGER.warn("Failed to save {}", this.file, e);
/*    */     } 
/*    */   }
/*    */ }


/* Location:              C:\Users\karol\AppData\Roaming\ModrinthApp\meta\versions\26.2-0.19.5\26.2-0.19.5.jar!\net\minecraft\server\Eula.class
 * Java compiler version: 25 (69.0)
 * JD-Core Version:       1.1.3
 */