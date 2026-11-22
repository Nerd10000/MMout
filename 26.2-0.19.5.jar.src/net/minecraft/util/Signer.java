/*    */ package net.minecraft.util;
/*    */ 
/*    */ import com.mojang.logging.LogUtils;
/*    */ import java.security.PrivateKey;
/*    */ import java.security.Signature;
/*    */ import java.security.SignatureException;
/*    */ import java.util.Objects;
/*    */ import org.slf4j.Logger;
/*    */ 
/*    */ public interface Signer {
/* 10 */   public static final Logger LOGGER = LogUtils.getLogger();
/*    */   
/*    */   default byte[] sign(byte[] payload) {
/* 15 */     return sign(output -> output.update(payload));
/*    */   }
/*    */   
/*    */   static Signer from(PrivateKey privateKey, String algorithm) {
/* 19 */     return updater -> {
/*    */         try {
/*    */           Signature signer = Signature.getInstance(algorithm);
/*    */           signer.initSign(privateKey);
/*    */           Objects.requireNonNull(signer);
/*    */           updater.update(signer::update);
/*    */           return signer.sign();
/* 25 */         } catch (Exception e) {
/*    */           throw new IllegalStateException("Failed to sign message", e);
/*    */         } 
/*    */       };
/*    */   }
/*    */   
/*    */   byte[] sign(SignatureUpdater paramSignatureUpdater);
/*    */ }


/* Location:              C:\Users\karol\AppData\Roaming\ModrinthApp\meta\versions\26.2-0.19.5\26.2-0.19.5.jar!\net\minecraf\\util\Signer.class
 * Java compiler version: 25 (69.0)
 * JD-Core Version:       1.1.3
 */