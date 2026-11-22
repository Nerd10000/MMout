/*     */ package net.minecraft.server.packs.repository;
/*     */ 
/*     */ import com.google.gson.JsonParseException;
/*     */ import com.mojang.logging.LogUtils;
/*     */ import java.util.List;
/*     */ import java.util.function.Function;
/*     */ import net.minecraft.SharedConstants;
/*     */ import net.minecraft.network.chat.Component;
/*     */ import net.minecraft.server.packs.FeatureFlagsMetadataSection;
/*     */ import net.minecraft.server.packs.OverlayMetadataSection;
/*     */ import net.minecraft.server.packs.PackLocationInfo;
/*     */ import net.minecraft.server.packs.PackResources;
/*     */ import net.minecraft.server.packs.PackSelectionConfig;
/*     */ import net.minecraft.server.packs.PackType;
/*     */ import net.minecraft.server.packs.metadata.pack.PackFormat;
/*     */ import net.minecraft.server.packs.metadata.pack.PackMetadataSection;
/*     */ import net.minecraft.world.flag.FeatureFlagSet;
/*     */ import org.slf4j.Logger;
/*     */ 
/*     */ public class Pack {
/*  23 */   private static final Logger LOGGER = LogUtils.getLogger();
/*     */   
/*     */   private final PackLocationInfo location;
/*     */   
/*     */   private final ResourcesSupplier resources;
/*     */   
/*     */   private final Metadata metadata;
/*     */   
/*     */   private final PackSelectionConfig selectionConfig;
/*     */   
/*     */   public static interface ResourcesSupplier {
/*     */     PackResources openPrimary(PackLocationInfo param1PackLocationInfo);
/*     */     
/*     */     PackResources openFull(PackLocationInfo param1PackLocationInfo, Pack.Metadata param1Metadata);
/*     */   }
/*     */   
/*     */   public static final class Metadata extends Record {
/*     */     private final Component description;
/*     */     
/*     */     private final PackCompatibility compatibility;
/*     */     
/*     */     private final FeatureFlagSet requestedFeatures;
/*     */     
/*     */     private final List<String> overlays;
/*     */     
/*     */     public Metadata(Component description, PackCompatibility compatibility, FeatureFlagSet requestedFeatures, List<String> overlays) {
/*  36 */       this.description = description;
/*  36 */       this.compatibility = compatibility;
/*  36 */       this.requestedFeatures = requestedFeatures;
/*  36 */       this.overlays = overlays;
/*     */     }
/*     */     
/*     */     public final String toString() {
/*     */       // Byte code:
/*     */       //   0: aload_0
/*     */       //   1: <illegal opcode> toString : (Lnet/minecraft/server/packs/repository/Pack$Metadata;)Ljava/lang/String;
/*     */       //   6: areturn
/*     */       // Line number table:
/*     */       //   Java source line number -> byte code offset
/*     */       //   #36	-> 0
/*     */       // Local variable table:
/*     */       //   start	length	slot	name	descriptor
/*     */       //   0	7	0	this	Lnet/minecraft/server/packs/repository/Pack$Metadata;
/*     */     }
/*     */     
/*     */     public final int hashCode() {
/*     */       // Byte code:
/*     */       //   0: aload_0
/*     */       //   1: <illegal opcode> hashCode : (Lnet/minecraft/server/packs/repository/Pack$Metadata;)I
/*     */       //   6: ireturn
/*     */       // Line number table:
/*     */       //   Java source line number -> byte code offset
/*     */       //   #36	-> 0
/*     */       // Local variable table:
/*     */       //   start	length	slot	name	descriptor
/*     */       //   0	7	0	this	Lnet/minecraft/server/packs/repository/Pack$Metadata;
/*     */     }
/*     */     
/*     */     public final boolean equals(Object o) {
/*     */       // Byte code:
/*     */       //   0: aload_0
/*     */       //   1: aload_1
/*     */       //   2: <illegal opcode> equals : (Lnet/minecraft/server/packs/repository/Pack$Metadata;Ljava/lang/Object;)Z
/*     */       //   7: ireturn
/*     */       // Line number table:
/*     */       //   Java source line number -> byte code offset
/*     */       //   #36	-> 0
/*     */       // Local variable table:
/*     */       //   start	length	slot	name	descriptor
/*     */       //   0	8	0	this	Lnet/minecraft/server/packs/repository/Pack$Metadata;
/*     */       //   0	8	1	o	Ljava/lang/Object;
/*     */     }
/*     */     
/*     */     public Component description() {
/*  36 */       return this.description;
/*     */     }
/*     */     
/*     */     public PackCompatibility compatibility() {
/*  36 */       return this.compatibility;
/*     */     }
/*     */     
/*     */     public FeatureFlagSet requestedFeatures() {
/*  36 */       return this.requestedFeatures;
/*     */     }
/*     */     
/*     */     public List<String> overlays() {
/*  36 */       return this.overlays;
/*     */     }
/*     */   }
/*     */   
/*     */   public static Pack readMetaAndCreate(PackLocationInfo location, ResourcesSupplier resources, PackType packType, PackSelectionConfig selectionConfig) {
/*  40 */     PackFormat currentPackVersion = SharedConstants.getCurrentVersion().packVersion(packType);
/*  41 */     Metadata meta = readPackMetadata(location, resources, currentPackVersion, packType);
/*  42 */     return (meta != null) ? new Pack(location, resources, meta, selectionConfig) : null;
/*     */   }
/*     */   
/*     */   public Pack(PackLocationInfo location, ResourcesSupplier resources, Metadata metadata, PackSelectionConfig selectionConfig) {
/*  46 */     this.location = location;
/*  47 */     this.resources = resources;
/*  48 */     this.metadata = metadata;
/*  49 */     this.selectionConfig = selectionConfig;
/*     */   }
/*     */   
/*     */   public static Metadata readPackMetadata(PackLocationInfo location, ResourcesSupplier resources, PackFormat currentPackVersion, PackType type) {
/*     */     try {
/*  53 */       PackResources pack = resources.openPrimary(location);
/*     */       try {
/*     */         PackMetadataSection meta;
/*     */         try {
/*  56 */           meta = (PackMetadataSection)pack.getMetadataSection(PackMetadataSection.forPackType(type));
/*  57 */         } catch (JsonParseException e) {
/*  59 */           LOGGER.warn("Error reading pack metadata, attempting fallback type", (Throwable)e);
/*  60 */           meta = (PackMetadataSection)pack.getMetadataSection(PackMetadataSection.FALLBACK_TYPE);
/*     */         } 
/*  62 */         if (meta == null) {
/*  63 */           LOGGER.warn("Missing metadata in pack {}", location.id());
/*  64 */           Metadata metadata1 = null;
/*  76 */           if (pack != null)
/*  76 */             pack.close(); 
/*     */           return metadata1;
/*     */         } 
/*     */         FeatureFlagsMetadataSection featureFlagMeta = (FeatureFlagsMetadataSection)pack.getMetadataSection(FeatureFlagsMetadataSection.TYPE);
/*     */         FeatureFlagSet requiredFlags = (featureFlagMeta != null) ? featureFlagMeta.flags() : FeatureFlagSet.of();
/*     */         PackCompatibility packCompatibility = PackCompatibility.forVersion(meta.supportedFormats(), currentPackVersion);
/*     */         OverlayMetadataSection overlays = (OverlayMetadataSection)pack.getMetadataSection(OverlayMetadataSection.forPackType(type));
/*     */         List<String> overlaySet = (overlays != null) ? overlays.overlaysForVersion(currentPackVersion) : List.<String>of();
/*     */         Metadata metadata = new Metadata(meta.description(), packCompatibility, requiredFlags, overlaySet);
/*  76 */         if (pack != null)
/*  76 */           pack.close(); 
/*     */         return metadata;
/*     */       } catch (Throwable throwable) {
/*     */         if (pack != null)
/*     */           try {
/*     */             pack.close();
/*     */           } catch (Throwable throwable1) {
/*     */             throwable.addSuppressed(throwable1);
/*     */           }  
/*     */         throw throwable;
/*     */       } 
/*  76 */     } catch (Exception e) {
/*  77 */       LOGGER.warn("Failed to read pack {} metadata", location.id(), e);
/*  79 */       return null;
/*     */     } 
/*     */   }
/*     */   
/*     */   public PackLocationInfo location() {
/*  83 */     return this.location;
/*     */   }
/*     */   
/*     */   public Component getTitle() {
/*  87 */     return this.location.title();
/*     */   }
/*     */   
/*     */   public Component getDescription() {
/*  91 */     return this.metadata.description();
/*     */   }
/*     */   
/*     */   public Component getChatLink(boolean enabled) {
/*  95 */     return this.location.createChatLink(enabled, this.metadata.description);
/*     */   }
/*     */   
/*     */   public PackCompatibility getCompatibility() {
/*  99 */     return this.metadata.compatibility();
/*     */   }
/*     */   
/*     */   public FeatureFlagSet getRequestedFeatures() {
/* 103 */     return this.metadata.requestedFeatures();
/*     */   }
/*     */   
/*     */   public PackResources open() {
/* 107 */     return this.resources.openFull(this.location, this.metadata);
/*     */   }
/*     */   
/*     */   public String getId() {
/* 111 */     return this.location.id();
/*     */   }
/*     */   
/*     */   public PackSelectionConfig selectionConfig() {
/* 115 */     return this.selectionConfig;
/*     */   }
/*     */   
/*     */   public boolean isRequired() {
/* 119 */     return this.selectionConfig.required();
/*     */   }
/*     */   
/*     */   public boolean isFixedPosition() {
/* 123 */     return this.selectionConfig.fixedPosition();
/*     */   }
/*     */   
/*     */   public Position getDefaultPosition() {
/* 127 */     return this.selectionConfig.defaultPosition();
/*     */   }
/*     */   
/*     */   public PackSource getPackSource() {
/* 131 */     return this.location.source();
/*     */   }
/*     */   
/*     */   public boolean equals(Object o) {
/*     */     Pack that;
/* 136 */     if (this == o)
/* 137 */       return true; 
/* 139 */     if (o instanceof Pack) {
/* 139 */       that = (Pack)o;
/*     */     } else {
/* 140 */       return false;
/*     */     } 
/* 143 */     return this.location.equals(that.location);
/*     */   }
/*     */   
/*     */   public int hashCode() {
/* 148 */     return this.location.hashCode();
/*     */   }
/*     */   
/*     */   public enum Position {
/* 152 */     TOP, BOTTOM;
/*     */     
/*     */     public <T> int insert(List<T> list, T value, Function<T, PackSelectionConfig> converter, boolean reverse) {
/* 157 */       Position self = reverse ? opposite() : this;
/* 158 */       if (self == BOTTOM) {
/* 159 */         int i = 0;
/* 160 */         while (i < list.size()) {
/* 161 */           PackSelectionConfig pack = converter.apply(list.get(i));
/* 162 */           if (pack.fixedPosition() && pack.defaultPosition() == this)
/* 163 */             i++; 
/*     */         } 
/* 168 */         list.add(i, value);
/* 169 */         return i;
/*     */       } 
/* 171 */       int index = list.size() - 1;
/* 172 */       while (index >= 0) {
/* 173 */         PackSelectionConfig pack = converter.apply(list.get(index));
/* 174 */         if (pack.fixedPosition() && pack.defaultPosition() == this)
/* 175 */           index--; 
/*     */       } 
/* 180 */       list.add(index + 1, value);
/* 181 */       return index + 1;
/*     */     }
/*     */     
/*     */     public Position opposite() {
/* 186 */       return (this == TOP) ? BOTTOM : TOP;
/*     */     }
/*     */   }
/*     */ }


/* Location:              C:\Users\karol\AppData\Roaming\ModrinthApp\meta\versions\26.2-0.19.5\26.2-0.19.5.jar!\net\minecraft\server\packs\repository\Pack.class
 * Java compiler version: 25 (69.0)
 * JD-Core Version:       1.1.3
 */