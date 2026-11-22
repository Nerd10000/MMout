/*     */ package net.minecraft.client.gui.screens.inventory;
/*     */ 
/*     */ import java.util.stream.IntStream;
/*     */ import net.minecraft.client.gui.GuiGraphicsExtractor;
/*     */ import net.minecraft.client.gui.components.Button;
/*     */ import net.minecraft.client.gui.components.IMEPreeditOverlay;
/*     */ import net.minecraft.client.gui.components.Renderable;
/*     */ import net.minecraft.client.gui.components.TextCursorUtils;
/*     */ import net.minecraft.client.gui.components.events.GuiEventListener;
/*     */ import net.minecraft.client.gui.font.TextFieldHelper;
/*     */ import net.minecraft.client.gui.screens.Screen;
/*     */ import net.minecraft.client.input.CharacterEvent;
/*     */ import net.minecraft.client.input.KeyEvent;
/*     */ import net.minecraft.client.input.PreeditEvent;
/*     */ import net.minecraft.client.multiplayer.ClientPacketListener;
/*     */ import net.minecraft.client.renderer.blockentity.AbstractSignRenderer;
/*     */ import net.minecraft.network.chat.CommonComponents;
/*     */ import net.minecraft.network.chat.Component;
/*     */ import net.minecraft.network.protocol.Packet;
/*     */ import net.minecraft.network.protocol.game.ServerboundSignUpdatePacket;
/*     */ import net.minecraft.util.ARGB;
/*     */ import net.minecraft.util.Util;
/*     */ import net.minecraft.world.level.block.SignBlock;
/*     */ import net.minecraft.world.level.block.entity.SignBlockEntity;
/*     */ import net.minecraft.world.level.block.entity.SignText;
/*     */ import net.minecraft.world.level.block.state.properties.WoodType;
/*     */ import org.joml.Vector2f;
/*     */ import org.joml.Vector3fc;
/*     */ 
/*     */ public abstract class AbstractSignEditScreen extends Screen {
/*     */   protected final SignBlockEntity sign;
/*     */   
/*     */   private SignText text;
/*     */   
/*     */   private final String[] messages;
/*     */   
/*     */   private final boolean isFrontText;
/*     */   
/*     */   protected final WoodType woodType;
/*     */   
/*     */   private long cursorBlinkStartTime;
/*     */   
/*     */   private int line;
/*     */   
/*     */   private TextFieldHelper signField;
/*     */   
/*     */   private IMEPreeditOverlay preeditOverlay;
/*     */   
/*  40 */   private final Vector2f cursorPosScratch = new Vector2f();
/*     */   
/*     */   public AbstractSignEditScreen(SignBlockEntity sign, boolean isFrontText, boolean shouldFilter) {
/*  43 */     this(sign, isFrontText, shouldFilter, (Component)Component.translatable("sign.edit"));
/*     */   }
/*     */   
/*     */   public AbstractSignEditScreen(SignBlockEntity sign, boolean isFrontText, boolean shouldFilter, Component title) {
/*  47 */     super(title);
/*  49 */     this.sign = sign;
/*  50 */     this.text = sign.getText(isFrontText);
/*  51 */     this.isFrontText = isFrontText;
/*  52 */     this.woodType = SignBlock.getWoodType(sign.getBlockState().getBlock());
/*  54 */     this.messages = (String[])IntStream.range(0, 4).mapToObj(index -> this.text.getMessage(index, shouldFilter)).map(Component::getString).toArray(x$0 -> new String[x$0]);
/*     */   }
/*     */   
/*     */   protected void init() {
/*  59 */     this.minecraft.textInputManager().startTextInput();
/*  60 */     this.cursorBlinkStartTime = Util.getMillis();
/*  61 */     addRenderableWidget((GuiEventListener)Button.builder(CommonComponents.GUI_DONE, button -> onDone()).bounds(this.width / 2 - 100, this.height / 4 + 144, 200, 20).build());
/*  63 */     this
/*     */       
/*  67 */       .signField = new TextFieldHelper(() -> this.messages[this.line], this::setMessage, TextFieldHelper.createClipboardGetter(this.minecraft), TextFieldHelper.createClipboardSetter(this.minecraft), s -> (this.minecraft.font.width(s) <= this.sign.getMaxTextLineWidth()));
/*     */   }
/*     */   
/*     */   public void tick() {
/*  74 */     if (!isValid())
/*  75 */       onDone(); 
/*     */   }
/*     */   
/*     */   private boolean isValid() {
/*  80 */     return (this.minecraft.player != null && 
/*  81 */       !this.sign.isRemoved() && 
/*  82 */       !this.sign.playerIsTooFarAwayToEdit(this.minecraft.player.getUUID()));
/*     */   }
/*     */   
/*     */   public boolean keyPressed(KeyEvent event) {
/*  87 */     if (event.isUp()) {
/*  88 */       this.line = this.line - 1 & 0x3;
/*  89 */       this.signField.setCursorToEnd();
/*  90 */       return true;
/*     */     } 
/*  92 */     if (event.isDown() || event.isConfirmation()) {
/*  93 */       this.line = this.line + 1 & 0x3;
/*  94 */       this.signField.setCursorToEnd();
/*  95 */       return true;
/*     */     } 
/*  97 */     if (this.signField.keyPressed(event))
/*  98 */       return true; 
/* 101 */     return super.keyPressed(event);
/*     */   }
/*     */   
/*     */   public boolean charTyped(CharacterEvent event) {
/* 106 */     this.signField.charTyped(event);
/* 107 */     return true;
/*     */   }
/*     */   
/*     */   public boolean preeditUpdated(PreeditEvent event) {
/* 112 */     this.preeditOverlay = (event != null) ? new IMEPreeditOverlay(event, this.font, this.sign.getTextLineHeight()) : null;
/* 113 */     return true;
/*     */   }
/*     */   
/*     */   public void extractRenderState(GuiGraphicsExtractor graphics, int mouseX, int mouseY, float a) {
/* 118 */     super.extractRenderState(graphics, mouseX, mouseY, a);
/* 120 */     graphics.centeredText(this.font, this.title, this.width / 2, 40, -1);
/* 121 */     extractSign(graphics);
/*     */   }
/*     */   
/*     */   public void onClose() {
/* 126 */     onDone();
/*     */   }
/*     */   
/*     */   public void removed() {
/* 131 */     ClientPacketListener connection = this.minecraft.getConnection();
/* 132 */     if (connection != null)
/* 133 */       connection.send((Packet)new ServerboundSignUpdatePacket(this.sign.getBlockPos(), this.isFrontText, this.messages[0], this.messages[1], this.messages[2], this.messages[3])); 
/* 135 */     this.minecraft.textInputManager().stopTextInput();
/*     */   }
/*     */   
/*     */   public boolean isPauseScreen() {
/* 140 */     return false;
/*     */   }
/*     */   
/*     */   public boolean isInGameUi() {
/* 145 */     return true;
/*     */   }
/*     */   
/*     */   private void extractSign(GuiGraphicsExtractor graphics) {
/* 155 */     graphics.pose().pushMatrix();
/* 156 */     float offsetX = this.width / 2.0F;
/* 157 */     float offsetY = getSignYOffset();
/* 158 */     graphics.pose().translate(offsetX, offsetY);
/* 160 */     graphics.pose().pushMatrix();
/* 161 */     extractSignBackground(graphics);
/* 162 */     graphics.pose().popMatrix();
/* 164 */     Vector3fc textScale = getSignTextScale();
/* 165 */     graphics.pose().scale(textScale.x(), textScale.y());
/* 166 */     this.cursorPosScratch.zero();
/* 167 */     extractSignText(graphics, this.cursorPosScratch);
/* 168 */     graphics.pose().popMatrix();
/* 170 */     if (this.preeditOverlay != null) {
/* 172 */       this.cursorPosScratch.mul(textScale.x(), textScale.y()).add(offsetX, offsetY);
/* 173 */       this.preeditOverlay.updateInputPosition((int)this.cursorPosScratch.x, (int)this.cursorPosScratch.y);
/* 174 */       graphics.setPreeditOverlay((Renderable)this.preeditOverlay);
/*     */     } 
/*     */   }
/*     */   
/*     */   private void extractSignText(GuiGraphicsExtractor graphics, Vector2f cursorPosOutput) {
/* 179 */     int color = this.text.hasGlowingText() ? this.text.getColor().getTextColor() : AbstractSignRenderer.getDarkColor(this.text);
/* 180 */     boolean showCursor = TextCursorUtils.isCursorVisible(Util.getMillis() - this.cursorBlinkStartTime);
/* 181 */     boolean needsValidCursorPos = (this.preeditOverlay != null);
/* 182 */     int cursorPos = this.signField.getCursorPos();
/* 183 */     int selectionPos = this.signField.getSelectionPos();
/* 185 */     int signMidpoint = 4 * this.sign.getTextLineHeight() / 2;
/* 186 */     int cursorY = this.line * this.sign.getTextLineHeight() - signMidpoint;
/*     */     int i;
/* 188 */     for (i = 0; i < this.messages.length; i++) {
/* 189 */       String line = this.messages[i];
/* 190 */       if (line != null) {
/* 193 */         if (this.font.isBidirectional())
/* 194 */           line = this.font.bidirectionalShaping(line); 
/* 197 */         int x1 = -this.font.width(line) / 2;
/* 198 */         graphics.text(this.font, line, x1, i * this.sign.getTextLineHeight() - signMidpoint, color, false);
/* 201 */         if (i == this.line && cursorPos >= 0 && (showCursor || needsValidCursorPos)) {
/* 205 */           int cursorPosition = this.font.width(line.substring(0, Math.max(Math.min(cursorPos, line.length()), 0)));
/* 206 */           int cursorX = cursorPosition - this.font.width(line) / 2;
/* 208 */           if (cursorPos >= line.length()) {
/* 209 */             if (showCursor)
/* 210 */               TextCursorUtils.extractAppendCursor(graphics, this.font, cursorX, cursorY, color, false); 
/* 212 */             cursorPosOutput.set(cursorX, cursorY);
/*     */           } 
/*     */         } 
/*     */       } 
/*     */     } 
/* 216 */     for (i = 0; i < this.messages.length; i++) {
/* 217 */       String line = this.messages[i];
/* 218 */       if (line != null && i == this.line && cursorPos >= 0) {
/* 223 */         int cursorPosition = this.font.width(line.substring(0, Math.max(Math.min(cursorPos, line.length()), 0)));
/* 224 */         int cursorX = cursorPosition - this.font.width(line) / 2;
/* 226 */         if (cursorPos < line.length()) {
/* 227 */           if (showCursor)
/* 228 */             TextCursorUtils.extractInsertCursor(graphics, cursorX, cursorY, ARGB.opaque(color), this.sign.getTextLineHeight()); 
/* 230 */           cursorPosOutput.set(cursorX, cursorY);
/*     */         } 
/* 233 */         if (selectionPos != cursorPos) {
/* 234 */           int startIndex = Math.min(cursorPos, selectionPos);
/* 235 */           int endIndex = Math.max(cursorPos, selectionPos);
/* 236 */           int startPosX = this.font.width(line.substring(0, startIndex)) - this.font.width(line) / 2;
/* 237 */           int endPosX = this.font.width(line.substring(0, endIndex)) - this.font.width(line) / 2;
/* 239 */           int fromX = Math.min(startPosX, endPosX);
/* 240 */           int toX = Math.max(startPosX, endPosX);
/* 242 */           graphics.textHighlight(fromX, cursorY, toX, cursorY + this.sign.getTextLineHeight(), true);
/*     */         } 
/*     */       } 
/*     */     } 
/*     */   }
/*     */   
/*     */   private void setMessage(String message) {
/* 248 */     this.messages[this.line] = message;
/* 249 */     this.text = this.text.setMessage(this.line, (Component)Component.literal(message));
/* 250 */     this.sign.setText(this.text, this.isFrontText);
/*     */   }
/*     */   
/*     */   private void onDone() {
/* 254 */     this.minecraft.gui.setScreen(null);
/*     */   }
/*     */   
/*     */   protected abstract void extractSignBackground(GuiGraphicsExtractor paramGuiGraphicsExtractor);
/*     */   
/*     */   protected abstract Vector3fc getSignTextScale();
/*     */   
/*     */   protected abstract float getSignYOffset();
/*     */ }


/* Location:              C:\Users\karol\AppData\Roaming\ModrinthApp\meta\versions\26.2-0.19.5\26.2-0.19.5.jar!\net\minecraft\client\gui\screens\inventory\AbstractSignEditScreen.class
 * Java compiler version: 25 (69.0)
 * JD-Core Version:       1.1.3
 */