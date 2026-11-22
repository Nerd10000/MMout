/*     */ package net.minecraft.nbt;
/*     */ 
/*     */ import java.util.HashMap;
/*     */ import java.util.Map;
/*     */ import java.util.Objects;
/*     */ import net.minecraft.network.chat.Component;
/*     */ import net.minecraft.network.chat.Style;
/*     */ 
/*     */ public class PlainStyling implements TextComponentTagVisitor.Styling {
/* 388 */   public static final TextComponentTagVisitor.Styling INSTANCE = new PlainStyling();
/*     */   
/* 390 */   private final Map<TextComponentTagVisitor.Token, Component> tokens = new HashMap<>();
/*     */   
/*     */   private PlainStyling() {
/* 393 */     for (TextComponentTagVisitor.Token value : TextComponentTagVisitor.Token.values())
/* 394 */       this.tokens.put(value, Component.literal(value.text)); 
/*     */   }
/*     */   
/*     */   public Style keyStyle() {
/* 400 */     return Style.EMPTY;
/*     */   }
/*     */   
/*     */   public Style stringStyle() {
/* 405 */     return Style.EMPTY;
/*     */   }
/*     */   
/*     */   public Style numberStyle() {
/* 410 */     return Style.EMPTY;
/*     */   }
/*     */   
/*     */   public Component token(TextComponentTagVisitor.Token token) {
/* 415 */     return Objects.<Component>requireNonNull(this.tokens.get(token));
/*     */   }
/*     */ }


/* Location:              C:\Users\karol\AppData\Roaming\ModrinthApp\meta\versions\26.2-0.19.5\26.2-0.19.5.jar!\net\minecraft\nbt\TextComponentTagVisitor$PlainStyling.class
 * Java compiler version: 25 (69.0)
 * JD-Core Version:       1.1.3
 */