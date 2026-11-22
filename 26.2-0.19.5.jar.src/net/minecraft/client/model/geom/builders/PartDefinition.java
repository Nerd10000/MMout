/*    */ package net.minecraft.client.model.geom.builders;
/*    */ 
/*    */ import com.google.common.collect.Maps;
/*    */ import it.unimi.dsi.fastutil.objects.Object2ObjectArrayMap;
/*    */ import java.util.List;
/*    */ import java.util.Map;
/*    */ import java.util.Set;
/*    */ import java.util.function.Supplier;
/*    */ import java.util.function.UnaryOperator;
/*    */ import java.util.stream.Collectors;
/*    */ import net.minecraft.client.model.geom.ModelPart;
/*    */ import net.minecraft.client.model.geom.PartPose;
/*    */ 
/*    */ public class PartDefinition {
/*    */   private final List<CubeDefinition> cubes;
/*    */   
/*    */   private final PartPose partPose;
/*    */   
/* 18 */   private final Map<String, PartDefinition> children = Maps.newHashMap();
/*    */   
/*    */   PartDefinition(List<CubeDefinition> cubes, PartPose partPose) {
/* 22 */     this.cubes = cubes;
/* 23 */     this.partPose = partPose;
/*    */   }
/*    */   
/*    */   public PartDefinition addOrReplaceChild(String name, CubeListBuilder cubes, PartPose partPose) {
/* 27 */     PartDefinition child = new PartDefinition(cubes.getCubes(), partPose);
/* 28 */     return addOrReplaceChild(name, child);
/*    */   }
/*    */   
/*    */   public PartDefinition addOrReplaceChild(String name, PartDefinition child) {
/* 32 */     PartDefinition previous = this.children.put(name, child);
/* 33 */     if (previous != null)
/* 34 */       child.children.putAll(previous.children); 
/* 36 */     return child;
/*    */   }
/*    */   
/*    */   public PartDefinition clearRecursively() {
/* 40 */     for (String name : this.children.keySet())
/* 41 */       clearChild(name).clearRecursively(); 
/* 43 */     return this;
/*    */   }
/*    */   
/*    */   public PartDefinition clearChild(String name) {
/* 47 */     PartDefinition child = this.children.get(name);
/* 48 */     if (child == null)
/* 49 */       throw new IllegalArgumentException("No child with name: " + name); 
/* 52 */     return addOrReplaceChild(name, CubeListBuilder.create(), child.partPose);
/*    */   }
/*    */   
/*    */   public void retainPartsAndChildren(Set<String> parts) {
/* 56 */     for (Map.Entry<String, PartDefinition> entry : this.children.entrySet()) {
/* 57 */       PartDefinition child = entry.getValue();
/* 58 */       if (!parts.contains(entry.getKey()))
/* 59 */         addOrReplaceChild(entry.getKey(), CubeListBuilder.create(), child.partPose).retainPartsAndChildren(parts); 
/*    */     } 
/*    */   }
/*    */   
/*    */   public void retainExactParts(Set<String> parts) {
/* 65 */     for (Map.Entry<String, PartDefinition> entry : this.children.entrySet()) {
/* 66 */       PartDefinition child = entry.getValue();
/* 67 */       if (parts.contains(entry.getKey())) {
/* 68 */         child.clearRecursively();
/*    */         continue;
/*    */       } 
/* 70 */       addOrReplaceChild(entry.getKey(), CubeListBuilder.create(), child.partPose).retainExactParts(parts);
/*    */     } 
/*    */   }
/*    */   
/*    */   public ModelPart bake(int texScaleX, int texScaleY) {
/* 76 */     Object2ObjectArrayMap<String, ModelPart> bakedChildren = (Object2ObjectArrayMap<String, ModelPart>)this.children.entrySet().stream().collect(Collectors.toMap(Map.Entry::getKey, e -> ((PartDefinition)e.getValue()).bake(texScaleX, texScaleY), (a, b) -> a, Object2ObjectArrayMap::new));
/* 77 */     List<ModelPart.Cube> bakedCubes = this.cubes.stream().map(definition -> definition.bake(texScaleX, texScaleY)).toList();
/* 79 */     ModelPart result = new ModelPart(bakedCubes, (Map)bakedChildren);
/* 80 */     result.setInitialPose(this.partPose);
/* 81 */     result.loadPose(this.partPose);
/* 82 */     return result;
/*    */   }
/*    */   
/*    */   public PartDefinition getChild(String name) {
/* 86 */     return this.children.get(name);
/*    */   }
/*    */   
/*    */   public Set<Map.Entry<String, PartDefinition>> getChildren() {
/* 90 */     return this.children.entrySet();
/*    */   }
/*    */   
/*    */   public PartDefinition transformed(UnaryOperator<PartPose> function) {
/* 94 */     PartDefinition newPart = new PartDefinition(this.cubes, function.apply(this.partPose));
/* 95 */     newPart.children.putAll(this.children);
/* 96 */     return newPart;
/*    */   }
/*    */ }


/* Location:              C:\Users\karol\AppData\Roaming\ModrinthApp\meta\versions\26.2-0.19.5\26.2-0.19.5.jar!\net\minecraft\client\model\geom\builders\PartDefinition.class
 * Java compiler version: 25 (69.0)
 * JD-Core Version:       1.1.3
 */