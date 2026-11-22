/*    */ package net.minecraft.network.chat;
/*    */ 
/*    */ import com.mojang.datafixers.kinds.App;
/*    */ import com.mojang.datafixers.kinds.Applicative;
/*    */ import com.mojang.serialization.Codec;
/*    */ import com.mojang.serialization.MapCodec;
/*    */ import com.mojang.serialization.codecs.RecordCodecBuilder;
/*    */ import java.io.File;
/*    */ import java.nio.file.Path;
/*    */ import java.util.function.Function;
/*    */ 
/*    */ public final class OpenFile extends Record implements ClickEvent {
/*    */   private final String path;
/*    */   
/*    */   public static final MapCodec<OpenFile> CODEC;
/*    */   
/*    */   public OpenFile(String path) {
/* 36 */     this.path = path;
/*    */   }
/*    */   
/*    */   public final String toString() {
/*    */     // Byte code:
/*    */     //   0: aload_0
/*    */     //   1: <illegal opcode> toString : (Lnet/minecraft/network/chat/ClickEvent$OpenFile;)Ljava/lang/String;
/*    */     //   6: areturn
/*    */     // Line number table:
/*    */     //   Java source line number -> byte code offset
/*    */     //   #36	-> 0
/*    */     // Local variable table:
/*    */     //   start	length	slot	name	descriptor
/*    */     //   0	7	0	this	Lnet/minecraft/network/chat/ClickEvent$OpenFile;
/*    */   }
/*    */   
/*    */   public final int hashCode() {
/*    */     // Byte code:
/*    */     //   0: aload_0
/*    */     //   1: <illegal opcode> hashCode : (Lnet/minecraft/network/chat/ClickEvent$OpenFile;)I
/*    */     //   6: ireturn
/*    */     // Line number table:
/*    */     //   Java source line number -> byte code offset
/*    */     //   #36	-> 0
/*    */     // Local variable table:
/*    */     //   start	length	slot	name	descriptor
/*    */     //   0	7	0	this	Lnet/minecraft/network/chat/ClickEvent$OpenFile;
/*    */   }
/*    */   
/*    */   public final boolean equals(Object o) {
/*    */     // Byte code:
/*    */     //   0: aload_0
/*    */     //   1: aload_1
/*    */     //   2: <illegal opcode> equals : (Lnet/minecraft/network/chat/ClickEvent$OpenFile;Ljava/lang/Object;)Z
/*    */     //   7: ireturn
/*    */     // Line number table:
/*    */     //   Java source line number -> byte code offset
/*    */     //   #36	-> 0
/*    */     // Local variable table:
/*    */     //   start	length	slot	name	descriptor
/*    */     //   0	8	0	this	Lnet/minecraft/network/chat/ClickEvent$OpenFile;
/*    */     //   0	8	1	o	Ljava/lang/Object;
/*    */   }
/*    */   
/*    */   public String path() {
/* 36 */     return this.path;
/*    */   }
/*    */   
/*    */   static {
/* 37 */     CODEC = RecordCodecBuilder.mapCodec(i -> i.group((App)Codec.STRING.fieldOf("path").forGetter(OpenFile::path)).apply((Applicative)i, OpenFile::new));
/*    */   }
/*    */   
/*    */   public OpenFile(File file) {
/* 42 */     this(file.toString());
/*    */   }
/*    */   
/*    */   public OpenFile(Path path) {
/* 47 */     this(path.toFile());
/*    */   }
/*    */   
/*    */   public File file() {
/* 51 */     return new File(this.path);
/*    */   }
/*    */   
/*    */   public ClickEvent.Action action() {
/* 56 */     return ClickEvent.Action.OPEN_FILE;
/*    */   }
/*    */ }


/* Location:              C:\Users\karol\AppData\Roaming\ModrinthApp\meta\versions\26.2-0.19.5\26.2-0.19.5.jar!\net\minecraft\network\chat\ClickEvent$OpenFile.class
 * Java compiler version: 25 (69.0)
 * JD-Core Version:       1.1.3
 */