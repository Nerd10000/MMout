/*    */ package net.minecraft.server.packs.repository;
/*    */ 
/*    */ import com.mojang.datafixers.util.Function3;
/*    */ import io.netty.buffer.ByteBuf;
/*    */ import net.minecraft.SharedConstants;
/*    */ import net.minecraft.network.codec.ByteBufCodecs;
/*    */ import net.minecraft.network.codec.StreamCodec;
/*    */ 
/*    */ public final class KnownPack extends Record {
/*    */   private final String namespace;
/*    */   
/*    */   private final String id;
/*    */   
/*    */   private final String version;
/*    */   
/*    */   public KnownPack(String namespace, String id, String version) {
/*  9 */     this.namespace = namespace;
/*  9 */     this.id = id;
/*  9 */     this.version = version;
/*    */   }
/*    */   
/*    */   public final int hashCode() {
/*    */     // Byte code:
/*    */     //   0: aload_0
/*    */     //   1: <illegal opcode> hashCode : (Lnet/minecraft/server/packs/repository/KnownPack;)I
/*    */     //   6: ireturn
/*    */     // Line number table:
/*    */     //   Java source line number -> byte code offset
/*    */     //   #9	-> 0
/*    */     // Local variable table:
/*    */     //   start	length	slot	name	descriptor
/*    */     //   0	7	0	this	Lnet/minecraft/server/packs/repository/KnownPack;
/*    */   }
/*    */   
/*    */   public final boolean equals(Object o) {
/*    */     // Byte code:
/*    */     //   0: aload_0
/*    */     //   1: aload_1
/*    */     //   2: <illegal opcode> equals : (Lnet/minecraft/server/packs/repository/KnownPack;Ljava/lang/Object;)Z
/*    */     //   7: ireturn
/*    */     // Line number table:
/*    */     //   Java source line number -> byte code offset
/*    */     //   #9	-> 0
/*    */     // Local variable table:
/*    */     //   start	length	slot	name	descriptor
/*    */     //   0	8	0	this	Lnet/minecraft/server/packs/repository/KnownPack;
/*    */     //   0	8	1	o	Ljava/lang/Object;
/*    */   }
/*    */   
/*    */   public String namespace() {
/*  9 */     return this.namespace;
/*    */   }
/*    */   
/*    */   public String id() {
/*  9 */     return this.id;
/*    */   }
/*    */   
/*    */   public String version() {
/*  9 */     return this.version;
/*    */   }
/*    */   
/* 10 */   public static final StreamCodec<ByteBuf, KnownPack> STREAM_CODEC = StreamCodec.composite(ByteBufCodecs.STRING_UTF8, KnownPack::namespace, ByteBufCodecs.STRING_UTF8, KnownPack::id, ByteBufCodecs.STRING_UTF8, KnownPack::version, KnownPack::new);
/*    */   
/*    */   public static final String VANILLA_NAMESPACE = "minecraft";
/*    */   
/*    */   public static KnownPack vanilla(String id) {
/* 20 */     return new KnownPack("minecraft", id, SharedConstants.getCurrentVersion().id());
/*    */   }
/*    */   
/*    */   public boolean isVanilla() {
/* 24 */     return this.namespace.equals("minecraft");
/*    */   }
/*    */   
/*    */   public String toString() {
/* 29 */     return this.namespace + ":" + this.namespace + ":" + this.id;
/*    */   }
/*    */ }


/* Location:              C:\Users\karol\AppData\Roaming\ModrinthApp\meta\versions\26.2-0.19.5\26.2-0.19.5.jar!\net\minecraft\server\packs\repository\KnownPack.class
 * Java compiler version: 25 (69.0)
 * JD-Core Version:       1.1.3
 */