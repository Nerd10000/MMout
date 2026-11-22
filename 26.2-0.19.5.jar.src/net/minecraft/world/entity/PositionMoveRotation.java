/*    */ package net.minecraft.world.entity;
/*    */ 
/*    */ import com.mojang.datafixers.util.Function4;
/*    */ import java.util.Set;
/*    */ import net.minecraft.network.FriendlyByteBuf;
/*    */ import net.minecraft.network.codec.ByteBufCodecs;
/*    */ import net.minecraft.network.codec.StreamCodec;
/*    */ import net.minecraft.util.Mth;
/*    */ import net.minecraft.world.level.portal.TeleportTransition;
/*    */ import net.minecraft.world.phys.Vec3;
/*    */ 
/*    */ public final class PositionMoveRotation extends Record {
/*    */   private final Vec3 position;
/*    */   
/*    */   private final Vec3 deltaMovement;
/*    */   
/*    */   private final float yRot;
/*    */   
/*    */   private final float xRot;
/*    */   
/*    */   public PositionMoveRotation(Vec3 position, Vec3 deltaMovement, float yRot, float xRot) {
/* 12 */     this.position = position;
/* 12 */     this.deltaMovement = deltaMovement;
/* 12 */     this.yRot = yRot;
/* 12 */     this.xRot = xRot;
/*    */   }
/*    */   
/*    */   public final String toString() {
/*    */     // Byte code:
/*    */     //   0: aload_0
/*    */     //   1: <illegal opcode> toString : (Lnet/minecraft/world/entity/PositionMoveRotation;)Ljava/lang/String;
/*    */     //   6: areturn
/*    */     // Line number table:
/*    */     //   Java source line number -> byte code offset
/*    */     //   #12	-> 0
/*    */     // Local variable table:
/*    */     //   start	length	slot	name	descriptor
/*    */     //   0	7	0	this	Lnet/minecraft/world/entity/PositionMoveRotation;
/*    */   }
/*    */   
/*    */   public final int hashCode() {
/*    */     // Byte code:
/*    */     //   0: aload_0
/*    */     //   1: <illegal opcode> hashCode : (Lnet/minecraft/world/entity/PositionMoveRotation;)I
/*    */     //   6: ireturn
/*    */     // Line number table:
/*    */     //   Java source line number -> byte code offset
/*    */     //   #12	-> 0
/*    */     // Local variable table:
/*    */     //   start	length	slot	name	descriptor
/*    */     //   0	7	0	this	Lnet/minecraft/world/entity/PositionMoveRotation;
/*    */   }
/*    */   
/*    */   public final boolean equals(Object o) {
/*    */     // Byte code:
/*    */     //   0: aload_0
/*    */     //   1: aload_1
/*    */     //   2: <illegal opcode> equals : (Lnet/minecraft/world/entity/PositionMoveRotation;Ljava/lang/Object;)Z
/*    */     //   7: ireturn
/*    */     // Line number table:
/*    */     //   Java source line number -> byte code offset
/*    */     //   #12	-> 0
/*    */     // Local variable table:
/*    */     //   start	length	slot	name	descriptor
/*    */     //   0	8	0	this	Lnet/minecraft/world/entity/PositionMoveRotation;
/*    */     //   0	8	1	o	Ljava/lang/Object;
/*    */   }
/*    */   
/*    */   public Vec3 position() {
/* 12 */     return this.position;
/*    */   }
/*    */   
/*    */   public Vec3 deltaMovement() {
/* 12 */     return this.deltaMovement;
/*    */   }
/*    */   
/*    */   public float yRot() {
/* 12 */     return this.yRot;
/*    */   }
/*    */   
/*    */   public float xRot() {
/* 12 */     return this.xRot;
/*    */   }
/*    */   
/* 13 */   public static final StreamCodec<FriendlyByteBuf, PositionMoveRotation> STREAM_CODEC = StreamCodec.composite(Vec3.STREAM_CODEC, PositionMoveRotation::position, Vec3.STREAM_CODEC, PositionMoveRotation::deltaMovement, ByteBufCodecs.FLOAT, PositionMoveRotation::yRot, ByteBufCodecs.FLOAT, PositionMoveRotation::xRot, PositionMoveRotation::new);
/*    */   
/*    */   public static PositionMoveRotation of(Entity entity) {
/* 22 */     if (entity.isInterpolating())
/* 23 */       return new PositionMoveRotation(entity.getInterpolation().position(), entity.getKnownMovement(), entity.getInterpolation().yRot(), entity.getInterpolation().xRot()); 
/* 25 */     return new PositionMoveRotation(entity.position(), entity.getKnownMovement(), entity.getYRot(), entity.getXRot());
/*    */   }
/*    */   
/*    */   public PositionMoveRotation withRotation(float yRot, float xRot) {
/* 30 */     return new PositionMoveRotation(position(), deltaMovement(), yRot, xRot);
/*    */   }
/*    */   
/*    */   public static PositionMoveRotation of(TeleportTransition transition) {
/* 34 */     return new PositionMoveRotation(transition.position(), transition.deltaMovement(), transition.yRot(), transition.xRot());
/*    */   }
/*    */   
/*    */   public static PositionMoveRotation calculateAbsolute(PositionMoveRotation source, PositionMoveRotation change, Set<Relative> relatives) {
/* 38 */     double offsetX = relatives.contains(Relative.X) ? source.position.x : 0.0D;
/* 39 */     double offsetY = relatives.contains(Relative.Y) ? source.position.y : 0.0D;
/* 40 */     double offsetZ = relatives.contains(Relative.Z) ? source.position.z : 0.0D;
/* 41 */     float offsetYRot = relatives.contains(Relative.Y_ROT) ? source.yRot : 0.0F;
/* 42 */     float offsetXRot = relatives.contains(Relative.X_ROT) ? source.xRot : 0.0F;
/* 44 */     Vec3 absolutePosition = new Vec3(offsetX + change.position.x, offsetY + change.position.y, offsetZ + change.position.z);
/* 45 */     float absoluteYRot = offsetYRot + change.yRot;
/* 46 */     float absoluteXRot = Mth.clamp(offsetXRot + change.xRot, -90.0F, 90.0F);
/* 48 */     Vec3 rotatedCurrentMovement = source.deltaMovement;
/* 49 */     if (relatives.contains(Relative.ROTATE_DELTA)) {
/* 50 */       float diffYRot = source.yRot - absoluteYRot;
/* 51 */       float diffXRot = source.xRot - absoluteXRot;
/* 52 */       rotatedCurrentMovement = rotatedCurrentMovement.xRot((float)Math.toRadians(diffXRot));
/* 53 */       rotatedCurrentMovement = rotatedCurrentMovement.yRot((float)Math.toRadians(diffYRot));
/*    */     } 
/* 59 */     Vec3 absoluteDeltaMovement = new Vec3(calculateDelta(rotatedCurrentMovement.x, change.deltaMovement.x, relatives, Relative.DELTA_X), calculateDelta(rotatedCurrentMovement.y, change.deltaMovement.y, relatives, Relative.DELTA_Y), calculateDelta(rotatedCurrentMovement.z, change.deltaMovement.z, relatives, Relative.DELTA_Z));
/* 61 */     return new PositionMoveRotation(absolutePosition, absoluteDeltaMovement, absoluteYRot, absoluteXRot);
/*    */   }
/*    */   
/*    */   private static double calculateDelta(double currentDelta, double deltaChange, Set<Relative> relatives, Relative relative) {
/* 65 */     return relatives.contains(relative) ? (currentDelta + deltaChange) : deltaChange;
/*    */   }
/*    */ }


/* Location:              C:\Users\karol\AppData\Roaming\ModrinthApp\meta\versions\26.2-0.19.5\26.2-0.19.5.jar!\net\minecraft\world\entity\PositionMoveRotation.class
 * Java compiler version: 25 (69.0)
 * JD-Core Version:       1.1.3
 */