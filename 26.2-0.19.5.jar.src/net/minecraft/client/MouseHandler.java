/*     */ package net.minecraft.client;
/*     */ 
/*     */ import com.mojang.blaze3d.Blaze3D;
/*     */ import com.mojang.blaze3d.platform.InputConstants;
/*     */ import com.mojang.blaze3d.platform.Window;
/*     */ import com.mojang.logging.LogUtils;
/*     */ import java.nio.file.InvalidPathException;
/*     */ import java.nio.file.Path;
/*     */ import java.nio.file.Paths;
/*     */ import java.util.ArrayList;
/*     */ import java.util.List;
/*     */ import java.util.Locale;
/*     */ import net.minecraft.CrashReport;
/*     */ import net.minecraft.CrashReportCategory;
/*     */ import net.minecraft.ReportedException;
/*     */ import net.minecraft.client.gui.Font;
/*     */ import net.minecraft.client.gui.GuiGraphicsExtractor;
/*     */ import net.minecraft.client.gui.components.toasts.SystemToast;
/*     */ import net.minecraft.client.gui.screens.Screen;
/*     */ import net.minecraft.client.input.InputQuirks;
/*     */ import net.minecraft.client.input.MouseButtonEvent;
/*     */ import net.minecraft.client.input.MouseButtonInfo;
/*     */ import net.minecraft.util.Mth;
/*     */ import net.minecraft.util.SmoothDouble;
/*     */ import net.minecraft.util.Util;
/*     */ import net.minecraft.world.entity.player.Inventory;
/*     */ import org.joml.Vector2i;
/*     */ import org.lwjgl.glfw.GLFWDropCallback;
/*     */ import org.slf4j.Logger;
/*     */ 
/*     */ public class MouseHandler {
/*  37 */   private static final Logger LOGGER = LogUtils.getLogger();
/*     */   
/*     */   public static final long DOUBLE_CLICK_THRESHOLD_MS = 250L;
/*     */   
/*     */   private final Minecraft minecraft;
/*     */   
/*     */   private boolean isLeftPressed;
/*     */   
/*     */   private boolean isMiddlePressed;
/*     */   
/*     */   private boolean isRightPressed;
/*     */   
/*     */   private double xpos;
/*     */   
/*     */   private double ypos;
/*     */   
/*     */   private LastClick lastClick;
/*     */   
/*     */   protected int lastClickButton;
/*     */   
/*     */   private int fakeRightMouse;
/*     */   
/*  56 */   private MouseButtonInfo activeButton = null;
/*     */   
/*     */   private boolean ignoreFirstMove = true;
/*     */   
/*     */   private double mousePressedTime;
/*     */   
/*  62 */   private final SmoothDouble smoothTurnX = new SmoothDouble();
/*     */   
/*  63 */   private final SmoothDouble smoothTurnY = new SmoothDouble();
/*     */   
/*     */   private double accumulatedDX;
/*     */   
/*     */   private double accumulatedDY;
/*     */   
/*     */   private final ScrollWheelHandler scrollWheelHandler;
/*     */   
/*  68 */   private double lastHandleMovementTime = Double.MIN_VALUE;
/*     */   
/*     */   private boolean mouseGrabbed;
/*     */   
/*     */   public MouseHandler(Minecraft minecraft) {
/*  71 */     this.minecraft = minecraft;
/*  72 */     this.scrollWheelHandler = new ScrollWheelHandler();
/*     */   }
/*     */   
/*     */   private void onButton(long handle, MouseButtonInfo rawButtonInfo, int action) {
/*  76 */     Window window = this.minecraft.getWindow();
/*  77 */     if (handle != window.handle())
/*     */       return; 
/*  81 */     this.minecraft.getFramerateLimitTracker().onInputReceived();
/*  83 */     if (this.minecraft.gui.screen() != null)
/*  84 */       this.minecraft.setLastInputType(InputType.MOUSE); 
/*  87 */     boolean pressed = (action == 1);
/*  89 */     MouseButtonInfo buttonInfo = simulateRightClick(rawButtonInfo, pressed);
/*  90 */     if (pressed) {
/*  91 */       this.activeButton = buttonInfo;
/*  92 */       this.mousePressedTime = Blaze3D.getTime();
/*  93 */     } else if (this.activeButton != null) {
/*  94 */       this.activeButton = null;
/*     */     } 
/*  97 */     if (this.minecraft.gui.overlay() == null) {
/*  99 */       if (pressed && this.minecraft
/* 100 */         .handleGlobalKeyPress(InputConstants.Type.MOUSE.getOrCreate(buttonInfo.button()), buttonInfo.hasControlDownWithQuirk()))
/*     */         return; 
/* 102 */       if (this.minecraft.gui.screen() == null) {
/* 103 */         if (!this.mouseGrabbed && pressed)
/* 104 */           grabMouse(); 
/*     */       } else {
/* 107 */         double xm = getScaledXPos(window);
/* 108 */         double ym = getScaledYPos(window);
/* 109 */         Screen screen = this.minecraft.gui.screen();
/* 110 */         MouseButtonEvent event = new MouseButtonEvent(xm, ym, buttonInfo);
/* 112 */         if (pressed) {
/* 113 */           screen.afterMouseAction();
/*     */           try {
/* 115 */             long currentTime = Util.getMillis();
/* 116 */             boolean doubleClick = (this.lastClick != null && currentTime - this.lastClick.time() < 250L && this.lastClick.screen() == screen && this.lastClickButton == event.button());
/* 117 */             if (screen.mouseClicked(event, doubleClick)) {
/* 118 */               this.lastClick = new LastClick(currentTime, screen);
/* 119 */               this.lastClickButton = buttonInfo.button();
/*     */               return;
/*     */             } 
/* 122 */           } catch (Throwable t) {
/* 123 */             CrashReport report = CrashReport.forThrowable(t, "mouseClicked event handler");
/* 124 */             screen.fillCrashDetails(report);
/* 125 */             CrashReportCategory mouseDetails = report.addCategory("Mouse");
/* 126 */             fillMousePositionDetails(mouseDetails, window);
/* 127 */             mouseDetails.setDetail("Button", Integer.valueOf(event.button()));
/* 128 */             throw new ReportedException(report);
/*     */           } 
/*     */         } else {
/*     */           try {
/* 132 */             if (screen.mouseReleased(event))
/*     */               return; 
/* 135 */           } catch (Throwable t) {
/* 136 */             CrashReport report = CrashReport.forThrowable(t, "mouseReleased event handler");
/* 137 */             screen.fillCrashDetails(report);
/* 138 */             CrashReportCategory mouseDetails = report.addCategory("Mouse");
/* 140 */             fillMousePositionDetails(mouseDetails, window);
/* 142 */             mouseDetails.setDetail("Button", Integer.valueOf(event.button()));
/* 143 */             throw new ReportedException(report);
/*     */           } 
/*     */         } 
/*     */       } 
/*     */     } 
/* 147 */     if (this.minecraft.gui.screen() == null && this.minecraft.gui.overlay() == null) {
/* 148 */       if (buttonInfo.button() == 0) {
/* 149 */         this.isLeftPressed = pressed;
/* 150 */       } else if (buttonInfo.button() == 2) {
/* 151 */         this.isMiddlePressed = pressed;
/* 152 */       } else if (buttonInfo.button() == 1) {
/* 153 */         this.isRightPressed = pressed;
/*     */       } 
/* 156 */       InputConstants.Key mouseKey = InputConstants.Type.MOUSE.getOrCreate(buttonInfo.button());
/* 157 */       KeyMapping.set(mouseKey, pressed);
/* 158 */       if (pressed)
/* 159 */         KeyMapping.click(mouseKey); 
/*     */     } 
/*     */   }
/*     */   
/*     */   private MouseButtonInfo simulateRightClick(MouseButtonInfo info, boolean pressed) {
/* 165 */     if (InputQuirks.SIMULATE_RIGHT_CLICK_WITH_LONG_LEFT_CLICK && info.button() == 0)
/* 166 */       if (pressed) {
/* 167 */         if ((info.modifiers() & 0x2) == 2) {
/* 168 */           this.fakeRightMouse++;
/* 169 */           return new MouseButtonInfo(1, info.modifiers());
/*     */         } 
/* 171 */       } else if (this.fakeRightMouse > 0) {
/* 172 */         this.fakeRightMouse--;
/* 173 */         return new MouseButtonInfo(1, info.modifiers());
/*     */       }  
/* 176 */     return info;
/*     */   }
/*     */   
/*     */   public void fillMousePositionDetails(CrashReportCategory category, Window window) {
/* 180 */     category.setDetail("Mouse location", () -> String.format(Locale.ROOT, "Scaled: (%f, %f). Absolute: (%f, %f)", new Object[] { Double.valueOf(getScaledXPos(window, this.xpos)), Double.valueOf(getScaledYPos(window, this.ypos)), Double.valueOf(this.xpos), Double.valueOf(this.ypos) }));
/* 181 */     category.setDetail("Screen size", () -> String.format(Locale.ROOT, "Scaled: (%d, %d). Absolute: (%d, %d). Scale factor of %d", new Object[] { Integer.valueOf(window.getGuiScaledWidth()), Integer.valueOf(window.getGuiScaledHeight()), Integer.valueOf(window.getWidth()), Integer.valueOf(window.getHeight()), Integer.valueOf(window.getGuiScale()) }));
/*     */   }
/*     */   
/*     */   private void onScroll(long handle, double xoffset, double yoffset) {
/* 185 */     if (handle == this.minecraft.getWindow().handle()) {
/* 186 */       this.minecraft.getFramerateLimitTracker().onInputReceived();
/* 187 */       boolean discreteScroll = ((Boolean)this.minecraft.options.discreteMouseScroll().get()).booleanValue();
/* 188 */       double scrollSensitivity = ((Double)this.minecraft.options.mouseWheelSensitivity().get()).doubleValue();
/* 189 */       double scaledXOffset = (discreteScroll ? Math.signum(xoffset) : xoffset) * scrollSensitivity;
/* 190 */       double scaledYOffset = (discreteScroll ? Math.signum(yoffset) : yoffset) * scrollSensitivity;
/* 191 */       if (this.minecraft.gui.overlay() == null)
/* 193 */         if (this.minecraft.gui.screen() != null) {
/* 194 */           double xm = getScaledXPos(this.minecraft.getWindow());
/* 195 */           double ym = getScaledYPos(this.minecraft.getWindow());
/* 196 */           this.minecraft.gui.screen().mouseScrolled(xm, ym, scaledXOffset, scaledYOffset);
/* 197 */           this.minecraft.gui.screen().afterMouseAction();
/* 198 */         } else if (this.minecraft.player != null) {
/* 199 */           Vector2i wheelXY = this.scrollWheelHandler.onMouseScroll(scaledXOffset, scaledYOffset);
/* 200 */           if (wheelXY.x == 0 && wheelXY.y == 0)
/*     */             return; 
/* 203 */           int wheel = (wheelXY.y == 0) ? -wheelXY.x : wheelXY.y;
/* 205 */           if (this.minecraft.player.isSpectator()) {
/* 206 */             if (this.minecraft.gui.hud.getSpectatorGui().isMenuActive()) {
/* 207 */               this.minecraft.gui.hud.getSpectatorGui().onMouseScrolled(-wheel);
/*     */             } else {
/* 209 */               float speed = Mth.clamp(this.minecraft.player.getAbilities().getFlyingSpeed() + wheelXY.y * 0.005F, 0.0F, 0.2F);
/* 210 */               this.minecraft.player.getAbilities().setFlyingSpeed(speed);
/*     */             } 
/*     */           } else {
/* 216 */             Inventory inventory = this.minecraft.player.getInventory();
/* 217 */             inventory.setSelectedSlot(ScrollWheelHandler.getNextScrollWheelSelection(wheel, inventory.getSelectedSlot(), Inventory.getSelectionSize()));
/*     */           } 
/*     */         }  
/*     */     } 
/*     */   }
/*     */   
/*     */   private void onDrop(long handle, List<Path> files, int failedCount) {
/* 224 */     this.minecraft.getFramerateLimitTracker().onInputReceived();
/* 225 */     if (this.minecraft.gui.screen() != null)
/* 226 */       this.minecraft.gui.screen().onFilesDrop(files); 
/* 229 */     if (failedCount > 0)
/* 230 */       SystemToast.onFileDropFailure(this.minecraft, failedCount); 
/*     */   }
/*     */   
/*     */   public void setup(Window window) {
/* 235 */     InputConstants.setupMouseCallbacks(window, (window1, xpos, ypos) -> this.minecraft.execute(()), (window1, button, action, mods) -> {
/*     */           MouseButtonInfo buttonInfo = new MouseButtonInfo(button, mods);
/*     */           this.minecraft.execute(());
/*     */         }(window1, xoffset, yoffset) -> this.minecraft.execute(()), (window1, count, namesPtr) -> {
/*     */           List<Path> names = new ArrayList<>(count);
/*     */           int failedCount = 0;
/*     */           for (int i = 0; i < count; i++) {
/*     */             String name = GLFWDropCallback.getName(namesPtr, i);
/*     */             try {
/*     */               names.add(Paths.get(name, new String[0]));
/* 249 */             } catch (InvalidPathException e) {
/*     */               failedCount++;
/*     */               LOGGER.error("Failed to parse path '{}'", name, e);
/*     */             } 
/*     */           } 
/*     */           if (!names.isEmpty()) {
/*     */             int finalFailedCount = failedCount;
/*     */             this.minecraft.execute(());
/*     */           } 
/*     */         });
/*     */   }
/*     */   
/*     */   private void onMove(long handle, double xpos, double ypos) {
/* 263 */     if (handle != this.minecraft.getWindow().handle())
/*     */       return; 
/* 266 */     if (this.ignoreFirstMove) {
/* 267 */       this.xpos = xpos;
/* 268 */       this.ypos = ypos;
/* 269 */       this.ignoreFirstMove = false;
/*     */       return;
/*     */     } 
/* 273 */     if (this.minecraft.isWindowActive()) {
/* 274 */       this.accumulatedDX += xpos - this.xpos;
/* 275 */       this.accumulatedDY += ypos - this.ypos;
/*     */     } 
/* 278 */     this.xpos = xpos;
/* 279 */     this.ypos = ypos;
/*     */   }
/*     */   
/*     */   public void handleAccumulatedMovement() {
/* 284 */     double time = Blaze3D.getTime();
/* 285 */     double mousea = time - this.lastHandleMovementTime;
/* 286 */     this.lastHandleMovementTime = time;
/* 287 */     if (this.minecraft.isWindowActive()) {
/* 288 */       Screen screen = this.minecraft.gui.screen();
/* 289 */       boolean mouseMoved = (this.accumulatedDX != 0.0D || this.accumulatedDY != 0.0D);
/* 290 */       if (mouseMoved)
/* 291 */         this.minecraft.getFramerateLimitTracker().onInputReceived(); 
/* 293 */       if (screen != null && this.minecraft.gui.overlay() == null && mouseMoved) {
/* 294 */         Window window = this.minecraft.getWindow();
/* 295 */         double xm = getScaledXPos(window);
/* 296 */         double ym = getScaledYPos(window);
/*     */         try {
/* 299 */           screen.mouseMoved(xm, ym);
/* 300 */         } catch (Throwable t) {
/* 301 */           CrashReport report = CrashReport.forThrowable(t, "mouseMoved event handler");
/* 302 */           screen.fillCrashDetails(report);
/* 303 */           CrashReportCategory mouseDetails = report.addCategory("Mouse");
/* 304 */           fillMousePositionDetails(mouseDetails, window);
/* 305 */           throw new ReportedException(report);
/*     */         } 
/* 308 */         if (this.activeButton != null && this.mousePressedTime > 0.0D) {
/* 309 */           double dx = getScaledXPos(window, this.accumulatedDX);
/* 310 */           double dy = getScaledYPos(window, this.accumulatedDY);
/*     */           try {
/* 312 */             screen.mouseDragged(new MouseButtonEvent(xm, ym, this.activeButton), dx, dy);
/* 313 */           } catch (Throwable t) {
/* 314 */             CrashReport report = CrashReport.forThrowable(t, "mouseDragged event handler");
/* 315 */             screen.fillCrashDetails(report);
/* 316 */             CrashReportCategory mouseDetails = report.addCategory("Mouse");
/* 317 */             fillMousePositionDetails(mouseDetails, window);
/* 318 */             throw new ReportedException(report);
/*     */           } 
/*     */         } 
/* 321 */         screen.afterMouseMove();
/*     */       } 
/* 323 */       if (isMouseGrabbed() && this.minecraft.player != null)
/* 324 */         turnPlayer(mousea); 
/*     */     } 
/* 327 */     this.accumulatedDX = 0.0D;
/* 328 */     this.accumulatedDY = 0.0D;
/*     */   }
/*     */   
/*     */   public static double getScaledXPos(Window window, double x) {
/* 332 */     return x * window.getGuiScaledWidth() / window.getScreenWidth();
/*     */   }
/*     */   
/*     */   public double getScaledXPos(Window window) {
/* 336 */     return getScaledXPos(window, this.xpos);
/*     */   }
/*     */   
/*     */   public static double getScaledYPos(Window window, double y) {
/* 340 */     return y * window.getGuiScaledHeight() / window.getScreenHeight();
/*     */   }
/*     */   
/*     */   public double getScaledYPos(Window window) {
/* 344 */     return getScaledYPos(window, this.ypos);
/*     */   }
/*     */   
/*     */   private void turnPlayer(double mousea) {
/* 351 */     double xo, yo, ss = ((Double)this.minecraft.options.sensitivity().get()).doubleValue() * 0.6000000238418579D + 0.20000000298023224D;
/* 352 */     double sensitivityMod = ss * ss * ss;
/* 353 */     double sens = sensitivityMod * 8.0D;
/* 355 */     if (this.minecraft.options.smoothCamera) {
/* 356 */       double dx = this.smoothTurnX.getNewDeltaValue(this.accumulatedDX * sens, mousea * sens);
/* 357 */       double dy = this.smoothTurnY.getNewDeltaValue(this.accumulatedDY * sens, mousea * sens);
/* 358 */       xo = dx;
/* 359 */       yo = dy;
/* 360 */     } else if (this.minecraft.options.getCameraType().isFirstPerson() && this.minecraft.player.isScoping()) {
/* 361 */       this.smoothTurnX.reset();
/* 362 */       this.smoothTurnY.reset();
/* 364 */       xo = this.accumulatedDX * sensitivityMod;
/* 365 */       yo = this.accumulatedDY * sensitivityMod;
/*     */     } else {
/* 367 */       this.smoothTurnX.reset();
/* 368 */       this.smoothTurnY.reset();
/* 370 */       xo = this.accumulatedDX * sens;
/* 371 */       yo = this.accumulatedDY * sens;
/*     */     } 
/* 374 */     this.minecraft.getTutorial().onMouse(xo, yo);
/* 375 */     if (this.minecraft.player != null)
/* 376 */       this.minecraft.player.turn(
/* 377 */           ((Boolean)this.minecraft.options.invertMouseX().get()).booleanValue() ? -xo : xo, 
/* 378 */           ((Boolean)this.minecraft.options.invertMouseY().get()).booleanValue() ? -yo : yo); 
/*     */   }
/*     */   
/*     */   public boolean isLeftPressed() {
/* 384 */     return this.isLeftPressed;
/*     */   }
/*     */   
/*     */   public boolean isMiddlePressed() {
/* 388 */     return this.isMiddlePressed;
/*     */   }
/*     */   
/*     */   public boolean isRightPressed() {
/* 392 */     return this.isRightPressed;
/*     */   }
/*     */   
/*     */   public double xpos() {
/* 396 */     return this.xpos;
/*     */   }
/*     */   
/*     */   public double ypos() {
/* 400 */     return this.ypos;
/*     */   }
/*     */   
/*     */   public void setIgnoreFirstMove() {
/* 404 */     this.ignoreFirstMove = true;
/*     */   }
/*     */   
/*     */   public boolean isMouseGrabbed() {
/* 410 */     return this.mouseGrabbed;
/*     */   }
/*     */   
/*     */   public void grabMouse() {
/* 414 */     if (!this.minecraft.isWindowActive())
/*     */       return; 
/* 417 */     if (this.mouseGrabbed)
/*     */       return; 
/* 420 */     if (InputQuirks.RESTORE_KEY_STATE_AFTER_MOUSE_GRAB)
/* 421 */       KeyMapping.setAll(); 
/* 423 */     this.mouseGrabbed = true;
/* 424 */     this.xpos = (this.minecraft.getWindow().getScreenWidth() / 2);
/* 425 */     this.ypos = (this.minecraft.getWindow().getScreenHeight() / 2);
/* 426 */     InputConstants.grabOrReleaseMouse(this.minecraft.getWindow(), 212995, this.xpos, this.ypos);
/* 427 */     this.minecraft.gui.setScreen(null);
/* 428 */     this.minecraft.missTime = 10000;
/* 429 */     this.ignoreFirstMove = true;
/*     */   }
/*     */   
/*     */   public void releaseMouse() {
/* 433 */     if (!this.mouseGrabbed)
/*     */       return; 
/* 436 */     this.mouseGrabbed = false;
/* 437 */     this.xpos = (this.minecraft.getWindow().getScreenWidth() / 2);
/* 438 */     this.ypos = (this.minecraft.getWindow().getScreenHeight() / 2);
/* 439 */     InputConstants.grabOrReleaseMouse(this.minecraft.getWindow(), 212993, this.xpos, this.ypos);
/*     */   }
/*     */   
/*     */   public void cursorEntered() {
/* 443 */     this.ignoreFirstMove = true;
/*     */   }
/*     */   
/*     */   public void drawDebugMouseInfo(Font font, GuiGraphicsExtractor graphics) {
/* 447 */     Window window = this.minecraft.getWindow();
/* 448 */     double x = getScaledXPos(window);
/* 449 */     double y = getScaledYPos(window) - 8.0D;
/* 450 */     String text = String.format(Locale.ROOT, "%.0f,%.0f", new Object[] { Double.valueOf(x), Double.valueOf(y) });
/* 451 */     graphics.text(font, text, (int)x, (int)y, -1);
/*     */   }
/*     */   
/*     */   private static final class LastClick extends Record {
/*     */     private final long time;
/*     */     
/*     */     private final Screen screen;
/*     */     
/*     */     private LastClick(long time, Screen screen) {
/* 454 */       this.time = time;
/* 454 */       this.screen = screen;
/*     */     }
/*     */     
/*     */     public final String toString() {
/*     */       // Byte code:
/*     */       //   0: aload_0
/*     */       //   1: <illegal opcode> toString : (Lnet/minecraft/client/MouseHandler$LastClick;)Ljava/lang/String;
/*     */       //   6: areturn
/*     */       // Line number table:
/*     */       //   Java source line number -> byte code offset
/*     */       //   #454	-> 0
/*     */       // Local variable table:
/*     */       //   start	length	slot	name	descriptor
/*     */       //   0	7	0	this	Lnet/minecraft/client/MouseHandler$LastClick;
/*     */     }
/*     */     
/*     */     public final int hashCode() {
/*     */       // Byte code:
/*     */       //   0: aload_0
/*     */       //   1: <illegal opcode> hashCode : (Lnet/minecraft/client/MouseHandler$LastClick;)I
/*     */       //   6: ireturn
/*     */       // Line number table:
/*     */       //   Java source line number -> byte code offset
/*     */       //   #454	-> 0
/*     */       // Local variable table:
/*     */       //   start	length	slot	name	descriptor
/*     */       //   0	7	0	this	Lnet/minecraft/client/MouseHandler$LastClick;
/*     */     }
/*     */     
/*     */     public final boolean equals(Object o) {
/*     */       // Byte code:
/*     */       //   0: aload_0
/*     */       //   1: aload_1
/*     */       //   2: <illegal opcode> equals : (Lnet/minecraft/client/MouseHandler$LastClick;Ljava/lang/Object;)Z
/*     */       //   7: ireturn
/*     */       // Line number table:
/*     */       //   Java source line number -> byte code offset
/*     */       //   #454	-> 0
/*     */       // Local variable table:
/*     */       //   start	length	slot	name	descriptor
/*     */       //   0	8	0	this	Lnet/minecraft/client/MouseHandler$LastClick;
/*     */       //   0	8	1	o	Ljava/lang/Object;
/*     */     }
/*     */     
/*     */     public long time() {
/* 454 */       return this.time;
/*     */     }
/*     */     
/*     */     public Screen screen() {
/* 454 */       return this.screen;
/*     */     }
/*     */   }
/*     */ }


/* Location:              C:\Users\karol\AppData\Roaming\ModrinthApp\meta\versions\26.2-0.19.5\26.2-0.19.5.jar!\net\minecraft\client\MouseHandler.class
 * Java compiler version: 25 (69.0)
 * JD-Core Version:       1.1.3
 */