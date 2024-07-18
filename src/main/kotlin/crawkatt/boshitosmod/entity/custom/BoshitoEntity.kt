package crawkatt.boshitosmod.entity.custom

import crawkatt.boshitosmod.entity.ModEntities
import net.minecraft.nbt.CompoundTag
import net.minecraft.network.syncher.EntityDataAccessor
import net.minecraft.network.syncher.EntityDataSerializers
import net.minecraft.network.syncher.SynchedEntityData
import net.minecraft.server.level.ServerLevel
import net.minecraft.util.valueproviders.UniformInt
import net.minecraft.world.InteractionHand
import net.minecraft.world.InteractionResult
import net.minecraft.world.entity.*
import net.minecraft.world.entity.ai.attributes.AttributeSupplier
import net.minecraft.world.entity.ai.attributes.Attributes
import net.minecraft.world.entity.ai.goal.*
import net.minecraft.world.entity.ai.goal.target.*
import net.minecraft.world.entity.animal.Turtle
import net.minecraft.world.entity.animal.horse.AbstractHorse
import net.minecraft.world.entity.item.ItemEntity
import net.minecraft.world.entity.monster.AbstractSkeleton
import net.minecraft.world.entity.monster.Ghast
import net.minecraft.world.entity.player.Player
import net.minecraft.world.item.Item
import net.minecraft.world.item.ItemStack
import net.minecraft.world.item.Items
import net.minecraft.world.level.Level
import net.minecraftforge.event.ForgeEventFactory
import software.bernie.geckolib.animatable.GeoEntity
import software.bernie.geckolib.core.animatable.instance.AnimatableInstanceCache
import software.bernie.geckolib.core.animation.AnimatableManager
import software.bernie.geckolib.util.GeckoLibUtil
import java.util.*


class BoshitoEntity(entityType: EntityType<out TamableAnimal>, level: Level) : TamableAnimal(entityType, level), NeutralMob, GeoEntity {
    private var persistentAngerTarget: UUID? = null

    // `companion object` es el equivalente a `static` en Java, sirve para definir constantes y métodos estáticos
    companion object {
        private val PERSISTENT_ANGER_TIME: UniformInt = UniformInt.of(20, 39)
        private val DATA_REMAINING_ANGER_TIME: EntityDataAccessor<Int> = SynchedEntityData.defineId(BoshitoEntity::class.java, EntityDataSerializers.INT)
        private val SITTING: EntityDataAccessor<Boolean> = SynchedEntityData.defineId(BoshitoEntity::class.java, EntityDataSerializers.BOOLEAN)

        fun createAttributes(): AttributeSupplier {
            return createLivingAttributes()
                .add(Attributes.MAX_HEALTH, 20.0)
                .add(Attributes.FOLLOW_RANGE, 24.0)
                .add(Attributes.MOVEMENT_SPEED, 0.15)
                .add(Attributes.ARMOR_TOUGHNESS, 0.1)
                .add(Attributes.ATTACK_KNOCKBACK, 2.0)
                .add(Attributes.ATTACK_DAMAGE, 2.0)
                .build()
        }
    }

    private val cache: AnimatableInstanceCache = GeckoLibUtil.createInstanceCache(this)

    // Inicializa la entidad y establece una condición para equipar una espada de diamante o un arco si se encuentra cerca
    override fun tick() {
        super.tick()

        if (!this.isAlive) {
            return
        }

        val itemsNearby = this.level().getEntitiesOfClass(ItemEntity::class.java, this.boundingBox.inflate(1.0))
        for (itemEntity in itemsNearby) {
            val item = itemEntity.item.item
            if (item == Items.DIAMOND_SWORD || item == Items.BOW) {
                this.take(itemEntity, itemEntity.item.count)

                // Equipar la espada
                this.setItemSlot(EquipmentSlot.MAINHAND, itemEntity.item)
                itemEntity.discard()
                break
            }
        }
    }

    // Define el comportamiento de la IA del Boshito
    override fun registerGoals() {
        goalSelector.addGoal(1, FloatGoal(this))
        goalSelector.addGoal(2, SitWhenOrderedToGoal(this))
        goalSelector.addGoal(4, LeapAtTargetGoal(this, 0.4f))
        goalSelector.addGoal(5, MeleeAttackGoal(this, 1.0, true))
        goalSelector.addGoal(6, FollowOwnerGoal(this, 1.0, 10.0f, 2.0f, false))
        goalSelector.addGoal(7, BreedGoal(this, 1.0))
        goalSelector.addGoal(8, WaterAvoidingRandomStrollGoal(this, 1.0))
        goalSelector.addGoal(10, LookAtPlayerGoal(this, Player::class.java, 8.0f))
        goalSelector.addGoal(10, RandomLookAroundGoal(this))
        targetSelector.addGoal(1, OwnerHurtByTargetGoal(this))
        targetSelector.addGoal(2, OwnerHurtTargetGoal(this))
        targetSelector.addGoal(3, HurtByTargetGoal(this).setAlertOthers())
        targetSelector.addGoal(6, NonTameRandomTargetGoal(this, Turtle::class.java, false, Turtle.BABY_ON_LAND_SELECTOR))
        targetSelector.addGoal(7, NearestAttackableTargetGoal(this, AbstractSkeleton::class.java, false))
        targetSelector.addGoal(8, ResetUniversalAngerTargetGoal(this, true))
    }

