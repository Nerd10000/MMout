/*   */ package net.minecraft.core;
/*   */ 
/*   */ import com.mojang.serialization.Lifecycle;
/*   */ import java.util.Optional;
/*   */ import net.minecraft.server.packs.repository.KnownPack;
/*   */ 
/*   */ public final class RegistrationInfo extends Record {
/*   */   private final Optional<KnownPack> knownPackInfo;
/*   */   
/*   */   private final Lifecycle lifecycle;
/*   */   
/*   */   public RegistrationInfo(Optional<KnownPack> knownPackInfo, Lifecycle lifecycle) {
/* 8 */     this.knownPackInfo = knownPackInfo;
/* 8 */     this.lifecycle = lifecycle;
/*   */   }
/*   */   
/*   */   public final String toString() {
/*   */     // Byte code:
/*   */     //   0: aload_0
/*   */     //   1: <illegal opcode> toString : (Lnet/minecraft/core/RegistrationInfo;)Ljava/lang/String;
/*   */     //   6: areturn
/*   */     // Line number table:
/*   */     //   Java source line number -> byte code offset
/*   */     //   #8	-> 0
/*   */     // Local variable table:
/*   */     //   start	length	slot	name	descriptor
/*   */     //   0	7	0	this	Lnet/minecraft/core/RegistrationInfo;
/*   */   }
/*   */   
/*   */   public final int hashCode() {
/*   */     // Byte code:
/*   */     //   0: aload_0
/*   */     //   1: <illegal opcode> hashCode : (Lnet/minecraft/core/RegistrationInfo;)I
/*   */     //   6: ireturn
/*   */     // Line number table:
/*   */     //   Java source line number -> byte code offset
/*   */     //   #8	-> 0
/*   */     // Local variable table:
/*   */     //   start	length	slot	name	descriptor
/*   */     //   0	7	0	this	Lnet/minecraft/core/RegistrationInfo;
/*   */   }
/*   */   
/*   */   public final boolean equals(Object o) {
/*   */     // Byte code:
/*   */     //   0: aload_0
/*   */     //   1: aload_1
/*   */     //   2: <illegal opcode> equals : (Lnet/minecraft/core/RegistrationInfo;Ljava/lang/Object;)Z
/*   */     //   7: ireturn
/*   */     // Line number table:
/*   */     //   Java source line number -> byte code offset
/*   */     //   #8	-> 0
/*   */     // Local variable table:
/*   */     //   start	length	slot	name	descriptor
/*   */     //   0	8	0	this	Lnet/minecraft/core/RegistrationInfo;
/*   */     //   0	8	1	o	Ljava/lang/Object;
/*   */   }
/*   */   
/*   */   public Optional<KnownPack> knownPackInfo() {
/* 8 */     return this.knownPackInfo;
/*   */   }
/*   */   
/*   */   public Lifecycle lifecycle() {
/* 8 */     return this.lifecycle;
/*   */   }
/*   */   
/* 9 */   public static final RegistrationInfo BUILT_IN = new RegistrationInfo(Optional.empty(), Lifecycle.stable());
/*   */ }


/* Location:              C:\Users\karol\AppData\Roaming\ModrinthApp\meta\versions\26.2-0.19.5\26.2-0.19.5.jar!\net\minecraft\core\RegistrationInfo.class
 * Java compiler version: 25 (69.0)
 * JD-Core Version:       1.1.3
 */