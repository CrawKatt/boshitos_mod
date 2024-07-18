package crawkatt.boshitosmod.entity

import crawkatt.boshitosmod.BoshitosMod
import crawkatt.boshitosmod.entity.custom.BoshitoEntity
import crawkatt.boshitosmod.entity.custom.MaiNoboshiEntity
import net.minecraft.world.entity.EntityType
import net.minecraft.world.entity.MobCategory
import net.minecraftforge.registries.DeferredRegister
import net.minecraftforge.registries.ForgeRegistries
import net.minecraftforge.registries.RegistryObject

object ModEntities {
    val ENTITY_TYPES: DeferredRegister<EntityType<*>> = DeferredRegister.create(ForgeRegistries.ENTITY_TYPES, BoshitosMod.MOD_ID)

    val BOSHITO: RegistryObject<EntityType<BoshitoEntity>> = ENTITY_TYPES.register("boshito") {
        EntityType.Builder.of(::BoshitoEntity, MobCategory.CREATURE).sized(0.6f, 1.95f).build("boshito")
    }

    val MAI_NOBOSHI: RegistryObject<EntityType<MaiNoboshiEntity>> = ENTITY_TYPES.register("mai_noboshi") {
        EntityType.Builder.of(::MaiNoboshiEntity, MobCategory.MONSTER)
            .sized(0.6F, 1.95F)
            .build("mai_noboshi")
    }
}