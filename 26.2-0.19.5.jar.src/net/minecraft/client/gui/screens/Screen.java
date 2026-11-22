/*     */ package net.minecraft.client.gui.screens;
/*     */ 
/*     */ import com.google.common.annotations.VisibleForTesting;
/*     */ import com.google.common.collect.Lists;
/*     */ import com.mojang.logging.LogUtils;
/*     */ import java.net.URI;
/*     */ import java.nio.file.Path;
/*     */ import java.util.Comparator;
/*     */ import java.util.List;
/*     */ import java.util.concurrent.Executor;
/*     */ import java.util.concurrent.TimeUnit;
/*     */ import java.util.stream.Stream;
/*     */ import net.minecraft.CrashReport;
/*     */ import net.minecraft.CrashReportCategory;
/*     */ import net.minecraft.SharedConstants;
/*     */ import net.minecraft.client.Minecraft;
/*     */ import net.minecraft.client.NarratorStatus;
/*     */ import net.minecraft.client.gui.ComponentPath;
/*     */ import net.minecraft.client.gui.Font;
/*     */ import net.minecraft.client.gui.GuiGraphicsExtractor;
/*     */ import net.minecraft.client.gui.components.AbstractWidget;
/*     */ import net.minecraft.client.gui.components.CycleButton;
/*     */ import net.minecraft.client.gui.components.Renderable;
/*     */ import net.minecraft.client.gui.components.TabOrderedElement;
/*     */ import net.minecraft.client.gui.components.events.AbstractContainerEventHandler;
/*     */ import net.minecraft.client.gui.components.events.ContainerEventHandler;
/*     */ import net.minecraft.client.gui.components.events.GuiEventListener;
/*     */ import net.minecraft.client.gui.narration.NarratableEntry;
/*     */ import net.minecraft.client.gui.narration.NarratedElementType;
/*     */ import net.minecraft.client.gui.narration.NarrationElementOutput;
/*     */ import net.minecraft.client.gui.narration.ScreenNarrationCollector;
/*     */ import net.minecraft.client.gui.navigation.FocusNavigationEvent;
/*     */ import net.minecraft.client.gui.navigation.ScreenDirection;
/*     */ import net.minecraft.client.gui.navigation.ScreenRectangle;
/*     */ import net.minecraft.client.input.KeyEvent;
/*     */ import net.minecraft.client.player.LocalPlayer;
/*     */ import net.minecraft.client.renderer.RenderPipelines;
/*     */ import net.minecraft.commands.Commands;
/*     */ import net.minecraft.network.chat.ClickEvent;
/*     */ import net.minecraft.network.chat.Component;
/*     */ import net.minecraft.resources.Identifier;
/*     */ import net.minecraft.sounds.Music;
/*     */ import net.minecraft.util.Util;
/*     */ import net.minecraft.world.entity.player.Player;
/*     */ import net.minecraft.world.item.Item;
/*     */ import net.minecraft.world.item.ItemStack;
/*     */ import net.minecraft.world.item.TooltipFlag;
/*     */ import net.minecraft.world.level.Level;
/*     */ import org.slf4j.Logger;
/*     */ 
/*     */ public abstract class Screen extends AbstractContainerEventHandler implements Renderable {
/*  53 */   private static final Logger LOGGER = LogUtils.getLogger();
/*     */   
/*  54 */   private static final Component SCREEN_USAGE_NARRATION = (Component)Component.translatable("narrator.screen.usage");
/*     */   
/*  56 */   public static final Identifier MENU_BACKGROUND = Identifier.withDefaultNamespace("textures/gui/menu_background.png");
/*     */   
/*  57 */   public static final Identifier HEADER_SEPARATOR = Identifier.withDefaultNamespace("textures/gui/header_separator.png");
/*     */   
/*  58 */   public static final Identifier FOOTER_SEPARATOR = Identifier.withDefaultNamespace("textures/gui/footer_separator.png");
/*     */   
/*  59 */   private static final Identifier INWORLD_MENU_BACKGROUND = Identifier.withDefaultNamespace("textures/gui/inworld_menu_background.png");
/*     */   
/*  60 */   public static final Identifier INWORLD_HEADER_SEPARATOR = Identifier.withDefaultNamespace("textures/gui/inworld_header_separator.png");
/*     */   
/*  61 */   public static final Identifier INWORLD_FOOTER_SEPARATOR = Identifier.withDefaultNamespace("textures/gui/inworld_footer_separator.png");
/*     */   
/*     */   protected static final float FADE_IN_TIME = 2000.0F;
/*     */   
/*     */   protected final Component title;
/*     */   
/*  67 */   private final List<GuiEventListener> children = Lists.newArrayList();
/*     */   
/*  68 */   private final List<NarratableEntry> narratables = Lists.newArrayList();
/*     */   
/*     */   protected final Minecraft minecraft;
/*     */   
/*     */   private boolean initialized;
/*     */   
/*     */   public int width;
/*     */   
/*     */   public int height;
/*     */   
/*  73 */   private final List<Renderable> renderables = Lists.newArrayList();
/*     */   
/*     */   protected final Font font;
/*     */   
/*  76 */   private static final long NARRATE_SUPPRESS_AFTER_INIT_TIME = TimeUnit.SECONDS.toMillis(2L);
/*     */   
/*  77 */   private static final long NARRATE_DELAY_NARRATOR_ENABLED = NARRATE_SUPPRESS_AFTER_INIT_TIME;
/*     */   
/*     */   private static final long NARRATE_DELAY_MOUSE_MOVE = 750L;
/*     */   
/*     */   private static final long NARRATE_DELAY_MOUSE_ACTION = 200L;
/*     */   
/*     */   private static final long NARRATE_DELAY_KEYBOARD_ACTION = 200L;
/*     */   
/*  82 */   private final ScreenNarrationCollector narrationState = new ScreenNarrationCollector();
/*     */   
/*  83 */   private long narrationSuppressTime = Long.MIN_VALUE;
/*     */   
/*  84 */   private long nextNarrationTime = Long.MAX_VALUE;
/*     */   
/*     */   protected CycleButton<NarratorStatus> narratorButton;
/*     */   
/*     */   private NarratableEntry lastNarratable;
/*     */   
/*     */   protected final Executor screenExecutor;
/*     */   
/*     */   protected Screen(Component title) {
/*  91 */     this(Minecraft.getInstance(), (Minecraft.getInstance()).font, title);
/*     */   }
/*     */   
/*     */   protected Screen(Minecraft minecraft, Font font, Component title) {
/*  95 */     this.minecraft = minecraft;
/*  96 */     this.font = font;
/*  97 */     this.title = title;
/*  99 */     this.screenExecutor = (runnable -> minecraft.execute(()));
/*     */   }
/*     */   
/*     */   public Component getTitle() {
/* 107 */     return this.title;
/*     */   }
/*     */   
/*     */   public Component getNarrationMessage() {
/* 111 */     return getTitle();
/*     */   }
/*     */   
/*     */   public final void extractRenderStateWithTooltipAndSubtitles(GuiGraphicsExtractor graphics, int mouseX, int mouseY, float a) {
/* 115 */     graphics.nextStratum();
/* 116 */     extractBackground(graphics, mouseX, mouseY, a);
/* 117 */     graphics.nextStratum();
/* 119 */     extractRenderState(graphics, mouseX, mouseY, a);
/* 120 */     graphics.extractDeferredElements(mouseX, mouseY, a);
/*     */   }
/*     */   
/*     */   public void extractRenderState(GuiGraphicsExtractor graphics, int mouseX, int mouseY, float a) {
/* 125 */     for (Renderable renderable : this.renderables)
/* 126 */       renderable.extractRenderState(graphics, mouseX, mouseY, a); 
/*     */   }
/*     */   
/*     */   public boolean keyPressed(KeyEvent event) {
/* 132 */     if (event.isEscape() && shouldCloseOnEsc()) {
/* 133 */       onClose();
/* 134 */       return true;
/*     */     } 
/* 136 */     if (super.keyPressed(event))
/* 137 */       return true; 
/* 139 */     switch (event.key()) {
/*     */       case 263:
/*     */       
/*     */       case 262:
/*     */       
/*     */       case 265:
/*     */       
/*     */       case 264:
/*     */       
/*     */       case 258:
/*     */       
/*     */       default:
/*     */         break;
/*     */     } 
/* 146 */     FocusNavigationEvent navigationEvent = null;
/* 147 */     if (navigationEvent != null) {
/* 148 */       ComponentPath focusPath = nextFocusPath(navigationEvent);
/* 150 */       if (focusPath == null && navigationEvent instanceof FocusNavigationEvent.TabNavigation) {
/* 151 */         clearFocus();
/* 152 */         focusPath = nextFocusPath(navigationEvent);
/*     */       } 
/* 154 */       if (focusPath != null)
/* 155 */         changeFocus(focusPath); 
/*     */     } 
/* 158 */     return false;
/*     */   }
/*     */   
/*     */   private FocusNavigationEvent.TabNavigation createTabEvent(boolean forward) {
/* 162 */     return new FocusNavigationEvent.TabNavigation(forward);
/*     */   }
/*     */   
/*     */   private FocusNavigationEvent.ArrowNavigation createArrowEvent(ScreenDirection direction) {
/* 166 */     return new FocusNavigationEvent.ArrowNavigation(direction);
/*     */   }
/*     */   
/*     */   protected void setInitialFocus() {
/* 170 */     if (this.minecraft.getLastInputType().isKeyboard()) {
/* 171 */       FocusNavigationEvent.TabNavigation forwardTabEvent = new FocusNavigationEvent.TabNavigation(true);
/* 172 */       ComponentPath focusPath = nextFocusPath((FocusNavigationEvent)forwardTabEvent);
/* 173 */       if (focusPath != null)
/* 174 */         changeFocus(focusPath); 
/*     */     } 
/*     */   }
/*     */   
/*     */   protected void setInitialFocus(GuiEventListener target) {
/* 180 */     ComponentPath path = ComponentPath.path((ContainerEventHandler)this, target.nextFocusPath((FocusNavigationEvent)new FocusNavigationEvent.InitialFocus()));
/* 181 */     if (path != null)
/* 182 */       changeFocus(path); 
/*     */   }
/*     */   
/*     */   public void clearFocus() {
/* 187 */     ComponentPath componentPath = getCurrentFocusPath();
/* 188 */     if (componentPath != null)
/* 189 */       componentPath.applyFocus(false); 
/*     */   }
/*     */   
/*     */   @VisibleForTesting
/*     */   protected void changeFocus(ComponentPath componentPath) {
/* 195 */     clearFocus();
/* 196 */     componentPath.applyFocus(true);
/*     */   }
/*     */   
/*     */   public boolean shouldCloseOnEsc() {
/* 200 */     return true;
/*     */   }
/*     */   
/*     */   public void onClose() {
/* 204 */     this.minecraft.gui.setScreen(null);
/*     */   }
/*     */   
/*     */   protected <T extends GuiEventListener & Renderable & NarratableEntry> T addRenderableWidget(T widget) {
/* 208 */     this.renderables.add((Renderable)widget);
/* 209 */     return (T)addWidget((GuiEventListener)widget);
/*     */   }
/*     */   
/*     */   protected <T extends Renderable> T addRenderableOnly(T renderable) {
/* 213 */     this.renderables.add((Renderable)renderable);
/* 214 */     return renderable;
/*     */   }
/*     */   
/*     */   protected <T extends GuiEventListener & NarratableEntry> T addWidget(T widget) {
/* 218 */     this.children.add((GuiEventListener)widget);
/* 219 */     this.narratables.add((NarratableEntry)widget);
/* 220 */     return widget;
/*     */   }
/*     */   
/*     */   protected void removeWidget(GuiEventListener widget) {
/* 224 */     if (widget instanceof Renderable) {
/* 224 */       Renderable renderable = (Renderable)widget;
/* 225 */       this.renderables.remove(renderable);
/*     */     } 
/* 227 */     if (widget instanceof NarratableEntry) {
/* 227 */       NarratableEntry narratableEntry = (NarratableEntry)widget;
/* 228 */       this.narratables.remove(narratableEntry);
/*     */     } 
/* 230 */     if (getFocused() == widget)
/* 231 */       clearFocus(); 
/* 233 */     this.children.remove(widget);
/*     */   }
/*     */   
/*     */   protected void clearWidgets() {
/* 237 */     this.renderables.clear();
/* 238 */     this.children.clear();
/* 239 */     this.narratables.clear();
/*     */   }
/*     */   
/*     */   public static List<Component> getTooltipFromItem(Minecraft minecraft, ItemStack itemStack) {
/* 243 */     return itemStack.getTooltipLines(Item.TooltipContext.of((Level)minecraft.level), (Player)minecraft.player, minecraft.options.advancedItemTooltips ? (TooltipFlag)TooltipFlag.Default.ADVANCED : (TooltipFlag)TooltipFlag.Default.NORMAL);
/*     */   }
/*     */   
/*     */   protected void insertText(String text, boolean replace) {}
/*     */   
/*     */   protected static void defaultHandleGameClickEvent(ClickEvent event, Minecraft minecraft, Screen activeScreen) {
/*     */     // Byte code:
/*     */     //   0: aload_1
/*     */     //   1: getfield player : Lnet/minecraft/client/player/LocalPlayer;
/*     */     //   4: ldc_w 'Player not available'
/*     */     //   7: invokestatic requireNonNull : (Ljava/lang/Object;Ljava/lang/String;)Ljava/lang/Object;
/*     */     //   10: checkcast net/minecraft/client/player/LocalPlayer
/*     */     //   13: astore_3
/*     */     //   14: aload_0
/*     */     //   15: dup
/*     */     //   16: invokestatic requireNonNull : (Ljava/lang/Object;)Ljava/lang/Object;
/*     */     //   19: pop
/*     */     //   20: astore #4
/*     */     //   22: iconst_0
/*     */     //   23: istore #5
/*     */     //   25: aload #4
/*     */     //   27: iload #5
/*     */     //   29: <illegal opcode> typeSwitch : (Lnet/minecraft/network/chat/ClickEvent;I)I
/*     */     //   34: tableswitch default -> 164, 0 -> 60, 1 -> 88, 2 -> 111
/*     */     //   60: aload #4
/*     */     //   62: checkcast net/minecraft/network/chat/ClickEvent$RunCommand
/*     */     //   65: astore #6
/*     */     //   67: aload #6
/*     */     //   69: invokevirtual command : ()Ljava/lang/String;
/*     */     //   72: astore #8
/*     */     //   74: aload #8
/*     */     //   76: astore #7
/*     */     //   78: aload_3
/*     */     //   79: aload #7
/*     */     //   81: aload_2
/*     */     //   82: invokestatic clickCommandAction : (Lnet/minecraft/client/player/LocalPlayer;Ljava/lang/String;Lnet/minecraft/client/gui/screens/Screen;)V
/*     */     //   85: goto -> 170
/*     */     //   88: aload #4
/*     */     //   90: checkcast net/minecraft/network/chat/ClickEvent$ShowDialog
/*     */     //   93: astore #8
/*     */     //   95: aload_3
/*     */     //   96: getfield connection : Lnet/minecraft/client/multiplayer/ClientPacketListener;
/*     */     //   99: aload #8
/*     */     //   101: invokevirtual dialog : ()Lnet/minecraft/core/Holder;
/*     */     //   104: aload_2
/*     */     //   105: invokevirtual showDialog : (Lnet/minecraft/core/Holder;Lnet/minecraft/client/gui/screens/Screen;)V
/*     */     //   108: goto -> 170
/*     */     //   111: aload #4
/*     */     //   113: checkcast net/minecraft/network/chat/ClickEvent$Custom
/*     */     //   116: astore #9
/*     */     //   118: aload_3
/*     */     //   119: getfield connection : Lnet/minecraft/client/multiplayer/ClientPacketListener;
/*     */     //   122: new net/minecraft/network/protocol/common/ServerboundCustomClickActionPacket
/*     */     //   125: dup
/*     */     //   126: aload #9
/*     */     //   128: invokevirtual id : ()Lnet/minecraft/resources/Identifier;
/*     */     //   131: aload #9
/*     */     //   133: invokevirtual payload : ()Ljava/util/Optional;
/*     */     //   136: invokespecial <init> : (Lnet/minecraft/resources/Identifier;Ljava/util/Optional;)V
/*     */     //   139: invokevirtual send : (Lnet/minecraft/network/protocol/Packet;)V
/*     */     //   142: aload_1
/*     */     //   143: getfield gui : Lnet/minecraft/client/gui/Gui;
/*     */     //   146: invokevirtual screen : ()Lnet/minecraft/client/gui/screens/Screen;
/*     */     //   149: aload_2
/*     */     //   150: if_acmpeq -> 170
/*     */     //   153: aload_1
/*     */     //   154: getfield gui : Lnet/minecraft/client/gui/Gui;
/*     */     //   157: aload_2
/*     */     //   158: invokevirtual setScreen : (Lnet/minecraft/client/gui/screens/Screen;)V
/*     */     //   161: goto -> 170
/*     */     //   164: aload_0
/*     */     //   165: aload_1
/*     */     //   166: aload_2
/*     */     //   167: invokestatic defaultHandleClickEvent : (Lnet/minecraft/network/chat/ClickEvent;Lnet/minecraft/client/Minecraft;Lnet/minecraft/client/gui/screens/Screen;)V
/*     */     //   170: goto -> 190
/*     */     //   173: astore #4
/*     */     //   175: new java/lang/MatchException
/*     */     //   178: dup
/*     */     //   179: aload #4
/*     */     //   181: invokevirtual toString : ()Ljava/lang/String;
/*     */     //   184: aload #4
/*     */     //   186: invokespecial <init> : (Ljava/lang/String;Ljava/lang/Throwable;)V
/*     */     //   189: athrow
/*     */     //   190: return
/*     */     // Line number table:
/*     */     //   Java source line number -> byte code offset
/*     */     //   #252	-> 0
/*     */     //   #254	-> 14
/*     */     //   #255	-> 60
/*     */     //   #256	-> 88
/*     */     //   #257	-> 111
/*     */     //   #258	-> 118
/*     */     //   #259	-> 142
/*     */     //   #260	-> 153
/*     */     //   #263	-> 164
/*     */     //   #257	-> 173
/*     */     //   #265	-> 190
/*     */     // Local variable table:
/*     */     //   start	length	slot	name	descriptor
/*     */     //   78	10	7	command	Ljava/lang/String;
/*     */     //   95	16	8	dialog	Lnet/minecraft/network/chat/ClickEvent$ShowDialog;
/*     */     //   118	46	9	custom	Lnet/minecraft/network/chat/ClickEvent$Custom;
/*     */     //   14	159	3	player	Lnet/minecraft/client/player/LocalPlayer;
/*     */     //   0	191	0	event	Lnet/minecraft/network/chat/ClickEvent;
/*     */     //   0	191	1	minecraft	Lnet/minecraft/client/Minecraft;
/*     */     //   0	191	2	activeScreen	Lnet/minecraft/client/gui/screens/Screen;
/*     */     //   190	1	3	player	Lnet/minecraft/client/player/LocalPlayer;
/*     */     // Exception table:
/*     */     //   from	to	target	type
/*     */     //   69	72	173	java/lang/Throwable
/*     */   }
/*     */   
/*     */   protected static void defaultHandleClickEvent(ClickEvent event, Minecraft minecraft, Screen activeScreen) {
/*     */     // Byte code:
/*     */     //   0: aload_0
/*     */     //   1: dup
/*     */     //   2: invokestatic requireNonNull : (Ljava/lang/Object;)Ljava/lang/Object;
/*     */     //   5: pop
/*     */     //   6: astore #4
/*     */     //   8: iconst_0
/*     */     //   9: istore #5
/*     */     //   11: aload #4
/*     */     //   13: iload #5
/*     */     //   15: <illegal opcode> typeSwitch : (Lnet/minecraft/network/chat/ClickEvent;I)I
/*     */     //   20: tableswitch default -> 168, 0 -> 52, 1 -> 82, 2 -> 104, 3 -> 137
/*     */     //   52: aload #4
/*     */     //   54: checkcast net/minecraft/network/chat/ClickEvent$OpenUrl
/*     */     //   57: astore #6
/*     */     //   59: aload #6
/*     */     //   61: invokevirtual uri : ()Ljava/net/URI;
/*     */     //   64: astore #8
/*     */     //   66: aload #8
/*     */     //   68: astore #7
/*     */     //   70: aload_1
/*     */     //   71: aload_2
/*     */     //   72: aload #7
/*     */     //   74: invokestatic clickUrlAction : (Lnet/minecraft/client/Minecraft;Lnet/minecraft/client/gui/screens/Screen;Ljava/net/URI;)Z
/*     */     //   77: pop
/*     */     //   78: iconst_0
/*     */     //   79: goto -> 181
/*     */     //   82: aload #4
/*     */     //   84: checkcast net/minecraft/network/chat/ClickEvent$OpenFile
/*     */     //   87: astore #8
/*     */     //   89: invokestatic getPlatform : ()Lnet/minecraft/util/Util$OS;
/*     */     //   92: aload #8
/*     */     //   94: invokevirtual file : ()Ljava/io/File;
/*     */     //   97: invokevirtual openFile : (Ljava/io/File;)V
/*     */     //   100: iconst_1
/*     */     //   101: goto -> 181
/*     */     //   104: aload #4
/*     */     //   106: checkcast net/minecraft/network/chat/ClickEvent$SuggestCommand
/*     */     //   109: astore #9
/*     */     //   111: aload #9
/*     */     //   113: invokevirtual command : ()Ljava/lang/String;
/*     */     //   116: astore #11
/*     */     //   118: aload #11
/*     */     //   120: astore #10
/*     */     //   122: aload_2
/*     */     //   123: ifnull -> 133
/*     */     //   126: aload_2
/*     */     //   127: aload #10
/*     */     //   129: iconst_1
/*     */     //   130: invokevirtual insertText : (Ljava/lang/String;Z)V
/*     */     //   133: iconst_1
/*     */     //   134: goto -> 181
/*     */     //   137: aload #4
/*     */     //   139: checkcast net/minecraft/network/chat/ClickEvent$CopyToClipboard
/*     */     //   142: astore #11
/*     */     //   144: aload #11
/*     */     //   146: invokevirtual value : ()Ljava/lang/String;
/*     */     //   149: astore #13
/*     */     //   151: aload #13
/*     */     //   153: astore #12
/*     */     //   155: aload_1
/*     */     //   156: getfield keyboardHandler : Lnet/minecraft/client/KeyboardHandler;
/*     */     //   159: aload #12
/*     */     //   161: invokevirtual setClipboard : (Ljava/lang/String;)V
/*     */     //   164: iconst_1
/*     */     //   165: goto -> 181
/*     */     //   168: getstatic net/minecraft/client/gui/screens/Screen.LOGGER : Lorg/slf4j/Logger;
/*     */     //   171: ldc_w 'Don't know how to handle {}'
/*     */     //   174: aload_0
/*     */     //   175: invokeinterface error : (Ljava/lang/String;Ljava/lang/Object;)V
/*     */     //   180: iconst_1
/*     */     //   181: istore_3
/*     */     //   182: iload_3
/*     */     //   183: ifeq -> 225
/*     */     //   186: aload_1
/*     */     //   187: getfield gui : Lnet/minecraft/client/gui/Gui;
/*     */     //   190: invokevirtual screen : ()Lnet/minecraft/client/gui/screens/Screen;
/*     */     //   193: aload_2
/*     */     //   194: if_acmpeq -> 225
/*     */     //   197: aload_1
/*     */     //   198: getfield gui : Lnet/minecraft/client/gui/Gui;
/*     */     //   201: aload_2
/*     */     //   202: invokevirtual setScreen : (Lnet/minecraft/client/gui/screens/Screen;)V
/*     */     //   205: goto -> 225
/*     */     //   208: astore #4
/*     */     //   210: new java/lang/MatchException
/*     */     //   213: dup
/*     */     //   214: aload #4
/*     */     //   216: invokevirtual toString : ()Ljava/lang/String;
/*     */     //   219: aload #4
/*     */     //   221: invokespecial <init> : (Ljava/lang/String;Ljava/lang/Throwable;)V
/*     */     //   224: athrow
/*     */     //   225: return
/*     */     // Line number table:
/*     */     //   Java source line number -> byte code offset
/*     */     //   #268	-> 0
/*     */     //   #269	-> 52
/*     */     //   #270	-> 70
/*     */     //   #272	-> 78
/*     */     //   #274	-> 82
/*     */     //   #275	-> 89
/*     */     //   #276	-> 100
/*     */     //   #278	-> 104
/*     */     //   #279	-> 122
/*     */     //   #280	-> 126
/*     */     //   #282	-> 133
/*     */     //   #284	-> 137
/*     */     //   #285	-> 155
/*     */     //   #286	-> 164
/*     */     //   #289	-> 168
/*     */     //   #291	-> 180
/*     */     //   #293	-> 181
/*     */     //   #294	-> 182
/*     */     //   #295	-> 197
/*     */     //   #284	-> 208
/*     */     //   #297	-> 225
/*     */     // Local variable table:
/*     */     //   start	length	slot	name	descriptor
/*     */     //   70	12	7	uri	Ljava/net/URI;
/*     */     //   89	15	8	openFile	Lnet/minecraft/network/chat/ClickEvent$OpenFile;
/*     */     //   122	15	10	command	Ljava/lang/String;
/*     */     //   155	13	12	value	Ljava/lang/String;
/*     */     //   182	26	3	shouldActivateScreen	Z
/*     */     //   0	226	0	event	Lnet/minecraft/network/chat/ClickEvent;
/*     */     //   0	226	1	minecraft	Lnet/minecraft/client/Minecraft;
/*     */     //   0	226	2	activeScreen	Lnet/minecraft/client/gui/screens/Screen;
/*     */     //   225	1	3	shouldActivateScreen	Z
/*     */     // Exception table:
/*     */     //   from	to	target	type
/*     */     //   61	64	208	java/lang/Throwable
/*     */     //   113	116	208	java/lang/Throwable
/*     */     //   146	149	208	java/lang/Throwable
/*     */   }
/*     */   
/*     */   protected static boolean clickUrlAction(Minecraft minecraft, Screen screen, URI uri) {
/* 300 */     if (!((Boolean)minecraft.options.chatLinks().get()).booleanValue())
/* 301 */       return false; 
/* 303 */     if (((Boolean)minecraft.options.chatLinksPrompt().get()).booleanValue()) {
/* 304 */       minecraft.gui.setScreen(new ConfirmLinkScreen(result -> {
/*     */               if (result)
/*     */                 Util.getPlatform().openUri(uri); 
/*     */               minecraft.gui.setScreen(screen);
/* 304 */             }uri
/*     */             
/* 309 */             .toString(), false));
/*     */     } else {
/* 311 */       Util.getPlatform().openUri(uri);
/*     */     } 
/* 313 */     return true;
/*     */   }
/*     */   
/*     */   protected static void clickCommandAction(LocalPlayer player, String command, Screen screenAfterCommand) {
/* 317 */     player.connection.sendUnattendedCommand(Commands.trimOptionalPrefix(command), screenAfterCommand);
/*     */   }
/*     */   
/*     */   public final void init(int width, int height) {
/* 321 */     this.width = width;
/* 322 */     this.height = height;
/* 323 */     if (!this.initialized) {
/* 324 */       init();
/* 325 */       setInitialFocus();
/*     */     } else {
/* 327 */       repositionElements();
/*     */     } 
/* 329 */     this.initialized = true;
/* 330 */     triggerImmediateNarration(false);
/* 332 */     if (this.minecraft.getLastInputType().isKeyboard()) {
/* 333 */       setNarrationSuppressTime(Long.MAX_VALUE);
/*     */     } else {
/* 335 */       suppressNarration(NARRATE_SUPPRESS_AFTER_INIT_TIME);
/*     */     } 
/*     */   }
/*     */   
/*     */   protected void rebuildWidgets() {
/* 340 */     clearWidgets();
/* 341 */     clearFocus();
/* 342 */     init();
/* 343 */     setInitialFocus();
/*     */   }
/*     */   
/*     */   protected void fadeWidgets(float widgetFade) {
/* 347 */     for (GuiEventListener button : children()) {
/* 348 */       if (button instanceof AbstractWidget) {
/* 348 */         AbstractWidget widget = (AbstractWidget)button;
/* 349 */         widget.setAlpha(widgetFade);
/*     */       } 
/*     */     } 
/*     */   }
/*     */   
/*     */   public List<? extends GuiEventListener> children() {
/* 356 */     return this.children;
/*     */   }
/*     */   
/*     */   protected void init() {}
/*     */   
/*     */   public void tick() {}
/*     */   
/*     */   public void removed() {}
/*     */   
/*     */   public void added() {}
/*     */   
/*     */   public void extractBackground(GuiGraphicsExtractor graphics, int mouseX, int mouseY, float a) {
/* 372 */     if (isInGameUi()) {
/* 373 */       extractTransparentBackground(graphics);
/*     */     } else {
/* 375 */       if (this.minecraft.level == null)
/* 376 */         extractPanorama(graphics, a); 
/* 378 */       extractBlurredBackground(graphics);
/* 379 */       extractMenuBackground(graphics);
/*     */     } 
/* 382 */     this.minecraft.gui.hud.extractDeferredSubtitles();
/*     */   }
/*     */   
/*     */   protected void extractBlurredBackground(GuiGraphicsExtractor graphics) {
/* 386 */     float blurRadius = this.minecraft.options.getMenuBackgroundBlurriness();
/* 387 */     if (blurRadius >= 1.0F)
/* 388 */       graphics.blurBeforeThisStratum(); 
/*     */   }
/*     */   
/*     */   protected void extractPanorama(GuiGraphicsExtractor graphics, float a) {
/* 393 */     this.minecraft.gameRenderer.panorama().extractRenderState(graphics, this.width, this.height);
/*     */   }
/*     */   
/*     */   protected void extractMenuBackground(GuiGraphicsExtractor graphics) {
/* 397 */     extractMenuBackground(graphics, 0, 0, this.width, this.height);
/*     */   }
/*     */   
/*     */   protected void extractMenuBackground(GuiGraphicsExtractor graphics, int x, int y, int width, int height) {
/* 401 */     extractMenuBackgroundTexture(graphics, (this.minecraft.level == null) ? MENU_BACKGROUND : INWORLD_MENU_BACKGROUND, x, y, 0.0F, 0.0F, width, height);
/*     */   }
/*     */   
/*     */   public static void extractMenuBackgroundTexture(GuiGraphicsExtractor graphics, Identifier menuBackground, int x, int y, float u, float v, int width, int height) {
/* 405 */     int size = 32;
/* 406 */     graphics.blit(RenderPipelines.GUI_TEXTURED, menuBackground, x, y, u, v, width, height, 32, 32);
/*     */   }
/*     */   
/*     */   public void extractTransparentBackground(GuiGraphicsExtractor graphics) {
/* 410 */     graphics.fillGradient(0, 0, this.width, this.height, -1072689136, -804253680);
/*     */   }
/*     */   
/*     */   public boolean isPauseScreen() {
/* 414 */     return true;
/*     */   }
/*     */   
/*     */   public boolean isInGameUi() {
/* 418 */     return false;
/*     */   }
/*     */   
/*     */   public boolean isAllowedInPortal() {
/* 422 */     return isPauseScreen();
/*     */   }
/*     */   
/*     */   protected void repositionElements() {
/* 427 */     rebuildWidgets();
/*     */   }
/*     */   
/*     */   public void resize(int width, int height) {
/* 432 */     this.width = width;
/* 433 */     this.height = height;
/* 434 */     repositionElements();
/*     */   }
/*     */   
/*     */   public void fillCrashDetails(CrashReport report) {
/* 438 */     CrashReportCategory category = report.addCategory("Affected screen", 1);
/* 439 */     category.setDetail("Screen name", () -> getClass().getCanonicalName());
/*     */   }
/*     */   
/*     */   protected boolean isValidCharacterForName(String currentName, int newChar, int cursorPos) {
/* 443 */     int colonPos = currentName.indexOf(':');
/* 444 */     int slashPos = currentName.indexOf('/');
/* 446 */     if (newChar == 58)
/* 447 */       return ((slashPos == -1 || cursorPos <= slashPos) && colonPos == -1); 
/* 450 */     if (newChar == 47)
/* 451 */       return (cursorPos > colonPos); 
/* 454 */     return (newChar == 95 || newChar == 45 || (newChar >= 97 && newChar <= 122) || (newChar >= 48 && newChar <= 57) || newChar == 46);
/*     */   }
/*     */   
/*     */   public boolean isMouseOver(double mouseX, double mouseY) {
/* 459 */     return true;
/*     */   }
/*     */   
/*     */   public void onFilesDrop(List<Path> files) {}
/*     */   
/*     */   private void scheduleNarration(long delay, boolean ignoreSuppression) {
/* 466 */     this.nextNarrationTime = Util.getMillis() + delay;
/* 467 */     if (ignoreSuppression)
/* 468 */       this.narrationSuppressTime = Long.MIN_VALUE; 
/*     */   }
/*     */   
/*     */   private void suppressNarration(long duration) {
/* 473 */     setNarrationSuppressTime(Util.getMillis() + duration);
/*     */   }
/*     */   
/*     */   private void setNarrationSuppressTime(long narrationSuppressTime) {
/* 477 */     this.narrationSuppressTime = narrationSuppressTime;
/*     */   }
/*     */   
/*     */   public void afterMouseMove() {
/* 481 */     scheduleNarration(750L, false);
/*     */   }
/*     */   
/*     */   public void afterMouseAction() {
/* 485 */     scheduleNarration(200L, true);
/*     */   }
/*     */   
/*     */   public void afterKeyboardAction() {
/* 489 */     scheduleNarration(200L, true);
/*     */   }
/*     */   
/*     */   private boolean shouldRunNarration() {
/* 493 */     return (SharedConstants.DEBUG_UI_NARRATION || this.minecraft.getNarrator().isActive());
/*     */   }
/*     */   
/*     */   public void handleDelayedNarration() {
/* 497 */     if (shouldRunNarration()) {
/* 498 */       long currentTime = Util.getMillis();
/* 499 */       if (currentTime > this.nextNarrationTime && currentTime > this.narrationSuppressTime) {
/* 500 */         runNarration(true);
/* 501 */         this.nextNarrationTime = Long.MAX_VALUE;
/*     */       } 
/*     */     } 
/*     */   }
/*     */   
/*     */   public void triggerImmediateNarration(boolean onlyChanged) {
/* 507 */     if (shouldRunNarration())
/* 508 */       runNarration(onlyChanged); 
/*     */   }
/*     */   
/*     */   private void runNarration(boolean onlyChanged) {
/* 513 */     this.narrationState.update(this::updateNarrationState);
/* 514 */     String narration = this.narrationState.collectNarrationText(!onlyChanged);
/* 515 */     if (!narration.isEmpty())
/* 516 */       this.minecraft.getNarrator().saySystemNow(narration); 
/*     */   }
/*     */   
/*     */   protected boolean shouldNarrateNavigation() {
/* 521 */     return true;
/*     */   }
/*     */   
/*     */   protected void updateNarrationState(NarrationElementOutput output) {
/* 525 */     output.add(NarratedElementType.TITLE, getNarrationMessage());
/* 526 */     updateNarratedWidget(output);
/*     */   }
/*     */   
/*     */   protected void updateNarratedWidget(NarrationElementOutput output) {
/* 534 */     List<? extends NarratableEntry> activeNarratables = this.narratables.stream().flatMap(narratableEntry -> narratableEntry.getNarratables().stream()).filter(NarratableEntry::isActive).sorted(Comparator.comparingInt(TabOrderedElement::getTabOrderGroup)).toList();
/* 536 */     NarratableSearchResult result = findNarratableWidget(activeNarratables, this.lastNarratable);
/* 537 */     if (result != null) {
/* 539 */       if (result.priority.isTerminal())
/* 540 */         this.lastNarratable = result.entry; 
/* 542 */       if (activeNarratables.size() > 1) {
/* 543 */         output.add(NarratedElementType.POSITION, (Component)Component.translatable("narrator.position.screen", new Object[] { Integer.valueOf(result.index + 1), Integer.valueOf(activeNarratables.size()) }));
/* 544 */         if (result.priority == NarratableEntry.NarrationPriority.FOCUSED)
/* 545 */           output.add(NarratedElementType.USAGE, getUsageNarration()); 
/*     */       } 
/* 548 */       result.entry.updateNarration(output.nest());
/* 549 */     } else if (shouldNarrateNavigation()) {
/* 550 */       output.add(NarratedElementType.USAGE, SCREEN_USAGE_NARRATION);
/*     */     } 
/*     */   }
/*     */   
/*     */   protected Component getUsageNarration() {
/* 555 */     return (Component)Component.translatable("narration.component_list.usage");
/*     */   }
/*     */   
/*     */   public static NarratableSearchResult findNarratableWidget(List<? extends NarratableEntry> narratableEntries, NarratableEntry lastNarratable) {
/* 559 */     NarratableSearchResult result = null;
/* 560 */     NarratableSearchResult lowPrioNarratable = null;
/* 561 */     for (int i = 0, narratablesSize = narratableEntries.size(); i < narratablesSize; i++) {
/* 562 */       NarratableEntry narratable = narratableEntries.get(i);
/* 563 */       NarratableEntry.NarrationPriority priority = narratable.narrationPriority();
/* 564 */       if (priority.isTerminal()) {
/* 565 */         if (narratable == lastNarratable) {
/* 566 */           lowPrioNarratable = new NarratableSearchResult(narratable, i, priority);
/*     */         } else {
/* 568 */           return new NarratableSearchResult(narratable, i, priority);
/*     */         } 
/* 570 */       } else if (priority.compareTo((result != null) ? (Enum)result.priority : (Enum)NarratableEntry.NarrationPriority.NONE) > 0) {
/* 571 */         result = new NarratableSearchResult(narratable, i, priority);
/*     */       } 
/*     */     } 
/* 575 */     return (result != null) ? result : lowPrioNarratable;
/*     */   }
/*     */   
/*     */   public void updateNarratorStatus(boolean wasDisabled) {
/* 579 */     if (wasDisabled)
/* 580 */       scheduleNarration(NARRATE_DELAY_NARRATOR_ENABLED, false); 
/* 583 */     if (this.narratorButton != null)
/* 584 */       this.narratorButton.setValue(this.minecraft.options.narrator().get()); 
/*     */   }
/*     */   
/*     */   public Font getFont() {
/* 589 */     return this.font;
/*     */   }
/*     */   
/*     */   public boolean showsActiveEffects() {
/* 593 */     return false;
/*     */   }
/*     */   
/*     */   public boolean canInterruptWithAnotherScreen() {
/* 597 */     return shouldCloseOnEsc();
/*     */   }
/*     */   
/*     */   public static final class NarratableSearchResult extends Record {
/*     */     private final NarratableEntry entry;
/*     */     
/*     */     private final int index;
/*     */     
/*     */     private final NarratableEntry.NarrationPriority priority;
/*     */     
/*     */     public NarratableSearchResult(NarratableEntry entry, int index, NarratableEntry.NarrationPriority priority) {
/* 600 */       this.entry = entry;
/* 600 */       this.index = index;
/* 600 */       this.priority = priority;
/*     */     }
/*     */     
/*     */     public final String toString() {
/*     */       // Byte code:
/*     */       //   0: aload_0
/*     */       //   1: <illegal opcode> toString : (Lnet/minecraft/client/gui/screens/Screen$NarratableSearchResult;)Ljava/lang/String;
/*     */       //   6: areturn
/*     */       // Line number table:
/*     */       //   Java source line number -> byte code offset
/*     */       //   #600	-> 0
/*     */       // Local variable table:
/*     */       //   start	length	slot	name	descriptor
/*     */       //   0	7	0	this	Lnet/minecraft/client/gui/screens/Screen$NarratableSearchResult;
/*     */     }
/*     */     
/*     */     public final int hashCode() {
/*     */       // Byte code:
/*     */       //   0: aload_0
/*     */       //   1: <illegal opcode> hashCode : (Lnet/minecraft/client/gui/screens/Screen$NarratableSearchResult;)I
/*     */       //   6: ireturn
/*     */       // Line number table:
/*     */       //   Java source line number -> byte code offset
/*     */       //   #600	-> 0
/*     */       // Local variable table:
/*     */       //   start	length	slot	name	descriptor
/*     */       //   0	7	0	this	Lnet/minecraft/client/gui/screens/Screen$NarratableSearchResult;
/*     */     }
/*     */     
/*     */     public final boolean equals(Object o) {
/*     */       // Byte code:
/*     */       //   0: aload_0
/*     */       //   1: aload_1
/*     */       //   2: <illegal opcode> equals : (Lnet/minecraft/client/gui/screens/Screen$NarratableSearchResult;Ljava/lang/Object;)Z
/*     */       //   7: ireturn
/*     */       // Line number table:
/*     */       //   Java source line number -> byte code offset
/*     */       //   #600	-> 0
/*     */       // Local variable table:
/*     */       //   start	length	slot	name	descriptor
/*     */       //   0	8	0	this	Lnet/minecraft/client/gui/screens/Screen$NarratableSearchResult;
/*     */       //   0	8	1	o	Ljava/lang/Object;
/*     */     }
/*     */     
/*     */     public NarratableEntry entry() {
/* 600 */       return this.entry;
/*     */     }
/*     */     
/*     */     public int index() {
/* 600 */       return this.index;
/*     */     }
/*     */     
/*     */     public NarratableEntry.NarrationPriority priority() {
/* 600 */       return this.priority;
/*     */     }
/*     */   }
/*     */   
/*     */   public ScreenRectangle getRectangle() {
/* 605 */     return new ScreenRectangle(0, 0, this.width, this.height);
/*     */   }
/*     */   
/*     */   public Music getBackgroundMusic() {
/* 609 */     return null;
/*     */   }
/*     */ }


/* Location:              C:\Users\karol\AppData\Roaming\ModrinthApp\meta\versions\26.2-0.19.5\26.2-0.19.5.jar!\net\minecraft\client\gui\screens\Screen.class
 * Java compiler version: 25 (69.0)
 * JD-Core Version:       1.1.3
 */