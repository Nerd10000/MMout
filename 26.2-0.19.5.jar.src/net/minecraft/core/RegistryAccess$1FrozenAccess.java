/*     */ package net.minecraft.core;
/*     */ 
/*     */ import java.util.stream.Stream;
/*     */ 
/*     */ class FrozenAccess extends RegistryAccess.ImmutableRegistryAccess implements RegistryAccess.Frozen {
/*     */   protected FrozenAccess(RegistryAccess this$0, Stream<RegistryAccess.RegistryEntry<?>> entries) {
/* 104 */     super(entries);
/*     */   }
/*     */ }


/* Location:              C:\Users\karol\AppData\Roaming\ModrinthApp\meta\versions\26.2-0.19.5\26.2-0.19.5.jar!\net\minecraft\core\RegistryAccess$1FrozenAccess.class
 * Java compiler version: 25 (69.0)
 * JD-Core Version:       1.1.3
 */