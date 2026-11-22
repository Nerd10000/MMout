/*     */ package net.minecraft.client.gui.screens.reporting;
/*     */ 
/*     */ import com.mojang.authlib.minecraft.report.AbuseReportLimits;
/*     */ import com.mojang.logging.LogUtils;
/*     */ import java.util.concurrent.CompletableFuture;
/*     */ import java.util.concurrent.Executor;
/*     */ import java.util.function.Consumer;
/*     */ import net.minecraft.ChatFormatting;
/*     */ import net.minecraft.Optionull;
/*     */ import net.minecraft.client.gui.components.AbstractWidget;
/*     */ import net.minecraft.client.gui.components.Button;
/*     */ import net.minecraft.client.gui.components.Checkbox;
/*     */ import net.minecraft.client.gui.components.MultiLineEditBox;
/*     */ import net.minecraft.client.gui.components.StringWidget;
/*     */ import net.minecraft.client.gui.components.Tooltip;
/*     */ import net.minecraft.client.gui.components.events.GuiEventListener;
/*     */ import net.minecraft.client.gui.layouts.FrameLayout;
/*     */ import net.minecraft.client.gui.layouts.Layout;
/*     */ import net.minecraft.client.gui.layouts.LayoutElement;
/*     */ import net.minecraft.client.gui.layouts.LinearLayout;
/*     */ import net.minecraft.client.gui.screens.GenericWaitingScreen;
/*     */ import net.minecraft.client.gui.screens.Screen;
/*     */ import net.minecraft.client.gui.screens.multiplayer.WarningScreen;
/*     */ import net.minecraft.client.multiplayer.chat.report.Report;
/*     */ import net.minecraft.client.multiplayer.chat.report.ReportingContext;
/*     */ import net.minecraft.network.chat.CommonComponents;
/*     */ import net.minecraft.network.chat.Component;
/*     */ import net.minecraft.network.chat.MutableComponent;
/*     */ import net.minecraft.network.chat.ThrowingComponent;
/*     */ import org.slf4j.Logger;
/*     */ 
/*     */ public abstract class AbstractReportScreen<B extends Report.Builder<?>> extends Screen {
/*  33 */   private static final Component REPORT_SENT_MESSAGE = (Component)Component.translatable("gui.abuseReport.report_sent_msg");
/*     */   
/*  34 */   private static final Component REPORT_SENDING_TITLE = (Component)Component.translatable("gui.abuseReport.sending.title").withStyle(ChatFormatting.BOLD);
/*     */   
/*  35 */   private static final Component REPORT_SENT_TITLE = (Component)Component.translatable("gui.abuseReport.sent.title").withStyle(ChatFormatting.BOLD);
/*     */   
/*  36 */   private static final Component REPORT_ERROR_TITLE = (Component)Component.translatable("gui.abuseReport.error.title").withStyle(ChatFormatting.BOLD);
/*     */   
/*  37 */   private static final Component REPORT_SEND_GENERIC_ERROR = (Component)Component.translatable("gui.abuseReport.send.generic_error");
/*     */   
/*  39 */   protected static final Component SEND_REPORT = (Component)Component.translatable("gui.abuseReport.send");
/*     */   
/*  40 */   protected static final Component OBSERVED_WHAT_LABEL = (Component)Component.translatable("gui.abuseReport.observed_what");
/*     */   
/*  41 */   protected static final Component SELECT_REASON = (Component)Component.translatable("gui.abuseReport.select_reason");
/*     */   
/*  42 */   private static final Component DESCRIBE_PLACEHOLDER = (Component)Component.translatable("gui.abuseReport.describe");
/*     */   
/*  43 */   protected static final Component MORE_COMMENTS_LABEL = (Component)Component.translatable("gui.abuseReport.more_comments");
/*     */   
/*  44 */   private static final Component MORE_COMMENTS_NARRATION = (Component)Component.translatable("gui.abuseReport.comments");
/*     */   
/*  45 */   private static final Component ATTESTATION_CHECKBOX = (Component)Component.translatable("gui.abuseReport.attestation").withColor(-2039584);
/*     */   
/*     */   protected static final int BUTTON_WIDTH = 120;
/*     */   
/*     */   protected static final int MARGIN = 20;
/*     */   
/*     */   protected static final int SCREEN_WIDTH = 280;
/*     */   
/*     */   protected static final int SPACING = 8;
/*     */   
/*  52 */   private static final Logger LOGGER = LogUtils.getLogger();
/*     */   
/*     */   protected final Screen lastScreen;
/*     */   
/*     */   protected final ReportingContext reportingContext;
/*     */   
/*  57 */   protected final LinearLayout layout = LinearLayout.vertical().spacing(8);
/*     */   
/*     */   protected B reportBuilder;
/*     */   
/*     */   private Checkbox attestation;
/*     */   
/*     */   protected Button sendButton;
/*     */   
/*     */   protected AbstractReportScreen(Component title, Screen lastScreen, ReportingContext reportingContext, B reportBuilder) {
/*  65 */     super(title);
/*  66 */     this.lastScreen = lastScreen;
/*  67 */     this.reportingContext = reportingContext;
/*  68 */     this.reportBuilder = reportBuilder;
/*     */   }
/*     */   
/*     */   protected MultiLineEditBox createCommentBox(int width, int height, Consumer<String> valueListener) {
/*  72 */     AbuseReportLimits reportLimits = this.reportingContext.sender().reportLimits();
/*  74 */     MultiLineEditBox commentBox = MultiLineEditBox.builder().setPlaceholder(DESCRIBE_PLACEHOLDER).build(this.font, width, height, MORE_COMMENTS_NARRATION);
/*  75 */     commentBox.setValue(this.reportBuilder.comments());
/*  76 */     commentBox.setCharacterLimit(reportLimits.maxOpinionCommentsLength());
/*  77 */     commentBox.setValueListener(valueListener);
/*  79 */     return commentBox;
/*     */   }
/*     */   
/*     */   protected void init() {
/*  84 */     this.layout.defaultCellSetting().alignHorizontallyCenter();
/*  86 */     createHeader();
/*  87 */     addContent();
/*  88 */     createFooter();
/*  90 */     onReportChanged();
/*  92 */     AbstractReportScreen<B> abstractReportScreen = this;
/*  92 */     this.layout.visitWidgets(x$0 -> rec$.addRenderableWidget((GuiEventListener)x$0));
/*  93 */     repositionElements();
/*     */   }
/*     */   
/*     */   protected void createHeader() {
/*  97 */     this.layout.addChild((LayoutElement)new StringWidget(this.title, this.font));
/*     */   }
/*     */   
/*     */   protected void createFooter() {
/* 103 */     this.attestation = (Checkbox)this.layout.addChild((LayoutElement)Checkbox.builder(ATTESTATION_CHECKBOX, this.font)
/* 104 */         .selected(this.reportBuilder.attested())
/* 105 */         .maxWidth(280)
/* 106 */         .tooltip(Tooltip.create(ATTESTATION_CHECKBOX))
/* 107 */         .onValueChange((checkbox, value) -> {
/*     */             this.reportBuilder.setAttested(value);
/*     */             onReportChanged();
/* 111 */           }).build());
/* 113 */     LinearLayout buttonsLayout = (LinearLayout)this.layout.addChild((LayoutElement)LinearLayout.horizontal().spacing(8));
/* 114 */     buttonsLayout.addChild((LayoutElement)Button.builder(CommonComponents.GUI_BACK, b -> onClose()).width(120).build());
/* 115 */     this.sendButton = (Button)buttonsLayout.addChild((LayoutElement)Button.builder(SEND_REPORT, b -> sendReport()).width(120).build());
/*     */   }
/*     */   
/*     */   protected void onReportChanged() {
/* 119 */     Report.CannotBuildReason cannotBuildReason = this.reportBuilder.checkBuildable();
/* 120 */     this.sendButton.active = (cannotBuildReason == null && this.attestation.selected());
/* 121 */     this.sendButton.setTooltip((Tooltip)Optionull.map(cannotBuildReason, Report.CannotBuildReason::tooltip));
/*     */   }
/*     */   
/*     */   protected void repositionElements() {
/* 126 */     this.layout.arrangeElements();
/* 127 */     FrameLayout.centerInRectangle((LayoutElement)this.layout, getRectangle());
/*     */   }
/*     */   
/*     */   protected void sendReport() {
/* 131 */     this.reportBuilder.build(this.reportingContext).ifLeft(result -> {
/*     */           CompletableFuture<?> sendFuture = this.reportingContext.sender().send(result.id(), result.reportType(), result.report());
/*     */           this.minecraft.gui.setScreen((Screen)GenericWaitingScreen.createWaiting(REPORT_SENDING_TITLE, CommonComponents.GUI_CANCEL, ()));
/*     */           sendFuture.handleAsync((), (Executor)this.minecraft);
/* 149 */         }).ifRight(reason -> displayReportSendError(reason.message()));
/*     */   }
/*     */   
/*     */   private void onReportSendSuccess() {
/* 153 */     clearDraft();
/* 154 */     this.minecraft.gui.setScreen((Screen)GenericWaitingScreen.createCompleted(REPORT_SENT_TITLE, REPORT_SENT_MESSAGE, CommonComponents.GUI_DONE, () -> this.minecraft.gui.setScreen(null)));
/*     */   }
/*     */   
/*     */   private void onReportSendError(Throwable throwable) {
/*     */     Component message;
/* 160 */     LOGGER.error("Encountered error while sending abuse report", throwable);
/* 163 */     Throwable throwable1 = throwable.getCause();
/* 163 */     if (throwable1 instanceof ThrowingComponent) {
/* 163 */       ThrowingComponent error = (ThrowingComponent)throwable1;
/* 164 */       message = error.getComponent();
/*     */     } else {
/* 166 */       message = REPORT_SEND_GENERIC_ERROR;
/*     */     } 
/* 169 */     displayReportSendError(message);
/*     */   }
/*     */   
/*     */   private void displayReportSendError(Component message) {
/* 173 */     MutableComponent mutableComponent = message.copy().withStyle(ChatFormatting.RED);
/* 175 */     this.minecraft.gui.setScreen((Screen)GenericWaitingScreen.createCompleted(REPORT_ERROR_TITLE, (Component)mutableComponent, CommonComponents.GUI_BACK, () -> this.minecraft.gui.setScreen(this)));
/*     */   }
/*     */   
/*     */   private void saveDraft() {
/* 181 */     if (this.reportBuilder.hasContent())
/* 182 */       this.reportingContext.setReportDraft(this.reportBuilder.report().copy()); 
/*     */   }
/*     */   
/*     */   private void clearDraft() {
/* 187 */     this.reportingContext.setReportDraft(null);
/*     */   }
/*     */   
/*     */   public void onClose() {
/* 192 */     if (this.reportBuilder.hasContent()) {
/* 193 */       this.minecraft.gui.setScreen((Screen)new DiscardReportWarningScreen());
/*     */     } else {
/* 195 */       this.minecraft.gui.setScreen(this.lastScreen);
/*     */     } 
/*     */   }
/*     */   
/*     */   public void removed() {
/* 201 */     saveDraft();
/* 202 */     super.removed();
/*     */   }
/*     */   
/*     */   protected abstract void addContent();
/*     */   
/*     */   private class DiscardReportWarningScreen extends WarningScreen {
/* 206 */     private static final Component TITLE = (Component)Component.translatable("gui.abuseReport.discard.title").withStyle(ChatFormatting.BOLD);
/*     */     
/* 207 */     private static final Component MESSAGE = (Component)Component.translatable("gui.abuseReport.discard.content");
/*     */     
/* 208 */     private static final Component RETURN = (Component)Component.translatable("gui.abuseReport.discard.return");
/*     */     
/* 209 */     private static final Component DRAFT = (Component)Component.translatable("gui.abuseReport.discard.draft");
/*     */     
/* 210 */     private static final Component DISCARD = (Component)Component.translatable("gui.abuseReport.discard.discard");
/*     */     
/*     */     protected DiscardReportWarningScreen() {
/* 213 */       super(TITLE, MESSAGE, MESSAGE);
/*     */     }
/*     */     
/*     */     protected Layout addFooterButtons() {
/* 218 */       LinearLayout footer = LinearLayout.vertical().spacing(8);
/* 219 */       footer.defaultCellSetting().alignHorizontallyCenter();
/* 220 */       LinearLayout firstFooterRow = (LinearLayout)footer.addChild((LayoutElement)LinearLayout.horizontal().spacing(8));
/* 221 */       firstFooterRow.addChild(
/* 222 */           (LayoutElement)Button.builder(RETURN, button -> onClose())
/* 223 */           .build());
/* 225 */       firstFooterRow.addChild(
/* 226 */           (LayoutElement)Button.builder(DRAFT, button -> {
/*     */               AbstractReportScreen.this.saveDraft();
/*     */               this.minecraft.gui.setScreen(AbstractReportScreen.this.lastScreen);
/* 230 */             }).build());
/* 232 */       footer.addChild(
/* 233 */           (LayoutElement)Button.builder(DISCARD, button -> {
/*     */               AbstractReportScreen.this.clearDraft();
/*     */               this.minecraft.gui.setScreen(AbstractReportScreen.this.lastScreen);
/* 237 */             }).build());
/* 239 */       return (Layout)footer;
/*     */     }
/*     */     
/*     */     public void onClose() {
/* 244 */       this.minecraft.gui.setScreen(AbstractReportScreen.this);
/*     */     }
/*     */     
/*     */     public boolean shouldCloseOnEsc() {
/* 249 */       return false;
/*     */     }
/*     */   }
/*     */ }


/* Location:              C:\Users\karol\AppData\Roaming\ModrinthApp\meta\versions\26.2-0.19.5\26.2-0.19.5.jar!\net\minecraft\client\gui\screens\reporting\AbstractReportScreen.class
 * Java compiler version: 25 (69.0)
 * JD-Core Version:       1.1.3
 */