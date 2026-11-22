/*    */ package net.minecraft.commands.arguments;
/*    */ 
/*    */ import com.mojang.brigadier.Message;
/*    */ import com.mojang.brigadier.context.CommandContext;
/*    */ import com.mojang.brigadier.exceptions.CommandSyntaxException;
/*    */ import com.mojang.brigadier.exceptions.SimpleCommandExceptionType;
/*    */ import net.minecraft.commands.CommandSourceStack;
/*    */ import net.minecraft.commands.arguments.selector.EntitySelector;
/*    */ import net.minecraft.network.chat.Component;
/*    */ import net.minecraft.world.entity.Entity;
/*    */ import net.minecraft.world.waypoints.WaypointTransmitter;
/*    */ 
/*    */ public class WaypointArgument {
/* 13 */   public static final SimpleCommandExceptionType ERROR_NOT_A_WAYPOINT = new SimpleCommandExceptionType((Message)Component.translatable("argument.waypoint.invalid"));
/*    */   
/*    */   public static WaypointTransmitter getWaypoint(CommandContext<CommandSourceStack> context, String name) throws CommandSyntaxException {
/* 16 */     Entity singleEntity = ((EntitySelector)context.getArgument(name, EntitySelector.class)).findSingleEntity((CommandSourceStack)context.getSource());
/* 17 */     if (singleEntity instanceof WaypointTransmitter) {
/* 17 */       WaypointTransmitter waypointTransmitter = (WaypointTransmitter)singleEntity;
/* 18 */       return waypointTransmitter;
/*    */     } 
/* 20 */     throw ERROR_NOT_A_WAYPOINT.create();
/*    */   }
/*    */ }


/* Location:              C:\Users\karol\AppData\Roaming\ModrinthApp\meta\versions\26.2-0.19.5\26.2-0.19.5.jar!\net\minecraft\commands\arguments\WaypointArgument.class
 * Java compiler version: 25 (69.0)
 * JD-Core Version:       1.1.3
 */