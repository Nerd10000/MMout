/*     */ package net.minecraft.locale;
/*     */ 
/*     */ import com.google.common.collect.ImmutableList;
/*     */ import com.google.gson.Gson;
/*     */ import com.google.gson.JsonElement;
/*     */ import com.google.gson.JsonObject;
/*     */ import com.mojang.logging.LogUtils;
/*     */ import java.io.IOException;
/*     */ import java.io.InputStream;
/*     */ import java.io.InputStreamReader;
/*     */ import java.nio.charset.StandardCharsets;
/*     */ import java.util.HashMap;
/*     */ import java.util.List;
/*     */ import java.util.Map;
/*     */ import java.util.Objects;
/*     */ import java.util.Optional;
/*     */ import java.util.function.BiConsumer;
/*     */ import java.util.regex.Pattern;
/*     */ import net.minecraft.network.chat.FormattedText;
/*     */ import net.minecraft.network.chat.Style;
/*     */ import net.minecraft.util.FormattedCharSequence;
/*     */ import net.minecraft.util.FormattedCharSink;
/*     */ import net.minecraft.util.GsonHelper;
/*     */ import net.minecraft.util.StringDecomposer;
/*     */ import org.slf4j.Logger;
/*     */ 
/*     */ public abstract class Language {
/*  29 */   private static final Logger LOGGER = LogUtils.getLogger();
/*     */   
/*  30 */   private static final Gson GSON = new Gson();
/*     */   
/*  33 */   private static final Pattern UNSUPPORTED_FORMAT_PATTERN = Pattern.compile("%(\\d+\\$)?[\\d.]*[df]");
/*     */   
/*     */   public static final String DEFAULT = "en_us";
/*     */   
/*  35 */   public static final Language DEFAULT_INSTANCE = loadDefault();
/*     */   
/*  36 */   private static volatile Language instance = DEFAULT_INSTANCE;
/*     */   
/*     */   private static Language loadDefault() {
/*  39 */     DeprecatedTranslationsInfo deprecatedInfo = DeprecatedTranslationsInfo.loadFromDefaultResource();
/*  41 */     Map<String, String> loadedData = new HashMap<>();
/*  42 */     Objects.requireNonNull(loadedData);
/*  42 */     BiConsumer<String, String> output = loadedData::put;
/*  43 */     parseTranslations(output, "/assets/minecraft/lang/en_us.json");
/*  45 */     deprecatedInfo.applyToMap(loadedData);
/*  47 */     final Map<String, String> storage = Map.copyOf(loadedData);
/*  48 */     return new Language() {
/*     */         public String getOrDefault(String elementId, String defaultValue) {
/*  51 */           return (String)storage.getOrDefault(elementId, defaultValue);
/*     */         }
/*     */         
/*     */         public boolean has(String elementId) {
/*  56 */           return storage.containsKey(elementId);
/*     */         }
/*     */         
/*     */         public boolean isDefaultRightToLeft() {
/*  61 */           return false;
/*     */         }
/*     */         
/*     */         public FormattedCharSequence getVisualOrder(FormattedText logicalOrderText) {
/*  67 */           return output -> logicalOrderText.visit((), Style.EMPTY).isPresent();
/*     */         }
/*     */       };
/*     */   }
/*     */   
/*     */   private static void parseTranslations(BiConsumer<String, String> output, String path) {
/*     */     try {
/*  75 */       InputStream stream = Language.class.getResourceAsStream(path);
/*     */       try {
/*  76 */         loadFromJson(stream, output);
/*  77 */         if (stream != null)
/*  77 */           stream.close(); 
/*     */       } catch (Throwable throwable) {
/*     */         if (stream != null)
/*     */           try {
/*     */             stream.close();
/*     */           } catch (Throwable throwable1) {
/*     */             throwable.addSuppressed(throwable1);
/*     */           }  
/*     */         throw throwable;
/*     */       } 
/*  77 */     } catch (IOException|com.google.gson.JsonParseException e) {
/*  78 */       LOGGER.error("Couldn't read strings from {}", path, e);
/*     */     } 
/*     */   }
/*     */   
/*     */   public static void loadFromJson(InputStream stream, BiConsumer<String, String> output) {
/*  83 */     JsonObject entries = (JsonObject)GSON.fromJson(new InputStreamReader(stream, StandardCharsets.UTF_8), JsonObject.class);
/*  84 */     for (Map.Entry<String, JsonElement> entry : (Iterable<Map.Entry<String, JsonElement>>)entries.entrySet()) {
/*  85 */       String text = UNSUPPORTED_FORMAT_PATTERN.matcher(GsonHelper.convertToString(entry.getValue(), entry.getKey())).replaceAll("%$1s");
/*  86 */       output.accept(entry.getKey(), text);
/*     */     } 
/*     */   }
/*     */   
/*     */   public static Language getInstance() {
/*  91 */     return instance;
/*     */   }
/*     */   
/*     */   public static void inject(Language language) {
/*  95 */     instance = language;
/*     */   }
/*     */   
/*     */   public String getOrDefault(String elementId) {
/*  99 */     return getOrDefault(elementId, elementId);
/*     */   }
/*     */   
/*     */   public abstract String getOrDefault(String paramString1, String paramString2);
/*     */   
/*     */   public abstract boolean has(String paramString);
/*     */   
/*     */   public abstract boolean isDefaultRightToLeft();
/*     */   
/*     */   public abstract FormattedCharSequence getVisualOrder(FormattedText paramFormattedText);
/*     */   
/*     */   public List<FormattedCharSequence> getVisualOrder(List<FormattedText> lines) {
/* 111 */     return (List<FormattedCharSequence>)lines.stream().map(this::getVisualOrder).collect(ImmutableList.toImmutableList());
/*     */   }
/*     */ }


/* Location:              C:\Users\karol\AppData\Roaming\ModrinthApp\meta\versions\26.2-0.19.5\26.2-0.19.5.jar!\net\minecraft\locale\Language.class
 * Java compiler version: 25 (69.0)
 * JD-Core Version:       1.1.3
 */