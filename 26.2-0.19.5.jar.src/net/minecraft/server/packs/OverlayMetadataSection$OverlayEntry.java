/*    */ package net.minecraft.server.packs;
/*    */ 
/*    */ import com.mojang.datafixers.kinds.App;
/*    */ import com.mojang.datafixers.kinds.Applicative;
/*    */ import com.mojang.serialization.Codec;
/*    */ import com.mojang.serialization.DataResult;
/*    */ import com.mojang.serialization.codecs.RecordCodecBuilder;
/*    */ import java.util.List;
/*    */ import java.util.function.BiFunction;
/*    */ import net.minecraft.server.packs.metadata.pack.PackFormat;
/*    */ import net.minecraft.util.InclusiveRange;
/*    */ 
/*    */ public final class OverlayEntry extends Record {
/*    */   private final InclusiveRange<PackFormat> format;
/*    */   
/*    */   private final String overlay;
/*    */   
/*    */   public OverlayEntry(InclusiveRange<PackFormat> format, String overlay) {
/* 17 */     this.format = format;
/* 17 */     this.overlay = overlay;
/*    */   }
/*    */   
/*    */   public final String toString() {
/*    */     // Byte code:
/*    */     //   0: aload_0
/*    */     //   1: <illegal opcode> toString : (Lnet/minecraft/server/packs/OverlayMetadataSection$OverlayEntry;)Ljava/lang/String;
/*    */     //   6: areturn
/*    */     // Line number table:
/*    */     //   Java source line number -> byte code offset
/*    */     //   #17	-> 0
/*    */     // Local variable table:
/*    */     //   start	length	slot	name	descriptor
/*    */     //   0	7	0	this	Lnet/minecraft/server/packs/OverlayMetadataSection$OverlayEntry;
/*    */   }
/*    */   
/*    */   public final int hashCode() {
/*    */     // Byte code:
/*    */     //   0: aload_0
/*    */     //   1: <illegal opcode> hashCode : (Lnet/minecraft/server/packs/OverlayMetadataSection$OverlayEntry;)I
/*    */     //   6: ireturn
/*    */     // Line number table:
/*    */     //   Java source line number -> byte code offset
/*    */     //   #17	-> 0
/*    */     // Local variable table:
/*    */     //   start	length	slot	name	descriptor
/*    */     //   0	7	0	this	Lnet/minecraft/server/packs/OverlayMetadataSection$OverlayEntry;
/*    */   }
/*    */   
/*    */   public final boolean equals(Object o) {
/*    */     // Byte code:
/*    */     //   0: aload_0
/*    */     //   1: aload_1
/*    */     //   2: <illegal opcode> equals : (Lnet/minecraft/server/packs/OverlayMetadataSection$OverlayEntry;Ljava/lang/Object;)Z
/*    */     //   7: ireturn
/*    */     // Line number table:
/*    */     //   Java source line number -> byte code offset
/*    */     //   #17	-> 0
/*    */     // Local variable table:
/*    */     //   start	length	slot	name	descriptor
/*    */     //   0	8	0	this	Lnet/minecraft/server/packs/OverlayMetadataSection$OverlayEntry;
/*    */     //   0	8	1	o	Ljava/lang/Object;
/*    */   }
/*    */   
/*    */   public InclusiveRange<PackFormat> format() {
/* 17 */     return this.format;
/*    */   }
/*    */   
/*    */   public String overlay() {
/* 17 */     return this.overlay;
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
/* 18 */       this.format = format;
/* 18 */       this.overlay = overlay;
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
/* 18 */       return this.format;
/*    */     }
/*    */     
/*    */     public String overlay() {
/* 18 */       return this.overlay;
/*    */     }
/*    */     
/*    */     static {
/* 19 */       CODEC = RecordCodecBuilder.create(i -> i.group((App)PackFormat.IntermediaryFormat.OVERLAY_CODEC.forGetter(IntermediateEntry::format), (App)Codec.STRING.validate(OverlayMetadataSection::validateOverlayDir).fieldOf("directory").forGetter(IntermediateEntry::overlay)).apply((Applicative)i, IntermediateEntry::new));
/*    */     }
/*    */     
/*    */     public String toString() {
/* 26 */       return this.overlay;
/*    */     }
/*    */   }
/*    */   
/*    */   private static Codec<List<OverlayEntry>> listCodecForPackType(PackType packType) {
/* 31 */     int lastPreMinorVersion = PackFormat.lastPreMinorVersion(packType);
/* 32 */     return IntermediateEntry.CODEC.listOf().flatXmap(list -> PackFormat.validateHolderList(list, lastPreMinorVersion, ()), list -> DataResult.success(list.stream().map(()).toList()));
/*    */   }
/*    */   
/*    */   public boolean isApplicable(PackFormat formatToTest) {
/* 39 */     return this.format.isValueInRange((Comparable)formatToTest);
/*    */   }
/*    */ }


/* Location:              C:\Users\karol\AppData\Roaming\ModrinthApp\meta\versions\26.2-0.19.5\26.2-0.19.5.jar!\net\minecraft\server\packs\OverlayMetadataSection$OverlayEntry.class
 * Java compiler version: 25 (69.0)
 * JD-Core Version:       1.1.3
 */