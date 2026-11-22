/*    */ package net.minecraft.world.entity.vehicle.minecart;
/*    */ 
/*    */ import com.mojang.datafixers.util.Function5;
/*    */ import io.netty.buffer.ByteBuf;
/*    */ import net.minecraft.network.codec.ByteBufCodecs;
/*    */ import net.minecraft.network.codec.StreamCodec;
/*    */ import net.minecraft.world.phys.Vec3;
/*    */ 
/*    */ public final class MinecartStep extends Record {
/*    */   private final Vec3 position;
/*    */   
/*    */   private final Vec3 movement;
/*    */   
/*    */   private final float yRot;
/*    */   
/*    */   private final float xRot;
/*    */   
/*    */   private final float weight;
/*    */   
/*    */   public MinecartStep(Vec3 position, Vec3 movement, float yRot, float xRot, float weight) {
/* 41 */     this.position = position;
/* 41 */     this.movement = movement;
/* 41 */     this.yRot = yRot;
/* 41 */     this.xRot = xRot;
/* 41 */     this.weight = weight;
/*    */   }
/*    */   
/*    */   public final String toString() {
/*    */     // Byte code:
/*    */     //   0: aload_0
/*    */     //   1: <illegal opcode> toString : (Lnet/minecraft/world/entity/vehicle/minecart/NewMinecartBehavior$MinecartStep;)Ljava/lang/String;
/*    */     //   6: areturn
/*    */     // Line number table:
/*    */     //   Java source line number -> byte code offset
/*    */     //   #41	-> 0
/*    */     // Local variable table:
/*    */     //   start	length	slot	name	descriptor
/*    */     //   0	7	0	this	Lnet/minecraft/world/entity/vehicle/minecart/NewMinecartBehavior$MinecartStep;
/*    */   }
/*    */   
/*    */   public final int hashCode() {
/*    */     // Byte code:
/*    */     //   0: aload_0
/*    */     //   1: <illegal opcode> hashCode : (Lnet/minecraft/world/entity/vehicle/minecart/NewMinecartBehavior$MinecartStep;)I
/*    */     //   6: ireturn
/*    */     // Line number table:
/*    */     //   Java source line number -> byte code offset
/*    */     //   #41	-> 0
/*    */     // Local variable table:
/*    */     //   start	length	slot	name	descriptor
/*    */     //   0	7	0	this	Lnet/minecraft/world/entity/vehicle/minecart/NewMinecartBehavior$MinecartStep;
/*    */   }
/*    */   
/*    */   public final boolean equals(Object o) {
/*    */     // Byte code:
/*    */     //   0: aload_0
/*    */     //   1: aload_1
/*    */     //   2: <illegal opcode> equals : (Lnet/minecraft/world/entity/vehicle/minecart/NewMinecartBehavior$MinecartStep;Ljava/lang/Object;)Z
/*    */     //   7: ireturn
/*    */     // Line number table:
/*    */     //   Java source line number -> byte code offset
/*    */     //   #41	-> 0
/*    */     // Local variable table:
/*    */     //   start	length	slot	name	descriptor
/*    */     //   0	8	0	this	Lnet/minecraft/world/entity/vehicle/minecart/NewMinecartBehavior$MinecartStep;
/*    */     //   0	8	1	o	Ljava/lang/Object;
/*    */   }
/*    */   
/*    */   public Vec3 position() {
/* 41 */     return this.position;
/*    */   }
/*    */   
/*    */   public Vec3 movement() {
/* 41 */     return this.movement;
/*    */   }
/*    */   
/*    */   public float yRot() {
/* 41 */     return this.yRot;
/*    */   }
/*    */   
/*    */   public float xRot() {
/* 41 */     return this.xRot;
/*    */   }
/*    */   
/*    */   public float weight() {
/* 41 */     return this.weight;
/*    */   }
/*    */   
/* 43 */   public static final StreamCodec<ByteBuf, MinecartStep> STREAM_CODEC = StreamCodec.composite(Vec3.STREAM_CODEC, MinecartStep::position, Vec3.STREAM_CODEC, MinecartStep::movement, ByteBufCodecs.ROTATION_BYTE, MinecartStep::yRot, ByteBufCodecs.ROTATION_BYTE, MinecartStep::xRot, ByteBufCodecs.FLOAT, MinecartStep::weight, MinecartStep::new);
/*    */   
/* 52 */   public static final MinecartStep ZERO = new MinecartStep(Vec3.ZERO, Vec3.ZERO, 0.0F, 0.0F, 0.0F);
/*    */ }


/* Location:              C:\Users\karol\AppData\Roaming\ModrinthApp\meta\versions\26.2-0.19.5\26.2-0.19.5.jar!\net\minecraft\world\entity\vehicle\minecart\NewMinecartBehavior$MinecartStep.class
 * Java compiler version: 25 (69.0)
 * JD-Core Version:       1.1.3
 */