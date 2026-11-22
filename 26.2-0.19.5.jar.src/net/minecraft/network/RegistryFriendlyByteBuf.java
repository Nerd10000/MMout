/*    */ package net.minecraft.network;
/*    */ 
/*    */ import io.netty.buffer.ByteBuf;
/*    */ import java.util.function.Function;
/*    */ import net.minecraft.core.RegistryAccess;
/*    */ 
/*    */ public class RegistryFriendlyByteBuf extends FriendlyByteBuf {
/*    */   private final RegistryAccess registryAccess;
/*    */   
/*    */   public RegistryFriendlyByteBuf(ByteBuf source, RegistryAccess registryAccess) {
/* 12 */     super(source);
/* 13 */     this.registryAccess = registryAccess;
/*    */   }
/*    */   
/*    */   public RegistryAccess registryAccess() {
/* 17 */     return this.registryAccess;
/*    */   }
/*    */   
/*    */   public static Function<ByteBuf, RegistryFriendlyByteBuf> decorator(RegistryAccess registryAccess) {
/* 21 */     return buf -> new RegistryFriendlyByteBuf(buf, registryAccess);
/*    */   }
/*    */ }


/* Location:              C:\Users\karol\AppData\Roaming\ModrinthApp\meta\versions\26.2-0.19.5\26.2-0.19.5.jar!\net\minecraft\network\RegistryFriendlyByteBuf.class
 * Java compiler version: 25 (69.0)
 * JD-Core Version:       1.1.3
 */