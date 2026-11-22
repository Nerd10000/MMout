/*    */ package net.minecraft.client.gui.components;
/*    */ 
/*    */ import com.google.common.collect.Lists;
/*    */ import java.util.List;
/*    */ import java.util.Optional;
/*    */ import net.minecraft.ChatFormatting;
/*    */ import net.minecraft.client.ComponentCollector;
/*    */ import net.minecraft.client.Minecraft;
/*    */ import net.minecraft.client.gui.Font;
/*    */ import net.minecraft.locale.Language;
/*    */ import net.minecraft.network.chat.CommonComponents;
/*    */ import net.minecraft.network.chat.Component;
/*    */ import net.minecraft.network.chat.FormattedText;
/*    */ import net.minecraft.network.chat.Style;
/*    */ import net.minecraft.util.FormattedCharSequence;
/*    */ 
/*    */ public class ComponentRenderUtils {
/* 19 */   private static final FormattedCharSequence INDENT = FormattedCharSequence.codepoint(32, Style.EMPTY);
/*    */   
/*    */   private static String stripColor(String input) {
/* 22 */     return ((Boolean)(Minecraft.getInstance()).options.chatColors().get()).booleanValue() ? input : ChatFormatting.stripFormatting(input);
/*    */   }
/*    */   
/*    */   public static List<FormattedCharSequence> wrapComponents(FormattedText message, int maxWidth, Font font) {
/* 26 */     ComponentCollector collector = new ComponentCollector();
/* 27 */     message.visit((style, contents) -> {
/*    */           collector.append(FormattedText.of(stripColor(contents), style));
/*    */           return Optional.empty();
/* 27 */         }Style.EMPTY);
/* 32 */     List<FormattedCharSequence> result = Lists.newArrayList();
/* 34 */     font.getSplitter().splitLines(collector.getResultOrEmpty(), maxWidth, Style.EMPTY, (text, wrapped) -> {
/*    */           FormattedCharSequence reorderedText = Language.getInstance().getVisualOrder(text);
/*    */           result.add(wrapped.booleanValue() ? FormattedCharSequence.composite(INDENT, reorderedText) : reorderedText);
/*    */         });
/* 40 */     if (result.isEmpty())
/* 42 */       return Lists.newArrayList((Object[])new FormattedCharSequence[] { FormattedCharSequence.EMPTY }); 
/* 44 */     return result;
/*    */   }
/*    */   
/*    */   public static FormattedCharSequence clipText(Component text, Font font, int width) {
/* 48 */     FormattedText clippedText = font.substrByWidth((FormattedText)text, width - font.width((FormattedText)CommonComponents.ELLIPSIS));
/* 49 */     return Language.getInstance().getVisualOrder(FormattedText.composite(new FormattedText[] { clippedText, (FormattedText)CommonComponents.ELLIPSIS }));
/*    */   }
/*    */ }


/* Location:              C:\Users\karol\AppData\Roaming\ModrinthApp\meta\versions\26.2-0.19.5\26.2-0.19.5.jar!\net\minecraft\client\gui\components\ComponentRenderUtils.class
 * Java compiler version: 25 (69.0)
 * JD-Core Version:       1.1.3
 */