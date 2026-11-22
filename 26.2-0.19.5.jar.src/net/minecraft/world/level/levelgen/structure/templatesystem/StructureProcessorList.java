/*    */ package net.minecraft.world.level.levelgen.structure.templatesystem;
/*    */ 
/*    */ import java.util.List;
/*    */ 
/*    */ public class StructureProcessorList {
/*    */   private final List<StructureProcessor> list;
/*    */   
/*    */   public StructureProcessorList(List<StructureProcessor> list) {
/*  9 */     this.list = list;
/*    */   }
/*    */   
/*    */   public List<StructureProcessor> list() {
/* 13 */     return this.list;
/*    */   }
/*    */   
/*    */   public String toString() {
/* 18 */     return "ProcessorList[" + String.valueOf(this.list) + "]";
/*    */   }
/*    */ }


/* Location:              C:\Users\karol\AppData\Roaming\ModrinthApp\meta\versions\26.2-0.19.5\26.2-0.19.5.jar!\net\minecraft\world\level\levelgen\structure\templatesystem\StructureProcessorList.class
 * Java compiler version: 25 (69.0)
 * JD-Core Version:       1.1.3
 */