/*    */ package net.minecraft.server.dialog.action;
/*    */ 
/*    */ import com.google.common.collect.Maps;
/*    */ import java.util.Map;
/*    */ import java.util.function.Supplier;
/*    */ import net.minecraft.nbt.StringTag;
/*    */ import net.minecraft.nbt.Tag;
/*    */ 
/*    */ public interface ValueGetter {
/*    */   String asTemplateSubstitution();
/*    */   
/*    */   Tag asTag();
/*    */   
/*    */   static Map<String, String> getAsTemplateSubstitutions(Map<String, ValueGetter> parameters) {
/* 30 */     return Maps.transformValues(parameters, ValueGetter::asTemplateSubstitution);
/*    */   }
/*    */   
/*    */   static ValueGetter of(final String value) {
/* 34 */     return new ValueGetter() {
/*    */         public String asTemplateSubstitution() {
/* 37 */           return value;
/*    */         }
/*    */         
/*    */         public Tag asTag() {
/* 42 */           return (Tag)StringTag.valueOf(value);
/*    */         }
/*    */       };
/*    */   }
/*    */   
/*    */   static ValueGetter of(final Supplier<String> value) {
/* 48 */     return new ValueGetter() {
/*    */         public String asTemplateSubstitution() {
/* 51 */           return value.get();
/*    */         }
/*    */         
/*    */         public Tag asTag() {
/* 56 */           return (Tag)StringTag.valueOf(value.get());
/*    */         }
/*    */       };
/*    */   }
/*    */ }


/* Location:              C:\Users\karol\AppData\Roaming\ModrinthApp\meta\versions\26.2-0.19.5\26.2-0.19.5.jar!\net\minecraft\server\dialog\action\Action$ValueGetter.class
 * Java compiler version: 25 (69.0)
 * JD-Core Version:       1.1.3
 */