/*      */ package net.minecraft.client;
/*      */ 
/*      */ import com.google.gson.JsonElement;
/*      */ import com.mojang.serialization.DataResult;
/*      */ import com.mojang.serialization.DynamicOps;
/*      */ import com.mojang.serialization.JsonOps;
/*      */ import java.io.PrintWriter;
/*      */ import java.util.function.Function;
/*      */ 
/*      */ class null implements Options.FieldAccess {
/*      */   null(Options this$0) {}
/*      */   
/*      */   public void writePrefix(String name) {
/* 2034 */     writer.print(name);
/* 2035 */     writer.print(':');
/*      */   }
/*      */   
/*      */   public <T> void process(String name, OptionInstance<T> option) {
/* 2040 */     option.codec().encodeStart((DynamicOps)JsonOps.INSTANCE, option.get())
/* 2041 */       .ifError(error -> Options.LOGGER.error("Error saving option {}: {}", option, error.message()))
/* 2042 */       .ifSuccess(element -> {
/*      */           writePrefix(name);
/*      */           writer.println(Options.GSON.toJson(element));
/*      */         });
/*      */   }
/*      */   
/*      */   public int process(String name, int value) {
/* 2050 */     writePrefix(name);
/* 2051 */     writer.println(value);
/* 2052 */     return value;
/*      */   }
/*      */   
/*      */   public boolean process(String name, boolean value) {
/* 2057 */     writePrefix(name);
/* 2058 */     writer.println(value);
/* 2059 */     return value;
/*      */   }
/*      */   
/*      */   public String process(String name, String value) {
/* 2064 */     writePrefix(name);
/* 2065 */     writer.println(value);
/* 2066 */     return value;
/*      */   }
/*      */   
/*      */   public float process(String name, float value) {
/* 2071 */     writePrefix(name);
/* 2072 */     writer.println(value);
/* 2073 */     return value;
/*      */   }
/*      */   
/*      */   public <T> T process(String name, T value, Function<String, T> reader, Function<T, String> converter) {
/* 2078 */     writePrefix(name);
/* 2079 */     writer.println(converter.apply(value));
/* 2080 */     return value;
/*      */   }
/*      */ }


/* Location:              C:\Users\karol\AppData\Roaming\ModrinthApp\meta\versions\26.2-0.19.5\26.2-0.19.5.jar!\net\minecraft\client\Options$3.class
 * Java compiler version: 25 (69.0)
 * JD-Core Version:       1.1.3
 */