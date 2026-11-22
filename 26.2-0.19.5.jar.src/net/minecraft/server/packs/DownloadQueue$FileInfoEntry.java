/*    */ package net.minecraft.server.packs;
/*    */ 
/*    */ import com.mojang.datafixers.kinds.App;
/*    */ import com.mojang.datafixers.kinds.Applicative;
/*    */ import com.mojang.serialization.Codec;
/*    */ import com.mojang.serialization.codecs.RecordCodecBuilder;
/*    */ import java.util.function.BiFunction;
/*    */ 
/*    */ final class FileInfoEntry extends Record {
/*    */   private final String name;
/*    */   
/*    */   private final long size;
/*    */   
/*    */   public static final Codec<FileInfoEntry> CODEC;
/*    */   
/*    */   private FileInfoEntry(String name, long size) {
/* 38 */     this.name = name;
/* 38 */     this.size = size;
/*    */   }
/*    */   
/*    */   public final String toString() {
/*    */     // Byte code:
/*    */     //   0: aload_0
/*    */     //   1: <illegal opcode> toString : (Lnet/minecraft/server/packs/DownloadQueue$FileInfoEntry;)Ljava/lang/String;
/*    */     //   6: areturn
/*    */     // Line number table:
/*    */     //   Java source line number -> byte code offset
/*    */     //   #38	-> 0
/*    */     // Local variable table:
/*    */     //   start	length	slot	name	descriptor
/*    */     //   0	7	0	this	Lnet/minecraft/server/packs/DownloadQueue$FileInfoEntry;
/*    */   }
/*    */   
/*    */   public final int hashCode() {
/*    */     // Byte code:
/*    */     //   0: aload_0
/*    */     //   1: <illegal opcode> hashCode : (Lnet/minecraft/server/packs/DownloadQueue$FileInfoEntry;)I
/*    */     //   6: ireturn
/*    */     // Line number table:
/*    */     //   Java source line number -> byte code offset
/*    */     //   #38	-> 0
/*    */     // Local variable table:
/*    */     //   start	length	slot	name	descriptor
/*    */     //   0	7	0	this	Lnet/minecraft/server/packs/DownloadQueue$FileInfoEntry;
/*    */   }
/*    */   
/*    */   public final boolean equals(Object o) {
/*    */     // Byte code:
/*    */     //   0: aload_0
/*    */     //   1: aload_1
/*    */     //   2: <illegal opcode> equals : (Lnet/minecraft/server/packs/DownloadQueue$FileInfoEntry;Ljava/lang/Object;)Z
/*    */     //   7: ireturn
/*    */     // Line number table:
/*    */     //   Java source line number -> byte code offset
/*    */     //   #38	-> 0
/*    */     // Local variable table:
/*    */     //   start	length	slot	name	descriptor
/*    */     //   0	8	0	this	Lnet/minecraft/server/packs/DownloadQueue$FileInfoEntry;
/*    */     //   0	8	1	o	Ljava/lang/Object;
/*    */   }
/*    */   
/*    */   public String name() {
/* 38 */     return this.name;
/*    */   }
/*    */   
/*    */   public long size() {
/* 38 */     return this.size;
/*    */   }
/*    */   
/*    */   static {
/* 42 */     CODEC = RecordCodecBuilder.create(i -> i.group((App)Codec.STRING.fieldOf("name").forGetter(FileInfoEntry::name), (App)Codec.LONG.fieldOf("size").forGetter(FileInfoEntry::size)).apply((Applicative)i, FileInfoEntry::new));
/*    */   }
/*    */ }


/* Location:              C:\Users\karol\AppData\Roaming\ModrinthApp\meta\versions\26.2-0.19.5\26.2-0.19.5.jar!\net\minecraft\server\packs\DownloadQueue$FileInfoEntry.class
 * Java compiler version: 25 (69.0)
 * JD-Core Version:       1.1.3
 */