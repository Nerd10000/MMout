/*     */ package net.minecraft.client.gui.components.toasts;
/*     */ 
/*     */ import com.google.common.collect.ImmutableList;
/*     */ import java.util.List;
/*     */ import java.util.Objects;
/*     */ import java.util.stream.Stream;
/*     */ import net.minecraft.ChatFormatting;
/*     */ import net.minecraft.client.Minecraft;
/*     */ import net.minecraft.client.gui.Font;
/*     */ import net.minecraft.client.gui.GuiGraphicsExtractor;
/*     */ import net.minecraft.client.renderer.RenderPipelines;
/*     */ import net.minecraft.network.chat.Component;
/*     */ import net.minecraft.network.chat.FormattedText;
/*     */ import net.minecraft.resources.Identifier;
/*     */ import net.minecraft.util.FormattedCharSequence;
/*     */ import net.minecraft.world.level.ChunkPos;
/*     */ 
/*     */ public class SystemToast implements Toast {
/*  20 */   private static final Identifier BACKGROUND_SPRITE = Identifier.withDefaultNamespace("toast/system");
/*     */   
/*     */   private static final int MAX_LINE_SIZE = 200;
/*     */   
/*     */   private static final int LINE_SPACING = 12;
/*     */   
/*     */   private static final int MARGIN = 10;
/*     */   
/*     */   private static final int TEXT_X_START = 18;
/*     */   
/*     */   private final SystemToastId id;
/*     */   
/*     */   private List<FormattedCharSequence> titleLines;
/*     */   
/*     */   private List<FormattedCharSequence> messageLines;
/*     */   
/*     */   private long lastChanged;
/*     */   
/*     */   private boolean changed;
/*     */   
/*     */   private int width;
/*     */   
/*     */   private boolean forceHide;
/*     */   
/*  33 */   private Toast.Visibility wantedVisibility = Toast.Visibility.HIDE;
/*     */   
/*     */   public SystemToast(SystemToastId id, Component title, Component message) {
/*  36 */     this.id = id;
/*  37 */     update(title, message);
/*     */   }
/*     */   
/*     */   private static List<FormattedCharSequence> nullToEmpty(Component text) {
/*  41 */     return (text == null) ? (List<FormattedCharSequence>)ImmutableList.of() : splitToLength(text);
/*     */   }
/*     */   
/*     */   private static List<FormattedCharSequence> splitToLength(Component text) {
/*  45 */     return (Minecraft.getInstance()).font.split((FormattedText)text, 200);
/*     */   }
/*     */   
/*     */   public int width() {
/*  50 */     return this.width;
/*     */   }
/*     */   
/*     */   public int height() {
/*  55 */     int titleHeight = (this.titleLines.size() - 1) * 12;
/*  56 */     int messageHeight = Math.max(this.messageLines.size(), 1) * 12;
/*  57 */     return 20 + titleHeight + messageHeight;
/*     */   }
/*     */   
/*     */   public void forceHide() {
/*  61 */     this.forceHide = true;
/*     */   }
/*     */   
/*     */   public Toast.Visibility getWantedVisibility() {
/*  66 */     return this.wantedVisibility;
/*     */   }
/*     */   
/*     */   public void update(ToastManager manager, long fullyVisibleForMs) {
/*  71 */     if (this.changed) {
/*  72 */       this.lastChanged = fullyVisibleForMs;
/*  73 */       this.changed = false;
/*     */     } 
/*  75 */     double timeToDisplayUpdate = this.id.displayTime * manager.getNotificationDisplayTimeMultiplier();
/*  76 */     long timeSinceUpdate = fullyVisibleForMs - this.lastChanged;
/*  78 */     this.wantedVisibility = (!this.forceHide && timeSinceUpdate < timeToDisplayUpdate) ? Toast.Visibility.SHOW : Toast.Visibility.HIDE;
/*     */   }
/*     */   
/*     */   public void extractRenderState(GuiGraphicsExtractor graphics, Font font, long fullyVisibleForMs) {
/*  83 */     graphics.blitSprite(RenderPipelines.GUI_TEXTURED, BACKGROUND_SPRITE, 0, 0, width(), height());
/*  84 */     if (this.messageLines.isEmpty()) {
/*  85 */       extractTextLines(graphics, font, this.titleLines, 12, -256);
/*     */     } else {
/*  87 */       extractTextLines(graphics, font, this.titleLines, 7, -256);
/*  88 */       extractTextLines(graphics, font, this.messageLines, 7 + this.titleLines.size() * 12, -1);
/*     */     } 
/*     */   }
/*     */   
/*     */   private void extractTextLines(GuiGraphicsExtractor graphics, Font font, List<FormattedCharSequence> textLines, int yStart, int textColor) {
/*  93 */     for (int i = 0; i < textLines.size(); i++)
/*  94 */       graphics.text(font, textLines.get(i), 18, yStart + i * 12, textColor, false); 
/*     */   }
/*     */   
/*     */   public void reset(Component title, Component message) {
/*  99 */     update(title, message);
/* 100 */     this.changed = true;
/*     */   }
/*     */   
/*     */   private void update(Component title, Component message) {
/* 104 */     this.titleLines = splitToLength(title);
/* 105 */     this.messageLines = nullToEmpty(message);
/* 106 */     recalculateWidth();
/*     */   }
/*     */   
/*     */   public void recalculateWidth() {
/* 113 */     Objects.requireNonNull((Minecraft.getInstance()).font);
/* 113 */     int width = Math.max(160, Stream.concat(this.titleLines.stream(), this.messageLines.stream()).mapToInt((Minecraft.getInstance()).font::width)
/* 114 */         .max()
/* 115 */         .orElse(200));
/* 117 */     this.width = width + 30;
/*     */   }
/*     */   
/*     */   public SystemToastId getToken() {
/* 122 */     return this.id;
/*     */   }
/*     */   
/*     */   public static class SystemToastId {
/* 126 */     public static final SystemToastId NARRATOR_TOGGLE = new SystemToastId();
/*     */     
/* 127 */     public static final SystemToastId WORLD_BACKUP = new SystemToastId();
/*     */     
/* 128 */     public static final SystemToastId PACK_LOAD_FAILURE = new SystemToastId();
/*     */     
/* 129 */     public static final SystemToastId WORLD_ACCESS_FAILURE = new SystemToastId();
/*     */     
/* 130 */     public static final SystemToastId PACK_COPY_FAILURE = new SystemToastId();
/*     */     
/* 131 */     public static final SystemToastId FILE_DROP_FAILURE = new SystemToastId();
/*     */     
/* 132 */     public static final SystemToastId PERIODIC_NOTIFICATION = new SystemToastId();
/*     */     
/* 133 */     public static final SystemToastId LOW_DISK_SPACE = new SystemToastId(10000L);
/*     */     
/* 134 */     public static final SystemToastId CHUNK_LOAD_FAILURE = new SystemToastId();
/*     */     
/* 135 */     public static final SystemToastId CHUNK_SAVE_FAILURE = new SystemToastId();
/*     */     
/* 136 */     public static final SystemToastId UNSECURE_SERVER_WARNING = new SystemToastId(10000L);
/*     */     
/* 137 */     public static final SystemToastId FRIEND_SYSTEM_NOTIFICATION = new SystemToastId();
/*     */     
/*     */     private final long displayTime;
/*     */     
/*     */     public SystemToastId(long displayTime) {
/* 142 */       this.displayTime = displayTime;
/*     */     }
/*     */     
/*     */     public SystemToastId() {
/* 146 */       this(5000L);
/*     */     }
/*     */   }
/*     */   
/*     */   public static void add(ToastManager toastManager, SystemToastId id, Component title, Component message) {
/* 151 */     toastManager.addToast(new SystemToast(id, title, message));
/*     */   }
/*     */   
/*     */   public static void addOrUpdate(ToastManager toastManager, SystemToastId id, Component title, Component message) {
/* 155 */     SystemToast toast = toastManager.<SystemToast>getToast(SystemToast.class, id);
/* 156 */     if (toast == null) {
/* 157 */       add(toastManager, id, title, message);
/*     */     } else {
/* 159 */       toast.reset(title, message);
/*     */     } 
/*     */   }
/*     */   
/*     */   public static void forceHide(ToastManager toastManager, SystemToastId id) {
/* 164 */     SystemToast toast = toastManager.<SystemToast>getToast(SystemToast.class, id);
/* 165 */     if (toast != null)
/* 166 */       toast.forceHide(); 
/*     */   }
/*     */   
/*     */   public static void onWorldAccessFailure(Minecraft minecraft, String levelId) {
/* 171 */     add(minecraft.gui.toastManager(), SystemToastId.WORLD_ACCESS_FAILURE, (Component)Component.translatable("selectWorld.access_failure"), (Component)Component.literal(levelId));
/*     */   }
/*     */   
/*     */   public static void onWorldDeleteFailure(Minecraft minecraft, String levelId) {
/* 175 */     add(minecraft.gui.toastManager(), SystemToastId.WORLD_ACCESS_FAILURE, (Component)Component.translatable("selectWorld.delete_failure"), (Component)Component.literal(levelId));
/*     */   }
/*     */   
/*     */   public static void onPackCopyFailure(Minecraft minecraft, String extraInfo) {
/* 179 */     add(minecraft.gui.toastManager(), SystemToastId.PACK_COPY_FAILURE, (Component)Component.translatable("pack.copyFailure"), (Component)Component.literal(extraInfo));
/*     */   }
/*     */   
/*     */   public static void onFileDropFailure(Minecraft minecraft, int count) {
/* 183 */     add(minecraft.gui.toastManager(), SystemToastId.FILE_DROP_FAILURE, (Component)Component.translatable("gui.fileDropFailure.title"), (Component)Component.translatable("gui.fileDropFailure.detail", new Object[] { Integer.valueOf(count) }));
/*     */   }
/*     */   
/*     */   public static void onLowDiskSpace(Minecraft minecraft) {
/* 187 */     addOrUpdate(minecraft.gui.toastManager(), SystemToastId.LOW_DISK_SPACE, (Component)Component.translatable("chunk.toast.lowDiskSpace"), (Component)Component.translatable("chunk.toast.lowDiskSpace.description"));
/*     */   }
/*     */   
/*     */   public static void onChunkLoadFailure(Minecraft minecraft, ChunkPos pos) {
/* 191 */     addOrUpdate(minecraft.gui
/* 192 */         .toastManager(), SystemToastId.CHUNK_LOAD_FAILURE, 
/*     */         
/* 194 */         (Component)Component.translatable("chunk.toast.loadFailure", new Object[] { Component.translationArg(pos) }).withStyle(ChatFormatting.RED), 
/* 195 */         (Component)Component.translatable("chunk.toast.checkLog"));
/*     */   }
/*     */   
/*     */   public static void onChunkSaveFailure(Minecraft minecraft, ChunkPos pos) {
/* 200 */     addOrUpdate(minecraft.gui
/* 201 */         .toastManager(), SystemToastId.CHUNK_SAVE_FAILURE, 
/*     */         
/* 203 */         (Component)Component.translatable("chunk.toast.saveFailure", new Object[] { Component.translationArg(pos) }).withStyle(ChatFormatting.RED), 
/* 204 */         (Component)Component.translatable("chunk.toast.checkLog"));
/*     */   }
/*     */ }


/* Location:              C:\Users\karol\AppData\Roaming\ModrinthApp\meta\versions\26.2-0.19.5\26.2-0.19.5.jar!\net\minecraft\client\gui\components\toasts\SystemToast.class
 * Java compiler version: 25 (69.0)
 * JD-Core Version:       1.1.3
 */