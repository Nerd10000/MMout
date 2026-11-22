/*    */ package net.minecraft.core;
/*    */ 
/*    */ import com.mojang.serialization.Codec;
/*    */ import com.mojang.serialization.DataResult;
/*    */ import io.netty.buffer.ByteBuf;
/*    */ import java.util.List;
/*    */ import net.minecraft.network.codec.StreamCodec;
/*    */ import net.minecraft.util.Util;
/*    */ 
/*    */ public final class Rotations extends Record {
/*    */   private final float x;
/*    */   
/*    */   private final float y;
/*    */   
/*    */   private final float z;
/*    */   
/*    */   public static final Codec<Rotations> CODEC;
/*    */   
/*    */   public final String toString() {
/*    */     // Byte code:
/*    */     //   0: aload_0
/*    */     //   1: <illegal opcode> toString : (Lnet/minecraft/core/Rotations;)Ljava/lang/String;
/*    */     //   6: areturn
/*    */     // Line number table:
/*    */     //   Java source line number -> byte code offset
/*    */     //   #11	-> 0
/*    */     // Local variable table:
/*    */     //   start	length	slot	name	descriptor
/*    */     //   0	7	0	this	Lnet/minecraft/core/Rotations;
/*    */   }
/*    */   
/*    */   public final int hashCode() {
/*    */     // Byte code:
/*    */     //   0: aload_0
/*    */     //   1: <illegal opcode> hashCode : (Lnet/minecraft/core/Rotations;)I
/*    */     //   6: ireturn
/*    */     // Line number table:
/*    */     //   Java source line number -> byte code offset
/*    */     //   #11	-> 0
/*    */     // Local variable table:
/*    */     //   start	length	slot	name	descriptor
/*    */     //   0	7	0	this	Lnet/minecraft/core/Rotations;
/*    */   }
/*    */   
/*    */   public final boolean equals(Object o) {
/*    */     // Byte code:
/*    */     //   0: aload_0
/*    */     //   1: aload_1
/*    */     //   2: <illegal opcode> equals : (Lnet/minecraft/core/Rotations;Ljava/lang/Object;)Z
/*    */     //   7: ireturn
/*    */     // Line number table:
/*    */     //   Java source line number -> byte code offset
/*    */     //   #11	-> 0
/*    */     // Local variable table:
/*    */     //   start	length	slot	name	descriptor
/*    */     //   0	8	0	this	Lnet/minecraft/core/Rotations;
/*    */     //   0	8	1	o	Ljava/lang/Object;
/*    */   }
/*    */   
/*    */   public float x() {
/* 11 */     return this.x;
/*    */   }
/*    */   
/*    */   public float y() {
/* 11 */     return this.y;
/*    */   }
/*    */   
/*    */   public float z() {
/* 11 */     return this.z;
/*    */   }
/*    */   
/*    */   static {
/* 16 */     CODEC = Codec.FLOAT.listOf().comapFlatMap(input -> Util.fixedSize(input, 3).map(()), rotations -> List.of(Float.valueOf(rotations.x()), Float.valueOf(rotations.y()), Float.valueOf(rotations.z())));
/*    */   }
/*    */   
/* 21 */   public static final StreamCodec<ByteBuf, Rotations> STREAM_CODEC = new StreamCodec<ByteBuf, Rotations>() {
/*    */       public Rotations decode(ByteBuf input) {
/* 24 */         return new Rotations(input
/* 25 */             .readFloat(), input
/* 26 */             .readFloat(), input
/* 27 */             .readFloat());
/*    */       }
/*    */       
/*    */       public void encode(ByteBuf output, Rotations value) {
/* 33 */         output.writeFloat(value.x);
/* 34 */         output.writeFloat(value.y);
/* 35 */         output.writeFloat(value.z);
/*    */       }
/*    */     };
/*    */   
/*    */   public Rotations(float x, float y, float z) {
/* 40 */     x = (Float.isInfinite(x) || Float.isNaN(x)) ? 0.0F : (x % 360.0F);
/* 41 */     y = (Float.isInfinite(y) || Float.isNaN(y)) ? 0.0F : (y % 360.0F);
/* 42 */     z = (Float.isInfinite(z) || Float.isNaN(z)) ? 0.0F : (z % 360.0F);
/*    */     this.x = x;
/*    */     this.y = y;
/*    */     this.z = z;
/*    */   }
/*    */ }


/* Location:              C:\Users\karol\AppData\Roaming\ModrinthApp\meta\versions\26.2-0.19.5\26.2-0.19.5.jar!\net\minecraft\core\Rotations.class
 * Java compiler version: 25 (69.0)
 * JD-Core Version:       1.1.3
 */