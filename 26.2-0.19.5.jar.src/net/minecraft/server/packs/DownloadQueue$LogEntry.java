/*    */ package net.minecraft.server.packs;
/*    */ 
/*    */ import com.mojang.datafixers.kinds.App;
/*    */ import com.mojang.datafixers.kinds.Applicative;
/*    */ import com.mojang.datafixers.util.Either;
/*    */ import com.mojang.datafixers.util.Function5;
/*    */ import com.mojang.serialization.Codec;
/*    */ import com.mojang.serialization.codecs.RecordCodecBuilder;
/*    */ import java.time.Instant;
/*    */ import java.util.Optional;
/*    */ import java.util.UUID;
/*    */ import net.minecraft.core.UUIDUtil;
/*    */ import net.minecraft.util.ExtraCodecs;
/*    */ 
/*    */ final class LogEntry extends Record {
/*    */   private final UUID id;
/*    */   
/*    */   private final String url;
/*    */   
/*    */   private final Instant time;
/*    */   
/*    */   private final Optional<String> hash;
/*    */   
/*    */   private final Either<String, DownloadQueue.FileInfoEntry> errorOrFileInfo;
/*    */   
/*    */   public static final Codec<LogEntry> CODEC;
/*    */   
/*    */   private LogEntry(UUID id, String url, Instant time, Optional<String> hash, Either<String, DownloadQueue.FileInfoEntry> errorOrFileInfo) {
/* 48 */     this.id = id;
/* 48 */     this.url = url;
/* 48 */     this.time = time;
/* 48 */     this.hash = hash;
/* 48 */     this.errorOrFileInfo = errorOrFileInfo;
/*    */   }
/*    */   
/*    */   public final String toString() {
/*    */     // Byte code:
/*    */     //   0: aload_0
/*    */     //   1: <illegal opcode> toString : (Lnet/minecraft/server/packs/DownloadQueue$LogEntry;)Ljava/lang/String;
/*    */     //   6: areturn
/*    */     // Line number table:
/*    */     //   Java source line number -> byte code offset
/*    */     //   #48	-> 0
/*    */     // Local variable table:
/*    */     //   start	length	slot	name	descriptor
/*    */     //   0	7	0	this	Lnet/minecraft/server/packs/DownloadQueue$LogEntry;
/*    */   }
/*    */   
/*    */   public final int hashCode() {
/*    */     // Byte code:
/*    */     //   0: aload_0
/*    */     //   1: <illegal opcode> hashCode : (Lnet/minecraft/server/packs/DownloadQueue$LogEntry;)I
/*    */     //   6: ireturn
/*    */     // Line number table:
/*    */     //   Java source line number -> byte code offset
/*    */     //   #48	-> 0
/*    */     // Local variable table:
/*    */     //   start	length	slot	name	descriptor
/*    */     //   0	7	0	this	Lnet/minecraft/server/packs/DownloadQueue$LogEntry;
/*    */   }
/*    */   
/*    */   public final boolean equals(Object o) {
/*    */     // Byte code:
/*    */     //   0: aload_0
/*    */     //   1: aload_1
/*    */     //   2: <illegal opcode> equals : (Lnet/minecraft/server/packs/DownloadQueue$LogEntry;Ljava/lang/Object;)Z
/*    */     //   7: ireturn
/*    */     // Line number table:
/*    */     //   Java source line number -> byte code offset
/*    */     //   #48	-> 0
/*    */     // Local variable table:
/*    */     //   start	length	slot	name	descriptor
/*    */     //   0	8	0	this	Lnet/minecraft/server/packs/DownloadQueue$LogEntry;
/*    */     //   0	8	1	o	Ljava/lang/Object;
/*    */   }
/*    */   
/*    */   public UUID id() {
/* 48 */     return this.id;
/*    */   }
/*    */   
/*    */   public String url() {
/* 48 */     return this.url;
/*    */   }
/*    */   
/*    */   public Instant time() {
/* 48 */     return this.time;
/*    */   }
/*    */   
/*    */   public Optional<String> hash() {
/* 48 */     return this.hash;
/*    */   }
/*    */   
/*    */   public Either<String, DownloadQueue.FileInfoEntry> errorOrFileInfo() {
/* 48 */     return this.errorOrFileInfo;
/*    */   }
/*    */   
/*    */   static {
/* 55 */     CODEC = RecordCodecBuilder.create(i -> i.group((App)UUIDUtil.STRING_CODEC.fieldOf("id").forGetter(LogEntry::id), (App)Codec.STRING.fieldOf("url").forGetter(LogEntry::url), (App)ExtraCodecs.INSTANT_ISO8601.fieldOf("time").forGetter(LogEntry::time), (App)Codec.STRING.optionalFieldOf("hash").forGetter(LogEntry::hash), (App)Codec.mapEither(Codec.STRING.fieldOf("error"), DownloadQueue.FileInfoEntry.CODEC.fieldOf("file")).forGetter(LogEntry::errorOrFileInfo)).apply((Applicative)i, LogEntry::new));
/*    */   }
/*    */ }


/* Location:              C:\Users\karol\AppData\Roaming\ModrinthApp\meta\versions\26.2-0.19.5\26.2-0.19.5.jar!\net\minecraft\server\packs\DownloadQueue$LogEntry.class
 * Java compiler version: 25 (69.0)
 * JD-Core Version:       1.1.3
 */