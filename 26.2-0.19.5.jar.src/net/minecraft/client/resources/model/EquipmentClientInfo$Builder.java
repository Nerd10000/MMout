/*    */ package net.minecraft.client.resources.model;
/*    */ 
/*    */ import com.google.common.collect.ImmutableMap;
/*    */ import java.util.ArrayList;
/*    */ import java.util.Collection;
/*    */ import java.util.Collections;
/*    */ import java.util.EnumMap;
/*    */ import java.util.List;
/*    */ import java.util.Map;
/*    */ import net.minecraft.resources.Identifier;
/*    */ 
/*    */ public class Builder {
/* 71 */   private final Map<EquipmentClientInfo.LayerType, List<EquipmentClientInfo.Layer>> layersByType = new EnumMap<>(EquipmentClientInfo.LayerType.class);
/*    */   
/*    */   public Builder addHumanoidLayers(Identifier textureId) {
/* 77 */     return addHumanoidLayers(textureId, false);
/*    */   }
/*    */   
/*    */   public Builder addHumanoidLayers(Identifier textureId, boolean dyeable) {
/* 81 */     addLayers(EquipmentClientInfo.LayerType.HUMANOID_LEGGINGS, new EquipmentClientInfo.Layer[] { EquipmentClientInfo.Layer.leatherDyeable(textureId, dyeable) });
/* 82 */     addMainHumanoidLayer(textureId, dyeable);
/* 83 */     return this;
/*    */   }
/*    */   
/*    */   public Builder addMainHumanoidLayer(Identifier textureId, boolean dyeable) {
/* 87 */     addLayers(EquipmentClientInfo.LayerType.HUMANOID, new EquipmentClientInfo.Layer[] { EquipmentClientInfo.Layer.leatherDyeable(textureId, dyeable) });
/* 88 */     addLayers(EquipmentClientInfo.LayerType.HUMANOID_BABY, new EquipmentClientInfo.Layer[] { EquipmentClientInfo.Layer.leatherDyeable(textureId, dyeable) });
/* 89 */     return this;
/*    */   }
/*    */   
/*    */   public Builder addLayers(EquipmentClientInfo.LayerType type, EquipmentClientInfo.Layer... layers) {
/* 93 */     Collections.addAll(this.layersByType.computeIfAbsent(type, t -> new ArrayList()), layers);
/* 94 */     return this;
/*    */   }
/*    */   
/*    */   public EquipmentClientInfo build() {
/* 98 */     return new EquipmentClientInfo((Map<EquipmentClientInfo.LayerType, List<EquipmentClientInfo.Layer>>)this.layersByType
/* 99 */         .entrySet().stream().collect(ImmutableMap.toImmutableMap(Map.Entry::getKey, entry -> List.copyOf((Collection)entry.getValue()))));
/*    */   }
/*    */ }


/* Location:              C:\Users\karol\AppData\Roaming\ModrinthApp\meta\versions\26.2-0.19.5\26.2-0.19.5.jar!\net\minecraft\client\resources\model\EquipmentClientInfo$Builder.class
 * Java compiler version: 25 (69.0)
 * JD-Core Version:       1.1.3
 */