/*    */ package net.minecraft.world.level.levelgen.structure.templatesystem;
/*    */ 
/*    */ import com.mojang.serialization.MapCodec;
/*    */ 
/*    */ public class NopProcessor implements StructureProcessor {
/*  6 */   public static final MapCodec<NopProcessor> MAP_CODEC = MapCodec.unit(() -> INSTANCE);
/*    */   
/*  8 */   public static final NopProcessor INSTANCE = new NopProcessor();
/*    */   
/*    */   public MapCodec<NopProcessor> codec() {
/* 15 */     return MAP_CODEC;
/*    */   }
/*    */ }


/* Location:              C:\Users\karol\AppData\Roaming\ModrinthApp\meta\versions\26.2-0.19.5\26.2-0.19.5.jar!\net\minecraft\world\level\levelgen\structure\templatesystem\NopProcessor.class
 * Java compiler version: 25 (69.0)
 * JD-Core Version:       1.1.3
 */