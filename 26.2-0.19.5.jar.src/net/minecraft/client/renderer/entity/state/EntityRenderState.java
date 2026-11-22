/*    */ package net.minecraft.client.renderer.entity.state;
/*    */ 
/*    */ import java.util.ArrayList;
/*    */ import java.util.List;
/*    */ import java.util.Locale;
/*    */ import net.minecraft.CrashReportCategory;
/*    */ import net.minecraft.network.chat.Component;
/*    */ import net.minecraft.world.entity.EntityType;
/*    */ import net.minecraft.world.phys.Vec3;
/*    */ import net.minecraft.world.phys.shapes.VoxelShape;
/*    */ 
/*    */ public class EntityRenderState {
/*    */   public static final int NO_OUTLINE = 0;
/*    */   
/*    */   public EntityType<?> entityType;
/*    */   
/*    */   public double x;
/*    */   
/*    */   public double y;
/*    */   
/*    */   public double z;
/*    */   
/*    */   public float ageInTicks;
/*    */   
/*    */   public float boundingBoxWidth;
/*    */   
/*    */   public float boundingBoxHeight;
/*    */   
/*    */   public float eyeHeight;
/*    */   
/*    */   public double distanceToCameraSq;
/*    */   
/*    */   public boolean isInvisible;
/*    */   
/*    */   public boolean isDiscrete;
/*    */   
/*    */   public boolean displayFireAnimation;
/*    */   
/* 31 */   public int lightCoords = 15728880;
/*    */   
/* 32 */   public int outlineColor = 0;
/*    */   
/*    */   public Vec3 passengerOffset;
/*    */   
/*    */   public Component nameTag;
/*    */   
/*    */   public Component scoreText;
/*    */   
/*    */   public Vec3 nameTagAttachment;
/*    */   
/*    */   public List<LeashState> leashStates;
/*    */   
/*    */   public float shadowRadius;
/*    */   
/* 40 */   public final List<ShadowPiece> shadowPieces = new ArrayList<>();
/*    */   
/*    */   public boolean appearsGlowing() {
/* 43 */     return (this.outlineColor != 0);
/*    */   }
/*    */   
/*    */   public static class LeashState {
/* 47 */     public Vec3 offset = Vec3.ZERO;
/*    */     
/* 48 */     public Vec3 start = Vec3.ZERO;
/*    */     
/* 49 */     public Vec3 end = Vec3.ZERO;
/*    */     
/* 50 */     public int startBlockLight = 0;
/*    */     
/* 51 */     public int endBlockLight = 0;
/*    */     
/* 52 */     public int startSkyLight = 15;
/*    */     
/* 53 */     public int endSkyLight = 15;
/*    */     
/*    */     public boolean slack = true;
/*    */   }
/*    */   
/*    */   public void fillCrashReportCategory(CrashReportCategory category) {
/* 58 */     category.setDetail("EntityRenderState", getClass().getCanonicalName());
/* 59 */     category.setDetail("Entity's Exact location", String.format(Locale.ROOT, "%.2f, %.2f, %.2f", new Object[] { Double.valueOf(this.x), Double.valueOf(this.y), Double.valueOf(this.z) }));
/*    */   }
/*    */   
/*    */   public static final class ShadowPiece extends Record {
/*    */     private final float relativeX;
/*    */     
/*    */     private final float relativeY;
/*    */     
/*    */     private final float relativeZ;
/*    */     
/*    */     private final VoxelShape shapeBelow;
/*    */     
/*    */     private final float alpha;
/*    */     
/*    */     public ShadowPiece(float relativeX, float relativeY, float relativeZ, VoxelShape shapeBelow, float alpha) {
/* 62 */       this.relativeX = relativeX;
/* 62 */       this.relativeY = relativeY;
/* 62 */       this.relativeZ = relativeZ;
/* 62 */       this.shapeBelow = shapeBelow;
/* 62 */       this.alpha = alpha;
/*    */     }
/*    */     
/*    */     public final String toString() {
/*    */       // Byte code:
/*    */       //   0: aload_0
/*    */       //   1: <illegal opcode> toString : (Lnet/minecraft/client/renderer/entity/state/EntityRenderState$ShadowPiece;)Ljava/lang/String;
/*    */       //   6: areturn
/*    */       // Line number table:
/*    */       //   Java source line number -> byte code offset
/*    */       //   #62	-> 0
/*    */       // Local variable table:
/*    */       //   start	length	slot	name	descriptor
/*    */       //   0	7	0	this	Lnet/minecraft/client/renderer/entity/state/EntityRenderState$ShadowPiece;
/*    */     }
/*    */     
/*    */     public final int hashCode() {
/*    */       // Byte code:
/*    */       //   0: aload_0
/*    */       //   1: <illegal opcode> hashCode : (Lnet/minecraft/client/renderer/entity/state/EntityRenderState$ShadowPiece;)I
/*    */       //   6: ireturn
/*    */       // Line number table:
/*    */       //   Java source line number -> byte code offset
/*    */       //   #62	-> 0
/*    */       // Local variable table:
/*    */       //   start	length	slot	name	descriptor
/*    */       //   0	7	0	this	Lnet/minecraft/client/renderer/entity/state/EntityRenderState$ShadowPiece;
/*    */     }
/*    */     
/*    */     public final boolean equals(Object o) {
/*    */       // Byte code:
/*    */       //   0: aload_0
/*    */       //   1: aload_1
/*    */       //   2: <illegal opcode> equals : (Lnet/minecraft/client/renderer/entity/state/EntityRenderState$ShadowPiece;Ljava/lang/Object;)Z
/*    */       //   7: ireturn
/*    */       // Line number table:
/*    */       //   Java source line number -> byte code offset
/*    */       //   #62	-> 0
/*    */       // Local variable table:
/*    */       //   start	length	slot	name	descriptor
/*    */       //   0	8	0	this	Lnet/minecraft/client/renderer/entity/state/EntityRenderState$ShadowPiece;
/*    */       //   0	8	1	o	Ljava/lang/Object;
/*    */     }
/*    */     
/*    */     public float relativeX() {
/* 62 */       return this.relativeX;
/*    */     }
/*    */     
/*    */     public float relativeY() {
/* 62 */       return this.relativeY;
/*    */     }
/*    */     
/*    */     public float relativeZ() {
/* 62 */       return this.relativeZ;
/*    */     }
/*    */     
/*    */     public VoxelShape shapeBelow() {
/* 62 */       return this.shapeBelow;
/*    */     }
/*    */     
/*    */     public float alpha() {
/* 62 */       return this.alpha;
/*    */     }
/*    */   }
/*    */ }


/* Location:              C:\Users\karol\AppData\Roaming\ModrinthApp\meta\versions\26.2-0.19.5\26.2-0.19.5.jar!\net\minecraft\client\renderer\entity\state\EntityRenderState.class
 * Java compiler version: 25 (69.0)
 * JD-Core Version:       1.1.3
 */