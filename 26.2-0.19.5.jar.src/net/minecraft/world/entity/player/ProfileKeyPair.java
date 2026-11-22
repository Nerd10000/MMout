/*    */ package net.minecraft.world.entity.player;
/*    */ 
/*    */ import com.mojang.datafixers.kinds.App;
/*    */ import com.mojang.datafixers.kinds.Applicative;
/*    */ import com.mojang.datafixers.util.Function3;
/*    */ import com.mojang.serialization.Codec;
/*    */ import com.mojang.serialization.codecs.RecordCodecBuilder;
/*    */ import java.security.PrivateKey;
/*    */ import java.time.Instant;
/*    */ import net.minecraft.util.Crypt;
/*    */ import net.minecraft.util.ExtraCodecs;
/*    */ 
/*    */ public final class ProfileKeyPair extends Record {
/*    */   private final PrivateKey privateKey;
/*    */   
/*    */   private final ProfilePublicKey publicKey;
/*    */   
/*    */   private final Instant refreshedAfter;
/*    */   
/*    */   public static final Codec<ProfileKeyPair> CODEC;
/*    */   
/*    */   public ProfileKeyPair(PrivateKey privateKey, ProfilePublicKey publicKey, Instant refreshedAfter) {
/* 11 */     this.privateKey = privateKey;
/* 11 */     this.publicKey = publicKey;
/* 11 */     this.refreshedAfter = refreshedAfter;
/*    */   }
/*    */   
/*    */   public final String toString() {
/*    */     // Byte code:
/*    */     //   0: aload_0
/*    */     //   1: <illegal opcode> toString : (Lnet/minecraft/world/entity/player/ProfileKeyPair;)Ljava/lang/String;
/*    */     //   6: areturn
/*    */     // Line number table:
/*    */     //   Java source line number -> byte code offset
/*    */     //   #11	-> 0
/*    */     // Local variable table:
/*    */     //   start	length	slot	name	descriptor
/*    */     //   0	7	0	this	Lnet/minecraft/world/entity/player/ProfileKeyPair;
/*    */   }
/*    */   
/*    */   public final int hashCode() {
/*    */     // Byte code:
/*    */     //   0: aload_0
/*    */     //   1: <illegal opcode> hashCode : (Lnet/minecraft/world/entity/player/ProfileKeyPair;)I
/*    */     //   6: ireturn
/*    */     // Line number table:
/*    */     //   Java source line number -> byte code offset
/*    */     //   #11	-> 0
/*    */     // Local variable table:
/*    */     //   start	length	slot	name	descriptor
/*    */     //   0	7	0	this	Lnet/minecraft/world/entity/player/ProfileKeyPair;
/*    */   }
/*    */   
/*    */   public final boolean equals(Object o) {
/*    */     // Byte code:
/*    */     //   0: aload_0
/*    */     //   1: aload_1
/*    */     //   2: <illegal opcode> equals : (Lnet/minecraft/world/entity/player/ProfileKeyPair;Ljava/lang/Object;)Z
/*    */     //   7: ireturn
/*    */     // Line number table:
/*    */     //   Java source line number -> byte code offset
/*    */     //   #11	-> 0
/*    */     // Local variable table:
/*    */     //   start	length	slot	name	descriptor
/*    */     //   0	8	0	this	Lnet/minecraft/world/entity/player/ProfileKeyPair;
/*    */     //   0	8	1	o	Ljava/lang/Object;
/*    */   }
/*    */   
/*    */   public PrivateKey privateKey() {
/* 11 */     return this.privateKey;
/*    */   }
/*    */   
/*    */   public ProfilePublicKey publicKey() {
/* 11 */     return this.publicKey;
/*    */   }
/*    */   
/*    */   public Instant refreshedAfter() {
/* 11 */     return this.refreshedAfter;
/*    */   }
/*    */   
/*    */   static {
/* 12 */     CODEC = RecordCodecBuilder.create(i -> i.group((App)Crypt.PRIVATE_KEY_CODEC.fieldOf("private_key").forGetter(ProfileKeyPair::privateKey), (App)ProfilePublicKey.TRUSTED_CODEC.fieldOf("public_key").forGetter(ProfileKeyPair::publicKey), (App)ExtraCodecs.INSTANT_ISO8601.fieldOf("refreshed_after").forGetter(ProfileKeyPair::refreshedAfter)).apply((Applicative)i, ProfileKeyPair::new));
/*    */   }
/*    */   
/*    */   public boolean dueRefresh() {
/* 19 */     return this.refreshedAfter.isBefore(Instant.now());
/*    */   }
/*    */ }


/* Location:              C:\Users\karol\AppData\Roaming\ModrinthApp\meta\versions\26.2-0.19.5\26.2-0.19.5.jar!\net\minecraft\world\entity\player\ProfileKeyPair.class
 * Java compiler version: 25 (69.0)
 * JD-Core Version:       1.1.3
 */