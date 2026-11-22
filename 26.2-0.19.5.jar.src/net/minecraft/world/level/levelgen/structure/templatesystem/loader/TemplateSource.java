/*    */ package net.minecraft.world.level.levelgen.structure.templatesystem.loader;
/*    */ 
/*    */ import com.mojang.brigadier.exceptions.CommandSyntaxException;
/*    */ import com.mojang.datafixers.DataFixer;
/*    */ import java.io.FileNotFoundException;
/*    */ import java.io.IOException;
/*    */ import java.io.InputStream;
/*    */ import java.io.InputStreamReader;
/*    */ import java.io.Reader;
/*    */ import java.nio.charset.StandardCharsets;
/*    */ import java.util.Optional;
/*    */ import java.util.function.Consumer;
/*    */ import java.util.stream.Stream;
/*    */ import net.minecraft.core.HolderGetter;
/*    */ import net.minecraft.nbt.CompoundTag;
/*    */ import net.minecraft.nbt.NbtAccounter;
/*    */ import net.minecraft.nbt.NbtIo;
/*    */ import net.minecraft.nbt.NbtUtils;
/*    */ import net.minecraft.resources.Identifier;
/*    */ import net.minecraft.server.packs.resources.IoSupplier;
/*    */ import net.minecraft.util.FastBufferedInputStream;
/*    */ import net.minecraft.util.datafix.DataFixTypes;
/*    */ import net.minecraft.world.level.block.Block;
/*    */ import net.minecraft.world.level.levelgen.structure.templatesystem.StructureTemplate;
/*    */ 
/*    */ public abstract class TemplateSource {
/*    */   private final DataFixer fixerUpper;
/*    */   
/*    */   private final HolderGetter<Block> blockLookup;
/*    */   
/*    */   protected TemplateSource(DataFixer fixerUpper, HolderGetter<Block> blockLookup) {
/* 33 */     this.fixerUpper = fixerUpper;
/* 34 */     this.blockLookup = blockLookup;
/*    */   }
/*    */   
/*    */   public abstract Optional<StructureTemplate> load(Identifier paramIdentifier);
/*    */   
/*    */   public abstract Stream<Identifier> list();
/*    */   
/*    */   protected Optional<StructureTemplate> load(IoSupplier<InputStream> opener, boolean asText, Consumer<Throwable> onError) {
/*    */     try {
/* 43 */       InputStream rawInput = (InputStream)opener.get();
/*    */       try {
/* 44 */         FastBufferedInputStream fastBufferedInputStream = new FastBufferedInputStream(rawInput);
/*    */         try {
/*    */           CompoundTag structureTag;
/* 47 */           if (asText) {
/* 48 */             structureTag = readTextStructure((InputStream)fastBufferedInputStream);
/*    */           } else {
/* 50 */             structureTag = readStructure((InputStream)fastBufferedInputStream);
/*    */           } 
/* 52 */           Optional<StructureTemplate> optional = Optional.of(readStructure(structureTag));
/* 53 */           fastBufferedInputStream.close();
/* 53 */           if (rawInput != null)
/* 53 */             rawInput.close(); 
/*    */           return optional;
/*    */         } catch (Throwable throwable) {
/*    */           try {
/*    */             fastBufferedInputStream.close();
/*    */           } catch (Throwable throwable1) {
/*    */             throwable.addSuppressed(throwable1);
/*    */           } 
/*    */           throw throwable;
/*    */         } 
/*    */       } catch (Throwable throwable) {
/*    */         if (rawInput != null)
/*    */           try {
/*    */             rawInput.close();
/*    */           } catch (Throwable throwable1) {
/*    */             throwable.addSuppressed(throwable1);
/*    */           }  
/*    */         throw throwable;
/*    */       } 
/* 53 */     } catch (FileNotFoundException e) {
/* 54 */       return Optional.empty();
/* 55 */     } catch (Throwable e) {
/* 56 */       onError.accept(e);
/* 57 */       return Optional.empty();
/*    */     } 
/*    */   }
/*    */   
/*    */   private static CompoundTag readStructure(InputStream input) throws IOException {
/* 62 */     return NbtIo.readCompressed(input, NbtAccounter.unlimitedHeap());
/*    */   }
/*    */   
/*    */   private static CompoundTag readTextStructure(InputStream input) throws IOException, CommandSyntaxException {
/* 66 */     Reader reader = new InputStreamReader(input, StandardCharsets.UTF_8);
/*    */     try {
/* 67 */       String contents = reader.readAllAsString();
/* 68 */       CompoundTag compoundTag = NbtUtils.snbtToStructure(contents);
/* 69 */       reader.close();
/*    */       return compoundTag;
/*    */     } catch (Throwable throwable) {
/*    */       try {
/*    */         reader.close();
/*    */       } catch (Throwable throwable1) {
/*    */         throwable.addSuppressed(throwable1);
/*    */       } 
/*    */       throw throwable;
/*    */     } 
/*    */   }
/*    */   
/*    */   private StructureTemplate readStructure(CompoundTag tag) {
/* 73 */     StructureTemplate structureTemplate = new StructureTemplate();
/* 75 */     int version = NbtUtils.getDataVersion(tag, 500);
/* 76 */     structureTemplate.load(this.blockLookup, DataFixTypes.STRUCTURE.updateToCurrentVersion(this.fixerUpper, tag, version));
/* 77 */     return structureTemplate;
/*    */   }
/*    */ }


/* Location:              C:\Users\karol\AppData\Roaming\ModrinthApp\meta\versions\26.2-0.19.5\26.2-0.19.5.jar!\net\minecraft\world\level\levelgen\structure\templatesystem\loader\TemplateSource.class
 * Java compiler version: 25 (69.0)
 * JD-Core Version:       1.1.3
 */