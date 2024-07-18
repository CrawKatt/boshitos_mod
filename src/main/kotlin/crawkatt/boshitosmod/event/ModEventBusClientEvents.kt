package crawkatt.boshitosmod.event

import crawkatt.boshitosmod.BoshitosMod
import crawkatt.boshitosmod.entity.ModEntities
import crawkatt.boshitosmod.entity.client.MaiNoboshiModel
import crawkatt.boshitosmod.entity.client.MaiNoboshiRenderer
import net.minecraftforge.client.event.EntityRenderersEvent
import net.minecraftforge.client.event.EntityRenderersEvent.RegisterLayerDefinitions
import net.minecraftforge.eventbus.api.SubscribeEvent
import net.minecraftforge.fml.common.Mod

@Mod.EventBusSubscriber(modid = BoshitosMod.MOD_ID, bus = Mod.EventBusSubscriber.Bus.MOD)
object ModEventBusClientEvents {
    @SubscribeEvent
    fun registerRenderers(event: EntityRenderersEvent.RegisterRenderers) {
        event.registerEntityRenderer(ModEntities.MAI_NOBOSHI.get(), ::MaiNoboshiRenderer)
    }

    @SubscribeEvent
    fun registerLayerDefinition(event: RegisterLayerDefinitions) {
        event.registerLayerDefinition(MaiNoboshiModel.LAYER_LOCATION, MaiNoboshiModel::createBodyLayer)
    }
}