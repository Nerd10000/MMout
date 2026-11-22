/*     */ package net.minecraft.client.gui.screens.dialog.input;
/*     */ 
/*     */ import java.util.function.Supplier;
/*     */ import net.minecraft.nbt.StringTag;
/*     */ import net.minecraft.nbt.Tag;
/*     */ import net.minecraft.server.dialog.action.Action;
/*     */ 
/*     */ class null implements Action.ValueGetter {
/*     */   null(InputControlHandlers.TextInputHandler this$0) {}
/*     */   
/*     */   public String asTemplateSubstitution() {
/*  95 */     return StringTag.escapeWithoutQuotes(getter.get());
/*     */   }
/*     */   
/*     */   public Tag asTag() {
/* 100 */     return (Tag)StringTag.valueOf(getter.get());
/*     */   }
/*     */ }


/* Location:              C:\Users\karol\AppData\Roaming\ModrinthApp\meta\versions\26.2-0.19.5\26.2-0.19.5.jar!\net\minecraft\client\gui\screens\dialog\input\InputControlHandlers$TextInputHandler$1.class
 * Java compiler version: 25 (69.0)
 * JD-Core Version:       1.1.3
 */