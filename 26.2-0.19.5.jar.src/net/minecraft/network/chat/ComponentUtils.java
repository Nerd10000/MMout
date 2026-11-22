/*     */ package net.minecraft.network.chat;
/*     */ 
/*     */ import com.google.common.collect.Lists;
/*     */ import com.mojang.brigadier.Message;
/*     */ import com.mojang.brigadier.exceptions.CommandSyntaxException;
/*     */ import com.mojang.datafixers.DataFixUtils;
/*     */ import java.util.Collection;
/*     */ import java.util.List;
/*     */ import java.util.Optional;
/*     */ import java.util.function.Function;
/*     */ import javax.annotation.CheckReturnValue;
/*     */ import net.minecraft.ChatFormatting;
/*     */ import net.minecraft.locale.Language;
/*     */ import net.minecraft.network.chat.contents.TranslatableContents;
/*     */ 
/*     */ public class ComponentUtils {
/*     */   public static final String DEFAULT_SEPARATOR_TEXT = ", ";
/*     */   
/*  20 */   public static final Component DEFAULT_SEPARATOR = Component.literal(", ").withStyle(ChatFormatting.GRAY);
/*     */   
/*  21 */   public static final Component DEFAULT_NO_STYLE_SEPARATOR = Component.literal(", ");
/*     */   
/*     */   @CheckReturnValue
/*     */   public static MutableComponent mergeStyles(MutableComponent component, Style style) {
/*  25 */     if (style.isEmpty())
/*  26 */       return component; 
/*  29 */     Style inner = component.getStyle();
/*  30 */     if (inner.isEmpty())
/*  31 */       return component.setStyle(style); 
/*  34 */     if (inner.equals(style))
/*  35 */       return component; 
/*  38 */     return component.setStyle(inner.applyTo(style));
/*     */   }
/*     */   
/*     */   @CheckReturnValue
/*     */   public static Component mergeStyles(Component component, Style style) {
/*  43 */     if (style.isEmpty())
/*  44 */       return component; 
/*  47 */     Style inner = component.getStyle();
/*  48 */     if (inner.isEmpty())
/*  49 */       return component.copy().setStyle(style); 
/*  52 */     if (inner.equals(style))
/*  53 */       return component; 
/*  56 */     return component.copy().setStyle(inner.applyTo(style));
/*     */   }
/*     */   
/*     */   public static Optional<MutableComponent> resolve(ResolutionContext context, Optional<Component> component, int recursionDepth) throws CommandSyntaxException {
/*  60 */     return component.isPresent() ? Optional.<MutableComponent>of(resolve(context, component.get(), recursionDepth)) : Optional.<MutableComponent>empty();
/*     */   }
/*     */   
/*     */   public static MutableComponent resolve(ResolutionContext context, Component component) throws CommandSyntaxException {
/*  64 */     return resolve(context, component, 0);
/*     */   }
/*     */   
/*     */   public static MutableComponent resolve(ResolutionContext context, Component component, int recursionDepth) throws CommandSyntaxException {
/*  68 */     if (recursionDepth > context.depthLimit()) {
/*  69 */       switch (context.depthLimitBehavior()) {
/*     */         default:
/*  69 */           throw new MatchException(null, null);
/*     */         case DISCARD_REMAINING:
/*     */         
/*     */         case STOP_PROCESSING_AND_COPY_REMAINING:
/*     */           break;
/*     */       } 
/*  69 */       return 
/*     */         
/*  71 */         component.copy();
/*     */     } 
/*  75 */     MutableComponent result = component.getContents().resolve(context, recursionDepth + 1);
/*  77 */     for (Component sibling : component.getSiblings())
/*  78 */       result.append(resolve(context, sibling, recursionDepth + 1)); 
/*  81 */     return result.withStyle(resolveStyle(context, component.getStyle(), recursionDepth));
/*     */   }
/*     */   
/*     */   private static Style resolveStyle(ResolutionContext context, Style style, int recursionDepth) throws CommandSyntaxException {
/*  85 */     HoverEvent hoverEvent = style.getHoverEvent();
/*  86 */     if (hoverEvent instanceof HoverEvent.ShowText) {
/*  86 */       HoverEvent.ShowText showText = (HoverEvent.ShowText)hoverEvent;
/*     */       try {
/*  86 */         Component component1 = showText.value(), text = component1;
/*  87 */         HoverEvent resolved = new HoverEvent.ShowText(resolve(context, text, recursionDepth + 1));
/*  88 */         return style.withHoverEvent(resolved);
/*     */       } catch (Throwable throwable) {
/*     */         throw new MatchException(throwable.toString(), throwable);
/*     */       } 
/*     */     } 
/*  91 */     return style;
/*     */   }
/*     */   
/*     */   public static Component formatList(Collection<String> values) {
/*  95 */     return formatAndSortList(values, v -> Component.literal(v).withStyle(ChatFormatting.GREEN));
/*     */   }
/*     */   
/*     */   public static <T extends Comparable<T>> Component formatAndSortList(Collection<T> values, Function<T, Component> formatter) {
/*  99 */     if (values.isEmpty())
/* 100 */       return CommonComponents.EMPTY; 
/* 101 */     if (values.size() == 1)
/* 102 */       return formatter.apply((T)values.iterator().next()); 
/* 105 */     List<T> sorted = Lists.newArrayList(values);
/* 106 */     sorted.sort(Comparable::compareTo);
/* 107 */     return formatList(sorted, formatter);
/*     */   }
/*     */   
/*     */   public static <T> Component formatList(Collection<? extends T> values, Function<T, Component> formatter) {
/* 111 */     return formatList(values, DEFAULT_SEPARATOR, formatter);
/*     */   }
/*     */   
/*     */   public static <T> MutableComponent formatList(Collection<? extends T> values, Optional<? extends Component> separator, Function<T, Component> formatter) {
/* 115 */     return formatList(values, (Component)DataFixUtils.orElse(separator, DEFAULT_SEPARATOR), formatter);
/*     */   }
/*     */   
/*     */   public static Component formatList(Collection<? extends Component> values, Component separator) {
/* 119 */     return formatList(values, separator, Function.identity());
/*     */   }
/*     */   
/*     */   public static <T> MutableComponent formatList(Collection<? extends T> values, Component separator, Function<T, Component> formatter) {
/* 123 */     if (values.isEmpty())
/* 124 */       return Component.empty(); 
/* 125 */     if (values.size() == 1)
/* 126 */       return ((Component)formatter.apply(values.iterator().next())).copy(); 
/* 129 */     MutableComponent result = Component.empty();
/* 130 */     boolean first = true;
/* 131 */     for (T value : values) {
/* 132 */       if (!first)
/* 133 */         result.append(separator); 
/* 135 */       result.append(formatter.apply(value));
/* 136 */       first = false;
/*     */     } 
/* 139 */     return result;
/*     */   }
/*     */   
/*     */   public static MutableComponent wrapInSquareBrackets(Component inner) {
/* 143 */     return Component.translatable("chat.square_brackets", new Object[] { inner });
/*     */   }
/*     */   
/*     */   public static Component fromMessage(Message message) {
/* 147 */     if (message instanceof Component) {
/* 147 */       Component component = (Component)message;
/* 148 */       return component;
/*     */     } 
/* 150 */     return Component.literal(message.getString());
/*     */   }
/*     */   
/*     */   public static boolean isTranslationResolvable(Component component) {
/* 154 */     if (component != null) {
/* 154 */       ComponentContents componentContents = component.getContents();
/* 154 */       if (componentContents instanceof TranslatableContents) {
/* 154 */         TranslatableContents translatable = (TranslatableContents)componentContents;
/* 155 */         String key = translatable.getKey();
/* 156 */         String fallback = translatable.getFallback();
/* 157 */         return (fallback != null || Language.getInstance().has(key));
/*     */       } 
/*     */     } 
/* 159 */     return true;
/*     */   }
/*     */   
/*     */   public static MutableComponent copyOnClickText(String text) {
/* 163 */     return wrapInSquareBrackets(Component.literal(text).withStyle(s -> s.withColor(ChatFormatting.GREEN).withClickEvent(new ClickEvent.CopyToClipboard(text)).withHoverEvent(new HoverEvent.ShowText(Component.translatable("chat.copy.click"))).withInsertion(text)));
/*     */   }
/*     */ }


/* Location:              C:\Users\karol\AppData\Roaming\ModrinthApp\meta\versions\26.2-0.19.5\26.2-0.19.5.jar!\net\minecraft\network\chat\ComponentUtils.class
 * Java compiler version: 25 (69.0)
 * JD-Core Version:       1.1.3
 */