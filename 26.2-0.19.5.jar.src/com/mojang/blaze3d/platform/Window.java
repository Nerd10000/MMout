/*     */ package com.mojang.blaze3d.platform;
/*     */ 
/*     */ import com.mojang.blaze3d.GLFWErrorCapture;
/*     */ import com.mojang.blaze3d.GLFWErrorScope;
/*     */ import com.mojang.blaze3d.platform.cursor.CursorType;
/*     */ import com.mojang.blaze3d.systems.BackendCreationException;
/*     */ import com.mojang.blaze3d.systems.GpuBackend;
/*     */ import com.mojang.blaze3d.systems.RenderSystem;
/*     */ import com.mojang.logging.LogUtils;
/*     */ import java.io.IOException;
/*     */ import java.io.InputStream;
/*     */ import java.nio.ByteBuffer;
/*     */ import java.util.ArrayList;
/*     */ import java.util.List;
/*     */ import java.util.Locale;
/*     */ import java.util.Optional;
/*     */ import java.util.function.BiConsumer;
/*     */ import net.minecraft.CrashReport;
/*     */ import net.minecraft.CrashReportCategory;
/*     */ import net.minecraft.ReportedException;
/*     */ import net.minecraft.client.main.SilentInitException;
/*     */ import net.minecraft.server.packs.PackResources;
/*     */ import net.minecraft.server.packs.resources.IoSupplier;
/*     */ import net.minecraft.util.Util;
/*     */ import org.lwjgl.PointerBuffer;
/*     */ import org.lwjgl.glfw.Callbacks;
/*     */ import org.lwjgl.glfw.GLFW;
/*     */ import org.lwjgl.glfw.GLFWErrorCallback;
/*     */ import org.lwjgl.glfw.GLFWErrorCallbackI;
/*     */ import org.lwjgl.glfw.GLFWImage;
/*     */ import org.lwjgl.glfw.GLFWWindowCloseCallback;
/*     */ import org.lwjgl.system.MemoryStack;
/*     */ import org.lwjgl.system.MemoryUtil;
/*     */ import org.slf4j.Logger;
/*     */ 
/*     */ public final class Window implements AutoCloseable {
/*  39 */   private static final Logger LOGGER = LogUtils.getLogger();
/*     */   
/*     */   public static final int BASE_WIDTH = 320;
/*     */   
/*     */   public static final int BASE_HEIGHT = 240;
/*     */   
/*  44 */   private final GLFWErrorCallback defaultErrorCallback = GLFWErrorCallback.create(this::defaultErrorCallback);
/*     */   
/*     */   private final WindowEventHandler eventHandler;
/*     */   
/*     */   private final MonitorManager monitorManager;
/*     */   
/*     */   private final long handle;
/*     */   
/*     */   private int windowedX;
/*     */   
/*     */   private int windowedY;
/*     */   
/*     */   private int windowedWidth;
/*     */   
/*     */   private int windowedHeight;
/*     */   
/*     */   private Optional<VideoMode> preferredFullscreenVideoMode;
/*     */   
/*     */   private boolean fullscreen;
/*     */   
/*     */   private boolean actuallyFullscreen;
/*     */   
/*     */   private int x;
/*     */   
/*     */   private int y;
/*     */   
/*     */   private int width;
/*     */   
/*     */   private int height;
/*     */   
/*     */   private int framebufferWidth;
/*     */   
/*     */   private int framebufferHeight;
/*     */   
/*     */   private int guiScaledWidth;
/*     */   
/*     */   private int guiScaledHeight;
/*     */   
/*     */   private int guiScale;
/*     */   
/*  65 */   private String errorSection = "";
/*     */   
/*     */   private boolean dirty;
/*     */   
/*     */   private boolean vsync;
/*     */   
/*     */   private boolean iconified;
/*     */   
/*     */   private boolean focused = true;
/*     */   
/*     */   private boolean minimized;
/*     */   
/*     */   private boolean allowCursorChanges;
/*     */   
/*  76 */   private CursorType currentCursor = CursorType.DEFAULT;
/*     */   
/*     */   private final boolean exclusiveFullscreen;
/*     */   
/*     */   private final GpuBackend backend;
/*     */   
/*     */   public Window(WindowEventHandler eventHandler, DisplayData displayData, String fullscreenVideoModeString, boolean exclusiveFullscreen, String title, MonitorManager monitorManager, GpuBackend backend) throws BackendCreationException {
/*  83 */     this.monitorManager = monitorManager;
/*  84 */     this.exclusiveFullscreen = exclusiveFullscreen;
/*  86 */     setBootErrorCallback();
/*  87 */     setErrorSection("Pre startup");
/*  89 */     this.eventHandler = eventHandler;
/*  91 */     Optional<VideoMode> optionsMode = VideoMode.read(fullscreenVideoModeString);
/*  92 */     if (optionsMode.isPresent()) {
/*  93 */       this.preferredFullscreenVideoMode = optionsMode;
/*  94 */     } else if (displayData.fullscreenWidth().isPresent() && displayData.fullscreenHeight().isPresent()) {
/*  95 */       this.preferredFullscreenVideoMode = Optional.of(new VideoMode(displayData.fullscreenWidth().getAsInt(), displayData.fullscreenHeight().getAsInt(), 8, 8, 8, 60));
/*     */     } else {
/*  97 */       this.preferredFullscreenVideoMode = Optional.empty();
/*     */     } 
/*  99 */     this.actuallyFullscreen = this.fullscreen = displayData.isFullscreen();
/* 102 */     Monitor initialMonitor = monitorManager.getMonitor(GLFW.glfwGetPrimaryMonitor());
/* 104 */     this.windowedWidth = this.width = allowedWindowMinSize(displayData.width());
/* 105 */     this.windowedHeight = this.height = allowedWindowMinSize(displayData.height());
/* 107 */     this.handle = createWindow(backend, this.width, this.height, title, (this.fullscreen && initialMonitor != null) ? initialMonitor.monitor() : 0L);
/* 108 */     this.backend = backend;
/* 110 */     if (initialMonitor != null) {
/* 111 */       VideoMode mode = initialMonitor.getPreferredVidMode(this.fullscreen ? this.preferredFullscreenVideoMode : Optional.<VideoMode>empty());
/* 112 */       this.windowedX = this.x = initialMonitor.x() + mode.getWidth() / 2 - this.width / 2;
/* 113 */       this.windowedY = this.y = initialMonitor.y() + mode.getHeight() / 2 - this.height / 2;
/*     */     } else {
/* 115 */       int[] actualX = new int[1];
/* 116 */       int[] actualY = new int[1];
/* 117 */       GLFW.glfwGetWindowPos(this.handle, actualX, actualY);
/* 118 */       this.windowedX = this.x = actualX[0];
/* 119 */       this.windowedY = this.y = actualY[0];
/*     */     } 
/* 122 */     setMode();
/* 124 */     refreshFramebufferSize();
/* 126 */     GLFW.glfwSetFramebufferSizeCallback(this.handle, this::onFramebufferResize);
/* 127 */     GLFW.glfwSetWindowPosCallback(this.handle, this::onMove);
/* 128 */     GLFW.glfwSetWindowSizeCallback(this.handle, this::onResize);
/* 129 */     GLFW.glfwSetWindowFocusCallback(this.handle, this::onFocus);
/* 130 */     GLFW.glfwSetCursorEnterCallback(this.handle, this::onEnter);
/* 131 */     GLFW.glfwSetWindowIconifyCallback(this.handle, this::onIconify);
/*     */   }
/*     */   
/*     */   public static long createGlfwWindow(int width, int height, String title, long monitor, GpuBackend backend) throws BackendCreationException {
/*     */     long windowHandle;
/* 135 */     GLFWErrorCapture glfwErrors = new GLFWErrorCapture();
/* 137 */     GLFWErrorScope gLFWErrorScope = new GLFWErrorScope((GLFWErrorCallbackI)glfwErrors);
/*     */     try {
/* 138 */       backend.setWindowHints();
/* 139 */       GLFW.glfwWindowHint(131076, 0);
/* 141 */       windowHandle = GLFW.glfwCreateWindow(width, height, title, monitor, 0L);
/* 142 */       if (windowHandle == 0L)
/* 143 */         backend.handleWindowCreationErrors(glfwErrors.firstError()); 
/* 145 */       gLFWErrorScope.close();
/*     */     } catch (Throwable throwable) {
/*     */       try {
/*     */         gLFWErrorScope.close();
/*     */       } catch (Throwable throwable1) {
/*     */         throwable.addSuppressed(throwable1);
/*     */       } 
/*     */       throw throwable;
/*     */     } 
/* 147 */     for (GLFWErrorCapture.Error error : glfwErrors)
/* 148 */       LOGGER.error("GLFW error collected during GL backend initialization: {}", error); 
/* 151 */     return windowHandle;
/*     */   }
/*     */   
/*     */   private long createWindow(GpuBackend backend, int width, int height, String title, long initialMonitor) throws BackendCreationException {
/* 155 */     return createGlfwWindow(width, height, title, initialMonitor, backend);
/*     */   }
/*     */   
/*     */   public static String getPlatform() {
/* 159 */     int platform = GLX.getGlfwPlatform();
/* 160 */     switch (platform) {
/*     */       case 0:
/*     */       
/*     */       case 393217:
/*     */       
/*     */       case 393218:
/*     */       
/*     */       case 393219:
/*     */       
/*     */       case 393220:
/*     */       
/*     */       case 393221:
/*     */       
/*     */     } 
/* 160 */     return 
/*     */       
/* 167 */       String.format(Locale.ROOT, "unknown (%08X)", new Object[] { Integer.valueOf(platform) });
/*     */   }
/*     */   
/*     */   public int getRefreshRate() {
/* 172 */     RenderSystem.assertOnRenderThread();
/* 173 */     return GLX._getRefreshRate(this);
/*     */   }
/*     */   
/*     */   public boolean shouldClose() {
/* 178 */     return GLX._shouldClose(this);
/*     */   }
/*     */   
/*     */   public static void checkGlfwError(BiConsumer<Integer, String> errorConsumer) {
/* 182 */     MemoryStack stack = MemoryStack.stackPush();
/*     */     try {
/* 183 */       PointerBuffer errorDescription = stack.mallocPointer(1);
/* 184 */       int errorCode = GLFW.glfwGetError(errorDescription);
/* 185 */       if (errorCode != 0) {
/* 186 */         long errorDescriptionAddress = errorDescription.get();
/* 187 */         String errorMessage = (errorDescriptionAddress == 0L) ? "" : MemoryUtil.memUTF8(errorDescriptionAddress);
/* 188 */         errorConsumer.accept(Integer.valueOf(errorCode), errorMessage);
/*     */       } 
/* 190 */       if (stack != null)
/* 190 */         stack.close(); 
/*     */     } catch (Throwable throwable) {
/*     */       if (stack != null)
/*     */         try {
/*     */           stack.close();
/*     */         } catch (Throwable throwable1) {
/*     */           throwable.addSuppressed(throwable1);
/*     */         }  
/*     */       throw throwable;
/*     */     } 
/*     */   }
/*     */   
/*     */   public void setIcon(PackResources resources, IconSet iconSet) throws IOException {
/*     */     List<IoSupplier<InputStream>> iconStreams;
/*     */     List<ByteBuffer> allocatedBuffers;
/* 194 */     int platform = GLX.getGlfwPlatform();
/* 195 */     switch (platform) {
/*     */       case 393217:
/*     */       case 393220:
/* 197 */         iconStreams = iconSet.getStandardIcons(resources);
/* 198 */         allocatedBuffers = new ArrayList<>(iconStreams.size());
/*     */         try {
/* 199 */           MemoryStack stack = MemoryStack.stackPush();
/*     */           try {
/* 200 */             GLFWImage.Buffer icons = GLFWImage.malloc(iconStreams.size(), stack);
/* 201 */             for (int i = 0; i < iconStreams.size(); i++) {
/* 202 */               NativeImage image = NativeImage.read((InputStream)((IoSupplier)iconStreams.get(i)).get());
/*     */               try {
/* 203 */                 ByteBuffer pixels = MemoryUtil.memAlloc(image.getWidth() * image.getHeight() * 4);
/* 204 */                 allocatedBuffers.add(pixels);
/* 205 */                 pixels.asIntBuffer().put(image.getPixelsABGR());
/* 207 */                 icons.position(i);
/* 208 */                 icons.width(image.getWidth());
/* 209 */                 icons.height(image.getHeight());
/* 210 */                 icons.pixels(pixels);
/* 211 */                 if (image != null)
/* 211 */                   image.close(); 
/*     */               } catch (Throwable throwable) {
/*     */                 if (image != null)
/*     */                   try {
/*     */                     image.close();
/*     */                   } catch (Throwable throwable1) {
/*     */                     throwable.addSuppressed(throwable1);
/*     */                   }  
/*     */                 throw throwable;
/*     */               } 
/*     */             } 
/* 214 */             GLFW.glfwSetWindowIcon(this.handle, (GLFWImage.Buffer)icons.position(0));
/* 215 */             if (stack != null)
/* 215 */               stack.close(); 
/*     */           } catch (Throwable throwable) {
/*     */             if (stack != null)
/*     */               try {
/*     */                 stack.close();
/*     */               } catch (Throwable throwable1) {
/*     */                 throwable.addSuppressed(throwable1);
/*     */               }  
/*     */             throw throwable;
/*     */           } 
/*     */         } finally {
/* 216 */           allocatedBuffers.forEach(MemoryUtil::memFree);
/*     */         } 
/*     */       case 393218:
/* 219 */         MacosUtil.loadIcon(iconSet.getMacIcon(resources));
/*     */       case 393219:
/*     */       case 393221:
/*     */         return;
/*     */     } 
/* 223 */     LOGGER.warn("Not setting icon for unrecognized platform: {}", Integer.valueOf(platform));
/*     */   }
/*     */   
/*     */   public void setErrorSection(String string) {
/* 228 */     this.errorSection = string;
/*     */   }
/*     */   
/*     */   private void setBootErrorCallback() {
/* 234 */     GLFW.glfwSetErrorCallback(Window::bootCrash);
/*     */   }
/*     */   
/*     */   private static void bootCrash(int error, long description) {
/* 238 */     String message = "GLFW error " + error + ": " + MemoryUtil.memUTF8(description);
/* 239 */     MessageBox.error(message + ".\n\nPlease make sure you have up-to-date drivers (see aka.ms/mcdriver for instructions).");
/* 240 */     throw new WindowInitFailed(message);
/*     */   }
/*     */   
/*     */   public void defaultErrorCallback(int errorCode, long description) {
/* 244 */     if (!RenderSystem.isOnRenderThread())
/* 245 */       LOGGER.error("Error callback from invalid thread ({})", Thread.currentThread().getName()); 
/* 247 */     String errorString = MemoryUtil.memUTF8(description);
/* 248 */     LOGGER.error("########## GL ERROR ##########");
/* 249 */     LOGGER.error("@ {}", this.errorSection);
/* 250 */     LOGGER.error("{}: {}", Integer.valueOf(errorCode), errorString);
/*     */   }
/*     */   
/*     */   public void setDefaultErrorCallback() {
/* 254 */     GLFWErrorCallback previousCallback = GLFW.glfwSetErrorCallback((GLFWErrorCallbackI)this.defaultErrorCallback);
/* 255 */     if (previousCallback != null)
/* 256 */       previousCallback.free(); 
/*     */   }
/*     */   
/*     */   public void close() {
/* 262 */     RenderSystem.assertOnRenderThread();
/* 263 */     Callbacks.glfwFreeCallbacks(this.handle);
/* 264 */     GLFW.glfwSetErrorCallback(null);
/* 265 */     this.defaultErrorCallback.close();
/* 266 */     GLFW.glfwDestroyWindow(this.handle);
/*     */   }
/*     */   
/*     */   private void onMove(long handle, int x, int y) {
/* 270 */     this.x = x;
/* 271 */     this.y = y;
/*     */   }
/*     */   
/*     */   private boolean isSoftScreen() {
/* 277 */     return (Util.getPlatform() == Util.OS.WINDOWS && this.fullscreen && !this.exclusiveFullscreen);
/*     */   }
/*     */   
/*     */   private void onFramebufferResize(long handle, int newWidth, int newHeight) {
/* 281 */     if (handle != this.handle)
/*     */       return; 
/* 284 */     int oldWidth = getWidth();
/* 285 */     int oldHeight = getHeight();
/* 287 */     if (newWidth == 0 || newHeight == 0) {
/* 288 */       this.minimized = true;
/*     */       return;
/*     */     } 
/* 292 */     int newFrameBufferHeight = isSoftScreen() ? (newHeight - 1) : newHeight;
/* 293 */     if (newWidth == oldWidth && newFrameBufferHeight == oldHeight && !this.minimized)
/*     */       return; 
/* 296 */     this.minimized = false;
/* 298 */     this.framebufferWidth = newWidth;
/* 299 */     this.framebufferHeight = newFrameBufferHeight;
/*     */     try {
/* 302 */       this.eventHandler.framebufferSizeChanged();
/* 303 */     } catch (Exception e) {
/* 304 */       CrashReport report = CrashReport.forThrowable(e, "Window resize");
/* 305 */       CrashReportCategory windowSizeDetails = report.addCategory("Window Dimensions");
/* 306 */       windowSizeDetails.setDetail("Old", "" + oldWidth + "x" + oldWidth);
/* 307 */       windowSizeDetails.setDetail("New", "" + newWidth + "x" + newWidth);
/* 308 */       throw new ReportedException(report);
/*     */     } 
/*     */   }
/*     */   
/*     */   private void refreshFramebufferSize() {
/* 313 */     int[] outWidth = new int[1];
/* 314 */     int[] outHeight = new int[1];
/* 315 */     GLFW.glfwGetFramebufferSize(this.handle, outWidth, outHeight);
/* 317 */     outHeight[0] = isSoftScreen() ? (outHeight[0] - 1) : outHeight[0];
/* 318 */     this.framebufferWidth = (outWidth[0] > 0) ? outWidth[0] : 1;
/* 319 */     this.framebufferHeight = (outHeight[0] > 0) ? outHeight[0] : 1;
/*     */   }
/*     */   
/*     */   private void onResize(long handle, int newWidth, int newHeight) {
/* 323 */     this.width = newWidth;
/* 324 */     this.height = newHeight;
/*     */   }
/*     */   
/*     */   private void onFocus(long handle, boolean focused) {
/* 328 */     if (handle == this.handle)
/* 329 */       this.focused = focused; 
/*     */   }
/*     */   
/*     */   private void onEnter(long handle, boolean entered) {
/* 334 */     if (entered)
/* 335 */       this.eventHandler.cursorEntered(); 
/*     */   }
/*     */   
/*     */   private void onIconify(long handle, boolean iconified) {
/* 340 */     this.iconified = iconified;
/*     */   }
/*     */   
/*     */   public void updateFullscreenIfChanged() {
/* 344 */     if (this.fullscreen != this.actuallyFullscreen) {
/* 345 */       this.actuallyFullscreen = this.fullscreen;
/*     */       try {
/* 347 */         setMode();
/* 348 */         this.eventHandler.framebufferSizeChanged();
/* 349 */       } catch (Exception e) {
/* 350 */         LOGGER.error("Couldn't toggle fullscreen", e);
/*     */       } 
/*     */     } 
/*     */   }
/*     */   
/*     */   public Optional<VideoMode> getPreferredFullscreenVideoMode() {
/* 356 */     return this.preferredFullscreenVideoMode;
/*     */   }
/*     */   
/*     */   public void setPreferredFullscreenVideoMode(Optional<VideoMode> preferredFullscreenVideoMode) {
/* 360 */     boolean changed = !preferredFullscreenVideoMode.equals(this.preferredFullscreenVideoMode);
/* 361 */     this.preferredFullscreenVideoMode = preferredFullscreenVideoMode;
/* 362 */     if (changed)
/* 363 */       this.dirty = true; 
/*     */   }
/*     */   
/*     */   public void changeFullscreenVideoMode() {
/* 368 */     if (this.fullscreen && this.dirty) {
/* 369 */       this.dirty = false;
/* 370 */       setMode();
/* 371 */       this.eventHandler.framebufferSizeChanged();
/*     */     } 
/*     */   }
/*     */   
/*     */   private void setMode() {
/* 378 */     boolean wasFullscreen = (GLFW.glfwGetWindowMonitor(this.handle) != 0L);
/* 379 */     if (this.fullscreen) {
/* 380 */       Monitor monitor = this.monitorManager.findBestMonitor(this);
/* 381 */       if (monitor == null) {
/* 382 */         LOGGER.warn("Failed to find suitable monitor for fullscreen mode");
/* 383 */         this.fullscreen = false;
/*     */       } else {
/* 385 */         if (MacosUtil.IS_MACOS)
/* 387 */           MacosUtil.exitNativeFullscreen(this); 
/* 389 */         VideoMode mode = monitor.getPreferredVidMode(this.preferredFullscreenVideoMode);
/* 390 */         if (!wasFullscreen) {
/* 391 */           this.windowedX = this.x;
/* 392 */           this.windowedY = this.y;
/* 393 */           this.windowedWidth = allowedWindowMinSize(this.width);
/* 394 */           this.windowedHeight = allowedWindowMinSize(this.height);
/*     */         } 
/* 396 */         this.x = 0;
/* 397 */         this.y = 0;
/* 398 */         this.width = allowedWindowMinSize(mode.getWidth());
/* 399 */         this.height = allowedWindowMinSize(mode.getHeight());
/* 401 */         GLFW.glfwSetWindowMonitor(this.handle, monitor.monitor(), this.x, this.y, this.width, this.height, mode.getRefreshRate());
/* 402 */         if (MacosUtil.IS_MACOS)
/* 404 */           MacosUtil.clearResizableBit(this); 
/*     */       } 
/*     */     } else {
/* 408 */       this.x = this.windowedX;
/* 409 */       this.y = this.windowedY;
/* 410 */       this.width = allowedWindowMinSize(this.windowedWidth);
/* 411 */       this.height = allowedWindowMinSize(this.windowedHeight);
/* 413 */       GLFW.glfwSetWindowMonitor(this.handle, 0L, this.x, this.y, this.width, this.height, -1);
/*     */     } 
/*     */   }
/*     */   
/*     */   public void toggleFullScreen() {
/* 418 */     this.fullscreen = !this.fullscreen;
/*     */   }
/*     */   
/*     */   public void setWindowed(int width, int height) {
/* 422 */     this.windowedWidth = allowedWindowMinSize(width);
/* 423 */     this.windowedHeight = allowedWindowMinSize(height);
/* 424 */     this.fullscreen = false;
/* 425 */     setMode();
/*     */   }
/*     */   
/*     */   public int calculateScale(int maxScale, boolean enforceUnicode) {
/* 429 */     int scale = 1;
/* 430 */     while (scale != maxScale && scale < this.framebufferWidth && scale < this.framebufferHeight && this.framebufferWidth / (scale + 1) >= 320 && this.framebufferHeight / (scale + 1) >= 240)
/* 431 */       scale++; 
/* 433 */     if (enforceUnicode && scale % 2 != 0)
/* 434 */       scale++; 
/* 436 */     return scale;
/*     */   }
/*     */   
/*     */   public void setGuiScale(int guiScale) {
/* 440 */     this.guiScale = guiScale;
/* 441 */     double doubleGuiScale = guiScale;
/* 442 */     int width = (int)(this.framebufferWidth / doubleGuiScale);
/* 443 */     this.guiScaledWidth = (this.framebufferWidth / doubleGuiScale > width) ? (width + 1) : width;
/* 444 */     int height = (int)(this.framebufferHeight / doubleGuiScale);
/* 445 */     this.guiScaledHeight = (this.framebufferHeight / doubleGuiScale > height) ? (height + 1) : height;
/*     */   }
/*     */   
/*     */   public void setTitle(String title) {
/* 449 */     GLFW.glfwSetWindowTitle(this.handle, title);
/*     */   }
/*     */   
/*     */   public long handle() {
/* 453 */     return this.handle;
/*     */   }
/*     */   
/*     */   public boolean isFullscreen() {
/* 457 */     return this.fullscreen;
/*     */   }
/*     */   
/*     */   public boolean isIconified() {
/* 461 */     return this.iconified;
/*     */   }
/*     */   
/*     */   public boolean isFocused() {
/* 465 */     return this.focused;
/*     */   }
/*     */   
/*     */   public int getWidth() {
/* 469 */     return this.framebufferWidth;
/*     */   }
/*     */   
/*     */   public int getHeight() {
/* 473 */     return this.framebufferHeight;
/*     */   }
/*     */   
/*     */   public void setWidth(int width) {
/* 477 */     this.framebufferWidth = width;
/*     */   }
/*     */   
/*     */   public void setHeight(int height) {
/* 481 */     this.framebufferHeight = height;
/*     */   }
/*     */   
/*     */   public int getScreenWidth() {
/* 485 */     return this.width;
/*     */   }
/*     */   
/*     */   public int getScreenHeight() {
/* 489 */     return this.height;
/*     */   }
/*     */   
/*     */   public int getGuiScaledWidth() {
/* 493 */     return this.guiScaledWidth;
/*     */   }
/*     */   
/*     */   public int getGuiScaledHeight() {
/* 497 */     return this.guiScaledHeight;
/*     */   }
/*     */   
/*     */   public int getX() {
/* 501 */     return this.x;
/*     */   }
/*     */   
/*     */   public int getY() {
/* 505 */     return this.y;
/*     */   }
/*     */   
/*     */   public int getGuiScale() {
/* 509 */     return this.guiScale;
/*     */   }
/*     */   
/*     */   public Monitor findBestMonitor() {
/* 513 */     return this.monitorManager.findBestMonitor(this);
/*     */   }
/*     */   
/*     */   public void updateRawMouseInput(boolean value) {
/* 517 */     InputConstants.updateRawMouseInput(this, value);
/*     */   }
/*     */   
/*     */   public void setWindowCloseCallback(Runnable task) {
/* 521 */     GLFWWindowCloseCallback prev = GLFW.glfwSetWindowCloseCallback(this.handle, id -> task.run());
/* 522 */     if (prev != null)
/* 523 */       prev.free(); 
/*     */   }
/*     */   
/*     */   public static class WindowInitFailed extends SilentInitException {
/*     */     public WindowInitFailed(String message) {
/* 529 */       super(message);
/*     */     }
/*     */   }
/*     */   
/*     */   public boolean isMinimized() {
/* 534 */     return this.minimized;
/*     */   }
/*     */   
/*     */   public void setAllowCursorChanges(boolean value) {
/* 538 */     this.allowCursorChanges = value;
/*     */   }
/*     */   
/*     */   public void selectCursor(CursorType cursor) {
/* 542 */     CursorType effectiveCursor = this.allowCursorChanges ? cursor : CursorType.DEFAULT;
/* 544 */     if (this.currentCursor != effectiveCursor) {
/* 545 */       this.currentCursor = effectiveCursor;
/* 546 */       effectiveCursor.select(this);
/*     */     } 
/*     */   }
/*     */   
/*     */   public float getAppropriateLineWidth() {
/* 551 */     return Math.max(2.5F, getWidth() / 1920.0F * 2.5F);
/*     */   }
/*     */   
/*     */   public GpuBackend backend() {
/* 555 */     return this.backend;
/*     */   }
/*     */   
/*     */   private static int allowedWindowMinSize(int size) {
/* 559 */     return Math.max(size, 1);
/*     */   }
/*     */ }


/* Location:              C:\Users\karol\AppData\Roaming\ModrinthApp\meta\versions\26.2-0.19.5\26.2-0.19.5.jar!\com\mojang\blaze3d\platform\Window.class
 * Java compiler version: 25 (69.0)
 * JD-Core Version:       1.1.3
 */