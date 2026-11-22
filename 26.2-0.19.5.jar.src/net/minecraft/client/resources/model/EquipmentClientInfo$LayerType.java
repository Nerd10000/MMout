/*     */ package net.minecraft.client.resources.model;
/*     */ 
/*     */ import com.mojang.serialization.Codec;
/*     */ import net.minecraft.util.StringRepresentable;
/*     */ 
/*     */ public enum LayerType implements StringRepresentable {
/* 108 */   HUMANOID("humanoid"),
/* 109 */   HUMANOID_LEGGINGS("humanoid_leggings"),
/* 110 */   HUMANOID_BABY("humanoid_baby"),
/* 111 */   WINGS("wings"),
/* 112 */   WOLF_BODY("wolf_body"),
/* 113 */   HORSE_BODY("horse_body"),
/* 114 */   LLAMA_BODY("llama_body"),
/* 115 */   PIG_SADDLE("pig_saddle"),
/* 116 */   STRIDER_SADDLE("strider_saddle"),
/* 117 */   CAMEL_SADDLE("camel_saddle"),
/* 118 */   CAMEL_HUSK_SADDLE("camel_husk_saddle"),
/* 119 */   HORSE_SADDLE("horse_saddle"),
/* 120 */   DONKEY_SADDLE("donkey_saddle"),
/* 121 */   MULE_SADDLE("mule_saddle"),
/* 122 */   ZOMBIE_HORSE_SADDLE("zombie_horse_saddle"),
/* 123 */   SKELETON_HORSE_SADDLE("skeleton_horse_saddle"),
/* 124 */   HAPPY_GHAST_BODY("happy_ghast_body"),
/* 125 */   NAUTILUS_SADDLE("nautilus_saddle"),
/* 126 */   NAUTILUS_BODY("nautilus_body");
/*     */   
/*     */   public static final Codec<LayerType> CODEC;
/*     */   
/*     */   private final String id;
/*     */   
/*     */   static {
/* 129 */     CODEC = (Codec<LayerType>)StringRepresentable.fromEnum(LayerType::values);
/*     */   }
/*     */   
/*     */   LayerType(String id) {
/* 134 */     this.id = id;
/*     */   }
/*     */   
/*     */   public String getSerializedName() {
/* 139 */     return this.id;
/*     */   }
/*     */   
/*     */   public String trimAssetPrefix() {
/* 143 */     return "trims/entity/" + this.id;
/*     */   }
/*     */ }


/* Location:              C:\Users\karol\AppData\Roaming\ModrinthApp\meta\versions\26.2-0.19.5\26.2-0.19.5.jar!\net\minecraft\client\resources\model\EquipmentClientInfo$LayerType.class
 * Java compiler version: 25 (69.0)
 * JD-Core Version:       1.1.3
 */