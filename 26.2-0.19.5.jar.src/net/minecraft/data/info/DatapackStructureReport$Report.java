/*    */ package net.minecraft.data.info;
/*    */ 
/*    */ import com.mojang.datafixers.kinds.App;
/*    */ import com.mojang.datafixers.kinds.Applicative;
/*    */ import com.mojang.serialization.Codec;
/*    */ import com.mojang.serialization.codecs.RecordCodecBuilder;
/*    */ import java.util.Map;
/*    */ import java.util.function.BiFunction;
/*    */ import net.minecraft.core.Registry;
/*    */ import net.minecraft.resources.ResourceKey;
/*    */ 
/*    */ final class Report extends Record {
/*    */   private final Map<ResourceKey<? extends Registry<?>>, DatapackStructureReport.Entry> registries;
/*    */   
/*    */   private final Map<String, DatapackStructureReport.CustomPackEntry> others;
/*    */   
/*    */   public static final Codec<Report> CODEC;
/*    */   
/*    */   private Report(Map<ResourceKey<? extends Registry<?>>, DatapackStructureReport.Entry> registries, Map<String, DatapackStructureReport.CustomPackEntry> others) {
/* 52 */     this.registries = registries;
/* 52 */     this.others = others;
/*    */   }
/*    */   
/*    */   public final String toString() {
/*    */     // Byte code:
/*    */     //   0: aload_0
/*    */     //   1: <illegal opcode> toString : (Lnet/minecraft/data/info/DatapackStructureReport$Report;)Ljava/lang/String;
/*    */     //   6: areturn
/*    */     // Line number table:
/*    */     //   Java source line number -> byte code offset
/*    */     //   #52	-> 0
/*    */     // Local variable table:
/*    */     //   start	length	slot	name	descriptor
/*    */     //   0	7	0	this	Lnet/minecraft/data/info/DatapackStructureReport$Report;
/*    */   }
/*    */   
/*    */   public final int hashCode() {
/*    */     // Byte code:
/*    */     //   0: aload_0
/*    */     //   1: <illegal opcode> hashCode : (Lnet/minecraft/data/info/DatapackStructureReport$Report;)I
/*    */     //   6: ireturn
/*    */     // Line number table:
/*    */     //   Java source line number -> byte code offset
/*    */     //   #52	-> 0
/*    */     // Local variable table:
/*    */     //   start	length	slot	name	descriptor
/*    */     //   0	7	0	this	Lnet/minecraft/data/info/DatapackStructureReport$Report;
/*    */   }
/*    */   
/*    */   public final boolean equals(Object o) {
/*    */     // Byte code:
/*    */     //   0: aload_0
/*    */     //   1: aload_1
/*    */     //   2: <illegal opcode> equals : (Lnet/minecraft/data/info/DatapackStructureReport$Report;Ljava/lang/Object;)Z
/*    */     //   7: ireturn
/*    */     // Line number table:
/*    */     //   Java source line number -> byte code offset
/*    */     //   #52	-> 0
/*    */     // Local variable table:
/*    */     //   start	length	slot	name	descriptor
/*    */     //   0	8	0	this	Lnet/minecraft/data/info/DatapackStructureReport$Report;
/*    */     //   0	8	1	o	Ljava/lang/Object;
/*    */   }
/*    */   
/*    */   public Map<ResourceKey<? extends Registry<?>>, DatapackStructureReport.Entry> registries() {
/* 52 */     return this.registries;
/*    */   }
/*    */   
/*    */   public Map<String, DatapackStructureReport.CustomPackEntry> others() {
/* 52 */     return this.others;
/*    */   }
/*    */   
/*    */   static {
/* 56 */     CODEC = RecordCodecBuilder.create(i -> i.group((App)Codec.unboundedMap(DatapackStructureReport.REGISTRY_KEY_CODEC, DatapackStructureReport.Entry.CODEC).fieldOf("registries").forGetter(Report::registries), (App)Codec.unboundedMap((Codec)Codec.STRING, DatapackStructureReport.CustomPackEntry.CODEC).fieldOf("others").forGetter(Report::others)).apply((Applicative)i, Report::new));
/*    */   }
/*    */ }


/* Location:              C:\Users\karol\AppData\Roaming\ModrinthApp\meta\versions\26.2-0.19.5\26.2-0.19.5.jar!\net\minecraft\data\info\DatapackStructureReport$Report.class
 * Java compiler version: 25 (69.0)
 * JD-Core Version:       1.1.3
 */