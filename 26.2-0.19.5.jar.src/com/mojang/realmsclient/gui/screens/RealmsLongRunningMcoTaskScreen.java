/*     */ package com.mojang.realmsclient.gui.screens;
/*     */ 
/*     */ import com.mojang.logging.LogUtils;
/*     */ import com.mojang.realmsclient.exception.RealmsDefaultUncaughtExceptionHandler;
/*     */ import com.mojang.realmsclient.util.task.LongRunningTask;
/*     */ import java.time.Duration;
/*     */ import java.util.List;
/*     */ import net.minecraft.client.GameNarrator;
/*     */ import net.minecraft.client.gui.components.AbstractWidget;
/*     */ import net.minecraft.client.gui.components.Button;
/*     */ import net.minecraft.client.gui.components.LoadingDotsWidget;
/*     */ import net.minecraft.client.gui.components.events.GuiEventListener;
/*     */ import net.minecraft.client.gui.layouts.FrameLayout;
/*     */ import net.minecraft.client.gui.layouts.LayoutElement;
/*     */ import net.minecraft.client.gui.layouts.LayoutSettings;
/*     */ import net.minecraft.client.gui.layouts.LinearLayout;
/*     */ import net.minecraft.client.gui.screens.Screen;
/*     */ import net.minecraft.client.input.KeyEvent;
/*     */ import net.minecraft.network.chat.CommonComponents;
/*     */ import net.minecraft.network.chat.Component;
/*     */ import net.minecraft.realms.RealmsScreen;
/*     */ import net.minecraft.realms.RepeatedNarrator;
/*     */ import org.slf4j.Logger;
/*     */ 
/*     */ public class RealmsLongRunningMcoTaskScreen extends RealmsScreen {
/*  24 */   private static final Logger LOGGER = LogUtils.getLogger();
/*     */   
/*  26 */   private static final RepeatedNarrator REPEATED_NARRATOR = new RepeatedNarrator(Duration.ofSeconds(5L));
/*     */   
/*     */   private final List<LongRunningTask> queuedTasks;
/*     */   
/*     */   private final Screen lastScreen;
/*     */   
/*  31 */   protected final LinearLayout layout = LinearLayout.vertical();
/*     */   
/*     */   private volatile Component title;
/*     */   
/*     */   private LoadingDotsWidget loadingDotsWidget;
/*     */   
/*     */   public RealmsLongRunningMcoTaskScreen(Screen lastScreen, LongRunningTask... tasks) {
/*  37 */     super(GameNarrator.NO_TITLE);
/*  38 */     this.lastScreen = lastScreen;
/*  39 */     this.queuedTasks = List.of(tasks);
/*  40 */     if (this.queuedTasks.isEmpty())
/*  41 */       throw new IllegalArgumentException("No tasks added"); 
/*  44 */     this.title = ((LongRunningTask)this.queuedTasks.get(0)).getTitle();
/*  45 */     Runnable runnable = () -> {
/*     */         LongRunningTask[] arr$ = tasks;
/*     */         int len$ = arr$.length;
/*     */         int i$ = 0;
/*     */         while (i$ < len$) {
/*     */           LongRunningTask task = arr$[i$];
/*     */           setTitle(task.getTitle());
/*     */           if (!task.aborted()) {
/*     */             task.run();
/*     */             if (task.aborted())
/*     */               return; 
/*     */             i$++;
/*     */           } 
/*     */         } 
/*     */       };
/*  59 */     Thread thread = new Thread(runnable, "Realms-long-running-task");
/*  60 */     thread.setUncaughtExceptionHandler((Thread.UncaughtExceptionHandler)new RealmsDefaultUncaughtExceptionHandler(LOGGER));
/*  61 */     thread.start();
/*     */   }
/*     */   
/*     */   public boolean canInterruptWithAnotherScreen() {
/*  66 */     return false;
/*     */   }
/*     */   
/*     */   public void tick() {
/*  71 */     super.tick();
/*  73 */     if (this.loadingDotsWidget != null)
/*  74 */       REPEATED_NARRATOR.narrate(this.minecraft.getNarrator(), this.loadingDotsWidget.getMessage()); 
/*     */   }
/*     */   
/*     */   public boolean keyPressed(KeyEvent event) {
/*  80 */     if (event.isEscape()) {
/*  81 */       cancel();
/*  82 */       return true;
/*     */     } 
/*  84 */     return super.keyPressed(event);
/*     */   }
/*     */   
/*     */   public void init() {
/*  89 */     this.layout.defaultCellSetting().alignHorizontallyCenter();
/*  90 */     this.layout.addChild((LayoutElement)realmsLogo());
/*  91 */     this.loadingDotsWidget = new LoadingDotsWidget(this.font, this.title);
/*  92 */     this.layout.addChild((LayoutElement)this.loadingDotsWidget, layoutSettings -> layoutSettings.paddingTop(10).paddingBottom(30));
/*  93 */     this.layout.addChild((LayoutElement)Button.builder(CommonComponents.GUI_CANCEL, button -> cancel()).build());
/*  94 */     RealmsLongRunningMcoTaskScreen realmsLongRunningMcoTaskScreen = this;
/*  94 */     this.layout.visitWidgets(x$0 -> rec$.addRenderableWidget((GuiEventListener)x$0));
/*  95 */     repositionElements();
/*     */   }
/*     */   
/*     */   protected void repositionElements() {
/* 100 */     this.layout.arrangeElements();
/* 101 */     FrameLayout.centerInRectangle((LayoutElement)this.layout, getRectangle());
/*     */   }
/*     */   
/*     */   protected void cancel() {
/* 105 */     for (LongRunningTask queuedTask : this.queuedTasks)
/* 106 */       queuedTask.abortTask(); 
/* 108 */     this.minecraft.gui.setScreen(this.lastScreen);
/*     */   }
/*     */   
/*     */   public void setTitle(Component title) {
/* 112 */     if (this.loadingDotsWidget != null)
/* 113 */       this.loadingDotsWidget.setMessage(title); 
/* 115 */     this.title = title;
/*     */   }
/*     */ }


/* Location:              C:\Users\karol\AppData\Roaming\ModrinthApp\meta\versions\26.2-0.19.5\26.2-0.19.5.jar!\com\mojang\realmsclient\gui\screens\RealmsLongRunningMcoTaskScreen.class
 * Java compiler version: 25 (69.0)
 * JD-Core Version:       1.1.3
 */