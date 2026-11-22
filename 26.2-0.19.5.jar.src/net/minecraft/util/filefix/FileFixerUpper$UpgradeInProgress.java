/*     */ package net.minecraft.util.filefix;
/*     */ 
/*     */ import com.mojang.datafixers.kinds.App;
/*     */ import com.mojang.datafixers.kinds.Applicative;
/*     */ import com.mojang.serialization.Codec;
/*     */ import com.mojang.serialization.codecs.RecordCodecBuilder;
/*     */ import java.nio.file.Path;
/*     */ import java.util.List;
/*     */ import java.util.function.Function;
/*     */ import net.minecraft.util.filefix.virtualfilesystem.FileMove;
/*     */ 
/*     */ public final class UpgradeInProgress extends Record {
/*     */   private final List<FileMove> moves;
/*     */   
/*     */   public UpgradeInProgress(List<FileMove> moves) {
/* 356 */     this.moves = moves;
/*     */   }
/*     */   
/*     */   public final String toString() {
/*     */     // Byte code:
/*     */     //   0: aload_0
/*     */     //   1: <illegal opcode> toString : (Lnet/minecraft/util/filefix/FileFixerUpper$UpgradeInProgress;)Ljava/lang/String;
/*     */     //   6: areturn
/*     */     // Line number table:
/*     */     //   Java source line number -> byte code offset
/*     */     //   #356	-> 0
/*     */     // Local variable table:
/*     */     //   start	length	slot	name	descriptor
/*     */     //   0	7	0	this	Lnet/minecraft/util/filefix/FileFixerUpper$UpgradeInProgress;
/*     */   }
/*     */   
/*     */   public final int hashCode() {
/*     */     // Byte code:
/*     */     //   0: aload_0
/*     */     //   1: <illegal opcode> hashCode : (Lnet/minecraft/util/filefix/FileFixerUpper$UpgradeInProgress;)I
/*     */     //   6: ireturn
/*     */     // Line number table:
/*     */     //   Java source line number -> byte code offset
/*     */     //   #356	-> 0
/*     */     // Local variable table:
/*     */     //   start	length	slot	name	descriptor
/*     */     //   0	7	0	this	Lnet/minecraft/util/filefix/FileFixerUpper$UpgradeInProgress;
/*     */   }
/*     */   
/*     */   public final boolean equals(Object o) {
/*     */     // Byte code:
/*     */     //   0: aload_0
/*     */     //   1: aload_1
/*     */     //   2: <illegal opcode> equals : (Lnet/minecraft/util/filefix/FileFixerUpper$UpgradeInProgress;Ljava/lang/Object;)Z
/*     */     //   7: ireturn
/*     */     // Line number table:
/*     */     //   Java source line number -> byte code offset
/*     */     //   #356	-> 0
/*     */     // Local variable table:
/*     */     //   start	length	slot	name	descriptor
/*     */     //   0	8	0	this	Lnet/minecraft/util/filefix/FileFixerUpper$UpgradeInProgress;
/*     */     //   0	8	1	o	Ljava/lang/Object;
/*     */   }
/*     */   
/*     */   public List<FileMove> moves() {
/* 356 */     return this.moves;
/*     */   }
/*     */   
/*     */   public static Codec<UpgradeInProgress> codec(Path fromDirectory, Path toDirectory) {
/* 360 */     return RecordCodecBuilder.create(i -> i.group((App)FileMove.moveCodec(fromDirectory, toDirectory).listOf().fieldOf("moves").forGetter(UpgradeInProgress::moves)).apply((Applicative)i, UpgradeInProgress::new));
/*     */   }
/*     */ }


/* Location:              C:\Users\karol\AppData\Roaming\ModrinthApp\meta\versions\26.2-0.19.5\26.2-0.19.5.jar!\net\minecraf\\util\filefix\FileFixerUpper$UpgradeInProgress.class
 * Java compiler version: 25 (69.0)
 * JD-Core Version:       1.1.3
 */