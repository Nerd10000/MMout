/*    */ package net.minecraft.server.packs;
/*    */ 
/*    */ import com.google.common.annotations.VisibleForTesting;
/*    */ import com.mojang.datafixers.kinds.App;
/*    */ import com.mojang.datafixers.kinds.Applicative;
/*    */ import com.mojang.serialization.Codec;
/*    */ import com.mojang.serialization.DataResult;
/*    */ import com.mojang.serialization.codecs.RecordCodecBuilder;
/*    */ import java.util.List;
/*    */ import java.util.function.BiFunction;
/*    */ import java.util.function.Function;
/*    */ import java.util.regex.Pattern;
/*    */ import net.minecraft.server.packs.metadata.MetadataSectionType;
/*    */ import net.minecraft.server.packs.metadata.pack.PackFormat;
/*    */ import net.minecraft.util.InclusiveRange;
/*    */ 
/*    */ public final class OverlayMetadataSection extends Record {
/*    */   private final List<OverlayEntry> overlays;
/*    */   
/*    */   public OverlayMetadataSection(List<OverlayEntry> overlays) {
/* 14 */     this.overlays = overlays;
/*    */   }
/*    */   
/*    */   public final String toString() {
/*    */     // Byte code:
/*    */     //   0: aload_0
/*    */     //   1: <illegal opcode> toString : (Lnet/minecraft/server/packs/OverlayMetadataSection;)Ljava/lang/String;
/*    */     //   6: areturn
/*    */     // Line number table:
/*    */     //   Java source line number -> byte code offset
/*    */     //   #14	-> 0
/*    */     // Local variable table:
/*    */     //   start	length	slot	name	descriptor
/*    */     //   0	7	0	this	Lnet/minecraft/server/packs/OverlayMetadataSection;
/*    */   }
/*    */   
/*    */   public final int hashCode() {
/*    */     // Byte code:
/*    */     //   0: aload_0
/*    */     //   1: <illegal opcode> hashCode : (Lnet/minecraft/server/packs/OverlayMetadataSection;)I
/*    */     //   6: ireturn
/*    */     // Line number table:
/*    */     //   Java source line number -> byte code offset
/*    */     //   #14	-> 0
/*    */     // Local variable table:
/*    */     //   start	length	slot	name	descriptor
/*    */     //   0	7	0	this	Lnet/minecraft/server/packs/OverlayMetadataSection;
/*    */   }
/*    */   
/*    */   public final boolean equals(Object o) {
/*    */     // Byte code:
/*    */     //   0: aload_0
/*    */     //   1: aload_1
/*    */     //   2: <illegal opcode> equals : (Lnet/minecraft/server/packs/OverlayMetadataSection;Ljava/lang/Object;)Z
/*    */     //   7: ireturn
/*    */     // Line number table:
/*    */     //   Java source line number -> byte code offset
/*    */     //   #14	-> 0
/*    */     // Local variable table:
/*    */     //   start	length	slot	name	descriptor
/*    */     //   0	8	0	this	Lnet/minecraft/server/packs/OverlayMetadataSection;
/*    */     //   0	8	1	o	Ljava/lang/Object;
/*    */   }
/*    */   
/*    */   public List<OverlayEntry> overlays() {
/* 14 */     return this.overlays;
/*    */   }
/*    */   
/* 15 */   private static final Pattern DIR_VALIDATOR = Pattern.compile("[-_a-zA-Z0-9.]+");
/*    */   
/*    */   public static final class OverlayEntry extends Record {
/*    */     private final InclusiveRange<PackFormat> format;
/*    */     
/*    */     private final String overlay;
/*    */     
/*    */     public OverlayEntry(InclusiveRange<PackFormat> format, String overlay) {
/* 17 */       this.format = format;
/* 17 */       this.overlay = overlay;
/*    */     }
/*    */     
/*    */     public final String toString() {
/*    */       // Byte code:
/*    */       //   0: aload_0
/*    */       //   1: <illegal opcode> toString : (Lnet/minecraft/server/packs/OverlayMetadataSection$OverlayEntry;)Ljava/lang/String;
/*    */       //   6: areturn
/*    */       // Line number table:
/*    */       //   Java source line number -> byte code offset
/*    */       //   #17	-> 0
/*    */       // Local variable table:
/*    */       //   start	length	slot	name	descriptor
/*    */       //   0	7	0	this	Lnet/minecraft/server/packs/OverlayMetadataSection$OverlayEntry;
/*    */     }
/*    */     
/*    */     public final int hashCode() {
/*    */       // Byte code:
/*    */       //   0: aload_0
/*    */       //   1: <illegal opcode> hashCode : (Lnet/minecraft/server/packs/OverlayMetadataSection$OverlayEntry;)I
/*    */       //   6: ireturn
/*    */       // Line number table:
/*    */       //   Java source line number -> byte code offset
/*    */       //   #17	-> 0
/*    */       // Local variable table:
/*    */       //   start	length	slot	name	descriptor
/*    */       //   0	7	0	this	Lnet/minecraft/server/packs/OverlayMetadataSection$OverlayEntry;
/*    */     }
/*    */     
/*    */     public final boolean equals(Object o) {
/*    */       // Byte code:
/*    */       //   0: aload_0
/*    */       //   1: aload_1
/*    */       //   2: <illegal opcode> equals : (Lnet/minecraft/server/packs/OverlayMetadataSection$OverlayEntry;Ljava/lang/Object;)Z
/*    */       //   7: ireturn
/*    */       // Line number table:
/*    */       //   Java source line number -> byte code offset
/*    */       //   #17	-> 0
/*    */       // Local variable table:
/*    */       //   start	length	slot	name	descriptor
/*    */       //   0	8	0	this	Lnet/minecraft/server/packs/OverlayMetadataSection$OverlayEntry;
/*    */       //   0	8	1	o	Ljava/lang/Object;
/*    */     }
/*    */     
/*    */     public InclusiveRange<PackFormat> format() {
/* 17 */       return this.format;
/*    */     }
/*    */     
/*    */     public String overlay() {
/* 17 */       return this.overlay;
/*    */     }
/*    */     
/*    */     private static final class IntermediateEntry extends Record implements PackFormat.IntermediaryFormatHolder {
/*    */       private final PackFormat.IntermediaryFormat format;
/*    */       
/*    */       private final String overlay;
/*    */       
/*    */       private static final Codec<IntermediateEntry> CODEC;
/*    */       
/*    */       private IntermediateEntry(PackFormat.IntermediaryFormat format, String overlay) {
/* 18 */         this.format = format;
/* 18 */         this.overlay = overlay;
/*    */       }
/*    */       
/*    */       public final int hashCode() {
/*    */         // Byte code:
/*    */         //   0: aload_0
/*    */         //   1: <illegal opcode> hashCode : (Lnet/minecraft/server/packs/OverlayMetadataSection$OverlayEntry$IntermediateEntry;)I
/*    */         //   6: ireturn
/*    */         // Line number table:
/*    */         //   Java source line number -> byte code offset
/*    */         //   #18	-> 0
/*    */         // Local variable table:
/*    */         //   start	length	slot	name	descriptor
/*    */         //   0	7	0	this	Lnet/minecraft/server/packs/OverlayMetadataSection$OverlayEntry$IntermediateEntry;
/*    */       }
/*    */       
/*    */       public final boolean equals(Object o) {
/*    */         // Byte code:
/*    */         //   0: aload_0
/*    */         //   1: aload_1
/*    */         //   2: <illegal opcode> equals : (Lnet/minecraft/server/packs/OverlayMetadataSection$OverlayEntry$IntermediateEntry;Ljava/lang/Object;)Z
/*    */         //   7: ireturn
/*    */         // Line number table:
/*    */         //   Java source line number -> byte code offset
/*    */         //   #18	-> 0
/*    */         // Local variable table:
/*    */         //   start	length	slot	name	descriptor
/*    */         //   0	8	0	this	Lnet/minecraft/server/packs/OverlayMetadataSection$OverlayEntry$IntermediateEntry;
/*    */         //   0	8	1	o	Ljava/lang/Object;
/*    */       }
/*    */       
/*    */       public PackFormat.IntermediaryFormat format() {
/* 18 */         return this.format;
/*    */       }
/*    */       
/*    */       public String overlay() {
/* 18 */         return this.overlay;
/*    */       }
/*    */       
/*    */       static {
/* 19 */         CODEC = RecordCodecBuilder.create(i -> i.group((App)PackFormat.IntermediaryFormat.OVERLAY_CODEC.forGetter(IntermediateEntry::format), (App)Codec.STRING.validate(OverlayMetadataSection::validateOverlayDir).fieldOf("directory").forGetter(IntermediateEntry::overlay)).apply((Applicative)i, IntermediateEntry::new));
/*    */       }
/*    */       
/*    */       public String toString() {
/* 26 */         return this.overlay;
/*    */       }
/*    */     }
/*    */     
/*    */     private static Codec<List<OverlayEntry>> listCodecForPackType(PackType packType) {
/* 31 */       int lastPreMinorVersion = PackFormat.lastPreMinorVersion(packType);
/* 32 */       return IntermediateEntry.CODEC.listOf().flatXmap(list -> PackFormat.validateHolderList(list, lastPreMinorVersion, ()), list -> DataResult.success(list.stream().map(()).toList()));
/*    */     }
/*    */     
/*    */     public boolean isApplicable(PackFormat formatToTest) {
/* 39 */       return this.format.isValueInRange((Comparable)formatToTest);
/*    */     }
/*    */   }
/*    */   
/*    */   private static final class IntermediateEntry extends Record implements PackFormat.IntermediaryFormatHolder {
/*    */     private final PackFormat.IntermediaryFormat format;
/*    */     
/*    */     private final String overlay;
/*    */     
/*    */     private static final Codec<IntermediateEntry> CODEC;
/*    */     
/*    */     private IntermediateEntry(PackFormat.IntermediaryFormat format, String overlay) {
/*    */       this.format = format;
/*    */       this.overlay = overlay;
/*    */     }
/*    */     
/*    */     public final int hashCode() {
/*    */       // Byte code:
/*    */       //   0: aload_0
/*    */       //   1: <illegal opcode> hashCode : (Lnet/minecraft/server/packs/OverlayMetadataSection$OverlayEntry$IntermediateEntry;)I
/*    */       //   6: ireturn
/*    */       // Line number table:
/*    */       //   Java source line number -> byte code offset
/*    */       //   #18	-> 0
/*    */       // Local variable table:
/*    */       //   start	length	slot	name	descriptor
/*    */       //   0	7	0	this	Lnet/minecraft/server/packs/OverlayMetadataSection$OverlayEntry$IntermediateEntry;
/*    */     }
/*    */     
/*    */     public final boolean equals(Object o) {
/*    */       // Byte code:
/*    */       //   0: aload_0
/*    */       //   1: aload_1
/*    */       //   2: <illegal opcode> equals : (Lnet/minecraft/server/packs/OverlayMetadataSection$OverlayEntry$IntermediateEntry;Ljava/lang/Object;)Z
/*    */       //   7: ireturn
/*    */       // Line number table:
/*    */       //   Java source line number -> byte code offset
/*    */       //   #18	-> 0
/*    */       // Local variable table:
/*    */       //   start	length	slot	name	descriptor
/*    */       //   0	8	0	this	Lnet/minecraft/server/packs/OverlayMetadataSection$OverlayEntry$IntermediateEntry;
/*    */       //   0	8	1	o	Ljava/lang/Object;
/*    */     }
/*    */     
/*    */     public PackFormat.IntermediaryFormat format() {
/*    */       return this.format;
/*    */     }
/*    */     
/*    */     public String overlay() {
/*    */       return this.overlay;
/*    */     }
/*    */     
/*    */     static {
/*    */       CODEC = RecordCodecBuilder.create(i -> i.group((App)PackFormat.IntermediaryFormat.OVERLAY_CODEC.forGetter(IntermediateEntry::format), (App)Codec.STRING.validate(OverlayMetadataSection::validateOverlayDir).fieldOf("directory").forGetter(IntermediateEntry::overlay)).apply((Applicative)i, IntermediateEntry::new));
/*    */     }
/*    */     
/*    */     public String toString() {
/*    */       return this.overlay;
/*    */     }
/*    */   }
/*    */   
/*    */   private static DataResult<String> validateOverlayDir(String path) {
/* 44 */     if (!DIR_VALIDATOR.matcher(path).matches())
/* 45 */       return DataResult.error(() -> path + " is not accepted directory name"); 
/* 47 */     return DataResult.success(path);
/*    */   }
/*    */   
/*    */   @VisibleForTesting
/*    */   public static Codec<OverlayMetadataSection> codecForPackType(PackType packType) {
/* 52 */     return RecordCodecBuilder.create(i -> i.group((App)OverlayEntry.listCodecForPackType(packType).fieldOf("entries").forGetter(OverlayMetadataSection::overlays)).apply((Applicative)i, OverlayMetadataSection::new));
/*    */   }
/*    */   
/* 57 */   public static final MetadataSectionType<OverlayMetadataSection> CLIENT_TYPE = new MetadataSectionType("overlays", codecForPackType(PackType.CLIENT_RESOURCES));
/*    */   
/* 58 */   public static final MetadataSectionType<OverlayMetadataSection> SERVER_TYPE = new MetadataSectionType("overlays", codecForPackType(PackType.SERVER_DATA));
/*    */   
/*    */   public static MetadataSectionType<OverlayMetadataSection> forPackType(PackType packType) {
/* 61 */     switch (packType) {
/*    */       default:
/* 61 */         throw new MatchException(null, null);
/*    */       case CLIENT_RESOURCES:
/*    */       
/*    */       case SERVER_DATA:
/*    */         break;
/*    */     } 
/* 61 */     return 
/*    */       
/* 63 */       SERVER_TYPE;
/*    */   }
/*    */   
/*    */   public List<String> overlaysForVersion(PackFormat version) {
/* 68 */     return this.overlays.stream().filter(entry -> entry.isApplicable(version)).map(OverlayEntry::overlay).toList();
/*    */   }
/*    */ }


/* Location:              C:\Users\karol\AppData\Roaming\ModrinthApp\meta\versions\26.2-0.19.5\26.2-0.19.5.jar!\net\minecraft\server\packs\OverlayMetadataSection.class
 * Java compiler version: 25 (69.0)
 * JD-Core Version:       1.1.3
 */