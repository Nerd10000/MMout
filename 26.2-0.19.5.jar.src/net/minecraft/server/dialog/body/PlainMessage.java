/*    */ package net.minecraft.server.dialog.body;
/*    */ 
/*    */ import com.mojang.datafixers.kinds.App;
/*    */ import com.mojang.datafixers.kinds.Applicative;
/*    */ import com.mojang.serialization.Codec;
/*    */ import com.mojang.serialization.MapCodec;
/*    */ import com.mojang.serialization.codecs.RecordCodecBuilder;
/*    */ import java.util.function.BiFunction;
/*    */ import net.minecraft.network.chat.Component;
/*    */ import net.minecraft.network.chat.ComponentSerialization;
/*    */ import net.minecraft.server.dialog.Dialog;
/*    */ 
/*    */ public final class PlainMessage extends Record implements DialogBody {
/*    */   private final Component contents;
/*    */   
/*    */   private final int width;
/*    */   
/*    */   public static final int DEFAULT_WIDTH = 200;
/*    */   
/*    */   public static final MapCodec<PlainMessage> MAP_CODEC;
/*    */   
/*    */   public static final Codec<PlainMessage> CODEC;
/*    */   
/*    */   public PlainMessage(Component contents, int width) {
/* 10 */     this.contents = contents;
/* 10 */     this.width = width;
/*    */   }
/*    */   
/*    */   public final String toString() {
/*    */     // Byte code:
/*    */     //   0: aload_0
/*    */     //   1: <illegal opcode> toString : (Lnet/minecraft/server/dialog/body/PlainMessage;)Ljava/lang/String;
/*    */     //   6: areturn
/*    */     // Line number table:
/*    */     //   Java source line number -> byte code offset
/*    */     //   #10	-> 0
/*    */     // Local variable table:
/*    */     //   start	length	slot	name	descriptor
/*    */     //   0	7	0	this	Lnet/minecraft/server/dialog/body/PlainMessage;
/*    */   }
/*    */   
/*    */   public final int hashCode() {
/*    */     // Byte code:
/*    */     //   0: aload_0
/*    */     //   1: <illegal opcode> hashCode : (Lnet/minecraft/server/dialog/body/PlainMessage;)I
/*    */     //   6: ireturn
/*    */     // Line number table:
/*    */     //   Java source line number -> byte code offset
/*    */     //   #10	-> 0
/*    */     // Local variable table:
/*    */     //   start	length	slot	name	descriptor
/*    */     //   0	7	0	this	Lnet/minecraft/server/dialog/body/PlainMessage;
/*    */   }
/*    */   
/*    */   public final boolean equals(Object o) {
/*    */     // Byte code:
/*    */     //   0: aload_0
/*    */     //   1: aload_1
/*    */     //   2: <illegal opcode> equals : (Lnet/minecraft/server/dialog/body/PlainMessage;Ljava/lang/Object;)Z
/*    */     //   7: ireturn
/*    */     // Line number table:
/*    */     //   Java source line number -> byte code offset
/*    */     //   #10	-> 0
/*    */     // Local variable table:
/*    */     //   start	length	slot	name	descriptor
/*    */     //   0	8	0	this	Lnet/minecraft/server/dialog/body/PlainMessage;
/*    */     //   0	8	1	o	Ljava/lang/Object;
/*    */   }
/*    */   
/*    */   public Component contents() {
/* 10 */     return this.contents;
/*    */   }
/*    */   
/*    */   public int width() {
/* 10 */     return this.width;
/*    */   }
/*    */   
/*    */   static {
/* 16 */     MAP_CODEC = RecordCodecBuilder.mapCodec(i -> i.group((App)ComponentSerialization.CODEC.fieldOf("contents").forGetter(PlainMessage::contents), (App)Dialog.WIDTH_CODEC.optionalFieldOf("width", Integer.valueOf(200)).forGetter(PlainMessage::width)).apply((Applicative)i, PlainMessage::new));
/* 21 */     CODEC = Codec.withAlternative(MAP_CODEC
/* 22 */         .codec(), ComponentSerialization.CODEC, contents -> new PlainMessage(contents, 200));
/*    */   }
/*    */   
/*    */   public MapCodec<PlainMessage> mapCodec() {
/* 28 */     return MAP_CODEC;
/*    */   }
/*    */ }


/* Location:              C:\Users\karol\AppData\Roaming\ModrinthApp\meta\versions\26.2-0.19.5\26.2-0.19.5.jar!\net\minecraft\server\dialog\body\PlainMessage.class
 * Java compiler version: 25 (69.0)
 * JD-Core Version:       1.1.3
 */