/*     */ package net.minecraft.world.level.saveddata.maps;
/*     */ 
/*     */ import net.minecraft.core.Holder;
/*     */ 
/*     */ final class MapDecorationLocation extends Record {
/*     */   private final Holder<MapDecorationType> type;
/*     */   
/*     */   private final byte x;
/*     */   
/*     */   private final byte y;
/*     */   
/*     */   private final byte rot;
/*     */   
/*     */   private MapDecorationLocation(Holder<MapDecorationType> type, byte x, byte y, byte rot) {
/* 468 */     this.type = type;
/* 468 */     this.x = x;
/* 468 */     this.y = y;
/* 468 */     this.rot = rot;
/*     */   }
/*     */   
/*     */   public final String toString() {
/*     */     // Byte code:
/*     */     //   0: aload_0
/*     */     //   1: <illegal opcode> toString : (Lnet/minecraft/world/level/saveddata/maps/MapItemSavedData$MapDecorationLocation;)Ljava/lang/String;
/*     */     //   6: areturn
/*     */     // Line number table:
/*     */     //   Java source line number -> byte code offset
/*     */     //   #468	-> 0
/*     */     // Local variable table:
/*     */     //   start	length	slot	name	descriptor
/*     */     //   0	7	0	this	Lnet/minecraft/world/level/saveddata/maps/MapItemSavedData$MapDecorationLocation;
/*     */   }
/*     */   
/*     */   public final int hashCode() {
/*     */     // Byte code:
/*     */     //   0: aload_0
/*     */     //   1: <illegal opcode> hashCode : (Lnet/minecraft/world/level/saveddata/maps/MapItemSavedData$MapDecorationLocation;)I
/*     */     //   6: ireturn
/*     */     // Line number table:
/*     */     //   Java source line number -> byte code offset
/*     */     //   #468	-> 0
/*     */     // Local variable table:
/*     */     //   start	length	slot	name	descriptor
/*     */     //   0	7	0	this	Lnet/minecraft/world/level/saveddata/maps/MapItemSavedData$MapDecorationLocation;
/*     */   }
/*     */   
/*     */   public final boolean equals(Object o) {
/*     */     // Byte code:
/*     */     //   0: aload_0
/*     */     //   1: aload_1
/*     */     //   2: <illegal opcode> equals : (Lnet/minecraft/world/level/saveddata/maps/MapItemSavedData$MapDecorationLocation;Ljava/lang/Object;)Z
/*     */     //   7: ireturn
/*     */     // Line number table:
/*     */     //   Java source line number -> byte code offset
/*     */     //   #468	-> 0
/*     */     // Local variable table:
/*     */     //   start	length	slot	name	descriptor
/*     */     //   0	8	0	this	Lnet/minecraft/world/level/saveddata/maps/MapItemSavedData$MapDecorationLocation;
/*     */     //   0	8	1	o	Ljava/lang/Object;
/*     */   }
/*     */   
/*     */   public Holder<MapDecorationType> type() {
/* 468 */     return this.type;
/*     */   }
/*     */   
/*     */   public byte x() {
/* 468 */     return this.x;
/*     */   }
/*     */   
/*     */   public byte y() {
/* 468 */     return this.y;
/*     */   }
/*     */   
/*     */   public byte rot() {
/* 468 */     return this.rot;
/*     */   }
/*     */ }


/* Location:              C:\Users\karol\AppData\Roaming\ModrinthApp\meta\versions\26.2-0.19.5\26.2-0.19.5.jar!\net\minecraft\world\level\saveddata\maps\MapItemSavedData$MapDecorationLocation.class
 * Java compiler version: 25 (69.0)
 * JD-Core Version:       1.1.3
 */