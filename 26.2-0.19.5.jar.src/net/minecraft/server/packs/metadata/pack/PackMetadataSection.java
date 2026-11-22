/*    */ package net.minecraft.server.packs.metadata.pack;
/*    */ 
/*    */ import com.mojang.datafixers.kinds.App;
/*    */ import com.mojang.datafixers.kinds.Applicative;
/*    */ import com.mojang.serialization.Codec;
/*    */ import com.mojang.serialization.codecs.RecordCodecBuilder;
/*    */ import java.util.function.BiFunction;
/*    */ import net.minecraft.network.chat.Component;
/*    */ import net.minecraft.network.chat.ComponentSerialization;
/*    */ import net.minecraft.server.packs.PackType;
/*    */ import net.minecraft.server.packs.metadata.MetadataSectionType;
/*    */ import net.minecraft.util.InclusiveRange;
/*    */ 
/*    */ public final class PackMetadataSection extends Record {
/*    */   private final Component description;
/*    */   
/*    */   private final InclusiveRange<PackFormat> supportedFormats;
/*    */   
/*    */   private static final Codec<PackMetadataSection> FALLBACK_CODEC;
/*    */   
/*    */   public PackMetadataSection(Component description, InclusiveRange<PackFormat> supportedFormats) {
/* 12 */     this.description = description;
/* 12 */     this.supportedFormats = supportedFormats;
/*    */   }
/*    */   
/*    */   public final String toString() {
/*    */     // Byte code:
/*    */     //   0: aload_0
/*    */     //   1: <illegal opcode> toString : (Lnet/minecraft/server/packs/metadata/pack/PackMetadataSection;)Ljava/lang/String;
/*    */     //   6: areturn
/*    */     // Line number table:
/*    */     //   Java source line number -> byte code offset
/*    */     //   #12	-> 0
/*    */     // Local variable table:
/*    */     //   start	length	slot	name	descriptor
/*    */     //   0	7	0	this	Lnet/minecraft/server/packs/metadata/pack/PackMetadataSection;
/*    */   }
/*    */   
/*    */   public final int hashCode() {
/*    */     // Byte code:
/*    */     //   0: aload_0
/*    */     //   1: <illegal opcode> hashCode : (Lnet/minecraft/server/packs/metadata/pack/PackMetadataSection;)I
/*    */     //   6: ireturn
/*    */     // Line number table:
/*    */     //   Java source line number -> byte code offset
/*    */     //   #12	-> 0
/*    */     // Local variable table:
/*    */     //   start	length	slot	name	descriptor
/*    */     //   0	7	0	this	Lnet/minecraft/server/packs/metadata/pack/PackMetadataSection;
/*    */   }
/*    */   
/*    */   public final boolean equals(Object o) {
/*    */     // Byte code:
/*    */     //   0: aload_0
/*    */     //   1: aload_1
/*    */     //   2: <illegal opcode> equals : (Lnet/minecraft/server/packs/metadata/pack/PackMetadataSection;Ljava/lang/Object;)Z
/*    */     //   7: ireturn
/*    */     // Line number table:
/*    */     //   Java source line number -> byte code offset
/*    */     //   #12	-> 0
/*    */     // Local variable table:
/*    */     //   start	length	slot	name	descriptor
/*    */     //   0	8	0	this	Lnet/minecraft/server/packs/metadata/pack/PackMetadataSection;
/*    */     //   0	8	1	o	Ljava/lang/Object;
/*    */   }
/*    */   
/*    */   public Component description() {
/* 12 */     return this.description;
/*    */   }
/*    */   
/*    */   public InclusiveRange<PackFormat> supportedFormats() {
/* 12 */     return this.supportedFormats;
/*    */   }
/*    */   
/*    */   static {
/* 13 */     FALLBACK_CODEC = RecordCodecBuilder.create(i -> i.group((App)ComponentSerialization.CODEC.fieldOf("description").forGetter(PackMetadataSection::description)).apply((Applicative)i, ()));
/*    */   }
/*    */   
/*    */   private static Codec<PackMetadataSection> codecForPackType(PackType packType) {
/* 18 */     return RecordCodecBuilder.create(i -> i.group((App)ComponentSerialization.CODEC.fieldOf("description").forGetter(PackMetadataSection::description), (App)PackFormat.packCodec(packType).forGetter(PackMetadataSection::supportedFormats)).apply((Applicative)i, PackMetadataSection::new));
/*    */   }
/*    */   
/* 24 */   public static final MetadataSectionType<PackMetadataSection> CLIENT_TYPE = new MetadataSectionType("pack", codecForPackType(PackType.CLIENT_RESOURCES));
/*    */   
/* 25 */   public static final MetadataSectionType<PackMetadataSection> SERVER_TYPE = new MetadataSectionType("pack", codecForPackType(PackType.SERVER_DATA));
/*    */   
/* 26 */   public static final MetadataSectionType<PackMetadataSection> FALLBACK_TYPE = new MetadataSectionType("pack", FALLBACK_CODEC);
/*    */   
/*    */   public static MetadataSectionType<PackMetadataSection> forPackType(PackType packType) {
/* 29 */     switch (packType) {
/*    */       default:
/* 29 */         throw new MatchException(null, null);
/*    */       case CLIENT_RESOURCES:
/*    */       
/*    */       case SERVER_DATA:
/*    */         break;
/*    */     } 
/* 29 */     return 
/*    */       
/* 31 */       SERVER_TYPE;
/*    */   }
/*    */ }


/* Location:              C:\Users\karol\AppData\Roaming\ModrinthApp\meta\versions\26.2-0.19.5\26.2-0.19.5.jar!\net\minecraft\server\packs\metadata\pack\PackMetadataSection.class
 * Java compiler version: 25 (69.0)
 * JD-Core Version:       1.1.3
 */