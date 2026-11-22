/*     */ package net.minecraft.client.resources;
/*     */ 
/*     */ import com.mojang.authlib.properties.Property;
/*     */ import java.util.UUID;
/*     */ 
/*     */ final class CacheKey extends Record {
/*     */   private final UUID profileId;
/*     */   
/*     */   private final Property packedTextures;
/*     */   
/*     */   private CacheKey(UUID profileId, Property packedTextures) {
/* 174 */     this.profileId = profileId;
/* 174 */     this.packedTextures = packedTextures;
/*     */   }
/*     */   
/*     */   public final String toString() {
/*     */     // Byte code:
/*     */     //   0: aload_0
/*     */     //   1: <illegal opcode> toString : (Lnet/minecraft/client/resources/SkinManager$CacheKey;)Ljava/lang/String;
/*     */     //   6: areturn
/*     */     // Line number table:
/*     */     //   Java source line number -> byte code offset
/*     */     //   #174	-> 0
/*     */     // Local variable table:
/*     */     //   start	length	slot	name	descriptor
/*     */     //   0	7	0	this	Lnet/minecraft/client/resources/SkinManager$CacheKey;
/*     */   }
/*     */   
/*     */   public final int hashCode() {
/*     */     // Byte code:
/*     */     //   0: aload_0
/*     */     //   1: <illegal opcode> hashCode : (Lnet/minecraft/client/resources/SkinManager$CacheKey;)I
/*     */     //   6: ireturn
/*     */     // Line number table:
/*     */     //   Java source line number -> byte code offset
/*     */     //   #174	-> 0
/*     */     // Local variable table:
/*     */     //   start	length	slot	name	descriptor
/*     */     //   0	7	0	this	Lnet/minecraft/client/resources/SkinManager$CacheKey;
/*     */   }
/*     */   
/*     */   public final boolean equals(Object o) {
/*     */     // Byte code:
/*     */     //   0: aload_0
/*     */     //   1: aload_1
/*     */     //   2: <illegal opcode> equals : (Lnet/minecraft/client/resources/SkinManager$CacheKey;Ljava/lang/Object;)Z
/*     */     //   7: ireturn
/*     */     // Line number table:
/*     */     //   Java source line number -> byte code offset
/*     */     //   #174	-> 0
/*     */     // Local variable table:
/*     */     //   start	length	slot	name	descriptor
/*     */     //   0	8	0	this	Lnet/minecraft/client/resources/SkinManager$CacheKey;
/*     */     //   0	8	1	o	Ljava/lang/Object;
/*     */   }
/*     */   
/*     */   public UUID profileId() {
/* 174 */     return this.profileId;
/*     */   }
/*     */   
/*     */   public Property packedTextures() {
/* 174 */     return this.packedTextures;
/*     */   }
/*     */ }


/* Location:              C:\Users\karol\AppData\Roaming\ModrinthApp\meta\versions\26.2-0.19.5\26.2-0.19.5.jar!\net\minecraft\client\resources\SkinManager$CacheKey.class
 * Java compiler version: 25 (69.0)
 * JD-Core Version:       1.1.3
 */