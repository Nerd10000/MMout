/*     */ package com.mojang.blaze3d.opengl;
/*     */ 
/*     */ import com.mojang.blaze3d.platform.MacosUtil;
/*     */ import com.mojang.blaze3d.systems.RenderSystem;
/*     */ import com.mojang.jtracy.Plot;
/*     */ import com.mojang.jtracy.TracyClient;
/*     */ import java.nio.ByteBuffer;
/*     */ import java.nio.charset.StandardCharsets;
/*     */ import java.util.Arrays;
/*     */ import java.util.stream.IntStream;
/*     */ import org.joml.Vector4fc;
/*     */ import org.lwjgl.PointerBuffer;
/*     */ import org.lwjgl.opengl.GL33C;
/*     */ import org.lwjgl.system.MemoryStack;
/*     */ import org.lwjgl.system.MemoryUtil;
/*     */ 
/*     */ public class GlStateManager {
/*  23 */   private static final Plot PLOT_TEXTURES = TracyClient.createPlot("GPU Textures");
/*     */   
/*  24 */   private static int numTextures = 0;
/*     */   
/*  25 */   private static final Plot PLOT_BUFFERS = TracyClient.createPlot("GPU Buffers");
/*     */   
/*  26 */   private static int numBuffers = 0;
/*     */   
/*  28 */   private static final BlendState[] BLEND = new BlendState[8];
/*     */   
/*  29 */   private static final DepthState DEPTH = new DepthState();
/*     */   
/*  30 */   private static final CullState CULL = new CullState();
/*     */   
/*  31 */   private static final PolygonOffsetState POLY_OFFSET = new PolygonOffsetState();
/*     */   
/*  32 */   private static final ColorLogicState COLOR_LOGIC = new ColorLogicState();
/*     */   
/*  33 */   private static final ScissorState SCISSOR = new ScissorState();
/*     */   
/*     */   private static int activeTexture;
/*     */   
/*     */   private static final int TEXTURE_COUNT = 12;
/*     */   
/*     */   private static final TextureState[] TEXTURES;
/*     */   
/*     */   static {
/*  38 */     TEXTURES = (TextureState[])IntStream.range(0, 12).mapToObj(i -> new TextureState()).toArray(x$0 -> new TextureState[x$0]);
/*     */   }
/*     */   
/*  40 */   private static final int[] COLOR_MASK = new int[8];
/*     */   
/*     */   private static int readFbo;
/*     */   
/*     */   private static int writeFbo;
/*     */   
/*     */   static {
/*  46 */     Arrays.setAll(COLOR_MASK, paramInt -> 15);
/*  47 */     Arrays.setAll(BLEND, paramInt -> new BlendState());
/*     */   }
/*     */   
/*     */   public static void _disableScissorTest() {
/*  51 */     RenderSystem.assertOnRenderThread();
/*  52 */     SCISSOR.mode.disable();
/*     */   }
/*     */   
/*     */   public static void _enableScissorTest() {
/*  56 */     RenderSystem.assertOnRenderThread();
/*  57 */     SCISSOR.mode.enable();
/*     */   }
/*     */   
/*     */   public static void _scissorBox(int x, int y, int width, int height) {
/*  61 */     RenderSystem.assertOnRenderThread();
/*  62 */     GL33C.glScissor(x, y, width, height);
/*     */   }
/*     */   
/*     */   public static void _disableDepthTest() {
/*  66 */     RenderSystem.assertOnRenderThread();
/*  67 */     DEPTH.mode.disable();
/*     */   }
/*     */   
/*     */   public static void _enableDepthTest() {
/*  71 */     RenderSystem.assertOnRenderThread();
/*  72 */     DEPTH.mode.enable();
/*     */   }
/*     */   
/*     */   public static void _depthFunc(int func) {
/*  76 */     RenderSystem.assertOnRenderThread();
/*  77 */     if (func != DEPTH.func) {
/*  78 */       DEPTH.func = func;
/*  79 */       GL33C.glDepthFunc(func);
/*     */     } 
/*     */   }
/*     */   
/*     */   public static void _depthMask(boolean mask) {
/*  84 */     RenderSystem.assertOnRenderThread();
/*  85 */     if (mask != DEPTH.mask) {
/*  86 */       DEPTH.mask = mask;
/*  87 */       GL33C.glDepthMask(mask);
/*     */     } 
/*     */   }
/*     */   
/*     */   public static void _disableBlend(int index) {
/*  92 */     RenderSystem.assertOnRenderThread();
/*  93 */     (BLEND[index]).mode.disable();
/*     */   }
/*     */   
/*     */   public static void _enableBlend(int index) {
/*  97 */     RenderSystem.assertOnRenderThread();
/*  98 */     (BLEND[index]).mode.enable();
/*     */   }
/*     */   
/*     */   public static void _blendFuncSeparate(int srcRgb, int dstRgb, int srcAlpha, int dstAlpha) {
/* 102 */     RenderSystem.assertOnRenderThread();
/* 104 */     BlendState firstBlend = BLEND[0];
/* 105 */     if (srcRgb != firstBlend.srcRgb || dstRgb != firstBlend.dstRgb || srcAlpha != firstBlend.srcAlpha || dstAlpha != firstBlend.dstAlpha) {
/* 106 */       firstBlend.srcRgb = srcRgb;
/* 107 */       firstBlend.dstRgb = dstRgb;
/* 108 */       firstBlend.srcAlpha = srcAlpha;
/* 109 */       firstBlend.dstAlpha = dstAlpha;
/* 110 */       glBlendFuncSeparate(srcRgb, dstRgb, srcAlpha, dstAlpha);
/*     */     } 
/*     */   }
/*     */   
/*     */   public static void _blendEquationSeparate(int modeRgb, int modeAlpha) {
/* 115 */     RenderSystem.assertOnRenderThread();
/* 117 */     BlendState firstBlend = BLEND[0];
/* 118 */     if (modeRgb != firstBlend.modeRgb || modeAlpha != firstBlend.modeAlpha) {
/* 119 */       firstBlend.modeRgb = modeRgb;
/* 120 */       firstBlend.modeAlpha = modeAlpha;
/* 121 */       glBlendEquationSeparate(modeRgb, modeAlpha);
/*     */     } 
/*     */   }
/*     */   
/*     */   public static int glGetProgrami(int program, int pname) {
/* 126 */     RenderSystem.assertOnRenderThread();
/* 127 */     return GL33C.glGetProgrami(program, pname);
/*     */   }
/*     */   
/*     */   public static void glAttachShader(int program, int shader) {
/* 131 */     RenderSystem.assertOnRenderThread();
/* 132 */     GL33C.glAttachShader(program, shader);
/*     */   }
/*     */   
/*     */   public static void glDeleteShader(int shader) {
/* 136 */     RenderSystem.assertOnRenderThread();
/* 137 */     GL33C.glDeleteShader(shader);
/*     */   }
/*     */   
/*     */   public static int glCreateShader(int type) {
/* 141 */     RenderSystem.assertOnRenderThread();
/* 142 */     return GL33C.glCreateShader(type);
/*     */   }
/*     */   
/*     */   public static void glShaderSource(int shader, String source) {
/* 146 */     RenderSystem.assertOnRenderThread();
/* 150 */     byte[] encoded = source.getBytes(StandardCharsets.UTF_8);
/* 151 */     ByteBuffer buffer = MemoryUtil.memAlloc(encoded.length + 1);
/* 152 */     buffer.put(encoded);
/* 153 */     buffer.put((byte)0);
/* 154 */     buffer.flip();
/*     */     try {
/* 156 */       MemoryStack stack = MemoryStack.stackPush();
/*     */       try {
/* 157 */         PointerBuffer pointers = stack.mallocPointer(1);
/* 158 */         pointers.put(buffer);
/* 159 */         GL33C.nglShaderSource(shader, 1, pointers.address0(), 0L);
/* 160 */         if (stack != null)
/* 160 */           stack.close(); 
/*     */       } catch (Throwable throwable) {
/*     */         if (stack != null)
/*     */           try {
/*     */             stack.close();
/*     */           } catch (Throwable throwable1) {
/*     */             throwable.addSuppressed(throwable1);
/*     */           }  
/*     */         throw throwable;
/*     */       } 
/*     */     } finally {
/* 161 */       MemoryUtil.memFree(buffer);
/*     */     } 
/*     */   }
/*     */   
/*     */   public static void glCompileShader(int shader) {
/* 166 */     RenderSystem.assertOnRenderThread();
/* 167 */     GL33C.glCompileShader(shader);
/*     */   }
/*     */   
/*     */   public static int glGetShaderi(int shader, int pname) {
/* 171 */     RenderSystem.assertOnRenderThread();
/* 172 */     return GL33C.glGetShaderi(shader, pname);
/*     */   }
/*     */   
/*     */   public static void _glUseProgram(int program) {
/* 176 */     RenderSystem.assertOnRenderThread();
/* 177 */     GL33C.glUseProgram(program);
/*     */   }
/*     */   
/*     */   public static int glCreateProgram() {
/* 181 */     RenderSystem.assertOnRenderThread();
/* 182 */     return GL33C.glCreateProgram();
/*     */   }
/*     */   
/*     */   public static void glDeleteProgram(int program) {
/* 186 */     RenderSystem.assertOnRenderThread();
/* 187 */     GL33C.glDeleteProgram(program);
/*     */   }
/*     */   
/*     */   public static void glLinkProgram(int program) {
/* 191 */     RenderSystem.assertOnRenderThread();
/* 192 */     GL33C.glLinkProgram(program);
/*     */   }
/*     */   
/*     */   public static int _glGetUniformLocation(int program, CharSequence name) {
/* 196 */     RenderSystem.assertOnRenderThread();
/* 197 */     return GL33C.glGetUniformLocation(program, name);
/*     */   }
/*     */   
/*     */   public static void _glUniform1i(int location, int v0) {
/* 201 */     RenderSystem.assertOnRenderThread();
/* 202 */     GL33C.glUniform1i(location, v0);
/*     */   }
/*     */   
/*     */   public static void _glBindAttribLocation(int program, int location, CharSequence name) {
/* 206 */     RenderSystem.assertOnRenderThread();
/* 207 */     GL33C.glBindAttribLocation(program, location, name);
/*     */   }
/*     */   
/*     */   static void incrementTrackedBuffers() {
/* 212 */     numBuffers++;
/* 213 */     PLOT_BUFFERS.setValue(numBuffers);
/*     */   }
/*     */   
/*     */   public static int _glGenBuffers() {
/* 217 */     RenderSystem.assertOnRenderThread();
/* 218 */     incrementTrackedBuffers();
/* 219 */     return GL33C.glGenBuffers();
/*     */   }
/*     */   
/*     */   public static int _glGenVertexArrays() {
/* 223 */     RenderSystem.assertOnRenderThread();
/* 224 */     return GL33C.glGenVertexArrays();
/*     */   }
/*     */   
/*     */   public static void _glBindBuffer(int target, int buffer) {
/* 228 */     RenderSystem.assertOnRenderThread();
/* 229 */     GL33C.glBindBuffer(target, buffer);
/*     */   }
/*     */   
/*     */   public static void _glBindVertexArray(int arrayId) {
/* 233 */     RenderSystem.assertOnRenderThread();
/* 234 */     GL33C.glBindVertexArray(arrayId);
/*     */   }
/*     */   
/*     */   public static void _glBufferData(int target, ByteBuffer data, int usage) {
/* 238 */     RenderSystem.assertOnRenderThread();
/* 239 */     GL33C.glBufferData(target, data, usage);
/*     */   }
/*     */   
/*     */   public static void _glBufferSubData(int target, long offset, ByteBuffer data) {
/* 243 */     RenderSystem.assertOnRenderThread();
/* 244 */     GL33C.glBufferSubData(target, offset, data);
/*     */   }
/*     */   
/*     */   public static void _glBufferData(int target, long size, int usage) {
/* 248 */     RenderSystem.assertOnRenderThread();
/* 249 */     GL33C.glBufferData(target, size, usage);
/*     */   }
/*     */   
/*     */   public static ByteBuffer _glMapBufferRange(int target, long offset, long length, int access) {
/* 253 */     RenderSystem.assertOnRenderThread();
/* 254 */     return GL33C.glMapBufferRange(target, offset, length, access);
/*     */   }
/*     */   
/*     */   public static void _glUnmapBuffer(int target) {
/* 258 */     RenderSystem.assertOnRenderThread();
/* 259 */     GL33C.glUnmapBuffer(target);
/*     */   }
/*     */   
/*     */   public static void _glDeleteBuffers(int buffer) {
/* 263 */     RenderSystem.assertOnRenderThread();
/* 264 */     numBuffers--;
/* 265 */     PLOT_BUFFERS.setValue(numBuffers);
/* 266 */     GL33C.glDeleteBuffers(buffer);
/*     */   }
/*     */   
/*     */   public static void _glBindFramebuffer(int target, int framebuffer) {
/* 270 */     if ((target == 36008 || target == 36160) && readFbo != framebuffer) {
/* 271 */       GL33C.glBindFramebuffer(36008, framebuffer);
/* 272 */       readFbo = framebuffer;
/*     */     } 
/* 274 */     if ((target == 36009 || target == 36160) && writeFbo != framebuffer) {
/* 275 */       GL33C.glBindFramebuffer(36009, framebuffer);
/* 276 */       writeFbo = framebuffer;
/*     */     } 
/*     */   }
/*     */   
/*     */   public static int getFrameBuffer(int target) {
/* 281 */     if (target == 36008)
/* 282 */       return readFbo; 
/* 284 */     if (target == 36009)
/* 285 */       return writeFbo; 
/* 287 */     return 0;
/*     */   }
/*     */   
/*     */   public static void _glBlitFrameBuffer(int srcX0, int srcY0, int srcX1, int srcY1, int dstX0, int dstY0, int dstX1, int dstY1, int mask, int filter) {
/* 291 */     RenderSystem.assertOnRenderThread();
/* 292 */     GL33C.glBlitFramebuffer(srcX0, srcY0, srcX1, srcY1, dstX0, dstY0, dstX1, dstY1, mask, filter);
/*     */   }
/*     */   
/*     */   public static void _glDeleteFramebuffers(int framebuffer) {
/* 296 */     RenderSystem.assertOnRenderThread();
/* 297 */     GL33C.glDeleteFramebuffers(framebuffer);
/* 298 */     if (readFbo == framebuffer)
/* 299 */       readFbo = 0; 
/* 301 */     if (writeFbo == framebuffer)
/* 302 */       writeFbo = 0; 
/*     */   }
/*     */   
/*     */   public static int glGenFramebuffers() {
/* 307 */     RenderSystem.assertOnRenderThread();
/* 309 */     return GL33C.glGenFramebuffers();
/*     */   }
/*     */   
/*     */   public static void _glFramebufferTexture2D(int target, int attachment, int textarget, int texture, int level) {
/* 313 */     RenderSystem.assertOnRenderThread();
/* 315 */     GL33C.glFramebufferTexture2D(target, attachment, textarget, texture, level);
/*     */   }
/*     */   
/*     */   public static void glBlendFuncSeparate(int srcColor, int dstColor, int srcAlpha, int dstAlpha) {
/* 319 */     RenderSystem.assertOnRenderThread();
/* 320 */     GL33C.glBlendFuncSeparate(srcColor, dstColor, srcAlpha, dstAlpha);
/*     */   }
/*     */   
/*     */   public static void glBlendEquationSeparate(int modeRgb, int modeAlpha) {
/* 324 */     RenderSystem.assertOnRenderThread();
/* 325 */     GL33C.glBlendEquationSeparate(modeRgb, modeAlpha);
/*     */   }
/*     */   
/*     */   public static String glGetShaderInfoLog(int shader, int maxLength) {
/* 329 */     RenderSystem.assertOnRenderThread();
/* 330 */     return GL33C.glGetShaderInfoLog(shader, maxLength);
/*     */   }
/*     */   
/*     */   public static String glGetProgramInfoLog(int program, int maxLength) {
/* 334 */     RenderSystem.assertOnRenderThread();
/* 335 */     return GL33C.glGetProgramInfoLog(program, maxLength);
/*     */   }
/*     */   
/*     */   public static void _enableCull() {
/* 339 */     RenderSystem.assertOnRenderThread();
/* 340 */     CULL.enable.enable();
/*     */   }
/*     */   
/*     */   public static void _disableCull() {
/* 344 */     RenderSystem.assertOnRenderThread();
/* 345 */     CULL.enable.disable();
/*     */   }
/*     */   
/*     */   public static void _polygonMode(int face, int mode) {
/* 349 */     RenderSystem.assertOnRenderThread();
/* 350 */     GL33C.glPolygonMode(face, mode);
/*     */   }
/*     */   
/*     */   public static void _enablePolygonOffset() {
/* 354 */     RenderSystem.assertOnRenderThread();
/* 355 */     POLY_OFFSET.fill.enable();
/*     */   }
/*     */   
/*     */   public static void _disablePolygonOffset() {
/* 359 */     RenderSystem.assertOnRenderThread();
/* 360 */     POLY_OFFSET.fill.disable();
/*     */   }
/*     */   
/*     */   public static void _polygonOffset(float factor, float units) {
/* 364 */     RenderSystem.assertOnRenderThread();
/* 365 */     if (factor != POLY_OFFSET.factor || units != POLY_OFFSET.units) {
/* 366 */       POLY_OFFSET.factor = factor;
/* 367 */       POLY_OFFSET.units = units;
/* 368 */       GL33C.glPolygonOffset(factor, units);
/*     */     } 
/*     */   }
/*     */   
/*     */   public static void _enableColorLogicOp() {
/* 373 */     RenderSystem.assertOnRenderThread();
/* 374 */     COLOR_LOGIC.enable.enable();
/*     */   }
/*     */   
/*     */   public static void _disableColorLogicOp() {
/* 378 */     RenderSystem.assertOnRenderThread();
/* 379 */     COLOR_LOGIC.enable.disable();
/*     */   }
/*     */   
/*     */   public static void _logicOp(int op) {
/* 383 */     RenderSystem.assertOnRenderThread();
/* 384 */     if (op != COLOR_LOGIC.op) {
/* 385 */       COLOR_LOGIC.op = op;
/* 386 */       GL33C.glLogicOp(op);
/*     */     } 
/*     */   }
/*     */   
/*     */   public static void _activeTexture(int texture) {
/* 391 */     RenderSystem.assertOnRenderThread();
/* 392 */     if (activeTexture != texture - 33984) {
/* 393 */       activeTexture = texture - 33984;
/* 394 */       GL33C.glActiveTexture(texture);
/*     */     } 
/*     */   }
/*     */   
/*     */   public static void _texParameter(int target, int name, int value) {
/* 399 */     RenderSystem.assertOnRenderThread();
/* 400 */     GL33C.glTexParameteri(target, name, value);
/*     */   }
/*     */   
/*     */   public static int _getTexLevelParameter(int target, int level, int name) {
/* 404 */     return GL33C.glGetTexLevelParameteri(target, level, name);
/*     */   }
/*     */   
/*     */   public static int _genTexture() {
/* 408 */     RenderSystem.assertOnRenderThread();
/* 409 */     numTextures++;
/* 410 */     PLOT_TEXTURES.setValue(numTextures);
/* 411 */     return GL33C.glGenTextures();
/*     */   }
/*     */   
/*     */   public static void _deleteTexture(int id) {
/* 415 */     RenderSystem.assertOnRenderThread();
/* 416 */     GL33C.glDeleteTextures(id);
/* 417 */     for (TextureState state : TEXTURES) {
/* 418 */       if (state.binding == id)
/* 419 */         state.binding = -1; 
/*     */     } 
/* 422 */     numTextures--;
/* 423 */     PLOT_TEXTURES.setValue(numTextures);
/*     */   }
/*     */   
/*     */   public static void _bindTexture(int id) {
/* 427 */     RenderSystem.assertOnRenderThread();
/* 428 */     if (id != (TEXTURES[activeTexture]).binding) {
/* 429 */       (TEXTURES[activeTexture]).binding = id;
/* 430 */       GL33C.glBindTexture(3553, id);
/*     */     } 
/*     */   }
/*     */   
/*     */   public static void _texImage2D(int target, int level, int internalformat, int width, int height, int border, int format, int type, ByteBuffer pixels) {
/* 435 */     RenderSystem.assertOnRenderThread();
/* 436 */     GL33C.glTexImage2D(target, level, internalformat, width, height, border, format, type, pixels);
/*     */   }
/*     */   
/*     */   public static void _texSubImage2D(int target, int level, int xoffset, int yoffset, int width, int height, int format, int type, long pixels) {
/* 440 */     RenderSystem.assertOnRenderThread();
/* 441 */     GL33C.glTexSubImage2D(target, level, xoffset, yoffset, width, height, format, type, pixels);
/*     */   }
/*     */   
/*     */   public static void _texSubImage2D(int target, int level, int xoffset, int yoffset, int width, int height, int format, int type, ByteBuffer pixels) {
/* 445 */     RenderSystem.assertOnRenderThread();
/* 446 */     GL33C.glTexSubImage2D(target, level, xoffset, yoffset, width, height, format, type, pixels);
/*     */   }
/*     */   
/*     */   public static void _viewport(int x, int y, int width, int height) {
/* 450 */     GL33C.glViewport(x, y, width, height);
/*     */   }
/*     */   
/*     */   public static void _colorMask(int writeMask) {
/* 454 */     RenderSystem.assertOnRenderThread();
/* 455 */     for (int i = 0; i < COLOR_MASK.length; i++) {
/* 456 */       if (writeMask != COLOR_MASK[i]) {
/* 457 */         COLOR_MASK[i] = writeMask;
/* 458 */         GL33C.glColorMaski(i, ((writeMask & 0x1) != 0), ((writeMask & 0x2) != 0), ((writeMask & 0x4) != 0), ((writeMask & 0x8) != 0));
/*     */       } 
/*     */     } 
/*     */   }
/*     */   
/*     */   public static void _colorMask(int index, int writeMask) {
/* 464 */     RenderSystem.assertOnRenderThread();
/* 465 */     if (writeMask != COLOR_MASK[index]) {
/* 466 */       COLOR_MASK[index] = writeMask;
/* 467 */       GL33C.glColorMaski(index, ((writeMask & 0x1) != 0), ((writeMask & 0x2) != 0), ((writeMask & 0x4) != 0), ((writeMask & 0x8) != 0));
/*     */     } 
/*     */   }
/*     */   
/*     */   public static void _clear(int mask) {
/* 472 */     RenderSystem.assertOnRenderThread();
/* 473 */     GL33C.glClear(mask);
/* 475 */     if (MacosUtil.IS_MACOS)
/* 476 */       _getError(); 
/*     */   }
/*     */   
/*     */   public static void _clearBuffer(int index, Vector4fc clearColor) {
/* 481 */     RenderSystem.assertOnRenderThread();
/* 482 */     GL33C.glClearBufferfv(6144, index, new float[] { clearColor.x(), clearColor.y(), clearColor.z(), clearColor.w() });
/* 484 */     if (MacosUtil.IS_MACOS)
/* 485 */       _getError(); 
/*     */   }
/*     */   
/*     */   public static void _clearBuffer(double clearDepth) {
/* 490 */     RenderSystem.assertOnRenderThread();
/* 491 */     GL33C.glClearBufferfv(6145, 0, new float[] { (float)clearDepth });
/* 493 */     if (MacosUtil.IS_MACOS)
/* 494 */       _getError(); 
/*     */   }
/*     */   
/*     */   public static void _vertexAttribPointer(int index, int size, int type, boolean normalized, int stride, long value) {
/* 499 */     RenderSystem.assertOnRenderThread();
/* 500 */     GL33C.glVertexAttribPointer(index, size, type, normalized, stride, value);
/*     */   }
/*     */   
/*     */   public static void _vertexAttribIPointer(int index, int size, int type, int stride, long value) {
/* 504 */     RenderSystem.assertOnRenderThread();
/* 505 */     GL33C.glVertexAttribIPointer(index, size, type, stride, value);
/*     */   }
/*     */   
/*     */   public static void _enableVertexAttribArray(int index) {
/* 509 */     RenderSystem.assertOnRenderThread();
/* 510 */     GL33C.glEnableVertexAttribArray(index);
/*     */   }
/*     */   
/*     */   public static void _drawElements(int mode, int count, int type, long indices) {
/* 514 */     RenderSystem.assertOnRenderThread();
/* 515 */     GL33C.glDrawElements(mode, count, type, indices);
/*     */   }
/*     */   
/*     */   public static void _drawArrays(int mode, int first, int count) {
/* 519 */     RenderSystem.assertOnRenderThread();
/* 520 */     GL33C.glDrawArrays(mode, first, count);
/*     */   }
/*     */   
/*     */   public static void _pixelStore(int name, int value) {
/* 524 */     RenderSystem.assertOnRenderThread();
/* 525 */     GL33C.glPixelStorei(name, value);
/*     */   }
/*     */   
/*     */   public static void _readPixels(int x, int y, int width, int height, int format, int type, long pixels) {
/* 529 */     RenderSystem.assertOnRenderThread();
/* 530 */     GL33C.glReadPixels(x, y, width, height, format, type, pixels);
/*     */   }
/*     */   
/*     */   public static int _getError() {
/* 534 */     RenderSystem.assertOnRenderThread();
/* 535 */     return GL33C.glGetError();
/*     */   }
/*     */   
/*     */   public static void clearGlErrors() {
/* 539 */     RenderSystem.assertOnRenderThread();
/* 541 */     while (GL33C.glGetError() != 0);
/*     */   }
/*     */   
/*     */   public static String _getString(int id) {
/* 546 */     RenderSystem.assertOnRenderThread();
/* 547 */     return GL33C.glGetString(id);
/*     */   }
/*     */   
/*     */   public static int _getInteger(int name) {
/* 551 */     RenderSystem.assertOnRenderThread();
/* 552 */     return GL33C.glGetInteger(name);
/*     */   }
/*     */   
/*     */   public static long _glFenceSync(int condition, int flags) {
/* 556 */     RenderSystem.assertOnRenderThread();
/* 557 */     return GL33C.glFenceSync(condition, flags);
/*     */   }
/*     */   
/*     */   public static int _glClientWaitSync(long sync, int flags, long timeout) {
/* 561 */     RenderSystem.assertOnRenderThread();
/* 562 */     return GL33C.glClientWaitSync(sync, flags, timeout);
/*     */   }
/*     */   
/*     */   public static void _glDeleteSync(long sync) {
/* 566 */     RenderSystem.assertOnRenderThread();
/* 567 */     GL33C.glDeleteSync(sync);
/*     */   }
/*     */   
/*     */   private static class TextureState {
/*     */     public int binding;
/*     */   }
/*     */   
/*     */   private static class BlendState {
/* 576 */     public final GlStateManager.BooleanState mode = new GlStateManager.BooleanState(3042);
/*     */     
/* 577 */     public int srcRgb = 1;
/*     */     
/* 578 */     public int dstRgb = 0;
/*     */     
/* 579 */     public int modeRgb = 32774;
/*     */     
/* 580 */     public int srcAlpha = 1;
/*     */     
/* 581 */     public int dstAlpha = 0;
/*     */     
/* 582 */     public int modeAlpha = 32774;
/*     */   }
/*     */   
/*     */   private static class DepthState {
/* 586 */     public final GlStateManager.BooleanState mode = new GlStateManager.BooleanState(2929);
/*     */     
/*     */     public boolean mask = true;
/*     */     
/* 588 */     public int func = 513;
/*     */   }
/*     */   
/*     */   private static class CullState {
/* 592 */     public final GlStateManager.BooleanState enable = new GlStateManager.BooleanState(2884);
/*     */   }
/*     */   
/*     */   private static class PolygonOffsetState {
/* 596 */     public final GlStateManager.BooleanState fill = new GlStateManager.BooleanState(32823);
/*     */     
/*     */     public float factor;
/*     */     
/*     */     public float units;
/*     */   }
/*     */   
/*     */   private static class ColorLogicState {
/* 602 */     public final GlStateManager.BooleanState enable = new GlStateManager.BooleanState(3058);
/*     */     
/* 603 */     public int op = 5379;
/*     */   }
/*     */   
/*     */   private static class ScissorState {
/* 607 */     public final GlStateManager.BooleanState mode = new GlStateManager.BooleanState(3089);
/*     */   }
/*     */   
/*     */   private static class BooleanState {
/*     */     private final int state;
/*     */     
/*     */     private boolean enabled;
/*     */     
/*     */     public BooleanState(int state) {
/* 615 */       this.state = state;
/*     */     }
/*     */     
/*     */     public void disable() {
/* 619 */       setEnabled(false);
/*     */     }
/*     */     
/*     */     public void enable() {
/* 623 */       setEnabled(true);
/*     */     }
/*     */     
/*     */     public void setEnabled(boolean enabled) {
/* 627 */       RenderSystem.assertOnRenderThread();
/* 628 */       if (enabled != this.enabled) {
/* 629 */         this.enabled = enabled;
/* 630 */         if (enabled) {
/* 631 */           GL33C.glEnable(this.state);
/*     */         } else {
/* 633 */           GL33C.glDisable(this.state);
/*     */         } 
/*     */       } 
/*     */     }
/*     */   }
/*     */ }


/* Location:              C:\Users\karol\AppData\Roaming\ModrinthApp\meta\versions\26.2-0.19.5\26.2-0.19.5.jar!\com\mojang\blaze3d\opengl\GlStateManager.class
 * Java compiler version: 25 (69.0)
 * JD-Core Version:       1.1.3
 */