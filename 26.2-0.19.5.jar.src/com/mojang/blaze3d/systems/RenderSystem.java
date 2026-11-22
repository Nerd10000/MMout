/*     */ package com.mojang.blaze3d.systems;
/*     */ 
/*     */ import com.mojang.blaze3d.IndexType;
/*     */ import com.mojang.blaze3d.PrimitiveTopology;
/*     */ import com.mojang.blaze3d.ProjectionType;
/*     */ import com.mojang.blaze3d.buffers.GpuBuffer;
/*     */ import com.mojang.blaze3d.buffers.GpuBufferSlice;
/*     */ import com.mojang.blaze3d.buffers.GpuFence;
/*     */ import com.mojang.blaze3d.buffers.Std140SizeCalculator;
/*     */ import com.mojang.blaze3d.platform.GLX;
/*     */ import com.mojang.blaze3d.textures.GpuTextureView;
/*     */ import com.mojang.logging.LogUtils;
/*     */ import it.unimi.dsi.fastutil.ints.IntConsumer;
/*     */ import java.nio.ByteBuffer;
/*     */ import java.util.Locale;
/*     */ import java.util.Objects;
/*     */ import java.util.concurrent.atomic.AtomicBoolean;
/*     */ import java.util.concurrent.atomic.AtomicLong;
/*     */ import java.util.function.IntConsumer;
/*     */ import net.minecraft.client.renderer.DynamicUniforms;
/*     */ import net.minecraft.util.ArrayListDeque;
/*     */ import net.minecraft.util.Mth;
/*     */ import net.minecraft.util.TimeSource;
/*     */ import net.minecraft.util.Util;
/*     */ import org.joml.Matrix4f;
/*     */ import org.joml.Matrix4fStack;
/*     */ import org.joml.Matrix4fc;
/*     */ import org.lwjgl.glfw.GLFW;
/*     */ import org.lwjgl.glfw.GLFWErrorCallbackI;
/*     */ import org.lwjgl.system.MemoryUtil;
/*     */ import org.slf4j.Logger;
/*     */ 
/*     */ public class RenderSystem {
/*  34 */   private static final Logger LOGGER = LogUtils.getLogger();
/*     */   
/*     */   public static final double DEFAULT_DEPTH_CLEAR_VALUE = 0.0D;
/*     */   
/*     */   public static final int MINIMUM_ATLAS_TEXTURE_SIZE = 1024;
/*     */   
/*  38 */   public static final int PROJECTION_MATRIX_UBO_SIZE = (new Std140SizeCalculator()).putMat4f().get();
/*     */   
/*     */   private static Thread renderThread;
/*     */   
/*     */   private static GpuDevice DEVICE;
/*     */   
/*  44 */   private static final AutoStorageIndexBuffer sharedSequential = new AutoStorageIndexBuffer(1, 1, IntConsumer::accept);
/*     */   
/*     */   private static final AutoStorageIndexBuffer sharedSequentialQuad;
/*     */   
/*     */   private static final AutoStorageIndexBuffer sharedSequentialLines;
/*     */   
/*     */   static {
/*  45 */     sharedSequentialQuad = new AutoStorageIndexBuffer(4, 6, (c, i) -> {
/*     */           c.accept(i);
/*     */           c.accept(i + 1);
/*     */           c.accept(i + 2);
/*     */           c.accept(i + 2);
/*     */           c.accept(i + 3);
/*     */           c.accept(i);
/*     */         });
/*  53 */     sharedSequentialLines = new AutoStorageIndexBuffer(4, 6, (c, i) -> {
/*     */           c.accept(i);
/*     */           c.accept(i + 1);
/*     */           c.accept(i + 2);
/*     */           c.accept(i + 3);
/*     */           c.accept(i + 2);
/*     */           c.accept(i + 1);
/*     */         });
/*     */   }
/*     */   
/*  62 */   private static ProjectionType projectionType = ProjectionType.PERSPECTIVE;
/*     */   
/*  63 */   private static ProjectionType savedProjectionType = ProjectionType.PERSPECTIVE;
/*     */   
/*  66 */   private static final Matrix4fStack modelViewStack = new Matrix4fStack(16);
/*     */   
/*  68 */   private static GpuBufferSlice shaderFog = null;
/*     */   
/*     */   private static GpuBufferSlice shaderLightDirections;
/*     */   
/*     */   private static GpuBufferSlice projectionMatrixBuffer;
/*     */   
/*     */   private static GpuBufferSlice savedProjectionMatrixBuffer;
/*     */   
/*  76 */   private static final AtomicLong pollEventsWaitStart = new AtomicLong();
/*     */   
/*  77 */   private static final AtomicBoolean pollingEvents = new AtomicBoolean(false);
/*     */   
/*  79 */   private static final ArrayListDeque<GpuAsyncTask> PENDING_FENCES = new ArrayListDeque();
/*     */   
/*     */   public static GpuTextureView outputColorTextureOverride;
/*     */   
/*     */   public static GpuTextureView outputDepthTextureOverride;
/*     */   
/*     */   private static GpuBuffer globalSettingsUniform;
/*     */   
/*     */   private static DynamicUniforms dynamicUniforms;
/*     */   
/*  86 */   private static final ScissorState scissorStateForRenderTypeDraws = new ScissorState();
/*     */   
/*  87 */   private static final SamplerCache samplerCache = new SamplerCache();
/*     */   
/*     */   public static SamplerCache getSamplerCache() {
/*  90 */     return samplerCache;
/*     */   }
/*     */   
/*     */   public static void initRenderThread() {
/*  94 */     if (renderThread != null)
/*  95 */       throw new IllegalStateException("Could not initialize render thread"); 
/*  97 */     renderThread = Thread.currentThread();
/*     */   }
/*     */   
/*     */   public static boolean isOnRenderThread() {
/* 101 */     return (Thread.currentThread() == renderThread);
/*     */   }
/*     */   
/*     */   public static void assertOnRenderThread() {
/* 105 */     if (!isOnRenderThread())
/* 106 */       throw constructThreadException(); 
/*     */   }
/*     */   
/*     */   private static IllegalStateException constructThreadException() {
/* 111 */     return new IllegalStateException("Rendersystem called from wrong thread");
/*     */   }
/*     */   
/*     */   public static void pollEvents() {
/* 115 */     pollEventsWaitStart.set(Util.getMillis());
/* 116 */     pollingEvents.set(true);
/* 117 */     GLFW.glfwPollEvents();
/* 119 */     pollingEvents.set(false);
/*     */   }
/*     */   
/*     */   public static boolean isFrozenAtPollEvents() {
/* 123 */     return (pollingEvents.get() && Util.getMillis() - pollEventsWaitStart.get() > 200L);
/*     */   }
/*     */   
/*     */   public static void setShaderFog(GpuBufferSlice fog) {
/* 129 */     shaderFog = fog;
/*     */   }
/*     */   
/*     */   public static GpuBufferSlice getShaderFog() {
/* 133 */     return shaderFog;
/*     */   }
/*     */   
/*     */   public static void setShaderLights(GpuBufferSlice buffer) {
/* 137 */     shaderLightDirections = buffer;
/*     */   }
/*     */   
/*     */   public static GpuBufferSlice getShaderLights() {
/* 141 */     return shaderLightDirections;
/*     */   }
/*     */   
/*     */   public static void enableScissorForRenderTypeDraws(int x, int y, int width, int height) {
/* 145 */     scissorStateForRenderTypeDraws.enable(x, y, width, height);
/*     */   }
/*     */   
/*     */   public static void disableScissorForRenderTypeDraws() {
/* 149 */     scissorStateForRenderTypeDraws.disable();
/*     */   }
/*     */   
/*     */   public static ScissorState getScissorStateForRenderTypeDraws() {
/* 153 */     return scissorStateForRenderTypeDraws;
/*     */   }
/*     */   
/*     */   public static String getBackendDescription() {
/* 157 */     return String.format(Locale.ROOT, "LWJGL version %s", new Object[] { GLX._getLWJGLVersion() });
/*     */   }
/*     */   
/*     */   public static TimeSource.NanoTimeSource initBackendSystem() {
/* 161 */     Objects.requireNonNull(GLX._initGlfw());
/* 161 */     return GLX._initGlfw()::getAsLong;
/*     */   }
/*     */   
/*     */   public static void initRenderer(GpuDevice device) {
/* 165 */     if (DEVICE != null)
/* 166 */       throw new IllegalStateException("RenderSystem.DEVICE already initialized"); 
/* 168 */     DEVICE = device;
/* 169 */     dynamicUniforms = new DynamicUniforms();
/* 170 */     samplerCache.initialize();
/*     */   }
/*     */   
/*     */   public static void shutdownRenderer() {
/* 174 */     sharedSequential.close();
/* 175 */     sharedSequentialQuad.close();
/* 176 */     sharedSequentialLines.close();
/* 177 */     samplerCache.close();
/* 178 */     if (dynamicUniforms != null)
/* 179 */       dynamicUniforms.close(); 
/* 181 */     if (DEVICE != null)
/* 182 */       DEVICE.close(); 
/*     */   }
/*     */   
/*     */   public static void setErrorCallback(GLFWErrorCallbackI onFullscreenError) {
/* 187 */     GLX._setGlfwErrorCallback(onFullscreenError);
/*     */   }
/*     */   
/*     */   public static void setupDefaultState() {
/* 191 */     modelViewStack.clear();
/*     */   }
/*     */   
/*     */   public static void setProjectionMatrix(GpuBufferSlice projectionMatrixBuffer, ProjectionType type) {
/* 195 */     assertOnRenderThread();
/* 196 */     RenderSystem.projectionMatrixBuffer = projectionMatrixBuffer;
/* 197 */     projectionType = type;
/*     */   }
/*     */   
/*     */   public static void backupProjectionMatrix() {
/* 201 */     assertOnRenderThread();
/* 202 */     savedProjectionMatrixBuffer = projectionMatrixBuffer;
/* 203 */     savedProjectionType = projectionType;
/*     */   }
/*     */   
/*     */   public static void restoreProjectionMatrix() {
/* 207 */     assertOnRenderThread();
/* 208 */     projectionMatrixBuffer = savedProjectionMatrixBuffer;
/* 209 */     projectionType = savedProjectionType;
/*     */   }
/*     */   
/*     */   public static GpuBufferSlice getProjectionMatrixBuffer() {
/* 213 */     assertOnRenderThread();
/* 214 */     return projectionMatrixBuffer;
/*     */   }
/*     */   
/*     */   public static Matrix4f getModelViewMatrixCopy() {
/* 218 */     assertOnRenderThread();
/* 219 */     return new Matrix4f((Matrix4fc)modelViewStack);
/*     */   }
/*     */   
/*     */   public static Matrix4fStack getModelViewStack() {
/* 223 */     assertOnRenderThread();
/* 224 */     return modelViewStack;
/*     */   }
/*     */   
/*     */   public static AutoStorageIndexBuffer getSequentialBuffer(PrimitiveTopology primitiveTopology) {
/* 228 */     assertOnRenderThread();
/* 229 */     switch (primitiveTopology) {
/*     */       case SHORT:
/*     */       
/*     */       case INT:
/*     */       
/*     */     } 
/* 229 */     return 
/*     */       
/* 232 */       sharedSequential;
/*     */   }
/*     */   
/*     */   public static void setGlobalSettingsUniform(GpuBuffer buffer) {
/* 237 */     globalSettingsUniform = buffer;
/*     */   }
/*     */   
/*     */   public static GpuBuffer getGlobalSettingsUniform() {
/* 241 */     return globalSettingsUniform;
/*     */   }
/*     */   
/*     */   public static ProjectionType getProjectionType() {
/* 245 */     assertOnRenderThread();
/* 246 */     return projectionType;
/*     */   }
/*     */   
/*     */   public static void queueFencedTask(Runnable task) {
/* 250 */     PENDING_FENCES.addLast(new GpuAsyncTask(task, getDevice().createCommandEncoder().createFence()));
/*     */   }
/*     */   
/*     */   public static void executePendingTasks() {
/* 255 */     GpuAsyncTask task = (GpuAsyncTask)PENDING_FENCES.peekFirst();
/* 256 */     while (task != null) {
/* 257 */       if (task.fence.awaitCompletion(0L)) {
/*     */         try {
/* 259 */           task.callback.run();
/*     */         } finally {
/* 261 */           task.fence.close();
/*     */         } 
/* 263 */         PENDING_FENCES.removeFirst();
/* 264 */         task = (GpuAsyncTask)PENDING_FENCES.peekFirst();
/*     */         continue;
/*     */       } 
/*     */       return;
/*     */     } 
/*     */   }
/*     */   
/*     */   public static GpuDevice getDevice() {
/* 273 */     if (DEVICE == null)
/* 274 */       throw new IllegalStateException("Can't getDevice() before it was initialized"); 
/* 276 */     return DEVICE;
/*     */   }
/*     */   
/*     */   public static GpuDevice tryGetDevice() {
/* 280 */     return DEVICE;
/*     */   }
/*     */   
/*     */   public static DynamicUniforms getDynamicUniforms() {
/* 284 */     if (dynamicUniforms == null)
/* 285 */       throw new IllegalStateException("Can't getDynamicUniforms() before device was initialized"); 
/* 287 */     return dynamicUniforms;
/*     */   }
/*     */   
/*     */   public static void bindDefaultUniforms(RenderPass renderPass) {
/* 292 */     GpuBufferSlice projectionMatrix = getProjectionMatrixBuffer();
/* 293 */     if (projectionMatrix != null)
/* 294 */       renderPass.setUniform("Projection", projectionMatrix); 
/* 296 */     GpuBufferSlice fog = getShaderFog();
/* 297 */     if (fog != null)
/* 298 */       renderPass.setUniform("Fog", fog); 
/* 300 */     GpuBuffer globalUniform = getGlobalSettingsUniform();
/* 301 */     if (globalUniform != null)
/* 302 */       renderPass.setUniform("Globals", globalUniform); 
/* 304 */     GpuBufferSlice shaderLights = getShaderLights();
/* 305 */     if (shaderLights != null)
/* 306 */       renderPass.setUniform("Lighting", shaderLights); 
/*     */   }
/*     */   
/*     */   public static final class AutoStorageIndexBuffer implements AutoCloseable {
/*     */     private final int vertexStride;
/*     */     
/*     */     private final int indexStride;
/*     */     
/*     */     private final IndexGenerator generator;
/*     */     
/*     */     private GpuBuffer buffer;
/*     */     
/* 317 */     private IndexType type = IndexType.SHORT;
/*     */     
/*     */     private int indexCount;
/*     */     
/*     */     private AutoStorageIndexBuffer(int vertexStride, int indexStride, IndexGenerator generator) {
/* 321 */       this.vertexStride = vertexStride;
/* 322 */       this.indexStride = indexStride;
/* 323 */       this.generator = generator;
/*     */     }
/*     */     
/*     */     public void close() {
/* 328 */       if (this.buffer != null)
/* 329 */         this.buffer.close(); 
/*     */     }
/*     */     
/*     */     public boolean hasStorage(int indexCount) {
/* 334 */       return (indexCount <= this.indexCount);
/*     */     }
/*     */     
/*     */     public GpuBuffer getBuffer(int indexCount) {
/* 338 */       ensureStorage(indexCount);
/* 339 */       return this.buffer;
/*     */     }
/*     */     
/*     */     private void ensureStorage(int indexCount) {
/* 343 */       if (hasStorage(indexCount))
/*     */         return; 
/* 347 */       indexCount = Mth.roundToward(indexCount * 2, this.indexStride);
/* 348 */       RenderSystem.LOGGER.debug("Growing IndexBuffer: Old limit {}, new limit {}.", Integer.valueOf(this.indexCount), Integer.valueOf(indexCount));
/* 350 */       int primitiveCount = indexCount / this.indexStride;
/* 351 */       int vertexCount = primitiveCount * this.vertexStride;
/* 353 */       IndexType type = IndexType.least(vertexCount);
/* 354 */       int bufferSize = Mth.roundToward(indexCount * type.bytes, 4);
/* 356 */       ByteBuffer data = MemoryUtil.memAlloc(bufferSize);
/*     */       try {
/* 359 */         this.type = type;
/* 361 */         IntConsumer intConsumer = intConsumer(data);
/*     */         int ii;
/* 362 */         for (ii = 0; ii < indexCount; ii += this.indexStride)
/* 363 */           this.generator.accept(intConsumer, ii * this.vertexStride / this.indexStride); 
/* 365 */         data.flip();
/* 367 */         if (this.buffer != null)
/* 368 */           this.buffer.close(); 
/* 370 */         this.buffer = RenderSystem.getDevice().createBuffer(() -> "Auto Storage index buffer", 64, data);
/*     */       } finally {
/* 372 */         MemoryUtil.memFree(data);
/*     */       } 
/* 375 */       this.indexCount = indexCount;
/*     */     }
/*     */     
/*     */     private IntConsumer intConsumer(ByteBuffer buffer) {
/*     */       // Byte code:
/*     */       //   0: getstatic com/mojang/blaze3d/systems/RenderSystem$1.$SwitchMap$com$mojang$blaze3d$IndexType : [I
/*     */       //   3: aload_0
/*     */       //   4: getfield type : Lcom/mojang/blaze3d/IndexType;
/*     */       //   7: invokevirtual ordinal : ()I
/*     */       //   10: iaload
/*     */       //   11: lookupswitch default -> 43, 1 -> 36, 2 -> 43
/*     */       //   36: aload_1
/*     */       //   37: <illegal opcode> accept : (Ljava/nio/ByteBuffer;)Lit/unimi/dsi/fastutil/ints/IntConsumer;
/*     */       //   42: areturn
/*     */       //   43: aload_1
/*     */       //   44: dup
/*     */       //   45: invokestatic requireNonNull : (Ljava/lang/Object;)Ljava/lang/Object;
/*     */       //   48: pop
/*     */       //   49: <illegal opcode> accept : (Ljava/nio/ByteBuffer;)Lit/unimi/dsi/fastutil/ints/IntConsumer;
/*     */       //   54: areturn
/*     */       // Line number table:
/*     */       //   Java source line number -> byte code offset
/*     */       //   #379	-> 0
/*     */       //   #381	-> 36
/*     */       //   #384	-> 43
/*     */       // Local variable table:
/*     */       //   start	length	slot	name	descriptor
/*     */       //   0	55	0	this	Lcom/mojang/blaze3d/systems/RenderSystem$AutoStorageIndexBuffer;
/*     */       //   0	55	1	buffer	Ljava/nio/ByteBuffer;
/*     */     }
/*     */     
/*     */     public IndexType type() {
/* 389 */       return this.type;
/*     */     }
/*     */     
/*     */     private static interface IndexGenerator {
/*     */       void accept(IntConsumer param2IntConsumer, int param2Int);
/*     */     }
/*     */   }
/*     */   
/*     */   private static final class GpuAsyncTask extends Record {
/*     */     private final Runnable callback;
/*     */     
/*     */     private final GpuFence fence;
/*     */     
/*     */     private GpuAsyncTask(Runnable callback, GpuFence fence) {
/* 397 */       this.callback = callback;
/* 397 */       this.fence = fence;
/*     */     }
/*     */     
/*     */     public final String toString() {
/*     */       // Byte code:
/*     */       //   0: aload_0
/*     */       //   1: <illegal opcode> toString : (Lcom/mojang/blaze3d/systems/RenderSystem$GpuAsyncTask;)Ljava/lang/String;
/*     */       //   6: areturn
/*     */       // Line number table:
/*     */       //   Java source line number -> byte code offset
/*     */       //   #397	-> 0
/*     */       // Local variable table:
/*     */       //   start	length	slot	name	descriptor
/*     */       //   0	7	0	this	Lcom/mojang/blaze3d/systems/RenderSystem$GpuAsyncTask;
/*     */     }
/*     */     
/*     */     public final int hashCode() {
/*     */       // Byte code:
/*     */       //   0: aload_0
/*     */       //   1: <illegal opcode> hashCode : (Lcom/mojang/blaze3d/systems/RenderSystem$GpuAsyncTask;)I
/*     */       //   6: ireturn
/*     */       // Line number table:
/*     */       //   Java source line number -> byte code offset
/*     */       //   #397	-> 0
/*     */       // Local variable table:
/*     */       //   start	length	slot	name	descriptor
/*     */       //   0	7	0	this	Lcom/mojang/blaze3d/systems/RenderSystem$GpuAsyncTask;
/*     */     }
/*     */     
/*     */     public final boolean equals(Object o) {
/*     */       // Byte code:
/*     */       //   0: aload_0
/*     */       //   1: aload_1
/*     */       //   2: <illegal opcode> equals : (Lcom/mojang/blaze3d/systems/RenderSystem$GpuAsyncTask;Ljava/lang/Object;)Z
/*     */       //   7: ireturn
/*     */       // Line number table:
/*     */       //   Java source line number -> byte code offset
/*     */       //   #397	-> 0
/*     */       // Local variable table:
/*     */       //   start	length	slot	name	descriptor
/*     */       //   0	8	0	this	Lcom/mojang/blaze3d/systems/RenderSystem$GpuAsyncTask;
/*     */       //   0	8	1	o	Ljava/lang/Object;
/*     */     }
/*     */     
/*     */     public Runnable callback() {
/* 397 */       return this.callback;
/*     */     }
/*     */     
/*     */     public GpuFence fence() {
/* 397 */       return this.fence;
/*     */     }
/*     */   }
/*     */   
/*     */   private static interface IndexGenerator {
/*     */     void accept(IntConsumer param1IntConsumer, int param1Int);
/*     */   }
/*     */ }


/* Location:              C:\Users\karol\AppData\Roaming\ModrinthApp\meta\versions\26.2-0.19.5\26.2-0.19.5.jar!\com\mojang\blaze3d\systems\RenderSystem.class
 * Java compiler version: 25 (69.0)
 * JD-Core Version:       1.1.3
 */