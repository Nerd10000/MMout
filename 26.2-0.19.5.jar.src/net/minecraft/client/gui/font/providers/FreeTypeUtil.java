/*    */ package net.minecraft.client.gui.font.providers;
/*    */ 
/*    */ import com.mojang.logging.LogUtils;
/*    */ import org.lwjgl.PointerBuffer;
/*    */ import org.lwjgl.system.MemoryStack;
/*    */ import org.lwjgl.util.freetype.FT_Vector;
/*    */ import org.lwjgl.util.freetype.FreeType;
/*    */ import org.slf4j.Logger;
/*    */ 
/*    */ public class FreeTypeUtil {
/* 12 */   private static final Logger LOGGER = LogUtils.getLogger();
/*    */   
/* 14 */   public static final Object LIBRARY_LOCK = new Object();
/*    */   
/* 16 */   private static long library = 0L;
/*    */   
/*    */   public static long getLibrary() {
/* 19 */     synchronized (LIBRARY_LOCK) {
/* 20 */       if (library == 0L) {
/* 21 */         MemoryStack stack = MemoryStack.stackPush();
/*    */         try {
/* 22 */           PointerBuffer libraryBuffer = stack.mallocPointer(1);
/* 23 */           assertError(FreeType.FT_Init_FreeType(libraryBuffer), "Initializing FreeType library");
/* 24 */           library = libraryBuffer.get();
/* 25 */           if (stack != null)
/* 25 */             stack.close(); 
/*    */         } catch (Throwable throwable) {
/*    */           if (stack != null)
/*    */             try {
/*    */               stack.close();
/*    */             } catch (Throwable throwable1) {
/*    */               throwable.addSuppressed(throwable1);
/*    */             }  
/*    */           throw throwable;
/*    */         } 
/*    */       } 
/* 27 */       return library;
/*    */     } 
/*    */   }
/*    */   
/*    */   public static void assertError(int errorCode, String type) {
/* 32 */     if (errorCode != 0)
/* 33 */       throw new IllegalStateException("FreeType error: " + describeError(errorCode) + " (" + type + ")"); 
/*    */   }
/*    */   
/*    */   public static boolean checkError(int errorCode, String type) {
/* 38 */     if (errorCode != 0) {
/* 39 */       LOGGER.error("FreeType error: {} ({})", describeError(errorCode), type);
/* 40 */       return true;
/*    */     } 
/* 42 */     return false;
/*    */   }
/*    */   
/*    */   private static String describeError(int code) {
/* 46 */     String string = FreeType.FT_Error_String(code);
/* 47 */     if (string != null)
/* 48 */       return string; 
/* 50 */     return "Unrecognized error: 0x" + Integer.toHexString(code);
/*    */   }
/*    */   
/*    */   public static FT_Vector setVector(FT_Vector vector, float x, float y) {
/* 54 */     long fixedPointX = Math.round(x * 64.0F);
/* 55 */     long fixedPointY = Math.round(y * 64.0F);
/* 56 */     return vector.set(fixedPointX, fixedPointY);
/*    */   }
/*    */   
/*    */   public static float x(FT_Vector vector) {
/* 60 */     return (float)vector.x() / 64.0F;
/*    */   }
/*    */   
/*    */   public static void destroy() {
/* 64 */     synchronized (LIBRARY_LOCK) {
/* 65 */       if (library != 0L) {
/* 66 */         FreeType.FT_Done_Library(library);
/* 67 */         library = 0L;
/*    */       } 
/*    */     } 
/*    */   }
/*    */ }


/* Location:              C:\Users\karol\AppData\Roaming\ModrinthApp\meta\versions\26.2-0.19.5\26.2-0.19.5.jar!\net\minecraft\client\gui\font\providers\FreeTypeUtil.class
 * Java compiler version: 25 (69.0)
 * JD-Core Version:       1.1.3
 */