/*    */ package net.minecraft.client.resources.language;
/*    */ 
/*    */ import com.google.common.collect.ImmutableMap;
/*    */ import com.google.common.collect.Maps;
/*    */ import com.mojang.logging.LogUtils;
/*    */ import java.util.ArrayList;
/*    */ import java.util.List;
/*    */ import java.util.Map;
/*    */ import java.util.Objects;
/*    */ import java.util.SortedMap;
/*    */ import java.util.TreeMap;
/*    */ import java.util.function.Consumer;
/*    */ import java.util.stream.Stream;
/*    */ import net.minecraft.client.resources.metadata.language.LanguageMetadataSection;
/*    */ import net.minecraft.locale.Language;
/*    */ import net.minecraft.server.packs.PackResources;
/*    */ import net.minecraft.server.packs.resources.ResourceManager;
/*    */ import net.minecraft.server.packs.resources.ResourceManagerReloadListener;
/*    */ import org.slf4j.Logger;
/*    */ 
/*    */ public class LanguageManager implements ResourceManagerReloadListener {
/* 23 */   private static final Logger LOGGER = LogUtils.getLogger();
/*    */   
/* 24 */   private static final LanguageInfo DEFAULT_LANGUAGE = new LanguageInfo("US", "English", false);
/*    */   
/* 26 */   private Map<String, LanguageInfo> languages = (Map<String, LanguageInfo>)ImmutableMap.of("en_us", DEFAULT_LANGUAGE);
/*    */   
/*    */   private String currentCode;
/*    */   
/*    */   private final Consumer<ClientLanguage> reloadCallback;
/*    */   
/*    */   public LanguageManager(String languageCode, Consumer<ClientLanguage> reloadCallback) {
/* 31 */     this.currentCode = languageCode;
/* 32 */     this.reloadCallback = reloadCallback;
/*    */   }
/*    */   
/*    */   private static Map<String, LanguageInfo> extractLanguages(Stream<PackResources> resourcePacks) {
/* 36 */     Map<String, LanguageInfo> result = Maps.newHashMap();
/* 38 */     resourcePacks.forEach(resourcePack -> {
/*    */           try {
/*    */             LanguageMetadataSection languageMetadataSection = (LanguageMetadataSection)resourcePack.getMetadataSection(LanguageMetadataSection.TYPE);
/*    */             if (languageMetadataSection != null) {
/*    */               Objects.requireNonNull(result);
/*    */               languageMetadataSection.languages().forEach(result::putIfAbsent);
/*    */             } 
/* 45 */           } catch (Exception e) {
/*    */             LOGGER.warn("Unable to parse language metadata section of resourcepack: {}", resourcePack.packId(), e);
/*    */           } 
/*    */         });
/* 50 */     return (Map<String, LanguageInfo>)ImmutableMap.copyOf(result);
/*    */   }
/*    */   
/*    */   public void onResourceManagerReload(ResourceManager resourceManager) {
/* 55 */     this.languages = extractLanguages(resourceManager.listPacks());
/* 56 */     List<String> languageStack = new ArrayList<>(2);
/* 57 */     boolean defaultRightToLeft = DEFAULT_LANGUAGE.bidirectional();
/* 58 */     languageStack.add("en_us");
/* 60 */     if (!this.currentCode.equals("en_us")) {
/* 61 */       LanguageInfo currentLanguage = this.languages.get(this.currentCode);
/* 62 */       if (currentLanguage != null) {
/* 63 */         languageStack.add(this.currentCode);
/* 64 */         defaultRightToLeft = currentLanguage.bidirectional();
/*    */       } 
/*    */     } 
/* 68 */     ClientLanguage locale = ClientLanguage.loadFrom(resourceManager, languageStack, defaultRightToLeft);
/* 71 */     Language.inject(locale);
/* 73 */     this.reloadCallback.accept(locale);
/*    */   }
/*    */   
/*    */   public void setSelected(String code) {
/* 77 */     this.currentCode = code;
/*    */   }
/*    */   
/*    */   public String getSelected() {
/* 81 */     return this.currentCode;
/*    */   }
/*    */   
/*    */   public SortedMap<String, LanguageInfo> getLanguages() {
/* 85 */     return new TreeMap<>(this.languages);
/*    */   }
/*    */   
/*    */   public LanguageInfo getLanguage(String code) {
/* 89 */     return this.languages.get(code);
/*    */   }
/*    */ }


/* Location:              C:\Users\karol\AppData\Roaming\ModrinthApp\meta\versions\26.2-0.19.5\26.2-0.19.5.jar!\net\minecraft\client\resources\language\LanguageManager.class
 * Java compiler version: 25 (69.0)
 * JD-Core Version:       1.1.3
 */