/*    */ package com.mojang.blaze3d.audio;
/*    */ 
/*    */ import com.mojang.logging.LogUtils;
/*    */ import javax.sound.sampled.AudioFormat;
/*    */ import org.lwjgl.openal.AL10;
/*    */ import org.lwjgl.openal.ALC10;
/*    */ import org.slf4j.Logger;
/*    */ 
/*    */ public class OpenAlUtil {
/* 11 */   private static final Logger LOGGER = LogUtils.getLogger();
/*    */   
/*    */   private static String alErrorToString(int error) {
/* 14 */     switch (error) {
/*    */       case 40961:
/*    */       
/*    */       case 40962:
/*    */       
/*    */       case 40963:
/*    */       
/*    */       case 40964:
/*    */       
/*    */       case 40965:
/*    */       
/*    */     } 
/* 14 */     return 
/*    */       
/* 20 */       "An unrecognized error occurred.";
/*    */   }
/*    */   
/*    */   public static boolean checkALError(String location) {
/* 25 */     int error = AL10.alGetError();
/* 26 */     if (error != 0) {
/* 27 */       LOGGER.error("{}: {}", location, alErrorToString(error));
/* 28 */       return true;
/*    */     } 
/* 30 */     return false;
/*    */   }
/*    */   
/*    */   private static String alcErrorToString(int error) {
/* 34 */     switch (error) {
/*    */       case 40961:
/*    */       
/*    */       case 40962:
/*    */       
/*    */       case 40964:
/*    */       
/*    */       case 40963:
/*    */       
/*    */       case 40965:
/*    */       
/*    */     } 
/* 34 */     return 
/*    */       
/* 40 */       "An unrecognized error occurred.";
/*    */   }
/*    */   
/*    */   public static boolean checkALCError(long device, String location) {
/* 45 */     int error = ALC10.alcGetError(device);
/* 46 */     if (error != 0) {
/* 47 */       LOGGER.error("{} ({}): {}", new Object[] { location, Long.valueOf(device), alcErrorToString(error) });
/* 48 */       return true;
/*    */     } 
/* 50 */     return false;
/*    */   }
/*    */   
/*    */   public static int audioFormatToOpenAl(AudioFormat audioFormat) {
/* 54 */     AudioFormat.Encoding encoding = audioFormat.getEncoding();
/* 55 */     int channels = audioFormat.getChannels();
/* 56 */     int sampleSizeInBits = audioFormat.getSampleSizeInBits();
/* 58 */     if (encoding.equals(AudioFormat.Encoding.PCM_UNSIGNED) || encoding.equals(AudioFormat.Encoding.PCM_SIGNED))
/* 59 */       if (channels == 1) {
/* 60 */         if (sampleSizeInBits == 8)
/* 61 */           return 4352; 
/* 62 */         if (sampleSizeInBits == 16)
/* 63 */           return 4353; 
/* 65 */       } else if (channels == 2) {
/* 66 */         if (sampleSizeInBits == 8)
/* 67 */           return 4354; 
/* 68 */         if (sampleSizeInBits == 16)
/* 69 */           return 4355; 
/*    */       }  
/* 74 */     throw new IllegalArgumentException("Invalid audio format: " + String.valueOf(audioFormat));
/*    */   }
/*    */ }


/* Location:              C:\Users\karol\AppData\Roaming\ModrinthApp\meta\versions\26.2-0.19.5\26.2-0.19.5.jar!\com\mojang\blaze3d\audio\OpenAlUtil.class
 * Java compiler version: 25 (69.0)
 * JD-Core Version:       1.1.3
 */