/*    */ package net.minecraft.client.gui.font;
/*    */ 
/*    */ import com.mojang.serialization.Codec;
/*    */ import java.util.HashMap;
/*    */ import java.util.Map;
/*    */ import java.util.Set;
/*    */ import java.util.function.Function;
/*    */ 
/*    */ public class Filter {
/*    */   private final Map<FontOption, Boolean> values;
/*    */   
/*    */   public static final Codec<Filter> CODEC;
/*    */   
/*    */   static {
/* 31 */     CODEC = Codec.unboundedMap(FontOption.CODEC, (Codec)Codec.BOOL).xmap(Filter::new, p -> p.values);
/*    */   }
/*    */   
/* 36 */   public static final Filter ALWAYS_PASS = new Filter(Map.of());
/*    */   
/*    */   public Filter(Map<FontOption, Boolean> values) {
/* 39 */     this.values = values;
/*    */   }
/*    */   
/*    */   public boolean apply(Set<FontOption> options) {
/* 43 */     for (Map.Entry<FontOption, Boolean> e : this.values.entrySet()) {
/* 44 */       if (options.contains(e.getKey()) != ((Boolean)e.getValue()).booleanValue())
/* 45 */         return false; 
/*    */     } 
/* 48 */     return true;
/*    */   }
/*    */   
/*    */   public Filter merge(Filter other) {
/* 52 */     Map<FontOption, Boolean> options = new HashMap<>(other.values);
/* 53 */     options.putAll(this.values);
/* 54 */     return new Filter(Map.copyOf(options));
/*    */   }
/*    */ }


/* Location:              C:\Users\karol\AppData\Roaming\ModrinthApp\meta\versions\26.2-0.19.5\26.2-0.19.5.jar!\net\minecraft\client\gui\font\FontOption$Filter.class
 * Java compiler version: 25 (69.0)
 * JD-Core Version:       1.1.3
 */