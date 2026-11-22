/*    */ package net.minecraft.client.renderer.item;
/*    */ 
/*    */ import com.mojang.serialization.MapCodec;
/*    */ import java.util.function.Supplier;
/*    */ import net.minecraft.client.multiplayer.ClientLevel;
/*    */ import net.minecraft.client.resources.model.ResolvableModel;
/*    */ import net.minecraft.world.entity.ItemOwner;
/*    */ import net.minecraft.world.item.ItemDisplayContext;
/*    */ import net.minecraft.world.item.ItemStack;
/*    */ import org.joml.Matrix4fc;
/*    */ 
/*    */ public class EmptyModel implements ItemModel {
/* 12 */   public static final ItemModel INSTANCE = new EmptyModel();
/*    */   
/*    */   public void update(ItemStackRenderState output, ItemStack item, ItemModelResolver resolver, ItemDisplayContext displayContext, ClientLevel level, ItemOwner owner, int seed) {
/* 16 */     output.appendModelIdentityElement(this);
/*    */   }
/*    */   
/*    */   public static final class Unbaked extends Record implements ItemModel.Unbaked {
/*    */     public final String toString() {
/*    */       // Byte code:
/*    */       //   0: aload_0
/*    */       //   1: <illegal opcode> toString : (Lnet/minecraft/client/renderer/item/EmptyModel$Unbaked;)Ljava/lang/String;
/*    */       //   6: areturn
/*    */       // Line number table:
/*    */       //   Java source line number -> byte code offset
/*    */       //   #19	-> 0
/*    */       // Local variable table:
/*    */       //   start	length	slot	name	descriptor
/*    */       //   0	7	0	this	Lnet/minecraft/client/renderer/item/EmptyModel$Unbaked;
/*    */     }
/*    */     
/*    */     public final int hashCode() {
/*    */       // Byte code:
/*    */       //   0: aload_0
/*    */       //   1: <illegal opcode> hashCode : (Lnet/minecraft/client/renderer/item/EmptyModel$Unbaked;)I
/*    */       //   6: ireturn
/*    */       // Line number table:
/*    */       //   Java source line number -> byte code offset
/*    */       //   #19	-> 0
/*    */       // Local variable table:
/*    */       //   start	length	slot	name	descriptor
/*    */       //   0	7	0	this	Lnet/minecraft/client/renderer/item/EmptyModel$Unbaked;
/*    */     }
/*    */     
/*    */     public final boolean equals(Object o) {
/*    */       // Byte code:
/*    */       //   0: aload_0
/*    */       //   1: aload_1
/*    */       //   2: <illegal opcode> equals : (Lnet/minecraft/client/renderer/item/EmptyModel$Unbaked;Ljava/lang/Object;)Z
/*    */       //   7: ireturn
/*    */       // Line number table:
/*    */       //   Java source line number -> byte code offset
/*    */       //   #19	-> 0
/*    */       // Local variable table:
/*    */       //   start	length	slot	name	descriptor
/*    */       //   0	8	0	this	Lnet/minecraft/client/renderer/item/EmptyModel$Unbaked;
/*    */       //   0	8	1	o	Ljava/lang/Object;
/*    */     }
/*    */     
/* 20 */     public static final MapCodec<Unbaked> MAP_CODEC = MapCodec.unit(Unbaked::new);
/*    */     
/*    */     public void resolveDependencies(ResolvableModel.Resolver resolver) {}
/*    */     
/*    */     public ItemModel bake(ItemModel.BakingContext context, Matrix4fc transformation) {
/* 28 */       return EmptyModel.INSTANCE;
/*    */     }
/*    */     
/*    */     public MapCodec<Unbaked> type() {
/* 33 */       return MAP_CODEC;
/*    */     }
/*    */   }
/*    */ }


/* Location:              C:\Users\karol\AppData\Roaming\ModrinthApp\meta\versions\26.2-0.19.5\26.2-0.19.5.jar!\net\minecraft\client\renderer\item\EmptyModel.class
 * Java compiler version: 25 (69.0)
 * JD-Core Version:       1.1.3
 */