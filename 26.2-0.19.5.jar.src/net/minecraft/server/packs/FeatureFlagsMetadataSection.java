/*    */ package net.minecraft.server.packs;
/*    */ 
/*    */ import com.mojang.datafixers.kinds.App;
/*    */ import com.mojang.datafixers.kinds.Applicative;
/*    */ import com.mojang.serialization.Codec;
/*    */ import com.mojang.serialization.codecs.RecordCodecBuilder;
/*    */ import java.util.function.Function;
/*    */ import net.minecraft.server.packs.metadata.MetadataSectionType;
/*    */ import net.minecraft.world.flag.FeatureFlagSet;
/*    */ import net.minecraft.world.flag.FeatureFlags;
/*    */ 
/*    */ public final class FeatureFlagsMetadataSection extends Record {
/*    */   private final FeatureFlagSet flags;
/*    */   
/*    */   private static final Codec<FeatureFlagsMetadataSection> CODEC;
/*    */   
/*    */   public FeatureFlagsMetadataSection(FeatureFlagSet flags) {
/*  9 */     this.flags = flags;
/*    */   }
/*    */   
/*    */   public final String toString() {
/*    */     // Byte code:
/*    */     //   0: aload_0
/*    */     //   1: <illegal opcode> toString : (Lnet/minecraft/server/packs/FeatureFlagsMetadataSection;)Ljava/lang/String;
/*    */     //   6: areturn
/*    */     // Line number table:
/*    */     //   Java source line number -> byte code offset
/*    */     //   #9	-> 0
/*    */     // Local variable table:
/*    */     //   start	length	slot	name	descriptor
/*    */     //   0	7	0	this	Lnet/minecraft/server/packs/FeatureFlagsMetadataSection;
/*    */   }
/*    */   
/*    */   public final int hashCode() {
/*    */     // Byte code:
/*    */     //   0: aload_0
/*    */     //   1: <illegal opcode> hashCode : (Lnet/minecraft/server/packs/FeatureFlagsMetadataSection;)I
/*    */     //   6: ireturn
/*    */     // Line number table:
/*    */     //   Java source line number -> byte code offset
/*    */     //   #9	-> 0
/*    */     // Local variable table:
/*    */     //   start	length	slot	name	descriptor
/*    */     //   0	7	0	this	Lnet/minecraft/server/packs/FeatureFlagsMetadataSection;
/*    */   }
/*    */   
/*    */   public final boolean equals(Object o) {
/*    */     // Byte code:
/*    */     //   0: aload_0
/*    */     //   1: aload_1
/*    */     //   2: <illegal opcode> equals : (Lnet/minecraft/server/packs/FeatureFlagsMetadataSection;Ljava/lang/Object;)Z
/*    */     //   7: ireturn
/*    */     // Line number table:
/*    */     //   Java source line number -> byte code offset
/*    */     //   #9	-> 0
/*    */     // Local variable table:
/*    */     //   start	length	slot	name	descriptor
/*    */     //   0	8	0	this	Lnet/minecraft/server/packs/FeatureFlagsMetadataSection;
/*    */     //   0	8	1	o	Ljava/lang/Object;
/*    */   }
/*    */   
/*    */   public FeatureFlagSet flags() {
/*  9 */     return this.flags;
/*    */   }
/*    */   
/*    */   static {
/* 10 */     CODEC = RecordCodecBuilder.create(i -> i.group((App)FeatureFlags.CODEC.fieldOf("enabled").forGetter(FeatureFlagsMetadataSection::flags)).apply((Applicative)i, FeatureFlagsMetadataSection::new));
/*    */   }
/*    */   
/* 14 */   public static final MetadataSectionType<FeatureFlagsMetadataSection> TYPE = new MetadataSectionType("features", CODEC);
/*    */ }


/* Location:              C:\Users\karol\AppData\Roaming\ModrinthApp\meta\versions\26.2-0.19.5\26.2-0.19.5.jar!\net\minecraft\server\packs\FeatureFlagsMetadataSection.class
 * Java compiler version: 25 (69.0)
 * JD-Core Version:       1.1.3
 */