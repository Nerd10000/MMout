/*     */ package net.minecraft.client.gui.screens.dialog.input;
/*     */ 
/*     */ import java.util.Objects;
/*     */ import java.util.function.Supplier;
/*     */ import net.minecraft.client.gui.Font;
/*     */ import net.minecraft.client.gui.components.EditBox;
/*     */ import net.minecraft.client.gui.components.MultiLineEditBox;
/*     */ import net.minecraft.client.gui.layouts.CommonLayouts;
/*     */ import net.minecraft.client.gui.layouts.LayoutElement;
/*     */ import net.minecraft.client.gui.screens.Screen;
/*     */ import net.minecraft.nbt.StringTag;
/*     */ import net.minecraft.nbt.Tag;
/*     */ import net.minecraft.network.chat.CommonComponents;
/*     */ import net.minecraft.server.dialog.action.Action;
/*     */ import net.minecraft.server.dialog.input.InputControl;
/*     */ import net.minecraft.server.dialog.input.TextInput;
/*     */ 
/*     */ class TextInputHandler implements InputControlHandler<TextInput> {
/*     */   public void addControl(TextInput input, Screen screen, InputControlHandler.Output output) {
/*     */     EditBox editBox;
/*     */     final Supplier<String> getter;
/*  69 */     Font font = screen.getFont();
/*  72 */     if (input.multiline().isPresent()) {
/*  73 */       TextInput.MultilineOptions multiline = input.multiline().get();
/*  74 */       int computedHeight = ((Integer)multiline.height().orElseGet(() -> {
/*     */             int lineCountToFit = ((Integer)multiline.maxLines().orElse(Integer.valueOf(4))).intValue();
/*     */             Objects.requireNonNull(font);
/*     */             return Integer.valueOf(Math.min(9 * lineCountToFit + 8, 512));
/*  74 */           })).intValue();
/*  78 */       MultiLineEditBox multiLineEditBox2 = MultiLineEditBox.builder().build(font, input.width(), computedHeight, CommonComponents.EMPTY);
/*  79 */       multiLineEditBox2.setCharacterLimit(input.maxLength());
/*  80 */       Objects.requireNonNull(multiLineEditBox2);
/*  80 */       multiline.maxLines().ifPresent(multiLineEditBox2::setLineLimit);
/*  81 */       multiLineEditBox2.setValue(input.initial());
/*  82 */       MultiLineEditBox multiLineEditBox1 = multiLineEditBox2;
/*  83 */       Objects.requireNonNull(multiLineEditBox2);
/*  83 */       getter = multiLineEditBox2::getValue;
/*     */     } else {
/*  85 */       EditBox editBox1 = new EditBox(font, input.width(), 20, input.label());
/*  86 */       editBox1.setMaxLength(input.maxLength());
/*  87 */       editBox1.setValue(input.initial());
/*  88 */       editBox = editBox1;
/*  89 */       Objects.requireNonNull(editBox1);
/*  89 */       getter = editBox1::getValue;
/*     */     } 
/*  91 */     LayoutElement wrappedControl = input.labelVisible() ? (LayoutElement)CommonLayouts.labeledElement(font, (LayoutElement)editBox, input.label()) : (LayoutElement)editBox;
/*  92 */     output.accept(wrappedControl, new Action.ValueGetter(this) {
/*     */           public String asTemplateSubstitution() {
/*  95 */             return StringTag.escapeWithoutQuotes(getter.get());
/*     */           }
/*     */           
/*     */           public Tag asTag() {
/* 100 */             return (Tag)StringTag.valueOf(getter.get());
/*     */           }
/*     */         });
/*     */   }
/*     */ }


/* Location:              C:\Users\karol\AppData\Roaming\ModrinthApp\meta\versions\26.2-0.19.5\26.2-0.19.5.jar!\net\minecraft\client\gui\screens\dialog\input\InputControlHandlers$TextInputHandler.class
 * Java compiler version: 25 (69.0)
 * JD-Core Version:       1.1.3
 */