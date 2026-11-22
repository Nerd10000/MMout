/*     */ package net.minecraft.world.level.storage.loot.functions;
/*     */ 
/*     */ import com.mojang.logging.LogUtils;
/*     */ import com.mojang.serialization.MapCodec;
/*     */ import java.util.List;
/*     */ import java.util.stream.Stream;
/*     */ import org.slf4j.Logger;
/*     */ 
/*     */ public class Append implements ListOperation {
/* 159 */   private static final Logger LOGGER = LogUtils.getLogger();
/*     */   
/* 161 */   public static final Append INSTANCE = new Append();
/*     */   
/* 162 */   public static final MapCodec<Append> MAP_CODEC = MapCodec.unit(() -> INSTANCE);
/*     */   
/*     */   public ListOperation.Type mode() {
/* 169 */     return ListOperation.Type.APPEND;
/*     */   }
/*     */   
/*     */   public <T> List<T> apply(List<T> original, List<T> replacement, int maxSize) {
/* 174 */     if (original.size() + replacement.size() > maxSize) {
/* 175 */       LOGGER.error("Contents overflow in section append");
/* 176 */       return original;
/*     */     } 
/* 178 */     return Stream.<T>concat(original.stream(), replacement.stream()).toList();
/*     */   }
/*     */ }


/* Location:              C:\Users\karol\AppData\Roaming\ModrinthApp\meta\versions\26.2-0.19.5\26.2-0.19.5.jar!\net\minecraft\world\level\storage\loot\functions\ListOperation$Append.class
 * Java compiler version: 25 (69.0)
 * JD-Core Version:       1.1.3
 */