package crawkatt.boshitosmod.event

import crawkatt.boshitosmod.BoshitosMod
import crawkatt.boshitosmod.entity.ModEntities
import crawkatt.boshitosmod.entity.custom.BoshitoEntity
import crawkatt.boshitosmod.entity.custom.MaiNoboshiEntity
import net.minecraftforge.event.entity.EntityAttributeCreationEvent
import net.minecraftforge.eventbus.api.SubscribeEvent
import net.minecraftforge.fml.common.Mod

// NO USAR CLASS EN EL EVENTBUS
@Mod.EventBusSubscriber(modid = BoshitosMod.MOD_ID, bus = Mod.EventBusSubscriber.Bus.MOD)
object ModEventBusEvents {

    @SubscribeEvent
    fun registerAttributes(event: EntityAttributeCreationEvent) {
        event.put(ModEntities.BOSHITO.get(), BoshitoEntity.createAttributes())
        event.put(ModEntities.MAI_NOBOSHI.get(), MaiNoboshiEntity.createAttributes())
    }
}