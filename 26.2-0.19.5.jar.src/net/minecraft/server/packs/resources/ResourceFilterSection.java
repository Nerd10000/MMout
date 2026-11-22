/*    */ package net.minecraft.server.packs.resources;
/*    */ 
/*    */ import com.mojang.datafixers.kinds.App;
/*    */ import com.mojang.datafixers.kinds.Applicative;
/*    */ import com.mojang.serialization.Codec;
/*    */ import com.mojang.serialization.codecs.RecordCodecBuilder;
/*    */ import java.util.List;
/*    */ import java.util.function.Function;
/*    */ import net.minecraft.server.packs.metadata.MetadataSectionType;
/*    */ import net.minecraft.util.IdentifierPattern;
/*    */ 
/*    */ public class ResourceFilterSection {
/*    */   private static final Codec<ResourceFilterSection> CODEC;
/*    */   
/*    */   static {
/* 11 */     CODEC = RecordCodecBuilder.create(i -> i.group((App)Codec.list(IdentifierPattern.CODEC).fieldOf("block").forGetter(())).apply((Applicative)i, ResourceFilterSection::new));
/*    */   }
/*    */   
/* 15 */   public static final MetadataSectionType<ResourceFilterSection> TYPE = new MetadataSectionType("filter", CODEC);
/*    */   
/*    */   private final List<IdentifierPattern> blockList;
/*    */   
/*    */   public ResourceFilterSection(List<IdentifierPattern> blockList) {
/* 20 */     this.blockList = List.copyOf(blockList);
/*    */   }
/*    */   
/*    */   public boolean isNamespaceFiltered(String namespace) {
/* 24 */     return this.blockList.stream().anyMatch(p -> p.namespacePredicate().test(namespace));
/*    */   }
/*    */   
/*    */   public boolean isPathFiltered(String path) {
/* 28 */     return this.blockList.stream().anyMatch(p -> p.pathPredicate().test(path));
/*    */   }
/*    */ }


/* Location:              C:\Users\karol\AppData\Roaming\ModrinthApp\meta\versions\26.2-0.19.5\26.2-0.19.5.jar!\net\minecraft\server\packs\resources\ResourceFilterSection.class
 * Java compiler version: 25 (69.0)
 * JD-Core Version:       1.1.3
 */