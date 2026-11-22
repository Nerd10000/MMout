/*    */ package net.minecraft.client.renderer;
/*    */ 
/*    */ import com.mojang.blaze3d.buffers.GpuBuffer;
/*    */ import com.mojang.blaze3d.buffers.Std140Builder;
/*    */ import com.mojang.blaze3d.buffers.Std140SizeCalculator;
/*    */ import com.mojang.blaze3d.systems.RenderSystem;
/*    */ import java.nio.ByteBuffer;
/*    */ import net.minecraft.client.DeltaTracker;
/*    */ import net.minecraft.util.Mth;
/*    */ import net.minecraft.world.phys.Vec3;
/*    */ import org.lwjgl.system.MemoryStack;
/*    */ 
/*    */ public class GlobalSettingsUniform implements AutoCloseable {
/* 15 */   public static final int UBO_SIZE = (new Std140SizeCalculator())
/* 16 */     .putIVec3()
/* 17 */     .putVec3()
/* 18 */     .putVec2()
/* 19 */     .putFloat()
/* 20 */     .putFloat()
/* 21 */     .putInt()
/* 22 */     .putInt()
/* 23 */     .get();
/*    */   
/* 28 */   private final GpuBuffer buffer = RenderSystem.getDevice().createBuffer(() -> "Global Settings UBO", 136, UBO_SIZE);
/*    */   
/*    */   public void update(int width, int height, double glintAlpha, long gameTime, DeltaTracker deltaTracker, int menuBlurRadius, Vec3 cameraPos, boolean useRgss) {
/* 32 */     MemoryStack stack = MemoryStack.stackPush();
/*    */     try {
/* 33 */       int cameraX = Mth.floor(cameraPos.x);
/* 34 */       int cameraY = Mth.floor(cameraPos.y);
/* 35 */       int cameraZ = Mth.floor(cameraPos.z);
/* 44 */       ByteBuffer data = Std140Builder.onStack(stack, UBO_SIZE).putIVec3(cameraX, cameraY, cameraZ).putVec3((float)(cameraX - cameraPos.x), (float)(cameraY - cameraPos.y), (float)(cameraZ - cameraPos.z)).putVec2(width, height).putFloat((float)glintAlpha).putFloat(((float)(gameTime % 24000L) + deltaTracker.getGameTimeDeltaPartialTick(false)) / 24000.0F).putInt(menuBlurRadius).putInt(useRgss ? 1 : 0).get();
/* 45 */       RenderSystem.getDevice().createCommandEncoder().writeToBuffer(this.buffer.slice(), data);
/* 46 */       if (stack != null)
/* 46 */         stack.close(); 
/*    */     } catch (Throwable throwable) {
/*    */       if (stack != null)
/*    */         try {
/*    */           stack.close();
/*    */         } catch (Throwable throwable1) {
/*    */           throwable.addSuppressed(throwable1);
/*    */         }  
/*    */       throw throwable;
/*    */     } 
/* 47 */     RenderSystem.setGlobalSettingsUniform(this.buffer);
/*    */   }
/*    */   
/*    */   public void close() {
/* 52 */     this.buffer.close();
/*    */   }
/*    */ }


/* Location:              C:\Users\karol\AppData\Roaming\ModrinthApp\meta\versions\26.2-0.19.5\26.2-0.19.5.jar!\net\minecraft\client\renderer\GlobalSettingsUniform.class
 * Java compiler version: 25 (69.0)
 * JD-Core Version:       1.1.3
 */