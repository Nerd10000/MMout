/*    */ package net.minecraft.util.datafix.fixes;
/*    */ 
/*    */ import com.google.common.collect.ImmutableMap;
/*    */ import com.mojang.datafixers.schemas.Schema;
/*    */ import java.util.Map;
/*    */ import java.util.Objects;
/*    */ 
/*    */ public class EntityRavagerRenameFix extends SimplestEntityRenameFix {
/* 10 */   public static final Map<String, String> RENAMED_IDS = (Map<String, String>)ImmutableMap.builder()
/* 11 */     .put("minecraft:illager_beast_spawn_egg", "minecraft:ravager_spawn_egg")
/* 12 */     .build();
/*    */   
/*    */   public EntityRavagerRenameFix(Schema outputSchema, boolean changesType) {
/* 15 */     super("EntityRavagerRenameFix", outputSchema, changesType);
/*    */   }
/*    */   
/*    */   protected String rename(String name) {
/* 20 */     return Objects.equals("minecraft:illager_beast", name) ? "minecraft:ravager" : name;
/*    */   }
/*    */ }


/* Location:              C:\Users\karol\AppData\Roaming\ModrinthApp\meta\versions\26.2-0.19.5\26.2-0.19.5.jar!\net\minecraf\\util\datafix\fixes\EntityRavagerRenameFix.class
 * Java compiler version: 25 (69.0)
 * JD-Core Version:       1.1.3
 */