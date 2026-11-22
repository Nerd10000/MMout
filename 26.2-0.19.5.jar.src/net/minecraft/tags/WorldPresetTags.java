/*    */ package net.minecraft.tags;
/*    */ 
/*    */ import net.minecraft.core.registries.Registries;
/*    */ import net.minecraft.resources.Identifier;
/*    */ import net.minecraft.world.level.levelgen.presets.WorldPreset;
/*    */ 
/*    */ public class WorldPresetTags {
/* 12 */   public static final TagKey<WorldPreset> NORMAL = create("normal");
/*    */   
/* 14 */   public static final TagKey<WorldPreset> EXTENDED = create("extended");
/*    */   
/*    */   private static TagKey<WorldPreset> create(String name) {
/* 17 */     return TagKey.create(Registries.WORLD_PRESET, Identifier.withDefaultNamespace(name));
/*    */   }
/*    */ }


/* Location:              C:\Users\karol\AppData\Roaming\ModrinthApp\meta\versions\26.2-0.19.5\26.2-0.19.5.jar!\net\minecraft\tags\WorldPresetTags.class
 * Java compiler version: 25 (69.0)
 * JD-Core Version:       1.1.3
 */