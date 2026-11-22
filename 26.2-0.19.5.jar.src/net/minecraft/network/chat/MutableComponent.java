/*     */ package net.minecraft.network.chat;
/*     */ 
/*     */ import com.google.common.collect.Lists;
/*     */ import java.util.List;
/*     */ import java.util.function.UnaryOperator;
/*     */ import net.minecraft.ChatFormatting;
/*     */ import net.minecraft.locale.Language;
/*     */ import net.minecraft.util.FormattedCharSequence;
/*     */ 
/*     */ public final class MutableComponent implements Component {
/*     */   private final ComponentContents contents;
/*     */   
/*     */   private final List<Component> siblings;
/*     */   
/*     */   private Style style;
/*     */   
/*  18 */   private FormattedCharSequence visualOrderText = FormattedCharSequence.EMPTY;
/*     */   
/*     */   private Language decomposedWith;
/*     */   
/*     */   MutableComponent(ComponentContents contents, List<Component> siblings, Style style) {
/*  23 */     this.contents = contents;
/*  24 */     this.siblings = siblings;
/*  25 */     this.style = style;
/*     */   }
/*     */   
/*     */   public static MutableComponent create(ComponentContents contents) {
/*  29 */     return new MutableComponent(contents, Lists.newArrayList(), Style.EMPTY);
/*     */   }
/*     */   
/*     */   public ComponentContents getContents() {
/*  34 */     return this.contents;
/*     */   }
/*     */   
/*     */   public List<Component> getSiblings() {
/*  39 */     return this.siblings;
/*     */   }
/*     */   
/*     */   public MutableComponent setStyle(Style style) {
/*  43 */     this.style = style;
/*  44 */     return this;
/*     */   }
/*     */   
/*     */   public Style getStyle() {
/*  49 */     return this.style;
/*     */   }
/*     */   
/*     */   public MutableComponent append(String text) {
/*  53 */     if (text.isEmpty())
/*  54 */       return this; 
/*  56 */     return append(Component.literal(text));
/*     */   }
/*     */   
/*     */   public MutableComponent append(Component component) {
/*  60 */     this.siblings.add(component);
/*  61 */     return this;
/*     */   }
/*     */   
/*     */   public MutableComponent withStyle(UnaryOperator<Style> updater) {
/*  65 */     setStyle(updater.apply(getStyle()));
/*  66 */     return this;
/*     */   }
/*     */   
/*     */   public MutableComponent withStyle(Style patch) {
/*  70 */     setStyle(patch.applyTo(getStyle()));
/*  71 */     return this;
/*     */   }
/*     */   
/*     */   public MutableComponent withStyle(ChatFormatting... formats) {
/*  75 */     setStyle(getStyle().applyFormats(formats));
/*  76 */     return this;
/*     */   }
/*     */   
/*     */   public MutableComponent withStyle(ChatFormatting format) {
/*  80 */     setStyle(getStyle().applyFormat(format));
/*  81 */     return this;
/*     */   }
/*     */   
/*     */   public MutableComponent withColor(int color) {
/*  85 */     setStyle(getStyle().withColor(color));
/*  86 */     return this;
/*     */   }
/*     */   
/*     */   public MutableComponent withColor(TextColor color) {
/*  90 */     setStyle(getStyle().withColor(color));
/*  91 */     return this;
/*     */   }
/*     */   
/*     */   public MutableComponent withoutShadow() {
/*  95 */     setStyle(getStyle().withoutShadow());
/*  96 */     return this;
/*     */   }
/*     */   
/*     */   public FormattedCharSequence getVisualOrderText() {
/* 101 */     Language currentLanguage = Language.getInstance();
/* 102 */     if (this.decomposedWith != currentLanguage) {
/* 103 */       this.visualOrderText = currentLanguage.getVisualOrder(this);
/* 104 */       this.decomposedWith = currentLanguage;
/*     */     } 
/* 106 */     return this.visualOrderText;
/*     */   }
/*     */   
/*     */   public boolean equals(Object o) {
/*     */     // Byte code:
/*     */     //   0: aload_0
/*     */     //   1: aload_1
/*     */     //   2: if_acmpne -> 7
/*     */     //   5: iconst_1
/*     */     //   6: ireturn
/*     */     //   7: aload_1
/*     */     //   8: instanceof net/minecraft/network/chat/MutableComponent
/*     */     //   11: ifeq -> 69
/*     */     //   14: aload_1
/*     */     //   15: checkcast net/minecraft/network/chat/MutableComponent
/*     */     //   18: astore_2
/*     */     //   19: aload_0
/*     */     //   20: getfield contents : Lnet/minecraft/network/chat/ComponentContents;
/*     */     //   23: aload_2
/*     */     //   24: getfield contents : Lnet/minecraft/network/chat/ComponentContents;
/*     */     //   27: invokeinterface equals : (Ljava/lang/Object;)Z
/*     */     //   32: ifeq -> 69
/*     */     //   35: aload_0
/*     */     //   36: getfield style : Lnet/minecraft/network/chat/Style;
/*     */     //   39: aload_2
/*     */     //   40: getfield style : Lnet/minecraft/network/chat/Style;
/*     */     //   43: invokevirtual equals : (Ljava/lang/Object;)Z
/*     */     //   46: ifeq -> 69
/*     */     //   49: aload_0
/*     */     //   50: getfield siblings : Ljava/util/List;
/*     */     //   53: aload_2
/*     */     //   54: getfield siblings : Ljava/util/List;
/*     */     //   57: invokeinterface equals : (Ljava/lang/Object;)Z
/*     */     //   62: ifeq -> 69
/*     */     //   65: iconst_1
/*     */     //   66: goto -> 70
/*     */     //   69: iconst_0
/*     */     //   70: ireturn
/*     */     // Line number table:
/*     */     //   Java source line number -> byte code offset
/*     */     //   #111	-> 0
/*     */     //   #112	-> 5
/*     */     //   #118	-> 7
/*     */     //   #115	-> 14
/*     */     //   #116	-> 27
/*     */     //   #117	-> 43
/*     */     //   #118	-> 57
/*     */     //   #115	-> 70
/*     */     // Local variable table:
/*     */     //   start	length	slot	name	descriptor
/*     */     //   19	50	2	that	Lnet/minecraft/network/chat/MutableComponent;
/*     */     //   0	71	0	this	Lnet/minecraft/network/chat/MutableComponent;
/*     */     //   0	71	1	o	Ljava/lang/Object;
/*     */   }
/*     */   
/*     */   public int hashCode() {
/* 123 */     int result = 1;
/* 124 */     result = 31 * result + this.contents.hashCode();
/* 125 */     result = 31 * result + this.style.hashCode();
/* 126 */     result = 31 * result + this.siblings.hashCode();
/* 127 */     return result;
/*     */   }
/*     */   
/*     */   public String toString() {
/* 132 */     StringBuilder result = new StringBuilder(this.contents.toString());
/* 133 */     boolean hasStyle = !this.style.isEmpty();
/* 134 */     boolean hasSiblings = !this.siblings.isEmpty();
/* 135 */     if (hasStyle || hasSiblings) {
/* 136 */       result.append('[');
/* 137 */       if (hasStyle) {
/* 138 */         result.append("style=");
/* 139 */         result.append(this.style);
/*     */       } 
/* 141 */       if (hasStyle && hasSiblings)
/* 142 */         result.append(", "); 
/* 144 */       if (hasSiblings) {
/* 145 */         result.append("siblings=");
/* 146 */         result.append(this.siblings);
/*     */       } 
/* 148 */       result.append(']');
/*     */     } 
/* 150 */     return result.toString();
/*     */   }
/*     */ }


/* Location:              C:\Users\karol\AppData\Roaming\ModrinthApp\meta\versions\26.2-0.19.5\26.2-0.19.5.jar!\net\minecraft\network\chat\MutableComponent.class
 * Java compiler version: 25 (69.0)
 * JD-Core Version:       1.1.3
 */