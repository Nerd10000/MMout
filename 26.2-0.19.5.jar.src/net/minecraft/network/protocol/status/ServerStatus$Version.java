/*    */ package net.minecraft.network.protocol.status;
/*    */ 
/*    */ import com.mojang.datafixers.kinds.App;
/*    */ import com.mojang.datafixers.kinds.Applicative;
/*    */ import com.mojang.serialization.Codec;
/*    */ import com.mojang.serialization.codecs.RecordCodecBuilder;
/*    */ import java.util.function.BiFunction;
/*    */ import net.minecraft.SharedConstants;
/*    */ import net.minecraft.WorldVersion;
/*    */ 
/*    */ public final class Version extends Record {
/*    */   private final String name;
/*    */   
/*    */   private final int protocol;
/*    */   
/*    */   public static final Codec<Version> CODEC;
/*    */   
/*    */   public Version(String name, int protocol) {
/* 36 */     this.name = name;
/* 36 */     this.protocol = protocol;
/*    */   }
/*    */   
/*    */   public final String toString() {
/*    */     // Byte code:
/*    */     //   0: aload_0
/*    */     //   1: <illegal opcode> toString : (Lnet/minecraft/network/protocol/status/ServerStatus$Version;)Ljava/lang/String;
/*    */     //   6: areturn
/*    */     // Line number table:
/*    */     //   Java source line number -> byte code offset
/*    */     //   #36	-> 0
/*    */     // Local variable table:
/*    */     //   start	length	slot	name	descriptor
/*    */     //   0	7	0	this	Lnet/minecraft/network/protocol/status/ServerStatus$Version;
/*    */   }
/*    */   
/*    */   public final int hashCode() {
/*    */     // Byte code:
/*    */     //   0: aload_0
/*    */     //   1: <illegal opcode> hashCode : (Lnet/minecraft/network/protocol/status/ServerStatus$Version;)I
/*    */     //   6: ireturn
/*    */     // Line number table:
/*    */     //   Java source line number -> byte code offset
/*    */     //   #36	-> 0
/*    */     // Local variable table:
/*    */     //   start	length	slot	name	descriptor
/*    */     //   0	7	0	this	Lnet/minecraft/network/protocol/status/ServerStatus$Version;
/*    */   }
/*    */   
/*    */   public final boolean equals(Object o) {
/*    */     // Byte code:
/*    */     //   0: aload_0
/*    */     //   1: aload_1
/*    */     //   2: <illegal opcode> equals : (Lnet/minecraft/network/protocol/status/ServerStatus$Version;Ljava/lang/Object;)Z
/*    */     //   7: ireturn
/*    */     // Line number table:
/*    */     //   Java source line number -> byte code offset
/*    */     //   #36	-> 0
/*    */     // Local variable table:
/*    */     //   start	length	slot	name	descriptor
/*    */     //   0	8	0	this	Lnet/minecraft/network/protocol/status/ServerStatus$Version;
/*    */     //   0	8	1	o	Ljava/lang/Object;
/*    */   }
/*    */   
/*    */   public String name() {
/* 36 */     return this.name;
/*    */   }
/*    */   
/*    */   public int protocol() {
/* 36 */     return this.protocol;
/*    */   }
/*    */   
/*    */   static {
/* 37 */     CODEC = RecordCodecBuilder.create(i -> i.group((App)Codec.STRING.fieldOf("name").forGetter(Version::name), (App)Codec.INT.fieldOf("protocol").forGetter(Version::protocol)).apply((Applicative)i, Version::new));
/*    */   }
/*    */   
/*    */   public static Version current() {
/* 43 */     WorldVersion version = SharedConstants.getCurrentVersion();
/* 44 */     return new Version(version.name(), version.protocolVersion());
/*    */   }
/*    */ }


/* Location:              C:\Users\karol\AppData\Roaming\ModrinthApp\meta\versions\26.2-0.19.5\26.2-0.19.5.jar!\net\minecraft\network\protocol\status\ServerStatus$Version.class
 * Java compiler version: 25 (69.0)
 * JD-Core Version:       1.1.3
 */