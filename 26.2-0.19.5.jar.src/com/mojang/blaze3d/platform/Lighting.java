/*    */ package com.mojang.blaze3d.platform;
/*    */ 
/*    */ import com.mojang.blaze3d.buffers.GpuBuffer;
/*    */ import com.mojang.blaze3d.buffers.Std140Builder;
/*    */ import com.mojang.blaze3d.buffers.Std140SizeCalculator;
/*    */ import com.mojang.blaze3d.systems.GpuDevice;
/*    */ import com.mojang.blaze3d.systems.RenderSystem;
/*    */ import java.nio.ByteBuffer;
/*    */ import net.minecraft.util.Mth;
/*    */ import net.minecraft.world.level.CardinalLighting;
/*    */ import org.joml.Matrix4f;
/*    */ import org.joml.Vector3f;
/*    */ import org.joml.Vector3fc;
/*    */ import org.lwjgl.system.MemoryStack;
/*    */ 
/*    */ public class Lighting implements AutoCloseable {
/* 18 */   private static final Vector3fc DIFFUSE_LIGHT_0 = (Vector3fc)(new Vector3f(0.2F, 1.0F, -0.7F)).normalize();
/*    */   
/* 19 */   private static final Vector3fc DIFFUSE_LIGHT_1 = (Vector3fc)(new Vector3f(-0.2F, 1.0F, 0.7F)).normalize();
/*    */   
/* 21 */   private static final Vector3fc NETHER_DIFFUSE_LIGHT_0 = (Vector3fc)(new Vector3f(0.2F, 1.0F, -0.7F)).normalize();
/*    */   
/* 22 */   private static final Vector3fc NETHER_DIFFUSE_LIGHT_1 = (Vector3fc)(new Vector3f(-0.2F, -1.0F, 0.7F)).normalize();
/*    */   
/* 24 */   private static final Vector3fc INVENTORY_DIFFUSE_LIGHT_0 = (Vector3fc)(new Vector3f(0.2F, -1.0F, 1.0F)).normalize();
/*    */   
/* 25 */   private static final Vector3fc INVENTORY_DIFFUSE_LIGHT_1 = (Vector3fc)(new Vector3f(-0.2F, -1.0F, 0.0F)).normalize();
/*    */   
/* 27 */   public static final int UBO_SIZE = (new Std140SizeCalculator())
/* 28 */     .putVec3()
/* 29 */     .putVec3()
/* 30 */     .get();
/*    */   
/*    */   private final GpuBuffer buffer;
/*    */   
/*    */   private final long paddedSize;
/*    */   
/*    */   public Lighting() {
/* 36 */     GpuDevice device = RenderSystem.getDevice();
/* 37 */     this.paddedSize = Mth.roundToward(UBO_SIZE, device.getDeviceInfo().limits().minUniformOffsetAlignment());
/* 38 */     this.buffer = device.createBuffer(() -> "Lighting UBO", 136, this.paddedSize * (Entry.values()).length);
/* 42 */     Matrix4f flatPose = (new Matrix4f()).rotationY(-0.3926991F).rotateX(2.3561945F);
/* 43 */     updateBuffer(Entry.ITEMS_FLAT, (Vector3fc)flatPose.transformDirection(DIFFUSE_LIGHT_0, new Vector3f()), (Vector3fc)flatPose.transformDirection(DIFFUSE_LIGHT_1, new Vector3f()));
/* 48 */     Matrix4f item3DPose = (new Matrix4f()).scaling(1.0F, -1.0F, 1.0F).rotateYXZ(1.0821041F, 3.2375858F, 0.0F).rotateYXZ(-0.3926991F, 2.3561945F, 0.0F);
/* 49 */     updateBuffer(Entry.ITEMS_3D, (Vector3fc)item3DPose.transformDirection(DIFFUSE_LIGHT_0, new Vector3f()), (Vector3fc)item3DPose.transformDirection(DIFFUSE_LIGHT_1, new Vector3f()));
/* 51 */     updateBuffer(Entry.ENTITY_IN_UI, INVENTORY_DIFFUSE_LIGHT_0, INVENTORY_DIFFUSE_LIGHT_1);
/* 53 */     Matrix4f playerSkinPose = new Matrix4f();
/* 54 */     updateBuffer(Entry.PLAYER_SKIN, (Vector3fc)playerSkinPose.transformDirection(INVENTORY_DIFFUSE_LIGHT_0, new Vector3f()), (Vector3fc)playerSkinPose.transformDirection(INVENTORY_DIFFUSE_LIGHT_1, new Vector3f()));
/*    */   }
/*    */   
/*    */   public void updateLevel(CardinalLighting.Type type) {
/* 58 */     switch (type) {
/*    */       case DEFAULT:
/* 59 */         updateBuffer(Entry.LEVEL, DIFFUSE_LIGHT_0, DIFFUSE_LIGHT_1);
/*    */         break;
/*    */       case NETHER:
/* 60 */         updateBuffer(Entry.LEVEL, NETHER_DIFFUSE_LIGHT_0, NETHER_DIFFUSE_LIGHT_1);
/*    */         break;
/*    */     } 
/*    */   }
/*    */   
/*    */   private void updateBuffer(Entry entry, Vector3fc light0, Vector3fc light1) {
/* 65 */     MemoryStack stack = MemoryStack.stackPush();
/*    */     try {
/* 66 */       ByteBuffer byteBuffer = Std140Builder.onStack(stack, UBO_SIZE).putVec3(light0).putVec3(light1).get();
/* 67 */       RenderSystem.getDevice().createCommandEncoder().writeToBuffer(this.buffer.slice(entry.ordinal() * this.paddedSize, this.paddedSize), byteBuffer);
/* 68 */       if (stack != null)
/* 68 */         stack.close(); 
/*    */     } catch (Throwable throwable) {
/*    */       if (stack != null)
/*    */         try {
/*    */           stack.close();
/*    */         } catch (Throwable throwable1) {
/*    */           throwable.addSuppressed(throwable1);
/*    */         }  
/*    */       throw throwable;
/*    */     } 
/*    */   }
/*    */   
/*    */   public void setupFor(Entry entry) {
/* 72 */     RenderSystem.setShaderLights(this.buffer.slice(entry.ordinal() * this.paddedSize, UBO_SIZE));
/*    */   }
/*    */   
/*    */   public void close() {
/* 77 */     this.buffer.close();
/*    */   }
/*    */   
/*    */   public enum Entry {
/* 81 */     LEVEL, ITEMS_FLAT, ITEMS_3D, ENTITY_IN_UI, PLAYER_SKIN;
/*    */   }
/*    */ }


/* Location:              C:\Users\karol\AppData\Roaming\ModrinthApp\meta\versions\26.2-0.19.5\26.2-0.19.5.jar!\com\mojang\blaze3d\platform\Lighting.class
 * Java compiler version: 25 (69.0)
 * JD-Core Version:       1.1.3
 */