/*    */ package net.minecraft.world.level.saveddata.maps;
/*    */ 
/*    */ import io.netty.buffer.ByteBuf;
/*    */ import java.util.Optional;
/*    */ import net.minecraft.network.FriendlyByteBuf;
/*    */ import net.minecraft.network.codec.StreamCodec;
/*    */ 
/*    */ public final class MapPatch extends Record {
/*    */   private final int startX;
/*    */   
/*    */   private final int startY;
/*    */   
/*    */   private final int width;
/*    */   
/*    */   private final int height;
/*    */   
/*    */   private final byte[] mapColors;
/*    */   
/*    */   public MapPatch(int startX, int startY, int width, int height, byte[] mapColors) {
/* 65 */     this.startX = startX;
/* 65 */     this.startY = startY;
/* 65 */     this.width = width;
/* 65 */     this.height = height;
/* 65 */     this.mapColors = mapColors;
/*    */   }
/*    */   
/*    */   public final String toString() {
/*    */     // Byte code:
/*    */     //   0: aload_0
/*    */     //   1: <illegal opcode> toString : (Lnet/minecraft/world/level/saveddata/maps/MapItemSavedData$MapPatch;)Ljava/lang/String;
/*    */     //   6: areturn
/*    */     // Line number table:
/*    */     //   Java source line number -> byte code offset
/*    */     //   #65	-> 0
/*    */     // Local variable table:
/*    */     //   start	length	slot	name	descriptor
/*    */     //   0	7	0	this	Lnet/minecraft/world/level/saveddata/maps/MapItemSavedData$MapPatch;
/*    */   }
/*    */   
/*    */   public final int hashCode() {
/*    */     // Byte code:
/*    */     //   0: aload_0
/*    */     //   1: <illegal opcode> hashCode : (Lnet/minecraft/world/level/saveddata/maps/MapItemSavedData$MapPatch;)I
/*    */     //   6: ireturn
/*    */     // Line number table:
/*    */     //   Java source line number -> byte code offset
/*    */     //   #65	-> 0
/*    */     // Local variable table:
/*    */     //   start	length	slot	name	descriptor
/*    */     //   0	7	0	this	Lnet/minecraft/world/level/saveddata/maps/MapItemSavedData$MapPatch;
/*    */   }
/*    */   
/*    */   public final boolean equals(Object o) {
/*    */     // Byte code:
/*    */     //   0: aload_0
/*    */     //   1: aload_1
/*    */     //   2: <illegal opcode> equals : (Lnet/minecraft/world/level/saveddata/maps/MapItemSavedData$MapPatch;Ljava/lang/Object;)Z
/*    */     //   7: ireturn
/*    */     // Line number table:
/*    */     //   Java source line number -> byte code offset
/*    */     //   #65	-> 0
/*    */     // Local variable table:
/*    */     //   start	length	slot	name	descriptor
/*    */     //   0	8	0	this	Lnet/minecraft/world/level/saveddata/maps/MapItemSavedData$MapPatch;
/*    */     //   0	8	1	o	Ljava/lang/Object;
/*    */   }
/*    */   
/*    */   public int startX() {
/* 65 */     return this.startX;
/*    */   }
/*    */   
/*    */   public int startY() {
/* 65 */     return this.startY;
/*    */   }
/*    */   
/*    */   public int width() {
/* 65 */     return this.width;
/*    */   }
/*    */   
/*    */   public int height() {
/* 65 */     return this.height;
/*    */   }
/*    */   
/*    */   public byte[] mapColors() {
/* 65 */     return this.mapColors;
/*    */   }
/*    */   
/* 66 */   public static final StreamCodec<ByteBuf, Optional<MapPatch>> STREAM_CODEC = StreamCodec.of(MapPatch::write, MapPatch::read);
/*    */   
/*    */   private static void write(ByteBuf output, Optional<MapPatch> optional) {
/* 69 */     if (optional.isPresent()) {
/* 70 */       MapPatch patch = optional.get();
/* 71 */       output.writeByte(patch.width);
/* 72 */       output.writeByte(patch.height);
/* 73 */       output.writeByte(patch.startX);
/* 74 */       output.writeByte(patch.startY);
/* 75 */       FriendlyByteBuf.writeByteArray(output, patch.mapColors);
/*    */     } else {
/* 77 */       output.writeByte(0);
/*    */     } 
/*    */   }
/*    */   
/*    */   private static Optional<MapPatch> read(ByteBuf input) {
/* 82 */     int width = input.readUnsignedByte();
/* 83 */     if (width > 0) {
/* 84 */       int height = input.readUnsignedByte();
/* 85 */       int startX = input.readUnsignedByte();
/* 86 */       int startY = input.readUnsignedByte();
/* 87 */       byte[] mapColors = FriendlyByteBuf.readByteArray(input);
/* 88 */       return Optional.of(new MapPatch(startX, startY, width, height, mapColors));
/*    */     } 
/* 90 */     return Optional.empty();
/*    */   }
/*    */   
/*    */   public void applyToMap(MapItemSavedData map) {
/* 95 */     for (int x = 0; x < this.width; x++) {
/* 96 */       for (int y = 0; y < this.height; y++)
/* 97 */         map.setColor(this.startX + x, this.startY + y, this.mapColors[x + y * this.width]); 
/*    */     } 
/*    */   }
/*    */ }


/* Location:              C:\Users\karol\AppData\Roaming\ModrinthApp\meta\versions\26.2-0.19.5\26.2-0.19.5.jar!\net\minecraft\world\level\saveddata\maps\MapItemSavedData$MapPatch.class
 * Java compiler version: 25 (69.0)
 * JD-Core Version:       1.1.3
 */