/*    */ package net.minecraft.world.level.storage.loot.functions;
/*    */ 
/*    */ import com.mojang.serialization.MapCodec;
/*    */ import java.util.List;
/*    */ 
/*    */ public class ReplaceAll implements ListOperation {
/* 68 */   public static final ReplaceAll INSTANCE = new ReplaceAll();
/*    */   
/* 69 */   public static final MapCodec<ReplaceAll> MAP_CODEC = MapCodec.unit(() -> INSTANCE);
/*    */   
/*    */   public ListOperation.Type mode() {
/* 76 */     return ListOperation.Type.REPLACE_ALL;
/*    */   }
/*    */   
/*    */   public <T> List<T> apply(List<T> original, List<T> replacement, int maxSize) {
/* 81 */     return replacement;
/*    */   }
/*    */ }


/* Location:              C:\Users\karol\AppData\Roaming\ModrinthApp\meta\versions\26.2-0.19.5\26.2-0.19.5.jar!\net\minecraft\world\level\storage\loot\functions\ListOperation$ReplaceAll.class
 * Java compiler version: 25 (69.0)
 * JD-Core Version:       1.1.3
 */