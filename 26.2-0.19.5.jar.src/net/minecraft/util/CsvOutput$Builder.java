/*    */ package net.minecraft.util;
/*    */ 
/*    */ import com.google.common.collect.Lists;
/*    */ import java.io.IOException;
/*    */ import java.io.Writer;
/*    */ import java.util.List;
/*    */ 
/*    */ public class Builder {
/* 46 */   private final List<String> headers = Lists.newArrayList();
/*    */   
/*    */   public Builder addColumn(String header) {
/* 49 */     this.headers.add(header);
/* 50 */     return this;
/*    */   }
/*    */   
/*    */   public CsvOutput build(Writer writer) throws IOException {
/* 54 */     return new CsvOutput(writer, this.headers);
/*    */   }
/*    */ }


/* Location:              C:\Users\karol\AppData\Roaming\ModrinthApp\meta\versions\26.2-0.19.5\26.2-0.19.5.jar!\net\minecraf\\util\CsvOutput$Builder.class
 * Java compiler version: 25 (69.0)
 * JD-Core Version:       1.1.3
 */