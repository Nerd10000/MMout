/*     */ package net.minecraft.client.renderer;
/*     */ 
/*     */ import java.util.EnumMap;
/*     */ import java.util.Map;
/*     */ import net.minecraft.core.Direction;
/*     */ import net.minecraft.util.Util;
/*     */ import org.joml.Vector3f;
/*     */ import org.joml.Vector3fc;
/*     */ 
/*     */ public enum FaceInfo {
/*  12 */   DOWN(new VertexInfo[] { new VertexInfo(Extent.MIN_X, Extent.MIN_Y, Extent.MAX_Z), new VertexInfo(Extent.MIN_X, Extent.MIN_Y, Extent.MIN_Z), new VertexInfo(Extent.MAX_X, Extent.MIN_Y, Extent.MIN_Z), new VertexInfo(Extent.MAX_X, Extent.MIN_Y, Extent.MAX_Z) }),
/*  17 */   UP(new VertexInfo[] { new VertexInfo(Extent.MIN_X, Extent.MAX_Y, Extent.MIN_Z), new VertexInfo(Extent.MIN_X, Extent.MAX_Y, Extent.MAX_Z), new VertexInfo(Extent.MAX_X, Extent.MAX_Y, Extent.MAX_Z), new VertexInfo(Extent.MAX_X, Extent.MAX_Y, Extent.MIN_Z) }),
/*  22 */   NORTH(new VertexInfo[] { new VertexInfo(Extent.MAX_X, Extent.MAX_Y, Extent.MIN_Z), new VertexInfo(Extent.MAX_X, Extent.MIN_Y, Extent.MIN_Z), new VertexInfo(Extent.MIN_X, Extent.MIN_Y, Extent.MIN_Z), new VertexInfo(Extent.MIN_X, Extent.MAX_Y, Extent.MIN_Z) }),
/*  27 */   SOUTH(new VertexInfo[] { new VertexInfo(Extent.MIN_X, Extent.MAX_Y, Extent.MAX_Z), new VertexInfo(Extent.MIN_X, Extent.MIN_Y, Extent.MAX_Z), new VertexInfo(Extent.MAX_X, Extent.MIN_Y, Extent.MAX_Z), new VertexInfo(Extent.MAX_X, Extent.MAX_Y, Extent.MAX_Z) }),
/*  32 */   WEST(new VertexInfo[] { new VertexInfo(Extent.MIN_X, Extent.MAX_Y, Extent.MIN_Z), new VertexInfo(Extent.MIN_X, Extent.MIN_Y, Extent.MIN_Z), new VertexInfo(Extent.MIN_X, Extent.MIN_Y, Extent.MAX_Z), new VertexInfo(Extent.MIN_X, Extent.MAX_Y, Extent.MAX_Z) }),
/*  37 */   EAST(new VertexInfo[] { new VertexInfo(Extent.MAX_X, Extent.MAX_Y, Extent.MAX_Z), new VertexInfo(Extent.MAX_X, Extent.MIN_Y, Extent.MAX_Z), new VertexInfo(Extent.MAX_X, Extent.MIN_Y, Extent.MIN_Z), new VertexInfo(Extent.MAX_X, Extent.MAX_Y, Extent.MIN_Z) });
/*     */   
/*     */   private static final Map<Direction, FaceInfo> BY_FACING;
/*     */   
/*     */   private final VertexInfo[] infos;
/*     */   
/*     */   public enum Extent {
/*  45 */     MIN_X, MIN_Y, MIN_Z, MAX_X, MAX_Y, MAX_Z;
/*     */     
/*     */     public float select(Vector3fc min, Vector3fc max) {
/*  55 */       switch (ordinal()) {
/*     */         default:
/*  55 */           throw new MatchException(null, null);
/*     */         case 0:
/*     */         
/*     */         case 1:
/*     */         
/*     */         case 2:
/*     */         
/*     */         case 3:
/*     */         
/*     */         case 4:
/*     */         
/*     */         case 5:
/*     */           break;
/*     */       } 
/*  55 */       return 
/*     */         
/*  62 */         max.z();
/*     */     }
/*     */     
/*     */     public float select(float minX, float minY, float minZ, float maxX, float maxY, float maxZ) {
/*  67 */       switch (ordinal()) {
/*     */         default:
/*  67 */           throw new MatchException(null, null);
/*     */         case 0:
/*     */         
/*     */         case 1:
/*     */         
/*     */         case 2:
/*     */         
/*     */         case 3:
/*     */         
/*     */         case 4:
/*     */         
/*     */         case 5:
/*     */           break;
/*     */       } 
/*  67 */       return 
/*     */         
/*  74 */         maxZ;
/*     */     }
/*     */   }
/*     */   
/*     */   static {
/*  79 */     BY_FACING = (Map<Direction, FaceInfo>)Util.make(new EnumMap<>(Direction.class), map -> {
/*     */           map.put(Direction.DOWN, DOWN);
/*     */           map.put(Direction.UP, UP);
/*     */           map.put(Direction.NORTH, NORTH);
/*     */           map.put(Direction.SOUTH, SOUTH);
/*     */           map.put(Direction.WEST, WEST);
/*     */           map.put(Direction.EAST, EAST);
/*     */         });
/*     */   }
/*     */   
/*     */   public static FaceInfo fromFacing(Direction direction) {
/*  91 */     return BY_FACING.get(direction);
/*     */   }
/*     */   
/*     */   FaceInfo(VertexInfo... infos) {
/*  95 */     this.infos = infos;
/*     */   }
/*     */   
/*     */   public VertexInfo getVertexInfo(int index) {
/*  99 */     return this.infos[index];
/*     */   }
/*     */   
/*     */   public static final class VertexInfo extends Record {
/*     */     private final FaceInfo.Extent xFace;
/*     */     
/*     */     private final FaceInfo.Extent yFace;
/*     */     
/*     */     private final FaceInfo.Extent zFace;
/*     */     
/*     */     public VertexInfo(FaceInfo.Extent xFace, FaceInfo.Extent yFace, FaceInfo.Extent zFace) {
/* 102 */       this.xFace = xFace;
/* 102 */       this.yFace = yFace;
/* 102 */       this.zFace = zFace;
/*     */     }
/*     */     
/*     */     public final String toString() {
/*     */       // Byte code:
/*     */       //   0: aload_0
/*     */       //   1: <illegal opcode> toString : (Lnet/minecraft/client/renderer/FaceInfo$VertexInfo;)Ljava/lang/String;
/*     */       //   6: areturn
/*     */       // Line number table:
/*     */       //   Java source line number -> byte code offset
/*     */       //   #102	-> 0
/*     */       // Local variable table:
/*     */       //   start	length	slot	name	descriptor
/*     */       //   0	7	0	this	Lnet/minecraft/client/renderer/FaceInfo$VertexInfo;
/*     */     }
/*     */     
/*     */     public final int hashCode() {
/*     */       // Byte code:
/*     */       //   0: aload_0
/*     */       //   1: <illegal opcode> hashCode : (Lnet/minecraft/client/renderer/FaceInfo$VertexInfo;)I
/*     */       //   6: ireturn
/*     */       // Line number table:
/*     */       //   Java source line number -> byte code offset
/*     */       //   #102	-> 0
/*     */       // Local variable table:
/*     */       //   start	length	slot	name	descriptor
/*     */       //   0	7	0	this	Lnet/minecraft/client/renderer/FaceInfo$VertexInfo;
/*     */     }
/*     */     
/*     */     public final boolean equals(Object o) {
/*     */       // Byte code:
/*     */       //   0: aload_0
/*     */       //   1: aload_1
/*     */       //   2: <illegal opcode> equals : (Lnet/minecraft/client/renderer/FaceInfo$VertexInfo;Ljava/lang/Object;)Z
/*     */       //   7: ireturn
/*     */       // Line number table:
/*     */       //   Java source line number -> byte code offset
/*     */       //   #102	-> 0
/*     */       // Local variable table:
/*     */       //   start	length	slot	name	descriptor
/*     */       //   0	8	0	this	Lnet/minecraft/client/renderer/FaceInfo$VertexInfo;
/*     */       //   0	8	1	o	Ljava/lang/Object;
/*     */     }
/*     */     
/*     */     public FaceInfo.Extent xFace() {
/* 102 */       return this.xFace;
/*     */     }
/*     */     
/*     */     public FaceInfo.Extent yFace() {
/* 102 */       return this.yFace;
/*     */     }
/*     */     
/*     */     public FaceInfo.Extent zFace() {
/* 102 */       return this.zFace;
/*     */     }
/*     */     
/*     */     public Vector3f select(Vector3fc min, Vector3fc max) {
/* 104 */       return new Vector3f(this.xFace
/* 105 */           .select(min, max), this.yFace
/* 106 */           .select(min, max), this.zFace
/* 107 */           .select(min, max));
/*     */     }
/*     */   }
/*     */ }


/* Location:              C:\Users\karol\AppData\Roaming\ModrinthApp\meta\versions\26.2-0.19.5\26.2-0.19.5.jar!\net\minecraft\client\renderer\FaceInfo.class
 * Java compiler version: 25 (69.0)
 * JD-Core Version:       1.1.3
 */