/*    */ package net.minecraft.world.level.saveddata.maps;
/*    */ 
/*    */ import com.mojang.datafixers.kinds.App;
/*    */ import com.mojang.datafixers.kinds.Applicative;
/*    */ import com.mojang.datafixers.util.Function3;
/*    */ import com.mojang.serialization.Codec;
/*    */ import com.mojang.serialization.codecs.RecordCodecBuilder;
/*    */ import net.minecraft.core.BlockPos;
/*    */ 
/*    */ public final class MapFrame extends Record {
/*    */   private final BlockPos pos;
/*    */   
/*    */   private final int rotation;
/*    */   
/*    */   private final int entityId;
/*    */   
/*    */   public static final Codec<MapFrame> CODEC;
/*    */   
/*    */   public MapFrame(BlockPos pos, int rotation, int entityId) {
/*  7 */     this.pos = pos;
/*  7 */     this.rotation = rotation;
/*  7 */     this.entityId = entityId;
/*    */   }
/*    */   
/*    */   public final String toString() {
/*    */     // Byte code:
/*    */     //   0: aload_0
/*    */     //   1: <illegal opcode> toString : (Lnet/minecraft/world/level/saveddata/maps/MapFrame;)Ljava/lang/String;
/*    */     //   6: areturn
/*    */     // Line number table:
/*    */     //   Java source line number -> byte code offset
/*    */     //   #7	-> 0
/*    */     // Local variable table:
/*    */     //   start	length	slot	name	descriptor
/*    */     //   0	7	0	this	Lnet/minecraft/world/level/saveddata/maps/MapFrame;
/*    */   }
/*    */   
/*    */   public final int hashCode() {
/*    */     // Byte code:
/*    */     //   0: aload_0
/*    */     //   1: <illegal opcode> hashCode : (Lnet/minecraft/world/level/saveddata/maps/MapFrame;)I
/*    */     //   6: ireturn
/*    */     // Line number table:
/*    */     //   Java source line number -> byte code offset
/*    */     //   #7	-> 0
/*    */     // Local variable table:
/*    */     //   start	length	slot	name	descriptor
/*    */     //   0	7	0	this	Lnet/minecraft/world/level/saveddata/maps/MapFrame;
/*    */   }
/*    */   
/*    */   public final boolean equals(Object o) {
/*    */     // Byte code:
/*    */     //   0: aload_0
/*    */     //   1: aload_1
/*    */     //   2: <illegal opcode> equals : (Lnet/minecraft/world/level/saveddata/maps/MapFrame;Ljava/lang/Object;)Z
/*    */     //   7: ireturn
/*    */     // Line number table:
/*    */     //   Java source line number -> byte code offset
/*    */     //   #7	-> 0
/*    */     // Local variable table:
/*    */     //   start	length	slot	name	descriptor
/*    */     //   0	8	0	this	Lnet/minecraft/world/level/saveddata/maps/MapFrame;
/*    */     //   0	8	1	o	Ljava/lang/Object;
/*    */   }
/*    */   
/*    */   public BlockPos pos() {
/*  7 */     return this.pos;
/*    */   }
/*    */   
/*    */   public int rotation() {
/*  7 */     return this.rotation;
/*    */   }
/*    */   
/*    */   public int entityId() {
/*  7 */     return this.entityId;
/*    */   }
/*    */   
/*    */   static {
/*  8 */     CODEC = RecordCodecBuilder.create(i -> i.group((App)BlockPos.CODEC.fieldOf("pos").forGetter(MapFrame::pos), (App)Codec.INT.fieldOf("rotation").forGetter(MapFrame::rotation), (App)Codec.INT.fieldOf("entity_id").forGetter(MapFrame::entityId)).apply((Applicative)i, MapFrame::new));
/*    */   }
/*    */   
/*    */   public String getId() {
/* 15 */     return frameId(this.pos);
/*    */   }
/*    */   
/*    */   public static String frameId(BlockPos pos) {
/* 19 */     return "frame-" + pos.getX() + "," + pos.getY() + "," + pos.getZ();
/*    */   }
/*    */ }


/* Location:              C:\Users\karol\AppData\Roaming\ModrinthApp\meta\versions\26.2-0.19.5\26.2-0.19.5.jar!\net\minecraft\world\level\saveddata\maps\MapFrame.class
 * Java compiler version: 25 (69.0)
 * JD-Core Version:       1.1.3
 */