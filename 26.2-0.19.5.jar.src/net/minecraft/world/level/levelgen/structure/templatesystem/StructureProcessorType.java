/*    */ package net.minecraft.world.level.levelgen.structure.templatesystem;
/*    */ 
/*    */ import com.mojang.serialization.Codec;
/*    */ import com.mojang.serialization.MapCodec;
/*    */ import java.util.function.Function;
/*    */ import net.minecraft.core.Holder;
/*    */ import net.minecraft.core.registries.BuiltInRegistries;
/*    */ import net.minecraft.core.registries.Registries;
/*    */ import net.minecraft.resources.RegistryFileCodec;
/*    */ 
/*    */ public interface StructureProcessorType {
/*    */   public static final Codec<StructureProcessor> SINGLE_CODEC;
/*    */   
/*    */   static {
/* 10 */     SINGLE_CODEC = BuiltInRegistries.STRUCTURE_PROCESSOR.byNameCodec().dispatch("processor_type", StructureProcessor::codec, c -> c);
/*    */   }
/*    */   
/* 11 */   public static final Codec<StructureProcessorList> LIST_OBJECT_CODEC = SINGLE_CODEC.listOf().xmap(StructureProcessorList::new, StructureProcessorList::list);
/*    */   
/* 13 */   public static final Codec<StructureProcessorList> DIRECT_CODEC = Codec.withAlternative(LIST_OBJECT_CODEC
/* 14 */       .fieldOf("processors").codec(), LIST_OBJECT_CODEC);
/*    */   
/* 17 */   public static final Codec<Holder<StructureProcessorList>> LIST_CODEC = (Codec<Holder<StructureProcessorList>>)RegistryFileCodec.create(Registries.PROCESSOR_LIST, DIRECT_CODEC);
/*    */ }


/* Location:              C:\Users\karol\AppData\Roaming\ModrinthApp\meta\versions\26.2-0.19.5\26.2-0.19.5.jar!\net\minecraft\world\level\levelgen\structure\templatesystem\StructureProcessorType.class
 * Java compiler version: 25 (69.0)
 * JD-Core Version:       1.1.3
 */