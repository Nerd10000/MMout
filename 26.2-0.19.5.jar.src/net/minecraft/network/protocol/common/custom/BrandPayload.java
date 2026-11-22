/*    */ package net.minecraft.network.protocol.common.custom;
/*    */ 
/*    */ import net.minecraft.network.FriendlyByteBuf;
/*    */ import net.minecraft.network.codec.StreamCodec;
/*    */ import net.minecraft.network.codec.StreamDecoder;
/*    */ 
/*    */ public final class BrandPayload extends Record implements CustomPacketPayload {
/*    */   private final String brand;
/*    */   
/*    */   public BrandPayload(String brand) {
/*  6 */     this.brand = brand;
/*    */   }
/*    */   
/*    */   public final String toString() {
/*    */     // Byte code:
/*    */     //   0: aload_0
/*    */     //   1: <illegal opcode> toString : (Lnet/minecraft/network/protocol/common/custom/BrandPayload;)Ljava/lang/String;
/*    */     //   6: areturn
/*    */     // Line number table:
/*    */     //   Java source line number -> byte code offset
/*    */     //   #6	-> 0
/*    */     // Local variable table:
/*    */     //   start	length	slot	name	descriptor
/*    */     //   0	7	0	this	Lnet/minecraft/network/protocol/common/custom/BrandPayload;
/*    */   }
/*    */   
/*    */   public final int hashCode() {
/*    */     // Byte code:
/*    */     //   0: aload_0
/*    */     //   1: <illegal opcode> hashCode : (Lnet/minecraft/network/protocol/common/custom/BrandPayload;)I
/*    */     //   6: ireturn
/*    */     // Line number table:
/*    */     //   Java source line number -> byte code offset
/*    */     //   #6	-> 0
/*    */     // Local variable table:
/*    */     //   start	length	slot	name	descriptor
/*    */     //   0	7	0	this	Lnet/minecraft/network/protocol/common/custom/BrandPayload;
/*    */   }
/*    */   
/*    */   public final boolean equals(Object o) {
/*    */     // Byte code:
/*    */     //   0: aload_0
/*    */     //   1: aload_1
/*    */     //   2: <illegal opcode> equals : (Lnet/minecraft/network/protocol/common/custom/BrandPayload;Ljava/lang/Object;)Z
/*    */     //   7: ireturn
/*    */     // Line number table:
/*    */     //   Java source line number -> byte code offset
/*    */     //   #6	-> 0
/*    */     // Local variable table:
/*    */     //   start	length	slot	name	descriptor
/*    */     //   0	8	0	this	Lnet/minecraft/network/protocol/common/custom/BrandPayload;
/*    */     //   0	8	1	o	Ljava/lang/Object;
/*    */   }
/*    */   
/*    */   public String brand() {
/*  6 */     return this.brand;
/*    */   }
/*    */   
/*  7 */   public static final StreamCodec<FriendlyByteBuf, BrandPayload> STREAM_CODEC = CustomPacketPayload.codec(BrandPayload::write, BrandPayload::new);
/*    */   
/*  8 */   public static final CustomPacketPayload.Type<BrandPayload> TYPE = CustomPacketPayload.createType("brand");
/*    */   
/*    */   private BrandPayload(FriendlyByteBuf input) {
/* 11 */     this(input.readUtf());
/*    */   }
/*    */   
/*    */   private void write(FriendlyByteBuf output) {
/* 15 */     output.writeUtf(this.brand);
/*    */   }
/*    */   
/*    */   public CustomPacketPayload.Type<BrandPayload> type() {
/* 20 */     return TYPE;
/*    */   }
/*    */ }


/* Location:              C:\Users\karol\AppData\Roaming\ModrinthApp\meta\versions\26.2-0.19.5\26.2-0.19.5.jar!\net\minecraft\network\protocol\common\custom\BrandPayload.class
 * Java compiler version: 25 (69.0)
 * JD-Core Version:       1.1.3
 */