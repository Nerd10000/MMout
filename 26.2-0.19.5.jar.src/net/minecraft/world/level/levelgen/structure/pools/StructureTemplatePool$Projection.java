/*    */ package net.minecraft.world.level.levelgen.structure.pools;
/*    */ 
/*    */ import com.google.common.collect.ImmutableList;
/*    */ import net.minecraft.util.StringRepresentable;
/*    */ import net.minecraft.world.level.levelgen.Heightmap;
/*    */ import net.minecraft.world.level.levelgen.structure.templatesystem.GravityProcessor;
/*    */ import net.minecraft.world.level.levelgen.structure.templatesystem.StructureProcessor;
/*    */ 
/*    */ public enum Projection implements StringRepresentable {
/* 44 */   TERRAIN_MATCHING("terrain_matching", 
/*    */     
/* 46 */     ImmutableList.of(new GravityProcessor(Heightmap.Types.WORLD_SURFACE_WG, -1))),
/* 48 */   RIGID("rigid", 
/*    */     
/* 50 */     ImmutableList.of());
/*    */   
/*    */   public static final StringRepresentable.EnumCodec<Projection> CODEC;
/*    */   
/*    */   private final String name;
/*    */   
/*    */   private final ImmutableList<StructureProcessor> processors;
/*    */   
/*    */   static {
/* 53 */     CODEC = StringRepresentable.fromEnum(Projection::values);
/*    */   }
/*    */   
/*    */   Projection(String name, ImmutableList<StructureProcessor> processors) {
/* 59 */     this.name = name;
/* 60 */     this.processors = processors;
/*    */   }
/*    */   
/*    */   public String getName() {
/* 64 */     return this.name;
/*    */   }
/*    */   
/*    */   public static Projection byName(String name) {
/* 68 */     return (Projection)CODEC.byName(name);
/*    */   }
/*    */   
/*    */   public ImmutableList<StructureProcessor> getProcessors() {
/* 72 */     return this.processors;
/*    */   }
/*    */   
/*    */   public String getSerializedName() {
/* 77 */     return this.name;
/*    */   }
/*    */ }


/* Location:              C:\Users\karol\AppData\Roaming\ModrinthApp\meta\versions\26.2-0.19.5\26.2-0.19.5.jar!\net\minecraft\world\level\levelgen\structure\pools\StructureTemplatePool$Projection.class
 * Java compiler version: 25 (69.0)
 * JD-Core Version:       1.1.3
 */