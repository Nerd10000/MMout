/*     */ package net.minecraft.server.rcon.thread;
/*     */ 
/*     */ import java.net.DatagramPacket;
/*     */ import java.nio.charset.StandardCharsets;
/*     */ import java.util.Date;
/*     */ import java.util.Locale;
/*     */ import net.minecraft.util.RandomSource;
/*     */ 
/*     */ class RequestChallenge {
/* 327 */   private final long time = (new Date()).getTime();
/*     */   
/*     */   private final int challenge;
/*     */   
/*     */   private final byte[] identBytes;
/*     */   
/*     */   private final byte[] challengeBytes;
/*     */   
/*     */   private final String ident;
/*     */   
/*     */   public RequestChallenge(DatagramPacket src) {
/* 328 */     byte[] buf = src.getData();
/* 329 */     this.identBytes = new byte[4];
/* 330 */     this.identBytes[0] = buf[3];
/* 331 */     this.identBytes[1] = buf[4];
/* 332 */     this.identBytes[2] = buf[5];
/* 333 */     this.identBytes[3] = buf[6];
/* 334 */     this.ident = new String(this.identBytes, StandardCharsets.UTF_8);
/* 335 */     this.challenge = RandomSource.createThreadLocalInstance().nextInt(16777216);
/* 336 */     this.challengeBytes = String.format(Locale.ROOT, "\t%s%d\000", new Object[] { this.ident, Integer.valueOf(this.challenge) }).getBytes(StandardCharsets.UTF_8);
/*     */   }
/*     */   
/*     */   public Boolean before(long time) {
/* 340 */     return Boolean.valueOf((this.time < time));
/*     */   }
/*     */   
/*     */   public int getChallenge() {
/* 344 */     return this.challenge;
/*     */   }
/*     */   
/*     */   public byte[] getChallengeBytes() {
/* 348 */     return this.challengeBytes;
/*     */   }
/*     */   
/*     */   public byte[] getIdentBytes() {
/* 352 */     return this.identBytes;
/*     */   }
/*     */   
/*     */   public String getIdent() {
/* 357 */     return this.ident;
/*     */   }
/*     */ }


/* Location:              C:\Users\karol\AppData\Roaming\ModrinthApp\meta\versions\26.2-0.19.5\26.2-0.19.5.jar!\net\minecraft\server\rcon\thread\QueryThreadGs4$RequestChallenge.class
 * Java compiler version: 25 (69.0)
 * JD-Core Version:       1.1.3
 */