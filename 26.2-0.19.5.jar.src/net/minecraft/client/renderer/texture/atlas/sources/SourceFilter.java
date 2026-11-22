/*    */ package net.minecraft.client.renderer.texture.atlas.sources;
/*    */ 
/*    */ import com.mojang.datafixers.kinds.App;
/*    */ import com.mojang.datafixers.kinds.Applicative;
/*    */ import com.mojang.serialization.MapCodec;
/*    */ import com.mojang.serialization.codecs.RecordCodecBuilder;
/*    */ import java.util.function.Function;
/*    */ import net.minecraft.client.renderer.texture.atlas.SpriteSource;
/*    */ import net.minecraft.server.packs.resources.ResourceManager;
/*    */ import net.minecraft.util.IdentifierPattern;
/*    */ 
/*    */ public final class SourceFilter extends Record implements SpriteSource {
/*    */   private final IdentifierPattern filter;
/*    */   
/*    */   public static final MapCodec<SourceFilter> MAP_CODEC;
/*    */   
/*    */   public final String toString() {
/*    */     // Byte code:
/*    */     //   0: aload_0
/*    */     //   1: <illegal opcode> toString : (Lnet/minecraft/client/renderer/texture/atlas/sources/SourceFilter;)Ljava/lang/String;
/*    */     //   6: areturn
/*    */     // Line number table:
/*    */     //   Java source line number -> byte code offset
/*    */     //   #9	-> 0
/*    */     // Local variable table:
/*    */     //   start	length	slot	name	descriptor
/*    */     //   0	7	0	this	Lnet/minecraft/client/renderer/texture/atlas/sources/SourceFilter;
/*    */   }
/*    */   
/*    */   public final int hashCode() {
/*    */     // Byte code:
/*    */     //   0: aload_0
/*    */     //   1: <illegal opcode> hashCode : (Lnet/minecraft/client/renderer/texture/atlas/sources/SourceFilter;)I
/*    */     //   6: ireturn
/*    */     // Line number table:
/*    */     //   Java source line number -> byte code offset
/*    */     //   #9	-> 0
/*    */     // Local variable table:
/*    */     //   start	length	slot	name	descriptor
/*    */     //   0	7	0	this	Lnet/minecraft/client/renderer/texture/atlas/sources/SourceFilter;
/*    */   }
/*    */   
/*    */   public final boolean equals(Object o) {
/*    */     // Byte code:
/*    */     //   0: aload_0
/*    */     //   1: aload_1
/*    */     //   2: <illegal opcode> equals : (Lnet/minecraft/client/renderer/texture/atlas/sources/SourceFilter;Ljava/lang/Object;)Z
/*    */     //   7: ireturn
/*    */     // Line number table:
/*    */     //   Java source line number -> byte code offset
/*    */     //   #9	-> 0
/*    */     // Local variable table:
/*    */     //   start	length	slot	name	descriptor
/*    */     //   0	8	0	this	Lnet/minecraft/client/renderer/texture/atlas/sources/SourceFilter;
/*    */     //   0	8	1	o	Ljava/lang/Object;
/*    */   }
/*    */   
/*    */   public IdentifierPattern filter() {
/*  9 */     return this.filter;
/*    */   }
/*    */   
/*    */   static {
/* 12 */     MAP_CODEC = RecordCodecBuilder.mapCodec(i -> i.group((App)IdentifierPattern.CODEC.fieldOf("pattern").forGetter(SourceFilter::filter)).apply((Applicative)i, SourceFilter::new));
/*    */   }
/*    */   
/*    */   public SourceFilter(IdentifierPattern filter) {
/* 17 */     this.filter = filter;
/*    */   }
/*    */   
/*    */   public void run(ResourceManager resourceManager, SpriteSource.Output output) {
/* 22 */     output.removeAll(this.filter.locationPredicate());
/*    */   }
/*    */   
/*    */   public MapCodec<SourceFilter> codec() {
/* 27 */     return MAP_CODEC;
/*    */   }
/*    */ }


/* Location:              C:\Users\karol\AppData\Roaming\ModrinthApp\meta\versions\26.2-0.19.5\26.2-0.19.5.jar!\net\minecraft\client\renderer\texture\atlas\sources\SourceFilter.class
 * Java compiler version: 25 (69.0)
 * JD-Core Version:       1.1.3
 */