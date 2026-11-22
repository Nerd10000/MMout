/*    */ package net.minecraft.client.resources.model;
/*    */ 
/*    */ import com.google.common.collect.Sets;
/*    */ import it.unimi.dsi.fastutil.objects.Object2IntMap;
/*    */ import it.unimi.dsi.fastutil.objects.Object2IntOpenHashMap;
/*    */ import java.util.HashMap;
/*    */ import java.util.Iterator;
/*    */ import java.util.List;
/*    */ import java.util.Map;
/*    */ import java.util.Set;
/*    */ import net.minecraft.client.color.block.BlockColors;
/*    */ import net.minecraft.client.renderer.block.dispatch.BlockStateModel;
/*    */ import net.minecraft.world.level.block.Block;
/*    */ import net.minecraft.world.level.block.RenderShape;
/*    */ import net.minecraft.world.level.block.state.BlockState;
/*    */ import net.minecraft.world.level.block.state.properties.Property;
/*    */ 
/*    */ public class ModelGroupCollector {
/*    */   static final int SINGLETON_MODEL_GROUP = -1;
/*    */   
/*    */   private static final int INVISIBLE_MODEL_GROUP = 0;
/*    */   
/*    */   public static Object2IntMap<BlockState> build(BlockColors blockColors, BlockStateModelLoader.LoadedModels input) {
/* 26 */     Map<Block, List<Property<?>>> coloringPropertiesCache = new HashMap<>();
/* 27 */     Map<GroupKey, Set<BlockState>> modelGroups = new HashMap<>();
/* 29 */     input.models().forEach((state, loadedModel) -> {
/*    */           List<Property<?>> coloringProperties = coloringPropertiesCache.computeIfAbsent(state.getBlock(), ());
/*    */           GroupKey key = GroupKey.create(state, loadedModel, coloringProperties);
/*    */           ((Set<BlockState>)modelGroups.computeIfAbsent(key, ())).add(state);
/*    */         });
/* 38 */     int nextModelGroup = 1;
/* 39 */     Object2IntOpenHashMap object2IntOpenHashMap = new Object2IntOpenHashMap();
/* 40 */     object2IntOpenHashMap.defaultReturnValue(-1);
/* 42 */     for (Set<BlockState> states : modelGroups.values()) {
/* 43 */       Iterator<BlockState> it = states.iterator();
/* 44 */       while (it.hasNext()) {
/* 45 */         BlockState state = it.next();
/* 46 */         if (state.getRenderShape() != RenderShape.MODEL) {
/* 47 */           it.remove();
/* 48 */           object2IntOpenHashMap.put(state, 0);
/*    */         } 
/*    */       } 
/* 52 */       if (states.size() > 1) {
/* 53 */         int modelGroup = nextModelGroup++;
/* 54 */         states.forEach(blockState -> result.put(blockState, modelGroup));
/*    */       } 
/*    */     } 
/* 58 */     return (Object2IntMap<BlockState>)object2IntOpenHashMap;
/*    */   }
/*    */   
/*    */   private static final class GroupKey extends Record {
/*    */     private final Object equalityGroup;
/*    */     
/*    */     private final List<Object> coloringValues;
/*    */     
/*    */     private GroupKey(Object equalityGroup, List<Object> coloringValues) {
/* 61 */       this.equalityGroup = equalityGroup;
/* 61 */       this.coloringValues = coloringValues;
/*    */     }
/*    */     
/*    */     public final String toString() {
/*    */       // Byte code:
/*    */       //   0: aload_0
/*    */       //   1: <illegal opcode> toString : (Lnet/minecraft/client/resources/model/ModelGroupCollector$GroupKey;)Ljava/lang/String;
/*    */       //   6: areturn
/*    */       // Line number table:
/*    */       //   Java source line number -> byte code offset
/*    */       //   #61	-> 0
/*    */       // Local variable table:
/*    */       //   start	length	slot	name	descriptor
/*    */       //   0	7	0	this	Lnet/minecraft/client/resources/model/ModelGroupCollector$GroupKey;
/*    */     }
/*    */     
/*    */     public final int hashCode() {
/*    */       // Byte code:
/*    */       //   0: aload_0
/*    */       //   1: <illegal opcode> hashCode : (Lnet/minecraft/client/resources/model/ModelGroupCollector$GroupKey;)I
/*    */       //   6: ireturn
/*    */       // Line number table:
/*    */       //   Java source line number -> byte code offset
/*    */       //   #61	-> 0
/*    */       // Local variable table:
/*    */       //   start	length	slot	name	descriptor
/*    */       //   0	7	0	this	Lnet/minecraft/client/resources/model/ModelGroupCollector$GroupKey;
/*    */     }
/*    */     
/*    */     public final boolean equals(Object o) {
/*    */       // Byte code:
/*    */       //   0: aload_0
/*    */       //   1: aload_1
/*    */       //   2: <illegal opcode> equals : (Lnet/minecraft/client/resources/model/ModelGroupCollector$GroupKey;Ljava/lang/Object;)Z
/*    */       //   7: ireturn
/*    */       // Line number table:
/*    */       //   Java source line number -> byte code offset
/*    */       //   #61	-> 0
/*    */       // Local variable table:
/*    */       //   start	length	slot	name	descriptor
/*    */       //   0	8	0	this	Lnet/minecraft/client/resources/model/ModelGroupCollector$GroupKey;
/*    */       //   0	8	1	o	Ljava/lang/Object;
/*    */     }
/*    */     
/*    */     public Object equalityGroup() {
/* 61 */       return this.equalityGroup;
/*    */     }
/*    */     
/*    */     public List<Object> coloringValues() {
/* 61 */       return this.coloringValues;
/*    */     }
/*    */     
/*    */     public static GroupKey create(BlockState state, BlockStateModel.UnbakedRoot model, List<Property<?>> coloringProperties) {
/* 63 */       List<Object> coloringValues = getColoringValues(state, coloringProperties);
/* 64 */       Object equalityGroup = model.visualEqualityGroup(state);
/* 65 */       return new GroupKey(equalityGroup, coloringValues);
/*    */     }
/*    */     
/*    */     private static List<Object> getColoringValues(BlockState state, List<Property<?>> coloringProperties) {
/* 69 */       Object[] coloringValues = new Object[coloringProperties.size()];
/* 70 */       for (int i = 0; i < coloringProperties.size(); i++)
/* 71 */         coloringValues[i] = state.getValue(coloringProperties.get(i)); 
/* 73 */       return List.of(coloringValues);
/*    */     }
/*    */   }
/*    */ }


/* Location:              C:\Users\karol\AppData\Roaming\ModrinthApp\meta\versions\26.2-0.19.5\26.2-0.19.5.jar!\net\minecraft\client\resources\model\ModelGroupCollector.class
 * Java compiler version: 25 (69.0)
 * JD-Core Version:       1.1.3
 */