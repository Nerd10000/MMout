/*      */ package net.minecraft.client;
/*      */ 
/*      */ import com.google.common.base.MoreObjects;
/*      */ import com.google.gson.JsonElement;
/*      */ import com.mojang.serialization.DataResult;
/*      */ import com.mojang.serialization.DynamicOps;
/*      */ import com.mojang.serialization.JsonOps;
/*      */ import java.util.Objects;
/*      */ import java.util.function.Function;
/*      */ import net.minecraft.nbt.CompoundTag;
/*      */ import net.minecraft.nbt.StringTag;
/*      */ import net.minecraft.nbt.Tag;
/*      */ import net.minecraft.util.LenientJsonParser;
/*      */ 
/*      */ class null implements Options.FieldAccess {
/*      */   null(Options this$0) {}
/*      */   
/*      */   private String getValue(String name) {
/* 1925 */     Tag tag = options.get(name);
/* 1926 */     if (tag == null)
/* 1927 */       return null; 
/* 1929 */     if (tag instanceof StringTag) {
/* 1929 */       StringTag stringTag = (StringTag)tag;
/*      */       try {
/* 1929 */         String str1 = stringTag.value(), value = str1;
/* 1930 */         return value;
/*      */       } catch (Throwable throwable) {
/*      */         throw new MatchException(throwable.toString(), throwable);
/*      */       } 
/*      */     } 
/* 1932 */     throw new IllegalStateException("Cannot read field of wrong type, expected string: " + String.valueOf(tag));
/*      */   }
/*      */   
/*      */   public <T> void process(String name, OptionInstance<T> option) {
/* 1937 */     String result = getValue(name);
/* 1938 */     if (result != null) {
/* 1941 */       JsonElement element = LenientJsonParser.parse(result.isEmpty() ? "\"\"" : result);
/* 1944 */       Objects.requireNonNull(option);
/* 1944 */       option.codec().parse((DynamicOps)JsonOps.INSTANCE, element).ifError(error -> Options.LOGGER.error("Error parsing option value {} for option {}: {}", new Object[] { result, option, error.message() })).ifSuccess(option::set);
/*      */     } 
/*      */   }
/*      */   
/*      */   public int process(String name, int current) {
/* 1950 */     String result = getValue(name);
/* 1951 */     if (result != null)
/*      */       try {
/* 1953 */         return Integer.parseInt(result);
/* 1954 */       } catch (NumberFormatException e) {
/* 1955 */         Options.LOGGER.warn("Invalid integer value for option {} = {}", new Object[] { name, result, e });
/*      */       }  
/* 1958 */     return current;
/*      */   }
/*      */   
/*      */   public boolean process(String name, boolean current) {
/* 1963 */     String result = getValue(name);
/* 1964 */     return (result != null) ? Options.isTrue(result) : current;
/*      */   }
/*      */   
/*      */   public String process(String name, String current) {
/* 1969 */     return (String)MoreObjects.firstNonNull(getValue(name), current);
/*      */   }
/*      */   
/*      */   public float process(String name, float current) {
/* 1974 */     String result = getValue(name);
/* 1975 */     if (result != null) {
/* 1977 */       if (Options.isTrue(result))
/* 1978 */         return 1.0F; 
/* 1980 */       if (Options.isFalse(result))
/* 1981 */         return 0.0F; 
/*      */       try {
/* 1984 */         return Float.parseFloat(result);
/* 1985 */       } catch (NumberFormatException e) {
/* 1986 */         Options.LOGGER.warn("Invalid floating point value for option {} = {}", new Object[] { name, result, e });
/*      */       } 
/*      */     } 
/* 1989 */     return current;
/*      */   }
/*      */   
/*      */   public <T> T process(String name, T current, Function<String, T> reader, Function<T, String> writer) {
/* 1994 */     String rawResult = getValue(name);
/* 1995 */     return (rawResult == null) ? current : reader.apply(rawResult);
/*      */   }
/*      */ }


/* Location:              C:\Users\karol\AppData\Roaming\ModrinthApp\meta\versions\26.2-0.19.5\26.2-0.19.5.jar!\net\minecraft\client\Options$2.class
 * Java compiler version: 25 (69.0)
 * JD-Core Version:       1.1.3
 */