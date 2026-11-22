/*    */ package net.minecraft.client.resources.metadata.animation;
/*    */ 
/*    */ import com.mojang.datafixers.kinds.App;
/*    */ import com.mojang.datafixers.kinds.Applicative;
/*    */ import com.mojang.serialization.Codec;
/*    */ import com.mojang.serialization.codecs.RecordCodecBuilder;
/*    */ import java.util.function.Function;
/*    */ import net.minecraft.server.packs.metadata.MetadataSectionType;
/*    */ import net.minecraft.util.StringRepresentable;
/*    */ 
/*    */ public final class VillagerMetadataSection extends Record {
/*    */   private final Hat hat;
/*    */   
/*    */   public static final Codec<VillagerMetadataSection> CODEC;
/*    */   
/*    */   public VillagerMetadataSection(Hat hat) {
/*  8 */     this.hat = hat;
/*    */   }
/*    */   
/*    */   public final String toString() {
/*    */     // Byte code:
/*    */     //   0: aload_0
/*    */     //   1: <illegal opcode> toString : (Lnet/minecraft/client/resources/metadata/animation/VillagerMetadataSection;)Ljava/lang/String;
/*    */     //   6: areturn
/*    */     // Line number table:
/*    */     //   Java source line number -> byte code offset
/*    */     //   #8	-> 0
/*    */     // Local variable table:
/*    */     //   start	length	slot	name	descriptor
/*    */     //   0	7	0	this	Lnet/minecraft/client/resources/metadata/animation/VillagerMetadataSection;
/*    */   }
/*    */   
/*    */   public final int hashCode() {
/*    */     // Byte code:
/*    */     //   0: aload_0
/*    */     //   1: <illegal opcode> hashCode : (Lnet/minecraft/client/resources/metadata/animation/VillagerMetadataSection;)I
/*    */     //   6: ireturn
/*    */     // Line number table:
/*    */     //   Java source line number -> byte code offset
/*    */     //   #8	-> 0
/*    */     // Local variable table:
/*    */     //   start	length	slot	name	descriptor
/*    */     //   0	7	0	this	Lnet/minecraft/client/resources/metadata/animation/VillagerMetadataSection;
/*    */   }
/*    */   
/*    */   public final boolean equals(Object o) {
/*    */     // Byte code:
/*    */     //   0: aload_0
/*    */     //   1: aload_1
/*    */     //   2: <illegal opcode> equals : (Lnet/minecraft/client/resources/metadata/animation/VillagerMetadataSection;Ljava/lang/Object;)Z
/*    */     //   7: ireturn
/*    */     // Line number table:
/*    */     //   Java source line number -> byte code offset
/*    */     //   #8	-> 0
/*    */     // Local variable table:
/*    */     //   start	length	slot	name	descriptor
/*    */     //   0	8	0	this	Lnet/minecraft/client/resources/metadata/animation/VillagerMetadataSection;
/*    */     //   0	8	1	o	Ljava/lang/Object;
/*    */   }
/*    */   
/*    */   public Hat hat() {
/*  8 */     return this.hat;
/*    */   }
/*    */   
/*    */   static {
/* 11 */     CODEC = RecordCodecBuilder.create(i -> i.group((App)Hat.CODEC.optionalFieldOf("hat", Hat.NONE).forGetter(VillagerMetadataSection::hat)).apply((Applicative)i, VillagerMetadataSection::new));
/*    */   }
/*    */   
/* 15 */   public static final MetadataSectionType<VillagerMetadataSection> TYPE = new MetadataSectionType("villager", CODEC);
/*    */   
/*    */   public enum Hat implements StringRepresentable {
/* 18 */     NONE("none"),
/* 19 */     PARTIAL("partial"),
/* 20 */     FULL("full");
/*    */     
/* 22 */     public static final Codec<Hat> CODEC = (Codec<Hat>)StringRepresentable.fromEnum(Hat::values);
/*    */     
/*    */     private final String name;
/*    */     
/*    */     static {
/*    */     
/*    */     }
/*    */     
/*    */     Hat(String name) {
/* 27 */       this.name = name;
/*    */     }
/*    */     
/*    */     public String getSerializedName() {
/* 32 */       return this.name;
/*    */     }
/*    */   }
/*    */ }


/* Location:              C:\Users\karol\AppData\Roaming\ModrinthApp\meta\versions\26.2-0.19.5\26.2-0.19.5.jar!\net\minecraft\client\resources\metadata\animation\VillagerMetadataSection.class
 * Java compiler version: 25 (69.0)
 * JD-Core Version:       1.1.3
 */