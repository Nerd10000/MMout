/*    */ package net.minecraft.client.renderer.texture.atlas.sources;
/*    */ 
/*    */ import com.mojang.datafixers.kinds.App;
/*    */ import com.mojang.datafixers.kinds.Applicative;
/*    */ import com.mojang.serialization.Codec;
/*    */ import com.mojang.serialization.MapCodec;
/*    */ import com.mojang.serialization.codecs.RecordCodecBuilder;
/*    */ import java.util.function.BiFunction;
/*    */ import net.minecraft.client.renderer.texture.atlas.SpriteSource;
/*    */ import net.minecraft.resources.FileToIdConverter;
/*    */ import net.minecraft.resources.Identifier;
/*    */ import net.minecraft.server.packs.resources.Resource;
/*    */ import net.minecraft.server.packs.resources.ResourceManager;
/*    */ 
/*    */ public final class DirectoryLister extends Record implements SpriteSource {
/*    */   private final String sourcePath;
/*    */   
/*    */   private final String idPrefix;
/*    */   
/*    */   public static final MapCodec<DirectoryLister> MAP_CODEC;
/*    */   
/*    */   public DirectoryLister(String sourcePath, String idPrefix) {
/* 11 */     this.sourcePath = sourcePath;
/* 11 */     this.idPrefix = idPrefix;
/*    */   }
/*    */   
/*    */   public final String toString() {
/*    */     // Byte code:
/*    */     //   0: aload_0
/*    */     //   1: <illegal opcode> toString : (Lnet/minecraft/client/renderer/texture/atlas/sources/DirectoryLister;)Ljava/lang/String;
/*    */     //   6: areturn
/*    */     // Line number table:
/*    */     //   Java source line number -> byte code offset
/*    */     //   #11	-> 0
/*    */     // Local variable table:
/*    */     //   start	length	slot	name	descriptor
/*    */     //   0	7	0	this	Lnet/minecraft/client/renderer/texture/atlas/sources/DirectoryLister;
/*    */   }
/*    */   
/*    */   public final int hashCode() {
/*    */     // Byte code:
/*    */     //   0: aload_0
/*    */     //   1: <illegal opcode> hashCode : (Lnet/minecraft/client/renderer/texture/atlas/sources/DirectoryLister;)I
/*    */     //   6: ireturn
/*    */     // Line number table:
/*    */     //   Java source line number -> byte code offset
/*    */     //   #11	-> 0
/*    */     // Local variable table:
/*    */     //   start	length	slot	name	descriptor
/*    */     //   0	7	0	this	Lnet/minecraft/client/renderer/texture/atlas/sources/DirectoryLister;
/*    */   }
/*    */   
/*    */   public final boolean equals(Object o) {
/*    */     // Byte code:
/*    */     //   0: aload_0
/*    */     //   1: aload_1
/*    */     //   2: <illegal opcode> equals : (Lnet/minecraft/client/renderer/texture/atlas/sources/DirectoryLister;Ljava/lang/Object;)Z
/*    */     //   7: ireturn
/*    */     // Line number table:
/*    */     //   Java source line number -> byte code offset
/*    */     //   #11	-> 0
/*    */     // Local variable table:
/*    */     //   start	length	slot	name	descriptor
/*    */     //   0	8	0	this	Lnet/minecraft/client/renderer/texture/atlas/sources/DirectoryLister;
/*    */     //   0	8	1	o	Ljava/lang/Object;
/*    */   }
/*    */   
/*    */   public String sourcePath() {
/* 11 */     return this.sourcePath;
/*    */   }
/*    */   
/*    */   public String idPrefix() {
/* 11 */     return this.idPrefix;
/*    */   }
/*    */   
/*    */   static {
/* 15 */     MAP_CODEC = RecordCodecBuilder.mapCodec(i -> i.group((App)Codec.STRING.fieldOf("source").forGetter(DirectoryLister::sourcePath), (App)Codec.STRING.fieldOf("prefix").forGetter(DirectoryLister::idPrefix)).apply((Applicative)i, DirectoryLister::new));
/*    */   }
/*    */   
/*    */   public void run(ResourceManager resourceManager, SpriteSource.Output output) {
/* 22 */     FileToIdConverter converter = new FileToIdConverter("textures/" + this.sourcePath, ".png");
/* 24 */     converter.listMatchingResources(resourceManager).forEach((identifier, resource) -> {
/*    */           Identifier spriteLocation = converter.fileToId(identifier).withPrefix(this.idPrefix);
/*    */           output.add(spriteLocation, resource);
/*    */         });
/*    */   }
/*    */   
/*    */   public MapCodec<DirectoryLister> codec() {
/* 32 */     return MAP_CODEC;
/*    */   }
/*    */ }


/* Location:              C:\Users\karol\AppData\Roaming\ModrinthApp\meta\versions\26.2-0.19.5\26.2-0.19.5.jar!\net\minecraft\client\renderer\texture\atlas\sources\DirectoryLister.class
 * Java compiler version: 25 (69.0)
 * JD-Core Version:       1.1.3
 */