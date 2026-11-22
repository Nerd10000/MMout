/*     */ package com.mojang.blaze3d.opengl;
/*     */ 
/*     */ import com.mojang.blaze3d.GpuFormat;
/*     */ import com.mojang.blaze3d.GpuOutOfMemoryException;
/*     */ import com.mojang.blaze3d.buffers.GpuBuffer;
/*     */ import com.mojang.blaze3d.pipeline.CompiledRenderPipeline;
/*     */ import com.mojang.blaze3d.pipeline.RenderPipeline;
/*     */ import com.mojang.blaze3d.preprocessor.GlslPreprocessor;
/*     */ import com.mojang.blaze3d.shaders.GpuDebugOptions;
/*     */ import com.mojang.blaze3d.shaders.ShaderSource;
/*     */ import com.mojang.blaze3d.shaders.ShaderType;
/*     */ import com.mojang.blaze3d.systems.CommandEncoderBackend;
/*     */ import com.mojang.blaze3d.systems.DeviceInfo;
/*     */ import com.mojang.blaze3d.systems.GpuDeviceBackend;
/*     */ import com.mojang.blaze3d.systems.GpuQueryPool;
/*     */ import com.mojang.blaze3d.systems.GpuSurfaceBackend;
/*     */ import com.mojang.blaze3d.textures.AddressMode;
/*     */ import com.mojang.blaze3d.textures.FilterMode;
/*     */ import com.mojang.blaze3d.textures.GpuSampler;
/*     */ import com.mojang.blaze3d.textures.GpuTexture;
/*     */ import com.mojang.blaze3d.textures.GpuTextureView;
/*     */ import com.mojang.logging.LogUtils;
/*     */ import java.nio.ByteBuffer;
/*     */ import java.util.Collections;
/*     */ import java.util.HashMap;
/*     */ import java.util.HashSet;
/*     */ import java.util.IdentityHashMap;
/*     */ import java.util.List;
/*     */ import java.util.Map;
/*     */ import java.util.OptionalDouble;
/*     */ import java.util.Set;
/*     */ import java.util.function.Supplier;
/*     */ import net.minecraft.client.renderer.ShaderDefines;
/*     */ import net.minecraft.client.renderer.ShaderManager;
/*     */ import net.minecraft.resources.Identifier;
/*     */ import net.minecraft.util.Mth;
/*     */ import org.apache.commons.lang3.StringUtils;
/*     */ import org.lwjgl.glfw.GLFW;
/*     */ import org.lwjgl.opengl.ARBClipControl;
/*     */ import org.lwjgl.opengl.GL;
/*     */ import org.lwjgl.opengl.GL33C;
/*     */ import org.lwjgl.opengl.GLCapabilities;
/*     */ import org.slf4j.Logger;
/*     */ 
/*     */ class GlDevice implements GpuDeviceBackend {
/*  50 */   private static final Logger LOGGER = LogUtils.getLogger();
/*     */   
/*     */   protected static boolean USE_GL_ARB_vertex_attrib_binding = true;
/*     */   
/*     */   protected static boolean USE_GL_KHR_debug = true;
/*     */   
/*     */   protected static boolean USE_GL_EXT_debug_label = true;
/*     */   
/*     */   protected static boolean USE_GL_ARB_debug_output = true;
/*     */   
/*     */   protected static boolean USE_GL_ARB_direct_state_access = true;
/*     */   
/*     */   protected static boolean USE_GL_ARB_buffer_storage = true;
/*     */   
/*     */   protected static boolean USE_GL_ARB_base_instance = true;
/*     */   
/*     */   protected static boolean USE_GL_ARB_draw_indirect = true;
/*     */   
/*     */   protected static boolean USE_GL_ARB_multi_draw_indirect = true;
/*     */   
/*     */   protected static boolean USE_GL_ARB_shader_draw_parameters = true;
/*     */   
/*     */   private final GlCommandEncoder encoder;
/*     */   
/*     */   private final GlDebug debugLog;
/*     */   
/*     */   private final GlDebugLabel debugLabels;
/*     */   
/*     */   private final DirectStateAccess directStateAccess;
/*     */   
/*     */   private final ShaderSource defaultShaderSource;
/*     */   
/*     */   private final Map<RenderPipeline, GlRenderPipeline> pipelineCache;
/*     */   
/*     */   private final Map<ShaderCompilationKey, GlShaderModule> shaderCache;
/*     */   
/*     */   private final FrameBufferCache frameBufferCache;
/*     */   
/*     */   private final VertexArrayCache vertexArrayCache;
/*     */   
/*     */   private final BufferStorage bufferStorage;
/*     */   
/*     */   private final DeviceInfo deviceInfo;
/*     */   
/*     */   public GlDevice(long windowHandle, ShaderSource defaultShaderSource, GpuDebugOptions debugOptions) {
/*     */     int maxSupportedAnisotropy;
/*  69 */     this.pipelineCache = new IdentityHashMap<>();
/*  70 */     this.shaderCache = new HashMap<>();
/*  71 */     this.frameBufferCache = new FrameBufferCache();
/*  77 */     GLFW.glfwMakeContextCurrent(windowHandle);
/*  78 */     GLCapabilities capabilities = GL.createCapabilities();
/*  80 */     Set<String> enabledExtensions = new HashSet<>();
/*  82 */     if (capabilities.GL_EXT_texture_filter_anisotropic) {
/*  83 */       maxSupportedAnisotropy = Mth.floor(GL33C.glGetFloat(34047));
/*  84 */       enabledExtensions.add("GL_EXT_texture_filter_anisotropic");
/*     */     } else {
/*  86 */       maxSupportedAnisotropy = 1;
/*     */     } 
/*  89 */     GlHeuristics heuristics = new GlHeuristics(GlStateManager._getString(7937));
/*  91 */     this.debugLog = GlDebug.enableDebugCallback(debugOptions.logLevel(), debugOptions.synchronousLogs(), enabledExtensions);
/*  92 */     this.debugLabels = GlDebugLabel.create(capabilities, debugOptions.useLabels(), enabledExtensions);
/*  93 */     this.vertexArrayCache = VertexArrayCache.create(capabilities, this.debugLabels, enabledExtensions);
/*  94 */     this.bufferStorage = BufferStorage.create(capabilities, enabledExtensions);
/*  95 */     this.directStateAccess = DirectStateAccess.create(capabilities, enabledExtensions, heuristics);
/*  97 */     this.defaultShaderSource = defaultShaderSource;
/*  99 */     GL33C.glEnable(34895);
/* 100 */     GL33C.glEnable(34370);
/* 101 */     if (capabilities.GL_ARB_clip_control) {
/* 102 */       ARBClipControl.glClipControl(36001, 37727);
/* 103 */       enabledExtensions.add("GL_ARB_clip_control");
/*     */     } 
/* 105 */     if (capabilities.GL_ARB_shader_draw_parameters && USE_GL_ARB_shader_draw_parameters)
/* 106 */       enabledExtensions.add("GL_ARB_shader_draw_parameters"); 
/* 108 */     if (capabilities.GL_ARB_draw_indirect && USE_GL_ARB_draw_indirect) {
/* 109 */       enabledExtensions.add("GL_ARB_draw_indirect");
/* 110 */       if (capabilities.GL_ARB_multi_draw_indirect && USE_GL_ARB_multi_draw_indirect)
/* 111 */         enabledExtensions.add("GL_ARB_multi_draw_indirect"); 
/*     */     } 
/* 114 */     if (capabilities.GL_ARB_base_instance && USE_GL_ARB_base_instance)
/* 115 */       enabledExtensions.add("GL_ARB_base_instance"); 
/* 118 */     this.deviceInfo = heuristics.createDeviceInfo(capabilities, maxSupportedAnisotropy, enabledExtensions);
/* 120 */     this.encoder = new GlCommandEncoder(this);
/*     */   }
/*     */   
/*     */   public GlDebugLabel debugLabels() {
/* 124 */     return this.debugLabels;
/*     */   }
/*     */   
/*     */   public GpuSurfaceBackend createSurface(long windowHandle) {
/* 129 */     return new GlSurface(windowHandle);
/*     */   }
/*     */   
/*     */   public CommandEncoderBackend createCommandEncoder() {
/* 135 */     return this.encoder;
/*     */   }
/*     */   
/*     */   public GpuSampler createSampler(AddressMode addressModeU, AddressMode addressModeV, FilterMode minFilter, FilterMode magFilter, int maxAnisotropy, OptionalDouble maxLod) {
/* 140 */     return new GlSampler(addressModeU, addressModeV, minFilter, magFilter, maxAnisotropy, maxLod);
/*     */   }
/*     */   
/*     */   public GpuTexture createTexture(Supplier<String> label, int usage, GpuFormat format, int width, int height, int depthOrLayers, int mipLevels) {
/* 145 */     return createTexture((this.debugLabels.exists() && label != null) ? label.get() : null, usage, format, width, height, depthOrLayers, mipLevels);
/*     */   }
/*     */   
/*     */   public GpuTexture createTexture(String label, int usage, GpuFormat format, int width, int height, int depthOrLayers, int mipLevels) {
/*     */     int target;
/* 151 */     GlStateManager.clearGlErrors();
/* 152 */     int id = GlStateManager._genTexture();
/* 153 */     if (label == null)
/* 154 */       label = String.valueOf(id); 
/* 157 */     boolean isCubemap = ((usage & 0x10) != 0);
/* 158 */     if (isCubemap) {
/* 159 */       GL33C.glBindTexture(34067, id);
/* 160 */       target = 34067;
/*     */     } else {
/* 162 */       GlStateManager._bindTexture(id);
/* 163 */       target = 3553;
/*     */     } 
/* 165 */     GlStateManager._texParameter(target, 33085, mipLevels - 1);
/* 166 */     GlStateManager._texParameter(target, 33082, 0);
/* 167 */     GlStateManager._texParameter(target, 33083, mipLevels - 1);
/* 168 */     if (format.hasDepthAspect())
/* 169 */       GlStateManager._texParameter(target, 34892, 0); 
/* 172 */     int glInternalID = GlConst.toGlInternalId(format);
/* 173 */     int glExternalID = GlConst.toGlExternalId(format);
/* 174 */     int glType = GlConst.toGlType(format);
/* 176 */     if (glInternalID == 0 || glExternalID == 0 || glType == 0)
/* 177 */       throw new IllegalArgumentException(String.valueOf(format) + " format cannot be used to create textures"); 
/* 180 */     if (isCubemap) {
/* 181 */       for (int cubeTarget : GlConst.CUBEMAP_TARGETS) {
/* 182 */         for (int i = 0; i < mipLevels; i++)
/* 183 */           GlStateManager._texImage2D(cubeTarget, i, glInternalID, width >> i, height >> i, 0, glExternalID, glType, null); 
/*     */       } 
/*     */     } else {
/* 187 */       for (int i = 0; i < mipLevels; i++)
/* 188 */         GlStateManager._texImage2D(target, i, glInternalID, width >> i, height >> i, 0, glExternalID, glType, null); 
/*     */     } 
/* 192 */     int error = GlStateManager._getError();
/* 193 */     if (error == 1285)
/* 194 */       throw new GpuOutOfMemoryException("Could not allocate texture of " + width + "x" + height + " for " + label); 
/* 195 */     if (error != 0)
/* 196 */       throw new IllegalStateException("OpenGL error " + error); 
/* 199 */     GlTexture texture = new GlTexture(usage, label, format, width, height, depthOrLayers, mipLevels, id, this.frameBufferCache);
/* 200 */     this.debugLabels.applyLabel(texture);
/* 201 */     return texture;
/*     */   }
/*     */   
/*     */   public GpuTextureView createTextureView(GpuTexture texture) {
/* 206 */     return createTextureView(texture, 0, texture.getMipLevels());
/*     */   }
/*     */   
/*     */   public GpuTextureView createTextureView(GpuTexture texture, int baseMipLevel, int mipLevels) {
/* 211 */     return new GlTextureView((GlTexture)texture, baseMipLevel, mipLevels, this.frameBufferCache);
/*     */   }
/*     */   
/*     */   public GpuBuffer createBuffer(Supplier<String> label, int usage, long size) {
/* 217 */     GlStateManager.clearGlErrors();
/* 218 */     GlBuffer buffer = this.bufferStorage.createBuffer(this.directStateAccess, usage, size);
/* 219 */     int error = GlStateManager._getError();
/* 220 */     if (error == 1285)
/* 221 */       throw new GpuOutOfMemoryException("Could not allocate buffer of " + size + " for " + String.valueOf(label)); 
/* 222 */     if (error != 0)
/* 223 */       throw new IllegalStateException("OpenGL error " + error); 
/* 225 */     this.debugLabels.applyLabel(buffer, label);
/* 226 */     return buffer;
/*     */   }
/*     */   
/*     */   public GpuBuffer createBuffer(Supplier<String> label, int usage, ByteBuffer data) {
/* 232 */     GlStateManager.clearGlErrors();
/* 233 */     long size = data.remaining();
/* 234 */     GlBuffer buffer = this.bufferStorage.createBuffer(this.directStateAccess, usage, data);
/* 235 */     int error = GlStateManager._getError();
/* 236 */     if (error == 1285)
/* 237 */       throw new GpuOutOfMemoryException("Could not allocate buffer of " + size + " for " + String.valueOf(label)); 
/* 238 */     if (error != 0)
/* 239 */       throw new IllegalStateException("OpenGL error " + error); 
/* 241 */     this.debugLabels.applyLabel(buffer, label);
/* 242 */     return buffer;
/*     */   }
/*     */   
/*     */   public List<String> getLastDebugMessages() {
/* 247 */     return (this.debugLog == null) ? Collections.<String>emptyList() : this.debugLog.getLastOpenGlDebugMessages();
/*     */   }
/*     */   
/*     */   public boolean isDebuggingEnabled() {
/* 252 */     return (this.debugLog != null);
/*     */   }
/*     */   
/*     */   public void clearPipelineCache() {
/* 257 */     for (GlRenderPipeline pipeline : this.pipelineCache.values()) {
/* 258 */       if (pipeline.program() != GlProgram.INVALID_PROGRAM)
/* 259 */         pipeline.program().close(); 
/*     */     } 
/* 262 */     this.pipelineCache.clear();
/* 263 */     for (GlShaderModule shader : this.shaderCache.values()) {
/* 264 */       if (shader != GlShaderModule.INVALID_SHADER)
/* 265 */         shader.close(); 
/*     */     } 
/* 268 */     this.shaderCache.clear();
/* 270 */     String glRenderer = GlStateManager._getString(7937);
/* 271 */     if (glRenderer.contains("AMD"))
/* 272 */       sacrificeShaderToOpenGlAndAmd(); 
/*     */   }
/*     */   
/*     */   private static void sacrificeShaderToOpenGlAndAmd() {
/* 279 */     int shader = GlStateManager.glCreateShader(35633);
/* 280 */     int program = GlStateManager.glCreateProgram();
/* 281 */     GlStateManager.glAttachShader(program, shader);
/* 282 */     GlStateManager.glDeleteShader(shader);
/* 283 */     GlStateManager.glDeleteProgram(program);
/*     */   }
/*     */   
/*     */   public void close() {
/* 288 */     clearPipelineCache();
/* 289 */     this.encoder.close();
/*     */   }
/*     */   
/*     */   public DirectStateAccess directStateAccess() {
/* 293 */     return this.directStateAccess;
/*     */   }
/*     */   
/*     */   protected GlRenderPipeline getOrCompilePipeline(RenderPipeline pipeline) {
/* 297 */     return this.pipelineCache.computeIfAbsent(pipeline, p -> compilePipeline(p, this.defaultShaderSource));
/*     */   }
/*     */   
/*     */   protected GlShaderModule getOrCompileShader(Identifier id, ShaderType type, ShaderDefines defines, ShaderSource shaderSource) {
/* 301 */     ShaderCompilationKey key = new ShaderCompilationKey(id, type, defines);
/* 302 */     return this.shaderCache.computeIfAbsent(key, k -> compileShader(k, shaderSource));
/*     */   }
/*     */   
/*     */   public GlRenderPipeline precompilePipeline(RenderPipeline pipeline, ShaderSource customShaderSource) {
/* 307 */     ShaderSource shaderSource = (customShaderSource == null) ? this.defaultShaderSource : customShaderSource;
/* 308 */     return this.pipelineCache.computeIfAbsent(pipeline, p -> compilePipeline(p, shaderSource));
/*     */   }
/*     */   
/*     */   private GlShaderModule compileShader(ShaderCompilationKey key, ShaderSource shaderSource) {
/* 312 */     String source = shaderSource.get(key.id, key.type);
/* 313 */     if (source == null) {
/* 314 */       LOGGER.error("Couldn't find source for {} shader ({})", key.type, key.id);
/* 315 */       return GlShaderModule.INVALID_SHADER;
/*     */     } 
/* 317 */     String sourceWithDefines = GlslPreprocessor.injectDefines(source, key.defines);
/* 318 */     int shaderId = GlStateManager.glCreateShader(GlConst.toGl(key.type));
/* 319 */     GlStateManager.glShaderSource(shaderId, sourceWithDefines);
/* 320 */     GlStateManager.glCompileShader(shaderId);
/* 322 */     if (GlStateManager.glGetShaderi(shaderId, 35713) == 0) {
/* 323 */       String logInfo = StringUtils.trim(GlStateManager.glGetShaderInfoLog(shaderId, 32768));
/* 324 */       LOGGER.error("Couldn't compile {} shader ({}): {}", new Object[] { key.type.getName(), key.id, logInfo });
/* 325 */       return GlShaderModule.INVALID_SHADER;
/*     */     } 
/* 328 */     GlShaderModule module = new GlShaderModule(shaderId, key.id, key.type);
/* 329 */     this.debugLabels.applyLabel(module);
/* 330 */     return module;
/*     */   }
/*     */   
/*     */   private GlProgram compileProgram(RenderPipeline pipeline, ShaderSource shaderSource) {
/* 334 */     GlShaderModule vertexShader = getOrCompileShader(pipeline.getVertexShader(), ShaderType.VERTEX, pipeline.getShaderDefines(), shaderSource);
/* 335 */     GlShaderModule fragmentShader = getOrCompileShader(pipeline.getFragmentShader(), ShaderType.FRAGMENT, pipeline.getShaderDefines(), shaderSource);
/* 336 */     if (vertexShader == GlShaderModule.INVALID_SHADER) {
/* 337 */       LOGGER.error("Couldn't compile pipeline {}: vertex shader {} was invalid", pipeline.getLocation(), pipeline.getVertexShader());
/* 338 */       return GlProgram.INVALID_PROGRAM;
/*     */     } 
/* 340 */     if (fragmentShader == GlShaderModule.INVALID_SHADER) {
/* 341 */       LOGGER.error("Couldn't compile pipeline {}: fragment shader {} was invalid", pipeline.getLocation(), pipeline.getFragmentShader());
/* 342 */       return GlProgram.INVALID_PROGRAM;
/*     */     } 
/*     */     try {
/* 345 */       GlProgram compiled = GlProgram.link(vertexShader, fragmentShader, pipeline.getVertexFormatBindings(), pipeline.getLocation().toString());
/* 346 */       compiled.setupBindGroupLayouts(pipeline.getBindGroupLayouts());
/* 347 */       this.debugLabels.applyLabel(compiled);
/* 348 */       return compiled;
/* 349 */     } catch (IllegalArgumentException e) {
/* 350 */       LOGGER.error("Couldn't compile program for pipeline {}: {}", pipeline.getLocation(), e.getMessage());
/* 351 */       return GlProgram.INVALID_PROGRAM;
/* 352 */     } catch (net.minecraft.client.renderer.ShaderManager.CompilationException e) {
/* 353 */       LOGGER.error("Couldn't compile program for pipeline {}: {}", pipeline.getLocation(), e);
/* 354 */       return GlProgram.INVALID_PROGRAM;
/*     */     } 
/*     */   }
/*     */   
/*     */   private GlRenderPipeline compilePipeline(RenderPipeline pipeline, ShaderSource shaderSource) {
/* 359 */     return new GlRenderPipeline(pipeline, compileProgram(pipeline, shaderSource));
/*     */   }
/*     */   
/*     */   public VertexArrayCache vertexArrayCache() {
/* 363 */     return this.vertexArrayCache;
/*     */   }
/*     */   
/*     */   public BufferStorage getBufferStorage() {
/* 367 */     return this.bufferStorage;
/*     */   }
/*     */   
/*     */   public FrameBufferCache frameBufferCache() {
/* 371 */     return this.frameBufferCache;
/*     */   }
/*     */   
/*     */   private static final class ShaderCompilationKey extends Record {
/*     */     private final Identifier id;
/*     */     
/*     */     private final ShaderType type;
/*     */     
/*     */     private final ShaderDefines defines;
/*     */     
/*     */     private ShaderCompilationKey(Identifier id, ShaderType type, ShaderDefines defines) {
/* 374 */       this.id = id;
/* 374 */       this.type = type;
/* 374 */       this.defines = defines;
/*     */     }
/*     */     
/*     */     public final int hashCode() {
/*     */       // Byte code:
/*     */       //   0: aload_0
/*     */       //   1: <illegal opcode> hashCode : (Lcom/mojang/blaze3d/opengl/GlDevice$ShaderCompilationKey;)I
/*     */       //   6: ireturn
/*     */       // Line number table:
/*     */       //   Java source line number -> byte code offset
/*     */       //   #374	-> 0
/*     */       // Local variable table:
/*     */       //   start	length	slot	name	descriptor
/*     */       //   0	7	0	this	Lcom/mojang/blaze3d/opengl/GlDevice$ShaderCompilationKey;
/*     */     }
/*     */     
/*     */     public final boolean equals(Object o) {
/*     */       // Byte code:
/*     */       //   0: aload_0
/*     */       //   1: aload_1
/*     */       //   2: <illegal opcode> equals : (Lcom/mojang/blaze3d/opengl/GlDevice$ShaderCompilationKey;Ljava/lang/Object;)Z
/*     */       //   7: ireturn
/*     */       // Line number table:
/*     */       //   Java source line number -> byte code offset
/*     */       //   #374	-> 0
/*     */       // Local variable table:
/*     */       //   start	length	slot	name	descriptor
/*     */       //   0	8	0	this	Lcom/mojang/blaze3d/opengl/GlDevice$ShaderCompilationKey;
/*     */       //   0	8	1	o	Ljava/lang/Object;
/*     */     }
/*     */     
/*     */     public Identifier id() {
/* 374 */       return this.id;
/*     */     }
/*     */     
/*     */     public ShaderType type() {
/* 374 */       return this.type;
/*     */     }
/*     */     
/*     */     public ShaderDefines defines() {
/* 374 */       return this.defines;
/*     */     }
/*     */     
/*     */     public String toString() {
/* 377 */       String string = String.valueOf(this.id) + " (" + String.valueOf(this.id) + ")";
/* 378 */       if (!this.defines.isEmpty())
/* 379 */         return string + " with " + string; 
/* 381 */       return string;
/*     */     }
/*     */   }
/*     */   
/*     */   public GpuQueryPool createTimestampQueryPool(int size) {
/* 387 */     return new GlQueryPool(size);
/*     */   }
/*     */   
/*     */   public long getTimestampNow() {
/* 392 */     return GL33C.glGetInteger64(36392);
/*     */   }
/*     */   
/*     */   public DeviceInfo getDeviceInfo() {
/* 397 */     return this.deviceInfo;
/*     */   }
/*     */ }


/* Location:              C:\Users\karol\AppData\Roaming\ModrinthApp\meta\versions\26.2-0.19.5\26.2-0.19.5.jar!\com\mojang\blaze3d\opengl\GlDevice.class
 * Java compiler version: 25 (69.0)
 * JD-Core Version:       1.1.3
 */