/*    */ package com.mojang.blaze3d.platform;
/*    */ 
/*    */ import ca.weblite.objc.Client;
/*    */ import ca.weblite.objc.NSObject;
/*    */ import com.sun.jna.Pointer;
/*    */ import java.io.IOException;
/*    */ import java.io.InputStream;
/*    */ import java.util.Base64;
/*    */ import java.util.Locale;
/*    */ import java.util.Optional;
/*    */ import net.minecraft.server.packs.resources.IoSupplier;
/*    */ import org.lwjgl.glfw.GLFWNativeCocoa;
/*    */ 
/*    */ public class MacosUtil {
/* 17 */   public static final boolean IS_MACOS = System.getProperty("os.name").toLowerCase(Locale.ROOT).contains("mac");
/*    */   
/*    */   private static final int NS_RESIZABLE_WINDOW_MASK = 8;
/*    */   
/*    */   private static final int NS_FULL_SCREEN_WINDOW_MASK = 16384;
/*    */   
/*    */   public static void exitNativeFullscreen(Window window) {
/* 24 */     getNsWindow(window).filter(MacosUtil::isInNativeFullscreen).ifPresent(MacosUtil::toggleNativeFullscreen);
/*    */   }
/*    */   
/*    */   public static void clearResizableBit(Window window) {
/* 28 */     getNsWindow(window).ifPresent(nsWindow -> {
/*    */           long styleMask = getStyleMask(nsWindow);
/*    */           nsWindow.send("setStyleMask:", new Object[] { Long.valueOf(styleMask & 0xFFFFFFFFFFFFFFF7L) });
/*    */         });
/*    */   }
/*    */   
/*    */   private static Optional<NSObject> getNsWindow(Window window) {
/* 35 */     return getNsWindow(window.handle());
/*    */   }
/*    */   
/*    */   private static Optional<NSObject> getNsWindow(long windowHandle) {
/* 39 */     long nsWindow = GLFWNativeCocoa.glfwGetCocoaWindow(windowHandle);
/* 40 */     if (nsWindow != 0L)
/* 41 */       return Optional.of(new NSObject(new Pointer(nsWindow))); 
/* 43 */     return Optional.empty();
/*    */   }
/*    */   
/*    */   private static boolean isInNativeFullscreen(NSObject nsWindow) {
/* 47 */     return ((getStyleMask(nsWindow) & 0x4000L) != 0L);
/*    */   }
/*    */   
/*    */   private static long getStyleMask(NSObject nsWindow) {
/* 51 */     return ((Long)nsWindow.sendRaw("styleMask", new Object[0])).longValue();
/*    */   }
/*    */   
/*    */   private static void toggleNativeFullscreen(NSObject nsWindow) {
/* 55 */     nsWindow.send("toggleFullScreen:", new Object[] { Pointer.NULL });
/*    */   }
/*    */   
/*    */   public static void loadIcon(IoSupplier<InputStream> icon) throws IOException {
/* 59 */     InputStream iconStream = (InputStream)icon.get();
/*    */     try {
/* 60 */       String base64Icon = Base64.getEncoder().encodeToString(iconStream.readAllBytes());
/* 61 */       Client objc = Client.getInstance();
/* 63 */       Object data = objc.sendProxy("NSData", "alloc", new Object[0]).send("initWithBase64Encoding:", new Object[] { base64Icon });
/* 64 */       Object image = objc.sendProxy("NSImage", "alloc", new Object[0]).send("initWithData:", new Object[] { data });
/* 66 */       objc.sendProxy("NSApplication", "sharedApplication", new Object[0]).send("setApplicationIconImage:", new Object[] { image });
/* 67 */       if (iconStream != null)
/* 67 */         iconStream.close(); 
/*    */     } catch (Throwable throwable) {
/*    */       if (iconStream != null)
/*    */         try {
/*    */           iconStream.close();
/*    */         } catch (Throwable throwable1) {
/*    */           throwable.addSuppressed(throwable1);
/*    */         }  
/*    */       throw throwable;
/*    */     } 
/*    */   }
/*    */   
/*    */   public static void setWindowColorSpaceForOpenGLBecauseGLFWDoesnt(long glfwWindowHandle) {
/* 73 */     getNsWindow(glfwWindowHandle).ifPresent(nsWindow -> {
/*    */           Object sRGBColorSpace = nsWindow.getClient().send("NSColorSpace", "sRGBColorSpace", new Object[0]);
/*    */           nsWindow.send("setColorSpace:", new Object[] { sRGBColorSpace });
/*    */         });
/*    */   }
/*    */ }


/* Location:              C:\Users\karol\AppData\Roaming\ModrinthApp\meta\versions\26.2-0.19.5\26.2-0.19.5.jar!\com\mojang\blaze3d\platform\MacosUtil.class
 * Java compiler version: 25 (69.0)
 * JD-Core Version:       1.1.3
 */