/*    */ package net.minecraft.world.phys;
/*    */ 
/*    */ import io.netty.buffer.ByteBuf;
/*    */ import net.minecraft.network.codec.StreamCodec;
/*    */ 
/*    */ class null implements StreamCodec<ByteBuf, Vec3> {
/*    */   public Vec3 decode(ByteBuf input) {
/* 28 */     return new Vec3(input.readDouble(), input.readDouble(), input.readDouble());
/*    */   }
/*    */   
/*    */   public void encode(ByteBuf output, Vec3 value) {
/* 33 */     output.writeDouble(value.x());
/* 34 */     output.writeDouble(value.y());
/* 35 */     output.writeDouble(value.z());
/*    */   }
/*    */ }


/* Location:              C:\Users\karol\AppData\Roaming\ModrinthApp\meta\versions\26.2-0.19.5\26.2-0.19.5.jar!\net\minecraft\world\phys\Vec3$1.class
 * Java compiler version: 25 (69.0)
 * JD-Core Version:       1.1.3
 */