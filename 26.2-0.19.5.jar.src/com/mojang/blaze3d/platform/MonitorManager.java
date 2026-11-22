/*     */ package com.mojang.blaze3d.platform;
/*     */ 
/*     */ import com.mojang.blaze3d.systems.RenderSystem;
/*     */ import com.mojang.logging.LogUtils;
/*     */ import it.unimi.dsi.fastutil.longs.Long2ObjectMap;
/*     */ import it.unimi.dsi.fastutil.longs.Long2ObjectOpenHashMap;
/*     */ import it.unimi.dsi.fastutil.objects.ObjectIterator;
/*     */ import org.lwjgl.PointerBuffer;
/*     */ import org.lwjgl.glfw.GLFW;
/*     */ import org.lwjgl.glfw.GLFWMonitorCallback;
/*     */ import org.slf4j.Logger;
/*     */ 
/*     */ public class MonitorManager implements AutoCloseable {
/*  15 */   private static final Logger LOGGER = LogUtils.getLogger();
/*     */   
/*  16 */   private final Long2ObjectMap<Monitor> monitors = (Long2ObjectMap<Monitor>)new Long2ObjectOpenHashMap();
/*     */   
/*     */   public MonitorManager() {
/*  19 */     GLFW.glfwSetMonitorCallback(this::onMonitorChange);
/*  20 */     PointerBuffer buffer = GLFW.glfwGetMonitors();
/*  21 */     if (buffer != null)
/*  22 */       for (int i = 0; i < buffer.limit(); i++) {
/*  23 */         Monitor monitor = Monitor.tryCreate(buffer.get(i));
/*  24 */         if (monitor != null)
/*  25 */           this.monitors.put(buffer.get(i), monitor); 
/*     */       }  
/*     */   }
/*     */   
/*     */   private void onMonitorChange(long monitorHandle, int event) {
/*  32 */     RenderSystem.assertOnRenderThread();
/*  33 */     if (event == 262145) {
/*  34 */       Monitor monitor = Monitor.tryCreate(monitorHandle);
/*  35 */       if (monitor != null) {
/*  36 */         this.monitors.put(monitorHandle, monitor);
/*  37 */         LOGGER.debug("Monitor {} connected. Current monitors: {}", monitor, this.monitors);
/*     */       } 
/*  39 */     } else if (event == 262146) {
/*  40 */       Monitor monitor = (Monitor)this.monitors.remove(monitorHandle);
/*  41 */       LOGGER.debug("Monitor {} disconnected. Current monitors: {}", (monitor != null) ? monitor : Long.valueOf(monitorHandle), this.monitors);
/*     */     } 
/*     */   }
/*     */   
/*     */   public Monitor getMonitor(long monitor) {
/*  46 */     return (Monitor)this.monitors.get(monitor);
/*     */   }
/*     */   
/*     */   public Monitor findBestMonitor(Window window) {
/*  51 */     long windowMonitor = GLFW.glfwGetWindowMonitor(window.handle());
/*  52 */     if (windowMonitor != 0L)
/*  53 */       return getMonitor(windowMonitor); 
/*  56 */     int winMinX = window.getX();
/*  57 */     int winMaxX = winMinX + window.getScreenWidth();
/*  58 */     int winMinY = window.getY();
/*  59 */     int winMaxY = winMinY + window.getScreenHeight();
/*  61 */     int maxArea = -1;
/*  62 */     Monitor result = null;
/*  63 */     long primaryMonitor = GLFW.glfwGetPrimaryMonitor();
/*  64 */     LOGGER.debug("Selecting monitor - primary: {}, current monitors: {}", Long.valueOf(primaryMonitor), this.monitors);
/*  66 */     for (ObjectIterator<Monitor> objectIterator = this.monitors.values().iterator(); objectIterator.hasNext(); ) {
/*  66 */       Monitor monitor = objectIterator.next();
/*  67 */       int monMinX = monitor.x();
/*  68 */       int monMaxX = monMinX + monitor.currentMode().getWidth();
/*  69 */       int monMinY = monitor.y();
/*  70 */       int monMaxY = monMinY + monitor.currentMode().getHeight();
/*  72 */       int minX = clamp(winMinX, monMinX, monMaxX);
/*  73 */       int maxX = clamp(winMaxX, monMinX, monMaxX);
/*  74 */       int minY = clamp(winMinY, monMinY, monMaxY);
/*  75 */       int maxY = clamp(winMaxY, monMinY, monMaxY);
/*  77 */       int sx = Math.max(0, maxX - minX);
/*  78 */       int sy = Math.max(0, maxY - minY);
/*  79 */       int area = sx * sy;
/*  80 */       if (area > maxArea) {
/*  81 */         result = monitor;
/*  82 */         maxArea = area;
/*     */         continue;
/*     */       } 
/*  83 */       if (area == maxArea && primaryMonitor == monitor.monitor()) {
/*  84 */         LOGGER.debug("Primary monitor {} is preferred to monitor {}", monitor, result);
/*  85 */         result = monitor;
/*     */       } 
/*     */     } 
/*  88 */     LOGGER.debug("Selected monitor: {}", result);
/*  89 */     return result;
/*     */   }
/*     */   
/*     */   public static int clamp(int value, int min, int max) {
/*  93 */     if (value < min)
/*  94 */       return min; 
/*  96 */     if (value > max)
/*  97 */       return max; 
/*  99 */     return value;
/*     */   }
/*     */   
/*     */   public void close() {
/* 104 */     RenderSystem.assertOnRenderThread();
/* 105 */     GLFWMonitorCallback callback = GLFW.glfwSetMonitorCallback(null);
/* 106 */     if (callback != null)
/* 107 */       callback.free(); 
/*     */   }
/*     */ }


/* Location:              C:\Users\karol\AppData\Roaming\ModrinthApp\meta\versions\26.2-0.19.5\26.2-0.19.5.jar!\com\mojang\blaze3d\platform\MonitorManager.class
 * Java compiler version: 25 (69.0)
 * JD-Core Version:       1.1.3
 */