    // el Operador `?` en `AgeableMob?` es el equivalente a `@Nullable` en Java
    override fun getBreedOffspring(pLevel: ServerLevel, pOtherParent: AgeableMob): AgeableMob? {
        return ModEntities.BOSHITO.get().create(pLevel)
    }

    override fun isFood(pStack: ItemStack): Boolean {
        return pStack.`is`(Items.CARROT)
    }

    override fun getRemainingPersistentAngerTime(): Int {
        return this.entityData.get(DATA_REMAINING_ANGER_TIME)
    }

    override fun setRemainingPersistentAngerTime(i: Int) {
        this.entityData.set(DATA_REMAINING_ANGER_TIME, i)
    }

    override fun getPersistentAngerTarget(): UUID? {
        return this.persistentAngerTarget
    }

    override fun setPersistentAngerTarget(pTarget: UUID?) {
        this.persistentAngerTarget = pTarget
    }

    override fun startPersistentAngerTimer() {
        this.remainingPersistentAngerTime = PERSISTENT_ANGER_TIME.sample(this.random)
    }

    // Define la lógica para que el Boshito sea tameable
    override fun mobInteract(player: Player, hand: InteractionHand): InteractionResult {
        val itemStack: ItemStack = player.getItemInHand(hand)
        val item: Item = itemStack.item
        val itemForTaming: Item = Items.APPLE

        if (isFood(itemStack)) {
            return super.mobInteract(player, hand)
        }

        if (item == itemForTaming && !isTame) {
            if (this.level().isClientSide) {
                return InteractionResult.CONSUME
            } else {
                if (!player.abilities.instabuild) {
                    itemStack.shrink(1)
                }

                if (!ForgeEventFactory.onAnimalTame(this, player)) {
                    if (!this.level().isClientSide) {
                        super.tame(player)
                        this.navigation.recomputePath()
                        this.target = null
                        this.level().broadcastEntityEvent(this, 7)
                        setSitting(false)
                    }
                }

                return InteractionResult.SUCCESS
            }
        }

        if (isTame && !this.level().isClientSide && hand == InteractionHand.MAIN_HAND) {
            setSitting(!isSitting())
            return InteractionResult.SUCCESS
        }

        if (itemStack.item == itemForTaming) {
            return InteractionResult.PASS
        }

        return super.mobInteract(player, hand)
    }

    override fun wantsToAttack(pTarget: LivingEntity, pOwner: LivingEntity): Boolean {
        return if (pTarget !is Ghast) {
            if (pTarget is BoshitoEntity) {
                !pTarget.isTame || pTarget.owner !== pOwner
            } else if (pTarget is Player && pOwner is Player && !pOwner.canHarmPlayer(pTarget)) {
                return false
            } else if (pTarget is AbstractHorse && pTarget.isTamed) {
                return false
            } else {
                pTarget !is TamableAnimal || !pTarget.isTame
            }
        } else {
            return false
        }
    }

    override fun isAlliedTo(pEntity: Entity): Boolean {
        return if (this.isTame && this.owner != null) {
            pEntity == this.owner
        } else {
            super.isAlliedTo(pEntity)
        }
    }

    override fun readAdditionalSaveData(tag: CompoundTag) {
        super.readAdditionalSaveData(tag)
        setSitting(tag.getBoolean("isSitting"))
    }

    override fun addAdditionalSaveData(tag: CompoundTag) {
        super.addAdditionalSaveData(tag)
        tag.putBoolean("isSitting", this.isSitting())
    }

    override fun defineSynchedData() {
        super.defineSynchedData()
        this.entityData.define(SITTING, false)
    }

    private fun setSitting(sitting: Boolean) {
        this.entityData.set(SITTING, sitting)
        this.isOrderedToSit = sitting
    }

    private fun isSitting(): Boolean {
        return this.entityData.get(SITTING)
    }

    override fun setTame(tamed: Boolean) {
        super.setTame(tamed)
        if (tamed) {
            getAttribute(Attributes.MAX_HEALTH)!!.baseValue = 60.0
            getAttribute(Attributes.ATTACK_DAMAGE)!!.baseValue = 4.0
            getAttribute(Attributes.MOVEMENT_SPEED)!!.baseValue = 0.5
        } else {
            getAttribute(Attributes.MAX_HEALTH)!!.baseValue = 30.0
            getAttribute(Attributes.ATTACK_DAMAGE)!!.baseValue = 2.0
            getAttribute(Attributes.MOVEMENT_SPEED)!!.baseValue = 0.25
        }
    }

    override fun registerControllers(p0: AnimatableManager.ControllerRegistrar?) {

    }

    override fun getAnimatableInstanceCache(): AnimatableInstanceCache {
        return cache
    }

}
