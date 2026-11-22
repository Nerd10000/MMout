/*     */ package net.minecraft.util;
/*     */ 
/*     */ import java.io.File;
/*     */ import java.io.IOException;
/*     */ import java.net.URI;
/*     */ import java.net.URISyntaxException;
/*     */ import java.nio.file.Path;
/*     */ 
/*     */ public enum OS {
/* 499 */   LINUX("linux"),
/* 500 */   SOLARIS("solaris"),
/* 501 */   WINDOWS("windows") {
/*     */     protected String[] getOpenUriArguments(URI uri) {
/* 504 */       return new String[] { "rundll32", "url.dll,FileProtocolHandler", uri.toString() };
/*     */     }
/*     */   },
/* 507 */   OSX("mac") {
/*     */     protected String[] getOpenUriArguments(URI uri) {
/* 510 */       return new String[] { "open", uri.toString() };
/*     */     }
/*     */   },
/* 513 */   UNKNOWN("unknown");
/*     */   
/*     */   private final String telemetryName;
/*     */   
/*     */   OS(String telemetryName) {
/* 518 */     this.telemetryName = telemetryName;
/*     */   }
/*     */   
/*     */   public void openUri(URI uri) {
/*     */     try {
/* 523 */       Process process = Runtime.getRuntime().exec(getOpenUriArguments(uri));
/* 524 */       process.getInputStream().close();
/* 525 */       process.getErrorStream().close();
/* 526 */       process.getOutputStream().close();
/* 527 */     } catch (IOException e) {
/* 528 */       Util.LOGGER.error("Couldn't open location '{}'", uri, e);
/*     */     } 
/*     */   }
/*     */   
/*     */   public void openFile(File file) {
/* 533 */     openUri(file.toURI());
/*     */   }
/*     */   
/*     */   public void openPath(Path path) {
/* 537 */     openUri(path.toUri());
/*     */   }
/*     */   
/*     */   protected String[] getOpenUriArguments(URI uri) {
/* 541 */     String string = uri.toString();
/* 542 */     if ("file".equals(uri.getScheme()))
/* 544 */       string = string.replace("file:", "file://"); 
/* 546 */     return new String[] { "xdg-open", string };
/*     */   }
/*     */   
/*     */   public void openUri(String uri) {
/*     */     try {
/* 551 */       openUri(new URI(uri));
/* 553 */     } catch (URISyntaxException|IllegalArgumentException e) {
/* 554 */       Util.LOGGER.error("Couldn't open uri '{}'", uri, e);
/*     */     } 
/*     */   }
/*     */   
/*     */   public String telemetryName() {
/* 559 */     return this.telemetryName;
/*     */   }
/*     */ }


/* Location:              C:\Users\karol\AppData\Roaming\ModrinthApp\meta\versions\26.2-0.19.5\26.2-0.19.5.jar!\net\minecraf\\util\Util$OS.class
 * Java compiler version: 25 (69.0)
 * JD-Core Version:       1.1.3
 */