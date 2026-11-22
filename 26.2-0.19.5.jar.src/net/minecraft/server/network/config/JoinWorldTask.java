/*    */ package net.minecraft.server.network.config;
/*    */ 
/*    */ import java.util.function.Consumer;
/*    */ import net.minecraft.network.protocol.Packet;
/*    */ import net.minecraft.network.protocol.configuration.ClientboundFinishConfigurationPacket;
/*    */ import net.minecraft.server.network.ConfigurationTask;
/*    */ 
/*    */ public class JoinWorldTask implements ConfigurationTask {
/* 10 */   public static final ConfigurationTask.Type TYPE = new ConfigurationTask.Type("join_world");
/*    */   
/*    */   public void start(Consumer<Packet<?>> connection) {
/* 14 */     connection.accept(ClientboundFinishConfigurationPacket.INSTANCE);
/*    */   }
/*    */   
/*    */   public ConfigurationTask.Type type() {
/* 19 */     return TYPE;
/*    */   }
/*    */ }


/* Location:              C:\Users\karol\AppData\Roaming\ModrinthApp\meta\versions\26.2-0.19.5\26.2-0.19.5.jar!\net\minecraft\server\network\config\JoinWorldTask.class
 * Java compiler version: 25 (69.0)
 * JD-Core Version:       1.1.3
 */