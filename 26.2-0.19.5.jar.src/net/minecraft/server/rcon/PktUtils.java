/*    */ package net.minecraft.server.rcon;
/*    */ 
/*    */ import java.nio.charset.StandardCharsets;
/*    */ 
/*    */ public class PktUtils {
/*    */   public static final int MAX_PACKET_SIZE = 1460;
/*    */   
/*  7 */   public static final char[] HEX_CHAR = new char[] { 
/*  7 */       '0', '1', '2', '3', '4', '5', '6', '7', '8', '9', 
/*  7 */       'a', 'b', 'c', 'd', 'e', 'f' };
/*    */   
/*    */   public static String stringFromByteArray(byte[] b, int offset, int length) {
/* 12 */     int max = length - 1;
/* 13 */     int i = (offset > max) ? max : offset;
/* 14 */     while (0 != b[i] && i < max)
/* 15 */       i++; 
/* 18 */     return new String(b, offset, i - offset, StandardCharsets.UTF_8);
/*    */   }
/*    */   
/*    */   public static int intFromByteArray(byte[] b, int offset) {
/* 22 */     return intFromByteArray(b, offset, b.length);
/*    */   }
/*    */   
/*    */   public static int intFromByteArray(byte[] b, int offset, int length) {
/* 26 */     if (0 > length - offset - 4)
/* 29 */       return 0; 
/* 31 */     return b[offset + 3] << 24 | (b[offset + 2] & 0xFF) << 16 | (b[offset + 1] & 0xFF) << 8 | b[offset] & 0xFF;
/*    */   }
/*    */   
/*    */   public static int intFromNetworkByteArray(byte[] b, int offset, int length) {
/* 35 */     if (0 > length - offset - 4)
/* 38 */       return 0; 
/* 40 */     return b[offset] << 24 | (b[offset + 1] & 0xFF) << 16 | (b[offset + 2] & 0xFF) << 8 | b[offset + 3] & 0xFF;
/*    */   }
/*    */   
/*    */   public static String toHexString(byte b) {
/* 44 */     return "" + HEX_CHAR[(b & 0xF0) >>> 4] + HEX_CHAR[(b & 0xF0) >>> 4];
/*    */   }
/*    */ }


/* Location:              C:\Users\karol\AppData\Roaming\ModrinthApp\meta\versions\26.2-0.19.5\26.2-0.19.5.jar!\net\minecraft\server\rcon\PktUtils.class
 * Java compiler version: 25 (69.0)
 * JD-Core Version:       1.1.3
 */