/*    */ package net.minecraft.util.filefix.virtualfilesystem;
/*    */ 
/*    */ import com.mojang.datafixers.kinds.App;
/*    */ import com.mojang.datafixers.kinds.Applicative;
/*    */ import com.mojang.serialization.Codec;
/*    */ import com.mojang.serialization.codecs.RecordCodecBuilder;
/*    */ import java.nio.file.Path;
/*    */ import java.util.function.BiFunction;
/*    */ import net.minecraft.util.ExtraCodecs;
/*    */ 
/*    */ public final class FileMove extends Record {
/*    */   private final Path from;
/*    */   
/*    */   private final Path to;
/*    */   
/*    */   public FileMove(Path from, Path to) {
/*  9 */     this.from = from;
/*  9 */     this.to = to;
/*    */   }
/*    */   
/*    */   public final String toString() {
/*    */     // Byte code:
/*    */     //   0: aload_0
/*    */     //   1: <illegal opcode> toString : (Lnet/minecraft/util/filefix/virtualfilesystem/FileMove;)Ljava/lang/String;
/*    */     //   6: areturn
/*    */     // Line number table:
/*    */     //   Java source line number -> byte code offset
/*    */     //   #9	-> 0
/*    */     // Local variable table:
/*    */     //   start	length	slot	name	descriptor
/*    */     //   0	7	0	this	Lnet/minecraft/util/filefix/virtualfilesystem/FileMove;
/*    */   }
/*    */   
/*    */   public final int hashCode() {
/*    */     // Byte code:
/*    */     //   0: aload_0
/*    */     //   1: <illegal opcode> hashCode : (Lnet/minecraft/util/filefix/virtualfilesystem/FileMove;)I
/*    */     //   6: ireturn
/*    */     // Line number table:
/*    */     //   Java source line number -> byte code offset
/*    */     //   #9	-> 0
/*    */     // Local variable table:
/*    */     //   start	length	slot	name	descriptor
/*    */     //   0	7	0	this	Lnet/minecraft/util/filefix/virtualfilesystem/FileMove;
/*    */   }
/*    */   
/*    */   public final boolean equals(Object o) {
/*    */     // Byte code:
/*    */     //   0: aload_0
/*    */     //   1: aload_1
/*    */     //   2: <illegal opcode> equals : (Lnet/minecraft/util/filefix/virtualfilesystem/FileMove;Ljava/lang/Object;)Z
/*    */     //   7: ireturn
/*    */     // Line number table:
/*    */     //   Java source line number -> byte code offset
/*    */     //   #9	-> 0
/*    */     // Local variable table:
/*    */     //   start	length	slot	name	descriptor
/*    */     //   0	8	0	this	Lnet/minecraft/util/filefix/virtualfilesystem/FileMove;
/*    */     //   0	8	1	o	Ljava/lang/Object;
/*    */   }
/*    */   
/*    */   public Path from() {
/*  9 */     return this.from;
/*    */   }
/*    */   
/*    */   public Path to() {
/*  9 */     return this.to;
/*    */   }
/*    */   
/*    */   public static Codec<FileMove> moveCodec(Path fromDirectory, Path toDirectory) {
/* 14 */     return RecordCodecBuilder.create(i -> i.group((App)ExtraCodecs.guardedPathCodec(fromDirectory).fieldOf("from").forGetter(()), (App)ExtraCodecs.guardedPathCodec(toDirectory).fieldOf("to").forGetter(())).apply((Applicative)i, FileMove::new));
/*    */   }
/*    */ }


/* Location:              C:\Users\karol\AppData\Roaming\ModrinthApp\meta\versions\26.2-0.19.5\26.2-0.19.5.jar!\net\minecraf\\util\filefix\virtualfilesystem\FileMove.class
 * Java compiler version: 25 (69.0)
 * JD-Core Version:       1.1.3
 */