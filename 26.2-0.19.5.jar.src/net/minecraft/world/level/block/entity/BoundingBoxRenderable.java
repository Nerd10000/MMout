/*    */ package net.minecraft.world.level.block.entity;
/*    */ 
/*    */ import net.minecraft.core.BlockPos;
/*    */ import net.minecraft.core.Vec3i;
/*    */ 
/*    */ public interface BoundingBoxRenderable {
/*    */   Mode renderMode();
/*    */   
/*    */   RenderableBox getRenderableBox();
/*    */   
/*    */   public static final class RenderableBox extends Record {
/*    */     private final BlockPos localPos;
/*    */     
/*    */     private final Vec3i size;
/*    */     
/*    */     public RenderableBox(BlockPos localPos, Vec3i size) {
/*  7 */       this.localPos = localPos;
/*  7 */       this.size = size;
/*    */     }
/*    */     
/*    */     public final String toString() {
/*    */       // Byte code:
/*    */       //   0: aload_0
/*    */       //   1: <illegal opcode> toString : (Lnet/minecraft/world/level/block/entity/BoundingBoxRenderable$RenderableBox;)Ljava/lang/String;
/*    */       //   6: areturn
/*    */       // Line number table:
/*    */       //   Java source line number -> byte code offset
/*    */       //   #7	-> 0
/*    */       // Local variable table:
/*    */       //   start	length	slot	name	descriptor
/*    */       //   0	7	0	this	Lnet/minecraft/world/level/block/entity/BoundingBoxRenderable$RenderableBox;
/*    */     }
/*    */     
/*    */     public final int hashCode() {
/*    */       // Byte code:
/*    */       //   0: aload_0
/*    */       //   1: <illegal opcode> hashCode : (Lnet/minecraft/world/level/block/entity/BoundingBoxRenderable$RenderableBox;)I
/*    */       //   6: ireturn
/*    */       // Line number table:
/*    */       //   Java source line number -> byte code offset
/*    */       //   #7	-> 0
/*    */       // Local variable table:
/*    */       //   start	length	slot	name	descriptor
/*    */       //   0	7	0	this	Lnet/minecraft/world/level/block/entity/BoundingBoxRenderable$RenderableBox;
/*    */     }
/*    */     
/*    */     public final boolean equals(Object o) {
/*    */       // Byte code:
/*    */       //   0: aload_0
/*    */       //   1: aload_1
/*    */       //   2: <illegal opcode> equals : (Lnet/minecraft/world/level/block/entity/BoundingBoxRenderable$RenderableBox;Ljava/lang/Object;)Z
/*    */       //   7: ireturn
/*    */       // Line number table:
/*    */       //   Java source line number -> byte code offset
/*    */       //   #7	-> 0
/*    */       // Local variable table:
/*    */       //   start	length	slot	name	descriptor
/*    */       //   0	8	0	this	Lnet/minecraft/world/level/block/entity/BoundingBoxRenderable$RenderableBox;
/*    */       //   0	8	1	o	Ljava/lang/Object;
/*    */     }
/*    */     
/*    */     public BlockPos localPos() {
/*  7 */       return this.localPos;
/*    */     }
/*    */     
/*    */     public Vec3i size() {
/*  7 */       return this.size;
/*    */     }
/*    */     
/*    */     public static RenderableBox fromCorners(int x1, int y1, int z1, int x2, int y2, int z2) {
/*  9 */       int x = Math.min(x1, x2);
/* 10 */       int y = Math.min(y1, y2);
/* 11 */       int z = Math.min(z1, z2);
/* 12 */       return new RenderableBox(new BlockPos(x, y, z), new Vec3i(Math.max(x1, x2) - x, Math.max(y1, y2) - y, Math.max(z1, z2) - z));
/*    */     }
/*    */   }
/*    */   
/*    */   public enum Mode {
/* 17 */     NONE, BOX, BOX_AND_INVISIBLE_BLOCKS;
/*    */   }
/*    */ }


/* Location:              C:\Users\karol\AppData\Roaming\ModrinthApp\meta\versions\26.2-0.19.5\26.2-0.19.5.jar!\net\minecraft\world\level\block\entity\BoundingBoxRenderable.class
 * Java compiler version: 25 (69.0)
 * JD-Core Version:       1.1.3
 */